package org.pqt.mr2rib.matsub;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import org.pqt.mr2rib.GlobalOptions;
import org.pqt.mr2rib.GlobalRegistry;
import org.pqt.mr2rib.PrivateOptions;
import org.pqt.mr2rib.RendererOptions;
import org.pqt.mr2rib.matsub.RSLMaster;
import org.pqt.mr2rib.mrutil.ShaderDefinition;
import org.pqt.mr2rib.ribtranslator.PrettyPrint;
import org.pqt.mr2rib.ribtranslator.RIBException;
import org.pqt.mr2rib.ribtranslator.RenderContext;
import org.pqt.mr2rib.ribtranslator.Util;
import org.pqt.mr2rib.script.ReplacePart;

/**This holds a single renderman shader, and has methods to help construct it
 *
 * @author Peter Quint  */
public class ShaderConstructor {

    private HashSet variableNames = new HashSet();

    /**The RSLInstances representing the various named shaders used in the
     *renderman shader. Keys are names (the names used in the shader
     * definition, the values are vectors of RSLInstances - vectors because
     * in theory we may need to store more than one instance with the same
     * name because they need to be separate to work as embedded shaders */
    public Hashtable rslInstances = new Hashtable();

    /**The root RSLInstance - i.e. the one at the top of the dependency tree*/
    public RSLInstance rootRSLInstance = null;

    /**The shader type - in renderman shading language terms - surface, 
     * displacement, light etc from the constants defined in this class*/
    public int type;

    public static final int SURFACE = 1;

    public static final int DISPLACEMENT = 2;

    public static final int LIGHT = 3;

    public static final int VOLUME = 4;

    public static final int IMAGER = 5;

    public static final String typeNames[] = { "surface", "displacement", "light", "volume", "imager" };

    /**The RSLMasters representing the underlying shaders - i.e. rather than
     *the named shaders created by shader definitions, the master shaders
     *created in shader declarations*/
    public Hashtable rslMasters = new Hashtable();

    /**The fileName this shader is stored in*/
    public String fileName = null;

    /**The list of classes implementing ParameterUser interfaces, that deal
     * with special use parameters*/
    Vector useClasses = new Vector();

    /**This contains the name of the shadowmap parameter if this is a light
     *source shader that accepts shadow maps*/
    public String shadowMapParameter = null;

    /**This is true if the shadowMapParameter is for a point light - in other
     * words the parameter expects an array of map names, rather than a single
     * map name*/
    public boolean isPointLight = false;

    /**This contains a list of names of parameters that take options, and
     *the options they take (both as strings*/
    public Hashtable optionArguments = new Hashtable();

    /**This is a list of Strings representing arguments that take the
     * object Number special default*/
    public Vector objectNumberArgs = new Vector(2, 2);

    /**This contains a ShaderRec to capture all the basic structural and parameter
     * information. This is used as the basis of deciding whether a shader
     * has changed between one frame and the next, and thus whether to recompile it
     * or change its values using top level arguments*/
    public ShaderRec shaderRec = new ShaderRec();

    /**Holds a hashtable of shadernode names (as keys) and instances of
     *ReplacePart - holding all the replacepart instructions that apply to this
     *shader*/
    Hashtable replacePartTable = new Hashtable();

    /**This contains a list of vector parameter name strings that must be treated specially - 
     * that is they are vectors used to store non-vector quantities. Under 
     * renderman default behaviour they may be converted from "shader" to "current"
     * space if they are provided as shader arguments in the RIB file. We
     * need to convert them back.
     */
    private Vector vectorSpecials = new Vector();

    /**This contains a list of matrix parameter name strings that must be 
     * treated specially. That is they must be invarient between their forms
     * as parameters to shaders, and their forms inside the shader*/
    private Vector matrixSpecials = new Vector();

    /**Return a variable name that will be unique within this shader
     * @param name the basic name of the variable
     * @return a version of the name that is unique, achieved by prefixing
     * name with "v" and a number
     */
    public String getUniqueVariableName(String name) {
        String s;
        if (name.length() > RendererOptions.maxIdentifierLen) name = name.substring(0, RendererOptions.maxIdentifierLen);
        if (!variableNames.contains(name)) {
            variableNames.add(name);
            return name;
        } else {
            int count = 1;
            s = name + Integer.toString(count);
            while (variableNames.contains(s) && (s.length() < RendererOptions.maxIdentifierLen)) {
                count++;
                s = name + Integer.toString(count);
            }
            if (s.length() >= RendererOptions.maxIdentifierLen) {
                count = 1;
                while (variableNames.contains("v" + Integer.toString(count) + '_' + name)) count++;
                s = "v" + Integer.toString(count) + '_' + name;
            }
            variableNames.add(s);
            return (s);
        }
    }

    public String getUniqueVariableName(String name, String prefix) {
        if (name.length() > RendererOptions.maxIdentifierLen) name = name.substring(0, RendererOptions.maxIdentifierLen);
        if (!variableNames.contains(name)) {
            variableNames.add(name);
            return name;
        } else return getUniqueVariableName(prefix + '_' + name);
    }

    /**Return the RSLMaster for given mental ray shader, if necessary loading a
     * the rsl file (and adding a reference to it in rslMasters)
     * @param name the name of the file to load
     * @throws RIBException
     * @return an RSLMaster for the given mental ray shader
     */
    public RSLMaster loadRSLMaster(String name) throws RIBException {
        return loadRSLMaster(new File(GlobalOptions.rslDirectory, name + ".rsl"));
    }

    /**Return the RSLMaster for given mental ray shader, if necessary loading a
     * the rsl file (and adding a reference to it in rslMasters)
     * @param file a File containing a fully qualified path to the rsl file
     * @throws RIBException
     * @return an RSLMaster for the given mental ray shader
     */
    public RSLMaster loadRSLMaster(File file) throws RIBException {
        RSLMaster rslm = (RSLMaster) rslMasters.get(file.getName());
        if (rslm == null) {
            rslm = RSLMaster.load(file.getPath());
            rslMasters.put(file.getName(), rslm);
        }
        if (!rslm.implemented) throw new RIBException("shader " + rslm.name + " is not implemented");
        return rslm;
    }

    /**Return an RSL instance for the given named mental ray shader
     * @param name the name of the shader as used in the shader definition
     * @param parent the parent RSLInstance for this instance, null if it
     * not a local instance
     * @param paramReplacements a hashtable of parameter names and values. If 
     * a parameter is named in this table is value is taken from the table rather
     * than whatever value was given in the mental ray file. This can be null;
     * @throws RIBException
     * @return the value of the RSLInstance associated with this shader
     */
    public RSLInstance getMRShader(String name, RSLInstance parent, Hashtable paramReplacements) throws RIBException {
        Hashtable actualReplacements;
        Vector rsls = (Vector) rslInstances.get(name);
        RSLInstance rsl;
        if (rsls != null) {
            Iterator i = rsls.iterator();
            while (i.hasNext()) {
                rsl = (RSLInstance) i.next();
                if (rsl.parentInstance == parent) return rsl;
            }
        } else rsls = new Vector();
        ShaderDefinition sdefn = (ShaderDefinition) PrivateOptions.registry.shaderDefinitionRegistry.get(name);
        if (sdefn == null) throw new RIBException("cannot find shader definition for " + name);
        ReplacePart rp = null;
        if (replacePartTable != null) rp = (ReplacePart) replacePartTable.get(sdefn.getName());
        RSLMaster rslm;
        if (rp != null) {
            if (paramReplacements != null) {
                actualReplacements = new Hashtable(paramReplacements);
                actualReplacements.putAll(rp.params);
            } else actualReplacements = rp.params;
            rslm = loadRSLMaster(new File(rp.rslFileName));
            rsl = new RSLInstance(this, sdefn, rslm, parent, actualReplacements);
        } else {
            rslm = loadRSLMaster(sdefn.shader.name);
            rsl = new RSLInstance(this, sdefn, rslm, parent, paramReplacements);
        }
        rsls.add(rsl);
        rsls.trimToSize();
        rslInstances.put(name, rsls);
        return rsl;
    }

    /**Return an RSL instance for the given named mental ray shader
     * @param name the name of the shader as used in the shader definition
     * @param parent the parent RSLInstance for this instance, null if it
     * not a local instance
     * @throws RIBException
     * @return the value of the RSLInstance associated with this shader
     */
    public RSLInstance getMRShader(String name, RSLInstance parent) throws RIBException {
        return getMRShader(name, parent, null);
    }

    /**Create a set of RIB assignments that make an existing, compiled shader, behave
     *like this one. This uses the ShaderRec saved at the same time as the sourcecode
     *to check if this shader and the previous one are compatible.
     *@param file the file this shader would be saved in
     *@return an array of RIB assignments, or null if the shader cannot be made
     *compatible and must be recompiled.
     */
    public String[] createRIBAssignments(File file, Hashtable replaceParams) {
        if (GlobalOptions.useFullShaderPath) fileName = Util.convertPath(file.getPath()); else fileName = Util.convertPath(file.getName());
        FileInputStream fis = null;
        String name = Util.stripExtension(file.getPath()) + ".rec";
        try {
            fis = new FileInputStream(name);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object o = ois.readObject();
            ShaderRec sr = (ShaderRec) o;
            if (!sr.compatible(shaderRec)) return null;
            return sr.createRIBArguments(shaderRec, replaceParams);
        } catch (Exception e) {
            if (fis != null) try {
                fis.close();
            } catch (Exception f) {
            }
            return null;
        }
    }

    /** This writes the shader to the named file, from whence it can be compiled
     * @param file the file to write to
     * @param type the type of shader, e.g. "surface"
     * @param number a number for distinguishing multiple shaders in a material
     * that has multiple shaders, if <=0 number has no effect
     * @throws RIBException
     */
    public void writeShader(File file) throws RIBException {
        String name = Util.stripExtension(file.getName());
        name = Util.makeLegalShaderName(name, -1);
        file = new File(file.getParentFile(), name + ".sl");
        PrintStream out;
        try {
            out = new PrintStream(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            throw new RIBException("file " + file.getName() + " not found");
        }
        out.println("/* renderman shader " + file.getName() + " created by mental ray to renderman translator*/");
        writeDefines(out);
        if (type == VOLUME) out.println("#define VOLUME_SHADER");
        if (GlobalOptions.inlineCode) {
            out.println("#define INLINECODE");
            writeIncludes(out);
        }
        String header = typeNames[type - 1] + ' ' + name + "(";
        out.print(header);
        char[] spaces = new char[header.length()];
        java.util.Arrays.fill(spaces, ' ');
        String[] topLevelArgs = shaderRec.createTopLevelArguments(((type == LIGHT) || (type == VOLUME)));
        for (int i = 0; i < topLevelArgs.length; i++) {
            out.print(topLevelArgs[i]);
            if (i + 1 < topLevelArgs.length) {
                out.println();
                out.print(spaces);
            }
        }
        out.println(") {");
        if (!GlobalOptions.inlineCode) writeIncludes(out);
        if ((type == SURFACE) || (type == DISPLACEMENT)) {
            out.println("#pragma nolint");
            out.println(RSLInstance.tabin + "varying point PP = (hasPref > 0) ? transform(\"object\",Pref) : P;");
            if (!GlobalOptions.exportShaderMode) out.println(RSLInstance.tabin + "varying float ss = s, tt = t;"); else out.println(RSLInstance.tabin + "varying float ss = s, tt = (reverse_t > 0) ? (1 - t) : t; ");
            out.println(RSLInstance.tabin + "varying float ds = max(abs(Du(ss)*du) + abs(Dv(ss)*dv), 1.0e-6), dt = max(abs(Du(tt)*du) + abs(Dv(tt)*dv), 1.0e-6);");
            out.println("#pragma lint");
            out.println(RSLInstance.tabin + "varying float ds1 = 1e-6, dt1 = 1e-6, ds2 = 1e-6, dt2 = 1e-6, " + "ds3 = 1e-6, dt3 = 1e-6, ds4 = 1e-6, dt4 = 1e-6;");
        }
        if (!GlobalOptions.shaderInCurrentSpace || (type == LIGHT)) writeSpecials(out);
        StringBuffer vars = new StringBuffer();
        StringBuffer calls = new StringBuffer();
        StringBuffer preamble = new StringBuffer();
        rootRSLInstance.writeThis(vars, calls, preamble);
        out.println(preamble);
        out.println(RSLInstance.tabin + "/* The main shader routine */");
        out.print(vars);
        out.print(calls);
        if ((type == SURFACE) || (type == VOLUME)) {
            {
                out.println(RSLInstance.tabin + "Oi = alpha_out;");
                out.println(RSLInstance.tabin + "Ci = out;");
            }
        }
        out.println("}");
        out.close();
        try {
            FileOutputStream fos = new FileOutputStream(Util.stripExtension(file.getPath()) + ".rec");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(shaderRec);
            oos.close();
        } catch (Exception e) {
        }
        if (GlobalOptions.useFullShaderPath) fileName = Util.convertPath(file.getPath()); else fileName = Util.convertPath(file.getName());
        rootRSLInstance = null;
        rslMasters = null;
        rslInstances = null;
        variableNames = null;
    }

    public ShaderConstructor(ShaderDefinition sdefn, Hashtable replaceParts, int type) throws RIBException {
        this.type = type;
        for (int i = 0; i < PrivateOptions.predefinedVars.length; i++) variableNames.add(PrivateOptions.predefinedVars[i]);
        replacePartTable = replaceParts;
        ReplacePart rp;
        if (replaceParts != null) rp = (ReplacePart) replaceParts.get(PrivateOptions.ROOTSHADERNODE); else rp = null;
        RSLMaster rslm;
        RSLInstance rsl = null;
        if (rp != null) {
            rslm = loadRSLMaster(new File(rp.rslFileName));
            rsl = new RSLInstance(this, sdefn, rslm, null, rp.params);
        } else {
            rslm = loadRSLMaster(sdefn.shader.name);
            rsl = new RSLInstance(this, sdefn, rslm, null, null);
        }
        rootRSLInstance = rsl;
    }

    public void clear() {
        rslInstances = null;
        rslMasters = null;
        rootRSLInstance = null;
        variableNames = null;
    }

    /**Write out any special RIB produced by the special parameterUse
     * classes that are part of this shader. These classes are invoked
     * to handle, for example, light linking.
     * @param out the RIB stream we are writing to
     * @param rc the rendercontext of the item we are writing out
     */
    public void processUseClasses(PrettyPrint out, RenderContext rc) {
        Iterator i = useClasses.iterator();
        while (i.hasNext()) ((ParameterUser) i.next()).process(out, rc);
    }

    public void setFileName(File file) {
        String name = Util.stripExtension(file.getName());
        name = Util.makeLegalShaderName(name, -1);
        file = new File(file.getParentFile(), name + ".sl");
        if (GlobalOptions.useFullShaderPath) fileName = Util.convertPath(file.getPath()); else fileName = Util.convertPath(file.getName());
    }

    public void writeIncludes(PrintStream out) {
        RSLMaster masters[] = new RSLMaster[rslMasters.size()];
        rslMasters.values().toArray(masters);
        out.println(RSLMaster.createIncludes(masters));
    }

    /**Write the #define statements for the standard preprocessor 
     * macros NOOPTIMISE and PASSES, and for any macros
     * defined in Renderer Options*/
    public void writeDefines(PrintStream out) {
        if (GlobalOptions.noOptimise) out.println("#define NOOPTIMISE");
        if (GlobalOptions.passes.length() > 0) out.println("#define PASSES");
        if (RendererOptions.shaderMacroDefines != null) {
            StringTokenizer stok = new StringTokenizer(RendererOptions.shaderMacroDefines);
            while (stok.hasMoreTokens()) out.println("#define " + stok.nextToken());
        }
    }

    /**Add the name of a vector special to the list that ShaderConstructor maintains
     * vector specials are vectors used to store other types of data
     *@param name the name of the vector*/
    public void addVectorSpecial(String name) {
        vectorSpecials.add(name);
    }

    /**Add the name of a matrix special to the list that ShaderConstructor maintains
     * 
     *@param name the name of the matrix parameter*/
    public void addMatrixSpecial(String name) {
        matrixSpecials.add(name);
    }

    /**Write out the vector transformations needed to ensure that the contents
     * of vector specials (vectors used to store other types of data) are not
     * affected by Rendermans default space transformations. Ditto for
     * matrices.
     * @param out the stream to print to
     */
    public void writeSpecials(PrintStream out) {
        Iterator i = vectorSpecials.iterator();
        String name;
        while (i.hasNext()) {
            name = (String) i.next();
            out.print(RSLInstance.tabin);
            out.println("uniform vector _" + name + " = vtransform(\"shader\", " + name + ");");
        }
        if (matrixSpecials.size() > 0) {
            out.print(RSLInstance.tabin);
            out.println("uniform matrix _one = 1;");
            out.print(RSLInstance.tabin);
            out.println("uniform matrix _shader = matrix \"shader\" 1;");
            out.print(RSLInstance.tabin);
            out.println("uniform matrix _oneovershader = _one / _shader;");
            i = matrixSpecials.iterator();
            while (i.hasNext()) {
                name = (String) i.next();
                out.print(RSLInstance.tabin);
                out.print("uniform matrix _" + name + " = ");
                if (RendererOptions.delightMatrixMult) out.println(name + " * _oneovershader;"); else out.println("_oneovershader * " + name + ";");
            }
        }
    }
}

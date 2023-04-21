package com.jogamp.gluegen.opengl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
   * Builds the StaticGLInfo class from the OpenGL header files (i.e., gl.h
   * and glext.h) whose paths were passed as arguments to {@link
   * #main(String[])}.
   *
   * It relies upon the assumption that a function's membership is scoped by
   * preprocessor blocks in the header files that match the following pattern:
   * <br>
   *
   * <pre>
   * 
   * #ifndef GL_XXXX
   * GLAPI <returnType> <APIENTRY|GLAPIENTRY> glFuncName(<params>)
   * #endif GL_XXXX
   *
   * </pre>
   *
   * For example, if it parses the following data:
   *
   * <pre>
   * 
   * #ifndef GL_VERSION_1_3
   * GLAPI void APIENTRY glActiveTexture (GLenum);
   * GLAPI void APIENTRY glMultiTexCoord1dv (GLenum, const GLdouble *);
   * GLAPI void  <APIENTRY|GLAPIENTRY> glFuncName(<params>)
   * #endif GL_VERSION_1_3
   *
   * #ifndef GL_ARB_texture_compression
   * GLAPI void APIENTRY glCompressedTexImage3DARB (GLenum, GLint, GLenum, GLsizei, GLsizei, GLsizei, GLint, GLsizei, const GLvoid *);
   * GLAPI void APIENTRY glCompressedTexImage2DARB (GLenum, GLint, GLenum, GLsizei, GLsizei, GLint, GLsizei, const GLvoid *);
   * #endif
   * 
   * </pre>
   *
   * It will associate
   *   <code> glActiveTexture </code> and
   *   <code> glMultiTexCoord1dv </code>
   * with the symbol
   *   <code> GL_VERSION_1_3 </code>,
   * and associate
   *   <code> glCompressedTexImage2DARB </code> and
   *   <code> glCompressedTexImage3DARB </code>
   * with the symbol
   *   <code> GL_ARB_texture_compression </code>.
   * */
public class BuildStaticGLInfo {

    protected static int funcIdentifierGroup = 10;

    protected static Pattern funcPattern = Pattern.compile("^(GLAPI|GL_API|GL_APICALL|EGLAPI|extern)?(\\s*)((unsigned|const)\\s+)?(\\w+)(\\s*\\*)?(\\s+)(GLAPIENTRY|GL_APIENTRY|APIENTRY|EGLAPIENTRY|WINAPI)?(\\s*)([ew]?gl\\w+)\\s?(\\(.*)");

    protected static Pattern associationPattern = Pattern.compile("\\#ifndef ([CEW]?GL[XU]?_[A-Za-z0-9_]+)(.*)");

    protected static int defineIdentifierGroup = 1;

    protected static Pattern definePattern = Pattern.compile("\\#define ([CEW]?GL[XU]?_[A-Za-z0-9_]+)\\s*([A-Za-z0-9_]+)(.*)");

    protected Map<String, String> declarationToExtensionMap = new HashMap<String, String>();

    protected Map<String, Set<String>> extensionToDeclarationMap = new HashMap<String, Set<String>>();

    protected boolean debug = false;

    /**
     * The first argument is the package to which the StaticGLInfo class
     * belongs, the second is the path to the directory in which that package's
     * classes reside, and the remaining arguments are paths to the C header
     * files that should be parsed
     */
    public static void main(String[] args) throws IOException {
        if (args.length > 0 && args[0].equals("-test")) {
            BuildStaticGLInfo builder = new BuildStaticGLInfo();
            builder.setDebug(true);
            String[] newArgs = new String[args.length - 1];
            System.arraycopy(args, 1, newArgs, 0, args.length - 1);
            builder.parse(newArgs);
            builder.dump();
            System.exit(0);
        }
        String packageName = args[0];
        String packageDir = args[1];
        String[] cHeaderFilePaths = new String[args.length - 2];
        System.arraycopy(args, 2, cHeaderFilePaths, 0, cHeaderFilePaths.length);
        BuildStaticGLInfo builder = new BuildStaticGLInfo();
        try {
            builder.parse(cHeaderFilePaths);
            File file = new File(packageDir + File.separatorChar + "StaticGLInfo.java");
            String parentDir = file.getParent();
            if (parentDir != null) {
                File pDirFile = new File(parentDir);
                pDirFile.mkdirs();
            }
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            builder.emitJavaCode(writer, packageName);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            StringBuilder buf = new StringBuilder("{ ");
            for (int i = 0; i < cHeaderFilePaths.length; ++i) {
                buf.append(cHeaderFilePaths[i]);
                buf.append(" ");
            }
            buf.append('}');
            throw new RuntimeException("Error building StaticGLInfo.java from " + buf.toString(), e);
        }
    }

    public void setDebug(boolean v) {
        debug = v;
    }

    /** Parses the supplied C header files and adds the function
    associations contained therein to the internal map. */
    public void parse(String[] cHeaderFilePaths) throws IOException {
        for (int i = 0; i < cHeaderFilePaths.length; i++) {
            parse(cHeaderFilePaths[i]);
        }
    }

    /** Parses the supplied C header file and adds the function
    associations contained therein to the internal map. */
    public void parse(String cHeaderFilePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(cHeaderFilePath));
        String line, activeAssociation = null;
        Matcher m = null;
        while ((line = reader.readLine()) != null) {
            int type = 0;
            if (activeAssociation != null) {
                String identifier = null;
                if ((m = funcPattern.matcher(line)).matches()) {
                    identifier = m.group(funcIdentifierGroup).trim();
                    type = 2;
                } else if ((m = definePattern.matcher(line)).matches()) {
                    identifier = m.group(defineIdentifierGroup).trim();
                    type = 1;
                } else if (line.startsWith("#endif")) {
                    if (debug) {
                        System.err.println("END ASSOCIATION BLOCK: <" + activeAssociation + ">");
                    }
                    activeAssociation = null;
                }
                if ((identifier != null) && (activeAssociation != null) && !identifier.equals(activeAssociation)) {
                    addAssociation(identifier, activeAssociation);
                    if (debug) {
                        System.err.println("  ADDING ASSOCIATION: <" + identifier + "> <" + activeAssociation + "> ; type " + type);
                    }
                }
            } else if ((m = associationPattern.matcher(line)).matches()) {
                activeAssociation = m.group(1).trim();
                if (debug) {
                    System.err.println("BEGIN ASSOCIATION BLOCK: <" + activeAssociation + ">");
                }
            }
        }
        reader.close();
    }

    public void dump() {
        for (String name : extensionToDeclarationMap.keySet()) {
            Set<String> decls = extensionToDeclarationMap.get(name);
            System.out.println("<" + name + "> :");
            List<String> l = new ArrayList<String>();
            l.addAll(decls);
            Collections.sort(l);
            for (String str : l) {
                System.out.println("  <" + str + ">");
            }
        }
    }

    public String getExtension(String identifier) {
        return declarationToExtensionMap.get(identifier);
    }

    public Set<String> getDeclarations(String extension) {
        return extensionToDeclarationMap.get(extension);
    }

    public Set<String> getExtensions() {
        return extensionToDeclarationMap.keySet();
    }

    public void emitJavaCode(PrintWriter output, String packageName) {
        output.println("package " + packageName + ";");
        output.println();
        output.println("import java.util.*;");
        output.println();
        output.println("public final class StaticGLInfo");
        output.println("{");
        output.println("  // maps function names to the extension string or OpenGL");
        output.println("  // specification version string to which they correspond.");
        output.println("  private static HashMap funcToAssocMap;");
        output.println();
        output.println("  /**");
        output.println("   * Returns the OpenGL extension string or GL_VERSION string with which the");
        output.println("   * given function is associated. <P>");
        output.println("   *");
        output.println("   * If the");
        output.println("   * function is part of the OpenGL core, the returned value will be");
        output.println("   * GL_VERSION_XXX where XXX represents the OpenGL version of which the");
        output.println("   * function is a member (XXX will be of the form \"A\" or \"A_B\" or \"A_B_C\";");
        output.println("   * e.g., GL_VERSION_1_2_1 for OpenGL version 1.2.1).");
        output.println("   *");
        output.println("   * If the function is an extension function, the returned value will the");
        output.println("   * OpenGL extension string for the extension to which the function");
        output.println("   * corresponds. For example, if glLoadTransposeMatrixfARB is the argument,");
        output.println("   * GL_ARB_transpose_matrix will be the value returned.");
        output.println("   * Please see http://oss.sgi.com/projects/ogl-sample/registry/index.html for");
        output.println("   * a list of extension names and the functions they expose.");
        output.println("   *");
        output.println("   * If the function specified is not part of any known OpenGL core version or");
        output.println("   * extension, then NULL will be returned.");
        output.println("   */");
        output.println("  public static String getFunctionAssociation(String glFunctionName)");
        output.println("  {");
        output.println("    String mappedName = null;");
        output.println("    int  funcNamePermNum = com.jogamp.gluegen.runtime.opengl.GLExtensionNames.getFuncNamePermutationNumber(glFunctionName);");
        output.println("    for(int i = 0; null==mappedName && i < funcNamePermNum; i++) {");
        output.println("        String tmp = com.jogamp.gluegen.runtime.opengl.GLExtensionNames.getFuncNamePermutation(glFunctionName, i);");
        output.println("        try {");
        output.println("          mappedName = (String)funcToAssocMap.get(tmp);");
        output.println("        } catch (Exception e) { }");
        output.println("    }");
        output.println("    return mappedName;");
        output.println("  }");
        output.println();
        output.println("  static");
        output.println("  {");
        int maxCapacity = 0;
        for (String name : declarationToExtensionMap.keySet()) {
            if (!name.startsWith("GL")) {
                ++maxCapacity;
            }
        }
        output.println("    funcToAssocMap = new HashMap(" + maxCapacity + "); // approximate max capacity");
        output.println("    String group;");
        ArrayList<String> sets = new ArrayList<String>(extensionToDeclarationMap.keySet());
        Collections.sort(sets);
        for (String groupName : sets) {
            Set<String> funcs = extensionToDeclarationMap.get(groupName);
            List<String> l = new ArrayList<String>();
            l.addAll(funcs);
            Collections.sort(l);
            Iterator<String> funcIter = l.iterator();
            boolean printedHeader = false;
            while (funcIter.hasNext()) {
                String funcName = funcIter.next();
                if (!funcName.startsWith("GL")) {
                    if (!printedHeader) {
                        output.println();
                        output.println("    //----------------------------------------------------------------");
                        output.println("    //                 " + groupName);
                        output.println("    //----------------------------------------------------------------");
                        output.println("    group = \"" + groupName + "\";");
                        printedHeader = true;
                    }
                    output.println("    funcToAssocMap.put(\"" + funcName + "\", group);");
                }
            }
        }
        output.println("  }");
        output.println("} // end class StaticGLInfo");
    }

    protected void addAssociation(String identifier, String association) {
        declarationToExtensionMap.put(identifier, association);
        Set<String> identifiers = extensionToDeclarationMap.get(association);
        if (identifiers == null) {
            identifiers = new HashSet<String>();
            extensionToDeclarationMap.put(association, identifiers);
        }
        identifiers.add(identifier);
    }
}

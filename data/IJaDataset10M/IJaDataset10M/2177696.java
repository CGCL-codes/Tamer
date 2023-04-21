package org.argouml.language.java.generator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;
import org.argouml.application.ArgoVersion;
import org.argouml.application.api.Argo;
import org.argouml.application.api.Notation;
import org.argouml.application.api.PluggableNotation;
import org.argouml.model.uml.UmlHelper;
import org.argouml.uml.DocumentationManager;
import org.argouml.uml.generator.FileGenerator;
import org.argouml.uml.generator.Generator;
import ru.novosoft.uml.behavior.collaborations.MAssociationRole;
import ru.novosoft.uml.behavior.collaborations.MMessage;
import ru.novosoft.uml.behavior.common_behavior.MAction;
import ru.novosoft.uml.behavior.state_machines.MGuard;
import ru.novosoft.uml.behavior.state_machines.MState;
import ru.novosoft.uml.behavior.state_machines.MTransition;
import ru.novosoft.uml.behavior.use_cases.MExtensionPoint;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationEnd;
import ru.novosoft.uml.foundation.core.MAttribute;
import ru.novosoft.uml.foundation.core.MBehavioralFeature;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MConstraint;
import ru.novosoft.uml.foundation.core.MDataType;
import ru.novosoft.uml.foundation.core.MFeature;
import ru.novosoft.uml.foundation.core.MGeneralizableElement;
import ru.novosoft.uml.foundation.core.MGeneralization;
import ru.novosoft.uml.foundation.core.MInterface;
import ru.novosoft.uml.foundation.core.MMethod;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.core.MOperation;
import ru.novosoft.uml.foundation.core.MParameter;
import ru.novosoft.uml.foundation.core.MStructuralFeature;
import ru.novosoft.uml.foundation.data_types.MCallConcurrencyKind;
import ru.novosoft.uml.foundation.data_types.MChangeableKind;
import ru.novosoft.uml.foundation.data_types.MExpression;
import ru.novosoft.uml.foundation.data_types.MMultiplicity;
import ru.novosoft.uml.foundation.data_types.MMultiplicityRange;
import ru.novosoft.uml.foundation.data_types.MScopeKind;
import ru.novosoft.uml.foundation.data_types.MVisibilityKind;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;
import ru.novosoft.uml.foundation.extension_mechanisms.MTaggedValue;
import ru.novosoft.uml.model_management.MPackage;

public class GeneratorJava extends Generator implements PluggableNotation, FileGenerator {

    protected boolean _verboseDocs = false;

    protected boolean _lfBeforeCurly = false;

    private static final boolean VERBOSE_DOCS = false;

    private static GeneratorJava SINGLETON = new GeneratorJava();

    public static GeneratorJava getInstance() {
        return SINGLETON;
    }

    protected GeneratorJava() {
        super(Notation.makeNotation("Java", null, Argo.lookupIconResource("JavaNotation")));
    }

    public static String Generate(Object o) {
        return SINGLETON.generate(o);
    }

    /** Generates a file for the classifier. 
     * This method could have been static if it where not for the need to
     * call it through the Generatorinterface.
     * @return the full path name of the the generated file or
     * 	       null if no file can be generated.
     */
    public String GenerateFile(MClassifier cls, String path) {
        String name = cls.getName();
        if (name == null || name.length() == 0) return null;
        String filename = name + ".java";
        if (!path.endsWith(FILE_SEPARATOR)) path += FILE_SEPARATOR;
        String packagePath = cls.getNamespace().getName();
        MNamespace parent = cls.getNamespace().getNamespace();
        while (parent != null) {
            if (parent.getNamespace() != null) packagePath = parent.getName() + "." + packagePath;
            parent = parent.getNamespace();
        }
        int lastIndex = -1;
        do {
            File f = new File(path);
            if (!f.isDirectory()) {
                if (!f.mkdir()) {
                    Argo.log.error(" could not make directory " + path);
                    return null;
                }
            }
            if (lastIndex == packagePath.length()) break;
            int index = packagePath.indexOf(".", lastIndex + 1);
            if (index == -1) index = packagePath.length();
            path += packagePath.substring(lastIndex + 1, index) + FILE_SEPARATOR;
            lastIndex = index;
        } while (true);
        String pathname = path + filename;
        File f = new File(pathname);
        if (f.exists()) {
            try {
                update(cls, f);
            } catch (Exception exp) {
                Argo.log.error("FAILED: " + f.getPath());
            }
            return pathname;
        }
        Argo.log.info("Generating (new) " + f.getPath());
        String header = SINGLETON.generateHeader(cls, pathname, packagePath);
        String src = SINGLETON.generate(cls);
        BufferedWriter fos = null;
        try {
            fos = new BufferedWriter(new FileWriter(f));
            fos.write(header);
            fos.write(src);
        } catch (IOException exp) {
        } finally {
            try {
                if (fos != null) fos.close();
            } catch (IOException exp) {
                Argo.log.error("FAILED: " + f.getPath());
            }
        }
        return pathname;
    }

    public String generateHeader(MClassifier cls, String pathname, String packagePath) {
        StringBuffer sb = new StringBuffer(80);
        if (VERBOSE_DOCS) sb.append("// FILE: ").append(pathname.replace('\\', '/')).append("\n\n");
        if (packagePath.length() > 0) sb.append("package ").append(packagePath).append(";\n");
        sb.append("import java.util.*;\n");
        return sb.toString();
    }

    /**
     * <p>Generate code for an extension point.</p>
     *
     * <p>Provided to comply with the interface, but returns null
     *   since no code will be generated. This should prevent a source tab
     *   being shown.</p>
     *
     * @param ep  The extension point to generate for
     *
     * @return    The generated code string. Always empty in this
     *            implementation.
     */
    public String generateExtensionPoint(MExtensionPoint ep) {
        return null;
    }

    public String generateAssociationRole(MAssociationRole m) {
        return "";
    }

    public String generateOperation(MOperation op, boolean documented) {
        StringBuffer sb = new StringBuffer(80);
        String nameStr = null;
        boolean constructor = false;
        MStereotype stereo = op.getStereotype();
        if (stereo != null && stereo.getName().equals("create")) {
            nameStr = generateName(op.getOwner().getName());
            constructor = true;
        } else {
            nameStr = generateName(op.getName());
        }
        if (documented) sb.append('\n');
        String s = generateConstraintEnrichedDocComment(op, documented, INDENT);
        if (s != null && s.trim().length() > 0) sb.append(INDENT).append(s);
        sb.append(INDENT);
        sb.append(generateConcurrency(op));
        sb.append(generateAbstractness(op));
        sb.append(generateChangeability(op));
        sb.append(generateScope(op));
        sb.append(generateVisibility(op));
        MParameter rp = UmlHelper.getHelper().getCore().getReturnParameter(op);
        if (rp != null) {
            MClassifier returnType = rp.getType();
            if (returnType == null && !constructor) {
                sb.append("void ");
            } else if (returnType != null) {
                sb.append(generateClassifierRef(returnType)).append(' ');
            }
        }
        Vector params = new Vector(op.getParameters());
        params.remove(rp);
        sb.append(nameStr).append('(');
        if (params != null) {
            boolean first = true;
            for (int i = 0; i < params.size(); i++) {
                MParameter p = (MParameter) params.elementAt(i);
                if (!first) sb.append(", ");
                sb.append(generateParameter(p));
                first = false;
            }
        }
        sb.append(')');
        return sb.toString();
    }

    public String generateAttribute(MAttribute attr, boolean documented) {
        StringBuffer sb = new StringBuffer(80);
        String s = generateConstraintEnrichedDocComment(attr, documented, INDENT);
        if (s != null && s.trim().length() > 0) sb.append('\n').append(INDENT).append(s);
        sb.append(INDENT);
        sb.append(generateVisibility(attr));
        sb.append(generateScope(attr));
        sb.append(generateChangability(attr));
        MClassifier type = attr.getType();
        MMultiplicity multi = attr.getMultiplicity();
        if (type != null && multi != null) {
            if (multi.equals(MMultiplicity.M1_1)) {
                sb.append(generateClassifierRef(type)).append(' ');
            } else if (type instanceof MDataType) {
                sb.append(generateClassifierRef(type)).append("[] ");
            } else sb.append("java.util.Vector ");
        }
        sb.append(generateName(attr.getName()));
        MExpression init = attr.getInitialValue();
        if (init != null) {
            String initStr = generateExpression(init).trim();
            if (initStr.length() > 0) sb.append(" = ").append(initStr);
        }
        sb.append(";\n");
        return sb.toString();
    }

    public String generateParameter(MParameter param) {
        StringBuffer sb = new StringBuffer(20);
        sb.append(generateClassifierRef(param.getType())).append(' ');
        sb.append(generateName(param.getName()));
        return sb.toString();
    }

    public String generatePackage(MPackage p) {
        StringBuffer sb = new StringBuffer(80);
        String packName = generateName(p.getName());
        sb.append("package ").append(packName).append(" {\n");
        Collection ownedElements = p.getOwnedElements();
        if (ownedElements != null) {
            Iterator ownedEnum = ownedElements.iterator();
            while (ownedEnum.hasNext()) {
                MModelElement me = (MModelElement) ownedEnum.next();
                sb.append(generate(me));
                sb.append("\n\n");
            }
        } else {
            sb.append("(no elements)");
        }
        sb.append("\n}\n");
        return sb.toString();
    }

    /**
   * Generate the start sequence for a classifier. The start sequence is
   * everything from the preceding javadoc comment to the opening curly brace.
   * Start sequences are non-empty for classes and interfaces only.
   *
   * This method is intented for package internal usage only.
   *
   * @param cls the classifier for which to generate the start sequence
   *
   * @return the generated start sequence
   */
    StringBuffer generateClassifierStart(MClassifier cls) {
        String sClassifierKeyword;
        if (cls instanceof MClass) sClassifierKeyword = "class"; else if (cls instanceof MInterface) sClassifierKeyword = "interface"; else return null;
        StringBuffer sb = new StringBuffer(80);
        sb.append('\n').append(DocumentationManager.getComments(cls)).append(generateConstraintEnrichedDocComment(cls, true, ""));
        sb.append(generateVisibility(cls.getVisibility()));
        if (cls.isAbstract() && !(cls instanceof MInterface)) {
            sb.append("abstract ");
        }
        if (cls.isLeaf()) {
            sb.append("final ");
        }
        sb.append(sClassifierKeyword).append(" ").append(generateName(cls.getName()));
        String baseClass = generateGeneralization(cls.getGeneralizations());
        if (!baseClass.equals("")) {
            sb.append(" ").append("extends ").append(baseClass);
        }
        if (cls instanceof MClass) {
            String interfaces = generateSpecification((MClass) cls);
            if (!interfaces.equals("")) {
                sb.append(" ").append("implements ").append(interfaces);
            }
        }
        sb.append(_lfBeforeCurly ? "\n{" : " {");
        String tv = generateTaggedValues(cls);
        if (tv != null && tv.length() > 0) {
            sb.append("\n").append(INDENT).append(tv);
        }
        return sb;
    }

    protected StringBuffer generateClassifierEnd(MClassifier cls) {
        StringBuffer sb = new StringBuffer();
        if (cls instanceof MClass || cls instanceof MInterface) {
            if (_verboseDocs) {
                String classifierkeyword = null;
                if (cls instanceof MClass) {
                    classifierkeyword = "class";
                } else {
                    classifierkeyword = "interface";
                }
                sb.append("\n//end of " + classifierkeyword + " " + cls.getName() + "\n");
            }
            sb.append("}");
        }
        return sb;
    }

    /**
     * Append the classifier end sequence to the prefix text specified. The
     * classifier end sequence is the closing curly brace together with any
     * comments marking the end of the classifier.
     *
     * This method is intented for package internal usage.
     *
     * @param sbPrefix the prefix text to be amended. It is OK to call append on
     *                 this parameter.
     * @param cls      the classifier for which to generate the classifier end
     *                 sequence. Only classes and interfaces have a classifier
     *                 end sequence.
     * @param fPlain   if true, only the closing brace is generated. Otherwise,
     *                 this may also generate some comments.
     *
     * @return the complete classifier code, i.e., sbPrefix plus the classifier
     *         end sequence
     */
    StringBuffer appendClassifierEnd(StringBuffer sbPrefix, MClassifier cls, boolean fPlain) {
        sbPrefix.append(generateClassifierEnd(cls));
        return sbPrefix;
    }

    /**
     * Generates code for a classifier. In case of Java code is generated for classes and interfaces only at the moment.
     * @see org.argouml.application.api.NotationProvider#generateClassifier(MClassifier)
     */
    public String generateClassifier(MClassifier cls) {
        StringBuffer returnValue = new StringBuffer();
        StringBuffer start = generateClassifierStart(cls);
        if ((start != null) && (start.length() > 0)) {
            StringBuffer body = generateClassifierBody(cls);
            StringBuffer end = generateClassifierEnd(cls);
            returnValue.append(start);
            if ((body != null) && (body.length() > 0)) {
                returnValue.append("\n");
                returnValue.append(body);
                if (_lfBeforeCurly) {
                    returnValue.append("\n");
                }
            }
            returnValue.append((end != null) ? end.toString() : "");
        }
        return returnValue.toString();
    }

    /**
     * Generates the body of a class or interface.
     * @param cls
     * @return StringBuffer
     */
    protected StringBuffer generateClassifierBody(MClassifier cls) {
        StringBuffer sb = new StringBuffer();
        if (cls instanceof MClass || cls instanceof MInterface) {
            String tv = null;
            Collection strs = UmlHelper.getHelper().getCore().getAttributes(cls);
            if (!strs.isEmpty()) {
                sb.append('\n');
                if (_verboseDocs && cls instanceof MClass) {
                    sb.append(INDENT).append("// Attributes\n");
                }
                Iterator strEnum = strs.iterator();
                while (strEnum.hasNext()) {
                    MStructuralFeature sf = (MStructuralFeature) strEnum.next();
                    sb.append(generate(sf));
                    tv = generateTaggedValues(sf);
                    if (tv != null && tv.length() > 0) {
                        sb.append(INDENT).append(tv);
                    }
                }
            }
            Collection ends = cls.getAssociationEnds();
            if (!ends.isEmpty()) {
                sb.append('\n');
                if (_verboseDocs && cls instanceof MClass) {
                    sb.append(INDENT).append("// Associations\n");
                }
                Iterator endEnum = ends.iterator();
                while (endEnum.hasNext()) {
                    MAssociationEnd ae = (MAssociationEnd) endEnum.next();
                    MAssociation a = ae.getAssociation();
                    sb.append(generateAssociationFrom(a, ae));
                    tv = generateTaggedValues(a);
                    if (tv != null && tv.length() > 0) {
                        sb.append(INDENT).append(tv);
                    }
                }
            }
            Collection behs = UmlHelper.getHelper().getCore().getOperations(cls);
            if (!behs.isEmpty()) {
                sb.append('\n');
                if (_verboseDocs) {
                    sb.append(INDENT).append("// Operations\n");
                }
                Iterator behEnum = behs.iterator();
                while (behEnum.hasNext()) {
                    MBehavioralFeature bf = (MBehavioralFeature) behEnum.next();
                    sb.append(generate(bf));
                    tv = generateTaggedValues((MModelElement) bf);
                    if ((cls instanceof MClass) && (bf instanceof MOperation) && (!((MOperation) bf).isAbstract())) {
                        if (_lfBeforeCurly) sb.append('\n').append(INDENT); else sb.append(' ');
                        sb.append('{');
                        if (tv.length() > 0) {
                            sb.append('\n').append(INDENT).append(tv);
                        }
                        sb.append('\n').append(generateMethodBody((MOperation) bf)).append(INDENT).append("}\n");
                    } else {
                        sb.append(";\n");
                        if (tv.length() > 0) {
                            sb.append(INDENT).append(tv).append('\n');
                        }
                    }
                }
            }
        }
        return sb;
    }

    /**
   * Generate the body of a method associated with the given operation. This
   * assumes there's at most one method associated!
   *
   * If no method is associated with the operation, a default method body will
   * be generated.
   */
    public String generateMethodBody(MOperation op) {
        if (op != null) {
            Collection methods = op.getMethods();
            Iterator i = methods.iterator();
            MMethod m = null;
            while (i != null && i.hasNext()) {
                m = (MMethod) i.next();
                if (m != null) {
                    if (m.getBody() != null) return m.getBody().getBody(); else return "";
                }
            }
            MParameter rp = UmlHelper.getHelper().getCore().getReturnParameter(op);
            if (rp != null) {
                MClassifier returnType = rp.getType();
                return generateDefaultReturnStatement(returnType);
            }
        }
        return generateDefaultReturnStatement(null);
    }

    public String generateDefaultReturnStatement(MClassifier cls) {
        if (cls == null) return "";
        String clsName = cls.getName();
        if (clsName.equals("void")) return "";
        if (clsName.equals("char")) return INDENT + "return 'x';\n";
        if (clsName.equals("int")) return INDENT + "return 0;\n";
        if (clsName.equals("boolean")) return INDENT + "return false;\n";
        if (clsName.equals("byte")) return INDENT + "return 0;\n";
        if (clsName.equals("long")) return INDENT + "return 0;\n";
        if (clsName.equals("float")) return INDENT + "return 0.0;\n";
        if (clsName.equals("double")) return INDENT + "return 0.0;\n";
        return INDENT + "return null;\n";
    }

    public String generateTaggedValues(MModelElement e) {
        Collection tvs = e.getTaggedValues();
        if (tvs == null || tvs.size() == 0) return "";
        boolean first = true;
        StringBuffer buf = new StringBuffer();
        Iterator iter = tvs.iterator();
        String s = null;
        while (iter.hasNext()) {
            s = generate((MTaggedValue) iter.next());
            if (s != null && s.length() > 0) {
                if (first) {
                    buf.append("/* {");
                    first = false;
                } else {
                    buf.append(", ");
                }
                buf.append(s);
            }
        }
        if (!first) buf.append("}*/\n");
        return buf.toString();
    }

    public String generateTaggedValue(MTaggedValue tv) {
        if (tv == null) return "";
        String s = generateUninterpreted(tv.getValue());
        if (s == null || s.length() == 0 || s.equals("/** */")) return "";
        String t = tv.getTag();
        if (t.equals("documentation")) return "";
        return generateName(t) + "=" + s;
    }

    /**
   * Enhance/Create the doccomment for the given model element, including tags
   * for any OCL constraints connected to the model element. The tags generated
   * are suitable for use with the ocl injector which is part of the Dresden OCL
   * Toolkit and are in detail:
   *
   * &nbsp;@invariant for each invariant specified
   * &nbsp;@precondition for each precondition specified
   * &nbsp;@postcondition for each postcondition specified
   * &nbsp;@key-type specifying the class of the keys of a mapped association
   * &nbsp; Currently mapped associations are not supported yet...
   * &nbsp;@element-type specifying the class referenced in an association
   *
   * @since 2001-09-26 ArgoUML 0.9.3
   * @author Steffen Zschaler
   *
   * @param me the model element for which the documentation comment is needed
   * @param ae the association end which is represented by the model element
   * @return the documentation comment for the specified model element, either
   * enhanced or completely generated
   */
    public String generateConstraintEnrichedDocComment(MModelElement me, MAssociationEnd ae) {
        String s = generateConstraintEnrichedDocComment(me, true, INDENT);
        MMultiplicity m = ae.getMultiplicity();
        if (!(MMultiplicity.M1_1.equals(m) || MMultiplicity.M0_1.equals(m))) {
            StringBuffer sDocComment = new StringBuffer(80);
            if (s != null) {
                sDocComment.append(s.substring(0, s.indexOf("*/") + 1));
            } else {
                sDocComment.append(INDENT).append("/**\n").append(INDENT).append(" * \n").append(INDENT).append(" *");
            }
            MClassifier type = ae.getType();
            if (type != null) {
                sDocComment.append(" @element-type ").append(type.getName());
            } else {
            }
            sDocComment.append('\n').append(INDENT).append(" */\n");
            return sDocComment.toString();
        } else {
            return (s != null) ? s : "";
        }
    }

    /**
   * Enhance/Create the doccomment for the given model element, including tags
   * for any OCL constraints connected to the model element. The tags generated
   * are suitable for use with the ocl injector which is part of the Dresden OCL
   * Toolkit and are in detail:
   *
   * &nbsp;@invariant for each invariant specified
   * &nbsp;@precondition for each precondition specified
   * &nbsp;@postcondition for each postcondition specified
   *
   * @since 2001-09-26 ArgoUML 0.9.3
   * @author Steffen Zschaler
   *
   * @param me the model element for which the documentation comment is needed
   * @param documented if existing tagged values should be generated in addition to javadoc
   * @param indent indent String (usually blanks) for indentation of generated comments
   * @return the documentation comment for the specified model element, either
   * enhanced or completely generated
   */
    public static String generateConstraintEnrichedDocComment(MModelElement me, boolean documented, String indent) {
        String s = (VERBOSE_DOCS || DocumentationManager.hasDocs(me)) ? DocumentationManager.getDocs(me, indent) : null;
        StringBuffer sDocComment = new StringBuffer(80);
        if (s != null && s.trim().length() > 0) {
            sDocComment.append(s).append('\n');
        }
        if (!documented) return sDocComment.toString();
        Collection cConstraints = me.getConstraints();
        if (cConstraints.size() == 0) {
            return sDocComment.toString();
        }
        if (s != null) {
            s = sDocComment.toString();
            sDocComment = new StringBuffer(s.substring(0, s.indexOf("*/") + 1));
        } else {
            sDocComment.append(INDENT).append("/**\n").append(INDENT).append(" * \n").append(INDENT).append(" *");
        }
        class TagExtractor extends tudresden.ocl.parser.analysis.DepthFirstAdapter {

            private LinkedList m_llsTags = new LinkedList();

            private String m_sConstraintName;

            private int m_nConstraintID = 0;

            public TagExtractor(String sConstraintName) {
                super();
                m_sConstraintName = sConstraintName;
            }

            public Iterator getTags() {
                return m_llsTags.iterator();
            }

            public void caseAConstraintBody(tudresden.ocl.parser.node.AConstraintBody node) {
                String sKind = (node.getStereotype() != null) ? (node.getStereotype().toString()) : (null);
                String sExpression = (node.getExpression() != null) ? (node.getExpression().toString()) : (null);
                String sName = (node.getName() != null) ? (node.getName().getText().toString()) : (m_sConstraintName + "_" + (m_nConstraintID++));
                if ((sKind == null) || (sExpression == null)) {
                    return;
                }
                String sTag;
                if (sKind.equals("inv ")) {
                    sTag = "@invariant ";
                } else if (sKind.equals("post ")) {
                    sTag = "@postcondition ";
                } else if (sKind.equals("pre ")) {
                    sTag = "@precondition ";
                } else {
                    return;
                }
                sTag += sName + ": " + sExpression;
                m_llsTags.addLast(sTag);
            }
        }
        tudresden.ocl.check.types.ModelFacade mf = new org.argouml.ocl.ArgoFacade(me);
        for (Iterator i = cConstraints.iterator(); i.hasNext(); ) {
            MConstraint mc = (MConstraint) i.next();
            try {
                tudresden.ocl.OclTree otParsed = tudresden.ocl.OclTree.createTree(mc.getBody().getBody(), mf);
                TagExtractor te = new TagExtractor(mc.getName());
                otParsed.apply(te);
                for (Iterator j = te.getTags(); j.hasNext(); ) {
                    sDocComment.append(' ').append(j.next()).append('\n').append(INDENT).append(" *");
                }
            } catch (java.io.IOException ioe) {
            }
        }
        sDocComment.append("/\n");
        return sDocComment.toString();
    }

    public String generateConstraints(MModelElement me) {
        Collection cs = me.getConstraints();
        if (cs == null || cs.size() == 0) return "";
        StringBuffer sb = new StringBuffer(80);
        if (VERBOSE_DOCS) sb.append(INDENT).append("// constraints\n");
        int size = cs.size();
        for (Iterator i = cs.iterator(); i.hasNext(); ) {
            MConstraint c = (MConstraint) i.next();
            String constrStr = generateConstraint(c);
            java.util.StringTokenizer st = new java.util.StringTokenizer(constrStr, "\n\r");
            while (st.hasMoreElements()) {
                String constrLine = st.nextToken();
                sb.append(INDENT).append("// ").append(constrLine).append('\n');
            }
        }
        sb.append('\n');
        return sb.toString();
    }

    public String generateConstraint(MConstraint c) {
        if (c == null) return "";
        StringBuffer sb = new StringBuffer(20);
        if (c.getName() != null && c.getName().length() != 0) sb.append(generateName(c.getName())).append(": ");
        sb.append(generateExpression(c));
        return sb.toString();
    }

    public String generateAssociationFrom(MAssociation a, MAssociationEnd ae) {
        StringBuffer sb = new StringBuffer(80);
        Collection connections = a.getConnections();
        Iterator connEnum = connections.iterator();
        while (connEnum.hasNext()) {
            MAssociationEnd ae2 = (MAssociationEnd) connEnum.next();
            if (ae2 != ae) {
                sb.append(INDENT).append(generateConstraintEnrichedDocComment(a, ae2));
                sb.append(generateAssociationEnd(ae2));
            }
        }
        return sb.toString();
    }

    public String generateAssociation(MAssociation a) {
        return "";
    }

    public String generateAssociationEnd(MAssociationEnd ae) {
        if (!ae.isNavigable()) return "";
        if (ae.getAssociation().isAbstract()) return "";
        StringBuffer sb = new StringBuffer(80);
        sb.append(INDENT).append(generateVisibility(ae.getVisibility()));
        if (MScopeKind.CLASSIFIER.equals(ae.getTargetScope())) sb.append("static ");
        MMultiplicity m = ae.getMultiplicity();
        if (MMultiplicity.M1_1.equals(m) || MMultiplicity.M0_1.equals(m)) sb.append(generateClassifierRef(ae.getType())); else sb.append("Vector ");
        sb.append(' ');
        String n = ae.getName();
        MAssociation asc = ae.getAssociation();
        String ascName = asc.getName();
        if (n != null && n != null && n.length() > 0) {
            sb.append(generateName(n));
        } else if (ascName != null && ascName != null && ascName.length() > 0) {
            sb.append(generateName(ascName));
        } else {
            sb.append("my").append(generateClassifierRef(ae.getType()));
        }
        return (sb.append(";\n")).toString();
    }

    public String generateGeneralization(Collection generalizations) {
        if (generalizations == null) return "";
        Collection classes = new ArrayList();
        Iterator enu = generalizations.iterator();
        while (enu.hasNext()) {
            MGeneralization g = (MGeneralization) enu.next();
            MGeneralizableElement ge = g.getParent();
            if (ge != null) classes.add(ge);
        }
        return generateClassList(classes);
    }

    public String generateSpecification(MClass cls) {
        Collection realizations = UmlHelper.getHelper().getCore().getSpecifications(cls);
        if (realizations == null) return "";
        StringBuffer sb = new StringBuffer(80);
        Iterator clsEnum = realizations.iterator();
        while (clsEnum.hasNext()) {
            MInterface i = (MInterface) clsEnum.next();
            sb.append(generateClassifierRef(i));
            if (clsEnum.hasNext()) sb.append(", ");
        }
        return sb.toString();
    }

    public String generateClassList(Collection classifiers) {
        if (classifiers == null) return "";
        StringBuffer sb = new StringBuffer(80);
        Iterator clsEnum = classifiers.iterator();
        while (clsEnum.hasNext()) {
            sb.append(generateClassifierRef((MClassifier) clsEnum.next()));
            if (clsEnum.hasNext()) sb.append(", ");
        }
        return sb.toString();
    }

    public String generateVisibility(MVisibilityKind vis) {
        if (MVisibilityKind.PUBLIC.equals(vis)) return "public ";
        if (MVisibilityKind.PRIVATE.equals(vis)) return "private ";
        if (MVisibilityKind.PROTECTED.equals(vis)) return "protected ";
        return "";
    }

    public String generateVisibility(MFeature f) {
        MVisibilityKind vis = f.getVisibility();
        if (MVisibilityKind.PUBLIC.equals(vis)) return "public ";
        if (MVisibilityKind.PRIVATE.equals(vis)) return "private ";
        if (MVisibilityKind.PROTECTED.equals(vis)) return "protected ";
        return "";
    }

    public String generateScope(MFeature f) {
        MScopeKind scope = f.getOwnerScope();
        if (MScopeKind.CLASSIFIER.equals(scope)) return "static ";
        return "";
    }

    /**
   * Generate "abstract" keyword for an abstract operation.
   */
    public String generateAbstractness(MOperation op) {
        if (op.isAbstract()) {
            return "abstract ";
        } else {
            return "";
        }
    }

    /**
   * Generate "final" keyword for final operations.
   */
    public String generateChangeability(MOperation op) {
        if (op.isLeaf()) {
            return "final ";
        } else {
            return "";
        }
    }

    public String generateChangability(MStructuralFeature sf) {
        MChangeableKind ck = sf.getChangeability();
        if (MChangeableKind.FROZEN.equals(ck)) return "final ";
        return "";
    }

    /**
     * Generates "synchronized" keyword for guarded operations.
     * @param op The operation
     * @return String The synchronized keyword if the operation is guarded, else ""
     */
    public String generateConcurrency(MOperation op) {
        if (op.getConcurrency() != null && op.getConcurrency().getValue() == MCallConcurrencyKind._GUARDED) {
            return "synchronized ";
        }
        return "";
    }

    public String generateMultiplicity(MMultiplicity m) {
        if (m == null) {
            return "";
        }
        if (MMultiplicity.M0_N.equals(m)) return ANY_RANGE;
        Collection v = m.getRanges();
        if (v == null) return "";
        StringBuffer sb = new StringBuffer(20);
        Iterator rangeEnum = v.iterator();
        while (rangeEnum.hasNext()) {
            MMultiplicityRange mr = (MMultiplicityRange) rangeEnum.next();
            sb.append(generateMultiplicityRange(mr));
            if (rangeEnum.hasNext()) sb.append(',');
        }
        return sb.toString();
    }

    public static final String ANY_RANGE = "0..*";

    public String generateMultiplicityRange(MMultiplicityRange mr) {
        Integer lower = new Integer(mr.getLower());
        Integer upper = new Integer(mr.getUpper());
        if (lower == null && upper == null) return ANY_RANGE;
        if (lower == null) return "*.." + upper.toString();
        if (upper == null) return lower.toString() + "..*";
        if (lower.intValue() == upper.intValue()) return lower.toString();
        return lower.toString() + ".." + upper.toString();
    }

    public String generateState(MState m) {
        return m.getName();
    }

    public String generateStateBody(MState m) {
        Argo.log.info("GeneratorJava: generating state body");
        StringBuffer sb = new StringBuffer(80);
        MAction entry = m.getEntry();
        MAction exit = m.getExit();
        if (entry != null) {
            String entryStr = Generate(entry);
            if (entryStr.length() > 0) sb.append("entry / ").append(entryStr);
        }
        if (exit != null) {
            String exitStr = Generate(exit);
            if (sb.length() > 0) sb.append('\n');
            if (exitStr.length() > 0) sb.append("exit / ").append(exitStr);
        }
        Collection trans = m.getInternalTransitions();
        if (trans != null) {
            Iterator iter = trans.iterator();
            while (iter.hasNext()) {
                if (sb.length() > 0) sb.append('\n');
                sb.append(generateTransition((MTransition) iter.next()));
            }
        }
        return sb.toString();
    }

    public String generateTransition(MTransition m) {
        StringBuffer sb = new StringBuffer(generate(m.getName()));
        String t = generate(m.getTrigger());
        String g = generate(m.getGuard());
        String e = generate(m.getEffect());
        if (sb.length() > 0) sb.append(": ");
        sb.append(t);
        if (g.length() > 0) sb.append(" [").append(g).append(']');
        if (e.length() > 0) sb.append(" / ").append(e);
        return sb.toString();
    }

    public String generateAction(MAction m) {
        if ((m.getScript() != null) && (m.getScript().getBody() != null)) return m.getScript().getBody();
        return "";
    }

    public String generateGuard(MGuard m) {
        if (m.getExpression() != null) return generateExpression(m.getExpression());
        return "";
    }

    public String generateMessage(MMessage m) {
        if (m == null) return "";
        return generateName(m.getName()) + "::" + generateAction(m.getAction());
    }

    /**
       Update a source code file.

       @param mClassifier The classifier to update from.
       @param file The file to update.
    */
    protected static void update(MClassifier mClassifier, File file) throws Exception {
        Argo.log.info("Parsing " + file.getPath());
        BufferedReader in = new BufferedReader(new FileReader(file));
        JavaLexer lexer = new JavaLexer(in);
        JavaRecognizer parser = new JavaRecognizer(lexer);
        CodePieceCollector cpc = new CodePieceCollector();
        parser.compilationUnit(cpc);
        in.close();
        File origFile = new File(file.getAbsolutePath());
        File newFile = new File(file.getAbsolutePath() + ".updated");
        File backupFile = new File(file.getAbsolutePath() + ".backup");
        if (backupFile.exists()) backupFile.delete();
        cpc.filter(file, newFile, mClassifier.getNamespace());
        file.renameTo(backupFile);
        Argo.log.info("Updating " + file.getPath());
        newFile.renameTo(origFile);
    }

    public boolean canParse() {
        return true;
    }

    public boolean canParse(Object o) {
        return true;
    }

    public String getModuleName() {
        return "GeneratorJava";
    }

    public String getModuleDescription() {
        return "Java Notation and Code Generator";
    }

    public String getModuleAuthor() {
        return "ArgoUML Core";
    }

    public String getModuleVersion() {
        return ArgoVersion.VERSION;
    }

    public String getModuleKey() {
        return "module.language.java.generator";
    }

    /**
     * Returns the _lfBeforeCurly.
     * @return boolean
     */
    public boolean isLfBeforeCurly() {
        return _lfBeforeCurly;
    }

    /**
     * Returns the _verboseDocs.
     * @return boolean
     */
    public boolean isVerboseDocs() {
        return _verboseDocs;
    }

    /**
     * Sets the _lfBeforeCurly.
     * @param _lfBeforeCurly The _lfBeforeCurly to set
     */
    public void setLfBeforeCurly(boolean _lfBeforeCurly) {
        this._lfBeforeCurly = _lfBeforeCurly;
    }

    /**
     * Sets the _verboseDocs.
     * @param _verboseDocs The _verboseDocs to set
     */
    public void setVerboseDocs(boolean _verboseDocs) {
        this._verboseDocs = _verboseDocs;
    }
}

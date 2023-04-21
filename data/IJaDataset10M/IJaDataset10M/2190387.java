package org.jdna.sage.api.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.jdna.sage.api.generator.MethodParser.MethodParam;
import org.jdna.sage.api.generator.MethodParser.SageMethod;

/**
 * Generates a JSP tag file wrapper API for SageTV APIs. A tag file is created
 * for each function.
 */
public class SageTagFileAPIGenerator {

    private static final Logger log = Logger.getLogger(SageTagFileAPIGenerator.class);

    private File srcDir;

    private String packageName;

    private String className;

    private File outFile;

    private File outDir;

    private PrintWriter pw = null;

    private List<SageMethod> methods;

    public SageTagFileAPIGenerator(File srcDir, String packageName, String className, List<SageMethod> methods) {
        this.srcDir = srcDir;
        this.packageName = packageName;
        this.className = className;
        this.methods = methods;
        outDir = new File(srcDir, packageName.replaceAll("\\.", "/") + "/");
    }

    public void generate() throws Exception {
        String apiDirName = className.replaceFirst("API$", "").toLowerCase();
        File apiDir = new File(outDir, apiDirName);
        apiDir.mkdirs();
        for (SageMethod m : methods) {
            String filename = m.name;
            for (MethodParam arg : m.args) {
                if (arg.varName.equals("MediaMask")) {
                    filename += "ForMediaMask";
                } else if (arg.varName.equals("True")) {
                    arg.varName += "Value";
                } else if (arg.varName.equals("False")) {
                    arg.varName += "Value";
                }
            }
            outFile = new File(apiDir, filename + ".tag");
            log.info("Creating Tag File: " + outFile.getAbsolutePath());
            pw = new PrintWriter(new FileWriter(outFile));
            String signature = createSignature(m);
            pw.println("<%--");
            pw.println("  -- Unofficial SageTV Generated File - Never Edit");
            pw.println("  -- Generated Date/Time: " + new SimpleDateFormat().format(new Date()));
            pw.printf("  -- See Official Sage Documentation at <a href='http://download.sage.tv/api/sage/api/%s.html#%s'>%s.%s</a>\n", className, signature, className, m.name);
            pw.println("  -- This Generated API is not Affiliated with SageTV.  It is user contributed.");
            pw.println("  --%>");
            pw.println("<%@ tag body-content=\"empty\"%>");
            pw.println("<%@ taglib prefix=\"c\" uri=\"http://java.sun.com/jsp/jstl/core\" %>");
            pw.println("");
            if (!m.returnType.contains("void")) {
                pw.println("<%@ attribute name=\"var\" required=\"true\" rtexprvalue=\"false\" %>");
                pw.println("<%@ variable name-from-attribute=\"var\" alias=\"varLocal\" scope=\"AT_END\" %>");
            }
            pw.println("<%@ attribute name=\"context\" required=\"false\" type=\"java.lang.String\" %>");
            for (int i = 0; i < m.args.size(); i++) {
                MethodParam arg = m.args.get(i);
                pw.printf("<%%@ attribute name=\"%s\" required=\"true\" type=\"%s\" %%>\n", toCamelCase(arg.varName), makeObject(fixSageApi(arg.dataType)));
            }
            if ((!m.returnType.contains("void")) || (m.args.size() > 0)) {
                pw.println();
            }
            pw.println("<%");
            pw.println("String contextAttr = (String) jspContext.getAttribute(\"context\");");
            for (int i = 0; i < m.args.size(); i++) {
                MethodParam arg = m.args.get(i);
                pw.printf("Object %sAttr = jspContext.getAttribute(\"%s\");\n", toCamelCase(arg.varName), toCamelCase(arg.varName));
            }
            if (m.args.size() > 0) {
                pw.println();
            }
            callSageService(pw, m);
            pw.println("%>");
            if (!m.returnType.contains("void")) {
                pw.println("");
                pw.println("<c:set var=\"varLocal\" value=\"<%= returnVal %>\"/>");
            }
            pw.close();
            pw.flush();
        }
    }

    /**
	 * SageTV's API does not expose the native object types, everything is an
	 * object. Convert the type names in the Javadoc to 'Object' types.
	 * 
	 * @param name
	 * @return
	 */
    private String fixSageApi(String name) {
        return name.replaceAll("sage\\.[^$\\[\\]]+", "java.lang.Object");
    }

    /**
	 * Prints the code to the file that calls the underlying SageTV API.
	 * 
	 * @param pw
	 * @param m
	 */
    private void callSageService(PrintWriter pw, SageMethod m) {
        String returnStmt = "";
        if (!m.returnType.contains("void")) {
            pw.println("Object returnVal = null;");
            pw.println("");
            returnStmt = "returnVal = ";
        }
        pw.println("if ((context != null) && (context.trim().length() > 0))");
        pw.println("{");
        pw.printf("    %ssage.SageTV.apiUI(%s, \"%s\", %s);\n", returnStmt, "context", m.name, buildObjectArray(m));
        pw.println("}");
        pw.println("else");
        pw.println("{");
        pw.printf("    %ssage.SageTV.api(\"%s\", %s);\n", returnStmt, m.name, buildObjectArray(m));
        pw.println("}");
    }

    /**
	 * Creates the code that represents the object array that is passed to the
	 * underlying SageTV API.
	 * 
	 * @param m
	 * @return
	 */
    private String buildObjectArray(SageMethod m) {
        if (m.args.size() == 0) {
            return "(Object[])null";
        }
        StringBuffer sb = new StringBuffer("new Object[] {");
        for (int i = 0; i < m.args.size(); i++) {
            MethodParam mp = m.args.get(i);
            sb.append(toCamelCase(mp.varName));
            sb.append("Attr");
            if (i < m.args.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    /**
	 * Converts a String representing a Java primitive data type to a wrapper
	 * object.
	 * 
	 * @param o
	 * @return
	 */
    private String makeObject(String o) {
        if (o.contains("[]")) {
            return o;
        } else {
            o = o.replace("boolean", "java.lang.Boolean");
            o = o.replace("long", "java.lang.Long");
            o = o.replace("int", "java.lang.Integer");
            o = o.replace("float", "java.lang.Float");
            o = o.replace("double", "java.lang.Double");
        }
        return o;
    }

    /**
	 * Create the method signature that is printed in the file's comment. It is
	 * used for linking to SageTV's online Javadoc API.
	 * 
	 * @param m
	 * @return
	 */
    private String createSignature(SageMethod m) {
        String signature = m.name + "(";
        for (int i = 0; i < m.args.size(); i++) {
            MethodParam arg = m.args.get(i);
            signature += arg.dataType;
            if (i < m.args.size() - 1) {
                signature += ", ";
            }
        }
        signature += ")";
        return signature;
    }

    private String toCamelCase(String s) {
        return s.substring(0, 1).toLowerCase() + s.substring(1);
    }
}

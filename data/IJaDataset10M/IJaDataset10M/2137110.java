package org.sun.dbe.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * 
 * @author bob
 * 
 * 
 */
public class Wsdl2Java {

    /** Java File header */
    private static final String HEADER = "/*\n * Wsdl2Java file\n * Please, modify anything you want\n */\n";

    /** Import Sentence */
    private static final String IMPORT = "";

    private static final String CLASS_DEFINITION_PRE = "public class ";

    private static final String CLASS_DEFINITION_POST = " implements org.sun.dbe.server.Adapter {\n";

    /** Constructor method PRE */
    private static final String CONSTURCT_METHOD_PRE = "\t/**\n" + "\t * @see org.apache.axis.client.Stub\n" + "\t */\n" + "\tpublic ";

    /** Constructor method POST */
    private static final String CONSTURCT_METHOD_POST = "(java.net.URL url) throws org.apache.axis.AxisFault {\n" + "\t\tsuper(url, null);\n" + "\t}\n\n";

    /** Init method declaration */
    private static final String INIT_METHOD = "\t/**\n" + "\t * @see org.sun.dbe.server.Adapter#init(java.util.Properties)\n" + "\t */\n" + "\tpublic void init(java.util.Properties props) {\n" + "\t\t// TODO: Auto-generated method\n\t}\n\n";

    /** destroy method declaration */
    private static final String DESTROY_METHOD = "\t/**\n" + "\t * @see org.sun.dbe.server.Adapter#destroy()\n" + "\t */\n" + "\tpublic void destroy() {\n" + "\t\t// TODO: Auto-generated method\n\t}\n\n";

    /**
     * Write a file with the Adapter code
     * 
     * @param adapterClass
     *            name of the Adapter class (to be created)
     * @param classToExtend
     *            name of the class with the code (already created)
     * @throws IOException
     *             exception
     */
    public static void writeAdapter(String adapterClass, String classToExtend) throws IOException {
        String packageName = null;
        String className = adapterClass;
        int begin = adapterClass.lastIndexOf(".");
        if (begin > 0) {
            packageName = adapterClass.substring(0, begin);
            className = adapterClass.substring(begin + 1);
        }
        if (packageName != null) {
            File file = new File(packageName.replace('.', '/'));
            file.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(adapterClass.replace('.', '/') + ".java");
        Writer output = new OutputStreamWriter(fos);
        output.write(HEADER);
        if (packageName != null) {
            output.write("package " + packageName + ";\n\n");
        }
        output.write(IMPORT);
        output.write(CLASS_DEFINITION_PRE);
        output.write(className + " extends " + classToExtend);
        output.write(CLASS_DEFINITION_POST);
        output.write(CONSTURCT_METHOD_PRE);
        output.write(className);
        output.write(CONSTURCT_METHOD_POST);
        output.write(INIT_METHOD);
        output.write(DESTROY_METHOD);
        output.write("}");
        output.flush();
        output.close();
    }

    /**
     * Execute the Axis WSDL2Java and later, once the Stub haa been creates,
     * execute the method to create the Adapter (which extends from the stub)
     * 
     * @param args
     *            command line parameters
     */
    public static void main(String[] args) {
        try {
            writeAdapter(args[0], args[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

;

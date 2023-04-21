package org.ajc.maximo4eclipse.templates;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * <p>
 * Generates the code for the Mbo class.
 * </p>
 * TODO For now the code is hardcoded, the goal is to put it in template files.
 * 
 * @author Antonio Jacob Costa
 */
public class Mbo {

    /**
   * <p>
   * We will initialize file contents with a sample text.
   * </p>
   */
    public static InputStream openContentStream(String mboName, String packageName, String superclassName) {
        String contents = "/**\n" + "*\n" + "*/\n" + "package " + packageName + ";\n" + "\n" + "import java.rmi.RemoteException;\n" + "\n" + "import psdi.mbo.MboSet;\n" + "import psdi.util.MXException;\n" + "\n" + "/**\n" + " * Mbo class for the " + mboName + " Mbo.\n" + " *\n" + " * <p>\n" + " *   Class generated using maximo4eclipse\n" + " *   (http://code.google.com/p/maximo4eclipse/)\n" + " * </p>\n" + " *\n" + " */\n" + "public class " + mboName + " extends " + superclassName + " implements " + mboName + "Remote {\n" + "  /**\n" + "   * @param  mboSet <p>Parent MboSet.</p>\n" + "   * @throws RemoteException\n" + "   */\n" + "  public " + mboName + "(MboSet mboSet) throws MXException, RemoteException {\n" + "    super(mboSet);\n" + "  }\n" + "}\n";
        return new ByteArrayInputStream(contents.getBytes());
    }
}

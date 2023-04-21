package org.ajc.maximo4eclipse.templates;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * <p>
 * Generates the code for the MboSetRemote class.
 * </p>
 * TODO For now the code is hardcoded, the goal is to put it in template files.
 * 
 * @author Antonio Jacob Costa
 */
public class MboSetRemote {

    /**
   * <p>
   * We will initialize file contents with a sample text.
   * </p>
   */
    public static InputStream openContentStream(String mboName, String packageName, String superclassName) {
        String contents = "/**\n" + " * \n" + " */\n" + "package " + packageName + ";\n" + "\n" + " /**\n" + "  * SetRemote interface for the " + mboName + " Mbo.\n" + " *\n" + " * <p>\n" + " *   Class generated using maximo4eclipse\n" + " *   (http://code.google.com/p/maximo4eclipse/)\n" + " * </p>\n" + " *\n" + " */\n" + "public interface " + mboName + "SetRemote extends " + superclassName + "SetRemote {\n" + "}\n";
        return new ByteArrayInputStream(contents.getBytes());
    }
}

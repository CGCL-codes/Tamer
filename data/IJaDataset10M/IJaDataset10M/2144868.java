package net.sf.jfxdplugin.doclets;

import com.sun.javadoc.RootDoc;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nathan Erwin
 * @version $Revision: 14 $ $Date: 2010-03-08 10:03:12 -0500 (Mon, 08 Mar 2010) $
 */
public class TestDoclet {

    private static File outDocsDir = new File("fxdocs");

    public static boolean start(RootDoc root) {
        FileWriter writer = null;
        try {
            if (!outDocsDir.exists()) {
                outDocsDir.mkdir();
            }
            writer = new FileWriter(new File(outDocsDir, "test_index.html"));
            writer.write("<html>\n");
            writer.write("<head>\n");
            writer.write("<title>Test Doclet Output</title>\n");
            writer.write("</head>\n");
            writer.write("<body>\n");
            writer.write("<p>This page intentionally blank.</p>");
            writer.write("</body>\n");
            writer.write("</html>\n");
            writer.flush();
        } catch (IOException ex) {
            Logger.getLogger(TestDoclet.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return false;
        } finally {
            if (null != writer) {
                try {
                    writer.close();
                } catch (IOException ex) {
                    Logger.getLogger(TestDoclet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return true;
    }
}

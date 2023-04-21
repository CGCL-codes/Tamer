package ca.etsmtl.ihe.test;

import java.io.File;
import java.io.FileFilter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import org.apache.log4j.Logger;

/**
 * Handle html generation
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class HtmlGenerator {

    static Logger log = Logger.getRootLogger();

    /**
     * Generate the page where the user have to choose an actor
     *
     * @param path path to the folder containing all the actor directory
     * @param servletname name of the servlet (use to build the url)
     * @param out a printwriter where the html will be written
     */
    public static void actorSelector(String path, String servletname, PrintWriter out) {
        File dir = new File(path);
        FileFilter fileFilter = new FileFilter() {

            public boolean accept(File file) {
                return file.isDirectory();
            }
        };
        File[] directories = dir.listFiles(fileFilter);
        Arrays.sort(directories, new Comparator<File>() {

            public int compare(File f1, File f2) {
                return f1.getName().compareToIgnoreCase(f2.getName());
            }
        });
        StringBuffer sbuff = new StringBuffer();
        sbuff.append("<h2>Actors</h2>");
        sbuff.append("Select the actor to be tested.<p>");
        if (!(directories == null)) {
            for (int i = 0; i < directories.length; i++) {
                String dirname = directories[i].getName();
                dirname.replace("_", " ");
                sbuff.append("<A href=\"" + servletname + "?" + "actor=" + directories[i].getName() + "\">" + dirname + "</A>" + "<p>");
            }
        }
        formaHtmlResponse(sbuff.toString(), out);
    }

    /**
     * Generate the page where the user have to choose a test
     *
     * @param path path to the folder containing all test directory
     * @param servletname name of the servlet (use to build the url)
     * @param out a printwriter where the html will be written
     * @param actor only test of this actor will displayed
     */
    public static void testSelector(String path, String servletname, PrintWriter out, String actor) {
        TestXmlProcessor txp = new TestXmlProcessor();
        File dir = new File(path);
        FileFilter fileFilter = new FileFilter() {

            public boolean accept(File file) {
                return file.isDirectory();
            }
        };
        File[] directories = dir.listFiles(fileFilter);
        Arrays.sort(directories, new Comparator<File>() {

            public int compare(File f1, File f2) {
                return f1.getName().compareToIgnoreCase(f2.getName());
            }
        });
        StringBuffer sbuff = new StringBuffer();
        sbuff.append("<h2>Test Cases</h2>");
        if (!(directories == null)) {
            for (int i = 0; i < directories.length; i++) {
                String dirname = directories[i].getName();
                sbuff.append("<A href=\"" + servletname + "?" + "info=" + actor + "/" + directories[i].getName() + "\">" + dirname + "</A>" + " : " + txp.getdescription(directories[i].getPath() + "/test.xml") + "<p>");
            }
        }
        formaHtmlResponse(sbuff.toString(), out);
    }

    /**
     * Generate the web page with the info of a particular test
     *
     * @param testRootPathath path to the root folder containing all actor subdirectory.
     * @param relativepath path to the test folder
     * @param servletname name of the servlet (use to build the url)
     * @param out a printwriter where the html will be written
     */
    public static void printTestInfo(String testRootPathath, String relativepath, String servletname, PrintWriter out) {
        TestXmlProcessor txp = new TestXmlProcessor();
        StringBuffer sbuff = new StringBuffer();
        sbuff.append(txp.getIntroduction(testRootPathath + relativepath + "/test.xml"));
        sbuff.append("<p>" + "<FORM>" + "<INPUT TYPE=\"BUTTON\" VALUE=\"Start\" ONCLICK=\"window.location.href='" + servletname + "?" + "test=" + relativepath + "'\">" + "</FORM>");
        formaHtmlResponse(sbuff.toString(), out);
    }

    private static void formaHtmlResponse(String content, PrintWriter out) {
        out.append("<html>");
        out.append("<body>");
        out.append(content);
        out.append("</body>");
        out.append("</html>");
    }
}

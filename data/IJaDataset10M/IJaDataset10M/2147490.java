package net.sf.jfxdplugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.junit.Assert;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author Nathan Erwin
 * @version $Revision: 60 $ $Date: 2010-03-29 15:13:40 -0400 (Mon, 29 Mar 2010) $
 */
public class MojoTestHelper extends AbstractMojoTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public String findTagText(Document doc, String tagPath) {
        String text = "";
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            text = xpath.evaluate(tagPath, doc);
        } catch (XPathExpressionException ex) {
            Assert.assertTrue(ex.getMessage(), false);
        }
        return text;
    }

    public BufferedReader getFileReader(File file) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException ex) {
            Assert.assertTrue(ex.getMessage(), false);
        }
        return reader;
    }

    public Mojo getMojo(String goal, String pomFile) {
        File pom = new File(getBasedir(), pomFile);
        Assert.assertNotNull(pom);
        Assert.assertTrue(pom.exists());
        Mojo mojo = null;
        try {
            mojo = lookupMojo(goal, pom);
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage(), false);
        }
        Assert.assertNotNull("mojo must exist", mojo);
        return mojo;
    }

    public Document parseXML(File file) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(file);
        } catch (ParserConfigurationException ex) {
            Assert.assertTrue(ex.getMessage(), false);
        } catch (SAXException ex) {
            Assert.assertTrue(ex.getMessage(), false);
        } catch (IOException ex) {
            Assert.assertTrue(ex.getMessage(), false);
        }
        return null;
    }

    public boolean searchFile(File file, String searchFor) {
        boolean found = false;
        BufferedReader reader = getFileReader(file);
        try {
            String data = reader.readLine();
            while ((!found) && (null != data)) {
                if (-1 != data.indexOf(searchFor)) {
                    found = true;
                }
                data = reader.readLine();
            }
        } catch (IOException ex) {
            Assert.assertTrue(ex.getMessage(), false);
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    Assert.assertTrue(ex.getMessage(), false);
                }
            }
        }
        return found;
    }

    public void validateBasicFiles(String dir) {
        File generatedFile = new File(getBasedir(), dir + "empty.html");
        Assert.assertTrue(generatedFile.exists());
        generatedFile = new File(getBasedir(), dir + "general.css");
        Assert.assertTrue(generatedFile.exists());
        generatedFile = new File(getBasedir(), dir + "index.html");
        Assert.assertTrue(generatedFile.exists());
        generatedFile = new File(getBasedir(), dir + "master-index.html");
        Assert.assertTrue(generatedFile.exists());
        generatedFile = new File(getBasedir(), dir + "mootools-1.2.1-yui.js");
        Assert.assertTrue(generatedFile.exists());
        generatedFile = new File(getBasedir(), dir + "sdk.css");
        Assert.assertTrue(generatedFile.exists());
        generatedFile = new File(getBasedir(), dir + "sdk.js");
        Assert.assertTrue(generatedFile.exists());
        generatedFile = new File(getBasedir(), dir + "sessvars.js");
        Assert.assertTrue(generatedFile.exists());
        generatedFile = new File(getBasedir(), dir + "images/JFX_arrow_down.png");
        Assert.assertTrue(generatedFile.exists());
        generatedFile = new File(getBasedir(), dir + "images/JFX_arrow_right.png");
        Assert.assertTrue(generatedFile.exists());
        generatedFile = new File(getBasedir(), dir + "images/JFX_arrow_up.png");
        Assert.assertTrue(generatedFile.exists());
        generatedFile = new File(getBasedir(), dir + "images/JFX_highlight_dot.png");
        Assert.assertTrue(generatedFile.exists());
        generatedFile = new File(getBasedir(), dir + "images/quote-background-1.gif");
        Assert.assertTrue(generatedFile.exists());
    }

    public void validateHiddenClass() {
        File generatedFile = new File(MojoTestHelper.getBasedir(), "target/test/unit/show-public/apidocs/index.html");
        boolean found = false;
        BufferedReader reader = getFileReader(generatedFile);
        try {
            String data = reader.readLine();
            while ((!found) && (null != data)) {
                if ("<li class=\"profile-common private\">".equals(data)) {
                    data = reader.readLine();
                    if ("<a href=\"pkg/pkg.MyPersonalClass.html\">MyPersonalClass</a>".equals(data)) {
                        found = true;
                    }
                }
                data = reader.readLine();
            }
        } catch (IOException ex) {
            Assert.assertTrue(ex.getMessage(), false);
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    Assert.assertTrue(ex.getMessage(), false);
                }
            }
        }
        Assert.assertTrue(found);
    }
}

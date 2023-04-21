package goym;

import java.io.File;
import java.io.IOException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

public class GoymLang {

    private GoymStatus status;

    private Document doc;

    private int langIDnumber;

    public GoymLang(int langIDnumber, GoymStatus status) {
        this.status = status;
        this.langIDnumber = langIDnumber;
        File langFile = new File("lang.xml");
        try {
            SAXBuilder builder = new SAXBuilder();
            this.doc = builder.build("lang.xml");
        } catch (IOException e) {
            this.status.write("Couldn�t open language file: " + langFile.getAbsolutePath(), e);
        } catch (JDOMException e) {
            this.status.write("Error while parsing xml-file: " + langFile.getAbsolutePath(), e);
        }
    }

    public String getWord(String name) {
        try {
            String query = "//language[@const='" + this.langIDnumber + "']/section/string[@name='" + name + "']";
            Element result = (Element) XPath.selectSingleNode(this.doc, query);
            return result.getTextNormalize();
        } catch (Exception e) {
            this.status.write("word not in wordlist: " + name, e);
            return name;
        }
    }
}

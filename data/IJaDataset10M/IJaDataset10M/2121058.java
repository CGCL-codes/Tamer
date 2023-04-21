package uk.ac.cam.caret.minibix.taggy;

import java.io.*;
import org.xml.sax.*;
import uk.ac.cam.caret.tagphage.parser.*;
import uk.ac.cam.caret.tagphage.parser.ObjectStack;
import uk.ac.cam.caret.tagphage.parser.ruleparser.*;

public class TaggyFactory {

    private ParserFactory pf;

    private InputStream load(String file) {
        String qackage = getClass().getPackage().getName().replace(".", "/");
        InputStream out = Thread.currentThread().getContextClassLoader().getResourceAsStream(qackage + "/" + file);
        return out;
    }

    public TaggyFactory() {
        try {
            RuleParser rp = new RuleParser();
            InputStream rules = load("taggy-tagphage.xml");
            RuleSet rs = rp.parseRules(new InputSource(rules));
            Schema schema = rs.getSchemaByName("main");
            pf = schema.getParserFactory();
        } catch (CreationException x) {
            throw new RuntimeException("Bad rules file in taggy", x);
        } catch (BadConfigException x) {
            throw new RuntimeException("Bad rules file in taggy", x);
        } catch (SAXException x) {
            throw new RuntimeException("Bad rules file in taggy", x);
        } catch (IOException x) {
            throw new RuntimeException("Bad rules file in taggy", x);
        }
    }

    public TaggyFile createEmptyFile() {
        return new TaggyFile();
    }

    public TaggyFile parseFile(InputStream in) throws SAXException, IOException {
        try {
            ParseState parser = pf.getParser();
            XMLReader reader = parser.getReader();
            reader.parse(new InputSource(in));
            ObjectStack os = parser.getObjectStack();
            Object res1 = os.pop();
            return (TaggyFile) res1;
        } catch (BadConfigException x) {
            throw new RuntimeException("Bad rules file in taggy", x);
        }
    }
}

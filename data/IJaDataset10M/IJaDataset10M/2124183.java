package org.jpedal.examples.text.extractheadlines;

import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.jpedal.storypad.Configuration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class HeadlineConfiguration extends Configuration {

    /**default value*/
    String[] sectionTokens = { "font face=\"TimesClassicDisplay\" style=\"font-size:16pt\"", "font face=\"TimesClassicDisplayBold\" style=\"font-size:16pt\"", "font face=\"ClassicFranklinNeoBeta\" style=\"font-size:16pt\"", "font face=\"ClassicFranklinNeoBeta\" style=\"font-size:18pt\"", "font face=\"ClassicFranklinNeoExt-Bold\" style=\"font-size:18pt\"" };

    private int[] x1 = { -10, 180, 10 };

    private int[] x2 = { 105, 316, 1537 };

    private int[] y1 = { 930, 930, 970 };

    private int[] y2 = { 908, 900, 946 };

    private HeadlineConfiguration() {
    }

    /**initialise category and load or create config file*/
    public HeadlineConfiguration(String configDir) {
        boolean fileExists = loadValues(configDir);
        if (!fileExists) {
            saveValues();
            loadValues(configDir);
        }
    }

    /**
     * write out a config file
     */
    public boolean saveValues() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();
            Node root = doc.createElement(sectionName);
            Node creation = doc.createComment("Created " + org.jpedal.utils.TimeNow.getShortTimeNow());
            doc.appendChild(creation);
            doc.appendChild(root);
            int count = sectionTokens.length;
            Element tagCount = doc.createElement("xmlCount");
            tagCount.setAttribute("value", "" + count);
            root.appendChild(tagCount);
            String key = "xmlTag";
            for (int i = 0; i < count; i++) {
                String currentKey = key + "_" + i;
                Element section = doc.createElement(currentKey);
                section.setAttribute("value", "" + sectionTokens[i]);
                root.appendChild(section);
            }
            int locCount = this.x1.length;
            Element loc = doc.createElement("locationCount");
            loc.setAttribute("value", "" + locCount);
            root.appendChild(loc);
            key = "locTag";
            String[] coords = { "x1", "y1", "x2", "y2" };
            for (int i = 0; i < locCount; i++) {
                for (int coord = 0; coord < 4; coord++) {
                    String currentKey = key + "_" + i + "_" + coords[coord];
                    Element section = doc.createElement(currentKey);
                    switch(coord) {
                        case 0:
                            section.setAttribute("value", "" + x1[i]);
                            break;
                        case 1:
                            section.setAttribute("value", "" + y1[i]);
                            break;
                        case 2:
                            section.setAttribute("value", "" + x2[i]);
                            break;
                        case 3:
                            section.setAttribute("value", "" + y2[i]);
                            break;
                    }
                    root.appendChild(section);
                }
            }
            InputStream stylesheet = this.getClass().getResourceAsStream("/org/jpedal/examples/simpleviewer/res/xmlstyle.xslt");
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer(new StreamSource(stylesheet));
            transformer.transform(new DOMSource(doc), new StreamResult(configDir + sectionName + ".xml"));
            System.out.println("Created " + configDir + sectionName + ".xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}

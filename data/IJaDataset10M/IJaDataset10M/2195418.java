package net.sourceforge.olduvai.lrac.jdbcdaytonadataservice.structure;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.sourceforge.olduvai.lrac.drawer.AccordionLRACDrawer;
import net.sourceforge.olduvai.lrac.genericdataservice.AbstractSource;
import net.sourceforge.olduvai.lrac.genericdataservice.structure.SourceInterface;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/** 
 *	SWIFT jdbc implementation of a Source.  This class specifies the methods needed to 
 * implement a Source from the jdbc data.      
 *
 * @author Peter McLachlan (spark343@cs.ubc.ca)
 */
public class Source extends AbstractSource {

    private static final String ROUTER_TYPE = "router";

    private static final String ROOTTAG = "root";

    private static final String SOURCENAMETAG = "sourcename";

    Source(String sourceName, AccordionLRACDrawer lrd) {
        super(sourceName, lrd);
    }

    public String getURL() {
        return "To be done";
    }

    public String toString() {
        return getName();
    }

    /**
	 * Creates a new source given the source ID.  Will not create a source with a null name. 
	 * 
	 * TODO: there may be other daytona source metadata we wish to store in the future
	 * 
	 * @param newSourceId
	 * @return
	 */
    public static Source createSource(String newSourceId) {
        if (newSourceId == null || newSourceId.equals("")) return null;
        final Source source = new Source(newSourceId, null);
        source.setInternalType(ROUTER_TYPE);
        return source;
    }

    /**
	 * saves the list of selected sources to file.  
	 * @param fileName
	 * @param sources
	 * @throws IOException 
	 */
    public static void saveSelectedSources(File fileName, List<SourceInterface> sources) throws IOException {
        Element newRoot = new Element(ROOTTAG);
        for (SourceInterface source : sources) newRoot.addContent(source.getXML());
        Document doc = new Document();
        doc.addContent(newRoot);
        XMLOutputter outputter = new XMLOutputter();
        outputter.output(doc, new FileWriter(fileName));
    }

    /**
	 * Returns a subset from the parameter allGroups which match with the list of groups 
	 * specified by the XML stream handle sourceGroupStream 
	 * 
	 * @param allGroups
	 * @param sourceGroupStream
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public static List<SourceInterface> loadSelectedSources(Map<String, SourceInterface> allSources, InputStream savedSourceStream) throws IOException {
        SAXBuilder builder = new SAXBuilder();
        Document doc;
        List<SourceInterface> result = new ArrayList<SourceInterface>();
        try {
            doc = builder.build(savedSourceStream);
        } catch (JDOMException e1) {
            System.err.println("SourceGroup: Error parsing saved source file: " + savedSourceStream);
            return result;
        } catch (IOException e2) {
            System.err.println("SourceGroup: Cannot load saved source file: " + savedSourceStream);
            return result;
        }
        List sources = doc.getRootElement().getChildren(SOURCENAMETAG);
        List<String> savedList = new ArrayList<String>();
        for (Object sourcenameobj : sources) {
            final Element sourcenameEl = (Element) sourcenameobj;
            savedList.add(sourceNameFromXML(sourcenameEl));
        }
        for (String sourceName : savedList) {
            final SourceInterface source = allSources.get(sourceName);
            if (source != null) result.add(source);
        }
        return result;
    }

    private static String sourceNameFromXML(Element sourcenameEl) {
        return sourcenameEl.getText();
    }

    /**
	 * Retrieves a minimal description of the data source that allows it to
	 * be uniquely identified.   
	 */
    public Element getXML() {
        Element sourceEl = new Element(SOURCENAMETAG);
        sourceEl.setText(getInternalName());
        return sourceEl;
    }
}

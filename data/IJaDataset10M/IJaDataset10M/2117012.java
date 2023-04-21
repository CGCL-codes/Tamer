package taxonfinder.lexicon;

import java.io.IOException;
import java.util.ArrayList;
import taxonfinder.util.Util;

/**
 * @author ahmedabdeenhamed
 *
 */
public class LexiconWriter {

    /**
	 * A method that takes a list of GenusSpecies objects and writes them to 
	 * a dictionary file so that it can be used from within the ConceptMapper
	 * for lookup.
	 * 
	 * The method uses a helper from the utility library to write the objects
	 * to an xml
	 *   
	 * @param gsList
	 */
    public static void writeGenusSpeciesToDictionary(ArrayList<GenusSpecies> gsList) {
        StringBuffer aBuffer = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        aBuffer.append("\n");
        aBuffer.append("<synonym>");
        aBuffer.append("\n");
        for (GenusSpecies gs : gsList) {
            aBuffer.append("	<token canonical=\"");
            aBuffer.append(gs.getSuffixString());
            aBuffer.append("\">");
            aBuffer.append("\n");
            aBuffer.append("		<variant base=\"");
            aBuffer.append(gs.getSuffixString());
            aBuffer.append("\"/>");
            aBuffer.append("\n");
            aBuffer.append("	</token>");
            aBuffer.append("\n");
        }
        aBuffer.append("</synonym>");
        try {
            Util.writeStringToFile(aBuffer.toString(), "", "genus-species-model.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * 
	 * @param ntList
	 */
    public static void writeNonTaxonToDictionary(ArrayList<String> ntList) {
        StringBuffer aBuffer = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        aBuffer.append("\n");
        aBuffer.append("<synonym>");
        aBuffer.append("\n");
        for (String nt : ntList) {
            aBuffer.append("	<token canonical=\"");
            aBuffer.append(nt);
            aBuffer.append("\">");
            aBuffer.append("\n");
            aBuffer.append("		<variant base=\"");
            aBuffer.append(nt);
            aBuffer.append("\"/>");
            aBuffer.append("\n");
            aBuffer.append("	</token>");
            aBuffer.append("\n");
        }
        aBuffer.append("</synonym>");
        try {
            Util.writeStringToFile(aBuffer.toString(), "", "non-taxon-words.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

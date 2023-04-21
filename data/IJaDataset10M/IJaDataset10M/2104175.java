package org.pausequafe.core.parsers;

import java.util.ArrayList;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.pausequafe.data.character.APIData;

public class CharacterListParser {

    /**
     * Generates a character list from a JDOM Document.
     * 
     * @param doc
     * @param apiKey
     * @param userID
     * @return a character list
     */
    @SuppressWarnings("unchecked")
    public static List<APIData> getList(Document doc, int userID, String apiKey) {
        Element root = doc.getRootElement();
        List<Element> xmlList = root.getChild("result").getChild("rowset").getChildren("row");
        List<APIData> list = new ArrayList<APIData>();
        for (Element row : xmlList) {
            String chrName = row.getAttributeValue("name");
            int charID = Integer.parseInt(row.getAttributeValue("characterID"));
            list.add(new APIData(charID, chrName, userID, apiKey));
        }
        return list;
    }
}

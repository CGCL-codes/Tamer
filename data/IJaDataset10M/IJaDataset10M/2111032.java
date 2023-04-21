package com.loribel.tools.sa;

import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.loribel.commons.abstraction.ENCODING;
import com.loribel.commons.business.GB_BOFactoryTools;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObject;
import com.loribel.commons.exception.GB_LoadException;
import com.loribel.commons.net.GB_HttpTools;
import com.loribel.commons.util.CTools;
import com.loribel.commons.util.GB_XmlReaderTools;
import com.loribel.commons.util.Info;
import com.loribel.commons.util.Log;
import com.loribel.commons.util.STools;
import com.loribel.commons.xml.GB_ElementTools;
import com.loribel.commons.xml.GB_XmlXPathTools;
import com.loribel.tools.GB_ToolsInitializer;
import com.loribel.tools.sa.bo.GB_CharLatinBO;

/**
 * Tools for Latin chars.
 * 
 * @author Gr�gory Borelli
 */
public final class GB_LatinFullTools {

    private static GB_CharLatinBO[] charLatins;

    private GB_LatinFullTools() {
    }

    public static void main(String[] args) {
        GB_ToolsInitializer.initAll();
        debugToSystemOut();
        try {
            File l_file = new File("E:/MyDocuments/_txt/wiki-lng.xml");
            Document l_doc = GB_XmlReaderTools.parseFile(l_file);
            NodeList l_nodes = GB_XmlXPathTools.selectNodeList(l_doc, "//img");
            int len = l_nodes.getLength();
            for (int i = 0; i < len; i++) {
                Element l_node = (Element) l_nodes.item(i);
                String l_src = l_node.getAttribute("src");
                System.out.println(l_src);
                Node l_nodeTr = GB_XmlXPathTools.selectSingleNode(l_node, "ancestor::tr");
                NodeList l_nodeTds = GB_XmlXPathTools.selectNodeList(l_nodeTr, "td");
                Node l_nodeTd = l_nodeTds.item(2);
                Node l_nodeA = GB_ElementTools.getFirstChildElement(l_nodeTd);
                String l_id = GB_ElementTools.getElementTextValue(l_nodeA);
                l_id = l_id.toLowerCase();
                System.out.println(l_id);
                File l_destFile = new File("C:/temp/flag/" + l_id + ".png");
                GB_HttpTools.loadUrlToFile(l_src, l_destFile, ENCODING.ISO_8859_1);
                l_destFile = new File("C:/temp/flag/big/" + l_id + ".svg");
                l_src = STools.replace(l_src, "thumb/", "");
                int l_index = l_src.indexOf("px-");
                l_src = l_src.substring(0, l_index - 3);
                GB_HttpTools.loadUrlToFile(l_src, l_destFile, ENCODING.ISO_8859_1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void debugToSystemOut() {
        GB_CharLatinBO[] l_bos = getLatins();
        int len = CTools.getSize(l_bos);
        for (int i = 0; i < len; i++) {
            GB_CharLatinBO l_item = l_bos[i];
            System.out.println(l_item.getLabel().getFr());
        }
    }

    private static GB_CharLatinBO[] getLatins() {
        if (charLatins == null) {
            try {
                GB_SimpleBusinessObject[] l_bos = GB_BOFactoryTools.getFactory().loadFromXmlResource(GB_CharLatinBO.class, "latin.xml");
                int len = CTools.getSize(l_bos);
                charLatins = new GB_CharLatinBO[len];
                System.arraycopy(l_bos, 0, charLatins, 0, len);
            } catch (GB_LoadException ex) {
                Log.logWarning(GB_LatinFullTools.class, ex);
            }
        }
        return charLatins;
    }

    public static String replaceEntityByChar(String a_text) {
        String retour = a_text;
        GB_CharLatinBO[] l_bos = getLatins();
        int len = CTools.getSize(l_bos);
        int l_idInfo = Info.newId(len);
        for (int i = 0; i < len; i++) {
            Info.setInfoList(l_idInfo, i, "");
            GB_CharLatinBO l_item = l_bos[i];
            String l_entity = l_item.getEntityName();
            String l_char = l_item.getCaractere();
            if (STools.isNotNull(l_entity) && STools.isNotNull(l_char)) {
                l_entity = "&" + l_entity + ";";
                retour = STools.replace(retour, l_entity, l_char);
            }
        }
        Info.end(l_idInfo);
        return retour;
    }
}

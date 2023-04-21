package gb.fwk.xsml;

import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.loribel.commons.abstraction.ENCODING;
import com.loribel.commons.abstraction.GB_StringAction;
import com.loribel.commons.util.Log;
import com.loribel.commons.util.string.GB_SAFormatAllHtmlFromTxt;
import com.loribel.commons.xml.GB_ElementTools;
import com.loribel.commons.xml.GB_NodeList;
import com.loribel.commons.xml.GB_XmlWriterDefault;
import com.loribel.commons.xml.GB_XsmlReaderDefault;

/**
 * Class to execute an action.
 *
 * @author Gr�gory Borelli
 */
public class GB_XsmlAction_xmlGen extends GB_XsmlActionAbstract {

    /**
     * Constructor of GB_XsmlAction_xmlGen with parameter(s).
     *
     * @param a_nodeAction Element -
     * @param a_directory File -
     */
    public GB_XsmlAction_xmlGen(Element a_nodeAction, File a_directory) {
        super(a_nodeAction, a_directory);
    }

    public void execute() {
        try {
            File l_xsmlFile = getParamFileReadCheck("xsmlFile", null);
            File l_xmlFile = getParamFileCheck("xmlFile");
            GB_XsmlReaderDefault l_reader = new GB_XsmlReaderDefault();
            Document l_document = l_reader.readFile(l_xsmlFile);
            Element l_root = l_document.getDocumentElement();
            GB_NodeList l_children = GB_ElementTools.getChildElementsByName(l_root, "html", -1);
            if (l_children != null) {
                GB_StringAction l_stringAction = new GB_SAFormatAllHtmlFromTxt();
                l_children.updateElementValues(l_stringAction);
            }
            GB_XmlWriterDefault l_writer = new GB_XmlWriterDefault(l_xmlFile, ENCODING.DEFAULT);
            l_writer.writePrologue(l_document);
            l_writer.write(l_document.getDocumentElement());
            l_writer.close();
            Log.debug(this, "gen " + l_xmlFile.getName());
        } catch (Exception ex) {
            Log.logWarning(this, ex);
            return;
        }
    }
}

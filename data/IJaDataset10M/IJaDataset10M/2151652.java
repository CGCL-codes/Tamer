package net.etherstorm.jOpenRPG.nodehandlers;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import java.net.URL;
import net.etherstorm.jOpenRPG.utils.ExceptionHandler;

/**
 * Class declaration
 *
 *
 * @author $Author: tedberg $
 * @version $Revision: 352 $
 */
public class file_loader extends BaseNodehandler {

    /**
	 * Constructor declaration
	 *
	 *
	 * @param e
	 *
	 */
    public file_loader(Element e) {
        super(e);
    }

    /**
	 * Method declaration
	 *
	 *
	 *
	 */
    public void loadFile() {
        try {
            SAXBuilder sax = new SAXBuilder();
            String ref = myElement.getChild("file").getAttributeValue("name");
            URL url = file_loader.class.getResource("/addon/" + ref);
            Element e = sax.build(url).getRootElement();
            e.detach();
            referenceManager.getMainFrame().getGameTreePanel().doImportXML(new Element("foo").addContent(e));
        } catch (Exception ex) {
            ExceptionHandler.handleException(ex);
        }
    }

    /**
	 * Method declaration
	 *
	 *
	 *
	 */
    public void openHandler() {
        loadFile();
    }
}

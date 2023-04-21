package gjset.tools;

import gjset.GameConstants;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

/**
 * This class contains a wide variety of useful classes and utilities for working with messages.
 */
public class MessageUtils {

    /**
	 * Wraps a message with enclosing tags and a comm version.
	 *
	 * @param messageElement
	 * @return
	 */
    public static Element wrapMessage(Element messageElement) {
        DocumentFactory documentFactory = DocumentFactory.getInstance();
        Element rootElement = documentFactory.createElement("combocards");
        Element versionElement = documentFactory.createElement("version");
        versionElement.setText(GameConstants.COMM_VERSION);
        rootElement.add(versionElement);
        rootElement.add(messageElement);
        return rootElement;
    }

    /**
	 * Useful for debugging, this command pretty prints XML.
	 * 
	 * @param element
	 * @return
	 */
    public static String prettyPrint(Element element) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        XMLWriter writer;
        try {
            writer = new XMLWriter(stream);
            writer.write(element);
            writer.flush();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stream.toString();
    }
}

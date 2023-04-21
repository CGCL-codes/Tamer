package org.pagger.data.picture.xmp;

import java.util.Locale;
import org.pagger.data.MetadataException;
import org.pagger.data.ValueAccess;
import org.w3c.dom.Node;

/**
 * @author Gerd Saurer
 */
public class XmpLocaleMetadataAccess extends AbstractXmpMetadataAccess<Locale> {

    @Override
    public Locale getXmpValue(Node node) throws MetadataException {
        Locale retVal = null;
        String textValue = getTextContent(node);
        if (textValue != null) {
            final Locale[] availableLocales = Locale.getAvailableLocales();
            for (int i = 0; i < availableLocales.length; i++) {
                Locale locale = availableLocales[i];
                if (locale.toString().equals(textValue)) {
                    retVal = locale;
                    break;
                }
            }
        }
        return retVal;
    }

    @Override
    public void setXmpValue(Node node, Locale value) throws MetadataException {
        String textValue = null;
        if (value != null) {
            textValue = value.toString();
        }
        setTextContent(node, textValue);
    }

    @Override
    public ValueAccess<Node, Locale> copy() {
        return new XmpLocaleMetadataAccess();
    }
}

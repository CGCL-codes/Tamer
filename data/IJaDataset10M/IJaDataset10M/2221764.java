package org.apache.myfaces.trinidadinternal.renderkit.core.xhtml;

import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.apache.myfaces.trinidad.context.RenderingContext;
import org.apache.myfaces.trinidad.render.CoreRenderer;

/**
 * Utilities for working with access keys.
 */
public class AccessKeyUtils {

    /**
   * Renders the text with access key having default style of underline.
   */
    public static void renderAccessKeyText(FacesContext context, Object textValue, int keyIndex) throws IOException {
        renderAccessKeyText(context, textValue, keyIndex, SkinSelectors.AF_ACCESSKEY_STYLE_CLASS);
    }

    public static void renderAccessKeyText(FacesContext context, Object textValue, int keyIndex, String accessKeyClass) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        if ((textValue != null) && (keyIndex != -1)) {
            String textString = textValue.toString();
            char[] textChars = textString.toCharArray();
            writer.writeText(textChars, 0, keyIndex);
            if (accessKeyClass != null && accessKeyClass.length() > 0) {
                writer.startElement("span", null);
                XhtmlRenderer.renderStyleClass(context, RenderingContext.getCurrentInstance(), accessKeyClass);
                writer.writeText(textChars, keyIndex, 1);
                writer.endElement("span");
                keyIndex++;
            }
            int charsLeft = textChars.length - keyIndex;
            if (charsLeft > 0) {
                writer.writeText(textChars, keyIndex, charsLeft);
            }
        } else {
            if (textValue != null) writer.writeText(textValue, null);
        }
    }

    /**
   * Renders the text with the access key highlighted as appropriate.
   */
    public static void renderAccessKeyText(FacesContext context, Object textValue, char accessKey, String highlightElement) throws IOException {
        Object textString = (textValue != null) ? textValue.toString() : null;
        renderAccessKeyText(context, textString, getAccessKeyIndex(textString, accessKey), highlightElement);
    }

    /**
   * Returns the index of the access key in the specified text.
   */
    public static int getAccessKeyIndex(Object textValue, char accessChar) {
        int keyIndex = -1;
        if ((textValue != null) && (accessChar != CoreRenderer.CHAR_UNDEFINED)) {
            String textString = textValue.toString();
            keyIndex = textString.indexOf(accessChar);
            if (keyIndex == -1) {
                char oppositeChar = Character.toLowerCase(accessChar);
                if (oppositeChar == accessChar) {
                    oppositeChar = Character.toUpperCase(accessChar);
                }
                if (oppositeChar != accessChar) {
                    keyIndex = textString.indexOf(oppositeChar);
                }
            }
        }
        return keyIndex;
    }
}

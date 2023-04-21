package org.waveprotocol.wave.client.editor.testing;

import org.waveprotocol.wave.client.editor.Editor;
import org.waveprotocol.wave.model.schema.conversation.ConversationSchemas;
import org.waveprotocol.wave.model.document.util.DocProviders;
import org.waveprotocol.wave.model.document.util.XmlStringBuilder;

/**
 * Utility that decorates editors by adding the ability to get/set content using types
 *   other than DocInitialisation ops.
 *
 * TODO(patcoleman): figure out which class(/es) should be natively supported inside Editor
 * the interface, and which should be decorated within this class.
 *
 * @author patcoleman@google.com (Pat Coleman)
 */
public class ContentSerialisationUtil {

    /** Static utility, hence private constructor. */
    private ContentSerialisationUtil() {
    }

    /** Gets the editor's persistent document as a String. */
    public static String getContentString(Editor editor) {
        return XmlStringBuilder.innerXml(editor.getPersistentDocument()).toString();
    }

    /**
   * Sets the editor's content to a given string - the accepted format currently is XML,
   *   note to be careful that attribute values are surrounded by ", not '.
   */
    public static void setContentString(Editor editor, String content) {
        editor.setContent(DocProviders.POJO.parse(content).asOperation(), ConversationSchemas.BLIP_SCHEMA_CONSTRAINTS);
    }
}

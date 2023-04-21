package chrriis.dj.nativeswing.swtimpl.components;

import java.util.EventObject;

/**
 * @author Christopher Deckers
 */
public class HTMLEditorEvent extends EventObject {

    private JHTMLEditor htmlEditor;

    /**
   * Construct an HTML editor event.
   * @param htmlEditor the HTML editor.
   */
    public HTMLEditorEvent(JHTMLEditor htmlEditor) {
        super(htmlEditor);
        this.htmlEditor = htmlEditor;
    }

    /**
   * Get the HTML editor.
   * @return the HTML editor.
   */
    public JHTMLEditor getHTMLEditor() {
        return htmlEditor;
    }
}

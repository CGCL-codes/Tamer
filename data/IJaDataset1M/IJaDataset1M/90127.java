package freemind.common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import freemind.main.HtmlTools;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.MindMapController.MindMapControllerPlugin;

public class ScriptEditorProperty extends PropertyBean implements PropertyControl, ActionListener {

    public interface ScriptEditorStarter extends MindMapControllerPlugin {

        String startEditor(String scriptInput);
    }

    String description;

    String label;

    String script;

    JButton mButton;

    final JPopupMenu menu = new JPopupMenu();

    private final MindMapController mMindMapController;

    private static java.util.logging.Logger logger = null;

    /**
	 */
    public ScriptEditorProperty(String description, String label, MindMapController pMindMapController) {
        super();
        this.description = description;
        this.label = label;
        mMindMapController = pMindMapController;
        if (logger == null) {
            logger = mMindMapController.getFrame().getLogger(this.getClass().getName());
        }
        mButton = new JButton();
        mButton.addActionListener(this);
        script = "";
    }

    public String getDescription() {
        return description;
    }

    public String getLabel() {
        return label;
    }

    public void setValue(String value) {
        setScriptValue(value);
    }

    public String getValue() {
        return HtmlTools.unicodeToHTMLUnicodeEntity(HtmlTools.toXMLEscapedText(script));
    }

    public void layout(DefaultFormBuilder builder, TextTranslator pTranslator) {
        JLabel label = builder.append(pTranslator.getText(getLabel()), mButton);
        label.setToolTipText(pTranslator.getText(getDescription()));
    }

    public void actionPerformed(ActionEvent arg0) {
        for (Iterator iter = mMindMapController.getPlugins().iterator(); iter.hasNext(); ) {
            MindMapControllerPlugin plugin = (MindMapControllerPlugin) iter.next();
            if (plugin instanceof ScriptEditorStarter) {
                ScriptEditorStarter starter = (ScriptEditorStarter) plugin;
                String resultScript = starter.startEditor(script);
                if (resultScript != null) {
                    script = resultScript;
                    firePropertyChangeEvent();
                }
            }
        }
    }

    /**
	 */
    private void setScriptValue(String result) {
        if (result == null) {
            result = "";
        }
        script = HtmlTools.toXMLUnescapedText(HtmlTools.unescapeHTMLUnicodeEntity(result));
        logger.fine("Setting script to " + script);
        mButton.setText(script);
    }

    public void setEnabled(boolean pEnabled) {
        mButton.setEnabled(pEnabled);
    }
}

package pt.igeo.snig.mig.editor.ui.recordEditor.formFiller.forms.format;

import javax.swing.JComponent;
import javax.swing.JLabel;
import pt.igeo.snig.mig.editor.i18n.StringsManager;
import pt.igeo.snig.mig.editor.i18n.ToolTipStringsManager;
import fi.mmm.yhteinen.swing.core.component.YPanel;
import fi.mmm.yhteinen.swing.core.component.YTitledBorder;
import fi.mmm.yhteinen.swing.core.component.text.YTextField;
import fi.mmm.yhteinen.swing.core.tools.YUIToolkit;

/**
 * View for the format form.
 * 
 * @author Ant�nio Silva
 * @version $Revision: 9175 $
 * @since 1.0
 */
public class FormatView extends YPanel {

    /** Java requirement */
    private static final long serialVersionUID = 3465365600961641073L;

    /** name */
    private YTextField name = new YTextField();

    /** name label */
    private JLabel nameLabel = new JLabel("a");

    /** version */
    private YTextField version = new YTextField();

    /** version label */
    private JLabel versionLabel = new JLabel("a");

    /** titled border */
    private YTitledBorder border = new YTitledBorder("a");

    /**
	 * Constructor for this view
	 * 
	 */
    public FormatView() {
        prepareComponents();
        setMVCNames();
        translate();
        YUIToolkit.guessViewComponents(this);
    }

    /**
	 * Adding components to the panel.
	 */
    private void prepareComponents() {
        setBorder(border);
        JComponent[][] comps = { { nameLabel, name }, { null }, { versionLabel, version } };
        int[][] widths = { { 4, 11 }, { 15 }, { 4, 11 } };
        addComponents(comps, widths, 15);
        setToolTips();
    }

    /**
	 * set tool tips
	 */
    public void setToolTips() {
        name.setToolTipText(ToolTipStringsManager.getInstance().getString("pt.igeo.snig.mig.editor.ui.recordEditor.formFiller.forms.format.name"));
        version.setToolTipText(ToolTipStringsManager.getInstance().getString("pt.igeo.snig.mig.editor.ui.recordEditor.formFiller.forms.format.version"));
    }

    /**
	 * Set MVC names
	 */
    private void setMVCNames() {
        name.setMvcName("format.name");
        version.setMvcName("format.version");
    }

    /**
	 * translates this component
	 */
    public void translate() {
        setToolTips();
        StringsManager sm = StringsManager.getInstance();
        nameLabel.setText(sm.getString("formatName"));
        versionLabel.setText(sm.getString("formatVersion"));
        border.setTitle(sm.getString("format"));
    }
}

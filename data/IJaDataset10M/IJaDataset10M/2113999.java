package uk.ac.ukoln.rrorife.gui;

import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.brunchboy.util.swing.relativelayout.AttributeConstraint;
import com.brunchboy.util.swing.relativelayout.AttributeType;
import com.brunchboy.util.swing.relativelayout.DependencyManager;
import com.brunchboy.util.swing.relativelayout.RelativeLayout;

/**
 * Generates a Converter Explorer panel, consisting of a title and a Converter
 * Information panel.
 * 
 * @author Alex Ball
 */
public class GuiConvExpl extends JPanel {

    private static final long serialVersionUID = 3312171759355701590L;

    /**
	 * The title text for the panel.
	 */
    public static String NAME = "Converter Explorer";

    /**
	 * Layout manager for the panel.
	 */
    private final RelativeLayout panelLayout = new RelativeLayout();

    /**
	 * Panel used for displaying converter information.
	 */
    private final GuiConvInfo convInfoPanel;

    /**
	 * Registry containing converter information.
	 */
    private final Registry registry;

    /**
	 * Generates the panel title and an instance of the Converter Information
	 * panel for all the converters in the Registry.
	 * 
	 * @param r
	 *          A Registry containing converter information
	 */
    public GuiConvExpl(Registry r) {
        registry = r;
        setLayout(panelLayout);
        JLabel title = new JLabel(NAME);
        title.setFont(title.getFont().deriveFont(16.0f));
        title.setFont(title.getFont().deriveFont(Font.BOLD));
        panelLayout.addConstraint("title", AttributeType.TOP, new AttributeConstraint(DependencyManager.ROOT_NAME, AttributeType.TOP, 4));
        panelLayout.addConstraint("title", AttributeType.LEFT, new AttributeConstraint(DependencyManager.ROOT_NAME, AttributeType.LEFT, 4));
        add(title, "title");
        convInfoPanel = new GuiConvInfo(registry, registry.getSoftwareIDs());
        panelLayout.addConstraint("convInfoPanel", AttributeType.TOP, new AttributeConstraint("title", AttributeType.BOTTOM, 8));
        panelLayout.addConstraint("convInfoPanel", AttributeType.LEFT, new AttributeConstraint(DependencyManager.ROOT_NAME, AttributeType.LEFT, 4));
        panelLayout.addConstraint("convInfoPanel", AttributeType.RIGHT, new AttributeConstraint(DependencyManager.ROOT_NAME, AttributeType.RIGHT, -4));
        add(convInfoPanel, "convInfoPanel");
    }

    /**
	 * Passes request to display information about a particular converter to the
	 * Converter Information panel.
	 * 
	 * @param id
	 *          SoftwareID corresponding to the converter about which information
	 *          is to be displayed.
	 */
    public void setSelectedID(SoftwareID id) {
        convInfoPanel.setSelectedID(id);
    }

    /**
	 * Passes request to display information about a conversion from a particular
	 * format to the Converter Information panel.
	 * 
	 * @param id
	 *          FormatID corresponding to the input format of the conversion.
	 */
    public void setInputFormatID(FormatID id) {
        convInfoPanel.setInputFormatID(id);
    }

    /**
	 * Passes request to display information about a conversion to a particular
	 * format to the Converter Information panel.
	 * 
	 * @param id
	 *          FormatID corresponding to the output format of the conversion.
	 */
    public void setOutputFormatID(FormatID id) {
        convInfoPanel.setOutputFormatID(id);
    }

    /**
	 * Passes request to update Converter Information panel.
	 */
    public void updateConvList() {
        convInfoPanel.update(registry.getSoftwareIDs());
    }
}

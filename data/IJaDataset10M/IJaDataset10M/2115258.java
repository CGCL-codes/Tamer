package fr.itris.glips.extension.jwidget.trends.edition.general;

import java.awt.*;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.rtdaeditor.jwidget.*;

/**
 * the panel containing all the general widgets used to configure the trends
 * @author ITRIS, Jordi SUC
 */
public class GeneralConfigPanel extends JPanel {

    /**
	 * the jwidget edition object
	 */
    private JWidgetEdition jwidgetEdition;

    /**
	 * the start panel
	 */
    private StartPanel startPanel;

    /**
	 * the real time panel
	 */
    private RealTimePanel realTimePanel;

    /**
	 * the history panel
	 */
    private HistoryPanel historyPanel;

    /**
	 * the interpolation panel
	 */
    private InterpolationPanel interpolationPanel;

    /**
	 * the constructor of the class
	 * @param jwidgetEdition the jwidget edition object
	 */
    public GeneralConfigPanel(JWidgetEdition jwidgetEdition) {
        this.jwidgetEdition = jwidgetEdition;
        build();
    }

    /**
	 * builds the panel
	 */
    protected void build() {
        startPanel = new StartPanel(jwidgetEdition);
        realTimePanel = new RealTimePanel(jwidgetEdition);
        historyPanel = new HistoryPanel(jwidgetEdition);
        interpolationPanel = new InterpolationPanel(jwidgetEdition);
        GridBagLayout gridBag = new GridBagLayout();
        setLayout(gridBag);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 0, 0);
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        gridBag.setConstraints(startPanel, c);
        add(startPanel);
        c.gridx = 1;
        c.gridheight = 2;
        gridBag.setConstraints(historyPanel, c);
        add(historyPanel);
        c.gridx = 0;
        c.gridy = 1;
        c.gridheight = 1;
        gridBag.setConstraints(interpolationPanel, c);
        add(interpolationPanel);
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        gridBag.setConstraints(realTimePanel, c);
        add(realTimePanel);
    }

    /**
	 * initializes the panel
	 * @param element an element
	 */
    public void initialize(Element element) {
        startPanel.initialize(element);
        realTimePanel.initialize(element);
        historyPanel.initialize(element);
        interpolationPanel.initialize(element);
    }
}

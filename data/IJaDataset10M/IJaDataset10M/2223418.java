package de.sambalmueslie.ds.helptool.gui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import de.sambalmueslie.ds.helptool.DSHelpTool;
import de.sambalmueslie.ds.helptool.gui.components.VillageOverview;
import de.sambalmueslie.ds.helptool.parser.OwnUnitsAndVillagesParser;

/**
 * panel to input the own troops.
 * 
 * @author Sambalmueslie
 * 
 * @date 25.11.2008
 * 
 */
public class OwnTroopsAndVillagesInputPanel extends JPanel implements ActionPanel, ActionListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4583169834470258413L;

    /**
	 * constructor.
	 */
    public OwnTroopsAndVillagesInputPanel() {
        setLayout(new BorderLayout());
        add(getHeadlinePanel(), BorderLayout.NORTH);
        add(getMainPanel(), BorderLayout.CENTER);
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
    @Override
    public void actionPerformed(final ActionEvent e) {
        final String action = e.getActionCommand();
        if (action.equals("parse")) {
            getParser().parseText(getInputArea().getText());
        } else if (action.equals("clear")) {
            getInputArea().setText("");
        }
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see de.sambalmueslie.ds.helptool.gui.panels.ActionPanel#getIcon()
	 */
    @Override
    public Icon getIcon() {
        return new ImageIcon("img/icons/address-book-new.png");
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see de.sambalmueslie.ds.helptool.gui.panels.ActionPanel#getId()
	 */
    @Override
    public String getId() {
        return OwnTroopsAndVillagesInputPanel.class.getSimpleName();
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see de.sambalmueslie.ds.helptool.gui.panels.ActionPanel#getPanel()
	 */
    @Override
    public JPanel getPanel() {
        return this;
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see de.sambalmueslie.ds.helptool.gui.panels.ActionPanel#getTooltipText()
	 */
    @Override
    public String getTooltipText() {
        return "Add your own troops and villages!";
    }

    /**
	 * getter for the village overview.
	 * 
	 * @return the overview
	 */
    public VillageOverview getVillageOverview() {
        if (villageOverview == null) {
            villageOverview = new VillageOverview(DSHelpTool.getModel().getOwnVillagesModel());
        }
        return villageOverview;
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see de.sambalmueslie.ds.helptool.gui.panels.ActionPanel#initialise()
	 */
    @Override
    public void initialise() {
    }

    /**
	 * getter for the control panel.
	 * 
	 * @return the panel
	 */
    private JPanel getControlPanel() {
        if (controlPanel == null) {
            controlPanel = new JPanel();
            controlPanel.setLayout(new BorderLayout());
            controlPanel.add(getVillageOverview(), BorderLayout.CENTER);
        }
        return controlPanel;
    }

    /**
	 * getter for the headline panel.
	 * 
	 * @return the panel
	 */
    private HeadlinePanel getHeadlinePanel() {
        if (headlinePanel == null) {
            headlinePanel = new HeadlinePanel();
            headlinePanel.setHeadlineText("Add your own troops and villages");
        }
        return headlinePanel;
    }

    /**
	 * getter for the input text area.
	 * 
	 * @return the text are
	 */
    private JTextArea getInputArea() {
        if (inputArea == null) {
            inputArea = new JTextArea(5, 35);
        }
        return inputArea;
    }

    /**
	 * getter for the input clear button.
	 * 
	 * @return the button
	 */
    private JButton getInputClearButton() {
        if (inputClear == null) {
            inputClear = new JButton("Clear");
            inputClear.setActionCommand("clear");
            inputClear.addActionListener(this);
        }
        return inputClear;
    }

    /**
	 * getter for the input control panel.
	 * 
	 * @return the panel
	 */
    private JPanel getInputControlPanel() {
        if (inputControlPanel == null) {
            inputControlPanel = new JPanel();
            inputControlPanel.setLayout(new BoxLayout(inputControlPanel, BoxLayout.LINE_AXIS));
            inputControlPanel.add(Box.createHorizontalGlue());
            inputControlPanel.add(getInputParseButton());
            inputControlPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            inputControlPanel.add(getInputClearButton());
            inputControlPanel.add(Box.createHorizontalGlue());
        }
        return inputControlPanel;
    }

    /**
	 * getter for the input panel.
	 * 
	 * @return the panel
	 */
    private JPanel getInputPanel() {
        if (inputPanel == null) {
            inputPanel = new JPanel();
            inputPanel.setLayout(new BorderLayout());
            JScrollPane input = new JScrollPane(getInputArea());
            input.setPreferredSize(new Dimension(350, 500));
            inputPanel.add(input, BorderLayout.CENTER);
            inputPanel.add(getInputControlPanel(), BorderLayout.SOUTH);
        }
        return inputPanel;
    }

    /**
	 * getter for the input parse button.
	 * 
	 * @return the button
	 */
    private JButton getInputParseButton() {
        if (inputParse == null) {
            inputParse = new JButton("Parse");
            inputParse.setActionCommand("parse");
            inputParse.addActionListener(this);
        }
        return inputParse;
    }

    /**
	 * getter for the main panel.
	 * 
	 * @return the panel
	 */
    private JPanel getMainPanel() {
        if (mainPanel == null) {
            mainPanel = new JPanel();
            mainPanel.setLayout(new GridLayout(1, 2, 5, 5));
            mainPanel.add(getInputPanel());
            mainPanel.add(getControlPanel());
        }
        return mainPanel;
    }

    /**
	 * getter for the own units and villages parser.
	 * 
	 * @return the parser
	 */
    private OwnUnitsAndVillagesParser getParser() {
        if (parser == null) {
            parser = new OwnUnitsAndVillagesParser();
        }
        return parser;
    }

    /** the control panel. */
    private JPanel controlPanel = null;

    /** the headline panel. */
    private HeadlinePanel headlinePanel = null;

    /** the input text area. */
    private JTextArea inputArea = null;

    /** the input clear button . */
    private JButton inputClear = null;

    /** the input control panel. */
    private JPanel inputControlPanel = null;

    /** the input panel. */
    private JPanel inputPanel = null;

    /** the parse input button. */
    private JButton inputParse = null;

    /** the main panel. */
    private JPanel mainPanel = null;

    /** the parser for the own units and villages. */
    private OwnUnitsAndVillagesParser parser = null;

    /** the village overview. */
    private VillageOverview villageOverview = null;
}

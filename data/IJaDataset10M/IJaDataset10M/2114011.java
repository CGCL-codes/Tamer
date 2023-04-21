package se.kth.speech.skatta.designer;

import net.miginfocom.swing.MigLayout;
import se.kth.speech.skatta.util.ExtendedElement;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A form for setting up the basic details for tests. For more details see
 * chapter 4.2 in the design document.
 */
public class BasicsForm extends Form implements ActionListener {

    /**
     *
     */
    private static final long serialVersionUID = -2336445033883344621L;

    private JTextField m_nameField, m_headerField, m_passwordField, m_resultsField, m_stimuliField, m_pauseMessageField;

    private JTextField m_upField, m_downField, m_nextField, m_previousField, m_startField, m_endField;

    private JRadioButton m_standardButton, m_ddButton, m_syncButton;

    private JRadioButton m_pausePagesButton, m_pauseMinutesButton;

    private JSpinner m_pauseDelayField;

    public BasicsForm(Designer designer) {
        super(designer);
        setName("Basics");
        m_nameField = new JTextField("My test");
        m_headerField = new JTextField("$n - ($#/$t)");
        m_passwordField = new JTextField();
        m_resultsField = new JTextField();
        m_stimuliField = new JTextField();
        m_pauseMessageField = new JTextField();
        m_upField = new JTextField("Up");
        m_downField = new JTextField("Down");
        m_nextField = new JTextField("Next page");
        m_previousField = new JTextField("Previous page");
        m_startField = new JTextField("Start the test");
        m_endField = new JTextField("End the test");
        m_standardButton = new JRadioButton("Standard Test", true);
        m_ddButton = new JRadioButton("Drag-and-Drop Test", false);
        m_syncButton = new JRadioButton("Timesync Test", false);
        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(m_standardButton);
        typeGroup.add(m_ddButton);
        typeGroup.add(m_syncButton);
        m_pauseMinutesButton = new JRadioButton("minutes", true);
        m_pausePagesButton = new JRadioButton("pages", false);
        ButtonGroup pauseGroup = new ButtonGroup();
        pauseGroup.add(m_pauseMinutesButton);
        pauseGroup.add(m_pausePagesButton);
        m_pauseDelayField = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
        JLabel pauseLabel = new JLabel("Pause every");
        JLabel pauseMessageLabel = new JLabel("With the message:");
        JButton nextButton = new JButton("Next");
        JButton cancelButton = new JButton("Cancel");
        m_nameField.setToolTipText("A unique identifier to separate this test from any other tests you may create. It will only appear in the result file.");
        m_headerField.setToolTipText("<html>The text that will appear at the top of every page during the main test.<br><br>$n - The name of the current stimulus.<br>$# - The page number.<br>$t - The total number of pages.<br>$$ - a dollar-sign</html>");
        m_passwordField.setToolTipText("The password required to end the test. If left blank only confirmation is asked when exiting the test.");
        m_resultsField.setToolTipText("This can be a folder or zip-file. The path can be absolute or relative to where you save this testfile.");
        m_stimuliField.setToolTipText("All files in this folder not considered hidden by the system running the test will be considered stimulus. The path can be absolute or relative to where you save this testfile.");
        m_pauseMessageField.setToolTipText("The message that will appear when it is time for a pause.");
        m_pauseDelayField.setToolTipText("The delay between pauses. Set to 0 to not have any pauses.");
        m_pauseMinutesButton.setToolTipText("The pauses will occur when changing to a new page after the time has passed.");
        m_pausePagesButton.setToolTipText("Revisiting old pages will not affect this trigger.");
        m_startField.setToolTipText("For going from the initial page to the main test.");
        m_endField.setToolTipText("For going from the main test to the final page, as well as to end the final page.");
        m_nextField.setToolTipText("For displaying the next stimulus.");
        m_previousField.setToolTipText("For displaying the previous stimulus.");
        m_upField.setToolTipText("For scrolling up when there are too many questions to fit on a single page.");
        m_downField.setToolTipText("For scrolling down when there are too many questions to fit on a single page.");
        JButton browseResults = new JButton("Browse");
        browseResults.setActionCommand("browseResults");
        browseResults.addActionListener(this);
        JButton browseStimuli = new JButton("Browse");
        browseStimuli.setActionCommand("browseStimuli");
        browseStimuli.addActionListener(this);
        setLayout(new MigLayout("fill, wrap 1", "[fill, grow]"));
        add(titleComponent(m_nameField, "Test name"));
        add(titleComponent(m_headerField, "Header"));
        add(titleComponent(m_passwordField, "Password"));
        JPanel typePanel = new JPanel(new MigLayout("fill, insets 0", "[fill]"));
        typePanel.add(m_standardButton);
        typePanel.add(m_ddButton);
        typePanel.add(m_syncButton);
        add(titleComponent(typePanel, "Test type"));
        JPanel pausePanel = new JPanel(new MigLayout("fill, nogrid, insets 0", "[fill]"));
        pausePanel.add(pauseLabel, "grow 0, sg label");
        pausePanel.add(m_pauseDelayField, "alignx left, w 50lp!");
        pausePanel.add(m_pauseMinutesButton, "grow 0");
        pausePanel.add(m_pausePagesButton, "wrap, grow 0, gapright push");
        pausePanel.add(pauseMessageLabel, "sg label");
        pausePanel.add(m_pauseMessageField, "growx");
        add(titleComponent(pausePanel, "Pauses"));
        JPanel resultsPanel = new JPanel(new MigLayout("fill, insets 0"));
        resultsPanel.add(m_resultsField, "growx");
        resultsPanel.add(browseResults);
        add(titleComponent(resultsPanel, "Results storage"));
        JPanel stimuliPanel = new JPanel(new MigLayout("fill, insets 0"));
        stimuliPanel.add(m_stimuliField, "growx");
        stimuliPanel.add(browseStimuli);
        add(titleComponent(stimuliPanel, "Stimuli source"));
        JPanel labelPanel = new JPanel(new MigLayout("fill, wrap 2, insets 0", "[sg, fill]"));
        labelPanel.add(m_startField);
        labelPanel.add(m_endField);
        labelPanel.add(m_nextField);
        labelPanel.add(m_previousField);
        labelPanel.add(m_upField);
        labelPanel.add(m_downField);
        add(titleComponent(labelPanel, "Button Labels"));
        nextButton.setActionCommand("next");
        cancelButton.setActionCommand("cancel");
        nextButton.addActionListener(this);
        cancelButton.addActionListener(this);
        add(cancelButton, "span, split, growx, sg");
        add(nextButton, "wrap, growx, sg");
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("cancel")) {
            m_designer.dispose();
        } else if (e.getActionCommand().equals("next")) {
            Form mainForm = m_designer.getForm(Designer.MAIN_FORM);
            if (m_standardButton.isSelected()) {
                if (!(mainForm instanceof StandardTestForm)) {
                    m_designer.setForm(Designer.MAIN_FORM, new StandardTestForm(m_designer));
                }
            } else if (m_ddButton.isSelected()) {
                if (!(mainForm instanceof DragAndDropTestForm)) {
                    m_designer.setForm(Designer.MAIN_FORM, new DragAndDropTestForm(m_designer));
                }
            } else if (m_syncButton.isSelected()) {
                if (!(mainForm instanceof TimesyncTestForm)) {
                    m_designer.setForm(Designer.MAIN_FORM, new TimesyncTestForm(m_designer));
                }
            }
            if (m_designer.getForm(Designer.INITIAL_FORM) == null) {
                m_designer.setForm(Designer.INITIAL_FORM, new InitialForm(m_designer));
            }
            m_designer.show(Designer.INITIAL_FORM);
        } else if (e.getActionCommand().equals("browseResults")) {
            JFileChooser jfc = new JFileChooser(".");
            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) m_resultsField.setText(jfc.getSelectedFile().getAbsolutePath());
        } else if (e.getActionCommand().equals("browseStimuli")) {
            JFileChooser jfc = new JFileChooser(".");
            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) m_stimuliField.setText(jfc.getSelectedFile().getAbsolutePath());
        }
    }

    public ExtendedElement save(ExtendedElement parent) {
        parent.setAttribute("name", m_nameField.getText());
        if (m_passwordField.getText().length() != 0) parent.setAttribute("password", m_passwordField.getText());
        ExtendedElement buttonsElement = parent.createChildWithName("buttons");
        buttonsElement.setAttribute("up", m_upField.getText());
        buttonsElement.setAttribute("down", m_downField.getText());
        buttonsElement.setAttribute("next", m_nextField.getText());
        buttonsElement.setAttribute("previous", m_previousField.getText());
        buttonsElement.setAttribute("start", m_startField.getText());
        buttonsElement.setAttribute("end", m_endField.getText());
        parent.createChildWithNameAndText("results", m_resultsField.getText());
        parent.createChildWithNameAndText("stimuli", m_stimuliField.getText());
        ExtendedElement mainElement;
        if (m_standardButton.isSelected()) mainElement = parent.getOrCreateChildWithName("standardTest"); else if (m_ddButton.isSelected()) mainElement = parent.getOrCreateChildWithName("dragdropTest"); else mainElement = parent.getOrCreateChildWithName("timesyncTest");
        mainElement.createChildWithNameAndText("header", m_headerField.getText());
        if (((Integer) m_pauseDelayField.getValue()).intValue() != 0) {
            ExtendedElement pauseElement = mainElement.createChildWithNameAndText("pause", m_pauseMessageField.getText());
            pauseElement.setAttribute("delay", ((Integer) m_pauseDelayField.getValue()).toString());
            pauseElement.setAttribute("unit", m_pauseMinutesButton.isSelected() ? "minutes" : "pages");
        }
        return parent;
    }

    public void load(ExtendedElement e) {
        m_nameField.setText(e.attribute("name"));
        m_passwordField.setText(e.attribute("password"));
        ExtendedElement buttons = e.child("buttons");
        m_upField.setText(buttons.attribute("up"));
        m_downField.setText(buttons.attribute("down"));
        m_nextField.setText(buttons.attribute("next"));
        m_previousField.setText(buttons.attribute("previous"));
        m_startField.setText(buttons.attribute("start"));
        m_endField.setText(buttons.attribute("end"));
        m_resultsField.setText(e.textChild("results"));
        m_stimuliField.setText(e.textChild("stimuli"));
        ExtendedElement main = e.child("standardTest");
        if (main != null) m_standardButton.setSelected(true); else {
            main = e.child("dragdropTest");
            if (main != null) m_ddButton.setSelected(true); else {
                main = e.child("timesyncTest");
                m_syncButton.setSelected(true);
            }
        }
        m_headerField.setText(main.textChild("header"));
        ExtendedElement pause = main.child("pause");
        if (pause != null) {
            m_pauseMessageField.setText(pause.text());
            m_pauseDelayField.setValue(new Integer(pause.intAttribute("delay")));
            if (pause.attribute("unit").equals("minutes")) m_pauseMinutesButton.setSelected(true); else m_pausePagesButton.setSelected(true);
        }
    }
}

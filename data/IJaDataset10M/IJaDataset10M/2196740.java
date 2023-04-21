package com.onyourmind.tra;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextComponent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.IOException;
import com.onyourmind.awt.GroupBoxPanel;
import com.onyourmind.awt.InputPagePanel;
import com.onyourmind.awt.OymFrame;

public class PanelTraMain extends InputPagePanel implements ConstantsTra {

    private static final long serialVersionUID = -3948346969098123550L;

    String[] buttonNames = { WINDOW_STRUCTURING, WINDOW_DATA_ASSESSMENTS, WINDOW_TORNADO_CHART, WINDOW_RISK_PROFILE_CHART, WINDOW_DATA_SUMMARY, WINDOW_RISK_MITIGATION_PLAN, WINDOW_NOTES };

    private static final int NUMTEXTFIELDS = 7;

    private static final int NUMCHECKBOXES = 3;

    private final int NUMBUTTONS = buttonNames.length;

    private static final int PROJECT_DESCRIPTION_INDEX = 6;

    public void initialize(OymFrame pFrame, String strWindowName) {
        super.initialize(pFrame, strWindowName);
        setLayout(new BorderLayout());
        textComponents = new TextComponent[NUMTEXTFIELDS];
        checkboxes = new Checkbox[NUMCHECKBOXES];
        buttons = new Button[NUMBUTTONS];
        addGroups();
        fillChoices();
    }

    public void fillChoices() {
    }

    public void onBack() {
        frame.selectWindow(WINDOW_START);
    }

    public String getProjectName() {
        return textComponents[0].getText();
    }

    public String getApplicationName() {
        return textComponents[1].getText();
    }

    public String getTargetDate() {
        return textComponents[2].getText();
    }

    public void addSpecsGroup(Container parent, GridBagLayout gridbag, GridBagConstraints c) {
        String[] textFieldLabels = { LABEL_PROJECT_CODE, LABEL_PROJECT_DESC, "Target Completion Date" };
        for (int i = 0; i < textFieldLabels.length; i++) {
            c.gridwidth = GridBagConstraints.RELATIVE;
            makeLabel(parent, textFieldLabels[i], gridbag, c);
            c.gridwidth = GridBagConstraints.REMAINDER;
            textComponents[i] = makeTextField(parent, gridbag, c);
        }
        c.gridwidth = GridBagConstraints.REMAINDER;
        checkboxes[0] = new Checkbox("Y2K Ready", false);
        gridbag.setConstraints(checkboxes[0], c);
        parent.add(checkboxes[0]);
        checkboxes[0].setVisible(SPECIAL_VERSION);
        checkboxes[0].addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                onY2KClick();
            }
        });
        c.gridwidth = GridBagConstraints.RELATIVE;
        makeLabel(parent, "Project Description", gridbag, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        textComponents[PROJECT_DESCRIPTION_INDEX] = makeTextArea(parent, "", 5, 20, gridbag, c);
        c.gridwidth = GridBagConstraints.RELATIVE;
        makeLabel(parent, "Time unit", gridbag, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        CheckboxGroup cbg = new CheckboxGroup();
        checkboxes[1] = makeCheckbox(parent, "Weeks", gridbag, c, cbg);
        checkboxes[1].setState(true);
        c.gridwidth = GridBagConstraints.RELATIVE;
        makeLabel(parent, "", gridbag, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        checkboxes[2] = makeCheckbox(parent, "Months", gridbag, c, cbg);
    }

    public String getTimeUnit() {
        String result = "months";
        if (checkboxes[1].getState()) {
            result = "weeks";
        }
        return result;
    }

    public double getDaysPerTimeUnit() {
        double result = 30.5;
        if (checkboxes[1].getState()) {
            result = 7.0;
        }
        return result;
    }

    public void addInfoGroup(Container parent, GridBagLayout gridbag, GridBagConstraints c) {
        String[] textFieldLabels = { "Workshop Participants", "Workshop Date", "Workshop Facilitators" };
        for (int i = 0; i < textFieldLabels.length; i++) {
            c.gridwidth = GridBagConstraints.RELATIVE;
            makeLabel(parent, textFieldLabels[i], gridbag, c);
            c.gridwidth = GridBagConstraints.REMAINDER;
            if (i == 1) textComponents[i + 3] = makeTextField(parent, gridbag, c); else textComponents[i + 3] = makeTextArea(parent, "", 5, 20, gridbag, c);
        }
        addKeyListeners();
    }

    public void addKeyListeners() {
        for (int i = 0; i < textComponents.length; i++) {
            textComponents[i].addKeyListener(new KeyAdapter() {

                public void keyTyped(KeyEvent e) {
                    frame.setModified(true);
                }
            });
        }
    }

    public void onChoiceSelection() {
        frame.setModified(true);
    }

    public void onY2KClick() {
    }

    public void addGroups() {
        Panel panelBig = new Panel();
        add("Center", panelBig);
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        panelBig.setFont(new Font("Dialog", Font.PLAIN, 12));
        panelBig.setLayout(new GridLayout(2, 2));
        panelBig.setBackground(Color.lightGray);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        GroupBoxPanel panel = new GroupBoxPanel("Specifications");
        c.gridwidth = GridBagConstraints.RELATIVE;
        gridbag.setConstraints(panel, c);
        panelBig.add(panel);
        panel.setLayout(gridbag);
        addSpecsGroup(panel, gridbag, c);
        panel = new GroupBoxPanel("Workshop Information");
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(panel, c);
        panelBig.add(panel);
        panel.setLayout(gridbag);
        addInfoGroup(panel, gridbag, c);
        panel = new GroupBoxPanel("Navigation");
        c.gridwidth = GridBagConstraints.RELATIVE;
        gridbag.setConstraints(panel, c);
        panelBig.add(panel);
        panel.setLayout(gridbag);
        c.gridwidth = GridBagConstraints.REMAINDER;
        for (int nButton = 0; nButton < buttonNames.length; nButton++) {
            buttons[nButton] = makeButton(panel, buttonNames[nButton], gridbag, c);
            addButtonListeners(buttons[nButton]);
        }
    }

    public void clearContents() {
        super.clearContents();
        fillChoices();
    }

    public void load(DataInputStream disCurrent) {
        clearContents();
        try {
            if (checkboxes != null) {
                checkboxes[0].setState(disCurrent.readBoolean());
                if (frame.getFileVersion() >= 101) {
                    checkboxes[1].setState(disCurrent.readBoolean());
                    checkboxes[2].setState(disCurrent.readBoolean());
                }
            }
            if (textComponents != null) {
                for (int i = 0; i < 3; i++) textComponents[i].setText(disCurrent.readUTF());
                if (frame.getFileVersion() >= START_VERSION_NUMBER) {
                    for (int i = 3; i < PROJECT_DESCRIPTION_INDEX; i++) textComponents[i].setText(disCurrent.readUTF());
                    if (frame.getFileVersion() >= 50) textComponents[PROJECT_DESCRIPTION_INDEX].setText(disCurrent.readUTF());
                }
            }
            if (choices != null) {
                for (int i = 0; i < choices.length; i++) {
                    int nItemCount = disCurrent.readInt();
                    for (int nItem = 0; nItem < nItemCount; nItem++) choices[i].addItem(disCurrent.readUTF());
                    choices[i].select(disCurrent.readUTF());
                }
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}

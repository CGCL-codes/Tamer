package examples;

import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.TitledBorder;
import fiswidgets.fisgui.*;
import fiswidgets.fisutils.*;

public class FisButtons extends FisBase implements ActionListener {

    FisButton button;

    FisFrame advFrame;

    FisAdvancedButton advButton;

    FisComboBox box1;

    FisTextField tfield;

    FisFileBrowser fbrowse;

    FisListBox lb;

    public FisButtons() {
        super();
        addToAbout("The FisButtons widget was created by Maureen McHugo, University of Pittsburgh.");
        setTitle("FisButtons");
        FisPanel panel = new FisPanel();
        button = new FisButton("FisButton", panel);
        button.addActionListener(this);
        advFrame = new FisFrame();
        FisPanel advPanel = new FisPanel("FisPanel");
        advFrame.addFisPanel(advPanel, 1, 1);
        FisCheckText ct = new FisCheckText("FisTextField", advPanel);
        advPanel.newLine();
        FisCheckBox cb = new FisCheckBox("FisCheckBox", advPanel);
        advButton = new FisAdvancedButton("FisAdvancedButton", panel, advFrame);
        addFisPanel(panel, 3, 1);
        newLine();
        addBreakLine();
        String[] choices;
        FisPanel boxpanel = new FisPanel();
        boxpanel.setPanelBorder(new TitledBorder("FisComboBox with FisFileBrowser"));
        choices = new String[] { "File", "Directory" };
        box1 = new FisComboBox(choices, boxpanel);
        box1.addActionListener(this);
        tfield = new FisTextField("FisTextField", boxpanel);
        fbrowse = new FisFileBrowser(boxpanel);
        fbrowse.attachTo(tfield);
        addFisPanel(boxpanel, 1, 1);
        newLine();
        boxpanel = new FisPanel();
        boxpanel.setPanelBorder(new TitledBorder("FisListBox with FisFileChooser"));
        String[] listitems;
        listitems = new String[5];
        for (int i = 0; i < 5; i++) listitems[i] = "List item " + (i + 1);
        lb = new FisListBox(200, listitems, boxpanel);
        FisFileChooser choose = new FisFileChooser(boxpanel);
        choose.attachTo(lb);
        boxpanel.newLine();
        addFisPanel(boxpanel, 1, 1);
        newLine();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) Dialogs.ShowMessageDialog(this, "Hello!", "FisButtonMessage");
        if (e.getSource() == box1) {
            if (box1.getSelection().equals("Directory")) fbrowse.DirectoriesOnly = true; else fbrowse.DirectoriesOnly = false;
        }
    }

    public static void main(String[] args) {
        FisButtons wt = new FisButtons();
        wt.pack();
        wt.setVisible(true);
    }
}

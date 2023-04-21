package com.datag.form.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import jwizardcomponent.JWizardComponents;
import jwizardcomponent.JWizardPanel;
import com.datag.form.frame.DataGenerator;
import com.datag.util.DataGeneratorUtility;
import cz.autel.dmi.HIGConstraints;
import cz.autel.dmi.HIGLayout;

/**
 * @author kimi
 * 
 */
@SuppressWarnings("serial")
public class EndWizardPanel extends JWizardPanel implements ActionListener {

    private JTextArea textArea;

    public EndWizardPanel(JWizardComponents arg0) {
        super(arg0);
        init();
    }

    public EndWizardPanel(JWizardComponents arg0, String arg1) {
        super(arg0, arg1);
        init();
    }

    private void init() {
        setLayout();
        setBehavior();
        setLookAndFeel();
    }

    private void setBehavior() {
        getWizardComponents().getFinishButton().addActionListener(this);
    }

    private void setLayout() {
        int width[] = { 10, 100, 10 };
        int height[] = { 10, 100, 10 };
        HIGLayout higLayout = new HIGLayout(width, height);
        HIGConstraints higConstraints = new HIGConstraints();
        higLayout.setRowWeight(2, 1);
        higLayout.setColumnWeight(2, 1);
        this.setLayout(higLayout);
        textArea = new JTextArea();
        this.add(new JScrollPane(textArea), higConstraints.rcwh(2, 2, 1, 1));
    }

    private void setLookAndFeel() {
        textArea.setTabSize(2);
        textArea.setEditable(false);
        textArea.setFont(DataGenerator.getInstance().getServiceContainer().getLookAndFeelService().getFontTextField());
        textArea.setBackground(DataGeneratorUtility.BACKGROUND_COLOR);
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public void back() {
        super.back();
        DataGeneratorUtility.setTitle(DataGenerator.getInstance(), getWizardComponents());
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(getWizardComponents().getFinishButton())) DataGenerator.getInstance().getDataGeneratorManager().save();
    }
}

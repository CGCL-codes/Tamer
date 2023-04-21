package com.elibera.ccs.panel.question;

import java.awt.FlowLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.elibera.ccs.app.MLEConfig;
import com.elibera.ccs.panel.HelperPanel;
import com.elibera.ccs.res.Msg;
import com.elibera.ccs.tagdata.DataAnswerRoot;
import com.elibera.ccs.util.HelperStd;

/**
 * @author meisi
 *
 */
public class PanelAnswerCheckBox extends PanelAnswerRoot {

    /**
	 * This is the default constructor
	 */
    public PanelAnswerCheckBox(DataAnswerRoot data, MLEConfig conf) {
        super(data, conf);
    }

    public void reset(DataAnswerRoot data, MLEConfig conf) {
        super.reset(data, conf);
        checkSolutionInit();
        ((JCheckBox) store1).setSelected(data.answerSolution.compareTo("1") == 0);
    }

    /**
		 * wird im Konstruktor aufgerufen, um die richtigkeit der Lösung zu checken
		 *
		 */
    protected void checkSolutionInit() {
        if (HelperStd.isEmpty(data.answerSolution)) data.answerSolution = "0";
    }

    /**
		 * This method initializes jPanel	
		 * 	
		 * @return javax.swing.JPanel	
		 */
    protected JPanel getSolutionPanel() {
        JLabel jLabel1 = new JLabel();
        jLabel1.setText(Msg.getString("PanelAnswerCheckBox.LABEL_CHECKBOX_SELECTED_FOR_ANSWER_TO_BE_CORRECT"));
        JPanel jPanel = new JPanel();
        HelperPanel.formatPanel(jPanel);
        jPanel.setLayout(new FlowLayout());
        jPanel.add(jLabel1, null);
        jPanel.add(getJCheckBox(), null);
        return jPanel;
    }

    /**
		 * This method initializes jCheckBox	
		 * 	
		 * @return javax.swing.JCheckBox	
		 */
    private JCheckBox getJCheckBox() {
        JCheckBox jCheckBoxSolutionCheck = new JCheckBox();
        HelperPanel.formatCheckBox(jCheckBoxSolutionCheck);
        if (data.answerSolution.compareTo("1") == 0) jCheckBoxSolutionCheck.setSelected(true);
        jCheckBoxSolutionCheck.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                JCheckBox jCheckBoxSolutionCheck = (JCheckBox) e.getSource();
                if (jCheckBoxSolutionCheck.isSelected()) data.answerSolution = "1"; else data.answerSolution = "0";
            }
        });
        this.store1 = jCheckBoxSolutionCheck;
        return jCheckBoxSolutionCheck;
    }

    /**
		 * ToolTip der beim TextFeld für die Punkte angezeigt wird
		 * @return
		 */
    protected String getToolTipPointsTextField() {
        return Msg.getString("PanelAnswerCheckBox.TOOLTIP_POINTS_FOR_WRONG_ANSWER");
    }
}

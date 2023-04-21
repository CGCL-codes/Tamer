package com.elibera.ccs.dialog;

import java.awt.ComponentOrientation;
import java.awt.Container;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.elibera.ccs.app.MLEConfig;
import com.elibera.ccs.panel.HelperPanel;
import com.elibera.ccs.panel.InterfaceDialogClose;
import com.elibera.ccs.panel.question.PanelNewSingleChoiceQuestion;
import com.elibera.ccs.panel.std.PanelRootFooter;
import com.elibera.ccs.panel.std.PanelRootHeader;
import com.elibera.ccs.res.Msg;

/**
 * @author meisi
 *
 */
public class DialogNewSingleChoiceQuestion extends JDialog implements InterfaceDialogClose {

    private static final long serialVersionUID = 100000000202L;

    private JScrollPane jScrollPane = null;

    private JPanel jPanel = null;

    private MLEConfig conf;

    static DialogNewSingleChoiceQuestion dialog;

    public static void showDialog(MLEConfig conf, boolean preload) {
        if (dialog == null) {
            dialog = new DialogNewSingleChoiceQuestion(conf);
        } else {
            if (preload) return;
            dialog.panelRootFooter.reset(dialog, conf);
            dialog.conf = conf;
            dialog.panelNewSingleChoiceQuestion.reset(conf, dialog);
        }
        if (preload) return;
        dialog.setVisible(true);
    }

    /**
	 * This is the default constructor
	 */
    private DialogNewSingleChoiceQuestion(MLEConfig conf) {
        super(JOptionPane.getFrameForComponent(conf.ep), Msg.getMsg("DIALOG_NEW_SINGLE_CHOICE_QUESTION_TITLE"), true);
        this.conf = conf;
        initialize();
        pack();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setContentPane(getJScrollPane());
    }

    public void setContentPane(Container contentPane) {
        HelperPanel.applyComponentOrientation(contentPane);
        super.setContentPane(contentPane);
    }

    /**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setViewportView(getJPanel());
        }
        return jScrollPane;
    }

    /**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel() {
        if (jPanel == null) {
            jPanel = new JPanel();
            HelperPanel.formatPanel(jPanel);
            jPanel.setLayout(new BoxLayout(getJPanel(), BoxLayout.Y_AXIS));
            jPanel.add(new PanelRootHeader(conf, "HELP_NEW_SINGLE_CHOICE_QUESTION_DIALOG", null));
            panelNewSingleChoiceQuestion = new PanelNewSingleChoiceQuestion(conf, this);
            jPanel.add(panelNewSingleChoiceQuestion);
            panelRootFooter = new PanelRootFooter(this, conf);
            jPanel.add(panelRootFooter);
        }
        return jPanel;
    }

    PanelRootFooter panelRootFooter;

    PanelNewSingleChoiceQuestion panelNewSingleChoiceQuestion;

    public void closeDialog() {
        setVisible(false);
    }
}

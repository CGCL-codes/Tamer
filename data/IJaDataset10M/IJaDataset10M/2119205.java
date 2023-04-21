package de.itar.exceptions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import de.itar.resources.Const;
import de.itar.resources.Constants;
import de.itar.swing.MainFrame;
import hm.core.utils.BooleanWrapper;
import hm.core.utils.SwingHelper;
import hm.core.utils.Base64.Base64utils;

public class ExceptionQuestionDialog extends JDialog implements ActionListener {

    private JCheckBox cbErneutFragen;

    private JButton btYes;

    private JButton btNo;

    private BooleanWrapper sendMail;

    private BooleanWrapper saveResult;

    public ExceptionQuestionDialog(BooleanWrapper sendMail, BooleanWrapper saveResult) {
        super(MainFrame.getInstance(), true);
        setTitle(Const.PROGRAMM_NAME_VERSION);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                cbErneutFragen.setSelected(false);
                actionPerformed(new ActionEvent(btNo, 4711, ""));
            }
        });
        this.saveResult = saveResult;
        this.sendMail = sendMail;
        cbErneutFragen = new JCheckBox(Const.getInstance().getProperty(Constants.DO_NOT_ASK_AGAIN));
        cbErneutFragen.addActionListener(this);
        btYes = new JButton(Const.getInstance().getProperty(Const.getInstance().getProperty(Constants.EX_QU_SEND)));
        btYes.addActionListener(this);
        btNo = new JButton(Const.getInstance().getProperty(Const.getInstance().getProperty(Constants.EX_QU_SEND_NO)));
        btNo.addActionListener(this);
        JPanel bottons = ButtonBarFactory.buildRightAlignedBar(btYes, btNo);
        JPanel pnlBericht = new JPanel();
        pnlBericht.add(new JLabel("<html>" + "<b>" + Const.getInstance().getProperty(Constants.EX_QU_SEND_ERROR) + "</b>" + "<br><br>" + Const.getInstance().getProperty(Constants.EX_QU_CONNECTION) + "<br><br>" + Const.getInstance().getProperty(Constants.EX_QU_INFO) + "</html>"));
        ImageIcon icon = Base64utils.getImageIcon(new Const(), Const.IMAGE_WORLD_C);
        FormLayout layout = new FormLayout("15dlu, p, 5dlu, p, 15dlu", "15dlu, t:p, 10dlu, p, 5dlu, p, 5dlu,");
        CellConstraints cc = new CellConstraints();
        PanelBuilder pb = new PanelBuilder(layout);
        pb.add(new JLabel(icon), cc.xy(2, 2));
        pb.add(pnlBericht, cc.xy(4, 2));
        pb.add(bottons, cc.xy(4, 4));
        pb.add(cbErneutFragen, cc.xyw(2, 6, 3));
        this.getContentPane().add(pb.getPanel());
        this.pack();
        this.setResizable(false);
        MainFrame frame = MainFrame.getInstance();
        if (frame != null && frame.isVisible()) {
            SwingHelper.centerInFrame(MainFrame.getInstance(), this);
        }
    }

    public void actionPerformed(ActionEvent e) {
        try {
            Object source = e.getSource();
            if (source == cbErneutFragen) {
                this.saveResult.setBooleanValue(cbErneutFragen.isSelected());
            } else {
                if (source == btYes) {
                    this.sendMail.setBooleanValue(true);
                } else if (source == btNo) {
                    this.sendMail.setBooleanValue(false);
                } else {
                    BiboExceptionHelper.showErrorDialog(new Exception("Interner Fehler"));
                }
                this.setVisible(false);
                this.dispose();
            }
        } catch (Exception ex) {
            BiboExceptionHelper.showErrorDialog(ex);
        }
    }
}

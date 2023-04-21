package genestudio.Controllers.Main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;

/**
 *
 * @author User
 */
public class ShowORFListener implements ActionListener {

    MainWindowCode mwcMain;

    public ShowORFListener(MainWindowCode main) {
        mwcMain = main;
    }

    public void actionPerformed(ActionEvent e) {
        mwcMain.getGml().setShowORF(((AbstractButton) e.getSource()).isSelected());
        mwcMain.LoadGML();
        mwcMain.jpmGraphics.setVisible(false);
    }
}

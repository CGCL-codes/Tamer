package com.tabuto.jsimlife.actions;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import com.tabuto.jsimlife.JSimLife;
import com.tabuto.jsimlife.Seed;
import com.tabuto.jsimlife.views.JSLNewSeedView;

/**
 * Class uses from JSLNewSeedView in order to add some seed into the Game
 * @author tabuto83
 * 
 * @see {@link JSimLife#addSeed(int, int, int, double)}
 * @see JSLNewSeedView
 *
 */
public class CreateNewSeedAction extends AbstractAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    JSLNewSeedView newSeedDialog;

    private ResourceBundle resource;

    public CreateNewSeedAction(JSLNewSeedView newseeddialog) {
        super();
        newSeedDialog = newseeddialog;
        resource = ResourceBundle.getBundle("StringAndLabels", newSeedDialog.Game.getConfiguration().getLocale());
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (checkValues()) {
            Seed newSeed = new Seed(newSeedDialog.Game.getDimension(), Integer.valueOf(newSeedDialog.fieldX.getText()), Integer.valueOf(newSeedDialog.fieldY.getText()), Integer.valueOf(newSeedDialog.fieldRadius.getText()), (double) Integer.valueOf(newSeedDialog.fieldDensity.getText()));
            ((JSimLife) newSeedDialog.Game).addSeed(newSeed);
            newSeedDialog.dispose();
        }
    }

    /**
	 * @return true if values are correctly written
	 */
    private boolean checkValues() {
        boolean flag = false;
        try {
            Integer.valueOf(newSeedDialog.fieldX.getText());
            Integer.valueOf(newSeedDialog.fieldY.getText());
            Integer.valueOf(newSeedDialog.fieldRadius.getText());
            Integer.valueOf(newSeedDialog.fieldDensity.getText());
            flag = true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(newSeedDialog, resource.getString("asd_valueMisformedDesc"), resource.getString("asd_valueMisformed"), JOptionPane.ERROR_MESSAGE);
        }
        if (Integer.valueOf(newSeedDialog.fieldX.getText()) >= 0 && Integer.valueOf(newSeedDialog.fieldX.getText()) <= newSeedDialog.Game.getDimension().getWidth() && Integer.valueOf(newSeedDialog.fieldY.getText()) >= 0 && Integer.valueOf(newSeedDialog.fieldY.getText()) <= newSeedDialog.Game.getDimension().getHeight()) flag = true; else {
            JOptionPane.showMessageDialog(newSeedDialog, resource.getString("asd_ValueOutOfRange"), resource.getString("asd_ValueOutOfRangeDesc"), JOptionPane.ERROR_MESSAGE);
            flag = false;
        }
        return flag;
    }
}

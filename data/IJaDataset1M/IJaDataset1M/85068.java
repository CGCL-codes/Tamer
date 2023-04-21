package org.geoforge.guillc.radiobutton;

import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.AbstractButton;
import org.geoforge.io.finder.GfrFactoryIconShared;
import org.geoforge.io.finder.GfrFndResImgIntGfr;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public abstract class RbnIcnFxAbs extends RbnIcnAbs implements MouseListener {

    protected RbnIcnFxAbs(String strText, int intSize, int inset, int intLocationText) {
        super(strText, GfrFactoryIconShared.s_getDefaultRadiobutton(intSize));
        this._initComponent_(intSize, inset, intLocationText);
        super.addMouseListener((MouseListener) this);
    }

    private void _initComponent_(int intSize, int inset, int intLocationText) {
        super.setPressedIcon(GfrFactoryIconShared.s_getPressedRadiobutton(intSize));
        super.setRolloverIcon(GfrFactoryIconShared.s_getRolloverRadiobutton(intSize));
        super.setRolloverSelectedIcon(GfrFactoryIconShared.s_getRolloverSelectedRadiobutton(intSize));
        super.setSelectedIcon(GfrFactoryIconShared.s_getSelectedRadiobutton(intSize));
        if (intLocationText == AbstractButton.RIGHT) super.setMargin(new Insets(0, inset, 0, inset)); else if (intLocationText == AbstractButton.TOP) {
            super.setMargin(new Insets(inset, 0, inset, 0));
            super.setVerticalTextPosition(AbstractButton.TOP);
            super.setHorizontalTextPosition(AbstractButton.CENTER);
        } else {
            System.err.println("DEV CODING ERROR: not yet implemented, intLocationText=" + intLocationText + ", exiting");
            System.exit(1);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
}

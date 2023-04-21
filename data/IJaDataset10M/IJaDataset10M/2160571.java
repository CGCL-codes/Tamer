package org.mbari.vcr.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.mbari.vcr.IVCR;
import org.mbari.vcr.IVCRTimecode;

/**
 * <p>
 * A frame that allows one to specify a timecode. This frame is popped up by the
 * VCRGotoButton and is used to specify a timecode for the vcr to seek to.
 * </p>
 * @author : $Author: hohonuuli $
 * @version : $Revision: 332 $
 */
public class VCRGotoFrame extends TimeCodeSelectionFrame {

    /**
     *
     */
    private static final long serialVersionUID = 6281710184676611749L;

    /**
	 * @uml.property  name="vcr"
	 * @uml.associationEnd  
	 */
    private IVCR vcr;

    /**
     * Constructs ...
     *
     */
    public VCRGotoFrame() {
        super();
    }

    /**
     * @return The ActionListener that is called when the OK button of this panel
     *          is clicked.
     */
    public ActionListener getOkActionListener() {
        if (okActionListener == null) {
            okActionListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (vcr != null) {
                        setVisible(false);
                        vcr.seekTimecode(timePanel.getTime());
                    }
                    ;
                }
            };
        }
        return okActionListener;
    }

    /**
	 * @return  The IVCR object that this class is registered to
	 * @uml.property  name="vcr"
	 */
    public org.mbari.vcr.IVCR getVcr() {
        return vcr;
    }

    /**
	 * Sets the IVCR object to register to and pass commands to.
	 * @param  vcr
	 * @uml.property  name="vcr"
	 */
    public void setVcr(org.mbari.vcr.IVCR vcr) {
        this.vcr = vcr;
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param b
     */
    public void setVisible(boolean b) {
        if (b) {
            final IVCR v = getVcr();
            final TimeSelectPanel tp = getTimePanel();
            if (v != null) {
                final IVCRTimecode tc = v.getVcrTimecode();
                tp.getHourWidget().setTime(tc.getHour());
                tp.getMinuteWidget().setTime(tc.getMinute());
                tp.getSecondWidget().setTime(tc.getSecond());
                tp.getFrameWidget().setTime(tc.getFrame());
            }
            tp.getHourWidget().getTextField().requestFocus();
        }
        super.setVisible(b);
    }
}

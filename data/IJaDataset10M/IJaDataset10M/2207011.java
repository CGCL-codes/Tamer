package org.mbari.vcr.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mbari.util.IObserver;
import org.mbari.vcr.IVCR;
import org.mbari.vcr.Timecode;
import org.mbari.vcr.VCRAdapter;

/**
 * <p>This panel provides VCR controls for the annotation GUI. To use this
 * panel as a standalone control run org.mbari.vims.annotation.bui.vcr.VCRFrame
 * </p>
 *
 * @author : $Author: hohonuuli $
 * @version : $Id: VCRPanel.java 332 2006-08-01 18:38:46Z hohonuuli $
 *
 */
public class VCRPanel extends JPanel {

    /**
     *
     */
    private static final Log log = LogFactory.getLog(VCRPanel.class);

    private static final long serialVersionUID = -3169420522894558598L;

    /**
	 * @uml.property  name="vcrButtonPanel"
	 * @uml.associationEnd  
	 */
    private VCRButtonPanel vcrButtonPanel;

    /**
	 * @uml.property  name="timeCodePanel"
	 * @uml.associationEnd  
	 */
    private JPanel timeCodePanel;

    /**
	 * @uml.property  name="timeCodeField"
	 * @uml.associationEnd  
	 */
    private JTextField timeCodeField;

    /**
	 * @uml.property  name="sliderPanel"
	 * @uml.associationEnd  
	 */
    private VCRShuttleSpeedPanel sliderPanel;

    /**
	 * @uml.property  name="vcr"
	 * @uml.associationEnd  
	 */
    private IVCR vcr;

    /**
     * Constructs ...
     *
     */
    public VCRPanel() {
        try {
            initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <p><!-- Method description --></p>
     *
     */
    public void disconnect() {
        vcr.disconnect();
        getTimeCodeField().setText("NO VCR");
    }

    /**
     * <p><!-- Method description --></p>
     *
     */
    public void finalize() {
        try {
            super.finalize();
            getVcr().disconnect();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     *     Access the current timecode from the VCR
     *
     *     @return A Timecode object. To get a string formatted in HH:MM:SS:FF
     *             use toString(). The parts of the timecode can be accessed
     *             individually using getHour(), getMInute(), getSecond(), and
     *             getFrame(). Null is returned if no Timecode object is returned
     */
    public Timecode getTimecode() {
        Timecode timecode = null;
        if (vcr != null) {
            timecode = vcr.getVcrTimecode().getTimecode();
        }
        return timecode;
    }

    /**
	 * Allow classes to programtically access the VCR
	 * @return
	 * @uml.property  name="vcr"
	 */
    public IVCR getVcr() {
        return vcr;
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @throws Exception
     */
    private void initialize() throws Exception {
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        layout.setHgap(10);
        setLayout(layout);
        add(getTimeCodePanel());
        add(getVcrButtonPanel());
        setVcr(new VCRAdapter());
    }

    /**
	 * Change the VCR used by the control panel
	 * @param  newVcr
	 * @uml.property  name="vcr"
	 */
    public void setVcr(IVCR newVcr) {
        getTimeCodeField().setText("NO VCR");
        if (vcr != null) {
            vcr.disconnect();
            vcr.getVcrTimecode().removeObserver((IObserver) getTimeCodeField());
        }
        if (newVcr != null) {
            vcr = newVcr;
            vcr.getVcrTimecode().addObserver((IObserver) getTimeCodeField());
            try {
                getTimeCodeField().setText(vcr.getVcrTimecode().toString());
            } catch (Exception e) {
                getTimeCodeField().setText("NO VCR");
                if (log.isInfoEnabled()) {
                    log.info("Unable to set the timecode value in the display.", e);
                }
            }
            getVcrButtonPanel().setVcr(vcr);
            getSliderPanel().setVcr(vcr);
            vcr.requestStatus();
        } else {
            getTimeCodeField().setText("NO VCR");
        }
    }

    /**
	 * @return  the vcrButtonPanel
	 * @uml.property  name="vcrButtonPanel"
	 */
    VCRButtonPanel getVcrButtonPanel() {
        if (vcrButtonPanel == null) {
            vcrButtonPanel = new VCRButtonPanel(getVcr());
            vcrButtonPanel.setSlider(getSliderPanel().getSlider());
        }
        return vcrButtonPanel;
    }

    /**
	 * @return  the timeCodePanel
	 * @uml.property  name="timeCodePanel"
	 */
    JPanel getTimeCodePanel() {
        if (timeCodePanel == null) {
            timeCodePanel = new JPanel();
            timeCodePanel.setMinimumSize(new Dimension(180, 100));
            timeCodePanel.setPreferredSize(new Dimension(180, 100));
            timeCodePanel.add(getTimeCodeField());
            timeCodePanel.add(getSliderPanel());
        }
        return timeCodePanel;
    }

    /**
	 * @return  the timeCodeField
	 * @uml.property  name="timeCodeField"
	 */
    JTextField getTimeCodeField() {
        if (timeCodeField == null) {
            timeCodeField = new TimeCodeField();
        }
        return timeCodeField;
    }

    /**
	 * @return  the sliderPanel
	 * @uml.property  name="sliderPanel"
	 */
    public VCRShuttleSpeedPanel getSliderPanel() {
        if (sliderPanel == null) {
            sliderPanel = new VCRShuttleSpeedPanel();
        }
        return sliderPanel;
    }
}

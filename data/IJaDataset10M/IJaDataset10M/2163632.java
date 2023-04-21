package gov.sns.apps.pta.view;

import gov.sns.apps.pta.MainApplication;
import gov.sns.apps.pta.MainDocument;
import gov.sns.apps.pta.daq.DaqController;
import gov.sns.apps.pta.daq.MeasurementData;
import gov.sns.apps.pta.tools.logging.IEventLogger;
import gov.sns.apps.pta.view.cmn.DeviceSelectorPanel;
import gov.sns.apps.pta.view.daq.DaqControlPanel;
import gov.sns.apps.pta.view.plt.LiveDaqDisplayPanel;
import gov.sns.apps.pta.view.plt.LiveDisplayBase.FORMAT;
import gov.sns.ca.ConnectionException;
import gov.sns.ca.GetException;
import gov.sns.tools.pvlogger.PvLoggerException;
import gov.sns.xal.smf.AcceleratorNode;
import gov.sns.xal.smf.impl.WireScanner;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * GUI for controlling the profile device data acquisition.
 *
 * @since  Jun 10, 2009
 * @author Christopher K. Allen
 */
public class DataAcquisitionView extends JPanel implements IDocView, DeviceSelectorPanel.IDeviceSelectionListener, DaqController.IDaqControllerListener {

    /**  Serialization version */
    private static final long serialVersionUID = 1L;

    /** The central data of the application */
    private final MainDocument docMain;

    /** The profile device selection panel */
    private DeviceSelectorPanel pnlDevSel;

    /** The data acquisition control panel */
    private DaqControlPanel pnlDaqCtrl;

    /** The acquisition data display panel */
    private LiveDaqDisplayPanel pnlAcqData;

    /**
     * Create a new <code>DataAcquisitionView</code> object
     * and attach it to the given main data document with
     * it main application window.
     *
     * @param docMain   document where the measurement data is stored
     *
     * @since     Jun 10, 2009
     * @author    Christopher K. Allen
     */
    public DataAcquisitionView(MainDocument docMain) {
        this.docMain = docMain;
        this.buildGuiComponents();
        this.layoutGui();
    }

    /**
     * Returns the main application data object.
     *
     * @return  the data document for the application
     *
     * @since  Jun 10, 2009
     * @author Christopher K. Allen
     */
    public MainDocument getMainDocument() {
        return docMain;
    }

    /**
     * Returns the application event lgrApp
     *
     * @return the event lgrApp for the main application
     * 
     * @since  Nov 14, 2009
     * @author Christopher K. Allen
     */
    public IEventLogger getLogger() {
        return MainApplication.getEventLogger();
    }

    /**
     * Responds to the scan initiated event from the
     * DAQ controller panel.
     *
     * 
     * @since  Nov 14, 2009
     * @author Christopher K. Allen
     *
     * @see gov.sns.apps.pta.daq.DaqController.IDaqControllerListener#scanInitiated(java.util.List)
     */
    @Override
    public void scanInitiated(List<AcceleratorNode> lstDevs) {
        if (lstDevs.size() == 0) return;
    }

    /**
     * Responds to the scan completed event from the
     * DAQ controller panel.
     *
     * @param lstDevs       list of all the acquisition devices that 
     *                      have completed successfully.
     *
     * 
     * @since  Nov 14, 2009
     * @author Christopher K. Allen
     */
    @Override
    public void scanCompleted(List<AcceleratorNode> lstDevs) {
        try {
            MeasurementData setMsmt = MeasurementData.acquire(lstDevs);
            this.getMainDocument().setMeasurementData(setMsmt);
        } catch (ConnectionException e) {
            this.getLogger().logException(getClass(), e, "Static Acquisition: Unable to connect to " + lstDevs);
        } catch (GetException e) {
            this.getLogger().logException(getClass(), e, "Static Acquisition: Missing or correct data in " + lstDevs);
        } catch (PvLoggerException e) {
            this.getLogger().logException(getClass(), e, "Unable to take PV logger snapshot for measurement" + lstDevs);
        }
    }

    /**
     * Nothing to do here.
     *
     * @since   Dec 11, 2009
     * @author  Christopher K. Allen
     *
     * @see gov.sns.apps.pta.daq.DaqController.IDaqControllerListener#scanActuatorsParked()
     */
    @Override
    public void scanActuatorsParked() {
    }

    /**
     * Responds to the scan aborted event from the
     * DACQ controller panel.
     *
     * 
     * @since  Nov 14, 2009
     * @author Christopher K. Allen
     */
    public void scanAborted() {
    }

    /**
     * Nothing to do here.
     * 
     * @since 	Apr 1, 2010
     * @author  Christopher K. Allen
     *
     * @see gov.sns.apps.pta.daq.DaqController.IDaqControllerListener#scanActuatorsStopped()
     */
    @Override
    public void scanActuatorsStopped() {
    }

    /**
     * Nothing done here.
     *
     * @since   Dec 2, 2009
     * @author  Christopher K. Allen
     *
     * @see gov.sns.apps.pta.daq.DaqController.IDaqControllerListener#scanDeviceFailure(AcceleratorNode)
     */
    @Override
    public void scanDeviceFailure(AcceleratorNode smfDev) {
    }

    /**
     * <p>
     * Forwards the selected devices to the DACQ controller.
     * </p>
     * <p>
     * I prefer to catch the device selection event at the top
     * level and forward it in order to performs any other action
     * that may be necessary in the future.
     * </p>
     *
     * @since   Nov 17, 2009
     * @author  Christopher K. Allen
     *
     * @see gov.sns.apps.pta.view.cmn.DeviceSelectorPanel.IDeviceSelectionListener#newDeviceSelection(java.util.List)
     */
    @Override
    public void newDeviceSelection(List<AcceleratorNode> lstDevs) {
        this.pnlDaqCtrl.setDaqDevices(lstDevs);
    }

    /**
     * Resets the accelerator in the device selection panel
     *  
     * @param docMain   main application document 
     *
     * @since   Jun 18, 2009
     * @author  Christopher K. Allen
     *
     * @see gov.sns.apps.pta.view.IDocView#updateAccelerator(gov.sns.apps.pta.MainDocument)
     */
    public void updateAccelerator(MainDocument docMain) {
        if (this.pnlDevSel != null) this.pnlDevSel.resetAccelerator(docMain.getAccelerator());
    }

    /**
     * <p>
     * Update the graph display.
     * </p>
     *
     * @since   Mar 1, 2010
     * @author  Christopher K. Allen
     *
     * @see gov.sns.apps.pta.view.IDocView#updateMeasurementData(gov.sns.apps.pta.MainDocument)
     */
    @Override
    public void updateMeasurementData(MainDocument docMain) {
        this.pnlAcqData.clearGraphs();
        this.pnlAcqData.displayRawData(docMain.getMeasurementData());
    }

    /**
     * Initializes all the components of the
     * GUI display.
     *
     * 
     * @since  Aug 19, 2009
     * @author Christopher K. Allen
     */
    @SuppressWarnings("unchecked")
    private void buildGuiComponents() {
        this.pnlDevSel = new DeviceSelectorPanel(this.docMain.getAccelerator(), WireScanner.class);
        this.pnlDevSel.setBorder(new TitledBorder("Select Profile Devices"));
        this.pnlDevSel.registerDeviceSelectedListener(this);
        this.pnlDaqCtrl = new DaqControlPanel(this.docMain);
        this.pnlAcqData = new LiveDaqDisplayPanel(FORMAT.MULTIGRAPH_HOR);
        MainApplication.getDaqController().registerControllerListener(this.pnlAcqData);
        this.pnlAcqData.setBorder(new TitledBorder("Current Data"));
        this.pnlAcqData.setLiveData(true);
        this.pnlAcqData.setLiveDataButton(true);
    }

    /**
     * Lays out and build the GUI using
     * the initialized components.
     *
     * 
     * @since  Aug 19, 2009
     * @author Christopher K. Allen
     */
    private void layoutGui() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbcLayout = new GridBagConstraints();
        gbcLayout.insets = new Insets(5, 5, 5, 5);
        ;
        gbcLayout.gridx = 0;
        gbcLayout.gridy = 0;
        gbcLayout.weightx = 0.3;
        gbcLayout.weighty = 0.01;
        gbcLayout.fill = GridBagConstraints.BOTH;
        gbcLayout.anchor = GridBagConstraints.CENTER;
        gbcLayout.gridwidth = 1;
        this.add(this.pnlDevSel, gbcLayout);
        gbcLayout.gridx = 1;
        gbcLayout.gridy = 0;
        gbcLayout.weightx = 0.3;
        gbcLayout.weighty = 0.;
        gbcLayout.fill = GridBagConstraints.BOTH;
        gbcLayout.anchor = GridBagConstraints.CENTER;
        gbcLayout.gridwidth = 1;
        this.add(this.pnlDaqCtrl, gbcLayout);
        gbcLayout.insets = new Insets(0, 0, 0, 0);
        gbcLayout.gridx = 0;
        gbcLayout.gridy = 1;
        gbcLayout.weightx = 0.1;
        gbcLayout.weighty = 0.1;
        gbcLayout.fill = GridBagConstraints.BOTH;
        gbcLayout.anchor = GridBagConstraints.CENTER;
        gbcLayout.gridwidth = 2;
        this.add(this.pnlAcqData, gbcLayout);
    }
}

package gov.sns.apps.pasta;

import java.util.*;
import java.net.*;
import gov.sns.ca.*;
import gov.sns.xal.smf.application.*;
import gov.sns.xal.smf.*;
import gov.sns.xal.smf.impl.*;
import gov.sns.xal.smf.impl.qualify.*;
import gov.sns.tools.scan.SecondEdition.*;

/**
 * This class contains the primary internal working objects of the 
 * pasta application. E.g. which parts of the accelerator are being used.
 *
 * @author  jdg
 */
public class PastaDocument extends AcceleratorDocument {

    /** the helper class to save and open documents to/from xml files */
    private SaveOpen saveOpen;

    /** the first BPM to use in gathering beam info */
    protected BPM BPM1;

    /** the second BPM to use in gathering beam info */
    protected BPM BPM2;

    /** The RF cavity to analyze */
    protected RfCavity theCavity;

    /** The BCM to validate with */
    protected CurrentMonitor theBCM;

    /** the cavity design amplitude (MV/m)
    * Note - since we iterate the design values during the matching proceedure,
    * this should be grabed before matching starts */
    protected double theDesignAmp;

    /** the cavity design phase (deg)*/
    protected double theDesignPhase;

    /** the collection of possible BPMs */
    protected Collection theBPMs;

    /** the collection of possible cavities */
    protected Collection theCavities;

    /** list of BCMs to use as validator */
    protected Collection theBCMs;

    /** the parametric scan variable (cavity amplitude) */
    private ScanVariable scanVariableParameter = null;

    /** the scan variable (cavity phase) */
    private ScanVariable scanVariable = null;

    /** container for the measured variables (BPM phases + amplitudes) */
    private Vector measuredValuesV;

    /** the measured quantities for the Scan */
    private MeasuredValue BPM1PhaseMV, BPM1AmpMV, BPM2PhaseMV, BPM2AmpMV;

    /** make a copy of the selctedSequence so other classes in this package can use it */
    protected AcceleratorSeq theSequence;

    /** container of scan information) */
    protected ScanStuff scanStuff;

    /** container of analysis information) */
    protected AnalysisStuff analysisStuff;

    /** an amount to shift the DTL phase by  for analysis, to avoid +-180 deg 
    * wrapping which complicates the analysis */
    protected double DTLPhaseOffset = 0.;

    /** an amount to shift the BPM phase difference by for analysis + measurement, 
    * to avoid +-180 deg  wrapping which complicates the analysis */
    protected double BPMPhaseDiffOffset = 0.;

    /** this array holds Booleans indicating whether or not to use the scan 
    * number corresponding to the array index (starts at 0) 
    * in the matching analysis */
    protected ArrayList useScanInMatch = new ArrayList();

    /** this is an arbitrary shift to add to the model calculated BPM phase difference. It should be 0 - use cauutiously (deg) */
    protected double fudgePhaseOffset = 0.;

    /** a flag to use the fudgePhaseOffset fudge factor as a matching variable */
    protected boolean varyFudgePhaseOffset = false;

    /** workaround to avoid jca context initialization exception */
    static {
        ChannelFactory.defaultFactory().init();
    }

    /** Create a new empty document */
    public PastaDocument() {
        scanStuff = new ScanStuff(this);
        analysisStuff = new AnalysisStuff(this);
        saveOpen = new SaveOpen(this);
    }

    /** 
     * Create a new document loaded from the URL file 
     * @param url The URL of the file to load into the new document.
     */
    public PastaDocument(java.net.URL url) {
        this();
        if (url == null) {
            return;
        } else {
            System.out.println("Opening document: " + url.toString());
            setSource(url);
        }
    }

    /**
     * Make a main window by instantiating the my custom window.  Set the text 
     * pane to use the textDocument variable as its document.
     */
    @Override
    public void makeMainWindow() {
        mainWindow = new PastaWindow(this);
        if (getSource() != null) saveOpen.readSetupFrom(getSource());
    }

    /**
     * Save the document to the specified URL.
     * @param url The URL to which the document should be saved.
     */
    @Override
    public void saveDocumentAs(URL url) {
        saveOpen.saveTo(url);
        setHasChanges(false);
    }

    /**
     * Convenience method for getting the main window cast to the proper subclass of XalWindow.
     * This allows me to avoid casting the window every time I reference it.
     * @return The main window cast to its dynamic runtime class
     */
    protected PastaWindow myWindow() {
        return (PastaWindow) mainWindow;
    }

    /**
     * Handle the accelerator changed event by displaying the elements of the 
     * accelerator in the main window.
     */
    @Override
    public void acceleratorChanged() {
        System.out.println("accelerator path: " + acceleratorFilePath);
    }

    /**
     * Handle the selected sequence changed event by displaying the elements of the 
     * selected sequence in the main window.
     */
    @Override
    public void selectedSequenceChanged() {
        Collection cavs2;
        theSequence = selectedSequence;
        if (selectedSequence == null) return;
        theBPMs = selectedSequence.getAllNodesOfType("BPM");
        theCavities = selectedSequence.getAllNodesOfType("rfcavity");
        KindQualifier kq = new KindQualifier("rfcavity");
        cavs2 = selectedSequence.getAllInclusiveNodesWithQualifier(kq);
        Iterator itr = cavs2.iterator();
        while (itr.hasNext()) theCavities.add(itr.next());
        AcceleratorSeqCombo seq2 = getAccelerator().getComboSequence("MEBT-DTL");
        theBCMs = seq2.getAllNodesOfType("BCM");
        myWindow().updateSelectionLists();
        analysisStuff.modelReady = false;
    }
}

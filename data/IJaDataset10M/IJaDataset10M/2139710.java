package gov.sns.apps.ringinjection;

import java.lang.*;
import java.util.*;
import java.net.*;
import java.io.*;
import java.io.File;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;
import javax.swing.event.*;
import gov.sns.xal.smf.application.*;
import gov.sns.xal.smf.*;
import gov.sns.xal.smf.impl.*;
import gov.sns.xal.smf.data.*;
import gov.sns.application.*;
import gov.sns.xal.model.*;
import gov.sns.ca.*;
import gov.sns.tools.*;
import gov.sns.tools.beam.*;
import gov.sns.tools.xml.*;
import gov.sns.tools.data.*;
import gov.sns.tools.messaging.*;
import java.util.HashMap;
import gov.sns.tools.xml.XmlDataAdaptor;
import gov.sns.xal.model.probe.traj.*;
import gov.sns.xal.model.probe.*;
import gov.sns.xal.model.xml.*;
import gov.sns.xal.model.scenario.*;
import gov.sns.tools.optimizer.*;

/**
 * GenDocument is a custom XALDocument for loss viewing application 
 *
 * @version   0.1 12/1/2003
 * @author  cp3
 * @author  Sarah Cousineau
 */
public class GenDocument extends AcceleratorDocument implements SettingListener, DataListener {

    /**
     * The document for the text pane in the main window.
     */
    protected PlainDocument textDocument;

    protected Lattice lattice = null;

    /** the name of the xml file containing the accelerator */
    protected String theProbeFile;

    /** Create a new empty document */
    public GenDocument() {
        this(null);
        BPMFactory();
    }

    /** 
     * Create a new document loaded from the URL file 
     * @param url The URL of the file to load into the new document.
     */
    public GenDocument(java.net.URL url) {
        setSource(url);
        if (url != null) {
            try {
                System.out.println("Opening document: " + url.toString());
                DataAdaptor documentAdaptor = XmlDataAdaptor.adaptorForUrl(url, false);
                update(documentAdaptor.childAdaptor("GenDocument"));
                setHasChanges(false);
            } catch (Exception exception) {
                exception.printStackTrace();
                displayError("Open Failed!", "Open failed due to an internal exception!", exception);
            }
        }
        if (url == null) return;
    }

    /**
     * Make a main window by instantiating the my custom window.  Set the text 
     * pane to use the textDocument variable as its document.
     */
    public void makeMainWindow() {
        mainWindow = new GenWindow(this);
    }

    /**
     * Convenience method for getting the main window cast to the proper 
     * subclass of XalWindow.  This allows me to avoid casting the window 
     * every time I reference it.
     * @return The main window cast to its dynamic runtime class
     */
    public GenWindow myWindow() {
        return (GenWindow) mainWindow;
    }

    /** 
     * Customize any special button commands.
     */
    protected void customizeCommands(Commander commander) {
    }

    /**
     * Save the document to the specified URL.
     * @url The URL to which the document should be saved.
     */
    public void saveDocumentAs(java.net.URL url) {
        try {
            XmlDataAdaptor documentAdaptor = XmlDataAdaptor.newEmptyDocumentAdaptor();
            documentAdaptor.writeNode(this);
            documentAdaptor.writeToUrl(url);
            setHasChanges(false);
        } catch (XmlDataAdaptor.WriteException exception) {
            exception.printStackTrace();
            displayError("Save Failed!", "Save failed due to an internal write exception!", exception);
        } catch (Exception exception) {
            exception.printStackTrace();
            displayError("Save Failed!", "Save failed due to an internal exception!", exception);
        }
    }

    /** 
     * dataLabel() provides the name used to identify the class in an 
     * external data source.
     * @return The tag for this data node.
     */
    public String dataLabel() {
        return "GenDocument";
    }

    /**
     * Instructs the receiver to update its data based on the given adaptor.
     * @param adaptor The data adaptor corresponding to this object's data 
     * node.
     */
    public void update(DataAdaptor adaptor) {
    }

    /**
     * When called this method indicates that a setting has changed in 
     * the source.
     * @param source The source whose setting has changed.
     */
    public void settingChanged(Object source) {
        setHasChanges(true);
    }

    /**
     * Instructs the receiver to write its data to the adaptor for external
     * storage.
     * @param adaptor The data adaptor corresponding to this object's data 
     * node.
     */
    public void write(DataAdaptor adaptor) {
    }

    /** The root locatin of xaldev directory **/
    public double[] inj_params = new double[4];

    private Accelerator accl = new Accelerator();

    public ArrayList bpmagents;

    public int nbpmagents;

    public void BPMFactory() {
        this.loadDefaultAccelerator();
        accl = this.getAccelerator();
        AcceleratorSeq ringseq = (AcceleratorSeq) accl.getRings().get(0);
        createAgents(ringseq);
        nbpmagents = bpmagents.size();
    }

    public ArrayList createAgents(AcceleratorSeq asequence) {
        ArrayList bpms = (ArrayList) asequence.getNodesOfType("BPM");
        bpmagents = new ArrayList();
        Iterator itr = bpms.iterator();
        while (itr.hasNext()) {
            BpmAgent agent = new BpmAgent(asequence, (BPM) itr.next());
            if (agent.isOkay()) bpmagents.add((BpmAgent) agent);
            System.out.println("This is BPM is " + agent.name() + " in sequence " + asequence + ", and status is " + agent.isOkay());
        }
        return bpmagents;
    }

    public void setInjSpot(double[] params) {
        inj_params = params;
    }

    public double[] getInjSpot() {
        return inj_params;
    }
}

package edu.indiana.extreme.xbaya.mylead.gui;

import edu.indiana.extreme.xbaya.XBayaConstants;
import edu.indiana.extreme.xbaya.XBayaEngine;
import edu.indiana.extreme.xbaya.component.ComponentException;
import edu.indiana.extreme.xbaya.graph.GraphException;
import edu.indiana.extreme.xbaya.gui.Cancelable;
import edu.indiana.extreme.xbaya.gui.ErrorMessages;
import edu.indiana.extreme.xbaya.gui.WaitDialog;
import edu.indiana.extreme.xbaya.mylead.MyLead;
import edu.indiana.extreme.xbaya.mylead.MyLeadException;
import edu.indiana.extreme.xbaya.wf.Workflow;
import edu.indiana.extreme.xbaya.workflow.WorkflowEngineException;
import edu.indiana.extreme.xbaya.xregistry.XRegistryAccesser;
import xsul5.MLogger;
import java.net.URI;
import java.net.URISyntaxException;
import javax.xml.namespace.QName;
import org.ogce.xregistry.utils.XRegistryClientException;

/**
 * @author Satoshi Shirasuna
 */
public class MyLeadLoader implements Cancelable {

    private static final MLogger logger = MLogger.getLogger();

    private XBayaEngine engine;

    private MyLead myLead;

    private Thread loadThread;

    private boolean canceled;

    private WaitDialog loadingDialog;

    /**
     * Constructs a MyLeadWorkflowLoader.
     * 
     * @param engine
     */
    public MyLeadLoader(XBayaEngine engine) {
        this(engine, engine.getMyLead());
    }

    /**
     * Constructs a MyLeadWorkflowLoader.
     * 
     * This method is used to load workflows from a specified location, not from
     * the user's default location.
     * 
     * @param client
     * @param connection
     */
    public MyLeadLoader(XBayaEngine client, MyLead connection) {
        this.engine = client;
        this.myLead = connection;
        this.loadingDialog = new WaitDialog(this, "Loading the Workflow.", "Loading the Workflow. " + "Please wait for a moment.", this.engine);
    }

    /**
     * @see edu.indiana.extreme.xbaya.gui.Cancelable#cancel()
     */
    public void cancel() {
        this.canceled = true;
        this.loadThread.interrupt();
    }

    /**
     * Loads the workflow.
     * 
     * @param resouceID
     * @param blocking
     *            true for blocking call, false for non-blocking call
     */
    public void load(final QName resouceID, boolean blocking) {
        this.canceled = false;
        this.loadThread = new Thread() {

            @Override
            public void run() {
                runInThread(new QName(XBayaConstants.LEAD_NS, resouceID.getLocalPart()));
            }
        };
        this.loadThread.start();
        this.loadingDialog.show();
        if (blocking) {
            try {
                this.loadThread.join();
            } catch (InterruptedException e) {
                logger.caught(e);
            }
        }
    }

    /**
     * @param resouceID
     */
    private void runInThread(QName resouceID) {
        try {
            Workflow workflow;
            XRegistryAccesser xregistryAccesser = new XRegistryAccesser(this.engine);
            workflow = xregistryAccesser.getWorkflow(resouceID);
            this.loadingDialog.hide();
            if (this.canceled) {
                return;
            }
            this.engine.setWorkflow(workflow);
        } catch (RuntimeException e) {
            if (this.canceled) {
                logger.caught(e);
            } else {
                this.engine.getErrorWindow().error(ErrorMessages.MYLEAD_LOAD_TEMPLATE_ERROR, e);
                this.loadingDialog.hide();
            }
        } catch (Error e) {
            this.engine.getErrorWindow().error(ErrorMessages.UNEXPECTED_ERROR, e);
            this.loadingDialog.hide();
        }
    }
}

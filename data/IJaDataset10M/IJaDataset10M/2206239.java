package com.fujitsu.arcon.njs.actions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.cert.CertificateException;
import org.unicore.AJOIdentifier;
import org.unicore.Ulogin;
import org.unicore.ajo.AbstractAction;
import org.unicore.ajo.AbstractJob;
import org.unicore.ajo.Control;
import org.unicore.ajo.Portfolio;
import org.unicore.idiomatic.StreamFile;
import org.unicore.outcome.AbstractActionStatus;
import org.unicore.outcome.AbstractJob_Outcome;
import org.unicore.outcome.Outcome;
import org.unicore.sets.OutcomeEnumeration;
import org.unicore.sets.PortfolioEnumeration;
import org.unicore.sets.PortfolioSet;
import org.unicore.upl.ConsignJob;
import org.unicore.upl.ConsignJobReply;
import org.unicore.upl.RetrieveOutcome;
import org.unicore.upl.RetrieveOutcomeAck;
import org.unicore.upl.RetrieveOutcomeReply;
import org.unicore.utility.AbstractActionIterator;
import org.unicore.utility.ConsignForm;
import org.unicore.utility.UserAttributesConverter;
import com.fujitsu.arcon.njs.KnownActionDB;
import com.fujitsu.arcon.njs.NJSGlobals;
import com.fujitsu.arcon.njs.XAJOHandler;
import com.fujitsu.arcon.njs.interfaces.AFT;
import com.fujitsu.arcon.njs.interfaces.AJORewriter;
import com.fujitsu.arcon.njs.interfaces.AJORewriterException;
import com.fujitsu.arcon.njs.interfaces.AltFT;
import com.fujitsu.arcon.njs.interfaces.NJSException;
import com.fujitsu.arcon.njs.interfaces.TSIUnavailableException;
import com.fujitsu.arcon.njs.reception.GatewayConnectionFactory;
import com.fujitsu.arcon.njs.reception.RemoteNJS;
import com.fujitsu.arcon.njs.reception.SSLCredentials;
import com.fujitsu.arcon.njs.reception.UPLConnection;

/**
 * The parent wrapper for AbstractJobs to be executed
 * on another NJS (e.g. child AJOs of an RAJO on this NJS)
 *
 * @author Sven van den Berghe, fujitsu
 *
 * @version $Revision: 1.5 $ $Date: 2006/06/01 12:42:10 $
 *
 **/
public class XKnownAction extends KnownActionImpl {

    private transient XAJOHandler xh;

    public boolean requiresUspace() {
        return true;
    }

    public boolean isInteresting() {
        return true;
    }

    public static class Factory extends KnownAction.Factory {

        public KnownAction create(AbstractAction a, Outcome o, ParentAction p, RootAJO r, KnownActionDB k) {
            return new XKnownAction(a, o, p, r, k);
        }
    }

    public XKnownAction(AbstractAction a, Outcome o, ParentAction p, RootAJO r, KnownActionDB k) {
        super(a, p, r, k);
        if (o == null) {
            outcome = new AbstractJob_Outcome((AbstractJob) a, AbstractActionStatus.PENDING);
        } else {
            outcome = (AbstractJob_Outcome) o;
        }
        setAAType("SUBAJO");
    }

    private static AFT.AFTFactory old_style_aft_factory;

    public static void setOldStyleAFT(AFT.AFTFactory a) {
        old_style_aft_factory = a;
    }

    private static AltFT.AFTFactory aft_factory;

    public static void setAFT(AltFT.AFTFactory a) {
        aft_factory = a;
    }

    public void set(XAJOHandler in_xh) {
        xh = in_xh;
    }

    private Outcome pre_consign_outcome;

    /**
	 * Overwrite the Outcome, retaining the Log
	 * (For remote AJOs returning)
	 *
	 **/
    public void setOutcome(AbstractJob_Outcome ajoo) {
        if (pre_consign_outcome != null) {
            outcome.setStatusHistory(pre_consign_outcome.getStatusHistory());
            outcome.getStatusHistory().merge(ajoo.getStatusHistory());
            outcome.setLog(pre_consign_outcome.getLog());
            outcome.getLog().merge(ajoo.getLog());
            ((AbstractJob_Outcome) outcome).setAnonOutcomes(ajoo.getAnonOutcomes());
        } else {
            super.setOutcome(ajoo);
        }
        if (ajoo.getStatus().isEquivalent(AbstractActionStatus.DONE)) {
            if (wasCancelled()) {
                setStatus(AbstractActionStatus.KILLED, "Cancelled on parent's Vsite");
            } else if (wasAborted()) {
                setStatus(AbstractActionStatus.KILLED, "Aborted on parent's Vsite");
            } else {
                setStatus(ajoo.getStatus(), ajoo.getReason());
            }
        } else {
        }
    }

    private boolean finishedRewriting = false;

    public void setFinishedRewriting() {
        finishedRewriting = true;
    }

    private boolean startedRewriting = false;

    private boolean isRewritten = false;

    /**
	 * Call from Memory on reloading to set AJO to read rewritten one.
	 */
    public void replaceAction(AbstractJob rewritten) {
        action = rewritten;
        finishedRewriting = true;
        startedRewriting = true;
        isRewritten = true;
    }

    /**
	 * The AJO rewriter has changed the AJO that this instance wraps. Need to change 
	 * our copy.
	 */
    public void replaceJob(AbstractJob job) {
        if (!finishedRewriting) {
            logError("Attempt to replace a job that hasn't been through the AJORewriter denied.");
            return;
        }
        if (!getAction().getId().equals(job.getId())) {
            logError("Attempt to replace a job denied due to an Identifier mismatch. Tried was <" + job.getName() + "/" + Integer.toHexString(job.getId().getValue()) + ">");
            return;
        }
        logEvent("AJO is replaced by AJORewriter");
        try {
            SSLCredentials signer = ((GatewayConnectionFactory) RemoteNJS.getConnectionFactory(job.getVsite())).getCreds();
            Ulogin endorser = new Ulogin(signer.getUlogin().getCertificateChain());
            AbstractActionIterator aai = new AbstractActionIterator(job);
            while (aai.hasNext()) {
                AbstractAction aa = aai.next();
                if (aa instanceof AbstractJob) {
                    AbstractJob candidate = (AbstractJob) aa;
                    if (candidate.getConsignForm() == null || candidate.getSignature() == null) {
                        candidate.setAJOEndorser(null);
                        if (candidate.getUserAttributes() != null) candidate.setUserAttributes(((AbstractJob) getAction()).getUserAttributes());
                        candidate.setAJOEndorser(endorser);
                    }
                }
            }
            synchronized (signer) {
                action = ConsignForm.convertTo(job, signer);
            }
            isRewritten = true;
            NJSGlobals.getMemory().remember(this);
        } catch (Exception e) {
            this.setStatus(AbstractActionStatus.FAILED_IN_EXECUTION, "Failed to replace job with the rewritten version: " + e.getMessage());
        }
    }

    /**
	 * The method that does the work. 
	 * 
	 **/
    public synchronized void process() {
        AbstractActionStatus state = getStatus();
        if (state.equals(AbstractActionStatus.KILLED)) {
            logTrace("Event aborting, because kill detected");
        } else if (state.equals(AbstractActionStatus.READY)) {
            AJORewriter rewriter = NJSGlobals.getAJORewriter();
            AbstractJob job = (AbstractJob) getAction();
            if (!startedRewriting) {
                startedRewriting = true;
                if (rewriter.mayNeedRewriting((AbstractJob) getAction())) {
                    logEvent("Calling AJO rewriter");
                    try {
                        NJSGlobals.getAJORewriter().rewrite((AbstractJob) getAction());
                    } catch (AJORewriterException are) {
                        setStatus(AbstractActionStatus.FAILED_IN_EXECUTION, "Exception while calling AJORewriter: " + are.getMessage());
                    }
                    return;
                } else {
                    finishedRewriting = true;
                }
            }
            if (finishedRewriting) {
                execute();
                if (getStatus().isEquivalent(AbstractActionStatus.CONSIGNED)) NJSGlobals.getMemory().rememberConsignedStatus(this);
                doNext();
            }
        } else if (state.isEquivalent(AbstractActionStatus.CONSIGNED)) {
            update();
            doNext();
        } else if (state.isEquivalent(AbstractActionStatus.HELD)) {
            if (logger.CHAT) logger.chat("XAJO event aborting, because hold detected");
        } else {
            logTrace("process() called for External AJO in unexpected state <" + state + ">");
        }
    }

    private long delay_length = 1000;

    private void doNext() {
        if (getStatus().isEquivalent(AbstractActionStatus.NOT_DONE)) {
            xh.addDelayed(this, delay_length);
            if (delay_length < 20 * 1000) {
                delay_length += 1000;
            } else if (delay_length < 300 * 1000) {
                delay_length += 10 * 1000;
            }
        }
    }

    public void updateStatus() {
        if (getStatus().isEquivalent(AbstractActionStatus.CONSIGNED)) {
            update();
        }
    }

    public void abort() throws com.fujitsu.arcon.njs.interfaces.NJSException {
        logTrace("Will act on ABORT after AJO has finished executing.");
    }

    public void cancel() throws com.fujitsu.arcon.njs.interfaces.NJSException {
        logTrace("Will act on CANCEL after AJO has finished executing.");
    }

    /**
	 * Overrides KnownActionImpl.done(); to signal the AJORewriter that rewritten AJOs
	 * are done.
	 *
	 */
    public void done() {
        super.done();
        if (isRewritten) {
            NJSGlobals.getAJORewriter().rewrittenJobFinished((AbstractJob) getAction(), getStatus());
        }
    }

    public void resume(Control why) {
        logTrace("Resuming polling loop for child AJO");
        process();
    }

    private void execute() {
        AbstractJob ajo = (AbstractJob) getAction();
        ConsignJob cj = new ConsignJob();
        cj.setTarget(ajo.getAJOId());
        if (ajo.getVsite() == null) {
            setStatus(AbstractActionStatus.FAILED_IN_CONSIGN, "No Vsite set");
            return;
        }
        cj.setVsite(ajo.getVsite());
        PortfolioSet portfolios_to_stream = null;
        try {
            Ulogin endorser = ajo.getAJOEndorser();
            if (endorser == null) {
                endorser = new Ulogin(getEndorser().getCertificateChain());
                ajo.setAJOEndorser(endorser);
            }
            cj.setEndorser(UserAttributesConverter.convert(ajo.getUserAttributes()));
            cj.setAJOEndorser(endorser);
            cj.setAJO(ajo.getConsignForm());
            cj.setSignature(ajo.getSignature());
            cj.setSSO(ajo.getSSO());
            cj.setPolling(true);
            if (ajo.getStreamed() != null) {
                com.fujitsu.arcon.njs.interfaces.AFT.AlternativeFileTransfer old_style_aft = old_style_aft_factory.get(ajo.getVsite());
                com.fujitsu.arcon.njs.interfaces.AltFT.AlternativeFileTransfer aft = aft_factory.get(ajo.getVsite());
                if ((old_style_aft != null || aft != null) && !(ajo instanceof StreamFile.AJO)) {
                    portfolios_to_stream = new PortfolioSet();
                    if (old_style_aft != null) getRoot().logConfig("Transferring files to <" + ajo.getVsite().getName() + "> using an (old style) alternative file transfer route <" + old_style_aft.getName() + ">");
                    if (aft != null) getRoot().logConfig("Transferring files to <" + ajo.getVsite().getName() + "> using an alternative file transfer route <" + aft.getName() + ">");
                    PortfolioEnumeration pe = ajo.getStreamed().elements();
                    while (pe.hasMoreElements()) {
                        Portfolio p = pe.nextElement();
                        String[] files;
                        try {
                            while (true) {
                                try {
                                    files = NJSGlobals.getUspaceManager().getPFContents(getRoot(), p.getId());
                                    break;
                                } catch (com.fujitsu.arcon.njs.interfaces.TSIUnavailableException tuex) {
                                    if (wasAborted()) {
                                        return;
                                    }
                                    logTrace(tuex.getMessage() + ". Trying again to get file names");
                                    try {
                                        wait(NJSGlobals.TSI_UNAVAILABLE_DELAY);
                                    } catch (Exception ex) {
                                    }
                                    ;
                                }
                            }
                            for (int i = 0; i < files.length; i++) {
                                getRoot().logConfig("Transferring file <" + files[i] + "> via an alternative file transfer");
                            }
                        } catch (Exception ex) {
                            logger.warning("Problems getting Portfolio contents for AFT <" + Integer.toHexString(ajo.getId().getValue()) + "/" + Integer.toHexString(p.getId().getValue()) + ">", ex);
                            getRoot().logConfig("Internal NJS problem getting Portfolio contents for <" + p.getName() + "> will try UPL");
                            continue;
                        }
                        logConfig("Transferring portfolio <" + p.getName() + "> via an alternative file transfer");
                        try {
                            if (aft != null) old_style_aft.fileTransferRequest(files, getIncarnatedUser(), ajo.getVsite(), ajo.getAJOId(), p.getId(), cj.getEndorser(), (byte[]) null, getOutcome()); else if (old_style_aft != null) aft.fileTransferRequest(files, getIncarnatedUser(), ajo.getVsite(), ajo.getAJOId(), p.getId(), ajo.getUserAttributes(), (byte[]) null, getOutcome());
                        } catch (com.fujitsu.arcon.njs.interfaces.TransferNotSupportedException tnsex) {
                            getRoot().logEvent("Alternative file transfer failed will try UPL. Reason: " + tnsex.getMessage());
                            portfolios_to_stream.add(p);
                        } catch (com.fujitsu.arcon.njs.interfaces.TransferFailedException tfex) {
                            getRoot().logEvent("Alternative file transfer failed will retry. Reason: " + tfex.getMessage());
                            try {
                                if (aft != null) old_style_aft.fileTransferRequest(files, getIncarnatedUser(), ajo.getVsite(), ajo.getAJOId(), p.getId(), cj.getEndorser(), (byte[]) null, getOutcome()); else if (old_style_aft != null) aft.fileTransferRequest(files, getIncarnatedUser(), ajo.getVsite(), ajo.getAJOId(), p.getId(), ajo.getUserAttributes(), (byte[]) null, getOutcome());
                            } catch (Exception ex) {
                                getRoot().logEvent("Retry of alternative file transfer failed will try UPL. Reason: " + ex.getMessage());
                                portfolios_to_stream.add(p);
                            }
                        } catch (Exception ex) {
                            getRoot().logEvent("Error during alternative file transfer will try UPL. Reason: " + ex.getMessage());
                            portfolios_to_stream.add(p);
                        }
                    }
                } else {
                    portfolios_to_stream = ajo.getStreamed();
                }
                cj.setStreamed(portfolios_to_stream.elements().hasMoreElements());
            } else {
                cj.setStreamed(false);
            }
        } catch (CertificateException ex) {
            setStatus(AbstractActionStatus.FAILED_IN_CONSIGN, "Problems with NJS certificate: " + ex.getMessage());
            return;
        }
        ConsignJobReply reply = null;
        Outcome o = getOutcome();
        if (NJSGlobals.getNJSUlogin() == null) {
            failedSerious("Consigning sub-AJO with NO NJS certificate set.", null);
            return;
        } else {
            cj.setConsignor(NJSGlobals.getNJSUlogin());
            logConfig("Consigning to <" + ajo.getVsite().getName() + ">");
            pre_consign_outcome = o;
            UPLConnection c = null;
            try {
                if (connection_factory == null) connection_factory = RemoteNJS.getConnectionFactory(ajo.getVsite());
                c = (UPLConnection) connection_factory.connect();
                reply = c.consignJob(cj, portfolios_to_stream, getRoot(), NJSGlobals.getMaxStreams());
                c.closeOK();
            } catch (NJSException.Soft ex) {
                if (c != null) c.closeOK();
                setStatus(AbstractActionStatus.FAILED_IN_CONSIGN, ex.getMessage());
                return;
            } catch (IOException e) {
                if (c != null) c.closeError();
                setStatus(AbstractActionStatus.FAILED_IN_CONSIGN, e.getMessage());
                return;
            } catch (NJSException e) {
                setStatus(AbstractActionStatus.FAILED_IN_CONSIGN, "Remote NJS error on consign: " + e.getMessage());
                if (c != null) c.closeOK();
                return;
            } catch (TSIUnavailableException e) {
                if (c != null) c.closeOK();
                setStatus(AbstractActionStatus.FAILED_IN_CONSIGN, "Consign error, locally: " + e.getMessage());
                return;
            } catch (Exception e) {
                setStatus(AbstractActionStatus.FAILED_IN_CONSIGN, "NJS error? : " + e.getMessage());
                logger.logError("Problems with AJO <" + ajo.getId().getName() + "/" + Integer.toHexString(ajo.getId().getValue()) + "> consign ", e);
                if (c != null) c.closeError();
                return;
            }
        }
        org.unicore.upl.UnicoreResponse[] ura = reply.getTrace();
        for (int i = 0; i < ura.length; i++) {
            o.getLog().add(ura[i].getComment() + " Response code: " + ura[i].getReturnCode() + "\n");
        }
        setStatus(AbstractActionStatus.CONSIGNED);
    }

    private boolean updating = false;

    public void update() {
        if (getStatus().isEquivalent(AbstractActionStatus.DONE)) {
            logTrace("XAJO already done, but update called. This is OK.");
            return;
        }
        AbstractJob ajo = (AbstractJob) getAction();
        if (updating) return;
        updating = true;
        logTrace("Updating status on <" + ajo.getVsite().getName() + ">");
        UPLConnection c = null;
        try {
            RetrieveOutcome ro = new RetrieveOutcome((AJOIdentifier) ajo.getId(), ajo.getVsite());
            ro.setConsignor(NJSGlobals.getNJSUlogin());
            ro.setVsite(ajo.getVsite());
            if (connection_factory == null) connection_factory = RemoteNJS.getConnectionFactory(((AbstractJob) getAction()).getVsite());
            c = (UPLConnection) connection_factory.connect();
            RetrieveOutcomeReply reply = c.retrieveOutcome(ro, this, NJSGlobals.getMaxStreams());
            c.closeOK();
            try {
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(reply.getOutcome()));
                setOutcome((AbstractJob_Outcome) ois.readObject());
            } catch (Exception ex) {
                failedSerious("Problems converting Outcome from remote NJS", ex);
            }
        } catch (NJSException.Soft ex) {
            if (c != null) c.closeOK();
            logger.warning("Updating external AJO on <" + ajo.getVsite().getName() + "> Status not refreshed, will try again: " + ex.getMessage());
            logTrace("Status on remote NJS not refreshed, will try again: " + ex.getMessage());
        } catch (NJSException ex) {
            if (c != null) c.closeOK();
            logger.warning("Error updating external AJO on <" + ajo.getVsite().getName() + "> Status not refreshed, will try again: " + ex.getMessage());
            logTrace("Status on remote NJS not refreshed, will try again: " + ex.getMessage());
        } catch (IOException ex) {
            if (c != null) c.closeError();
            logger.warning("IO error updating external AJO on <" + ajo.getVsite().getName() + "> Status not refreshed, will try again: " + ex.getMessage());
            logTrace("Status on remote NJS not refreshed, will try again: " + ex.getMessage());
        } catch (TSIUnavailableException ex) {
            if (c != null) c.closeOK();
            logger.warning("Local TSI busy, status not refreshed, will try again: " + ex.getMessage());
            logTrace("Status not refreshed (local TSI busy), will try again: " + ex.getMessage());
        } finally {
            updating = false;
        }
    }

    /**
	 * Send a RetrieveOutcomeACk to the remote Vsite to clean up completed job
	 *
	 **/
    public void doROA() {
        AbstractJob ajo = (AbstractJob) getAction();
        UPLConnection c = null;
        try {
            RetrieveOutcomeAck roa = new RetrieveOutcomeAck((AJOIdentifier) ajo.getId(), ajo.getVsite());
            roa.setConsignor(NJSGlobals.getNJSUlogin());
            roa.setVsite(ajo.getVsite());
            if (connection_factory == null) connection_factory = RemoteNJS.getConnectionFactory(((AbstractJob) getAction()).getVsite());
            c = (UPLConnection) connection_factory.connect();
            c.retrieveOutcomeAck(roa);
            c.closeOK();
        } catch (IOException ex) {
            if (c != null) c.closeError();
            logger.warning("Execution of RetrieveOutcomeAck failed for <" + Integer.toHexString(getId().getValue()) + "> on <" + ajo.getVsite() + ">", ex);
        } catch (NJSException ex) {
            if (c != null) c.closeOK();
            logger.warning("Execution of RetrieveOutcomeAck failed for <" + Integer.toHexString(getId().getValue()) + "> on <" + ajo.getVsite() + ">", ex);
        }
    }

    private UPLConnection.Factory connection_factory;

    public String getDetailedListing() {
        String result = super.getDetailedListing();
        try {
            AbstractJob xajo = (AbstractJob) action;
            result += "DEST VSITE:      " + xajo.getVsite() + "\n";
            if (xajo.getAJOEndorser() != null) {
                result += "DEST ENDORSER:   " + xajo.getAJOEndorser().getCertificate().getSubjectDN().getName() + "\n";
            }
            if (xajo.getUserAttributes() != null) {
                result += "DEST USER:       " + xajo.getUserAttributes().getUser().getCertificate().getSubjectDN().getName() + "\n";
                String temp = UserAttributesConverter.getXlogin(xajo.getUserAttributes());
                if (temp != null) result += "REQUESTED XLOGIN:" + temp + "\n";
                temp = UserAttributesConverter.getProject(xajo.getUserAttributes());
                if (temp != null) result += "REQUESTED PROJCT:" + temp + "\n";
            }
            if (isRewritten) result += "AJO has been rewritten.";
            if (xajo.getStreamed() != null) {
                PortfolioEnumeration pfe = xajo.getStreamed().elements();
                while (pfe.hasMoreElements()) {
                    result += "PF TO SEND:      " + Integer.toHexString(pfe.nextElement().getId().getValue());
                }
            }
        } catch (Exception ex) {
            result += "ERROR GETTING INFORMATION, INCOMPLETE FIELDS? " + ex + "\n";
        }
        return result;
    }

    public String getLongListing() {
        String result = super.getLongListing();
        if (getStatus().isEquivalent(AbstractActionStatus.DONE)) {
            result += "\nOUTCOME LOG FROM REMOTE EXECUTION: \n\n";
            try {
                AbstractJob_Outcome converted = org.unicore.utility.ConsignForm.convertFrom((AbstractJob_Outcome) outcome);
                result += printOutcome(converted);
            } catch (Exception ex) {
                result += "PROBLEMS DECODING OUTCOME: " + ex + "\n";
            }
        }
        return result;
    }

    private String printOutcome(AbstractJob_Outcome ao) {
        String result = "";
        try {
            OutcomeEnumeration oe = ao.getOutcomes();
            while (oe.hasMoreElements()) {
                Outcome o = oe.nextElement();
                result += "\n" + o.getId().getName() + "(" + Integer.toHexString(o.getId().getValue()) + ")" + o.getClass() + "\n";
                result += o.getLog();
                if (o instanceof AbstractJob_Outcome) {
                    result += "\n AJO OUTCOME " + o.getId().getName() + "\n";
                    result += printOutcome((AbstractJob_Outcome) o);
                    result += "\n END AJO OUTCOME " + o.getId().getName() + "\n";
                }
            }
        } catch (Exception ex) {
            result += "PROBLEMS PRINTING OUTCOME: " + ex + "\n";
        }
        return result;
    }
}

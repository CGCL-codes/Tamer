package es.caib.bpm.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.security.Principal;
import java.security.cert.CertificateEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Include;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import es.caib.bpm.attachment.TaskAttachmentManager;
import es.caib.bpm.beans.exception.DocumentBeanException;
import es.caib.bpm.beans.remote.Document;
import es.caib.bpm.nas.exception.NASException;
import es.caib.bpm.toolkit.SignaturaHandler;
import es.caib.bpm.toolkit.WorkflowWindow;
import es.caib.bpm.toolkit.exception.SystemWorkflowException;
import es.caib.bpm.toolkit.exception.UserWorkflowException;
import es.caib.bpm.toolkit.exception.WorkflowException;
import es.caib.signatura.api.ParsedCertificate;
import es.caib.signatura.api.Signature;
import es.caib.zkib.zkiblaf.SignApplet;

public class SignatureManager implements SignaturaHandler {

    private WorkflowWindow window;

    public SignatureManager(WorkflowWindow window) {
        super();
        this.window = window;
    }

    public void sign(String tag) throws WorkflowException {
        try {
            TaskAttachmentManager am = new TaskAttachmentManager(window.getTask());
            Document doc = am.getDocument(tag);
            if (doc == null) throw new SystemWorkflowException(Labels.getLabel("error.msgFirma3") + " " + tag);
            for (Iterator it = doc.getSigns().iterator(); it.hasNext(); ) {
                Signature sig = (Signature) it.next();
                if (verifyAuthor(sig)) return;
            }
            doSign(am, doc);
        } catch (NamingException e) {
            throw new SystemWorkflowException(e);
        } catch (RemoteException e) {
            throw new SystemWorkflowException(e);
        } catch (CreateException e) {
            throw new SystemWorkflowException(e);
        } catch (NASException e) {
            throw new SystemWorkflowException(e);
        } catch (IOException e) {
            throw new SystemWorkflowException(e);
        } catch (SuspendNotAllowedException e) {
            throw new SystemWorkflowException(e);
        } catch (DocumentBeanException e) {
            throw new SystemWorkflowException(e);
        } catch (InterruptedException e) {
            throw new SystemWorkflowException(e);
        } catch (CertificateEncodingException e) {
            throw new SystemWorkflowException(e);
        }
    }

    /**
	 * Metodo para implementar seguridad.
	 */
    private boolean verifyAuthor(Signature sig) throws NamingException, CertificateEncodingException, IOException {
        Principal p = (Principal) window.getDesktop().getExecution().getUserPrincipal();
        if (p != null) return true; else return false;
    }

    private void doSign(TaskAttachmentManager am, Document doc) throws RemoteException, NASException, IOException, DocumentBeanException, SuspendNotAllowedException, InterruptedException, UserWorkflowException, NamingException, CertificateEncodingException {
        doc.openDownloadTransfer();
        Desktop desktop = window.getDesktop();
        String realPath = desktop.getWebApp().getRealPath("");
        String id = ((HttpSession) desktop.getSession().getNativeSession()).getId() + "-" + new Date().getTime();
        File fileGenerado = new File(realPath + "/files/" + id + ".xml");
        FileOutputStream stream = new FileOutputStream(fileGenerado);
        byte[] pack = doc.nextDownloadPackage(8192);
        while (pack != null) {
            stream.write(pack);
            pack = doc.nextDownloadPackage(8192);
        }
        stream.close();
        HttpServletRequest request = (HttpServletRequest) window.getDesktop().getExecution().getNativeRequest();
        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        url += "/files/" + URLEncoder.encode(id, "UTF-8") + ".xml";
        Window windowFirma = (Window) Path.getComponent("//sign/window");
        Events.postEvent(new Event("onSetSource", windowFirma, url));
        windowFirma.doModal();
        fileGenerado.delete();
        Signature signatura = (Signature) windowFirma.getAttribute("signature");
        if (signatura == null) throw new UserWorkflowException(Labels.getLabel("error.msgFirma1"));
        if (!verifyAuthor(signatura)) throw new UserWorkflowException(Labels.getLabel("error.msgFirma2") + " " + desktop.getExecution().getUserPrincipal().getName());
        doc.addSign(signatura);
    }
}

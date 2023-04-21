package net.sf.dpdesktop.module.util.ssl;

import com.google.inject.Inject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.dpdesktop.gui.CertificateDialog;
import net.sf.dpdesktop.module.guistate.ApplicationStateListener;
import net.sf.dpdesktop.module.guistate.ApplicationStateModel;
import net.sf.dpdesktop.module.guistate.TraySupportEnum;
import net.sf.dpdesktop.module.message.MessageModel;
import net.sf.dpdesktop.module.settings.LanguageModel;
import net.sf.dpdesktop.service.ContextProvider;
import net.sf.dpdesktop.service.ServiceAuthException;
import net.sf.dpdesktop.service.ServiceDataException;
import org.dom4j.DocumentException;

/**
 *
 * @author Heiner Reinhardt
 */
public class SSLController implements ApplicationStateListener {

    private final SSLModel sslModel;

    private final CertificateDialog certificateDialog;

    private final MessageModel messageModel;

    private final ContextProvider contextProvider;

    private final LanguageModel languageModel;

    @Inject
    public SSLController(SSLModel sslModel, CertificateDialog certificateDialog, MessageModel messageModel, ContextProvider contextProvider, ApplicationStateModel applicationStateModel, LanguageModel languageModel) {
        this.sslModel = sslModel;
        this.certificateDialog = certificateDialog;
        this.messageModel = messageModel;
        this.contextProvider = contextProvider;
        this.languageModel = languageModel;
        try {
            sslModel.load(contextProvider.getContextFile());
        } catch (ServiceAuthException ex) {
        } catch (MalformedURLException ex) {
        } catch (IOException ex) {
        } catch (DocumentException ex) {
            sslModel.clean(contextProvider.getContextFile());
        }
        applicationStateModel.addApplicationStateListener(this);
    }

    public boolean isCertificateAllowed(String hostname, X509Certificate x509Certificate) {
        boolean isAllowed = false;
        try {
            isAllowed = sslModel.isCertificateAllowed(hostname, x509Certificate);
        } catch (ServiceDataException ex) {
            switch(certificateDialog.isAllowed(hostname, x509Certificate)) {
                case YES_EVERYTIME:
                    sslModel.setCertificateAllowed(hostname, x509Certificate, true, true);
                    isAllowed = true;
                    break;
                case YES_ONCE:
                    sslModel.setCertificateAllowed(hostname, x509Certificate, true, false);
                    isAllowed = true;
                    break;
                case NO_EVERYTIME:
                    sslModel.setCertificateAllowed(hostname, x509Certificate, false, true);
                    break;
                case NO_ONCE:
                    sslModel.setCertificateAllowed(hostname, x509Certificate, false, false);
                    break;
            }
        }
        return isAllowed;
    }

    public boolean isUntrustedConnectionAllowed(String hostname) {
        return messageModel.question(languageModel.getString("SSLController.message.untrustedConnection.headline"), languageModel.getString("SSLController.message.untrustedConnection.text").replace("%HOST%", hostname));
    }

    @Override
    public void maximize() {
    }

    @Override
    public void minimize() {
    }

    @Override
    public void exiting() {
        try {
            sslModel.store(contextProvider.getContextFile());
        } catch (MalformedURLException ex) {
        } catch (ServiceAuthException ex) {
        } catch (IOException ex) {
        }
    }

    @Override
    public TraySupportEnum isTraySupported() {
        return TraySupportEnum.BOTH;
    }
}

package net.sf.poormans.gui.listener;

import net.sf.poormans.configuration.InitializationManager;
import net.sf.poormans.configuration.resource.LabelHolder;
import net.sf.poormans.exception.ProgressException;
import net.sf.poormans.gui.dialog.DialogManager;
import net.sf.poormans.model.domain.pojo.Site;
import net.sf.poormans.model.tool.Upload;
import net.sf.poormans.tool.DESCryptor;
import net.sf.poormans.tool.connection.ConnectionAuthentificationException;
import net.sf.poormans.tool.connection.ConnectionException;
import net.sf.poormans.tool.connection.ConnectionFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * Listener that triggers a site transfer without triggering an export.
 *
 * @version $Id: ListenerUploadSiteWithoutExport.java 2141 2011-08-11 19:10:34Z th-schwarz $
 * @author <a href="mailto:th-schwarz@users.sourceforge.net">Thilo Schwarz</a>
 */
public class ListenerUploadSiteWithoutExport implements SelectionListener {

    private Site site;

    public ListenerUploadSiteWithoutExport(final Site site) {
        this.site = site;
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent arg0) {
    }

    @Override
    public void widgetSelected(SelectionEvent event) {
        final Shell shell = event.display.getActiveShell();
        String checkumsFileBasename = InitializationManager.getProperty("poormans.filename.checksums");
        DESCryptor cryptor = InitializationManager.getBean(DESCryptor.class);
        String plainPwd = cryptor.decrypt(site.getTransferLoginPassword());
        Upload transferer = new Upload(site, ConnectionFactory.getFtp(site.getTransferHost(), site.getTransferLoginUser(), plainPwd, site.getTransferStartDirectory()), checkumsFileBasename);
        try {
            DialogManager.startProgressDialog(shell, transferer);
            MessageBox mb = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
            mb.setText(LabelHolder.get("popup.info"));
            mb.setMessage("Site [" + site.getDecorationString() + "] uploaded successfull.");
            mb.open();
        } catch (ProgressException e) {
            MessageBox msg = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
            msg.setText(LabelHolder.get("popup.error"));
            if (e.getCause() instanceof ConnectionAuthentificationException) msg.setMessage("Ftp login failed. \nPlease check your login properties!"); else msg.setMessage("An unknown error was happend: " + e.getMessage() + "\n\n" + e.getStackTrace());
            msg.open();
            return;
        } catch (ConnectionException cone) {
            MessageBox msg = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
            msg.setText(LabelHolder.get("popup.error"));
            msg.setMessage("While trying to establish the transfer connection: " + cone.getMessage());
            msg.open();
            return;
        } catch (Exception e) {
            MessageBox msg = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
            msg.setText(LabelHolder.get("popup.error"));
            msg.setMessage("While transfering the files, the following error was happend: " + e.getMessage() + "\n" + e.getStackTrace());
            msg.open();
        }
    }
}

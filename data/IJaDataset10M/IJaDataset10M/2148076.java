package net.sf.catchup.client;

import javax.swing.JOptionPane;
import net.sf.catchup.client.ui.SignupWindow;
import net.sf.catchup.client.ui.UIHelper;
import net.sf.catchup.client.util.ClientsideMsgListener;
import net.sf.catchup.client.util.ClientsideSocketReader;
import net.sf.catchup.client.util.SyncTransactionManager;
import net.sf.catchup.common.PeerSocketWrapper;
import net.sf.catchup.common.credentials.Credentials;
import net.sf.catchup.common.logging.LoggerFactory;
import net.sf.catchup.common.packet.SignupAction;
import net.sf.catchup.common.workers.Work;
import net.sf.catchup.common.workers.Worker;
import net.sf.catchup.common.workers.WorkerFactory;

public class SignupSequence implements Work {

    private final Credentials credentials;

    /**
	 * Prepares this work for execution by setting the credentials object
	 * 
	 * @param credentials
	 */
    public SignupSequence(final Credentials credentials) {
        this.credentials = credentials;
    }

    public void execute() {
        beginSignup();
    }

    private void beginSignup() {
        PeerSocketWrapper peerSocketWrapper = null;
        try {
            peerSocketWrapper = ServerConnectionAdapter.getConnection();
            final ClientsideSocketReader socketReader = new ClientsideSocketReader(peerSocketWrapper);
            socketReader.addMsgListener(new ClientsideMsgListener(peerSocketWrapper));
            final Worker worker = WorkerFactory.getInstance().getWorker();
            worker.assignWork(socketReader);
            worker.doWork();
            SignupAction signup = new SignupAction();
            signup.setCredentials(credentials);
            signup = (SignupAction) new SyncTransactionManager().sendRequestAndGetResponse(signup, peerSocketWrapper, 1000L * 15);
            peerSocketWrapper.clean();
            if (signup.isAccepted()) {
                SignupWindow.getInstance().dispose();
                UIHelper.showMessageDialog("User " + credentials.getUsername() + " successfully registered", "Signup complete", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(SignupWindow.getInstance(), signup.getReason(), "Signup failed", JOptionPane.ERROR_MESSAGE);
                SignupWindow.getInstance().reset();
            }
        } catch (final Exception ex) {
            LoggerFactory.getAsyncLogger().error(ex.getMessage(), ex);
            UIHelper.showMessageDialog("Unable to connect to server\n" + UIHelper.filterMessage(ex.getMessage()), "Error", JOptionPane.ERROR_MESSAGE);
            SignupWindow.getInstance().reset();
            if (peerSocketWrapper != null) {
                peerSocketWrapper.clean();
            }
        }
    }
}

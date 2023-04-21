package pdp.scrabble.ihm;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextField;
import pdp.scrabble.ihm.action.MultiplayerAction;
import pdp.scrabble.utility.Display;
import static pdp.scrabble.Language.getGameLang;

/** Connection dialog handler (getting ip and port for connection).
 * Connection will be established later.
 */
public class MultiplayerConnectDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    /** Main frame reference. */
    private MainFrame_old mainFrame = null;

    /** MultiplayerAction reference. */
    private MultiplayerAction multiplayerAction = null;

    /** Ip field. */
    private JTextField ipField = null;

    /** Port field. */
    private JTextField portField = null;

    /** Create a connect dialog.
     * @param mainFrame main frame reference.
     * @param multiplayerAction multiplayerAction reference.
     */
    public MultiplayerConnectDialog(MainFrame_old mainFrame, MultiplayerAction multiplayerAction) {
        this.mainFrame = mainFrame;
        this.multiplayerAction = multiplayerAction;
    }

    /** Create and start dialog. */
    public void create() {
        this.setTitle(getGameLang("New connection ..."));
        this.setLayout(new GridLayout(0, 2));
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.ipField = new JTextField("localhost");
        this.ipField.setPreferredSize(new Dimension(116, 34));
        this.add(this.ipField);
        this.portField = new JTextField("1099");
        this.portField.setPreferredSize(new Dimension(48, 24));
        this.add(this.portField);
        JButton button = new JButton(getGameLang("Confirm"));
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    confirm();
                } catch (NotBoundException ex) {
                    Display.error("Connection", "Error during connection !");
                } catch (MalformedURLException ex) {
                    Display.error("Connection", "Bad URL !");
                } catch (RemoteException ex) {
                    Display.error("Connection", "Cannot connect !");
                }
            }
        });
        this.add(button);
        button = new JButton(getGameLang("Cancel"));
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        });
        this.add(button);
        this.mainFrame.setEnabled(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /** Confirm action.
     * @throws RemoteException Remote exception.
     * @throws NotBoundException Not bound exception.
     * @throws MalformedURLException Malformed URL exception.
     */
    private void confirm() throws NotBoundException, MalformedURLException, RemoteException {
        if (this.multiplayerAction.connectTo(this.getIP(), this.getPort())) {
            this.mainFrame.setEnabled(true);
            this.dispose();
        } else {
            return;
        }
    }

    /** Cancel action. */
    private void cancel() {
        this.mainFrame.setEnabled(true);
        this.dispose();
    }

    /** Get entered ip.
     * @return entered ip.
     */
    public String getIP() {
        return this.ipField.getText();
    }

    /** Get entered port.
     * @return entered port.
     */
    public String getPort() {
        return this.portField.getText();
    }
}

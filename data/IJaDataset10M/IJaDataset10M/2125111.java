package ui.dialogs;

import java.awt.Container;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Hashtable;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import model.admin.AdminUser;
import model.configuration.Configuration;
import ui.EdujationMainFrame;
import ui.GUIData;
import ui.JPanelApplet;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class JDialogLogin extends JPanel {

    private static final long serialVersionUID = -884343427435048052L;

    private Container parent;

    private JButton jButtonAccept;

    private JLabel jLabelUserName;

    private JLabel jLabelPassword;

    protected JTextField jTextFieldPassword;

    protected JTextField jTextFieldUserName;

    private JButton jButtonCancel;

    private Boolean autor;

    private Integer id_user;

    private JLabel jLabelServer;

    private JTextField jTextFieldServer;

    private JLabel jLabelState;

    public JDialogLogin(Container parent) {
        super();
        this.parent = parent;
        initGUI();
    }

    private void initGUI() {
        try {
            {
                this.setLayout(null);
                {
                    jButtonAccept = new JButton();
                    this.add(jButtonAccept);
                    jButtonAccept.setText("Aceptar");
                    jButtonAccept.setBounds(231, 154, 168, 28);
                    jButtonAccept.addKeyListener(new KeyAdapter() {

                        public void keyTyped(KeyEvent evt) {
                            jButtonAcceptKeyTyped(evt);
                        }
                    });
                    jButtonAccept.addMouseListener(new MouseAdapter() {

                        public void mouseClicked(MouseEvent evt) {
                            jButtonAcceptMouseClicked();
                        }
                    });
                }
                {
                    jButtonCancel = new JButton();
                    this.add(jButtonCancel);
                    jButtonCancel.setText("Cancelar");
                    jButtonCancel.setBounds(63, 154, 161, 28);
                    jButtonCancel.addKeyListener(new KeyAdapter() {

                        public void keyTyped(KeyEvent evt) {
                            switch(evt.getKeyChar()) {
                                case ' ':
                                case '\n':
                                    jButtonCancelMouseClicked();
                            }
                        }
                    });
                    jButtonCancel.addMouseListener(new MouseAdapter() {

                        public void mouseClicked(MouseEvent evt) {
                            jButtonCancelMouseClicked();
                        }
                    });
                }
                {
                    jTextFieldUserName = new JTextField();
                    this.add(jTextFieldUserName);
                    jTextFieldUserName.setText("");
                    jTextFieldUserName.setBounds(210, 21, 154, 28);
                }
                {
                    jTextFieldPassword = new JTextField();
                    this.add(jTextFieldPassword);
                    jTextFieldPassword.setBounds(210, 63, 154, 28);
                }
                {
                    jLabelUserName = new JLabel();
                    this.add(jLabelUserName);
                    jLabelUserName.setText("Nombre de Usuario:");
                    jLabelUserName.setBounds(63, 21, 126, 28);
                }
                {
                    jLabelPassword = new JLabel();
                    this.add(jLabelPassword);
                    jLabelPassword.setText("Contraseña:");
                    jLabelPassword.setBounds(63, 63, 105, 28);
                }
                {
                    jLabelServer = new JLabel();
                    this.add(jLabelServer);
                    jLabelServer.setText("Direccion del servidor:");
                    jLabelServer.setBounds(63, 105, 140, 28);
                }
                {
                    jTextFieldServer = new JTextField("http://laboratorio2006.java.linti.unlp.edu.ar:8080/edujation/");
                    this.add(jTextFieldServer);
                    jTextFieldServer.setBounds(210, 105, 154, 28);
                }
                {
                    jLabelState = new JLabel();
                    this.add(jLabelState);
                    jLabelState.setBounds(56, 189, 378, 28);
                }
            }
            this.setSize(582, 358);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void jButtonCancelMouseClicked() {
        this.jTextFieldPassword.setText("");
        this.jTextFieldUserName.setText("");
        if (parent instanceof JPanelApplet) ((JPanelApplet) parent).jButtonCancelPushed(); else ((EdujationMainFrame) parent).jButtonCancelPushed();
    }

    /**
	 * Verifica que el nombre de usuario y contraseñas ingresados no sean vacios.
	 * @return true si name y pass no vacios.
	 */
    private boolean checkTexts() {
        return (this.jTextFieldUserName.getText().length() != 0 && this.jTextFieldPassword.getText().length() != 0);
    }

    private void jButtonAcceptMouseClicked() {
        AdminUser.setServer(this.jTextFieldServer.getText());
        Hashtable<String, Object> loguin = AdminUser.getInstance().login(this.jTextFieldUserName.getText(), this.jTextFieldPassword.getText());
        if (this.checkTexts() && (Boolean) loguin.get("result")) {
            jLabelState.setText("");
            jLabelState.updateUI();
            GUIData.getInstance().setAuthor((Boolean) loguin.get("autor"));
            GUIData.getInstance().setUserID((Integer) loguin.get("id_user"));
            GUIData.getInstance().setUserName(this.jTextFieldUserName.getText());
            GUIData.getInstance().setConfiguration((Configuration) loguin.get("config"));
            if (parent instanceof JPanelApplet) ((JPanelApplet) parent).userSelect(); else ((EdujationMainFrame) parent).userSelect();
        } else {
            jLabelState.setText("Usuario no encontrado");
            jLabelState.updateUI();
        }
    }

    private void jButtonAcceptKeyTyped(KeyEvent evt) {
        switch(evt.getKeyChar()) {
            case ' ':
            case '\n':
                this.jButtonAcceptMouseClicked();
        }
    }

    public Boolean isAuthorMode() {
        return this.autor;
    }

    public Integer getId_user() {
        return this.id_user;
    }

    public String getUserName() {
        return jTextFieldUserName.getText();
    }
}

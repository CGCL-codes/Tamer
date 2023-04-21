package Presentacion;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import chrriis.common.UIUtils;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.FlashPlayerListener;
import chrriis.dj.nativeswing.swtimpl.components.JFlashPlayer;
import edu.xtec.jclic.report.TCPReporter;
import edu.xtec.jclic.report.UserData;
import edu.xtec.util.ResourceManager;

public class MenuNeuro extends JPanel {

    private UserData user;

    private SANTi parent;

    private AsignacionActividad asigAct;

    private JFrame Registroframe;

    private Registro JRegistro;

    private TCPReporter tcp;

    private JFrame Asigancionframe;

    private JFrame Modificacionframe;

    private JFrame FAQframe;

    private ModificarActividad modAct;

    private MenuNeuro menu;

    private JFlashPlayer flashPlayer;

    private JFrame Eliminarframe;

    private eliminarUsuario elmUser;

    private FAQ faqNeuro;

    public MenuNeuro(final UserData user, final SANTi parent) {
        super(new BorderLayout());
        this.user = user;
        this.parent = parent;
        this.tcp = this.parent.getTcp();
        JPanel flashPlayerPanel = new JPanel(new BorderLayout());
        final JFlashPlayer flashPlayer = new JFlashPlayer();
        flashPlayer.load(getClass(), "resource/menuNeuropsicologo.swf");
        flashPlayerPanel.add(flashPlayer, BorderLayout.CENTER);
        add(flashPlayerPanel, BorderLayout.CENTER);
        GridBagLayout gridBag = new GridBagLayout();
        JPanel interactionsPanel = new JPanel(gridBag);
        interactionsPanel.setBorder(BorderFactory.createTitledBorder("Java Interactions"));
        GridBagConstraints cons = new GridBagConstraints();
        cons.gridx = 0;
        cons.gridy = 0;
        cons.insets = new Insets(1, 1, 1, 1);
        cons.anchor = GridBagConstraints.WEST;
        flashPlayer.invokeFlashFunction("nombreUsuario", this.user.getText());
        menu = this;
        flashPlayer.addFlashPlayerListener(new FlashPlayerListener() {

            int cantArgs;

            public void commandReceived(String command, String[] args) {
                if ("swf_asignarActividad".equals(command)) {
                    parent.ocultar(0);
                    iniciarAsignacionActividades(menu);
                } else if ("swf_crearActividad".equals(command)) {
                    parent.ocultar(0);
                    parent.crearActividad();
                } else if ("swf_modificarActividad".equals(command)) {
                    parent.ocultar(0);
                    iniciarModificarActividad(menu);
                } else if ("swf_crearUsuarioNino".equals(command)) {
                    parent.ocultar(0);
                    iniciarRegistro();
                } else if ("swf_reportes".equals(command)) {
                    try {
                        java.net.URL page = new java.net.URL("http", tcp.getServiceUrl().getHost(), 9000, "/");
                        edu.xtec.util.BrowserLauncher.openURL(page.toExternalForm());
                    } catch (Exception ex) {
                    }
                } else if ("swf_faq".equals(command)) {
                    parent.ocultar(0);
                    iniciarFAQ(menu);
                } else if ("swf_eliminarUsuarioNino".equals(command)) {
                    parent.ocultar(0);
                    eliminarUsuario();
                } else if ("swf_salir".equals(command)) {
                    parent.ocultar(1);
                }
            }

            public String[] armarString(String datos) {
                String da[] = new String[cantArgs];
                da = datos.split("//-//");
                for (int j = 0; j < cantArgs; j++) System.out.println(da[j]);
                return da;
            }

            public StringBuilder armarSB(String[] args) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < args.length; i++) {
                    if (i > 0) {
                        sb.append("//-//");
                    }
                    sb.append(args[i]);
                }
                cantArgs = args.length;
                return sb;
            }
        });
        this.flashPlayer = flashPlayer;
    }

    public void build() {
        flashPlayer.invokeFlashFunction("nombreUsuario", this.user.getText());
    }

    public TCPReporter getTCP() {
        return this.tcp;
    }

    public void mostrar(int opc) {
        if (opc == 0) {
            this.Registroframe.dispose();
            this.parent.mostrar(0);
        }
    }

    public void recargar() {
        flashPlayer.invokeFlashFunction("nombreUsuario", this.user.getText());
    }

    public void ocultar(int opc) {
        if (opc == 0) {
            this.Modificacionframe.dispose();
            this.Modificacionframe = null;
        } else if (opc == 1) {
            this.Asigancionframe.dispose();
            this.Asigancionframe = null;
            parent.mostrar(0);
        } else if (opc == 2) {
            this.FAQframe.dispose();
            this.FAQframe = null;
            parent.mostrar(0);
        } else if (opc == 3) {
            this.Eliminarframe.dispose();
            this.Eliminarframe = null;
            parent.mostrar(0);
        } else if (opc == 4) {
            this.Registroframe.dispose();
            this.Registroframe = null;
            parent.mostrar(0);
        }
    }

    public void iniciarAsignacionActividades(final MenuNeuro par) {
        UIUtils.setPreferredLookAndFeel();
        NativeInterface.open();
        asigAct = new AsignacionActividad(this.user, this.parent.getTcp(), par);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                Asigancionframe = new JFrame("SANTi");
                Asigancionframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                Asigancionframe.getContentPane().add(asigAct, BorderLayout.CENTER);
                Asigancionframe.setSize(768, 604);
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int scrW = (int) screenSize.getWidth();
                int scrH = (int) screenSize.getHeight();
                Asigancionframe.setLocationByPlatform(true);
                Asigancionframe.setLocation((scrW - Asigancionframe.getWidth()) / 2, (scrH - Asigancionframe.getHeight()) / 3);
                Asigancionframe.setIconImage(ResourceManager.getImageIcon("icons/miniSANTi.png").getImage());
                Asigancionframe.setAlwaysOnTop(true);
                Asigancionframe.setVisible(true);
                Asigancionframe.setResizable(false);
                class Hilo extends Thread {

                    public Thread hilo;

                    public Hilo() {
                        SwingUtilities.invokeLater(new Runnable() {

                            public void run() {
                                try {
                                    hilo.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                asigAct.build();
                            }
                        });
                    }
                }
                new Hilo().start();
            }
        });
        NativeInterface.runEventPump();
    }

    public void iniciarRegistro() {
        UIUtils.setPreferredLookAndFeel();
        NativeInterface.open();
        final MenuNeuro parent = this;
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                Registroframe = new JFrame("SANTi");
                Registroframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                JRegistro = new Registro(parent, user.getId());
                Registroframe.getContentPane().add(JRegistro, BorderLayout.CENTER);
                Registroframe.setSize(776, 611);
                Registroframe.setLocationByPlatform(true);
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                final int scrW = (int) screenSize.getWidth();
                final int scrH = (int) screenSize.getHeight();
                Registroframe.setLocation((scrW - Registroframe.getWidth()) / 2, (scrH - Registroframe.getHeight()) / 3);
                Registroframe.setIconImage(ResourceManager.getImageIcon("icons/miniSANTi.png").getImage());
                Registroframe.setAlwaysOnTop(true);
                Registroframe.setVisible(true);
                Registroframe.setAlwaysOnTop(false);
                Registroframe.setResizable(false);
            }
        });
        NativeInterface.runEventPump();
    }

    public void iniciarModificarActividad(final MenuNeuro p) {
        UIUtils.setPreferredLookAndFeel();
        NativeInterface.open();
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                Modificacionframe = new JFrame("SANTi");
                Modificacionframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                modAct = new ModificarActividad(p, user, tcp, parent);
                Modificacionframe.getContentPane().add(modAct, BorderLayout.CENTER);
                Modificacionframe.setSize(842, 660);
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int scrW = (int) screenSize.getWidth();
                int scrH = (int) screenSize.getHeight();
                Modificacionframe.setLocationByPlatform(true);
                Modificacionframe.setLocation((scrW - Modificacionframe.getWidth()) / 2, (scrH - Modificacionframe.getHeight()) / 3);
                Modificacionframe.setIconImage(ResourceManager.getImageIcon("icons/miniSANTi.png").getImage());
                Modificacionframe.setAlwaysOnTop(true);
                Modificacionframe.setVisible(true);
                Modificacionframe.setAlwaysOnTop(false);
                Modificacionframe.setResizable(false);
                class Hilo extends Thread {

                    public Thread hilo;

                    public Hilo() {
                        SwingUtilities.invokeLater(new Runnable() {

                            public void run() {
                                try {
                                    hilo.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                modAct.build();
                            }
                        });
                    }
                }
                new Hilo().start();
            }
        });
        NativeInterface.runEventPump();
    }

    public void iniciarFAQ(final MenuNeuro p) {
        UIUtils.setPreferredLookAndFeel();
        NativeInterface.open();
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                FAQframe = new JFrame("SANTi");
                FAQframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                faqNeuro = new FAQ(p);
                FAQframe.getContentPane().add(faqNeuro, BorderLayout.CENTER);
                FAQframe.setSize(842, 660);
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int scrW = (int) screenSize.getWidth();
                int scrH = (int) screenSize.getHeight();
                FAQframe.setLocationByPlatform(true);
                FAQframe.setLocation((scrW - FAQframe.getWidth()) / 2, (scrH - FAQframe.getHeight()) / 3);
                FAQframe.setIconImage(ResourceManager.getImageIcon("icons/miniSANTi.png").getImage());
                FAQframe.setAlwaysOnTop(true);
                FAQframe.setVisible(true);
                FAQframe.setAlwaysOnTop(false);
                FAQframe.setResizable(false);
            }
        });
        NativeInterface.runEventPump();
    }

    public void eliminarUsuario() {
        UIUtils.setPreferredLookAndFeel();
        NativeInterface.open();
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                Eliminarframe = new JFrame("SANTi");
                Eliminarframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                elmUser = new eliminarUsuario(menu, user, tcp);
                Eliminarframe.getContentPane().add(elmUser, BorderLayout.CENTER);
                Eliminarframe.setSize(842, 660);
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int scrW = (int) screenSize.getWidth();
                int scrH = (int) screenSize.getHeight();
                Eliminarframe.setLocationByPlatform(true);
                Eliminarframe.setLocation((scrW - Eliminarframe.getWidth()) / 2, (scrH - Eliminarframe.getHeight()) / 3);
                Eliminarframe.setIconImage(ResourceManager.getImageIcon("icons/miniSANTi.png").getImage());
                Eliminarframe.setAlwaysOnTop(true);
                Eliminarframe.setVisible(true);
                Eliminarframe.setAlwaysOnTop(false);
                Eliminarframe.setResizable(false);
                class Hilo extends Thread {

                    public Thread hilo;

                    public Hilo() {
                        SwingUtilities.invokeLater(new Runnable() {

                            public void run() {
                                try {
                                    hilo.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                elmUser.build();
                            }
                        });
                    }
                }
                new Hilo().start();
            }
        });
        NativeInterface.runEventPump();
    }

    public void tamano() {
        Dimension tamano = Registroframe.getSize();
        System.out.println("Tama�o: " + tamano);
    }
}

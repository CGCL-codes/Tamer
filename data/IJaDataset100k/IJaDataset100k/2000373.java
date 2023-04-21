package app;

import info.clearthought.layout.TableLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

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
public class Dialog extends javax.swing.JDialog {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JTextField url;

    private JLabel urlLabel;

    private JCheckBox link;

    private JCheckBox hd;

    private JMenuItem pegar;

    private JPopupMenu jPopupMenu1;

    private JButton cancel;

    private JButton ok;

    public Main main;

    private int tipo = 0;

    @SuppressWarnings("unused")
    private static final String ACTION_CLOSE = "ACTION_CLOSE";

    KeyStroke esc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);

    /**
	* Auto-generated main method to display this JDialog
	*/
    public Dialog(JFrame frame) {
        super(frame);
        initGUI();
        addEscapeKey();
    }

    public void setLinkEnabled(boolean t) {
        link.setSelected(t);
    }

    private void addEscapeKey() {
        KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        Action escapeAction = new AbstractAction() {

            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                cancelActionPerformed(e);
            }
        };
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", escapeAction);
    }

    public void obtenerFoco() {
        url.requestFocus();
    }

    public String getUrl() {
        return url.getText();
    }

    public void conectar(Main a1) {
        main = a1;
    }

    private void initGUI() {
        try {
            this.setIconImage(new ImageIcon(getClass().getClassLoader().getResource("app/postinga16x16.png")).getImage());
            TableLayout thisLayout = new TableLayout(new double[][] { { 7.0, TableLayout.FILL, TableLayout.FILL, 7.0, 7.0 }, { TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, 7.0, TableLayout.FILL, 7.0 } });
            thisLayout.setHGap(5);
            thisLayout.setVGap(5);
            getContentPane().setLayout(thisLayout);
            this.setPreferredSize(new java.awt.Dimension(337, 206));
            getContentPane().setBackground(new java.awt.Color(255, 255, 255));
            this.setLocation(new java.awt.Point(100, 24));
            this.setTitle("Insertar Link");
            this.setAlwaysOnTop(true);
            {
                url = new JTextField();
                getContentPane().add(url, "1, 2, 3, 2, f, c");
                url.setPreferredSize(new java.awt.Dimension(390, 123));
            }
            {
                urlLabel = new JLabel();
                getContentPane().add(urlLabel, "1, 1, 2, 1");
                urlLabel.setText("Ingresa la URL");
                urlLabel.setFont(new java.awt.Font("Arial", 1, 14));
            }
            {
                ok = new JButton();
                getContentPane().add(ok, "1, 5, r, f");
                ok.setText("Aceptar");
                ok.setFont(new java.awt.Font("Ubuntu", 0, 12));
                ok.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        okActionPerformed(evt);
                    }
                });
                ok.addMouseListener(new MouseAdapter() {

                    public void mouseClicked(MouseEvent evt) {
                        okMouseClicked(evt);
                    }
                });
            }
            {
                cancel = new JButton();
                getContentPane().add(cancel, "2, 5, l, f");
                cancel.setText("Cancelar");
                cancel.setFont(new java.awt.Font("Ubuntu", 0, 12));
                cancel.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        cancelActionPerformed(evt);
                    }
                });
            }
            {
                hd = new JCheckBox();
                getContentPane().add(hd, "2, 5, r, f");
                hd.setText("HD");
                hd.setFont(new java.awt.Font("Ubuntu", 0, 12));
            }
            {
                link = new JCheckBox();
                getContentPane().add(link, "1,3,2,3,r,f");
                link.setText("Agregar link por debajo");
                link.setFont(new java.awt.Font("Ubuntu", 0, 12));
            }
            {
                jPopupMenu1 = new JPopupMenu();
                url.setComponentPopupMenu(jPopupMenu1);
                {
                    pegar = new JMenuItem();
                    jPopupMenu1.add(pegar);
                    pegar.setText("Pegar");
                    pegar.setIcon(new ImageIcon(getClass().getResource("paste.gif")));
                    pegar.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent evt) {
                            pegarActionPerformed(evt);
                        }
                    });
                }
            }
            this.setSize(337, 206);
            getPopup1();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTipo(String str) {
        getRootPane().setDefaultButton(ok);
        hd.setVisible(false);
        link.setVisible(false);
        if (str == "imagen") {
            tipo = 1;
            link.setText("Centrar");
            link.setVisible(true);
        }
        if (str == "youtube") {
            tipo = 2;
            link.setText("Insertar link por debajo");
            link.setVisible(true);
            hd.setVisible(true);
        }
        if (str == "gvideo") {
            tipo = 3;
        }
        if (str == "goear") {
            tipo = 4;
        }
        if (str == "swf") {
            link.setText("Insertar link por debajo");
            link.setVisible(true);
            tipo = 5;
        }
        if (str == "link") {
            tipo = 7;
        }
        if (str == "bliptv") {
            tipo = 8;
        }
        if (str == "quote") tipo = 9;
        if (str == "vimeo") {
            tipo = 10;
        }
        if (str == "dailymotion") {
            tipo = 11;
        }
        if (str == "metacafe") {
            tipo = 12;
        }
    }

    private void okMouseClicked(MouseEvent evt) {
        okAction();
    }

    private void okAction() {
        String link = null;
        String texto;
        link = url.getText();
        if (!link.equals("")) {
            if (main.seleccion() == null) texto = link; else texto = main.seleccion();
            switch(tipo) {
                case 1:
                    main.generarImagen(link, this.link.isSelected());
                    break;
                case 2:
                    {
                        main.generarYoutube(link, hd.isSelected(), this.link.isSelected());
                        hd.setSelected(false);
                        break;
                    }
                case 3:
                    main.generarGvideo(link, this.link.isSelected());
                    break;
                case 4:
                    main.generarGoear(link, this.link.isSelected());
                    break;
                case 5:
                    main.generarSwf(link, this.link.isSelected());
                    break;
                case 7:
                    main.generarLink(link, texto);
                    break;
                case 8:
                    main.generarBliptv(link, this.link.isSelected());
                    break;
                case 9:
                    main.generarQuote(link);
                    break;
                case 10:
                    main.generarVimeo(link, this.link.isSelected());
                    break;
                case 11:
                    main.generarDailyMotion(link, this.link.isSelected());
                    break;
                case 12:
                    main.generarMetacafe(link, this.link.isSelected());
                    break;
            }
            url.setText("");
        }
        main.obtenerFoco();
        this.dispose();
    }

    public void cambiarLabel(String str) {
        urlLabel.setText(str);
    }

    /**
	* Auto-generated method for setting the popup menu for a component
	*/
    private void pegarActionPerformed(ActionEvent evt) {
        url.paste();
    }

    private void okActionPerformed(ActionEvent evt) {
        okAction();
        urlLabel.setText("Ingresa la URL");
    }

    private void cancelActionPerformed(ActionEvent evt) {
        url.setText("");
        urlLabel.setText("Ingresa la URL");
        hd.setSelected(false);
        main.obtenerFoco();
        this.dispose();
    }

    /********************************PopUp*******************************************/
    JPopupMenu popup1;

    JMenuItem PopCortar1;

    JMenuItem PopPegar1;

    JMenuItem PopCopiar1;

    JMenuItem PopSelAll1;

    private String icon_copy = "copy.gif";

    private String icon_paste = "paste.gif";

    private String icon_cut = "cut.gif";

    private String icon_select_all = "selectall.gif";

    private JMenuItem getPopCortar1() {
        if (PopCortar1 == null) {
            PopCortar1 = new JMenuItem("Cortar");
            PopCortar1.setIcon(new ImageIcon(getClass().getResource("cut.gif")));
            PopCortar1.setFont(new java.awt.Font("Ubuntu", 0, 12));
            PopCortar1.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    PopCortar1ActionPerformed(evt);
                }
            });
        }
        return PopCortar1;
    }

    private JMenuItem getPopCopiar1() {
        if (PopCopiar1 == null) {
            PopCopiar1 = new JMenuItem("Copiar");
            PopCopiar1.setFont(new java.awt.Font("Ubuntu", 0, 12));
            PopCopiar1.setIcon(new ImageIcon(getClass().getResource("copy.gif")));
            PopCopiar1.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    PopCopiar1ActionPerformed(evt);
                }
            });
        }
        return PopCopiar1;
    }

    private JMenuItem getPopPegar1() {
        if (PopPegar1 == null) {
            PopPegar1 = new JMenuItem("Pegar");
            PopPegar1.setIcon(new ImageIcon(getClass().getResource("paste.gif")));
            PopPegar1.setFont(new java.awt.Font("Ubuntu", 0, 12));
            PopPegar1.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    PopPegar1ActionPerformed(evt);
                }
            });
        }
        return PopPegar1;
    }

    private JMenuItem getPopSelAll1() {
        if (PopSelAll1 == null) {
            PopSelAll1 = new JMenuItem("Seleccionar todo");
            PopSelAll1.setIcon(new ImageIcon(getClass().getResource(icon_select_all)));
            PopSelAll1.setFont(new java.awt.Font("Ubuntu", 0, 12));
            PopSelAll1.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    PopSelAll1ActionPerformed(evt);
                }
            });
        }
        return PopSelAll1;
    }

    private void PopPegar1ActionPerformed(ActionEvent evt) {
        url.paste();
        url.requestFocus();
    }

    private void PopCopiar1ActionPerformed(ActionEvent evt) {
        url.copy();
    }

    private void PopSelAll1ActionPerformed(ActionEvent evt) {
        url.selectAll();
        url.requestFocus();
    }

    private void PopCortar1ActionPerformed(ActionEvent evt) {
        url.cut();
        url.requestFocus();
    }

    private JPopupMenu getPopup1() {
        if (popup1 == null) {
            popup1 = new JPopupMenu();
            url.setComponentPopupMenu(popup1);
            popup1.add(getPopCortar1());
            popup1.add(getPopCopiar1());
            popup1.add(getPopPegar1());
            popup1.add(getPopSelAll1());
        }
        return popup1;
    }
}

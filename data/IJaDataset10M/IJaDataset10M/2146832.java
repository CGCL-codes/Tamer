package totalpos;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Saúl Hidalgo
 */
public class MainWindows extends javax.swing.JFrame {

    private TreeMap<String, Set<Character>> mnemonics = new TreeMap<String, Set<Character>>();

    private Image wallpaper = new ImageIcon(getClass().getResource("/totalpos/resources/Fondo-Tramado.jpg")).getImage();

    MdiPanel mdiPanel = new MdiPanel(wallpaper);

    /** Creates new form MainWindows
     * @param user 
     */
    public MainWindows(User user) {
        initComponents();
        mdiPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        mdiPanel.setFocusable(true);
        mdiPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {

            public void mouseMoved(java.awt.event.MouseEvent evt) {
                mdiPanelMouseMoved(evt);
            }

            private void mdiPanelMouseMoved(MouseEvent evt) {
                Shared.getScreenSaver().actioned();
            }
        });
        mdiPanel.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    logout();
                }
            }
        });
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(mdiPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(mdiPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
        mdiPanel.setVisible(true);
        getContentPane().add(mdiPanel);
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        createMenu(menuBar, "root");
        if (!Shared.getConfig().containsKey("storeName")) {
            CreateShop cs = new CreateShop();
            if (cs.isOk) {
                mdiPanel.add(cs);
                cs.setVisible(true);
            } else {
                MessageBox msb = new MessageBox(MessageBox.SGN_DANGER, "No se pudo crear la tienda. No se puede continuar.");
                msb.show(null);
            }
        }
    }

    private void createMenu(JComponent menu, String root) {
        try {
            List<Edge> edges = ConnectionDrivers.listEdgesAllowed(root, Shared.getUser().getPerfil());
            Collections.sort(edges, Collections.reverseOrder());
            for (int i = 0; i < edges.size(); i++) {
                Edge ed = edges.get(i);
                JComponent child = null;
                if (ed.getFuncion().equals("menu")) {
                    child = new JMenu(new AppUserAction(ed, this));
                } else {
                    child = new JMenuItem(new AppUserAction(ed, this));
                }
                child.setFont(new Font("Courier New", 0, 12));
                child.setFocusable(false);
                menu.add(child);
                createMenu(child, ed.getId());
            }
        } catch (SQLException ex) {
            MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Problemas con la base de datos.", ex);
            msg.show(this);
        } catch (Exception ex) {
            MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, ex.getMessage(), ex);
            msg.show(this);
            this.dispose();
            Shared.reload();
        }
    }

    private int giveMeMnemonic(String nameMenu, String name) {
        for (char c : name.toCharArray()) {
            c = Character.toLowerCase(c);
            if (!(mnemonics.containsKey(nameMenu) && mnemonics.get(nameMenu).contains(c))) {
                if (!mnemonics.containsKey(nameMenu)) {
                    mnemonics.put(nameMenu, new TreeSet<Character>());
                }
                mnemonics.get(nameMenu).add(c);
                return Character.toLowerCase(c) - 'a';
            }
        }
        return 0;
    }

    private class AppUserAction extends AbstractAction {

        private Edge ed;

        private MainWindows mainWindows;

        private AppUserAction(Edge ed, MainWindows aThis) {
            this.ed = ed;
            this.mainWindows = aThis;
            putValue(Action.NAME, ed.getNombre());
            putValue(Action.MNEMONIC_KEY, new Integer(java.awt.event.KeyEvent.VK_A + giveMeMnemonic(ed.getPredecesor(), ed.getNombre())));
        }

        public void actionPerformed(ActionEvent evt) {
            if (ed.getFuncion().equals("addProfile")) {
                CreateProfile cp = new CreateProfile();
                mdiPanel.add(cp);
                cp.setVisible(true);
            } else if (ed.getFuncion().equals("searchProfile")) {
                SearchProfile sp = new SearchProfile();
                if (sp.isOk) {
                    mdiPanel.add(sp);
                    sp.setVisible(true);
                }
            } else if (ed.getFuncion().equals("manageUser")) {
                ManageUser mu = new ManageUser();
                if (mu.isOk) {
                    mdiPanel.add(mu);
                    mu.setVisible(true);
                }
            } else if (ed.getFuncion().equals("changeIdleTime")) {
                ChangeIdleTime cit = new ChangeIdleTime();
                if (cit.isOk) {
                    mdiPanel.add(cit);
                    cit.setVisible(true);
                }
            } else if (ed.getFuncion().equals("manageItem")) {
                ManageItem mi = new ManageItem();
                if (mi.isOk) {
                    mdiPanel.add(mi);
                    mi.setVisible(true);
                }
            } else if (ed.getFuncion().equals("listTurns")) {
                ListTurnsForm ct = new ListTurnsForm();
                if (ct.isOk) {
                    mdiPanel.add(ct);
                    ct.setVisible(true);
                }
            } else if (ed.getFuncion().equals("createStore")) {
                CreateShop cs = new CreateShop();
                if (cs.isOk) {
                    mdiPanel.add(cs);
                    cs.setVisible(true);
                }
            } else if (ed.getFuncion().equals("listPos")) {
                ListPOS cp = new ListPOS();
                if (cp.isOk) {
                    mdiPanel.add(cp);
                    cp.setVisible(true);
                }
            } else if (ed.getFuncion().equals("listTurnsAssigned")) {
                ListTurnsAssigned lta = new ListTurnsAssigned();
                if (lta.isOk) {
                    mdiPanel.add(lta);
                    lta.setVisible(true);
                }
            } else if (ed.getFuncion().equals("managePOSBank")) {
                ManageBank lta = new ManageBank();
                if (lta.isOk) {
                    mdiPanel.add(lta);
                    lta.setVisible(true);
                }
            } else if (ed.getFuncion().equals("listReports")) {
                ReportsForm rf = new ReportsForm();
                mdiPanel.add(rf);
                rf.setVisible(true);
            } else if (ed.getFuncion().equals("expenses")) {
                AddExpenses ae = new AddExpenses();
                mdiPanel.add(ae);
                ae.setVisible(true);
            } else if (ed.getFuncion().equals("manageDeposit")) {
                ManageDeposits md = new ManageDeposits();
                mdiPanel.add(md);
                md.setVisible(true);
            } else if (ed.getFuncion().equals("closingDay")) {
                JTextField textField = new JTextField();
                ChooseDate cal = new ChooseDate(Constants.appName, textField, 1);
                mdiPanel.add(cal);
                cal.setVisible(true);
            } else if (ed.getFuncion().equals("sellWithoutStockAd")) {
                EnableSellsWithoutStock esws = new EnableSellsWithoutStock();
                if (esws.isOk) {
                    mdiPanel.add(esws);
                    esws.setVisible(true);
                }
            } else if (ed.getFuncion().equals("manageMsg")) {
                ChooseMessage esws = new ChooseMessage();
                if (esws.isOk) {
                    mdiPanel.add(esws);
                    esws.setVisible(true);
                }
            } else if (ed.getFuncion().equals("manageConfig")) {
                ConfigurationForm esws = new ConfigurationForm();
                if (esws.isOk) {
                    mdiPanel.add(esws);
                    esws.setVisible(true);
                }
            } else if (ed.getFuncion().equals("exit")) {
                logout();
            } else if (ed.getFuncion().substring(0, Math.min("updateStock".length(), ed.getFuncion().length())).equals("updateStock")) {
                UpdateStock us = new UpdateStock(ed.getFuncion().substring("updateStock".length()));
                us.updateStock();
            } else if (ed.getFuncion().equals("updateMM")) {
                UpdateStockFromSAP usfs = new UpdateStockFromSAP("MM");
                usfs.updateStockFromSAP();
            } else if (ed.getFuncion().equals("updatePrices")) {
                UpdateStockFromSAP usfs = new UpdateStockFromSAP("Prices");
                usfs.updateStockFromSAP();
            } else if (ed.getFuncion().equals("initialStock")) {
                UpdateStockFromSAP usfs = new UpdateStockFromSAP("initialStock");
                usfs.updateStockFromSAP();
            } else if (ed.getFuncion().equals("udpateEmployees")) {
                UpdateStockFromSAP usfs = new UpdateStockFromSAP("profitWorkers");
                usfs.updateStockFromSAP();
            } else if (ed.getFuncion().equals("sendSells")) {
                SendSellsFrom ssf = new SendSellsFrom();
                mdiPanel.add(ssf);
                ssf.setVisible(true);
            } else if (ed.getFuncion().equals("manualHR")) {
                JTextField textField = new JTextField();
                ChooseDate cal = new ChooseDate(Constants.appName, textField, 2);
                mdiPanel.add(cal);
                cal.setVisible(true);
            } else if (ed.getFuncion().equals("reconfigureZebra")) {
                Sticker.configure();
            } else if (ed.getFuncion().equals("createCapture")) {
                if (Shared.isFingerOpened > 0) {
                    MessageBox msb = new MessageBox(MessageBox.SGN_DANGER, "Existe otra ventana para huellas abierta. Solo debe haber máximo 1");
                    msb.show(mainWindows);
                } else {
                    String id = JOptionPane.showInputDialog(null, "Código de empleado", "");
                    if (id != null) {
                        createCapture cc = new createCapture(id);
                        if (cc.isOk) {
                            mdiPanel.add(cc);
                            cc.setVisible(true);
                        } else {
                            MessageBox msg = new MessageBox(MessageBox.SGN_IMPORTANT, "Código de empleado no existente!");
                            msg.show(mainWindows);
                        }
                    }
                }
            } else if (ed.getFuncion().equals("verifyCapture")) {
                if (Shared.isFingerOpened > 0) {
                    MessageBox msb = new MessageBox(MessageBox.SGN_DANGER, "Existe otra ventana para huellas abierta. Solo debe haber máximo 1");
                    msb.show(mainWindows);
                } else {
                    CheckFingerprint cfp = new CheckFingerprint();
                    if (cfp.isOk) {
                        mdiPanel.add(cfp);
                        cfp.setVisible(true);
                    }
                }
            } else if (ed.getFuncion().equals("manualPresence")) {
                SelectRangeDayAndStore srds = new SelectRangeDayAndStore(false);
                mdiPanel.add(srds);
                srds.setVisible(true);
            } else if (ed.getFuncion().equals("manualCestatickets")) {
                SelectRangeDayAndStore srds = new SelectRangeDayAndStore(true);
                mdiPanel.add(srds);
                srds.setVisible(true);
            } else if (ed.getFuncion().isEmpty()) {
                MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Función no implementada aún");
                msg.show(mainWindows);
            } else {
                MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Funcion desconocida " + ed.getFuncion());
                msb.show(mainWindows);
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        whatTimeIsIt = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(Shared.getUser().getLogin() + " @ " + Constants.appName);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFocusable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {

            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setFocusable(false);
        jPanel1.setName("jPanel1");
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });
        jPanel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {

            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jPanel1MouseMoved(evt);
            }
        });
        whatTimeIsIt.setFocusable(false);
        whatTimeIsIt.setName("whatTimeIsIt");
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap(591, Short.MAX_VALUE).addComponent(whatTimeIsIt, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(whatTimeIsIt, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        menuBar.setName("menuBar");
        setJMenuBar(menuBar);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap(608, Short.MAX_VALUE).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
        pack();
    }

    public void createClosingDay(String day) {
        if (!day.isEmpty()) {
            ClosingDay cd = new ClosingDay(day, true);
            if (cd.isOk) {
                mdiPanel.add(cd);
                cd.setVisible(true);
            }
        }
    }

    public void createManualHR(String day) {
        if (!day.isEmpty()) {
            EmployStateTable est = new EmployStateTable(day);
            if (est.isOk) {
                mdiPanel.add(est);
                est.setVisible(true);
            }
        }
    }

    private void formKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            logout();
        }
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        if (logout()) {
            Shared.setUser(null);
        }
    }

    private void jPanel1MouseMoved(java.awt.event.MouseEvent evt) {
        Shared.getScreenSaver().actioned();
    }

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {
    }

    private void formMouseMoved(java.awt.event.MouseEvent evt) {
        Shared.getScreenSaver().actioned();
    }

    private boolean logout() {
        if (Shared.getProcessingWindows() != 0) {
            MessageBox msg = new MessageBox(MessageBox.SGN_IMPORTANT, "No se puede cerrar mientras está trabajando =(. Por favor, espere.");
            msg.show(null);
            return false;
        }
        Object[] options = { "Si", "No" };
        int n = JOptionPane.showOptionDialog(this, "¿Desea salir del sistema?", Constants.appName, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
        if (n == 0) {
            Login l = new Login();
            Shared.centerFrame(l);
            Shared.maximize(l);
            Shared.setUser(null);
            setVisible(false);
            dispose();
            l.setVisible(true);
            return true;
        }
        return false;
    }

    private javax.swing.JPanel jPanel1;

    private javax.swing.JMenuBar menuBar;

    public javax.swing.JLabel whatTimeIsIt;
}

package ops.debuggersupport;

import configlib.exception.FormatException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;
import ops.OPSObject;
import ops.Participant;
import ops.Subscriber;
import ops.Topic;
import opsreflection.OPSFactory;
import org.netbeans.spi.viewmodel.ColumnModel;
import org.netbeans.spi.viewmodel.Model;
import org.netbeans.spi.viewmodel.Models;
import org.netbeans.spi.viewmodel.NodeModel;
import org.netbeans.spi.viewmodel.TableModel;
import org.netbeans.spi.viewmodel.TreeExpansionModel;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.util.Utilities;

/**
 * Top component which displays something.
 */
final class OPSDebuggerTopComponent extends TopComponent {

    private static OPSDebuggerTopComponent instance;

    /** path to the icon used by the component and its open action */
    static final String ICON_PATH = "ops/debuggersupport/opsidlicon.GIF";

    private Subscriber subscriber;

    private JFileChooser fileChooser = new JFileChooser();

    private OPSFactory opsFactory = null;

    Serializable hatt;

    private ClassLoader clsLoader;

    private JTabbedPane tabbedPane;

    private OPSDebuggerTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(OPSDebuggerTopComponent.class, "CTL_OPSDebuggerTopComponent"));
        setToolTipText(NbBundle.getMessage(OPSDebuggerTopComponent.class, "HINT_OPSDebuggerTopComponent"));
        setIcon(Utilities.loadImage(ICON_PATH, true));
        clsLoader = new OPSObject().getClass().getClassLoader();
        tabbedPane = new JTabbedPane();
        tabPanel.add(tabbedPane);
    }

    private void initComponents() {
        jTabbedPane1 = new javax.swing.JTabbedPane();
        openButton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        tabPanel = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        topicComboBox = new javax.swing.JComboBox();
        domainTextField = new javax.swing.JTextField();
        org.openide.awt.Mnemonics.setLocalizedText(openButton, org.openide.util.NbBundle.getMessage(OPSDebuggerTopComponent.class, "OPSDebuggerTopComponent.openButton.text"));
        openButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openButtonActionPerformed(evt);
            }
        });
        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(OPSDebuggerTopComponent.class, "OPSDebuggerTopComponent.jButton1.text"));
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        tabPanel.setLayout(new java.awt.BorderLayout());
        jComboBox1.setEnabled(false);
        topicComboBox.setToolTipText((String) topicComboBox.getSelectedItem());
        domainTextField.setText(org.openide.util.NbBundle.getMessage(OPSDebuggerTopComponent.class, "OPSDebuggerTopComponent.domainTextField.text"));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(topicComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(openButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(domainTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(96, Short.MAX_VALUE)).addComponent(tabPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(openButton).addComponent(jButton1).addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(topicComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(domainTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(tabPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)));
    }

    private void openButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (opsFactory != null) {
            Participant participant = Participant.getInstance(domainTextField.getText());
            Topic topic = participant.createTopic((String) topicComboBox.getSelectedItem());
            subscriber = opsFactory.createSubscriber((String) topicComboBox.getSelectedItem());
            TreeModelImpl tm = new TreeModelImpl(subscriber);
            ColumnModel cm2 = new ValueColumnModel();
            TableModel tbm = new TableModelImpl();
            NodeModel nm = new NodeModelImpl();
            TreeExpansionModel tem = new TreeExpansionModelImpl();
            JComponent treeView = Models.createView(Models.createCompoundModel(Arrays.asList(new Model[] { tm, tbm, nm, tem, cm2 })));
            tabbedPane.addTab(topic.getName() + ":" + domainTextField.getText() + "(" + topic.getDomainAddress() + ")", treeView);
            tabbedPane.setTabComponentAt(tabbedPane.indexOfComponent(treeView), new ButtonTabComponent(tabbedPane));
        }
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        ArrayList<String> topicNames;
        if (opsFactory == null) {
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                if (fileChooser.getSelectedFile() != null) {
                    if (fileChooser.getSelectedFile().getName().endsWith("xml")) {
                        try {
                            opsFactory = new OPSFactory(fileChooser.getSelectedFile());
                            topicNames = opsFactory.listTopicNames();
                            openButton.setEnabled(true);
                            Collections.sort(topicNames);
                            for (String string : topicNames) {
                                topicComboBox.addItem(string);
                            }
                        } catch (FileNotFoundException ex) {
                            Exceptions.printStackTrace(ex);
                        } catch (FormatException ex) {
                            Exceptions.printStackTrace(ex);
                        } catch (IOException ex) {
                            Exceptions.printStackTrace(ex);
                        }
                    }
                }
            }
        }
    }

    private void onNewData(Object arg) {
    }

    private javax.swing.JTextField domainTextField;

    private javax.swing.JButton jButton1;

    private javax.swing.JComboBox jComboBox1;

    private javax.swing.JTabbedPane jTabbedPane1;

    private javax.swing.JButton openButton;

    private javax.swing.JPanel tabPanel;

    private javax.swing.JComboBox topicComboBox;

    public static synchronized OPSDebuggerTopComponent getDefault() {
        return new OPSDebuggerTopComponent();
    }

    public static synchronized OPSDebuggerTopComponent findInstance() {
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ONLY_OPENED;
    }

    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
    }
}

class ButtonTabComponent extends JPanel {

    private final JTabbedPane pane;

    public ButtonTabComponent(final JTabbedPane pane) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        if (pane == null) {
            throw new NullPointerException("TabbedPane is null");
        }
        this.pane = pane;
        setOpaque(false);
        JLabel label = new JLabel() {

            public String getText() {
                int i = pane.indexOfTabComponent(ButtonTabComponent.this);
                if (i != -1) {
                    return pane.getTitleAt(i);
                }
                return null;
            }
        };
        add(label);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        JButton button = new TabButton();
        add(button);
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
    }

    private class TabButton extends JButton implements ActionListener {

        public TabButton() {
            int size = 17;
            setPreferredSize(new Dimension(size, size));
            setToolTipText("close this tab");
            setUI(new BasicButtonUI());
            setContentAreaFilled(false);
            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            addMouseListener(buttonMouseListener);
            setRolloverEnabled(true);
            addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {
            int i = pane.indexOfTabComponent(ButtonTabComponent.this);
            if (i != -1) {
                pane.remove(i);
            }
        }

        public void updateUI() {
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            if (getModel().isPressed()) {
                g2.translate(1, 1);
            }
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.GRAY);
            if (getModel().isRollover()) {
                g2.setColor(Color.BLACK);
            }
            int delta = 6;
            g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
            g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
            g2.dispose();
        }
    }

    private static final MouseListener buttonMouseListener = new MouseAdapter() {

        public void mouseEntered(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(true);
            }
        }

        public void mouseExited(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(false);
            }
        }
    };
}

class TestObject {

    public int a = 1;

    public double b = 2;

    public String hej = "hatten";

    public TestObject2 object2 = new TestObject2();

    public TestObject() {
    }

    public int getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public String getHej() {
        return hej;
    }
}

class TestObject2 {

    public int c = 1;

    public double d = 2;

    public String hej = "Hatten!";
}

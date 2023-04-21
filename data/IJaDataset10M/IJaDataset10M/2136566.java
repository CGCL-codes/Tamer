package leditor.arcanesCodex;

import java.awt.Color;
import leditor.arcanesCodex.interfaceClasses.Plus;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

/**
 *
 * @author cerias
 */
public class CharakterView extends javax.swing.JInternalFrame {

    static int openFrameCount = 0;

    static final int xOffset = 30, yOffset = 30;

    int i;

    private Charakter charaData;

    private database dbData = new database();

    ActionMap aMap = Application.getInstance(leditor.LeditorApp.class).getContext().getActionMap(CharakterView.class, this);

    ResourceMap mgr = Application.getInstance(leditor.LeditorApp.class).getContext().getResourceMap(CharakterView.class);

    GridBagLayout charWindow = new GridBagLayout();

    GridBagConstraints cWindow = new GridBagConstraints();

    ImageIcon plusIcon = createImageIcon("/rem-icon.png", "Hinzufügen");

    private JPanel attributsPane = new JPanel();

    private List<JLabel> latri = new LinkedList<JLabel>();

    private List<JLabel> lartiWerte = new LinkedList<JLabel>();

    private List<JButton> plus = new LinkedList<JButton>();

    private List<JButton> minus = new LinkedList<JButton>();

    GridBagLayout attriGrid = new GridBagLayout();

    GridBagConstraints aCont = new GridBagConstraints();

    private List<Action> actionPlus = new LinkedList<Action>();

    private JPanel faehigkeitenPane = new JPanel();

    private JPanel faehigkeitenListe = new JPanel();

    private List<JLabel> listFah = new LinkedList<JLabel>();

    private List<JLabel> listFahWert = new LinkedList<JLabel>();

    private ArrayList<Faehigkeiten> dbListFah;

    private JButton fahplus = new JButton();

    private JComboBox dropDownFah = new JComboBox();

    private List<JButton> minusF = new LinkedList<JButton>();

    private List<JButton> plusF = new LinkedList<JButton>();

    /** Creates new form CharakterView */
    public CharakterView(Charakter charBox) {
        super("Arcane Codex #" + (++openFrameCount), true, true, true, true);
        charaData = charBox;
        setVisible(true);
        setLayout(charWindow);
        setLocation(xOffset * openFrameCount, yOffset * openFrameCount);
        initComponents();
        mainPanel.setLayout(charWindow);
        setup();
    }

    private void setup() {
        cWindow.anchor = cWindow.NORTH;
        cWindow.gridx = 0;
        cWindow.gridy = 0;
        attributeSetup();
        cWindow.gridy++;
        faehigkeitenSetup();
    }

    private void attributeSetup() {
        attributsPane.setLayout(attriGrid);
        attributsPane.setBorder(BorderFactory.createEtchedBorder(mgr.getColor("attributsPane.border.highlightColor"), null));
        attributsPane.setVisible(true);
        mainPanel.add(attributsPane, cWindow);
        aCont.fill = GridBagConstraints.HORIZONTAL;
        aCont.gridwidth = 8;
        aCont.gridx = 0;
        aCont.gridy = 0;
        attributsPane.add(new JLabel("Attribute"), aCont);
        aCont.ipadx = 10;
        aCont.gridwidth = 1;
        aCont.weightx = 0.5;
        aCont.gridx = 0;
        aCont.gridy++;
        for (i = 0; i < charaData.getAttribute().size(); i++) {
            latri.add(new JLabel(charaData.getAttribute().get(i).getAttribut().toString()));
            latri.get(i).setName(charaData.getAttribute().get(i).getAttribut().toString());
            latri.get(i).setVisible(true);
            lartiWerte.add(new JLabel("" + charaData.getAttribute().get(i).getWert()));
            lartiWerte.get(i).setName(charaData.getAttribute().get(i).getAttribut().toString());
            lartiWerte.get(i).setVisible(true);
            plus.add(new JButton());
            plus.get(i).addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    JButton tmp = (JButton) e.getSource();
                    charaData.riseAttribut(tmp.getName().toString());
                    for (int i = 0; i < lartiWerte.size(); i++) {
                        if (lartiWerte.get(i).getName().equals(charaData.getAttribute().get(i).getAttribut())) {
                            lartiWerte.get(i).setText("" + charaData.getAttribute().get(i).getWert());
                        }
                    }
                }
            });
            plus.get(i).setName(latri.get(i).getName().toString());
            plus.get(i).setIcon(mgr.getIcon("plus.icon"));
            plus.get(i).setBorder(null);
            plus.get(i).setContentAreaFilled(false);
            plus.get(i).setVisible(true);
            minus.add(new JButton());
            minus.get(i).setName(latri.get(i).getName().toString());
            minus.get(i).setIcon(mgr.getIcon("minus.icon"));
            minus.get(i).setBorder(null);
            minus.get(i).setContentAreaFilled(false);
            minus.get(i).addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    JButton tmp = (JButton) e.getSource();
                    charaData.downAttribut(tmp.getName().toString());
                    for (int i = 0; i < lartiWerte.size(); i++) {
                        if (lartiWerte.get(i).getName().equals(charaData.getAttribute().get(i).getAttribut())) {
                            lartiWerte.get(i).setText("" + charaData.getAttribute().get(i).getWert());
                        }
                    }
                }
            });
            aCont.gridx++;
            attributsPane.add(latri.get(i), aCont);
            aCont.gridx++;
            attributsPane.add(plus.get(i), aCont);
            aCont.gridx++;
            attributsPane.add(lartiWerte.get(i), aCont);
            aCont.gridx++;
            attributsPane.add(minus.get(i), aCont);
            if (i % 2 != 0) {
                aCont.gridy++;
                aCont.gridx = 0;
            }
        }
    }

    private void faehigkeitenSetup() {
        faehigkeitenPane.setLayout(attriGrid);
        faehigkeitenPane.setBorder(BorderFactory.createEtchedBorder(mgr.getColor("attributsPane.border.highlightColor"), null));
        faehigkeitenPane.setVisible(true);
        mainPanel.add(faehigkeitenPane, cWindow);
        aCont.fill = GridBagConstraints.HORIZONTAL;
        aCont.gridwidth = 2;
        aCont.gridx = 0;
        aCont.gridy = 0;
        faehigkeitenPane.add(new JLabel("Fähigkeiten"), aCont);
        aCont.gridx++;
        aCont.gridx++;
        dbListFah = dbData.createFaehigkeiten();
        for (i = 0; i < dbListFah.size(); i++) {
            dropDownFah.addItem(dbListFah.get(i).getName());
        }
        dropDownFah.setVisible(true);
        faehigkeitenPane.add(dropDownFah, aCont);
        aCont.gridx++;
        fahplus.setIcon(mgr.getIcon("plus.icon"));
        fahplus.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                charaData.addFaehigkeiten(dropDownFah.getSelectedItem().toString());
                if (charaData.addFaehigkeiten(dropDownFah.getSelectedItem().toString())) {
                    listFah.add(new JLabel(charaData.getFaehigkeiten().get(i).getName().toString()));
                    faehigkeitenListe.removeAll();
                    faehigkeitenListePaint();
                }
            }
        });
        faehigkeitenPane.add(fahplus);
        aCont.gridx = 0;
        aCont.gridy++;
        faehigkeitenListe.setLayout(attriGrid);
        faehigkeitenListe.setVisible(true);
        faehigkeitenPane.add(faehigkeitenListe, aCont);
        faehigkeitenListePaint();
    }

    protected ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = getClass().getResource(path);
        System.out.print(imgURL);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        LeditorPUEntityManager = java.beans.Beans.isDesignTime() ? null : javax.persistence.Persistence.createEntityManagerFactory("LeditorPU").createEntityManager();
        faehigkeitenQuery = java.beans.Beans.isDesignTime() ? null : LeditorPUEntityManager.createQuery("SELECT f FROM Faehigkeiten f");
        faehigkeitenList = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : faehigkeitenQuery.getResultList();
        mainPanel = new javax.swing.JPanel();
        setMaximumSize(new java.awt.Dimension(800, 600));
        setMinimumSize(new java.awt.Dimension(200, 303));
        setName("Form");
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(leditor.LeditorApp.class).getContext().getResourceMap(CharakterView.class);
        mainPanel.setBackground(resourceMap.getColor("mainPanel.background"));
        mainPanel.setName("mainPanel");
        mainPanel.setLayout(new java.awt.GridBagLayout());
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE));
    }

    @org.jdesktop.application.Action
    public void checkThis() {
        System.out.println("test");
        charaData.riseAttribut("Staerke");
    }

    private void faehigkeitenListePaint() {
        faehigkeitenListe.list();
        aCont.gridy++;
        for (i = 0; i < charaData.getFaehigkeiten().size(); i++) {
            listFah.add(new JLabel(charaData.getFaehigkeiten().get(i).getName().toString()));
            listFah.get(i).setVisible(true);
            listFahWert.add(new JLabel("" + charaData.getFaehigkeiten().get(i).getWert()));
            listFahWert.get(i).setName(charaData.getFaehigkeiten().get(i).getAttribut().toString());
            listFahWert.get(i).setVisible(true);
            plusF.add(new JButton());
            plusF.get(i).setBorder(null);
            plusF.get(i).setContentAreaFilled(false);
            plusF.get(i).setIcon(mgr.getIcon("Plus.icon"));
            plusF.get(i).setVisible(true);
            minusF.add(new JButton());
            minusF.get(i).setBorder(null);
            minusF.get(i).setContentAreaFilled(false);
            minusF.get(i).setIcon(mgr.getIcon("minus.icon"));
            aCont.gridx++;
            faehigkeitenListe.add(listFah.get(i), aCont);
            aCont.gridx++;
            faehigkeitenListe.add(plusF.get(i), aCont);
            aCont.gridx++;
            faehigkeitenListe.add(listFahWert.get(i), aCont);
            aCont.gridx++;
            faehigkeitenListe.add(minusF.get(i), aCont);
            aCont.gridy++;
            aCont.gridx = 0;
        }
    }

    private javax.persistence.EntityManager LeditorPUEntityManager;

    private java.util.List<leditor.arcanesCodex.Faehigkeiten> faehigkeitenList;

    private javax.persistence.Query faehigkeitenQuery;

    private javax.swing.JPanel mainPanel;
}

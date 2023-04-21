package sightmusic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 *
 * @author  sandie
 */
public class PlanPanel extends javax.swing.JPanel {

    private final String orangeIcon = "src/gui/matchMii/images/orange.jpg";

    private final String whiteIcon = "src/gui/matchMii/images/notSelected.jpg";

    JButton currentButton;

    static PlanPanel instance;

    ETAT etatCourant = ETAT.HOMEONE;

    enum ETAT {

        HOMEONE, HOMETWO, HOMEMULTI, PROFILONE, PROFILTWO, PROFILMULTI, HOTBASEONE, HOTBASETWO, HOTBASEMULTI, FLOWMAPONE, FLOWMAPTWO, FLOWMAPMULTI
    }

    public static PlanPanel getInstance() {
        if (instance == null) instance = new PlanPanel();
        return instance;
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /** Creates new form PlanPanel */
    private PlanPanel() {
        initComponents();
    }

    private void initComponents() {
        profil1 = new javax.swing.JButton();
        hlabel = new javax.swing.JLabel();
        plabel = new javax.swing.JLabel();
        flabel = new javax.swing.JLabel();
        label1 = new javax.swing.JLabel();
        label2 = new javax.swing.JLabel();
        label3 = new javax.swing.JLabel();
        hot1 = new javax.swing.JButton();
        profil2 = new javax.swing.JButton();
        hot2 = new javax.swing.JButton();
        flow2 = new javax.swing.JButton();
        flow1 = new javax.swing.JButton();
        profilN = new javax.swing.JButton();
        hotN = new javax.swing.JButton();
        flowN = new javax.swing.JButton();
        home1 = new javax.swing.JButton();
        home2 = new javax.swing.JButton();
        homeN = new javax.swing.JButton();
        plabel1 = new javax.swing.JLabel();
        currentButton = home1;
        home1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setHomeOne();
            }
        });
        home2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setHomeTwo();
            }
        });
        homeN.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setHomeMulti();
            }
        });
        profil1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setProfilOne();
            }
        });
        profil2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setProfilTwo();
            }
        });
        profilN.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setProfilMulti();
            }
        });
        hot1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setHotBaseOne();
            }
        });
        hot2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setHotBaseTwo();
            }
        });
        hotN.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setHotBaseMulti();
            }
        });
        flow1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setFlowChartOne();
            }
        });
        flow2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setFlowChartTwo();
            }
        });
        flowN.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setFlowChartMulti();
            }
        });
        home1.setToolTipText("Home individuel");
        home2.setToolTipText("Home comparatifs");
        homeN.setToolTipText("Home multiples");
        profil1.setToolTipText("Profil individuel");
        profil2.setToolTipText("Profils comparatifs");
        profilN.setToolTipText("Profils multiples");
        hot1.setToolTipText("HotBase individuel");
        hot2.setToolTipText("HotBase comparatifs");
        hotN.setToolTipText("Profil multiples");
        flow1.setToolTipText("Flowchart individuel");
        flow2.setToolTipText("Flowchart comparatifs");
        flowN.setToolTipText("Flowchart multiples");
        setBackground(new java.awt.Color(0, 0, 51));
        setForeground(new java.awt.Color(255, 255, 255));
        hlabel.setForeground(new java.awt.Color(255, 255, 255));
        hlabel.setText("P");
        plabel.setForeground(new java.awt.Color(255, 255, 255));
        plabel.setText("H");
        flabel.setForeground(new java.awt.Color(255, 255, 255));
        flabel.setText("F");
        label1.setForeground(new java.awt.Color(255, 255, 255));
        label1.setText("1");
        label2.setForeground(new java.awt.Color(255, 255, 255));
        label2.setText("2");
        label3.setForeground(new java.awt.Color(255, 255, 255));
        label3.setText("N");
        home1.setIcon(new javax.swing.ImageIcon(orangeIcon));
        home2.setIcon(new javax.swing.ImageIcon(whiteIcon));
        homeN.setIcon(new javax.swing.ImageIcon(whiteIcon));
        hot1.setIcon(new javax.swing.ImageIcon(whiteIcon));
        hot2.setIcon(new javax.swing.ImageIcon(whiteIcon));
        hotN.setIcon(new javax.swing.ImageIcon(whiteIcon));
        profil1.setIcon(new javax.swing.ImageIcon(whiteIcon));
        profil2.setIcon(new javax.swing.ImageIcon(whiteIcon));
        profilN.setIcon(new javax.swing.ImageIcon(whiteIcon));
        flow1.setIcon(new javax.swing.ImageIcon(whiteIcon));
        flow2.setIcon(new javax.swing.ImageIcon(whiteIcon));
        flowN.setIcon(new javax.swing.ImageIcon(whiteIcon));
        plabel1.setForeground(new java.awt.Color(255, 255, 255));
        plabel1.setText("H");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(home1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(home2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(homeN, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(plabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(8, 8, 8))).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(layout.createSequentialGroup().addGap(14, 14, 14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(profil1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(profil2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(profilN, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14, 14, 14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(hot1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(hot2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(hotN, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))).addGroup(layout.createSequentialGroup().addGap(29, 29, 29).addComponent(hlabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(plabel, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(14, 14, 14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(flow2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(flow1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(flowN, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))).addGroup(layout.createSequentialGroup().addGap(26, 26, 26).addComponent(flabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap(19, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(hlabel).addComponent(plabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(flabel, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(plabel, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addComponent(hot1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(hot2, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addComponent(home1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(home2, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(profil2, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(flow2, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(flow1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(profil1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(homeN, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(profilN, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(hotN, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(flowN, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(25, Short.MAX_VALUE)));
    }

    private javax.swing.JLabel flabel;

    private javax.swing.JButton flow1;

    private javax.swing.JButton flow2;

    private javax.swing.JButton flowN;

    private javax.swing.JLabel hlabel;

    private javax.swing.JButton home1;

    private javax.swing.JButton home2;

    private javax.swing.JButton homeN;

    private javax.swing.JButton hot1;

    private javax.swing.JButton hot2;

    private javax.swing.JButton hotN;

    private javax.swing.JLabel label1;

    private javax.swing.JLabel label2;

    private javax.swing.JLabel label3;

    private javax.swing.JLabel plabel;

    private javax.swing.JLabel plabel1;

    private javax.swing.JButton profil1;

    private javax.swing.JButton profil2;

    private javax.swing.JButton profilN;

    private void resetIcons() {
        home1.setIcon(new javax.swing.ImageIcon(whiteIcon));
        home2.setIcon(new javax.swing.ImageIcon(whiteIcon));
        homeN.setIcon(new javax.swing.ImageIcon(whiteIcon));
        hot1.setIcon(new javax.swing.ImageIcon(whiteIcon));
        hot2.setIcon(new javax.swing.ImageIcon(whiteIcon));
        hotN.setIcon(new javax.swing.ImageIcon(whiteIcon));
        profil1.setIcon(new javax.swing.ImageIcon(whiteIcon));
        profil2.setIcon(new javax.swing.ImageIcon(whiteIcon));
        profilN.setIcon(new javax.swing.ImageIcon(whiteIcon));
        flow1.setIcon(new javax.swing.ImageIcon(whiteIcon));
        flow2.setIcon(new javax.swing.ImageIcon(whiteIcon));
        flowN.setIcon(new javax.swing.ImageIcon(whiteIcon));
    }

    public void setHomeOne() {
        resetIcons();
        home1.setIcon(new javax.swing.ImageIcon(orangeIcon));
        etatCourant = ETAT.HOMEONE;
        MUI.getInstance().HomeOne();
    }

    public void setHomeTwo() {
        resetIcons();
        home2.setIcon(new javax.swing.ImageIcon(orangeIcon));
        etatCourant = ETAT.HOMETWO;
        MUI.getInstance().HomeTwo();
    }

    public void setHomeMulti() {
        resetIcons();
        homeN.setIcon(new javax.swing.ImageIcon(orangeIcon));
        etatCourant = ETAT.HOMEMULTI;
        MUI.getInstance().HomeMulti();
    }

    public void setProfilOne() {
        resetIcons();
        profil1.setIcon(new javax.swing.ImageIcon(orangeIcon));
        etatCourant = ETAT.PROFILONE;
        MUI.getInstance().ProfilOne();
    }

    public void setProfilTwo() {
        resetIcons();
        profil2.setIcon(new javax.swing.ImageIcon(orangeIcon));
        etatCourant = ETAT.PROFILTWO;
        MUI.getInstance().ProfilTwo();
    }

    public void setProfilMulti() {
        resetIcons();
        profilN.setIcon(new javax.swing.ImageIcon(orangeIcon));
        etatCourant = ETAT.PROFILMULTI;
        MUI.getInstance().ProfilMulti();
    }

    public void setHotBaseOne() {
        resetIcons();
        hot1.setIcon(new javax.swing.ImageIcon(orangeIcon));
        etatCourant = ETAT.HOTBASEONE;
        MUI.getInstance().HotBaseOne();
    }

    public void setHotBaseTwo() {
        resetIcons();
        hot2.setIcon(new javax.swing.ImageIcon(orangeIcon));
        etatCourant = ETAT.HOTBASETWO;
        MUI.getInstance().HotBaseTwo();
    }

    public void setHotBaseMulti() {
        resetIcons();
        hotN.setIcon(new javax.swing.ImageIcon(orangeIcon));
        etatCourant = ETAT.HOTBASEMULTI;
        MUI.getInstance().HotBaseMulti();
    }

    public void setFlowChartOne() {
        resetIcons();
        flow1.setIcon(new javax.swing.ImageIcon(orangeIcon));
        etatCourant = ETAT.FLOWMAPONE;
        MUI.getInstance().FlowChartOne();
    }

    public void setFlowChartTwo() {
        resetIcons();
        flow2.setIcon(new javax.swing.ImageIcon(orangeIcon));
        etatCourant = ETAT.FLOWMAPTWO;
        MUI.getInstance().FlowChartTwo();
    }

    public void setFlowChartMulti() {
        resetIcons();
        flowN.setIcon(new javax.swing.ImageIcon(orangeIcon));
        etatCourant = ETAT.FLOWMAPMULTI;
        MUI.getInstance().FlowChartMulti();
    }

    public void setNext() {
        if (etatCourant == ETAT.HOMEONE) {
            setProfilOne();
        } else if (etatCourant == ETAT.HOMETWO) {
            setProfilTwo();
        } else if (etatCourant == ETAT.HOMEMULTI) {
            setProfilMulti();
        } else if (etatCourant == ETAT.PROFILONE) {
            setHotBaseOne();
        } else if (etatCourant == ETAT.PROFILTWO) {
            setHotBaseTwo();
        } else if (etatCourant == ETAT.PROFILMULTI) {
            setHotBaseMulti();
        } else if (etatCourant == ETAT.HOTBASEONE) {
            setFlowChartOne();
        } else if (etatCourant == ETAT.HOTBASETWO) {
            setFlowChartTwo();
        } else if (etatCourant == ETAT.HOTBASEMULTI) {
            setFlowChartMulti();
        } else if (etatCourant == ETAT.FLOWMAPONE) {
            setHomeOne();
        } else if (etatCourant == ETAT.FLOWMAPTWO) {
            setHomeTwo();
        } else if (etatCourant == ETAT.FLOWMAPMULTI) {
            setHomeMulti();
        }
    }
}

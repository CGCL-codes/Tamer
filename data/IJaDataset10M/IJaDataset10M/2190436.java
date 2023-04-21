package EVEOverWatch;

import java.awt.Color;

/**
 *
 * @author  woodshop2300
 */
public class EVEOverWatchWidgetFrame extends javax.swing.JFrame {

    /** Creates new form EVEOverWatchWidgetFrame */
    public EVEOverWatchWidgetFrame(String characterID, String userID, String apiKey) {
        initComponents();
        setBackground(new Color(0, 0, 0, 0));
        setSize(400, 200);
        pack();
        eVEOverWatchWidgetPanal1.setApiKey(apiKey);
        eVEOverWatchWidgetPanal1.setCharacterID(characterID);
        eVEOverWatchWidgetPanal1.setUserID(userID);
        eVEOverWatchWidgetPanal1.reSync();
        MouseDragAdaptor da = new MouseDragAdaptor(this);
        eVEOverWatchWidgetPanal1.addMouseMotionListener(da);
        eVEOverWatchWidgetPanal1.addMouseListener(da);
    }

    private void initComponents() {
        eVEOverWatchWidgetPanal1 = new EVEOverWatch.EVEOverWatchWidgetPanal();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        eVEOverWatchWidgetPanal1.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                eVEOverWatchWidgetPanal1MousePressed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout eVEOverWatchWidgetPanal1Layout = new org.jdesktop.layout.GroupLayout(eVEOverWatchWidgetPanal1);
        eVEOverWatchWidgetPanal1.setLayout(eVEOverWatchWidgetPanal1Layout);
        eVEOverWatchWidgetPanal1Layout.setHorizontalGroup(eVEOverWatchWidgetPanal1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(0, 395, Short.MAX_VALUE));
        eVEOverWatchWidgetPanal1Layout.setVerticalGroup(eVEOverWatchWidgetPanal1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(0, 157, Short.MAX_VALUE));
        getContentPane().add(eVEOverWatchWidgetPanal1, java.awt.BorderLayout.CENTER);
        pack();
    }

    private void eVEOverWatchWidgetPanal1MousePressed(java.awt.event.MouseEvent evt) {
        if (eVEOverWatchWidgetPanal1.clickedClose(evt.getX(), evt.getY())) this.dispose();
    }

    private EVEOverWatch.EVEOverWatchWidgetPanal eVEOverWatchWidgetPanal1;
}

package org.sidora.opcit;

import java.io.Serializable;
import java.util.logging.Logger;
import javax.swing.UIManager;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
final class NotesTopComponent extends TopComponent {

    private static NotesTopComponent instance;

    private static final String PREFERRED_ID = "NotesTopComponent";

    private NotesTopComponent() {
        System.setProperty("Quaqua.tabLayoutPolicy", "wrap");
        try {
            UIManager.setLookAndFeel("ch.randelshofer.quaqua.QuaquaLookAndFeel");
        } catch (Exception e) {
        }
        initComponents();
        setName(NbBundle.getMessage(NotesTopComponent.class, "CTL_NotesTopComponent"));
        setToolTipText(NbBundle.getMessage(NotesTopComponent.class, "HINT_NotesTopComponent"));
    }

    private void initComponents() {
        topComponent1 = new org.openide.windows.TopComponent();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListContext = new javax.swing.JList();
        jListContext.putClientProperty("Quaqua.List.style", "striped");
        jButtonDelFeature = new javax.swing.JButton();
        jButtonDelFeature.putClientProperty("Quaqua.Button.style", "square");
        jButtonDelFeature1 = new javax.swing.JButton();
        jButtonDelFeature1.putClientProperty("Quaqua.Button.style", "square");
        jListContext.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListContextValueChanged(evt);
            }
        });
        jListContext.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListContextMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jListContext);
        org.openide.awt.Mnemonics.setLocalizedText(jButtonDelFeature, org.openide.util.NbBundle.getMessage(NotesTopComponent.class, "NotesTopComponent.jButtonDelFeature.text"));
        jButtonDelFeature.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDelFeatureActionPerformed(evt);
            }
        });
        org.openide.awt.Mnemonics.setLocalizedText(jButtonDelFeature1, org.openide.util.NbBundle.getMessage(NotesTopComponent.class, "NotesTopComponent.jButtonDelFeature1.text"));
        jButtonDelFeature1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDelFeature1ActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout topComponent1Layout = new javax.swing.GroupLayout(topComponent1);
        topComponent1.setLayout(topComponent1Layout);
        topComponent1Layout.setHorizontalGroup(topComponent1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, topComponent1Layout.createSequentialGroup().addContainerGap(82, Short.MAX_VALUE).addComponent(jButtonDelFeature1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jButtonDelFeature)));
        topComponent1Layout.setVerticalGroup(topComponent1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, topComponent1Layout.createSequentialGroup().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(topComponent1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButtonDelFeature).addComponent(jButtonDelFeature1))));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(topComponent1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(topComponent1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
    }

    private void jListContextValueChanged(javax.swing.event.ListSelectionEvent evt) {
    }

    private void jListContextMouseClicked(java.awt.event.MouseEvent evt) {
    }

    private void jButtonDelFeatureActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jButtonDelFeature1ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private javax.swing.JButton jButtonDelFeature;

    private javax.swing.JButton jButtonDelFeature1;

    private javax.swing.JList jListContext;

    private javax.swing.JScrollPane jScrollPane1;

    private org.openide.windows.TopComponent topComponent1;

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link findInstance}.
     */
    public static synchronized NotesTopComponent getDefault() {
        if (instance == null) {
            instance = new NotesTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the NotesTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized NotesTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(NotesTopComponent.class.getName()).warning("Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof NotesTopComponent) {
            return (NotesTopComponent) win;
        }
        Logger.getLogger(NotesTopComponent.class.getName()).warning("There seem to be multiple components with the '" + PREFERRED_ID + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
    }

    /** replaces this in object stream */
    @Override
    public Object writeReplace() {
        return new ResolvableHelper();
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    static final class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;

        public Object readResolve() {
            return NotesTopComponent.getDefault();
        }
    }
}

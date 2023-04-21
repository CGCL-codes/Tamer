package filereadtest;

import java.awt.Desktop;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import myutils.ErrDialog;
import myutils.ErrUtils;
import myutils.GuiUtils;
import org.jdesktop.application.Action;

public class AboutBox extends javax.swing.JDialog implements KeyEventDispatcher {

    public AboutBox(java.awt.Frame parent, String appName, String appVersion) {
        super(parent);
        try {
            initComponents();
            getRootPane().setDefaultButton(closeButton);
            final String resourceName = "resources/about.html";
            java.net.URL aboutTextUrl = getClass().getResource(resourceName);
            if (aboutTextUrl == null) throw new Exception("Cannot load resource \"" + resourceName + "\".");
            String aboutText = myutils.Misc.readHtmlResourceAsString(aboutTextUrl);
            jEditorPaneAbout.setContentType("text/html");
            aboutText = aboutText.replace("$(APP_NAME)", appName);
            aboutText = aboutText.replace("$(APP_VERSION)", appVersion);
            jEditorPaneAbout.setText(aboutText);
            jEditorPaneAbout.setCaretPosition(0);
            GuiUtils.addStandardPopupMenuAndUndoSupport(jEditorPaneAbout);
            jEditorPaneAbout.addHyperlinkListener(new HyperlinkListener() {

                @Override
                public void hyperlinkUpdate(HyperlinkEvent hlinkEvt) {
                    try {
                        if (hlinkEvt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                            if (!Desktop.isDesktopSupported()) throw new Exception("Cannot open link: this system does not support opening web links.");
                            Desktop desktop = Desktop.getDesktop();
                            desktop.browse(hlinkEvt.getURL().toURI());
                        }
                    } catch (Throwable e) {
                        ErrDialog.errorDialog(getContentPane(), ErrUtils.getExceptionMessage(e));
                    }
                }
            });
        } catch (Throwable e) {
            setVisible(false);
            throw new RuntimeException("Error initialising the about window: " + ErrUtils.getExceptionMessage(e));
        }
    }

    public void init() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
    }

    @Action
    public void closeAboutBox() {
        setVisible(false);
        dispose();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UNDEFINED && e.getKeyChar() == KeyEvent.VK_ESCAPE) {
            if (GuiUtils.isFocusInThisDialog(this)) {
                e.consume();
                closeAboutBox();
                return true;
            }
        }
        return false;
    }

    private void initComponents() {
        closeButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jEditorPaneAbout = new javax.swing.JEditorPane();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(filereadtest.FileReadTestApp.class).getContext().getResourceMap(AboutBox.class);
        setTitle(resourceMap.getString("title"));
        setMinimumSize(new java.awt.Dimension(400, 300));
        setModal(true);
        setName("aboutBox");
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(filereadtest.FileReadTestApp.class).getContext().getActionMap(AboutBox.class, this);
        closeButton.setAction(actionMap.get("closeAboutBox"));
        closeButton.setToolTipText(null);
        closeButton.setName("closeButton");
        jScrollPane2.setName("jScrollPane2");
        jEditorPaneAbout.setEditable(false);
        jEditorPaneAbout.setName("jEditorPaneAbout");
        jScrollPane2.setViewportView(jEditorPaneAbout);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE).addComponent(closeButton)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(closeButton).addContainerGap()));
        pack();
    }

    private void formWindowClosed(java.awt.event.WindowEvent evt) {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this);
    }

    private javax.swing.JButton closeButton;

    private javax.swing.JEditorPane jEditorPaneAbout;

    private javax.swing.JScrollPane jScrollPane2;
}

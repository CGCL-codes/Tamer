package datasoul.datashow;

import datasoul.serviceitems.ServiceItem;
import datasoul.config.ConfigObj;
import datasoul.config.DisplayControlConfig;
import datasoul.render.ContentManager;
import datasoul.util.ObjectManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author  Administrador
 */
public class LivePanel extends javax.swing.JPanel implements ListSelectionListener {

    private AutomaticChanger automaticChanger;

    private int lastSelectedIndex;

    /**
     * Creates new form LivePanel
     */
    public LivePanel() {
        initComponents();
        serviceItemTable1.addTableListener(this);
        automaticChanger = new AutomaticChanger();
        serviceItemTable1.addTableKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                evtKeyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    public void setFocusInTable() {
        serviceItemTable1.setFocusInTable();
    }

    public void showItem(ServiceItem serviceItem, boolean backwards) {
        ContentManager cm = ContentManager.getInstance();
        serviceItem.showItem();
        if (ConfigObj.getActiveInstance().getMonitorOutput()) {
            cm.setTemplateMonitorLive(serviceItem.getDefaultMonitorTemplate());
            ObjectManager.getInstance().getTimerControlPanel().setTimerFromServiceItem(serviceItem.getDuration());
        }
        if (backwards) {
            this.serviceItemTable1.setServiceItem(serviceItem, serviceItem.getRowCount() - 1);
        } else {
            this.serviceItemTable1.setServiceItem(serviceItem, 0);
        }
        updateContentValues();
        cm.saveTransitionImage();
        cm.slideChange(0);
    }

    private void initComponents() {
        serviceItemTable1 = new datasoul.serviceitems.ServiceItemTable();
        jPanel1 = new javax.swing.JPanel();
        cbAutoChange = new javax.swing.JCheckBox();
        spnTimer = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        setBorder(null);
        setDoubleBuffered(false);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("datasoul/internationalize");
        cbAutoChange.setText(bundle.getString("CHANGE EVERY"));
        cbAutoChange.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cbAutoChange.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbAutoChangeActionPerformed(evt);
            }
        });
        spnTimer.setModel(new SpinnerNumberModel(1, 1, 9999, 1));
        jLabel1.setText(bundle.getString("SECONDS"));
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(cbAutoChange).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(spnTimer, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel1).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(spnTimer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cbAutoChange).addComponent(jLabel1)));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(serviceItemTable1, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(serviceItemTable1, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
    }

    private void cbAutoChangeActionPerformed(java.awt.event.ActionEvent evt) {
        if (cbAutoChange.isSelected()) {
            automaticChanger.startChanging(Integer.parseInt(spnTimer.getValue().toString()) * 1000);
        } else {
            automaticChanger.stopChanging();
        }
    }

    public void setMediaControlsEnabled(boolean b) {
        serviceItemTable1.setMediaControlsEnabled(b);
    }

    public void valueChanged(ListSelectionEvent e) {
        if (serviceItemTable1.getSlideIndex() == lastSelectedIndex || serviceItemTable1.getSlideIndex() < 0) {
            return;
        } else {
            lastSelectedIndex = serviceItemTable1.getSlideIndex();
        }
        ContentManager cm = ContentManager.getInstance();
        cm.saveTransitionImage();
        updateContentValues();
        cm.slideChange(DisplayControlConfig.getInstance().getSlideTransitionTime());
    }

    public void updateContentValues() {
        ContentManager cm = ContentManager.getInstance();
        cm.setSlideLive(serviceItemTable1.getSlideText());
        cm.setNextSlideLive(serviceItemTable1.getNextSlideText());
        cm.setActiveImageLive(serviceItemTable1.getSlideImage());
        cm.setNextImageLive(serviceItemTable1.getNextSlideImage());
    }

    public void serviceNextSlide() {
        int count = serviceItemTable1.getSlideCount();
        int index = serviceItemTable1.getSlideIndex();
        if (index < count - 1) {
            serviceItemTable1.setSlideIndex(index + 1);
        } else {
            goToNextServiceItem();
        }
    }

    private void goToNextServiceItem() {
        if (ObjectManager.getInstance().getDatasoulMainForm().goToNextServiceItem()) {
            if (!DisplayControlConfig.getInstance().getAutomaticGoLiveBool()) {
                PreviewPanel pp = ObjectManager.getInstance().getPreviewPanel();
                pp.goLive(false);
            }
        }
    }

    public void servicePreviousSlide() {
        int index = serviceItemTable1.getSlideIndex();
        if (index > 0) {
            serviceItemTable1.setSlideIndex(index - 1);
        } else {
            goToPreviousServiceItem();
        }
    }

    private void goToPreviousServiceItem() {
        if (ObjectManager.getInstance().getDatasoulMainForm().goToPreviousServiceItem()) {
            if (!DisplayControlConfig.getInstance().getAutomaticGoLiveBool()) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        PreviewPanel pp = ObjectManager.getInstance().getPreviewPanel();
                        pp.goLive(true);
                    }
                });
            }
        }
    }

    public void notifyVideoEnd() {
        serviceItemTable1.notifyVideoEnd();
    }

    public void evtKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN && serviceItemTable1.getSlideIndex() + 1 >= serviceItemTable1.getSlideCount()) {
            goToNextServiceItem();
        }
        if (e.getKeyCode() == KeyEvent.VK_UP && serviceItemTable1.getSlideIndex() == 0) {
            goToPreviousServiceItem();
        }
    }

    private javax.swing.JCheckBox cbAutoChange;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JPanel jPanel1;

    private datasoul.serviceitems.ServiceItemTable serviceItemTable1;

    private javax.swing.JSpinner spnTimer;

    private class AutomaticChanger extends Thread {

        boolean stopThread = false;

        long timer;

        public AutomaticChanger() {
            this.start();
        }

        public void startChanging(int timer) {
            this.timer = timer;
            stopThread = false;
            synchronized (this) {
                this.notify();
            }
        }

        public void stopChanging() {
            stopThread = true;
            this.interrupt();
        }

        private void change() {
            int maxSlide = serviceItemTable1.getSlideCount();
            int showSlide = serviceItemTable1.getSlideIndex() + 1;
            if (showSlide >= maxSlide) {
                showSlide = 0;
            }
            serviceItemTable1.setSlideIndex(showSlide);
        }

        public void run() {
            long t1, t2;
            while (true) {
                synchronized (this) {
                    try {
                        this.wait();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                while (!stopThread) {
                    t1 = System.currentTimeMillis();
                    change();
                    t2 = System.currentTimeMillis();
                    if ((timer - (t2 - t1)) > 1) {
                        try {
                            Thread.sleep(timer - (t2 - t1));
                        } catch (InterruptedException ex) {
                        }
                    }
                }
            }
        }
    }
}

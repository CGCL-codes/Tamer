package monster2;

import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import org.jdesktop.application.Task;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.RollbackException;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jdesktop.beansbinding.AbstractBindingListener;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.PropertyStateEvent;

/**
 * The application's main frame.
 */
public class MainFrm extends FrameView {

    public MainFrm(SingleFrameApplication app) {
        super(app);
        initComponents();
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
        masterTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                firePropertyChange("recordSelected", !isRecordSelected(), isRecordSelected());
            }
        });
        bindingGroup.addBindingListener(new AbstractBindingListener() {

            @Override
            public void targetChanged(Binding binding, PropertyStateEvent event) {
                setSaveNeeded(true);
            }
        });
        entityManager.getTransaction().begin();
    }

    public boolean isSaveNeeded() {
        return saveNeeded;
    }

    private void setSaveNeeded(boolean saveNeeded) {
        if (saveNeeded != this.saveNeeded) {
            this.saveNeeded = saveNeeded;
            firePropertyChange("saveNeeded", !saveNeeded, saveNeeded);
        }
    }

    public boolean isRecordSelected() {
        return masterTable.getSelectedRow() != -1;
    }

    @Action
    public void newRecord() {
        entity.Product p = new entity.Product();
        entityManager.persist(p);
        list.add(p);
        int row = list.size() - 1;
        masterTable.setRowSelectionInterval(row, row);
        masterTable.scrollRectToVisible(masterTable.getCellRect(row, 0, true));
        setSaveNeeded(true);
    }

    @Action(enabledProperty = "recordSelected")
    public void deleteRecord() {
        int[] selected = masterTable.getSelectedRows();
        List<entity.Product> toRemove = new ArrayList<entity.Product>(selected.length);
        for (int idx = 0; idx < selected.length; idx++) {
            entity.Product p = list.get(masterTable.convertRowIndexToModel(selected[idx]));
            toRemove.add(p);
            entityManager.remove(p);
        }
        list.removeAll(toRemove);
        setSaveNeeded(true);
    }

    @Action(enabledProperty = "saveNeeded")
    public Task save() {
        return new SaveTask(getApplication());
    }

    private class SaveTask extends Task {

        SaveTask(org.jdesktop.application.Application app) {
            super(app);
        }

        @Override
        protected Void doInBackground() {
            try {
                entityManager.getTransaction().commit();
                entityManager.getTransaction().begin();
            } catch (RollbackException rex) {
                rex.printStackTrace();
                entityManager.getTransaction().begin();
                List<entity.Product> merged = new ArrayList<entity.Product>(list.size());
                for (entity.Product p : list) {
                    merged.add(entityManager.merge(p));
                }
                list.clear();
                list.addAll(merged);
            }
            return null;
        }

        @Override
        protected void finished() {
            setSaveNeeded(false);
        }
    }

    /**
     * An example action method showing how to create asynchronous tasks
     * (running on background) and how to show their progress. Note the
     * artificial 'Thread.sleep' calls making the task long enough to see the
     * progress visualization - remove the sleeps for real application.
     */
    @Action
    public Task refresh() {
        return new RefreshTask(getApplication());
    }

    private class RefreshTask extends Task {

        RefreshTask(org.jdesktop.application.Application app) {
            super(app);
        }

        @SuppressWarnings("unchecked")
        @Override
        protected Void doInBackground() {
            try {
                setProgress(0, 0, 4);
                setMessage("Rolling back the current changes...");
                setProgress(1, 0, 4);
                entityManager.getTransaction().rollback();
                Thread.sleep(1000L);
                setProgress(2, 0, 4);
                setMessage("Starting a new transaction...");
                entityManager.getTransaction().begin();
                Thread.sleep(500L);
                setProgress(3, 0, 4);
                setMessage("Fetching new data...");
                java.util.Collection data = query.getResultList();
                for (Object entity : data) {
                    entityManager.refresh(entity);
                }
                Thread.sleep(1300L);
                setProgress(4, 0, 4);
                Thread.sleep(150L);
                list.clear();
                list.addAll(data);
            } catch (InterruptedException ignore) {
            }
            return null;
        }

        @Override
        protected void finished() {
            setMessage("Done.");
            setSaveNeeded(false);
        }
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = Bootstrap.getApplication().getMainFrame();
            aboutBox = new AboutFrm(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        Bootstrap.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();
        mainPanel = new javax.swing.JPanel();
        masterScrollPane = new javax.swing.JScrollPane();
        masterTable = new javax.swing.JTable();
        productIdLabel = new javax.swing.JLabel();
        manufacturerIdLabel = new javax.swing.JLabel();
        purchaseCostLabel = new javax.swing.JLabel();
        quantityOnHandLabel = new javax.swing.JLabel();
        descriptionLabel = new javax.swing.JLabel();
        productIdField = new javax.swing.JTextField();
        manufacturerIdField = new javax.swing.JTextField();
        purchaseCostField = new javax.swing.JTextField();
        quantityOnHandField = new javax.swing.JTextField();
        descriptionField = new javax.swing.JTextField();
        saveButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();
        newButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem newRecordMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem deleteRecordMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        javax.swing.JMenuItem saveMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem refreshMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(monster2.Bootstrap.class).getContext().getResourceMap(MainFrm.class);
        entityManager = java.beans.Beans.isDesignTime() ? null : javax.persistence.Persistence.createEntityManagerFactory(resourceMap.getString("entityManager.persistenceUnit")).createEntityManager();
        query = java.beans.Beans.isDesignTime() ? null : entityManager.createQuery(resourceMap.getString("query.query"));
        list = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : org.jdesktop.observablecollections.ObservableCollections.observableList(query.getResultList());
        mainPanel.setName("mainPanel");
        masterScrollPane.setName("masterScrollPane");
        masterTable.setName("masterTable");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, list, masterTable);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${productId}"));
        columnBinding.setColumnName("Product Id");
        columnBinding.setColumnClass(Integer.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${manufacturerId}"));
        columnBinding.setColumnName("Manufacturer Id");
        columnBinding.setColumnClass(Integer.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${purchaseCost}"));
        columnBinding.setColumnName("Purchase Cost");
        columnBinding.setColumnClass(java.math.BigDecimal.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${quantityOnHand}"));
        columnBinding.setColumnName("Quantity On Hand");
        columnBinding.setColumnClass(Integer.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${description}"));
        columnBinding.setColumnName("Description");
        columnBinding.setColumnClass(String.class);
        bindingGroup.addBinding(jTableBinding);
        masterScrollPane.setViewportView(masterTable);
        productIdLabel.setText(resourceMap.getString("productIdLabel.text"));
        productIdLabel.setName("productIdLabel");
        manufacturerIdLabel.setText(resourceMap.getString("manufacturerIdLabel.text"));
        manufacturerIdLabel.setName("manufacturerIdLabel");
        purchaseCostLabel.setText(resourceMap.getString("purchaseCostLabel.text"));
        purchaseCostLabel.setName("purchaseCostLabel");
        quantityOnHandLabel.setText(resourceMap.getString("quantityOnHandLabel.text"));
        quantityOnHandLabel.setName("quantityOnHandLabel");
        descriptionLabel.setText(resourceMap.getString("descriptionLabel.text"));
        descriptionLabel.setName("descriptionLabel");
        productIdField.setName("productIdField");
        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, masterTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.productId}"), productIdField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setSourceUnreadableValue(null);
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, masterTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement != null}"), productIdField, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);
        manufacturerIdField.setName("manufacturerIdField");
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, masterTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.manufacturerId}"), manufacturerIdField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setSourceUnreadableValue(null);
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, masterTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement != null}"), manufacturerIdField, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);
        purchaseCostField.setName("purchaseCostField");
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, masterTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.purchaseCost}"), purchaseCostField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setSourceUnreadableValue(null);
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, masterTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement != null}"), purchaseCostField, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);
        quantityOnHandField.setName("quantityOnHandField");
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, masterTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.quantityOnHand}"), quantityOnHandField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setSourceUnreadableValue(null);
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, masterTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement != null}"), quantityOnHandField, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);
        descriptionField.setName("descriptionField");
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, masterTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.description}"), descriptionField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setSourceUnreadableValue(null);
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, masterTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement != null}"), descriptionField, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);
        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(monster2.Bootstrap.class).getContext().getActionMap(MainFrm.class, this);
        saveButton.setAction(actionMap.get("save"));
        saveButton.setName("saveButton");
        refreshButton.setAction(actionMap.get("refresh"));
        refreshButton.setName("refreshButton");
        newButton.setAction(actionMap.get("newRecord"));
        newButton.setName("newButton");
        deleteButton.setAction(actionMap.get("deleteRecord"));
        deleteButton.setName("deleteButton");
        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup().addComponent(newButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(deleteButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(refreshButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(saveButton).addContainerGap()).addGroup(mainPanelLayout.createSequentialGroup().addContainerGap().addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(productIdLabel).addComponent(manufacturerIdLabel).addComponent(purchaseCostLabel).addComponent(quantityOnHandLabel).addComponent(descriptionLabel)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(productIdField, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE).addComponent(manufacturerIdField, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE).addComponent(purchaseCostField, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE).addComponent(quantityOnHandField, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE).addComponent(descriptionField, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)).addContainerGap()).addGroup(mainPanelLayout.createSequentialGroup().addContainerGap().addComponent(masterScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE).addContainerGap()));
        mainPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { deleteButton, newButton, refreshButton, saveButton });
        mainPanelLayout.setVerticalGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(mainPanelLayout.createSequentialGroup().addContainerGap().addComponent(masterScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(productIdLabel).addComponent(productIdField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(manufacturerIdLabel).addComponent(manufacturerIdField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(purchaseCostLabel).addComponent(purchaseCostField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(quantityOnHandLabel).addComponent(quantityOnHandField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(descriptionLabel).addComponent(descriptionField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(saveButton).addComponent(refreshButton).addComponent(deleteButton).addComponent(newButton)).addContainerGap()));
        menuBar.setName("menuBar");
        fileMenu.setText(resourceMap.getString("fileMenu.text"));
        fileMenu.setName("fileMenu");
        newRecordMenuItem.setAction(actionMap.get("newRecord"));
        newRecordMenuItem.setName("newRecordMenuItem");
        fileMenu.add(newRecordMenuItem);
        deleteRecordMenuItem.setAction(actionMap.get("deleteRecord"));
        deleteRecordMenuItem.setName("deleteRecordMenuItem");
        fileMenu.add(deleteRecordMenuItem);
        jSeparator1.setName("jSeparator1");
        fileMenu.add(jSeparator1);
        saveMenuItem.setAction(actionMap.get("save"));
        saveMenuItem.setName("saveMenuItem");
        fileMenu.add(saveMenuItem);
        refreshMenuItem.setAction(actionMap.get("refresh"));
        refreshMenuItem.setName("refreshMenuItem");
        fileMenu.add(refreshMenuItem);
        jSeparator2.setName("jSeparator2");
        fileMenu.add(jSeparator2);
        exitMenuItem.setAction(actionMap.get("quit"));
        exitMenuItem.setName("exitMenuItem");
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        helpMenu.setText(resourceMap.getString("helpMenu.text"));
        helpMenu.setName("helpMenu");
        aboutMenuItem.setAction(actionMap.get("showAboutBox"));
        aboutMenuItem.setName("aboutMenuItem");
        helpMenu.add(aboutMenuItem);
        menuBar.add(helpMenu);
        statusPanel.setName("statusPanel");
        statusPanelSeparator.setName("statusPanelSeparator");
        statusMessageLabel.setName("statusMessageLabel");
        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel");
        progressBar.setName("progressBar");
        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE).addGroup(statusPanelLayout.createSequentialGroup().addContainerGap().addComponent(statusMessageLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 226, Short.MAX_VALUE).addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(statusAnimationLabel).addContainerGap()));
        statusPanelLayout.setVerticalGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(statusPanelLayout.createSequentialGroup().addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(statusMessageLabel).addComponent(statusAnimationLabel).addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(3, 3, 3)));
        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
        bindingGroup.bind();
    }

    private javax.swing.JButton deleteButton;

    private javax.swing.JTextField descriptionField;

    private javax.swing.JLabel descriptionLabel;

    private javax.persistence.EntityManager entityManager;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JSeparator jSeparator2;

    private java.util.List<entity.Product> list;

    private javax.swing.JPanel mainPanel;

    private javax.swing.JTextField manufacturerIdField;

    private javax.swing.JLabel manufacturerIdLabel;

    private javax.swing.JScrollPane masterScrollPane;

    private javax.swing.JTable masterTable;

    private javax.swing.JMenuBar menuBar;

    private javax.swing.JButton newButton;

    private javax.swing.JTextField productIdField;

    private javax.swing.JLabel productIdLabel;

    private javax.swing.JProgressBar progressBar;

    private javax.swing.JTextField purchaseCostField;

    private javax.swing.JLabel purchaseCostLabel;

    private javax.swing.JTextField quantityOnHandField;

    private javax.swing.JLabel quantityOnHandLabel;

    private javax.persistence.Query query;

    private javax.swing.JButton refreshButton;

    private javax.swing.JButton saveButton;

    private javax.swing.JLabel statusAnimationLabel;

    private javax.swing.JLabel statusMessageLabel;

    private javax.swing.JPanel statusPanel;

    private org.jdesktop.beansbinding.BindingGroup bindingGroup;

    private final Timer messageTimer;

    private final Timer busyIconTimer;

    private final Icon idleIcon;

    private final Icon[] busyIcons = new Icon[15];

    private int busyIconIndex = 0;

    private JDialog aboutBox;

    private boolean saveNeeded;
}

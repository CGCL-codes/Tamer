package net.sf.FFReport.Screen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import net.sf.FFReport.Common.JasperConstants;
import net.sf.FFReport.Common.Options;
import net.sf.FFReport.Common.RetrieveRELayout;
import net.sf.JRecord.Details.AbstractLayoutDetails;
import net.sf.JRecord.Details.LayoutDetail;
import net.sf.RecordEditor.utils.LayoutItem;
import net.sf.RecordEditor.utils.SystemItem;
import net.sf.RecordEditor.utils.common.Common;
import net.sf.RecordEditor.utils.swing.BasePanel;

public class RecordEditorPnl extends BaseRecordPnl implements ActionListener {

    private JComboBox dbCombo = new JComboBox();

    private JComboBox systemCombo = new JComboBox();

    private JComboBox layoutCombo = new JComboBox();

    private JTextArea description = new JTextArea();

    private JButton goBtn = new JButton("go");

    private JButton reloadBtn = new JButton("Reload DB");

    private ArrayList<SystemItem> systems = new ArrayList<SystemItem>();

    private ArrayList<LayoutItem> layouts = new ArrayList<LayoutItem>();

    private int[] layoutId = null;

    private RetrieveRELayout retrieveReLayout;

    private static Options defaultOption = null;

    private boolean skipAction = false;

    public RecordEditorPnl() {
        boolean free = Common.isSetDoFree(false);
        retrieveReLayout = new RetrieveRELayout();
        typeOfLayout = JasperConstants.PROVIDER_RECORD_EDITOR;
        retrieveLayout = retrieveReLayout;
        loadDBs();
        setupScreen();
        retrieveReLayout.setDoFree(free);
    }

    /**
     * Setup the screen fields
     */
    public void setupScreen() {
        addHeading("RecordLayout Selection screen");
        setGap(BasePanel.GAP1);
        addComponent("File", getFileName(), getFileName().getChooseFileButton());
        setGap(BasePanel.GAP2);
        addComponent("Database Id", dbCombo, reloadBtn);
        setGap(BasePanel.GAP1);
        addComponent("System", systemCombo);
        addComponent("Record Layout", layoutCombo);
        addComponent("Description", description);
        setHeight(BasePanel.NORMAL_HEIGHT * 3);
        setGap(BasePanel.GAP2);
        addComponent("Define fields as numeric", getFormatNumChk());
        setGap(BasePanel.GAP2);
        addComponent("Report Record", rptRecordCombo, goBtn);
        addComponent("Record", new JScrollPane(recordList));
        setHeight(BasePanel.NORMAL_HEIGHT * 6);
        setGap(BasePanel.GAP2);
        addMessage(new JScrollPane(message));
        setHeight(BasePanel.NORMAL_HEIGHT * 3);
        if (defaultOption == null) {
            getFormatNumChk().setSelected(true);
            if (layoutCombo.getItemCount() > 0) {
                layoutCombo.setSelectedIndex(0);
                setLayoutDetails();
            }
        } else {
            String layoutName = defaultOption.getLayoutName();
            getFileName().setText(defaultOption.getFileName());
            if (layoutName != null && !"".equals(layoutName)) {
                try {
                    layoutCombo.setSelectedItem(layoutName);
                    setLayoutDetails();
                    rptRecordCombo.setSelectedIndex(defaultOption.getRptRecordIndex() + 1);
                    getFormatNumChk().setSelected(defaultOption.isFormatAsNumeric());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        dbCombo.addActionListener(this);
        systemCombo.addActionListener(this);
        layoutCombo.addActionListener(this);
        goBtn.addActionListener(this);
        reloadBtn.addActionListener(this);
    }

    /**
	 * Load the record Layout combo
	 */
    private void loadLayoutCombo() {
        int i, j;
        int size = layouts.size();
        int sysIdx = systemCombo.getSelectedIndex();
        if ((layoutId == null) || layoutId.length < size) {
            layoutId = new int[size];
        }
        layoutCombo.removeAllItems();
        if (sysIdx <= 0) {
            for (i = 0; i < size; i++) {
                LayoutItem layout = layouts.get(i);
                layoutCombo.addItem(layout.getRecordName());
                layoutId[i] = i;
            }
        } else {
            int sys = systems.get(sysIdx - 1).systemId;
            j = 0;
            for (i = 0; i < size; i++) {
                LayoutItem layout = layouts.get(i);
                if (layout.getSystem() == sys) {
                    layoutCombo.addItem(layout.getRecordName());
                    layoutId[j++] = i;
                }
            }
        }
        setDescription(null);
        setLayoutDetails();
    }

    /**
	 * Set the layout description
	 *
	 */
    @Override
    protected final void setDescription(AbstractLayoutDetails layout) {
        try {
            if (layoutCombo.getSelectedIndex() >= 0) {
                int idx = layoutId[layoutCombo.getSelectedIndex()];
                description.setText(layouts.get(idx).getDescription());
            } else {
                description.setText("");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            description.setText("");
        }
    }

    /**
	 * Load the various systems from the DB
	 *
	 */
    private void loadSystems() {
        int i, num;
        SystemItem dtls;
        systemCombo.removeAllItems();
        systemCombo.addItem("<All>");
        try {
            systems = retrieveReLayout.getSystems();
        } catch (Exception ex) {
            message.setText(ex.getMessage());
            message.setCaretPosition(1);
            ex.printStackTrace();
        }
        num = systems.size();
        for (i = 0; i < num; i++) {
            dtls = systems.get(i);
            systemCombo.addItem(dtls.description);
        }
    }

    /**
	 * Get the options
	 * @return the options
	 */
    public Options getOptions() {
        if (skipAction) {
            return options;
        }
        options.dbIndex = dbCombo.getSelectedIndex();
        if (layoutCombo.getSelectedItem() != null) {
            options.setLayoutName(layoutCombo.getSelectedItem().toString());
        }
        return super.getOptions();
    }

    /**
	 * @param newOptions the options to set
	 */
    public void setOptions(Options newOptions) {
        skipAction = true;
        try {
            options = newOptions;
            dbCombo.setSelectedIndex(newOptions.dbIndex);
            retrieveReLayout.setDbIndex(newOptions.dbIndex);
            setupDbDetails();
            layoutCombo.setSelectedItem(newOptions.getLayoutName());
            setDescription(newOptions.getLayout());
            super.setOptions(newOptions);
        } catch (Exception e) {
            e.printStackTrace();
        }
        skipAction = false;
    }

    private void setupDbDetails() {
        boolean free = retrieveReLayout.isSetDoFree(false);
        loadSystems();
        retrieveReLayout.loadLayouts(layouts);
        loadLayoutCombo();
        retrieveReLayout.setDoFree(free);
    }

    /**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
    public final void actionPerformed(ActionEvent e) {
        if (skipAction) {
        } else if (e.getSource() == dbCombo) {
            setDBdetails();
        } else if (e.getSource() == systemCombo) {
            loadLayoutCombo();
        } else if (e.getSource() == layoutCombo) {
            if (layoutCombo.getSelectedItem() != null) {
                if (options != null) {
                    options.setLayoutName(layoutCombo.getSelectedItem().toString());
                }
                setLayoutDetails();
            }
        } else if (e.getSource() == reloadBtn) {
            setupDbDetails();
        } else if (e.getSource() == goBtn) {
            boolean ok = true;
            if (options.isFileRequired()) {
                if ("".equals(getFileName().getText())) {
                    ok = false;
                    getFileName().requestFocus();
                    message.setText("You must enter a File Name");
                } else if (!(new File(getFileName().getText())).exists()) {
                    ok = false;
                    getFileName().requestFocus();
                    message.setText("The Files does not exist");
                }
            }
            if (ok) {
                setVisible(false);
            }
        }
    }

    private void setDBdetails() {
        retrieveReLayout.setDbIndex(dbCombo.getSelectedIndex());
        retrieveLayout = retrieveReLayout;
        setupDbDetails();
    }

    /**
	 * load the various DB options available
	 *
	 */
    public void loadDBs() {
        int i;
        String[] dbs = Common.getSourceId();
        for (i = 0; (i < dbs.length) && (dbs[i] != null) && (!dbs[i].equals("")); i++) {
            dbCombo.addItem(dbs[i]);
        }
    }
}

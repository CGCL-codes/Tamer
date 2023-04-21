package org.lnicholls.galleon.apps.music;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import org.apache.log4j.Logger;
import org.lnicholls.galleon.app.AppConfiguration;
import org.lnicholls.galleon.app.AppConfigurationPanel;
import org.lnicholls.galleon.gui.FileOptionsTable;
import org.lnicholls.galleon.util.NameValue;

public class MusicOptionsPanel extends AppConfigurationPanel {

    private static Logger log = Logger.getLogger(MusicOptionsPanel.class.getName());

    public MusicOptionsPanel(AppConfiguration appConfiguration) {
        super(appConfiguration);
        setLayout(new GridLayout(0, 1));
        MusicConfiguration musicConfiguration = (MusicConfiguration) appConfiguration;
        mTitleField = new JTextField(musicConfiguration.getName());
        mSharedField = new JCheckBox("Share");
        mSharedField.setSelected(musicConfiguration.isShared());
        mSharedField.setToolTipText("Share this app");
        FormLayout layout = new FormLayout("right:pref, 3dlu, 50dlu:g, right:pref:grow", "pref, " + "9dlu, pref, " + "3dlu, pref, " + "9dlu, pref, " + "9dlu, pref");
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        builder.addSeparator("General", cc.xyw(1, 1, 4));
        builder.addLabel("Title", cc.xy(1, 3));
        builder.add(mTitleField, cc.xyw(3, 3, 1));
        builder.add(mSharedField, cc.xyw(3, 5, 1));
        builder.addSeparator("Directories", cc.xyw(1, 7, 4));
        mColumnValues = new ArrayList();
        int counter = 0;
        for (Iterator i = musicConfiguration.getPaths().iterator(); i.hasNext(); ) {
            NameValue value = (NameValue) i.next();
            ArrayList values = new ArrayList();
            values.add(0, value.getName());
            values.add(1, value.getValue());
            mColumnValues.add(counter++, values);
        }
        mFileOptionsTable = new FileOptionsTable(true, this, mColumnValues);
        ArrayList columnNames = new ArrayList();
        columnNames.add(0, "Name");
        columnNames.add(1, "Path");
        builder.add(mFileOptionsTable, cc.xyw(1, 9, 4));
        JPanel panel = builder.getPanel();
        add(panel);
    }

    public void load() {
    }

    public boolean valid() {
        if (mTitleField.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(this, "Invalid title.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (mColumnValues.size() == 0) {
            JOptionPane.showMessageDialog(this, "No directories configured.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public void save() {
        log.debug("save()");
        MusicConfiguration musicConfiguration = (MusicConfiguration) mAppConfiguration;
        musicConfiguration.setName(mTitleField.getText());
        ArrayList newItems = new ArrayList();
        Iterator iterator = mColumnValues.iterator();
        while (iterator.hasNext()) {
            ArrayList rows = (ArrayList) iterator.next();
            log.debug("Path=" + rows.get(0));
            newItems.add(new NameValue((String) rows.get(0), (String) rows.get(1)));
        }
        musicConfiguration.setPaths(newItems);
        musicConfiguration.setShared(mSharedField.isSelected());
    }

    private JTextComponent mTitleField;

    private FileOptionsTable mFileOptionsTable;

    private ArrayList mColumnValues;

    private JCheckBox mSharedField;
}

package org.lnicholls.galleon.apps.internetSlideshows;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import org.apache.log4j.Logger;
import org.lnicholls.galleon.app.AppConfiguration;
import org.lnicholls.galleon.app.AppConfigurationPanel;
import org.lnicholls.galleon.gui.FileOptionsTable;
import org.lnicholls.galleon.util.Effects;
import org.lnicholls.galleon.util.NameValue;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class InternetSlideshowsOptionsPanel extends AppConfigurationPanel {

    private static Logger log = Logger.getLogger(InternetSlideshowsOptionsPanel.class.getName());

    class ImagesWrapper extends NameValue {

        public ImagesWrapper(String name, String value) {
            super(name, value);
        }

        public String toString() {
            return getName();
        }
    }

    public InternetSlideshowsOptionsPanel(AppConfiguration appConfiguration) {
        super(appConfiguration);
        setLayout(new GridLayout(0, 1));
        InternetSlideshowsConfiguration imagesConfiguration = (InternetSlideshowsConfiguration) appConfiguration;
        mTitleField = new JTextField(imagesConfiguration.getName());
        mSharedField = new JCheckBox("Share");
        mSharedField.setSelected(imagesConfiguration.isShared());
        mSharedField.setToolTipText("Share this app");
        mUseSafeField = new JCheckBox("Use safe viewing area");
        mUseSafeField.setToolTipText("Check to specify that photos should fit within the safe viewing area of the TV");
        mUseSafeField.setSelected(imagesConfiguration.isUseSafe());
        mEffectsField = new JComboBox();
        mEffectsField.setToolTipText("Select a slideshow transition effect");
        mEffectsField.addItem(new ImagesWrapper(Effects.RANDOM, Effects.RANDOM));
        mEffectsField.addItem(new ImagesWrapper(Effects.SEQUENTIAL, Effects.SEQUENTIAL));
        String names[] = new String[0];
        names = (String[]) Effects.getEffectNames().toArray(names);
        Arrays.sort(names);
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            mEffectsField.addItem(new ImagesWrapper(name, name));
        }
        defaultCombo(mEffectsField, String.valueOf(imagesConfiguration.getEffect()));
        mDisplayTimeField = new JComboBox();
        mDisplayTimeField.setToolTipText("Select a slideshow display time");
        mDisplayTimeField.addItem(new ImagesWrapper("2 seconds", "2"));
        mDisplayTimeField.addItem(new ImagesWrapper("3 seconds", "3"));
        mDisplayTimeField.addItem(new ImagesWrapper("4 seconds", "4"));
        mDisplayTimeField.addItem(new ImagesWrapper("5 seconds", "5"));
        mDisplayTimeField.addItem(new ImagesWrapper("6 seconds", "6"));
        mDisplayTimeField.addItem(new ImagesWrapper("7 seconds", "7"));
        mDisplayTimeField.addItem(new ImagesWrapper("8 seconds", "8"));
        mDisplayTimeField.addItem(new ImagesWrapper("9 seconds", "9"));
        mDisplayTimeField.addItem(new ImagesWrapper("10 seconds", "10"));
        mDisplayTimeField.addItem(new ImagesWrapper("11 seconds", "11"));
        mDisplayTimeField.addItem(new ImagesWrapper("12 seconds", "12"));
        mDisplayTimeField.addItem(new ImagesWrapper("13 seconds", "13"));
        mDisplayTimeField.addItem(new ImagesWrapper("14 seconds", "14"));
        mDisplayTimeField.addItem(new ImagesWrapper("15 seconds", "15"));
        mDisplayTimeField.addItem(new ImagesWrapper("16 seconds", "16"));
        mDisplayTimeField.addItem(new ImagesWrapper("17 seconds", "17"));
        mDisplayTimeField.addItem(new ImagesWrapper("18 seconds", "18"));
        mDisplayTimeField.addItem(new ImagesWrapper("19 seconds", "19"));
        mDisplayTimeField.addItem(new ImagesWrapper("20 seconds", "20"));
        defaultCombo(mDisplayTimeField, String.valueOf(imagesConfiguration.getDisplayTime()));
        mTransitionTimeField = new JComboBox();
        mTransitionTimeField.setToolTipText("Select a slideshow transition time");
        mTransitionTimeField.addItem(new ImagesWrapper("2 seconds", "2"));
        mTransitionTimeField.addItem(new ImagesWrapper("3 seconds", "3"));
        mTransitionTimeField.addItem(new ImagesWrapper("4 seconds", "4"));
        mTransitionTimeField.addItem(new ImagesWrapper("5 seconds", "5"));
        mTransitionTimeField.addItem(new ImagesWrapper("6 seconds", "6"));
        mTransitionTimeField.addItem(new ImagesWrapper("7 seconds", "7"));
        mTransitionTimeField.addItem(new ImagesWrapper("8 seconds", "8"));
        mTransitionTimeField.addItem(new ImagesWrapper("9 seconds", "9"));
        mTransitionTimeField.addItem(new ImagesWrapper("10 seconds", "10"));
        mTransitionTimeField.addItem(new ImagesWrapper("11 seconds", "11"));
        mTransitionTimeField.addItem(new ImagesWrapper("12 seconds", "12"));
        mTransitionTimeField.addItem(new ImagesWrapper("13 seconds", "13"));
        mTransitionTimeField.addItem(new ImagesWrapper("14 seconds", "14"));
        mTransitionTimeField.addItem(new ImagesWrapper("15 seconds", "15"));
        mTransitionTimeField.addItem(new ImagesWrapper("16 seconds", "16"));
        mTransitionTimeField.addItem(new ImagesWrapper("17 seconds", "17"));
        mTransitionTimeField.addItem(new ImagesWrapper("18 seconds", "18"));
        mTransitionTimeField.addItem(new ImagesWrapper("19 seconds", "19"));
        mTransitionTimeField.addItem(new ImagesWrapper("20 seconds", "20"));
        defaultCombo(mTransitionTimeField, String.valueOf(imagesConfiguration.getTransitionTime()));
        FormLayout layout = new FormLayout("right:pref, 3dlu, 50dlu:g, right:pref:grow", "pref, 9dlu, " + "pref, 3dlu, " + "pref, 9dlu, " + "pref, 9dlu, " + "pref, 9dlu, " + "pref, 9dlu, " + "pref, 9dlu, " + "pref, 9dlu, " + "pref, 9dlu, " + "pref, 9dlu, " + "pref");
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        builder.addSeparator("General", cc.xyw(1, 1, 4));
        builder.addLabel("Title", cc.xy(1, 3));
        builder.add(mTitleField, cc.xyw(3, 3, 1));
        builder.add(mSharedField, cc.xyw(3, 5, 1));
        builder.addSeparator("Options", cc.xyw(1, 7, 4));
        builder.add(mUseSafeField, cc.xyw(3, 9, 1));
        builder.addSeparator("Slideshow Effects", cc.xyw(1, 11, 4));
        builder.addLabel("Effect", cc.xy(1, 13));
        builder.add(mEffectsField, cc.xyw(3, 13, 1));
        builder.addLabel("Display Time", cc.xy(1, 15));
        builder.add(mDisplayTimeField, cc.xy(3, 15));
        builder.addLabel("Transition Time", cc.xy(1, 17));
        builder.add(mTransitionTimeField, cc.xy(3, 17));
        builder.addSeparator("Feeds", cc.xyw(1, 19, 4));
        mColumnValues = new ArrayList();
        int counter = 0;
        for (Iterator i = imagesConfiguration.getPaths().iterator(); i.hasNext(); ) {
            NameValue value = (NameValue) i.next();
            ArrayList values = new ArrayList();
            values.add(0, value.getName());
            values.add(1, value.getValue());
            mColumnValues.add(counter++, values);
        }
        mFileOptionsTable = new FileOptionsTable(true, this, mColumnValues);
        ArrayList columnNames = new ArrayList();
        columnNames.add(0, "Name");
        columnNames.add(1, "URL");
        builder.add(mFileOptionsTable, cc.xyw(1, 21, 4));
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
            JOptionPane.showMessageDialog(this, "No sites configured.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public void save() {
        log.debug("save()");
        InternetSlideshowsConfiguration imagesConfiguration = (InternetSlideshowsConfiguration) mAppConfiguration;
        imagesConfiguration.setName(mTitleField.getText());
        imagesConfiguration.setUseSafe(mUseSafeField.isSelected());
        imagesConfiguration.setEffect(((NameValue) mEffectsField.getSelectedItem()).getValue());
        imagesConfiguration.setDisplayTime(Integer.parseInt(((NameValue) mDisplayTimeField.getSelectedItem()).getValue()));
        imagesConfiguration.setTransitionTime(Integer.parseInt(((NameValue) mTransitionTimeField.getSelectedItem()).getValue()));
        ArrayList newItems = new ArrayList();
        Iterator iterator = mColumnValues.iterator();
        while (iterator.hasNext()) {
            ArrayList rows = (ArrayList) iterator.next();
            log.debug("Path=" + rows.get(0));
            newItems.add(new NameValue((String) rows.get(0), (String) rows.get(1)));
        }
        imagesConfiguration.setPaths(newItems);
        imagesConfiguration.setShared(mSharedField.isSelected());
    }

    private JTextComponent mTitleField;

    private JCheckBox mUseSafeField;

    private JComboBox mEffectsField;

    private JComboBox mDisplayTimeField;

    private JComboBox mTransitionTimeField;

    private FileOptionsTable mFileOptionsTable;

    private ArrayList mColumnValues;

    private JCheckBox mSharedField;
}

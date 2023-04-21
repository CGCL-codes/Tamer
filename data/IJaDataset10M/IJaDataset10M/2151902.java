package de.sciss.fscape.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import de.sciss.app.AbstractApplication;
import de.sciss.app.AbstractWindow;
import de.sciss.app.PreferenceEntrySync;
import de.sciss.common.AppWindow;
import de.sciss.common.BasicWindowHandler;
import de.sciss.io.IOUtil;
import de.sciss.net.OSCChannel;
import de.sciss.util.Flag;
import de.sciss.util.ParamSpace;
import de.sciss.fscape.Main;
import de.sciss.fscape.io.GenericFile;
import de.sciss.fscape.net.OSCRoot;
import de.sciss.fscape.util.PrefsUtil;
import de.sciss.gui.AbstractWindowHandler;
import de.sciss.gui.CoverGrowBox;
import de.sciss.gui.HelpButton;
import de.sciss.gui.PrefCheckBox;
import de.sciss.gui.PrefComboBox;
import de.sciss.gui.PrefParamField;
import de.sciss.gui.PrefPathField;
import de.sciss.gui.SpringPanel;
import de.sciss.gui.StringItem;

/**
 *  This is the frame that
 *  displays the user adjustable
 *  application and session preferences
 *
 *  @author		Hanns Holger Rutz
 *  @version	0.73, 09-Aug-09
 */
public class PrefsFrame extends AppWindow {

    private static final ParamSpace spcIntegerFromZero = new ParamSpace(0, Double.POSITIVE_INFINITY, 1, 0, 0, 0);

    /**
	 *  Creates a new preferences frame
	 *
	 *  @param  root	application root
	 *  @param  doc		session document
	 */
    public PrefsFrame() {
        super(SUPPORT);
        setTitle(getResourceString("framePrefs"));
        final Container cp = getContentPane();
        final de.sciss.app.Application app = AbstractApplication.getApplication();
        final OSCRoot osc;
        final Flag haveWarned = new Flag(false);
        final String txtWarnLookAndFeel = getResourceString("warnLookAndFeelUpdate");
        JPanel p, tabWrap;
        SpringPanel tab;
        PrefParamField ggParam;
        PrefPathField ggPath;
        PrefCheckBox ggCheckBox;
        PrefComboBox ggChoice;
        JTabbedPane ggTabPane;
        JLabel lb;
        UIManager.LookAndFeelInfo[] lafInfos;
        Box b;
        Preferences prefs;
        String key, key2, title;
        int row;
        ggTabPane = new JTabbedPane();
        tab = new SpringPanel(2, 1, 4, 2);
        row = 0;
        prefs = IOUtil.getUserPrefs();
        key = IOUtil.KEY_TEMPDIR;
        key2 = "prefsTmpDir";
        lb = new JLabel(getResourceString(key2), SwingConstants.TRAILING);
        tab.gridAdd(lb, 0, row);
        ggPath = new PrefPathField(PathField.TYPE_FOLDER, getResourceString(key2));
        ggPath.setPreferences(prefs, key);
        tab.gridAdd(ggPath, 1, row);
        row++;
        prefs = app.getUserPrefs();
        key2 = "prefsAudioFileFormat";
        lb = new JLabel(getResourceString(key2), SwingConstants.TRAILING);
        tab.gridAdd(lb, 0, row);
        b = Box.createHorizontalBox();
        ggChoice = new PrefComboBox();
        for (int i = 0; i < GenericFile.TYPES_SOUND.length; i++) {
            ggChoice.addItem(new StringItem(GenericFile.getFileTypeStr(GenericFile.TYPES_SOUND[i]), GenericFile.getTypeDescr(GenericFile.TYPES_SOUND[i])));
        }
        key = "audioFileType";
        ggChoice.setPreferences(prefs, key);
        b.add(ggChoice);
        ggChoice = new PrefComboBox();
        for (int i = 0; i < PathField.SNDRES_NUM; i++) {
            ggChoice.addItem(new StringItem(PathField.getSoundResID(i), PathField.getSoundResDescr(i)));
        }
        key = "audioFileRes";
        ggChoice.setPreferences(prefs, key);
        b.add(ggChoice);
        ggChoice = new PrefComboBox();
        for (int i = 0; i < PathField.SNDRATE_NUM; i++) {
            ggChoice.addItem(new StringItem(PathField.getSoundRateID(i), PathField.getSoundRateDescr(i)));
        }
        key = "audioFileRate";
        ggChoice.setPreferences(prefs, key);
        b.add(ggChoice);
        tab.gridAdd(b, 1, row, -1, 1);
        row++;
        prefs = app.getUserPrefs();
        key2 = "prefsHeadroom";
        key = "headroom";
        lb = new JLabel(getResourceString(key2), SwingConstants.TRAILING);
        tab.gridAdd(lb, 0, row);
        ggParam = new PrefParamField();
        ggParam.addSpace(ParamSpace.spcAmpDecibels);
        ggParam.setPreferences(prefs, key);
        tab.gridAdd(ggParam, 1, row, -1, 1);
        row++;
        osc = OSCRoot.getInstance();
        prefs = osc.getPreferences();
        key = OSCRoot.KEY_ACTIVE;
        key2 = "prefsOSCServer";
        lb = new JLabel(getResourceString(key2), SwingConstants.TRAILING);
        tab.gridAdd(lb, 0, row);
        b = Box.createHorizontalBox();
        ggCheckBox = new PrefCheckBox(getResourceString("prefsOSCActive"));
        ggCheckBox.setPreferences(prefs, key);
        b.add(ggCheckBox);
        key = OSCRoot.KEY_PROTOCOL;
        key2 = "prefsOSCProtocol";
        lb = new JLabel(getResourceString(key2), SwingConstants.TRAILING);
        b.add(Box.createHorizontalStrut(16));
        b.add(lb);
        ggChoice = new PrefComboBox();
        ggChoice.addItem(new StringItem(OSCChannel.TCP, "TCP"));
        ggChoice.addItem(new StringItem(OSCChannel.UDP, "UDP"));
        ggChoice.setPreferences(prefs, key);
        b.add(ggChoice);
        key = OSCRoot.KEY_PORT;
        key2 = "prefsOSCPort";
        lb = new JLabel(getResourceString(key2), SwingConstants.TRAILING);
        b.add(Box.createHorizontalStrut(16));
        b.add(lb);
        ggParam = new PrefParamField();
        ggParam.addSpace(spcIntegerFromZero);
        ggParam.setPreferences(prefs, key);
        b.add(ggParam);
        tab.gridAdd(b, 1, row, -1, 1);
        row++;
        prefs = app.getUserPrefs();
        key = PrefsUtil.KEY_LOOKANDFEEL;
        key2 = "prefsLookAndFeel";
        title = getResourceString(key2);
        lb = new JLabel(title, SwingConstants.TRAILING);
        tab.gridAdd(lb, 0, row);
        ggChoice = new PrefComboBox();
        lafInfos = UIManager.getInstalledLookAndFeels();
        for (int i = 0; i < lafInfos.length; i++) {
            ggChoice.addItem(new StringItem(lafInfos[i].getClassName(), lafInfos[i].getName()));
        }
        ggChoice.setPreferences(prefs, key);
        ggChoice.addActionListener(new WarnPrefsChange(ggChoice, ggChoice, haveWarned, txtWarnLookAndFeel, title));
        tab.gridAdd(ggChoice, 1, row, -1, 1);
        row++;
        key = BasicWindowHandler.KEY_LAFDECORATION;
        key2 = "prefsLAFDecoration";
        title = getResourceString(key2);
        ggCheckBox = new PrefCheckBox(title);
        ggCheckBox.setPreferences(prefs, key);
        tab.gridAdd(ggCheckBox, 1, row, -1, 1);
        ggCheckBox.addActionListener(new WarnPrefsChange(ggCheckBox, ggCheckBox, haveWarned, txtWarnLookAndFeel, title));
        row++;
        key = BasicWindowHandler.KEY_INTERNALFRAMES;
        key2 = "prefsInternalFrames";
        title = getResourceString(key2);
        ggCheckBox = new PrefCheckBox(title);
        ggCheckBox.setPreferences(prefs, key);
        tab.gridAdd(ggCheckBox, 1, row, -1, 1);
        ggCheckBox.addActionListener(new WarnPrefsChange(ggCheckBox, ggCheckBox, haveWarned, txtWarnLookAndFeel, title));
        row++;
        key = CoverGrowBox.KEY_INTRUDINGSIZE;
        key2 = "prefsIntrudingSize";
        ggCheckBox = new PrefCheckBox(getResourceString(key2));
        ggCheckBox.setPreferences(prefs, key);
        tab.gridAdd(ggCheckBox, 1, row, -1, 1);
        row++;
        key = BasicWindowHandler.KEY_FLOATINGPALETTES;
        key2 = "prefsFloatingPalettes";
        ggCheckBox = new PrefCheckBox(getResourceString(key2));
        ggCheckBox.setPreferences(prefs, key);
        tab.gridAdd(ggCheckBox, 1, row, -1, 1);
        ggCheckBox.addActionListener(new WarnPrefsChange(ggCheckBox, ggCheckBox, haveWarned, txtWarnLookAndFeel, title));
        key2 = "prefsGeneral";
        tab.makeCompactGrid();
        tabWrap = new JPanel(new BorderLayout());
        tabWrap.add(tab, BorderLayout.NORTH);
        p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        p.add(new HelpButton(key2));
        tabWrap.add(p, BorderLayout.SOUTH);
        ggTabPane.addTab(getResourceString(key2), null, tabWrap, null);
        cp.add(ggTabPane, BorderLayout.CENTER);
        AbstractWindowHandler.setDeepFont(cp);
        addListener(new AbstractWindow.Adapter() {

            public void windowClosing(AbstractWindow.Event e) {
                setVisible(false);
                dispose();
            }
        });
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        init();
        app.addComponent(Main.COMP_PREFS, this);
    }

    public void dispose() {
        AbstractApplication.getApplication().removeComponent(Main.COMP_PREFS);
        super.dispose();
    }

    private static String getResourceString(String key) {
        return AbstractApplication.getApplication().getResourceString(key);
    }

    private static class WarnPrefsChange implements ActionListener {

        private final PreferenceEntrySync pes;

        private final Component c;

        private final Flag haveWarned;

        private final String text;

        private final String title;

        private final String initialValue;

        protected WarnPrefsChange(PreferenceEntrySync pes, Component c, Flag haveWarned, String text, String title) {
            this.pes = pes;
            this.c = c;
            this.haveWarned = haveWarned;
            this.text = text;
            this.title = title;
            initialValue = pes.getPreferenceNode().get(pes.getPreferenceKey(), null);
        }

        public void actionPerformed(ActionEvent e) {
            final String newValue = pes.getPreferenceNode().get(pes.getPreferenceKey(), initialValue);
            if (!newValue.equals(initialValue) && !haveWarned.isSet()) {
                JOptionPane.showMessageDialog(c, text, title, JOptionPane.INFORMATION_MESSAGE);
                haveWarned.set(true);
            }
        }
    }
}

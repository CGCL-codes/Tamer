package org.jfonia.view.main;

import org.jfonia.view.main.header.HeaderPanel;
import org.jfonia.view.main.header.MenuBar;
import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import org.jfonia.connect5.basics.BasicValueNode;
import org.jfonia.connect5.basics.MutableValueNode;
import org.jfonia.connect5.basics.Observer;
import org.jfonia.constants.ModelConstants;
import org.jfonia.constants.ViewConstants;
import org.jfonia.images.ImageConstants;
import org.jfonia.images.ScaledImageIcon;
import org.jfonia.language.DescriptionConstants;
import org.jfonia.language.LabelConstants;
import org.jfonia.language.LanguageResource;
import org.jfonia.model.Sequence;
import org.jfonia.model.Staff;
import org.jfonia.model.StaffCollection;
import org.jfonia.model.ToneSequence;
import org.jfonia.model.elements.Beat;
import org.jfonia.model.elements.Chord;
import org.jfonia.model.elements.Clef;
import org.jfonia.model.elements.KeySignature;
import org.jfonia.model.elements.Lyric;
import org.jfonia.model.elements.TimeSignature;
import org.jfonia.model.elements.Tone;
import org.jfonia.pitch.Base40;
import org.jfonia.pitch.MusicalInterval;
import org.jfonia.view.dialogs.ExtensionFileFilter;
import org.jfonia.view.dialogs.NewPartDialog;
import org.jfonia.view.dialogs.OpenFileDialog;
import org.jfonia.view.dialogs.SaveFileDialog;
import org.jfonia.view.panels.LoaderPanel;
import org.jfonia.view.panels.ProjectPanel;

/**
 *
 * @author Rik Bauwens
 */
public class MainFrame extends JFrame implements ComponentListener {

    private static MainFrame instance;

    private MutableValueNode<Double> xNode;

    private MutableValueNode<Double> yNode;

    private MutableValueNode<Double> widthNode;

    private MutableValueNode<Double> heightNode;

    private ProjectPanel projectPanel;

    private JLayeredPane mainPane;

    private JScrollPane contentPane;

    private LoaderPanel loaderPanel;

    private MainFrame() {
        super();
        xNode = new BasicValueNode<Double>(0.0);
        yNode = new BasicValueNode<Double>(0.0);
        widthNode = new BasicValueNode<Double>(0.0);
        heightNode = new BasicValueNode<Double>(0.0);
        addComponentListener(this);
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(LanguageResource.getInstance().getLabel(LabelConstants.INCREMENTAL_LEADSHEET_EDITOR));
        setIconImage(new ScaledImageIcon(ImageConstants.LOGO, "").setMaximumSide(ImageConstants.FRAME_ICON_SIZE).getImageIcon().getImage());
        getRootPane().putClientProperty("apple.awt.brushMetalLook", Boolean.TRUE);
        getContentPane().setLayout(new BorderLayout());
        setJMenuBar(new MenuBar());
        getContentPane().add(new HeaderPanel(), BorderLayout.NORTH);
        mainPane = new JLayeredPane() {

            @Override
            public boolean isOptimizedDrawingEnabled() {
                return false;
            }
        };
        mainPane.setLayout(new BorderLayout());
        projectPanel = new ProjectPanel(this, Project.getInstance());
        contentPane = new JScrollPane(projectPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        InputMap inputMap = contentPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.getKeyText(ViewConstants.MOVE_LEFT_KEYCODE).toUpperCase()), "");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.getKeyText(ViewConstants.MOVE_RIGHT_KEYCODE).toUpperCase()), "");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.getKeyText(ViewConstants.MOVE_UP_KEYCODE).toUpperCase()), "");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.getKeyText(ViewConstants.MOVE_DOWN_KEYCODE).toUpperCase()), "");
        loaderPanel = new LoaderPanel(contentPane);
        mainPane.add(loaderPanel, BorderLayout.CENTER, 0);
        mainPane.add(contentPane, BorderLayout.CENTER, 1);
        getContentPane().add(mainPane, BorderLayout.CENTER);
        new Thread() {

            @Override
            public void run() {
                LeadSheetPlayer.getInstance().addObserver(new Observer() {

                    public void onNotify(Object source) {
                        double leadSheetX = projectPanel.getLeadSheetPanel().getX(LeadSheetPlayer.getInstance().getCurrentTick());
                        contentPane.getHorizontalScrollBar().setValue((int) (leadSheetX - contentPane.getWidth() / 2.0));
                    }
                });
                LeadSheetPlayer.getInstance().addPlayerStateObserver(new Observer() {

                    public void onNotify(Object source) {
                        double leadSheetX = projectPanel.getLeadSheetPanel().getX(LeadSheetPlayer.getInstance().getCurrentTick());
                        if (LeadSheetPlayer.getInstance().isPlaying() && leadSheetX < contentPane.getWidth() / 2) contentPane.getHorizontalScrollBar().setValue(0);
                    }
                });
            }
        }.start();
        FileFilter fileFilter = new ExtensionFileFilter(LanguageResource.getInstance().getDescription(DescriptionConstants.LEADSHEET_FILE), new String[] { ModelConstants.EXTENSION });
        OpenFileDialog openFileDialog = OpenFileDialog.getInstance();
        openFileDialog.setParent(this);
        openFileDialog.setFileFilter(fileFilter);
        openFileDialog.setDialogTitle(LanguageResource.getInstance().getLabel(LabelConstants.OPEN_LEADSHEET));
        SaveFileDialog saveFileDialog = SaveFileDialog.getInstance();
        saveFileDialog.setParent(this);
        saveFileDialog.setFileFilter(fileFilter);
        saveFileDialog.setDialogTitle(LanguageResource.getInstance().getLabel(LabelConstants.SAVE_LEADSHEET));
        NewPartDialog newPartDialog = NewPartDialog.getInstance();
        newPartDialog.setParent(contentPane);
        pack();
        setLocationRelativeTo(null);
    }

    public static synchronized MainFrame getInstance() {
        if (instance == null) instance = new MainFrame();
        return instance;
    }

    public boolean hasLeadSheetFocus() {
        return projectPanel.hasLeadSheetFocus();
    }

    public boolean hasScribblesFocus() {
        return projectPanel.hasScribblesFocus();
    }

    public void showLoader(final String description) {
        loaderPanel.setDescription(description).setVisible(true);
    }

    public void disposeLoader() {
        loaderPanel.setVisible(false);
    }

    public MutableValueNode<Double> getXNode() {
        return xNode;
    }

    public MutableValueNode<Double> getYNode() {
        return yNode;
    }

    public MutableValueNode<Double> getWidthNode() {
        return widthNode;
    }

    public MutableValueNode<Double> getHeightNode() {
        return heightNode;
    }

    public void scrollTo(double ratio) {
        contentPane.getHorizontalScrollBar().setValue((int) (ratio * projectPanel.getLeadSheetPanel().getWidth()));
    }

    private void modelTest() {
        LeadSheet leadSheet = new LeadSheet();
        Project.getInstance().setLeadSheet(null, leadSheet);
        leadSheet.setBPM(120);
        StaffCollection staffCollection = new StaffCollection();
        leadSheet.addStaffCollection(staffCollection);
        Staff staff = new Staff();
        Sequence<Clef> clefSequence = new Sequence<Clef>(Clef.class);
        staff.addSequence(clefSequence);
        Clef clef = new Clef();
        clefSequence.add(clef);
        Sequence<KeySignature> keySignatureSequence = new Sequence<KeySignature>(KeySignature.class);
        staff.addSequence(keySignatureSequence);
        KeySignature keySignature = new KeySignature().setType(2);
        keySignatureSequence.add(keySignature);
        Sequence<TimeSignature> timeSignatureSequence = new Sequence<TimeSignature>(TimeSignature.class);
        staff.addSequence(timeSignatureSequence);
        TimeSignature timeSignature = new TimeSignature();
        timeSignatureSequence.add(timeSignature);
        ToneSequence toneSequence = new ToneSequence();
        staff.addSequence(toneSequence);
        toneSequence.add(new Tone(24).setBase40Rank(Base40.toBase40(new MusicalInterval(4, 0))));
        toneSequence.add(new Tone(24).setBase40Rank(Base40.toBase40(new MusicalInterval(0, 0))));
        staffCollection.addStaff(staff);
        Sequence<Beat> beatSequence = new Sequence<Beat>(Beat.class);
        beatSequence.add(new Beat(48).setMeasureBeat(true));
        staff.addSequence(beatSequence);
        Staff chordStaff = new Staff();
        Sequence<Chord> chordSequence = new Sequence<Chord>(Chord.class);
        chordStaff.addSequence(chordSequence);
        chordSequence.add(new Chord(24).setChord("Am"));
        chordSequence.add(new Chord(24).setChord("C"));
        Staff lyricStaff = new Staff();
        Sequence<Lyric> lyricSequence = new Sequence<Lyric>(Lyric.class);
        lyricStaff.addSequence(lyricSequence);
        lyricSequence.add(new Lyric(48).setLyric("dit"));
        LeadSheetPlayer.getInstance().setLeadSheet(leadSheet);
    }

    public void componentResized(ComponentEvent e) {
        widthNode.setValue(new Double(getWidth()));
        heightNode.setValue(new Double(getHeight()));
    }

    public void componentMoved(ComponentEvent e) {
        xNode.setValue(new Double(getLocationOnScreen().x));
        yNode.setValue(new Double(getLocationOnScreen().y));
    }

    public void componentShown(ComponentEvent e) {
    }

    public void componentHidden(ComponentEvent e) {
    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                MainFrame.getInstance().setVisible(true);
            }
        });
    }
}

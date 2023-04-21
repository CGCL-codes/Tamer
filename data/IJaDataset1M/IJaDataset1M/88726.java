package be.vds.jtbdive.view.panel;

import info.clearthought.layout.TableLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.VerticalLayout;
import be.vds.jtbdive.model.Dive;
import be.vds.jtbdive.model.DiveLocation;
import be.vds.jtbdive.model.Diver;
import be.vds.jtbdive.model.LogBookApplication;
import be.vds.jtbdive.model.LogBookApplicationFacade;
import be.vds.jtbdive.utils.LanguageManager;
import be.vds.jtbdive.view.component.DateTimeComponent;
import be.vds.jtbdive.view.listener.DiveLocationSelectionListener;
import be.vds.jtbdive.view.listener.DiverSelectionListener;
import be.vds.jtbdive.view.listener.ModificationListener;
import be.vds.jtbdive.view.panel.listenable.ModificationDiverSelectListenableJPanel;
import be.vds.jtbdive.view.util.RegexPlainDocument;

public class DivePanel extends ModificationDiverSelectListenableJPanel implements DiveLocationSelectionListener {

    private static final String DATE_FORMAT = new String("yyyy-MM-dd");

    private static final Font font = new Font("Arial", Font.BOLD, 12);

    private boolean modified;

    private Dive dive;

    private Window parentWindow;

    private JPanel mainPanel;

    private JTextField maxDepthTf;

    private JTextField waterTemperatureTf;

    private JTextField numberTf;

    private JTextField diveTimeTf;

    private JTextField surfaceTimeTf;

    private JTextArea commentTextArea;

    private DateTimeComponent dateTimeComponent;

    private DiveLocationChooser diveLocationChooser;

    private Set<DiveLocationSelectionListener> diveLocationSelectionListeners = new HashSet<DiveLocationSelectionListener>();

    private LogBookApplicationFacade logBookApplicationFacade;

    private KeyAdapter modifyAdapter;

    private PhysiologicalStatusPanel physiologicalStatusPanel;

    private JTextField altitudeTf;

    private JLabel altitudeLabel;

    private JLabel numberLabel;

    private JLabel dateLabel;

    private JLabel depthLabel;

    private JLabel diveTimeLabel;

    private JLabel diveSurfaceTimeLabel;

    private JLabel waterTemperatureLabel;

    private JLabel diveLocationLabel;

    private JXTaskPane commentTaskPane;

    private JXTaskPane palanqueeTaskPane;

    private JXTaskPane physiologicalTaskPane;

    private PalanqueeTablePanel palanqueeTablePanel;

    private JXTaskPane adminTaskPane;

    private JXTaskPane tankTaskPane;

    private JXTaskPane materialTaskPane;

    private DiveTanksPanel tanksPanel;

    private MaterialsPanel materialPanel;

    public DivePanel(Window parentWindow, LogBookApplicationFacade logBookApplicationFacade, Dive dive) {
        this.parentWindow = parentWindow;
        this.logBookApplicationFacade = logBookApplicationFacade;
        this.dive = dive;
        init();
        if (this.dive.getId() == -1) {
            modified = true;
        }
        fillDiveComponent();
        updateLocale();
    }

    public boolean isModified() {
        return modified;
    }

    private void init() {
        mainPanel = new JPanel();
        double[] cols = { 5, TableLayout.PREFERRED, 15, TableLayout.PREFERRED, TableLayout.FILL, 5 };
        double[] rows = { 5, TableLayout.PREFERRED, 5 };
        TableLayout tl = new TableLayout(cols, rows);
        mainPanel.setLayout(tl);
        modifyAdapter = new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                diveIsModified(true);
            }
        };
        mainPanel.add(createLeftTaskPanelContainer(), "1, 1");
        mainPanel.add(createRightTaskPaneContainer(), "3, 1");
        this.setLayout(new BorderLayout());
        JScrollPane scroll = new JScrollPane(mainPanel);
        this.add(scroll, BorderLayout.CENTER);
    }

    private Component createLeftTaskPanelContainer() {
        JXTaskPaneContainer taskContainer = new JXTaskPaneContainer();
        VerticalLayout vl = new VerticalLayout();
        vl.setGap(5);
        taskContainer.setLayout(vl);
        taskContainer.setOpaque(false);
        taskContainer.add(createAdminTaskPane());
        taskContainer.add(createCommentTaskPane());
        return taskContainer;
    }

    private JComponent createAdminTaskPane() {
        JXPanel staticDataPanel = new JXPanel();
        double[] cols = { 5, 100, 5, TableLayout.PREFERRED, TableLayout.FILL, 5 };
        double[] rows = { 5, TableLayout.MINIMUM, 5, TableLayout.MINIMUM, 5, TableLayout.MINIMUM, 5, TableLayout.MINIMUM, 5, TableLayout.MINIMUM, 5, TableLayout.MINIMUM, 5, TableLayout.MINIMUM, 5, TableLayout.MINIMUM, 5 };
        TableLayout tl = new TableLayout(cols, rows);
        staticDataPanel.setLayout(tl);
        staticDataPanel.add(createNumberLabel(), "1, 1, L, T");
        staticDataPanel.add(createDateLabel(), "1, 3, L, T");
        staticDataPanel.add(createDiveTimeLabel(), "1, 5, L, T");
        staticDataPanel.add(createMaxDepthLabel(), "1, 7, L, T");
        staticDataPanel.add(createSurfaceTimeLabel(), "1, 9, L, T");
        staticDataPanel.add(createWaterTemperatureLabel(), "1, 11, L, T");
        staticDataPanel.add(createAltitudeLabel(), "1, 13, L, T");
        staticDataPanel.add(createDivelocationLabel(), "1, 15, L, T");
        staticDataPanel.add(createNumberTextField(), "3, 1, L, T");
        staticDataPanel.add(createDateTimeComponent(), "3, 3, L, T");
        staticDataPanel.add(createDiveTimeTextField(), "3, 5, 4, 5, L, T");
        staticDataPanel.add(createMaxDepthTextField(), "3, 7, L, T");
        staticDataPanel.add(createSurfaceTimeTextField(), "3, 9, L, T");
        staticDataPanel.add(createWaterTemperatureTextField(), "3, 11, L, T");
        staticDataPanel.add(createAltitudeTextField(), "3, 13, L, T");
        staticDataPanel.add(createDivelocationComponent(), "3, 15, 4, 15, L, T");
        adminTaskPane = new JXTaskPane();
        adminTaskPane.add(staticDataPanel);
        return adminTaskPane;
    }

    private Component createAltitudeTextField() {
        altitudeTf = new JTextField(5);
        altitudeTf.addKeyListener(modifyAdapter);
        return altitudeTf;
    }

    private JComponent createAltitudeLabel() {
        altitudeLabel = new JLabel();
        return altitudeLabel;
    }

    private JComponent createRightTaskPaneContainer() {
        JXTaskPaneContainer taskContainer = new JXTaskPaneContainer();
        VerticalLayout vl = new VerticalLayout();
        vl.setGap(5);
        taskContainer.setLayout(vl);
        taskContainer.setOpaque(false);
        taskContainer.add(createPalanqueeTaskPane());
        taskContainer.add(createTankTaskPane());
        taskContainer.add(createMaterialTaskPane());
        taskContainer.add(createPhysiologicalTaskPane());
        return taskContainer;
    }

    private JComponent createNumberTextField() {
        numberTf = new JTextField(5);
        numberTf.addKeyListener(modifyAdapter);
        String[] a = { RegexPlainDocument.INTEGER };
        numberTf.setDocument(new RegexPlainDocument(a));
        return numberTf;
    }

    private JComponent createNumberLabel() {
        numberLabel = new JLabel();
        return numberLabel;
    }

    private JComponent createDateLabel() {
        dateLabel = new JLabel();
        return dateLabel;
    }

    private JComponent createDateTimeComponent() {
        dateTimeComponent = new DateTimeComponent();
        dateTimeComponent.setFormat(DATE_FORMAT);
        dateTimeComponent.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("date.modified".equals(evt.getPropertyName())) {
                    diveIsModified(true);
                }
            }
        });
        return dateTimeComponent;
    }

    private JComponent createMaxDepthLabel() {
        depthLabel = new JLabel();
        return depthLabel;
    }

    private JComponent createMaxDepthTextField() {
        maxDepthTf = new JTextField(5);
        maxDepthTf.addKeyListener(modifyAdapter);
        String[] a = { RegexPlainDocument.DOUBLE };
        maxDepthTf.setDocument(new RegexPlainDocument(a));
        return maxDepthTf;
    }

    private JComponent createDiveTimeLabel() {
        diveTimeLabel = new JLabel();
        return diveTimeLabel;
    }

    private JComponent createDiveTimeTextField() {
        diveTimeTf = new JTextField(5);
        diveTimeTf.addKeyListener(modifyAdapter);
        String[] a = { RegexPlainDocument.INTEGER };
        diveTimeTf.setDocument(new RegexPlainDocument(a));
        return diveTimeTf;
    }

    private JComponent createSurfaceTimeLabel() {
        diveSurfaceTimeLabel = new JLabel();
        return diveSurfaceTimeLabel;
    }

    private JComponent createSurfaceTimeTextField() {
        surfaceTimeTf = new JTextField(5);
        surfaceTimeTf.addKeyListener(modifyAdapter);
        String[] a = { RegexPlainDocument.INTEGER };
        surfaceTimeTf.setDocument(new RegexPlainDocument(a));
        return surfaceTimeTf;
    }

    private JComponent createWaterTemperatureLabel() {
        waterTemperatureLabel = new JLabel();
        return waterTemperatureLabel;
    }

    private JComponent createWaterTemperatureTextField() {
        waterTemperatureTf = new JTextField(5);
        waterTemperatureTf.addKeyListener(modifyAdapter);
        String[] a = { RegexPlainDocument.DOUBLE };
        waterTemperatureTf.setDocument(new RegexPlainDocument(a));
        return waterTemperatureTf;
    }

    private JComponent createCommentTaskPane() {
        commentTaskPane = new JXTaskPane();
        commentTextArea = new JTextArea();
        commentTextArea.addKeyListener(modifyAdapter);
        JScrollPane scroll = new JScrollPane(commentTextArea);
        scroll.setPreferredSize(new Dimension(200, 60));
        commentTaskPane.add(scroll);
        return commentTaskPane;
    }

    private JComponent createDivelocationLabel() {
        diveLocationLabel = new JLabel();
        return diveLocationLabel;
    }

    private JComponent createDivelocationComponent() {
        JXPanel divelocationPanel = new JXPanel();
        diveLocationChooser = new DiveLocationChooser(logBookApplicationFacade.getDiveLocationManagerFacade());
        diveLocationChooser.addDiveLocationSelectionListener(this);
        divelocationPanel.add(diveLocationChooser);
        diveLocationChooser.getComponent(0).addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                if (null != diveLocationChooser.getDiveLocation()) {
                    notifyDiveLocationSelectionListeners();
                }
            }
        });
        return diveLocationChooser;
    }

    private JComponent createPalanqueeTaskPane() {
        palanqueeTablePanel = new PalanqueeTablePanel(parentWindow, logBookApplicationFacade);
        palanqueeTablePanel.addDiverSelectionListener(new DiverSelectionListener() {

            @Override
            public void diverSelected(Diver diver) {
                notifyDiverSelectionListeners(diver);
            }
        });
        palanqueeTablePanel.addModificationListener(new ModificationListener() {

            @Override
            public void isModified(JComponent component, boolean isModified) {
                diveIsModified(isModified);
            }
        });
        palanqueeTaskPane = new JXTaskPane();
        palanqueeTaskPane.add(palanqueeTablePanel);
        return palanqueeTaskPane;
    }

    private JComponent createTankTaskPane() {
        tankTaskPane = new JXTaskPane();
        tanksPanel = new DiveTanksPanel(parentWindow);
        tanksPanel.addModificationListener(new ModificationListener() {

            @Override
            public void isModified(JComponent component, boolean isModified) {
                diveIsModified(isModified);
            }
        });
        tankTaskPane.add(tanksPanel);
        return tankTaskPane;
    }

    private JComponent createMaterialTaskPane() {
        materialTaskPane = new JXTaskPane();
        materialPanel = new MaterialsPanel(parentWindow);
        materialPanel.addModificationListener(new ModificationListener() {

            @Override
            public void isModified(JComponent component, boolean isModified) {
                diveIsModified(isModified);
            }
        });
        materialTaskPane.add(materialPanel);
        return materialTaskPane;
    }

    private JComponent createPhysiologicalTaskPane() {
        physiologicalTaskPane = new JXTaskPane();
        physiologicalStatusPanel = new PhysiologicalStatusPanel();
        physiologicalStatusPanel.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("date.modified".equals(evt.getPropertyName())) {
                    diveIsModified(true);
                }
            }
        });
        physiologicalTaskPane.add(physiologicalStatusPanel);
        return physiologicalTaskPane;
    }

    public Dive getDisplayedDive() {
        Dive dive = new Dive();
        dive.setId(this.dive.getId());
        dive.setDiveProfile(this.dive.getDiveProfile());
        dive.setNumber(Integer.parseInt(numberTf.getText()));
        if (null != dateTimeComponent.getDate()) {
            dive.setDate(dateTimeComponent.getDate());
        }
        if (null != maxDepthTf.getText() && !("".equals(maxDepthTf.getText().trim()))) {
            dive.setMaxDepth(Float.parseFloat(maxDepthTf.getText()));
        }
        if (null != diveTimeTf.getText() && !("".equals(diveTimeTf.getText().trim()))) {
            dive.setDiveTime(Integer.parseInt(diveTimeTf.getText()));
        }
        if (null != waterTemperatureTf.getText() && !("".equals(waterTemperatureTf.getText().trim()))) {
            dive.setWaterTemperature(Float.parseFloat(waterTemperatureTf.getText()));
        }
        if (null != surfaceTimeTf.getText() && !("".equals(surfaceTimeTf.getText().trim()))) {
            dive.setSurfaceTime(Integer.parseInt(surfaceTimeTf.getText()));
        }
        if (null != commentTextArea.getText() && !("".equals(commentTextArea.getText().trim()))) {
            dive.setComment(commentTextArea.getText());
        }
        if (null != altitudeTf.getText() && !("".equals(altitudeTf.getText().trim()))) {
            dive.setAltitude(Integer.parseInt(altitudeTf.getText()));
        }
        dive.setDiveLocation(diveLocationChooser.getDiveLocation());
        dive.setPalanquee(palanqueeTablePanel.getPalanquee());
        dive.setPhysiologicalStatus(physiologicalStatusPanel.getPhysiologicalStatus());
        dive.getEquipment().setDiveTanks(tanksPanel.getDiveTanks());
        dive.getEquipment().setMaterial(materialPanel.getMaterial());
        return dive;
    }

    public Dive getDive() {
        return dive;
    }

    private void fillDiveComponent() {
        numberTf.setText(String.valueOf(dive.getNumber()));
        if (null != dive.getDate()) {
            dateTimeComponent.activateNotification(false);
            dateTimeComponent.setDate(dive.getDate());
            dateTimeComponent.activateNotification(true);
        }
        maxDepthTf.setText(String.valueOf(dive.getMaxDepth()));
        diveTimeTf.setText(String.valueOf(dive.getDiveTime()));
        waterTemperatureTf.setText(String.valueOf(dive.getWaterTemperature()));
        surfaceTimeTf.setText(String.valueOf(dive.getSurfaceTime()));
        commentTextArea.setText(dive.getComment());
        altitudeTf.setText(String.valueOf(dive.getAltitude()));
        palanqueeTablePanel.setPalanquee(dive.getPalanquee());
        diveLocationChooser.setDiveLocation(dive.getDiveLocation());
        physiologicalStatusPanel.activateNotification(false);
        physiologicalStatusPanel.setPhysiologicalStatus(dive.getPhysiologicalStatus());
        physiologicalStatusPanel.activateNotification(true);
        tanksPanel.activateNotification(false);
        tanksPanel.setDiveTanks(dive.getEquipment().getDiveTanks());
        tanksPanel.activateNotification(true);
        materialPanel.activateNotification(false);
        materialPanel.setMaterial(dive.getEquipment().getMaterial());
        materialPanel.activateNotification(true);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(new DivePanel(f, new LogBookApplicationFacade(new LogBookApplication()), new Dive()));
        f.pack();
        f.setVisible(true);
    }

    private void diveIsModified(boolean b) {
        boolean oldValue = modified;
        this.modified = b;
        firePropertyChange("dive.modified", oldValue, this.modified);
    }

    public void setDive(Dive dive) {
        this.dive = dive;
        fillDiveComponent();
        diveIsModified(false);
    }

    public void addDiveLocationSelectionListener(DiveLocationSelectionListener diveLocationSelectionListener) {
        diveLocationSelectionListeners.add(diveLocationSelectionListener);
    }

    private void notifyDiveLocationSelectionListeners() {
        for (DiveLocationSelectionListener listener : diveLocationSelectionListeners) {
            listener.diveLocationSelected(diveLocationChooser.getDiveLocation());
        }
    }

    @Override
    public void diveLocationSelected(DiveLocation diveLocation) {
        diveIsModified(true);
    }

    public void updateLocale() {
        adminTaskPane.setTitle(LanguageManager.getKey("administration"));
        altitudeLabel.setText(LanguageManager.getKey("altitude"));
        altitudeLabel.setToolTipText(LanguageManager.getKey("altitude"));
        numberLabel.setText(LanguageManager.getKey("number"));
        numberLabel.setToolTipText(LanguageManager.getKey("number"));
        dateLabel.setText(LanguageManager.getKey("date"));
        dateLabel.setToolTipText(LanguageManager.getKey("date"));
        depthLabel.setText(LanguageManager.getKey("depth"));
        depthLabel.setToolTipText(LanguageManager.getKey("depth"));
        diveTimeLabel.setText(LanguageManager.getKey("dive.time.short"));
        diveTimeLabel.setToolTipText(LanguageManager.getKey("dive.time"));
        diveSurfaceTimeLabel.setText(LanguageManager.getKey("dive.surface.time.before.short"));
        diveSurfaceTimeLabel.setToolTipText(LanguageManager.getKey("dive.surface.time.before"));
        waterTemperatureLabel.setText(LanguageManager.getKey("water.temperature.short"));
        waterTemperatureLabel.setToolTipText(LanguageManager.getKey("water.temperature"));
        diveLocationLabel.setText(LanguageManager.getKey("divelocation.short"));
        diveLocationLabel.setToolTipText(LanguageManager.getKey("divelocation"));
        commentTaskPane.setTitle(LanguageManager.getKey("comment"));
        palanqueeTaskPane.setTitle(LanguageManager.getKey("palanquee"));
        palanqueeTablePanel.updateLocale();
        physiologicalTaskPane.setTitle(LanguageManager.getKey("physiologic.status"));
        physiologicalStatusPanel.updateLocale();
        tankTaskPane.setTitle(LanguageManager.getKey("tanks"));
        tanksPanel.updateLocale();
        materialTaskPane.setTitle(LanguageManager.getKey("material"));
        materialPanel.updateLocale();
    }
}

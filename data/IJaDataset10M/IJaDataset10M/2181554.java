package ch.intertec.storybook.view.chrono;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;
import net.miginfocom.swing.MigLayout;
import ch.intertec.storybook.SbConstants;
import ch.intertec.storybook.SbConstants.InternalKey;
import ch.intertec.storybook.controller.DocumentController;
import ch.intertec.storybook.model.hbn.entity.Internal;
import ch.intertec.storybook.model.hbn.entity.Scene;
import ch.intertec.storybook.toolkit.EntityUtil;
import ch.intertec.storybook.toolkit.swing.SwingUtil;
import ch.intertec.storybook.toolkit.swing.undo.UndoableTextArea;
import ch.intertec.storybook.view.AbstractScenePanel;
import ch.intertec.storybook.view.MainFrame;
import ch.intertec.storybook.view.label.SceneStateLabel;
import ch.intertec.storybook.view.linkspanel.LocationLinksPanel;
import ch.intertec.storybook.view.linkspanel.PersonLinksPanel;
import ch.intertec.storybook.view.linkspanel.StrandLinksPanel;

@SuppressWarnings("serial")
public class ChronoScenePanel extends AbstractScenePanel implements FocusListener {

    private final String CN_TITLE = "taTitle";

    private final String CN_TEXT = "tcText";

    private JPanel upperPanel;

    private UndoableTextArea taTitle;

    private JTextComponent tcText;

    private JLabel lbStatus;

    private JLabel lbSceneNo;

    private Integer size;

    public ChronoScenePanel(MainFrame mainFrame, Scene scene) {
        super(mainFrame, scene, true, Color.white, scene.getStrand().getJColor());
        init();
        initUi();
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        Object newValue = evt.getNewValue();
        String propName = evt.getPropertyName();
        if (DocumentController.StrandProps.UPDATE.check(propName)) {
            EntityUtil.refresh(mainFrame, scene.getStrand());
            setEndBgColor(scene.getStrand().getJColor());
            repaint();
            return;
        }
        if (DocumentController.SceneProps.UPDATE.check(propName)) {
            Scene newScene = (Scene) newValue;
            if (newScene.getId() != scene.getId()) {
                return;
            }
            scene = newScene;
            lbSceneNo.setText(scene.getChapterSceneNo(false));
            lbSceneNo.setToolTipText(scene.getChapterSceneToolTip());
            lbStatus.setIcon(scene.getStatusIcon());
            taTitle.setText(scene.getTitle());
            taTitle.setCaretPosition(0);
            tcText.setText(scene.getSummary());
            tcText.setCaretPosition(0);
            return;
        }
        if (DocumentController.ChronoViewProps.ZOOM.check(propName)) {
            setZoomedSize((Integer) newValue);
            refresh();
            return;
        }
    }

    private void setZoomedSize(int zoomValue) {
        size = zoomValue * 7;
    }

    @Override
    public void init() {
        try {
            Internal internal = EntityUtil.restoreInternal(mainFrame, InternalKey.CHRONO_ZOOM, SbConstants.DEFAULT_CHRONO_ZOOM);
            setZoomedSize(internal.getIntegerValue());
        } catch (Exception e) {
            e.printStackTrace();
            setZoomedSize(SbConstants.DEFAULT_CHRONO_ZOOM);
        }
    }

    @Override
    public void initUi() {
        refresh();
    }

    @Override
    public void refresh() {
        boolean showSummary = true;
        MigLayout layout = new MigLayout("fill,flowy,insets 4", "[]", "[][grow]");
        setLayout(layout);
        setPreferredSize(new Dimension(size, size));
        setComponentPopupMenu(EntityUtil.createPopupMenu(mainFrame, scene));
        removeAll();
        setBorder(SwingUtil.getBorderDefault());
        if (scene.hasChapter()) {
            if (scene.getChapter().getPart().getId() != mainFrame.getCurrentPart().getId()) {
                setBorder(SwingUtil.getBorderDot());
            }
        }
        StrandLinksPanel strandLinksPanel = new StrandLinksPanel(mainFrame, scene, true);
        PersonLinksPanel personLinksPanel = new PersonLinksPanel(mainFrame, scene);
        LocationLinksPanel locationLinksPanel = new LocationLinksPanel(mainFrame, scene);
        btNew = getNewButton();
        btNew.setSize20x20();
        btDelete = getDeleteButton();
        btDelete.setSize20x20();
        btEdit = getEditButton();
        btEdit.setSize20x20();
        lbSceneNo = new JLabel("", SwingConstants.CENTER);
        lbSceneNo.setText(scene.getChapterSceneNo(false));
        lbSceneNo.setToolTipText(scene.getChapterSceneToolTip());
        lbSceneNo.setOpaque(true);
        lbSceneNo.setBackground(Color.white);
        lbStatus = new SceneStateLabel(scene.getSceneState(), true);
        taTitle = new UndoableTextArea();
        taTitle.setName(CN_TITLE);
        taTitle.setText(scene.getTitle());
        taTitle.setLineWrap(true);
        taTitle.setWrapStyleWord(true);
        taTitle.setDragEnabled(true);
        taTitle.setCaretPosition(0);
        taTitle.getUndoManager().discardAllEdits();
        taTitle.addFocusListener(this);
        JScrollPane spTitle = new JScrollPane(taTitle);
        spTitle.setPreferredSize(new Dimension(50, 35));
        tcText = SwingUtil.createTextComponent(mainFrame);
        tcText.setName(CN_TEXT);
        tcText.setText(scene.getText());
        tcText.setDragEnabled(true);
        tcText.addFocusListener(this);
        JScrollPane spText = new JScrollPane(tcText);
        spText.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        spText.setPreferredSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        JPanel buttonPanel = new JPanel(new MigLayout("flowy,insets 0"));
        buttonPanel.setName("buttonpanel");
        buttonPanel.setOpaque(false);
        buttonPanel.add(btEdit);
        buttonPanel.add(btDelete);
        buttonPanel.add(btNew);
        upperPanel = new JPanel(new MigLayout("insets 0", "[][grow][]", "[top][top][top]"));
        upperPanel.setName("upperpanel");
        upperPanel.setOpaque(false);
        upperPanel.add(lbSceneNo, "grow,width pref+10px,split 2");
        upperPanel.add(lbStatus);
        upperPanel.add(strandLinksPanel, "grow");
        upperPanel.add(buttonPanel, "spany 3,wrap");
        JScrollPane scroller = new JScrollPane(personLinksPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.setMinimumSize(new Dimension(20, 16));
        scroller.setOpaque(false);
        scroller.getViewport().setOpaque(false);
        scroller.setBorder(null);
        upperPanel.add(scroller, "spanx 2,growx,wrap");
        upperPanel.add(locationLinksPanel, "spanx 2,grow,wrap");
        add(upperPanel, "growx");
        if (showSummary) {
            add(spTitle, "growx, h 35!");
            add(spText, "grow");
        } else {
            add(spTitle, "grow");
        }
        revalidate();
        repaint();
        tcText.setCaretPosition(0);
        taTitle.setCaretPosition(0);
    }

    protected ChronoScenePanel getThis() {
        return this;
    }

    public Scene getScene() {
        return this.scene;
    }

    @Override
    public void focusGained(FocusEvent e) {
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (e.getSource() instanceof JTextComponent) {
            JTextComponent tc = (JTextComponent) e.getSource();
            if (CN_TITLE.equals(tc.getName())) {
                scene.setTitle(tc.getText());
            } else if (CN_TEXT.equals(tc.getName())) {
                scene.setSummary(tc.getText());
            }
            mainFrame.getDocumentController().updateScene(scene);
        }
    }
}

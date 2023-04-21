package pl.otros.logview.gui.markers.editor;

import net.miginfocom.swing.MigLayout;
import pl.otros.logview.gui.markers.AutomaticMarker;
import pl.otros.logview.gui.markers.PropertyFileAbstractMarker;
import pl.otros.logview.gui.renderers.AutomaticMarkerRenderer;
import pl.otros.logview.importer.InitializationException;
import pl.otros.logview.loader.LvDynamicLoader;
import pl.otros.logview.pluginable.AllPluginables;
import pl.otros.logview.pluginable.PluginableElementsContainer;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.IOException;

public class MarkersEditor extends JPanel {

    private static final String CARD_LAYOUT_JAVA_BASED = "JAVA_BASED";

    private static final String CARD_LAYOUT_PROPERTIES_BASED = "PROP_BASED";

    private static final String CARD_LAYOUT_NO_SELECTION = "EMPTY";

    private JList markersList;

    private MarkerEditor editor;

    private CardLayout cardLayout;

    private JPanel southPanel;

    private JButton buttonNew;

    private JButton buttonSave;

    private MarkersListModel markersListModel;

    private PluginableElementsContainer<AutomaticMarker> markersContainser;

    public MarkersEditor() {
        final JPanel editorPanel = new JPanel();
        cardLayout = new CardLayout();
        editorPanel.setLayout(cardLayout);
        markersContainser = AllPluginables.getInstance().getMarkersContainser();
        markersListModel = new MarkersListModel(markersContainser.getElements());
        markersList = new JList(markersListModel);
        markersList.setCellRenderer(new AutomaticMarkerRenderer());
        markersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        markersList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (markersList.getSelectedIndex() > -1) {
                    AutomaticMarker automaticMarker = (AutomaticMarker) markersList.getSelectedValue();
                    if (automaticMarker instanceof PropertyFileAbstractMarker) {
                        PropertyFileAbstractMarker pmark = (PropertyFileAbstractMarker) automaticMarker;
                        editor.setViewFromProperties(pmark.toProperties());
                        cardLayout.show(editorPanel, CARD_LAYOUT_PROPERTIES_BASED);
                    } else {
                        cardLayout.show(editorPanel, CARD_LAYOUT_JAVA_BASED);
                    }
                } else {
                    cardLayout.show(editorPanel, CARD_LAYOUT_NO_SELECTION);
                }
                setSaveButtonEnable();
            }
        });
        markersContainser.addListener(markersListModel);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        editor = new MarkerEditor();
        editorPanel.add(editor, CARD_LAYOUT_PROPERTIES_BASED);
        editorPanel.add(new JLabel("This marker is code based, you can't edit it", SwingConstants.CENTER), CARD_LAYOUT_JAVA_BASED);
        editorPanel.add(new JLabel("No marker selcted. Select marker from list", SwingConstants.CENTER), CARD_LAYOUT_NO_SELECTION);
        cardLayout.show(editorPanel, CARD_LAYOUT_NO_SELECTION);
        editor.addChangeListener(new MarkerChangeListener());
        initSouthPanel();
        setSaveButtonEnable();
        splitPane.add(markersList);
        splitPane.add(new JScrollPane(editorPanel));
        this.setLayout(new BorderLayout());
        this.add(splitPane);
        this.add(southPanel, BorderLayout.SOUTH);
    }

    private void initSouthPanel() {
        southPanel = new JPanel(new MigLayout("", "[right]", ""));
        buttonNew = new JButton(new NewMarkerAction());
        southPanel.add(buttonNew, "right");
        buttonSave = new JButton(new SaveMarkerAction(editor));
        southPanel.add(buttonSave, "right");
    }

    protected void setSaveButtonEnable() {
        boolean b = false;
        if (markersListModel.getSize() > 0 && markersList.getSelectedIndex() > -1) {
            AutomaticMarker selectedValue = (AutomaticMarker) markersList.getSelectedValue();
            if (selectedValue instanceof PropertyFileAbstractMarker) {
                b = editor.isChanged();
            }
        }
        buttonSave.setEnabled(b);
    }

    private class MarkerChangeListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            setSaveButtonEnable();
        }
    }

    public static void main(String[] args) throws IOException, InitializationException {
        LvDynamicLoader.getInstance().loadAll();
        JFrame frame = new JFrame("Markers editor");
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(new MarkersEditor());
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

package net.sf.gridarta.gui.dialog.gomap;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.TreeSet;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import net.sf.gridarta.gui.map.mapview.MapViewsManager;
import net.sf.gridarta.gui.utils.SwingUtils;
import net.sf.gridarta.gui.utils.TextComponentUtils;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.index.IndexListener;
import net.sf.gridarta.model.index.MapsIndex;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.gridarta.utils.DelayedChangeListener;
import net.sf.gridarta.utils.DelayedChangeManager;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;

/**
 * A dialog to ask the user for a map to open.
 * @author Andreas Kirschbaum
 */
public class GoMapDialog<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The {@link ActionBuilder}.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The {@link JDialog} instance.}
     */
    @NotNull
    private final JDialog dialog;

    /**
     * The map name input field.
     */
    @NotNull
    private final JTextComponent input;

    /**
     * The {@link MapsIndex} for looking up maps.
     */
    @NotNull
    private final MapsIndex mapsIndex;

    /**
     * The {@link MapViewsManager} for opening maps.
     */
    @NotNull
    private final MapViewsManager<G, A, R> mapViewsManager;

    /**
     * The list model containing the search results.
     */
    @NotNull
    private final DefaultListModel listModel = new DefaultListModel();

    /**
     * The {@link JList} showing the matching maps.
     */
    @NotNull
    private final JList list = new JList(listModel);

    /**
     * Whether {@link #dialog} is currently shown.
     */
    private boolean dialogShown = false;

    /**
     * A {@link Comparator} that orders map files by map name.
     */
    @NotNull
    private final Comparator<File> mapNameComparator = new Comparator<File>() {

        @Override
        public int compare(@NotNull final File o1, @NotNull final File o2) {
            final String name1 = mapsIndex.getName(o1);
            final String name2 = mapsIndex.getName(o2);
            if (name1 == null) {
                return name2 == null ? 0 : -1;
            }
            if (name2 == null) {
                return +1;
            }
            final int cmp = name1.compareToIgnoreCase(name2);
            if (cmp != 0) {
                return cmp;
            }
            return o1.compareTo(o2);
        }
    };

    /**
     * The {@link IndexListener} attached to {@link #mapsIndex} to update search
     * results after index changes. The listener is attached only if {@link
     * #dialogShown} is set; if the dialog is closed, changes are not tracked.
     */
    @NotNull
    private final IndexListener<File> indexListener = new IndexListener<File>() {

        @Override
        public void valueAdded(@NotNull final File value) {
        }

        @Override
        public void valueRemoved(@NotNull final File value) {
        }

        @Override
        public void nameChanged() {
            delayedChangeManager.change();
        }

        @Override
        public void pendingChanged() {
        }

        @Override
        public void indexingFinished() {
            delayedChangeManager.finish();
        }
    };

    /**
     * The {@link WindowListener} attached to {@link #dialog} to track
     * opening/closing the dialog. Registers {@link #indexListener} to {@link
     * #mapsIndex} when the dialog is opened and de-registers when the dialog is
     * closed.
     */
    @NotNull
    private final WindowListener windowListener = new WindowListener() {

        @Override
        public void windowOpened(final WindowEvent e) {
            if (!dialogShown) {
                dialogShown = true;
                mapsIndex.addIndexListener(indexListener);
                doSearch();
            }
        }

        @Override
        public void windowClosing(final WindowEvent e) {
        }

        @Override
        public void windowClosed(final WindowEvent e) {
            if (dialogShown) {
                dialogShown = false;
                mapsIndex.removeIndexListener(indexListener);
            }
        }

        @Override
        public void windowIconified(final WindowEvent e) {
        }

        @Override
        public void windowDeiconified(final WindowEvent e) {
        }

        @Override
        public void windowActivated(final WindowEvent e) {
        }

        @Override
        public void windowDeactivated(final WindowEvent e) {
        }
    };

    /**
     * A {@link DelayedChangeManager} for updating search results after {@link
     * #mapsIndex} changes.
     */
    @NotNull
    private final DelayedChangeManager delayedChangeManager = new DelayedChangeManager(100, 1000, new DelayedChangeListener() {

        @Override
        public void change() {
            if (!dialogShown) {
                return;
            }
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    if (dialogShown) {
                        doSearch();
                    }
                }
            });
        }
    });

    /**
     * Creates a new instance.
     * @param parent the parent component for this dialog
     * @param mapsIndex the maps index to use
     * @param mapViewsManager the map views manager for opening maps
     */
    public GoMapDialog(@NotNull final Window parent, @NotNull final MapsIndex mapsIndex, @NotNull final MapViewsManager<G, A, R> mapViewsManager) {
        this.mapsIndex = mapsIndex;
        this.mapViewsManager = mapViewsManager;
        final Component label = new JLabel(ActionBuilderUtils.getString(ACTION_BUILDER, "goMapLabel"));
        input = new JTextField();
        SwingUtils.addAction(input, ACTION_BUILDER.createAction(false, "goMapScrollUp", this));
        SwingUtils.addAction(input, ACTION_BUILDER.createAction(false, "goMapScrollDown", this));
        SwingUtils.addAction(input, ACTION_BUILDER.createAction(false, "goMapScrollPageUp", this));
        SwingUtils.addAction(input, ACTION_BUILDER.createAction(false, "goMapScrollPageDown", this));
        SwingUtils.addAction(input, ACTION_BUILDER.createAction(false, "goMapScrollTop", this));
        SwingUtils.addAction(input, ACTION_BUILDER.createAction(false, "goMapScrollBottom", this));
        SwingUtils.addAction(input, ACTION_BUILDER.createAction(false, "goMapSelectUp", this));
        SwingUtils.addAction(input, ACTION_BUILDER.createAction(false, "goMapSelectDown", this));
        SwingUtils.addAction(input, ACTION_BUILDER.createAction(false, "goMapCancel", this));
        SwingUtils.addAction(input, ACTION_BUILDER.createAction(false, "goMapApply", this));
        final DocumentListener documentListener = new DocumentListener() {

            @Override
            public void changedUpdate(@NotNull final DocumentEvent e) {
                doSearch();
            }

            @Override
            public void insertUpdate(@NotNull final DocumentEvent e) {
                doSearch();
            }

            @Override
            public void removeUpdate(@NotNull final DocumentEvent e) {
                doSearch();
            }
        };
        input.getDocument().addDocumentListener(documentListener);
        TextComponentUtils.setAutoSelectOnFocus(input);
        final JComponent inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(label, BorderLayout.NORTH);
        inputPanel.add(input, BorderLayout.CENTER);
        inputPanel.setBorder(new EmptyBorder(3, 3, 3, 3));
        list.setFocusable(false);
        final Component scrollPane = new JScrollPane(list, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        list.setCellRenderer(new MapListCellRenderer(mapsIndex));
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        final MouseListener mouseListener = new MouseListener() {

            @Override
            public void mouseClicked(final MouseEvent e) {
            }

            @Override
            public void mousePressed(final MouseEvent e) {
                if (e.getClickCount() > 1) {
                    goMapApply();
                }
            }

            @Override
            public void mouseReleased(final MouseEvent e) {
            }

            @Override
            public void mouseEntered(final MouseEvent e) {
            }

            @Override
            public void mouseExited(final MouseEvent e) {
            }
        };
        list.addMouseListener(mouseListener);
        dialog = new JDialog(parent);
        dialog.setResizable(false);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(parent);
        dialog.setUndecorated(true);
        dialog.setLayout(new BorderLayout());
        dialog.add(inputPanel, BorderLayout.NORTH);
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
        dialog.addWindowListener(windowListener);
    }

    /**
     * Opens the dialog.
     */
    public void showDialog() {
        dialog.setVisible(true);
    }

    /**
     * Updates the maps display from the map name input field.
     */
    private void doSearch() {
        final String mapName = input.getText();
        if (mapName.isEmpty()) {
            listModel.clear();
            return;
        }
        final Collection<File> maps = mapsIndex.findPartialName(mapName);
        final Collection<File> sortedMaps = new TreeSet<File>(mapNameComparator);
        sortedMaps.addAll(maps);
        final Collection<Object> selectedValues = new HashSet<Object>(Arrays.asList(list.getSelectedValues()));
        listModel.clear();
        int visibleIndex = -1;
        for (final File map : sortedMaps) {
            listModel.addElement(map);
            if (selectedValues.contains(map)) {
                final int index = listModel.size() - 1;
                list.addSelectionInterval(index, index);
                visibleIndex = index;
            }
        }
        if (list.getSelectedIndex() == -1) {
            list.setSelectedIndex(0);
            visibleIndex = 0;
        }
        if (visibleIndex != -1) {
            list.ensureIndexIsVisible(visibleIndex);
        }
    }

    /**
     * Action method for apply.
     */
    @ActionMethod
    public void goMapApply() {
        if (goMap()) {
            dialog.removeWindowListener(windowListener);
            dialog.dispose();
        }
    }

    /**
     * Action method for cancel.
     */
    @ActionMethod
    public void goMapCancel() {
        dialog.removeWindowListener(windowListener);
        dialog.dispose();
    }

    /**
     * Action method for scroll up.
     */
    @ActionMethod
    public void goMapScrollUp() {
        final int index = list.getMinSelectionIndex();
        final int newIndex = index > 0 ? index - 1 : listModel.size() - 1;
        list.setSelectedIndex(newIndex);
        list.ensureIndexIsVisible(newIndex);
    }

    /**
     * Action method for scroll down.
     */
    @ActionMethod
    public void goMapScrollDown() {
        final int index = list.getMaxSelectionIndex() + 1;
        final int newIndex = index < listModel.size() ? index : 0;
        list.setSelectedIndex(newIndex);
        list.ensureIndexIsVisible(newIndex);
    }

    /**
     * Action method for scroll page up.
     */
    @ActionMethod
    public void goMapScrollPageUp() {
        final int index = list.getMinSelectionIndex();
        final int firstIndex = list.getFirstVisibleIndex();
        final int newIndex;
        if (firstIndex == -1) {
            newIndex = -1;
        } else if (index == -1) {
            newIndex = firstIndex;
        } else if (index > firstIndex) {
            newIndex = firstIndex;
        } else {
            newIndex = Math.max(firstIndex - (list.getLastVisibleIndex() - firstIndex), 0);
        }
        list.setSelectedIndex(newIndex);
        list.ensureIndexIsVisible(newIndex);
    }

    /**
     * Action method for scroll page down.
     */
    @ActionMethod
    public void goMapScrollPageDown() {
        final int index = list.getMaxSelectionIndex();
        final int lastIndex = list.getLastVisibleIndex();
        final int newIndex;
        if (lastIndex == -1) {
            newIndex = -1;
        } else if (index == -1) {
            newIndex = lastIndex;
        } else if (index < lastIndex) {
            newIndex = lastIndex;
        } else {
            newIndex = Math.min(lastIndex + (lastIndex - list.getFirstVisibleIndex()), listModel.size() - 1);
        }
        list.setSelectedIndex(newIndex);
        list.ensureIndexIsVisible(newIndex);
    }

    /**
     * Action method for scroll top.
     */
    @ActionMethod
    public void goMapScrollTop() {
        final int newIndex = 0;
        list.setSelectedIndex(newIndex);
        list.ensureIndexIsVisible(newIndex);
    }

    /**
     * Action method for scroll bottom.
     */
    @ActionMethod
    public void goMapScrollBottom() {
        final int newIndex = listModel.size() - 1;
        list.setSelectedIndex(newIndex);
        list.ensureIndexIsVisible(newIndex);
    }

    /**
     * Action method for select up.
     */
    @ActionMethod
    public void goMapSelectUp() {
        final int index = list.getMinSelectionIndex();
        if (index != 0) {
            final int newIndex = index > 0 ? index - 1 : listModel.size() - 1;
            list.addSelectionInterval(newIndex, newIndex);
            list.ensureIndexIsVisible(newIndex);
        }
    }

    /**
     * Action method for select down.
     */
    @ActionMethod
    public void goMapSelectDown() {
        final int index = list.getMaxSelectionIndex();
        if (index + 1 < listModel.size()) {
            final int newIndex = index + 1;
            list.addSelectionInterval(newIndex, newIndex);
            list.ensureIndexIsVisible(newIndex);
        }
    }

    /**
     * Opens the selected maps.
     * @return whether at least one map was opened
     */
    private boolean goMap() {
        final Object[] selectedValues = list.getSelectedValues();
        boolean result = false;
        for (final Object selectedValue : selectedValues) {
            try {
                mapViewsManager.openMapFileWithView((File) selectedValue, null, null);
                result = true;
            } catch (final IOException ex) {
                ACTION_BUILDER.showMessageDialog(dialog, "goMapIOException", selectedValue, ex.getMessage());
            }
        }
        return result;
    }
}

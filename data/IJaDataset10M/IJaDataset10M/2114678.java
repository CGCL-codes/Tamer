package org.simbrain.world.oscworld;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import org.simbrain.util.LabelledItemPanel;
import org.simbrain.workspace.gui.GenericFrame;
import org.simbrain.workspace.gui.GuiComponent;

/**
 * OSC world desktop component.
 */
public final class OscWorldDesktopComponent extends GuiComponent<OscWorldComponent> {

    /** List of OSC out message consumers. */
    private final JList consumers;

    /** List of OSC in message producers. */
    private final JList producers;

    /**
     * Create a new OSC world desktop component with the specified OSC world component.
     *
     * @param frame parent frame
     * @param oscWorldComponent OSC world component
     */
    public OscWorldDesktopComponent(final GenericFrame frame, final OscWorldComponent oscWorldComponent) {
        super(frame, oscWorldComponent);
        Action closeAction = new CloseAction();
        Action createOscInMessageAction = new CreateOscInMessageAction();
        Action createOscOutMessageAction = new CreateOscOutMessageAction();
        consumers = new JList();
        producers = new JList();
        consumers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        consumers.setVisibleRowCount(16);
        consumers.addMouseListener(new MouseAdapter() {

            /** {@inheritDoc} */
            public void mousePressed(final MouseEvent event) {
                if (event.isPopupTrigger()) {
                    showContextMenu(event);
                }
            }

            /** {@inheritDoc} */
            public void mouseReleased(final MouseEvent event) {
                if (event.isPopupTrigger()) {
                    showContextMenu(event);
                }
            }

            /**
                 * Show the consumer context menu if a consumer is selected.
                 *
                 * @param event mouse event
                 */
            private void showContextMenu(final MouseEvent event) {
                if (consumers.getSelectedIndex() > -1) {
                }
            }
        });
        producers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        producers.setVisibleRowCount(16);
        producers.addMouseListener(new MouseAdapter() {

            /** {@inheritDoc} */
            public void mousePressed(final MouseEvent event) {
                if (event.isPopupTrigger()) {
                    showContextMenu(event);
                }
            }

            /** {@inheritDoc} */
            public void mouseReleased(final MouseEvent event) {
                if (event.isPopupTrigger()) {
                    showContextMenu(event);
                }
            }

            /**
                 * Show the consumer context menu if a consumer is selected.
                 *
                 * @param event mouse event
                 */
            private void showContextMenu(final MouseEvent event) {
                if (producers.getSelectedIndex() > -1) {
                    JPopupMenu contextMenu = new JPopupMenu();
                }
            }
        });
        JMenuBar menuBar = new JMenuBar();
        JToolBar toolBar = new JToolBar();
        JMenu file = new JMenu("File");
        file.add(createOscInMessageAction);
        file.add(createOscOutMessageAction);
        file.addSeparator();
        file.add(closeAction);
        toolBar.add(createOscInMessageAction);
        toolBar.add(createOscOutMessageAction);
        menuBar.add(file);
        getParentFrame().setJMenuBar(menuBar);
        LabelledItemPanel inPanel = new LabelledItemPanel();
        inPanel.addItem("OSC in host:", new JLabel(oscWorldComponent.getOscInHost()));
        inPanel.addItem("OSC in port:", new JLabel(String.valueOf(oscWorldComponent.getOscInPort())));
        inPanel.addItem("OSC in messages:", new JScrollPane(producers));
        LabelledItemPanel outPanel = new LabelledItemPanel();
        outPanel.addItem("OSC out host:", new JLabel(oscWorldComponent.getOscOutHost()));
        outPanel.addItem("OSC out port:", new JLabel(String.valueOf(oscWorldComponent.getOscOutPort())));
        outPanel.addItem("OSC out messages:", new JScrollPane(consumers));
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(12, 12, 12, 12));
        mainPanel.setLayout(new GridLayout(1, 2, 12, 12));
        mainPanel.add(inPanel);
        mainPanel.add(outPanel);
        setLayout(new BorderLayout());
        add("North", toolBar);
        add("Center", mainPanel);
    }

    /** {@inheritDoc} */
    public void closing() {
    }

    /**
     * Close action.
     */
    private final class CloseAction extends AbstractAction {

        /**
         * Create a new close action.
         */
        CloseAction() {
            super("Close");
            putValue(Action.LONG_DESCRIPTION, "Close");
            KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
            putValue(Action.ACCELERATOR_KEY, keyStroke);
        }

        /** {@inheritDoc} */
        public void actionPerformed(final ActionEvent event) {
            close();
        }
    }

    /**
     * Create OSC in message action.
     */
    private final class CreateOscInMessageAction extends AbstractAction {

        /**
         * Create a new create OSC in message action.
         */
        CreateOscInMessageAction() {
            super("Create OSC in message");
            putValue(Action.LONG_DESCRIPTION, "Create a new OSC in message");
        }

        /** {@inheritDoc} */
        public void actionPerformed(final ActionEvent event) {
            SwingUtilities.invokeLater(new Runnable() {

                /** {@inheritDoc} */
                public void run() {
                    String address = JOptionPane.showInputDialog(null, "Create a new OSC in message with the specified address.\nThe address must begin with a '/' character.\n\n\nOSC in message address:", "Create a new OSC in message", JOptionPane.QUESTION_MESSAGE);
                    getWorkspaceComponent().addInMessage(address);
                }
            });
        }
    }

    /**
     * Create OSC out message action.
     */
    private final class CreateOscOutMessageAction extends AbstractAction {

        /**
         * Create a new create OSC out message action.
         */
        CreateOscOutMessageAction() {
            super("Create OSC out message");
            putValue(Action.LONG_DESCRIPTION, "Create a new OSC out message");
        }

        /** {@inheritDoc} */
        public void actionPerformed(final ActionEvent event) {
            SwingUtilities.invokeLater(new Runnable() {

                /** {@inheritDoc} */
                public void run() {
                    String address = JOptionPane.showInputDialog(null, "Create a new OSC out message with the specified address.\nThe address must begin with a '/' character.\n\n\nOSC out message address:", "Create a new OSC out message", JOptionPane.QUESTION_MESSAGE);
                    getWorkspaceComponent().addOutMessage(address);
                }
            });
        }
    }
}

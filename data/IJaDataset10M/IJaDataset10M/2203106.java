package net.sf.karatasi.views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/** Base class of cell renderers for ordered lists.
 *
 * @author <a href="mailto:kussinger@sourceforge.net">Mathias Kussinger</a>
 */
public class OrderedListCellRenderer extends JPanel implements ListCellRenderer {

    /** serial id */
    private static final long serialVersionUID = 8848782008665395268L;

    /** The flag to show re-controls. Only partial implemented! */
    private final boolean showReorderControlFlag = true;

    /** The list cell renderer for the content specific embedded cell. */
    private ListCellRenderer embeddedListCellRenderer = null;

    /** Button to move the database one entry up on the list. */
    private UpDownButton upButton = null;

    /** Button to move the database one entry down on the list. */
    private UpDownButton downButton = null;

    /** The panel with the buttons. */
    private JPanel buttonPane = null;

    /** Constructor
    *
    */
    public OrderedListCellRenderer(final ListCellRenderer embeddedRenderer) {
        super();
        assert embeddedRenderer != null;
        embeddedListCellRenderer = embeddedRenderer;
        initializeComponentsAndContent();
    }

    public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
        final Component cellContent = embeddedListCellRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        final JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));
        final GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.VERTICAL;
        if (buttonPane != null && showReorderControlFlag) {
            panel.add(buttonPane, c);
            c.gridx++;
        }
        c.weightx = 1.0;
        c.fill = GridBagConstraints.BOTH;
        panel.add(cellContent, c);
        return panel;
    }

    /** Create the components and initialize the cell content.
    *
    */
    private void initializeComponentsAndContent() {
        if (showReorderControlFlag) {
            final int buttonMinimalHeight = super.getFont().getSize();
            buttonPane = new JPanel();
            buttonPane.setBackground(Color.MAGENTA);
            buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.PAGE_AXIS));
            upButton = new UpDownButton(UpDownButton.ButtonIcon.UP);
            upButton.setMinimumSize(new Dimension(OrderedJList.UP_DOWN_BUTTON_WIDTH, buttonMinimalHeight));
            upButton.setPreferredSize(new Dimension(OrderedJList.UP_DOWN_BUTTON_WIDTH, buttonMinimalHeight));
            upButton.setMaximumSize(new Dimension(OrderedJList.UP_DOWN_BUTTON_WIDTH, Short.MAX_VALUE));
            buttonPane.add(upButton);
            downButton = new UpDownButton(UpDownButton.ButtonIcon.DOWN);
            downButton.setMinimumSize(new Dimension(OrderedJList.UP_DOWN_BUTTON_WIDTH, buttonMinimalHeight));
            downButton.setPreferredSize(new Dimension(OrderedJList.UP_DOWN_BUTTON_WIDTH, buttonMinimalHeight));
            downButton.setMaximumSize(new Dimension(OrderedJList.UP_DOWN_BUTTON_WIDTH, Short.MAX_VALUE));
            buttonPane.add(downButton);
            this.add(buttonPane);
        } else {
            buttonPane = null;
        }
    }
}

package be.lassi.ui.group;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import be.lassi.context.ShowContext;
import be.lassi.ui.icons.Icons;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Frame showing list of channel groups; allows the selection
 * of channel groups.
 */
public class GroupFrame extends AbstractGroupFrame {

    private JFrame moreFrame;

    private JButton buttonAll = new JButton("All");

    private JButton buttonNone = new JButton("None");

    /**
     * Constructs a new instance.
     * 
     * @param context the show context
     */
    public GroupFrame(final ShowContext context) {
        super(context, "Groups");
        init();
        updateWidgets();
        build();
    }

    /**
     * Sets the frame to maintain the group definition.
     * 
     * @param moreFrame the group definition frame
     */
    public void setMoreFrame(final JFrame moreFrame) {
        this.moreFrame = moreFrame;
    }

    /**
     * {@inheritDoc}
     */
    protected JComponent createPanel() {
        FormLayout layout = new FormLayout("min, 4dlu, min, 4dlu, min", "pref, 4dlu, pref, 4dlu, pref");
        layout.setColumnGroups(new int[][] { { 1, 3, 5 } });
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        builder.addTitle("Groups", cc.xyw(1, 1, 5));
        builder.add(createTableGroups(), cc.xyw(1, 3, 5));
        builder.add(buttonAll, cc.xy(1, 5));
        builder.add(buttonNone, cc.xy(3, 5));
        builder.add(createButtonMore(), cc.xy(5, 5));
        return builder.getPanel();
    }

    private void actionAll() {
        getContext().getShow().getGroups().setAllEnabled(true);
        getGroupTableModel().fireTableDataChanged();
        updateWidgets();
    }

    private void actionNone() {
        getContext().getShow().getGroups().setAllEnabled(false);
        getGroupTableModel().fireTableDataChanged();
        updateWidgets();
    }

    private void actionMore() {
        Rectangle thisBounds = getBounds();
        Rectangle bounds = moreFrame.getBounds();
        bounds.x = thisBounds.x;
        bounds.y = thisBounds.y;
        moreFrame.setBounds(bounds);
        moreFrame.setVisible(true);
        this.setVisible(false);
    }

    private void build() {
        buttonAll.addActionListener(new AbstractAction() {

            public void actionPerformed(final ActionEvent evt) {
                actionAll();
            }
        });
        buttonNone.addActionListener(new AbstractAction() {

            public void actionPerformed(final ActionEvent evt) {
                actionNone();
            }
        });
        getGroupTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(final ListSelectionEvent e) {
                updateWidgets();
            }
        });
    }

    private JComponent createButtonMore() {
        JButton button = new JButton("More", Icons.get("right.gif"));
        button.setHorizontalTextPosition(SwingConstants.LEFT);
        button.addActionListener(new AbstractAction() {

            public void actionPerformed(final ActionEvent evt) {
                actionMore();
            }
        });
        return button;
    }

    private void updateWidgets() {
        int groupCount = getContext().getShow().getGroups().size();
        int enabledGroupCount = getContext().getShow().getGroups().getEnabledGroupCount();
        buttonAll.setEnabled(groupCount > 0 && enabledGroupCount < groupCount);
        buttonNone.setEnabled(enabledGroupCount > 0);
    }

    /**
     * {@inheritDoc}
     */
    public void setVisible(final boolean visible) {
        updateWidgets();
        super.setVisible(visible);
    }
}

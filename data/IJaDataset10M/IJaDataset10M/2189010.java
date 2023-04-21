package be.lassi.ui.sheet;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import be.lassi.context.ShowContext;
import be.lassi.ui.base.BasicPanel;
import be.lassi.ui.sheet.actions.SheetActions;
import be.lassi.ui.sheet.scroll.ResizeHelper;
import be.lassi.ui.sheet.scroll.ScrollHelper;

/**
 * The main panel of the sheet user interface.
 */
public class SheetPanel extends BasicPanel {

    private final ShowContext context;

    /**
     * Constructs a new instance.
     * 
     * @param context the show context
     */
    public SheetPanel(final ShowContext context, SheetActions actions) {
        this.context = context;
        SheetTable tableHeaders = new SheetTableHeaders(context);
        SheetTable tableDetails = new SheetTableDetails(context);
        JScrollPane scrollPane = new JScrollPane(tableDetails);
        tableHeaders.addComponentListener(new ResizeHelper(scrollPane));
        scrollPane.setColumnHeaderView(tableDetails.getTableHeader());
        scrollPane.setViewportView(tableDetails);
        scrollPane.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, tableHeaders.getTableHeader());
        scrollPane.setRowHeaderView(tableHeaders);
        add(scrollPane, BorderLayout.CENTER);
        new ScrollHelper(scrollPane);
        scrollPane.addMouseListener(createMouseListener());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 500);
    }

    private MouseListener createMouseListener() {
        return new MouseAdapter() {

            public void mouseReleased(final MouseEvent event) {
                if (event.isPopupTrigger()) {
                    createPopup(event, -1);
                }
            }
        };
    }

    protected void createPopup(final MouseEvent event, final int cueIndex) {
        JPopupMenu popupMenu = new SheetActions(context, this, -1).getPopupMenu();
        popupMenu.show(this, event.getX(), event.getY());
    }
}

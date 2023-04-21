package net.sf.karatasi.views;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import net.sf.karatasi.viewmodels.ReorderingListener;

/** This is the base class for all lists with up/down buttons on the cells.
 * It supports moving entries up and down the list for sorting.
 * @author <a href="mailto:kussinger@sourceforge.net">Mathias Kussinger</a>
 *
 */
public class OrderedJList extends JList {

    /** serial id */
    private static final long serialVersionUID = -8261872932966788570L;

    /** Width of the up/down buttons on the left of the table cell */
    static final int UP_DOWN_BUTTON_WIDTH = 13;

    /** The handler that is responsible for actually moving the data. */
    private ReorderingListener reorderingListener = null;

    /** The re-order controls are enabled. */
    private boolean reorderingEnabled = false;

    /** State of the up/down buttons. */
    private boolean upDownButtonsVisible = false;

    /** the up Button */
    private final UpDownButton upButton = new UpDownButton(UpDownButton.ButtonIcon.UP);

    /** the down Button */
    private final UpDownButton downButton = new UpDownButton(UpDownButton.ButtonIcon.DOWN);

    /** Default constructor */
    public OrderedJList() {
        super();
    }

    /** Constructor
     * @param dataModel
     */
    public OrderedJList(final DefaultListModel dataModel) {
        super(dataModel);
    }

    /** Register the reordering listener for the list.
     * Only one listener can be registered.
     * @param newListener the new listener, or null to remove the listener.
     */
    public void setReorderingListener(final ReorderingListener newListener) {
        reorderingListener = newListener;
        if (newListener == null) {
            if (reorderingEnabled) {
                reorderingEnabled = false;
                if (upDownButtonsVisible) {
                    hideUpDownButtons();
                }
                this.invalidate();
            }
        } else {
            if (!reorderingEnabled) {
                reorderingEnabled = true;
                this.invalidate();
            }
        }
    }

    /** Overloaded mouse event to handle up/down button click.
     * @param event the mouse event.
     */
    @Override
    protected void processMouseEvent(final MouseEvent event) {
        if (reorderingEnabled) {
            if (event.getButton() == MouseEvent.BUTTON1) {
                final int index = locationToIndex(event.getPoint());
                if (index >= 0) {
                    final Rectangle cellBounds = getCellBounds(index, index);
                    if (cellBounds.x + UP_DOWN_BUTTON_WIDTH > event.getPoint().x) {
                        if (!upDownButtonsVisible && event.getID() == MouseEvent.MOUSE_PRESSED) {
                            showUpDownButtons(cellBounds, event.getPoint().y < cellBounds.y + cellBounds.height / 2);
                        } else if (upDownButtonsVisible && event.getID() == MouseEvent.MOUSE_RELEASED) {
                            hideUpDownButtons();
                            moveCell(index, event.getPoint().y < cellBounds.y + cellBounds.height / 2);
                        }
                        return;
                    }
                }
                if (upDownButtonsVisible && event.getID() == MouseEvent.MOUSE_RELEASED) {
                    hideUpDownButtons();
                }
            }
        }
        super.processMouseEvent(event);
    }

    /** Show the up/down buttons.
    * This function sets the upDownButtonsVisible flag;
    * @param cellBounds the bounding box of the table cell
    * @param upActive if true highlight the up button, else the down button
    */
    private void showUpDownButtons(final Rectangle cellBounds, final boolean upActive) {
        upButton.setBounds(new Rectangle(cellBounds.x, cellBounds.y, UP_DOWN_BUTTON_WIDTH, cellBounds.height / 2));
        downButton.setBounds(new Rectangle(cellBounds.x, cellBounds.y + cellBounds.height / 2, UP_DOWN_BUTTON_WIDTH, cellBounds.height / 2));
        upButton.setSelected(upActive);
        downButton.setSelected(!upActive);
        this.add(upButton);
        this.add(downButton);
        this.repaint(upButton.getBounds());
        this.repaint(downButton.getBounds());
        upDownButtonsVisible = true;
    }

    /** Hide the up/down buttons.
    * This function clears the upDownButtonsVisible flag;
    */
    private void hideUpDownButtons() {
        upButton.setSelected(false);
        downButton.setSelected(false);
        this.remove(upButton);
        this.remove(downButton);
        this.repaint(upButton.getBounds());
        this.repaint(downButton.getBounds());
        upDownButtonsVisible = false;
    }

    /** Move cell up/down.
    * @param index the cell index
    * @param moveUp if true move the cell up, else down
    */
    private void moveCell(final int index, final boolean moveUp) {
        clearSelection();
        int offset = 1;
        if (moveUp) {
            offset = -1;
        }
        if (reorderingListener != null) {
            reorderingListener.moveRow(index, offset);
        }
    }
}

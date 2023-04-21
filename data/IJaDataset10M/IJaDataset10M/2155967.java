package org.nakedobjects.viewer.skylark.value;

import org.nakedobjects.object.InvalidEntryException;
import org.nakedobjects.object.NakedObjectRuntimeException;
import org.nakedobjects.object.NakedValue;
import org.nakedobjects.object.TextEntryParseException;
import org.nakedobjects.object.control.AbstractConsent;
import org.nakedobjects.object.control.Consent;
import org.nakedobjects.object.persistence.ConcurrencyException;
import org.nakedobjects.viewer.skylark.Canvas;
import org.nakedobjects.viewer.skylark.Click;
import org.nakedobjects.viewer.skylark.Color;
import org.nakedobjects.viewer.skylark.Content;
import org.nakedobjects.viewer.skylark.Drag;
import org.nakedobjects.viewer.skylark.DragStart;
import org.nakedobjects.viewer.skylark.InternalDrag;
import org.nakedobjects.viewer.skylark.Location;
import org.nakedobjects.viewer.skylark.MenuOption;
import org.nakedobjects.viewer.skylark.MenuOptionSet;
import org.nakedobjects.viewer.skylark.SimpleInternalDrag;
import org.nakedobjects.viewer.skylark.Size;
import org.nakedobjects.viewer.skylark.Style;
import org.nakedobjects.viewer.skylark.Text;
import org.nakedobjects.viewer.skylark.View;
import org.nakedobjects.viewer.skylark.ViewAxis;
import org.nakedobjects.viewer.skylark.ViewSpecification;
import org.nakedobjects.viewer.skylark.Workspace;
import org.nakedobjects.viewer.skylark.basic.LabelAxis;
import org.nakedobjects.viewer.skylark.basic.PanelBorder;
import org.nakedobjects.viewer.skylark.core.TextView;
import org.nakedobjects.viewer.skylark.text.CursorPosition;
import org.nakedobjects.viewer.skylark.text.TextBlockTarget;
import org.nakedobjects.viewer.skylark.text.TextContent;
import org.nakedobjects.viewer.skylark.text.TextSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import org.apache.log4j.Logger;

public abstract class TextField extends AbstractField implements TextBlockTarget {

    private static final Logger LOG = Logger.getLogger(TextField.class);

    protected static final Text style = Style.NORMAL;

    protected CursorPosition cursor;

    private boolean identified;

    private String invalidReason = null;

    private boolean isSaved = true;

    private int maxTextWidth;

    protected TextSelection selection;

    protected TextContent textContent;

    private boolean showLines;

    private int maximumLength = 0;

    private int minumumLength = 0;

    public TextField(Content content, ViewSpecification specification, ViewAxis axis, boolean showLines, int width, int wrap) {
        super(content, specification, axis);
        this.showLines = showLines;
        setMaxTextWidth(width);
        NakedValue value = getValue();
        if (value != null) {
            maximumLength = value.getMaximumLength();
            minumumLength = value.getMinumumLength();
        }
        textContent = new TextContent(this, 1, wrap);
        cursor = new CursorPosition(textContent, 0, 0);
        selection = new TextSelection(cursor, cursor);
        textContent.setText(value == null ? "" : value.titleString());
        cursor.home();
        isSaved = true;
    }

    private NakedValue getValue() {
        return (NakedValue) getContent().getNaked();
    }

    public void setSize(Size size) {
        super.setSize(size);
        setMaxWidth(size.getWidth() - 2 * HPADDING);
        textContent.setNoDisplayLines(size.getHeight() / style.getTextHeight());
    }

    public boolean canFocus() {
        return canChangeValue();
    }

    private void copy() {
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        String text = textContent.getText(selection);
        StringSelection ss = new StringSelection(text);
        cb.setContents(ss, ss);
        LOG.debug("copied " + text);
    }

    public String debugDetails() {
        return super.debugDetails() + "\n" + textContent;
    }

    /**
     * Delete the character to the left of the cursor.
     */
    public void delete() {
        if (selection.hasSelection()) {
            textContent.delete(selection);
            selection.resetTo(selection.from());
            markDamaged();
        } else {
            textContent.deleteLeft(cursor);
            cursor.left();
            selection.resetTo(cursor);
        }
        isSaved = false;
    }

    /**
     * Delete the character to the right of the cursor.
     */
    public void deleteForward() {
        if (selection.hasSelection()) {
            textContent.delete(selection);
            selection.resetTo(selection.from());
            markDamaged();
        } else {
            textContent.deleteRight(cursor);
            markDamaged();
        }
        isSaved = false;
    }

    public void drag(InternalDrag drag) {
        if (canChangeValue()) {
            selection.extendTo(drag.getLocation());
            markDamaged();
        }
    }

    public Drag dragStart(DragStart drag) {
        Location at = drag.getLocation();
        Size size = getView().getSize();
        Location anchor = getView().getAbsoluteLocation();
        ViewAxis axis = getViewAxis();
        if (axis instanceof LabelAxis) {
            int width = ((LabelAxis) axis).getWidth();
            size.contractWidth(width);
            anchor.add(width, 0);
        }
        if (canChangeValue()) {
            cursor.cursorAt(at);
            resetSelection();
            return new SimpleInternalDrag(this, anchor);
        }
        markDamaged();
        return null;
    }

    public void dragTo(InternalDrag drag) {
        Location at = drag.getLocation();
        if (canChangeValue()) {
            selection.extendTo(at);
            markDamaged();
        }
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        int width = getMaxWidth();
        align();
        if (hasFocus() && selection.hasSelection()) {
            drawHighlight(canvas, width);
        }
        if (showLines == true && canChangeValue()) {
            Color color = identified ? Style.IDENTIFIED : Style.SECONDARY2;
            color = hasFocus() ? Style.PRIMARY1 : color;
            drawLines(canvas, color, width);
        }
        Color textColor;
        if (getState().isInvalid()) {
            textColor = Style.BLACK;
        } else if (hasFocus()) {
            if (isSaved) {
                textColor = Style.PRIMARY1;
            } else {
                textColor = Style.TEXT_EDIT;
            }
        } else {
            textColor = Style.BLACK;
        }
        drawText(canvas, textColor, width);
    }

    protected abstract void align();

    protected abstract void drawHighlight(Canvas canvas, int maxWidth);

    protected abstract void drawLines(Canvas canvas, Color color, int width);

    protected abstract void drawText(Canvas canvas, Color textColor, int width);

    public void editComplete() {
        if (canChangeValue() && !isSaved) {
            isSaved = true;
            initiateSave();
        }
    }

    protected void save() {
        String entry = textContent.getText();
        NakedValue value = getValue();
        if (!entry.equals(value == null ? "" : value.titleString())) {
            LOG.debug("field edited: \'" + entry + "\' to replace \'" + (value == null ? "" : value.titleString()) + "\'");
            if (entry.length() < minumumLength) {
                invalidReason = "Entry not long enough, must be at least " + minumumLength + " characters";
                LOG.error(invalidReason);
                getViewManager().setStatus(invalidReason);
                getState().setInvalid();
                markDamaged();
            } else {
                try {
                    parseEntry(entry.toString());
                    invalidReason = null;
                    getViewManager().getSpy().addAction("VALID ENTRY: " + entry);
                    getState().setValid();
                    markDamaged();
                    getParent().invalidateContent();
                } catch (TextEntryParseException e) {
                    invalidReason = "INVALID ENTRY: " + e.getMessage();
                    getViewManager().setStatus(invalidReason);
                    getState().setInvalid();
                    markDamaged();
                } catch (InvalidEntryException e) {
                    invalidReason = "INVALID ENTRY: " + e.getMessage();
                    getViewManager().setStatus(invalidReason);
                    getState().setInvalid();
                    markDamaged();
                } catch (ConcurrencyException e) {
                    invalidReason = "UPDATE FAILURE: " + e.getMessage();
                    LOG.warn(invalidReason, e);
                    getState().setOutOfSynch();
                    markDamaged();
                    throw e;
                } catch (NakedObjectRuntimeException e) {
                    invalidReason = "UPDATE FAILURE: " + e.getMessage();
                    LOG.warn(invalidReason, e);
                    getViewManager().setStatus(invalidReason);
                    getState().setOutOfSynch();
                    markDamaged();
                }
            }
        } else {
            getState().setValid();
        }
    }

    public void entered() {
        if (canChangeValue()) {
            getViewManager().showTextCursor();
            identified = true;
            markDamaged();
        }
    }

    public void exited() {
        if (canChangeValue()) {
            getViewManager().showArrowCursor();
            identified = false;
            markDamaged();
        }
    }

    /**
     * Responds to first click by placing the cursor between the two characters nearest the point of
     * the mouse.
     */
    public void firstClick(Click click) {
        if (canChangeValue()) {
            Location at = click.getLocation();
            at.subtract(HPADDING, VPADDING);
            cursor.cursorAt(at);
            resetSelection();
            markDamaged();
        }
        if (!canChangeValue() || click.isShift()) {
            View textView = new PanelBorder(1, Style.PRIMARY1, Style.PRIMARY3, new TextView(getContent().getNaked().titleString()));
            textView.setSize(textView.getRequiredSize());
            getViewManager().setOverlayView(textView);
            textView.setLocation(getAbsoluteLocation());
            textView.markDamaged();
        }
    }

    public void focusLost() {
        super.focusLost();
        editComplete();
    }

    public void focusReceived() {
        getViewManager().setStatus(invalidReason == null ? "" : invalidReason);
        resetSelection();
    }

    public int getBaseline() {
        return style.getAscent() + VPADDING;
    }

    public int getMaxWidth() {
        return maxTextWidth;
    }

    public Size getRequiredSize() {
        int width = HPADDING + maxTextWidth + HPADDING;
        int height = textContent.getNoDisplayLines() * getText().getLineHeight() + VPADDING * 2;
        height = Math.max(height, Style.defaultFieldHeight());
        return new Size(width, height);
    }

    public Text getText() {
        return style;
    }

    /**
     * modifies the selection object so that text is selected if the flag is true, or text is
     * unselected if false.
     */
    private void highlight(boolean select) {
        if (canChangeValue()) {
            if (!select) {
                selection.resetTo(cursor);
            } else {
                selection.extendTo(cursor);
            }
        }
    }

    private void insert(char character) {
        if (withinMaximum(1)) {
            insert("" + character);
            selection.resetTo(cursor);
        } else {
            getViewManager().setStatus("Entry can be no longer than " + maximumLength + " characters");
        }
    }

    private boolean withinMaximum(int characters) {
        return maximumLength == 0 || textContent.getText().length() + characters <= maximumLength;
    }

    private void insert(String characters) {
        if (withinMaximum(characters.length())) {
            int noLines = textContent.getNoDisplayLines();
            textContent.insert(cursor, characters);
            cursor.right(characters.length());
            if (textContent.getNoDisplayLines() != noLines) {
                invalidateLayout();
            }
            isSaved = false;
        } else {
            getViewManager().setStatus("Entry can be no longer than " + maximumLength + " characters");
        }
    }

    public boolean isIdentified() {
        return identified;
    }

    /**
     * Called when the user presses any key on the keyboard while this view has the focus.
     */
    public void keyPressed(final int keyCode, final int modifiers) {
        if (!canChangeValue()) {
            return;
        }
        if (keyCode == KeyEvent.VK_CONTROL || keyCode == KeyEvent.VK_SHIFT || keyCode == KeyEvent.VK_ALT) {
            return;
        }
        final boolean alt = (modifiers & InputEvent.ALT_MASK) > 0;
        final boolean shift = (modifiers & InputEvent.SHIFT_MASK) > 0;
        final boolean ctrl = (modifiers & InputEvent.CTRL_MASK) > 0;
        switch(keyCode) {
            case KeyEvent.VK_PAGE_UP:
                pageUp(ctrl);
                break;
            case KeyEvent.VK_PAGE_DOWN:
                pageDown(ctrl);
                break;
            case KeyEvent.VK_V:
                if (ctrl) {
                    paste();
                    highlight(false);
                }
                break;
            case KeyEvent.VK_C:
                if (ctrl) {
                    copy();
                }
                break;
            case KeyEvent.VK_DOWN:
                down(shift);
                break;
            case KeyEvent.VK_UP:
                up(shift);
                break;
            case KeyEvent.VK_HOME:
                home(alt, shift);
                break;
            case KeyEvent.VK_END:
                end(alt, shift);
                break;
            case KeyEvent.VK_LEFT:
                left(alt, shift);
                break;
            case KeyEvent.VK_RIGHT:
                right(alt, shift);
                break;
            case KeyEvent.VK_DELETE:
                deleteForward();
                break;
            case KeyEvent.VK_BACK_SPACE:
                delete();
                break;
            case KeyEvent.VK_TAB:
                tab();
                break;
            case KeyEvent.VK_ENTER:
                enter();
                getParent().keyPressed(keyCode, modifiers);
                break;
            case KeyEvent.VK_ESCAPE:
                escape();
                break;
            default:
                break;
        }
        markDamaged();
        LOG.debug("character at " + cursor.getCharacter() + " line " + cursor.getLine());
        LOG.debug(selection);
    }

    protected void pageDown(final boolean ctrl) {
        if (ctrl) {
            if (textContent.decreaseDepth()) {
                textContent.alignDisplay(cursor.getLine());
                invalidateLayout();
            }
        } else {
            cursor.pageDown();
        }
    }

    protected void pageUp(final boolean ctrl) {
        if (ctrl) {
            textContent.increaseDepth();
            textContent.alignDisplay(cursor.getLine());
            invalidateLayout();
        } else {
            cursor.pageUp();
        }
    }

    protected void down(final boolean shift) {
        cursor.lineDown();
        highlight(shift);
    }

    protected void up(final boolean shift) {
        cursor.lineUp();
        highlight(shift);
    }

    protected void home(final boolean alt, final boolean shift) {
        if (alt) {
            cursor.top();
        } else {
            cursor.home();
        }
        highlight(shift);
    }

    protected void end(final boolean alt, final boolean shift) {
        if (alt) {
            cursor.bottom();
        } else {
            cursor.end();
        }
        highlight(shift);
    }

    protected void left(final boolean alt, final boolean shift) {
        if (alt) {
            cursor.wordLeft();
        } else {
            cursor.left();
        }
        highlight(shift);
    }

    protected void right(final boolean alt, final boolean shift) {
        if (alt) {
            cursor.wordRight();
        } else {
            cursor.right();
        }
        highlight(shift);
    }

    protected void escape() {
        invalidReason = null;
        refresh();
    }

    protected void tab() {
        editComplete();
    }

    protected void enter() {
        editComplete();
    }

    /**
     * Called when the user releases any key on the keyboard while this view has the focus.
     */
    public void keyReleased(int keyCode, int modifiers) {
    }

    /**
     * Called when the user presses a non-control key (i.e. data entry keys and not shift, up-arrow
     * etc). Such a key press will result in a prior call to <code>keyPressed</code> and a
     * subsequent call to <code>keyReleased</code>.
     */
    public void keyTyped(char keyCode) {
        if (canChangeValue()) {
            insert(keyCode);
        }
    }

    public void contentMenuOptions(MenuOptionSet options) {
        options.add(MenuOptionSet.OBJECT, new MenuOption("Refresh") {

            public void execute(Workspace workspace, View view, Location at) {
                invalidReason = null;
                refresh();
            }

            public Consent disabled(View component) {
                return AbstractConsent.allow(invalidReason != null);
            }
        });
        super.contentMenuOptions(options);
    }

    public void paste() {
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable content = cb.getContents(this);
        try {
            String text = (String) content.getTransferData(DataFlavor.stringFlavor);
            insert(text);
            LOG.debug("pasted " + text);
        } catch (Throwable e) {
            LOG.error("invalid paste operation " + e);
        }
    }

    public void refresh() {
        super.refresh();
        NakedValue object = getValue();
        if (object == null) {
            textContent.setText("");
        } else {
            String value = object.titleString();
            textContent.setText(value);
            maximumLength = object.getMaximumLength();
            minumumLength = object.getMinumumLength();
        }
    }

    private void resetSelection() {
        selection.resetTo(cursor);
    }

    public void secondClick(Click click) {
        if (canChangeValue()) {
            selection.selectWord();
        }
    }

    /**
     * Set the maximum width of the field, as a number of characters
     */
    public void setMaxTextWidth(int noCharacters) {
        maxTextWidth = getText().charWidth('o') * noCharacters;
    }

    /**
     * Set the maximum width of the field, as a number of pixels
     */
    public void setMaxWidth(int width) {
        maxTextWidth = width;
    }

    public void thirdClick(Click click) {
        if (canChangeValue()) {
            selection.selectSentence();
            markDamaged();
        }
    }
}

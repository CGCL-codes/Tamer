package de.objectcode.time4u.client.controls;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.accessibility.AccessibleTextAdapter;
import org.eclipse.swt.accessibility.AccessibleTextEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TypedListener;
import org.vafada.swtcalendar.SWTCalendar;

public class DateCombo extends Composite {

    Text text;

    Button arrow;

    Shell popup;

    SWTCalendar m_dateChooser;

    boolean hasFocus;

    Listener listener, filter;

    Color foreground, background;

    Font font;

    public DateCombo(Composite parent, int style) {
        super(parent, style = checkStyle(style));
        int textStyle = SWT.SINGLE;
        if ((style & SWT.READ_ONLY) != 0) textStyle |= SWT.READ_ONLY;
        if ((style & SWT.FLAT) != 0) textStyle |= SWT.FLAT;
        text = new Text(this, textStyle);
        int arrowStyle = SWT.ARROW | SWT.DOWN;
        if ((style & SWT.FLAT) != 0) arrowStyle |= SWT.FLAT;
        arrow = new Button(this, arrowStyle);
        arrow.setVisible((style & SWT.READ_ONLY) == 0);
        listener = new Listener() {

            public void handleEvent(Event event) {
                if (popup == event.widget) {
                    popupEvent(event);
                    return;
                }
                if (text == event.widget) {
                    textEvent(event);
                    return;
                }
                if (m_dateChooser == event.widget) {
                    timeChooserEvent(event);
                    return;
                }
                if (arrow == event.widget) {
                    arrowEvent(event);
                    return;
                }
                if (DateCombo.this == event.widget) {
                    comboEvent(event);
                    return;
                }
                if (getShell() == event.widget) {
                    handleFocus(SWT.FocusOut);
                }
            }
        };
        filter = new Listener() {

            public void handleEvent(Event event) {
                Shell shell = ((Control) event.widget).getShell();
                if (shell == DateCombo.this.getShell()) {
                    handleFocus(SWT.FocusOut);
                }
            }
        };
        int[] comboEvents = { SWT.Dispose, SWT.Move, SWT.Resize };
        for (int i = 0; i < comboEvents.length; i++) this.addListener(comboEvents[i], listener);
        int[] textEvents = { SWT.KeyDown, SWT.KeyUp, SWT.MenuDetect, SWT.Modify, SWT.MouseDown, SWT.MouseUp, SWT.Traverse, SWT.FocusIn, SWT.FocusOut };
        for (int i = 0; i < textEvents.length; i++) text.addListener(textEvents[i], listener);
        int[] arrowEvents = { SWT.Selection, SWT.FocusIn };
        for (int i = 0; i < arrowEvents.length; i++) arrow.addListener(arrowEvents[i], listener);
        createPopup(null);
        initAccessible();
    }

    public void addSelectionListener(SelectionListener listener) {
        checkWidget();
        if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
        TypedListener typedListener = new TypedListener(listener);
        addListener(SWT.Selection, typedListener);
        addListener(SWT.DefaultSelection, typedListener);
    }

    public void removeSelectionListener(SelectionListener listener) {
        checkWidget();
        if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
        removeListener(SWT.Selection, listener);
        removeListener(SWT.DefaultSelection, listener);
    }

    char _findMnemonic(String string) {
        if (string == null) return '\0';
        int index = 0;
        int length = string.length();
        do {
            while (index < length && string.charAt(index) != '&') index++;
            if (++index >= length) return '\0';
            if (string.charAt(index) != '&') return Character.toLowerCase(string.charAt(index));
            index++;
        } while (index < length);
        return '\0';
    }

    void arrowEvent(Event event) {
        switch(event.type) {
            case SWT.FocusIn:
                {
                    handleFocus(SWT.FocusIn);
                    break;
                }
            case SWT.Selection:
                {
                    dropDown(!isDropped());
                    break;
                }
        }
    }

    void comboEvent(Event event) {
        switch(event.type) {
            case SWT.Dispose:
                if (popup != null && !popup.isDisposed()) {
                    m_dateChooser.removeListener(SWT.Dispose, listener);
                    popup.dispose();
                }
                Shell shell = getShell();
                shell.removeListener(SWT.Deactivate, listener);
                Display display = getDisplay();
                display.removeFilter(SWT.FocusIn, filter);
                popup = null;
                text = null;
                m_dateChooser = null;
                arrow = null;
                break;
            case SWT.Move:
                dropDown(false);
                break;
            case SWT.Resize:
                internalLayout(false);
                break;
        }
    }

    public Point computeSize(int wHint, int hHint, boolean changed) {
        checkWidget();
        int width = 0, height = 0;
        int textWidth = 0;
        GC gc = new GC(text);
        int spacer = gc.stringExtent(" ").x;
        textWidth = gc.stringExtent(DateFormat.getDateInstance().format(new Date())).x;
        gc.dispose();
        Point textSize = text.computeSize(SWT.DEFAULT, SWT.DEFAULT, changed);
        Point arrowSize = arrow.computeSize(SWT.DEFAULT, SWT.DEFAULT, changed);
        int borderWidth = getBorderWidth();
        height = Math.max(hHint, Math.max(textSize.y, arrowSize.y) + 2 * borderWidth);
        width = Math.max(wHint, textWidth + 2 * spacer + arrowSize.x + 2 * borderWidth);
        return new Point(width, height);
    }

    void createPopup(Calendar selection) {
        popup = new Shell(getShell(), SWT.NO_TRIM | SWT.ON_TOP);
        int style = getStyle();
        int listStyle = SWT.SINGLE;
        if ((style & SWT.FLAT) != 0) listStyle |= SWT.FLAT;
        if ((style & SWT.RIGHT_TO_LEFT) != 0) listStyle |= SWT.RIGHT_TO_LEFT;
        if ((style & SWT.LEFT_TO_RIGHT) != 0) listStyle |= SWT.LEFT_TO_RIGHT;
        m_dateChooser = new SWTCalendar(popup, listStyle);
        if (font != null) m_dateChooser.setFont(font);
        if (foreground != null) m_dateChooser.setForeground(foreground);
        if (background != null) m_dateChooser.setBackground(background);
        int[] popupEvents = { SWT.Close, SWT.Paint, SWT.Deactivate };
        for (int i = 0; i < popupEvents.length; i++) popup.addListener(popupEvents[i], listener);
        int[] listEvents = { SWT.MouseUp, SWT.Selection, SWT.Traverse, SWT.KeyDown, SWT.KeyUp, SWT.FocusIn, SWT.Dispose };
        for (int i = 0; i < listEvents.length; i++) m_dateChooser.addListener(listEvents[i], listener);
        if (selection != null) m_dateChooser.setCalendar(selection);
    }

    void dropDown(boolean drop) {
        if (drop && (getStyle() & SWT.READ_ONLY) != 0) return;
        if (drop == isDropped()) return;
        if (!drop) {
            popup.setVisible(false);
            if (!isDisposed() && arrow.isFocusControl()) {
                text.setFocus();
            }
            return;
        }
        if (getShell() != popup.getParent()) {
            Calendar selection = m_dateChooser.getCalendar();
            m_dateChooser.removeListener(SWT.Dispose, listener);
            popup.dispose();
            popup = null;
            m_dateChooser = null;
            createPopup(selection);
        }
        Point size = getSize();
        Point listSize = m_dateChooser.computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
        m_dateChooser.setBounds(1, 1, Math.max(size.x - 2, listSize.x), listSize.y);
        Display display = getDisplay();
        Rectangle listRect = m_dateChooser.getBounds();
        Rectangle parentRect = display.map(getParent(), null, getBounds());
        Point comboSize = getSize();
        Rectangle displayRect = getMonitor().getClientArea();
        int width = Math.max(comboSize.x, listRect.width + 2);
        int height = listRect.height + 2;
        int x = parentRect.x;
        int y = parentRect.y + comboSize.y;
        if (y + height > displayRect.y + displayRect.height) y = parentRect.y - height;
        popup.setBounds(x, y, width, height);
        popup.setVisible(true);
        m_dateChooser.setFocus();
    }

    Label getAssociatedLabel() {
        Control[] siblings = getParent().getChildren();
        for (int i = 0; i < siblings.length; i++) {
            if (siblings[i] == this) {
                if (i > 0 && siblings[i - 1] instanceof Label) {
                    return (Label) siblings[i - 1];
                }
            }
        }
        return null;
    }

    public boolean getEditable() {
        checkWidget();
        return text.getEditable();
    }

    public Calendar getSelection() {
        checkWidget();
        if (text.getText().length() > 0) return m_dateChooser.getCalendar();
        return null;
    }

    /**
   * Returns a string containing a copy of the contents of the receiver's text
   * field.
   * 
   * @return the receiver's text
   * 
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the receiver</li>
   *              </ul>
   */
    public String getText() {
        checkWidget();
        return text.getText();
    }

    void handleFocus(int type) {
        if (isDisposed()) return;
        switch(type) {
            case SWT.FocusIn:
                {
                    if (hasFocus) return;
                    if (getEditable()) text.selectAll();
                    hasFocus = true;
                    Shell shell = getShell();
                    shell.removeListener(SWT.Deactivate, listener);
                    shell.addListener(SWT.Deactivate, listener);
                    Display display = getDisplay();
                    display.removeFilter(SWT.FocusIn, filter);
                    display.addFilter(SWT.FocusIn, filter);
                    Event e = new Event();
                    notifyListeners(SWT.FocusIn, e);
                    break;
                }
            case SWT.FocusOut:
                {
                    if (!hasFocus) return;
                    Control focusControl = getDisplay().getFocusControl();
                    if (focusControl == arrow || focusControl == m_dateChooser || focusControl == text) return;
                    hasFocus = false;
                    Shell shell = getShell();
                    shell.removeListener(SWT.Deactivate, listener);
                    Display display = getDisplay();
                    display.removeFilter(SWT.FocusIn, filter);
                    Event e = new Event();
                    notifyListeners(SWT.FocusOut, e);
                    break;
                }
        }
    }

    void initAccessible() {
        AccessibleAdapter accessibleAdapter = new AccessibleAdapter() {

            public void getHelp(AccessibleEvent e) {
                e.result = getToolTipText();
            }

            public void getKeyboardShortcut(AccessibleEvent e) {
                String shortcut = null;
                Label label = getAssociatedLabel();
                if (label != null) {
                    String text = label.getText();
                    if (text != null) {
                        char mnemonic = _findMnemonic(text);
                        if (mnemonic != '\0') {
                            shortcut = "Alt+" + mnemonic;
                        }
                    }
                }
                e.result = shortcut;
            }

            public void getName(AccessibleEvent e) {
                String name = null;
                Label label = getAssociatedLabel();
                if (label != null) {
                    name = stripMnemonic(label.getText());
                }
                e.result = name;
            }
        };
        getAccessible().addAccessibleListener(accessibleAdapter);
        text.getAccessible().addAccessibleListener(accessibleAdapter);
        m_dateChooser.getAccessible().addAccessibleListener(accessibleAdapter);
        arrow.getAccessible().addAccessibleListener(new AccessibleAdapter() {

            public void getHelp(AccessibleEvent e) {
                e.result = getToolTipText();
            }

            public void getKeyboardShortcut(AccessibleEvent e) {
                e.result = "Alt+Down Arrow";
            }

            public void getName(AccessibleEvent e) {
                e.result = isDropped() ? SWT.getMessage("SWT_Close") : SWT.getMessage("SWT_Open");
            }
        });
        getAccessible().addAccessibleTextListener(new AccessibleTextAdapter() {

            public void getCaretOffset(AccessibleTextEvent e) {
                e.offset = text.getCaretPosition();
            }
        });
        getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {

            public void getChildAtPoint(AccessibleControlEvent e) {
                Point testPoint = toControl(e.x, e.y);
                if (getBounds().contains(testPoint)) {
                    e.childID = ACC.CHILDID_SELF;
                }
            }

            public void getChildCount(AccessibleControlEvent e) {
                e.detail = 0;
            }

            public void getLocation(AccessibleControlEvent e) {
                Rectangle location = getBounds();
                Point pt = toDisplay(location.x, location.y);
                e.x = pt.x;
                e.y = pt.y;
                e.width = location.width;
                e.height = location.height;
            }

            public void getRole(AccessibleControlEvent e) {
                e.detail = ACC.ROLE_COMBOBOX;
            }

            public void getState(AccessibleControlEvent e) {
                e.detail = ACC.STATE_NORMAL;
            }

            public void getValue(AccessibleControlEvent e) {
                e.result = getText();
            }
        });
        text.getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {

            public void getRole(AccessibleControlEvent e) {
                e.detail = text.getEditable() ? ACC.ROLE_TEXT : ACC.ROLE_LABEL;
            }
        });
        arrow.getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {

            public void getDefaultAction(AccessibleControlEvent e) {
                e.result = isDropped() ? SWT.getMessage("SWT_Close") : SWT.getMessage("SWT_Open");
            }
        });
    }

    void internalLayout(boolean changed) {
        if (isDropped()) dropDown(false);
        Rectangle rect = getClientArea();
        int width = rect.width;
        int height = rect.height;
        Point arrowSize = arrow.computeSize(SWT.DEFAULT, height, changed);
        text.setBounds(0, 0, width - arrowSize.x, height);
        arrow.setBounds(width - arrowSize.x, 0, arrowSize.x, arrowSize.y);
    }

    boolean isDropped() {
        return popup.getVisible();
    }

    void popupEvent(Event event) {
        switch(event.type) {
            case SWT.Paint:
                Rectangle listRect = m_dateChooser.getBounds();
                Color black = getDisplay().getSystemColor(SWT.COLOR_BLACK);
                event.gc.setForeground(black);
                event.gc.drawRectangle(0, 0, listRect.width + 1, listRect.height + 1);
                break;
            case SWT.Close:
                event.doit = false;
                dropDown(false);
                break;
            case SWT.Deactivate:
                dropDown(false);
                break;
        }
    }

    public void redraw() {
        super.redraw();
        text.redraw();
        arrow.redraw();
        if (popup.isVisible()) m_dateChooser.redraw();
    }

    public void redraw(int x, int y, int width, int height, boolean all) {
        super.redraw(x, y, width, height, true);
    }

    public void select(Calendar selection) {
        checkWidget();
        if (selection == null) {
            m_dateChooser.setCalendar(null);
            text.setText("");
            return;
        } else {
            if (!selection.equals(getSelection())) {
                text.setText(DateFormat.getDateInstance().format(selection.getTime()));
                m_dateChooser.setCalendar(selection);
            }
        }
    }

    String stripMnemonic(String string) {
        int index = 0;
        int length = string.length();
        do {
            while ((index < length) && (string.charAt(index) != '&')) index++;
            if (++index >= length) return string;
            if (string.charAt(index) != '&') {
                return string.substring(0, index - 1) + string.substring(index, length);
            }
            index++;
        } while (index < length);
        return string;
    }

    void textEvent(Event event) {
        switch(event.type) {
            case SWT.FocusIn:
                {
                    handleFocus(SWT.FocusIn);
                    break;
                }
            case SWT.KeyDown:
                {
                    if (event.character == SWT.CR) {
                        dropDown(false);
                        Event e = new Event();
                        e.time = event.time;
                        e.stateMask = event.stateMask;
                        notifyListeners(SWT.DefaultSelection, e);
                    }
                    if (isDisposed()) break;
                    if (event.keyCode == SWT.ARROW_UP || event.keyCode == SWT.ARROW_DOWN) {
                        event.doit = false;
                        if ((event.stateMask & SWT.ALT) != 0) {
                            boolean dropped = isDropped();
                            text.selectAll();
                            if (!dropped) setFocus();
                            dropDown(!dropped);
                            break;
                        }
                        Calendar oldSelection = m_dateChooser.getCalendar();
                        if (event.keyCode == SWT.ARROW_UP) {
                            oldSelection.add(Calendar.DAY_OF_MONTH, -1);
                            select(oldSelection);
                        } else {
                            oldSelection.add(Calendar.DAY_OF_MONTH, -1);
                            select(oldSelection);
                        }
                        if (oldSelection != getSelection()) {
                            Event e = new Event();
                            e.time = event.time;
                            e.stateMask = event.stateMask;
                            notifyListeners(SWT.Selection, e);
                        }
                        if (isDisposed()) break;
                    }
                    Event e = new Event();
                    e.time = event.time;
                    e.character = event.character;
                    e.keyCode = event.keyCode;
                    e.stateMask = event.stateMask;
                    notifyListeners(SWT.KeyDown, e);
                    break;
                }
            case SWT.KeyUp:
                {
                    Event e = new Event();
                    e.time = event.time;
                    e.character = event.character;
                    e.keyCode = event.keyCode;
                    e.stateMask = event.stateMask;
                    notifyListeners(SWT.KeyUp, e);
                    break;
                }
            case SWT.MenuDetect:
                {
                    Event e = new Event();
                    e.time = event.time;
                    notifyListeners(SWT.MenuDetect, e);
                    break;
                }
            case SWT.FocusOut:
                {
                    try {
                        Date date = DateFormat.getInstance().parse(text.getText());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        select(cal);
                    } catch (Exception e) {
                    }
                    break;
                }
            case SWT.MouseDown:
                {
                    if (event.button != 1) return;
                    if (text.getEditable()) return;
                    boolean dropped = isDropped();
                    text.selectAll();
                    if (!dropped) setFocus();
                    dropDown(!dropped);
                    break;
                }
            case SWT.MouseUp:
                {
                    if (event.button != 1) return;
                    if (text.getEditable()) return;
                    text.selectAll();
                    break;
                }
            case SWT.Traverse:
                {
                    switch(event.detail) {
                        case SWT.TRAVERSE_RETURN:
                        case SWT.TRAVERSE_ARROW_PREVIOUS:
                        case SWT.TRAVERSE_ARROW_NEXT:
                            event.doit = false;
                            break;
                    }
                    Event e = new Event();
                    e.time = event.time;
                    e.detail = event.detail;
                    e.doit = event.doit;
                    e.character = event.character;
                    e.keyCode = event.keyCode;
                    notifyListeners(SWT.Traverse, e);
                    event.doit = e.doit;
                    event.detail = e.detail;
                    break;
                }
        }
    }

    void timeChooserEvent(Event event) {
        switch(event.type) {
            case SWT.Dispose:
                if (getShell() != popup.getParent()) {
                    Calendar selection = m_dateChooser.getCalendar();
                    popup = null;
                    m_dateChooser = null;
                    createPopup(selection);
                }
                break;
            case SWT.FocusIn:
                {
                    handleFocus(SWT.FocusIn);
                    break;
                }
            case SWT.MouseUp:
                {
                    if (event.button != 1) return;
                    dropDown(false);
                    break;
                }
            case SWT.Selection:
                {
                    Calendar selection = m_dateChooser.getCalendar();
                    if (selection == null) return;
                    text.setText(DateFormat.getDateInstance().format(selection.getTime()));
                    text.selectAll();
                    Event e = new Event();
                    e.time = event.time;
                    e.stateMask = event.stateMask;
                    e.doit = event.doit;
                    notifyListeners(SWT.Selection, e);
                    event.doit = e.doit;
                    dropDown(false);
                    break;
                }
            case SWT.Traverse:
                {
                    switch(event.detail) {
                        case SWT.TRAVERSE_RETURN:
                        case SWT.TRAVERSE_ESCAPE:
                        case SWT.TRAVERSE_ARROW_PREVIOUS:
                        case SWT.TRAVERSE_ARROW_NEXT:
                            event.doit = false;
                            break;
                    }
                    Event e = new Event();
                    e.time = event.time;
                    e.detail = event.detail;
                    e.doit = event.doit;
                    e.character = event.character;
                    e.keyCode = event.keyCode;
                    notifyListeners(SWT.Traverse, e);
                    event.doit = e.doit;
                    event.detail = e.detail;
                    break;
                }
            case SWT.KeyUp:
                {
                    Event e = new Event();
                    e.time = event.time;
                    e.character = event.character;
                    e.keyCode = event.keyCode;
                    e.stateMask = event.stateMask;
                    notifyListeners(SWT.KeyUp, e);
                    break;
                }
            case SWT.KeyDown:
                {
                    if (event.character == SWT.ESC) {
                        dropDown(false);
                    }
                    if ((event.stateMask & SWT.ALT) != 0 && (event.keyCode == SWT.ARROW_UP || event.keyCode == SWT.ARROW_DOWN)) {
                        dropDown(false);
                    }
                    if (event.character == SWT.CR) {
                        dropDown(false);
                        Event e = new Event();
                        e.time = event.time;
                        e.stateMask = event.stateMask;
                        notifyListeners(SWT.DefaultSelection, e);
                    }
                    if (isDisposed()) break;
                    Event e = new Event();
                    e.time = event.time;
                    e.character = event.character;
                    e.keyCode = event.keyCode;
                    e.stateMask = event.stateMask;
                    notifyListeners(SWT.KeyDown, e);
                    break;
                }
        }
    }

    static int checkStyle(int style) {
        int mask = SWT.BORDER | SWT.READ_ONLY | SWT.FLAT | SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
        return style & mask;
    }
}

package com.parfumball.eclipse.plugin.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import com.parfumball.Utils;

/**
 * A text based view to display the hex dump of a packet.
 *  
 * @author prasanna
 */
public class PacketDumpView extends ViewPart {

    /**
     * The text control for displaying hex dump offsets.
     */
    private Text offsets;

    /**
     * The text control that displays the actual hex dump
     * of a packet. 
     */
    private Text hexDump;

    /**
     * The text control that displays the packet data 
     * as text.
     */
    private Text prettyPrint;

    /**
     * The Composite that holds the three
     * text controls.
     */
    private Composite composite;

    /**
     * The ScrolledComposite for controlling the
     * position of the scroll bars.
     */
    private ScrolledComposite sc;

    /**
     * The parent component inside which our stuff is displayed.
     * We will use the parent's client area size to determine if 
     * we need to scroll the hex dump.
     */
    private Composite parent;

    /**
     * The background color for the text control.
     */
    private Color white;

    /**
     * The foreground color for the text control.
     */
    private Color blue;

    /**
     * Our courier size 10 font.
     */
    private Font courier;

    /**
     * The current number of lines in the display.
     */
    private int lines;

    /**
     * 
     */
    public PacketDumpView() {
        super();
    }

    /**
     * Creates the text control.
     */
    public void createPartControl(Composite parent) {
        this.parent = parent;
        white = new Color(parent.getDisplay(), 255, 255, 255);
        blue = new Color(parent.getDisplay(), 0, 0, 255);
        courier = new Font(parent.getDisplay(), "Courier", 10, SWT.NORMAL);
        sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
        sc.setBackground(white);
        composite = new Composite(sc, SWT.NONE);
        composite.setBackground(white);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        layout.horizontalSpacing = 30;
        composite.setLayout(layout);
        sc.setContent(composite);
        GridData data = new GridData(GridData.FILL_VERTICAL);
        offsets = new Text(composite, SWT.MULTI | SWT.READ_ONLY);
        offsets.setLayoutData(data);
        hexDump = new Text(composite, SWT.MULTI | SWT.READ_ONLY);
        data = new GridData(GridData.FILL_BOTH);
        hexDump.setLayoutData(data);
        prettyPrint = new Text(composite, SWT.MULTI | SWT.READ_ONLY);
        data = new GridData(GridData.FILL_BOTH);
        prettyPrint.setLayoutData(data);
        offsets.setBackground(white);
        hexDump.setBackground(white);
        prettyPrint.setBackground(white);
        offsets.setForeground(blue);
        hexDump.setForeground(blue);
        prettyPrint.setForeground(blue);
        offsets.setFont(courier);
        hexDump.setFont(courier);
        prettyPrint.setFont(courier);
        offsets.setEnabled(false);
        offsets.setText("");
        hexDump.setText("");
        prettyPrint.setText("");
        composite.setSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        parent.addDisposeListener(new DisposeListener() {

            public void widgetDisposed(DisposeEvent e) {
                white.dispose();
                blue.dispose();
                courier.dispose();
                if (!sc.isDisposed()) {
                    sc.dispose();
                }
                if (!composite.isDisposed()) {
                    composite.dispose();
                }
                if (!offsets.isDisposed()) {
                    offsets.dispose();
                }
                if (!hexDump.isDisposed()) {
                    hexDump.dispose();
                }
                if (!prettyPrint.isDisposed()) {
                    prettyPrint.dispose();
                }
            }
        });
    }

    /**
     * 
     */
    public void setFocus() {
        composite.setFocus();
    }

    /**
     * Set the given hex dump as the text on the underlying 
     * text control.
     * 
     * @param dump
     */
    public void setPacketData(byte[] dump) {
        setOffsetText(dump);
        hexDump.setText(Utils.toHex(dump, false));
        prettyPrint.setText(Utils.prettyPrint(dump, 0, dump.length, 16));
        composite.setSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT, true));
    }

    /**
     * Sets the offset text.
     * 
     * @param dump
     */
    private void setOffsetText(byte[] dump) {
        lines = dump.length / 16 + 1;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < lines; i++) {
            sb.append(Utils.toHex(i * 16, 4)).append('\n');
        }
        offsets.setText(sb.toString());
    }

    /**
     * Computes and selects the respective start and end offsets in the 
     * packet dump view. The given start and end offsets are with respect
     * to the packet data. This needs to be mapped to the view offsets.
     * 
     * @param start
     * @param end
     */
    public void selectOffset(int start, int end) {
        if (start == -1 || end == -1) {
            hexDump.clearSelection();
            prettyPrint.clearSelection();
            sc.setOrigin(0, 0);
            return;
        }
        int realStart = start * 3 + start / 16;
        int realEnd = (end + 1) * 3 - 1 + end / 16;
        hexDump.setSelection(realStart, realEnd);
        realStart = start + (start / 16) * 2;
        realEnd = end + 1 + (end / 16) * 2;
        prettyPrint.setSelection(realStart, realEnd);
        hexDump.showSelection();
        prettyPrint.showSelection();
        scrollIfNeeded(start, end);
    }

    /**
     * Scrolls the ScrolledComposite if required.
     * 
     * @param start
     * @param end
     */
    private void scrollIfNeeded(int start, int end) {
        Point origin = sc.getOrigin();
        Rectangle rect = parent.getClientArea();
        int mid = origin.y + rect.height / 2;
        int targetPixel = (start / 16) * hexDump.getLineHeight();
        if (targetPixel > mid || targetPixel < origin.y) {
            if (targetPixel < origin.y) {
                targetPixel -= rect.height / 2;
                if (targetPixel < 0) {
                    targetPixel = 0;
                }
            }
            sc.setOrigin(0, targetPixel);
        }
    }
}

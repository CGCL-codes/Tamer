package net.community.chest.ui.components.datetime;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import net.community.chest.awt.layout.border.BorderLayoutPosition;
import net.community.chest.dom.DOMUtils;
import net.community.chest.ui.helpers.panel.PresetBorderLayoutPanel;

/**
 * <P>Copyright 2008 as per GPLv2</P>
 *
 * <P>Provides a day-of-month selection panel with days of the week on top</P>
 * 
 * @author Lyor G.
 * @since Dec 16, 2008 9:17:37 AM
 */
public class DateWithDayOfWeekPanel extends PresetBorderLayoutPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = -366673459179027369L;

    private DayOfMonthValuePanel _dayOfMonthPanel;

    public DayOfMonthValuePanel getDayOfMonthValuePanel() {
        return _dayOfMonthPanel;
    }

    public void setDayOfMonthValuePanel(DayOfMonthValuePanel p) {
        if (_dayOfMonthPanel != p) _dayOfMonthPanel = p;
    }

    protected DayOfMonthValuePanel createDayOfMonthPanel(Element elem, boolean useElement) throws RuntimeException {
        if ((null == elem) || (!useElement)) return new DayOfMonthValuePanel();
        return new DayOfMonthValuePanel(elem);
    }

    private DayOfWeekValuePanel _dayOfWeekPanel;

    public DayOfWeekValuePanel getDayOfWeekValuePanel() {
        return _dayOfWeekPanel;
    }

    public void setDayOfWeekValuePanel(DayOfWeekValuePanel p) {
        if (_dayOfWeekPanel != p) _dayOfWeekPanel = p;
    }

    protected DayOfWeekValuePanel createDayOfWeekValuePanel(Element elem, boolean useElement) throws RuntimeException {
        if ((null == elem) || (!useElement)) return new DayOfWeekValuePanel();
        return new DayOfWeekValuePanel(elem);
    }

    private BorderLayoutPosition _dowPos;

    public synchronized BorderLayoutPosition getDayOfWeekValuePanelPosition() {
        if (null == _dowPos) _dowPos = BorderLayoutPosition.NORTH;
        return _dowPos;
    }

    public synchronized void setDayOfWeekValuePanelPosition(BorderLayoutPosition pos) {
        if (_dowPos != pos) _dowPos = pos;
    }

    protected JComponent layoutDayOfWeekComponent(Element elem, boolean useElement) {
        DayOfWeekValuePanel p = getDayOfWeekValuePanel();
        if (null == p) {
            if ((p = createDayOfWeekValuePanel(elem, useElement)) != null) setDayOfWeekValuePanel(p);
        }
        if (p != null) {
            final BorderLayoutPosition pos = getDayOfWeekValuePanelPosition();
            if ((null == pos) || BorderLayoutPosition.CENTER.equals(pos)) throw new IllegalStateException("layoutComponent(" + DOMUtils.toString(elem) + ") illegal day-of-week panel position: " + pos);
            add(p, pos);
        }
        return p;
    }

    protected JComponent layoutDayOfMonthComponent(Element elem, boolean useElement) {
        DayOfMonthValuePanel p = getDayOfMonthValuePanel();
        if (null == p) {
            if ((p = createDayOfMonthPanel(elem, useElement)) != null) setDayOfMonthValuePanel(p);
        }
        if (p != null) add(p, BorderLayout.CENTER);
        return p;
    }

    @Override
    public void layoutComponent(Element elem) throws RuntimeException {
        super.layoutComponent(elem);
        layoutDayOfWeekComponent(elem, false);
        layoutDayOfMonthComponent(elem, false);
    }

    public DateWithDayOfWeekPanel(int hgap, int vgap, Document doc, boolean autoLayout) {
        super(hgap, vgap, doc, autoLayout);
    }

    public DateWithDayOfWeekPanel(int hgap, int vgap, boolean autoLayout) {
        this(hgap, vgap, (Document) null, autoLayout);
    }

    public DateWithDayOfWeekPanel(int hgap, int vgap) {
        this(hgap, vgap, true);
    }

    public DateWithDayOfWeekPanel(boolean autoLayout) {
        this(0, 0, autoLayout);
    }

    public DateWithDayOfWeekPanel(Element elem) {
        this(false);
        layoutComponent(elem);
    }

    public DateWithDayOfWeekPanel(Document doc) {
        this(false);
        layoutComponent(doc);
    }

    public DateWithDayOfWeekPanel() {
        this(true);
    }
}

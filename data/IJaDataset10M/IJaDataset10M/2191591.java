package org.rapla.components.calendarview.html;

import java.text.SimpleDateFormat;
import java.util.*;
import org.rapla.components.calendarview.Block;
import org.rapla.components.calendarview.Builder;
import org.rapla.components.calendarview.WeekdayMapper;
import org.rapla.components.util.DateTools;

public class HTMLMonthView extends AbstractHTMLView {

    public static final int ROWS = 6;

    public static final int COLUMNS = 7;

    HTMLSmallDaySlot[] slots;

    public Collection getBlocks() {
        ArrayList list = new ArrayList();
        for (int i = 0; i < slots.length; i++) {
            list.addAll(slots[i]);
        }
        return Collections.unmodifiableCollection(list);
    }

    protected boolean isEmpty(int column) {
        for (int i = column; i < slots.length; i += 7) {
            if (!slots[i].isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void rebuild() {
        Calendar counter = (Calendar) blockCalendar.clone();
        Iterator it = builders.iterator();
        while (it.hasNext()) {
            Builder b = (Builder) it.next();
            b.prepareBuild(getStartDate(), getEndDate());
        }
        slots = new HTMLSmallDaySlot[daysInMonth];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = new HTMLSmallDaySlot(String.valueOf(i + 1));
        }
        it = builders.iterator();
        while (it.hasNext()) {
            Builder b = (Builder) it.next();
            if (b.isEnabled()) {
                b.build(this);
            }
        }
        counter.setTime(getStartDate());
        int lastRow = 0;
        HTMLSmallDaySlot[][] table = new HTMLSmallDaySlot[ROWS][COLUMNS];
        counter.setTime(getStartDate());
        int firstDayOfWeek = getFirstWeekday();
        if (counter.get(Calendar.DAY_OF_WEEK) != firstDayOfWeek) {
            counter.add(Calendar.DATE, -6);
            counter.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);
        }
        int offset = (int) DateTools.countDays(counter.getTime(), getStartDate());
        for (int i = 0; i < daysInMonth; i++) {
            int column = (offset + i) % 7;
            int row = (counter.get(Calendar.DATE) + 6 - column) / 7;
            table[row][column] = slots[i];
            lastRow = row;
            slots[i].sort();
            counter.add(Calendar.DATE, 1);
        }
        StringBuffer result = new StringBuffer();
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMMM yyyy", locale);
        result.append("<h2 class=\"title\">" + monthYearFormat.format(getStartDate()) + "</h2>\n");
        result.append("<table class=\"month_table\">\n");
        result.append("<tr>\n");
        for (int i = 0; i < COLUMNS; i++) {
            if (isExcluded(i)) {
                continue;
            }
            int weekday = counter.get(Calendar.DAY_OF_WEEK);
            if (counter.getTime().equals(getStartDate())) {
                offset = i;
            }
            result.append("<td class=\"month_header\" width=\"14%\">");
            result.append("<nobr>");
            String name = getWeekdayName(weekday);
            result.append(name);
            result.append("</nobr>");
            result.append("</td>");
        }
        result.append("\n</tr>");
        for (int row = 0; row <= lastRow; row++) {
            boolean excludeRow = true;
            for (int column = 0; column < COLUMNS; column++) {
                if (table[row][column] != null && !isExcluded(column)) {
                    excludeRow = false;
                }
            }
            if (excludeRow) continue;
            result.append("<tr>\n");
            for (int column = 0; column < COLUMNS; column++) {
                if (isExcluded(column)) {
                    continue;
                }
                HTMLSmallDaySlot slot = table[row][column];
                if (slot == null) {
                    result.append("<td class=\"month_cell\" height=\"40\"></td>\n");
                } else {
                    result.append("<td class=\"month_cell\" valign=\"top\" height=\"40\">\n");
                    slot.paint(result);
                    result.append("</td>\n");
                }
            }
            result.append("</tr>\n");
        }
        result.append("</table>");
        m_html = result.toString();
    }

    public void addBlock(Block block, int col, int slot) {
        checkBlock(block);
        blockCalendar.setTime(block.getStart());
        int day = blockCalendar.get(Calendar.DATE);
        slots[day - 1].putBlock(block);
    }
}

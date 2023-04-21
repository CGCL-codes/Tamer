package org.blueoxygen.util;

import java.util.*;
import java.text.SimpleDateFormat;

/**
 * Class WebCalendar
 */
public class WebCalendar {

    /** */
    protected GregorianCalendar m_gc;

    /** */
    protected SimpleDateFormat m_sdf;

    /** */
    public WebCalendar() {
        m_gc = new GregorianCalendar();
        m_sdf = new SimpleDateFormat();
    }

    /** */
    public void setYear(int _iYear) {
        m_gc.set(Calendar.YEAR, _iYear);
    }

    /** */
    public void setMonth(int _iMonth) {
        m_gc.set(Calendar.MONTH, _iMonth);
    }

    /** */
    public String getMonth() {
        m_sdf.applyPattern("MMMM");
        return m_sdf.format(m_gc.getTime()).toString();
    }

    /** */
    public GregorianCalendar getCalendar() {
        return m_gc;
    }

    /** */
    public String[] getDays() {
        String days[] = new String[m_gc.getMaximum(Calendar.DAY_OF_WEEK) - m_gc.getMinimum(Calendar.DAY_OF_WEEK) + 1];
        m_gc.set(Calendar.DAY_OF_WEEK, 1);
        for (int i = 0; i < days.length; i++) {
            days[i] = m_sdf.format(m_gc.getTime()).toString();
            m_gc.roll(Calendar.DAY_OF_WEEK, true);
        }
        return days;
    }

    /** */
    public String renderOneMonth(String _sTableWidth, String _sCellWidth) {
        StringBuffer sb = new StringBuffer();
        sb.append("<table border='1' width='").append(_sTableWidth).append("'").append(" cellspacing='0' cellpadding='0'>").append("<tr><td align='center' colspan='7'>").append(getMonth()).append("</td></tr>").append("<tr>");
        String days[] = getDays();
        for (int i = 0; i < days.length; i++) {
            sb.append("<td width='").append(_sCellWidth).append("'").append(" align='center'>").append(days[i]).append("</td>");
        }
        sb.append("</tr>");
        m_gc.set(Calendar.DAY_OF_MONTH, 1);
        boolean finish = false;
        for (; ; ) {
            sb.append("<tr>");
            for (int i = 0; i < days.length; i++) {
                Date t = m_gc.getTime();
                m_sdf.applyPattern("EEEE");
                if (days[i].equals(m_sdf.format(t).toString())) {
                    m_sdf.applyPattern("d");
                    sb.append("<td align='center'>").append(m_sdf.format(t).toString()).append("</td>");
                    if (m_gc.get(Calendar.DAY_OF_MONTH) == m_gc.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                        finish = true;
                        break;
                    }
                    m_gc.roll(Calendar.DAY_OF_MONTH, true);
                } else {
                    sb.append("<td align='center'>&nbsp;</td>");
                }
            }
            sb.append("</tr>");
            if (finish) break;
        }
        sb.append("</table>");
        return sb.toString();
    }
}

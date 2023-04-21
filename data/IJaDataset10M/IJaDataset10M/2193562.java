package net.sf.sbcc.date;

import java.awt.Graphics;
import java.util.Calendar;
import javax.swing.SwingConstants;

/**
 * <br>
 * Modifications:
 * <ul>
 * <!--
 * <li> some text here (2008-mm-dd, Christoph Bimminger)</li>
 * -->
 * </ul>
 * <br>
 * <br>
 * <i>This class is part of the Swing Better Components Collection (SBCC), which is an open source project. 
 * The project is available at <a href="http://sbcc.sourceforge.net" >http://sbcc.sourceforge.net</a> and
 * is distributed under the conditions of the GNU Library or Lesser General Public License (LGPL).</i><br>
 * <br>
 * Filename: DefaultDateRenderer.java<br>
 * Last modified: 2008-04-19<br>
 * 
 * @author Christoph Bimminger

 
 */
public class DefaultDateRenderer extends AbstractDateRenderer {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Calendar calendar;

    public DefaultDateRenderer(Calendar P_calendar) {
        super();
        calendar = P_calendar;
        setHorizontalAlignment(SwingConstants.RIGHT);
        setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Calendar F_now = Calendar.getInstance();
        if (F_now.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) && F_now.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) && F_now.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
            g.drawRect(0, 0, getSize().width, getSize().height);
        }
    }
}

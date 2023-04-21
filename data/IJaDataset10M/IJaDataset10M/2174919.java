package org.mc4j.console.dashboard.components.mejb;

import javax.management.j2ee.statistics.Statistic;
import javax.management.j2ee.statistics.Stats;
import javax.swing.*;

/**
 * @author Greg Hinkle (ghinkle@users.sourceforge.net), Apr 9, 2004
 * @version $Revision: 480 $($Author: ghinkl $ / $Date: 2004-10-05 01:17:41 -0400 (Tue, 05 Oct 2004) $)
 */
public class StatsComponent extends JTextArea {

    public void refresh() {
        Stats stats = null;
        StringBuffer buf = new StringBuffer();
        Statistic[] statistics = stats.getStatistics();
        for (int i = 0; i < statistics.length; i++) {
            Statistic statistic = statistics[i];
            buf.append(statistic.getClass().getName());
            buf.append("\n");
            buf.append(statistic);
            buf.append("\n\n");
        }
    }
}

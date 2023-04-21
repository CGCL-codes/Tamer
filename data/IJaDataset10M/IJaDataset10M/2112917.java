package org.drftpd.slaveselection.filter;

import net.sf.drftpd.NoAvailableSlaveException;
import org.drftpd.master.RemoteSlave;
import org.drftpd.remotefile.LinkedRemoteFileInterface;
import org.drftpd.usermanager.User;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

/**
 * Checks ScoreChart for slaves with 0 bw usage and assigns 1 extra point to the one in that has been unused for the longest time.
 *
 * @author mog, zubov
 * @version $Id: CycleFilter.java 847 2004-12-02 03:32:41Z mog $
 */
public class CycleFilter extends Filter {

    public CycleFilter(FilterChain fc, int i, Properties p) {
    }

    public void process(ScoreChart scorechart, User user, InetAddress peer, char direction, LinkedRemoteFileInterface dir, RemoteSlave sourceSlave) throws NoAvailableSlaveException {
        ArrayList tempList = new ArrayList(scorechart.getSlaveScores());
        while (true) {
            if (tempList.isEmpty()) {
                return;
            }
            ScoreChart.SlaveScore first = (ScoreChart.SlaveScore) tempList.get(0);
            ArrayList equalList = new ArrayList();
            equalList.add(first);
            tempList.remove(first);
            for (Iterator iter = tempList.iterator(); iter.hasNext(); ) {
                ScoreChart.SlaveScore match = (ScoreChart.SlaveScore) iter.next();
                if (match.compareTo(first) == 0) {
                    equalList.add(match);
                    iter.remove();
                }
            }
            ScoreChart.SlaveScore leastUsed = first;
            for (Iterator iter = equalList.iterator(); iter.hasNext(); ) {
                ScoreChart.SlaveScore match = (ScoreChart.SlaveScore) iter.next();
                if (match.getRSlave().getLastTransferForDirection(direction) < leastUsed.getRSlave().getLastTransferForDirection(direction)) {
                    leastUsed = match;
                }
            }
            if (leastUsed != null) {
                leastUsed.addScore(1);
            }
        }
    }
}

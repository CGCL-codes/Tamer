package org.drftpd.slaveselection.filter;

import net.sf.drftpd.NoAvailableSlaveException;
import net.sf.drftpd.master.config.FtpConfig;
import org.drftpd.PropertyHelper;
import org.drftpd.Time;
import org.drftpd.master.RemoteSlave;
import org.drftpd.remotefile.LinkedRemoteFileInterface;
import org.drftpd.usermanager.User;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.Properties;

/**
 * @author mog
 * @version $Id: MintimeonlineFilter.java 936 2005-01-31 22:25:52Z mog $
 */
public class MintimeonlineFilter extends Filter {

    private long _minTime;

    private float _multiplier;

    public MintimeonlineFilter(FilterChain fc, int i, Properties p) {
        _minTime = Time.parseTime(PropertyHelper.getProperty(p, i + ".mintime"));
        _multiplier = BandwidthFilter.parseMultiplier(PropertyHelper.getProperty(p, i + ".multiplier"));
    }

    public void process(ScoreChart scorechart, User user, InetAddress peer, char direction, LinkedRemoteFileInterface dir, RemoteSlave sourceSlave) throws NoAvailableSlaveException {
        process(scorechart, user, peer, direction, dir, System.currentTimeMillis());
    }

    protected void process(ScoreChart scorechart, User user, InetAddress peer, char direction, LinkedRemoteFileInterface dir, long currentTime) throws NoAvailableSlaveException {
        for (Iterator iter = scorechart.getSlaveScores().iterator(); iter.hasNext(); ) {
            ScoreChart.SlaveScore score = (ScoreChart.SlaveScore) iter.next();
            long lastTransfer = currentTime - score.getRSlave().getLastTransferForDirection(direction);
            if (lastTransfer < _minTime) {
                score.addScore(-(long) (lastTransfer * _multiplier));
            }
        }
    }
}

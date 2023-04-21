package com.atosorigin.nl.jspring2008.buzzword.services;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import com.atosorigin.nl.jspring2008.buzzword.stats.BluetoothStatistics;
import com.atosorigin.nl.jspring2008.buzzword.stats.BuzzwordStatistics;
import com.atosorigin.nl.jspring2008.buzzword.stats.RiaStatistics;

/**
 * @author Jeroen Benckhuijsen (jeroen.benckhuijsen@gmail.com)
 * 
 */
public class StatisticsDispatcherImpl extends JmsTemplate implements StatisticsDispatcher {

    private static final Logger LOG = Logger.getLogger(StatisticsDispatcherImpl.class);

    /**
	 * 
	 */
    public StatisticsDispatcherImpl() {
        super();
    }

    @Override
    public void dispatchStatistics(final BuzzwordStatistics stats) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Sending message over JMS: " + stats);
        }
        final RiaStatistics riaStats = convertToRiaStatistics(stats);
        this.send(new MessageCreator() {

            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createObjectMessage(riaStats);
            }
        });
    }

    /**
	 * @param stats
	 * @return
	 */
    private RiaStatistics convertToRiaStatistics(BuzzwordStatistics stats) {
        RiaStatistics result = new RiaStatistics();
        Integer mp3s = 0;
        Integer pictures = 0;
        Integer videos = 0;
        Long kbytes = 0L;
        for (BluetoothStatistics btstats : stats.getBluetoothNodes()) {
            mp3s += btstats.getMusicClipsReceived().intValue();
            pictures += btstats.getPicturesReceived().intValue();
            videos += btstats.getVideosReceived().intValue();
            kbytes += btstats.getTotalFileSize().intValue();
        }
        Integer textmessages = 0;
        if (stats.getSmsNode() != null) {
            textmessages = stats.getSmsNode().getMessagesReceived().intValue();
            kbytes += stats.getSmsNode().getTotalMessageSize();
        }
        result.setKbytes(kbytes);
        result.setMp3s(mp3s);
        result.setPictures(pictures);
        result.setTextmessages(textmessages);
        result.setVideos(videos);
        return result;
    }
}

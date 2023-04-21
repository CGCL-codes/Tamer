package br.com.javaplanet.util;

import br.com.javaplanet.data.FeedLinkVo;
import br.com.javaplanet.informa.HibernateObserver;
import de.nava.informa.core.ChannelIF;
import de.nava.informa.core.ItemIF;
import de.nava.informa.impl.basic.ChannelBuilder;
import de.nava.informa.utils.manager.PersistenceManagerConfig;
import de.nava.informa.utils.poller.PersistenceObserver;
import de.nava.informa.utils.poller.Poller;
import de.nava.informa.utils.poller.PollerApproverIF;
import net.java.dev.springannotation.annotation.Bean;
import org.apache.log4j.Logger;
import java.net.URL;
import java.util.HashMap;

@Bean(name = "informa", initMethod = "init")
public class InformaConfigurator {

    private static final Logger log = Logger.getLogger(InformaConfigurator.class);

    private ConfigHelper config;

    private final ChannelBuilder builder = new ChannelBuilder();

    private final Poller poller = new Poller(3, Poller.POLICY_SKIP_AFTER_EXISTING, "JNuke CMS");

    private final HashMap<FeedLinkVo, ChannelIF> chanels = new HashMap<FeedLinkVo, ChannelIF>();

    private HibernateObserver hibernateObserver;

    public void setHibernateObserver(HibernateObserver hiernateObserver) {
        this.hibernateObserver = hiernateObserver;
    }

    public ChannelBuilder getBuilder() {
        return builder;
    }

    public HashMap<FeedLinkVo, ChannelIF> getChanels() {
        return chanels;
    }

    public void setConfig(final ConfigHelper config) {
        this.config = config;
    }

    public void init() {
        try {
            poller.setPeriod(120 * 60 * 1000);
            poller.addApprover(new PollerApproverIF() {

                public boolean canAddItem(final ItemIF item, final ChannelIF channel) {
                    return true;
                }
            });
            PersistenceManagerConfig.setPersistenceManagerClassName("de.nava.informa.utils.manager.memory.PersistenceManager");
            poller.addObserver(new PersistenceObserver(PersistenceManagerConfig.getPersistenceManager()));
            hibernateObserver.setChanels(chanels);
            poller.addObserver(hibernateObserver);
            for (final FeedLinkVo fl : config.sourceLinks()) {
                addLink(fl);
            }
        } catch (final Throwable e1) {
            InformaConfigurator.log.error("", e1);
        }
    }

    public void addLink(FeedLinkVo fl) {
        try {
            builder.beginTransaction();
            final ChannelIF channel = builder.createChannel("");
            channel.setLocation(new URL(fl.getUrl()));
            poller.registerChannel(channel);
            poller.updateChannel(channel);
            builder.endTransaction();
            chanels.put(fl, channel);
        } catch (final Throwable e) {
            InformaConfigurator.log.error("", e);
        }
    }

    public void removeLink(FeedLinkVo fl) {
        if (chanels.containsKey(fl)) {
            ChannelIF ch = chanels.get(fl);
            poller.unregisterChannel(ch);
            chanels.remove(fl);
        }
    }
}

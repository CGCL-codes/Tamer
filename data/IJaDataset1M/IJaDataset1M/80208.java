package com.avaje.ebeaninternal.server.autofetch;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;
import com.avaje.ebean.bean.NodeUsageCollector;
import com.avaje.ebean.meta.MetaAutoFetchStatistic.NodeUsageStats;
import com.avaje.ebean.text.PathProperties;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssoc;
import com.avaje.ebeaninternal.server.el.ElPropertyValue;
import com.avaje.ebeaninternal.server.query.SplitName;

/**
 * Collects usages statistics for a given node in the object graph.
 */
public class StatisticsNodeUsage implements Serializable {

    private static final long serialVersionUID = -1663951463963779547L;

    private static final Logger logger = Logger.getLogger(StatisticsNodeUsage.class.getName());

    private final String monitor = new String();

    private final String path;

    private final boolean queryTuningAddVersion;

    private int profileCount;

    private int profileUsedCount;

    private boolean modified;

    private Set<String> aggregateUsed = new LinkedHashSet<String>();

    public StatisticsNodeUsage(String path, boolean queryTuningAddVersion) {
        this.path = path;
        this.queryTuningAddVersion = queryTuningAddVersion;
    }

    public NodeUsageStats createPublicMeta() {
        synchronized (monitor) {
            String[] usedProps = aggregateUsed.toArray(new String[aggregateUsed.size()]);
            return new NodeUsageStats(path, profileCount, profileUsedCount, usedProps);
        }
    }

    public void buildTunedFetch(PathProperties pathProps, BeanDescriptor<?> rootDesc) {
        synchronized (monitor) {
            BeanDescriptor<?> desc = rootDesc;
            if (path != null) {
                ElPropertyValue elGetValue = rootDesc.getElGetValue(path);
                if (elGetValue == null) {
                    desc = null;
                    logger.warning("Autofetch: Can't find join for path[" + path + "] for " + rootDesc.getName());
                } else {
                    BeanProperty beanProperty = elGetValue.getBeanProperty();
                    if (beanProperty instanceof BeanPropertyAssoc<?>) {
                        desc = ((BeanPropertyAssoc<?>) beanProperty).getTargetDescriptor();
                    }
                }
            }
            for (String propName : aggregateUsed) {
                BeanProperty beanProp = desc.getBeanPropertyFromPath(propName);
                if (beanProp == null) {
                    logger.warning("Autofetch: Can't find property[" + propName + "] for " + desc.getName());
                } else {
                    if (beanProp instanceof BeanPropertyAssoc<?>) {
                        BeanPropertyAssoc<?> assocProp = (BeanPropertyAssoc<?>) beanProp;
                        String targetIdProp = assocProp.getTargetIdProperty();
                        String manyPath = SplitName.add(path, assocProp.getName());
                        pathProps.addToPath(manyPath, targetIdProp);
                    } else {
                        if (beanProp.isLob() && !beanProp.isFetchEager()) {
                        } else {
                            pathProps.addToPath(path, beanProp.getName());
                        }
                    }
                }
            }
            if ((modified || queryTuningAddVersion) && desc != null) {
                BeanProperty[] versionProps = desc.propertiesVersion();
                if (versionProps.length > 0) {
                    pathProps.addToPath(path, versionProps[0].getName());
                }
            }
        }
    }

    public void publish(NodeUsageCollector profile) {
        synchronized (monitor) {
            HashSet<String> used = profile.getUsed();
            profileCount++;
            if (!used.isEmpty()) {
                profileUsedCount++;
                aggregateUsed.addAll(used);
            }
            if (profile.isModified()) {
                modified = true;
            }
        }
    }

    public String toString() {
        return "path[" + path + "] profileCount[" + profileCount + "] used[" + profileUsedCount + "] props" + aggregateUsed;
    }
}

package net.community.chest.rrd4j.client.jmx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ThreadFactory;
import net.community.chest.dom.DOMUtils;
import net.community.chest.rrd4j.common.jmx.MBeanRrdDef;
import net.community.chest.util.logging.LoggerWrapper;
import net.community.chest.util.map.LongsMap;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * <P>Copyright 2008 as per GPLv2</P>
 *
 * @author Lyor G.
 * @since Jan 10, 2008 12:10:35 PM
 */
public abstract class AbstractRrdPollerInstantiator {

    protected AbstractRrdPollerInstantiator() {
        super();
    }

    public static final Collection<Collection<MBeanRrdDef>> updatePolledGroups(final Collection<Collection<MBeanRrdDef>> org, final Collection<? extends MBeanRrdDef> grp) {
        if ((null == grp) || (grp.size() <= 0)) return org;
        Collection<Collection<MBeanRrdDef>> res = org;
        Collection<MBeanRrdDef> grpMain = null;
        Map<String, MBeanRrdDef> grpMap = null;
        for (final MBeanRrdDef mbd : grp) {
            if (null == mbd) continue;
            final String mbName = mbd.getMBeanName();
            MBeanRrdDef prev = ((null == mbName) || (mbName.length() <= 0) || (null == grpMap) || (grpMap.size() <= 0)) ? null : grpMap.get(mbName);
            if (prev != null) {
                if (null == res) res = new LinkedList<Collection<MBeanRrdDef>>();
                res.add(Arrays.asList(mbd));
                continue;
            }
            if (null == grpMain) grpMain = new LinkedList<MBeanRrdDef>();
            grpMain.add(mbd);
            if ((null == mbName) || (mbName.length() <= 0)) continue;
            if (null == grpMap) grpMap = new TreeMap<String, MBeanRrdDef>(String.CASE_INSENSITIVE_ORDER);
            grpMap.put(mbName, mbd);
        }
        if (grpMain != null) {
            if (null == res) res = new LinkedList<Collection<MBeanRrdDef>>();
            res.add(grpMain);
        }
        return res;
    }

    /**
	 * Organizes the available {@link MBeanRrdDef}-initions into groups
	 * according to their step value. If within the same group an MBean
	 * name appears more than once then it is split into a separate group.
	 * The optimization is that we don't need a separate thread for each
	 * definition, but rather we can use a single query for a group of
	 * MBean(s) - provided we need to poll them using the same interval
	 * (which is the expected use-case)
	 * @param defs Original definitions - may be null/empty
	 * @return Grouped definitions - each represented as a separate
	 * {@link Collection} {@link MBeanRrdDef}-initions. May be null/empty if
	 * no original definitions supplied
	 */
    public static final Collection<? extends Collection<? extends MBeanRrdDef>> groupPolledDefinitions(final Collection<? extends MBeanRrdDef> defs) {
        final int numDefs = (null == defs) ? 0 : defs.size();
        if (numDefs <= 0) return null;
        @SuppressWarnings("unchecked") final LongsMap<Collection<MBeanRrdDef>> map = (LongsMap) new LongsMap<Collection>(Collection.class, numDefs, 4);
        for (final MBeanRrdDef mbd : defs) {
            if (null == mbd) continue;
            final long stepVal = mbd.getStep();
            Collection<MBeanRrdDef> group = map.get(stepVal);
            if (null == group) {
                group = new LinkedList<MBeanRrdDef>();
                map.put(stepVal, group);
            }
            group.add(mbd);
        }
        final Collection<Collection<MBeanRrdDef>> groups = map.values();
        if ((null == groups) || (groups.size() <= 0)) return null;
        Collection<Collection<MBeanRrdDef>> res = null;
        for (final Collection<MBeanRrdDef> g : groups) res = updatePolledGroups(res, g);
        return res;
    }

    /**
	 * @param defs The MBeans group to be polled - may NOT be null/empty
	 * @return A 'pair' represented as a {@link java.util.Map.Entry} whose key=the
	 * name of the {@link Thread} to be created for the associated value
	 * (the {@link AbstractMBeanRrdPoller} instance). Ignored if null.
	 * @throws Exception if cannot create the instance
	 */
    protected abstract Map.Entry<String, ? extends AbstractMBeanRrdPoller> createPollerInstance(Collection<? extends MBeanRrdDef> defs) throws Exception;

    /**
	 * @param defs The definitions to be used to create the polling threads.
	 * If null/empty then nothing is done
	 * @param tf The {@link ThreadFactory} to use for creating the threads.
	 * If null, then the default is used
	 * @param l A {@link LoggerWrapper} to use for logging exceptions incurred
	 * during spawning the threads. The wrapper's {@link LoggerWrapper#errorObject(String, Throwable, Object)}
	 * method is called where the {@link Object} is the definitions group that
	 * caused the problem (as a {@link Collection} of {@link MBeanRrdDef}-s. If
	 * no wrapper specified, then the exception is propagated
	 * @return A {@link Collection} of 'pairs' represented as {@link java.util.Map.Entry}-ies
	 * whose key=the thread name and the value the {@link AbstractMBeanRrdPoller}
	 * instance
	 * @throws Exception if no {@link LoggerWrapper} and exception(s) occurred
	 * (or the {@link LoggerWrapper} re-throws an exception)
	 */
    public Collection<? extends Map.Entry<String, ? extends AbstractMBeanRrdPoller>> start(final Collection<? extends MBeanRrdDef> defs, final ThreadFactory tf, final LoggerWrapper l) throws Exception {
        final Collection<? extends Collection<? extends MBeanRrdDef>> gpList = groupPolledDefinitions(defs);
        final int numGroups = (null == gpList) ? 0 : gpList.size();
        final Collection<Map.Entry<String, ? extends AbstractMBeanRrdPoller>> res = (numGroups <= 0) ? null : new ArrayList<Map.Entry<String, ? extends AbstractMBeanRrdPoller>>(numGroups);
        if (numGroups > 0) {
            for (final Collection<? extends MBeanRrdDef> tdl : gpList) {
                if ((null == tdl) || (tdl.size() <= 0)) continue;
                try {
                    final Map.Entry<String, ? extends AbstractMBeanRrdPoller> te = createPollerInstance(tdl);
                    if (null == te) continue;
                    final Thread t = (null == tf) ? new Thread(te.getValue(), te.getKey()) : tf.newThread(te.getValue());
                    if (null == t) continue;
                    t.start();
                    res.add(te);
                } catch (Exception e) {
                    if (l != null) l.errorObject("start()", e, tdl); else throw e;
                }
            }
        }
        return res;
    }

    public Collection<? extends Map.Entry<String, ? extends AbstractMBeanRrdPoller>> start(final NodeList nodes, final ThreadFactory tf, final LoggerWrapper l) throws Exception {
        return (null == nodes) ? null : start(DOMUtils.extractValues(nodes, MBeanRrdDef.XMLINST), tf, l);
    }

    public Collection<? extends Map.Entry<String, ? extends AbstractMBeanRrdPoller>> start(final Element root, final ThreadFactory tf, final LoggerWrapper l) throws Exception {
        return (null == root) ? null : start(DOMUtils.extractValues(root.getChildNodes(), MBeanRrdDef.XMLINST), tf, l);
    }
}

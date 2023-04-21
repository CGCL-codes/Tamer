package net.ontopia.topicmaps.utils.tmrap;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import net.ontopia.utils.StringifierIF;
import net.ontopia.utils.StringTemplateUtils;
import net.ontopia.infoset.core.LocatorIF;
import net.ontopia.topicmaps.core.TMObjectIF;
import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.core.TopicMapStoreIF;
import net.ontopia.topicmaps.entry.TopicMapReferenceIF;
import net.ontopia.topicmaps.entry.TopicMapRepositoryIF;
import net.ontopia.topicmaps.utils.TopicStringifiers;
import net.ontopia.topicmaps.xml.XTMFragmentExporter;

/**
 * EXPERIMENTAL: An implementation that looks up topics in all currently open
 * topic maps in the given registry.
 */
public class RegistryTopicIndex implements TopicIndexIF {

    protected TopicMapRepositoryIF repository;

    protected boolean readonly;

    protected String editBaseuri;

    protected String viewBaseuri;

    protected StringifierIF strify;

    /**
   * @param editBaseuri a URL of the form
   * http://whatever/omnigator/stuff.jsp?tmid=%tmid%&id=%topicid% Note
   * that the %key% tokens are used to build the correct URI.
   */
    public RegistryTopicIndex(TopicMapRepositoryIF repository, boolean readonly, String editBaseuri, String viewBaseuri) {
        this.repository = repository;
        this.readonly = readonly;
        this.editBaseuri = editBaseuri;
        this.viewBaseuri = viewBaseuri;
        this.strify = TopicStringifiers.getDefaultStringifier();
    }

    public Collection getTopics(Collection indicators, Collection sources, Collection subjects) {
        Collection topics = new ArrayList();
        Iterator iter = repository.getReferenceKeys().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            TopicMapReferenceIF ref = repository.getReferenceByKey(key);
            if (!ref.isOpen()) continue;
            TopicMapStoreIF store = null;
            try {
                store = ref.createStore(readonly);
                TopicMapIF topicmap = store.getTopicMap();
                TopicIF topic;
                Iterator it = indicators.iterator();
                while (it.hasNext()) {
                    LocatorIF indicator = (LocatorIF) it.next();
                    topic = topicmap.getTopicBySubjectIdentifier(indicator);
                    if (topic != null) topics.add(topic);
                }
                it = sources.iterator();
                while (it.hasNext()) {
                    LocatorIF srcloc = (LocatorIF) it.next();
                    TMObjectIF object = null;
                    String address = srcloc.getAddress();
                    if (XTMFragmentExporter.isVirtualReference(address)) {
                        if (key.equals(XTMFragmentExporter.sourceTopicMapFromVirtualReference(address))) {
                            object = topicmap.getObjectById(XTMFragmentExporter.resolveVirtualReference(address, key));
                        } else continue;
                    } else object = topicmap.getObjectByItemIdentifier(srcloc);
                    if (object instanceof TopicIF) topics.add(object);
                }
                it = subjects.iterator();
                while (it.hasNext()) {
                    LocatorIF subject = (LocatorIF) it.next();
                    topic = topicmap.getTopicBySubjectLocator(subject);
                    if (topic != null) topics.add(topic);
                }
            } catch (java.io.IOException e) {
                continue;
            } finally {
                if (store != null) store.close();
            }
        }
        return topics;
    }

    public Collection loadRelatedTopics(Collection indicators, Collection sources, Collection subjects, boolean two_step) {
        return getTopics(indicators, sources, subjects);
    }

    public Collection getTopicPages(Collection indicators, Collection sources, Collection subjects) {
        Collection pages = new ArrayList();
        Iterator iter = repository.getReferenceKeys().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            TopicMapReferenceIF ref = repository.getReferenceByKey(key);
            if (!ref.isOpen()) continue;
            TopicMapStoreIF store = null;
            try {
                store = ref.createStore(readonly);
                TopicMapIF topicmap = store.getTopicMap();
                TopicIF topic;
                Iterator it = indicators.iterator();
                while (it.hasNext()) {
                    LocatorIF indicator = (LocatorIF) it.next();
                    topic = topicmap.getTopicBySubjectIdentifier(indicator);
                    if (topic != null) pages.add(getTopicPage(topic, key));
                }
                it = sources.iterator();
                while (it.hasNext()) {
                    LocatorIF srcloc = (LocatorIF) it.next();
                    TMObjectIF object = null;
                    String address = srcloc.getAddress();
                    if (XTMFragmentExporter.isVirtualReference(address)) {
                        if (key.equals(XTMFragmentExporter.sourceTopicMapFromVirtualReference(address))) {
                            object = topicmap.getObjectById(XTMFragmentExporter.resolveVirtualReference(address, key));
                        } else continue;
                    } else object = topicmap.getObjectByItemIdentifier(srcloc);
                    if (object instanceof TopicIF) pages.add(getTopicPage((TopicIF) object, key));
                }
                it = subjects.iterator();
                while (it.hasNext()) {
                    LocatorIF subject = (LocatorIF) it.next();
                    topic = topicmap.getTopicBySubjectLocator(subject);
                    if (topic != null) pages.add(getTopicPage(topic, key));
                }
            } catch (java.io.IOException e) {
                continue;
            } finally {
                if (store != null) store.close();
            }
        }
        return pages;
    }

    public TopicPages getTopicPages2(Collection indicators, Collection sources, Collection subjects) {
        TopicPages retVal = new TopicPages();
        Iterator iter = repository.getReferenceKeys().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            TopicMapReferenceIF ref = repository.getReferenceByKey(key);
            if (!ref.isOpen()) continue;
            String topicMapHandle = ref.getId();
            TopicMapStoreIF store = null;
            try {
                store = ref.createStore(readonly);
                TopicMapIF topicmap = store.getTopicMap();
                TopicIF topic;
                String tmReifierName = TopicPage.getReifierName(topicmap);
                Iterator it = indicators.iterator();
                while (it.hasNext()) {
                    LocatorIF indicator = (LocatorIF) it.next();
                    topic = topicmap.getTopicBySubjectIdentifier(indicator);
                    if (topic != null) retVal.addPage(topicMapHandle, getTopicPage(topic, key), tmReifierName);
                }
                it = sources.iterator();
                while (it.hasNext()) {
                    LocatorIF srcloc = (LocatorIF) it.next();
                    TMObjectIF object = null;
                    String address = srcloc.getAddress();
                    if (XTMFragmentExporter.isVirtualReference(address)) {
                        if (key.equals(XTMFragmentExporter.sourceTopicMapFromVirtualReference(address))) {
                            object = topicmap.getObjectById(XTMFragmentExporter.resolveVirtualReference(address, key));
                        } else continue;
                    } else object = topicmap.getObjectByItemIdentifier(srcloc);
                    if (object instanceof TopicIF) {
                        retVal.addPage(topicMapHandle, getTopicPage((TopicIF) object, key), tmReifierName);
                    }
                }
                it = subjects.iterator();
                while (it.hasNext()) {
                    LocatorIF subject = (LocatorIF) it.next();
                    topic = topicmap.getTopicBySubjectLocator(subject);
                    if (topic != null) retVal.addPage(topicMapHandle, getTopicPage(topic, key), tmReifierName);
                }
            } catch (java.io.IOException e) {
                continue;
            } finally {
                if (store != null) store.close();
            }
        }
        return retVal;
    }

    public void close() {
        repository = null;
    }

    private TopicPage getTopicPage(TopicIF topic, String key) {
        Map map = new HashMap();
        map.put("tmid", key);
        map.put("topicid", topic.getObjectId());
        String name = strify.toString(topic);
        String editUrl = null;
        String viewUrl = null;
        if (editBaseuri != null) editUrl = StringTemplateUtils.replace(editBaseuri, map);
        if (viewBaseuri != null) viewUrl = StringTemplateUtils.replace(viewBaseuri, map);
        return new TopicPage(editUrl, viewUrl, name, name, topic);
    }
}

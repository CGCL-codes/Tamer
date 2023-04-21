package gwtm.server.tm;

import gwtm.client.services.tm.virtual.LocatorVirtual;
import gwtm.client.services.tm.virtual.TopicMapVirtual;
import java.util.Set;
import java.util.Vector;
import org.tmapi.core.TopicMap;
import org.tmapi.core.*;
import org.tmapi.core.Locator;

/**
 *
 * @author User
 */
public class TopicMapHelper {

    /** Creates a new instance of TopicMapHelper */
    public TopicMapHelper() {
    }

    public static TopicMapVirtual createVirtual(TopicMap tm) {
        if (tm == null) return null;
        TopicMapVirtual tmv = new TopicMapVirtual();
        loadToVirtual(tm, tmv);
        return tmv;
    }

    public static void loadToVirtual(TopicMap tm, TopicMapVirtual tmv) {
        tmv.setID(tm.getObjectId());
        String tmName = tm.getBaseLocator().getReference();
        LocatorVirtual loc = new LocatorVirtual(tmName);
        tmv.setBaseLocator(loc);
        tmv.setDisplayName(tmName);
    }

    public static Topic getTopicWithSubjectIdentifierForceExist(TopicMap tm, String sLoc) throws Exception {
        Topic t = getTopicWithSubjectIdentifier(tm, sLoc);
        if (t == null) t = createTopicWithSubjectIdentifier(tm, sLoc);
        return t;
    }

    public static Topic getTopicWithSubjectIdentifier(TopicMap tm, String sLoc) throws Exception {
        Locator loc = tm.createLocator(sLoc);
        Topic t = TopicsIndexHelper.getTopicsIndex(tm).getTopicBySubjectIdentifier(loc);
        return t;
    }

    public static Topic createTopicWithSubjectIdentifier(TopicMap tm, String sLoc) {
        Topic t = tm.createTopic();
        t.addSubjectIdentifier(tm.createLocator(sLoc));
        return t;
    }

    public static Vector<Topic> getTopicsWithTextLike(TopicMap tm, String text) {
        Vector<Topic> v = new Vector<Topic>();
        Set<Topic> set = tm.getTopics();
        for (Topic t : set) {
            boolean b = false;
            Set<TopicName> names = t.getTopicNames();
            for (TopicName n : names) {
                if (n.getValue().indexOf(text) != -1) b = true;
            }
            Set<Locator> locs = t.getSubjectIdentifiers();
            for (Locator loc : locs) {
                if (loc.getReference().indexOf(text) != -1) b = true;
            }
            locs = t.getSubjectLocators();
            for (Locator loc : locs) {
                if (loc.getReference().indexOf(text) != -1) b = true;
            }
            locs = t.getSourceLocators();
            for (Locator loc : locs) {
                if (loc.getReference().indexOf(text) != -1) b = true;
            }
            Set<Occurrence> ocs = t.getOccurrences();
            for (Occurrence oc : ocs) {
                if (oc.getValue() != null && oc.getValue().indexOf(text) != -1) b = true;
                if (oc.getResource() != null && oc.getResource().getReference().indexOf(text) != -1) b = true;
            }
            if (b) v.add(t);
        }
        return v;
    }

    public static Vector<Topic> getTopicsWithOutName(TopicMap tm) {
        Vector<Topic> v = new Vector<Topic>();
        Set<Topic> set = tm.getTopics();
        for (Topic t : set) {
            Set<TopicName> names = t.getTopicNames();
            if (names.size() == 0) v.add(t);
        }
        return v;
    }

    public static Vector<Association> getAssociationsWithRoleOfType(TopicMap tm, Topic roleType) {
        Vector<Association> v = new Vector<Association>();
        Set<Association> set = tm.getAssociations();
        for (Association a : set) {
            boolean b = false;
            Set<AssociationRole> roles = a.getAssociationRoles();
            for (AssociationRole r : roles) {
                Topic t = r.getType();
                if (t != null && t.getObjectId().equals(roleType.getObjectId())) b = true;
            }
            if (b) v.add(a);
        }
        return v;
    }

    public static Vector<Association> getAssociationsWithoutRole(TopicMap tm) {
        Vector<Association> v = new Vector<Association>();
        Set<Association> set = tm.getAssociations();
        for (Association a : set) {
            Set<AssociationRole> roles = a.getAssociationRoles();
            if (roles.size() == 0) v.add(a);
        }
        return v;
    }

    public static Vector<Association> getAssociationsWithPlayer(TopicMap tm, Topic player) {
        Vector<Association> v = new Vector<Association>();
        Set<Association> set = tm.getAssociations();
        for (Association a : set) {
            boolean b = false;
            Set<AssociationRole> roles = a.getAssociationRoles();
            for (AssociationRole r : roles) {
                Topic t = r.getPlayer();
                if (t != null && t.getObjectId().equals(player.getObjectId())) b = true;
            }
            if (b) v.add(a);
        }
        return v;
    }

    public static Vector<Association> getAssociationsWithoutPlayer(TopicMap tm) {
        Vector<Association> v = new Vector<Association>();
        Set<Association> set = tm.getAssociations();
        for (Association a : set) {
            Set<AssociationRole> roles = a.getAssociationRoles();
            if (roles.size() == 0) v.add(a);
        }
        return v;
    }
}

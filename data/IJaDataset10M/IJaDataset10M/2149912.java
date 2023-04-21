package org.nightlabs.jfire.base.ui.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.jdo.JDOHelper;
import javax.jdo.spi.PersistenceCapable;
import javax.security.auth.login.LoginException;
import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.nightlabs.base.ui.notification.SelectionManager;
import org.nightlabs.jdo.ObjectID;
import org.nightlabs.jfire.base.jdo.JDOObjectID2PCClassMap;
import org.nightlabs.jfire.base.ui.jdo.notification.SelectionNotificationProxy;
import org.nightlabs.jfire.base.ui.login.Login;
import org.nightlabs.jfire.config.dao.ConfigSetupDAO;
import org.nightlabs.jfire.config.id.ConfigID;
import org.nightlabs.notification.NotificationEvent;
import org.nightlabs.notification.SubjectCarrier;
import org.nightlabs.progress.NullProgressMonitor;

/**
 * SelectionListener based on SelectionNotificationProxy that additionally
 * triggers selections for a ConfigID when an object or its ObjectID from a set
 * of configuration-link objecttypes was selected.
 * This set of objecttypes can be configured through the
 * conifgtypeset extension-point.
 * 
 * @see org.nightlabs.jfire.base.ui.config.ConfigTypeRegistry
 * 
 * @author Alexander Bieber <alex[AT]nightlabs[DOT]de>
 *
 */
public class ConfigLinkSelectionNotificationProxy extends SelectionNotificationProxy {

    /**
	 * LOG4J logger used by this class
	 */
    private static final Logger logger = Logger.getLogger(ConfigLinkSelectionNotificationProxy.class);

    /**
	 * @param source
	 * @param zone
	 * @param ignoreInheritance
	 */
    public ConfigLinkSelectionNotificationProxy(Object source, String zone, boolean ignoreInheritance, boolean clearOnEmptySelection) {
        super(source, zone, ignoreInheritance, clearOnEmptySelection);
    }

    /**
	 * Triggers
	 * @param object
	 * @return
	 */
    private Object buildNotificationSubject(Object object) {
        Class objectClass = null;
        ObjectID objectID = null;
        if (object instanceof ObjectID) {
            Class jdoObjectClass = JDOObjectID2PCClassMap.sharedInstance().getPersistenceCapableClass(object);
            if (jdoObjectClass != null) {
                objectClass = jdoObjectClass;
                objectID = (ObjectID) object;
            }
        } else if (object instanceof PersistenceCapable) {
            Object idObject = JDOHelper.getObjectId(object);
            if (!(idObject instanceof ObjectID)) {
                logger.warn("ID-object of PersistenceCapable selection object was not an instance of ObjectID but " + idObject.getClass().getName() + " and was ignored.");
                return null;
            }
            objectID = (ObjectID) idObject;
            objectClass = object.getClass();
        } else return null;
        if (!ConfigSetupDAO.sharedInstance().containsRegistrationForLinkClass(objectClass, new NullProgressMonitor())) return null;
        try {
            return ConfigID.create(Login.getLogin().getOrganisationID(), objectID, objectClass);
        } catch (LoginException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void selectionChanged(SelectionChangedEvent event) {
        super.selectionChanged(event);
        ISelection selection = event.getSelection();
        if (!selection.isEmpty()) {
            NotificationEvent e = null;
            if (selection instanceof IStructuredSelection) {
                List subjects = new ArrayList();
                Iterator i = ((IStructuredSelection) selection).iterator();
                while (i.hasNext()) {
                    Object o = buildNotificationSubject(i.next());
                    if (o != null) subjects.add(o);
                }
                if (subjects.size() < 1) return;
                SubjectCarrier[] subjectCarriers = new SubjectCarrier[subjects.size()];
                for (int j = 0; j < subjects.size(); j++) {
                    subjectCarriers[j] = new SubjectCarrier(subjects.get(j));
                    subjectCarriers[j].setInheritanceIgnored(ignoreInheritance);
                }
                e = new NotificationEvent(source, zone, subjectCarriers);
            }
            SelectionManager.sharedInstance().notify(e);
        }
    }
}

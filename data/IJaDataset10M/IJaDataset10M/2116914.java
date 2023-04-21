package ch.bbv.notification.client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import ch.bbv.application.Application;
import ch.bbv.dog.DataObject;
import ch.bbv.dog.DataObjectMgr;
import ch.bbv.notification.communication.ClientProfile;
import ch.bbv.notification.communication.EventInfo;

/**
 * the SubjectObserver is the central observer for the client on all DataObjects<br>
 * each client has only one SubjectObserver, these observe all from the client requested DataObjects<br>
 * after the creation, the SubjectObserver creates an jms queue with the name: <code>ClientProfile.getClientIdentification()</code><br>
 * then the client can connect as listener to the queue. 
 * @author Bruno Persi
 */
public class SubjectObserverDirect implements PropertyChangeListener {

    private List<ClientProfile> _profiles;

    /**
	 * create a new SubjectObserver
	 * @param profile the ClientInformation as <code>ClientProfile</code>
	 * @pre profile != null
	 */
    SubjectObserverDirect(ClientProfile profile) {
        assert profile != null;
        _profiles = new ArrayList<ClientProfile>(20);
        if (profile.getClientIdentification().equals("client")) {
            fake(profile);
            return;
        }
        addProfile(profile);
    }

    /**
	 * add a new profile to these Observer
	 * @param profile the ClientInformation as <code>ClientProfile</code>
	 * @pre profile != null
	 */
    public void addProfile(ClientProfile profile) {
        assert profile != null;
        if (_profiles.contains(profile)) {
            return;
        }
        _profiles.add(profile);
        EventInfo event = profile.getInfo();
        int id = event.getSubjectId();
        getDOG(id).addPropertyChangeListener(this);
    }

    private void fake(ClientProfile prof) {
        DirectMutableEventInfo info = new DirectMutableEventInfo(prof.getInfo());
        info.setChangedProperties("prop");
        info.setNewValues("5");
        info.setOriginalValues("2");
        Updateable up = (Updateable) prof.getUpdateable();
        up.update(info);
        for (int i = 1, l = 4; i < l; i++) {
            try {
                Thread.currentThread().sleep(3000);
            } catch (Exception e) {
            }
            info.setChangedProperties(i);
            info.setNewValues(i);
            info.setOriginalValues(i);
            up.update(info);
        }
    }

    private DataObject getDOG(int id) {
        Application app = Application.getApplication();
        DataObjectMgr manager = (DataObjectMgr) app.getDataObjectMgr();
        return manager.getDataObjectHandler().retrieve("orm-dnm", DataObject.class, id);
    }

    /**
	 * remove an profile from these Observer
	 * @param info the Event to be removed as <code>EventInfo</code>
	 * @pre assert info != null
	 */
    public void removeProfile(EventInfo info) {
        assert info != null;
        for (int i = 0, l = _profiles.size(); i < l; i++) {
            if (_profiles.get(i).getInfo().equals(info)) {
                Object o = _profiles.remove(i);
                getDOG(info.getSubjectId()).removePropertyChangeListener(this);
                o = null;
                break;
            }
        }
    }

    /**
	 * @see java.beans.PropertyChangeEvent.propertyChange(PropertyChangeEvent evt)
	 * @pre evt instanceof DataObjectPropertyChangeEvent
	 */
    public void propertyChange(PropertyChangeEvent event) {
        DataObject dog = (DataObject) event.getSource();
        ClientProfile profile = this.getProfile(dog.getId());
        DirectMutableEventInfo info = new DirectMutableEventInfo(profile.getInfo());
        info.setChangedProperties(event.getPropertyName());
        info.setNewValues(event.getNewValue());
        info.setOriginalValues(event.getOldValue());
        Object obj = profile.getUpdateable();
        assert obj instanceof Updateable;
        Updateable o = (Updateable) obj;
        o.update(info);
    }

    private class DirectMutableEventInfo extends EventInfo {

        public DirectMutableEventInfo(EventInfo info) {
            super(info.getSubjectId(), info.getSubjectName(), info.getEventAction());
        }

        @Override
        public void setChangedProperties(Object... properties) {
            super.setChangedProperties(properties);
        }

        @Override
        public void setNewValues(Object... values) {
            super.setNewValues(values);
        }

        @Override
        public void setOriginalValues(Object... values) {
            super.setOriginalValues(values);
        }
    }

    private ClientProfile getProfile(Integer subjectId) {
        for (ClientProfile profile : _profiles) {
            if (profile.getInfo().getSubjectId() == subjectId) {
                return profile;
            }
        }
        throw new IllegalStateException("None ClientProfile registered for BOG: " + subjectId);
    }

    /**
	 * get the count of registered ClientProfile
	 * @return the count of registered ClientProfiles as <code>int</code>
	 */
    public int countClientProfiles() {
        return _profiles.size();
    }

    private EventInfo[] getEventInfos() {
        EventInfo[] infos = new EventInfo[_profiles.size()];
        for (int i = 0, l = _profiles.size(); i < l; i++) {
            infos[i] = _profiles.get(i).getInfo();
        }
        return infos;
    }

    /**
	 * check has the Observer the Event given by argument registered
	 * @param e the Event as <code>EventInfo</code>
	 * @return <code>true</code> if Event found, else <code>false</code>
	 * @pre e != null
	 */
    public boolean hasEvent(EventInfo e) {
        assert e != null;
        EventInfo[] infos = getEventInfos();
        for (EventInfo info : infos) {
            if (info.equals(e)) return true;
        }
        return false;
    }
}

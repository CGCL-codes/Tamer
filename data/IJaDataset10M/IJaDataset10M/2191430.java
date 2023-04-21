package test.granite.ejb3.service;

import java.util.Map;
import org.granite.messaging.service.annotations.RemoteDestination;
import org.granite.tide.data.DataEnabled;
import org.granite.tide.data.DataEnabled.PublishMode;
import test.granite.ejb3.entity.Person;

/**
 * @author Franck WOLFF
 */
@RemoteDestination
@DataEnabled(topic = "addressBookTopic", params = ObserveAllPublishAll.class, publish = PublishMode.ON_SUCCESS)
public interface PeopleService {

    public Map<String, Object> find(Person filter, int first, int max, String[] order, boolean[] desc);
}

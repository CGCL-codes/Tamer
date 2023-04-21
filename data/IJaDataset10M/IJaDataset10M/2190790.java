package gwtm.client.services.tm;

import com.google.gwt.user.client.rpc.AsyncCallback;
import gwtm.client.services.tm.virtual.LocatorVirtual;
import gwtm.client.services.tm.virtual.TopicMapVirtual;
import gwtm.client.services.tm.virtual.TopicVirtual;
import gwtm.client.services.tm.virtual.VTopicVirtuals;

/**
 *
 * @author User
 */
public interface IndexServiceAsync {

    public void getTopicTypes(TopicMapVirtual tmV, AsyncCallback callback);

    public void getTopicsByType(TopicMapVirtual tmV, TopicVirtual typeV, AsyncCallback callback);

    public void getTopicBySubjectIdentifier(TopicMapVirtual tmV, LocatorVirtual locV, AsyncCallback callback);

    public void getTopicBySubjectLocator(TopicMapVirtual tmV, LocatorVirtual locV, AsyncCallback callback);

    public void getTopicsByTypes(TopicMapVirtual tmV, VTopicVirtuals typesV, boolean matchAll, AsyncCallback callback);

    public void getTopicNameTypes(TopicMapVirtual tmV, AsyncCallback callback);

    public void getTopicNamesByType(TopicMapVirtual tmV, TopicVirtual typeV, AsyncCallback callback);

    public void getTopicNamesByValue(TopicMapVirtual tmV, String value, AsyncCallback callback);

    public void getOccurrenceTypes(TopicMapVirtual tmV, AsyncCallback callback);

    public void getOccurrencesByResource(TopicMapVirtual tmV, LocatorVirtual locV, AsyncCallback callback);

    public void getOccurrencesByType(TopicMapVirtual tmV, TopicVirtual typeV, AsyncCallback callback);

    public void getOccurrencesByValue(TopicMapVirtual tmV, String value, AsyncCallback callback);

    public void getAssociationTypes(TopicMapVirtual tmV, AsyncCallback callback);

    public void getAssociationsByType(TopicMapVirtual tmV, TopicVirtual typeV, AsyncCallback callback);

    public void getRoleTypes(TopicMapVirtual tmV, AsyncCallback callback);

    public void getRolesByType(TopicMapVirtual tmV, TopicVirtual typeV, AsyncCallback callback);
}

package gwtm.client.services.tm;

import com.google.gwt.user.client.rpc.RemoteService;
import gwtm.client.services.tm.virtual.NameVirtual;
import gwtm.client.services.tm.virtual.TopicVirtual;

/**
 *
 * @author User
 */
public interface TopicNameService extends RemoteService {

    public void remove(NameVirtual tnV) throws TmException;

    public void addScopingTopic(NameVirtual tnV, TopicVirtual tV) throws TmException;

    public TopicVirtual getReifier(NameVirtual tnV) throws TmException;

    public TopicVirtual getTopic(NameVirtual tnV) throws TmException;

    public TopicVirtual getType(NameVirtual tnV) throws TmException;

    public String getValue(NameVirtual tnV) throws TmException;

    public void removeScopingTopic(NameVirtual tnV, TopicVirtual topic) throws TmException;

    public void setType(NameVirtual tnV, TopicVirtual type) throws TmException;

    public void setValue(NameVirtual tnV, String value) throws TmException;
}

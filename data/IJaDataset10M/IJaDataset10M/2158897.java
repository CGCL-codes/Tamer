package org.tm4j.topicmap.tmdm;

import java.util.Set;
import org.tm4j.net.Locator;

/**
	@author <a href="mailto:xuan--2007.05--org.tm4j.topicmap.tmdm--tm4j.org@public.software.baldauf.org">Xuân Baldauf</a>
*/
public interface TopicName extends ReadableTopicName, Scopeable {

    @TMDM
    @TMAPI
    public void setType(Topic type);

    @TMDM
    @TMAPI
    public Topic getType();

    @TMDM
    @TMAPI
    public void setValue(String value);

    @TMDM
    @TMAPI
    public String getValue();

    /**
		FIXME: it is unclear whether we should expose this method in this interface.
	*/
    @TMDM
    public Topic getParent();
}

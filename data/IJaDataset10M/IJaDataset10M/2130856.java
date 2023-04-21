package org.tmapiutils.query.tolog.parser;

import java.util.List;
import org.tmapiutils.query.tolog.TologParserException;
import org.tmapiutils.query.tolog.TologProcessingException;
import org.tmapiutils.query.tolog.utils.TologContext;
import org.tmapiutils.query.tolog.utils.VariableSet;
import org.tmapi.core.TopicMap;

/**
 *
 * @author Kal Ahmed (kal[at]techquila.com)
 * @author Lars Heuer (heuer[at]semagia.com)
 */
public interface Predicate {

    public void initialise(TopicMap tm) throws TologParserException;

    public VariableSet matches(List params, TologContext context) throws TologProcessingException;

    public void setParameters(List params) throws TologParserException;

    public List getParameters();
}

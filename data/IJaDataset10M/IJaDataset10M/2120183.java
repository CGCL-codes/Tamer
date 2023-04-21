package org.ikross.twitter.task;

import java.io.InputStream;
import java.util.Set;
import org.ikross.twitter.connector.parameter.IParameter;
import org.ikross.twitter.exception.TaskException;

public interface IConnector {

    public void addParameter(String key, IParameter value);

    public Set<String> getKeyParameters();

    public IParameter getParameter(String key);

    public boolean checkParameters();

    public InputStream execute() throws TaskException;

    public IConnector clone();
}

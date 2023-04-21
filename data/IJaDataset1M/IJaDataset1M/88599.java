package openep.logger;

import org.apache.tools.ant.BuildEvent;

public interface EpLogger {

    public void setLogClass(Class clazz);

    public Class getLogClass();

    public void log(String s);

    public void debug(String s);

    public void error(Throwable t, String s);

    public void log(BuildEvent e);
}

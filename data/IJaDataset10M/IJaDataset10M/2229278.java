package org.ourgrid.broker.scheduler.workqueue.xmlcreator;

public class LoggerXMLCreator implements XMLCreatorIF {

    public static final int DEBUG = 0;

    public static final int WARN = 1;

    public static final int ERROR = 2;

    public static final int INFO = 3;

    public static final int TRACE = 4;

    public static final int FATAL = 5;

    /**
	 * <LOGGER message='String' type='int'/>
	 */
    public String getXML(String message, int type) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<LOGGER ");
        buffer.append("message='" + message + "' ");
        buffer.append("type='" + type + "' ");
        buffer.append("/>");
        return buffer.toString();
    }
}

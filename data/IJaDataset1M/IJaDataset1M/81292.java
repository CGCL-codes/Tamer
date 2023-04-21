package org.ogre4j;

import org.xbig.base.*;

public interface ILogManager extends INativeObject, org.ogre4j.IGeneralAllocatedObject, org.ogre4j.ISingleton<org.ogre4j.ILogManager> {

    public interface ILogList extends INativeObject, org.std.Imap<String, org.ogre4j.ILog> {

        /** **/
        public void clear();

        /** **/
        public int count(String key);

        /** **/
        public boolean empty();

        /** **/
        public int erase(String key);

        /** **/
        public int max_size();

        /** **/
        public int size();

        /** **/
        public org.ogre4j.ILog get(String key);

        /** **/
        public void insert(String key, org.ogre4j.ILog value);
    }

    /** 
    Creates a new log with the given name. **/
    public org.ogre4j.ILog createLog(String name, boolean defaultLog, boolean debuggerOutput, boolean suppressFileOutput);

    /** 
    Retrieves a log managed by this class. **/
    public org.ogre4j.ILog getLog(String name);

    /** 
    Returns a pointer to the default log. **/
    public org.ogre4j.ILog getDefaultLog();

    /** 
    Closes and removes a named log. **/
    public void destroyLog(String name);

    /** 
    Closes and removes a log. **/
    public void destroyLog(org.ogre4j.ILog log);

    /** 
    Sets the passed in log as the default log. **/
    public org.ogre4j.ILog setDefaultLog(org.ogre4j.ILog newLog);

    /** 
     a message to the default log. **/
    public void logMessage(String message, org.ogre4j.LogMessageLevel lml, boolean maskDebug);

    /** 
     a message to the default log (signature for backward compatibility). **/
    public void logMessage(org.ogre4j.LogMessageLevel lml, String message, boolean maskDebug);

    /** 
    Get a stream on the default log. **/
    public void stream(org.ogre4j.ILog.IStream returnValue, org.ogre4j.LogMessageLevel lml, boolean maskDebug);

    /** 
    Sets the level of detail of the default log. **/
    public void setLogDetail(org.ogre4j.LoggingLevel ll);
}

package com.dynamide.event;

/** A wrapper class for the source code which contains both the source and the language identifier.
 *  This is used when firing ScriptEvents, and when loading IInterpreter classes.
 */
public class ScriptEventSource {

    public ScriptEventSource() {
    }

    public ScriptEventSource(String name, String body, String source, String language, String ownerID, String resourceName) {
        this.name = name;
        this.body = body;
        this.source = source;
        this.language = language;
        this.ownerID = ownerID;
        this.resourceName = resourceName;
    }

    public String name = "";

    public String body = "";

    public String source = "";

    public String language = "";

    public String ownerID = "";

    /** Will be the xml filename or other resource id if not stored in filesystem. */
    public String resourceName = "";

    public String toString() {
        return "ScriptEventSource:{name:" + name + ",ownerID:" + ownerID + ",language:" + language + ",resourceName:" + resourceName + ",source.length:" + source.length() + "}";
    }
}

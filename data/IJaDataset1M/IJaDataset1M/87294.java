package org.syrup.helpers;

import org.syrup.PTask;

/**
 * A generic PTask Implementation.
 * 
 * @author Robbert van Dalen
 */
public class PTaskImpl implements PTask {

    static final String COPYRIGHT = "Copyright 2005 Robbert van Dalen." + "At your option, you may copy, distribute, or make derivative works under " + "the terms of The Artistic License. This License may be found at " + "http://www.opensource.org/licenses/artistic-license.php. " + "THERE IS NO WARRANTY; USE THIS PRODUCT AT YOUR OWN RISK.";

    private final String parentKey_;

    private final String key_;

    private final boolean or_type_;

    private final String function_class_;

    private final String name_;

    private final String description_;

    private final String parameter_;

    private final String environment_;

    private final boolean done_;

    private final long creation_time_;

    private final long modification_time_;

    private final long modifications_;

    private final boolean executable_;

    private final String worker_;

    private final boolean isParent_;

    /**
     * Constructor for the PTaskImpl object
     * 
     * @param parentKey
     *            The parentKey attribute.
     * @param key
     *            The key attribute.
     * @param or_type
     *            the orType attribute.
     * @param function_class
     *            The functionClass attribute.
     * @param name
     *            The name attribute.
     * @param description
     *            The description attribute.
     * @param environment
     *            The environment attribute.
     * @param parameter
     *            The parameter attribute
     * @param done
     *            The done attribute.
     * @param creation_time
     *            The creationTime attribute.
     * @param modification_time
     *            The modificationTime attribute.
     * @param modifications
     *            The modifications attribute.
     * @param executable
     *            The executable attribute.
     * @param worker
     *            The worker attribute.
     * @param isParent
     *            The isParent attribute.
     */
    public PTaskImpl(String parentKey, String key, boolean or_type, String function_class, String name, String description, String parameter, String environment, boolean done, long creation_time, long modification_time, long modifications, boolean executable, String worker, boolean isParent) {
        parentKey_ = parentKey;
        key_ = key;
        or_type_ = or_type;
        function_class_ = function_class;
        name_ = name;
        description_ = description;
        parameter_ = parameter;
        environment_ = environment;
        done_ = done;
        creation_time_ = creation_time;
        modification_time_ = modification_time;
        modifications_ = modifications;
        executable_ = executable;
        worker_ = worker;
        isParent_ = isParent;
    }

    /**
     */
    public String name() {
        return name_;
    }

    /**
     */
    public String description() {
        return description_;
    }

    /**
     */
    public String environment() {
        return environment_;
    }

    /**
     */
    public String functionClass() {
        return function_class_;
    }

    /**
     */
    public boolean orType() {
        return or_type_;
    }

    /** */
    public String parameter() {
        return parameter_;
    }

    /**
     */
    public String parentKey() {
        return parentKey_;
    }

    /**
     */
    public String key() {
        return key_;
    }

    /**
     */
    public long modifications() {
        return modifications_;
    }

    /**
     */
    public long modificationTime() {
        return modification_time_;
    }

    /**
     */
    public long creationTime() {
        return creation_time_;
    }

    /**
     */
    public boolean executable() {
        return executable_;
    }

    /**
     */
    public boolean done() {
        return done_;
    }

    /**
     */
    public String worker() {
        return worker_;
    }

    /**
     */
    public boolean isParent() {
        return isParent_;
    }

    /**
     */
    public String toString() {
        return "PTask(id=" + key_ + ", p_id=" + parentKey_ + ", isParent=" + isParent_ + ", executable=" + executable_ + ", name=" + name_ + ", environment=" + environment_ + ")";
    }
}

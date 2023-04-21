package com.lepidllama.packageeditor.dbpf2;

import com.lepidllama.packageeditor.dbpf.Tgi;
import com.lepidllama.packageeditor.resources.ResourceSupport;
import com.lepidllama.packageeditor.utility.StringUtils;

/**
 * Represents the TGI structure in dbpf v2 packages. If instance high is not
 * specified it will be defaulted to 0x0, which is consistent with the behaviour
 * observed in the indexes.
 * @author Echo
 *
 */
public class Tgi64 extends ResourceSupport implements Tgi {

    protected long type;

    protected long group;

    protected long instance;

    protected long instancehigh;

    public Tgi64() {
        super();
    }

    public Tgi64(long type, long group, long instance, long instancehigh) {
        this.type = type;
        this.group = group;
        this.instance = instance;
        this.instancehigh = instancehigh;
    }

    public String toString() {
        return "[" + StringUtils.printHex8(type) + ", " + StringUtils.printHex8(group) + ", " + StringUtils.printHex8(instancehigh) + StringUtils.printHex8(instance) + "] ";
    }

    public long getType() {
        return type;
    }

    public long getInstanceHigh() {
        return instancehigh;
    }

    public long getInstance() {
        return instance;
    }

    public long getGroup() {
        return group;
    }

    public void setType(long type) {
        Object old = this.type;
        this.type = type;
        this.propChangeSupp.firePropertyChange("type", old, type);
    }

    public void setInstanceHigh(long instancehigh) {
        Object old = this.instancehigh;
        this.instancehigh = instancehigh;
        this.propChangeSupp.firePropertyChange("group", old, instancehigh);
    }

    public void setInstance(long instance) {
        Object old = this.instance;
        this.instance = instance;
        this.propChangeSupp.firePropertyChange("instance", old, instance);
    }

    public void setGroup(long group) {
        Object old = this.group;
        this.group = group;
        this.propChangeSupp.firePropertyChange("group", old, group);
    }
}

package org.dant.ant.extension.types;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Reference;

public class Host extends DataType {

    protected String hostName;

    protected String ip;

    protected String port;

    protected String id;

    protected String networking;

    protected int highWaterMark = 1;

    protected File basedir;

    public void setBaseDir(File d) {
        this.basedir = d;
    }

    public File getBaseDir() {
        return basedir;
    }

    public void setNetworking(String clsname) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.networking = clsname;
    }

    public void setHighWaterMark(int hwm) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.highWaterMark = hwm;
    }

    public void setRefid(Reference r) throws BuildException {
        super.setRefid(r);
    }

    public void setId(String i) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.id = i;
    }

    public void setIp(String i) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.ip = i;
    }

    public void setPort(String p) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.port = p;
    }

    public void setHostName(String h) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.hostName = h;
    }

    public String getIp() {
        if (isReference()) {
            return getRef(getProject()).getIp();
        } else {
            return this.ip;
        }
    }

    public String getPort() {
        if (isReference()) {
            return getRef(getProject()).getPort();
        } else {
            return this.port;
        }
    }

    public String getHostName() {
        if (isReference()) {
            return getRef(getProject()).getHostName();
        } else {
            return this.hostName;
        }
    }

    public String getNetworking() {
        if (isReference()) {
            return getRef(getProject()).getNetworking();
        } else {
            return this.networking;
        }
    }

    public int getHighWaterMark() {
        if (isReference()) {
            return getRef(getProject()).getHighWaterMark();
        } else {
            return this.highWaterMark;
        }
    }

    public String getId() {
        if (isReference()) {
            return getRef(getProject()).getId();
        } else {
            return this.id;
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("HOST NAME: ");
        sb.append(getHostName());
        sb.append("\t[");
        sb.append(getIp());
        sb.append(":");
        sb.append(getPort());
        sb.append("]\t");
        sb.append("\nnetworking class: ");
        sb.append(networking);
        return sb.toString();
    }

    protected Host getRef(Project p) {
        Object o = ref.getReferencedObject(p);
        return (Host) o;
    }
}

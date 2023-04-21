package net.jsrb.runtime.impl.admin.cmd;

import java.io.Serializable;

public class SvcInfo implements Serializable {

    private static final long serialVersionUID = 4051059617331200157L;

    public String name;

    public String exec;

    public int sid;

    public int callCount;
}

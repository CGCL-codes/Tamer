package simulation;

import java.util.*;
import eu.popeye.middleware.groupmanagement.management.Workgroup;
import eu.popeye.middleware.groupmanagement.management.WorkgroupActionListener;
import eu.popeye.middleware.groupmanagement.management.WorkgroupManager;
import eu.popeye.middleware.groupmanagement.membership.Member;
import eu.popeye.middleware.groupmanagement.membership.MemberActionListener;
import eu.popeye.networkabstraction.communication.basic.util.WorkgroupAlreadyCreatedException;
import eu.popeye.networkabstraction.communication.basic.util.WorkgroupNotExistException;

public class WorkgroupManagerImpl implements WorkgroupManager {

    private SimulationServer server;

    protected Member mySelf;

    public WorkgroupManagerImpl(String username) {
        this.server = SimulationServer.getInstance();
        this.mySelf = this.server.createMember(username);
    }

    public void addWorkgroupActionListener(WorkgroupActionListener listener) {
        this.server.addWorkgroupActionListener(this.mySelf, listener);
    }

    public void removeWorkgroupActionListener(String name, WorkgroupActionListener listener) {
        this.server.removeWorkgroupActionListener(this.mySelf, listener);
    }

    public Workgroup createWorkgroup(String name, MemberActionListener listener) throws WorkgroupAlreadyCreatedException {
        return this.server.createWorkgroup(name, this.mySelf, listener);
    }

    public Workgroup createWorkgroup(String name, String multicastAddress, String port, MemberActionListener listener) throws WorkgroupAlreadyCreatedException {
        return this.server.createWorkgroup(name, this.mySelf, listener);
    }

    public LinkedList getExistingWorkgroupNames() {
        return this.server.getExistingWorkgroupNames();
    }

    public Enumeration getJoinedWorkgroupNames() {
        return this.server.getJoinedWorkgroupNames(this.mySelf);
    }

    public Workgroup getWorkgroup(String name) {
        return this.server.getWorkgroup(name, this.mySelf);
    }

    public LinkedList getWorkgroupActionListeners() {
        return this.server.getWorkgroupActionListeners(this.mySelf);
    }

    public boolean hasJoined(String name) {
        return this.server.hasJoined(name, this.mySelf);
    }

    public Workgroup joinWorkgroup(String name, MemberActionListener listener) throws WorkgroupNotExistException {
        return this.server.joinWorkgroup(name, this.mySelf, listener);
    }

    public void leaveWorkgroup(String name) {
        this.server.leaveWorkgroup(name, this.mySelf);
    }

    public void terminateWorkgroup(String name) {
        this.server.terminateWorkgroup(name);
    }
}

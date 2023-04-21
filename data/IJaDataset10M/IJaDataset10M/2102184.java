package net.sf.gap.impl.users;

import eduni.simjava.Sim_event;
import gridsim.net.Link;
import net.sf.gap.users.AbstractUser;

/**
 * 
 * @author Giovanni Novelli
 */
public abstract class COREUser extends AbstractUser {

    /** Creates a new instance of QAGESAUser */
    public COREUser(String name, Link link, int entityType, boolean trace_flag) throws Exception {
        super(name, link, entityType, trace_flag);
    }

    @Override
    public abstract void processOtherEvent(Sim_event ev);
}

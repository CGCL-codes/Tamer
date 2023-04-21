package org.mortbay.j2ee.session;

import javax.servlet.http.HttpServletRequest;

public interface Store extends Cloneable {

    Manager getManager();

    void setManager(Manager manager);

    void start() throws Exception;

    void stop();

    void destroy();

    void setScavengerPeriod(int secs);

    void setScavengerExtraTime(int secs);

    void setActualMaxInactiveInterval(int secs);

    int getActualMaxInactiveInterval();

    boolean isDistributed();

    String allocateId(HttpServletRequest request) throws Exception;

    void deallocateId(String id) throws Exception;

    State newState(String id, int maxInactiveInterval) throws Exception;

    State loadState(String id) throws Exception;

    void storeState(State state) throws Exception;

    void removeState(State state) throws Exception;

    void scavenge() throws Exception;

    void passivateSession(StateAdaptor sa);

    public Object clone();
}

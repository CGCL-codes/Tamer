package blueprint4j.comm;

import blueprint4j.messages.*;

public interface Directable {

    public Directable getNewInstance();

    public void process(Packet packet, Message data) throws Throwable;

    public String[] getDirection();
}

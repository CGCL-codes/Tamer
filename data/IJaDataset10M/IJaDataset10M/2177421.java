package com.kinglian.learn.reservation;

/**
 * @author kinglian
 *
 */
public interface ReserveService {

    public Server getServer(int serverID);

    public int createServer(Server server);

    public void deleteServer(int serverID);

    public void reserveServer(User user, Server server);

    public void releaseServer(User user, Server server);
}

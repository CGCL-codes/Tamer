package net.davidlauzon.assemblandroid.asyncTask;

public interface ITicketsLoadingListener {

    public void onTicketsLoaded();

    public void ticketsLoadReport(int count, int loadingSeconds, int parsingSeconds, Exception e);
}

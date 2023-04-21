package cs544.group6.server;

/**
 * @author group 6
 * 
 */
public class FTRemoveUserThread extends Thread {

    int clientId;

    /**
	 * @param clientId
	 */
    public FTRemoveUserThread(int clientId) {
        super();
        this.clientId = clientId;
    }

    @Override
    public void run() {
        super.run();
        try {
            sleep(2000);
            FTStore.purgeUser(clientId);
            System.out.println("Purged User with ID " + clientId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

package module.a.lab3.problem6;

public class Pot {

    private int capacity;

    /** Implicit solution decision: only one bee at a time is allowed */
    private int currentLevel;

    private Object fullPotFlag = new Object();

    /** Contains all shared variables */
    public Pot(int capacity) {
        this.capacity = capacity;
        this.currentLevel = 0;
    }

    /**
	 * Tries to add honey
	 * 
	 * @return true if this bee filled up the pot with last portion, else false
	 * @throws ArrayStoreException
	 *             in the event of full pot
	 */
    public synchronized boolean addHoneyPortion() throws ArrayStoreException {
        if (currentLevel >= this.capacity) throw new ArrayStoreException("Pot is full!");
        this.currentLevel++;
        if (currentLevel == this.capacity) return true;
        return false;
    }

    public synchronized void eatAll() {
        this.currentLevel = 0;
    }

    public boolean isFull() {
        return (this.currentLevel >= this.capacity);
    }

    /**
	 * 
	 * While bear is sleeping he is hanging on this semaphore Bear wait() if the
	 * pot is not full. Never called by bee only bear
	 */
    public void waitPotIsFull() {
        synchronized (fullPotFlag) {
            try {
                fullPotFlag.wait();
            } catch (InterruptedException e) {
            }
        }
    }

    public void signalPotIsFull() {
        synchronized (fullPotFlag) {
            fullPotFlag.notify();
        }
    }
}

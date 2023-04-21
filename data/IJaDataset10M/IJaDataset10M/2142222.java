package weekfive;

public class TwentyThreeFour {

    /**
	 * Define each of the following terms. 
	 * 1.thread
	 * 2.multithreading
	 * 3.runnable state
	 * 4.timed waiting state
	 * 5.preemptive scheduling
	 * 6.Runnable interface
	 * 7.signal method of class Condition
	 * 8.producer/consumer relationship
	 * 9.quantum
	 */
    static String term[] = { "thread", "multithreading", "runnable state", "timed waiting state", "preemptive scheduling", "Runnable interface", "signal method of class Condition", "producer/consumer relationship", "quantum" };

    static String definition[] = { "a basic processing unit to which an operating system allocates processor time.", "allows two parts of the same program to run concurrently", "a thread which is executing its task", "a thread which waits a specific period of time for an event to occur", "allows higher priority entering the ready state to interrupt operating threads.", "interface which has the run method needed to implement threads.", "call from a running thread that has completed its task which allows another thread to take the lock", "the producer portion of an application generates data and stores it in a shared object, and the consumer portion of an application reads data from the shared object.", "small amount of porcessor time allocated to a thread, also called timeshare." };

    public static void answer() {
        System.out.println("+Term+              +Definition+");
        for (int i = 0; i < 9; i++) {
            System.out.println(i + 1 + "." + term[i] + ":     " + definition[i]);
        }
    }

    public static void main(String[] args) {
        answer();
    }
}

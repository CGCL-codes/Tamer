package sparrow.applications.test;

import java.net.URL;
import sparrow.driver.MasterWorker;
import sparrow.util.CompletedTask;
import sparrow.util.InitData;

public class TestDriver extends MasterWorker {

    int taskCounter_;

    int totalTasks_;

    int initialTasks_;

    /**
   * Constructor
   * @param file
   * @param port
   */
    public TestDriver(int port, String IP, int initialTasks, int totalTasks) {
        super(port, IP);
        initialTasks_ = initialTasks;
        totalTasks_ = totalTasks;
    }

    /**
   * Sets the init data
   */
    public InitData setInitData() {
        URL[] urls = null;
        return new TestInitData(urls);
    }

    /**
   * Creates the list of initial tasks
   */
    public void CreateInitialTasks() {
        double[] array = { 3, 4, 5 };
        for (int i = 0; i < initialTasks_; i++) sharedObjects_.nonScheduledTask_.add(new TestTask(array));
    }

    /**
   * Acts when a new task has been computed
   */
    public void newCompletedTask(CompletedTask completedTask) {
        TestCompletedTask task = (TestCompletedTask) completedTask;
        System.out.println("Driver: received task. Value = " + task.value_);
        taskCounter_++;
        double[] array = { 6, 6, 6 };
        if (taskCounter_ < totalTasks_) {
            sharedObjects_.nonScheduledTask_.add(new TestTask(array));
        } else sharedObjects_.terminate_ = true;
    }

    /**
   * Print the results
   */
    public void printResults() {
        System.out.println("END OF DRIVER");
    }

    /**
   * Main method
   * @param args
   */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Error: invalid number of arguments");
            System.out.println("Usage: java sparrow.test.TestDriver port IP");
            System.exit(-1);
        }
        int initialTasks = 3;
        int totalTasks = 180000;
        TestDriver driver = new TestDriver(new Integer(args[0]), args[1], initialTasks, totalTasks);
        driver.execute();
    }
}

package Approach_3;

public class Computer {

    private final CPU cpu;

    private String name;

    public Computer(String n) {
        name = n;
        cpu = new CPU("Intel");
    }

    public boolean start() {
        System.out.println("CPU activated");
        return true;
    }

    public boolean executeTask() {
        System.out.println("CPU is Executing the Task");
        return true;
    }

    public boolean stop() {
        System.out.println("CPU is stopped");
        return true;
    }
}

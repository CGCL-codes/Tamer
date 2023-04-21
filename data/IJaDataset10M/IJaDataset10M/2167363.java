package uut.replaceMethod;

import uut.Monitor;

public class Case implements Runnable {

    public void toReplace1() {
        Monitor.flag = 1;
    }

    public void run() {
        switch(Monitor.number) {
            default:
                toReplace1();
        }
    }
}

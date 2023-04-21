package biz.xl.test.multithread;

import java.util.ArrayList;

public class SynSource {

    private ArrayList source = new ArrayList(10);

    public synchronized void push(char in) {
        if (source.size() < 10) {
            source.add(new Character(in));
            this.notify();
        } else {
            try {
                this.wait();
            } catch (Exception e) {
            }
        }
    }
}

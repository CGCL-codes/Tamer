package cn.edu.thss.iise.beehivez.server.basicprocess;

public class NumberGenerator {

    private int id = 0;

    public synchronized int next() {
        return id++;
    }
}

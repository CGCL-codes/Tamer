package com.winiex.datastructure;

public class SqQueueTestWithMain {

    public static void main(String[] args) {
        SqQueue<Integer> queue = new SqQueue<Integer>(6);
        try {
            queue.EnQueue(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

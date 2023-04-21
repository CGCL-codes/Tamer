package com.bobo._04stackAndQueue;

/**不通过当前数据项来构建队列*/
public class _007queue2 {

    private int maxSize;

    private String[] queArray;

    private int front;

    private int rear;

    public _007queue2(int s) {
        maxSize = s + 1;
        queArray = new String[maxSize];
        front = 0;
        rear = -1;
    }

    public void insert(String j) {
        if (rear == maxSize - 1) {
            rear = -1;
        }
        queArray[++rear] = j;
    }

    public String remove() {
        String temp = queArray[front++];
        if (front == maxSize) {
            front = 0;
        }
        return temp;
    }

    public String peek() {
        return queArray[front];
    }

    public boolean isEmpty() {
        return (rear + 1 == front || front + maxSize - 1 == rear);
    }

    public boolean isFull() {
        return (rear + 2 == front || front + maxSize - 2 == rear);
    }

    public int size() {
        if (rear >= front) {
            return rear - front + 1;
        } else {
            return (maxSize - front) + (rear + 1);
        }
    }
}

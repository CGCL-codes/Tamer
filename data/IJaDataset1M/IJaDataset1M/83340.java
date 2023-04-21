package com.dekaru.data;

class Node {

    private Object data;

    private Node next;

    Node(Object data) {
        this(data, null);
    }

    Node(Object data, Node next) {
        this.data = data;
        this.next = next;
    }

    Object getData() {
        return data;
    }

    Node getNext() {
        return next;
    }

    void setData(Object data) {
        this.data = data;
    }

    void setNext(Node next) {
        this.next = next;
    }
}

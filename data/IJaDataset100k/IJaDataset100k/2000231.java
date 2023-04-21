package com.adaptivehuffman;

public class Node {

    public Node left;

    public Node right;

    public Node parent;

    public String letter;

    public int frequency;

    Node(String letter, int frequency) {
        this.frequency = frequency;
        this.letter = letter;
    }
}

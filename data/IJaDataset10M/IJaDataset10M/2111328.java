package com.vayoodoot.util;

import org.apache.log4j.Logger;
import com.vayoodoot.packet.PacketListener;

/**
 * This class is an implementation of a queue, I could have used an existng java datastructure,
 * but what the heck, when am I going to put my Computer Sceince degree in to use.
 */
public class Queue {

    private Node head = null;

    private Node tail = null;

    private static Logger logger = Logger.getLogger(PacketListener.class);

    private volatile int currentCount = 0;

    public Queue() {
    }

    public synchronized void add(Object obj) {
        currentCount++;
        logger.debug("Adding packet to Queue:" + currentCount);
        Node node = new Node();
        node.setObject(obj);
        if (head == null) head = node;
        if (tail == null) tail = node; else {
            tail.nextLink = node;
            node.prevLink = tail;
            tail = node;
        }
    }

    public synchronized Object getNextObject() {
        if (head == null) {
            logger.info("Queue Returning Null:" + currentCount);
            return null;
        }
        Node node = head;
        head = (Node) node.nextLink;
        if (head != null) head.prevLink = null;
        Object obj = node.getObject();
        node.setObject(null);
        node.setNextLink(null);
        node.setPrevLink(null);
        node = null;
        currentCount--;
        return obj;
    }

    private static class Node {

        Object object = null;

        Object nextLink = null;

        Object prevLink = null;

        public Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }

        public Object getNextLink() {
            return nextLink;
        }

        public void setNextLink(Object nextLink) {
            this.nextLink = nextLink;
        }

        public Object getPrevLink() {
            return prevLink;
        }

        public void setPrevLink(Object prevLink) {
            this.prevLink = prevLink;
        }
    }
}

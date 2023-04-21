package org.stumeikle.Yggdrasil;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * world manager thread
 * spawned by the Yggdrasil master thread to manage the world
 * (super user modifyable for different worlds o course)
 *
 * stumeikle 20010403
 * Copyright (C) Stuart Meikle 2001
 *
 *
 * 2006-halloween: making this a little more generic now... world needs to provide
 * adequate interface for body virtual and real worlds. 
 */
public class World extends Thread {

    private static int iMaxNumBodies;

    /** max number of Bodys in the world*/
    private Vector<Body> iCreatureWrappers;

    private Version iTimeStamp;

    /** Time stamp for data going in/out of the world */
    private long iWorldTime;

    /** the world clock */
    private FountainOfLife iXtIncomingCreatures;

    /** Information about new incoming creatures */
    private BodyFactory iBodyFactory;

    /** user installable means to create specific body types */
    static Random rand;

    /** Constructor. 
     * @param e fountain of life structure, passed in from Yggdrasil.
     * This constructor is typically called once and once only from Yggdrasil class. 
     * Runs in its own thread. 
     */
    public World() {
        iMaxNumBodies = 100;
        iBodyFactory = null;
    }

    public void setFountainOfLife(FountainOfLife e) {
        iXtIncomingCreatures = e;
    }

    public void setBodyFactory(BodyFactory b) {
        iBodyFactory = b;
    }

    public void setMaxNumBodies(int i) {
        iMaxNumBodies = i;
    }

    public void init() {
        FileInputStream ipfs;
        ObjectInputStream ois;
        iCreatureWrappers = new Vector<Body>(iMaxNumBodies);
        iTimeStamp = new Version(1);
        iWorldTime = 0;
        rand = new Random();
    }

    /** Run the world , enter the main eventloop
     */
    public void run() {
        System.out.println("WORLD: hello errr world!");
        do {
            iWorldTime++;
            iTimeStamp.setValue(iTimeStamp.getValue() + 1);
            checkForNewConnections();
            for (int i = 0; i < iCreatureWrappers.size(); i++) {
                Body b = (Body) iCreatureWrappers.get(i);
                if (b.getUpdateTime() > iWorldTime) continue;
                b.generateSenses();
                b.exportSenses();
                b.importActions();
                b.updateBodyPhysics();
                b.setNextUpdateTime(iWorldTime);
                if (b.deadAndBuried()) {
                    iCreatureWrappers.remove(b);
                    System.out.println("Yggdrasil:World: creature " + b + " died. ");
                }
            }
            updateWorld();
            try {
                Thread.sleep(10);
            } catch (Exception e) {
            }
        } while (true);
    }

    /** check for new connections and add to the world. 
     *  also remove bodies from disconnected parties
     */
    void checkForNewConnections() {
        if (iBodyFactory == null) return;
        if (iXtIncomingCreatures.newCreature()) {
            Random rand = new Random();
            String t = iXtIncomingCreatures.getNewCreatureType();
            Life l = iXtIncomingCreatures.getLife();
            Body b = null;
            System.out.println("Yggdrasil:World: Client of type " + t + " connecting");
            b = iBodyFactory.createNewBody(t);
            if (b != null) {
                b.setLife(l);
                iXtIncomingCreatures.acknowledge();
                iCreatureWrappers.add(b);
            }
        }
    }

    public void updateWorld() {
    }
}

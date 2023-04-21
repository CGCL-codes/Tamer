package com.calefay.utils;

import com.calefay.exodusDefence.EDParticleManager;
import com.jme.scene.Node;

public class GameWorldInfo {

    private static GameWorldInfo currentWorldInfo = null;

    private Node rootNode = null;

    private GameEventHandler masterEventHandler = null;

    private EDParticleManager particleManager = null;

    public GameWorldInfo(Node root, GameEventHandler geHandler) {
        rootNode = root;
        masterEventHandler = geHandler;
        currentWorldInfo = this;
    }

    /** sets the instance which will be returned by GameWorldInfo.getGameWorldInfo() */
    public static void setDefault(GameWorldInfo defaultWorldInfo) {
        currentWorldInfo = defaultWorldInfo;
    }

    public static GameWorldInfo getGameWorldInfo() {
        return currentWorldInfo;
    }

    public void setRootNode(Node root) {
        this.rootNode = root;
    }

    public Node getRootNode() {
        return rootNode;
    }

    public GameEventHandler getEventHandler() {
        return masterEventHandler;
    }

    public void setEventHandler(GameEventHandler g) {
        masterEventHandler = g;
    }

    public EDParticleManager getParticleManager() {
        return particleManager;
    }

    public void setParticleManager(EDParticleManager particleManager) {
        this.particleManager = particleManager;
    }
}

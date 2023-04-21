package com.riseOfPeople.network.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import com.riseOfPeople.gameObjects.AbstractGameObject;
import com.riseOfPeople.gameObjects.AbstractGameObjectData;

/**
 * @author Cor
 *
 */
public class ClientWriter implements Runnable {

    private Socket socket;

    private ObjectOutputStream objectOutputStream;

    private OutputStream outputStream;

    private boolean inGame;

    private HashMap<Integer, AbstractGameObject> playerGameObjects;

    /**
	 * function ClientWriter
	 * @param socket, {@link Socket}
	 */
    public ClientWriter(Socket socket) {
        this.socket = socket;
        try {
            outputStream = this.socket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
            inGame = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * function run, implemented from the Runnable interface
	 */
    @Override
    public void run() {
        while (inGame) {
            if (playerGameObjects != null) {
                writeObjects();
            }
        }
    }

    private synchronized Collection<AbstractGameObject> getGameObjects() {
        return playerGameObjects.values();
    }

    /**
	 * function writeObjects
	 */
    private void writeObjects() {
        Collection<AbstractGameObject> collection = getGameObjects();
        AbstractGameObject[] gameObjects = collection.toArray(new AbstractGameObject[0]);
        if (collection.size() > 0) {
            try {
                ArrayList<AbstractGameObjectData> data = new ArrayList<AbstractGameObjectData>();
                for (int i = 0; i < gameObjects.length; i++) {
                    AbstractGameObject obj = gameObjects[i];
                    data.add(obj.getObjectData());
                }
                objectOutputStream.writeObject(data);
                objectOutputStream.reset();
                objectOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * function setPlayerObjects
	 * @param gameObjects, {@link HashMap}
	 */
    public synchronized void setPlayerObjects(HashMap<Integer, AbstractGameObject> gameObjects) {
        if (this.playerGameObjects == null) this.playerGameObjects = new HashMap<Integer, AbstractGameObject>();
        for (AbstractGameObject gameObject : gameObjects.values().toArray(new AbstractGameObject[0])) {
            this.playerGameObjects.put(gameObject.getObjectID(), gameObject);
        }
    }
}

package com.monad.homerun.rmictrl;

import java.rmi.RemoteException;
import com.monad.homerun.view.Scene;
import com.monad.homerun.view.Album;
import com.monad.homerun.view.Snapshot;

/**
 *  SceneCtrl interface exposes methods for manipulating scenes and albums.
 */
public interface SceneCtrl extends ServerCtrl {

    String[] getSceneCategories() throws RemoteException;

    String[] getSceneNames(String category) throws RemoteException;

    Scene getScene(String category, String sceneName) throws RemoteException;

    boolean addScene(Scene scene) throws RemoteException;

    boolean updateScene(Scene scene) throws RemoteException;

    boolean removeScene(Scene scene) throws RemoteException;

    void captureScene(String category, String sceneName) throws RemoteException;

    long[] getSnapshotTimes(String category, String sceneName) throws RemoteException;

    Snapshot getSnapshot(String category, String sceneName, long time) throws RemoteException;

    String[] getViewTypes() throws RemoteException;

    String[] getImageNames(String category) throws RemoteException;

    byte[] getImageBytes(String category, String imageName) throws RemoteException;

    byte[] getModelTrail(String domain, String object, String model, String options) throws RemoteException;

    String[] getAlbumNames() throws RemoteException;

    Album getAlbum(String albumName) throws RemoteException;

    boolean addAlbum(Album album) throws RemoteException;

    boolean updateAlbum(Album album) throws RemoteException;

    boolean removeAlbum(Album album) throws RemoteException;
}

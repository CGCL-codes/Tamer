package models;

import java.io.File;

public interface ILoadListener {

    public O3dFile getModelFile();

    public void loadStarted(File f);

    public void loadFinished(VisibleObject o);

    public void loadCancelled();
}

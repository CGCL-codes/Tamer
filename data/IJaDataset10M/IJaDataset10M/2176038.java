package net.sf.picasto;

import java.io.*;

public interface PrivateData {

    public abstract void loadPrivateData(InputStream is);

    public abstract void savePrivateData(OutputStream os);
}

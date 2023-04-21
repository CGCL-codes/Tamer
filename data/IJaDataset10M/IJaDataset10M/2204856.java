package hu.scytha.plugin;

import java.io.*;
import java.io.FileNotFoundException;
import java.io.InputStream;

public interface IPluginFile {

    /**
    * 
    * @return the stream
    * @throws FileNotFoundException
    * @throws IOException
    */
    public InputStream getInputStream() throws FileNotFoundException, IOException;

    public String getName();

    public long getSize();
}

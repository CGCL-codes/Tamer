package propres;

import java.io.*;

abstract class g_disco {

    public abstract Object llegir_objecte() throws IOException, ClassNotFoundException;

    abstract void guardar_objecte(Object o) throws IOException;

    public abstract Object llegir_objecte2(String path) throws IOException, ClassNotFoundException;

    abstract void guardar_objecte2(Object o, String path) throws IOException;
}

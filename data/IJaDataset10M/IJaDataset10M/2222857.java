package drjava.freegle;

import java.io.*;
import java.util.*;

public interface Index {

    public void addOrReplaceDocument(FDocument fd, int id, Vector uris) throws IOException;

    public void deleteDocument(int id) throws IOException;

    public void addDocument(FDocument fd, int id, Vector uris) throws IOException;

    public void close(boolean optimize) throws IOException;
}

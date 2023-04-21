package net.sf.semanticdebug.examples;

import java.io.RandomAccessFile;

/**
 * @author DS
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface Container {

    public void add(int num) throws Exception;

    public int get();

    public int[] getAll();

    public void saveToFile(RandomAccessFile filename);

    public void loadFromFile(RandomAccessFile filename);
}

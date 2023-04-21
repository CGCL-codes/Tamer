package com.ubu.psg.loader;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Mr.Athapol Chainram
 */
public interface FileLoader {

    public void LoadFile(String path);

    public ArrayList<File> getFile();
}

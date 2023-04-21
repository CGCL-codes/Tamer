package com.google.code.javastorage.cli.script;

import com.google.code.javastorage.cli.IOManager;
import com.google.code.javastorage.cli.Main;

/**
 * 
 * @author thomas.scheuchzer@gmail.com
 * 
 */
public interface Script extends Runnable {

    void setIOManager(IOManager ioManager);

    void setMain(Main main);
}

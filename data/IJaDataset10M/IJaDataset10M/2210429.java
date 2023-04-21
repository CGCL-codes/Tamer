package com.daffodilwoods.daffodildb.server.datasystem.persistentsystem;

import java.io.*;
import com.daffodilwoods.daffodildb.server.datasystem.persistentsystem.versioninfo.*;
import com.daffodilwoods.database.resource.*;

public class PowerFileFactory extends PowerFileFactoryUpto3_2 {

    public PowerFileFactory(String databaseUrl1) {
        super(databaseUrl1);
    }

    /**
   * It returns power file for the database.
   * Firstly we try to get from free powerFiles if we doesn't found than we makes a new one.
   *
   * @param clusterSize int            - cluster size of the database.
   * @param isReadOnlyMode boolean
   * @throws DException
   * @return RandomAccessFile          - power file to be returned.
   */
    public synchronized RandomAccessFile getFile(int clusterSize, boolean isReadOnlyMode) throws DException {
        if (freePowerfileList.size() > 0) {
            RandomAccessFile raf = (RandomAccessFile) freePowerfileList.remove(freePowerfileList.size() - 1);
            return raf;
        }
        try {
            File ff = new File(databaseURL);
            File ff1 = ff.getParentFile();
            ff = new File(ff1, "_" + name + "_PowerFile.log");
            RandomAccessFile powerFile = null;
            powerFile = new RandomAccessFile(ff, "rw");
            powerFile.seek(0);
            powerFile.writeByte(1);
            powerFile.setLength(clusterSize * 5 + 1024);
            list.add(powerFile);
            name++;
            return powerFile;
        } catch (Exception fnfe) {
            fnfe.printStackTrace();
            throw new DException("DSE2025", new Object[] { fnfe.getMessage() });
        }
    }
}

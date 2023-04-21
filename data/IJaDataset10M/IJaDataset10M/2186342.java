package com.daffodilwoods.daffodildb.server.datasystem.persistentsystem;

import com.daffodilwoods.daffodildb.server.datasystem.interfaces.*;
import com.daffodilwoods.daffodildb.utils.byteconverter.*;
import com.daffodilwoods.database.resource.*;
import java.io.*;
import java.sql.*;
import java.lang.ref.*;
import com.daffodilwoods.daffodildb.server.sql99.utils._Reference;
import com.daffodilwoods.daffodildb.server.datasystem.interfaces._DatabaseUser;
import com.daffodilwoods.daffodildb.server.datasystem.persistentsystem.versioninfo.VersionHandler;
import com.daffodilwoods.daffodildb.server.datasystem.btree.*;

/**
 * It is used to retieve and update blob column values, it reads and writes bytes in Cluster
 */
public class DBlob {

    /**
    * Length of data of blob object's Stream.
    */
    private int lengthOfBlob = -1;

    /**
    * First cluster used to store it.
    */
    private WeakReference startCluster;

    /**
    * First cluster address
    */
    private ClusterCharacteristics startClusterCharacteristics;

    /**
    * To get Cluster for read and write
    */
    private LobManager lobManager;

    /**
    * Record number in first cluster from where it is to be started to save.
    */
    short recordNumber = -1;

    /**
    * Database properties for getting any information like cluster size etc.
    */
    DatabaseProperties databaseProperties;

    /**
    * It is used to get constants which are changed according to version as LOBACTIVEBYTE etc.
    */
    VersionHandler versionHandler;

    DBlob(ClusterCharacteristics cc, LobManager lobManager0, short recordNumber0) {
        startClusterCharacteristics = cc;
        startCluster = new WeakReference(null);
        lobManager = lobManager0;
        recordNumber = recordNumber0;
        versionHandler = lobManager.getVersionHandler();
    }

    /**
   *
   * @throws DException
   */
    protected void initialize() throws DException {
        Cluster cluster = startCluster == null ? null : (Cluster) startCluster.get();
        if (cluster == null && startClusterCharacteristics != null) cluster = lobManager.getCluster(startClusterCharacteristics);
        if (lengthOfBlob == -1 && startCluster != null) {
            int startPointer = cluster.getStartPointerOfRecord(recordNumber);
            startPointer += 2;
            lengthOfBlob = CCzufDpowfsufs.getIntValue(cluster.getBytes(), startPointer);
            if (lengthOfBlob < 0) {
                ;
            }
        } else {
            if (lengthOfBlob < 0) {
                ;
            }
        }
    }

    public long length() throws DException {
        initialize();
        return lengthOfBlob;
    }

    /**
    * returns bytes from specified position and of given length
    *
    * @param pos position from where string is required
    * @param length length of string which is required
    *
    * @return bytes from specified position and of given length
    *
    * @throws Exception If position is more than total length of bytes inserted
    *
    */
    public byte[] getBytes(long pos, int length) throws DException {
        if (length == 0) return new byte[0];
        initialize();
        if (pos < 1) throw new DException("DSE798", null);
        if (pos > lengthOfBlob) throw new DException("DSE939", new Object[] { new Long(pos), new Integer(lengthOfBlob) });
        if ((pos + length - 1) > lengthOfBlob) throw new DException("DSE836", new Object[] { new Integer(length), new Long(pos), new Integer(lengthOfBlob) });
        return retrieveBlobBytes((int) pos - 1, length);
    }

    /**
    * Update bytes from given position
    *
    * @param user To perform write operations on cluster
    * @param pos  position from where bytes has to update
    * @param bytes new bytes
    *
    * @return number of bytes which are updated
    *
    * @throws Exception If position is more than total length of bytes inserted

    */
    public int setBytes(_DatabaseUser user, long pos, byte[] bytes) throws DException {
        initialize();
        if (pos > lengthOfBlob) throw new DException("DSE939", new Object[] { new Long(pos), new Integer(lengthOfBlob) });
        updateBlobClob(user, (int) pos, bytes);
        return bytes.length;
    }

    private byte[] retrieveBlobBytes(int position, int lengthOfBytes) throws DException {
        try {
            lobManager.lock.lockRowForRead(lobManager.monitor);
            byte[] bytes = new byte[lengthOfBytes];
            Cluster cluster = startCluster == null ? null : (Cluster) startCluster.get();
            if (cluster == null && startClusterCharacteristics != null) cluster = lobManager.getCluster(startClusterCharacteristics);
            if (cluster == null) return new byte[0];
            byte[] clusterBytes = cluster.getBytes();
            short recordToRetrieve = recordNumber;
            short startPointer = cluster.getStartPointerOfRecord(recordToRetrieve);
            if (clusterBytes[startPointer] == versionHandler.DELETE) throw StaticExceptions.RECORD_DELETED_EXCEPTION;
            startPointer++;
            boolean isFull = clusterBytes[startPointer++] == versionHandler.FULL;
            startPointer += 4;
            if (isFull) {
                try {
                    System.arraycopy(clusterBytes, startPointer + position, bytes, 0, lengthOfBytes);
                } catch (ArrayIndexOutOfBoundsException ex) {
                    throw ex;
                }
                return bytes;
            }
            short numberOfBytesWrittenInThisCluster = -1;
            while (true) {
                numberOfBytesWrittenInThisCluster = getNumberOfBytesWrittenInThisCluster(cluster, recordToRetrieve);
                if (position > numberOfBytesWrittenInThisCluster) {
                    position = position - numberOfBytesWrittenInThisCluster;
                    cluster = lobManager.getCluster(cluster.getNextClusterCharacteristics());
                    recordToRetrieve = 1;
                } else break;
            }
            int sizeWhichCanAdjust = numberOfBytesWrittenInThisCluster - position;
            sizeWhichCanAdjust = sizeWhichCanAdjust > lengthOfBytes ? lengthOfBytes : sizeWhichCanAdjust;
            int place = 0;
            while (true) {
                clusterBytes = cluster.getBytes();
                startPointer = cluster.getStartPointerOfRecord(recordToRetrieve);
                startPointer += versionHandler.LOBACTIVEBYTE + versionHandler.LOBFULLBYTE + versionHandler.LOBRECORDLENGTH;
                System.arraycopy(clusterBytes, startPointer + position, bytes, place, sizeWhichCanAdjust);
                place += sizeWhichCanAdjust;
                if (sizeWhichCanAdjust >= lengthOfBytes) break; else {
                    lengthOfBytes -= sizeWhichCanAdjust;
                    cluster = lobManager.getCluster(cluster.getNextClusterCharacteristics());
                    recordToRetrieve = 1;
                    numberOfBytesWrittenInThisCluster = getNumberOfBytesWrittenInThisCluster(cluster, recordToRetrieve);
                    position = 0;
                    sizeWhichCanAdjust = numberOfBytesWrittenInThisCluster > lengthOfBytes ? lengthOfBytes : numberOfBytesWrittenInThisCluster;
                }
            }
            return bytes;
        } finally {
            lobManager.lock.releaseRowForRead(lobManager.monitor);
        }
    }

    private short getNumberOfBytesWrittenInThisCluster(Cluster cluster, short recordToRetrieve) throws DException {
        short numberOfBytesWrittenInThisCluster = -1;
        byte[] clusterBytes = cluster.getBytes();
        if (recordToRetrieve == cluster.actualRecordCount) {
            numberOfBytesWrittenInThisCluster = (short) (CCzufDpowfsufs.getShortValue(clusterBytes, 0) - cluster.getStartPointerOfRecord(recordToRetrieve) - versionHandler.LOBRECORDLENGTH - versionHandler.LOBACTIVEBYTE - versionHandler.LOBFULLBYTE);
        } else {
            numberOfBytesWrittenInThisCluster = (short) (cluster.getStartPointerOfRecord((short) (recordToRetrieve + 1)) - cluster.getStartPointerOfRecord(recordToRetrieve) - versionHandler.LOBRECORDLENGTH - versionHandler.LOBACTIVEBYTE - versionHandler.LOBFULLBYTE);
        }
        return numberOfBytesWrittenInThisCluster;
    }

    private void updateBlobClob(_DatabaseUser user, int position, byte[] bytes) throws DException {
        Cluster cluster = startCluster == null ? null : (Cluster) startCluster.get();
        if (cluster == null && startClusterCharacteristics != null) cluster = lobManager.getCluster(startClusterCharacteristics);
        short recordToRetrieve = recordNumber;
        int start = 0;
        int lengthOfBytes = bytes.length;
        int totalLength = CCzufDpowfsufs.getIntValue(cluster.getBytes(), cluster.getStartPointerOfRecord(recordToRetrieve) + versionHandler.LOBACTIVEBYTE + versionHandler.LOBFULLBYTE);
        Cluster temp = cluster;
        int i = 0;
        int writtenSize = 0;
        while (true) {
            int writtenNumberOfBytesInThisCluster = getNumberOfBytesWrittenInThisCluster(temp, recordToRetrieve);
            int range = getFreeSpaceForUpdate(temp, recordToRetrieve);
            if (position < writtenNumberOfBytesInThisCluster) {
                int startPointer = cluster.getStartPointerOfRecord(recordToRetrieve) + versionHandler.LOBACTIVEBYTE + versionHandler.LOBFULLBYTE + versionHandler.LOBRECORDLENGTH + position;
                int rw = position + lengthOfBytes <= range ? lengthOfBytes : range - position;
                lengthOfBytes -= rw;
                writtenSize += rw;
                byte[] clusterBytes = new byte[rw];
                System.arraycopy(bytes, start, clusterBytes, 0, rw);
                temp.updateBytes(startPointer, clusterBytes);
                if (writtenSize != totalLength) {
                    if (rw + position > writtenNumberOfBytesInThisCluster) {
                        short insertableAddress = (short) (rw + startPointer);
                        temp.updateBytes(0, CCzufDpowfsufs.getBytes(insertableAddress));
                    }
                }
                if (lengthOfBytes <= 0) break;
                start += rw;
                i++;
            }
            ClusterCharacteristics next = getNextClusterCharacteristics(temp);
            temp = lobManager.getDatabase().getClusterForWrite(user, next);
            recordToRetrieve = 1;
            position = position > writtenNumberOfBytesInThisCluster ? position - writtenNumberOfBytesInThisCluster : 0;
        }
    }

    ClusterCharacteristics getNextClusterCharacteristics(Cluster cc) throws DException {
        int startAddress = CCzufDpowfsufs.getIntValue(cc.getBytes(), databaseProperties.CLUSTERSIZE - 2 * versionHandler.LENGTH);
        return startAddress == 0 ? null : new ClusterCharacteristics(startAddress, false);
    }

    public void setDatabaseProperties(DatabaseProperties dp) {
        databaseProperties = dp;
    }

    public LobManager getLobManager() {
        return lobManager;
    }

    public int getFreeSpaceForUpdate(Cluster cluster, short recordNumber) throws DException {
        return (databaseProperties.CLUSTERSIZE - cluster.getStartPointerOfRecord(recordNumber) - versionHandler.NEWADDRESSLENGTH - cluster.actualRecordCount * versionHandler.LENGTH - versionHandler.LOBACTIVEBYTE - versionHandler.LOBFULLBYTE - versionHandler.LOBRECORDLENGTH - versionHandler.LENGTH - 1);
    }

    public void truncate(long len) throws DException {
        initialize();
        if (len > lengthOfBlob) throw new DException("DSE1017", new Object[] { new Long(len), new Integer(lengthOfBlob) });
        if (lengthOfBlob == len) return;
        updateLengthOfBlobClob(len);
    }

    void updateLengthOfBlobClob(long truncatedLength) throws DException {
        initialize();
        Cluster cluster = startCluster == null ? null : (Cluster) startCluster.get();
        if (cluster == null && startClusterCharacteristics != null) cluster = lobManager.getCluster(startClusterCharacteristics);
        byte[] bytes = CCzufDpowfsufs.getBytes(new Long(truncatedLength));
        cluster.updateBytes(versionHandler.ADDRESSLENGTH, bytes);
    }
}

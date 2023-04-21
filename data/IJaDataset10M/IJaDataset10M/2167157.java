package org.apache.hadoop.hdfs.server.protocol;

import java.io.IOException;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.apache.hadoop.hdfs.server.namenode.CheckpointSignature;
import org.apache.hadoop.ipc.VersionedProtocol;

/*****************************************************************************
 * Protocol that a secondary NameNode uses to communicate with the NameNode.
 * It's used to get part of the name node state
 *****************************************************************************/
public interface NamenodeProtocol extends VersionedProtocol {

    /**
   * 2: Added getEditLogSize(), rollEditLog(), rollFSImage().
   */
    public static final long versionID = 2L;

    /** Get a list of blocks belonged to <code>datanode</code>
    * whose total size is equal to <code>size</code>
   * @param datanode  a data node
   * @param size      requested size
   * @return          a list of blocks & their locations
   * @throws RemoteException if size is less than or equal to 0 or
                                   datanode does not exist
   */
    public BlocksWithLocations getBlocks(DatanodeInfo datanode, long size) throws IOException;

    /**
   * Get the size of the current edit log (in bytes).
   * @return The number of bytes in the current edit log.
   * @throws IOException
   */
    public long getEditLogSize() throws IOException;

    /**
   * Closes the current edit log and opens a new one. The 
   * call fails if the file system is in SafeMode.
   * @throws IOException
   * @return a unique token to identify this transaction.
   */
    public CheckpointSignature rollEditLog() throws IOException;

    /**
   * Rolls the fsImage log. It removes the old fsImage, copies the
   * new image to fsImage, removes the old edits and renames edits.new 
   * to edits. The call fails if any of the four files are missing.
   * @throws IOException
   */
    public void rollFsImage() throws IOException;
}

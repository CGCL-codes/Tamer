package org.ikasan.connector.basefiletransfer.outbound.command;

import javax.resource.ResourceException;
import org.apache.log4j.Logger;
import org.ikasan.connector.ConnectorException;
import org.ikasan.connector.base.command.ExecutionContext;
import org.ikasan.connector.base.command.ExecutionOutput;
import org.ikasan.connector.util.chunking.model.FileChunkHeader;
import org.ikasan.connector.util.chunking.model.dao.ChunkHeaderLoadException;
import org.ikasan.connector.util.chunking.model.dao.FileChunkDao;

/**
 * Delivers a specified payload to a remote File Transfer directory
 * 
 * @author Ikasan Development Team
 */
public class CleanupChunksCommand extends AbstractBaseFileTransferTransactionalResourceCommand {

    /** The logger instance. */
    private static Logger logger = Logger.getLogger(CleanupChunksCommand.class);

    /**
     * FileChunkHeader that we will be cleaning up
     * 
     * Note that this is not persisted. Only its id is persisted, and if
     * necessary this is later reloaded from the id
     */
    private FileChunkHeader fileChunkHeader;

    /**
     * Id for the fileChunkHeader.
     * 
     * Note that this is persisted
     */
    private Long fileChunkHeaderId;

    /**
     * Constructor
     */
    public CleanupChunksCommand() {
        super();
    }

    @Override
    protected ExecutionOutput performExecute() {
        logger.info("execute called on this command: [" + this + "]");
        this.fileChunkHeader = ((FileChunkHeader) executionContext.get(ExecutionContext.FILE_CHUNK_HEADER));
        this.fileChunkHeaderId = fileChunkHeader.getId();
        return new ExecutionOutput();
    }

    @Override
    protected void doCommit() throws ResourceException {
        logger.info("commit called on this command:" + this + "]");
        FileChunkDao fileChunkDao = (FileChunkDao) getBeanFactory().get("fileChunkDao");
        if (fileChunkHeader == null) {
            try {
                fileChunkHeader = fileChunkDao.load(fileChunkHeaderId);
            } catch (ChunkHeaderLoadException e) {
                throw new ConnectorException("FileChunkHeader with pk [" + fileChunkHeaderId + "] could not be reloaded from the database", e);
            }
        }
        fileChunkDao.delete(fileChunkHeader);
    }

    @Override
    protected void doRollback() {
        logger.info("rollback called on this command:" + this + "]");
    }

    /**
     * Accessor method required by Hibernate
     * 
     * @return fileChunkHeaderId
     */
    @SuppressWarnings("unused")
    private Long getFileChunkHeaderId() {
        return fileChunkHeaderId;
    }

    /**
     * Setter required by Hibernate
     * 
     * @param fileChunkHeaderId the file chunk header id to set 
     */
    @SuppressWarnings("unused")
    private void setFileChunkHeaderId(Long fileChunkHeaderId) {
        this.fileChunkHeaderId = fileChunkHeaderId;
    }
}

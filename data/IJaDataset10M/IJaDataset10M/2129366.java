package org.ourgrid.common.interfaces.to;

import java.io.Serializable;
import br.edu.ufcg.lsd.commune.processor.filetransfer.TransferHandle;

public class ProcessCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String PUT_COMMAND = "put";

    public static final String STORE_COMMAND = "store";

    public static final String GET_COMMAND = "get";

    private TransferHandle handle;

    private String destination;

    private String fileName;

    private Long transferBegin;

    private Long transferEnd;

    private Double transferRate;

    private String name;

    private String source;

    public ProcessCommand(String source, String destination, String fileName, Long transferBegin, Long transferEnd, String name, TransferHandle handle) {
        this.destination = destination;
        this.fileName = fileName;
        this.transferBegin = transferBegin;
        this.transferEnd = transferEnd;
        this.name = name;
        this.source = source;
        this.handle = handle;
    }

    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getFileName() {
        return fileName;
    }

    public Long getTransferBegin() {
        return transferBegin;
    }

    public Long getTransferEnd() {
        return transferEnd;
    }

    public Double getTransferRate() {
        return transferRate;
    }

    public TransferHandle getHandle() {
        return handle;
    }

    public void setHandle(TransferHandle handle) {
        this.handle = handle;
    }
}

package org.webstrips.core.data;

public class TransferManagerEvent {

    private Transfer<?> transfer;

    private long id;

    private String owner;

    public TransferManagerEvent(Transfer<?> transfer, long id, String owner) {
        super();
        this.transfer = transfer;
        this.id = id;
        this.owner = owner;
    }

    public boolean equals(Object o) {
        if (o instanceof TransferManagerEvent) {
            return ((TransferManagerEvent) o).id == id;
        }
        return false;
    }

    public long getId() {
        return id;
    }

    public Transfer<?> getTransfer() {
        return transfer;
    }

    public String getOwner() {
        return owner;
    }
}

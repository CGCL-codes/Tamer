package org.jpc.emulator.motherboard;

/**
 * Implemented by devices that can perform DMA transfers.
 * @author Chris Dennis
 */
public interface DMATransferCapable {

    /**
     * Requests that this <code>DMATransferCapable</code> device perform a dma
     * transfer to/from the requested channel.
     * @param channel points to memory region in question.
     * @param position position in current transfer.
     * @param size number of bytes to be transferred.
     * @return updated position in transfer.
     */
    public int handleTransfer(DMAController.DMAChannel channel, int position, int size);
}

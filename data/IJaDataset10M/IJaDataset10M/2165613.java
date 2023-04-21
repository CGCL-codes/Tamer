package com.dgtalize.netc.net;

/**
 *
 * @author Diego
 */
public class TransferCanceledException extends Exception {

    public TransferCanceledException() {
        super("The transfer has been canceled by the other side.");
    }
}

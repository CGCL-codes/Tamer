package com.netx.basic.R1.io;

import java.io.IOException;

public class SyncFailedException extends ReadWriteException {

    SyncFailedException(String message, IOException cause) {
        super(message, cause);
    }

    SyncFailedException(IOException cause) {
        super(cause);
    }
}

package com.netx.basic.R1.io;

import java.io.IOException;

public class UnknownIOException extends ReadWriteException {

    UnknownIOException(IOException cause, String streamName) {
        super(cause);
    }
}

package com.jcraft.jdxpc;

import java.io.*;
import java.util.*;

class ClientReadBuffer extends ReadBuffer {

    Channel channel;

    ClientReadBuffer(Channel channel) {
        super();
        this.channel = channel;
    }

    boolean locateMessage() {
        int size = inend - instart;
        if (size < 4) return false;
        if (firstMessage) {
            if (size < 12) return false;
            if (inbuffer[instart] == 0x42) {
                setBigEndian(true);
            } else {
                setBigEndian(false);
            }
            channel.setBigEndian(bigEndian);
            int foo = getUINT(inbuffer, instart + 6);
            foo = (foo + 3) / 4 * 4;
            int bar = getUINT(inbuffer, instart + 8);
            bar = (bar + 3) / 4 * 4;
            dataLength = 12 + foo + bar;
        } else {
            dataLength = getUINT(inbuffer, instart + 2) << 2;
        }
        if (size < dataLength) {
            while (true) {
                if (inbuffer.length < dataLength) {
                    byte[] foo = new byte[inbuffer.length * 2];
                    System.arraycopy(inbuffer, 0, foo, 0, inbuffer.length);
                    inbuffer = foo;
                    continue;
                }
                break;
            }
            try {
                read(dataLength);
            } catch (Exception e) {
            }
            if (dataLength <= inrest) return true;
            return false;
        }
        firstMessage = false;
        headerLength = 0;
        trailerLength = 0;
        return true;
    }
}

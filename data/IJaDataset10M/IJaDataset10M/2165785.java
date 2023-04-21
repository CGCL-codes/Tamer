package org.simpleframework.http;

import java.io.IOException;
import org.simpleframework.http.store.Storage;

class ChunkedConsumer extends StoreConsumer {

    private boolean terminal;

    private boolean last;

    private byte line[];

    private int count;

    private int chunk;

    public ChunkedConsumer(Storage storage) {
        this(storage, 1024);
    }

    private ChunkedConsumer(Storage storage, int chunk) {
        this.line = new byte[chunk];
        this.storage = storage;
    }

    protected int process(byte[] array, int off, int size) throws IOException {
        int mark = off + size;
        while (off < mark) {
            if (terminal || last) {
                while (off < mark) {
                    if (array[off++] == '\n') {
                        if (last) {
                            finished = true;
                            return mark - off;
                        }
                        terminal = false;
                        break;
                    }
                }
            } else if (chunk == 0) {
                while (chunk == 0) {
                    if (off >= mark) {
                        break;
                    } else if (array[off++] == '\n') {
                        parse();
                        if (chunk == 0) {
                            last = true;
                            break;
                        }
                    } else {
                        line[count++] = array[off - 1];
                    }
                }
            } else {
                int write = Math.min(mark - off, chunk);
                write(array, off, write);
                chunk -= write;
                off += write;
                if (chunk == 0) {
                    terminal = true;
                }
            }
        }
        return 0;
    }

    private void parse() throws IOException {
        int off = 0;
        while (off < count) {
            int octet = toDecimal(line[off]);
            if (octet < 0) {
                if (off < 1) {
                    throw new IOException("Invalid chunk size line");
                }
                break;
            }
            chunk <<= 4;
            chunk ^= octet;
            off++;
        }
        count = 0;
    }

    private int toDecimal(byte octet) {
        if (octet >= 'A' && octet <= 'Z') {
            return (octet - (int) 'A') + 10;
        }
        if (octet >= '0' && octet <= '9') {
            return octet - (int) '0';
        }
        if (octet >= 'a' && octet <= 'f') {
            return (octet - (int) 'a') + 10;
        }
        return -1;
    }
}

package com.hs.mail.imap.processor.fetch;

import java.nio.ByteBuffer;

/**
 * 
 * @author Won Chul Doh
 * @since Mar 8, 2010
 *
 */
public class Content {

    private String name;

    private ByteBuffer contents;

    private long size;

    public Content(String name, ByteBuffer contents) {
        this.name = name;
        this.contents = contents;
        this.size = contents.limit();
    }

    public Content(String name, ByteBuffer contents, long size) {
        this.name = name;
        this.contents = contents;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public ByteBuffer getContents() {
        return contents;
    }
}

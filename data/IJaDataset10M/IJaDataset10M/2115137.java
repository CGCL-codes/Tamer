package org.piax.gnt;

public interface SecurityManager {

    public byte[] wrap(Object src);

    public Object unwrap(byte[] src);
}

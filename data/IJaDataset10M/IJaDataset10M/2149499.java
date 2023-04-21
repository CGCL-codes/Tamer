package net.sf.selfim.core.kb;

import java.io.Serializable;

/**
 * This implements a basic(not optimised) functionality to mentain informations
 * on a stream.
 * add net.sf.selfim.info.ToDo importance=high title="make something" description="sa fac ceva interesant"
 * select net.sf.selfim.info.ToDo
 * @author: Costin Emilian GRIGORE
 */
public class StreamKB extends EncryptedStreamKB implements KnowledgeBase, Serializable {

    public StreamKB() {
        super(null);
    }

    public StreamKB(String password) {
        super(password);
    }

    protected boolean isEncrypted() {
        return password != null;
    }
}

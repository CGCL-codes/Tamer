package nts.node;

import nts.io.CharCode;

public interface WordBuilder {

    WordBuilder NULL = null;

    boolean add(CharCode code);

    void close(boolean boundary);
}

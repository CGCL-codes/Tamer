package org.gpc.evm.gen;

import java.io.IOException;
import org.gpc.gen.CodeBytes;
import org.gpc.gen.IBytesCollector;

class ConstantCollector {

    public ConstantCollector(IBytesCollector collector) {
    }

    public int add(CodeBytes bytes) throws IOException {
        if (bytes.mappedLength() != 4 && bytes.mappedLength() != 8) {
            throw new IllegalArgumentException("Constant bytes length incorrect");
        }
        return 0;
    }
}

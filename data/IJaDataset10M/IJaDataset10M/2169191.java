package targets;

import dalvik.annotation.VirtualTestTarget;
import java.security.AlgorithmParameterGenerator;

/**
 * @hide
 */
public interface AlgorithmParameterGenerators {

    /**
     * @hide
     */
    abstract class Internal extends AlgorithmParameterGenerator {

        protected Internal() {
            super(null, null, null);
        }
    }

    @VirtualTestTarget
    abstract static class AES extends Internal {

        protected abstract void method();
    }

    @VirtualTestTarget
    abstract static class DSA extends Internal {

        protected abstract void method();
    }

    @VirtualTestTarget
    abstract static class DH extends Internal {

        protected abstract void method();
    }
}

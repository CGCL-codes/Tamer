package xades4j.utils;

import com.google.inject.Inject;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.c14n.InvalidCanonicalizerException;
import xades4j.algorithms.Algorithm;
import xades4j.UnsupportedAlgorithmException;
import xades4j.xml.marshalling.algorithms.AlgorithmsParametersMarshallingProvider;

/**
 * @author Luís
 */
class TimeStampDigestInputFactoryImpl implements TimeStampDigestInputFactory {

    private final AlgorithmsParametersMarshallingProvider parametersMarshallingProvider;

    @Inject
    TimeStampDigestInputFactoryImpl(AlgorithmsParametersMarshallingProvider parametersMarshallingProvider) {
        this.parametersMarshallingProvider = parametersMarshallingProvider;
    }

    @Override
    public TimeStampDigestInput newTimeStampDigestInput(Algorithm c14n) throws UnsupportedAlgorithmException {
        if (null == c14n) {
            throw new NullPointerException("Canonicalization algorithm cannot be null");
        }
        try {
            Canonicalizer.getInstance(c14n.getUri());
        } catch (InvalidCanonicalizerException ex) {
            throw new UnsupportedAlgorithmException("Unsupported canonicalization method", c14n.getUri(), ex);
        }
        return new TimeStampDigestInputImpl(c14n, this.parametersMarshallingProvider);
    }
}

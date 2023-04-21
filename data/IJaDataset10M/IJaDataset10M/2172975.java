package de.ddb.charset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;
import de.ddb.charset.normalizer.Normalizer;
import de.ddb.charset.normalizer.NormalizerFactory;

/**
 * @author heck
 *
 */
public class NormalizerFactoryTest {

    private static final Log LOGGER = LogFactory.getLog(NormalizerFactoryTest.class);

    /**
     * @throws ClassNotFoundException
     */
    @Test
    public void test() throws ClassNotFoundException {
        String äString = "ä";
        byte[] decomposedRef = ("a" + String.valueOf('̈')).getBytes();
        Normalizer normalizer = NormalizerFactory.getInstance();
        String inputHex = Utils.toHexString(äString.getBytes());
        LOGGER.debug("ä: " + inputHex);
        String composed = normalizer.compose(äString);
        String composedHex = Utils.toHexString(composed.getBytes());
        LOGGER.debug("Composed: " + composedHex);
        Assert.assertArrayEquals(äString.getBytes(), composed.getBytes());
        String decomposed = normalizer.decompose(composed);
        String decomposedHex = Utils.toHexString(decomposed.getBytes());
        LOGGER.debug("Decomposed: " + decomposedHex);
        LOGGER.debug("DecomposedRef: " + Utils.toHexString(decomposedRef));
        Assert.assertArrayEquals(decomposedRef, decomposed.getBytes());
    }
}

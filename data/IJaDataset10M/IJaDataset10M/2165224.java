package net.lunglet.fft;

import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.Collection;
import net.lunglet.array4j.ComplexFloat;
import net.lunglet.array4j.Storage;
import net.lunglet.array4j.matrix.dense.CFloatDenseVector;
import net.lunglet.array4j.matrix.dense.FloatDenseVector;
import net.lunglet.fft.dfti.DftiFFT;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public final class FFTTest {

    @Parameters
    public static Collection<?> data() {
        return Arrays.asList(new Object[][] { { new DftiFFT(), Storage.DIRECT } });
    }

    private final FFT fft;

    private final Storage storage;

    public FFTTest(final FFT fft, final Storage storage) {
        this.fft = fft;
        this.storage = storage;
    }

    @Ignore
    @Test
    public void test() {
        FloatDenseVector x = null;
        CFloatDenseVector y = fft.fft(x, x.length());
        assertEquals(new ComplexFloat(6.0f, 0.0f), y.get(0));
        assertEquals(new ComplexFloat(-1.5f, 0.8660254f), y.get(1));
        assertEquals(new ComplexFloat(-1.5f, -0.8660254f), y.get(2));
        y = fft.fft(x, 4);
        assertEquals(new ComplexFloat(6.0f, 0.0f), y.get(0));
        assertEquals(new ComplexFloat(-2.0f, -2.0f), y.get(1));
        assertEquals(new ComplexFloat(2.0f, 0.0f), y.get(2));
        assertEquals(new ComplexFloat(-2.0f, 2.0f), y.get(3));
    }
}

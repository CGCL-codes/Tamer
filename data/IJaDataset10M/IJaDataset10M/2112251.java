package org.isakiev.wic;

import org.isakiev.wic.filter.FilterTestSuite;
import org.isakiev.wic.geometry.GeometryTestSuite;
import org.isakiev.wic.image.ImageTestSuite;
import org.isakiev.wic.j2kfacade.J2KFacadeTestSuite;
import org.isakiev.wic.polynomial.PolynomialTestSuite;
import org.isakiev.wic.processor.ProcessorTestSuite;
import org.isakiev.wic.spiht.core.SPIHTTestSuite;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Wavelet image compressor test suite
 * 
 * @author Ruslan Isakiev
 */
public class WICTestSuite {

    public static Test suite() {
        TestSuite suite = new TestSuite("Wavelet image compressor test suite");
        suite.addTest(FilterTestSuite.suite());
        suite.addTest(GeometryTestSuite.suite());
        suite.addTest(ImageTestSuite.suite());
        suite.addTest(J2KFacadeTestSuite.suite());
        suite.addTest(PolynomialTestSuite.suite());
        suite.addTest(ProcessorTestSuite.suite());
        suite.addTest(SPIHTTestSuite.suite());
        return suite;
    }
}

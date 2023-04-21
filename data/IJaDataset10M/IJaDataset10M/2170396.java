package com.g2inc.scap.library.domain.bundle;

import com.g2inc.scap.library.domain.xccdf.XCCDFBenchmark;

/**
 * Holds a reference to the loaded XCCDF dictionary document
 */
public class BundledXCCDFDocumentHandle extends BundledDocumentHandle {

    public BundledXCCDFDocumentHandle(XCCDFBenchmark benchmark, SCAPDocumentBundle bundle) {
        super(benchmark, bundle);
    }

    /**
     * Get the underlying XCCDF benchmark.
     * 
     * @return XCCDFBenchmark
     */
    @Override
    public XCCDFBenchmark getDocument() {
        return (XCCDFBenchmark) document;
    }
}

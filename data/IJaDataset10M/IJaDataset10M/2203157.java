package org.test.slice.minimal;

import org.test.slice.SliceServiceChurchillWatermanTest;

public abstract class TIGRSliceServiceChurchillWatermanTest extends SliceServiceChurchillWatermanTest {

    public TIGRSliceServiceChurchillWatermanTest(String arg0) {
        super(arg0);
    }

    @Override
    public void setSliceServiceClassName() {
        sliceServiceClassName = "org.tigr.cloe.model.facade.consensusFacade.sliceService.TIGRSliceService";
    }
}

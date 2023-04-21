package org.jcvi.fluValidator2.flan.errors;

import org.jcvi.fluValidator2.error.AbstractNonLocalizedError;

public class FrameShiftError extends AbstractNonLocalizedError {

    public FrameShiftError() {
        super("has frameshift");
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public String toString() {
        return "FrameShiftError [getMessage()=" + getMessage() + "]";
    }
}

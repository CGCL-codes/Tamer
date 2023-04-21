package com.datagenic.fourthdimension.dates;

import com.datagenic.fourthdimension.FourthDimensionRuntimeException;

public class IndexAlignmentException extends FourthDimensionRuntimeException {

    /**
   @param message
   @roseuid 4151A881008C
    */
    public IndexAlignmentException(String message) {
        super(message);
    }

    /**
   @roseuid 4151A6BE0138
    */
    public IndexAlignmentException() {
        super("Index range too large");
    }
}

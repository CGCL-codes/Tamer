package org.jowidgets.spi.impl.swt.common.util;

import org.eclipse.swt.SWT;
import org.jowidgets.common.types.AlignmentHorizontal;
import org.jowidgets.util.Assert;

public final class AlignmentConvert {

    private AlignmentConvert() {
    }

    ;

    public static int convert(final AlignmentHorizontal alignmentHorizontal) {
        Assert.paramNotNull(alignmentHorizontal, "alignmentHorizontal");
        if (AlignmentHorizontal.RIGHT.equals(alignmentHorizontal)) {
            return SWT.RIGHT;
        } else if (AlignmentHorizontal.LEFT.equals(alignmentHorizontal)) {
            return SWT.LEFT;
        } else if (AlignmentHorizontal.CENTER.equals(alignmentHorizontal)) {
            return SWT.CENTER;
        } else {
            throw new IllegalArgumentException("Alignment '" + alignmentHorizontal + "' is unknown");
        }
    }
}

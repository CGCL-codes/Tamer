package org.jfeature.pattern.helper.impl;

import java.util.Collections;
import java.util.List;
import org.jfeature.fpi.FPBound;
import org.jfeature.fpi.FPClass;
import org.jfeature.fpi.FPContext;
import org.jfeature.fpi.FPConverter;
import org.jfeature.fpi.FPElement;
import org.jpattern.helper.Helper;

public abstract class AbstractHelperToInstanceConverter<ETM, EMD, ETD, EAM, EMH> implements FPConverter<ETM, EMD, ETD, EAM, EMH> {

    public final FPConverter.Level getLevel() {
        return FPConverter.Level.BASE;
    }

    public final FPConverter<ETM, EMD, ETD, EAM, EMH> applies(FPContext<ETM, EMD, ETD, EAM, EMH> context, FPElement<ETM, EMD, ETD, EAM, EMH> destElement, FPElement<ETM, EMD, ETD, EAM, EMH> srcElement, FPClass<ETM, EMD, ETD, EAM, EMH> dest, FPClass<ETM, EMD, ETD, EAM, EMH> src) {
        if (!context.isAssignableTypeMirror(src.getTypeMirror(), context.classToTypeMirror(Helper.class))) return null;
        List<? extends FPBound<ETM>> rb = src.getReturnBounds(context.getMethod(src.getDeclaration(), "toInstance", Collections.EMPTY_LIST));
        if (rb.size() != 1) context.throwRuntimeException("multiple return bounds");
        return context.isAssignableTypeMirror(rb.get(0).getBoundTypeMirror(), dest.getTypeMirror()) ? this : null;
    }
}

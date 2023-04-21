package com.farukcankaya.simplemodel.ast;

import org.eclipse.dltk.ast.references.SimpleReference;

public class SMValue extends SimpleReference {

    public SMValue(int start, int end) {
        super(start, end, "value");
    }

    @Override
    public int getKind() {
        return SMConstants.E_VALUE;
    }
}

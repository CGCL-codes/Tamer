package com.dustedpixels.jasmin.asm;

import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;

/**
 * @author micapolos@gmail.com (Michal Pociecha-Los)
 */
public final class AsmMethodAdapter extends MethodAdapter {

    public AsmMethodAdapter(MethodVisitor mv) {
        super(mv);
    }
}

package net.sf.joafip.store.service.bytecode.proxy;

/**
 * 
 * @author luc peuvrier
 * 
 */
public class ForTestCheckMethodVisitor extends CheckMethodVisitor {

    public ForTestCheckMethodVisitor(final ICheckMethodListener listener, final String className, final String methodAbsoluteName) {
        super(listener, className, methodAbsoluteName);
    }
}

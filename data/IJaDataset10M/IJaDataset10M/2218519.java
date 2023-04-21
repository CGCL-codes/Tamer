package asm.net.sf.joafip.asm;

import net.sf.joafip.asm.AnnotationVisitor;
import net.sf.joafip.asm.ClassWriter;
import net.sf.joafip.asm.FieldVisitor;
import net.sf.joafip.asm.MethodVisitor;
import net.sf.joafip.asm.Opcodes;

@SuppressWarnings("PMD")
public class NewProxyCallBackDump implements Opcodes {

    @SuppressWarnings("unused")
    public static byte[] dump() throws Exception {
        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;
        AnnotationVisitor av0;
        cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, "net/sf/joafip/asm/NewProxyCallBack", null, "java/lang/Object", null);
        {
            fv = cw.visitField(ACC_PRIVATE + ACC_FINAL, "proxyCallBack", "Lnet/sf/joafip/store/service/proxy/IProxyCallBackProxyDelegation;", null, null);
            fv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, new String[] { "net/sf/joafip/store/service/proxy/ProxyException" });
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitTypeInsn(NEW, "net/sf/joafip/store/service/proxy/ProxyCallBack");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "net/sf/joafip/store/service/proxy/ProxyCallBack", "<init>", "()V");
            mv.visitFieldInsn(PUTFIELD, "net/sf/joafip/asm/NewProxyCallBack", "proxyCallBack", "Lnet/sf/joafip/store/service/proxy/IProxyCallBackProxyDelegation;");
            mv.visitInsn(RETURN);
            mv.visitMaxs(3, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "getProxyCallBack", "()Lnet/sf/joafip/store/service/proxy/IProxyCallBackProxyDelegation;", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, "net/sf/joafip/asm/NewProxyCallBack", "proxyCallBack", "Lnet/sf/joafip/store/service/proxy/IProxyCallBackProxyDelegation;");
            mv.visitInsn(ARETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        cw.visitEnd();
        return cw.toByteArray();
    }
}

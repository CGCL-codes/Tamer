package net.sf.joafip.store.service.bytecode.agent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.instrument.IllegalClassFormatException;
import net.sf.joafip.asm.ClassReader;
import net.sf.joafip.asm.util.CheckClassAdapter;

/**
 * invoke java agent transformer. use for for debug
 * 
 * @author luc peuvrier
 * 
 */
@SuppressWarnings("PMD")
public final class MainTransformerTest {

    private MainTransformerTest() {
        super();
    }

    public static void main(final String[] args) {
        try {
            final MainTransformerTest main = new MainTransformerTest();
            main.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void run() throws IllegalClassFormatException, IOException {
        final PersistableTransformer transformer = new PersistableTransformer(true);
        final String jvmClassName = "net/sf/joafip/store/service/Store";
        final InputStream stream = ClassLoader.getSystemResourceAsStream(jvmClassName + ".class");
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int read;
        while ((read = stream.read()) != -1) {
            byteArrayOutputStream.write(read);
        }
        byteArrayOutputStream.close();
        stream.close();
        System.err.println("transform of " + jvmClassName + " is " + transformer.transformAttribute(jvmClassName));
        final byte[] classfileBuffer = byteArrayOutputStream.toByteArray();
        ClassReader cr = new ClassReader(classfileBuffer);
        PrintWriter pw = new PrintWriter("logs/original_check.txt");
        CheckClassAdapter.verify(cr, true, pw);
        final byte[] generated = transformer.transform(null, jvmClassName, null, null, classfileBuffer);
        cr = new ClassReader(generated);
        pw = new PrintWriter("logs/generated_check.txt");
        CheckClassAdapter.verify(cr, true, pw);
    }
}

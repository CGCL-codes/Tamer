package net.sf.asyncobjects.asm.tests;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.StringTokenizer;
import net.sf.asyncobjects.AResolver;
import net.sf.asyncobjects.When;
import net.sf.asyncobjects.internal.asm.BufferingProxyGenerator;
import net.sf.asyncobjects.internal.asm.ClassGenerator;
import net.sf.asyncobjects.internal.asm.DirectProxyGenerator;
import net.sf.asyncobjects.internal.asm.GenerationContext;
import net.sf.asyncobjects.io.AByteInput;
import net.sf.asyncobjects.io.AByteOutput;
import net.sf.asyncobjects.io.BufferedByteInput;
import net.sf.asyncobjects.io.BufferedByteOutput;
import net.sf.asyncobjects.io.util.ExhaustingInput;
import net.sf.asyncobjects.net.ASocket;
import net.sf.asyncobjects.net.ASocketFactory;
import net.sf.asyncobjects.net.threading.TSocket;
import net.sf.asyncobjects.net.threading.TSocketFactory;
import java.util.logging.Logger;

/**
 * This class represents filesystem generation context that is used primarily
 * for testing code generators and to save copies of the generated code when it
 * is required for debugging.
 * 
 * @author const
 */
public class FileSystemGenerationContext implements GenerationContext {

    /** Logger. */
    private static final Logger log = Logger.getLogger(FileSystemGenerationContext.class.getName());

    /**
	 * Output directory for the file
	 */
    final File outputDir;

    /**
	 * Generated buffering proxies
	 */
    final HashSet<Class<?>> generatedBufferingProxies = new HashSet<Class<?>>();

    /**
	 * A constructor using fields
	 * 
	 * @param outputDir
	 *            an ouput directory
	 */
    public FileSystemGenerationContext(final File outputDir) {
        super();
        this.outputDir = outputDir;
    }

    /**
	 * @see net.sf.asyncobjects.internal.asm.GenerationContext#ensureBufferingProxyGenerated(java.lang.Class)
	 */
    public void ensureBufferingProxyGenerated(Class<?> interfaceClass) {
        if (!generatedBufferingProxies.contains(interfaceClass)) {
            ClassGenerator g = new BufferingProxyGenerator(this, interfaceClass);
            g.generateClassBody();
            try {
                g.publish(this);
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception ex) {
                log.log(java.util.logging.Level.SEVERE, "problem during publishing buffering proxy for " + interfaceClass.getName(), ex);
                throw new RuntimeException("problem during publishing buffering proxy for " + interfaceClass.getName());
            }
        }
    }

    /**
	 * @see net.sf.asyncobjects.internal.asm.GenerationContext#publishClass(java.lang.String,
	 *      byte[])
	 */
    public void publishClass(String internalName, byte[] bytecode) throws Exception {
        int lastSlash = internalName.lastIndexOf('/');
        final String fileName = internalName.substring(lastSlash + 1);
        final String dirName = internalName.substring(0, lastSlash);
        File outputDir = this.outputDir;
        for (StringTokenizer t = new StringTokenizer(dirName, "/", false); t.hasMoreTokens(); ) {
            String tk = t.nextToken();
            if (tk.length() != 0) {
                outputDir = new File(outputDir, tk);
            }
        }
        outputDir.mkdirs();
        File file = new File(outputDir, fileName + ".class");
        System.out.println(file);
        FileOutputStream fi = new FileOutputStream(file);
        try {
            fi.write(bytecode);
        } finally {
            fi.close();
        }
    }

    /**
	 * @see net.sf.asyncobjects.internal.asm.GenerationContext#startedBufferingProxyGeneration(java.lang.Class)
	 */
    public void startedBufferingProxyGeneration(Class<?> interfaceClass) {
        generatedBufferingProxies.add(interfaceClass);
    }

    /**
	 * Test generators
	 * 
	 * @param args
	 *            application arguments
	 * @throws Exception
	 *             in case of IO problem
	 */
    public static void main(String args[]) throws Exception {
        FileSystemGenerationContext ctx = new FileSystemGenerationContext(new File("c:\\temp\\genclasses"));
        ctx.ensureBufferingProxyGenerated(AResolver.class);
        ctx.ensureBufferingProxyGenerated(ASocketFactory.class);
        generateAndPublishDirectProxy(ctx, AByteInput.class, BufferedByteInput.class);
        generateAndPublishDirectProxy(ctx, AByteOutput.class, BufferedByteOutput.class);
        generateAndPublishDirectProxy(ctx, ASocket.class, TSocket.class);
        generateAndPublishDirectProxy(ctx, AResolver.class, When.InternalArgumentListener.class);
        generateAndPublishDirectProxy(ctx, ASocketFactory.class, TSocketFactory.class);
        generateAndPublishDirectProxy(ctx, AByteInput.class, ExhaustingInput.class);
    }

    /**
	 * Utility method that forces generation of direct proxy.
	 * 
	 * @param ctx
	 *            a context
	 * @param interfaceClass
	 *            interface class
	 * @param serverClass
	 *            server classs
	 * @throws Exception
	 *             if there is problem during generation
	 */
    private static void generateAndPublishDirectProxy(FileSystemGenerationContext ctx, Class<?> interfaceClass, Class<?> serverClass) throws Exception {
        DirectProxyGenerator g = new DirectProxyGenerator(ctx, serverClass);
        g.generateClassBody();
        g.publish(ctx);
    }
}

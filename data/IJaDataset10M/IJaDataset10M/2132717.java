package fr.x9c.cadmium.primitives.unix;

import java.io.IOException;
import java.io.InterruptedIOException;
import fr.x9c.cadmium.kernel.Channel;
import fr.x9c.cadmium.kernel.CodeRunner;
import fr.x9c.cadmium.kernel.Context;
import fr.x9c.cadmium.kernel.Fail;
import fr.x9c.cadmium.kernel.FalseExit;
import fr.x9c.cadmium.kernel.Fatal;
import fr.x9c.cadmium.kernel.Misc;
import fr.x9c.cadmium.kernel.Primitive;
import fr.x9c.cadmium.kernel.PrimitiveProvider;
import fr.x9c.cadmium.kernel.Value;
import fr.x9c.cadmium.primitives.stdlib.Sys;
import fr.x9c.cadmium.util.StreamCopyThread;

/**
 * This class provides implementation for some execution-related primitives.
 *
 * @author <a href="mailto:cadmium@x9c.fr">Xavier Clerc</a>
 * @version 1.0
 * @since 1.0
 */
@PrimitiveProvider
public final class Execve {

    /**
     * No instance of this class.
     */
    private Execve() {
    }

    /**
     * Simulates <i>execve</i> by executing given a program, then stopping
     * program.
     * @param ctxt context
     * @param path program to execute
     * @param args program arguments
     * @param env program environment
     * @return <i>unit</i>
     * @throws Fail.Exception if program cannot be executed
     */
    @Primitive
    public static Value unix_execve(final CodeRunner ctxt, final Value path, final Value args, final Value env) throws Fail.Exception, Fatal.Exception, FalseExit {
        final Context context = ctxt.getContext();
        final int sz = args.asBlock().getWoSize();
        final String[] a = new String[sz];
        for (int i = 0; i < sz; i++) {
            a[i] = args.asBlock().get(i).asBlock().asString();
        }
        a[0] = context.getRealFile(path).getAbsolutePath();
        final int sze = env.asBlock().getWoSize();
        final String[] e = new String[sze];
        for (int i = 0; i < sze; i++) {
            e[i] = env.asBlock().get(i).asBlock().asString();
        }
        try {
            final ProcessBuilder pb = new ProcessBuilder(Misc.prepareArguments(a));
            pb.directory(context.getPwd());
            final Process p = pb.start();
            final Channel out = context.getChannel(Channel.STDOUT);
            if (out != null) {
                final Thread t = new StreamCopyThread(out.asOutputStream(), p.getInputStream());
                t.start();
            }
            final Channel err = context.getChannel(Channel.STDERR);
            if (err != null) {
                final Thread t = new StreamCopyThread(err.asOutputStream(), p.getErrorStream());
                t.start();
            }
            final Channel in = context.getChannel(Channel.STDIN);
            if (in != null) {
                final Thread t = new StreamCopyThread(p.getOutputStream(), in.asInputStream());
                t.start();
            }
            return Sys.caml_sys_exit(ctxt, Value.createFromLong(p.waitFor()));
        } catch (final FalseExit sfe) {
            throw sfe;
        } catch (final InterruptedException ie) {
            Unix.fail(ctxt, "execv", ie);
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(context);
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            Unix.fail(ctxt, "execv", ioe);
        }
        return Value.UNIT;
    }
}

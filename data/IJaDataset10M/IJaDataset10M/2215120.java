package ch.blackspirit.graphics.jogl;

import java.util.logging.Level;
import javax.media.opengl.DebugGL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

/**
 * GLEventListener base class handling insertion of DebugGL and TraceGL.
 * @author Markus Koller
 */
abstract class AbstractGLEventListener implements GLEventListener {

    private boolean trace = false;

    private boolean debugGL = false;

    private Level traceLevel = Level.INFO;

    protected void debug(GLAutoDrawable drawable) {
        if (trace) {
            if (debugGL) {
                if (!(drawable.getContext().getGL() instanceof TraceGL)) {
                    drawable.getContext().setGL(new TraceGL(new DebugGL(drawable.getContext().getGL()), traceLevel));
                }
                if (!(drawable.getGL() instanceof TraceGL)) {
                    drawable.setGL(new TraceGL(new DebugGL(drawable.getGL()), traceLevel));
                }
            } else {
                if (!(drawable.getContext().getGL() instanceof TraceGL)) {
                    drawable.getContext().setGL(new TraceGL(drawable.getContext().getGL(), traceLevel));
                }
                if (!(drawable.getGL() instanceof TraceGL)) {
                    drawable.setGL(new TraceGL(drawable.getGL(), traceLevel));
                }
            }
        } else if (debugGL) {
            if (!(drawable.getContext().getGL() instanceof DebugGL)) {
                drawable.getContext().setGL(new DebugGL(drawable.getContext().getGL()));
            }
            if (!(drawable.getGL() instanceof DebugGL)) {
                drawable.setGL(new DebugGL(drawable.getGL()));
            }
        }
    }

    public boolean isTrace() {
        return trace;
    }

    public void setTrace(boolean trace) {
        this.trace = trace;
    }

    public boolean isDebugGL() {
        return debugGL;
    }

    public void setDebugGL(boolean debugGL) {
        this.debugGL = debugGL;
    }

    public Level getTraceLevel() {
        return traceLevel;
    }

    public void setTraceLevel(Level traceLevel) {
        this.traceLevel = traceLevel;
    }
}

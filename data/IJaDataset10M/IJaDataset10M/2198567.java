package freemarker.template.utility;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
import freemarker.template.TemplateTransformModel;
import freemarker.core.Environment;

/**
 * A crude first pass at an embeddable Jython interpreter
 * @author <mailto:jon@revusky.com>Jonathan Revusky</a>
 */
public class JythonRuntime extends PythonInterpreter implements TemplateTransformModel {

    public Writer getWriter(final Writer out, final Map args) {
        final StringBuffer buf = new StringBuffer();
        final Environment env = Environment.getCurrentEnvironment();
        return new Writer() {

            public void write(char cbuf[], int off, int len) {
                buf.append(cbuf, off, len);
            }

            public void flush() throws IOException {
                interpretBuffer();
                out.flush();
            }

            public void close() {
                interpretBuffer();
            }

            private void interpretBuffer() {
                synchronized (JythonRuntime.this) {
                    PyObject prevOut = systemState.stdout;
                    try {
                        setOut(out);
                        set("env", env);
                        exec(buf.toString());
                        buf.setLength(0);
                    } finally {
                        setOut(prevOut);
                    }
                }
            }
        };
    }
}

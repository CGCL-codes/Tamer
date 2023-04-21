package org.jtestcase.core.mapping;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

public class VelocityStandardEngine {

    private static final boolean THROW_ON_FAILURE = true;

    private static boolean _debug = false;

    public VelocityStandardEngine() {
        Velocity.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");
        try {
            Velocity.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void applyTemplate(VelocityContext context, Writer writer, String template) throws ParseErrorException, MethodInvocationException, ResourceNotFoundException, IOException {
        context.put("FormatterUtil", new FormatterUtil());
        Velocity.evaluate(context, writer, "LOG", template);
    }

    public void applyTemplate(String templateFilePath, VelocityContext context, Writer writer) throws ParseErrorException, MethodInvocationException, ResourceNotFoundException, IOException {
        StringWriter template = new StringWriter();
        InputStream input = null;
        if (input == null) {
            input = getClass().getResourceAsStream(templateFilePath);
        }
        InputStreamReader reader = new InputStreamReader(input);
        context.put("FormatterUtil", new FormatterUtil());
        int c;
        try {
            while ((c = reader.read()) != -1) template.write(c);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Velocity.evaluate(context, writer, "LOG", template.toString());
    }

    private static InputStream load(String name, ClassLoader loader) {
        if (name == null) {
            throw new IllegalArgumentException("null resource name");
        }
        InputStream in = null;
        try {
            if (loader == null) {
            }
            in = loader.getResourceAsStream(name);
        } catch (Exception e) {
            e.printStackTrace();
            in = null;
        } finally {
        }
        if (THROW_ON_FAILURE && in == null) {
            throw new IllegalArgumentException("couldn't load  resource [" + name + "]");
        }
        return in;
    }

    public static InputStream load(String name) {
        return load(name, Thread.currentThread().getContextClassLoader());
    }

    class FormatterUtil {

        public String UppercaseFirst(String input) {
            if (input.length() < 1) return input;
            return input.substring(0, 1).toUpperCase() + input.substring(1, input.length());
        }
    }
}

package grace.log;

import java.io.PrintWriter;
import java.io.PrintStream;
import grace.util.Properties;

/**
 * This class is a concrete Handler for the Log class that uses an
 * internal java.io.PrintWriter to which to print each event.
 * Currently, this can handle printing to the standard out or a file.
 **/
public class PrintHandler implements Handler {

    public static final String rcsid = "$Id: PrintHandler.java,v 1.1 2005/12/15 01:45:08 fbergmann Exp $";

    private java.io.PrintWriter out;

    protected EventFormat format = new EventFormat();

    public PrintHandler(String prefix, String name) throws java.rmi.RemoteException {
        this(System.out);
        loadProperties(prefix + name);
    }

    public PrintHandler(PrintWriter writer) throws java.rmi.RemoteException {
        out = writer;
    }

    public PrintHandler(PrintStream stream) throws java.rmi.RemoteException {
        setOut(stream);
    }

    public void loadProperties(String prefix) {
        Properties properties = new Properties();
        properties.loadSystem();
        String useColor = properties.get(prefix + ".color");
        if (useColor != null) format.useColors(useColor.equals("true"));
        try {
            Properties eventColors = properties.subset(prefix + "\\.event\\.(.+)\\.color$", "$1");
            java.util.Enumeration iter = eventColors.names();
            while (iter.hasMoreElements()) {
                String event = (String) iter.nextElement();
                String color = eventColors.get(event);
                format.setEventColor(event, format.colorListToColor(color));
            }
        } catch (Exception e) {
            Log.internal(e);
        }
        try {
            Properties eventColors = properties.subset(prefix + "\\.line\\.(.+)\\.color$", "$1");
            java.util.Enumeration iter = eventColors.names();
            while (iter.hasMoreElements()) {
                String event = (String) iter.nextElement();
                String color = eventColors.get(event);
                format.setLineColor(event, format.colorListToColor(color));
            }
        } catch (Exception e) {
            Log.internal(e);
        }
        String color = null;
        color = properties.get(prefix + ".time.color");
        if (color != null) format.setTimeColor(format.colorListToColor(color));
        color = properties.get(prefix + ".time.relative.color");
        if (color != null) format.setRelativeTimeColor(format.colorListToColor(color));
        color = properties.get(prefix + ".exception.color");
        if (color != null) format.setExceptionColor(format.colorListToColor(color));
        color = properties.get(prefix + ".message.color");
        if (color != null) format.setMessageColor(format.colorListToColor(color));
        color = properties.get(prefix + ".object.color");
        if (color != null) format.setObjectColor(format.colorListToColor(color));
        color = properties.get(prefix + ".thread.color");
        if (color != null) format.setThreadColor(format.colorListToColor(color));
        color = properties.get(prefix + ".classname.color");
        if (color != null) format.setClassnameColor(format.colorListToColor(color));
        color = properties.get(prefix + ".function.color");
        if (color != null) format.setFunctionColor(format.colorListToColor(color));
    }

    public void setOut(PrintWriter writer) {
        out = writer;
    }

    public void setOut(PrintStream stream) {
        if (stream instanceof PrintCatcher) stream = ((PrintCatcher) stream).getOriginalStream();
        out = new PrintWriter(stream);
    }

    public PrintWriter getOut() {
        return out;
    }

    public synchronized void handle(Event event) {
        out.print(format.format(event));
        out.flush();
    }
}

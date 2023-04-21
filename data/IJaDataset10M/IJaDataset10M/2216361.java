package mimosa.probe;

import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import mimosa.Mimosa;

/**
 * A probe output is a probe observer that saves sequentially in a file the selected probes. It is parameterized by:
 * <ul>
 * <li>the file name</li>
 * <li>the separator between the saved fields</li>
 * <li>whether the probe name, global time and local time has to be saved too</li>
 * <li>the list of saved probes</li>
 * </ul>
 * It can receive any probe of which the arguments shall be saved in the file, one per line, optionally
 * preceded by its name and its global and local time stamps.
 *
 * @author Jean-Pierre Muller
 */
public class ProbeOutput extends AbstractProbeObserver implements Closeable {

    private Writer stream;

    private String separator;

    /**
	 * The empty constructor adding this object to the closeables.
	 */
    public ProbeOutput() {
    }

    /**
	 * @see mimosa.probe.ProbeObserver#receiveProbe(mimosa.probe.Probe)
	 */
    public void receiveProbe(Probe probe) {
        ProbeOutputSpecification spec = (ProbeOutputSpecification) outputSpecification;
        boolean hasPrevious = false;
        String name = probe.getName();
        if (spec.getProbeFilter() != null && spec.getProbeFilter().contains(name)) {
            try {
                if (stream == null) {
                    stream = new FileWriter(((ProbeOutputSpecification) outputSpecification).getFile());
                    Mimosa.addCloseable(this);
                }
                if (spec.isProbeNameSaved()) {
                    stream.write(name);
                    hasPrevious = true;
                }
                if (spec.isGlobalTimeSaved()) {
                    stream.write((hasPrevious ? separator : "") + probe.getGlobalTime());
                    hasPrevious = true;
                }
                if (spec.isLocalTimeSaved()) {
                    stream.write((hasPrevious ? separator : "") + probe.getLocalTime());
                    hasPrevious = true;
                }
                Object[] arguments = probe.getArguments();
                for (Object arg : arguments) {
                    stream.write((hasPrevious ? separator : "") + arg);
                    hasPrevious = true;
                }
                stream.write("\n");
                stream.flush();
            } catch (IOException e) {
                Mimosa.displayWarningMessage(null, Messages.getString("ProbeOutput.1"));
            } catch (NullPointerException e) {
                Mimosa.displayWarningMessage(null, Messages.getString("ProbeOutput.3"));
            }
        }
    }

    /**
	 * @see mimosa.probe.ProbeObserver#initialize()
	 */
    public void initialize() {
        try {
            close();
        } catch (IOException e) {
            Mimosa.displayWarningMessage(null, Messages.getString("ProbeOutput.4"));
        }
    }

    /**
	 * @see mimosa.probe.ProbeObserver#setOutputSpecification(mimosa.probe.OutputSpecification)
	 */
    public void setOutputSpecification(OutputSpecification outputSpecification) {
        super.setOutputSpecification(outputSpecification);
        separator = ((ProbeOutputSpecification) outputSpecification).getSeparator();
        try {
            if (((ProbeOutputSpecification) outputSpecification).getFile() != null) stream = new FileWriter(((ProbeOutputSpecification) outputSpecification).getFile());
        } catch (IOException e) {
            Mimosa.displayWarningMessage(null, Messages.getString("ProbeOutput.2"));
            ((ProbeOutputSpecification) outputSpecification).setFile(null);
        }
    }

    /**
	 * @see java.io.Closeable#close()
	 */
    public void close() throws IOException {
        if (stream != null) {
            stream.flush();
            stream.close();
            stream = null;
        }
    }
}

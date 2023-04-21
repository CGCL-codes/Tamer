package com.aaspring.util.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import com.aaspring.InvalidStateException;

/**
 * @author Balazs
 * 
 */
public abstract class AbstractFileAccessor implements FileAccessor {

    public static enum States {

        MODIFIABLE, OPENED4INPUT, OPENED4OUTPUT
    }

    protected Set<FileAccessorProperties> fapSet = new HashSet<FileAccessorProperties>();

    protected Map<FileAccessorProperties, InputStream> inputStreamMap;

    protected Map<FileAccessorProperties, OutputStream> outputStreamMap;

    protected States state = States.MODIFIABLE;

    public void addFile(final FileAccessorProperties fap) throws InvalidStateException {
        if (state != States.MODIFIABLE) throw new InvalidStateException("Cannot add a new file" + " from list when it is not in modifiable state");
        fapSet.add(fap);
    }

    public void closeAndSetStatus2Modifiable() throws InvalidStateException {
        if (state == States.MODIFIABLE) throw new InvalidStateException("Cannot close in modifiable state");
        closeImpl();
        state = States.MODIFIABLE;
        inputStreamMap = null;
        outputStreamMap = null;
    }

    protected abstract void closeImpl();

    public Set<FileAccessorProperties> getFileAccessorPropertiesSet() {
        return Collections.unmodifiableSet(fapSet);
    }

    public InputStream getInputStream(final String type, final Map<String, String> attributes, final Locale locale) throws IOException {
        if (state != States.MODIFIABLE) throw new IllegalStateException("Input streams can be opened only " + "from modifiable state");
        return getInputStreams().get(new FileAccessorProperties(attributes, type, locale));
    }

    public Map<FileAccessorProperties, InputStream> getInputStreams() throws IOException {
        if (state != States.MODIFIABLE) throw new IllegalStateException("Input streams can be opened only " + "from modifiable state");
        state = States.OPENED4INPUT;
        inputStreamMap = Collections.unmodifiableMap(getInputStreamsImpl(fapSet));
        return inputStreamMap;
    }

    protected abstract Map<FileAccessorProperties, InputStream> getInputStreamsImpl(Set<FileAccessorProperties> fapSet);

    public Map<FileAccessorProperties, OutputStream> getOutputStreams() {
        if (state != States.MODIFIABLE) throw new IllegalStateException("Input streams can be opened only " + "from modifiable state");
        state = States.OPENED4OUTPUT;
        outputStreamMap = Collections.unmodifiableMap(getOutputStreamsImpl(fapSet));
        return outputStreamMap;
    }

    protected abstract Map<FileAccessorProperties, OutputStream> getOutputStreamsImpl(final Set<FileAccessorProperties> fapSet);
}

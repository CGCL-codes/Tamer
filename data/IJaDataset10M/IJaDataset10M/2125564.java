package com.ibm.tuningfork.core.graphics;

import com.ibm.tuningfork.core.figure.CategoryDescriptor;
import com.ibm.tuningfork.infra.event.IEvent;
import com.ibm.tuningfork.infra.stream.core.Stream;
import com.ibm.tuningfork.infra.streambundle.StreamBundle;

/**
 * A simple color mapping that maps an event to a color associated with the stream it came from.
 * The name field of each category is simply the stream name, so when used to generate a legend
 * it will show the stream-to-color associations.
 *
 * The last stream looked up is cached so that paint loops over many events in a single stream
 * will be fast.
 *
 * If no colors are provided for the mapper to use, it will use the default "color wheel", which
 * provides a cycle of colors designed to be maximally distinguishable.
 */
public class StreamToColorMapper extends NameBasedColorMapper {

    protected Stream lastStream;

    protected CategoryDescriptor lastCategory;

    protected RGBColor lastColor;

    private static final String DEFAULT_NAME = "Different Color for Each Stream";

    public StreamToColorMapper() {
        this(null);
    }

    public StreamToColorMapper(StreamBundle streams) {
        this(DEFAULT_NAME, COLOR_DEFAULTS, streams);
    }

    public StreamToColorMapper(String name, RGBColor[] colors, StreamBundle streams) {
        super(name, colors);
        if (streams != null) {
            addStreams(streams);
        }
    }

    public void clear() {
        super.clear();
        lastStream = null;
    }

    public void addStream(Stream stream) {
        makeDescriptor(stream.getName());
    }

    public void addStreams(StreamBundle streams) {
        for (Stream s : streams) {
            addStream(s);
        }
    }

    public RGBColor mapToColor(IEvent event, Stream source) {
        if (lastStream == source) {
            return lastColor;
        }
        return mapToCategory(event, source).toRGBColor();
    }

    public CategoryDescriptor mapToCategory(IEvent event, Stream source) {
        if (lastStream == source) {
            return lastCategory;
        }
        CategoryDescriptor descriptor = super.mapToCategory(event, source);
        lastStream = source;
        lastCategory = descriptor;
        lastColor = descriptor.toRGBColor();
        return descriptor;
    }

    @Override
    protected String extractName(IEvent event, Stream stream) {
        return stream.getName();
    }
}

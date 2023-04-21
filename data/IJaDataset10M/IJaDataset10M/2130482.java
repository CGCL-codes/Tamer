package com.ibm.tuningfork.core.graphics;

import java.util.Arrays;
import java.util.HashMap;
import com.ibm.tuningfork.core.configuration.FigureConfiguration;
import com.ibm.tuningfork.core.figure.CategoryDescriptor;
import com.ibm.tuningfork.infra.event.IEvent;
import com.ibm.tuningfork.infra.stream.core.Stream;
import com.ibm.tuningfork.infra.streambundle.StreamBundle;

public abstract class NameBasedColorMapper implements ICategoryMapper {

    protected final String mapperName;

    protected final RGBColor[] colors;

    protected final HashMap<String, CategoryDescriptor> map = new HashMap<String, CategoryDescriptor>();

    protected int nextColor;

    protected static final String DEFAULT_NAME = "Choose Color Based on Name";

    public static final RGBColor[] COLOR_DEFAULTS = FigureConfiguration.COLOR_DEFAULTS;

    protected static final CategoryDescriptor UNKNOWN = new CategoryDescriptor("Unknown", RGBColor.lookup("gray"));

    public NameBasedColorMapper(String name, RGBColor[] colors) {
        this.colors = colors;
        this.mapperName = name;
    }

    public NameBasedColorMapper(String name) {
        this(name, COLOR_DEFAULTS);
    }

    protected abstract String extractName(IEvent event, Stream stream);

    public CategoryDescriptor mapToCategory(IEvent event, Stream source) {
        String name = extractName(event, source);
        if (name == null) {
            return UNKNOWN;
        } else {
            CategoryDescriptor descriptor = map.get(name);
            if (descriptor != null) {
                return descriptor;
            } else {
                return makeDescriptor(name);
            }
        }
    }

    protected CategoryDescriptor makeDescriptor(String name) {
        CategoryDescriptor descriptor = map.get(name);
        if (descriptor == null) {
            RGBColor color = colors[nextColor++ % colors.length];
            descriptor = new CategoryDescriptor(name, color);
            map.put(name, descriptor);
        }
        return descriptor;
    }

    public RGBColor mapToColor(IEvent event, Stream source) {
        return mapToCategory(event, source).toRGBColor();
    }

    public void changeMapping(String categoryName, CategoryDescriptor descriptor) {
        map.put(categoryName, descriptor);
    }

    public void clear() {
        map.clear();
        nextColor = 0;
    }

    public int getCategoryCount() {
        return map.size();
    }

    public CategoryDescriptor[] getCategoryDescriptors() {
        CategoryDescriptor[] descriptors = map.values().toArray(new CategoryDescriptor[map.size()]);
        Arrays.sort(descriptors);
        return descriptors;
    }

    public String getName() {
        return mapperName;
    }

    public void addStream(Stream stream) {
    }

    public void addStreams(StreamBundle streams) {
    }
}

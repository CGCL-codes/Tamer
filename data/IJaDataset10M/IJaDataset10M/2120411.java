package com.ibm.tuningfork.core.streamgui;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import com.ibm.tuningfork.infra.stream.core.Stream;

public class StreamGUIRegistry {

    protected static final StreamGUIRegistry registry = new StreamGUIRegistry();

    protected final Map<String, StreamGUI> byNameMap = new HashMap<String, StreamGUI>();

    protected final Map<Class<?>, StreamGUI> byClassMap = new HashMap<Class<?>, StreamGUI>();

    protected final Vector<StreamGUI> orderedGUIs = new Vector<StreamGUI>();

    public static StreamGUIRegistry getRegistry() {
        return registry;
    }

    static {
        initialize();
    }

    private static final void initialize() {
        registry.registerStream(new TimeIntervalStreamGUI());
        registry.registerStream(new SampleStreamGUI());
        registry.registerStream(new HistogramStreamGUI());
        registry.registerStream(new FunctionStreamGUI());
        registry.registerStream(new BookmarkStreamGUI());
        registry.registerStream(new BaseTimeIntervalStreamGUI());
        registry.registerStream(new ComplementTimeIntervalStreamGUI());
        registry.registerStream(new DurationSampleStreamGUI());
        registry.registerStream(new SelectedTimeIntervalStreamGUI());
        registry.registerStream(new IntersectTimeIntervalStreamGUI());
        registry.registerStream(new UnionTimeIntervalStreamGUI());
        registry.registerStream(new UtilizationSampleStreamGUI());
        registry.registerStream(new SampleSelectedTimeIntervalStreamGUI());
        registry.registerStream(new TimeSelectedTimeIntervalStreamGUI());
        registry.registerStream(new AnnotationSelectedTimeIntervalStreamGUI());
        registry.registerStream(new BaseSampleStreamGUI());
        registry.registerStream(new AccumulatedSampleStreamGUI());
        registry.registerStream(new DifferentiatedSampleStreamGUI());
        registry.registerStream(new IntegratedSampleStreamGUI());
        registry.registerStream(new ConstantSampleStreamGUI());
        registry.registerStream(new SelectedSampleStreamGUI());
        registry.registerStream(new SmoothedSampleStreamGUI());
        registry.registerStream(new DifferenceStreamGUI());
        registry.registerStream(new MonotonicSampleStreamGUI());
        registry.registerStream(new SnippedSampleStreamGUI());
        registry.registerStream(new SplicedSampleStreamGUI());
        registry.registerStream(new UnzippedSampleStreamGUI());
        registry.registerStream(new TimeTranslatedSampleStreamGUI());
        registry.registerStream(new LazySampleStreamGUI());
        registry.registerStream(new AudioSampleStreamGUI());
        registry.registerStream(new ValueBasedTimeIntervalStreamGUI());
        registry.registerStream(new ValueRangeTimeIntervalStreamGUI());
        registry.registerStream(new SampleValueHistogramStreamGUI());
        registry.registerStream(new TimeIntervalDurationHistogramStreamGUI());
        registry.registerStream(new TimeTranslationStreamGUI());
        registry.registerStream(new BaseBookmarkStreamGUI());
        registry.registerStream(new SampleStreamBookmarkStreamGUI());
        registry.registerStream(new TimeIntervalBookmarkStreamGUI());
        registry.registerStream(new CoarsenedEventStreamGUI());
        registry.registerStream(new ConcatenationEventStreamGUI());
        registry.registerStream(new StatisticsEventStreamGUI());
        registry.registerStream(new AudioDiscontinuityTimeIntervalStreamGUI());
    }

    public void registerStream(StreamGUI gui) {
        if (byNameMap.get(gui.getName()) != null) {
            return;
        }
        byNameMap.put(gui.getName(), gui);
        byClassMap.put(gui.streamClass(), gui);
        orderedGUIs.add(gui);
    }

    public StreamGUI get(String name) {
        return byNameMap.get(name);
    }

    public StreamGUI get(Stream stream) {
        return get(stream.getClass());
    }

    public StreamGUI get(Class<?> cls) {
        StreamGUI match = byClassMap.get(cls);
        if (match == null) {
            Class<?> parent = cls.getSuperclass();
            if (parent != null) {
                return get(parent);
            }
        }
        return match;
    }

    public Vector<StreamGUI> getCompatibleStreams(Stream stream) {
        return getCompatibleStreams(stream.getClass());
    }

    public Vector<StreamGUI> getCompatibleStreams(Class<?> cls) {
        Vector<StreamGUI> result = new Vector<StreamGUI>();
        for (StreamGUI gui : orderedGUIs) {
            for (Class<?> consumedClass : gui.consumes()) {
                if (consumedClass.isAssignableFrom(cls)) {
                    result.add(gui);
                }
            }
        }
        return result;
    }
}

package org.esfinge.comparison.reader;

public class MetadataReaderProvider {

    private static MetadataReaderProvider provider;

    public static MetadataReaderProvider getProvider() {
        if (provider == null) {
            provider = new MetadataReaderProvider();
        }
        return provider;
    }

    private ComparisonMetadataReader reader;

    private MetadataReaderProvider() {
        reader = new AnnotationComparisonMetadataReader();
    }

    public void setReader(ComparisonMetadataReader reader) {
        this.reader = reader;
    }

    public ComparisonMetadataReader getReader() {
        return reader;
    }

    public static void set(ComparisonMetadataReader reader) {
        getProvider().setReader(reader);
    }

    public static ComparisonMetadataReader get() {
        return getProvider().getReader();
    }
}

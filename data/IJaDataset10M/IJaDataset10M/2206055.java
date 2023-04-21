package perestrojka.backend;

import java.util.ArrayList;
import java.util.List;

class ConversionCache {

    private List<ConversionCacheEntry> entries = new ArrayList<ConversionCacheEntry>();

    void add(String sourceFormat, String targetFormat, List<ConversionStep> conversionSteps) {
        ConversionCacheEntry newEntry = new ConversionCacheEntry(sourceFormat, targetFormat, conversionSteps);
        if (!entries.contains(newEntry)) {
            entries.add(newEntry);
        }
    }

    List<ConversionStep> get(String sourceFormat, String targetFormat) {
        int index = entries.indexOf(new ConversionCacheEntry(sourceFormat, targetFormat, null));
        if (index == -1) {
            return null;
        } else {
            return entries.get(index).getConversionSteps();
        }
    }

    private class ConversionCacheEntry {

        private String sourceFormat;

        private String targetFormat;

        private List<ConversionStep> conversionSteps;

        ConversionCacheEntry(String sourceFormat, String targetFormat, List<ConversionStep> conversionSteps) {
            this.sourceFormat = sourceFormat;
            this.targetFormat = targetFormat;
            this.conversionSteps = conversionSteps;
        }

        public List<ConversionStep> getConversionSteps() {
            return conversionSteps;
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof ConversionCacheEntry)) {
                return false;
            }
            ConversionCacheEntry entry = (ConversionCacheEntry) other;
            if (entry.sourceFormat.equalsIgnoreCase(sourceFormat) && entry.targetFormat.equalsIgnoreCase(targetFormat)) {
                return true;
            }
            return false;
        }
    }
}

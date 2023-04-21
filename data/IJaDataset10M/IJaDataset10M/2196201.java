package biber.api;

import java.util.List;
import java.util.Properties;

public interface EntryExporter {

    public static final String NAME = "EntryExporter.name";

    public static final String KEY = "EntryExporter.key";

    public static final String CONSERVING = "EntryExporter.conserving";

    public boolean exportEntries(List<Entry> entries, boolean useMostRecent);

    public Properties getProperties();
}

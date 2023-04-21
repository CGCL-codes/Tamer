package gov.sns.apps.opticseditor;

import java.util.regex.Pattern;

/** filter node records by name */
public class NodeRecordNameFilter implements NodeRecordFilter {

    /** pattern to apply to the node record ID */
    private Pattern _pattern;

    /** Primary Constructor */
    public NodeRecordNameFilter(final Pattern pattern) {
        setPattern(pattern);
    }

    /** Constructor */
    public NodeRecordNameFilter() {
        this(null);
    }

    /** set a new pattern to apply to the node record ID */
    public void setPattern(final Pattern pattern) {
        _pattern = pattern;
    }

    /** accept or reject the node record based on whether its name matches the pattern */
    public boolean accept(final NodeRecord record) {
        final Pattern pattern = _pattern;
        return pattern == null ? true : pattern.matcher(record.getNodeID()).matches();
    }
}

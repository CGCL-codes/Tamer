package dnb.analyze;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import dnb.data.RepositoryObject;

public class MatchResult {

    /**
	 * Recognition status of the match.
	 */
    public enum Status {

        /**
		 * Entry not known.
		 */
        UNKNOWN("Unknown", 2), /**
		 * The entry was similar (levenstein distance) to an existing entry and was corrected.  
		 */
        SIMILAR("Similar", 1), /**
		 * The entry is known (case is ignored)
		 */
        KNOWN("Known", 0);

        private Status(String description, int severity) {
            this.description = description;
            this.severity = severity;
        }

        private final String description;

        private final int severity;

        public String getDescription() {
            return description;
        }

        public boolean isWorseThan(Status other) {
            return severity > other.severity;
        }
    }

    /**
	 * The source this info came from.
	 */
    public enum Source {

        /** 
		 * From nfo file, {@link MatchResult#start} and {@link MatchResult#end}
		 * specify positions inside an nfo file.  
		 */
        NFO, /** 
		 * From the file path, {@link MatchResult#start} and {@link MatchResult#end}
		 * specify positions inside the path. 
		 */
        PATH, /** From an id3 tag, {@link MatchResult#start} and {@link MatchResult#end} are invalid */
        IDTAG, /** e.g. label name from code, {@link MatchResult#start} and {@link MatchResult#end} are invalid */
        LOOKUP
    }

    private int start;

    private int end;

    private String match;

    private Status status;

    private Source source;

    /**
	 * Is set if this Match represents an indexed field.
	 */
    private SimilarScanResult<? extends RepositoryObject> similarScanResult = null;

    private static final int INVALID = -1;

    public MatchResult(int start, int end, String match, Status status, Source source) {
        this.start = start;
        this.end = end;
        this.match = match;
        this.status = status;
        this.source = source;
    }

    public MatchResult(int start, int end, String match, Source source) {
        this(start, end, match, Status.UNKNOWN, source);
    }

    public MatchResult(String match, Source source, Status status) {
        this(INVALID, INVALID, match, status, source);
    }

    public boolean locationIsInvalid() {
        return start <= INVALID || end <= INVALID;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public String getMatch() {
        return match;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public SimilarScanResult<? extends RepositoryObject> getSimilarScanResult() {
        return similarScanResult;
    }

    public void setSimilarScanResult(SimilarScanResult<? extends RepositoryObject> similarScanResult) {
        this.similarScanResult = similarScanResult;
    }
}

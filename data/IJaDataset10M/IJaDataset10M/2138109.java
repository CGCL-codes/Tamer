package org.saosis.core.controllers.emblbank;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.saosis.core.controllers.FormatUtility;
import org.saosis.core.controllers.StringUtility;
import org.saosis.core.models.AssemblyLocation;
import org.saosis.core.models.BaseSequence;
import org.saosis.core.models.Citation;
import org.saosis.core.models.DatabaseCrossReference;
import org.saosis.core.models.SAOSISException;
import org.saosis.core.models.Span;
import org.saosis.core.models.Version;
import org.saosis.core.models.VersionHistory;
import org.saosis.core.models.Vocabularies;

/**
 * An EMBL format utility.
 * 
 * @author Daniel Allen Prust (danprust@yahoo.com)
 * 
 */
public class EMBLBankFormatUtility extends FormatUtility {

    public static SimpleDateFormat ENTRY_DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy");

    protected static Pattern PATTERN_BOOK = Pattern.compile("^([^:]+):?\\s*([\\d]+)?-?([\\d]+)?$");

    protected static Pattern PATTERN_DB_REFERENCE = Pattern.compile("^([^;]+);([^;]+);(.*)\\.$");

    protected static Pattern PATTERN_ISSUE_REFERENCE = Pattern.compile("^(.*)\\s+([^\\(]+)\\(([^\\)]+)\\):([\\d]+)-?([\\d]+)?\\(?([\\d]+)?\\)?\\.?$");

    protected static Pattern PATTERN_LOCATION_YEAR = Pattern.compile("^(.*) \\(([\\d]+)\\)[\\.]*$");

    protected static Pattern PATTERN_PATENT = Pattern.compile("Patent number ([^,]+), ([^\\.]+)\\.(.*)?$");

    protected static Pattern PATTERN_SPAN = Pattern.compile("^([\\d]+)-([\\d]+)$");

    protected static Pattern PATTERN_SUBMISSION = Pattern.compile("^Submitted \\(([^\\)]+)\\).*\\.(.*)$");

    protected static Pattern PATTERN_THESIS = Pattern.compile("^Thesis \\(([^\\)]+)\\),+(.*)$");

    protected static Pattern PATTERN_VERSION_HISTORY = Pattern.compile("^([^\\s]+)\\s\\(Rel\\. ([\\d\\.]+),\\s+([^,]+)(,\\s+Version\\s+)?([\\d\\.]+)?\\)$");

    protected static Pattern PATTERN_VOLUME_REFERENCE = Pattern.compile("^(.*)\\s+([^:]+):([\\d]+)-?([\\d]+)?\\(?([\\d]+)?\\)?\\.?$");

    public static void add(BaseSequence sequence, String text) throws SAOSISException {
        FormatUtility.add(sequence, text);
    }

    public static void fromString(AssemblyLocation location, String string) throws SAOSISException {
        String[] tokens = string.split("[\\s]+");
        if (tokens.length == 3 || tokens.length == 4) {
            String tpaSpanField = tokens[0].trim();
            String accessionField = tokens[1].trim();
            String primarySpanField = tokens[2].trim();
            String complementField = "";
            if (tokens.length == 4) complementField = tokens[3].trim();
            location.setAccession(accessionField);
            EMBLBankFormatUtility.fromString(location.getTPASpan(), tpaSpanField);
            EMBLBankFormatUtility.fromString(location.getPrimarySpan(), primarySpanField);
            if ("c".equals(complementField)) location.setComplement(true); else if (complementField.isEmpty() == false) throw new SAOSISException(103000, new String[] { complementField });
        } else {
            throw new SAOSISException(103001, new String[] { string });
        }
    }

    public static void fromString(Citation citation, String string) throws SAOSISException {
        if ("Unpublished.".equals(string)) citation.setType(Vocabularies.CitationTypes.UNPUBLISHED); else if (string.startsWith("(in)")) fromStringForBook(citation, string); else if (string.startsWith("Submitted (")) fromStringForDataSubmission(citation, string); else if (string.startsWith("Thesis (")) fromStringForThesis(citation, string); else if (string.startsWith("Patent number ")) fromStringForPatent(citation, string); else fromStringForJournal(citation, string);
    }

    public static void fromString(DatabaseCrossReference crossReference, String string) throws SAOSISException {
        Matcher matcher = PATTERN_DB_REFERENCE.matcher(string);
        if (matcher.matches()) {
            String databaseIDField = matcher.group(1).trim();
            String primaryIDField = matcher.group(2).trim();
            String secondaryIDField = matcher.group(3).trim();
            crossReference.setDatabaseType(Vocabularies.DatabaseTypes.fromINSDCString(databaseIDField));
            crossReference.setPrimaryID(primaryIDField);
            crossReference.setSecondaryID(secondaryIDField);
        } else {
            throw new SAOSISException(103008, new String[] { string });
        }
    }

    public static void fromString(Span span, String string) throws SAOSISException {
        if ("not_available".equals(string)) {
            span.setStart(0);
            span.setEnd(0);
        } else {
            Matcher matcher = PATTERN_SPAN.matcher(string);
            if (matcher.matches()) {
                String startField = matcher.group(1).trim();
                String endField = matcher.group(2).trim();
                span.setStart(StringUtility.parseInt(startField));
                span.setEnd(StringUtility.parseInt(endField));
            } else {
                throw new SAOSISException(103009, new String[] { string });
            }
        }
    }

    public static void fromString(Version version, String string) throws SAOSISException {
        FormatUtility.fromString(version, string);
    }

    public static void fromString(VersionHistory history, String string) throws SAOSISException {
        String dateField = "";
        String releaseNumberField = "";
        String descriptionField = "";
        String versionNumberField = "";
        Matcher matcher = PATTERN_VERSION_HISTORY.matcher(string);
        if (matcher.matches()) {
            history.empty();
            dateField = matcher.group(1).trim();
            releaseNumberField = matcher.group(2).trim();
            descriptionField = matcher.group(3).trim();
            versionNumberField = matcher.group(5) == null ? "" : matcher.group(5).trim();
            history.setDate(StringUtility.parseDate(EMBLBankFormatUtility.ENTRY_DATE_FORMAT, dateField));
            EMBLBankFormatUtility.fromString(history.getRelease(), releaseNumberField);
            history.setType(Vocabularies.VersionHistoryTypes.fromEMBLBankString(descriptionField));
            EMBLBankFormatUtility.fromString(history.getVersion(), versionNumberField);
        } else {
            throw new SAOSISException(103013, new String[] { string });
        }
    }

    protected static void fromStringForBook(Citation citation, String string) throws SAOSISException {
        citation.setType(Vocabularies.CitationTypes.BOOK);
        String tokens[] = string.split(";");
        if (tokens.length == 3) {
            String editorsField = tokens[0].trim();
            String bookField = tokens[1].trim();
            String locationAndYearField = tokens[2].trim();
            if (editorsField.startsWith("(in)")) editorsField = editorsField.substring(4);
            if (editorsField.endsWith("(Eds.)")) editorsField = editorsField.substring(0, editorsField.length() - 6);
            editorsField = editorsField.trim();
            StringUtility.splitToList(citation.getEditors(), editorsField, ",", null);
            Matcher bookMatcher = PATTERN_BOOK.matcher(bookField);
            if (bookMatcher.matches()) {
                String nameField = bookMatcher.group(1).trim();
                String startPageField = bookMatcher.group(2) == null ? "0" : bookMatcher.group(2);
                String endPageField = bookMatcher.group(3) == null ? "0" : bookMatcher.group(3);
                citation.setName(nameField);
                citation.setStartPage(StringUtility.parseInt(startPageField));
                citation.setEndPage(StringUtility.parseInt(endPageField));
                if (citation.getEndPage() == 0) citation.setEndPage(citation.getStartPage());
            } else {
                throw new SAOSISException(103002, new String[] { bookField });
            }
            Matcher locationYearMatcher = PATTERN_LOCATION_YEAR.matcher(locationAndYearField);
            if (locationYearMatcher.matches()) {
                String locationField = locationYearMatcher.group(1).trim();
                String yearField = locationYearMatcher.group(2).trim();
                citation.setPublisher(locationField);
                citation.setYear(StringUtility.parseInt(yearField));
            } else {
                citation.setPublisher(locationAndYearField.trim());
            }
        } else {
            throw new SAOSISException(103003, new String[] { string });
        }
    }

    protected static void fromStringForDataSubmission(Citation citation, String string) throws SAOSISException {
        citation.setType(Vocabularies.CitationTypes.DATA_SUBMISSION);
        Matcher matcher = PATTERN_SUBMISSION.matcher(string);
        if (matcher.matches()) {
            String dateField = matcher.group(1).trim();
            String locatorField = matcher.group(2).trim();
            citation.setDate(StringUtility.parseDate(ENTRY_DATE_FORMAT, dateField));
            citation.setLocator(locatorField);
        } else {
            throw new SAOSISException(103004, new String[] { string });
        }
    }

    protected static void fromStringForJournal(Citation citation, String string) throws SAOSISException {
        citation.setType(Vocabularies.CitationTypes.JOURNAL);
        String nameField = "";
        String volumeField = "";
        String issueField = "";
        String startPageField = "0";
        String endPageField = "0";
        String yearField = "0";
        Matcher volumeMatcher = PATTERN_VOLUME_REFERENCE.matcher(string);
        Matcher issueMatcher = PATTERN_ISSUE_REFERENCE.matcher(string);
        if (issueMatcher.matches()) {
            nameField = issueMatcher.group(1).trim();
            volumeField = issueMatcher.group(2).trim();
            issueField = issueMatcher.group(3).trim();
            startPageField = issueMatcher.group(4).trim();
            endPageField = issueMatcher.group(5) == null ? endPageField : issueMatcher.group(5).trim();
            yearField = issueMatcher.group(6) == null ? yearField : issueMatcher.group(6).trim();
        } else if (volumeMatcher.matches()) {
            nameField = volumeMatcher.group(1).trim();
            volumeField = volumeMatcher.group(2).trim();
            startPageField = volumeMatcher.group(3).trim();
            endPageField = volumeMatcher.group(4) == null ? endPageField : volumeMatcher.group(4).trim();
            yearField = volumeMatcher.group(5) == null ? yearField : volumeMatcher.group(5).trim();
        } else {
            throw new SAOSISException(103005, new String[] { string });
        }
        citation.setName(nameField);
        citation.setVolume(volumeField);
        citation.setIssue(issueField);
        citation.setStartPage(StringUtility.parseInt(startPageField));
        citation.setEndPage(StringUtility.parseInt(endPageField));
        citation.setYear(StringUtility.parseInt(yearField));
    }

    protected static void fromStringForPatent(Citation citation, String string) throws SAOSISException {
        citation.setType(Vocabularies.CitationTypes.PATENT);
        Matcher matcher = PATTERN_PATENT.matcher(string);
        if (matcher.matches()) {
            String patentNumberField = matcher.group(1).trim();
            String patentDateField = matcher.group(2).trim();
            String patentApplicantsField = ".";
            if (matcher.group(3) != null) patentApplicantsField = matcher.group(3).trim();
            citation.setPatentNumber(patentNumberField);
            citation.setDate(StringUtility.parseDate(ENTRY_DATE_FORMAT, patentDateField));
            StringUtility.splitToList(citation.getApplicants(), patentApplicantsField, ";", ".");
        } else {
            throw new SAOSISException(103006, new String[] { string });
        }
    }

    protected static void fromStringForThesis(Citation citation, String string) throws SAOSISException {
        citation.setType(Vocabularies.CitationTypes.THESIS);
        Matcher matcher = PATTERN_THESIS.matcher(string);
        if (matcher.matches()) {
            String yearField = matcher.group(1).trim();
            String instituteField = matcher.group(2).trim();
            citation.setYear(StringUtility.parseInt(yearField));
            citation.setInstitute(instituteField);
        } else {
            throw new SAOSISException(103007, new String[] { string });
        }
    }

    public static String toString(AssemblyLocation location) {
        StringBuilder builder = new StringBuilder();
        builder.append(StringUtility.padRight(toString(location.getTPASpan()), 15));
        builder.append(StringUtility.padRight(location.getAccession(), 23));
        builder.append(StringUtility.padRight(toString(location.getPrimarySpan()), 17));
        if (location.isComplement()) builder.append("c");
        return builder.toString();
    }

    public static String toString(BaseSequence sequence) {
        return FormatUtility.toString(sequence);
    }

    public static String toString(Citation citation) throws SAOSISException {
        switch(citation.getType()) {
            case BOOK:
                return toStringForBook(citation);
            case DATA_SUBMISSION:
                return toStringForDataSubmission(citation);
            case DATABASE_ONLY:
                throw new SAOSISException(103014);
            case THESIS:
                return toStringForThesis(citation);
            case JOURNAL:
                return toStringForJournal(citation);
            case UNPUBLISHED:
                return "Unpublished.";
            case PATENT:
                return toStringForPatent(citation);
        }
        throw new IllegalArgumentException(citation.getType().toString());
    }

    public static String toString(DatabaseCrossReference value) {
        StringBuilder builder = new StringBuilder();
        builder.append(value.getDatabaseType().getINSDCString());
        builder.append("; ");
        builder.append(value.getPrimaryID());
        builder.append("; ");
        builder.append(value.getSecondaryID());
        builder.append(".");
        return builder.toString();
    }

    public static String toString(Span span) {
        if (span.getStart() == 0 && span.getEnd() == 0) return "not_available";
        StringBuilder builder = new StringBuilder();
        builder.append(span.getStart());
        builder.append("-");
        builder.append(span.getEnd());
        return builder.toString();
    }

    public static String toString(Version version) {
        return FormatUtility.toString(version);
    }

    public static String toString(VersionHistory history) {
        StringBuilder builder = new StringBuilder();
        builder.append(ENTRY_DATE_FORMAT.format(history.getDate()).toUpperCase());
        builder.append(" (Rel. ");
        builder.append(EMBLBankFormatUtility.toString(history.getRelease()));
        builder.append(", ");
        builder.append(history.getType().getEMBLBankString());
        if (history.getVersion().isEmpty() == false) {
            builder.append(", Version ");
            builder.append(EMBLBankFormatUtility.toString(history.getVersion()));
        }
        builder.append(")");
        return builder.toString();
    }

    protected static String toStringForBook(Citation citation) {
        StringBuilder builder = new StringBuilder();
        builder.append("(in) ");
        builder.append(StringUtility.join(citation.getEditors(), ", "));
        builder.append(" (Eds.);");
        builder.append(StringUtility.getNewline());
        builder.append(toStringForVolumeReference(citation, false));
        builder.append(";");
        builder.append(StringUtility.getNewline());
        builder.append(citation.getPublisher());
        if (citation.getYear() != 0) {
            builder.append(" (");
            builder.append(String.valueOf(citation.getYear()));
            builder.append(")");
        }
        return builder.toString();
    }

    protected static String toStringForDataSubmission(Citation citation) throws SAOSISException {
        StringBuilder builder = new StringBuilder();
        builder.append("Submitted (");
        builder.append(ENTRY_DATE_FORMAT.format(citation.getDate()).toUpperCase());
        builder.append(") to the EMBL/GenBank/DDBJ databases.");
        builder.append(StringUtility.getNewline());
        builder.append(citation.getLocator());
        return builder.toString();
    }

    protected static String toStringForJournal(Citation citation) {
        StringBuilder builder = new StringBuilder();
        builder.append(toStringForVolumeReference(citation, true));
        builder.append(".");
        return builder.toString();
    }

    protected static String toStringForPatent(Citation citation) throws SAOSISException {
        if (citation.getDate() == null) throw new SAOSISException(103011);
        StringBuilder builder = new StringBuilder();
        builder.append("Patent number ");
        builder.append(citation.getPatentNumber());
        builder.append(", ");
        builder.append(ENTRY_DATE_FORMAT.format(citation.getDate()).toUpperCase());
        builder.append(".");
        builder.append(StringUtility.getNewline());
        builder.append(StringUtility.join(citation.getApplicants(), "; "));
        builder.append(".");
        return builder.toString();
    }

    protected static String toStringForThesis(Citation citation) {
        StringBuilder builder = new StringBuilder();
        builder.append("Thesis (");
        builder.append(String.valueOf(citation.getYear()));
        builder.append(")");
        if (citation.getInstitute().isEmpty() == false) {
            builder.append(", ");
            builder.append(citation.getInstitute());
        }
        return builder.toString();
    }

    protected static String toStringForVolumeReference(Citation citation, boolean includeYear) {
        StringBuilder builder = new StringBuilder();
        builder.append(citation.getName());
        if (citation.getIssue().isEmpty() == false) {
            builder.append(" ");
            builder.append(citation.getVolume());
            builder.append("(");
            builder.append(citation.getIssue());
            builder.append(")");
        } else if (citation.getVolume().isEmpty() == false) {
            builder.append(" ");
            builder.append(citation.getVolume());
        }
        if (citation.getStartPage() != 0 || citation.getEndPage() != 0) builder.append(":");
        if (citation.getStartPage() != 0) {
            builder.append(String.valueOf(citation.getStartPage()));
            if (citation.getEndPage() != 0) builder.append("-");
        }
        if (citation.getEndPage() != 0) builder.append(String.valueOf(citation.getEndPage()));
        if (includeYear && citation.getYear() != 0) {
            builder.append("(");
            builder.append(String.valueOf(citation.getYear()));
            builder.append(")");
        }
        return builder.toString();
    }
}

package net.sf.jabref.custom;

/**
 * This class defines entry types for BibLatex support.
 */
public class BibLatexEntryTypes {

    public static final BibtexEntryType ARTICLE = new BibtexEntryType() {

        public String getName() {
            return "Article";
        }

        public String[] getRequiredFields() {
            return new String[] { "author", "title", "journaltitle", "year", "date" };
        }

        public String[] getOptionalFields() {
            return new String[] { "translator", "annotator", "commentator", "subtitle", "titleaddon", "editor", "editora", "editorb", "editorc", "journalsubtitle", "issuetitle", "issuesubtitle", "language", "origlanguage", "series", "volume", "number", "eid", "issue", "date", "month", "year", "pages", "version", "note", "issn", "addendum", "pubstate", "doi", "eprint", "eprintclass", "eprinttype", "url", "urldate" };
        }

        public String[] getPrimaryOptionalFields() {
            return new String[] { "subtitle", "editor", "series", "volume", "number", "eid", "issue", "date", "month", "year", "pages", "note", "issn", "doi", "eprint", "eprintclass", "eprinttype", "url", "urldate" };
        }

        public String describeRequiredFields() {
            return "";
        }

        public boolean hasAllRequiredFields(BibtexEntry entry, BibtexDatabase database) {
            return entry.allFieldsPresent(getRequiredFields(), database);
        }
    };

    public static final BibtexEntryType BOOK = new BibtexEntryType() {

        public String getName() {
            return "Book";
        }

        public String[] getRequiredFields() {
            return new String[] { "author", "title", "year", "date" };
        }

        public String[] getOptionalFields() {
            return new String[] { "editor", "editora", "editorb", "editorc", "translator", "annotator", "commentator", "introduction", "foreword", "afterword", "subtitle", "titleaddon", "maintitle", "mainsubtitle", "maintitleaddon", "language", "origlanguage", "volume", "part", "edition", "volumes", "series", "number", "note", "publisher", "location", "isbn", "chapter", "pages", "pagetotal", "addendum", "pubstate", "doi", "eprint", "eprintclass", "eprinttype", "url", "urldate" };
        }

        public String[] getPrimaryOptionalFields() {
            return new String[] { "editor", "subtitle", "titleaddon", "maintitle", "mainsubtitle", "maintitleaddon", "volume", "edition", "publisher", "isbn", "chapter", "pages", "pagetotal", "doi", "eprint", "eprintclass", "eprinttype", "url", "urldate" };
        }

        public String describeRequiredFields() {
            return "";
        }

        public boolean hasAllRequiredFields(BibtexEntry entry, BibtexDatabase database) {
            return entry.allFieldsPresent(getRequiredFields(), database);
        }
    };

    public static final BibtexEntryType INBOOK = new BibtexEntryType() {

        public String getName() {
            return "Inbook";
        }

        public String[] getRequiredFields() {
            return new String[] { "author", "title", "booktitle", "year", "date" };
        }

        public String[] getOptionalFields() {
            return new String[] { "bookauthor", "editor", "editora", "editorb", "editorc", "translator", "annotator", "commentator", "introduction", "foreword", "afterword", "subtitle", "titleaddon", "maintitle", "mainsubtitle", "maintitleaddon", "booksubtitle", "booktitleaddon", "language", "origlanguage", "volume", "part", "edition", "volumes", "series", "number", "note", "publisher", "location", "isbn", "chapter", "pages", "addendum", "pubstate", "doi", "eprint", "eprintclass", "eprinttype", "url", "urldate" };
        }

        public String[] getPrimaryOptionalFields() {
            return new String[] { "bookauthor", "editor", "subtitle", "titleaddon", "maintitle", "mainsubtitle", "maintitleaddon", "booksubtitle", "booktitleaddon", "volume", "edition", "publisher", "isbn", "chapter", "pages", "doi", "eprint", "eprintclass", "eprinttype", "url", "urldate" };
        }

        public String describeRequiredFields() {
            return "";
        }

        public boolean hasAllRequiredFields(BibtexEntry entry, BibtexDatabase database) {
            return entry.allFieldsPresent(getRequiredFields(), database);
        }
    };

    public static final BibtexEntryType BOOKINBOOK = new BibtexEntryType() {

        public String getName() {
            return "Bookinbook";
        }

        public String[] getRequiredFields() {
            return BibLatexEntryTypes.INBOOK.getRequiredFields();
        }

        public String[] getOptionalFields() {
            return BibLatexEntryTypes.INBOOK.getOptionalFields();
        }

        public String[] getPrimaryOptionalFields() {
            return BibLatexEntryTypes.INBOOK.getPrimaryOptionalFields();
        }

        public String describeRequiredFields() {
            return "";
        }

        public boolean hasAllRequiredFields(BibtexEntry entry, BibtexDatabase database) {
            return entry.allFieldsPresent(getRequiredFields(), database);
        }
    };

    public static final BibtexEntryType SUPPBOOK = new BibtexEntryType() {

        public String getName() {
            return "Suppbook";
        }

        public String[] getRequiredFields() {
            return BibLatexEntryTypes.INBOOK.getRequiredFields();
        }

        public String[] getOptionalFields() {
            return BibLatexEntryTypes.INBOOK.getOptionalFields();
        }

        public String[] getPrimaryOptionalFields() {
            return BibLatexEntryTypes.INBOOK.getPrimaryOptionalFields();
        }

        public String describeRequiredFields() {
            return "";
        }

        public boolean hasAllRequiredFields(BibtexEntry entry, BibtexDatabase database) {
            return entry.allFieldsPresent(getRequiredFields(), database);
        }
    };

    public static final BibtexEntryType BOOKLET = new BibtexEntryType() {

        public String getName() {
            return "Booklet";
        }

        public String[] getRequiredFields() {
            return new String[] { "author", "editor", "title", "year", "date" };
        }

        public String[] getOptionalFields() {
            return new String[] { "subtitle", "titleaddon", "language", "howpublished", "type", "note", "location", "chapter", "pages", "pagetotal", "addendum", "pubstate", "doi", "eprint", "eprintclass", "eprinttype", "url", "urldate" };
        }

        public String[] getPrimaryOptionalFields() {
            return new String[] { "subtitle", "titleaddon", "howpublished", "chapter", "pages", "doi", "eprint", "eprintclass", "eprinttype", "url", "urldate" };
        }

        public String describeRequiredFields() {
            return "";
        }

        public boolean hasAllRequiredFields(BibtexEntry entry, BibtexDatabase database) {
            return entry.allFieldsPresent(getRequiredFields(), database);
        }
    };

    public static final BibtexEntryType COLLECTION = new BibtexEntryType() {

        public String getName() {
            return "Collection";
        }

        public String[] getRequiredFields() {
            return new String[] { "editor", "title", "year", "date" };
        }

        public String[] getOptionalFields() {
            return new String[] { "editora", "editorb", "editorc", "translator", "annotator", "commentator", "introduction", "foreword", "afterword", "subtitle", "titleaddon", "maintitle", "mainsubtitle", "maintitleaddon", "language", "origlanguage", "volume", "part", "edition", "volumes", "series", "number", "note", "publisher", "location", "isbn", "chapter", "pages", "pagetotal", "addendum", "pubstate", "doi", "eprint", "eprintclass", "eprinttype", "url", "urldate" };
        }

        public String[] getPrimaryOptionalFields() {
            return new String[] { "translator", "subtitle", "titleaddon", "maintitle", "mainsubtitle", "maintitleaddon", "volume", "edition", "publisher", "isbn", "chapter", "pages", "doi", "eprint", "eprintclass", "eprinttype", "url", "urldate" };
        }

        public String describeRequiredFields() {
            return "";
        }

        public boolean hasAllRequiredFields(BibtexEntry entry, BibtexDatabase database) {
            return entry.allFieldsPresent(getRequiredFields(), database);
        }
    };

    public static final BibtexEntryType INCOLLECTION = new BibtexEntryType() {

        public String getName() {
            return "Incollection";
        }

        public String[] getRequiredFields() {
            return new String[] { "author", "editor", "title", "booktitle", "year", "date" };
        }

        public String[] getOptionalFields() {
            return new String[] { "editora", "editorb", "editorc", "translator", "annotator", "commentator", "introduction", "foreword", "afterword", "subtitle", "titleaddon", "maintitle", "mainsubtitle", "maintitleaddon", "booksubtitle", "booktitleaddon", "language", "origlanguage", "volume", "part", "edition", "volumes", "series", "number", "note", "publisher", "location", "isbn", "chapter", "pages", "addendum", "pubstate", "doi", "eprint", "eprintclass", "eprinttype", "url", "urldate" };
        }

        public String[] getPrimaryOptionalFields() {
            return new String[] { "translator", "subtitle", "titleaddon", "maintitle", "mainsubtitle", "maintitleaddon", "booksubtitle", "booktitleaddon", "volume", "edition", "publisher", "isbn", "chapter", "pages", "doi", "eprint", "eprintclass", "eprinttype", "url", "urldate" };
        }

        public String describeRequiredFields() {
            return "";
        }

        public boolean hasAllRequiredFields(BibtexEntry entry, BibtexDatabase database) {
            return entry.allFieldsPresent(getRequiredFields(), database);
        }
    };

    public static final BibtexEntryType SUPPCOLLECTION = new BibtexEntryType() {

        public String getName() {
            return "Suppcollection";
        }

        public String[] getRequiredFields() {
            return BibLatexEntryTypes.INCOLLECTION.getRequiredFields();
        }

        public String[] getOptionalFields() {
            return BibLatexEntryTypes.INCOLLECTION.getOptionalFields();
        }

        public String[] getPrimaryOptionalFields() {
            return BibLatexEntryTypes.INCOLLECTION.getPrimaryOptionalFields();
        }

        public String describeRequiredFields() {
            return "";
        }

        public boolean hasAllRequiredFields(BibtexEntry entry, BibtexDatabase database) {
            return entry.allFieldsPresent(getRequiredFields(), database);
        }
    };

    public static final BibtexEntryType MANUAL = new BibtexEntryType() {

        public String getName() {
            return "Manual";
        }

        public String[] getRequiredFields() {
            return new String[] { "author", "editor", "title", "year", "date" };
        }

        public String[] getOptionalFields() {
            return new String[] { "subtitle", "titleaddon", "language", "edition", "type", "series", "number", "version", "note", "organization", "publisher", "location", "isbn", "chapter", "pages", "pagetotal", "addendum", "pubstate", "doi", "eprint", "eprintclass", "eprinttype", "url", "urldate" };
        }

        public String[] getPrimaryOptionalFields() {
            return new String[] { "subtitle", "titleaddon", "edition", "publisher", "isbn", "chapter", "pages", "doi", "eprint", "eprintclass", "eprinttype", "url", "urldate" };
        }

        public String describeRequiredFields() {
            return "";
        }

        public boolean hasAllRequiredFields(BibtexEntry entry, BibtexDatabase database) {
            return entry.allFieldsPresent(getRequiredFields(), database);
        }
    };

    public static final BibtexEntryType MISC = new BibtexEntryType() {

        public String getName() {
            return "Misc";
        }

        public String[] getRequiredFields() {
            return new String[] { "author", "editor", "title", "year", "date" };
        }

        public String[] getOptionalFields() {
            return new String[] { "subtitle", "titleaddon", "language", "howpublished", "type", "version", "note", "organization", "location", "date", "month", "year", "addendum", "pubstate", "doi", "eprint", "eprintclass", "eprinttype", "url", "urldate" };
        }

        public String[] getPrimaryOptionalFields() {
            return new String[] { "subtitle", "titleaddon", "howpublished", "location", "doi", "eprint", "eprintclass", "eprinttype", "url", "urldate" };
        }

        public String describeRequiredFields() {
            return "";
        }

        public boolean hasAllRequiredFields(BibtexEntry entry, BibtexDatabase database) {
            return entry.allFieldsPresent(getRequiredFields(), database);
        }
    };

    public static final BibtexEntryType ONLINE = new BibtexEntryType() {

        public String getName() {
            return "Online";
        }

        public String[] getRequiredFields() {
            return new String[] { "author", "editor", "title", "year", "date", "url" };
        }

        public String[] getOptionalFields() {
            return new String[] { "subtitle", "titleaddon", "language", "version", "note", "organization", "date", "month", "year", "addendum", "pubstate", "urldate" };
        }

        public String[] getPrimaryOptionalFields() {
            return new String[] { "subtitle", "titleaddon", "note", "organization", "urldate" };
        }

        public String describeRequiredFields() {
            return "";
        }

        public boolean hasAllRequiredFields(BibtexEntry entry, BibtexDatabase database) {
            return entry.allFieldsPresent(getRequiredFields(), database);
        }
    };

    public static final BibtexEntryType PATENT = new BibtexEntryType() {

        public String getName() {
            return "Patent";
        }

        public String[] getRequiredFields() {
            return new String[] { "author", "title", "number", "year", "date" };
        }

        public String[] getOptionalFields() {
            return new String[] { "holder", "subtitle", "titleaddon", "type", "version", "location", "note", "date", "month", "year", "addendum", "pubstate", "doi", "eprint", "eprintclass", "eprinttype", "url", "urldate" };
        }

        public String[] getPrimaryOptionalFields() {
            return new String[] { "holder", "subtitle", "titleaddon", "doi", "eprint", "eprintclass", "eprinttype", "url", "urldate" };
        }

        public String describeRequiredFields() {
            return "";
        }

        public boolean hasAllRequiredFields(BibtexEntry entry, BibtexDatabase database) {
            return entry.allFieldsPresent(getRequiredFields(), database);
        }
    };

    public static final BibtexEntryType PERIODICAL = new BibtexEntryType() {

        public String getName() {
            return "Periodical";
        }

        public String[] getRequiredFields() {
            return new String[] { "editor", "title", "year", "date" };
        }

        public String[] getOptionalFields() {
            return new String[] { "editora", "editorb", "editorc", "subtitle", "issuetitle", "issuesubtitle", "language", "series", "volume", "number", "issue", "date", "month", "year", "note", "issn", "addendum", "pubstate", "doi", "eprint", "eprintclass", "eprinttype", "url", "urldate" };
        }

        public String[] getPrimaryOptionalFields() {
            return new String[] { "subtitle", "issuetitle", "issuesubtitle", "issn", "doi", "eprint", "eprintclass", "eprinttype", "url", "urldate" };
        }

        public String describeRequiredFields() {
            return "";
        }

        public boolean hasAllRequiredFields(BibtexEntry entry, BibtexDatabase database) {
            return entry.allFieldsPresent(getRequiredFields(), database);
        }
    };

    public static final BibtexEntryType SUPPPERIODICAL = new BibtexEntryType() {

        public String getName() {
            return "Suppperiodical";
        }

        public String[] getRequiredFields() {
            return BibLatexEntryTypes.ARTICLE.getRequiredFields();
        }

        public String[] getOptionalFields() {
            return BibLatexEntryTypes.ARTICLE.getOptionalFields();
        }

        public String[] getPrimaryOptionalFields() {
            return BibLatexEntryTypes.ARTICLE.getPrimaryOptionalFields();
        }

        public String describeRequiredFields() {
            return "";
        }

        public boolean hasAllRequiredFields(BibtexEntry entry, BibtexDatabase database) {
            return entry.allFieldsPresent(getRequiredFields(), database);
        }
    };

    public static final BibtexEntryType PROCEEDINGS = new BibtexEntryType() {

        public String getName() {
            return "Proceedings";
        }

        public String[] getRequiredFields() {
            return new String[] { "editor", "title", "year", "date" };
        }

        public String[] getOptionalFields() {
            return new String[] { "subtitle", "titleaddon", "maintitle", "mainsubtitle", "maintitleaddon", "eventtitle", "eventdate", "venue", "language", "volume", "part", "volumes", "series", "number", "note", "organization", "publisher", "location", "month", "isbn", "chapter", "pages", "pagetotal", "addendum", "pubstate", "doi", "eprint", "eprintclass", "eprinttype", "url", "urldate" };
        }

        public String[] getPrimaryOptionalFields() {
            return new String[] { "subtitle", "titleaddon", "maintitle", "mainsubtitle", "maintitleaddon", "eventtitle", "volume", "publisher", "isbn", "chapter", "pages", "pagetotal", "doi", "eprint", "eprintclass", "eprinttype", "url", "urldate" };
        }

        public String describeRequiredFields() {
            return "";
        }

        public boolean hasAllRequiredFields(BibtexEntry entry, BibtexDatabase database) {
            return entry.allFieldsPresent(getRequiredFields(), database);
        }
    };

    public static final BibtexEntryType INPROCEEDINGS = new BibtexEntryType() {

        public String getName() {
            return "Inproceedings";
        }

        public String[] getRequiredFields() {
            return new String[] { "author", "editor", "title", "booktitle", "year", "date" };
        }

        public String[] getOptionalFields() {
            return new String[] { "subtitle", "titleaddon", "maintitle", "mainsubtitle", "maintitleaddon", "booksubtitle", "booktitleaddon", "eventtitle", "eventdate", "venue", "language", "volume", "part", "volumes", "series", "number", "note", "organization", "publisher", "location", "month", "isbn", "chapter", "pages", "addendum", "pubstate", "doi", "eprint", "eprintclass", "eprinttype", "url", "urldate" };
        }

        public String[] getPrimaryOptionalFields() {
            return new String[] { "subtitle", "titleaddon", "maintitle", "mainsubtitle", "maintitleaddon", "booksubtitle", "booktitleaddon", "eventtitle", "volume", "publisher", "isbn", "chapter", "pages", "doi", "eprint", "eprintclass", "eprinttype", "url", "urldate" };
        }

        public String describeRequiredFields() {
            return "";
        }

        public boolean hasAllRequiredFields(BibtexEntry entry, BibtexDatabase database) {
            return entry.allFieldsPresent(getRequiredFields(), database);
        }
    };

    public static final BibtexEntryType REFERENCE = new BibtexEntryType() {

        public String getName() {
            return "Reference";
        }

        public String[] getRequiredFields() {
            return BibLatexEntryTypes.COLLECTION.getRequiredFields();
        }

        public String[] getOptionalFields() {
            return BibLatexEntryTypes.COLLECTION.getOptionalFields();
        }

        public String[] getPrimaryOptionalFields() {
            return BibLatexEntryTypes.COLLECTION.getPrimaryOptionalFields();
        }

        public String describeRequiredFields() {
            return "";
        }

        public boolean hasAllRequiredFields(BibtexEntry entry, BibtexDatabase database) {
            return entry.allFieldsPresent(getRequiredFields(), database);
        }
    };

    public static final BibtexEntryType INREFERENCE = new BibtexEntryType() {

        public String getName() {
            return "Inreference";
        }

        public String[] getRequiredFields() {
            return BibLatexEntryTypes.INCOLLECTION.getRequiredFields();
        }

        public String[] getOptionalFields() {
            return BibLatexEntryTypes.INCOLLECTION.getOptionalFields();
        }

        public String[] getPrimaryOptionalFields() {
            return BibLatexEntryTypes.INCOLLECTION.getPrimaryOptionalFields();
        }

        public String describeRequiredFields() {
            return "";
        }

        public boolean hasAllRequiredFields(BibtexEntry entry, BibtexDatabase database) {
            return entry.allFieldsPresent(getRequiredFields(), database);
        }
    };

    public static final BibtexEntryType REPORT = new BibtexEntryType() {

        public String getName() {
            return "Report";
        }

        public String[] getRequiredFields() {
            return new String[] { "author", "title", "type", "institution", "year", "date" };
        }

        public String[] getOptionalFields() {
            return new String[] { "subtitle", "titleaddon", "language", "number", "version", "note", "location", "month", "isrn", "chapter", "pages", "pagetotal", "addendum", "pubstate", "doi", "eprint", "eprintclass", "eprinttype", "url", "urldate" };
        }

        public String[] getPrimaryOptionalFields() {
            return new String[] { "subtitle", "titleaddon", "number", "isrn", "chapter", "pages", "pagetotal", "doi", "eprint", "eprintclass", "eprinttype", "url", "urldate" };
        }

        public String describeRequiredFields() {
            return "";
        }

        public boolean hasAllRequiredFields(BibtexEntry entry, BibtexDatabase database) {
            return entry.allFieldsPresent(getRequiredFields(), database);
        }
    };

    public static final BibtexEntryType SET = new BibtexEntryType() {

        public String getName() {
            return "Set";
        }

        public String[] getRequiredFields() {
            return new String[] { "entryset", "crossref" };
        }

        public String[] getOptionalFields() {
            return null;
        }

        public String[] getPrimaryOptionalFields() {
            return null;
        }

        public String describeRequiredFields() {
            return "";
        }

        public boolean hasAllRequiredFields(BibtexEntry entry, BibtexDatabase database) {
            return entry.allFieldsPresent(getRequiredFields(), database);
        }
    };

    public static final BibtexEntryType THESIS = new BibtexEntryType() {

        public String getName() {
            return "Thesis";
        }

        public String[] getRequiredFields() {
            return new String[] { "author", "title", "type", "institution", "year", "date" };
        }

        public String[] getOptionalFields() {
            return new String[] { "subtitle", "titleaddon", "language", "note", "location", "month", "chapter", "pages", "pagetotal", "addendum", "pubstate", "doi", "eprint", "eprintclass", "eprinttype", "url", "urldate" };
        }

        public String[] getPrimaryOptionalFields() {
            return new String[] { "subtitle", "titleaddon", "chapter", "pages", "pagetotal", "doi", "eprint", "eprintclass", "eprinttype", "url", "urldate" };
        }

        public String describeRequiredFields() {
            return "";
        }

        public boolean hasAllRequiredFields(BibtexEntry entry, BibtexDatabase database) {
            return entry.allFieldsPresent(getRequiredFields(), database);
        }
    };

    public static final BibtexEntryType UNPUBLISHED = new BibtexEntryType() {

        public String getName() {
            return "Unpublished";
        }

        public String[] getRequiredFields() {
            return new String[] { "author", "title", "year", "date" };
        }

        public String[] getOptionalFields() {
            return new String[] { "subtitle", "titleaddon", "language", "howpublished", "note", "location", "date", "month", "year", "addendum", "pubstate", "url", "urldate" };
        }

        public String[] getPrimaryOptionalFields() {
            return new String[] { "subtitle", "titleaddon", "howpublished", "pubstate", "url", "urldate" };
        }

        public String describeRequiredFields() {
            return "";
        }

        public boolean hasAllRequiredFields(BibtexEntry entry, BibtexDatabase database) {
            return entry.allFieldsPresent(getRequiredFields(), database);
        }
    };

    public static final BibtexEntryType CONFERENCE = new BibtexEntryType() {

        public String getName() {
            return "Conference";
        }

        public String[] getRequiredFields() {
            return BibLatexEntryTypes.INPROCEEDINGS.getRequiredFields();
        }

        public String[] getOptionalFields() {
            return BibLatexEntryTypes.INPROCEEDINGS.getOptionalFields();
        }

        public String[] getPrimaryOptionalFields() {
            return BibLatexEntryTypes.INPROCEEDINGS.getPrimaryOptionalFields();
        }

        public String describeRequiredFields() {
            return "";
        }

        public boolean hasAllRequiredFields(BibtexEntry entry, BibtexDatabase database) {
            return entry.allFieldsPresent(getRequiredFields(), database);
        }
    };

    public static final BibtexEntryType ELECTRONIC = new BibtexEntryType() {

        public String getName() {
            return "Electronic";
        }

        public String[] getRequiredFields() {
            return BibLatexEntryTypes.ONLINE.getRequiredFields();
        }

        public String[] getOptionalFields() {
            return BibLatexEntryTypes.ONLINE.getOptionalFields();
        }

        public String[] getPrimaryOptionalFields() {
            return BibLatexEntryTypes.ONLINE.getPrimaryOptionalFields();
        }

        public String describeRequiredFields() {
            return "";
        }

        public boolean hasAllRequiredFields(BibtexEntry entry, BibtexDatabase database) {
            return entry.allFieldsPresent(getRequiredFields(), database);
        }
    };

    public static final BibtexEntryType MASTERSTHESIS = new BibtexEntryType() {

        public String getName() {
            return "Mastersthesis";
        }

        public String[] getRequiredFields() {
            return new String[] { "author", "title", "institution", "year", "date" };
        }

        public String[] getOptionalFields() {
            return new String[] { "subtitle", "titleaddon", "type", "language", "note", "location", "month", "chapter", "pages", "pagetotal", "addendum", "pubstate", "doi", "eprint", "eprintclass", "eprinttype", "url", "urldate" };
        }

        public String[] getPrimaryOptionalFields() {
            return new String[] { "subtitle", "titleaddon", "type", "chapter", "pages", "pagetotal", "doi", "eprint", "eprintclass", "eprinttype", "url", "urldate" };
        }

        public String describeRequiredFields() {
            return "";
        }

        public boolean hasAllRequiredFields(BibtexEntry entry, BibtexDatabase database) {
            return entry.allFieldsPresent(getRequiredFields(), database);
        }
    };

    public static final BibtexEntryType PHDTHESIS = new BibtexEntryType() {

        public String getName() {
            return "Phdthesis";
        }

        public String[] getRequiredFields() {
            return new String[] { "author", "title", "institution", "year", "date" };
        }

        public String[] getOptionalFields() {
            return new String[] { "subtitle", "titleaddon", "type", "language", "note", "location", "month", "chapter", "pages", "pagetotal", "addendum", "pubstate", "doi", "eprint", "eprintclass", "eprinttype", "url", "urldate" };
        }

        public String[] getPrimaryOptionalFields() {
            return new String[] { "subtitle", "titleaddon", "type", "chapter", "pages", "pagetotal", "doi", "eprint", "eprintclass", "eprinttype", "url", "urldate" };
        }

        public String describeRequiredFields() {
            return "";
        }

        public boolean hasAllRequiredFields(BibtexEntry entry, BibtexDatabase database) {
            return entry.allFieldsPresent(getRequiredFields(), database);
        }
    };

    public static final BibtexEntryType TECHREPORT = new BibtexEntryType() {

        public String getName() {
            return "Techreport";
        }

        public String[] getRequiredFields() {
            return new String[] { "author", "title", "institution", "year", "date" };
        }

        public String[] getOptionalFields() {
            return new String[] { "subtitle", "titleaddon", "type", "language", "number", "version", "note", "location", "month", "isrn", "chapter", "pages", "pagetotal", "addendum", "pubstate", "doi", "eprint", "eprintclass", "eprinttype", "url", "urldate" };
        }

        public String[] getPrimaryOptionalFields() {
            return new String[] { "subtitle", "titleaddon", "type", "number", "isrn", "chapter", "pages", "pagetotal", "doi", "eprint", "eprintclass", "eprinttype", "url", "urldate" };
        }

        public String describeRequiredFields() {
            return "";
        }

        public boolean hasAllRequiredFields(BibtexEntry entry, BibtexDatabase database) {
            return entry.allFieldsPresent(getRequiredFields(), database);
        }
    };

    public static final BibtexEntryType WWW = new BibtexEntryType() {

        public String getName() {
            return "Www";
        }

        public String[] getRequiredFields() {
            return BibLatexEntryTypes.ONLINE.getRequiredFields();
        }

        public String[] getOptionalFields() {
            return BibLatexEntryTypes.ONLINE.getOptionalFields();
        }

        public String[] getPrimaryOptionalFields() {
            return BibLatexEntryTypes.ONLINE.getPrimaryOptionalFields();
        }

        public String describeRequiredFields() {
            return "";
        }

        public boolean hasAllRequiredFields(BibtexEntry entry, BibtexDatabase database) {
            return entry.allFieldsPresent(getRequiredFields(), database);
        }
    };
}

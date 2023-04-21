package de.glossmaker.bib.datastructure;

/**
 * The entries CONFERENCE and INPROCEEDINGS have the same fields.
 * 
 * @author Markus Flingelli
 *
 */
abstract class ACommonConference extends ABibItem {

    ACommonConference(String key, EBibTeXReference reference) {
        super(key, reference);
    }

    public ACommonConference(String key, EBibTeXReference reference, String title, String author, String bookTitle, String year) {
        super(key, reference);
        setTitle(title);
        setAuthor(author);
        setBookTitle(bookTitle);
        setYear(year);
    }

    public String toString() {
        String result = "@" + getReference() + "{" + getKey() + "," + System.getProperty("line.separator");
        result += "  author = {" + getAuthor() + "}," + System.getProperty("line.separator");
        result += "  title = {" + getTitle() + "}," + System.getProperty("line.separator");
        result += "  booktitle = {" + getBookTitle() + "}," + System.getProperty("line.separator");
        result += "  year = {" + getYear() + "}," + System.getProperty("line.separator");
        if (getEditor() != null) {
            result += "  editor = {" + getEditor() + "}," + System.getProperty("line.separator");
        }
        if (getVolume() != null) {
            result += "  volume = {" + getVolume() + "}," + System.getProperty("line.separator");
        }
        if (getNumber() != null) {
            result += "  number = {" + getNumber() + "}," + System.getProperty("line.separator");
        }
        if (getSeries() != null) {
            result += "  series = {" + getSeries() + "}," + System.getProperty("line.separator");
        }
        if (getPages() != null) {
            result += "  pages = {" + getPages() + "}," + System.getProperty("line.separator");
        }
        if (getAddress() != null) {
            result += "  address= {" + getAddress() + "}," + System.getProperty("line.separator");
        }
        if (getMonth() != null) {
            result += "  month = {" + getMonth() + "}," + System.getProperty("line.separator");
        }
        if (getOrganization() != null) {
            result += "  organization = {" + getOrganization() + "}," + System.getProperty("line.separator");
        }
        if (getPublisher() != null) {
            result += "  publisher = {" + getPublisher() + "}," + System.getProperty("line.separator");
        }
        if (getNote() != null) {
            result += "  note = {" + getNote() + "}," + System.getProperty("line.separator");
        }
        result += "}";
        return result;
    }

    @Override
    public boolean isItemValid() {
        boolean result = getKey() != null && getKey().length() > 0;
        result &= getAuthor() != null && getAuthor().length() > 0;
        result &= getTitle() != null && getTitle().length() > 0;
        result &= getBookTitle() != null && getBookTitle().length() > 0;
        result &= getYear() != null;
        return result;
    }
}

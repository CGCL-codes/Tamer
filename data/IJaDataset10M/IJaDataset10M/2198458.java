package edu.ucsd.ncmir.ontology.browser.bioportal;

class BioportalRow implements Comparable {

    private String _id;

    private String _title;

    private String _format;

    BioportalRow(String id, String title, String format) {
        this._id = id;
        this._title = title.replaceAll(" +", " ").trim();
        this._format = format;
    }

    public String getDownloadReference() {
        return String.format(BioportalStrings.LOADBASE, this._id);
    }

    public String getExploreReference() {
        return String.format(BioportalStrings.EXPLOREBASE, this._id);
    }

    /**
     * @return the format
     */
    public String getFormat() {
        return this._format;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return this._title;
    }

    @Override
    public String toString() {
        return this.getTitle();
    }

    public int compareTo(Object o) {
        BioportalRow r = (BioportalRow) o;
        return this.getTitle().toLowerCase().compareTo(r.getTitle().toLowerCase());
    }
}

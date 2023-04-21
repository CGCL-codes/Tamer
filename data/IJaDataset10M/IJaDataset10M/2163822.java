package reports.utility.datamodel.administration;

import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class ACCESSION_SERIES_KEY_1 implements Serializable {

    private Integer library_id;

    private String series_name;

    /** Creates a new instance of ACCESSION_SERIES_KEY */
    public ACCESSION_SERIES_KEY_1() {
    }

    public Integer getLibrary_id() {
        return library_id;
    }

    public void setLibrary_id(Integer library_id) {
        this.library_id = library_id;
    }

    public String getSeries_name() {
        return series_name;
    }

    public void setSeries_name(String series_name) {
        this.series_name = series_name;
    }

    public boolean equals(Object obj) {
        boolean retValue;
        retValue = super.equals(obj);
        return retValue;
    }

    public int hashCode() {
        int retValue;
        retValue = super.hashCode();
        return retValue;
    }
}

package br.ufmg.lcc.pcollecta.dto;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("T")
public class CSVFile extends DataRepository {

    /**
	 * Physical directory for CSV text files of the repository
	 */
    private String directory;

    @Column(name = "DIRECTORY")
    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }
}

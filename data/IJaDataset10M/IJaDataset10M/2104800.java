package fi.passiba.services.biblestudy.datamining.persistance;

import fi.passiba.hibernate.AuditableEntity;
import fi.passiba.services.biblestudy.persistance.Book;
import fi.passiba.services.persistance.Status;
import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Book entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "bookdatasource")
@AttributeOverride(name = "id", column = @Column(name = "bookdatasource_id"))
public class Bookdatasource extends AuditableEntity {

    private String weburlName;

    private String status;

    private String scraperConfigFile;

    private String configFileDir;

    private String outputDir;

    private String outputSubDir;

    private String outputFileName;

    private Book book;

    /** default constructor */
    public Bookdatasource() {
    }

    @OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_book_id", referencedColumnName = "book_id", nullable = true)
    public Book getBook() {
        return this.book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Column(name = "status", unique = false, nullable = false, insertable = true, updatable = true, length = 50)
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "weburlname", unique = false, nullable = false, insertable = true, updatable = true, length = 50)
    public String getWeburlName() {
        return weburlName;
    }

    public void setWeburlName(String weburlName) {
        this.weburlName = weburlName;
    }

    @Column(name = "configfiledir", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
    public String getConfigFileDir() {
        return configFileDir;
    }

    public void setConfigFileDir(String configFileDir) {
        this.configFileDir = configFileDir;
    }

    @Column(name = "configfile", unique = false, nullable = true, insertable = true, updatable = true, length = 15)
    public String getScraperConfigFile() {
        return scraperConfigFile;
    }

    public void setScraperConfigFile(String scraperConfigFile) {
        this.scraperConfigFile = scraperConfigFile;
    }

    @Column(name = "outputdir", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    @Column(name = "outputfilename", unique = false, nullable = true, insertable = true, updatable = true, length = 15)
    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    @Column(name = "outputsubdir", unique = false, nullable = true, insertable = true, updatable = true, length = 15)
    public String getOutputSubDir() {
        return outputSubDir;
    }

    public void setOutputSubDir(String outputSubDir) {
        this.outputSubDir = outputSubDir;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Bookdatasource other = (Bookdatasource) obj;
        if (this.getId() != other.getId() && (this.getId() == null || !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }
}

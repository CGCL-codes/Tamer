package oopex.eclipselink1.jpa.inheritance.model;

import java.util.Formatter;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "FILETYPE")
@DiscriminatorValue("FILE")
public class FileAttachment extends Attachment {

    private String filename;

    private boolean virusChecked;

    @Lob
    private byte[] rawData;

    public FileAttachment() {
    }

    public FileAttachment(String filename) {
        super("text/plain");
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public boolean isVirusChecked() {
        return virusChecked;
    }

    public void setVirusChecked(boolean virusChecked) {
        this.virusChecked = virusChecked;
    }

    public byte[] getRawData() {
        return rawData;
    }

    public void setRawData(byte[] rawData) {
        this.rawData = rawData;
    }

    public String toString() {
        return super.toString() + " : " + new Formatter().format("FileAttachment: filename: %s, virus checked: %b, size: %d", filename, virusChecked, (rawData != null) ? rawData.length : 0).toString();
    }
}

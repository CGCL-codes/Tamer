package oopex.eclipselink1.jpax.inheritance.model;

public class PDFAttachment extends FileAttachment {

    public PDFAttachment() {
        setMimeType("application/pdf");
    }

    public PDFAttachment(String filename) {
        super(filename);
        setMimeType("application/pdf");
    }
}

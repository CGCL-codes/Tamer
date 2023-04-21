package org.openscience.jreferences.io;

import org.openscience.jreferences.*;
import java.util.*;
import java.io.IOException;
import java.io.Writer;
import net.sf.bibtexml.*;
import org.exolab.castor.types.GYear;

public class BibTexWriter extends ReferenceFileWriter {

    private LoggingTool logger;

    public BibTexWriter(Writer writer) {
        super(writer);
        logger = new LoggingTool(this.getClass().getName());
    }

    public void write(net.sf.bibtexml.File file) throws IOException {
        super.write(file);
    }

    public void write(Entry entry) throws IOException {
        if (entry.getArticle() != null) {
            writer.write("@ARTICLE{");
            writer.write(entry.getId() + ",\n");
            writeArticle(entry.getId(), entry.getArticle());
            writer.write("}\n");
        } else if (entry.getMisc() != null) {
            writer.write("@MISC{");
            writer.write(entry.getId() + ",\n");
            writeMisc(entry.getId(), entry.getMisc());
            writer.write("}\n");
        } else if (entry.getUnpublished() != null) {
            writer.write("@UNPUBLISHED{");
            writer.write(entry.getId() + ",\n");
            writeUnpublished(entry.getId(), entry.getUnpublished());
            writer.write("}\n");
        } else if (entry.getInbook() != null) {
            writer.write("@INBOOK{");
            writer.write(entry.getId() + ",\n");
            writeInbook(entry.getId(), entry.getInbook());
            writer.write("}\n");
        } else if (entry.getBook() != null) {
            writer.write("@BOOK{");
            writer.write(entry.getId() + ",\n");
            writeBook(entry.getId(), entry.getBook());
            writer.write("}\n");
        } else {
            super.write(entry);
        }
        writer.flush();
    }

    private void writeKeyValue(String name, int value) throws IOException {
        if (value != 0) {
            writeKeyValue(name, new Integer(value).toString());
        } else {
            logger.debug("Field " + name + " has zero int value");
        }
    }

    private void writeKeyValue(String name, String value) throws IOException {
        if (value != null && value.length() > 0) {
            writer.write("  ");
            writer.write(name);
            writer.write(" = \"");
            writer.write(value);
            writer.write("\",\n");
        } else {
            logger.debug("Field " + name + " is of zero length");
        }
    }

    private void writeKeyValue(String name, GYear year) throws IOException {
        if (year != null) {
            writeKeyValue(name, year.toString());
        } else {
            logger.debug("Field " + name + " is of null");
        }
    }

    private void formatExact(String name, String value) throws IOException {
        if (value != null && value.length() > 0) {
            writer.write("  ");
            writer.write(name);
            writer.write(" = \"{");
            writer.write(value);
            writer.write("}\",\n");
        } else {
            logger.debug("Field " + name + " is of zero length");
        }
    }

    protected void writeArticle(String id, Article reference) throws IOException {
        writeKeyValue("AUTHOR", reference.getAuthor());
        writeKeyValue("TITLE", reference.getTitle());
        writeKeyValue("JOURNAL", reference.getJournal());
        writeKeyValue("YEAR", reference.getYear());
        writeKeyValue("MONTH", reference.getMonth());
        writeKeyValue("VOLUME", reference.getVolume());
        writeKeyValue("NUMBER", reference.getNumber());
        writeKeyValue("PAGES", reference.getPages());
        writeKeyValue("NOTE", reference.getNote());
    }

    protected void writeMisc(String id, Misc reference) throws IOException {
        writeKeyValue("AUTHOR", reference.getAuthor());
        writeKeyValue("TITLE", reference.getTitle());
        writeKeyValue("JOURNAL", reference.getHowpublished());
        writeKeyValue("YEAR", reference.getYear().toString());
        writeKeyValue("MONTH", reference.getMonth());
        writeKeyValue("NOTE", reference.getNote());
    }

    protected void writeUnpublished(String id, Unpublished reference) throws IOException {
        writeKeyValue("AUTHOR", reference.getAuthor());
        writeKeyValue("TITLE", reference.getTitle());
        writeKeyValue("NOTE", reference.getNote());
        writeKeyValue("MONTH", reference.getMonth());
        org.exolab.castor.types.GYear year = reference.getYear();
        if (year != null) {
            writeKeyValue("YEAR", year.toString());
        }
    }

    protected void writeInbook(String id, Inbook reference) throws IOException {
        InbookChoice choice1 = reference.getInbookChoice();
        if (choice1 != null) {
            writeKeyValue("AUTHOR", choice1.getAuthor());
        }
        writeKeyValue("TITLE", reference.getTitle());
        InbookChoice2 choice2 = reference.getInbookChoice2();
        if (choice2 != null) {
            InbookGroup1 group1 = choice2.getInbookGroup1();
            if (group1 != null) {
                writeKeyValue("CHAPTER", group1.getChapter());
            }
        }
        org.exolab.castor.types.GYear year = reference.getYear();
        if (year != null) {
            writeKeyValue("YEAR", year.toString());
        }
        writeKeyValue("PUBLISHER", reference.getPublisher());
        writeKeyValue("ADDRESS", reference.getAddress());
    }

    protected void writeBook(String id, Book reference) throws IOException {
        BookChoice choice1 = reference.getBookChoice();
        if (choice1 != null) {
            writeKeyValue("AUTHOR", choice1.getAuthor());
        }
        writeKeyValue("TITLE", reference.getTitle());
        writeKeyValue("PUBLISHER", reference.getPublisher());
        org.exolab.castor.types.GYear year = reference.getYear();
        if (year != null) {
            writeKeyValue("YEAR", year.toString());
        }
        writeKeyValue("ADDRESS", reference.getAddress());
    }
}

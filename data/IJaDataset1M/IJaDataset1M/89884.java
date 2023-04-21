package net.sf.RecordEditor.examples;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import net.sf.RecordEditor.edit.EditRec;
import net.sf.JRecord.Common.Constants;
import net.sf.JRecord.Common.RecordException;
import net.sf.JRecord.Details.LayoutDetail;
import net.sf.JRecord.Details.LineProvider;
import net.sf.JRecord.IO.AbstractLineReader;
import net.sf.JRecord.IO.AbstractLineWriter;
import net.sf.JRecord.IO.LineIOProvider;
import net.sf.JRecord.IO.TextLineReader;
import net.sf.JRecord.IO.TextLineWriter;
import net.sf.RecordEditor.utils.CopyBookDbReader;

/**
 * This example of introducing a new file structure, in this
 * case a Comma / Tab Delimitered file with field names on the
 * second line of the file
 *
 * @author B Martin
 *
 */
public class XmplFileStructure2 extends LineIOProvider {

    private static final int MY_FILE_STRUCTURE = 1002;

    /**
	 * get LineReader for the supplied
	 */
    public AbstractLineReader getLineReader(int fileStructure) {
        if (fileStructure == MY_FILE_STRUCTURE) {
            return new DelimLineReader(super.getLineProvider(Constants.IO_TEXT_LINE));
        }
        return super.getLineReader(fileStructure);
    }

    /**
	 * get LineWriter for the supplied structure
	 */
    public AbstractLineWriter getLineWriter(int fileStructure) {
        if (fileStructure == MY_FILE_STRUCTURE) {
            return new DelimLineWriter();
        }
        return super.getLineWriter(fileStructure);
    }

    private class DelimLineReader extends TextLineReader {

        /**
		 * This class provides record oriented reading of a
		 * Tab/Comma Delimiter files with field names on the second line
		 *
		 * @param provider line provider
		 */
        public DelimLineReader(final LineProvider provider) {
            super(provider, true);
        }

        /**
	     * create a layout
	     *
	     * @param pReader file read
	     *
	     * @throws IOException sny IO error that occurs
	     */
        @Override
        protected void createLayout(BufferedReader pReader, InputStream inputStream, String font) throws IOException, RecordException {
            pReader.read();
            super.createLayout(pReader, inputStream, font);
        }
    }

    private class DelimLineWriter extends TextLineWriter {

        public DelimLineWriter() {
            super(true);
        }

        /**
	     * writes the field names to the file
	     *
	     * @param pWriter output writer
	     * @param layout record layout to write
	     *
	     * @throws IOException any error that occurs
	     */
        public void writeLayout(BufferedWriter pWriter, LayoutDetail layout) throws IOException {
            pWriter.newLine();
            super.writeLayout(pWriter, layout);
        }
    }

    /**
     * Example of defining your own Types
     * @param args program arguments
     */
    public static void main(String[] args) {
        CopyBookDbReader copybook = new CopyBookDbReader();
        try {
            new EditRec("", 1, new XmplFileStructure2(), copybook);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

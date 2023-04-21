package eln.nob;

import java.lang.*;
import java.io.*;
import java.awt.*;
import java.util.*;
import java.text.*;
import emsl.JavaShare.*;

/** NObArchive writes the DOE2000 standard MIME based Notebook archive
   file format.
*/
public class NObArchive extends PrintWriter {

    SeparatorGenerator mSeparatorGenerator = null;

    int mNObNum = -1;

    Stack mSeparators = new Stack();

    /** @param anOutputStream the place to direct output to
    @param theNotebookName the name of the Notebook being written
*/
    public NObArchive(OutputStream anOutputStream, String theNotebookName) {
        super(anOutputStream);
        StringBuffer aLine = new StringBuffer("From DOE2000 Notebook ");
        SimpleDateFormat notebookDateFormat = new SimpleDateFormat("EEE MMM dd H:m:s yyyy");
        FieldPosition fp = new FieldPosition(0);
        String theSeparator = null;
        mSeparatorGenerator = new SeparatorGenerator();
        mSeparators.push(mSeparatorGenerator.newSeparator());
        Date theDate = new Date();
        aLine = notebookDateFormat.format(theDate, aLine, fp);
        this.println(aLine);
        this.println("From: \"" + theNotebookName + "\", part of an EMSL DOE2000 Notebook");
        this.println("To: Notebook Archive");
        notebookDateFormat.applyPattern("EEE MMM dd H:m:s zzz yyyy");
        aLine = new StringBuffer("Date: ");
        aLine = notebookDateFormat.format(theDate, aLine, fp);
        this.println(aLine);
        this.println("Subject: DOE2000 Notebook Export");
        this.println("MIME-Version: 1.0");
        this.println("Content-type: multipart/mixed; boundary =\"" + getCurrentSeparator() + "\"");
        this.println("Content-Transfer-Encoding: 7bit");
        this.println("X-ENArcMIME-Version: 1.1");
        this.println();
    }

    /** @return the MIME separator at the current level of recursion in the archive
*/
    public String getCurrentSeparator() {
        return ((String) mSeparators.peek());
    }

    /** @return a new MIME separator string unique from previous separators used in
    this archive
    */
    public String newSeparator() {
        mSeparators.push(mSeparatorGenerator.newSeparator());
        return ((String) mSeparators.peek());
    }

    /** Method description: remove the current MIME separator from the stack of
    separators in use.
*/
    public void popSeparator() {
        mSeparators.pop();
    }

    /** @return the next sequential NObNum to use.
*/
    public int getNObNum() {
        mNObNum++;
        return mNObNum;
    }

    /** Method description: readMIMEBlock reads a series of lines of the form
    key=value
    into a Hashtable
    Parsing stops when a blank line is encountered.
    @return the Hashtable of key/value pairs in this MIME block
*/
    public static Hashtable readMIMEBlock(LineNumberReader anArchiveReader) throws IOException {
        Hashtable lines = new Hashtable();
        String line = null;
        try {
            line = anArchiveReader.readLine();
        } catch (IOException io) {
            throw new IOException("Error reading MIME block");
        }
        while (!line.equals("")) {
            StringTokenizer mimeTokenizer = new StringTokenizer(line, ":", true);
            if (mimeTokenizer.countTokens() <= 1) {
                throw new IOException("Incorrect line format (x:y):" + line);
            }
            String key = (String) mimeTokenizer.nextToken();
            mimeTokenizer.nextToken();
            String value = mimeTokenizer.nextToken("\r\n").trim();
            lines.put(key.toLowerCase(), value);
            try {
                line = anArchiveReader.readLine();
            } catch (IOException io) {
                throw new IOException("Error reading MIME block");
            }
        }
        return lines;
    }

    /** Method description: Read the given archive
    @return the Vector of NObNodes in the archive (NObs and NObNodes)
*/
    public static Vector ReadArchive(Reader anArchiveReader) throws IOException {
        LineNumberReader archiveLines = new LineNumberReader(anArchiveReader);
        String firstLine = archiveLines.readLine();
        if (!firstLine.startsWith("From DOE2000 Notebook ")) {
            System.err.println("Warning: Non-standard first line.\n");
            System.err.println("Line should contain the phrase \"From DOE2000 Notebook\"");
            System.err.println("Line is: " + firstLine);
        }
        Hashtable firstBlock = readMIMEBlock(archiveLines);
        if (!(firstBlock.containsKey("Content-ENArcMIME-Version".toLowerCase()) || firstBlock.containsKey("X-ENArcMIME-Version".toLowerCase()))) {
            throw new IOException("Error reading archive. It is not Content-ENArcMIME-Version compliant.");
        }
        String value = (String) firstBlock.get("Content-ENArcMIME-Version".toLowerCase());
        if (value == null) {
            value = (String) firstBlock.get("X-ENArcMIME-Version".toLowerCase());
        }
        if (!value.equals("1.1")) {
            throw new IOException("Error reading archive. It is not Content-ENArcMIME-Version: 1.1 compliant.");
        }
        if (!firstBlock.containsKey("Content-type".toLowerCase())) {
            throw new IOException("Error reading archive. No content-type key.");
        }
        StringTokenizer boundaryTokenizer = new StringTokenizer((String) firstBlock.get("Content-type".toLowerCase()), "\"");
        boundaryTokenizer.nextToken();
        String firstBoundary = boundaryTokenizer.nextToken();
        return ReadNObLists(archiveLines, firstBoundary);
    }

    public static Vector ReadNObLists(LineNumberReader theArchiveLines, String theFirstBoundary) throws IOException {
        Vector NObsInArchive = new Vector();
        String boundaryLine = theArchiveLines.readLine();
        while (boundaryLine.equals("")) {
            boundaryLine = theArchiveLines.readLine();
        }
        if (!boundaryLine.equals("--" + theFirstBoundary)) {
            throw new IOException("Error reading archive boundary.\r\n Expected --" + theFirstBoundary + "\r\nGot: " + boundaryLine);
        }
        while (boundaryLine.equals("--" + theFirstBoundary)) {
            NOb nextNOb = new NOb();
            String NObFieldBoundary = nextNOb.readNObFromMIMEPart1(theArchiveLines);
            if (NObFieldBoundary != null) {
                if (((String) (nextNOb.get("dataType"))).equals(NOb.NOBLISTMIMETYPE)) {
                    nextNOb = new NObList(nextNOb);
                }
                if (((String) (nextNOb.get("dataType"))).equals("application/NObList")) {
                    System.err.println("Warning: non-standard type for NObLists (\"application/NObList\" instead of \"application/x-eln/NObList\"");
                    nextNOb = new NObList(nextNOb);
                }
                nextNOb.readNObFromMIMEPart2(theArchiveLines, NObFieldBoundary);
                NObsInArchive.addElement(nextNOb);
            }
            boundaryLine = theArchiveLines.readLine();
            while (boundaryLine.equals("")) {
                boundaryLine = theArchiveLines.readLine();
            }
        }
        if (!boundaryLine.equals("--" + theFirstBoundary + "--")) {
            throw new IOException("Error reading final archive boundary.\r\n Expected --" + theFirstBoundary + "\r\nGot: " + boundaryLine);
        }
        return NObsInArchive;
    }

    /**  
 *  Overriding println so that it will always put /r/n at the end of the line
 *  this is standard on the Intel platforms but only /r is added on Unix
 *  boxes.  This causes us problems in reading/writing exported files
 */
    public void println(String aLine) {
        super.print(aLine);
        super.print("\r\n");
    }

    /** Debugging function that reads "test.mime" and writes "test2.mime" using the
    NObNodes found in "test.mime"
    @see eln.NObList main routine generates "test.mime"
*/
    public static void main(String argv[]) {
        File mimeFile = new File("test.mime");
        File mimeFile2 = new File("test2.mime");
        Vector theNObs = null;
        InputStream fileIn = null;
        OutputStream fileOut = null;
        try {
            System.out.println("Opening file \"test.mime\"");
            fileIn = (InputStream) new FileInputStream(mimeFile);
            System.out.println("Reading MIME");
            theNObs = NObArchive.ReadArchive(new InputStreamReader(fileIn));
            System.out.println("Done Reading");
            System.out.println("Read " + String.valueOf(theNObs.size()) + " top level NObs");
        } catch (IOException io) {
            System.err.println("There was an io error that lived in a shoe\n\r" + io);
        } finally {
            if (fileIn != null) {
                try {
                    System.out.println("Closing Stream");
                    fileIn.close();
                } catch (IOException io) {
                    System.err.println(io);
                }
            }
        }
        System.out.println("Done Reading");
        if (theNObs != null) {
            try {
                System.out.println("Preparing to write " + String.valueOf(theNObs.size()) + " top level NObs");
                System.out.println(((NObList) theNObs.firstElement()).toString());
                System.out.println("Opening file \"test2.mime\"");
                fileOut = (OutputStream) new FileOutputStream(mimeFile2);
                System.out.println("Writing MIME");
                ((NObList) theNObs.elementAt(0)).writeToMIME(fileOut);
                System.out.println("Done Writing");
            } catch (IOException io) {
                System.err.println("There was an io error that lived in a shoe\n\r" + io);
            } finally {
                if (fileOut != null) {
                    try {
                        System.out.println("Closing Stream");
                        fileOut.close();
                    } catch (IOException io) {
                        System.err.println(io);
                    }
                }
            }
        }
    }
}

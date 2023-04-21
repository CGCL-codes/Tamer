package jimporter;

import gnu.regexp.RE;
import gnu.regexp.REMatch;
import gnu.regexp.REMatchEnumeration;
import gnu.regexp.RESyntax;
import java.util.ArrayList;
import java.util.Iterator;
import org.gjt.sp.jedit.Buffer;

/**
 * List of the import source lines present in the current file.
 *
 * @author    Matthew Flower
 */
public class ImportList {

    private ArrayList importList = new ArrayList();

    private Buffer sourceBuffer;

    private boolean sourceBufferParsed = false;

    private int startingOffset = Integer.MAX_VALUE;

    private int endingOffset = -1;

    /** 
     * Get the current list of imports.
     *
     * @return A <CODE>ArrayList</CODE> item containing all of the <CODE>ImportItem</CODE>
     * objects found which parsing the source buffer.  Note that this method will parse
     * the source buffer if it hasn't already been parsed.
     */
    public ArrayList getImportList() {
        if (!sourceBufferParsed) {
            parseImports();
        }
        return importList;
    }

    /** 
     * Get an iterator pointing to the list of <CODE>ImportItem</CODE> objects that
     * this class contains.
     *
     * @return A <CODE>Iterator</CODE> object pointing to the list of import items that this
     * object contains.
     */
    public Iterator iterator() {
        if (!sourceBufferParsed) parseImports();
        return importList.iterator();
    }

    /** 
     * This method sets the offset of the first character of the first import
     * statement that we found while parsing the buffer.
     *
     * @param startingOffset The offset of the first character of the first import statement we found while
     * parsing.
     * @see #getStartingOffset()
     */
    public void setStartingOffset(int startingOffset) {
        this.startingOffset = startingOffset;
    }

    /** 
     * Get the offset of the first character of the first import that we found while
     * parsing the source buffer for import statements.
     *
     * @return An <CODE>int</CODE> value containing the offset of the first character of the
     * first import statement that we found while parsing.
     * @see #setStartingOffset(int)
     */
    public int getStartingOffset() {
        return startingOffset;
    }

    /** 
     * Set the offset of the last character of the last import statement that we found
     * in the source buffer.
     *
     * @param endingOffset The offset of the last character of the last import statement we found in the
     * source buffer.
     */
    public void setEndingOffset(int endingOffset) {
        this.endingOffset = endingOffset;
    }

    /** 
     * Get the offset of the last character in the last import statement that we found
     * while parsing the source buffer.
     *
     * @return An <CODE>int</CODE> value that indicates the last character offset of the last
     * import statement in the source buffer.
     */
    public int getEndingOffset() {
        return endingOffset;
    }

    /** 
     * Set the buffer that we are going to parse to find import declarations.
     *
     * @param sourceBuffer a pointer to the buffer we are going to parse to find
     * import declarations.
     * @see #getSourceBuffer()
     */
    public void setSourceBuffer(Buffer sourceBuffer) {
        this.sourceBuffer = sourceBuffer;
        sourceBufferParsed = false;
        importList.clear();
    }

    /** 
     * Get the source buffer that we are going to parse to find import declarations.
     *
     * @return The source buffer that we are going to parse to find import declarations.
     * @see #setSourceBuffer(Buffer)
     */
    public Buffer getSourceBuffer() {
        return sourceBuffer;
    }

    /**
     * Gets the last attribute of the ImportList object
     *
     * @return   The last value in the import list.
     */
    public ImportItem getLast() {
        if (!sourceBufferParsed) parseImports();
        return (ImportItem) getImportList().get(importList.size() - 1);
    }

    /**
     * Adds an ImportItem to the list of imports.
     *
     * @param importItem  The importItem to be added to the list.
     */
    public void addImport(ImportItem importItem) {
        importList.add(importItem);
    }

    /**
     * Returns the number of items in the import list.
     *
     * @return   A <code>int</code> value containing the number of items in the list.
     */
    public int size() {
        if (!sourceBufferParsed) parseImports();
        return importList.size();
    }

    /**
     * Find all of the import statements in the current file and add them to our
     * import list.
     */
    private void parseImports() {
        RE re;
        try {
            re = new RE("[^][[:space:]]*import[[:space:]]+[[:alnum:].$_*]*;", RE.REG_MULTILINE, RESyntax.RE_SYNTAX_POSIX_EXTENDED);
            REMatchEnumeration me = re.getMatchEnumeration(sourceBuffer.getText(0, sourceBuffer.getLength()));
            while (me.hasMoreMatches()) {
                ImportItem ii = new ImportItem(me.nextMatch());
                startingOffset = Math.min(ii.getStartLocation(), startingOffset);
                endingOffset = Math.max(ii.getEndLocation(), endingOffset);
                addImport(ii);
            }
            sourceBufferParsed = true;
        } catch (gnu.regexp.REException e) {
            throw new RuntimeException("Unexpected error while creating regular expression: " + e);
        }
    }
}

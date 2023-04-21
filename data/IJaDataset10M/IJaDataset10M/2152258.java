package ag.ion.bion.officelayer.text;

import ag.ion.bion.officelayer.document.IDocument;
import com.sun.star.text.XTextRange;

/**
 * Range of text in a text document.
 * 
 * @author Andreas Br�ker
 * @author Markus Kr�ger
 * @version $Revision: 11361 $
 */
public interface ITextRange {

    /**
	 * Returns OpenOffice.org XTextRange interface.
	 * 
	 * This method is not part of the public API. It should never used from
	 * outside.
	 * 
	 * @return OpenOffice.org XTextRange interface
	 * 
	 * @author Andreas Br�ker
	 */
    public XTextRange getXTextRange();

    /**
	 * Sets text of the text range.
	 * 
	 * @param text
	 *            text to be used
	 * 
	 * @author Andreas Br�ker
	 */
    public void setText(String text);

    /**
	 * Returns related page style of the text range.
	 * 
	 * @return page style of the text range
	 * 
	 * @throws TextException
	 * 
	 * @author Andreas Br�ker
	 */
    public IPageStyle getPageStyle() throws TextException;

    /**
	 * Returns cell of the texttable if the text range is part of cell.
	 * 
	 * @return cell of the texttable if the text range is part of cell or null
	 *         if the text range is not part of a cell
	 * 
	 * @author Andreas Br�ker
	 */
    public ITextTableCell getCell();

    /**
	 * Compares this text range with the given text range. Returns 1 if this
	 * range starts before the given text range, 0 if the text ranges start at
	 * the same position and -1 if this text range starts behin the given text
	 * range.
	 * 
	 * @param textRangeToCompare
	 *            the text range to compare
	 * 
	 * @return 1 if this range starts before the given text range, 0 if the text
	 *         ranges start at the same position and -1 if this text range
	 *         starts behin the given text range
	 * 
	 * @throws TextException
	 *             if the text ranges to compare are not within the same text.
	 * 
	 * @author Markus Kr�ger
	 */
    public short compareRange(ITextRange textRangeToCompare) throws TextException;

    /**
	 * Sets the document of the text range. This makes sense if the text range
	 * was generated as the document was unknown.
	 * 
	 * @param document
	 *            document to be used
	 * 
	 * @author Markus Kr�ger
	 * @date 25.01.2007
	 */
    public void setDocument(IDocument document);
}

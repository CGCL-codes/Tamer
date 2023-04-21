package wotlas.common.objects.interfaces;

/** 
 * The writable documents' class.
 * 
 * @author Elann
 */
public interface WritableInterface {

    /** Ready the document.
   * Unfold for a parchment, open for a book, ...  
   */
    public void makeReady();

    /** Write to the document.
   * @param text the text to write
   */
    public void writeText(String text);
}

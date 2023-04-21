package wotlas.common.objects.usefuls;

import wotlas.common.objects.interfaces.*;

/** 
 * The class of parchments.
 * 
 * @author Elann
 * @see wotlas.common.objects.usefuls.Document
 */
public class Parchment extends Document {

    /** Is it on ?
  */
    private boolean equipped;

    /** The text of the parchment. HTML formatted. Perhaps a Chapter ?
  */
    private String text;

    /** The default constructor.
   */
    public Parchment() {
        this.className = "Parchment";
        this.objectName = "default parchment";
    }

    /** Use the object.<br> 
   */
    public void use() {
        makeReady();
    }

    /** Put the object "on". Needed before action is possible.
   */
    public void equip() {
        equipped = true;
    }

    /** Ready the document.
   * Unfold for a parchment, open for a book, ...  
   */
    public void makeReady() {
    }

    /** Write to the document.
   * @param text the text to write
   */
    public void writeText(String text) {
    }

    /** Get the document's text.
   * @return current readable text
   */
    public String readText() {
        return text;
    }
}

package ankistream;

/** Contains a mapping of meta card types as they appear in the xml data files 
  * to the corresponding meta card class names.
  * <br>If you are making a new type of flash card, you should add one line
  * to the getClassName method of this class that returns the class name of 
  * your new MetaCard given its card type name.
  * <br> 
  * <br> Part of the Anki Stream project, a tool for long-term flash card
  * review.  Anki Stream is distributed under the MIT/X11 free software 
  * license, a copy of which should have come with this distribution.
  * @author Bishop Wilkins
  * @version 0.1
*/
public class CardTypeIndex {

    /** Gets the name of the MetaCard subclass that corresponds to a specified
	  * flash card type as it appears in a Anki Stream xml data file.
	  * <br>The Anki stream framework uses this method when loading MetaCards.
	  * @param cardTypeName the flash card type as it appears in the xml file.
	  * @return the name of the class corresponding to this card type.
	 */
    public static String getClassName(String cardTypeName) {
        if (cardTypeName.compareTo("basic") == 0) {
            return "ankistream.BasicCard";
        }
        if (cardTypeName.compareTo("heisig") == 0) {
            return "ankistream.HeisigCard";
        }
        return null;
    }
}

package HauptKasse;

import Sale.Display.*;

/**
* <code>ButtonAction</code> mit neuem Constructor.
*/
abstract class CKellnerAbmeldenButton implements ButtonAction {

    /**
* Name des Kellners der abgemeldet werden soll
*/
    String KellnerName;

    /**
*
* Constructor.
*
*@param Name Name des Kellners der abgemeldet werden soll
*/
    public CKellnerAbmeldenButton(String Name) {
        KellnerName = Name;
    }
}

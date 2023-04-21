package org.fudaa.dodico.sisyphe;

/**
 * Classe MyBooleen (permet tester si un booleen du fichier cas ets vrai ou
 * faux).
 *
 * @version      $Revision: 1.6 $ $Date: 2006-09-19 14:45:50 $ by $Author: deniger $
 * @author       Mickael Rubens 
 */
public class MyBool {

    private final boolean booleen_;

    public MyBool(final String _b) {
        booleen_ = boolTest(_b);
    }

    public boolean boolTest() {
        return booleen_;
    }

    public static boolean boolTest(final String _b) {
        if ((_b.indexOf("1") >= 0) || (_b.indexOf("VRAI") >= 0) || (_b.indexOf("TRUE") >= 0) || (_b.indexOf(".TRUE.") >= 0) || (_b.indexOf("YES") >= 0) || (_b.indexOf("OUI") >= 0)) {
            return true;
        }
        return false;
    }
}

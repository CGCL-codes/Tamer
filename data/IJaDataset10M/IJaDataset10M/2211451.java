package org.epo.jpxi.docsecurity;

/**
 * Standard Document Access Control.<BR>
 * Standard rules are :<BR>
 * <UL>
 * <LI>If document copyright is not the user copyright list Access is denied
 * <LI>If Access mode is not Admin Mode
 *  <UL>
 *  <LI>If Publication Date > Now then Access is denied
 *  </UL>
 * </UL> 
 * Creation date: (05/07/01 12:14:21)
 */
public class JpxiStdDocAccCtrl implements JpxiDocAccessCtrl {

    /**
 * JpxiStdDocAccCtrl constructor 
 */
    public JpxiStdDocAccCtrl() {
        super();
    }

    /**
 * Test the acces to the document.
 * Creation date: (05/07/01 12:14:21)
 * @return boolean
 * @param theProfileName java.lang.String
 * @param theAccessMode int
 * @param theCopyrightList java.lang.String
 * @param theDocPubDate java.sql.Timestamp
 * @param theDocCopyright char
 */
    public boolean accessAllowed(int theAccessMode, String theUserCopyrights, java.sql.Timestamp theDocPubDate, char theDocCopyright) {
        java.sql.Timestamp now = new java.sql.Timestamp(System.currentTimeMillis());
        if (theDocCopyright != ' ' & theUserCopyrights.indexOf(theDocCopyright) == -1) return false;
        if (theAccessMode != JpxiDocAccessCtrl.ADMIN_MODE) {
            return theDocPubDate.before(now);
        } else return true;
    }
}

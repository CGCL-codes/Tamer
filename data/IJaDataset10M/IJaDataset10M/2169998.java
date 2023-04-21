package issrg.pba.rbac;

import issrg.pba.ParsedToken;
import issrg.utils.ACNotFoundException;

/**
 * 
 * @author Rune Bjerk  10.07.2007
 */
public class DefaultRevocationChecker implements RevocationChecker {

    public boolean isRevoked(ParsedToken token) throws ACNotFoundException {
        if (token instanceof Revocable) {
            try {
                byte[] webdavAC = issrg.utils.WebdavUtil.getRawAC(((Revocable) token).getValidationURL());
                return compareACs(((Revocable) token).getOriginalAC(), webdavAC);
            } catch (ACNotFoundException valNotFound) {
                try {
                    byte[] revokedAC = issrg.utils.WebdavUtil.getRawAC(((Revocable) token).getValidationURL());
                    return compareACs(((Revocable) token).getOriginalAC(), revokedAC);
                } catch (ACNotFoundException revNotFound) {
                    throw new ACNotFoundException("Neither validation or revocation attribute certificate were found");
                }
            }
        }
        return true;
    }

    private boolean compareACs(byte[] originalAC, byte[] newAC) {
        if (originalAC.length != newAC.length) return false;
        for (int i = 0; i < originalAC.length; i++) {
            if (originalAC[i] != newAC[i]) return false;
        }
        return true;
    }

    public boolean isRevoked(Object token) throws RevocationNotDecisiveException {
        return false;
    }
}

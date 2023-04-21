package net.sf.jawp.gf.domain;

import net.sf.jawp.gf.api.domain.User;
import net.sf.jawp.util.PasswordUtil;

/**
 * 
 * User data object.
 * @author jarek
 * @version $Revision: 1.6 $
 *
 */
public class UserDO extends User {

    private static final long serialVersionUID = 1L;

    private String encodedPass;

    public UserDO(final long key) {
        super(key);
    }

    /**
	 * Returns the encodedPass.
	 * @return the value of encodedPass
	 */
    private String getEncodedPass() {
        return encodedPass;
    }

    /**
	 * Sets the value of encodedPass.
	 * @param encodedPass The encodedPass to set.
	 */
    public final void setEncodedPass(final String encodedPass) {
        this.encodedPass = encodedPass;
    }

    public final boolean login(final String clientMix, final String chapSeed) {
        return PasswordUtil.checkPass(clientMix, chapSeed, getEncodedPass());
    }
}

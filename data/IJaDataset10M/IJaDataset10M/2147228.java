package org.hardtokenmgmt.core.token;

import iaik.pkcs.pkcs11.Token;
import iaik.pkcs.pkcs11.TokenException;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Iterator;
import org.hardtokenmgmt.core.InterfaceFactory;
import org.hardtokenmgmt.core.log.LocalLog;

/**
 * A Base Token implementation that contains generic operations on
 * token that can be reused by most token implementations
 * 
 * @author Philip Vendil 2006-aug-30
 *
 * @version $Id$
 */
@SuppressWarnings("unchecked")
public class SetCos431InstantEIDToken extends BaseToken {

    private static final String[] SUPPORRTEDPINTYPES = { IToken.PINTYPE_BASIC, IToken.PINTYPE_SIGN };

    private static final String[] SUPPORTEDLABELS = { "Instant EID IP1" };

    private static final String[] INSTANTEIDIP1 = { "Instant EID IP1 (identification)", "Instant EID IP1 (signature)" };

    private static final String[][] PINLABELMAPPER = { INSTANTEIDIP1 };

    public boolean isTokenSupported(Token token) throws TokenException {
        LocalLog.debug("Token label : " + token.getTokenInfo().getLabel());
        boolean retval = false;
        String tokenLabel = token.getTokenInfo().getLabel().trim();
        for (int i = 0; i < SUPPORTEDLABELS.length; i++) {
            if (tokenLabel.startsWith(SUPPORTEDLABELS[i])) {
                retval = true;
                break;
            }
        }
        return retval;
    }

    public String[] getSupportedPINTypes() {
        return SUPPORRTEDPINTYPES;
    }

    /**
	 * The Setcos 431 cards are not ereasable so the have to be cleared in the same
	 * way as the clear method.
	 */
    public void initToken(String tokenlabel, String tokenserial, String[] pintypes, String[] pins, String[] puks, String[] currentpuks) throws OperationNotSupportedException, TokenException {
        clearToken(pintypes, puks);
        int basicindex = 0;
        int sigindex = 0;
        for (int i = 0; i < pintypes.length; i++) {
            if (pintypes[i].equals(IToken.PINTYPE_BASIC)) {
                basicindex = i;
            }
            if (pintypes[i].equals(IToken.PINTYPE_SIGN)) {
                sigindex = i;
            }
        }
        changePIN(IToken.PINTYPE_BASIC, InterfaceFactory.getGlobalSettings().getTokenInitialBasicPIN(), pins[basicindex]);
        changePIN(IToken.PINTYPE_SIGN, InterfaceFactory.getGlobalSettings().getTokenInitialSignaturePIN(), pins[sigindex]);
    }

    /**
	 * Method to use with the Setec 431 cards.
	 * It removes all dataobjects, then removes all Certificates
	 * then sets all pins to a default value defined in global.properties.
	 * 
	 */
    public void clearToken(String[] pintypes, String[] puks) throws OperationNotSupportedException, TokenException {
        String basicPUK = null;
        String sigPUK = null;
        if (pintypes[0].equals(PINTYPE_BASIC)) {
            basicPUK = puks[0];
            sigPUK = puks[1];
        } else {
            sigPUK = puks[0];
            basicPUK = puks[1];
        }
        if (basicPUK == null) {
            throw new TokenException("Error trying to fetch PUK data in cleartoken operation");
        }
        String initBasicPIN = InterfaceFactory.getGlobalSettings().getTokenInitialBasicPIN();
        if (initBasicPIN == null) {
            throw new OperationNotSupportedException("Error, no init basic PIN setting have been defined in global.properties");
        }
        String initSigPIN = InterfaceFactory.getGlobalSettings().getTokenInitialSignaturePIN();
        if (initSigPIN == null) {
            throw new OperationNotSupportedException("Error, no init sigature PIN setting have been defined in global.properties");
        }
        for (int i = 0; i < pintypes.length; i++) {
            PINInfo pinInfo = getPINInfo(pintypes[i]);
            while (!pinInfo.isPINLocked()) {
                pinInfo = unlockPIN(pintypes[i], "23412432");
            }
        }
        unblockPIN(PINTYPE_BASIC, basicPUK, initBasicPIN);
        unblockPIN(PINTYPE_SIGN, sigPUK, initSigPIN);
        for (int i = 0; i < pintypes.length; i++) {
            String initPIN = initBasicPIN;
            if (pintypes[i].equals(IToken.PINTYPE_SIGN)) {
                initPIN = initSigPIN;
            }
            Collection certificates = getCertificates(pintypes[i]);
            Iterator iter = certificates.iterator();
            while (iter.hasNext()) {
                X509Certificate cert = (X509Certificate) iter.next();
                removeCertificate(pintypes[i], initPIN, initBasicPIN, cert);
            }
            Collection dataObjects = getObjects(pintypes[i], initPIN, OBJECTTYPE_DATA);
            iter = dataObjects.iterator();
            while (iter.hasNext()) {
                removeObject(pintypes[i], initPIN, initBasicPIN, (IObject) iter.next());
            }
        }
    }

    protected String getPrivateKeyLabel(String keytype) throws OperationNotSupportedException {
        if (keytype.equals(IToken.KEYTYPE_AUTH)) {
            return "key aut + enc";
        } else if (keytype.equals(IToken.KEYTYPE_SIGN)) {
            return "key sign";
        } else {
            throw new OperationNotSupportedException("Keytype " + keytype + " not supported.");
        }
    }

    protected String getPINLabel(Token token, String pintype) throws OperationNotSupportedException, TokenException {
        String retval = null;
        String tokenLabel = token.getTokenInfo().getLabel().trim();
        for (int i = 0; i < SUPPORTEDLABELS.length; i++) {
            if (tokenLabel.startsWith(SUPPORTEDLABELS[i])) {
                if (pintype.equals(IToken.PINTYPE_BASIC)) {
                    retval = PINLABELMAPPER[i][0];
                }
                if (pintype.equals(IToken.PINTYPE_SIGN)) {
                    retval = PINLABELMAPPER[i][1];
                }
            }
        }
        if (retval == null) {
            throw new OperationNotSupportedException("Invalid PIN Type.");
        }
        return retval;
    }

    @Override
    public void downloadKeyStore(String keytype, String pintype, String pin, String certLabel, KeyStore keyStore, String keyStorePasswd) throws ObjectAlreadyExistsException, OperationNotSupportedException, TokenException {
        throw new OperationNotSupportedException("SetCos 431 doesn't support the downloadKeyStore operation.");
    }

    @Override
    public void genKey(String pintype, String pin, String basicpin, String keytype, String algorithm, int keysize, String label) throws ObjectAlreadyExistsException, OperationNotSupportedException, TokenException {
        throw new OperationNotSupportedException("SetCos 431 doesn't support the genKey operation.");
    }

    @Override
    public void removeKey(String pintype, String pin, String basicpin, String label) throws OperationNotSupportedException, TokenException {
        throw new OperationNotSupportedException("SetCos 431 doesn't support the removeKey operation.");
    }

    /**
	 * @throws TokenException 
	 * @see org.hardtokenmgmt.core.token.IToken#getInitializationRequirements()
	 */
    public InitializationRequirements getInitializationRequirements() throws TokenException {
        return InitializationRequirements.REQUIRESOPININDB;
    }

    /**
	 * @see org.hardtokenmgmt.core.token.IToken#generatePUK(String)
	 */
    public String generatePUK(String pintype) throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Token requires PUK in Database");
    }

    @Override
    protected boolean isInitialized(Token token) {
        return true;
    }
}

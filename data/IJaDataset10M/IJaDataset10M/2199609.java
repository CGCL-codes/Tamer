package auditorium;

import sexpression.*;

/**
 * This layer handles signatures.
 * 
 * @author kyle
 * 
 */
public class AuditoriumIntegrityLayer extends AAuditoriumLayer {

    public static final ASExpression PATTERN = new ListExpression(StringExpression.makeString("signed-message"), Wildcard.SINGLETON, Wildcard.SINGLETON);

    private final String _nodeID;

    private final IKeyStore _keystore;

    private Cert _mycert;

    public static final String CA_ANNOTATION = "ca";

    /**
     * @param child
     *            This new instance is a parent of this given instance.
     * @param host
     *            This is the host that is using this stack.
     * @param keystore
     *            This is the keystore to use for locating keys to perform cryptographic operations.
     */
    public AuditoriumIntegrityLayer(AAuditoriumLayer child, IAuditoriumHost host, IKeyStore keystore) {
        super(child, host);
        _nodeID = host.getNodeId();
        _keystore = keystore;
        if (keystore == null) Bugout.msg("warning: keystore is NULL in AuditoriumIntegrityLayer()");
        try {
            init();
        } catch (Exception e) {
            throw new FatalNetworkException("Can't perform necessary cryptographic operations", e);
        }
    }

    /**
     * @see auditorium.IAuditoriumLayer#makeAnnouncement(sexpression.ASExpression)
     */
    public ASExpression makeAnnouncement(ASExpression datum) {
        ASExpression newdatum;
        try {
            newdatum = new ListExpression(StringExpression.makeString("signed-message"), _mycert.toASE(), RSACrypto.SINGLETON.sign(datum, _keystore.loadKey(_nodeID)).toASE());
        } catch (AuditoriumCryptoException e) {
            throw new FatalNetworkException("Couldn't make an announcement because of a crypto error.", e);
        }
        return getChild().makeAnnouncement(newdatum);
    }

    /**
     * @see auditorium.IAuditoriumLayer#makeJoin(sexpression.ASExpression)
     */
    public ASExpression makeJoin(ASExpression datum) {
        return getChild().makeJoin(datum);
    }

    /**
     * @see auditorium.IAuditoriumLayer#makeJoinReply(sexpression.ASExpression,
     *      sexpression.ASExpression)
     */
    public ASExpression makeJoinReply(ASExpression datum) {
        return getChild().makeJoinReply(datum);
    }

    /**
     * @see auditorium.IAuditoriumLayer#receiveAnnouncement(sexpression.ASExpression)
     */
    public ASExpression receiveAnnouncement(ASExpression datum) throws IncorrectFormatException {
        try {
            ASExpression matchresult = PATTERN.match(getChild().receiveAnnouncement(datum));
            if (matchresult == NoMatch.SINGLETON) throw new IncorrectFormatException(datum, new Exception(datum + " doesn't match the pattern:" + PATTERN));
            ListExpression matchlist = (ListExpression) matchresult;
            Cert cer = new Cert(matchlist.get(0));
            Signature sig = new Signature(matchlist.get(1));
            RSACrypto.SINGLETON.verify(new Signature(matchlist.get(1)), new Cert(matchlist.get(0)));
            String signingKeyId = cer.getSignature().getId();
            Cert signingCert = _keystore.loadCert(signingKeyId);
            if (signingCert.getKey().getAnnotation().equals(CA_ANNOTATION)) {
                RSACrypto.SINGLETON.verify(cer.getSignature(), signingCert);
            } else {
                throw new SignerValidityException("Certificate on message signature was signed by non-authoritative key '" + signingKeyId + "' (annotation: '" + signingCert.getKey().getAnnotation() + "')");
            }
            return sig.getPayload();
        } catch (AuditoriumCryptoException e) {
            throw new IncorrectFormatException(datum, e);
        } catch (SignerValidityException e) {
            throw new IncorrectFormatException(datum, e);
        }
    }

    /**
     * @see auditorium.IAuditoriumLayer#receiveJoinReply(sexpression.ASExpression)
     */
    public ASExpression receiveJoinReply(ASExpression datum) throws IncorrectFormatException {
        return getChild().receiveJoinReply(datum);
    }

    /**
     * @see auditorium.IAuditoriumLayer#receiveJoin(sexpression.ASExpression)
     */
    public ASExpression receiveJoin(ASExpression datum) throws IncorrectFormatException {
        return datum;
    }

    /**
     * Run some initial tests to make sure that this host can do all the
     * cryptographic things it needs to be able to do.
     */
    private void init() throws Exception {
        StringExpression message = StringExpression.makeString("test");
        _mycert = _keystore.loadCert(_nodeID);
        Signature sig = RSACrypto.SINGLETON.sign(message, _keystore.loadKey(_nodeID));
        RSACrypto.SINGLETON.verify(sig, _mycert);
    }
}

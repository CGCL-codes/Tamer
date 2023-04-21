package nl.bird.ocean.p2p;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.pipe.PipeID;

/**
 * A class for creating various JXTA id's
 *
 * @author Diego Leider
 */
public class JxtaId {

    private static final String SEED = "OceanId";

    /**
     * Returns a SHA hash of String
     *
     * @param expression The expression to hash
     * @return A SHA1 hash of string or {@code null} if the expression could not be hashed.
     */
    private static byte[] hash(final String expression) {
        byte[] result;
        MessageDigest digest;
        if (expression == null) {
            throw new IllegalArgumentException("Invalid null expression");
        }
        try {
            digest = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            noSuchAlgorithmException.printStackTrace(System.err);
            RuntimeException failure = new IllegalStateException("Could not get SHA-1 message");
            failure.initCause(noSuchAlgorithmException);
            throw failure;
        }
        try {
            byte[] expressionBytes = expression.getBytes("UTF-8");
            result = digest.digest(expressionBytes);
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            unsupportedEncodingException.printStackTrace(System.err);
            RuntimeException failure = new IllegalStateException("Could not encode expression as UTF-8");
            failure.initCause(unsupportedEncodingException);
            throw failure;
        }
        return result;
    }

    /**
     * Given a pipe name, it returns a PipeID who's value is chosen based upon that name
     *
     * @param pgID The group ID encoding
     * @param pipeName Instance name
     * @return The PipeID value
     */
    public static PipeID createPipeID(PeerGroupID pgID, String pipeName) {
        String seed = pipeName + JxtaId.SEED;
        return IDFactory.newPipeID(pgID, hash(seed.toLowerCase()));
    }

    /**
     * Creates group encoded random pipe ID
     *
     * @param pgID The group ID encoding
     * @return The PipeID value
     */
    public static PipeID createNewPipeID(PeerGroupID pgID) {
        return IDFactory.newPipeID(pgID);
    }

    /**
     * Creates group encoded random PeerID
     *
     * @param pgID The group ID encoding
     * @return The PeerID value
     */
    public static PeerID createNewPeerID(PeerGroupID pgID) {
        return IDFactory.newPeerID(pgID);
    }

    /**
     * Given a peer name generates a PeerID who's value is chosen based upon that name
     * 
     * @param pgID The group id encoding
     * @param peerName Instance name
     * @return
     */
    public static PeerID createPeerID(PeerGroupID pgID, String peerName) {
        String seed = peerName + SEED;
        return IDFactory.newPeerID(pgID, hash(seed.toLowerCase()));
    }

    /**
     * Creates group encoded random PeerGroupID
     * 
     * @param pgID The group ID encoding
     * @return The PeerGroupID value
     */
    public static PeerGroupID createNewPeerGroupID(PeerGroupID pgID) {
        return IDFactory.newPeerGroupID(pgID);
    }

    /**
     * Given a group name generates a Peer Group ID who's value is chosen based upon that name
     * 
     * @param groupName Group name encoding value
     * @return The value of the PeerGroupID
     */
    public static PeerGroupID createPeerGroupID(final String groupName) {
        return IDFactory.newPeerGroupID(PeerGroupID.defaultNetPeerGroupID, hash(SEED + groupName.toLowerCase()));
    }

    /**
     * Constructs and returns a string encoded Infrastructure PeerGroupID
     * 
     * @param groupName The string encoding
     * @return The value of the Infrastructure Peer Group ID
     */
    public static PeerGroupID createInfraPeerGroupID(String groupName) {
        return createPeerGroupID(groupName);
    }

    public static void main(String[] args) {
        PeerGroupID infra = JxtaId.createInfraPeerGroupID("infra");
        PeerID peerID = JxtaId.createNewPeerID(infra);
        PipeID pipeID = JxtaId.createNewPipeID(infra);
        System.out.println(MessageFormat.format("\n\nAn Infrastructure PeerGroupID {0}", infra.toString()));
        System.out.println(MessageFormat.format("PeerID with above infrastructure encoding: {0}", peerID));
        System.out.println(MessageFormat.format("PipeID with the default defaultNetPeerGroupID encoding: {0}", pipeID));
    }
}

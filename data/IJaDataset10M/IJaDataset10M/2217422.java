package de.fhg.igd.semoa.security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import codec.asn1.ASN1Exception;
import codec.asn1.DERDecoder;
import codec.pkcs7.ContentInfo;
import codec.pkcs7.SignedData;
import codec.pkcs7.SignerInfo;
import codec.pkcs7.Verifier;
import codec.util.CertificateChainVerifier;
import codec.util.CertificateSource;
import de.fhg.igd.semoa.server.IllegalAgentException;
import de.fhg.igd.util.Manifest;
import de.fhg.igd.util.ManifestVerifier;
import de.fhg.igd.util.Resource;

/**
 * This class verifies the digital signatures on agent structures.
 *
 * @author Volker Roth
 * @version "$Id: AgentVerifier.java 1913 2007-08-08 02:41:53Z jpeters $"
 */
public class AgentVerifier extends Object {

    /**
     * The maximum size allowed for encoded signatures.
     */
    public static final int MAX_SIG_LEN = 32768;

    /**
     * The default trusted digest algorithms, which are
     * SHA1, MD5 and RIPEMD160 by default.
     */
    public static final String DEFAULT_DIGESTS = "SHA1,MD5,RIPEMD-160";

    /**
     * The default number of trusted digest algorithms
     * that must be correct for each entry in the agent
     * structure. The default is 2.
     */
    public static final int DEFAULT_THRESHOLD = 2;

    /**
     * The size of chunks used when reading the 'to be digested'
     * data (the data from which implicit names are computed).
     */
    public static final int BUFFER_SIZE = 1024;

    /**
     * The digest used to compute implicit agent names.
     */
    public static final String NAME_DIGEST = "SHA1";

    /**
     * The list of supported extensions of signature files
     * in an agent's JAR.
     */
    private static String[] extensions_ = { ".DSA", ".RSA", ".SIG" };

    /**
     * The resource that holds the agent's structure.
     */
    protected Resource struct_;

    /**
     * The MANIFEST file of the structure is kept
     * here after the agent is locked.
     */
    protected Manifest mf_;

    /**
     * The <code>ManifestVerifier</code> that is used to
     * check the signature files against the Manifest in
     * the agent.
     */
    protected ManifestVerifier mVerifier_;

    /**
     * The trusted digest algorithms to use for
     * computing digests.
     */
    protected String dAlgs_;

    /**
     * The minimum number of trusted digests that must be
     * correct for each entry to be accpeted
     */
    protected int threshold_ = 0;

    /**
     * The <code>CertificateChainVerifier</code> with the
     * trusted <code>CertificateSource</code>. If a signature
     * can be resolved against one of the trusted certificates
     * then it is deemed to be authentic.
     */
    protected CertificateChainVerifier chainVerifier_;

    /**
     * The set of names of entries in the agent structure
     * that failed verification for one or another reason.
     * Reasons might be a bad hash in the MANIFEST file or
     * a bad signature.
     */
    protected Set failed_ = new HashSet();

    /**
     * The map of <code>MANIFEST</code> entries of the files
     * signed by the agent's owner. This map is initialised
     * only after the signature of the agent's owner was
     * verified successfully.
     */
    protected Map static_;

    /**
     * Creates an instance that verifies the agent whose
     * structure is represented by the given Resource.
     * The Resource must be rooted at the directory in
     * which all the agent's data is kept.
     *
     * @param structure The structure of the agent.
     * @param digestAlgs The message digest algorithms
     *   to use for locking and signing the structure.
     * @param threshold The number of trusted digests
     *   that must be verified correctly for an entry
     *   in the agent structure.
     */
    public AgentVerifier(Resource struct, String dAlgs, int threshold) {
        if (struct == null || dAlgs == null) {
            throw new NullPointerException("Need a Resource and digest algorithms!");
        }
        struct_ = struct;
        dAlgs_ = dAlgs;
        threshold_ = threshold;
    }

    /**
     * Creates an instance that verifies the agent whose
     * structure is represented by the given Resource.
     * The Resource must be rooted at the directory in
     * which all the agent's data is kept.
     *
     * @param structure The structure of the agent.
     */
    public AgentVerifier(Resource struct) {
        if (struct == null) {
            throw new NullPointerException("Need a Resource");
        }
        struct_ = struct;
        dAlgs_ = DEFAULT_DIGESTS;
        threshold_ = DEFAULT_THRESHOLD;
    }

    /**
     * Sets the certificate source containing the trusted
     * certificates. Any signatures that can be resolved
     * against a certificate in that source are considered
     * valid.
     *
     * @param certstore The trusted CertificateSource.
     * @see CertificateSource
     */
    public void setTrustedCertificates(CertificateSource certstore) {
        chainVerifier_ = new CertificateChainVerifier(certstore);
    }

    /**
     * Sets the trusted digest algorithms and the threshold
     * number of algorithms that must be validated correctly
     * for an entry in the agent structure to be accepted.
     *
     * @param dAlgs The trusted digest algorithm names
     *   separated by colon, semicolon, space or comma.
     * @param threshold The minimum number of trusted
     *   algorithms that must be validated for an entry
     *   being accepted.
     */
    public void setTrustedAlgorithms(String dAlgs, int threshold) {
        dAlgs_ = dAlgs;
        threshold_ = threshold;
    }

    /**
     * Verifies the signature for the given alias name.
     * This methods attempts to load and verify the SF
     * and appropriate DSA, RSA or SIG file from the
     * agent structure. If the signature could be
     * verified correctly then the authenticated
     * certificate of the signer is returned.
     * <p>
     * Please note, if no trusted certificates are
     * set then the verification will abort with an
     * exception.
     * <p>
     * If the signature could not be verified successfully
     * then <code>null</code> is returned instead of the
     * certificate of the signer.
     *
     * @param alias The prefix of the signature files in
     *   the agent structure's META-INF folder.
     * @return The signer's certificate.
     * @exception IllegalAgentException if the agent is
     *   rejected due to a failed security check.
     * @exception GeneralSecurityException if some other
     *   security-related operation fails such as a missing
     *   algorithm.
     */
    public X509Certificate verifySignature(String alias) throws IOException, GeneralSecurityException {
        if (mf_ == null) {
            throw new IllegalStateException("Not initialized, call verifyManifest() first!");
        }
        ByteArrayOutputStream bos;
        ManifestVerifier mVerifier;
        X509Certificate cert;
        InputStream in;
        ContentInfo ci;
        DERDecoder dec;
        SignerInfo si;
        SignedData sd;
        Verifier sVerifier;
        Manifest sf;
        Iterator i;
        boolean ok;
        byte[] b;
        Set set;
        int n;
        in = struct_.getInputStream(AgentStructure.META_INF + alias + ".SF");
        if (in == null) {
            throw new FileNotFoundException(AgentStructure.META_INF + alias + ".SF");
        }
        b = new byte[BUFFER_SIZE];
        bos = new ByteArrayOutputStream();
        try {
            while ((n = in.read(b)) > 0) {
                bos.write(b, 0, n);
            }
            b = bos.toByteArray();
        } finally {
            bos.close();
            in.close();
        }
        sf = new Manifest(dAlgs_, dAlgs_, threshold_);
        in = new ByteArrayInputStream(b);
        sf.load(in);
        in.close();
        mVerifier_.verify(sf);
        set = mVerifier_.getFailed();
        if (set.size() > 0) {
            failed_.addAll(set);
            throw new IllegalAgentException("Digest mismatch in a signed Manifest section!");
        }
        set = mVerifier_.getMissing();
        if (set.size() > 0) {
            throw new IllegalAgentException("At least one signed Manifest section is missing!");
        }
        in = getSF(alias);
        dec = new DERDecoder(in, MAX_SIG_LEN);
        ci = new ContentInfo();
        try {
            ci.decode(dec);
            sd = (SignedData) ci.getContent();
        } catch (ASN1Exception e) {
            throw new IllegalAgentException("Cannot decode signature: " + e.getMessage());
        } catch (ClassCastException e) {
            throw new IllegalAgentException("Bad content info contents: " + e.getMessage());
        } finally {
            dec.close();
        }
        cert = null;
        i = sd.getSignerInfos().iterator();
        if (!i.hasNext()) {
            throw new IllegalAgentException("SignedData contains no SignerInfo!");
        }
        si = (SignerInfo) i.next();
        sVerifier = new Verifier(sd, si, null);
        sVerifier.update(b);
        cert = sVerifier.verify();
        if (cert == null) {
            throw new IllegalAgentException("Bad signature!");
        }
        if (chainVerifier_ == null) {
            throw new SignatureException("No trusted certificate chain verifier set!");
        }
        chainVerifier_.verify(cert, sd);
        if (alias.equals(AgentStructure.OWNER)) {
            setStaticPart(sf);
        }
        return cert;
    }

    /**
     * Does the final verification step. After the Manifest
     * and all signatures are verified, the Manifest must
     * still be checked for sections not covered by a valid
     * digital signature. Otehrwise, an attacker could add
     * garbage to the agent, and some additional (valid)
     * Manifest sections for the garbage entries.
     *
     * @exception IllegalAgentException if the agent and
     *   its Manifest contains unsigned data (so-called
     *   digital &quot;garbage&quot;, you don't want your
     *   agents to be abused as garbage bins or carriers
     *   of <i>unsolicited</i> dirt, do you?).
     */
    public void verifyFinal() throws IllegalAgentException {
        if (mf_ == null) {
            throw new IllegalStateException("Not initialized, call verifyManifest() first!");
        }
        if (mVerifier_.getGarbage().size() > 0) {
            throw new IllegalAgentException("Agent Manifest and Resource contains unsigned data!");
        }
        if (mVerifier_.getMissing().size() > 0) {
            throw new IllegalAgentException("Agent Manifest sections are missing!");
        }
    }

    /**
     * Puts all <code>MANIFEST</code> entries that appear also
     * in the given <code>SF</code> entries into the static part.
     * This method is called from <code>verifySignature(alias)
     * </code> if the &quot;OWNER&quot; signature is verified
     * (as determined from the alias name).
     *
     * @param sf The signature file.
     */
    protected void setStaticPart(Manifest sf) {
        Map.Entry entry;
        Iterator i;
        String key;
        Map map;
        static_ = new HashMap();
        map = sf.entryMap();
        for (i = mf_.entryMap().entrySet().iterator(); i.hasNext(); ) {
            entry = (Map.Entry) i.next();
            key = (String) entry.getKey();
            if (map.containsKey(key)) {
                static_.put(key, entry.getValue());
            }
        }
    }

    public void verifyManifest() throws IOException, GeneralSecurityException {
        ManifestVerifier verifier;
        InputStream in;
        Manifest mf;
        Iterator i;
        String name;
        Set set;
        in = struct_.getInputStream(AgentStructure.MANIFEST);
        if (in == null) {
            throw new FileNotFoundException("Could not find " + AgentStructure.MANIFEST);
        }
        mf = new Manifest(dAlgs_, dAlgs_, threshold_);
        try {
            mf.load(in);
        } finally {
            in.close();
        }
        verifier = new ManifestVerifier(struct_);
        verifier.verify(mf);
        if (verifier.getGarbage().size() > 0) {
            throw new IllegalAgentException("Agent contains files not listed in its Manifest!");
        }
        set = verifier.getFailed();
        if (set.size() > 0) {
            failed_.addAll(set);
            throw new IllegalAgentException("At least one Manifest section has a digest mismatch!");
        }
        set = verifier.getMissing();
        if (set.size() > 0) {
            for (i = set.iterator(); i.hasNext(); ) {
                name = (String) i.next();
                if (!name.endsWith(".class")) {
                    throw new IllegalAgentException("Non-class file is missing: " + name);
                }
            }
        }
        mf_ = mf;
        mVerifier_ = new ManifestVerifier(mf_);
    }

    /**
     * This method returns a collection of those entry names
     * that failed the verification either because the hash
     * values in the Manifest were bad or the signature was
     * bad.<p>
     *
     * Failed entries are cumulated over multiple calls. The
     * instance that is returned is the one that is maintained
     * internally. It should not be modified.
     *
     * @return The collection of failed entries.
     */
    public Set getFailedEntries() {
        return failed_;
    }

    /**
     * This method returns the map of <code>MANIFEST</code> of
     * files in the static part of the agent if the owner's
     * signature has been verified successfully. Otherwise it
     * returns <code>null</code>.<p>
     *
     * Be careful, the instance being returned is the one also
     * used internally. For this reason, you should create new
     * AgentSigners for each agent you verify.
     *
     * @return The entries of files in the agent's
     *   static part or <code>null</code> if the owner's
     *   identity is not established (yet).
     */
    public Map getStaticPart() {
        return static_;
    }

    /**
     * Returns the implicit name of this agent. The implicit
     * name consists of a digest of the owner's signature,
     * more precisely, a digest of the encoded PKCS7 <code>
     * ContentInfo</code> which contains the owner's signature
     * information.
     *
     * @return The implicit name.
     * @exception NoSuchAlgorithmException if the required digest
     *   algorithm could not be found.
     * @exception IOException if the data to be digested could not
     *   be read.
     */
    public byte[] getImplicitName() throws NoSuchAlgorithmException, IOException {
        MessageDigest digest;
        InputStream in;
        byte[] buf;
        int n;
        in = getSF(AgentStructure.OWNER);
        try {
            buf = new byte[BUFFER_SIZE];
            digest = MessageDigest.getInstance(AgentStructure.IMPLICIT_NAME_ALG);
            while ((n = in.read(buf)) > 0) {
                digest.update(buf, 0, n);
            }
            return digest.digest();
        } finally {
            in.close();
        }
    }

    /**
     * Returns an input stream to the signature file with
     * the given alias. This method tries the various
     * supported SF extensions until one is found.
     *
     * @param alias The alias for whose SF an input stream
     *   shall be returned.
     * @exception IOException if no SF for the
     *   given alias could be found, or an I/O error occurs
     *   while reading the data.
     */
    private InputStream getSF(String alias) throws IOException {
        InputStream in;
        int i;
        for (i = 0; i < extensions_.length; i++) {
            in = struct_.getInputStream(AgentStructure.META_INF + alias + extensions_[i]);
            if (in != null) {
                return in;
            }
        }
        throw new FileNotFoundException("Signature not found for alias \"" + alias + "\"!");
    }
}

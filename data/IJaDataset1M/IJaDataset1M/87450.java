package de.fhg.igd.semoa.security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import codec.Base64;
import codec.CorruptedCodeException;
import codec.asn1.ASN1Exception;
import codec.asn1.DERDecoder;
import codec.pkcs7.ContentInfo;
import codec.pkcs7.EnvelopedData;
import codec.pkcs7.RecipientInfo;
import de.fhg.igd.util.Attributes;
import de.fhg.igd.util.Manifest;
import de.fhg.igd.util.Resource;
import de.fhg.igd.util.Resources;

/**
 * This class encrypts agent structures according to a given policy.
 * When agents with encryption are created, the policy is expressed
 * by means of a <code>Groups</code> instance.
 *
 * @author Volker Roth
 * @version "$Id: AgentEncryptor.java 1913 2007-08-08 02:41:53Z jpeters $"
 * @see Groups
 */
public class AgentEncryptor extends Object {

    /**
     * The maximum number of octets that an encoded P7
     * is allowed to have.
     */
    public static final int MAX_P7_LEN = 32768;

    /**
     * The access control groups indexed by name. Each
     * entry maps to a PKCS#7 EnvelopedData structure
     * that is used to encrypt data that should be
     * accessible to that group.
     */
    protected Map groups_;

    /**
     * The folders that are access restricted and assigned
     * a particular access control group. The values of
     * this map consist of {@link Attributes Attributes}.
     */
    protected Map folders_;

    /**
     * The resource holding the agentstructure.
     */
    protected Resource struct_;

    /**
     * Creates an instance that operates on the agent
     * structure stored by the given {@link Resource
     * Resource}.<p>
     *
     * This constructor is typically used when agents
     * are decrypted by a receiving host. Before actual
     * decryption can take place, the bulk encryption
     * keys must be unlocked.
     *
     * @param res The agent's <code>Resource</code>.
     */
    public AgentEncryptor(Resource res) {
        struct_ = res;
    }

    /**
     * Creates an instance with the given {@link
     * Resource Resource}. The access groups and folder
     * assignments are taken over directly from it.<p>
     *
     * This constructor is typically used when agents are
     * created.
     *
     * @param groups The <code>Groups</code> with
     *   the readily initialized definition of the groups and
     *   folder assignments. The group files must already be
     *   unlocked. In other words, the groups structures must
     *   contain the secret bullk encryption key.
     */
    public AgentEncryptor(Resource res, Groups groups) {
        if (res == null || groups == null) {
            throw new NullPointerException("resource or encrypted structure definition");
        }
        struct_ = res;
        init(groups.folders_, groups.groups_);
    }

    /**
     * This method verifies that the given signing certificate
     * is the authorised owner of the data belonging to the
     * given group.This is done by checking the MACs in the
     * GROUPS entry of the SEAL-INF/INSTALL.MF file. This
     * establiches a simple non-interactive proof of knowledge
     * of the symmetric bulk decryption key for the respective
     * groups. Only valid recipients can make this check since
     * the symmetric key must be known available for this check.
     * <p>
     * This feature is required to avoid cut & paste attacks
     * on encrypted portions of an agent's structure. Else,
     * an attacker might cut encrypted data and group defs
     * from an agent and paste them into one of its own.
     * If this agent is then sent to a host that is
     * authorised to decrypt then the data is disclosed and
     * may be read by the attacker's agent.<p>
     *
     * @param cert The signing key certificate of the agent's
     *   owner.
     * @exception GeneralSecurityException if a cut &amp; paste
     *   attack was detected.
     */
    public void validateOwner(X509Certificate cert) throws GeneralSecurityException {
        EnvelopedData ed;
        Attributes attr;
        Map.Entry entry;
        SecretKey key;
        Iterator i;
        String s;
        byte[] b;
        byte[] p;
        Mac mac;
        if (groups_.size() == 0) {
            return;
        }
        attr = (Attributes) folders_.get(AgentStructure.GROUPS);
        if (attr == null) {
            groups_.clear();
            return;
        }
        for (i = groups_.entrySet().iterator(); i.hasNext(); ) {
            entry = (Map.Entry) i.next();
            s = (String) entry.getKey();
            s = attr.get(s);
            if (s == null) {
                i.remove();
                continue;
            }
            try {
                ed = (EnvelopedData) entry.getValue();
                key = ed.getSecretKey();
                mac = Mac.getInstance(AgentStructure.MAC);
                mac.init(key);
                p = mac.doFinal(cert.getEncoded());
                b = Base64.decode(s);
            } catch (CorruptedCodeException e) {
                i.remove();
                throw new GeneralSecurityException("MAC has corrupted Base64 encoding!");
            } catch (GeneralSecurityException e) {
                i.remove();
                throw e;
            }
            if (!Arrays.equals(b, p)) {
                i.remove();
                throw new GeneralSecurityException("Possible interleaving attack detected!\n+" + "Owner is " + cert.getSubjectDN().getName());
            }
        }
    }

    /**
     * This method verifies that the given certificate is
     * a valid recipient of the initialised access groups.
     * Any groups that do not have a recipient matching
     * the given certificate are removed. The PKCS#7
     * EnvelopedData instances are initialised with the
     * given private key and certificate.
     *
     * @param privKey The private decryption key.
     * @param cert The public encryption key certificate of the
     *   recipient.
     */
    public void validateRecipient(PrivateKey privkey, X509Certificate cert) throws NoSuchAlgorithmException {
        EnvelopedData ed;
        RecipientInfo ri;
        Map.Entry entry;
        Iterator i;
        if (groups_.size() == 0) {
            return;
        }
        for (i = groups_.entrySet().iterator(); i.hasNext(); ) {
            entry = (Map.Entry) i.next();
            ed = (EnvelopedData) entry.getValue();
            if (ed.hasRecipient(cert)) {
                try {
                    ed.init(cert, privkey);
                    continue;
                } catch (Exception e) {
                }
            }
            i.remove();
        }
    }

    /**
     * This method initialises the encryptor with the
     * given maps of folder/name and name/group entries.
     *
     * @param folders The map of folder/name entries.
     *   Each value of this map defines the name of a
     *   group in <code>groups</code>. In other words
     *   each value in this map corresponds to a key
     *   in the other.
     * @param groups The map of name/group entries.
     *   each value in this map consists of a PKCS#7
     *   EnvelopedData structure that contains the
     *   encrypted bulk encryption key to use when
     *   enveloping data that should be accessible
     *   only to the group of recipients defined in
     *   that structure.
     */
    public void init(Map folders, Map groups) {
        folders_ = folders;
        groups_ = groups;
    }

    /**
     * This method intialises this instance from the agent
     * structure stored in the Resource that was passed to
     * the constructor of this instance. It reads the
     * <code>P7</code> files and the <code>INSTALL.MF</code>
     * file and reconstructs the corresponding  mappings.<p>
     *
     * This method only reads in the appropriate structures.
     * The agent is not ready to be decrypted after calling
     * this method. This requires initialisation of the
     * decryption keys which is done through a call to
     * {@link #validateRecipient validateRecipient}.
     */
    public void init() throws IOException {
        EnvelopedData ed;
        ContentInfo ci;
        InputStream in;
        Attributes attr;
        DERDecoder dec;
        Map.Entry entry;
        Manifest mf;
        Iterator i;
        String s;
        String g;
        String k;
        folders_ = new HashMap();
        groups_ = new HashMap();
        mf = new Manifest();
        s = AgentStructure.INSTALL_MF;
        in = struct_.getInputStream(s);
        if (in == null) {
            throw new IOException("Cannot open " + s);
        }
        mf.load(in);
        in.close();
        for (i = mf.entryMap().entrySet().iterator(); i.hasNext(); ) {
            entry = (Map.Entry) i.next();
            k = (String) entry.getKey();
            attr = (Attributes) entry.getValue();
            g = AgentStructure.GROUPS;
            if (k.equals(g)) {
                if (!groups_.containsKey(g)) {
                    folders_.put(g, attr);
                }
                continue;
            }
            g = attr.get("Group");
            if (g == null) {
                continue;
            }
            g = g.trim();
            if (g.length() == 0) {
                continue;
            }
            if (!groups_.containsKey(g)) {
                s = AgentStructure.SEAL_INF + g + AgentStructure.EXT_P7;
                in = struct_.getInputStream(s);
                if (in == null) {
                    throw new IOException("[AgentEncryptor] Cannot open " + s);
                }
                dec = new DERDecoder(in, MAX_P7_LEN);
                ed = new EnvelopedData();
                ci = new ContentInfo(ed);
                try {
                    ci.decode(dec);
                    groups_.put(g, ed);
                } catch (ASN1Exception e) {
                    throw new IOException("[AgentEncryptor] Error parsing " + s + "\nCaught " + e.getClass().getName() + "(\"" + e.getMessage() + "\")");
                } finally {
                    dec.close();
                }
            }
            folders_.put(k, attr);
        }
    }

    /**
     * This method encrypts the folders in the agent
     * structure.
     *
     */
    public void encrypt() throws IOException {
        ByteArrayOutputStream bos;
        EnvelopedData ed;
        OutputStream out;
        InputStream in;
        Attributes attr;
        Map.Entry entry;
        Resource resource;
        Iterator i;
        String s;
        String g;
        int n;
        for (i = folders_.entrySet().iterator(); i.hasNext(); ) {
            entry = (Map.Entry) i.next();
            s = (String) entry.getKey();
            attr = (Attributes) entry.getValue();
            if (s.equals(AgentStructure.GROUPS)) {
                continue;
            }
            g = attr.get("Group");
            ed = (EnvelopedData) groups_.get(g);
            if (ed == null) {
                continue;
            }
            resource = struct_.subview(s);
            out = resource.getOutputStream(".ignore");
            out.write(0);
            out.close();
            bos = new ByteArrayOutputStream();
            n = Resources.zip(resource, bos);
            if (n == 0) {
                bos.close();
                continue;
            }
            in = new ByteArrayInputStream(bos.toByteArray());
            bos.close();
            s = AgentStructure.SEAL_INF + attr.get("EAR") + AgentStructure.EXT_EAR;
            out = struct_.getOutputStream(s);
            if (out == null) {
                throw new IOException("Cannot write to " + s);
            }
            try {
                ed.encryptBulkData(in, out);
            } catch (GeneralSecurityException e) {
                throw new IOException("[AgentEncryptor] Error while encrypting " + s + "\nCaught " + e.getClass().getName() + "(\"" + e.getMessage() + "\")");
            } finally {
                out.close();
                in.close();
            }
            resource.deleteAll();
        }
    }

    /**
     * This method decrypts the folders in the agent
     * structure.
     *
     */
    public void decrypt() throws IOException {
        ByteArrayOutputStream bos;
        EnvelopedData ed;
        OutputStream out;
        InputStream in;
        Attributes attr;
        Map.Entry entry;
        Resource resource;
        Iterator i;
        String s;
        String g;
        List list;
        for (i = folders_.entrySet().iterator(); i.hasNext(); ) {
            entry = (Map.Entry) i.next();
            s = (String) entry.getKey();
            attr = (Attributes) entry.getValue();
            if (s.equals(AgentStructure.GROUPS)) {
                continue;
            }
            resource = struct_.subview(s);
            list = resource.list();
            if (list.size() > 0) {
                throw new IOException("Destination folder \"" + s + "\" is not empty!");
            }
            g = attr.get("Group");
            ed = (EnvelopedData) groups_.get(g);
            if (ed == null) {
                System.err.println("[AgentEncryptor] Warning, cannot decrypt " + s);
                continue;
            }
            s = AgentStructure.SEAL_INF + attr.get("EAR") + AgentStructure.EXT_EAR;
            in = struct_.getInputStream(s);
            if (in == null) {
                throw new IOException("Cannot find " + s);
            }
            bos = new ByteArrayOutputStream();
            try {
                ed.decryptBulkData(in, bos);
            } catch (GeneralSecurityException e) {
                bos.close();
                throw new IOException("Error while decrypting " + s + "\nCaught " + e.getClass().getName() + "(\"" + e.getMessage() + "\")");
            } finally {
                in.close();
            }
            in = new ByteArrayInputStream(bos.toByteArray());
            bos.close();
            try {
                Resources.unzip(in, resource);
            } catch (Exception e) {
                throw new IOException("Error while unzipping " + s + "\nCaught " + e.getClass().getName() + "(\"" + e.getMessage() + "\")");
            } finally {
                in.close();
            }
        }
    }

    /**
     * This method returns <code>true</code> if the structure does
     * not contain encrypted data an thus must not be processed by
     * this instance.
     *
     * @return <code>true</code> if do processing is required.
     */
    public boolean isPlain() {
        return !struct_.exists(AgentStructure.INSTALL_MF);
    }
}

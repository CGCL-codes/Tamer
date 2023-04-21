package gnu.javax.crypto.keyring;

import gnu.java.security.Configuration;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

/**
 * An envelope entry is a generic container for some number of primitive and
 * other envelope entries.
 */
public abstract class EnvelopeEntry extends Entry {

    private static final Logger log = Logger.getLogger(EnvelopeEntry.class.getName());

    /** The envelope that contains this one (if any). */
    protected EnvelopeEntry containingEnvelope;

    /** The contained entries. */
    protected List entries;

    public EnvelopeEntry(int type, Properties properties) {
        super(type, properties);
        entries = new LinkedList();
        if (this.properties.get("alias-list") != null) this.properties.remove("alias-list");
    }

    protected EnvelopeEntry(int type) {
        super(type);
        entries = new LinkedList();
    }

    /**
   * Adds an entry to this envelope.
   * 
   * @param entry The entry to add.
   */
    public void add(Entry entry) {
        if (Configuration.DEBUG) log.entering(this.getClass().getName(), "add", entry);
        if (!containsEntry(entry)) {
            if (entry instanceof EnvelopeEntry) ((EnvelopeEntry) entry).setContainingEnvelope(this);
            entries.add(entry);
            if (Configuration.DEBUG) log.fine("Payload is " + (payload == null ? "" : "not ") + "null");
            makeAliasList();
        }
        if (Configuration.DEBUG) log.exiting(this.getClass().getName(), "add");
    }

    /**
   * Tests if this envelope contains a primitive entry with the given alias.
   * 
   * @param alias The alias to test.
   * @return True if this envelope (or one of the contained envelopes) contains
   *         a primitive entry with the given alias.
   */
    public boolean containsAlias(String alias) {
        if (Configuration.DEBUG) log.entering(this.getClass().getName(), "containsAlias", alias);
        String aliases = getAliasList();
        if (Configuration.DEBUG) log.fine("aliases = [" + aliases + "]");
        boolean result = false;
        if (aliases != null) {
            StringTokenizer tok = new StringTokenizer(aliases, ";");
            while (tok.hasMoreTokens()) if (tok.nextToken().equals(alias)) {
                result = true;
                break;
            }
        }
        if (Configuration.DEBUG) log.exiting(this.getClass().getName(), "containsAlias", Boolean.valueOf(result));
        return result;
    }

    /**
   * Tests if this envelope contains the given entry.
   * 
   * @param entry The entry to test.
   * @return True if this envelope contains the given entry.
   */
    public boolean containsEntry(Entry entry) {
        if (entry instanceof EnvelopeEntry) return entries.contains(entry);
        if (entry instanceof PrimitiveEntry) for (Iterator it = entries.iterator(); it.hasNext(); ) {
            Entry e = (Entry) it.next();
            if (e.equals(entry)) return true;
            if ((e instanceof EnvelopeEntry) && ((EnvelopeEntry) e).containsEntry(entry)) return true;
        }
        return false;
    }

    /**
   * Returns a copy of all entries this envelope contains.
   * 
   * @return All contained entries.
   */
    public List getEntries() {
        return new ArrayList(entries);
    }

    /**
   * Gets all primitive entries that have the given alias. If there are any
   * masked entries that contain the given alias, they will be returned as well.
   * 
   * @param alias The alias of the entries to get.
   * @return A list of all primitive entries that have the given alias.
   */
    public List get(String alias) {
        if (Configuration.DEBUG) log.entering(this.getClass().getName(), "get", alias);
        List result = new LinkedList();
        for (Iterator it = entries.iterator(); it.hasNext(); ) {
            Entry e = (Entry) it.next();
            if (e instanceof EnvelopeEntry) {
                EnvelopeEntry ee = (EnvelopeEntry) e;
                if (!ee.containsAlias(alias)) continue;
                if (ee instanceof MaskableEnvelopeEntry) {
                    MaskableEnvelopeEntry mee = (MaskableEnvelopeEntry) ee;
                    if (mee.isMasked()) {
                        if (Configuration.DEBUG) log.fine("Processing masked entry: " + mee);
                        result.add(mee);
                        continue;
                    }
                }
                if (Configuration.DEBUG) log.fine("Processing unmasked entry: " + ee);
                result.addAll(ee.get(alias));
            } else if (e instanceof PrimitiveEntry) {
                PrimitiveEntry pe = (PrimitiveEntry) e;
                if (pe.getAlias().equals(alias)) result.add(e);
            }
        }
        if (Configuration.DEBUG) log.exiting(this.getClass().getName(), "get", result);
        return result;
    }

    /**
   * Returns the list of all aliases contained by this envelope, separated by a
   * semicolon (';').
   * 
   * @return The list of aliases.
   */
    public String getAliasList() {
        String list = properties.get("alias-list");
        if (list == null) return ""; else return list;
    }

    /**
   * Removes the specified entry.
   * 
   * @param entry The entry.
   * @return True if an entry was removed.
   */
    public boolean remove(Entry entry) {
        if (Configuration.DEBUG) log.entering(this.getClass().getName(), "remove", entry);
        boolean ret = false;
        for (Iterator it = entries.iterator(); it.hasNext(); ) {
            Entry e = (Entry) it.next();
            if (e instanceof EnvelopeEntry) {
                if (e == entry) {
                    it.remove();
                    ret = true;
                    break;
                }
                if (((EnvelopeEntry) e).remove(entry)) {
                    ret = true;
                    break;
                }
            } else if (e instanceof PrimitiveEntry) {
                if (((PrimitiveEntry) e).equals(entry)) {
                    it.remove();
                    ret = true;
                    break;
                }
            }
        }
        if (ret) {
            if (Configuration.DEBUG) log.fine("State before: " + this);
            payload = null;
            makeAliasList();
            if (Configuration.DEBUG) log.fine("State after: " + this);
        }
        if (Configuration.DEBUG) log.exiting(this.getClass().getName(), "remove", Boolean.valueOf(ret));
        return ret;
    }

    /**
   * Removes all primitive entries that have the specified alias.
   * 
   * @param alias The alias of the entries to remove.
   * @return <code>true</code> if <code>alias</code> was present and was
   *         successfully trmoved. Returns <code>false</code> if
   *         <code>alias</code> was not present in the list of aliases in this
   *         envelope.
   */
    public boolean remove(String alias) {
        if (Configuration.DEBUG) log.entering(this.getClass().getName(), "remove", alias);
        boolean result = false;
        for (Iterator it = entries.iterator(); it.hasNext(); ) {
            Entry e = (Entry) it.next();
            if (e instanceof EnvelopeEntry) {
                EnvelopeEntry ee = (EnvelopeEntry) e;
                result = ee.remove(alias) || result;
            } else if (e instanceof PrimitiveEntry) {
                PrimitiveEntry pe = (PrimitiveEntry) e;
                if (pe.getAlias().equals(alias)) {
                    it.remove();
                    result = true;
                }
            }
        }
        if (result) {
            if (Configuration.DEBUG) log.fine("State before: " + this);
            payload = null;
            makeAliasList();
            if (Configuration.DEBUG) log.fine("State after: " + this);
        }
        if (Configuration.DEBUG) log.exiting(this.getClass().getName(), "remove", Boolean.valueOf(result));
        return result;
    }

    public String toString() {
        return new StringBuilder("Envelope{").append(super.toString()).append(", entries=").append(entries).append("}").toString();
    }

    protected void encodePayload() throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream(1024);
        DataOutputStream out = new DataOutputStream(bout);
        for (Iterator it = entries.iterator(); it.hasNext(); ) ((Entry) it.next()).encode(out);
    }

    protected void setContainingEnvelope(EnvelopeEntry e) {
        if (containingEnvelope != null) throw new IllegalArgumentException("envelopes may not be shared");
        containingEnvelope = e;
    }

    protected void decodeEnvelope(DataInputStream in) throws IOException {
        this.entries.clear();
        while (true) {
            int type = in.read();
            switch(type) {
                case EncryptedEntry.TYPE:
                    add(EncryptedEntry.decode(in));
                    break;
                case PasswordEncryptedEntry.TYPE:
                    add(PasswordEncryptedEntry.decode(in));
                    break;
                case PasswordAuthenticatedEntry.TYPE:
                    add(PasswordAuthenticatedEntry.decode(in));
                    break;
                case AuthenticatedEntry.TYPE:
                    add(AuthenticatedEntry.decode(in));
                    break;
                case CompressedEntry.TYPE:
                    add(CompressedEntry.decode(in));
                    break;
                case CertificateEntry.TYPE:
                    add(CertificateEntry.decode(in));
                    break;
                case PublicKeyEntry.TYPE:
                    add(PublicKeyEntry.decode(in));
                    break;
                case PrivateKeyEntry.TYPE:
                    add(PrivateKeyEntry.decode(in));
                    break;
                case CertPathEntry.TYPE:
                    add(CertPathEntry.decode(in));
                    break;
                case BinaryDataEntry.TYPE:
                    add(BinaryDataEntry.decode(in));
                    break;
                case -1:
                    return;
                default:
                    throw new MalformedKeyringException("unknown type " + type);
            }
        }
    }

    private void makeAliasList() {
        if (Configuration.DEBUG) log.entering(this.getClass().getName(), "makeAliasList");
        if (!entries.isEmpty()) {
            StringBuilder buf = new StringBuilder();
            String aliasOrList;
            for (Iterator it = entries.iterator(); it.hasNext(); ) {
                Entry entry = (Entry) it.next();
                aliasOrList = null;
                if (entry instanceof EnvelopeEntry) aliasOrList = ((EnvelopeEntry) entry).getAliasList(); else if (entry instanceof PrimitiveEntry) aliasOrList = ((PrimitiveEntry) entry).getAlias(); else if (Configuration.DEBUG) log.fine("Entry with no Alias. Ignored: " + entry);
                if (aliasOrList != null) {
                    aliasOrList = aliasOrList.trim();
                    if (aliasOrList.trim().length() > 0) {
                        buf.append(aliasOrList);
                        if (it.hasNext()) buf.append(';');
                    }
                }
            }
            String aliasList = buf.toString();
            properties.put("alias-list", aliasList);
            if (Configuration.DEBUG) log.fine("alias-list=[" + aliasList + "]");
            if (containingEnvelope != null) containingEnvelope.makeAliasList();
        }
        if (Configuration.DEBUG) log.exiting(this.getClass().getName(), "makeAliasList");
    }
}

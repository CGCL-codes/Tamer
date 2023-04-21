package org.exos.exchange;

import java.util.Hashtable;
import java.util.Vector;
import org.exos.InternalErrorException;
import org.exos.crypto.CryptoFactory;
import org.exos.crypto.PrivateKey;
import org.exos.crypto.PublicKey;
import org.exos.dao.CryptoInfo;
import org.exos.dao.Host;
import org.exos.exchange.web.WebDataAdapter;

/**
 * Abstract class representing the interface for a data exchange adapter.
 * <p>
 * Every data adapter needs to extend from this class.
 * <p>
 * Telephone numbers have to be formatted like this: "iiiiyyxxxxxxx"
 * , for instance: "0041795623344"
 * @author ada
 */
public abstract class DataAdapter {

    /**
	 * Creates an instance of the DataAdapter requested and returns it.
	 * @param protocol the requested adapter protocol
	 * @return the concrete data adapter
	 * @throws org.exos.exchange.NoSuchProtocolException if no adapter could
	 *	be found for the supplied protocol
	 */
    public static DataAdapter loadAdapter(String protocol) throws NoSuchProtocolException {
        if (protocol.equals("WEB")) {
            return new WebDataAdapter();
        }
        throw new NoSuchProtocolException("No such protocol: " + protocol);
    }

    /**
	 * Initializes the adapter with the necessary information. If the account
	 * for which data should be fetched or updated changes, the adapter
	 * needs to be re-initialized.
	 * @param host the host
	 * @param myTel the telephone identifier of the local client
	 * @param pkForSigning the public key used to sign the update requests
	 */
    public abstract void initAdapter(Host host, String myTel, PrivateKey pkForSigning) throws WrongProtocolVersionException;

    /**
	 * Informs the adapter over upcoming fetch operations.
	 * @throws org.exos.exchange.HostNotAvailableException if the host
	 *	could not be reached (most likely because of a connection problem)
	 */
    public abstract void startFetch() throws HostNotAvailableException;

    /**
	 * Fetches the status information for a person.
	 * @param tel the persons telephone number
	 * @return returns the status or null if the person could not be found
	 * @throws org.exos.exchange.HostNotAvailableException if the host
	 *	could not be reached (most likely because of a connection problem)
	 */
    public abstract Status getStatus(String tel) throws HostNotAvailableException, InternalErrorException;

    /**
	 * Fetches the public keys used for encryption and verifying belonging to the user 'tel'.
	 * @param tel the telephone identifier
	 * @return the PubKeys data of the keys as it was uploaded by another EXOS client:
	 *	DER <code>SubjectPublicKeyInfo</code> base64 encoded
	 * @throws NotVerifiedException if the key data could not be verified
	 *	by the servers public key
	 * @throws org.exos.exchange.HostNotAvailableException if the host
	 *	could not be reached (most likely because of a connection problem)
	 */
    public abstract PubKeys getPublicKeys(String tel) throws NotVerifiedException, HostNotAvailableException, InternalErrorException;

    /**
	 * Fetches the fragment listing and returns a list of all fragments
	 * excluding the ones where the id matches idPattern.
	 * <p>
	 * The returned list ({@link java.util.Hashtable}) uses the fragment
	 * id/name (String) as key
	 * and has {@link ListEntry} values mapped.
	 * </p>
	 * @param tel the telephone identifier
	 * @param idPattern pattern to limit the list
	 * @return a list in form of a hash table. If there are no matching fragments
	 * or no data entries at all the hash table is empty.
	 * @throws org.exos.exchange.HostNotAvailableException if the host
	 *	could not be reached (most likely because of a connection problem)
	 */
    public abstract Hashtable getList(String tel, String idPattern) throws HostNotAvailableException, InternalErrorException;

    /**
	 * Fetches and returns the data of a fragment
	 * @param tel the telephone identifier
	 * @param id the fragment identifier (part 1 of the EXOS URL)
	 * @param keyId the key identifier (part 2 of the EXOS URL)
	 * @return a byte array containing the data or null if the
	 *  fragment could not be found
	 * @throws org.exos.exchange.HostNotAvailableException if the host
	 *	could not be reached (most likely because of a connection problem)
	 */
    public abstract byte[] getData(String tel, String id, int keyId) throws HostNotAvailableException, InternalErrorException;

    /**
	 * Fetches and returns the personal key out of a key group (keyId).
	 * @param tel the telephone identifier
	 * @param keyId the identifier of the key
	 * @return a byte array containing the still encrypted version of the group
	 *	key, null if there is no key in this key group for the initialized myTel
	 * @throws org.exos.exchange.HostNotAvailableException if the host
	 *	could not be reached (most likely because of a connection problem)
	 */
    public abstract byte[] getKey(String tel, int keyId) throws HostNotAvailableException, InternalErrorException;

    /**
	 * Informs the adapter that there is not going to be fetched anymore data.
	 */
    public abstract void finishFetch();

    /**
	 * Informs the adapter that the client wants to update data.
	 * @throws org.exos.exchange.HostNotAvailableException if host is not
	 *	reachable
	 */
    public abstract void startUpdate() throws HostNotAvailableException, InternalErrorException;

    /**
	 * Sets a group key (update or initial upload).
	 * @param keyId the key identifier (most likely this is some sort of a group id)
	 * @param keyVersion the version of the key, has to change only if the key changes,
	 *	not if just a new person gets added to the group
	 * @param telList an array of telephone identifiers
	 * @param keyList an array of byte arrays, entry i belongs to telList[i],
	 *	each entry byte array contains the encrypted version of the group key for
	 *	the person telList[i]
	 * @param numKeys the number of key entries
	 * @throws org.exos.exchange.HostNotAvailableException if the host
	 *	could not be reached (most likely because of a connection problem)
	 */
    public abstract void setKey(int keyId, int keyVersion, String[] telList, byte[][] keyList, int numKeys) throws HostNotAvailableException, InternalErrorException;

    /**
	 * Sets data for a fragment (update or initial upload).
	 * @param dataId the fragment identifier
	 * @param dataVersion the last modified date of the data
	 * @param keyId the key id (the key has to be uploaded first, see {@link #setKey(int, int, java.lang.String[], byte[][], int) }
	 * @param data the byte (encrypted) data to upload
	 * @param length the length of the byte data
	 * @throws org.exos.exchange.HostNotAvailableException if the host
	 *	could not be reached (most likely because of a connection problem)
	 */
    public abstract void setData(String dataId, long dataVersion, int keyId, byte[] data, int length) throws HostNotAvailableException, InternalErrorException;

    /**
	 * Deletes a key with the supplied id. The data adapter is not responsible
	 * for keeping track of all keys and fragments so the caller needs to know
	 * if the key he is deleting is not used anymore.
	 * @param keyId the key id
	 * @throws org.exos.exchange.HostNotAvailableException if the host
	 *	could not be reached (most likely because of a connection problem)
	 */
    public abstract void deleteKey(int keyId, int keyVers) throws HostNotAvailableException, InternalErrorException;

    /**
	 * Deletes a fragment with the according data id
	 * @param dataId the data identifier
	 * @throws org.exos.exchange.HostNotAvailableException if the host
	 *	could not be reached (most likely because of a connection problem)
	 */
    public abstract void deleteData(String dataId, int keyId) throws HostNotAvailableException, InternalErrorException;

    /**
	 * Informs the adapter that the caller finished updating.
	 */
    public abstract void finishUpdate();

    /**
	 * Gets the key data of the data server to store in a host object
	 * @param hostUrl
	 * @param ttpData
	 * @return data server key data
	 * @throws HostNotAvailableException
	 * @throws InternalErrorException
	 */
    public abstract byte[] getDataServerKeyData(String hostUrl, byte[] ttpData) throws HostNotAvailableException, NotVerifiedException, InternalErrorException;

    /**
	 * List entry of a fragment list.
	 */
    public class ListEntry {

        private String dataId;

        private long dataLastModified;

        private int keyId;

        private int keyVersion;

        public ListEntry(String dataID, long dataLastModified, int keyId, int keyVersion) {
            this.dataId = dataID;
            this.dataLastModified = dataLastModified;
            this.keyId = keyId;
            this.keyVersion = keyVersion;
        }

        /**
		 * Returns the fragment identifier
		 * @return the fragment identifier
		 */
        public String getDataId() {
            return dataId;
        }

        /**
		 * Returns the last modified date from this data fragment
		 * @return the last modified date in milliseconds
		 */
        public long getDataLastModified() {
            return dataLastModified;
        }

        /**
		 * Returns the key id of the key that was used to encrypt this fragment
		 * @return the key id
		 */
        public int getKeyId() {
            return keyId;
        }

        /**
		 * Returns the key version of the key that was used to encrypt this fragment
		 * @return the key version
		 */
        public int getKeyVersion() {
            return keyVersion;
        }
    }

    /**
	 * Status for a persons data.
	 */
    public class Status {

        private long lastModifiedPubKeys;

        private long lastModifiedFragments;

        public Status(long lastModifiedPubKeys, long lastModifiedFragments) {
            this.lastModifiedFragments = lastModifiedFragments;
            this.lastModifiedPubKeys = lastModifiedPubKeys;
        }

        /**
		 * Returns the last modified date of the newest fragment uploaded by
		 * the person this status belongs to.
		 * @return the last modified date in milliseconds
		 */
        public long getLastModifiedFragments() {
            return lastModifiedFragments;
        }

        /**
		 * Returns the last modified date of the public keys of the person
		 * this status belongs to.
		 * @return the last modified date in milliseconds
		 */
        public long getLastModifiedPubKeys() {
            return lastModifiedPubKeys;
        }
    }

    /**
	 * Pubkeys Class for the other clients public keys for encryption and verifying
	 */
    public class PubKeys {

        private byte[] PublicKeyForEncryption;

        private byte[] PublicKeyForVerifying;

        public PubKeys(byte[] PublicKeyForEncryption, byte[] PublicKeyForVerifying) {
            this.PublicKeyForEncryption = PublicKeyForEncryption;
            this.PublicKeyForVerifying = PublicKeyForVerifying;
        }

        /**
		 * Returns the Public key for encryption
		 * @return the last modified date in milliseconds
		 */
        public byte[] getPublicKeyForEncryption() {
            return PublicKeyForEncryption;
        }

        /**
		 * Returns the public key for verifying
		 * @return the last modified date in milliseconds
		 */
        public byte[] getPublicKeyForVerifying() {
            return PublicKeyForVerifying;
        }
    }
}

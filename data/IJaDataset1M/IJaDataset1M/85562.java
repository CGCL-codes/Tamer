package eln.signatures;

import eln.client.event.*;
import eln.server.NotebookServerProxy;
import eln.nob.*;
import eln.VersionInfo;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.StringTokenizer;
import java.lang.Boolean;
import com.entrust.toolkit.User;
import com.entrust.toolkit.util.SecureStringBuffer;
import com.entrust.toolkit.exceptions.UserException;
import iaik.x509.*;
import iaik.asn1.*;
import javax.swing.*;
import iaik.ixsil.algorithms.CanonicalizationAlgorithmImplCanonicalXML;
import iaik.ixsil.algorithms.DigestAlgorithmImplSHA1;
import iaik.ixsil.core.Signer;
import com.entrust.toolkit.KeyAndCertificateSource;
import iaik.ixsil.keyinfo.x509.KeyProviderImplX509Data;
import iaik.asn1.structures.Name;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.Key;
import iaik.ixsil.core.SignerReference;
import iaik.ixsil.core.SignerSignedInfo;
import iaik.ixsil.core.SignerSignature;
import iaik.ixsil.init.IXSILInit;
import iaik.ixsil.keyinfo.SignerKeyManager;
import iaik.ixsil.util.URI;
import org.w3c.dom.Document;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import iaik.ixsil.core.Verifier;
import iaik.ixsil.core.VerifierSignature;
import com.entrust.toolkit.xml.dsig.keyinfo.tokenref.KeyProviderImplSecurityTokenRef;
import iaik.ixsil.keyinfo.KeyProviderInterface;
import com.entrust.toolkit.x509.CertVerifier;

/**
 *  Description of the Class
 * 
 */
public class BaseEntrustSigningUtility extends BaseSigningUtility {

    /**
	 *  Private Member Variables
	 */
    private NObNode mNOb = null;

    private NObListNode mParent = null;

    private SubmitEventSource mSubmitEventSource = new SubmitEventSource();

    /**
	 *  CONSTRUCTOR/DESTRUCTOR METHODS
	 *
	 *@param  aNOb  Description of the Parameter
	 *@param  nsp   Description of the Parameter
	 */
    protected BaseEntrustSigningUtility() {
    }

    public BaseEntrustSigningUtility(NObNode aNOb, NotebookServerProxy nsp) {
        super(aNOb, nsp);
    }

    /**
	 *  Shorthand to get subject distinguished name from Entrust User
	 *
	 *@return    The signerDNString value
	 */
    public String getSignerDNString() {
        String dn = null;
        try {
            dn = ((User) mUser).getVerificationCertificate().getSubjectDN().getName();
        } catch (Exception ue) {
            System.err.println(ue);
        }
        return dn;
    }

    /**
	 *  The getAuthorID method uses a StringTokenizer to pull the authorName out of
	 *  a full Distinguished Name.
	 *
	 *@param  theUser  Description of the Parameter
	 *@return          Returns a String representing the AuthorName
	*/
    public String getSignerID(User theUser) {
        String commonName = null;
        try {
            commonName = ((Name) theUser.getVerificationCertificate().getSubjectDN()).getRDN(ObjectID.commonName);
        } catch (ClassCastException cce) {
            System.err.println("CommonName not found in DN. (Principal not an iaik.asn1.structures.Name class)");
            commonName = getSignerDNString();
        } catch (UserException ue) {
            System.err.println(ue);
        }
        return commonName;
    }

    /**
	 *  login - Shows login dialog and creates user object by logging into entrust with an entrust user
	 *@param	 Boolean showMeaning - allow user to choose meaning
	 *@return    User created
	 */
    public Object login() {
        return login(true);
    }

    public Object login(boolean showMeaning) {
        User user = null;
        String action = "Signing";
        GridBagConstraints gridBagConstraints;
        if (!showMeaning) action = "Verify";
        String itemType = (String) getNObNode().get("level");
        String capItemType = itemType.substring(0, 1).toUpperCase() + itemType.substring(1);
        String dialogTitle = VersionInfo.Version + ": " + action + " " + capItemType + ": " + getNObNode().get("label");
        Label unamelabel = new Label("Username:");
        Label pwlabel = new Label("Password:");
        Label meaningslabel = new Label("Meanings:");
        Choice meanings = new Choice();
        for (int i = 0; i < gAllowedMeanings.length; i++) {
            meanings.addItem(gAllowedMeanings[i]);
        }
        Label commentlabel = new Label("Comments:");
        TextArea comments = new TextArea("", 5, 30, TextArea.SCROLLBARS_VERTICAL_ONLY);
        JPasswordField password = new JPasswordField(15);
        password.setEditable(true);
        JPanel pane = new JPanel();
        pane.setLayout(new java.awt.GridBagLayout());
        fileDialogButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                fileDialogButton_actionPerformed(e);
            }
        });
        pane.setBorder(BorderFactory.createLineBorder(Color.gray, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 175, 0, 0);
        pane.add(fileDialogButton, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        pane.add(password, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pane.add(unamelabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pane.add(pwlabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pane.add(username, gridBagConstraints);
        if (showMeaning) {
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 4;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            pane.add(meaningslabel, gridBagConstraints);
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 3;
            gridBagConstraints.gridy = 4;
            gridBagConstraints.ipadx = 125;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 0);
            pane.add(meanings, gridBagConstraints);
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 5;
            gridBagConstraints.gridheight = 6;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            pane.add(commentlabel, gridBagConstraints);
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 3;
            gridBagConstraints.gridy = 5;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 29);
            pane.add(comments, gridBagConstraints);
        }
        int choice = JOptionPane.showOptionDialog(null, pane, dialogTitle, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (choice == JOptionPane.OK_OPTION) {
            String uname = userfile.getText().trim();
            String pword = new String(password.getPassword());
            String comment = comments.getText().trim();
            String meaning = meanings.getSelectedItem().toString();
            try {
                if (uname.length() > 0 && pword.length() > 0) {
                    try {
                        user = SigningUtils.login(uname, new SecureStringBuffer(pword));
                    } catch (com.entrust.toolkit.exceptions.UserFatalException re) {
                        JOptionPane.showMessageDialog(null, "Error logging in: check username/password", "Entrust Login Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Error logging in: you must enter a username and password", "Entrust Login Error", JOptionPane.ERROR_MESSAGE);
                }
                boolean response = user.isLoggedIn();
                if (response == true) {
                    mUser = user;
                    mCurrentMeaning = meaning;
                    mComment = comment;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
        return user;
    }

    /**
	 *  The sign method generates a digital signature onto a NOb object. This
	 *  method uses a variable theLevel to determine how to sign the object. If the
	 *  level is 0, it will sign the data field of the NOb object, saving the
	 *  certificate used into 'cert0', and the generated signature into sig0. On
	 *  proceeding iterations (theLevel>=1), it will sign the last signature
	 *  (sig[theLevel-1]), and save into cert[theLevel] and sig[theLevel]. The
	 *  signature is generated by creating a SignerInfo object with the provided
	 *  certificate and user's private key. The signature and certificate are
	 *  stored as a DER-encoded byte array converted to Base64 notation.
	 *
	 */
    public void sign() {
        User signingUser = (User) login();
        if (signingUser != null) {
            mCurrentSigner = getSignerID(signingUser);
            mFullNOb = getFullNOb();
            BufferedInputStream bufin = null;
            String currentTime = getCurrentTime();
            KeyAndCertificateSource m_source;
            m_source = new KeyAndCertificateSource();
            try {
                PrivateKey pk = signingUser.getSigningKey();
                X509Certificate cert = signingUser.getVerificationCertificate();
                m_source.setSigningInfo(pk, cert);
                String initprops = "file:/C:/Program%20Files/ELNapp5.2/lib/entrust/properties/init.properties";
                URI initProps = new URI(initprops);
                System.out.println("Initializing IXSIL properties from \"" + initProps + "\"...");
                IXSILInit.init(initProps);
                String uriTobeSigned = mFullNOb.toString();
                Signer signer = null;
                URI uri = new URI(uriTobeSigned);
                signer = new Signer(uri);
                SignerSignature sig = signer.getSignature();
                sig.setId("Signature001");
                iaik.ixsil.core.Object dateObj = sig.createObject(currentTime);
                dateObj.setId("date");
                sig.addObject(dateObj);
                iaik.ixsil.core.Object mObj = sig.createObject(mCurrentMeaning);
                mObj.setId("meaning");
                sig.addObject(mObj);
                iaik.ixsil.core.Object sObj = sig.createObject(mCurrentSigner);
                sObj.setId("signer");
                sig.addObject(sObj);
                iaik.ixsil.core.Object cObj = sig.createObject(mComment);
                cObj.setId("comment");
                sig.addObject(cObj);
                SignerSignedInfo signedInfo = signer.getSignature().getSignerSignedInfo();
                CanonicalizationAlgorithmImplCanonicalXML c14nAlg = new CanonicalizationAlgorithmImplCanonicalXML();
                signedInfo.setCanonicalizationAlgorithm(c14nAlg);
                SigningUtils.configureAlgorithm(m_source, signedInfo);
                SignerReference reference = signedInfo.createReference();
                if (hasData()) {
                    reference.setURI(new URI(uriTobeSigned));
                } else {
                    InputStream inputStream = getPageStructure();
                    reference.setExplicitData(inputStream, SignerReference.EXPLICITDATATYPE_RAW_);
                }
                DigestAlgorithmImplSHA1 digestAlg = new DigestAlgorithmImplSHA1();
                reference.setDigestAlgorithm(digestAlg);
                signedInfo.addReference(reference);
                int x509Data = SigningUtils.X509DATA_CERTIFICATE;
                SignerKeyManager keyManager = SigningUtils.setX509KeyInfo(x509Data, signer, m_source.getVerificationCertificate(), "KeyInfo001");
                SigningUtils.signKeyInfo(signedInfo, keyManager);
                SigningUtils.signObjects(signedInfo, signer.getSignature().getObjects());
                SigningUtils.processTransforms(signer.getSignature(), false);
                signer.getSignature().sign();
                OutputStream os = new ByteArrayOutputStream();
                Document signedDoc = signer.toDocument();
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer trans = tf.newTransformer();
                trans.transform(new DOMSource(signedDoc.getDocumentElement()), new StreamResult(os));
                String resultxml = os.toString();
                if (resultxml.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")) resultxml = resultxml.substring(38);
                Hashtable sigProps = new Hashtable();
                sigProps.put("sigxml" + mTheSignerLevel, resultxml);
                submitSignedNOb(sigProps);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mFullNOb = getFullNOb();
            mTheSignerLevel = getSignerLevel(mFullNOb);
            setSizes(mTheSignerLevel);
            setTableValues(mTheSignerLevel);
            fireTableDataChanged();
            try {
                signingUser.logout();
            } catch (com.entrust.toolkit.exceptions.UserNotLoggedInException unli) {
                System.out.println("Signer was not logged in: " + unli);
            }
        } else {
            System.err.println("Login unsuccessful");
        }
    }

    /**
	 *  The verifySignature method will verify the specified signature and the
	 *  certs if setCheckCerts(true) has been called.
	 *
	 *@param  theSig         the output from the PKCS7EncodeStream method,
	 *      converted to a byte[]
	 *@param  theSigNum      Description of the Parameter
	 *@return                Description of the Return Value
	 */
    private boolean verifySignature(byte[] theSig, int theSigNum) {
        boolean sigStat = false;
        Boolean certStat = null;
        byte[] sigDate = getSigDate(theSigNum);
        String xmlsig = null;
        xmlsig = (String) mFullNOb.get("sigxml" + (theSigNum));
        if (xmlsig != null) {
            try {
                String initprops = "file:/C:/Program%20Files/ELNapp5.2/lib/entrust/properties/init.properties";
                CertVerifier certverifier = (CertVerifier) ((User) mUser).getCertVerifier();
                KeyAndCertificateSource m_source = new KeyAndCertificateSource(certverifier);
                IXSILInit.init(new URI(initprops));
                String m_schemaLocations = SigningUtils.initSchemaLocations();
                String signatureSelector = "//*[@Id=\"Signature001\"]";
                String noNamespaceSchemaLocation = null;
                String additionalNSPrefixes = null;
                m_schemaLocations = SigningUtils.getDSIGschemaLocations() + " " + SigningUtils.getWSschemaLocations();
                String newSigStr = "";
                StringTokenizer tok = new StringTokenizer(xmlsig, "\n");
                while (tok.hasMoreTokens()) {
                    String line = tok.nextToken().trim();
                    newSigStr = newSigStr + line;
                }
                ByteArrayInputStream istream = new ByteArrayInputStream(newSigStr.getBytes());
                Verifier verifier = new Verifier(istream, new URI(mFullNOb.toString()), signatureSelector, additionalNSPrefixes, noNamespaceSchemaLocation, m_schemaLocations);
                SigningUtils.setTrustmanager(verifier, m_source);
                VerifierSignature signature = verifier.getSignature();
                iaik.ixsil.core.VerifierSignedInfo mySignedInfo = signature.getVerifierSignedInfo();
                iaik.ixsil.algorithms.SignatureAlgorithm alg = mySignedInfo.getSignatureAlgorithm();
                if (!hasData()) {
                    mySignedInfo.setRawData(getPageStructure());
                }
                if (alg.verifierKeyUnknown()) {
                    KeyProviderInterface[] providers = signature.getKeyManager().getKeyProviders();
                    for (int i = 0; i < providers.length; i++) {
                        KeyProviderInterface provider = providers[i];
                        if (provider instanceof iaik.ixsil.keyinfo.x509.KeyProviderImplX509Data) {
                            Key key = provider.getVerifierKey();
                        }
                    }
                }
                try {
                    signature.verify();
                    sigStat = true;
                } catch (Exception e) {
                    sigStat = false;
                    System.err.println("Exception verifying signature " + theSigNum + ":" + e);
                }
                if (gCheckCerts && sigStat) {
                    try {
                        validatePublicKey(signature);
                        certStat = new Boolean(true);
                    } catch (Exception e) {
                        certStat = new Boolean(false);
                    }
                }
            } catch (Exception e) {
                System.err.println("ERROR: " + e);
            }
            setupTable(theSigNum, sigStat, certStat);
            setTableValues(mTheSignerLevel);
            fireTableDataChanged();
        }
        return sigStat;
    }

    /**
	 *  The verifySignatures method will verify all of the signatures contained
	 *  within a NOb object.
	 */
    public void verifyAllSignatures() {
        mFullNOb = getFullNOb();
        int numberOfSignatures = mTheSignerLevel;
        int i;
        if (!(mUser != null && ((User) mUser).isLoggedIn())) {
            login(false);
        }
        if (mUser != null && ((User) mUser).isLoggedIn()) {
            try {
                for (i = 0; i < numberOfSignatures; i++) {
                    verifySignature(getSig(i), i);
                }
            } catch (IOException ioe) {
                System.err.println("Error opening NOb data for signature verification: " + ioe);
            }
        }
    }

    /**
	   * Determines whether a verification public key came from a valid certificate
	   * or merely a KeyValue element in the XML Signature.  Please refer to the
	   * javadoc in Utils.isValidKeyValueInfo(PublicKey key) for more info.<p>
	   *
	   * Pre-condition -- Your application should invoke this method after calling
	   *                  VerifierSignature.verify().  Also, setTrustmanager()
	   *                  must have been called before the signature was verified.
	   *                  Please refer to the Verify.java sample.
	   *
	   * @param signature a VerifierSignature
	   * @see #isValidKeyValueInfo(PublicKey key)
	   */
    public void validatePublicKey(VerifierSignature signature) throws Exception {
        KeyProviderInterface kp = signature.getKeyManager().getKeyProvider();
        if (kp instanceof KeyProviderImplX509Data || kp instanceof KeyProviderImplSecurityTokenRef) {
            System.out.println("The signature was verified using a valid certificate.");
            return;
        }
        if (!isValidKeyValueInfo((PublicKey) signature.getKeyManager().getVerifierKey(), "")) {
            throw new Exception("Failed to validate the KeyValue information");
        }
    }
}

package de.xmlsicherheit.encrypt;

/**
 * <p>Stores the information from the wizard and returns the values in a
 * <code>EncryptionWizard</code> object.</p>
 *
 * <p>This plug-in is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.</p>
 *
 * <p>This plug-in is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.</p>
 *
 * <p>You should have received a copy of the GNU Lesser General Public License along
 * with this library;<br>
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA</p>
 *
 * @author Dominik Schadow (info@xml-sicherheit.de), www.xml-sicherheit.de
 * @version 1.6.1, 03.10.2006
 */
public class EncryptionWizard {

    /** The resource to encrypt. */
    private String resource;

    /** File to encrypt. */
    private String filePath;

    /** XPath, if selected. */
    private String xpath;

    /** BSP. */
    private boolean bsp;

    /** Encryption content. */
    private boolean content;

    /** Encryption algorithm. */
    private String encryptionAlgorithm;

    /** Encryption key algorithm. */
    private String keyAlgorithm;

    /** Encryption key size. */
    private String keyAlgorithmSize;

    /** Key cipher algorithm. */
    private String keyCipherAlgorithm;

    /** Key file. */
    private String keyFile;

    /** Key file password. */
    private String keyFilePassword;

    /** Encryption ID. */
    private String encryptionId;

    /**
	 * Sets the document (fragment) to encrypt.
	 *
	 * @param wResource Selection what to encrypt
	 */
    public void setResource(String wResource) {
        resource = wResource;
    }

    /**
	 * The file to encrypt.
	 *
	 * @param wFilePath The selected file to encrypt
	 */
    public void setFilePath(String wFilePath) {
        filePath = wFilePath;
    }

    /**
	 * Sets the selected XPath.
	 *
	 * @param wXPath XPath selection
	 */
    public void setXpath(String wXPath) {
        xpath = wXPath;
    }

    /**
	 * Basic Security Profil compliant encryption or not.
	 *
	 * @param wBSP True or false
	 */
    public void setBsp(boolean wBSP) {
        bsp = wBSP;
    }

    /**
	 * Encrypt only element content or complete element.
	 *
	 * @param wContent True or false
	 */
    public void setContent(boolean wContent) {
        content = wContent;
    }

    /**
	 * Sets the encryption algorithm.
	 *
	 * @param wEncryptionAlgorithm Encryption algorithm
	 */
    public void setEncryptionAlgorithm(String wEncryptionAlgorithm) {
        encryptionAlgorithm = wEncryptionAlgorithm;
    }

    /**
	 * Sets the encryption key algorithm.
	 *
	 * @param wKeyAlgorithm Encryption key algorithm
	 */
    public void setKeyAlgorithm(String wKeyAlgorithm) {
        keyAlgorithm = wKeyAlgorithm;
    }

    /**
	 * Sets the encryption key algorithm size.
	 *
	 * @param wKeyAlgorithmSize Size of the encryption key algorithm
	 */
    public void setKeyAlgorithmSize(String wKeyAlgorithmSize) {
        keyAlgorithmSize = wKeyAlgorithmSize;
    }

    /**
	 * Sets the key cipher algorithm.
	 *
	 * @param wKeyCipherAlgorithm key cipher algorithm
	 */
    public void setKeyCipherAlgorithm(String wKeyCipherAlgorithm) {
        keyCipherAlgorithm = wKeyCipherAlgorithm;
    }

    /**
	 * Sets the key file to store the generated key.
	 *
	 * @param wKeyFile File to store the generated key
	 */
    public void setKeyFile(String wKeyFile) {
        keyFile = wKeyFile;
    }

    /**
	 * Sets the key file password.
	 *
	 * @param wKeyFilePassword Key file password
	 */
    public void setKeyFilePassword(String wKeyFilePassword) {
        keyFilePassword = wKeyFilePassword;
    }

    /**
	 * The entered encryption ID.
	 *
	 * @param wEncryptionId The encryption Id to set
	 */
    public void setEncryptionId(String wEncryptionId) {
        encryptionId = wEncryptionId;
    }

    /**
	 * Returns the resource to encrypt.
	 *
	 * @return The resource to encrypt
	 */
    public String getResource() {
        return resource;
    }

    /**
	 * Returns the filepath to encrypt.
	 *
	 * @return The filepath to encrypt
	 */
    public String getFilePath() {
        return filePath;
    }

    /**
	 * Returns the XPath selection to encrypt.
	 *
	 * @return The XPath to encrypt
	 */
    public String getXpath() {
        return xpath;
    }

    /**
	 * Returns the Basic Security Profil selection.
	 *
	 * @return True or false
	 */
    public boolean getBsp() {
        return bsp;
    }

    /**
	 * Returns the part to encrypt.
	 *
	 * @return True or false
	 */
    public boolean getContent() {
        return content;
    }

    /**
	 * Returns the encryption algorithm.
	 *
	 * @return The selected encryption algorithm
	 */
    public String getEncryptionAlgorithm() {
        return encryptionAlgorithm;
    }

    /**
	 * Returns the encryption key algorithm.
	 *
	 * @return The encryption key algorithm
	 */
    public String getKeyAlgorithm() {
        return keyAlgorithm;
    }

    /**
	 * Returns the encryption key algorithm size.
	 *
	 * @return Size of the encryption key algorithm
	 */
    public String getKeyAlgorithmSize() {
        return keyAlgorithmSize;
    }

    /**
	 * Returns the key cipher algorithm.
	 *
	 * @return The selected key cipher algorithm
	 */
    public String getKeyCipherAlgorithm() {
        return keyCipherAlgorithm;
    }

    /**
	 * Returns the key file with the generated key.
	 *
	 * @return The file with the generated key
	 */
    public String getKeyFile() {
        return keyFile;
    }

    /**
	 * Returns the key file password.
	 *
	 * @return Key file password
	 */
    public String getFilePassword() {
        return keyFilePassword;
    }

    /**
	 * Returns the encryption ID.
	 *
	 * @return The encryption ID
	 */
    public String getEncryptionId() {
        return encryptionId;
    }
}

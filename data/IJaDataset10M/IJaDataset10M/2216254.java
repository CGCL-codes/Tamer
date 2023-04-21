package org.jcryptool.core.wizards.symmetric;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.operations.dataobject.modern.IModernDataObject;
import org.jcryptool.core.operations.dataobject.modern.hybrid.HybridDataObject;
import org.jcryptool.core.operations.dataobject.modern.symmetric.SymmetricDataObject;
import org.jcryptool.core.operations.providers.ProvidersManager;
import org.jcryptool.core.operations.util.ByteArrayUtils;
import org.jcryptool.core.operations.wizards.IAlgorithmWizard;
import org.jcryptool.core.wizards.WizardsPlugin;

/**
 * Enables the user to enter all the necessary details for a symmetric block cipher.
 * 
 * 
 * @author t-kern
 *
 */
public class SymmetricKeyWizardPage extends WizardPage implements Listener {

    /** The log4j logger */
    private static final Logger logger = WizardsPlugin.getLogManager().getLogger(SymmetricKeyWizardPage.class.getName());

    private static final String PAGE_NAME = "Symmetric Page";

    /** The symmetric data object */
    private SymmetricDataObject symmetricDataObject;

    /** The hybrid data object */
    private HybridDataObject hybridDataObject;

    /** Used to override a listener, since the change is performed on purpose! */
    private boolean PURPOSE_FLAG = false;

    /** The available key lengths */
    private int[] keyLengths;

    /** The available iv lengths */
    private int[] blockLengths;

    /** Contains all hex digits for convenience */
    private ArrayList<String> hexValues = new ArrayList<String>(16);

    private Group ivGroup = null;

    private Group opGroup = null;

    private Group cipherModeGroup = null;

    private Group paddingGroup = null;

    private Button encryptRadioButton = null;

    private Button decryptRadioButton = null;

    private Label cipherModeLabel = null;

    private CCombo cipherModeCombo = null;

    private Label paddingLabel = null;

    private CCombo paddingCombo = null;

    private Label ivDescriptionLabel = null;

    private Text ivText = null;

    private Button generateIVButton = null;

    private Group keyGroup = null;

    private Label keyDescriptionLabel = null;

    private Text keyText = null;

    private Button generateKeyButton = null;

    private Label keyLengthLabel = null;

    private CCombo keyLengthCombo = null;

    private Label bitsLabel = null;

    private Label remainingKeyLabel = null;

    private Label blockLengthLabel;

    private CCombo blockLengthCombo;

    private Label blockLengthBitsLabel;

    private Label remainingIVLabel;

    /** The default key length in bits */
    private final int defaultKeyLength = 128;

    /** The default block length in bits */
    private final int defaultBlockLength = 128;

    /** The available cipher modes */
    private String[] cipherModes;

    /** The available paddings */
    private String[] paddings;

    /** Indicates whether this page is used in a hybrid context */
    private boolean isHybrid = false;

    /**
	 * Creates a new instance of SymmetricKeyWizardPage.
	 * 
	 * @param wizard	This page's father wizard
	 */
    public SymmetricKeyWizardPage(IAlgorithmWizard wizard, boolean isHybrid) {
        super(PAGE_NAME, wizard.getAlgorithmName(), null);
        setPageComplete(false);
        setDescription("Enter the details for the symmetric cipher");
        this.isHybrid = isHybrid;
        this.keyLengths = ((IAlgorithmWizard) wizard).getKeyLengths();
        this.blockLengths = ((IAlgorithmWizard) wizard).getBlockLengths();
        if (this.isHybrid) {
            this.hybridDataObject = (HybridDataObject) ((IAlgorithmWizard) wizard).getDataObject();
            cipherModes = ProvidersManager.getInstance().getCipherModes("Cipher", hybridDataObject.getSymmetricAlgorithmName());
            paddings = ProvidersManager.getInstance().getPaddings("Cipher", hybridDataObject.getSymmetricAlgorithmName());
            cipherModes = possibleCipherModes(cipherModes);
            if (supportsPKCS5Padding(paddings)) {
                paddings = new String[1];
                paddings[0] = HYBRID_PADDING_MODE;
            } else {
                setErrorMessage("The selected Crypto Provider does not supported the required PKCS#5 padding scheme!");
            }
        } else {
            this.symmetricDataObject = (SymmetricDataObject) ((IAlgorithmWizard) wizard).getDataObject();
            cipherModes = ProvidersManager.getInstance().getCipherModes("Cipher", symmetricDataObject.getAlgorithmName());
            paddings = ProvidersManager.getInstance().getPaddings("Cipher", symmetricDataObject.getAlgorithmName());
        }
        setUpHexValues();
    }

    /**
	 * Checks whether the padding schemes supported by the current crypto provider contain
	 * the PKCS#5 padding scheme. 
	 * 
	 * @param supportedByProvider	The padding schemes supported by current crypto provider
	 * @return						<code>true</code>, if the crypto provider supports PKCS5Padding; <code>false</code> otherwise.
	 */
    private boolean supportsPKCS5Padding(String[] supportedByProvider) {
        for (int i = 0; i < supportedByProvider.length; i++) {
            if (HYBRID_PADDING_MODE.toLowerCase().equals(supportedByProvider[i].toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Compares the cipher modes supported by the current crypto provider to the list
	 * of the possible cipher modes.<br>
	 * Returns a list of the cipher modes which are in both lists.
	 * 
	 * @param supportedByProvider	The cipher modes supported by the current crypto provider
	 * @return						A list of the cipher modes which are in both lists
	 */
    private String[] possibleCipherModes(String[] supportedByProvider) {
        ArrayList<String> tmp = new ArrayList<String>();
        for (int i = 0; i < supportedByProvider.length; i++) {
            for (int j = 0; j < HYBRID_CIPHER_MODES.length; j++) {
                if (supportedByProvider[i].toLowerCase().equals(HYBRID_CIPHER_MODES[j].toLowerCase())) {
                    tmp.add(HYBRID_CIPHER_MODES[j]);
                }
            }
        }
        String[] result = new String[tmp.size()];
        int counter = 0;
        Iterator<String> it = tmp.iterator();
        while (it.hasNext()) {
            result[counter] = it.next();
            counter++;
        }
        return result;
    }

    /** Contains the 4 basic cipher modes supported by hybrid operations. Restriction to these modes is necessary due to information limits of the PKCS#7 container */
    private final String[] HYBRID_CIPHER_MODES = { "ECB", "CBC", "CFB", "OFB" };

    /** Since the PKCS#7 container requires a specific padding, we have to take this option away from the user */
    private final String HYBRID_PADDING_MODE = "PKCS5Padding";

    /**
	 * Sets this page's mode of operation
	 * 
	 * @param encrypt	<code>true</code>, if this page is supposed to be set up for an encryption
	 */
    public void setOperationsMode(boolean encrypt) {
        encryptRadioButton.setSelection(encrypt);
        decryptRadioButton.setSelection(!encrypt);
        encryptRadioButton.setEnabled(false);
        decryptRadioButton.setEnabled(false);
        opGroup.setEnabled(false);
    }

    /**
	 * Updates the remaining key digits label.
	 */
    private void updateRemainingKeyDigits() {
        int total = Integer.valueOf(keyLengthCombo.getText()) / 4;
        int enteredLength = keyText.getText().length();
        remainingKeyLabel.setText("(" + (total - enteredLength) + " hex-digits remaining)");
    }

    /**
	 * Updates the remaining IV digits label.
	 */
    private void updateRemainingIVDigits() {
        int total = Integer.valueOf(blockLengthCombo.getText()) / 4;
        int enteredLength = ivText.getText().length();
        remainingIVLabel.setText("(" + (total - enteredLength) + " hex-digits remaining)");
    }

    /**
	 * Sets a random value for the key.
	 */
    private void setRandomKeyValue() {
        PURPOSE_FLAG = true;
        keyText.setText(ByteArrayUtils.toHexString(generateRandomValue(Integer.valueOf(keyLengthCombo.getText()))).toUpperCase());
        PURPOSE_FLAG = false;
    }

    /**
	 * Sets a random value for the IV.
	 */
    private void setRandomIVValue() {
        PURPOSE_FLAG = true;
        ivText.setText(ByteArrayUtils.toHexString(generateRandomValue(Integer.valueOf(blockLengthCombo.getText()))).toUpperCase());
        PURPOSE_FLAG = false;
    }

    /**
	 * Generates a random value of the given bit length.<br>
	 * Uses the standard SHA1PRNG implementation of SUN's crypto provider.
	 * 
	 * @param bitLength	The bit length of the random value
	 * @return			The random value as a byte array
	 */
    private byte[] generateRandomValue(int bitLength) {
        byte[] result = new byte[bitLength / 8];
        SecureRandom sr;
        try {
            sr = SecureRandom.getInstance("SHA1PRNG");
            sr.nextBytes(result);
            return result;
        } catch (NoSuchAlgorithmException e) {
            logger.error("Exception while computing a random value", e);
        }
        return result;
    }

    /**
	 * Sets up an arraylist with hex values for convenience.
	 */
    private void setUpHexValues() {
        hexValues.add(0, "0");
        hexValues.add(1, "1");
        hexValues.add(2, "2");
        hexValues.add(3, "3");
        hexValues.add(4, "4");
        hexValues.add(5, "5");
        hexValues.add(6, "6");
        hexValues.add(7, "7");
        hexValues.add(8, "8");
        hexValues.add(9, "9");
        hexValues.add(10, "A");
        hexValues.add(11, "B");
        hexValues.add(12, "C");
        hexValues.add(13, "D");
        hexValues.add(14, "E");
        hexValues.add(15, "F");
    }

    /**
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
    public void createControl(Composite parent) {
        Composite pageComposite = new Composite(parent, SWT.NULL);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.makeColumnsEqualWidth = true;
        pageComposite.setLayout(gridLayout);
        GridData gridData = new GridData();
        gridData.grabExcessVerticalSpace = true;
        gridData.grabExcessHorizontalSpace = true;
        pageComposite.setLayoutData(gridData);
        createOpGroup(pageComposite);
        createKeyGroup(pageComposite);
        createIvGroup(pageComposite);
        createCipherModeGroup(pageComposite);
        createPaddingGroup(pageComposite);
        setControl(pageComposite);
        initForm();
    }

    /**
	 * Initializes the form.
	 */
    private void initForm() {
        registerListeners();
        fillKeyLengthCombo();
        fillBlockLenghtCombo();
        fillCipherModeCombo();
        fillPaddingCombo();
        updateRemainingKeyDigits();
        updateRemainingIVDigits();
        encryptRadioButton.setSelection(true);
    }

    /**
	 * Registers all listeners.
	 */
    private void registerListeners() {
        encryptRadioButton.addListener(SWT.Selection, this);
        decryptRadioButton.addListener(SWT.Selection, this);
        generateKeyButton.addListener(SWT.Selection, this);
        generateIVButton.addListener(SWT.Selection, this);
        keyLengthCombo.addListener(SWT.Selection, this);
        cipherModeCombo.addListener(SWT.Selection, this);
        paddingCombo.addListener(SWT.Selection, this);
        keyText.addListener(SWT.Modify, this);
        ivText.addListener(SWT.Modify, this);
    }

    /**
	 * Sets the available key lengths.
	 */
    private void fillKeyLengthCombo() {
        Arrays.sort(keyLengths);
        for (int i = 0; i < keyLengths.length; i++) {
            keyLengthCombo.add(String.valueOf(keyLengths[i]));
        }
        if (Arrays.binarySearch(keyLengths, defaultKeyLength) != -1) {
            keyLengthCombo.setText(String.valueOf(defaultKeyLength));
        } else {
            keyLengthCombo.setText(String.valueOf(keyLengths[0]));
        }
    }

    /**
	 * Sets the available block lengths. 
	 */
    private void fillBlockLenghtCombo() {
        Arrays.sort(blockLengths);
        for (int i = 0; i < blockLengths.length; i++) {
            blockLengthCombo.add(String.valueOf(blockLengths[i]));
        }
        if (Arrays.binarySearch(blockLengths, defaultBlockLength) != -1) {
            blockLengthCombo.setText(String.valueOf(defaultBlockLength));
        } else {
            blockLengthCombo.setText(String.valueOf(blockLengths[0]));
        }
    }

    /**
	 * Sets the available cipher modes.
	 */
    private void fillCipherModeCombo() {
        for (int i = 0; i < cipherModes.length; i++) {
            cipherModeCombo.add(cipherModes[i]);
        }
        cipherModeCombo.setText(cipherModes[0]);
    }

    /**
	 * Sets the available padding schemes.
	 */
    private void fillPaddingCombo() {
        for (int i = 0; i < paddings.length; i++) {
            paddingCombo.add(paddings[i]);
        }
        paddingCombo.setText(paddings[0]);
    }

    /**
	 * Returns the symmetric data object.
	 * 
	 * @return	The symmetric data object 
	 */
    public SymmetricDataObject getSymmetricDataObject() {
        return symmetricDataObject;
    }

    /**
	 * Returns the hybrid data object.
	 * 
	 * @return	The hybrid data object 
	 */
    public HybridDataObject getHybridDataObject() {
        return hybridDataObject;
    }

    /**
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
    public void handleEvent(Event event) {
        if (event.widget.equals(encryptRadioButton)) {
            logger.debug("encrypt");
        } else if (event.widget.equals(decryptRadioButton)) {
            logger.debug("decrypt");
        } else if (event.widget.equals(generateKeyButton)) {
            logger.debug("key button");
            setRandomKeyValue();
            updateRemainingKeyDigits();
        } else if (event.widget.equals(generateIVButton)) {
            logger.debug("iv button");
            setRandomIVValue();
            updateRemainingIVDigits();
        } else if (event.widget.equals(keyLengthCombo)) {
            logger.debug("keylength");
            keyLengthSelectionEvent();
            updateRemainingKeyDigits();
        } else if (event.widget.equals(blockLengthCombo)) {
            logger.debug("blocklength");
            blockLengthSelectionEvent();
            updateRemainingIVDigits();
        } else if (event.widget.equals(cipherModeCombo)) {
            logger.debug("ciphermode");
        } else if (event.widget.equals(paddingCombo)) {
            logger.debug("padding");
        } else if (event.widget.equals(keyText)) {
            logger.debug("keytext");
            updateRemainingKeyDigits();
        } else if (event.widget.equals(ivText)) {
            logger.debug("ivtext");
            updateRemainingIVDigits();
        }
        setPageComplete(getComplete());
    }

    /**
	 * Discards the superflous bits when a shorter key length is selected in the ComboBox.
	 */
    private void keyLengthSelectionEvent() {
        String key = keyText.getText();
        if (key.length() > Integer.valueOf(keyLengthCombo.getText()) / 4) {
            String shorted = key.substring(0, (Integer.valueOf(keyLengthCombo.getText()) / 4));
            PURPOSE_FLAG = true;
            keyText.setText(shorted);
            PURPOSE_FLAG = false;
        }
    }

    /**
	 * Discards the superflous bits when a shorter block length is selected in the ComboBox.
	 */
    private void blockLengthSelectionEvent() {
        String iv = ivText.getText();
        if (iv.length() > Integer.valueOf(blockLengthCombo.getText()) / 4) {
            String shorted = iv.substring(0, (Integer.valueOf(blockLengthCombo.getText()) / 4));
            PURPOSE_FLAG = true;
            keyText.setText(shorted);
            PURPOSE_FLAG = false;
        }
    }

    /**
	 * Returns the opmode. 
	 * 
	 * @return <code>0</code>, if an encryption is to be performed, <code>1</code> if a decryption is to be performed
	 */
    private int getOpmode() {
        if (encryptRadioButton.getSelection()) return IModernDataObject.ENCRYPT_MODE;
        if (decryptRadioButton.getSelection()) return IModernDataObject.DECRYPT_MODE; else return -1;
    }

    /**
	 * The "page complete" logic.<br>
	 * Checks if all the requisites are fulfilled and returns the complete status of this page.
	 * 
	 * @return	<code>true</code>, if all the properties for a symmetric cipher are fullfilled
	 */
    private boolean getComplete() {
        int keylength = Integer.valueOf(keyLengthCombo.getText()) / 4;
        String key = keyText.getText();
        String iv = ivText.getText();
        int ivlength = Integer.valueOf(blockLengthCombo.getText()) / 4;
        int opmode = getOpmode();
        String cipherMode = cipherModeCombo.getText();
        String padding = paddingCombo.getText();
        if ((key.length() == keylength && iv.length() == ivlength) && (opmode == IModernDataObject.ENCRYPT_MODE || opmode == IModernDataObject.DECRYPT_MODE) && (cipherMode != null && padding != null)) {
            if (isHybrid) {
                hybridDataObject.setSymmetricKey(ByteArrayUtils.fromHexString(key));
                hybridDataObject.setIV(ByteArrayUtils.fromHexString(iv));
                hybridDataObject.setCipherMode(cipherMode);
                hybridDataObject.setPaddingName(padding);
            } else {
                symmetricDataObject.setSymmetricKey(ByteArrayUtils.fromHexString(key));
                symmetricDataObject.setIV(ByteArrayUtils.fromHexString(iv));
                symmetricDataObject.setOpmode(opmode);
                symmetricDataObject.setCipherMode(cipherMode);
                symmetricDataObject.setPaddingName(padding);
            }
            return true;
        }
        return false;
    }

    /**
	 * This method initializes keyGroup	
	 *
	 */
    private void createKeyGroup(Composite parent) {
        GridData generateKeyButtonGridData = new GridData();
        generateKeyButtonGridData.horizontalSpan = 2;
        GridData remainingKeyLabelGridData = new GridData();
        remainingKeyLabelGridData.grabExcessVerticalSpace = true;
        remainingKeyLabelGridData.horizontalSpan = 5;
        GridData keyDescriptionLabelGridData = new GridData();
        keyDescriptionLabelGridData.horizontalSpan = 5;
        keyDescriptionLabelGridData.grabExcessVerticalSpace = true;
        GridData keyLengthLabelGridData = new GridData();
        keyLengthLabelGridData.horizontalAlignment = GridData.BEGINNING;
        keyLengthLabelGridData.grabExcessVerticalSpace = true;
        keyLengthLabelGridData.verticalAlignment = GridData.CENTER;
        GridData keyTextGridData = new GridData();
        keyTextGridData.horizontalSpan = 3;
        keyTextGridData.verticalAlignment = GridData.CENTER;
        keyTextGridData.grabExcessHorizontalSpace = true;
        keyTextGridData.horizontalAlignment = GridData.FILL;
        GridLayout gridLayout1 = new GridLayout();
        gridLayout1.numColumns = 5;
        GridData gridData1 = new GridData();
        gridData1.grabExcessHorizontalSpace = true;
        gridData1.horizontalAlignment = GridData.FILL;
        gridData1.verticalAlignment = GridData.FILL;
        gridData1.horizontalSpan = 3;
        gridData1.grabExcessVerticalSpace = true;
        keyGroup = new Group(parent, SWT.NONE);
        keyGroup.setText("Key");
        keyGroup.setLayout(gridLayout1);
        keyGroup.setLayoutData(gridData1);
        keyDescriptionLabel = new Label(keyGroup, SWT.NONE);
        keyDescriptionLabel.setText("Enter the key with hexadecimal digits (0-9, A-F).");
        keyDescriptionLabel.setLayoutData(keyDescriptionLabelGridData);
        keyLengthLabel = new Label(keyGroup, SWT.NONE);
        keyLengthLabel.setText("Key length:");
        keyLengthLabel.setLayoutData(keyLengthLabelGridData);
        keyLengthCombo = new CCombo(keyGroup, SWT.NONE | SWT.BORDER | SWT.READ_ONLY);
        bitsLabel = new Label(keyGroup, SWT.NONE);
        bitsLabel.setText("bits.");
        @SuppressWarnings("unused") Label filler11 = new Label(keyGroup, SWT.NONE);
        keyText = new Text(keyGroup, SWT.BORDER | SWT.SINGLE);
        keyText.setLayoutData(keyTextGridData);
        keyText.addVerifyListener(new VerifyListener() {

            public void verifyText(VerifyEvent e) {
                if (!PURPOSE_FLAG) {
                    setErrorMessage(null);
                    if (e.character != SWT.BS && e.character != SWT.DEL) {
                        String origInput = new String(keyText.getText());
                        e.text = e.text.toUpperCase();
                        if (!hexValues.contains(e.text)) {
                            setErrorMessage("You can only enter hexadecimal digits (0-9, A-F)");
                            e.doit = false;
                        } else if ((origInput.length() + 1) > Integer.valueOf(keyLengthCombo.getText()) / 4) {
                            setErrorMessage("You cannot enter more digits.");
                            e.doit = false;
                        }
                    }
                }
            }
        });
        generateKeyButton = new Button(keyGroup, SWT.NONE);
        generateKeyButton.setText("Random");
        generateKeyButton.setLayoutData(generateKeyButtonGridData);
        remainingKeyLabel = new Label(keyGroup, SWT.NONE);
        remainingKeyLabel.setText("(X hex-digits remaining)");
        remainingKeyLabel.setLayoutData(remainingKeyLabelGridData);
    }

    /**
	 * This method initializes ivGroup	
	 *
	 */
    private void createIvGroup(Composite parent) {
        GridData blockLengthBitsLabelGridData = new GridData();
        blockLengthBitsLabelGridData.grabExcessVerticalSpace = true;
        blockLengthBitsLabelGridData.grabExcessHorizontalSpace = false;
        GridData blockLengthComboGridData = new GridData();
        blockLengthComboGridData.grabExcessVerticalSpace = true;
        blockLengthComboGridData.grabExcessHorizontalSpace = false;
        GridData blockLengthLabelGridData = new GridData();
        blockLengthLabelGridData.grabExcessVerticalSpace = true;
        blockLengthLabelGridData.grabExcessHorizontalSpace = false;
        GridData generateIVButtonGridData = new GridData();
        generateIVButtonGridData.grabExcessVerticalSpace = true;
        generateIVButtonGridData.grabExcessHorizontalSpace = false;
        GridData remainingIVLabelGridData = new GridData();
        remainingIVLabelGridData.horizontalSpan = 5;
        remainingIVLabelGridData.grabExcessHorizontalSpace = false;
        remainingIVLabelGridData.grabExcessVerticalSpace = true;
        GridData ivTextGridData = new GridData();
        ivTextGridData.horizontalSpan = 4;
        ivTextGridData.horizontalAlignment = GridData.FILL;
        ivTextGridData.verticalAlignment = GridData.CENTER;
        ivTextGridData.grabExcessHorizontalSpace = true;
        GridData ivDescriptionLabelGridData = new GridData();
        ivDescriptionLabelGridData.horizontalSpan = 5;
        ivDescriptionLabelGridData.grabExcessVerticalSpace = true;
        ivDescriptionLabelGridData.grabExcessHorizontalSpace = false;
        GridLayout ivGroupGridLayout = new GridLayout();
        ivGroupGridLayout.numColumns = 5;
        GridData gridData = new GridData();
        gridData.horizontalSpan = 3;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;
        gridData.grabExcessVerticalSpace = true;
        gridData.grabExcessHorizontalSpace = true;
        ivGroup = new Group(parent, SWT.NONE);
        ivGroup.setLayoutData(gridData);
        ivGroup.setLayout(ivGroupGridLayout);
        ivGroup.setText("Initialization Vector (IV) and Block Size");
        ivDescriptionLabel = new Label(ivGroup, SWT.NONE);
        ivDescriptionLabel.setText("Enter the IV with hexadecimal digits (0-9, A-F).");
        ivDescriptionLabel.setLayoutData(ivDescriptionLabelGridData);
        blockLengthLabel = new Label(ivGroup, SWT.NONE);
        blockLengthLabel.setText("Block length:");
        blockLengthLabel.setLayoutData(blockLengthLabelGridData);
        blockLengthCombo = new CCombo(ivGroup, SWT.NONE | SWT.BORDER | SWT.READ_ONLY);
        blockLengthCombo.setLayoutData(blockLengthComboGridData);
        blockLengthBitsLabel = new Label(ivGroup, SWT.NONE);
        blockLengthBitsLabel.setText("bits.");
        blockLengthBitsLabel.setLayoutData(blockLengthBitsLabelGridData);
        @SuppressWarnings("unused") Label filler2 = new Label(ivGroup, SWT.NONE);
        @SuppressWarnings("unused") Label filler1 = new Label(ivGroup, SWT.NONE);
        ivText = new Text(ivGroup, SWT.BORDER);
        ivText.setLayoutData(ivTextGridData);
        ivText.addVerifyListener(new VerifyListener() {

            public void verifyText(VerifyEvent e) {
                if (!PURPOSE_FLAG) {
                    setErrorMessage(null);
                    if (e.character != SWT.BS && e.character != SWT.DEL) {
                        String origInput = new String(ivText.getText());
                        e.text = e.text.toUpperCase();
                        if (!hexValues.contains(e.text)) {
                            setErrorMessage("You can only enter hexadecimal digits (0-9, A-F)");
                            e.doit = false;
                        } else if ((origInput.length() + 1) > Integer.valueOf(blockLengthCombo.getText()) / 4) {
                            setErrorMessage("You cannot enter more digits.");
                            e.doit = false;
                        }
                    }
                }
            }
        });
        generateIVButton = new Button(ivGroup, SWT.NONE);
        generateIVButton.setText("Random");
        generateIVButton.setLayoutData(generateIVButtonGridData);
        remainingIVLabel = new Label(ivGroup, SWT.NONE);
        remainingIVLabel.setText("(X hex-digits remaining)");
        remainingIVLabel.setLayoutData(remainingIVLabelGridData);
    }

    /**
	 * This method initializes cipherModeGroup	
	 *
	 */
    private void createCipherModeGroup(Composite parent) {
        GridData gridData16 = new GridData();
        gridData16.grabExcessVerticalSpace = true;
        gridData16.grabExcessHorizontalSpace = true;
        GridData gridData13 = new GridData();
        gridData13.grabExcessHorizontalSpace = true;
        gridData13.horizontalAlignment = GridData.FILL;
        gridData13.verticalAlignment = GridData.CENTER;
        gridData13.grabExcessVerticalSpace = true;
        GridData gridData3 = new GridData();
        gridData3.horizontalAlignment = GridData.FILL;
        gridData3.grabExcessHorizontalSpace = true;
        gridData3.grabExcessVerticalSpace = true;
        gridData3.verticalAlignment = GridData.FILL;
        cipherModeGroup = new Group(parent, SWT.NONE);
        cipherModeGroup.setLayout(new GridLayout());
        cipherModeGroup.setLayoutData(gridData3);
        cipherModeGroup.setText("Cipher Mode");
        cipherModeLabel = new Label(cipherModeGroup, SWT.NONE);
        cipherModeLabel.setText("Select a cipher mode");
        cipherModeLabel.setLayoutData(gridData16);
        cipherModeCombo = new CCombo(cipherModeGroup, SWT.NONE | SWT.BORDER | SWT.READ_ONLY);
        cipherModeCombo.setLayoutData(gridData13);
    }

    /**
	 * This method initializes paddingGroup	
	 *
	 */
    private void createPaddingGroup(Composite parent) {
        GridData gridData15 = new GridData();
        gridData15.grabExcessVerticalSpace = true;
        gridData15.grabExcessHorizontalSpace = true;
        GridData gridData14 = new GridData();
        gridData14.horizontalAlignment = GridData.FILL;
        gridData14.grabExcessHorizontalSpace = true;
        gridData14.grabExcessVerticalSpace = true;
        gridData14.verticalAlignment = GridData.CENTER;
        GridData gridData2 = new GridData();
        gridData2.horizontalAlignment = GridData.FILL;
        gridData2.grabExcessHorizontalSpace = true;
        gridData2.grabExcessVerticalSpace = true;
        gridData2.verticalAlignment = GridData.FILL;
        paddingGroup = new Group(parent, SWT.NONE);
        paddingGroup.setLayout(new GridLayout());
        paddingGroup.setLayoutData(gridData2);
        paddingGroup.setText("Padding");
        paddingLabel = new Label(paddingGroup, SWT.NONE);
        paddingLabel.setText("Select a padding");
        paddingLabel.setLayoutData(gridData15);
        paddingCombo = new CCombo(paddingGroup, SWT.NONE | SWT.BORDER | SWT.READ_ONLY);
        paddingCombo.setEditable(false);
        paddingCombo.setLayoutData(gridData14);
    }

    /**
	 * This method initializes opGroup	
	 *
	 */
    private void createOpGroup(Composite parent) {
        GridData gridData6 = new GridData();
        gridData6.grabExcessHorizontalSpace = true;
        gridData6.horizontalAlignment = GridData.FILL;
        gridData6.verticalAlignment = GridData.FILL;
        gridData6.grabExcessVerticalSpace = true;
        GridData gridData5 = new GridData();
        gridData5.grabExcessHorizontalSpace = true;
        gridData5.horizontalAlignment = GridData.FILL;
        gridData5.verticalAlignment = GridData.FILL;
        gridData5.grabExcessVerticalSpace = true;
        GridLayout gridLayout3 = new GridLayout();
        gridLayout3.numColumns = 2;
        gridLayout3.makeColumnsEqualWidth = true;
        GridData gridData4 = new GridData();
        gridData4.horizontalSpan = 2;
        gridData4.verticalAlignment = GridData.FILL;
        gridData4.grabExcessHorizontalSpace = true;
        gridData4.grabExcessVerticalSpace = true;
        gridData4.horizontalAlignment = GridData.FILL;
        opGroup = new Group(parent, SWT.NONE);
        opGroup.setText("Operation");
        opGroup.setLayout(gridLayout3);
        opGroup.setLayoutData(gridData4);
        encryptRadioButton = new Button(opGroup, SWT.RADIO);
        encryptRadioButton.setText("Encrypt");
        encryptRadioButton.setLayoutData(gridData5);
        decryptRadioButton = new Button(opGroup, SWT.RADIO);
        decryptRadioButton.setText("Decrypt");
        decryptRadioButton.setLayoutData(gridData6);
    }
}

package org.jcryptool.crypto.flexiprovider.engines.cipher;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.cert.Certificate;
import org.apache.log4j.Logger;
import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.ui.PartInitException;
import org.jcryptool.core.operations.editors.EditorsManager;
import org.jcryptool.core.operations.util.PathEditorInput;
import org.jcryptool.crypto.flexiprovider.descriptors.IFlexiProviderOperation;
import org.jcryptool.crypto.flexiprovider.engines.FlexiProviderEngine;
import org.jcryptool.crypto.flexiprovider.engines.FlexiProviderEnginesPlugin;
import org.jcryptool.crypto.flexiprovider.types.OperationType;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import de.flexiprovider.api.AsymmetricBlockCipher;
import de.flexiprovider.api.Registry;
import de.flexiprovider.api.exceptions.BadPaddingException;
import de.flexiprovider.api.exceptions.IllegalBlockSizeException;
import de.flexiprovider.api.exceptions.InvalidAlgorithmParameterException;
import de.flexiprovider.api.exceptions.InvalidKeyException;
import de.flexiprovider.api.exceptions.NoSuchAlgorithmException;
import de.flexiprovider.api.keys.Key;

public class AsymmetricBlockCipherEngine extends FlexiProviderEngine {

    /** The log4j logger */
    private static final Logger logger = FlexiProviderEnginesPlugin.getLogManager().getLogger(AsymmetricBlockCipherEngine.class.getName());

    private AsymmetricBlockCipher cipher;

    public AsymmetricBlockCipherEngine() {
    }

    public void init(IFlexiProviderOperation operation) {
        logger.debug("initializing asymmetric block cipher engine");
        this.operation = operation;
        try {
            Key key = null;
            cipher = Registry.getAsymmetricBlockCipher(operation.getAlgorithmDescriptor().getAlgorithmName());
            if (operation.getOperation().equals(OperationType.ENCRYPT)) {
                Certificate certificate = KeyStoreManager.getInstance().getPublicKey(operation.getKeyStoreAlias());
                key = (Key) certificate.getPublicKey();
                cipher.initEncrypt(key, operation.getAlgorithmDescriptor().getAlgorithmParameterSpec(), FlexiProviderEnginesPlugin.getSecureRandom());
            } else {
                String password = promptPassword();
                key = (Key) KeyStoreManager.getInstance().getPrivateKey(operation.getKeyStoreAlias(), password);
                cipher.initDecrypt(key, operation.getAlgorithmDescriptor().getAlgorithmParameterSpec());
            }
            initialized = true;
        } catch (NoSuchAlgorithmException e) {
            logger.error("NoSuchAlgorithmException while initializing an asymmetric block cipher engine", e);
        } catch (InvalidKeyException e) {
            logger.error("InvalidKeyException while initializing an asymmetric block cipher engine", e);
        } catch (InvalidAlgorithmParameterException e) {
            logger.error("InvalidAlgorithmParameterException while initializing an asymmetric block cipher engine", e);
        } catch (Exception e) {
        }
    }

    public void perform() {
        if (initialized) {
            logger.debug("perfoming block cipher");
            InputStream inputStream = initInput(operation.getInput());
            OutputStream outputStream = initOutput(operation.getOutput());
            byte[] blockSize = new byte[cipher.getBlockSize()];
            byte[] outputBuffer;
            try {
                int length = inputStream.read(blockSize, 0, blockSize.length);
                outputBuffer = cipher.update(blockSize, 0, length);
                outputStream.write(outputBuffer);
                outputBuffer = cipher.doFinal();
                outputStream.write(outputBuffer);
                inputStream.close();
                outputStream.close();
                if (operation.getOutput().equals("<Editor>")) {
                    EditorsManager.getInstance().openNewHexEditor(new PathEditorInput(URIUtil.toPath(getOutputURI())));
                }
            } catch (IOException e) {
                logger.error("IOException while performing an asymmetric block cipher", e);
            } catch (IllegalBlockSizeException e) {
                logger.error("IllegalBlockSizeException while performing an asymmetric block cipher", e);
            } catch (BadPaddingException e) {
                logger.error("BadPaddingException while performing an asymmetric block cipher", e);
            } catch (PartInitException e) {
                logger.error("PartInitException while performing an asymmetric block cipher", e);
            }
        }
    }
}

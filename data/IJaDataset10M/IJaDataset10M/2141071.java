package framework;

import stream.Block;

/**
 * Interface for a special mode of an algorithm
 * 
 * @author arto
 *
 */
public abstract class Mode {

    protected CryptoAlgorithm algorithm;

    /**
	 * Constructor for Mode
	 * Set algorithm
	 * @param algorithm
	 */
    public Mode(CryptoAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    /**
	 * Get description of the mode
	 * @return description
	 */
    public abstract String getDescription();

    /**
	 * Encrypt a block with this mode
	 * @param plainBlock
	 * @return encrypt block
	 * @throws Exception 
	 */
    public abstract Block encryptBlock(Block plainBlock) throws Exception;

    /**
	 * Decrypt a block with this mode
	 * @param cipherBlock
	 * @return decrypt block
	 * @throws Exception 
	 */
    public abstract Block decryptBlock(Block cipherBlock) throws Exception;
}

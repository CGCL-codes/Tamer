package edu.rabbit.schema;

/**
 * Blob literal.
 * 
 * @author Yuanyan<yanyan.cao@gmail.com>
 * 
 */
public interface IBlobLiteral extends ILiteralValue {

    /**
     * <p>
     * Blob value as bytes array.
     * </p>
     * 
     * <p>
     * IMPORTANT: Do not modify the returned array!
     * </p>
     * 
     * @return blob value
     */
    public byte[] getValue();
}

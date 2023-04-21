package jout.structure;

/**
 *
 * @author shumi
 */
public interface ConstantData {

    public CT getType();

    public int getSize_byte();

    public boolean isValid(CPTable cpt);
}

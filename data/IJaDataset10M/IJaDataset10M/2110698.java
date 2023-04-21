package org.mobicents.slee.training.example4.first;

/**
 * The SBB Activity Context Interface 
 * @author amit bhayani
 *
 */
public interface FirstBounceSbbActivityContextInterface extends javax.slee.ActivityContextInterface {

    public boolean getFilteredBySecondBounceSbb();

    public void setFilteredBySecondBounceSbb(boolean val);

    public void setFilteredByMe(boolean val);

    public boolean getFilteredByMe();
}

package org.omg.CosTrading;

/***/
public interface OfferIteratorOperations {

    /***/
    public int max_left() throws UnknownMaxLeft;

    /***/
    public boolean next_n(int n, OfferSeqHolder offers);

    /***/
    public void destroy();
}

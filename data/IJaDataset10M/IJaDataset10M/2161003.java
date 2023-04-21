package com.ojt.balance;

/**
 * @author FMo
 * @since 20 avr. 2009 (FMo) : Cr�ation
 */
public interface BalanceFrame {

    enum TYPE {

        UNKNOWN, CMD, WEIGHT
    }

    TYPE getType();

    float getWeight();

    boolean isStableWeight();
}

package com.sixlegs.bigtwo.client;

import com.sixlegs.bigtwo.data.BigTwoCardComparator;
import com.threerings.parlor.card.data.Card;

class BySuitComparator extends BigTwoCardComparator {

    public BySuitComparator() {
        super(false);
    }

    @Override
    public int compare(Card card1, Card card2) {
        int c = card2.getSuit() - card1.getSuit();
        if (c != 0) return c;
        return super.compare(card1, card2);
    }
}

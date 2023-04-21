package com.sixlegs.bigtwo.client;

import com.sixlegs.bigtwo.client.card.CardImageProvider;
import com.threerings.media.image.BufferedMirage;
import com.threerings.media.image.Mirage;
import com.threerings.parlor.card.data.Card;
import com.threerings.parlor.card.data.CardCodes;
import java.awt.image.BufferedImage;

class SimpleCardImageProvider implements CardImageProvider {

    private BufferedMirage[][] cards;

    private BufferedMirage[][] micro;

    public SimpleCardImageProvider(BufferedImage cardsImage, BufferedImage microImage) {
        cards = splitImage(cardsImage, 4, 14);
        micro = splitImage(microImage, 4, 14);
    }

    private static BufferedMirage[][] splitImage(BufferedImage img, int rows, int columns) {
        int h = img.getHeight() / rows;
        int w = img.getWidth() / columns;
        BufferedMirage[][] array = new BufferedMirage[rows][columns];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                array[y][x] = new BufferedMirage(img.getSubimage(x * w, y * h, w, h));
            }
        }
        return array;
    }

    public Mirage getCardImage(Card card) {
        return getCardImage(cards, card);
    }

    public Mirage getMicroCardImage(Card card) {
        return getCardImage(micro, card);
    }

    public Mirage getCardBackImage() {
        return cards[0][13];
    }

    public Mirage getMicroCardBackImage() {
        return micro[0][13];
    }

    private static Mirage getCardImage(BufferedMirage[][] images, Card card) {
        if (card.isJoker()) {
            return images[(card.getNumber() == CardCodes.RED_JOKER) ? 3 : 2][13];
        } else {
            return images[card.getSuit()][card.getNumber() - 2];
        }
    }
}

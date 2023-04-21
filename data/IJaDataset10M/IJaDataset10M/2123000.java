package net.sourceforge.simplegamenet.manilles;

/**
 * Created by IntelliJ IDEA.
 * User: debackel
 * Date: 30-Jun-2005
 * Time: 12:31:47
 * To change this template use File | Settings | File Templates.
 */
public class ManillesRound {

    private static final int AMOUNT_CARDS = 32;

    private int trump;

    private int roundScore;

    private boolean firstRound;

    private boolean doubleScore;

    private int hitNumber;

    private int hitCardNumber;

    private ManillesHit[] manillesHits;

    private ManillesCard[] allCards;

    public ManillesRound(ManillesCard[] allCards) {
        trump = -1;
        firstRound = true;
        doubleScore = false;
        manillesHits = new ManillesHit[8];
        for (int i = 0; i < manillesHits.length; i++) {
            manillesHits[i] = new ManillesHit();
        }
        hitNumber = 0;
        hitCardNumber = 0;
        this.allCards = allCards;
    }

    public int getHitCardNumber() {
        return hitCardNumber;
    }

    public int getHitNumber() {
        return hitNumber;
    }

    public int getTrump() {
        return trump;
    }

    public void setTrump(int trump) {
        this.trump = trump;
        if (trump == ManillesCardsKind.NONE) {
            doubleScore = true;
        }
    }

    public void setAllCards(ManillesCard[] allCards) {
        this.allCards = allCards;
    }

    public int countRoundScore() {
        for (int i = 0; i < manillesHits.length; i++) {
            roundScore += manillesHits[i].countHitScore();
        }
        return roundScore;
    }

    public void resetRound() {
        trump = -1;
        firstRound = true;
        doubleScore = false;
        for (int i = 0; i < manillesHits.length; i++) {
            manillesHits[i].resetHit();
        }
        hitNumber = 0;
        hitCardNumber = 0;
    }

    public boolean playCard(ManillesCard playedCard) {
        boolean cardOK = false;
        System.out.println("hitNumber : " + hitNumber);
        System.out.println("hitCardNumber : " + hitCardNumber);
        switch(hitCardNumber) {
            case 0:
                for (int i = 0; i < AMOUNT_CARDS; i++) {
                    if (playedCard.getCard()[0] == allCards[i].getCard()[0] && playedCard.getCard()[1] == allCards[i].getCard()[1]) {
                        allCards[i].setCardPlayed(true);
                    }
                }
                manillesHits[hitNumber].setHitCard(hitCardNumber, playedCard);
                cardOK = true;
                hitCardNumber++;
                break;
            case 1:
                if (checkFollowUp(playedCard)) {
                    if (checkIfCardIsHigher(playedCard, manillesHits[hitNumber].getHitCards()[0])) {
                        for (int i = 0; i < AMOUNT_CARDS; i++) {
                            if (playedCard.getCard()[0] == allCards[i].getCard()[0] && playedCard.getCard()[1] == allCards[i].getCard()[1]) {
                                allCards[i].setCardPlayed(true);
                            }
                        }
                        manillesHits[hitNumber].setHitCard(hitCardNumber, playedCard);
                        cardOK = true;
                        hitCardNumber++;
                    }
                }
                break;
            case 2:
                if (checkFollowUp(playedCard)) {
                    if (checkIfCardIsHigher(playedCard, manillesHits[hitNumber].getHitCards()[1]) || (checkIfCardIsHigher(manillesHits[hitNumber].getHitCards()[0], manillesHits[hitNumber].getHitCards()[1]))) {
                        for (int i = 0; i < AMOUNT_CARDS; i++) {
                            if (playedCard.getCard()[0] == allCards[i].getCard()[0] && playedCard.getCard()[1] == allCards[i].getCard()[1]) {
                                allCards[i].setCardPlayed(true);
                            }
                        }
                        manillesHits[hitNumber].setHitCard(hitCardNumber, playedCard);
                        cardOK = true;
                        hitCardNumber++;
                    }
                }
                break;
            case 3:
                if (checkFollowUp(playedCard)) {
                    if ((checkIfCardIsHigher(playedCard, manillesHits[hitNumber].getHitCards()[0]) && checkIfCardIsHigher(playedCard, manillesHits[hitNumber].getHitCards()[2])) || (checkIfCardIsHigher(manillesHits[hitNumber].getHitCards()[1], manillesHits[hitNumber].getHitCards()[0]) && checkIfCardIsHigher(manillesHits[hitNumber].getHitCards()[1], manillesHits[hitNumber].getHitCards()[2]))) {
                        for (int i = 0; i < AMOUNT_CARDS; i++) {
                            if (playedCard.getCard()[0] == allCards[i].getCard()[0] && playedCard.getCard()[1] == allCards[i].getCard()[1]) {
                                allCards[i].setCardPlayed(true);
                            }
                        }
                        manillesHits[hitNumber].setHitCard(hitCardNumber, playedCard);
                        hitCardNumber = 0;
                        hitNumber++;
                        cardOK = true;
                    }
                }
                break;
        }
        return cardOK;
    }

    private boolean checkIfCardIsHigher(ManillesCard playedCard, ManillesCard checkCard) {
        System.out.println("checking (" + playedCard.getCard()[0] + "," + playedCard.getCard()[1] + ") with (" + checkCard.getCard()[0] + "," + checkCard.getCard()[1] + ")");
        if (checkOriginalCardKind(playedCard) && checkOriginalCardKind(checkCard)) {
            System.out.println("alletwee gelijk aan de originele");
            if (playedCard.getCard()[1] > checkCard.getCard()[1]) {
                System.out.println("--> de gespeelde kaart is kleiner dan de gecontroleerde");
                return (!checkHigherOriginalCardKindAvailable(playedCard, checkCard)) ? true : false;
            } else {
                return true;
            }
        } else if (!checkOriginalCardKind(playedCard) && checkOriginalCardKind(checkCard)) {
            System.out.println("eerste niet gelijk aan origineel");
            if (playedCard.getCard()[0] != trump) {
                System.out.println("--> gespeelde kaart is geen troef");
                return (checkTrumpAvailable(playedCard)) ? false : true;
            } else {
                System.out.println("--> gespeelde kaart is troef");
                return true;
            }
        } else if (checkOriginalCardKind(playedCard) && !checkOriginalCardKind(checkCard)) {
            System.out.println("tweede niet gelijk aan origineel");
            return true;
        } else if (!checkOriginalCardKind(playedCard) && !checkOriginalCardKind(checkCard)) {
            System.out.println("geen enkele gelijk aan origineel");
            if (playedCard.getCard()[0] == checkCard.getCard()[0]) {
                System.out.println("--> de gespeelde kaart is van dezelfde soort als de gecontroleerde");
                if (playedCard.getCard()[1] > checkCard.getCard()[1]) {
                    System.out.println("---> de gespeelde kaart is kleiner dan de gecontroleerde");
                    return (!checkHigherCardAvailable(playedCard, checkCard)) ? true : false;
                } else {
                    return true;
                }
            } else {
                System.out.println("--> de gespeelde kaart is niet van dezelfde soort als de gecontroleerde");
                return true;
            }
        } else {
            System.out.println("er klopt iets niet");
            return false;
        }
    }

    private boolean checkFollowUp(ManillesCard playedCard) {
        if (checkOriginalCardKind(playedCard)) {
            System.out.println("gesmeten volgens uitgekomen");
            return true;
        } else if (!checkOriginalCardKindAvailable(playedCard) && !checkOriginalCardKind(playedCard) && playedCard.getCard()[0] != trump && !checkTrumpAvailable(playedCard)) {
            System.out.println("niet gesmeten volgens uitgekomen, maar geen troef");
            return true;
        } else if (checkOriginalCardKindAvailable(playedCard) && !checkOriginalCardKind(playedCard)) {
            System.out.println("nog origineel over maar niet gesmeten");
            return false;
        } else if (!checkOriginalCardKindAvailable(playedCard) && !checkOriginalCardKind(playedCard) && playedCard.getCard()[0] != trump && checkTrumpAvailable(playedCard)) {
            System.out.println("niet gesmeten volgens uitgekomen of troef, maar wel nog troef");
            return false;
        } else if (!checkOriginalCardKindAvailable(playedCard) && !checkOriginalCardKind(playedCard) && playedCard.getCard()[0] == trump) {
            System.out.println("niet gesmeten volgens uitgekomen, troef gesmeten");
            return true;
        }
        return false;
    }

    private boolean checkKind(ManillesCard playedCard, ManillesCard checkCard) {
        if (playedCard.getCard()[0] == checkCard.getCard()[0]) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkValue(ManillesCard playedCard, ManillesCard checkCard) {
        if (playedCard.getCard()[1] < checkCard.getCard()[1]) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkHigherCardAvailable(ManillesCard playedCard, ManillesCard checkCard) {
        for (int i = 0; i < AMOUNT_CARDS; i++) {
            if (playedCard.getCardPlayerID().intValue() == allCards[i].getCardPlayerID().intValue() && playedCard.getCard()[1] < checkCard.getCard()[1] && (playedCard.getCard()[0] != allCards[i].getCard()[0] && playedCard.getCard()[1] != allCards[i].getCard()[1]) && !allCards[i].getCardPlayed() && playedCard.getCard()[0] == allCards[i].getCard()[0]) {
                if (allCards[i].getCard()[1] < playedCard.getCard()[1]) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkHigherOriginalCardKindAvailable(ManillesCard playedCard, ManillesCard checkCard) {
        System.out.println("playedCard.getCardPlayerID() : " + playedCard.getCardPlayerID());
        for (int i = 0; i < AMOUNT_CARDS; i++) {
            if (playedCard.getCardPlayerID().intValue() == allCards[i].getCardPlayerID().intValue() && allCards[i].getCard()[0] == manillesHits[hitNumber].getHitCards()[0].getCard()[0] && (playedCard.getCard()[0] != allCards[i].getCard()[0] && playedCard.getCard()[1] != allCards[i].getCard()[1]) && allCards[i].getCard()[1] < playedCard.getCard()[1] && allCards[i].getCard()[1] < checkCard.getCard()[1] && !allCards[i].getCardPlayed()) {
                System.out.println("allCards[" + i + "].getCardPlayerID() : " + allCards[i].getCardPlayerID());
                System.out.println("%%% er is nog een originele kaart");
                return true;
            }
        }
        System.out.println("%%% er is geen originele kaart meer");
        return false;
    }

    private boolean checkOriginalCardKindAvailable(ManillesCard playedCard) {
        System.out.println("playedCard.getCardPlayerID() : " + playedCard.getCardPlayerID());
        for (int i = 0; i < AMOUNT_CARDS; i++) {
            if (playedCard.getCardPlayerID().intValue() == allCards[i].getCardPlayerID().intValue() && allCards[i].getCard()[0] == manillesHits[hitNumber].getHitCards()[0].getCard()[0] && (playedCard.getCard()[0] != allCards[i].getCard()[0] && playedCard.getCard()[1] != allCards[i].getCard()[1]) && !allCards[i].getCardPlayed()) {
                System.out.println("allCards[" + i + "].getCardPlayerID() : " + allCards[i].getCardPlayerID());
                System.out.println("%%% er is nog een originele kaart");
                return true;
            }
        }
        System.out.println("%%% er is geen originele kaart meer");
        return false;
    }

    private boolean checkCardKindAvailable(ManillesCard playedCard) {
        for (int i = 0; i < AMOUNT_CARDS; i++) {
            if (playedCard.getCardPlayerID().equals(allCards[i].getCardPlayerID()) && !playedCard.equals(allCards[i]) && !allCards[i].getCardPlayed()) {
                if (checkKind(allCards[i], playedCard)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkOriginalCardKind(ManillesCard playedCard) {
        return (playedCard.getCard()[0] == manillesHits[hitNumber].getHitCards()[0].getCard()[0]) ? true : false;
    }

    private boolean checkTrumpAvailable(ManillesCard playedCard) {
        for (int i = 0; i < AMOUNT_CARDS; i++) {
            if (playedCard.getCardPlayerID().intValue() == allCards[i].getCardPlayerID().intValue() && (playedCard.getCard()[0] != allCards[i].getCard()[0] && playedCard.getCard()[1] != allCards[i].getCard()[1]) && !allCards[i].getCardPlayed() && allCards[i].getCard()[0] == trump) {
                System.out.println("%%% er is nog troef over");
                return true;
            }
        }
        System.out.println("%%% er is geen troef meer over");
        return false;
    }

    public Integer countHitWinner() {
        ManillesCard[] hitCards = manillesHits[hitNumber - 1].getHitCards();
        ManillesCard winningCard = hitCards[0];
        for (int i = 1; i < hitCards.length; i++) {
            if (checkKind(winningCard, hitCards[i]) && checkValue(hitCards[i], winningCard)) {
                winningCard = hitCards[i];
            } else if (winningCard.getCard()[0] == trump && hitCards[i].getCard()[0] == trump && checkValue(hitCards[i], winningCard)) {
                winningCard = hitCards[i];
            } else if (hitCards[i].getCard()[0] == trump && winningCard.getCard()[0] != trump) {
                winningCard = hitCards[i];
            }
        }
        return winningCard.getCardPlayerID();
    }
}

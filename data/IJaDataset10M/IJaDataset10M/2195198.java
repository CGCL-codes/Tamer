package de.rauchhaupt.games.poker.holdem.lib.handqualifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import de.rauchhaupt.games.poker.holdem.lib.Hand;
import de.rauchhaupt.games.poker.holdem.lib.RatingEnum;
import de.rauchhaupt.games.poker.holdem.lib.comparators.CardComparator;
import de.volkerraum.pokerbot.model.Card;
import de.volkerraum.pokerbot.model.Card.CARDVALUE;
import de.volkerraum.pokerbot.model.Card.COLOR;

public class HandStatistic {

    public static final String PREFIXKEY_color = "COLOR.";

    public static final String PREFIXKEY_rating = "RATING.";

    public static final String PREFIXKEY_value = "VALUE.";

    public static final String PREFIXKEY_general = "GENERAL.";

    public static final String KEY_statisticOnAnalizedHands = PREFIXKEY_general + "AnalyzedHands";

    public static final String KEY_accuracy = PREFIXKEY_general + "Accuracy";

    public static final String KEY_averageWinChance = PREFIXKEY_general + "AverageWinChance";

    private long statisticOnAnalyzedHands = 0L;

    private double accuracy = 0.0d;

    private double averageWinChance = 0.0d;

    private int amountOfPlayers = 2;

    private int amountOfCards = 2;

    Properties resultProperties = new Properties();

    static DecimalFormat df = new DecimalFormat("0.000");

    HandStatistic(int aAmountOfCards) {
        try {
            amountOfCards = aAmountOfCards;
            amountOfPlayers = 2;
            loadProperties(resultProperties);
            String aValue = resultProperties.getProperty(KEY_statisticOnAnalizedHands);
            if (aValue != null) statisticOnAnalyzedHands = Long.parseLong(aValue);
            aValue = resultProperties.getProperty(KEY_accuracy);
            if (aValue != null) accuracy = encodeDouble(aValue);
            computeAverageWinChance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadProperties(Properties someProperties) throws Exception {
        File inFile = new File(getFileName());
        InputStream tempInputStream = null;
        if (inFile.exists()) tempInputStream = new FileInputStream(inFile); else tempInputStream = this.getClass().getClassLoader().getResourceAsStream("/de/rauchhaupt/games/poker/holdem/lib/statistic/" + getFileName());
        if (tempInputStream != null) {
            someProperties.loadFromXML(tempInputStream);
            tempInputStream.close();
        }
    }

    String encodeDouble(double aDouble) {
        return "" + df.format(aDouble) + "%";
    }

    double encodeDouble(String aString) {
        double returnValue = 0.0d;
        aString = aString.replaceAll("%", "");
        aString = aString.replaceAll(",", ".");
        return Double.parseDouble(aString);
    }

    public void saveToProperties() throws Exception {
        Properties saveResultProperties = new Properties();
        loadProperties(saveResultProperties);
        accuracy = compareStatistics(resultProperties, saveResultProperties);
        computeAverageWinChance();
        resultProperties.put(KEY_statisticOnAnalizedHands, "" + statisticOnAnalyzedHands);
        resultProperties.put(KEY_accuracy, encodeDouble(accuracy));
        resultProperties.put(KEY_averageWinChance, encodeDouble(averageWinChance));
        File parDir = new File("stat");
        if (!parDir.exists()) parDir.mkdir();
        File inFile = new File(getFileName());
        FileOutputStream fout = new FileOutputStream(inFile);
        resultProperties.storeToXML(fout, "Statistic for Texas Holdem with " + amountOfPlayers + " players after " + amountOfCards + " cards. Average win chance is " + df.format(averageWinChance) + ".Accuracy is " + df.format(accuracy * 100.0d) + ".First Number is wins, second is losses, third is winchance. " + statisticOnAnalyzedHands + " Hands were analyzed. Combinations in prop:" + resultProperties.size() + ".");
        fout.flush();
        fout.close();
    }

    public WinLossStatistic computeOverallStatisticForCards(Hand aHand) {
        WinLossStatistic colorWinLossStatistic = getColorWinLossStatisticForCards(aHand);
        WinLossStatistic valueWinLossStatistic = getValueWinLossStatisticForCards(aHand);
        long overallWins = colorWinLossStatistic.getWins() + valueWinLossStatistic.getWins();
        long overallLosses = colorWinLossStatistic.getLosses() + valueWinLossStatistic.getLosses();
        WinLossStatistic tempWinLossStatistic = new WinLossStatistic(aHand.getCardsInOrderGiven().toString(), overallWins + "|" + overallLosses);
        return tempWinLossStatistic;
    }

    public WinLossStatistic getColorWinLossStatisticForCards(Hand aHand) {
        List<Card> orderedCardList = aHand.getCardsInOrderGiven();
        if (orderedCardList.size() != amountOfCards) throw new RuntimeException("This is a statistic for " + amountOfCards + " cards  and not for " + orderedCardList.size() + ".");
        String index = computeColorIndex(orderedCardList);
        String value = resultProperties.getProperty(index);
        WinLossStatistic returnValue = new WinLossStatistic(index, value);
        return returnValue;
    }

    public WinLossStatistic getValueWinLossStatisticForCards(Hand aHand) {
        List<Card> orderedCardList = aHand.getCardsInOrderGiven();
        if (orderedCardList.size() != amountOfCards) throw new RuntimeException("This is a statistic for " + amountOfCards + " cards  and not for " + orderedCardList.size() + ".");
        String index = computeValueIndex(orderedCardList);
        String value = resultProperties.getProperty(index);
        WinLossStatistic returnValue = new WinLossStatistic(index, value);
        return returnValue;
    }

    public WinLossStatistic getRatingWinLossStatisticForCards(Hand aHand) {
        List<Card> orderedCardList = aHand.getCardsInOrderGiven();
        if (orderedCardList.size() != amountOfCards) throw new RuntimeException("This is a statistic for " + amountOfCards + " cards  and not for " + orderedCardList.size() + ".");
        String index = computeRatingIndex(aHand.getRating().getHighestRatingEnum(), aHand.getRating().getRatingCardValues());
        String value = resultProperties.getProperty(index);
        WinLossStatistic returnValue = new WinLossStatistic(index, value);
        return returnValue;
    }

    public WinLossStatistic getReferencialRatingWinLossStatisticForCards(Hand aHand) {
        List<Card> orderedCardList = aHand.getCardsInOrderGiven();
        if (orderedCardList.size() != amountOfCards) throw new RuntimeException("This is a statistic for " + amountOfCards + " cards  and not for " + orderedCardList.size() + ".");
        String index = computeReferencialRatingIndex(aHand.getReferencialRating().getHighestRatingEnum(), aHand.getReferencialRating().getRatingCardValues(), aHand.getRating().getHighestRatingEnum(), aHand.getRating().getRatingCardValues());
        String value = resultProperties.getProperty(index);
        WinLossStatistic returnValue = new WinLossStatistic(index, value);
        return returnValue;
    }

    public String computeReferencialRatingIndex(RatingEnum aRelativeRatingEnum, CARDVALUE[] someRelativeCards, RatingEnum aRatingEnum, CARDVALUE[] someCards) {
        String returnValue = computeRatingIndex(aRelativeRatingEnum, someRelativeCards);
        returnValue = returnValue + "#" + computeRatingIndex(aRelativeRatingEnum, someCards);
        return returnValue;
    }

    public String computeRatingIndex(RatingEnum aRatingEnum, CARDVALUE[] someCards) {
        String returnValue = PREFIXKEY_rating + aRatingEnum.toString().toUpperCase().replace(' ', '_');
        if (aRatingEnum.getValue() == RatingEnum.STRAIGHT_FLUSH.getValue()) returnValue = returnValue + "-" + someCards[0]; else if (aRatingEnum.getValue() == RatingEnum.FOUR_OF_A_KIND.getValue()) returnValue = returnValue + "-" + someCards[0]; else if (aRatingEnum.getValue() == RatingEnum.FULL_HOUSE.getValue()) returnValue = returnValue + "-" + someCards[0] + "," + someCards[1]; else if (aRatingEnum.getValue() == RatingEnum.FLUSH.getValue()) returnValue = returnValue + "-" + someCards[0]; else if (aRatingEnum.getValue() == RatingEnum.STRAIGHT.getValue()) returnValue = returnValue + "-" + someCards[0]; else if (aRatingEnum.getValue() == RatingEnum.THREE_OF_A_KIND.getValue()) returnValue = returnValue + "-" + someCards[0]; else if (aRatingEnum.getValue() == RatingEnum.TWO_PAIRS.getValue()) returnValue = returnValue + "-" + someCards[0] + "," + someCards[1]; else if (aRatingEnum.getValue() == RatingEnum.ONE_PAIR.getValue()) returnValue = returnValue + "-" + someCards[0] + "," + someCards[1]; else if (aRatingEnum.getValue() == RatingEnum.HIGH_CARD.getValue()) returnValue = returnValue + "-" + someCards[0] + "," + someCards[1];
        return returnValue;
    }

    public WinLossStatistic getRatingStatisticForCards(Hand aHand) {
        String index = computeRatingIndex(aHand.getRating().getHighestRatingEnum(), aHand.getRating().getRatingCardValues());
        String value = resultProperties.getProperty(index);
        return new WinLossStatistic(index, value);
    }

    public WinLossStatistic getRelativeHandRatingStatisticForCards(Hand aHand) {
        String index = computeReferencialRatingIndex(aHand.getReferencialRating().getHighestRatingEnum(), aHand.getReferencialRating().getRatingCardValues(), aHand.getRating().getHighestRatingEnum(), aHand.getRating().getRatingCardValues());
        String value = resultProperties.getProperty(index);
        return new WinLossStatistic(index, value);
    }

    public void newHandAnalyzed() {
        statisticOnAnalyzedHands++;
    }

    public void addPlayResult(WinLossStatistic aWinLossStatistic, boolean won) {
        if (won) aWinLossStatistic.newWin(); else aWinLossStatistic.newLoss();
        resultProperties.put(aWinLossStatistic.getIndex(), aWinLossStatistic.toString());
    }

    private String computeColorIndex(List<Card> someCards) {
        List<Integer> colorMatcherList = new ArrayList<Integer>();
        for (int i = 0; i < 4; i++) {
            COLOR curColor = CardComparator.ColorArray[i];
            int curColorMatches = 0;
            for (Card tempCard : someCards) {
                if (curColor.equals(tempCard.getColor())) curColorMatches++;
            }
            colorMatcherList.add(new Integer(curColorMatches));
        }
        Collections.sort(colorMatcherList);
        String returnValue = PREFIXKEY_color;
        for (Integer curInt : colorMatcherList) {
            returnValue = returnValue + "_" + curInt;
        }
        return returnValue;
    }

    private String computeValueIndex(List<Card> someCardsInDealtOrder) {
        String resultString = "";
        List<Card> handList = new ArrayList<Card>();
        List<Card> deckList = new ArrayList<Card>();
        handList.add(someCardsInDealtOrder.get(0));
        handList.add(someCardsInDealtOrder.get(1));
        for (int i = 2; i < someCardsInDealtOrder.size(); i++) {
            deckList.add(someCardsInDealtOrder.get(i));
        }
        Collections.sort(handList, CardComparator.instance);
        Collections.sort(deckList, CardComparator.instance);
        List<Card> someCards = new ArrayList<Card>();
        someCards.addAll(handList);
        someCards.addAll(deckList);
        boolean first = true;
        for (Card curCard : someCards) {
            if (first) first = false; else resultString = resultString + ".";
            resultString = resultString + curCard.getValue();
        }
        return PREFIXKEY_value + resultString;
    }

    public long getStatisticOnAnalyzedHands() {
        return statisticOnAnalyzedHands;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public double getAverageWinChance() {
        return averageWinChance;
    }

    public void computeAverageWinChance() {
        averageWinChance = 0.0d;
        double keys = 0.0d;
        double addedWinChances = 0.0d;
        for (Iterator<Object> iter = resultProperties.keySet().iterator(); iter.hasNext(); ) {
            String curKey = (String) iter.next();
            if (!curKey.startsWith(PREFIXKEY_color)) continue;
            String stat1 = (String) resultProperties.get(curKey);
            if (stat1 == null) continue;
            StringTokenizer strTok1 = new StringTokenizer(stat1, "|%", false);
            long win1 = Long.parseLong(strTok1.nextToken());
            long loss1 = Long.parseLong(strTok1.nextToken());
            String tempString1 = strTok1.nextToken().replace(',', '.');
            double percentage1 = Double.parseDouble(tempString1);
            keys = keys + 1.0;
            addedWinChances = addedWinChances + percentage1;
        }
        averageWinChance = addedWinChances / keys;
    }

    protected String getFileName() {
        return "stat/" + amountOfCards + "_HandStat_" + amountOfPlayers + "_Players.xml";
    }

    private static double compareStatistics(Properties prop1, Properties prop2) {
        double returnValue = 0.0d;
        double maximum = 2.0d;
        if (prop1.size() > 30000) {
            double d1 = prop1.size();
            maximum = d1 / 300.0d;
        }
        int iWhichKey = 0;
        for (Iterator<?> iter = prop1.keySet().iterator(); iter.hasNext(); ) {
            iWhichKey++;
            String curKey = (String) iter.next();
            if (curKey.startsWith(PREFIXKEY_general)) continue;
            String stat1 = prop1.getProperty(curKey);
            String stat2 = prop2.getProperty(curKey);
            if ((stat1 == null) || (stat2 == null)) {
                return 2000.0;
            }
            StringTokenizer strTok1 = new StringTokenizer(stat1, "|%", false);
            StringTokenizer strTok2 = new StringTokenizer(stat2, "|%", false);
            long win1 = Long.parseLong(strTok1.nextToken());
            long win2 = Long.parseLong(strTok2.nextToken());
            long loss1 = Long.parseLong(strTok1.nextToken());
            long loss2 = Long.parseLong(strTok2.nextToken());
            String tempString1 = strTok1.nextToken().replace(',', '.');
            String tempString2 = strTok2.nextToken().replace(',', '.');
            double percentage1 = Double.parseDouble(tempString1) / 100.0d;
            double percentage2 = Double.parseDouble(tempString2) / 100.0d;
            double diff = percentage1 - percentage2;
            returnValue = returnValue + Math.abs(diff);
            if (returnValue > maximum) {
                System.out.print("stop at " + iWhichKey + "->");
                return 1000.0d;
            }
        }
        return returnValue;
    }
}

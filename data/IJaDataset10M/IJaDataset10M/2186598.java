package dominion.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import dominion.model.monies.Copper;
import dominion.model.monies.Gold;
import dominion.model.monies.Silver;
import dominion.model.points.Curse;
import dominion.model.points.Duchy;
import dominion.model.points.Estate;
import dominion.model.points.Province;
import dominion.utils.Constants;

public class GameBoard {

    private Deck coppers;

    private Deck silvers;

    private Deck golds;

    private Deck curses;

    private Deck estates;

    private Deck duchies;

    private Deck provinces;

    private Deck trash;

    private Deck action1;

    private Deck action2;

    private Deck action3;

    private Deck action4;

    private Deck action5;

    private Deck action6;

    private Deck action7;

    private Deck action8;

    private Deck action9;

    private Deck action10;

    private Map<String, Deck> map;

    public GameBoard() {
        coppers = new Deck();
        silvers = new Deck();
        golds = new Deck();
        curses = new Deck();
        estates = new Deck();
        duchies = new Deck();
        provinces = new Deck();
        trash = new Deck();
        action1 = new Deck();
        action2 = new Deck();
        action3 = new Deck();
        action4 = new Deck();
        action5 = new Deck();
        action6 = new Deck();
        action7 = new Deck();
        action8 = new Deck();
        action9 = new Deck();
        action10 = new Deck();
        map = new HashMap<String, Deck>();
    }

    public void createNewBoardGame(List<Card> kingdomCards, int victoryCards, int curseCards) {
        List<Card> storeList = new ArrayList<Card>();
        Constants.cardMap.put("copper", new Copper());
        for (int i = 0; i < 60; i++) {
            Card current = new Copper();
            storeList.add(current);
        }
        coppers.addToDeck(storeList);
        map.put("copper", coppers);
        storeList.clear();
        Constants.cardMap.put("silver", new Silver());
        for (int i = 0; i < 40; i++) {
            Card current = new Silver();
            storeList.add(current);
        }
        silvers.addToDeck(storeList);
        map.put("silver", silvers);
        storeList.clear();
        Constants.cardMap.put("gold", new Gold());
        for (int i = 0; i < 30; i++) {
            Card current = new Gold();
            storeList.add(current);
        }
        golds.addToDeck(storeList);
        map.put("gold", golds);
        storeList.clear();
        Constants.cardMap.put("estate", new Estate());
        Constants.cardMap.put("duchy", new Duchy());
        Constants.cardMap.put("province", new Province());
        for (int i = 0; i < victoryCards; i++) {
            Card estate = new Estate();
            Card duchy = new Duchy();
            Card prov = new Province();
            estates.addToDeck(estate);
            duchies.addToDeck(duchy);
            provinces.addToDeck(prov);
        }
        map.put("estate", estates);
        map.put("duchy", duchies);
        map.put("province", provinces);
        Constants.cardMap.put("curse", new Curse());
        for (int i = 0; i < curseCards; i++) {
            Card curse = new Curse();
            storeList.add(curse);
        }
        curses.addToDeck(storeList);
        map.put("curse", curses);
        for (Card action : kingdomCards) {
            Constants.cardMap.put(action.toString().toLowerCase(), action.clone());
            storeList.clear();
            for (int i = 0; i < 10; i++) {
                storeList.add(action.clone());
            }
            if (action1.isEmpty()) {
                action1.addToDeck(storeList);
                map.put(action.toString().toLowerCase(), action1);
            } else if (action2.isEmpty()) {
                action2.addToDeck(storeList);
                map.put(action.toString().toLowerCase(), action2);
            } else if (action3.isEmpty()) {
                action3.addToDeck(storeList);
                map.put(action.toString().toLowerCase(), action3);
            } else if (action4.isEmpty()) {
                action4.addToDeck(storeList);
                map.put(action.toString().toLowerCase(), action4);
            } else if (action5.isEmpty()) {
                action5.addToDeck(storeList);
                map.put(action.toString().toLowerCase(), action5);
            } else if (action6.isEmpty()) {
                action6.addToDeck(storeList);
                map.put(action.toString().toLowerCase(), action6);
            } else if (action7.isEmpty()) {
                action7.addToDeck(storeList);
                map.put(action.toString().toLowerCase(), action7);
            } else if (action8.isEmpty()) {
                action8.addToDeck(storeList);
                map.put(action.toString().toLowerCase(), action8);
            } else if (action9.isEmpty()) {
                action9.addToDeck(storeList);
                map.put(action.toString().toLowerCase(), action9);
            } else if (action10.isEmpty()) {
                action10.addToDeck(storeList);
                map.put(action.toString().toLowerCase(), action10);
            } else {
                System.out.println("ERROR WHEN CREATING NEW BOARD STATE!");
                System.exit(1);
            }
        }
    }

    public Card drawFromCopper() {
        return coppers.drawTopCard();
    }

    public void addToCopper(Copper card) {
        coppers.addToDeck(card);
    }

    public Card drawFromSilver() {
        return silvers.drawTopCard();
    }

    public void addToSilvers(Silver card) {
        silvers.addToDeck(card);
    }

    public Card drawFromGold() {
        return golds.drawTopCard();
    }

    public void addToGold(Gold card) {
        golds.addToDeck(card);
    }

    public Card drawFromEstates() {
        return estates.drawTopCard();
    }

    public void addToEstates(Estate card) {
        estates.addToDeck(card);
    }

    public Card drawFromDuchies() {
        return duchies.drawTopCard();
    }

    public void addToDuchies(Duchy card) {
        duchies.addToDeck(card);
    }

    public Card drawFromProvinces() {
        return provinces.drawTopCard();
    }

    public void addToProvinces(Province card) {
        provinces.addToDeck(card);
    }

    public Card drawFromCurses() {
        return curses.drawTopCard();
    }

    public void addToCurses(Curse card) {
        curses.addToDeck(card);
    }

    public Card drawFromTrash() {
        return trash.drawTopCard();
    }

    public void addToTrash(Card card) {
        trash.addToDeck(card);
    }

    public Card drawFromPile(int pileNumber) {
        Card card = null;
        switch(pileNumber) {
            case 1:
                card = action1.drawTopCard();
                break;
            case 2:
                card = action2.drawTopCard();
                break;
            case 3:
                card = action3.drawTopCard();
                break;
            case 4:
                card = action4.drawTopCard();
                break;
            case 5:
                card = action5.drawTopCard();
                break;
            case 6:
                card = action6.drawTopCard();
                break;
            case 7:
                card = action7.drawTopCard();
                break;
            case 8:
                card = action8.drawTopCard();
                break;
            case 9:
                card = action9.drawTopCard();
                break;
            case 10:
                card = action10.drawTopCard();
                break;
            default:
                System.out.println("WARNING: invalid draw pile given!");
                System.exit(1);
        }
        return card;
    }

    public List<Card> drawFromPile(int pile, int drawNumber) {
        List<Card> result = new ArrayList<Card>();
        for (int i = 0; i < drawNumber; i++) {
            switch(pile) {
                case Constants.COPPER_PILE:
                    result.add(drawFromCopper());
                    break;
                case Constants.SILVER_PILE:
                    result.add(drawFromSilver());
                    break;
                case Constants.GOLD_PILE:
                    result.add(drawFromGold());
                    break;
                case Constants.CURSE_PILE:
                    result.add(drawFromCurses());
                    break;
                case Constants.TRASH_PILE:
                    result.add(drawFromTrash());
                    break;
                case Constants.ESTATE_PILE:
                    result.add(drawFromEstates());
                    break;
                case Constants.DUCHY_PILE:
                    result.add(drawFromDuchies());
                    break;
                case Constants.PROVINCE_PILE:
                    result.add(drawFromProvinces());
                    break;
                default:
                    System.out.println("ERROR: Didn't define which pile to draw from");
                    System.exit(1);
            }
        }
        return result;
    }

    public List<Card> drawFromPile(String cardName, int drawNumber) {
        List<Card> result = new ArrayList<Card>();
        for (int i = 0; i < drawNumber; i++) {
            if (map.containsKey(cardName.toLowerCase())) {
                result.add(map.get(cardName.toLowerCase()).drawTopCard());
            } else {
                return null;
            }
        }
        return result;
    }

    public Card drawFromPile(String cardName) {
        return drawFromPile(cardName, 1).get(0);
    }

    public void addToPile(Card card, int pileNumber) {
        switch(pileNumber) {
            case 1:
                action1.addToDeck(card);
                break;
            case 2:
                action2.addToDeck(card);
                break;
            case 3:
                action3.addToDeck(card);
                break;
            case 4:
                action4.addToDeck(card);
                break;
            case 5:
                action5.addToDeck(card);
                break;
            case 6:
                action6.addToDeck(card);
                break;
            case 7:
                action7.addToDeck(card);
                break;
            case 8:
                action8.addToDeck(card);
                break;
            case 9:
                action9.addToDeck(card);
                break;
            case 10:
                action10.addToDeck(card);
                break;
            default:
                System.out.println("WARNING: invalid draw pile given!");
                System.exit(1);
        }
    }

    public Map<String, Deck> getMap() {
        return map;
    }

    public Deck getEstates() {
        return estates;
    }

    public Deck getDuchies() {
        return duchies;
    }

    public Deck getProvinces() {
        return provinces;
    }
}

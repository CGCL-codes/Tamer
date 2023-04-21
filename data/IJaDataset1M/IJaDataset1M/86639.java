package emil.poker.partyPoker;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;
import java.util.Map.Entry;
import javax.script.ScriptException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.jruby.IRuby;
import org.jruby.RubyArray;
import org.jruby.RubyHash;
import emil.poker.Card;
import emil.poker.Decision;
import emil.poker.Deck;
import emil.poker.Hand;
import emil.poker.LimitHoldem.Street;
import emil.poker.ai.AiPlayer;
import emil.poker.ai.DecisionHistory;
import emil.poker.ai.DecisionHistoryImpl;
import emil.poker.ai.DecisionProbability;
import emil.poker.ai.Robotnik;
import emil.poker.ai.RobotnikLimitHoldem;
import emil.poker.util.CycleVector;

public class MakeRobotnikDecision {

    Vector<AiPlayer> players;

    Map<Long, String> playersNamesAndPositions;

    long position = -1;

    AiPlayer lastActed;

    long dealer;

    List<Card> communityCards;

    List<Card> holeCards;

    private final String gameInfo;

    private Robotnik robotnik = new Robotnik();

    private List<Map<Street, Map<AiPlayer, Decision>>> decisions;

    private Hand hand;

    private Street lastStreet;

    private RobotnikLimitHoldem limitHoldem;

    public MakeRobotnikDecision(String gameInfo) {
        this.gameInfo = gameInfo;
    }

    public List<Card> getCommunityCards() {
        return communityCards;
    }

    public void setCommunityCards(List<Card> communityCards) {
        this.communityCards = communityCards;
    }

    public long getDealer() {
        return dealer;
    }

    public void setDealer(long dealer) {
        this.dealer = dealer;
    }

    public String getGameInfo() {
        return gameInfo;
    }

    public List<Card> getHoleCards() {
        return holeCards;
    }

    public void setHoleCards(List<Card> holeCards) {
        this.holeCards = holeCards;
    }

    public Vector<AiPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(CycleVector<AiPlayer> players) {
        this.players = players;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public static RubyHash getAsRubyHash(String info) throws ScriptException {
        IRuby runtime = RubyEngine.getInstance();
        Object object = runtime.evalScript(info);
        RubyHash rubyHash = (RubyHash) object;
        return rubyHash;
    }

    public void init() throws Exception {
        RubyHash rubyHash = getAsRubyHash(gameInfo);
        communityCards = createCards((RubyArray) rubyHash.get("communityCards"));
        holeCards = createCards((RubyArray) rubyHash.get("myCards"));
        dealer = (Long) rubyHash.get("dealer");
        position = (Long) rubyHash.get("myPosition");
        decisions = createDecisions(rubyHash);
        hand = createHand(rubyHash);
        if (players == null) {
            players = createPlayers(rubyHash, position);
        }
        RubyHash playersRH = (RubyHash) rubyHash.get("opponents");
        playersNamesAndPositions = new HashMap<Long, String>();
        playersNamesAndPositions.putAll(playersRH);
        playersNamesAndPositions.put(position, "Robotnik");
    }

    public Hand createHand(RubyHash rubyHash) throws ScriptException {
        RubyArray cards = (RubyArray) rubyHash.get("myCards");
        Card card = new Card(cards.get(0).toString());
        Card card2 = new Card(cards.get(1).toString());
        return new Hand(card, card2);
    }

    public Hand createHand(String info) throws ScriptException {
        return createHand(getAsRubyHash(info));
    }

    public List<Map<Street, Map<AiPlayer, Decision>>> createDecisions(RubyHash rh) {
        if (players == null) {
            players = createPlayers(rh, position);
        }
        RubyArray decisionsRA = (RubyArray) rh.get("decisions");
        List<Map<Street, Map<AiPlayer, Decision>>> decisionsList = new LinkedList<Map<Street, Map<AiPlayer, Decision>>>();
        for (Object object : decisionsRA) {
            RubyHash decisionsPerStreetRH = (RubyHash) object;
            Map<Street, Map<AiPlayer, Decision>> decisionsPerStreet = new HashMap<Street, Map<AiPlayer, Decision>>();
            for (Object entryObject : decisionsPerStreetRH.entrySet()) {
                Entry<String, RubyHash> entry = (Entry<String, RubyHash>) entryObject;
                RubyHash decisionPerPlayerRH = entry.getValue();
                Map<AiPlayer, Decision> decisionPerPlayer = new HashMap<AiPlayer, Decision>();
                for (Object entryObjectDecisionPerPlayer : decisionPerPlayerRH.entrySet()) {
                    Entry<String, String> entryDecisionPerPlayer = (Entry<String, String>) entryObjectDecisionPerPlayer;
                    AiPlayer player = getPlayer(entryDecisionPerPlayer.getKey());
                    Decision decision = Decision.valueOf(entryDecisionPerPlayer.getValue().toUpperCase());
                    decisionPerPlayer.put(player, decision);
                }
                decisionsPerStreet.put(Street.valueOf(entry.getKey().toUpperCase()), decisionPerPlayer);
            }
            decisionsList.add(decisionsPerStreet);
        }
        return decisionsList;
    }

    /**
	 * Skapar objekt av str�ngarna i RubyHash:en
	 * 
	 * @param gameInfo2
	 * @return
	 * @throws ScriptException
	 */
    public List<Map<Street, Map<AiPlayer, Decision>>> createDecisions(String gameInfo2) throws ScriptException {
        return createDecisions(getAsRubyHash(gameInfo2));
    }

    private AiPlayer getPlayer(String playerName) {
        for (AiPlayer player : players) {
            if (playerName.equals(player.getName())) {
                return player;
            }
        }
        throw new RuntimeException("Could not find player with name '" + playerName + "' in players " + players);
    }

    private List<Card> createCards(RubyArray array) {
        List<Card> cards = new LinkedList<Card>();
        if (array != null) {
            for (Object card : array) {
                String s = (String) card;
                String value = s.substring(0, 1);
                String suitShort = s.substring(1, 2);
                Card card2 = new Card(value, Deck.suitForShortString(suitShort));
                cards.add(card2);
            }
        }
        return cards;
    }

    public Vector<AiPlayer> createPlayers(RubyHash rh, long pos) {
        List<AiPlayer> tmpPlayers = new LinkedList<AiPlayer>();
        for (int i = 0; i < 11; i++) {
            tmpPlayers.add(null);
        }
        RubyHash liveOpponents = (RubyHash) rh.get("opponents");
        RubyHash allOpponents = (RubyHash) rh.get("allOpponents");
        if (allOpponents == null || allOpponents.size() == 0) {
            allOpponents = liveOpponents;
        }
        for (Object o : liveOpponents.entrySet()) {
            addPlayer(tmpPlayers, (Entry<Long, String>) o);
        }
        tmpPlayers.remove((int) pos);
        tmpPlayers.add((int) pos, robotnik);
        for (Object o : allOpponents.entrySet()) {
            Entry<Long, String> entry = (Entry<Long, String>) o;
            if ((entry.getKey().equals(dealer) || decisionExistsForPlayer(entry.getValue(), rh)) && !playerAlreadyAdded(entry.getValue(), tmpPlayers)) {
                addPlayer(tmpPlayers, (Entry<Long, String>) o);
            }
        }
        AiPlayer dealerPlayer = tmpPlayers.get((int) dealer);
        dealerPlayer.setDealer(true);
        while (tmpPlayers.indexOf(null) >= 0) {
            tmpPlayers.remove(null);
        }
        CycleVector<AiPlayer> cycleVector = new CycleVector<AiPlayer>();
        cycleVector.addAll(tmpPlayers);
        if (cycleVector.size() == 2) {
            dealerPlayer.setSmallBlind(true);
            dealerPlayer.setBigBlind(true);
        } else {
            cycleVector.get(cycleVector.indexOf(dealerPlayer) + 1).setSmallBlind(true);
            cycleVector.get(cycleVector.indexOf(dealerPlayer) + 2).setBigBlind(true);
        }
        return cycleVector;
    }

    private boolean playerAlreadyAdded(String playerName, List<AiPlayer> tmpPlayers) {
        for (AiPlayer player : tmpPlayers) {
            if (player != null && player.getName().equals(playerName)) {
                return true;
            }
        }
        return false;
    }

    private void addPlayer(List<AiPlayer> tmpPlayers, Entry<Long, String> entry) {
        PartyPokerPlayer player = new PartyPokerPlayer();
        player.setName(entry.getValue());
        Long l = entry.getKey();
        tmpPlayers.remove(l.intValue());
        tmpPlayers.add(l.intValue(), player);
    }

    private boolean decisionExistsForPlayer(String playerName, RubyHash rh) {
        RubyArray ra = (RubyArray) rh.get("decisions");
        for (Object object : ra) {
            RubyHash rh2 = (RubyHash) object;
            for (Object object2 : rh2.entrySet()) {
                Entry<Street, RubyHash> entry = (Entry<Street, RubyHash>) object2;
                RubyHash rh3 = entry.getValue();
                for (Object object3 : rh3.entrySet()) {
                    Entry<String, String> entry2 = (Entry<String, String>) object3;
                    if (entry2.getKey().equals(playerName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Vector<AiPlayer> createPlayers(String gameInfo2, long pos) throws ScriptException {
        return createPlayers(getAsRubyHash(gameInfo2), pos);
    }

    public List<Decision> getDecisions(String playerName, RubyHash rubyHash) {
        List<Decision> decisions = new LinkedList<Decision>();
        RubyArray rubyArray = (RubyArray) rubyHash.get("decisions");
        for (Object object : rubyArray) {
            RubyHash decisionPerPlayer = (RubyHash) object;
            for (Object key : decisionPerPlayer.keySet()) {
                if (key.equals(playerName)) {
                    String dec = decisionPerPlayer.get(key).toString();
                    Decision decision = Decision.valueOf(dec.toUpperCase());
                    decisions.add(decision);
                }
            }
        }
        return decisions;
    }

    public RobotnikLimitHoldem createGame() throws Exception {
        List<DecisionHistory> decisionHistories = createDecisionHistories();
        RobotnikLimitHoldem limitHoldem = RobotnikLimitHoldem.playGame(decisionHistories, players);
        limitHoldem.getTable().setCommunityCards(communityCards);
        return limitHoldem;
    }

    public List<DecisionHistory> createDecisionHistories() throws Exception {
        List<DecisionHistory> decisionHistories = new LinkedList<DecisionHistory>();
        while (atLeastOnePlayerLeftToAct()) {
            DecisionHistoryImpl decisionHistory = new DecisionHistoryImpl();
            GameInfo gameInfo = getNextGameInfo();
            decisionHistory.setStreet(gameInfo.getStreet());
            decisionHistory.setPlayer(gameInfo.getPlayer());
            decisionHistory.setDecision(gameInfo.getDecision());
            decisionHistories.add(decisionHistory);
        }
        return decisionHistories;
    }

    public GameInfo getNextGameInfo() {
        if (decisions.isEmpty()) {
            return null;
        }
        Map<Street, Map<AiPlayer, Decision>> firstMapInDecisions = decisions.get(0);
        while (firstMapInDecisions.isEmpty()) {
            decisions.remove(0);
            firstMapInDecisions = decisions.get(0);
        }
        SortedMap<Street, Map<AiPlayer, Decision>> sortedMap = new TreeMap<Street, Map<AiPlayer, Decision>>();
        sortedMap.putAll(firstMapInDecisions);
        Map<AiPlayer, Decision> decisionPerPlayer = sortedMap.get(sortedMap.firstKey());
        AiPlayer nextToAct = null;
        while (decisionPerPlayer.get(nextToAct) == null) {
            nextToAct = getNextPlayerToAct();
        }
        Decision decision = decisionPerPlayer.get(nextToAct);
        AiPlayer player = nextToAct;
        Street street = sortedMap.firstKey();
        decisionPerPlayer.remove(nextToAct);
        if (decisionPerPlayer.isEmpty()) {
            firstMapInDecisions.remove(sortedMap.firstKey());
        }
        if (firstMapInDecisions.isEmpty()) {
            decisions.remove(0);
        }
        return new GameInfo(player, decision, street);
    }

    /**
	 * FIXME Denna tar inte h�nsyn till om det �r en ny gata, det m�ste den ju
	 * g�ra.
	 * 
	 * @return
	 */
    public AiPlayer getNextPlayerToAct() {
        Street street = findCurrentStreet();
        CycleVector<AiPlayer> cycleVector = new CycleVector<AiPlayer>();
        cycleVector.addAll(players);
        AiPlayer bigBlind = null;
        AiPlayer dealerPlayer = null;
        for (AiPlayer player : players) {
            if (player.isBigBlind()) {
                bigBlind = player;
            }
            if (player.isDealer()) {
                dealerPlayer = player;
            }
        }
        if (street.equals(lastStreet)) {
            cycleVector.setCurrentPosition(cycleVector.indexOf(lastActed));
        } else {
            if (Street.PREFLOP.equals(street)) {
                cycleVector.setCurrentPosition(cycleVector.indexOf(bigBlind));
            } else {
                cycleVector.setCurrentPosition(cycleVector.indexOf(dealerPlayer));
            }
        }
        lastStreet = street;
        lastActed = cycleVector.next();
        return lastActed;
    }

    private Street findCurrentStreet() {
        Street street = Street.RIVER;
        for (Map<Street, Map<AiPlayer, Decision>> map : decisions) {
            for (Entry<Street, Map<AiPlayer, Decision>> decisionPerPlayerAndStreet : map.entrySet()) {
                if (decisionPerPlayerAndStreet.getKey().ordinal() < street.ordinal()) {
                    street = decisionPerPlayerAndStreet.getKey();
                }
            }
        }
        return street;
    }

    public boolean atLeastOnePlayerLeftToAct() {
        for (Map<Street, Map<AiPlayer, Decision>> map : decisions) {
            if (!map.isEmpty() && !map.entrySet().iterator().next().getValue().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) throws Exception {
        String gameInfo = args[0];
        MakeRobotnikDecision makeRobotnikDecision = new MakeRobotnikDecision(gameInfo);
        makeRobotnikDecision.init();
        Decision decision = makeRobotnikDecision.makeDecision();
        System.out.println(decision.toString());
    }

    public void prepareGame() throws Exception {
        Logger rootLogger = Logger.getRootLogger();
        limitHoldem = createGame();
        rootLogger.info("\n" + createHistoryInfo(limitHoldem));
        AiPlayer thisIsRobotnikClone = null;
        for (AiPlayer player : limitHoldem.getTable().getPlayers()) {
            if (player.getName().equals(robotnik.getName())) {
                thisIsRobotnikClone = player;
                break;
            }
        }
        if (thisIsRobotnikClone == null) {
            throw new RuntimeException();
        }
        PropertyUtils.copyProperties(robotnik, thisIsRobotnikClone);
        robotnik.setGame(limitHoldem);
        robotnik.setHand(hand);
        limitHoldem.replacePlayer(thisIsRobotnikClone, robotnik);
    }

    public DecisionProbability createDecisionProbability() throws Exception {
        return createDecisionProbability(true);
    }

    public DecisionProbability createDecisionProbability(boolean useCache) throws Exception {
        prepareGame();
        DecisionProbability decisionProbability = robotnik.createDecisionProbability(useCache);
        return decisionProbability;
    }

    public Decision makeDecision() throws Exception {
        DecisionProbability decisionProbability = createDecisionProbability();
        Decision[] legalDecisions = limitHoldem.getLegalDecisions(robotnik);
        Decision decision = robotnik.getLegalDecision(decisionProbability.getWeightedRandomDecision(), legalDecisions);
        return decision;
    }

    public static class GameInfo {

        AiPlayer player;

        Decision decision;

        Street street;

        public Decision getDecision() {
            return decision;
        }

        public void setDecision(Decision decision) {
            this.decision = decision;
        }

        public AiPlayer getPlayer() {
            return player;
        }

        public void setPlayer(AiPlayer player) {
            this.player = player;
        }

        public Street getStreet() {
            return street;
        }

        public void setStreet(Street street) {
            this.street = street;
        }

        public GameInfo(AiPlayer player, Decision decision, Street street) {
            super();
            this.player = player;
            this.decision = decision;
            this.street = street;
        }

        @Override
        public int hashCode() {
            final int PRIME = 31;
            int result = 1;
            result = PRIME * result + ((decision == null) ? 0 : decision.hashCode());
            result = PRIME * result + ((player == null) ? 0 : player.hashCode());
            result = PRIME * result + ((street == null) ? 0 : street.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            final GameInfo other = (GameInfo) obj;
            if (decision == null) {
                if (other.decision != null) return false;
            } else if (!decision.equals(other.decision)) return false;
            if (player == null) {
                if (other.player != null) return false;
            } else if (!player.equals(other.player)) return false;
            if (street == null) {
                if (other.street != null) return false;
            } else if (!street.equals(other.street)) return false;
            return true;
        }

        @Override
        public String toString() {
            return "<" + street.toString() + "," + decision.toString() + "," + player.getName() + ">";
        }
    }

    public void setPlayers(Vector<AiPlayer> players) {
        this.players = players;
    }

    public void setPlayers(List<AiPlayer> players2) {
        players = new CycleVector<AiPlayer>();
        players.addAll(players2);
    }

    public List<Map<Street, Map<AiPlayer, Decision>>> getDecisions() {
        return decisions;
    }

    public void setDecisions(List<Map<Street, Map<AiPlayer, Decision>>> decisions) {
        this.decisions = decisions;
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public String spaces(int numOfSpaces) {
        String s = "";
        for (int i = 0; i < numOfSpaces; i++) {
            s += " ";
        }
        return s;
    }

    /**
	 * Anv�nds f�r att �vers�tta decisionHistories till n�t som �r l�sbart f�r
	 * m�nniskor
	 * 
	 * @param limitHoldem
	 * @return
	 */
    public String createHistoryInfo(RobotnikLimitHoldem limitHoldem) {
        String s = spaces(15) + getPlayerInfoString(limitHoldem, 1) + "\n\n";
        s += spaces(5) + getPlayerInfoString(limitHoldem, 10) + spaces(27 - (spaces(5) + getPlayerInfoString(limitHoldem, 10)).length()) + getPlayerInfoString(limitHoldem, 2) + "\n\n";
        s += getPlayerInfoString(limitHoldem, 9) + spaces(31 - getPlayerInfoString(limitHoldem, 9).length()) + getPlayerInfoString(limitHoldem, 3) + "\n\n";
        s += getPlayerInfoString(limitHoldem, 8) + spaces(31 - getPlayerInfoString(limitHoldem, 8).length()) + getPlayerInfoString(limitHoldem, 4) + "\n\n";
        s += spaces(5) + getPlayerInfoString(limitHoldem, 7) + spaces(27 - (spaces(5) + getPlayerInfoString(limitHoldem, 7)).length()) + getPlayerInfoString(limitHoldem, 5) + "\n\n";
        s += spaces(15) + getPlayerInfoString(limitHoldem, 6) + "\n\n";
        return s;
    }

    private String getPlayerInfoString(RobotnikLimitHoldem limitHoldem, long i) {
        String s;
        try {
            AiPlayer iPlayer = null;
            for (AiPlayer player : limitHoldem.getPlayers()) {
                if (player.getName().equals(playersNamesAndPositions.get(i))) {
                    iPlayer = player;
                }
            }
            s = iPlayer.getName();
            if (iPlayer.isDealer()) {
                s += " @";
            } else if (iPlayer.isSmallBlind()) {
                s += " �";
            } else if (iPlayer.isBigBlind()) {
                s += " ��";
            }
            s += getPlayerBettingChainString(iPlayer, limitHoldem);
        } catch (RuntimeException e) {
            s = "--";
        }
        return s;
    }

    private String getPlayerBettingChainString(AiPlayer player, RobotnikLimitHoldem limitHoldem) {
        Street lastStreet = Street.PREFLOP;
        String decisions = " ";
        for (DecisionHistory decisionHistory : limitHoldem.getDecisionHistories()) {
            if (player.equals(decisionHistory.getPlayer())) {
                if (decisionHistory.getStreet().ordinal() > lastStreet.ordinal()) {
                    decisions += "|";
                }
                decisions += decisionHistory.getParentNodeDecision().toString().substring(0, 1);
                lastStreet = decisionHistory.getStreet();
            }
        }
        return decisions.toLowerCase();
    }

    public RobotnikLimitHoldem getRobotnikLimitHoldem() {
        return limitHoldem;
    }

    public Robotnik getRobotnik() {
        return robotnik;
    }
}

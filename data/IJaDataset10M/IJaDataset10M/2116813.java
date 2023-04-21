package emil.poker.ai;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.sourceforge.robotnik.poker.Decision;
import net.sourceforge.robotnik.poker.History;
import net.sourceforge.robotnik.poker.LimitHoldem;
import net.sourceforge.robotnik.poker.Player;
import net.sourceforge.robotnik.poker.Poker;
import net.sourceforge.robotnik.poker.Street;
import net.sourceforge.robotnik.poker.Hand.HandValue;
import net.sourceforge.robotnik.poker.util.Randomizer;
import net.sourceforge.robotnik.poker.util.RandomizerImpl;
import org.apache.log4j.Logger;
import emil.poker.ai.opponentEvaluators.CachedObjectKey;
import emil.poker.ai.opponentEvaluators.CachedObjects;
import emil.poker.ai.opponentEvaluators.ChanceToGetHandValuesOnRiverEvaluator;
import emil.poker.ai.opponentEvaluators.ChanceToGetHandValuesOnRiverEvaluatorImpl;
import emil.poker.ai.opponentEvaluators.ChanceToWinShowDownAllOpponentsEvaluator;
import emil.poker.ai.opponentEvaluators.ChanceToWinShowDownAllOpponentsEvaluatorImpl;
import emil.poker.ai.opponentEvaluators.CommunityCardsHandEvaluator;
import emil.poker.ai.opponentEvaluators.HistoryCreator;
import emil.poker.ai.opponentEvaluators.PostFlopDecisionsEvaluator;
import emil.poker.ai.opponentEvaluators.PostFlopHandAndDecisionEvaluator;
import emil.poker.ai.opponentEvaluators.PreFlopHandEvaluator;
import emil.poker.ai.opponentEvaluators.ProbabilityPerDecisionEvaluatorImpl;
import emil.poker.ai.util.ProbabilityHashMap;
import emil.poker.entities.PostFlopHist;
import emil.poker.entities.StartHandsHist;
import emil.poker.entities.StartHandsBase.StartHandsBaseKey;
import emil.poker.internetPlay.PreloaderImpl;

public class PostFlopDecisionMakerCreator implements DecisionMakerCreator {

    Randomizer randomizer = new RandomizerImpl();

    Map<String, PlayerType> playerTypes = new HashMap<String, PlayerType>();

    Map<Player, ProbabilityHashMap<Decision, Double>> chancePerDecisionPerPlayer = new HashMap<Player, ProbabilityHashMap<Decision, Double>>();

    Map<Player, Map<HandValue, Double>> chancePerCurrentValuePerOpponent = new HashMap<Player, Map<HandValue, Double>>();

    Map<Decision, Map<Player, ProbabilityHashMap<Decision, Double>>> chancePerDecisionPerPlayerPerStartDecision = new HashMap<Decision, Map<Player, ProbabilityHashMap<Decision, Double>>>();

    Map<Player, CommunityCardsHandEvaluator> communityCardsHandEvaluatorPerPlayer = new HashMap<Player, CommunityCardsHandEvaluator>();

    Map<Player, PostFlopHandAndDecisionEvaluator> postFlopHandAndDecisionEvaluators = new HashMap<Player, PostFlopHandAndDecisionEvaluator>();

    static Logger logger = Logger.getLogger(PostFlopDecisionMakerCreator.class);

    private final RobotnikLimitHoldem poker;

    private final Player player;

    public PostFlopDecisionMakerCreator(Player player1, Poker poker) {
        this.player = player1;
        this.poker = (RobotnikLimitHoldem) poker;
    }

    public DecisionMaker createDecisionMaker() {
        return createDecisionMaker(false);
    }

    public DecisionMaker createDecisionMaker(boolean useSimple) {
        List<Player> liveOpponents = poker.getActivePlayers();
        liveOpponents.remove(player);
        for (Player tmpPlayer : liveOpponents) {
            PlayerEvaluator playerEvaluator = new PostFlopPlayerEvaluator(tmpPlayer);
            playerEvaluator.evaluate();
            playerTypes.put(tmpPlayer.getName(), playerEvaluator.getPlayerType());
            createEvaluatorsForPlayer(tmpPlayer);
        }
        ChanceToGetHandValuesOnRiverEvaluator chanceToGetHandValuesOnRiverEvaluator = createChancePerValueEvaluator();
        Map<HandValue, Double> strengthPerHandValue = chanceToGetHandValuesOnRiverEvaluator.getStrengthPerHandValue();
        List<List<Player>> allPlayerCombinations = playerCombinations(liveOpponents);
        ChanceToWinShowDownAllOpponentsEvaluator chanceToWinShowDownAllOpponentsEvaluator = createChanceToWinShowdownEvaluator(chanceToGetHandValuesOnRiverEvaluator, strengthPerHandValue, allPlayerCombinations);
        PostFlopDecisionMaker postFlopDecisionMaker = initializePostFlopDecisionMaker(useSimple, liveOpponents);
        postFlopDecisionMaker.setStrengthPerHandValue(strengthPerHandValue);
        postFlopDecisionMaker.setChanceToWinShowDownAllOpponentsEvaluator(chanceToWinShowDownAllOpponentsEvaluator);
        postFlopDecisionMaker.setCommunityCardsHandEvaluatorPerPlayer(communityCardsHandEvaluatorPerPlayer);
        postFlopDecisionMaker.setPostFlopHandAndDecisionEvaluators(postFlopHandAndDecisionEvaluators);
        postFlopDecisionMaker.setChancePerDecisionPerPlayerPerStartDecision(chancePerDecisionPerPlayerPerStartDecision);
        postFlopDecisionMaker.setPlayerTypes(playerTypes);
        return postFlopDecisionMaker;
    }

    private ChanceToWinShowDownAllOpponentsEvaluator createChanceToWinShowdownEvaluator(ChanceToGetHandValuesOnRiverEvaluator chanceToGetHandValuesOnRiverEvaluator, Map<HandValue, Double> strengthPerHandValue, List<List<Player>> allPlayerCombinations) {
        ChanceToWinShowDownAllOpponentsEvaluator chanceToWinShowDownAllOpponentsEvaluator = new ChanceToWinShowDownAllOpponentsEvaluatorImpl();
        chanceToWinShowDownAllOpponentsEvaluator.setChancePerValueOnShowDown(chanceToGetHandValuesOnRiverEvaluator.getChanceToGetHandValuesOnRiver());
        chanceToWinShowDownAllOpponentsEvaluator.setPlayerCombinations(allPlayerCombinations);
        chanceToWinShowDownAllOpponentsEvaluator.setChancePerCurrentValuePerOpponent(chancePerCurrentValuePerOpponent);
        chanceToWinShowDownAllOpponentsEvaluator.setStrenghtPerHandValue(strengthPerHandValue);
        chanceToWinShowDownAllOpponentsEvaluator.evaluate();
        return chanceToWinShowDownAllOpponentsEvaluator;
    }

    private List<List<Player>> playerCombinations(List<Player> liveOpponents) {
        PlayerCombinations playerCombinations = new PlayerCombinationsImpl();
        playerCombinations.setPlayers(liveOpponents);
        List<List<Player>> allPlayerCombinations = playerCombinations.listAllCombinations();
        return allPlayerCombinations;
    }

    private PostFlopDecisionMaker initializePostFlopDecisionMaker(boolean useSimple, List<Player> liveOpponents) {
        int maxNumOfOpponents = new Integer(RobotnikProperties.getInstance().get("robotnik.PostFlopDecisionMaker.maxNumOfOpponents").toString());
        double useSimplePercentTime = new Double(RobotnikProperties.getInstance().get("robotnik.PostFlopDecisionMaker.useSimplePercentOfTime").toString());
        useSimplePercentTime /= 100;
        PostFlopDecisionMaker postFlopDecisionMaker;
        if (liveOpponents.size() > maxNumOfOpponents || randomizer.random() < useSimplePercentTime) {
            postFlopDecisionMaker = new SimplePostFlopDecisionMaker(player, poker);
        } else if (useSimple) {
            postFlopDecisionMaker = new SimplePostFlopDecisionMaker(player, poker);
        } else {
            postFlopDecisionMaker = new PostFlopDecisionMakerImpl(player, poker);
        }
        return postFlopDecisionMaker;
    }

    public static List<Class> classesToPreloadForAllPlayers() {
        List<Class> l = new LinkedList<Class>();
        l.add(PreFlopHandEvaluator.class);
        l.add(CommunityCardsHandEvaluator.class);
        l.add(PreFlopPlayerEvaluator.class);
        l.add(PostFlopPlayerEvaluator.class);
        return l;
    }

    public static Map<Class, Evaluator> createPreFlopHandEvaluatorsForPlayer(Player tmpPlayer, RobotnikLimitHoldem robotnikLimitHoldem) {
        HistoryCreator creator = new HistoryCreator();
        PostFlopHist postFlopHistAfterPlayerActed = creator.createHistory(robotnikLimitHoldem.getDecisionHistories(), tmpPlayer);
        if (((AiPlayer) tmpPlayer).isRobotnik()) {
            return null;
        }
        CachedObjectKey key = PreloaderImpl.createPreloadCacheKey(robotnikLimitHoldem, PreFlopHandEvaluator.class, tmpPlayer);
        PreFlopHandEvaluator preFlopHandEvaluator = (PreFlopHandEvaluator) CachedObjects.getCachedObject(key);
        if (preFlopHandEvaluator != null) {
            logger.info(key + " is cached, reusing it");
        } else {
            logger.info(key + " is not found in cache, creating it");
            preFlopHandEvaluator = new PreFlopHandEvaluator(tmpPlayer, (RobotnikLimitHoldem) robotnikLimitHoldem);
            preFlopHandEvaluator.evaluate();
            logger.info("Caching " + key);
            CachedObjects.cache(key, preFlopHandEvaluator);
        }
        HashMap<Long, Double> chancePlayerHasHandGroups;
        chancePlayerHasHandGroups = preFlopHandEvaluator.getChancePlayerHasHandGroups();
        CommunityCardsHandEvaluator communityCardsHandEvaluator = new CommunityCardsHandEvaluator(chancePlayerHasHandGroups, (LimitHoldem) robotnikLimitHoldem);
        communityCardsHandEvaluator.evaluate();
        Map<Class, Evaluator> m = new HashMap<Class, Evaluator>();
        m.put(PreFlopHandEvaluator.class, preFlopHandEvaluator);
        m.put(CommunityCardsHandEvaluator.class, communityCardsHandEvaluator);
        return m;
    }

    private void createEvaluatorsForPlayer(Player tmpPlayer) {
        Map<String, History> historyPerPlayer = HistoryPerPlayerCreator.createHistoryPerPlayer(poker.getDecisionHistories(), poker.getPlayers(), new RandomizerImpl());
        HistoryCreator creator = new HistoryCreator();
        PostFlopHist postFlopHistAfterPlayerActed = creator.createHistory(poker.getDecisionHistories(), tmpPlayer);
        Map<Class, Evaluator> preFlopHandEvaluatorsForPlayer = createPreFlopHandEvaluatorsForPlayer(tmpPlayer, poker);
        CommunityCardsHandEvaluator communityCardsHandEvaluator = (CommunityCardsHandEvaluator) preFlopHandEvaluatorsForPlayer.get(CommunityCardsHandEvaluator.class);
        communityCardsHandEvaluatorPerPlayer.put(tmpPlayer, communityCardsHandEvaluator);
        PostFlopDecisionsEvaluator postFlopDecisionsEvaluator = createPostFlopDecisionsEvaluator(tmpPlayer, historyPerPlayer.get(tmpPlayer.getName()));
        PostFlopHandAndDecisionEvaluator postFlopHandAndDecisionEvaluator = createPostFlopHandAndDecisionEvaluator(tmpPlayer, communityCardsHandEvaluator, postFlopDecisionsEvaluator);
        ProbabilityPerDecisionEvaluator probabilityPerDecisionEvaluator = createProbPerDecisionEvaluator(tmpPlayer, postFlopHistAfterPlayerActed, postFlopHandAndDecisionEvaluator);
        ProbabilityHashMap<Decision, Double> probabilitesPerDecision = probabilityPerDecisionEvaluator.getProbabilitesPerDecision();
        chancePerDecisionPerPlayer.put(tmpPlayer, probabilitesPerDecision);
        for (Decision d : Decision.values()) {
            chancePerDecisionPerPlayerPerStartDecision.put(d, chancePerDecisionPerPlayer);
        }
    }

    private ProbabilityPerDecisionEvaluator createProbPerDecisionEvaluator(Player tmpPlayer, PostFlopHist postFlopHistAfterPlayerActed, PostFlopHandAndDecisionEvaluator postFlopHandAndDecisionEvaluator) {
        ProbabilityPerDecisionEvaluator probabilityPerDecisionEvaluator = new ProbabilityPerDecisionEvaluatorImpl(tmpPlayer, poker);
        probabilityPerDecisionEvaluator.setPlayerType(playerTypes.get(tmpPlayer.getName()));
        probabilityPerDecisionEvaluator.setPostFlopHistKey(postFlopHistAfterPlayerActed.getPostFlopHistKey());
        probabilityPerDecisionEvaluator.setProbabilitesPerValue(postFlopHandAndDecisionEvaluator.getProbabilitiesPerValue());
        probabilityPerDecisionEvaluator.evaluate();
        return probabilityPerDecisionEvaluator;
    }

    private PostFlopHandAndDecisionEvaluator createPostFlopHandAndDecisionEvaluator(Player tmpPlayer, CommunityCardsHandEvaluator communityCardsHandEvaluator, PostFlopDecisionsEvaluator postFlopDecisionsEvaluator) {
        PostFlopHandAndDecisionEvaluator postFlopHandAndDecisionEvaluator = new PostFlopHandAndDecisionEvaluator(tmpPlayer, (RobotnikLimitHoldem) poker, postFlopDecisionsEvaluator.getProbabilitiesPerValue(), communityCardsHandEvaluator.getProbabilityPerHandValue());
        postFlopHandAndDecisionEvaluator.evaluate();
        postFlopHandAndDecisionEvaluators.put(tmpPlayer, postFlopHandAndDecisionEvaluator);
        chancePerCurrentValuePerOpponent.put(tmpPlayer, postFlopHandAndDecisionEvaluator.getProbabilitiesPerValue());
        return postFlopHandAndDecisionEvaluator;
    }

    private PostFlopDecisionsEvaluator createPostFlopDecisionsEvaluator(Player tmpPlayer, History historyBeforePlayerActed) {
        PostFlopDecisionsEvaluator postFlopDecisionsEvaluator = new PostFlopDecisionsEvaluator(tmpPlayer, (RobotnikLimitHoldem) poker);
        postFlopDecisionsEvaluator.setPlayerType(playerTypes.get(tmpPlayer.getName()));
        postFlopDecisionsEvaluator.setEvaluateDecision(tmpPlayer.getLastDecision());
        List<DecisionHistory> decisionHistoriesForPlayer = poker.getDecisionHistories(tmpPlayer);
        StartHandsBaseKey startHandsBaseKey = null;
        if (historyBeforePlayerActed instanceof PostFlopHist) {
            PostFlopHist postFlopHist = (PostFlopHist) historyBeforePlayerActed;
            postFlopDecisionsEvaluator.setPostFlopHistKey(postFlopHist.getPostFlopHistKey());
            startHandsBaseKey = postFlopHist.getPostFlopHistKey().getStartHandsBaseKey();
        } else {
            startHandsBaseKey = ((StartHandsHist) historyBeforePlayerActed).getStartHandsKey();
        }
        postFlopDecisionsEvaluator.setStartHandsBaseKey(startHandsBaseKey);
        try {
            if (decisionHistoriesForPlayer.get(decisionHistoriesForPlayer.size() - 1).getStreet().ordinal() >= Street.FLOP.ordinal()) {
                postFlopDecisionsEvaluator.evaluate();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return postFlopDecisionsEvaluator;
    }

    private ChanceToGetHandValuesOnRiverEvaluator createChancePerValueEvaluator() {
        ChanceToGetHandValuesOnRiverEvaluator chanceToGetHandValuesOnRiverEvaluator = new ChanceToGetHandValuesOnRiverEvaluatorImpl();
        chanceToGetHandValuesOnRiverEvaluator.setPlayer(player);
        chanceToGetHandValuesOnRiverEvaluator.setPoker(poker);
        chanceToGetHandValuesOnRiverEvaluator.setHand(player.getHand());
        chanceToGetHandValuesOnRiverEvaluator.setCommunityCards(poker.getTable().getCommunityCards());
        chanceToGetHandValuesOnRiverEvaluator.evaluate();
        logger.info("Chance per value: " + chanceToGetHandValuesOnRiverEvaluator.getChanceToGetHandValuesOnRiver());
        logger.info("Strength per value: " + chanceToGetHandValuesOnRiverEvaluator.getStrengthPerHandValue());
        return chanceToGetHandValuesOnRiverEvaluator;
    }
}

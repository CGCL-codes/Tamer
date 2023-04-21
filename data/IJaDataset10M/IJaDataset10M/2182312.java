package edu.asu.commons.foraging.conf;

import java.awt.Dimension;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import edu.asu.commons.conf.ExperimentRoundParameters;
import edu.asu.commons.foraging.graphics.Point3D;
import edu.asu.commons.foraging.model.ClientData;
import edu.asu.commons.foraging.model.EnforcementMechanism;

/**
 * $Id: RoundConfiguration.java 534 2011-05-08 02:02:39Z alllee $
 * 
 * At some point this should be persistent database objects in a key-value store..?  
 * 
 * Something like:
 * 
 * Parameter name, value, type, instructions 
 * 
 * need to deal with i18n at some point as well..
 * 
 * 
 *
 * @author <a href='mailto:Allen.Lee@asu.edu'>Allen Lee</a>
 * @version $Rev: 534 $
 */
public class RoundConfiguration extends ExperimentRoundParameters.Base<ServerConfiguration> {

    private static final long serialVersionUID = 8575239803733029326L;

    public static final double DEFAULT_REGROWTH_RATE = 0.01;

    public static final int DEFAULT_ROUND_TIME = 5 * 60;

    private static final int DEFAULT_SANCTION_FLASH_DURATION = 3;

    private static final double DEFAULT_DOLLARS_PER_TOKEN = .02d;

    private static final double DEFAULT_TOKEN_MOVEMENT_PROBABILITY = 0.2d;

    private static final double DEFAULT_TOKEN_BIRTH_PROBABILITY = 0.01d;

    public enum SanctionType {

        REAL_TIME, POST_ROUND, NONE;

        public static SanctionType find(String name) {
            try {
                return valueOf(name.toUpperCase().replaceAll("-", "_"));
            } catch (Exception exception) {
                return NONE;
            }
        }
    }

    private static final Map<String, ExperimentType> experimentTypeMap = new HashMap<String, ExperimentType>(3);

    public enum ExperimentType {

        TWO_DIMENSIONAL("2d"), ABSTRACT("abstract"), FORESTRY("forestry");

        private final String name;

        private ExperimentType(String name) {
            this.name = name;
            experimentTypeMap.put(name, this);
        }

        public static ExperimentType find(String name) {
            ExperimentType experimentType = experimentTypeMap.get(name);
            if (experimentType == null) {
                experimentType = TWO_DIMENSIONAL;
            }
            return experimentType;
        }

        public String toString() {
            return name;
        }
    }

    public enum SanctionAction {

        FINE {

            public void applySanctionCost(ClientData clientData) {
            }

            public void applySanctionPenalty(ClientData clientData) {
            }
        }
        , FREEZE {

            public void applySanctionCost(ClientData clientData) {
            }

            public void applySanctionPenalty(ClientData clientData) {
            }
        }
        ;

        public void applySanctionCost(ClientData clientData) {
        }

        public void applySanctionPenalty(ClientData clientData) {
        }

        public boolean isFine() {
            return this == FINE;
        }

        public boolean isFreeze() {
            return this == FREEZE;
        }
    }

    public RoundConfiguration() {
        super();
    }

    public RoundConfiguration(String resource) {
        super(resource);
    }

    public boolean shouldRandomizeGroup() {
        return (isPracticeRound() && isPrivateProperty()) || getBooleanProperty("randomize-group", false);
    }

    /**
     * Returns the number of seconds that the flashing visualization of
     * sanctioning should occur.
     * 
     * @return
     */
    public int getSanctionFlashDuration() {
        return getIntProperty("sanction-flash-duration", DEFAULT_SANCTION_FLASH_DURATION);
    }

    public double getTokenBirthProbability() {
        return getDoubleProperty("token-birth-probability", DEFAULT_TOKEN_BIRTH_PROBABILITY);
    }

    public double getTokenMovementProbability() {
        return getDoubleProperty("token-movement-probability", DEFAULT_TOKEN_MOVEMENT_PROBABILITY);
    }

    public boolean isTokensFieldOfVisionEnabled() {
        return getBooleanProperty("tokens-field-of-vision", false);
    }

    public boolean isSubjectsFieldOfVisionEnabled() {
        return getBooleanProperty("subjects-field-of-vision", false);
    }

    public double getViewSubjectsRadius() {
        if (isSubjectsFieldOfVisionEnabled()) {
            return getDoubleProperty("view-subjects-radius", 6.0d);
        }
        throw new UnsupportedOperationException("subject field of vision is not enabled.");
    }

    public double getViewTokensRadius() {
        if (isTokensFieldOfVisionEnabled()) {
            return getDoubleProperty("view-tokens-radius", 6.0d);
        }
        throw new UnsupportedOperationException("view tokens field of vision is not enabled.");
    }

    /**
     * Returns a double between [0, 1] used as a scaling factor modifying the probability 
     * that a token grows in a neighboring cell. 
     * @return
     */
    public double getRegrowthRate() {
        return getDoubleProperty("regrowth-rate", DEFAULT_REGROWTH_RATE);
    }

    public int getInitialNumberOfTokens() {
        return getIntProperty("starting-tokens", (int) (getInitialDistribution() * getResourceWidth() * getResourceDepth()));
    }

    public double getInitialDistribution() {
        return getDoubleProperty("initial-distribution", 0.25d);
    }

    public Dimension getBoardSize() {
        return new Dimension(getResourceWidth(), getResourceDepth());
    }

    public int getResourceWidth() {
        return getIntProperty("resource-width", 28);
    }

    public int getResourceDepth() {
        return getIntProperty("resource-depth", 28);
    }

    public boolean isPrivateProperty() {
        return getBooleanProperty("private-property");
    }

    public boolean isPracticeRound() {
        return getBooleanProperty("practice-round");
    }

    public int getClientsPerGroup() {
        if (isPrivateProperty()) {
            return 1;
        }
        return getIntProperty("clients-per-group", Integer.MAX_VALUE);
    }

    /**
     * Returns an int specifying how many tokens the sanctioner must pay to 
     * penalize another player.
     * @return
     */
    public int getSanctionCost() {
        return getIntProperty("sanction-cost", 1);
    }

    /**
     * Returns an int specifying how much we should scale the tokens used to sanction another 
     * player (for a bonus or penalty).
     * @return
     */
    public int getSanctionMultiplier() {
        return getIntProperty("sanction-multiplier", 2);
    }

    public int getSanctionPenalty() {
        return getSanctionCost() * getSanctionMultiplier();
    }

    public SanctionType getSanctionType() {
        return SanctionType.find(getProperty("sanction-type", "none"));
    }

    public boolean isPostRoundSanctioningEnabled() {
        return getSanctionType().equals(SanctionType.POST_ROUND);
    }

    public boolean isRealTimeSanctioningEnabled() {
        return getSanctionType().equals(SanctionType.REAL_TIME);
    }

    public boolean isSanctioningEnabled() {
        return isRealTimeSanctioningEnabled() || isPostRoundSanctioningEnabled();
    }

    public boolean shouldCheckOccupancy() {
        return getMaximumOccupancyPerCell() < getClientsPerGroup();
    }

    public int getMaximumOccupancyPerCell() {
        return getIntProperty("max-cell-occupancy", getClientsPerGroup());
    }

    public boolean isChatAnonymized() {
        return getBooleanProperty("anonymous-chat", false);
    }

    public double getDollarsPerToken() {
        return getDoubleProperty("dollars-per-token", DEFAULT_DOLLARS_PER_TOKEN);
    }

    /**
     * Returns the instructions for this round.
     */
    public String getInstructions() {
        return getProperty("instructions", "<b>No instructions available for this round.</b>");
    }

    public boolean shouldDisplayGroupTokens() {
        return getBooleanProperty("display-group-tokens");
    }

    public boolean isQuizEnabled() {
        return getBooleanProperty("quiz");
    }

    /**
     * FIXME: quiz instructions and quiz enabled should be tightly coupled..
     * @return
     */
    public String getQuizInstructions() {
        return getProperty("quiz-instructions");
    }

    public String getChatInstructions() {
        return getProperty("chat-instructions");
    }

    public String getRegulationInstructions() {
        return getProperty("regulation-instructions");
    }

    public String getVotingInstructions() {
        return getProperty("voting-instructions", "You may rank the options below from 1 to 5, where 1 is the most favorable and 5 is the least favorable.  When you rank a given option it will be sorted automatically.");
    }

    public String getLastRoundDebriefing() {
        return getProperty("last-round-debriefing");
    }

    public Map<String, String> getQuizAnswers() {
        Properties properties = getProperties();
        if (isQuizEnabled()) {
            Map<String, String> answers = new HashMap<String, String>();
            for (int i = 1; properties.containsKey("q" + i); i++) {
                String key = "q" + i;
                String answer = properties.getProperty(key);
                answers.put(key, answer);
            }
            return answers;
        }
        return Collections.emptyMap();
    }

    /**
     * Possible values, freeze, fine?
     * @return
     */
    public SanctionAction getSanctionAction() {
        return SanctionAction.valueOf(getProperty("sanction-action", "FINE"));
    }

    public int getNumberOfSanctionOpportunities() {
        return getIntProperty("sanction-opportunities", 30);
    }

    public int getChatDuration() {
        return getIntProperty("chat-duration", 240);
    }

    public int getSanctionVotingDuration() {
        return getIntProperty("sanction-voting-duration", 30);
    }

    public int getRegulationSubmissionDuration() {
        return getIntProperty("regulation-submission-duration", 60);
    }

    public int getRegulationDisplayDuration() {
        return getIntProperty("regulation-display-duration", 30);
    }

    public int getRegulationVotingDuration() {
        return getIntProperty("regulation-voting-duration", 60);
    }

    public int getEnforcementVotingDuration() {
        return getIntProperty("enforcement-voting-duration", 60);
    }

    public int getEnforcementDisplayDuration() {
        return getIntProperty("enforcement-display-duration", 30);
    }

    public String getSanctionInstructions() {
        return getProperty("sanction-instructions", "<h2>Voting instructions</h2>" + "<ul> " + "<li> You must make a choice within the next 30 seconds. " + "<li>The votes of all participants in your group will determine the outcome." + "</ul>");
    }

    public boolean isAlwaysInExplicitCollectionMode() {
        return getBooleanProperty("always-explicit", true);
    }

    public boolean isExplicitCollectionEnabled() {
        return getBooleanProperty("explicit-collection", true);
    }

    public double getTopRegrowthScalingFactor() {
        return getDoubleProperty("top-rate", 0.02);
    }

    public double getBottomRegrowthScalingFactor() {
        return getDoubleProperty("bottom-rate", 0.01);
    }

    public double getTopInitialResourceDistribution() {
        return getDoubleProperty("top-initial-distribution", 0.50);
    }

    public double getBottomInitialResourceDistribution() {
        return getDoubleProperty("bottom-initial-distribution", 0.25);
    }

    public String getResourceGeneratorType() {
        return getProperty("resource-generator", "density-dependent");
    }

    public int getWorldWidth() {
        return getResourceWidth() * getResourceWorldScale();
    }

    public int getWorldDepth() {
        return getResourceDepth() * getResourceWorldScale();
    }

    public int getResourceWorldScale() {
        return getIntProperty("resource-scale", 32);
    }

    public boolean isChatEnabled() {
        return !isPrivateProperty() && getBooleanProperty("chat-enabled");
    }

    public int getMaximumResourceAge() {
        return getIntProperty("maximum-resource-age", 10);
    }

    public int getChattingRadius() {
        return getIntProperty("chat-radius", 50);
    }

    public int getResourceAgingSecondsPerYear() {
        return getIntProperty("seconds-per-year", 10);
    }

    public Point3D getTopLeftCornerCoordinate() {
        float zExtend = getWorldWidth() / 2.0f;
        float xExtend = getWorldDepth() / 2.0f;
        return new Point3D(-xExtend, 0, -zExtend);
    }

    public int ageToTokens(int resourceAge) {
        switch(resourceAge) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 6;
            case 4:
                return 9;
            case 5:
                return 15;
            case 6:
                return 20;
            case 7:
                return 23;
            case 8:
            default:
                return 25;
        }
    }

    public int getTokensPerFruits() {
        return getIntProperty("tokens-per-fruits", 4);
    }

    public int getFruitHarvestDelay() {
        return getIntProperty("fruit-harvest-delay", 20);
    }

    public ExperimentType getExperimentType() {
        return ExperimentType.find(getStringProperty("experiment-type", "2d"));
    }

    public boolean is2dExperiment() {
        return getExperimentType().equals(ExperimentType.TWO_DIMENSIONAL);
    }

    public boolean is3dExperiment() {
        return !is2dExperiment();
    }

    public String getWelcomeInstructions() {
        return getParentConfiguration().getWelcomeInstructions();
    }

    public String getGeneralInstructions() {
        return getParentConfiguration().getGeneralInstructions();
    }

    public String getFieldOfVisionInstructions() {
        return getParentConfiguration().getFieldOfVisionInstructions();
    }

    public EnforcementMechanism[] getEnforcementMechanisms() {
        return EnforcementMechanism.values();
    }

    public boolean isRotatingMonitorEnabled() {
        return getBooleanProperty("rotating-monitor-enabled", false);
    }

    public boolean isVotingAndRegulationEnabled() {
        return getBooleanProperty("voting-and-regulation-enabled", false);
    }

    public boolean isFieldOfVisionEnabled() {
        return isTokensFieldOfVisionEnabled() || isSubjectsFieldOfVisionEnabled();
    }

    public boolean isCensoredChat() {
        return getBooleanProperty("censored-chat-enabled", false);
    }

    public boolean isTrustGameEnabled() {
        return getBooleanProperty("trust-game", false);
    }

    public boolean isInRoundChatEnabled() {
        return getBooleanProperty("in-round-chat-enabled", false);
    }

    public String getCensoredChatInstructions() {
        return getProperty("censored-chat-instructions", "Your messages must be approved before they will be relayed to the rest of your group.");
    }

    public int getNumberOfChatsPerSecond() {
        return getIntProperty("chats-per-second", 5);
    }

    public int getDelayBetweenChats() {
        return getIntProperty("delay-between-chats", 0);
    }

    public StringBuilder getCurrentRoundInstructions() {
        return buildInstructions(new StringBuilder());
    }

    /**
     * The preferred method of building instructions within the foraging experiment.
     * 
     * Given a StringBuilder, will append the various instructions conditionally relevant
     * to this {@link #RoundConfiguration()}.
     * 
     * For example, if the field of vision is enabled, will append the field of vision instructions,
     * if censored chat is enabled, then it will aadd the censored chat instructions, if the 
     * chat is enabled, will append the chat instructions.
     * 
     * @param instructionsBuilder
     * @return
     */
    public StringBuilder buildInstructions(StringBuilder instructionsBuilder) {
        if (isFirstRound()) {
            instructionsBuilder.append(getGeneralInstructions());
        }
        instructionsBuilder.append(getInstructions());
        if (isFieldOfVisionEnabled()) {
            instructionsBuilder.append("<hr><b>");
            instructionsBuilder.append(getFieldOfVisionInstructions()).append("</b>");
        }
        if (isChatEnabled()) {
            instructionsBuilder.append("<hr><b>");
            instructionsBuilder.append("Before the beginning of this round you will be able to chat with the other members of your group for ").append(getChatDuration()).append(" seconds.</b>");
        }
        if (isCensoredChat()) {
            instructionsBuilder.append("<hr><b>");
            instructionsBuilder.append(getCensoredChatInstructions()).append("</b>");
        }
        if (isQuizEnabled()) {
            instructionsBuilder.append("<hr><b>");
            instructionsBuilder.append(getQuizInstructions()).append("</b>");
        }
        return instructionsBuilder;
    }
}

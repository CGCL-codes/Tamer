package org.rivalry.example.boardgame;

import java.util.Comparator;
import java.util.List;
import org.rivalry.core.datacollector.DefaultDataPostProcessor;
import org.rivalry.core.model.Candidate;
import org.rivalry.core.model.Category;
import org.rivalry.core.model.Criterion;
import org.rivalry.core.model.DefaultCriterion;
import org.rivalry.core.model.RivalryData;

/**
 * Provides a data post processor for boardgames.
 */
public class BoardgameDataPostProcessor extends DefaultDataPostProcessor {

    /**
     * Construct this object.
     */
    public BoardgameDataPostProcessor() {
        super();
    }

    /**
     * Construct this object with the given parameters.
     * 
     * @param candidateComparator Candidate comparator.
     * @param categoryComparator Category comparator.
     * @param criterionComparator Criterion comparator.
     */
    public BoardgameDataPostProcessor(final Comparator<Candidate> candidateComparator, final Comparator<Category> categoryComparator, final Comparator<Criterion> criterionComparator) {
        super(candidateComparator, categoryComparator, criterionComparator);
    }

    @Override
    public void postProcess(final RivalryData rivalryData) {
        convertPlayingTime(rivalryData);
        processNumPlayers(rivalryData);
        super.postProcess(rivalryData);
    }

    /**
     * @param rivalryData Rivalry data.
     */
    private void convertPlayingTime(final RivalryData rivalryData) {
        final String criterionName = "Playing Time";
        final Criterion criterion = rivalryData.findCriterionByName(criterionName);
        final List<Candidate> candidates = rivalryData.getCandidates();
        for (final Candidate candidate : candidates) {
            final Object value = candidate.getValue(criterion);
            if (value instanceof String) {
                String playingTime = (String) value;
                final int index = playingTime.indexOf(" minutes");
                if (index >= 0) {
                    playingTime = playingTime.substring(0, index);
                    final Integer time = parseInt(playingTime);
                    if (time != null) {
                        candidate.putValue(criterion, time);
                    }
                }
            }
        }
    }

    /**
     * 
     * @param rivalryData Rivalry data.
     * @param criterionName Criterion name.
     * 
     * @return a new criterion.
     */
    private Criterion getCriterion(final RivalryData rivalryData, final String criterionName) {
        Criterion answer = rivalryData.findCriterionByName(criterionName);
        if (answer == null) {
            answer = new DefaultCriterion();
            answer.setName(criterionName);
            rivalryData.getCriteria().add(answer);
        }
        return answer;
    }

    /**
     * @param value Value.
     * 
     * @return a new integer parsed from the given parameter.
     */
    private Integer parseInt(final String value) {
        Integer answer = null;
        try {
            answer = Integer.parseInt(value);
        } catch (final NumberFormatException ignore) {
        }
        return answer;
    }

    /**
     * @param rivalryData Rivalry data.
     */
    private void processNumPlayers(final RivalryData rivalryData) {
        final String criterionName = "# of Players";
        final Criterion criterion = rivalryData.findCriterionByName(criterionName);
        final String minCriterionName = "Min Players";
        final Criterion minCriterion = getCriterion(rivalryData, minCriterionName);
        final String maxCriterionName = "Max Players";
        final Criterion maxCriterion = getCriterion(rivalryData, maxCriterionName);
        final List<Candidate> candidates = rivalryData.getCandidates();
        for (final Candidate candidate : candidates) {
            final Object value = candidate.getValue(criterion);
            if (value instanceof String) {
                final String numPlayersString = (String) value;
                final String[] parts = numPlayersString.split(" ");
                if (parts.length == 3) {
                    final String minPlayerString = parts[0];
                    final Integer minPlayer = parseInt(minPlayerString);
                    candidate.putValue(minCriterion, minPlayer);
                    final String maxPlayerString = parts[2];
                    final Integer maxPlayer = parseInt(maxPlayerString);
                    candidate.putValue(maxCriterion, maxPlayer);
                } else {
                    final Integer numPlayers = parseInt(numPlayersString);
                    candidate.putValue(minCriterion, numPlayers);
                    candidate.putValue(maxCriterion, numPlayers);
                }
            } else if (value instanceof Number) {
                candidate.putValue(minCriterion, ((Number) value).intValue());
                candidate.putValue(maxCriterion, ((Number) value).intValue());
            }
            candidate.getValues().remove(criterion);
        }
        rivalryData.getCriteria().remove(criterion);
    }
}

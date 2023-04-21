package org.kommando.core.search.ranking;

import java.util.Set;

/**
 * {@link StringScorer} implementation that gives higher scores for acronym matches:
 * <p>
 * for example <code>FF</code> gives a higher score to <code>FireFox</code>
 * 
 * @author Peter De Bruycker
 */
public class AcronymStringScorer implements StringScorer {

    public float score(String label, String input, Set<Integer> scoredIndexes) {
        return score(0, label, input, scoredIndexes);
    }

    public float score(int offset, String scoreMe, String input, Set<Integer> scoredIndexes) {
        if (input.length() == 0) {
            return 0.95f - countSpaces(scoreMe) * 0.3f;
        }
        if (input.length() > scoreMe.length()) {
            return 0.0f;
        }
        for (int length = input.length(); length > 0; length--) {
            int index = scoreMe.indexOf(input.substring(0, length));
            if (index < 0) {
                index = scoreMe.toLowerCase().indexOf(input.substring(0, length).toLowerCase());
            }
            if (index >= 0) {
                scoredIndexes.add(index + offset);
                int remainingLength = scoreMe.length() - (index + length);
                float remainingScore = score(offset + index + length, scoreMe.substring(index + length), input.substring(length), scoredIndexes);
                if (remainingScore > 0) {
                    float score = index + length;
                    if (index > 0) {
                        if (Character.isWhitespace(scoreMe.charAt(index - 1))) {
                            for (int j = index - 2; j >= 0; j--) {
                                if (Character.isWhitespace(scoreMe.charAt(j))) {
                                    score--;
                                } else {
                                    score -= 0.15;
                                }
                            }
                        }
                        if (Character.isUpperCase(scoreMe.charAt(index))) {
                            for (int j = index - 1; j >= 0; j--) {
                                if (Character.isUpperCase(scoreMe.charAt(j))) {
                                    score--;
                                } else {
                                    score -= 0.15;
                                }
                            }
                        } else {
                            score -= index;
                        }
                    }
                    score += remainingScore * remainingLength;
                    score /= scoreMe.length();
                    return score;
                }
            }
        }
        return 0;
    }

    private int countSpaces(String text) {
        int count = 0;
        for (char c : text.toCharArray()) {
            if (Character.isWhitespace(c)) {
                count++;
            }
        }
        return count;
    }
}

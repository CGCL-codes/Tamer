package net.sourceforge.etsysync.utils.etsy.api.resources;

import java.util.Iterator;
import net.sourceforge.etsysync.utils.ResponsePattern;

public class FeedbackInfo extends EtsyObject {

    private Integer count;

    private Integer score;

    public FeedbackInfo(Integer count, Integer score) {
        this.count = count;
        this.score = score;
    }

    /**
	 * @return The number of feedbacks.
	 */
    public Integer getCount() {
        return count;
    }

    /**
	 * @return The feedback score percentage.
	 */
    public Integer getScore() {
        return score;
    }

    public String toString() {
        return "[count:" + count + "],[score:" + score + "]";
    }

    public boolean equals(Object obj) {
        if (obj != null && this.getClass() == obj.getClass()) {
            FeedbackInfo operand = (FeedbackInfo) obj;
            if ((count.equals(operand.getCount())) && score.equals(operand.getScore())) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public int hashCode() {
        return count.intValue() + score.intValue();
    }

    public static ResponsePattern getPattern() {
        ResponsePattern pattern = new ResponsePattern(FeedbackInfo.class.getSimpleName());
        pattern.add("count");
        pattern.add("score");
        return pattern;
    }

    public static FeedbackInfo create(ResponsePattern pattern) {
        Iterator<ResponsePattern> patternIterator = pattern.iterator();
        Integer count = patternIterator.next().getInteger();
        Integer score = patternIterator.next().getInteger();
        return new FeedbackInfo(count, score);
    }
}

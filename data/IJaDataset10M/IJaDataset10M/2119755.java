package hu.squirreltech.pages;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

public class Guess {

    @Persist
    private int target;

    @Property
    private int guess;

    Object initialize(int target) {
        this.target = target;
        return this;
    }

    @Persist
    @Property
    private String message;

    String onActionFromLink(int guess) {
        if (guess == target) {
            return "GameOver";
        }
        if (guess < target) {
            message = String.format("%d is too low.", guess);
        } else {
            message = String.format("%d is too high.", guess);
        }
        return null;
    }

    public int getTarget() {
        return target;
    }
}

package samples.pages;

import wheel.annotations.ActionMethod;
import wheel.annotations.Persist;
import wheel.components.Form;
import wheel.persistence.Scope;
import java.util.Random;

public class NumberGuessing extends SamplesApp {

    @Persist
    private int correctNumber = -1;

    private int guess;

    @Persist
    private int tries = 1;

    public NumberGuessing() {
    }

    public NumberGuessing(String componentId) {
        super(componentId);
    }

    public void preBuild() {
        if (correctNumber == -1) correctNumber = (new Random()).nextInt(100);
        config().exposeField("guess");
    }

    public void buildComponent() {
        super.buildComponent();
        if (guess == correctNumber) {
            p("Yes, the answer was " + guess + ".");
            p("You guessed it in " + tries + " tries.");
            a("start again").actionBinding("this.reset");
        } else {
            p("Guess a number from 0 to 100");
            if (!isValid("guess")) {
                p("The guess is invalid.");
            } else {
                if (isRewinding()) {
                    tries++;
                    if (guess < correctNumber) p().i("The answer is higher."); else p().i("The answer is lower.");
                }
            }
            form("numberGuessForm").textInput("guess").fieldBinding("guess").validationRule("isInt(value) && value >= 0 && value <= 100").initialFieldValue("give a number").requestFocus().end().br().end().submit("numberGuessSubmit").value("Guess");
        }
        br();
        a("Back").actionBinding("Home");
    }

    @ActionMethod
    public void reset() {
        correctNumber = -1;
        tries = 0;
    }

    public int getCorrectNumber() {
        return correctNumber;
    }

    public void setCorrectNumber(int correctNumber) {
        this.correctNumber = correctNumber;
    }

    public int getGuess() {
        return guess;
    }

    public void setGuess(int guess) {
        this.guess = guess;
    }
}

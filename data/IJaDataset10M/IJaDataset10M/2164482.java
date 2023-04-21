package Gui;

import java.io.Serializable;
import General.Phrase;

public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    public String name;

    public int score;

    Phrase phrase;

    int amount;

    protected Round curround;

    protected IGame game;

    public Player(String name) {
        this.name = name;
        this.score = 0;
    }

    public Player() {
    }

    public void PlayTurn(Phrase phrase, Round round) {
        this.curround = round;
        this.phrase = phrase;
        game = App.getGame();
        game.enableKeys(this);
        game.getInstructions().setText(this.getName() + ", Click on a letter\n to guess");
    }

    public void getLet(char c) {
        int i = getScore() + (phrase.guessLet(c) * amount);
        setScore(i);
        status_panel.println(this.getName() + " guessed the letter " + c);
        status_panel.println(this.getName() + " got " + i + "$");
        game.getPanel_8().initPanelToPhrase(phrase);
        game.getInstructions().setText(this.getName() + ", Click the Guess button\nor end the turn");
    }

    public void getVowel(char c) {
        setScore(getScore() - 50);
        phrase.guessLet(c);
        status_panel.println(this.getName() + " guessed the vowel " + c);
        game.getPanel_8().initPanelToPhrase(phrase);
    }

    public void guessExp(String guess) {
        status_panel.println(this.getName() + " guessed the expression\n " + guess);
        phrase.guessExp(guess);
        if (phrase.Guessed()) status_panel.println("and won the round with 1000$ bonus"); else status_panel.println("and was wrong");
        game.getPanel_8().initPanelToPhrase(phrase);
    }

    public void endTurn() {
        curround.endTurn(this);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int i) {
        this.score = i;
        curround.setScore(score);
    }

    public String getName() {
        return name;
    }

    public void setName(String string) {
        this.name = string;
    }

    public void endRound() {
        game.getPanel_8().initPanelToPhrase(phrase);
    }
}

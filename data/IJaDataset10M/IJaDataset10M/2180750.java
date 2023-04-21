package team5hangman;

/**
 * @author John McCoey
 *
 */
public class HangmanGame {

    protected HangmanWord secretWord;

    protected int count;

    public HangmanGame(HangmanWord word) {
        secretWord = word;
        count = 0;
    }

    public String playGame() {
        String secretWordStr = secretWord.getWord();
        char[] secretWordArray = new char[secretWordStr.length()];
        for (int count = 0; count < secretWordStr.length(); count++) secretWordArray[count] = secretWordStr.charAt(count);
        for (count = 0; count < secretWordStr.length(); count++) System.out.print("_ ");
        System.out.println("");
        System.out.println("\n\nPlayer 2, please enter a letter.");
        return "Win or Lose Result";
    }
}

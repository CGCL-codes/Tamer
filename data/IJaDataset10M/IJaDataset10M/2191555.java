package game.questioner;

public class ValidAnswer {

    private final String anwer;

    ValidAnswer(final String anwer) {
        this.anwer = anwer;
    }

    public String unbox() {
        return anwer;
    }
}

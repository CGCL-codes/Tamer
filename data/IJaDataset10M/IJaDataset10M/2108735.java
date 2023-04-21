package quizgame.protocol.admin;

/**
 * Admin-to-server packet. Sets a new active question on the active game board.
 * @author Dukken
 */
public class SetActiveQuestion implements AdminPacket {

    private int questionIndex;

    private int categoryIndex;

    /** Creates a new instance of SetActiveQuestion */
    public SetActiveQuestion(int categoryIndex, int questionIndex) {
        this.categoryIndex = categoryIndex;
        this.questionIndex = questionIndex;
    }

    public int getCategoryIndex() {
        return categoryIndex;
    }

    public int getQuestionIndex() {
        return questionIndex;
    }
}

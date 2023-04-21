package go;

public class GoResult {

    enum State {

        UNKNOWN("unknown"), UNFINISHED("unfinished"), NO_RESULT("no result"), DRAW("draw"), BLACK_WIN("black win"), WHITE_WIN("white win");

        private String text;

        private State(String str) {
            text = str;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    enum Type {

        UNKNOWN("unknown"), SCORE("score"), RESIGN("resign"), TIME("time"), FORFEIT("forfeit");

        private String text;

        private Type(String str) {
            text = str;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    private State state;

    private Type type;

    private double score;

    public GoResult() {
        state = State.UNKNOWN;
        type = Type.UNKNOWN;
        score = 0.0;
    }

    public static GoResult createDrawResult() {
        GoResult result = new GoResult();
        result.state = State.DRAW;
        return result;
    }

    public static GoResult createScoreResult(GoColor winner, double score) {
        GoResult result = new GoResult();
        if (score == 0.0) {
            return createDrawResult();
        } else {
            if (winner.isBlack()) {
                result.state = State.BLACK_WIN;
            } else {
                result.state = State.WHITE_WIN;
            }
            result.score = score;
            return result;
        }
    }

    public boolean equals(GoResult result) {
        if (state == result.state && type == result.type && score == result.score) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("GoResult:");
        str.append(state.toString());
        str.append(type.toString());
        str.append(score);
        return str.toString();
    }
}

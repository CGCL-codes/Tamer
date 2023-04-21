package parameters;

/**
 *
 * @author juan
 */
public class TopScores {

    private GameRecord[] records;

    private static TopScores instance = new TopScores();

    private TopScores() {
        records = new GameRecord[10];
    }

    public static void addNewRecord(int score) {
        String name = Parameters.getPlayerName();
        instance.addRecord(name, score);
    }

    public static void setRecord(GameRecord gr) {
        if (gr == null) return;
        instance.setPreparedRecord(gr);
    }

    public static GameRecord[] getRecords() {
        return instance.records;
    }

    public static void setClear() {
        instance.initClear();
    }

    private void initClear() {
        for (int i = 0; i < records.length; i++) {
            records[i] = new GameRecord();
            records[i].setPlayer("NADIE");
            records[i].setScore(0);
            records[i].setPosition(i);
        }
    }

    private void setPreparedRecord(GameRecord gr) {
        records[gr.getPosition()] = gr;
    }

    private void addRecord(String name, int score) {
        int i = 0;
        int last = records.length - 1;
        if (score <= records[last].getScore()) return;
        for (i = 0; i < records.length; i++) {
            if (records[i].getScore() < score) {
                break;
            }
        }
        GameRecord rec = new GameRecord();
        rec.setPlayer(name);
        rec.setPosition(i);
        rec.setScore(score);
        for (int j = records.length - 1; j >= i; j--) {
            if (j > 0) {
                records[j] = records[j - 1];
                records[j].setPosition(j);
            }
        }
        records[i] = rec;
    }
}

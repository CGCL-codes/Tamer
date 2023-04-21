package santa.nice.ocr.kernel.recognition;

public class Result implements Comparable<Result> {

    public final char character;

    public final double score;

    public Result(char ch, double sc) {
        character = ch;
        score = sc;
    }

    @Override
    public int compareTo(Result o) {
        if (o.score < score) return -1; else if (o.score > score) return 1; else return 0;
    }
}

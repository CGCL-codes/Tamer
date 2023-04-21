package net.murphyindustries.scorescore;

import org.jfugue.parsers.MusicStringParser;
import org.jfugue.ParserListenerAdapter;
import org.jfugue.Pattern;
import org.jfugue.elements.Note;
import org.jfugue.elements.Time;

/**
 *
 * @author majorm
 */
public class TimeScorer extends ParserListenerAdapter {

    long lengthInMiliseconds = 0;

    @Override
    public void noteEvent(Note note) {
    }

    @Override
    public void timeEvent(Time time) {
        lengthInMiliseconds = time.getTime();
    }

    public long getTimeInMS(Pattern pattern) {
        MusicStringParser parser;
        parser = new MusicStringParser();
        parser.addParserListener(this);
        parser.parse(pattern);
        return lengthInMiliseconds;
    }

    public float scoreTime(Pattern sampledPattern, Pattern targetPattern) {
        long sampledLength = this.getTimeInMS(sampledPattern);
        lengthInMiliseconds = 0;
        long targetLength = this.getTimeInMS(targetPattern);
        System.out.println("Sampled Length: " + sampledLength);
        System.out.println("Target Length: " + targetLength);
        float score = (float) targetLength / (float) sampledLength;
        System.out.println("Time Score: " + score * 100);
        return score;
    }
}

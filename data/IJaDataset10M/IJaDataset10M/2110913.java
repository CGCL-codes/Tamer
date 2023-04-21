package jsubwizard.subtitles.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import jsubwizard.subtitles.Position;
import jsubwizard.subtitles.Subtitle;

/**
 * This class ensures parsing of subtitle records in SubRip format (*.srt) and 
 * returning them as {@link jsubwizard.subtitles.Subtitle}. 
 * @author banhof
 */
public class SubParser {

    public SubParser(String path, float framerate) throws ParsingException {
        try {
            reader = new BufferedReader(new FileReader(path));
            this.framerate = framerate;
        } catch (FileNotFoundException ex) {
            throw new ParsingException("Error while loading.");
        }
    }

    /**
     * Used for getting particular parts of subtitles.
     * @return Instance of {@link jsubwizard.subtitles.Subtitle} or null in the end of file.
     * @throws jsubwizard.subtitles.parser.ParsingException 
     *         with specified message in case of error (bad format, io error etc.) 
     */
    public Subtitle parse() throws ParsingException {
        try {
            String tmp = reader.readLine();
            if (tmp == null || tmp.isEmpty()) {
                return null;
            }
            String[] positions = tmp.split("}", 3);
            int startFrame = new Integer(positions[0].substring(1));
            int endFrame = new Integer(positions[1].substring(1));
            Position start = new Position(startFrame, framerate);
            Position end = new Position(endFrame, framerate);
            tmp = tmp.substring(tmp.lastIndexOf("}") + 1);
            Vector t = new Vector(2);
            while (!tmp.isEmpty()) {
                int p = tmp.indexOf("|");
                if (p == -1) {
                    t.add(tmp);
                    tmp = "";
                } else {
                    t.add(tmp.substring(0, p));
                    tmp = tmp.substring(p + 1, tmp.length());
                }
            }
            String[] text = (String[]) t.toArray(new String[t.size()]);
            return new Subtitle(text, start, end);
        } catch (IOException ex) {
            throw new ParsingException("Error while reading.");
        } catch (NumberFormatException ex) {
            throw new ParsingException("Error while parsing.");
        }
    }

    BufferedReader reader;

    float framerate;
}

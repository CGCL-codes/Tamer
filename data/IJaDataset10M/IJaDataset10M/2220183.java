package br.com.infotec.jbee.core.reader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for reading the file history and return line by line in a list of Strings
 * 
 * @author Carlos Alberto (euprogramador@gmail.com)
 * @since 1.0
 */
public class HistoryReader {

    public List<String> readFile(InputStream in) {
        List<String> story = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                story.add(line);
            }
        } catch (Exception e) {
            throw new RuntimeException("", e);
        }
        return story;
    }
}

package org.sifebint.backend;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * This class parse sql file and return list of sql commands
 * 
 * @author kovar
 *
 */
public class SqlScript {

    private static enum State {

        S, C1, C2
    }

    ;

    public static List<String> parse(InputStream inputStream) throws IOException {
        Reader reader = new InputStreamReader(inputStream);
        State state = State.S;
        List<String> tokens = new ArrayList<String>();
        int symbol;
        String word = new String();
        while ((symbol = reader.read()) != -1) {
            switch(state) {
                case S:
                    if (symbol == '-') {
                        state = State.C1;
                    } else if (symbol == ';') {
                        tokens.add(word);
                        word = new String();
                    } else {
                        word += (char) symbol;
                    }
                    break;
                case C1:
                    if (symbol == '-') {
                        state = State.C2;
                    } else {
                        word += '-';
                        word += (char) symbol;
                        state = State.S;
                    }
                    break;
                case C2:
                    if (symbol == '\n') {
                        state = State.S;
                        word += ' ';
                    }
            }
        }
        if (word.trim().length() > 0) {
            tokens.add(word);
        }
        return tokens;
    }

    public static void main(String[] args) throws IOException {
        List<String> sqlCommands = SqlScript.parse(SqlScript.class.getResourceAsStream("/ddl.sql"));
        for (String c : sqlCommands) {
            System.out.println("\nCommand:");
            System.out.println(c);
        }
        System.out.println("Finished");
    }
}

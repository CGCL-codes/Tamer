package pl.clareo.coroutines.examples;

import static pl.clareo.coroutines.user.Coroutines._;
import static pl.clareo.coroutines.user.Coroutines.yield;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;
import pl.clareo.coroutines.user.CoIterator;
import pl.clareo.coroutines.user.Coroutine;
import pl.clareo.coroutines.user.CoroutineExitException;

public class Grep {

    @Coroutine
    public static CoIterator<String, Void> grep(String pattern, String file) throws IOException {
        FileReader freader = new FileReader(file);
        BufferedReader reader = new BufferedReader(freader);
        String line;
        Pattern regex = Pattern.compile(pattern);
        try {
            while ((line = reader.readLine()) != null) {
                boolean isFound = regex.matcher(line).find();
                if (isFound) {
                    yield(line);
                }
            }
            yield(null);
        } catch (CoroutineExitException e) {
        } finally {
            freader.close();
        }
        return _();
    }

    public static void main(String[] args) {
        String file = args[1];
        String pattern = args[0];
        try {
            for (String line : grep(pattern, file).each()) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

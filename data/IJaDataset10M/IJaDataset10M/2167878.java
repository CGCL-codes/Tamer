package irve.io;

import irve.data.Question;
import irve.data.WordQuestion;
import irve.exceptions.WrongFileException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class WordQuestionsIO {

    public static List<Question> load() throws FileNotFoundException, WrongFileException {
        return load(Config.getQuestionsFileName());
    }

    public static List<Question> load(String f) throws FileNotFoundException, WrongFileException {
        return load(new File(f));
    }

    public static List<Question> load(File f) throws FileNotFoundException, WrongFileException {
        return load(new FileInputStream(f));
    }

    public static List<Question> load(InputStream is) throws WrongFileException {
        try {
            List<Question> questions = new ArrayList<Question>();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                WordQuestion q = new WordQuestion();
                String[] lineArr = line.trim().toLowerCase().split(";");
                q.setId(lineArr[0]);
                for (int i = 1; i < lineArr.length; i++) {
                    String[] altAnswers = lineArr[i].split(",");
                    String[] trimedAltAnswers = new String[altAnswers.length];
                    for (int j = 0; j < altAnswers.length; j++) {
                        trimedAltAnswers[j] = altAnswers[j].trim();
                    }
                    q.getAnswers().add(trimedAltAnswers);
                }
                questions.add(q);
            }
            return questions;
        } catch (Exception e) {
            throw new WrongFileException(e);
        }
    }
}

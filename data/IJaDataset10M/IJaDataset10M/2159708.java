package trivia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import questionary.Questionary;
import myExceptions.MyFileException;

public class NewGame {

    private Questionary q;

    /**
	 * Crea un nuevo juego a partir de un archivo de preguntas f y la dificultad selecionada difficult
	 * @param f
	 * @param difficult
	 * @throws MyFileException
	 */
    public NewGame(File f, String difficult) throws MyFileException {
        try {
            q = new Questionary(new BufferedReader(new FileReader(f)), difficult);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MyFileException e) {
            throw e;
        }
    }

    /**
	 * 
	 * @return devuelve el cuestionario con todas las preguntas cargadas
	 */
    public Questionary getQ() {
        return q;
    }
}

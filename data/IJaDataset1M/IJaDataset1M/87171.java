package mesoutils;

import java.util.InputMismatchException;
import java.util.Scanner;

public class LecteurCodeBarre {

    /**
	 * Lecture d'un code barre (simul�e par une saisie d'entier)
	 * @return le code barre lu (ici un int)
	 * @throws LectureImpossibleException
	 */
    public static int litNumero() throws LectureImpossibleException {
        Scanner lecteur = new Scanner(System.in);
        try {
            System.out.println("\nLecture du code barre...");
            return lecteur.nextInt();
        } catch (InputMismatchException I) {
            throw new LectureImpossibleException();
        }
    }
}

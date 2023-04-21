package m2cci.ex1;

import java.io.File;

public class Utils {

    /**
	 * Retourne un String avec la concaténation des paramètres différents de
	 * null
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
    public String concat(String a, String b) {
        return a + b;
    }

    /**
	 * Retourne true si les deux strings sont egales
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
    public boolean equals(String a, String b) {
        return a.equals(b);
    }

    /**
	 * Retourne true si 'folder' contient des fichiers
	 * 
	 * @param folder
	 * @return
	 */
    public boolean hasChildren(File folder) {
        return folder.listFiles().length > 0;
    }
}

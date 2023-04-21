package net.sf.entDownloader.core;

import java.security.InvalidParameterException;
import java.util.Iterator;

/**
 * Représente un chemin vers un fichier ou un dossier stocké sur l'ENT. Cette
 * classe ne fournit qu'une représentation
 * locale du chemin, et n'assure par conséquent aucun contrôle sur l'existence
 * réelle de la ressource représentée.
 */
public class ENTPath implements Iterable<String> {

    private Stack<String> path = null;

    /**
	 * Construit un nouveau chemin initialisé à la racine.
	 */
    public ENTPath() {
        path = new Stack<String>();
        path.push("/");
    }

    /**
	 * Construit un nouveau chemin à partir de sa représentation textuelle. Le
	 * chemin donné doit être absolu.
	 */
    public ENTPath(String path) {
        this();
        if (!isAbsolute(path)) throw new InvalidParameterException();
        for (String name : path.split("/")) {
            if (name.isEmpty()) {
                continue;
            }
            if (name.equals("..")) {
                if (!this.path.isEmpty()) {
                    this.path.pop();
                }
            } else if (!name.equals(".")) {
                this.path.push(name);
            }
        }
    }

    /**
	 * Copie le chemin donné en paramètre
	 */
    public ENTPath(ENTPath path) {
        this.path = new Stack<String>(path.path);
    }

    /**
	 * Fusionne le chemin donné en paramètre avec le chemin actuel
	 * 
	 * @param path
	 *            Le chemin de déplacement
	 */
    public void goTo(String path) {
        if (isAbsolute(path)) {
            this.path.clear();
            this.path.push("/");
        }
        for (String name : path.split("/")) {
            if (name.isEmpty()) {
                continue;
            }
            if (name.equals("..")) {
                if (!isRoot()) {
                    this.path.pop();
                }
            } else if (!name.equals(".")) {
                this.path.push(name);
            }
        }
    }

    /**
	 * Calcul et retourne le chemin relatif le plus simple correspondant au
	 * chemin absolu donné en paramètre par rapport
	 * au dossier courant actuellement représenté par cette instance.
	 * 
	 * @param destination
	 *            le chemin absolu à relativiser.
	 * @return Le chemin relatif le plus simple permettant d'accéder au dossier
	 *         <i>destination</i> à partir du dossier courant
	 */
    public String getRelative(ENTPath destination) {
        int nbremontes = compareTo(destination), pathLength = destination.path.size();
        String relpath = "";
        if (nbremontes > 0) {
            relpath += "../";
            for (int i = 2; i <= nbremontes; ++i) {
                relpath += "../";
            }
        }
        for (int i = path.size() - nbremontes + 1; i <= pathLength; ++i) {
            relpath += destination.path.get(pathLength - i) + "/";
        }
        if (relpath.isEmpty()) {
            relpath = ".";
        }
        return relpath;
    }

    /**
	 * Compare le chemin donné avec le chemin de l'instance et retourne le
	 * nombre de dossiers devant être remontés pour trouver un dossier commun au
	 * deux chemins.<br>
	 * Exemples :
	 * <ul>
	 * <li>Pour le dossier courant /home/user<i>/musics/playlists/artists</i> et
	 * le chemin /home/user<i>/settings</i>, la méthode retournera 3.</li>
	 * <li>Pour le dossier courant /home/user<i>/settings</i> et le chemin
	 * /home/user<i>/musics/playlists/artists</i>, la méthode retournera 1.</li>
	 * <li>Pour le dossier courant /<i>etc/acpi</i> et le chemin
	 * /<i>home/user/musics/playlists</i>, la méthode retournera 2.</li>
	 * </ul>
	 * La méthode ne retourne donc jamais plus de this.path.size()-1
	 * 
	 * @param absolutePath
	 *            le chemin absolu à comparer
	 * @return Le nombre de dossiers devant être remontés pour trouver un
	 *         dossier commun au deux chemins.
	 */
    private int compareTo(ENTPath absolutePath) {
        int thisPathSize = this.path.size(), absolutePathSize = absolutePath.path.size(), nbdiff = thisPathSize, shorterPath = Math.min(thisPathSize, absolutePathSize);
        for (int i = 1; i <= shorterPath; ++i, --nbdiff) if (!path.get(thisPathSize - i).equals(absolutePath.path.get(absolutePathSize - i))) {
            break;
        }
        return nbdiff;
    }

    /**
	 * Détermine si un chemin est absolu ou relatif.
	 * 
	 * @param path Le chemin à analyser.
	 * @return True si le chemin est absolu, false sinon.
	 */
    public static boolean isAbsolute(String path) {
        return path.charAt(0) == '/';
    }

    /**
	 * Détermine si un chemin est absolu ou relatif.
	 * 
	 * @param path Le chemin à analyser.
	 * @return True si le chemin est absolu, false sinon.
	 */
    public static boolean isAbsolute(String[] path) {
        return path[0].equals("/");
    }

    /**
	 * Détermine le nombre de requêtes nécessaires pour atteindre
	 * le dossier définit par cette instance en partant de la racine (/).
	 * 
	 * @return Le nombre de requêtes nécessaires pour atteindre le dossier en
	 *         partant de la racine (/).
	 */
    public int getNbRequests() {
        return getNbRequests(this);
    }

    /**
	 * Détermine le nombre de requêtes nécessaires pour atteindre
	 * le dossier représenté par <code>path</code> en partant de la racine (/).
	 * 
	 * @param path Le chemin à analyser.
	 * @return Le nombre de requêtes nécessaires pour atteindre le dossier en
	 *         partant de la racine (/).
	 */
    public static int getNbRequests(ENTPath path) {
        return path.path.size();
    }

    /**
	 * Détermine le nombre de requêtes nécessaires pour atteindre
	 * le dossier représenté par <code>path</code> en partant de la racine (/).
	 * 
	 * @param path Le chemin à analyser.
	 * @return Le nombre de requêtes nécessaires pour atteindre le dossier en
	 *         partant de la racine (/).
	 */
    public static int getNbRequests(String path) {
        return getNbRequests(path.split("/"));
    }

    /**
	 * Détermine le nombre de requêtes nécessaires pour atteindre
	 * le dossier représenté par <code>path</code> en partant de la racine (/).
	 * 
	 * @param path Le chemin à analyser.
	 * @return Le nombre de requêtes nécessaires pour atteindre le dossier en
	 *         partant de la racine (/).
	 */
    public static int getNbRequests(String[] path) {
        return path.length;
    }

    /**
	 * Retourne une représentation textuelle du chemin absolu.
	 * 
	 * @return Une représentation textuelle du chemin absolu.
	 */
    @Override
    public String toString() {
        String path = this.path.getLast();
        for (int i = this.path.size() - 2; i >= 0; --i) {
            if (i != this.path.size() - 2) {
                path += "/";
            }
            path += this.path.get(i);
        }
        return path;
    }

    /**
	 * Retourne un itérateur sur les éléments du chemin, en partant de la
	 * racine.
	 * 
	 * @return Un itérateur sur les éléments du chemin, en partant de la racine.
	 */
    @Override
    public Iterator<String> iterator() {
        return path.descendingIterator();
    }

    /**
	 * Retourne un itérateur sur les éléments du chemin, en partant du dossier
	 * courant jusqu'à la racine.
	 * 
	 * @return Un itérateur sur les éléments du chemin, en partant du dossier
	 *         courant jusqu'à la racine.
	 */
    public Iterator<String> descendingIterator() {
        return path.iterator();
    }

    /**
	 * Supprime le chemin actuellement enregistré et retourne à la racine
	 */
    public void clear() {
        goTo("/");
    }

    /**
	 * Retourne le nombre d'éléments constituant ce chemin.
	 * 
	 * @return Le nombre d'éléments constituant ce chemin.
	 */
    public int size() {
        return path.size();
    }

    /**
	 * Retourne vrai si le chemin actuel désigne la racine.
	 * 
	 * @return True si le chemin actuel désigne la racine
	 */
    public boolean isRoot() {
        return size() == 1;
    }

    /**
	 * Retourne le nom du répertoire courant.
	 */
    public String getDirectoryName() {
        return path.peek();
    }
}

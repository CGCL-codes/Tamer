package jsave.action;

import java.io.File;
import jsave.exception.JSaveArretSauvegarde;
import jsave.exception.JSaveException;
import jsave.tree.FileNode;

public class ActionCopierInc extends Action {

    @Override
    public void executer(File s, File d) throws JSaveArretSauvegarde {
        File parentDir = d.getParentFile();
        if (!parentDir.exists()) {
            createParentDirectory(parentDir);
        }
        if (s.isDirectory()) {
            creerDossier(d);
        } else {
            ajouterFichier(s, d);
        }
    }

    @Override
    public String getNom() {
        return "COPIER-INC";
    }

    @Override
    public void executer(String sourcePath, String destinatinoPath, FileNode n) throws JSaveException {
        throw new JSaveException("Methode non implementee");
    }
}

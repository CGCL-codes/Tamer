package algutil.fichier;

import java.io.File;
import algutil.fichier.exception.DossierNExistePasException;

public class VerificationFichiers {

    public static void verifDossierExiste(String pRep) throws DossierNExistePasException {
        verifDossierExiste(new File(pRep));
    }

    public static void verifDossierExiste(File pRep) throws DossierNExistePasException {
        if (!pRep.isDirectory()) {
            throw new DossierNExistePasException("Le dossier '" + pRep.getPath() + "' n'existe pas!");
        }
    }
}

package gphoto.services;

import gphoto.bo.repertoire.Emplacement;
import gphoto.bo.repertoire.Repertoire;
import gphoto.exception.DossierNExistePasException;
import gphoto.vo.PhotoATrierVO;
import java.io.File;
import java.util.List;

public interface ParcoursServices {

    /**
	 * @return Retourne le nombre de photos contenu dans le dossier pNomRep
	 * @throws DossierNExistePasException 
	 */
    public int getNbPhotosDansDossier(String pNomRep, boolean pRecursif) throws DossierNExistePasException;

    /**
	 * @return Retourne le nombre de photos contenu dans le dossier pNomRep
	 * @throws DossierNExistePasException 
	 */
    public int getNbPhotosDansDossier(File pRep, boolean pRecursif) throws DossierNExistePasException;

    /**
	 * @return la liste des photos dans le r�pertoire pass� en param�tre
	 */
    public List<File> getPhotosDansDossier(String nomRep, boolean recursif);

    /**
	 * @return la liste des photos dans le r�pertoire pass� en param�tre
	 * M�thode d'appel
	 */
    public List<File> getPhotosDansDossier(File rep, boolean recursif);

    /**
	 * Retourne la liste des sous r�pertoires de l'emplacement pass� en param�tre.
	 */
    public List<Repertoire> getSousRepertoiresDuRepertoire(Emplacement repertoire);

    /**
	 * Retourne la liste des sous r�pertoires de l'emplacement pass� en param�tre.
	 */
    public List<Repertoire> getSousRepertoiresDuRepertoire(Repertoire repertoire);

    /**
	 * @deprecated
	 */
    public List<PhotoATrierVO> getPhotoATrierVODansRepertoire(Emplacement repertoire);

    public List<PhotoATrierVO> getPhotoATrierVODansRepertoire(String path);

    public void genererVignettes(List<PhotoATrierVO> photoVOs);

    public List<Repertoire> getSousRepertoiresDuRepertoire(String path);

    public int getTaillesPhotosEnMoDansDossier(File rep, boolean pRecursif) throws DossierNExistePasException;

    public int getTaillesPhotosEnMoDansDossier(String nomRep, boolean pRecursif) throws DossierNExistePasException;
}

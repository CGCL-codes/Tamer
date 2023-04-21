package gphoto.services.impl;

import gphoto.bo.Photo;
import gphoto.bo.repertoire.Emplacement;
import gphoto.bo.repertoire.Repertoire;
import gphoto.services.DeplacementServices;
import gphoto.services.EmplacementServices;
import gphoto.vo.PhotoATrierVO;
import java.io.File;
import util.fichier.ActionsFichiers;
import util.fichier.exception.CopieException;
import util.fichier.exception.SuppressionException;

public class DeplacementServicesImpl implements DeplacementServices {

    private static DeplacementServicesImpl deplacementServices = null;

    private DeplacementServicesImpl() {
    }

    public static DeplacementServices getInstance() {
        if (deplacementServices == null) {
            deplacementServices = new DeplacementServicesImpl();
        }
        return deplacementServices;
    }

    /**
	 * D�place une photo dans la corbeille 
	 * @throws SuppressionException 
	 * @throws CopieException 
	 */
    public void deplacerPhotoDansCorbeille(Photo p) throws CopieException, SuppressionException {
    }

    /**
	 * D�place un repertoire dans la corbeille 
	 */
    public void deplacerRepDansCorbeille(Emplacement r) {
    }

    /**
	 * D�place la photo p dans le repertoire r
	 */
    public void deplacerPhotoDansRepertoire(Photo p, Repertoire r) throws CopieException, SuppressionException {
        EmplacementServices repS = EmplacementServicesImpl.getInstance();
        ActionsFichiers.deplacerFichier(p.getFile(), repS.getRepertoireCorbeille().getFile());
    }

    /**
	 * D�place la repertoire source dans le repertoire dest
	 */
    public void deplacerRepDansRepertoire(Emplacement source, Emplacement dest) {
    }

    public void deplacerPhotoATrierDansRepertoire(PhotoATrierVO pVO, Photo pBO) throws CopieException, SuppressionException {
        ActionsFichiers.deplacerFichier(pVO.getFile(), pBO.getFile());
        File vignette = new File(pBO.getFile().getParent() + File.separator + "_v" + File.separator + "v_" + pBO.getFile().getName());
        ActionsFichiers.deplacerFichier(pVO.getFileVignette(), vignette);
    }
}

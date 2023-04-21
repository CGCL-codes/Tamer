package jsave.process;

import java.io.File;
import java.io.IOException;
import java.util.List;
import jsave.conf.Configuration;
import jsave.exception.JSaveArretSauvegarde;
import jsave.exception.JSaveException;
import jsave.services.JSaveServices;
import jsave.tree.FileNode;
import jsave.util.Console;
import algutil.fichier.ActionsFichiers;
import algutil.fichier.exception.SuppressionException;

public class MirrorWithJSaveTreeProcess extends JSaveProcess {

    public MirrorWithJSaveTreeProcess(Configuration conf) {
        super(conf);
    }

    @Override
    public void execute() {
        if (conf.isActif()) {
            conf.getChrono().start();
            FileNode rootNode = conf.getDestinationJSaveTree().getRootNode();
            try {
                isEstimationActivated = conf.isEstimationPreParcoursActivated();
                if (isEstimationActivated) {
                    nbDossiersTotalSurLeNiveau = getNbRepertoire();
                    log.debug("Nb rep jusqu'au niveau " + niveauPreParcours + " : " + nbDossiersTotalSurLeNiveau);
                }
                save(conf.getSource(), rootNode, 1);
            } catch (JSaveArretSauvegarde e) {
                log.info("Arret demande par l'utilisateur...");
            } catch (JSaveException e) {
                log.error(e);
                return;
            }
            Console.logParcours("");
            try {
                JSaveServices.persistJSaveJTree2File(conf.getDestinationJSaveTree(), new File(conf.getDestinationJSaveTreeFile().getParent() + File.separator + ".jsaveTree_" + System.currentTimeMillis()));
                if (conf.getDestinationJSaveTreeFile().getName().endsWith(".zip")) {
                    File jstreeUnZip = new File(conf.getDestinationJSaveTreeFile().getPath().replaceAll(".zip", ""));
                    if (jstreeUnZip.exists() && jstreeUnZip.isFile()) {
                        ActionsFichiers.supprimerFichier(jstreeUnZip);
                    }
                }
            } catch (IOException e) {
                log.error("Erreur lors de la sauvegarde du JTree \n" + e.getMessage(), e);
            } catch (SuppressionException e) {
                log.error("Erreur lors de la suppression du JTree dezippe \n" + e.getMessage(), e);
            }
            Console.logParcours("");
            conf.setNbDossiersParcourus(nbDossiersParcourus);
            conf.setNbFichiersParcourus(nbFichiersParcourus);
            conf.getChrono().stop();
            conf.afficherResume();
            createHistoHiddenFile(conf.getDestination());
            conf.desactiverSauvegarde();
        }
    }

    private void save(File s, FileNode node, int niv) throws JSaveException {
        File[] ts = s.listFiles(fileFilterS);
        File f = null;
        FileNode childNode = null;
        Console.logParcours(isEstimationActivated ? ((int) (((float) nbDossiersParcourusPourEstimation / (float) nbDossiersTotalSurLeNiveau) * 100)) + "% " + s.getPath() : s.getPath());
        for (int i = 0; i < ts.length; i++) {
            f = ts[i];
            childNode = comparaisonDesFichiersDeS(f, node);
            if (f.isDirectory()) {
                nbDossiersParcourus++;
                save(f, childNode, niv + 1);
                if (niv <= niveauPreParcours) {
                    nbDossiersParcourusPourEstimation++;
                }
            } else {
                nbFichiersParcourus++;
            }
        }
        effectuerActionFichiersDeDPasS(s, node);
    }

    private FileNode comparaisonDesFichiersDeS(File f, FileNode n) throws JSaveArretSauvegarde {
        FileNode nd = n.getChildNodeNamed(f.getName());
        File d = null;
        if (nd == null) {
            d = new File(destinationPath + File.separator + n.getRelativePath() + File.separator + f.getName());
            conf.getActionA().executer(f, d);
            nd = new FileNode(d, n);
            n.addChild(nd);
        } else {
            if (!f.isDirectory()) {
                if (f.lastModified() - nd.lastModified() > ecartDateFichierCopie) {
                    conf.getActionC().executer(f, new File(destinationPath + File.separator + nd.getRelativePath()));
                    nd.setLastUpdateDatetime(f.lastModified());
                } else if (nd.lastModified() - f.lastModified() > ecartDateFichierCopie) {
                    conf.getActionD().executer(f, new File(destinationPath + File.separator + nd.getRelativePath()));
                }
            }
        }
        return nd;
    }

    private void effectuerActionFichiersDeDPasS(File s, FileNode n) throws JSaveException {
        File[] sfiles = s.listFiles(fileFilterS);
        List<FileNode> dnodes = n.getChildrenNodes();
        boolean fileFinding;
        for (int i = dnodes.size() - 1; i >= 0; i--) {
            fileFinding = false;
            for (int j = sfiles.length - 1; j >= 0; j--) {
                if (sfiles[j].getName().equals(dnodes.get(i).getName())) {
                    fileFinding = true;
                    break;
                }
            }
            if (!fileFinding) {
                conf.getActionB().executer(sourcePath, destinationPath, dnodes.get(i));
            }
        }
    }
}

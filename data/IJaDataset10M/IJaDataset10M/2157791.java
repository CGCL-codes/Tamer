package fr.megiste.interloc.rtf;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import fr.megiste.interloc.InterlocMain;
import fr.megiste.interloc.data.ModeleLiens;
import fr.megiste.interloc.ihm.DessinateurLiens;
import fr.megiste.interloc.util.InterlocException;
import fr.megiste.interloc.util.Messages;

public abstract class DocumentExporter {

    private static final int LARGEUR_REFERENCE_PAS_A_PAS = 1200;

    private static final int LARGEUR_DESSIN_MAX_PAS_A_PAS = 400;

    public interface AffichageHelper {

        public void afficherMessage(String message, Object[] objects);

        public void afficherProgres(int noEtape);

        public void init(int nbEtapes);

        public void finish();
    }

    protected ArrayList packs = new ArrayList();

    private ImagePack currentImagePack;

    static Logger logger = Logger.getLogger(DocumentExporter.class.getName());

    public static final String SUFFIX_HTML = "html";

    public static BufferedImage scale(BufferedImage bi, double scaleValue) {
        AffineTransform tx = new AffineTransform();
        tx.scale(scaleValue, scaleValue);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage biNew = new BufferedImage((int) (bi.getWidth() * scaleValue), (int) (bi.getHeight() * scaleValue), bi.getType());
        return op.filter(bi, biNew);
    }

    private AffichageHelper affichageHelper;

    protected DessinateurLiens dessinateur;

    protected ModeleLiens modele;

    protected ModeleLiens modeleTmp;

    protected File workingDir = new File("./img.tmp");

    public DocumentExporter(ModeleLiens modele) {
        this.modele = modele;
        if (!workingDir.exists()) {
            workingDir.mkdirs();
        }
        packs = new ArrayList();
    }

    private void dessinerEtape(int noEtape, int nbEtapes, int phraseMin, int phraseMax) {
        logger.info("Pas à pas : " + noEtape);
        String message = "label.pasapas";
        affichageHelper.afficherMessage(message, new Object[] { "" + noEtape });
        affichageHelper.afficherProgres(noEtape);
        newImagePack();
        currentImagePack.setTitle("Etape " + noEtape + " sur " + nbEtapes);
        BufferedImage rendImage = dessinateur.creerImage();
        splitAndInsertImages(rendImage, noEtape);
        currentImagePack.setCommentary(Messages.getInstance().getValue("texte.commentaireetape", new Object[] { "" + noEtape }));
    }

    public void exporterPasAPas(File outputFile, int phraseMin, int phraseMax) throws ErreurExportException {
        if (phraseMax == 0) {
            phraseMax = Integer.MAX_VALUE;
        }
        if (phraseMin == Integer.MAX_VALUE) {
            phraseMin = 0;
        }
        logger.info("Export pas à pas...");
        if (outputFile.exists()) {
            outputFile.delete();
        }
        try {
            modele.setIgnoreModifs(true);
            int nbEtapes = modele.size() - modele.getNbFeuilles();
            affichageHelper.init(nbEtapes);
            modeleTmp = modele;
            dessinateur = new DessinateurLiens(modeleTmp);
            dessinateur.setLargeurDessinMax(LARGEUR_DESSIN_MAX_PAS_A_PAS);
            dessinateur.setLargeurReference(LARGEUR_REFERENCE_PAS_A_PAS);
            dessinateur.redessiner();
            dessinateur.reculer(nbEtapes);
            packs.clear();
            currentImagePack = null;
            initDocument(outputFile);
            for (int i = 0; i < nbEtapes; i++) {
                dessinateur.avancer(1);
                if (modele.getMaxIndexFeuilleAvecParent() > phraseMin && modele.getMaxIndexFeuilleAvecParent() <= phraseMax) {
                    dessinerEtape(i + 1, nbEtapes, phraseMin, phraseMax);
                }
            }
            writeImagePacks();
        } catch (Exception e) {
            InterlocMain.erreur(e);
            throw new ErreurExportException(e.getMessage());
        }
        modele.setIgnoreModifs(false);
        logger.info("Export pp OK...");
        affichageHelper.finish();
    }

    public abstract void writeImagePacks();

    public void exportTotal(File outputFile, int phraseMin, int phraseMax) throws ErreurExportException {
        if (phraseMax == 0) {
            phraseMax = Integer.MAX_VALUE;
        }
        if (phraseMin == Integer.MAX_VALUE) {
            phraseMin = 0;
        }
        logger.info("Export total...");
        if (outputFile.exists()) {
            outputFile.delete();
        }
        try {
            initDocument(outputFile);
            newImagePack();
            currentImagePack.setTitle("Export total");
            modele.setIgnoreModifs(true);
            modeleTmp = modele;
            dessinateur = new DessinateurLiens(modeleTmp);
            dessinateur.setLargeurDessinMax(300);
            dessinateur.setLargeurReference(600);
            dessinateur.reculer(1);
            dessinateur.avancer(1);
            BufferedImage rendImage = dessinateur.creerImage();
            int[] cuts = dessinateur.getCoupuresPossibles();
            int oldpos = 0;
            for (int i = 0; i < cuts.length; i++) {
                if (i < phraseMin || i > phraseMax) {
                    continue;
                }
                int pos = cuts[i];
                int width = rendImage.getWidth();
                int y = oldpos;
                int h = pos - oldpos;
                if (y + h > rendImage.getHeight()) {
                    h = rendImage.getHeight() - y;
                }
                if (h < 0) continue;
                BufferedImage subImage = rendImage.getSubimage(0, y, width, h);
                File tmp = File.createTempFile("img", "jpg");
                ImageIO.write(subImage, "jpg", tmp);
                currentImagePack.getImages().add(tmp);
                oldpos = pos;
            }
            writeImagePacks();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErreurExportException(e.getMessage());
        }
        modele.setIgnoreModifs(true);
        logger.info("Export total OK...");
    }

    private void newImagePack() {
        currentImagePack = new ImagePack();
        packs.add(currentImagePack);
    }

    abstract void initDocument(File outputFile);

    /**
	 * @param affichageHelper
	 *            the affichageHelper to set
	 */
    public void setAffichageHelper(AffichageHelper affichageHelper) {
        this.affichageHelper = affichageHelper;
    }

    private void splitAndInsertImages(BufferedImage rendImage, int noEtape) {
        int[] cuts = dessinateur.getCoupuresPossibles();
        int oldpos = 0;
        for (int i = 0; i < cuts.length; i++) {
            if (i >= modeleTmp.getMaxIndexFeuilleAvecParent()) {
                continue;
            }
            int pos = cuts[i];
            int width = rendImage.getWidth();
            BufferedImage subImage = rendImage.getSubimage(0, oldpos, width, pos - oldpos);
            File tmp;
            try {
                tmp = File.createTempFile("img", "tmp");
                ImageIO.write(subImage, "jpg", tmp);
            } catch (IOException e) {
                throw new InterlocException(e);
            }
            currentImagePack.getImages().add(tmp);
            oldpos = pos;
        }
    }
}

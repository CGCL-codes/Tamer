package appli;

import gphoto.bo.PhotoBean;
import gphoto.services.impl.PhotoServicesImpl;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;
import org.apache.sanselan.ImageReadException;
import algutil.temps.Chronometre;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.MetadataException;

public class FormatPhotoName extends ApplicationLC {

    public static final String VERSION = "1.1.1";

    public static final String VERSION_DATE = "10/10/2011";

    private static final Logger log = Logger.getLogger(FormatPhotoName.class);

    private File rep = null;

    public List<PhotoBean> photos = null;

    private boolean recursif = false;

    public FormatPhotoName(String[] args) throws Exception {
        super();
        creerOptions();
        if (!controlArguments(args)) {
            afficherUsage();
            arretExecution(1);
        }
        Chronometre chrono = new Chronometre();
        chrono.start();
        if (!recursif) {
            process(rep);
        } else {
            processR(rep);
        }
        chrono.stop();
        log.info("Temps de traitement : " + chrono);
        arretExecution(0);
    }

    @Override
    protected void afficherUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("FormatPhotoName", options);
    }

    private void creerOptions() {
        options = new Options();
        options.addOption("r", "recursif", false, "Parcours recursif");
        options.addOption(OptionBuilder.withArgName("file").hasArg().withDescription("use given file for log").create("logfile"));
        Option o = new Option("p", "path", true, "Repertoire a traiter");
        o.setRequired(true);
        options.addOption(o);
    }

    @Override
    protected boolean controlArguments(String[] args) {
        GestionArguments gargs = new GestionArguments(args, 2, 3);
        if (!gargs.hasValifNbArguments()) {
            log.error("Nombre d'argments invalide");
            log.info(gargs);
            return false;
        }
        if (!gargs.containsDirectoryPath()) {
            log.error("Pas de repertoire valide");
            log.info(gargs);
            return false;
        } else {
            rep = gargs.getFirstDirectoryPath();
        }
        if (gargs.containsArgs("-r")) {
            recursif = true;
        }
        return true;
    }

    private void processR(File dir) throws JpegProcessingException, MetadataException, ImageReadException, IOException {
        process(dir);
        File[] sdir = dir.listFiles();
        for (int i = 0; i < sdir.length; i++) {
            if (sdir[i].isDirectory()) {
                processR(sdir[i]);
            }
        }
    }

    private void process(File dir) throws JpegProcessingException, MetadataException, ImageReadException, IOException {
        log.info("%%%%%%%%% RESUME %%%%%%%%%%");
        log.info("Rep : " + dir.getPath());
        if (dir.getName().endsWith("_[npf]")) {
            log.info("Le repertoire est exclu *_[npf]");
            log.info("%%%%%%%%%%% FIN %%%%%%%%%%%");
        }
        photos = PhotoServicesImpl.getInstance().recupererPhotos(dir);
        log.info("%%%%%% RECUPERATION %%%%%%%");
        log.info("Recuperation et tri de " + photos.size() + " photos");
        Collections.sort(photos);
        log.info("%%%%%%%% RENOMMAGE %%%%%%%%");
        int num = 1;
        String lastDate = "";
        PhotoBean photo;
        File generatedFile = null;
        int nbRenommage = 0;
        for (int i = 0; i < photos.size(); i++) {
            photo = photos.get(i);
            log.debug("# photo : " + photos.get(i).getNom());
            if (photo.getDateForDisplay().equalsIgnoreCase(lastDate)) {
                num++;
            } else {
                num = 1;
                lastDate = photo.getDateForDisplay();
            }
            generatedFile = generateFile(photo, num);
            log.debug("# generatedFile : " + generatedFile);
            if (photo.getFile().getPath().equalsIgnoreCase(generatedFile.getPath())) {
                log.debug("OK " + photo.getNom());
            } else {
                log.info(photo.getNom() + " -> " + generatedFile.getName());
                if (!generatedFile.exists()) {
                    photo.renameTo(generatedFile);
                    nbRenommage++;
                } else {
                    for (int j = 0; j < photos.size(); j++) {
                        if (photos.get(j).getNom().equalsIgnoreCase(generatedFile.getName())) {
                            File tmpFile = new File(photos.get(j).getParent() + File.separator + System.currentTimeMillis() + photos.get(j).getNom());
                            photos.get(j).getFile().renameTo(tmpFile);
                            photos.get(j).setFile(tmpFile);
                            log.debug(photos.get(j).getNom() + " -> " + tmpFile.getName());
                            break;
                        }
                    }
                    photo.renameTo(generatedFile);
                    nbRenommage++;
                }
            }
        }
        log.info("Renommage de " + nbRenommage + " photos");
        log.info("%%%%%%%%%%% FIN %%%%%%%%%%%");
    }

    public File generateFile(PhotoBean photo, int num) {
        return new File(photo.getFile().getParent() + File.separator + photo.getDateForDisplay() + "_" + formatNum(num) + ".jpg");
    }

    public int getNum(int pos) {
        int num = 1;
        PhotoBean photo = photos.get(pos);
        for (int i = pos - 1; i >= 0; i--) {
            if (photo.getDateForDisplay().equalsIgnoreCase(photos.get(i).getDateForDisplay())) {
                num++;
            } else {
                break;
            }
        }
        return num;
    }

    public String formatNum(int num) {
        if (num < 10) {
            return "00" + num;
        } else if (num < 100) {
            return "0" + num;
        } else {
            return "" + num;
        }
    }

    public PhotoBean getPhotoByPath(File path) {
        PhotoBean pb = null;
        for (int i = 0; i < photos.size(); i++) {
            if (photos.get(i).getNom().equalsIgnoreCase(path.getName())) {
                pb = photos.get(i);
                break;
            }
        }
        return pb;
    }

    public static void main(String[] args) throws Exception {
        new FormatPhotoName(args);
    }

    @Override
    protected void genererCommandOptions() {
    }
}

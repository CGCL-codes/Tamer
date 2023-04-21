package fr.megiste.interloc.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Logger;
import fr.megiste.interloc.InterlocMain;
import fr.megiste.interloc.hist.FabriqueHistorique;
import fr.megiste.interloc.hist.Historique;

public class AccesseurFichierHOM {

    private static final String LNK = "lnk:";

    private static final String SUFFIX_SAV = "sav";

    public static final String CHARSET = "Cp1252";

    private static final String SURL = "surl:";

    private String charsetLecture = "Cp1252";

    private Vector liens;

    private HashMap mapClefsLiens = new HashMap();

    private int ligneCourante = -1;

    private Vector historique;

    private Vector surlignages = new Vector();

    public static String SUFFIX_HOM = "hom";

    private static Logger logger = InterlocMain.getLogger();

    public Vector getLiensCharges() {
        return liens;
    }

    public AccesseurFichierHOM(String charsetLecture) {
        super();
        this.charsetLecture = charsetLecture;
    }

    private Lien transformerLigneEnLien(String strLine, HashMap mapAffectations) {
        StringTokenizer st = new StringTokenizer(strLine, ";");
        String clef = st.nextToken();
        String desc = st.nextToken();
        Lien sortie = new Lien(clef, desc);
        Vector vClefs = new Vector(0);
        while (st.hasMoreTokens()) {
            vClefs.add(st.nextToken());
        }
        mapAffectations.put(sortie.getClef(), vClefs);
        return sortie;
    }

    public void chargerLiens(File file) throws LectureHOMException {
        liens = new Vector();
        historique = new Vector();
        ligneCourante = 0;
        HashMap mapAffectations = new HashMap();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetLecture));
            String ligne;
            int indice = 1;
            while ((ligne = br.readLine()) != null) {
                ligne = ligne.trim();
                if (ligne.length() == 0) continue;
                if (ligne.startsWith("//") || ligne.startsWith("'") || ligne.startsWith("#")) continue;
                Lien lien = null;
                if (ligne.startsWith(Historique.HIST)) {
                    Historique h = FabriqueHistorique.fabrique(ligne);
                    if (h != null) {
                        historique.add(h);
                    }
                } else if (ligne.startsWith(LNK)) {
                    ligne = ligne.substring(LNK.length());
                    lien = transformerLigneEnLien(ligne, mapAffectations);
                } else if (ligne.startsWith(SURL)) {
                    ligne = ligne.substring(SURL.length());
                    surlignages.add(ligne);
                } else {
                    lien = new Lien(ligne, indice);
                    indice++;
                }
                if (lien != null) {
                    liens.add(lien);
                    mapClefsLiens.put(lien.getClef(), lien);
                }
                ligneCourante++;
            }
            br.close();
        } catch (UnsupportedEncodingException e) {
            throw new LectureHOMException(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new LectureHOMException(e.getMessage());
        } catch (IOException e) {
            InterlocMain.quitter(e);
        }
        for (Iterator iter = liens.iterator(); iter.hasNext(); ) {
            Lien lien = (Lien) iter.next();
            Vector affectations = (Vector) mapAffectations.get(lien.getClef());
            if (affectations != null) {
                for (int i = 0; i < affectations.size(); i++) {
                    String clef = (String) affectations.get(i);
                    Lien enfant = (Lien) mapClefsLiens.get(clef);
                    if (enfant == null) {
                        throw new LectureHOMException("Impossible de trouver le lien de la clef : " + clef);
                    }
                    lien.setFils(i, enfant);
                }
            }
        }
        logger.info("" + liens.size() + " liens charg�s");
    }

    public void setLiensCharges(Vector liensASauver) {
        liens = liensASauver;
    }

    public void enregistrer(File fichierCourant) throws ErreurEcritureException {
        try {
            if (fichierCourant.exists()) {
                File fichierSav = new File(fichierCourant.getParentFile(), fichierCourant.getName().replaceAll("." + SUFFIX_HOM, "." + SUFFIX_SAV));
                if (fichierSav.exists()) fichierSav.delete();
                FileInputStream fis = new FileInputStream(fichierCourant);
                FileOutputStream fos = new FileOutputStream(fichierSav);
                InterlocMain.copyInputStream(fis, fos);
                fichierCourant.delete();
            }
            BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fichierCourant), CHARSET));
            for (Iterator iter = liens.iterator(); iter.hasNext(); ) {
                Lien lien = (Lien) iter.next();
                if (lien.estFeuille()) {
                    fw.write(lien.getTexte() + "\n");
                } else {
                    fw.write(LNK);
                    fw.write(lien.getClef());
                    fw.write(";");
                    fw.write(lien.getTexte());
                    fw.write(";");
                    for (int i = 0; i < lien.getNombreEnfants(); i++) {
                        fw.write(lien.getFils(i).getClef());
                        fw.write(";");
                    }
                    fw.write("\n");
                }
            }
            for (Iterator iter = surlignages.iterator(); iter.hasNext(); ) {
                String clef = (String) iter.next();
                fw.write(SURL);
                fw.write(clef);
                fw.write("\n");
            }
            fw.close();
        } catch (Exception e) {
            throw new ErreurEcritureException("" + e.getClass().getName() + ":" + e.getMessage());
        }
    }

    public Vector getHistoriqueCharge() {
        return historique;
    }

    public void setHistoriqueCharge(Vector historique2) {
        historique = historique2;
    }

    /**
	 * @return the surlignages
	 */
    public Vector getSurlignages() {
        return surlignages;
    }

    /**
	 * @param surlignages the surlignages to set
	 */
    public void setSurlignages(Vector surlignages) {
        this.surlignages = surlignages;
    }
}

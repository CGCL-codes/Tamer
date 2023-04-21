package org.fudaa.dodico.mascaret;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.StringTokenizer;
import java.util.Vector;
import org.fudaa.dodico.corba.mascaret.SResultatsTemporelSpatial;
import org.fudaa.dodico.corba.mascaret.SResultatsTemporelSpatialBief;
import org.fudaa.dodico.corba.mascaret.SResultatsVariable;

/**
 * Classe permettant de lire un fichier r�sultat au format OPTHYCA et de produire
 * un ��SResultatsTemporelSpatial��.
 * @version      $Revision: 1.11 $ $Date: 2007-11-20 11:43:00 $ by $Author: bmarchan $
 * @author       Jean-Marc Lacombe
 */
class Opthyca1DReader {

    private File file_;

    private LineNumberReader read_;

    public Opthyca1DReader(File file) throws FichierMascaretException {
        try {
            file_ = file;
            FileReader fr = new FileReader(file_);
            long taille = file_.length();
            if (taille > 6553600) {
                read_ = new LineNumberReader(fr, 655360);
            } else if (taille > 655360) {
                read_ = new LineNumberReader(fr, 65536);
            } else {
                read_ = new LineNumberReader(fr, 8192);
            }
        } catch (FileNotFoundException ex) {
            throw new FichierMascaretException("", "Fichier introuvable :" + file_.getName());
        }
    }

    public SResultatsTemporelSpatial read() throws FichierMascaretException {
        SResultatsTemporelSpatial results = new SResultatsTemporelSpatial();
        int nbLigne = 0;
        String uneChaine = null;
        try {
            System.out.println("D�but 1ere passe");
            while (!(uneChaine = read_.readLine()).equals("[variables]")) {
                nbLigne++;
            }
            Vector lesVariables = new Vector();
            while (!(uneChaine = read_.readLine()).equals("[resultats]")) {
                nbLigne++;
                StringTokenizer st = new StringTokenizer(uneChaine, ";");
                SResultatsVariable laVariable = new SResultatsVariable();
                laVariable.nomLong = st.nextToken().replace('"', ' ').trim();
                laVariable.nomCourt = st.nextToken().replace('"', ' ').trim();
                laVariable.unite = st.nextToken().replace('"', ' ').trim();
                laVariable.nbDecimal = Integer.parseInt(st.nextToken().replace('"', ' ').trim());
                lesVariables.addElement(laVariable);
            }
            int nbVariable = lesVariables.size();
            results.variables = new SResultatsVariable[nbVariable];
            lesVariables.copyInto(results.variables);
            if (DescriptionVariables.isContientNomsCasier(lesVariables)) {
                results.resultatsCasier = true;
                results.resultatsLiaison = false;
                results.resultatsTracer = false;
                results.resultatsCalageAuto = false;
            } else if (DescriptionVariables.isContientNomsLiaison(lesVariables)) {
                results.resultatsLiaison = true;
                results.resultatsCasier = false;
                results.resultatsTracer = false;
                results.resultatsCalageAuto = false;
            } else if (DescriptionVariables.isContientNomsTracer(lesVariables)) {
                results.resultatsLiaison = false;
                results.resultatsCasier = false;
                results.resultatsTracer = true;
                results.resultatsCalageAuto = false;
            } else if (DescriptionVariables.isContientNomsCalageAuto(lesVariables)) {
                results.resultatsLiaison = false;
                results.resultatsCasier = false;
                results.resultatsTracer = false;
                results.resultatsCalageAuto = true;
            } else {
                results.resultatsLiaison = false;
                results.resultatsCasier = false;
                results.resultatsTracer = false;
                results.resultatsCalageAuto = false;
            }
            lesVariables = null;
            Vector nbSectionParBief = new Vector();
            int nbPasTemps = 0;
            int nbBief = 0;
            boolean resultatSpatial = true;
            double tPre = Double.NEGATIVE_INFINITY;
            double t;
            String codeBiefPre = "";
            String codeBief;
            int compteurPasTps = 0;
            int compteurSection = 0;
            int compteurBief = 0;
            boolean resultatSpatialInit = false;
            while ((uneChaine = read_.readLine()) != null) {
                nbLigne++;
                compteurSection++;
                StringTokenizer st = new StringTokenizer(uneChaine, ";\"");
                t = Double.parseDouble(st.nextToken());
                codeBief = st.nextToken();
                if (!resultatSpatialInit) {
                    resultatSpatial = (st.countTokens() > nbVariable);
                    resultatSpatialInit = true;
                }
                if (tPre != t) {
                    compteurPasTps++;
                    nbSectionParBief.add(new Integer(compteurSection));
                    nbBief = compteurBief;
                    compteurSection = 0;
                    compteurBief = 0;
                    compteurBief++;
                    nbSectionParBief = new Vector();
                } else if (!codeBief.equals(codeBiefPre)) {
                    nbSectionParBief.add(new Integer(compteurSection));
                    compteurBief++;
                    compteurSection = 0;
                }
                tPre = t;
                codeBiefPre = codeBief;
            }
            read_.close();
            compteurSection++;
            nbSectionParBief.add(new Integer(compteurSection));
            nbPasTemps = compteurPasTps;
            nbBief = compteurBief;
            System.out.println("D�but allocation");
            results.pasTemps = new double[nbPasTemps];
            results.resultatsBiefs = new SResultatsTemporelSpatialBief[nbBief];
            for (int i = 0; i < nbBief; i++) {
                results.resultatsBiefs[i] = new SResultatsTemporelSpatialBief();
                int nbSection = ((Integer) nbSectionParBief.get(i)).intValue();
                results.resultatsBiefs[i].abscissesSections = new double[nbSection];
                results.resultatsBiefs[i].valeursVariables = new double[nbVariable][nbPasTemps][nbSection];
            }
            System.out.println("Fin allocation");
            FileReader fr = new FileReader(file_);
            long taille = file_.length();
            if (taille > 6553600) {
                read_ = new LineNumberReader(fr, 655360);
            } else if (taille > 655360) {
                read_ = new LineNumberReader(fr, 65536);
            } else {
                read_ = new LineNumberReader(fr, 8192);
            }
            nbLigne = 0;
            while (!(uneChaine = read_.readLine()).equals("[variables]")) {
                nbLigne++;
            }
            while (!(uneChaine = read_.readLine()).equals("[resultats]")) {
                nbLigne++;
            }
            compteurPasTps = -1;
            compteurSection = -1;
            compteurBief = -1;
            tPre = Double.NEGATIVE_INFINITY;
            double absc;
            codeBiefPre = "";
            while ((uneChaine = read_.readLine()) != null) {
                nbLigne++;
                compteurSection++;
                StringTokenizer st = new StringTokenizer(uneChaine, ";\"");
                t = Double.parseDouble(st.nextToken());
                codeBief = st.nextToken();
                if (tPre != t) {
                    compteurPasTps++;
                    compteurBief++;
                    compteurSection = 0;
                    compteurBief = 0;
                } else if (!codeBief.equals(codeBiefPre)) {
                    compteurBief++;
                    compteurSection = 0;
                }
                if (resultatSpatial) {
                    st.nextToken();
                    absc = Double.parseDouble(st.nextToken());
                } else {
                    absc = 0;
                }
                results.pasTemps[compteurPasTps] = t;
                results.resultatsBiefs[compteurBief].abscissesSections[compteurSection] = absc;
                for (int i = 0; i < nbVariable; i++) {
                    try {
                        double val = Double.parseDouble(st.nextToken());
                        results.resultatsBiefs[compteurBief].valeursVariables[i][compteurPasTps][compteurSection] = val;
                    } catch (NumberFormatException ex) {
                        results.resultatsBiefs[compteurBief].valeursVariables[i][compteurPasTps][compteurSection] = Double.POSITIVE_INFINITY;
                    }
                }
                tPre = t;
                codeBiefPre = codeBief;
            }
            read_.close();
        } catch (NumberFormatException ex1) {
            throw new FichierMascaretException(read_.getLineNumber(), uneChaine, "Format fichier invalide � la ligne n�" + read_.getLineNumber());
        } catch (IOException ex1) {
            throw new FichierMascaretException(read_.getLineNumber(), uneChaine, "Probl�me entr�e-sortie du fichier optyca � la ligne n�" + read_.getLineNumber());
        } catch (Throwable ex1) {
            throw new FichierMascaretException(read_.getLineNumber(), uneChaine, "Probl�me de lecture du fichier optyca car : " + ex1.getLocalizedMessage());
        }
        return results;
    }

    public static void main(String[] args) {
        try {
            String nomFichier = "test3BiefsPetitPermanent.opt";
            if (args.length > 0) {
                nomFichier = args[0];
            }
            File file = new File(nomFichier);
            Opthyca1DReader rr = new Opthyca1DReader(file);
            rr.read();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

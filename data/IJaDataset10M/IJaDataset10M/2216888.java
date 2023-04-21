package org.fudaa.dodico.fichiers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.fudaa.dodico.corba.ef.IElement;
import org.fudaa.dodico.corba.ef.IMaillage;
import org.fudaa.dodico.corba.ef.INoeud;
import org.fudaa.dodico.corba.geometrie.IPoint;
import org.fudaa.dodico.corba.geometrie.LTypeElement;
import org.fudaa.dodico.corba.usine.IUsine;
import org.fudaa.dodico.fortran.FortranReader;
import org.fudaa.dodico.fortran.FortranWriter;
import org.fudaa.dodico.objet.UsineLib;

/**
 * Filtre sur les fichiers maillage .cor/.ele/.bth (format Preflux)
 *
 * @version      $Revision: 1.12 $ $Date: 2004-08-02 08:43:41 $ by $Author: deniger $
 * @author
 */
public final class MaillageCORELEBTH {

    private MaillageCORELEBTH() {
    }

    /**
   * Lecture d'un maillage depuis les fichiers noeuds, bathy et �l�ments de
   * maillage.
   * @param _fichier Nom du fichier maillage. Le maillage est contenu
   *                 dans les _fichier.cor, _fichier.bth et _fichier.ele
   * @exception FileNotFoundException Un fichier de maillage n'est pas trouv�
   * @exception IOException Une erreur de lecture s'est produite
   * @return L'objet maillage
   */
    public static IMaillage lire(File _fichier) throws IOException {
        String racine;
        File file;
        INoeud[] noeuds;
        IElement[] elements;
        String eleExt = ".ele";
        String corExt = ".cor";
        String bthExt = ".bth";
        racine = _fichier.getPath();
        if (racine.endsWith(eleExt) || racine.endsWith(corExt) || racine.endsWith(bthExt)) racine = racine.substring(0, racine.lastIndexOf("."));
        String[] extension = { bthExt, corExt, eleExt };
        for (int i = 0; i < extension.length; i++) {
            file = new File(racine + extension[i]);
            if (!file.exists() || !file.canRead()) throw new FileNotFoundException(racine + extension[i]);
        }
        System.err.println("### Lecture " + racine + corExt);
        noeuds = lireFichierCOR(racine + corExt);
        System.err.println("### Lecture " + racine + bthExt);
        noeuds = lireFichierBTH(racine + bthExt, noeuds);
        System.err.println("### Lecture " + racine + eleExt);
        elements = lireFichierELE(racine + eleExt, noeuds);
        IMaillage res = UsineLib.findUsine().creeEfMaillage();
        res.elements(elements);
        return res;
    }

    /**
   * Enregistrement d'un maillage sur les fichiers noeuds, bathy et �l�ments de
   * maillage.
   * @param _fichier Nom du fichier maillage. Le maillage est contenu
   *                 dans les _fichier.cor, _fichier.bth et _fichier.ele
   * @param _maillage Le maillage � enregistrer
   * @exception FileNotFoundException Un fichier de maillage ne peut �tre ouvert
   * @exception IOException Une erreur d'�criture s'est produite
   */
    public static void enregistrer(IMaillage _maillage, File _fichier) throws IOException {
        String racine;
        File file;
        racine = _fichier.getPath();
        if (racine.endsWith(".ele") || racine.endsWith(".cor") || racine.endsWith(".bth")) racine = racine.substring(0, racine.lastIndexOf("."));
        String[] extension = { ".bth", ".cor", ".ele" };
        for (int i = 0; i < extension.length; i++) {
            file = new File(racine + extension[i]);
            if (file.exists() && !file.canWrite()) throw new FileNotFoundException(racine + extension[i]);
        }
        ecrireFichierCOR(racine + ".cor", _maillage.noeuds());
        ecrireFichierBTH(racine + ".bth", _maillage.noeuds());
        ecrireFichierELE(racine + ".ele", _maillage.elements());
    }

    /**
   * Lecture des informations contenues dans le fichier .bth de nom specifie et
   * stockage dans les objets INoeud
   * @param _nomFichier Nom du fichier a lire
   * @param _noeuds Noeuds d�j� lus dans le fichier .cor des coordonn�es x,y
   * @exception IOException Une erreur de lecture s'est produite
   */
    private static INoeud[] lireFichierBTH(String nomFichier, INoeud[] _noeuds) throws IOException {
        int[] fmt;
        double[] coordonnees;
        int nbFields;
        FortranReader file = null;
        try {
            file = new FortranReader(new FileReader(nomFichier));
            fmt = new int[] { 12, 12, 12, 12, 12, 12 };
            nbFields = 6;
            for (int i = 0; i < _noeuds.length; i++) {
                if (nbFields == 6) {
                    file.readFields(fmt);
                    nbFields = 0;
                }
                coordonnees = _noeuds[i].point().coordonnees();
                coordonnees[2] = file.doubleField(nbFields);
                nbFields++;
            }
        } catch (IOException exc) {
            throw new IOException("Erreur de lecture sur " + nomFichier);
        } catch (NumberFormatException exc) {
            throw new IOException("Erreur de lecture sur " + nomFichier);
        } finally {
            if (file != null) file.close();
        }
        return _noeuds;
    }

    /**
   * Ecriture des z des noeuds sur le fichier .bth de nom specifie
   * @param _nomFichier Nom du fichier � �crire
   * @param _noeuds Noeuds pour lequels �crire le z
   * @exception FileNotFoundException Le fichier ne peut �tre ouvert
   * @exception IOException Une erreur d'�criture s'est produite
   */
    private static void ecrireFichierBTH(String _nomFichier, INoeud[] _noeuds) throws IOException {
        int[] fmt;
        double[] coordonnees;
        int nbFields;
        List lsNoeuds;
        INoeud noeud;
        lsNoeuds = new ArrayList(_noeuds.length);
        for (int i = 0; i < _noeuds.length; i++) lsNoeuds.add(_noeuds[i]);
        Collections.sort(lsNoeuds, new Comparator() {

            public int compare(Object _o1, Object _o2) {
                return ((INoeud) _o1).numero() - ((INoeud) _o2).numero();
            }
        });
        FortranWriter file = null;
        try {
            file = new FortranWriter(new FileWriter(_nomFichier));
            fmt = new int[] { 12, 12, 12, 12, 12, 12 };
            nbFields = 0;
            for (Iterator i = lsNoeuds.iterator(); i.hasNext(); ) {
                noeud = (INoeud) i.next();
                coordonnees = noeud.point().coordonnees();
                file.doubleField(nbFields, coordonnees[2]);
                nbFields++;
                if (nbFields == 6 || !i.hasNext()) {
                    file.writeFields(fmt);
                    nbFields = 0;
                }
            }
        } catch (IOException exc) {
            throw new IOException("Erreur d'�criture sur " + _nomFichier);
        } catch (NumberFormatException exc) {
            throw new IOException("Erreur d'�criture sur " + _nomFichier);
        } finally {
            if (file != null) file.close();
        }
    }

    /**
   * Lecture des informations contenues dans le fichier .cor de nom specifie et
   * stockage dans les objets INoeud
   * @param _nomFichier Nom du fichier a lire
   * @exception FileNotFoundException Le fichier est introuvable
   * @exception IOException Une erreur de lecture s'est produite
   */
    private static INoeud[] lireFichierCOR(String _nomFichier) throws IOException {
        INoeud[] noeuds;
        int[] fmt;
        int nbNoeuds;
        FortranReader file = null;
        try {
            file = new FortranReader(new FileReader(_nomFichier));
            fmt = new int[] {};
            file.readFields();
            String val = file.stringField(0);
            int formatCol1 = val.length();
            System.err.println("### Format colonne 1 COR: " + formatCol1);
            nbNoeuds = file.intField(0);
            noeuds = new INoeud[nbNoeuds];
            fmt = new int[] { formatCol1, 10, 10 };
            IUsine usine = UsineLib.findUsine();
            for (int i = 0; i < nbNoeuds; i++) {
                file.readFields(fmt);
                INoeud no = usine.creeEfNoeud();
                IPoint po = usine.creeGeometriePoint();
                po.coordonnees(new double[] { file.doubleField(1), file.doubleField(2), 0. });
                no.point(po);
                noeuds[i] = no;
                noeuds[i].numero(file.intField(0) - 1);
            }
        } catch (IOException exc) {
            throw new IOException("Erreur de lecture sur " + _nomFichier);
        } catch (NumberFormatException exc) {
            throw new IOException("Erreur de lecture sur " + _nomFichier);
        } finally {
            if (file != null) file.close();
        }
        return noeuds;
    }

    /**
   * Ecriture des x,y des noeuds sur le fichier .cor de nom specifie
   * @param _nomFichier Nom du fichier � �crire
   * @param _noeuds Noeuds pour lequels �crire le x,y
   * @exception FileNotFoundException Le fichier ne peut �tre ouvert
   * @exception IOException Une erreur d'�criture s'est produite
   */
    private static void ecrireFichierCOR(String _nomFichier, INoeud[] _noeuds) throws IOException {
        int[] fmt;
        double[] coordonnees;
        int num;
        INoeud noeud;
        List lsNoeuds;
        lsNoeuds = new ArrayList(_noeuds.length);
        for (int i = 0; i < _noeuds.length; i++) lsNoeuds.add(_noeuds[i]);
        Collections.sort(lsNoeuds, new Comparator() {

            public int compare(Object _o1, Object _o2) {
                return ((INoeud) _o1).numero() - ((INoeud) _o2).numero();
            }
        });
        FortranWriter file = null;
        try {
            file = new FortranWriter(new FileWriter(_nomFichier));
            fmt = new int[] { 10, 5, 5, 10, 10, 10 };
            file.intField(0, _noeuds.length);
            file.intField(1, 6);
            file.intField(2, 2);
            file.doubleField(3, 1.);
            file.doubleField(4, 1.);
            file.doubleField(5, 1.);
            file.writeFields(fmt);
            fmt = new int[] { 10, 10, 10 };
            for (Iterator i = lsNoeuds.iterator(); i.hasNext(); ) {
                noeud = (INoeud) i.next();
                coordonnees = noeud.point().coordonnees();
                num = noeud.numero();
                file.intField(0, num + 1);
                file.doubleField(1, coordonnees[0]);
                file.doubleField(2, coordonnees[1]);
                file.writeFields(fmt);
            }
        } catch (IOException exc) {
            throw new IOException("Erreur d'�criture sur " + _nomFichier);
        } catch (NumberFormatException exc) {
            throw new IOException("Erreur d'�criture sur " + _nomFichier);
        } finally {
            if (file != null) file.close();
        }
    }

    /**
   * Lecture des informations contenues dans le fichier .ele de nom specifie et
   * stockage dans les objets CElement
   * @param _nomFichier Nom du fichier a lire
   * @param _noeuds Noeuds d�j� lus dans le fichier .cor
   * @exception FileNotFoundException Le fichier est introuvable
   * @exception IOException Une erreur de lecture s'est produite
   */
    private static IElement[] lireFichierELE(String _nomFichier, INoeud[] _noeuds) throws IOException {
        IElement[] elements = null;
        INoeud[] connectivite;
        INoeud[] connMax;
        LTypeElement typeElement;
        int[] fmt;
        int nbElements;
        int nbMaxNoeuds;
        int nbNoeuds;
        int numNoeud;
        FortranReader file = null;
        try {
            file = new FortranReader(new FileReader(_nomFichier));
            fmt = new int[] {};
            file.readFields();
            String val = file.stringField(0);
            int formatCol1 = val.length();
            System.err.println("### Format colonne 1 ELE: " + formatCol1);
            nbElements = file.intField(0);
            nbMaxNoeuds = file.intField(1);
            connMax = new INoeud[nbMaxNoeuds];
            elements = new IElement[nbElements];
            fmt = new int[] { formatCol1, formatCol1, formatCol1, formatCol1, formatCol1, formatCol1, formatCol1, formatCol1, formatCol1, formatCol1 };
            IUsine usine = UsineLib.findUsine();
            for (int i = 0; i < nbElements; i++) {
                file.readFields(fmt);
                for (nbNoeuds = 0; nbNoeuds < nbMaxNoeuds; nbNoeuds++) {
                    if ((numNoeud = file.intField(nbNoeuds + 2)) == 0) break;
                    connMax[nbNoeuds] = _noeuds[numNoeud - 1];
                }
                connectivite = new INoeud[nbNoeuds];
                System.arraycopy(connMax, 0, connectivite, 0, nbNoeuds);
                switch(nbNoeuds) {
                    case 3:
                        typeElement = LTypeElement.T3;
                        break;
                    case 6:
                        typeElement = LTypeElement.T6;
                        break;
                    case 4:
                        typeElement = LTypeElement.Q4;
                        break;
                    case 8:
                        typeElement = LTypeElement.Q8;
                        break;
                    default:
                        typeElement = null;
                }
                elements[i] = usine.creeEfElement();
                elements[i].noeuds(connectivite);
                elements[i].type(typeElement);
                elements[i].numero(file.intField(0) - 1);
            }
        } catch (IOException exc) {
            throw new IOException("Erreur de lecture sur " + _nomFichier);
        } catch (NumberFormatException exc) {
            throw new IOException("Erreur de lecture sur " + _nomFichier);
        } catch (Exception exc) {
            System.out.println(exc);
        } finally {
            if (file != null) file.close();
        }
        return elements;
    }

    /**
   * Ecriture des �l�ments sur le fichier .ele de nom specifie
   * @param _nomFichier Nom du fichier � �crire
   * @param _elements El�ments � �crire
   * @exception IOException Une erreur d'�criture s'est produite
   */
    private static void ecrireFichierELE(String _nomFichier, IElement[] _elements) throws IOException {
        INoeud[] noeuds;
        int[] fmt;
        int nbMaxNoeuds;
        FortranWriter file = null;
        try {
            file = new FortranWriter(new FileWriter(_nomFichier));
            nbMaxNoeuds = 0;
            for (int i = 0; i < _elements.length; i++) nbMaxNoeuds = Math.max(nbMaxNoeuds, _elements[i].noeuds().length);
            fmt = new int[] { 10, 5 };
            file.intField(0, _elements.length);
            file.intField(1, nbMaxNoeuds);
            file.writeFields(fmt);
            fmt = new int[] { 10, 25, 10, 10, 10, 10, 10, 10, 10, 10 };
            for (int i = 0; i < _elements.length; i++) {
                file.intField(0, _elements[i].numero() + 1);
                file.stringField(1, "");
                noeuds = _elements[i].noeuds();
                for (int j = 0; j < noeuds.length; j++) {
                    file.intField(j + 2, noeuds[j].numero() + 1);
                }
                file.writeFields(fmt);
            }
        } catch (IOException exc) {
            throw new IOException("Erreur d'�criture sur " + _nomFichier);
        } catch (NumberFormatException exc) {
            throw new IOException("Erreur d'�criture sur " + _nomFichier);
        } catch (Exception exc) {
            System.out.println(exc);
        } finally {
            if (file != null) file.close();
        }
    }
}

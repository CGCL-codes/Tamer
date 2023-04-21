package org.fudaa.dodico.hiswa;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import com.memoire.fu.FuLog;
import org.fudaa.ctulu.fileformat.FortranLib;
import org.fudaa.dodico.corba.hiswa.IResultatsHiswa;
import org.fudaa.dodico.corba.hiswa.IResultatsHiswaOperations;
import org.fudaa.dodico.corba.hiswa.SResultatsHiswaTable;
import org.fudaa.dodico.corba.hiswa.SResultatsLigneHiswaTable;
import org.fudaa.dodico.calcul.DResultats;
import org.fudaa.dodico.fortran.FortranReader;
import org.fudaa.dodico.objet.CDodico;

/**
 * Les resultats Hiswa.
 *
 * @version $Revision: 1.12 $ $Date: 2006-09-19 14:43:23 $ by $Author: deniger $
 * @author Axel Guerrand
 */
public class DResultatsHiswa extends DResultats implements IResultatsHiswa, IResultatsHiswaOperations {

    private SResultatsHiswaTable resultats_;

    private String filenameTable_;

    private boolean fileChangedTable_;

    private static boolean fileSmall_;

    public DResultatsHiswa() {
        super();
    }

    public final Object clone() throws CloneNotSupportedException {
        return new DResultatsHiswa();
    }

    public String toString() {
        return "Fonctionnement des resultats";
    }

    public void nomFichier(final String _base) {
        try {
            if ((_base == null) || (_base.equals(filenameTable_))) {
                return;
            }
            fileChangedTable_ = true;
            filenameTable_ = _base + "ResuTable";
        } catch (final Exception e) {
            CDodico.exceptionAxel(this, e);
        }
    }

    public SResultatsHiswaTable resultatsHiswaTable() {
        try {
            if ((fileChangedTable_) || (resultats_ == null)) {
                resultats_ = litResultatsHiswaTable(filenameTable_);
                fileChangedTable_ = false;
            }
        } catch (final Exception e) {
            CDodico.exceptionAxel(this, e);
        }
        return resultats_;
    }

    public void resultatsHiswaTable(final SResultatsHiswaTable _resultats) {
        resultats_ = _resultats;
    }

    public static SResultatsHiswaTable litResultatsHiswaTable(final String _fichier) throws IOException {
        final String fichier = _fichier + ".dat";
        final SResultatsHiswaTable results = new SResultatsHiswaTable();
        results.lignes = new SResultatsLigneHiswaTable[0];
        final FortranReader fhis = new FortranReader(new BufferedReader(new FileReader(fichier), 262144));
        if (fhis.ready()) {
            boolean hsign = false;
            boolean dir = false;
            boolean per = false;
            boolean dept = false;
            boolean velX = false;
            boolean velY = false;
            boolean forceX = false;
            boolean forceY = false;
            boolean transpX = false;
            boolean transpY = false;
            boolean dirspr = false;
            boolean dissip = false;
            boolean leak = false;
            boolean qb = false;
            boolean xp = false;
            boolean yp = false;
            boolean distan = false;
            boolean ubottom = false;
            boolean steepn = false;
            boolean wlength = false;
            int n = 0;
            int lig = 0;
            int nbData = 0;
            String str;
            System.out.println("D�but de la lecture du fichier r�sultat");
            nbData = nbLig(fichier);
            do {
                fhis.readFields();
                str = fhis.stringField(0);
            } while (!str.equals("Table:"));
            fhis.readFields();
            fhis.readFields();
            fhis.readFields();
            str = fhis.stringField(0);
            while (!str.equals("")) {
                if (str.equals("Hsign")) {
                    hsign = true;
                }
                if (str.equals("Dir")) {
                    dir = true;
                }
                if (str.equals("Per")) {
                    per = true;
                }
                if (str.equals("Dept")) {
                    dept = true;
                }
                if (str.equals("Ux")) {
                    velX = true;
                }
                if (str.equals("Uy")) {
                    velY = true;
                }
                if (str.equals("FRx")) {
                    forceX = true;
                }
                if (str.equals("FRy")) {
                    forceY = true;
                }
                if (str.equals("ENx")) {
                    transpX = true;
                }
                if (str.equals("ENy")) {
                    transpY = true;
                }
                if (str.equals("DirSpr")) {
                    dirspr = true;
                }
                if (str.equals("Dissip")) {
                    dissip = true;
                }
                if (str.equals("Leak")) {
                    leak = true;
                }
                if (str.equals("Qb")) {
                    qb = true;
                }
                if (str.equals("Xp")) {
                    xp = true;
                }
                if (str.equals("Yp")) {
                    yp = true;
                }
                if (str.equals("Distan")) {
                    distan = true;
                }
                if (str.equals("Ubottom")) {
                    ubottom = true;
                }
                if (str.equals("Steepn")) {
                    steepn = true;
                }
                if (str.equals("WLength")) {
                    wlength = true;
                }
                n++;
                str = fhis.stringField(n);
            }
            fhis.readFields();
            fhis.readFields();
            fhis.readFields();
            fhis.readFields();
            fhis.readFields();
            results.lignes = new SResultatsLigneHiswaTable[nbData];
            try {
                lig = 0;
                while (!fhis.getLine().equals("")) {
                    n = 0;
                    results.lignes[lig] = new SResultatsLigneHiswaTable();
                    if (hsign) {
                        results.lignes[lig].hsign = fhis.doubleField(n);
                        n++;
                    }
                    if (dir) {
                        results.lignes[lig].dir = fhis.doubleField(n);
                        n++;
                    }
                    if (per) {
                        results.lignes[lig].period = fhis.doubleField(n);
                        n++;
                    }
                    if (dept) {
                        results.lignes[lig].depth = fhis.doubleField(n);
                        n++;
                    }
                    if (velX) {
                        results.lignes[lig].velX = fhis.doubleField(n);
                        n++;
                    }
                    if (velY) {
                        results.lignes[lig].velY = fhis.doubleField(n);
                        n++;
                    }
                    if (forceX) {
                        results.lignes[lig].forceX = fhis.doubleField(n);
                        n++;
                    }
                    if (forceY) {
                        results.lignes[lig].forceY = fhis.doubleField(n);
                        n++;
                    }
                    if (transpX) {
                        results.lignes[lig].transpX = fhis.doubleField(n);
                        n++;
                    }
                    if (transpY) {
                        results.lignes[lig].transpY = fhis.doubleField(n);
                        n++;
                    }
                    if (dirspr) {
                        results.lignes[lig].dspr = fhis.doubleField(n);
                        n++;
                    }
                    if (dissip) {
                        results.lignes[lig].dissip = fhis.doubleField(n);
                        n++;
                    }
                    if (leak) {
                        results.lignes[lig].leak = fhis.doubleField(n);
                        n++;
                    }
                    if (qb) {
                        results.lignes[lig].qb = fhis.doubleField(n);
                        n++;
                    }
                    if (xp) {
                        results.lignes[lig].xp = fhis.doubleField(n);
                        n++;
                    }
                    if (yp) {
                        results.lignes[lig].yp = fhis.doubleField(n);
                        n++;
                    }
                    if (distan) {
                        results.lignes[lig].dist = fhis.doubleField(n);
                        n++;
                    }
                    if (ubottom) {
                        results.lignes[lig].ubot = fhis.doubleField(n);
                        n++;
                    }
                    if (steepn) {
                        results.lignes[lig].steepn = fhis.doubleField(n);
                        n++;
                    }
                    if (wlength) {
                        results.lignes[lig].wlength = fhis.doubleField(n);
                        n++;
                    }
                    fhis.readFields();
                    lig++;
                }
                if (!fileSmall_) {
                    hsign = false;
                    dir = false;
                    per = false;
                    dept = false;
                    velX = false;
                    velY = false;
                    forceX = false;
                    forceY = false;
                    transpX = false;
                    transpY = false;
                    dirspr = false;
                    dissip = false;
                    leak = false;
                    qb = false;
                    xp = false;
                    yp = false;
                    distan = false;
                    ubottom = false;
                    steepn = false;
                    wlength = false;
                    n = 0;
                    fhis.readFields();
                    fhis.readFields();
                    str = fhis.stringField(0);
                    while (!str.equals("")) {
                        if (str.equals("Hsign")) {
                            hsign = true;
                        }
                        if (str.equals("Dir")) {
                            dir = true;
                        }
                        if (str.equals("Per")) {
                            per = true;
                        }
                        if (str.equals("Dept")) {
                            dept = true;
                        }
                        if (str.equals("Ux")) {
                            velX = true;
                        }
                        if (str.equals("Uy")) {
                            velY = true;
                        }
                        if (str.equals("FRx")) {
                            forceX = true;
                        }
                        if (str.equals("FRy")) {
                            forceY = true;
                        }
                        if (str.equals("ENx")) {
                            transpX = true;
                        }
                        if (str.equals("ENy")) {
                            transpY = true;
                        }
                        if (str.equals("DirSpr")) {
                            dirspr = true;
                        }
                        if (str.equals("Dissip")) {
                            dissip = true;
                        }
                        if (str.equals("Leak")) {
                            leak = true;
                        }
                        if (str.equals("Qb")) {
                            qb = true;
                        }
                        if (str.equals("Xp")) {
                            xp = true;
                        }
                        if (str.equals("Yp")) {
                            yp = true;
                        }
                        if (str.equals("Distan")) {
                            distan = true;
                        }
                        if (str.equals("Ubottom")) {
                            ubottom = true;
                        }
                        if (str.equals("Steepn")) {
                            steepn = true;
                        }
                        if (str.equals("WLength")) {
                            wlength = true;
                        }
                        n++;
                        str = fhis.stringField(n);
                    }
                    fhis.readFields();
                    fhis.readFields();
                    fhis.readFields();
                    fhis.readFields();
                    fhis.readFields();
                    lig = 0;
                    while (!fhis.getLine().equals("")) {
                        n = 0;
                        if (hsign) {
                            results.lignes[lig].hsign = fhis.doubleField(n);
                            n++;
                        }
                        if (dir) {
                            results.lignes[lig].dir = fhis.doubleField(n);
                            n++;
                        }
                        if (per) {
                            results.lignes[lig].period = fhis.doubleField(n);
                            n++;
                        }
                        if (dept) {
                            results.lignes[lig].depth = fhis.doubleField(n);
                            n++;
                        }
                        if (velX) {
                            results.lignes[lig].velX = fhis.doubleField(n);
                            n++;
                        }
                        if (velY) {
                            results.lignes[lig].velY = fhis.doubleField(n);
                            n++;
                        }
                        if (forceX) {
                            results.lignes[lig].forceX = fhis.doubleField(n);
                            n++;
                        }
                        if (forceY) {
                            results.lignes[lig].forceY = fhis.doubleField(n);
                            n++;
                        }
                        if (transpX) {
                            results.lignes[lig].transpX = fhis.doubleField(n);
                            n++;
                        }
                        if (transpY) {
                            results.lignes[lig].transpY = fhis.doubleField(n);
                            n++;
                        }
                        if (dirspr) {
                            results.lignes[lig].dspr = fhis.doubleField(n);
                            n++;
                        }
                        if (dissip) {
                            results.lignes[lig].dissip = fhis.doubleField(n);
                            n++;
                        }
                        if (leak) {
                            results.lignes[lig].leak = fhis.doubleField(n);
                            n++;
                        }
                        if (qb) {
                            results.lignes[lig].qb = fhis.doubleField(n);
                            n++;
                        }
                        if (xp) {
                            results.lignes[lig].xp = fhis.doubleField(n);
                            n++;
                        }
                        if (yp) {
                            results.lignes[lig].yp = fhis.doubleField(n);
                            n++;
                        }
                        if (distan) {
                            results.lignes[lig].dist = fhis.doubleField(n);
                            n++;
                        }
                        if (ubottom) {
                            results.lignes[lig].ubot = fhis.doubleField(n);
                            n++;
                        }
                        if (steepn) {
                            results.lignes[lig].steepn = fhis.doubleField(n);
                            n++;
                        }
                        if (wlength) {
                            results.lignes[lig].wlength = fhis.doubleField(n);
                            n++;
                        }
                        fhis.readFields();
                        lig++;
                    }
                }
            } catch (final EOFException err) {
            }
        } else {
            System.out.println("Le fichier " + fichier + ".dat n'existe pas");
        }
        System.out.println("Fin de la lecture du fichier resultat");
        fhis.close();
        return results;
    }

    public static int nbLig(final String _filename) {
        int ndeb = 0;
        int n = 0;
        FortranReader ftot = null;
        try {
            ftot = new FortranReader(new FileReader(_filename));
            String str;
            do {
                ftot.readFields();
                str = ftot.stringField(0);
            } while (!str.equals("Table:"));
            for (int i = 0; i < 8; i++) {
                ftot.readFields();
            }
            ndeb = ftot.getLineNumber();
            try {
                while (!ftot.getLine().equals("")) {
                    ftot.readFields();
                }
                n = (ftot.getLineNumber() - ndeb);
            } catch (final EOFException err) {
                fileSmall_ = true;
                n = (ftot.getLineNumber() - ndeb + 1);
            }
        } catch (final FileNotFoundException _evt) {
            FuLog.error(_evt);
        } catch (final IOException _evt) {
            FuLog.error(_evt);
        } finally {
            FortranLib.close(ftot);
        }
        return n;
    }
}

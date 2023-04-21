package jproteinsurface;

/**
 *
 * @author Héctor Valverde Pareja
 * Universidad de Málaga
 *
 */
public class ASGraphConstruct {

    private int memory = 100000;

    public int nvt = 0;

    public int ncircle = 0;

    public int ncycles = 0;

    public vertex[] vertices = new vertex[memory];

    public circle[] circles = new circle[memory];

    public cycle[] cycles = new cycle[50];

    private char[] veflag;

    /**
     * Store preliminar parameters of contacting atoms.
     * @return true if succesfully
     */
    public static boolean overlappsAtoms() {
        int contador = 0;
        for (int i = 0; i < Main.atomos.length; i++) {
            for (int j = 0; j < Main.atomos[i].contactAtoms.length; j++) {
                contador++;
                double v1[] = { Main.atomos[i].x, Main.atomos[i].y, Main.atomos[i].z };
                double v2[] = { Main.atomos[Main.atomos[i].contactAtoms[j].contactAtomID].x, Main.atomos[Main.atomos[i].contactAtoms[j].contactAtomID].y, Main.atomos[Main.atomos[i].contactAtoms[j].contactAtomID].z };
                double dist2 = Math.sqrt(Vector.dist2(v2, v1));
                double[] diff = Vector.vdiff(v2, v1);
                double[] torus = Vector.vtimk(diff, 1. / dist2);
                Main.atomos[i].contactAtoms[j].dist2ij = dist2;
                Main.atomos[i].contactAtoms[j].uconij[0] = torus[0];
                Main.atomos[i].contactAtoms[j].uconij[1] = torus[1];
                Main.atomos[i].contactAtoms[j].uconij[2] = torus[2];
                double c = (Main.atomos[i].RADIO * Main.atomos[i].RADIO - Main.atomos[Main.atomos[i].contactAtoms[j].contactAtomID].RADIO * Main.atomos[Main.atomos[i].contactAtoms[j].contactAtomID].RADIO) / dist2;
                Main.atomos[i].contactAtoms[j].tconij[0] = 0.5 * (Main.atomos[i].x + Main.atomos[Main.atomos[i].contactAtoms[j].contactAtomID].x + torus[0] * c);
                Main.atomos[i].contactAtoms[j].tconij[1] = 0.5 * (Main.atomos[i].y + Main.atomos[Main.atomos[i].contactAtoms[j].contactAtomID].y + torus[1] * c);
                Main.atomos[i].contactAtoms[j].tconij[2] = 0.5 * (Main.atomos[i].z + Main.atomos[Main.atomos[i].contactAtoms[j].contactAtomID].z + torus[2] * c);
            }
        }
        return (true);
    }

    /**
     * Loading vertices and circles. This function fetch vertices those are not buried
     * and circles that are not intersected by other atoms and not buried
     * @return true if success
     */
    public boolean fetchVertexCircles() {
        jproteinsurface.Atom atom1;
        jproteinsurface.Atom atom2;
        jproteinsurface.Atom atom3;
        int countdebug = 1;
        int countdebug2 = 1;
        for (int ai = 0; ai < Main.atomos.length; ai++) {
            atom1 = Main.atomos[ai];
            for (int aj = 0; aj < atom1.contactAtoms.length; aj++) {
                atom2 = Main.atomos[Main.atomos[ai].contactAtoms[aj].contactAtomID];
                if (atom1.atomID < atom2.atomID) {
                    int nvk = 0;
                    for (int ak = 0; ak < atom2.contactAtoms.length; ak++) {
                        atom3 = Main.atomos[atom2.contactAtoms[ak].contactAtomID];
                        if (atom3.atomID > atom2.atomID && ASGraphConstruct.comparaContacto(atom3.atomID, atom1.contactAtoms)) {
                            double cba = Vector.scalarprod(atom1.contactAtoms[aj].uconij, atom2.contactAtoms[ak].uconij);
                            if (cba < 1 && cba > -1) {
                                double[] vectorprod = Vector.vectorprod(atom1.contactAtoms[aj].uconij, atom2.contactAtoms[ak].uconij);
                                double sba = Math.sqrt(1. - cba * cba);
                                double[] vtimk = Vector.vtimk(vectorprod, 1. / sba);
                                double[] utb = Vector.vectorprod(vtimk, atom1.contactAtoms[aj].uconij);
                                double[] bijk = Vector.vdiff(atom2.contactAtoms[ak].tconij, atom1.contactAtoms[aj].tconij);
                                double[] nutb = Vector.vtimk(utb, Vector.scalarprod(atom2.contactAtoms[ak].uconij, bijk) / sba);
                                double[] nbijk = Vector.vsum(atom1.contactAtoms[aj].tconij, nutb);
                                double[] coordai = { atom1.x, atom1.y, atom1.z };
                                double[] diff = Vector.vdiff(nbijk, coordai);
                                double ph2 = atom1.RADIO * atom1.RADIO - Vector.scalarprod(diff, diff);
                                if (ph2 > 0) {
                                    nvk = 1;
                                    double[] nvtimk = Vector.vtimk(vtimk, Math.sqrt(ph2));
                                    double[] ve1 = Vector.vsum(nbijk, nvtimk);
                                    double[] ve2 = Vector.vdiff(nbijk, nvtimk);
                                    if (atom1.contactAtoms.length > 2) {
                                        boolean bur1 = false;
                                        boolean bur2 = false;
                                        for (int l = 0; l < atom1.contactAtoms.length; l++) {
                                            Atom atomL = Main.atomos[atom1.contactAtoms[l].contactAtomID];
                                            if (atomL.atomID != atom2.atomID && atomL.atomID != atom3.atomID) {
                                                double[] coordAtomL = { (double) atomL.x, (double) atomL.y, (double) atomL.z };
                                                if (Vector.dis2(ve1, coordAtomL) < atomL.RADIO * atomL.RADIO) {
                                                    bur1 = true;
                                                    break;
                                                }
                                            }
                                        }
                                        for (int l = 0; l < atom1.contactAtoms.length; l++) {
                                            Atom atomL = Main.atomos[atom1.contactAtoms[l].contactAtomID];
                                            if (atomL.atomID != atom2.atomID && atomL.atomID != atom3.atomID) {
                                                double[] coordAtomL = { (double) atomL.x, (double) atomL.y, (double) atomL.z };
                                                if (Vector.dis2(ve2, coordAtomL) < atomL.RADIO * atomL.RADIO) {
                                                    bur2 = true;
                                                    break;
                                                }
                                            }
                                        }
                                        if (!bur1) {
                                            vertices[nvt] = new ASGraphConstruct.vertex();
                                            vertices[nvt].atomsinContact[0] = atom1.atomID;
                                            vertices[nvt].atomsinContact[1] = atom2.atomID;
                                            vertices[nvt].atomsinContact[2] = atom3.atomID;
                                            vertices[nvt].x = ve1[0];
                                            vertices[nvt].y = ve1[1];
                                            vertices[nvt].z = ve1[2];
                                            nvt++;
                                        }
                                        if (!bur2) {
                                            vertices[nvt] = new ASGraphConstruct.vertex();
                                            vertices[nvt].atomsinContact[0] = atom1.atomID;
                                            vertices[nvt].atomsinContact[1] = atom2.atomID;
                                            vertices[nvt].atomsinContact[2] = atom3.atomID;
                                            vertices[nvt].x = ve2[0];
                                            vertices[nvt].y = ve2[1];
                                            vertices[nvt].z = ve2[2];
                                            nvt++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (nvk == 0) {
                        if (atom1.contactAtoms.length > 1) {
                            for (int ah = 0; ah < aj; ah++) {
                                Atom atomAh = Main.atomos[atom1.contactAtoms[ah].contactAtomID];
                                double cbaAh = Vector.scalarprod(atom1.contactAtoms[ah].uconij, atom1.contactAtoms[aj].uconij);
                                if (cbaAh < 1 && cbaAh > -1) {
                                    double[] vectorprod = Vector.vectorprod(atom1.contactAtoms[aj].uconij, atom1.contactAtoms[ah].uconij);
                                    double sba = Math.sqrt(1. - cbaAh * cbaAh);
                                    double[] vtimk = Vector.vtimk(vectorprod, 1. / sba);
                                    double[] utb = Vector.vectorprod(vtimk, atom1.contactAtoms[aj].uconij);
                                    double[] bijk = Vector.vdiff(atom1.contactAtoms[ah].tconij, atom1.contactAtoms[aj].tconij);
                                    double[] nutb = Vector.vtimk(utb, Vector.scalarprod(atom1.contactAtoms[ah].uconij, bijk) / sba);
                                    double[] nbijk = Vector.vsum(atom1.contactAtoms[aj].tconij, nutb);
                                    double[] coordai = { atom1.x, atom1.y, atom1.z };
                                    double[] diff = Vector.vdiff(nbijk, coordai);
                                    double ph2 = atom1.RADIO * atom1.RADIO - Vector.scalarprod(diff, diff);
                                    if (ph2 > 0) {
                                        nvk = 1;
                                    }
                                }
                            }
                        }
                    }
                    if (nvk == 0) {
                        boolean cbur = false;
                        double[] diff = { 0., 0., 0. };
                        if (atom1.contactAtoms[aj].uconij[0] != 0. || atom1.contactAtoms[aj].uconij[1] != 0.) {
                            double c = Math.sqrt(atom1.contactAtoms[aj].uconij[0] * atom1.contactAtoms[aj].uconij[0] + atom1.contactAtoms[aj].uconij[1] * atom1.contactAtoms[aj].uconij[1]);
                            diff[0] = -atom1.contactAtoms[aj].uconij[1] / c;
                            diff[1] = atom1.contactAtoms[aj].uconij[0] / c;
                            diff[2] = 0.;
                        } else {
                            double c = Math.sqrt(atom1.contactAtoms[aj].uconij[0] * atom1.contactAtoms[aj].uconij[0] + atom1.contactAtoms[aj].uconij[2] * atom1.contactAtoms[aj].uconij[2]);
                            diff[0] = -atom1.contactAtoms[aj].uconij[2] / c;
                            diff[1] = 0.;
                            diff[2] = atom1.contactAtoms[aj].uconij[0] / c;
                        }
                        double[] re = Vector.vsum(atom1.contactAtoms[aj].tconij, diff);
                        for (int l = 0; l < atom1.contactAtoms.length; l++) {
                            Atom atomL = Main.atomos[atom1.contactAtoms[l].contactAtomID];
                            if (atomL.atomID != atom2.atomID) {
                                double[] coordAtomL = { (double) atomL.x, (double) atomL.y, (double) atomL.z };
                                if (Vector.dis2(re, coordAtomL) < atomL.RADIO * atomL.RADIO) {
                                    cbur = true;
                                    break;
                                }
                            }
                        }
                        if (!cbur) {
                            circles[ncircle] = new circle();
                            circles[ncircle].atomsinContact[0] = atom1.atomID;
                            circles[ncircle].atomsinContact[1] = atom2.atomID;
                            circles[ncircle].x = re[0];
                            circles[ncircle].y = re[1];
                            circles[ncircle].z = re[2];
                            ncircle++;
                        }
                    }
                }
            }
        }
        return (true);
    }

    /**
     * When de circles and vertices are stored, reserve the memory for veflag
     * @return
     */
    public boolean setveflag() {
        veflag = new char[nvt + ncircle];
        return (true);
    }

    /**
     * Fetch all cycles formed by vertices and circles
     * @return true is success
     */
    private int fetchCycles(int natom) {
        int[] verte = new int[50];
        double[] v00 = new double[3];
        int i, ii, j, nve, icycle, v0, v1, comatom, p, cycmin, pflag;
        for (i = 0; i <= 49; i++) {
            verte[i] = -1;
        }
        nve = 0;
        for (i = 0; i <= nvt - 1; i++) {
            if (vertices[i].atomsinContact[0] > natom) {
                break;
            }
            if (vertices[i].atomsinContact[0] == natom || vertices[i].atomsinContact[1] == natom || vertices[i].atomsinContact[2] == natom) {
                verte[nve] = i;
                nve++;
            }
        }
        icycle = 0;
        for (i = 0; i <= ncircle - 1; i++) {
            if (circles[i].atomsinContact[0] > natom) {
                break;
            }
            if (circles[i].atomsinContact[0] == natom || circles[i].atomsinContact[1] == natom) {
                cycles[icycle].a[0] = -2;
                cycles[icycle].a[1] = i;
                cycles[icycle].nvincyc = 1;
                if (circles[i].atomsinContact[0] != natom) {
                    cycles[icycle].common[0] = circles[i].atomsinContact[0];
                } else {
                    cycles[icycle].common[0] = circles[i].atomsinContact[1];
                }
                icycle++;
            }
        }
        while (true) {
            for (i = 0; i <= nve - 1; i++) {
                if (verte[i] != -1) {
                    break;
                }
            }
            if (i == nve) {
                break;
            }
            v0 = i;
            v00[0] = vertices[verte[v0]].x;
            v00[1] = vertices[verte[v0]].y;
            v00[2] = vertices[verte[v0]].z;
            v1 = v0;
            j = 0;
            cycles[icycle] = new ASGraphConstruct.cycle();
            cycles[icycle].nvincyc = 0;
            do {
                cycles[icycle].nvincyc++;
                v0 = v1;
                cycles[icycle].a[j] = verte[v1];
                int[] pot = this.potoedge(natom, v0, v1, verte, nve);
                v1 = pot[1];
                comatom = pot[0];
                cycles[icycle].common[j] = comatom;
                j++;
            } while (Math.abs(vertices[verte[v1]].x - v00[0]) >= 1.e-6 || Math.abs(vertices[verte[v1]].y - v00[1]) >= 1.e-6 || Math.abs(vertices[verte[v1]].z - v00[2]) >= 1.e-6);
            for (p = 0; p <= j - 1; p++) for (ii = 0; ii <= nve - 1; ii++) {
                if (verte[ii] != -1 && cycles[icycle].a[p] == verte[ii]) verte[ii] = -1;
            }
            for (p = 0; p <= -1; p++) {
                cycmin = 0;
                pflag = 0;
                for (ii = 0; ii <= nve - 1; ii++) if (verte[ii] != -1) if (cycles[icycle].a[p] != verte[ii]) if (Math.abs(vertices[cycles[icycle].a[p]].x - vertices[verte[ii]].x) < 1.e-6 && Math.abs(vertices[cycles[icycle].a[p]].y - vertices[verte[ii]].y) < 1.e-6 && Math.abs(vertices[cycles[icycle].a[p]].z - vertices[verte[ii]].z) < 1.e-6) {
                    if (p != 0) {
                        if (((verte[ii] < cycmin && pflag == 1) || (pflag == 0)) && (vertices[verte[ii]].atomsinContact[0] == cycles[icycle].common[p - 1] || vertices[verte[ii]].atomsinContact[1] == cycles[icycle].common[p - 1] || vertices[verte[ii]].atomsinContact[2] == cycles[icycle].common[p - 1])) {
                            veflag[cycles[icycle].a[p]] = '1';
                            cycles[icycle].a[p] = verte[ii];
                            cycmin = verte[ii];
                            pflag = 1;
                        }
                    } else if (((verte[ii] < cycmin && pflag == 1) || pflag == 0) && (vertices[verte[ii]].atomsinContact[0] == cycles[icycle].common[j - 1] || vertices[verte[ii]].atomsinContact[1] == cycles[icycle].common[j - 1] || vertices[verte[ii]].atomsinContact[2] == cycles[icycle].common[j - 1])) {
                        veflag[cycles[icycle].a[p]] = '1';
                        cycles[icycle].a[p] = verte[ii];
                        cycmin = verte[ii];
                        pflag = 1;
                    }
                    verte[ii] = -1;
                }
            }
            icycle++;
        }
        return (icycle);
    }

    /**
     *
     * @param natom
     * @return the number of atom shares the edge with natom
     */
    private int[] potoedge(int natom, int v0, int v1, int[] verte, int nverte) {
        int i, j, k, ndegen, ndegat;
        int[] degen = new int[20];
        int[] degatom = new int[20];
        double[] e1 = new double[3];
        double[] e2 = new double[3];
        double[] e3 = new double[3];
        double[] tni = new double[3];
        double[] check = new double[3];
        double[][] vect = new double[40][3];
        double sssp, sp, ssp, ang, minangle;
        double d;
        double[] finvect = new double[3];
        double[] vp = new double[3];
        int[] vercom = new int[50];
        int vc;
        int corratom = 0;
        degen[0] = v0;
        ndegen = 1;
        for (i = 0; i <= nverte - 1; i++) if (i != v0 && verte[i] != -1) if (Math.abs(vertices[verte[v0]].x - vertices[verte[i]].x) < 1.e-6 && Math.abs(vertices[verte[v0]].y - vertices[verte[i]].y) < 1.e-6 && Math.abs(vertices[verte[v0]].z - vertices[verte[i]].z) < 1.e-6) {
            degen[ndegen] = i;
            ndegen++;
            veflag[verte[i]] = '1';
        }
        double[] coord = { Main.atomos[natom].x, Main.atomos[natom].y, Main.atomos[natom].z };
        double[] vcoord = { vertices[verte[v0]].x, vertices[verte[v0]].y, vertices[verte[v0]].z };
        e1 = Vector.vdiff(vcoord, coord);
        e1 = Vector.vtimk(e1, 1. / Main.atomos[natom].RADIO);
        ndegat = 0;
        if (vertices[verte[v0]].atomsinContact[0] != natom) {
            degatom[ndegat] = vertices[verte[v0]].atomsinContact[0];
            ndegat++;
        }
        if (vertices[verte[v0]].atomsinContact[1] != natom) {
            degatom[ndegat] = vertices[verte[v0]].atomsinContact[1];
            ndegat++;
        }
        if (vertices[verte[v0]].atomsinContact[2] != natom) {
            degatom[ndegat] = vertices[verte[v0]].atomsinContact[2];
            ndegat++;
        }
        for (i = 1; i <= ndegen - 1; i++) {
            if (vertices[verte[degen[i]]].atomsinContact[0] != natom) {
                for (j = 0; j <= ndegat - 1; j++) {
                    if (vertices[verte[degen[i]]].atomsinContact[0] == degatom[j]) break;
                    if (j == ndegat) {
                        degatom[ndegat] = vertices[verte[degen[i]]].atomsinContact[0];
                        ndegat++;
                    }
                }
            }
            if (vertices[verte[degen[i]]].atomsinContact[1] != natom) {
                for (j = 0; j <= ndegat - 1; j++) {
                    if (vertices[verte[degen[i]]].atomsinContact[1] == degatom[j]) break;
                    if (j == ndegat) {
                        degatom[ndegat] = vertices[verte[degen[i]]].atomsinContact[1];
                        ndegat++;
                    }
                }
            }
            if (vertices[verte[degen[i]]].atomsinContact[2] != natom) {
                for (j = 0; j <= ndegat - 1; j++) {
                    if (vertices[verte[degen[i]]].atomsinContact[2] == degatom[j]) break;
                    if (j == ndegat) {
                        degatom[ndegat] = vertices[verte[degen[i]]].atomsinContact[2];
                        ndegat++;
                    }
                }
            }
        }
        for (i = 0; i <= ndegat - 1; i++) {
            double[] vercoord = { vertices[verte[v0]].x, vertices[verte[v0]].y, vertices[verte[v0]].z };
            vect[i] = Vector.cunnvec(natom, degatom[i], vercoord);
            d = Math.sqrt(Vector.scalarprod(vect[i], vect[i]));
            vect[i] = Vector.vtimk(vect[i], 1. / d);
        }
        corratom = degatom[0];
        finvect[0] = vect[0][0];
        finvect[1] = vect[0][1];
        finvect[2] = vect[0][2];
        for (i = 1; i <= ndegat - 1; i++) {
            ssp = Vector.scalarprod(finvect, vect[i]);
            if (ssp < 1 && ssp > -1) {
                check = Vector.vectorprod(finvect, vect[i]);
                sp = Vector.scalarprod(check, e1);
                if (sp > 0) {
                    finvect[0] = vect[i][0];
                    finvect[1] = vect[i][1];
                    finvect[2] = vect[i][2];
                    corratom = degatom[i];
                }
            }
        }
        vc = 0;
        for (i = 0; i <= nverte - 1; i++) {
            if (i != v0 && verte[i] != 1) {
                for (k = 1; k <= ndegen - 1; k++) if (i == degen[k]) break;
                if (k == ndegen) if (vertices[verte[i]].atomsinContact[0] == corratom || vertices[verte[i]].atomsinContact[1] == corratom || vertices[verte[i]].atomsinContact[2] == corratom) {
                    vercom[vc] = i;
                    vc++;
                }
            }
        }
        if (vc >= 2) {
            minangle = 3. * Math.PI;
            v1 = vercom[0];
            tni = Vector.toruscenter(natom, corratom);
            e2 = Vector.torusaxis(natom, corratom);
            double[] vecoord = { vertices[verte[v0]].x, vertices[verte[v0]].y, vertices[verte[v0]].z };
            e3 = Vector.vdiff(vecoord, tni);
            d = Math.sqrt(Vector.scalarprod(e3, e3));
            e3 = Vector.vtimk(e3, 1. / d);
            for (i = 0; i <= vc - 1; i++) {
                double[] vicoord = { vertices[verte[vercom[i]]].x, vertices[verte[vercom[i]]].y, vertices[verte[vercom[i]]].z };
                check = Vector.vdiff(vicoord, tni);
                check = Vector.vtimk(check, 1. / d);
                sssp = Vector.scalarprod(check, e3);
                if (sssp >= 1.) ang = 0; else if (sssp <= 1.) ang = Math.PI; else ang = Math.acos(sssp);
                if (ang != 0. && ang != Math.PI) {
                    vp = Vector.vectorprod(check, e3);
                    sp = Vector.scalarprod(vp, e2);
                    if (sp < 0) ang = 2. * Math.PI;
                }
                if (ang < minangle) {
                    minangle = ang;
                    v1 = vercom[i];
                }
            }
        } else v1 = vercom[0];
        int[] ret = { corratom, v1 };
        return (ret);
    }

    /**
     * Load internal external relationship into cycles[i].respect[]
     * @param natom
     * @return
     */
    private boolean loadresp(int natom) {
        int i, j, k;
        double[][] verte = new double[cycle.mem][3];
        double[] point = new double[3];
        if (ncycles == 1) {
            cycles[0].respect[0] = 0;
            return (true);
        }
        for (i = 0; i <= ncycles - 1; i++) {
            cycles[i].respect[i] = 0;
            if (cycles[i].a[0] == -2) {
                for (j = 0; j <= ncycles - 1; j++) if (j != 1) cycles[i].respect[j] = 1;
            } else {
                for (j = 0; j <= cycles[i].nvincyc - 1; j++) {
                    verte[j][0] = vertices[cycles[i].a[j]].x;
                    verte[j][1] = vertices[cycles[i].a[j]].y;
                    verte[j][2] = vertices[cycles[i].a[j]].z;
                }
                for (k = 0; k <= ncycles - 1; k++) {
                    if (k != i) {
                        if (j == 2) cycles[i].respect[k] = 1; else {
                            if (cycles[k].a[0] == -2) {
                                point[0] = circles[cycles[k].a[1]].x;
                                point[1] = circles[cycles[k].a[1]].y;
                                point[2] = circles[cycles[k].a[1]].z;
                            } else {
                                point[0] = vertices[cycles[k].a[0]].x;
                                point[1] = vertices[cycles[k].a[0]].y;
                                point[2] = vertices[cycles[k].a[0]].z;
                            }
                            cycles[i].respect[k] = this.checkcycle(natom, j - 1, verte, point);
                        }
                    }
                }
            }
        }
        return (true);
    }

    /**
     * Checks whether point is internal or external to cycle
     * @param natom
     * @return
     */
    private int checkcycle(int j, int nver, double[][] cycle, double[] point) {
        int i;
        double[] e3 = new double[3];
        double[][] proj = new double[50][3];
        double[] v = new double[3];
        double[][] diff = new double[50][3];
        double sp, ssp, c, sumangle;
        if (nvt == 1) return (1);
        double[] coord = { Main.atomos[j].x, Main.atomos[j].y, Main.atomos[j].z };
        e3 = Vector.vdiff(point, coord);
        e3 = Vector.vtimk(e3, 1. / Main.atomos[j].RADIO);
        sumangle = 0;
        for (i = 0; i <= nver - 1; i++) {
            v = Vector.vdiff(cycle[i], point);
            v = Vector.vtimk(v, 1. / Math.sqrt(Vector.dis2(point, cycle[i])));
            c = -2. * Main.atomos[j].RADIO / Vector.scalarprod(e3, v);
            v = Vector.vtimk(v, c);
            proj[i] = Vector.vsum(point, v);
        }
        for (i = 0; i <= nver - 1; i++) {
            diff[i] = Vector.vdiff(proj[i + 1], proj[i]);
            c = Math.sqrt(Vector.scalarprod(diff[i], diff[i]));
            diff[i] = Vector.vtimk(diff[i], 1. / c);
        }
        diff[nver] = Vector.vdiff(proj[0], proj[nver]);
        c = Math.sqrt(Vector.scalarprod(diff[nver], diff[nver]));
        diff[nver] = Vector.vtimk(diff[nver], 1. / c);
        for (i = 0; i <= nver - 1; i++) {
            v = Vector.vectorprod(diff[i], diff[i + 1]);
            ssp = Vector.scalarprod(diff[i], diff[i + 1]);
            if (ssp <= 1.) c = 0; else if (ssp <= -1.) c = Math.PI; else c = Math.acos(ssp);
            if (Vector.scalarprod(v, e3) > 0) {
                sumangle += c;
            } else {
                sumangle -= c;
            }
        }
        v = Vector.vectorprod(diff[nver], diff[0]);
        sp = Vector.scalarprod(diff[nver], diff[0]);
        if (sp >= 1.) c = 0.; else if (sp <= -1.) c = Math.PI; else c = Math.acos(sp);
        if (Vector.scalarprod(v, e3) > 0) sumangle += c; else sumangle -= c;
        if (sumangle > 0) return (1);
        return (0);
    }

    /**
     *
     * @return
     */
    public boolean buildCycles() {
        double zmax = -100000.;
        int atom0 = -1;
        double z;
        boolean res;
        for (int i = 0; i < Main.atomos.length; i++) {
            z = Main.atomos[i].z + Main.atomos[i].RADIO;
            if (z > zmax) {
                zmax = z;
                atom0 = i;
            }
        }
        ncycles = fetchCycles(atom0);
        res = loadresp(atom0);
        return (true);
    }

    /**
     *
     * @param idAtomo
     * @param atomosContactados
     * @return false if are different
     */
    private static boolean comparaContacto(int idAtomo, jproteinsurface.Atom.ContactAtom[] atomosContactados) {
        for (int i = 0; i < atomosContactados.length; i++) {
            if (idAtomo == atomosContactados[i].contactAtomID) {
                return true;
            }
        }
        return false;
    }

    /**
     * A vertex contains information of the possition and the atoms which form it
     */
    public static class vertex {

        int[] atomsinContact = new int[3];

        double x;

        double y;

        double z;
    }

    /**
     * A circle contains information of the possition and the atoms which form it
     */
    public static class circle {

        int[] atomsinContact = new int[2];

        double x;

        double y;

        double z;
    }

    /**
     * Cycles
     */
    public static class cycle {

        public static int mem = 40;

        int[] a = new int[mem];

        int[] common = new int[mem];

        int[] respect = new int[mem];

        int nvincyc;
    }
}

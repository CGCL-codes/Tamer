package org.fudaa.dodico.ef;

import gnu.trove.TIntObjectHashMap;
import gnu.trove.TObjectIntHashMap;
import gnu.trove.TObjectIntIterator;
import java.util.Arrays;
import org.fudaa.ctulu.CtuluActivity;
import org.fudaa.ctulu.CtuluAnalyze;
import org.fudaa.ctulu.CtuluLibString;
import org.fudaa.ctulu.ProgressionInterface;
import org.fudaa.ctulu.ProgressionUpdater;
import org.fudaa.dodico.commun.DodicoLib;

/**
 * Permet de trouver pour chaque �l�ment les �l�ments voisins (ayant une arete en commun) et pour chaque arete les 1/2
 * �l�ments voisins. Cette classe est teste avec les classes de Rubar: {@link org.fudaa.dodico.rubar.TestJDAT}
 * 
 * @author fred deniger
 * @version $Id: EfVoisinageFinderActivity.java,v 1.2 2007-05-04 13:45:58 deniger Exp $
 */
public class EfVoisinageFinderActivity implements CtuluActivity {

    boolean stop_;

    /**
   * La taille est celle du nombre d'�l�ments.
   */
    int[][] elementVoisinsParElement_;

    int[][] elementVoisinsParArete_;

    public void stop() {
        stop_ = true;
    }

    EfElementVolume[] elts_;

    EfSegment[] edges_;

    /**
   * @param _grid
   * @param _prog
   */
    public boolean process(final EfGridInterface _grid, final ProgressionInterface _prog, final CtuluAnalyze _analyse) {
        stop_ = false;
        final int eltNb = _grid.getEltNb();
        elts_ = new EfElementVolume[eltNb];
        final int guessAreteNb = (int) (eltNb * 2.5);
        final TObjectIntHashMap edges = new TObjectIntHashMap(guessAreteNb);
        final TIntObjectHashMap areteVoisins = new TIntObjectHashMap(guessAreteNb);
        final ProgressionUpdater up = new ProgressionUpdater(_prog);
        up.setValue(5, eltNb, 0, 50);
        final EfSegmentMutable tmp = new EfSegmentMutable(-1, -1);
        int compteurAretes = 0;
        for (int idxElt = 0; idxElt < eltNb; idxElt++) {
            if (stop_) {
                return false;
            }
            final EfElement vi = _grid.getElement(idxElt);
            final int[] arete = new int[vi.getNbEdge()];
            for (int idxAreteLocal = 0; idxAreteLocal < arete.length; idxAreteLocal++) {
                final int idx1 = vi.getEdgePt1(idxAreteLocal);
                final int idx2 = vi.getEdgePt2(idxAreteLocal);
                tmp.setMinMaxIdx(idx1, idx2);
                int idxAreteGlobal = -1;
                if (edges.contains(tmp)) {
                    idxAreteGlobal = edges.get(tmp);
                } else {
                    idxAreteGlobal = compteurAretes;
                    edges.put(new EfSegment(tmp), compteurAretes++);
                }
                arete[idxAreteLocal] = idxAreteGlobal;
                int[] voisins = (int[]) areteVoisins.get(idxAreteGlobal);
                if (voisins == null) {
                    voisins = new int[2];
                    Arrays.fill(voisins, -1);
                    areteVoisins.put(idxAreteGlobal, voisins);
                }
                int idxVoisins = 1;
                boolean trigo = vi.isOrientedIn(_grid, true);
                boolean isEdgeInMeshOrder = tmp.getPt1Idx() == idx1;
                if ((trigo && isEdgeInMeshOrder) || (!trigo && !isEdgeInMeshOrder)) {
                    idxVoisins = 0;
                }
                if (voisins[idxVoisins] >= 0) {
                    _analyse.addFatalError(DodicoLib.getS("Le voisinage de l'ar�te {0} n'est pas valide", CtuluLibString.getString(idxAreteGlobal + 1)));
                    return false;
                }
                voisins[idxVoisins] = idxElt;
            }
            elts_[idxElt] = new EfElementVolume(arete, vi.getPtIndex());
            up.majAvancement();
        }
        final int nbAretes = edges.size();
        edges_ = new EfSegment[nbAretes];
        final TObjectIntIterator it = edges.iterator();
        for (int i = edges.size(); i-- > 0; ) {
            it.advance();
            edges_[it.value()] = (EfSegment) it.key();
        }
        elementVoisinsParArete_ = new int[nbAretes][2];
        for (int i = 0; i < nbAretes; i++) {
            elementVoisinsParArete_[i] = (int[]) areteVoisins.get(i);
        }
        elementVoisinsParElement_ = new int[eltNb][];
        up.setValue(5, eltNb, 50, 50);
        up.majProgessionStateOnly();
        for (int idxElt = 0; idxElt < eltNb; idxElt++) {
            if (stop_) {
                return false;
            }
            final EfElementVolume vi = elts_[idxElt];
            final int nbAretesInElt = vi.getNbAretes();
            elementVoisinsParElement_[idxElt] = new int[nbAretesInElt];
            Arrays.fill(elementVoisinsParElement_[idxElt], -1);
            for (int k = 0; k < nbAretesInElt; k++) {
                final int idxArete = vi.getIdxArete(k);
                final int[] eltVoisinsPourArete = elementVoisinsParArete_[idxArete];
                int test = eltVoisinsPourArete[0];
                if (test >= 0 && test != idxElt) {
                    elementVoisinsParElement_[idxElt][k] = test;
                } else {
                    test = eltVoisinsPourArete[1];
                    if (test >= 0 && test != idxElt) {
                        elementVoisinsParElement_[idxElt][k] = test;
                    }
                }
            }
            up.majAvancement();
        }
        return true;
    }

    /**
   * Pour les aretes possedant 2 voisins, les 2 indices seront >=0. Pour une arete frontiere, le premier indice vaut -1.
   * 
   * @return un tableau non null ( si process lance) de taille [ nbArete][2]
   */
    public int[][] getElementVoisinsParArete() {
        return elementVoisinsParArete_;
    }

    public int[][] getElementVoisinsParElement() {
        return elementVoisinsParElement_;
    }

    public EfElementVolume[] getElts() {
        return elts_;
    }

    public EfSegment[] getEdges() {
        return edges_;
    }
}

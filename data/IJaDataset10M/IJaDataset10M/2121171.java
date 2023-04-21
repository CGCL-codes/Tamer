package org.fudaa.dodico.ef;

import gnu.trove.TIntIntHashMap;
import gnu.trove.TObjectIntHashMap;
import java.util.ArrayList;
import java.util.List;
import org.fudaa.ctulu.CtuluAnalyze;
import org.fudaa.ctulu.ProgressionInterface;
import org.fudaa.ctulu.ProgressionUpdater;
import org.fudaa.dodico.commun.DodicoLib;
import org.fudaa.dodico.commun.DodicoResource;
import org.fudaa.dodico.ef.impl.EfGrid;
import org.fudaa.dodico.ef.impl.EfGridRedispatched;

/**
 * @author fred deniger
 * @version $Id: EfLibImpl.java,v 1.2 2007-04-20 16:21:27 deniger Exp $
 */
public final class EfLibImpl {

    private EfLibImpl() {
    }

    /**
   * Contruit un maillage T6 a partir de ce maillage UNIQUEMENT si c'est un maillage T3. Si le parametres
   * <code>_segmentPtAjouteIdx</code> est non nul la correspondance segment point ajoute est insere (utile par la
   * suite pour determiner les bords, les valeurs sur les nouveaux points,...). Un segment est une "arete" d'un element.
   * 
   * @param _init le maillage initial
   * @param _progress la barre de progress
   * @param _segmentPtAjouteIdx pour chaque segment donne le num du point ajoute.Peut etre nul.
   * @return le maillage T6
   */
    public static EfGridInterface maillageT3enT6(final EfGridInterface _init, final ProgressionInterface _progress, final TObjectIntHashMap _segmentPtAjouteIdx) {
        if (_init.getEltType() != EfElementType.T3) {
            return null;
        }
        final int nelem = _init.getEltNb();
        int npoint = _init.getPtsNb();
        final List t6points = new ArrayList(npoint + nelem * 3);
        for (int i = 0; i < npoint; i++) {
            t6points.add(new EfNode(_init.getCoor(i)));
        }
        final EfElement[] t6elems = new EfElement[nelem];
        TObjectIntHashMap segmentPtIdx = null;
        if (_segmentPtAjouteIdx == null) {
            segmentPtIdx = new TObjectIntHashMap(_init.getEltNb() * 3);
        } else {
            _segmentPtAjouteIdx.ensureCapacity(_init.getEltNb() * 3);
            segmentPtIdx = _segmentPtAjouteIdx;
        }
        int n0, n1, n2, n3, n4, n5;
        final EfSegmentMutable s = new EfSegmentMutable(0, 0);
        final ProgressionUpdater pu = new ProgressionUpdater(_progress);
        pu.setValue(5, nelem);
        pu.majProgessionStateOnly(DodicoLib.getS("transformation T3->T6"));
        for (int i = 0; i < nelem; i++) {
            n0 = _init.getElement(i).getPtIndex(0);
            n2 = _init.getElement(i).getPtIndex(1);
            n4 = _init.getElement(i).getPtIndex(2);
            s.setMinMaxIdx(n0, n2);
            if (segmentPtIdx.containsKey(s)) {
                n1 = segmentPtIdx.get(s);
            } else {
                t6points.add(_init.getMilieu(n0, n2));
                n1 = npoint;
                segmentPtIdx.put(new EfSegment(s), npoint++);
            }
            s.setMinMaxIdx(n2, n4);
            if (segmentPtIdx.containsKey(s)) {
                n3 = segmentPtIdx.get(s);
            } else {
                t6points.add(_init.getMilieu(n2, n4));
                n3 = npoint;
                segmentPtIdx.put(new EfSegment(s), npoint++);
            }
            s.setMinMaxIdx(n4, n0);
            if (segmentPtIdx.containsKey(s)) {
                n5 = segmentPtIdx.get(s);
            } else {
                t6points.add(_init.getMilieu(n4, n0));
                n5 = npoint;
                segmentPtIdx.put(new EfSegment(s), npoint++);
            }
            t6elems[i] = new EfElement(new int[] { n0, n1, n2, n3, n4, n5 });
            pu.majAvancement();
        }
        final EfNode[] t6pointsTab = new EfNode[npoint];
        t6points.toArray(t6pointsTab);
        return new EfGrid(t6pointsTab, t6elems);
    }

    /**
   * Ne touche pas aux noeuds.
   * 
   * @param _init le maillage initial
   * @param _progress la barre de progression
   * @return le T3 issu de <code>_init</code> avec les T6 transforme en 4 T3.
   */
    public static EfGridInterface maillageT6en4T3(final EfGridInterface _init, final ProgressionInterface _progress) {
        if (_init.getEltType() != EfElementType.T6) {
            return null;
        }
        final ProgressionUpdater pu = new ProgressionUpdater(_progress);
        pu.majProgessionStateOnly(DodicoLib.getS("transformation T6 en 4 T3"));
        final int nelemT6 = _init.getEltNb();
        final EfElement[] eltsT3 = new EfElement[4 * nelemT6];
        pu.setValue(8, nelemT6, 20, 80);
        pu.majProgessionStateOnly();
        int temp;
        EfElement t6ec;
        for (int i = 0; i < nelemT6; i++) {
            temp = i * 4;
            t6ec = _init.getElement(i);
            eltsT3[temp] = new EfElement(new int[] { t6ec.getPtIndex(0), t6ec.getPtIndex(1), t6ec.getPtIndex(5) });
            eltsT3[temp + 1] = new EfElement(new int[] { t6ec.getPtIndex(1), t6ec.getPtIndex(2), t6ec.getPtIndex(3) });
            eltsT3[temp + 2] = new EfElement(new int[] { t6ec.getPtIndex(3), t6ec.getPtIndex(4), t6ec.getPtIndex(5) });
            eltsT3[temp + 3] = new EfElement(new int[] { t6ec.getPtIndex(1), t6ec.getPtIndex(3), t6ec.getPtIndex(5) });
            pu.majAvancement();
        }
        return new EfGridRedispatched(_init, eltsT3, EfElementType.T3);
    }

    /**
   * Supprime les elements milieux et renumerote les points. L'ordre est conserve. La correspondance entre les num T6 et
   * T3 peut etre recupere avec <code>_ptT6IdxPptT3Idx</code>
   * 
   * @param _init le maillage initiale
   * @param _progress la barre de progress
   * @param _ptT6IdxPtT3Idx correspondance des num : peut etre nul. old->new
   * @return le maillage T3 issu du maillage T6 (suppression des points milieux)
   */
    public static EfGrid maillageT6enT3(final EfGridInterface _init, final ProgressionInterface _progress, final TIntIntHashMap _ptT6IdxPtT3Idx) {
        if (_init.getEltType() != EfElementType.T6) {
            return null;
        }
        final int nelemT6 = _init.getEltNb();
        TIntIntHashMap t6IdxT3Idx;
        if (_ptT6IdxPtT3Idx == null) {
            t6IdxT3Idx = new TIntIntHashMap(_init.getPtsNb() / 2);
        } else {
            _ptT6IdxPtT3Idx.ensureCapacity(_init.getPtsNb() / 2);
            t6IdxT3Idx = _ptT6IdxPtT3Idx;
        }
        final ProgressionUpdater pu = new ProgressionUpdater(_progress);
        pu.setValue(5, nelemT6, 0, 50);
        pu.majProgessionStateOnly(DodicoLib.getS("transformation T6->T3"));
        EfElement eltEnCours;
        for (int i = 0; i < nelemT6; i++) {
            eltEnCours = _init.getElement(i);
            t6IdxT3Idx.put(eltEnCours.getPtIndex(0), -1);
            t6IdxT3Idx.put(eltEnCours.getPtIndex(2), -1);
            t6IdxT3Idx.put(eltEnCours.getPtIndex(4), -1);
            pu.majAvancement();
        }
        int t3index = 0;
        final List ptT3Temp = new ArrayList(_init.getPtsNb());
        pu.setValue(5, _init.getPtsNb(), 50, 50);
        pu.majProgessionStateOnly();
        for (int i = 0; i < _init.getPtsNb(); i++) {
            if (t6IdxT3Idx.contains(i)) {
                t6IdxT3Idx.put(i, t3index++);
                ptT3Temp.add(new EfNode(_init.getPtX(i), _init.getPtY(i), _init.getPtZ(i)));
            }
            pu.majAvancement();
        }
        final EfNode[] ptsT3 = new EfNode[t3index];
        ptT3Temp.toArray(ptsT3);
        final EfElement[] eltT3 = new EfElement[nelemT6];
        for (int i = 0; i < nelemT6; i++) {
            eltEnCours = _init.getElement(i);
            eltT3[i] = new EfElement(new int[] { t6IdxT3Idx.get(eltEnCours.getPtIndex(0)), t6IdxT3Idx.get(eltEnCours.getPtIndex(2)), t6IdxT3Idx.get(eltEnCours.getPtIndex(4)) });
        }
        return new EfGrid(ptsT3, eltT3);
    }

    /**
   * Ne touche pas aux noeuds.
   * 
   * @param _g le maillage a transformer
   * @param _prog la barre de prog
   * @param _analyze l'analyse
   * @param _eltNewIdxOldIdx correspond indice nouvel �l�ment-> indice ancien �l�ment
   * @return maillage T3.
   */
    public static EfGridInterface toT3(EfGridInterface _g, final ProgressionInterface _prog, final CtuluAnalyze _analyze, TIntIntHashMap _eltNewIdxOldIdx) {
        if (_g == null || _g.getEltType() == EfElementType.T3) return _g;
        final EfGridInterface g = _g;
        final int nbElt = g.getEltNb();
        final List newEltT3 = new ArrayList(nbElt);
        final ProgressionUpdater up = new ProgressionUpdater(_prog);
        up.setValue(4, nbElt);
        int idx = 0;
        for (int i = 0; i < nbElt; i++) {
            final EfElement el = g.getElement(i);
            final EfElementType type = el.getDefaultType();
            if (type == null) {
                _analyze.addFatalError(DodicoResource.DODICO.getString("Type d'�lement inconnu") + " (" + el.getPtNb() + ")");
                return null;
            }
            if (type.equals(EfElementType.T3)) {
                newEltT3.add(el);
                if (_eltNewIdxOldIdx != null) _eltNewIdxOldIdx.put(idx, i);
                idx++;
            } else if (type.equals(EfElementType.T6)) {
                newEltT3.add(new EfElement(new int[] { el.getPtIndex(0), el.getPtIndex(1), el.getPtIndex(5) }));
                if (_eltNewIdxOldIdx != null) _eltNewIdxOldIdx.put(idx, i);
                idx++;
                newEltT3.add(new EfElement(new int[] { el.getPtIndex(1), el.getPtIndex(2), el.getPtIndex(3) }));
                if (_eltNewIdxOldIdx != null) _eltNewIdxOldIdx.put(idx, i);
                idx++;
                newEltT3.add(new EfElement(new int[] { el.getPtIndex(3), el.getPtIndex(4), el.getPtIndex(5) }));
                if (_eltNewIdxOldIdx != null) _eltNewIdxOldIdx.put(idx, i);
                idx++;
                newEltT3.add(new EfElement(new int[] { el.getPtIndex(1), el.getPtIndex(3), el.getPtIndex(5) }));
                if (_eltNewIdxOldIdx != null) _eltNewIdxOldIdx.put(idx, i);
                idx++;
            } else if (type.equals(EfElementType.Q4)) {
                newEltT3.add(new EfElement(new int[] { el.getPtIndex(0), el.getPtIndex(1), el.getPtIndex(3) }));
                if (_eltNewIdxOldIdx != null) _eltNewIdxOldIdx.put(idx, i);
                idx++;
                newEltT3.add(new EfElement(new int[] { el.getPtIndex(1), el.getPtIndex(2), el.getPtIndex(3) }));
                if (_eltNewIdxOldIdx != null) _eltNewIdxOldIdx.put(idx, i);
                idx++;
            }
            up.majAvancement();
        }
        final EfElement[] elt = new EfElement[newEltT3.size()];
        newEltT3.toArray(elt);
        return new EfGridRedispatched(g, elt, EfElementType.T3);
    }
}

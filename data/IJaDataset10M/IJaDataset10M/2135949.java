package org.fudaa.fudaa.meshviewer;

import gnu.trove.TIntHashSet;
import java.awt.event.MouseEvent;
import java.util.StringTokenizer;
import org.fudaa.ebli.calque.BCalque;
import org.fudaa.ebli.calque.ZCalqueAffichageDonnees;
import org.fudaa.ebli.calque.ZCalqueSondeInteraction;
import org.fudaa.ebli.calque.ZCalqueSondeInterface;
import org.fudaa.ebli.calque.ZEbliFilleCalques;
import org.fudaa.ebli.geometrie.GrBoite;
import org.fudaa.ebli.geometrie.GrPoint;
import org.fudaa.fudaa.meshviewer.layer.MvLayer;

/**
 * @author deniger
 * @version $Id: MvFindActionAbstract.java,v 1.2 2005-01-31 17:14:28 deniger Exp $
 */
public abstract class MvFindActionAbstract implements MvFindAction {

    ZCalqueAffichageDonnees layer_;

    MvVisuPanel visu_;

    /**
   * 
   */
    public MvFindActionAbstract(MvVisuPanel _visu, ZCalqueAffichageDonnees _layer) {
        layer_ = _layer;
        visu_ = _visu;
    }

    /**
   * @see org.fudaa.fudaa.meshviewer.MvFindAction#erreur(java.lang.String, java.lang.String)
   */
    public String erreur(String _s, String _action) {
        if (_s == null || _s.length() == 0) {
            return MvResource.getS("Entr�e vide");
        }
        String r = null;
        if (_action.equals("SONDE")) {
            StringTokenizer tk = new StringTokenizer(_s, ";");
            if (tk.countTokens() == 2) {
                try {
                    double x = Double.parseDouble(tk.nextToken());
                    double y = Double.parseDouble(tk.nextToken());
                    ZCalqueSondeInterface target = (ZCalqueSondeInterface) layer_;
                    if (!target.isPointSondable(new GrPoint(x, y, 0))) {
                        r = MvResource.getS("Le point � sonder est en dehors des limites");
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            } else r = MvResource.getS("Donn�es invalides");
        } else {
            int[] idx = getIndex(_s);
            r = MvFindActionAbstract.testVide(idx);
            if (r == null) {
                boolean err = false;
                int max = layer_.modeleDonnees().getNombre();
                for (int i = idx.length - 1; i >= 0 && (!err); i--) {
                    if (idx[i] < 0 || idx[i] >= max) err = true;
                }
                if (err) {
                    r = MvResource.getS("Des indices sont en dehors des limites");
                }
            }
        }
        return r;
    }

    public boolean find(String _idx, String _action) {
        visu_.clearSelections();
        visu_.setCalqueActif(layer_);
        if (_action.equals("SONDE")) {
            StringTokenizer tk = new StringTokenizer(_idx, ";");
            if (tk.countTokens() == 2) {
                try {
                    double x = Double.parseDouble(tk.nextToken());
                    double y = Double.parseDouble(tk.nextToken());
                    ZCalqueSondeInteraction s = visu_.getCalqueSondeInteraction();
                    if (s != null) {
                        s.setGele(false);
                    }
                    ZCalqueSondeInterface target = (ZCalqueSondeInterface) layer_;
                    boolean r = target.changeSonde(new GrPoint(x, y, 0));
                    if (r) visu_.setInfoPaletteActive();
                    return r;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        } else {
            int[] idx = getIndex(_idx);
            if (idx != null) return layer_.setSelection(idx);
        }
        return false;
    }

    public static String testVide(int[] _idx) {
        if (_idx == null || _idx.length == 0) {
            return MvResource.getS("Entr�e vide");
        }
        return null;
    }

    public static int[] getIndex(String _idx) {
        if ((_idx == null) || (_idx.length() == 0)) return null;
        StringTokenizer tk = new StringTokenizer(_idx, ";");
        TIntHashSet l = new TIntHashSet(tk.countTokens());
        while (tk.hasMoreTokens()) {
            String tok = tk.nextToken();
            int idxTiret = tok.indexOf('-');
            if (idxTiret > 0 && idxTiret < tok.length() - 1) {
                try {
                    int i1 = Integer.parseInt(tok.substring(0, idxTiret));
                    int i2 = Integer.parseInt(tok.substring(idxTiret + 1));
                    if (i1 > i2) {
                        int tmp = i2;
                        i2 = i1;
                        i1 = tmp;
                    }
                    for (int i = i1; i <= i2; i++) {
                        l.add(i - 1);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("format non valide");
                }
            } else {
                int i;
                try {
                    i = Integer.parseInt(tok);
                } catch (NumberFormatException e) {
                    i = -1;
                }
                l.add(i - 1);
            }
        }
        return l.toArray();
    }

    /**
   *
   */
    public final MvFindComponent createFindComponent() {
        if (layer_ instanceof ZCalqueSondeInterface) {
            return new MvFindComponentDefault(MvResource.getS("Indice(s)"), true, ((ZCalqueSondeInterface) layer_).isSondeActive());
        }
        return new MvFindComponentDefault(MvResource.getS("Indice(s)"));
    }

    /**
   *
   */
    public String toString() {
        return layer_.getTitle();
    }

    public BCalque getCalque() {
        return layer_;
    }

    /**
   *
   */
    public GrBoite getZoomOnSelected() {
        if (layer_ instanceof ZCalqueSondeInterface) {
            ZCalqueSondeInterface s = (ZCalqueSondeInterface) layer_;
            if (s.isSondeActive()) {
                GrPoint p = new GrPoint(s.getSondeX(), s.getSondeY(), 0);
                GrBoite b = new GrBoite();
                b.e = p;
                b.o = new GrPoint(p);
                ZEbliFilleCalques.ajusteZoomOnSelected(b, layer_.modeleDonnees());
                return b;
            }
        }
        return ((MvLayer) layer_).getZoomOnSelected();
    }
}

package org.fudaa.dodico.ef;

import java.util.BitSet;
import org.fudaa.ctulu.CtuluListSelectionInterface;

/**
 * Un filtre sur des indices de selections: Attention le constructeur peut etre long.
 * 
 * @author Fred Deniger
 * @version $Id: EfFilterSelectedElement.java,v 1.2 2006-04-07 09:23:10 deniger Exp $
 */
public class EfFilterSelectedElement implements EfFilter {

    final CtuluListSelectionInterface selectedElement_;

    BitSet selectedPt_;

    final EfGridInterface g_;

    /**
   * @param _selectedElement les elements selectionn�es
   * @param _g
   */
    public EfFilterSelectedElement(final CtuluListSelectionInterface _selectedElement, final EfGridInterface _g) {
        super();
        g_ = _g;
        selectedElement_ = _selectedElement;
    }

    public boolean isActivated(final int _idxPt) {
        if (selectedPt_ == null && selectedElement_ != null && !selectedElement_.isEmpty()) {
            selectedPt_ = new BitSet(g_.getPtsNb());
            for (int i = g_.getEltNb() - 1; i >= 0; i--) {
                if (selectedElement_.isSelected(i)) {
                    final EfElement el = g_.getElement(i);
                    for (int j = el.getPtNb() - 1; j >= 0; j--) {
                        selectedPt_.set(el.getPtIndex(j));
                    }
                }
            }
        }
        return selectedPt_ == null ? false : selectedPt_.get(_idxPt);
    }

    public boolean isActivatedElt(final int _idxElt) {
        return selectedElement_ == null ? false : selectedElement_.isSelected(_idxElt);
    }

    public boolean isActivatedElt(final int idxElt, final boolean strict) {
        return isActivatedElt(idxElt);
    }
}

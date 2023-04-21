package org.fudaa.fudaa.tr.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.fudaa.ctulu.CtuluLibArray;
import org.fudaa.dodico.h2d.type.H2dVariableTransType;
import org.fudaa.dodico.mesure.EvolutionReguliereInterface;
import org.fudaa.fudaa.tr.data.TrVisuPanel;
import org.fudaa.fudaa.tr.rubar.TrRubarBcAreteLayer;
import org.fudaa.fudaa.tr.rubar.TrRubarVisuPanel;
import com.memoire.fu.FuComparator;

/**
 * @author fred deniger
 * @version $Id: TrRubarCourbeUseResults.java,v 1.2 2007-04-30 14:22:41 deniger Exp $
 */
public class TrRubarCourbeUseResults extends TrCourbeUseResultsAbstract {

    final TrCourbeUseCounter usedInCl_ = new TrCourbeUseCounter(TrResource.getS("Conditions limites"));

    final TrCourbeUseCounter usedInApp_ = new TrCourbeUseCounter(H2dVariableTransType.APPORT_PLUIE.getName());

    boolean[] isApp_;

    public TrRubarCourbeUseResults(final TrVisuPanel _visu) {
        super(_visu);
    }

    public void setFilter(final EvolutionReguliereInterface _evol) {
        usedInApp_.setEvolToTest(_evol);
        usedInCl_.setEvolToTest(_evol);
    }

    public void validSearch() {
        final int nbEvolCl = usedInCl_.getNbEvol();
        final int nbEvolApp = usedInApp_.getNbEvol();
        final List allEvols = new ArrayList(nbEvolCl + nbEvolApp);
        EvolutionReguliereInterface[] evols = (EvolutionReguliereInterface[]) usedInCl_.getEvols().toArray(new EvolutionReguliereInterface[nbEvolCl]);
        Arrays.sort(evols, FuComparator.STRING_COMPARATOR);
        allEvols.addAll(Arrays.asList(evols));
        int size = allEvols.size();
        final int idx = size;
        evols = (EvolutionReguliereInterface[]) usedInApp_.getEvols().toArray(new EvolutionReguliereInterface[nbEvolApp]);
        allEvols.addAll(Arrays.asList(evols));
        size = allEvols.size();
        evols_ = (EvolutionReguliereInterface[]) allEvols.toArray(new EvolutionReguliereInterface[size]);
        isApp_ = new boolean[evols_.length];
        for (int i = idx; i < size; i++) {
            isApp_[i] = true;
        }
    }

    @Override
    public TrCourbeUseCounter getCounter(final int _rowIndex) {
        return isApp_[_rowIndex] ? usedInApp_ : usedInCl_;
    }

    public void selectInView(final EvolutionReguliereInterface _eve) {
        final int idx = CtuluLibArray.findObject(evols_, _eve);
        if (idx >= 0) {
            if (isApp_[idx]) {
                selectAppInView(_eve);
            } else {
                selectClInView(_eve);
            }
        }
    }

    protected void selectAppInView(final EvolutionReguliereInterface _eve) {
        selectedInMeshLayer(usedInApp_.getIdxSelected(_eve));
    }

    protected void selectClInView(final EvolutionReguliereInterface _eve) {
        final int[] select = usedInCl_.getIdxSelected(_eve);
        if (select != null) {
            final TrRubarBcAreteLayer layer = ((TrRubarVisuPanel) visu_).getBcAreteLayer();
            visu_.getArbreCalqueModel().setSelectionCalque(layer);
            layer.setSelection(select);
            visu_.zoomOnSelected();
            activateVisuFrame();
        }
    }
}

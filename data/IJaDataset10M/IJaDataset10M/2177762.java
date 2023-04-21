package org.fudaa.fudaa.meshviewer.export;

import java.io.File;
import org.fudaa.ctulu.CtuluActivity;
import org.fudaa.ctulu.CtuluAnalyze;
import org.fudaa.ctulu.CtuluVariable;
import org.fudaa.ctulu.ProgressionInterface;
import org.fudaa.ctulu.interpolation.InterpolationVectorContainer;
import org.fudaa.dodico.ef.EfFilter;
import org.fudaa.dodico.ef.EfGridData;

/**
 * @author Fred Deniger
 * @version $Id: MvExportActInterface.java,v 1.3 2007-06-05 09:01:12 deniger Exp $
 */
public interface MvExportActInterface extends CtuluActivity {

    /**
   * @param _strict nouvel etat du filtre
   */
    void setFitreStrict(boolean _strict);

    void setSelectedTimeStep(double[] _idx);

    void setSrc(EfGridData _src, InterpolationVectorContainer _vects);

    void setSelectedVar(CtuluVariable[] _idx);

    int getNbLabelInfoNeeded();

    String getTitre();

    /**
   * Le constructeur de filtre.
   * 
   * @param _filtrer le nouveau filtre
   */
    void setFilter(EfFilter _filtrer);

    /**
   * @param _prog
   * @param _dest
   * @param _analyze
   */
    void actExport(ProgressionInterface _prog, File[] _dest, CtuluAnalyze _analyze, String[] _message);
}

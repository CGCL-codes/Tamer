package org.fudaa.fudaa.tr.rubar;

import java.util.List;
import org.fudaa.dodico.ef.EfFrontierInterface;

/**
 * @author Fred Deniger
 * @version $Id: TrRubarBcAreteModel.java,v 1.5 2007-01-19 13:14:12 deniger Exp $
 */
public interface TrRubarBcAreteModel extends TrRubarAreteModel {

    /**
   * @return la liste des bords utilises
   */
    List getUsedBoundaryType();

    /**
   * @return la liste de tous les bords disponibles pour le projet en cours
   */
    List getBordList();

    /**
   * @return le nombre de bord diff utilise
   */
    int getNbTypeBord();

    EfFrontierInterface getFr();
}

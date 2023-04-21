package org.fudaa.dodico.h2d.rubar;

import org.fudaa.dodico.dico.DicoParamsListener;
import org.fudaa.dodico.h2d.H2dProjectDispatcherListener;
import org.fudaa.dodico.h2d.H2dSIListener;
import org.fudaa.dodico.mesure.EvolutionListener;

/**
 * Un listener pour tout ...
 * 
 * @author Fred Deniger
 * @version $Id: H2dRubarProjectDispatcherListener.java,v 1.13 2006-11-20 14:23:55 deniger Exp $
 */
public interface H2dRubarProjectDispatcherListener extends H2dProjectDispatcherListener, DicoParamsListener, EvolutionListener, H2dRubarBcListener, H2dRubarTarageListener, H2dRubarDonneesBrutesListener, H2dSIListener, H2DRubarFrictionListener, H2dRubarApportListener, H2dRubarLimniListener, H2dRubarOuvrageListener, H2DRubarDiffusionListener, H2dRubarVentListener, H2dRubarSedimentListener {
}

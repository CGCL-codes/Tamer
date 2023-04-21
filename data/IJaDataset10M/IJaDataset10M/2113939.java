package com.iver.cit.gvsig.fmap.operations.strategies;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.exceptions.visitors.ProcessVisitorException;
import com.iver.cit.gvsig.exceptions.visitors.StartVisitorException;
import com.iver.cit.gvsig.exceptions.visitors.VisitorException;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.layers.FLayer;

/**
 * Interfaz que ofrece los m�todos para iniciar, finalizar y visitar una
 * feature.
 */
public interface FeatureVisitor {

    /**
	 * Recibe las geometr�as a medida que se van recorriendo en la estrategia.
	 *
	 * @param g Geometr�a que se recorre
	 * @param index �ndice de la geometr�a
	 * @throws ReadDriverException
	 * @throws VisitorException TODO
	 * @throws ProcessVisitorException TODO
	 */
    void visit(IGeometry g, int index) throws ReadDriverException, VisitorException, ProcessVisitorException;

    /**
	 * All FeatureVisitor is linked with a call to Strategy.processXXX method.
	 * It represents an iterative process over all (or some) features of a Layer.
	 * This method returns a descriptive text of the process that a visitor makes.
	 *
	 * FIXME Internacionalizamos el mensaje???
	 * @return
	 */
    String getProcessDescription();

    /**
	 * M�todo invocado al finalizar las visitas con el fin de que se puedan
	 * liberar los recursos reservados en start
	 *
	 * @param layer Capa sobre la que se actua
	 * @throws VisitorException TODO
	 */
    void stop(FLayer layer) throws VisitorException;

    /**
	 * M�todo invocado antes de las visitas para que el visitor pueda reservar
	 * alg�n tipo de recurso que sea necesario
	 *
	 * @param layer Capa sobre la que se act�a
	 *
	 * @return Devuelve true si el visitor se puede aplicar sobre la capa que
	 * 		   se pasa como par�metro
	 * @throws StartVisitorException TODO
	 */
    boolean start(FLayer layer) throws StartVisitorException;
}

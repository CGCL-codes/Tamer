package net.sf.jregression.reglas.strategies.impl;

import java.util.Collection;
import java.util.Iterator;
import net.sf.jregression.reglas.vo.Lote;
import net.sf.jregression.reglas.vo.Parametro;
import net.sf.jregression.reglas.vo.ParametroColeccion;
import net.sf.jregression.reglas.vo.ParametroVo;

/**
 * @author mblasi
 * mailto: matias.blasi@gmail.com
 *
 * QueryResultAssertionStrategy
 *
 * Esta clase evalua si cada uno de los parametros del lote esta contenido dentro 
 * del resultado obtenido, pero en forma independiente, sin importar el orden, 
 * ni la cantidad en cada conjunto.
 */
public class QueryResultAssertionStrategy extends ExactlyQueryResultAssertionStrategy {

    /**
	 * Constructor
	 */
    public QueryResultAssertionStrategy() {
        super();
    }

    /**
	 * Evalua si cada uno de los parametros del lote esta contenido dentro del
	 * resultado obtenido, pero en forma independiente, sin importar el orden, 
	 * ni la cantidad en cada conjunto.
	 * @see net.sf.jregression.reglas.strategies.AssertionStrategy#asserts(java.lang.Object, net.sf.jregression.reglas.vo.Lote)
	 */
    public boolean asserts(Object real, Lote esperado) {
        boolean ret = true;
        Collection params = esperado.getParametros();
        Iterator p = params.iterator();
        while (p.hasNext() && ret) {
            Parametro param = (Parametro) p.next();
            ret &= evaluarParametro(real, param);
        }
        return ret;
    }

    /**
	 * Evalua que el valor de un parametro (convertido a mensaje soap, exista
	 * dentro del resultado real.
	 * Este metodo se invoca recursivamente por cada parametro existente dentro 
	 * de un ParametroColeccion, y en cada invocacion con un ParametroSimple o 
	 * un ParametroVO se delega a la superclase la evaluacion del mismo.
	 * @param real resultado real
	 * @param param parametro a evaluar
	 * @return boolean true en caso de existir - false en caso contrario
	 */
    private boolean evaluarParametro(Object real, Parametro param) {
        boolean ret = true;
        if (Parametro.PARAMETRO_COLECCION == param.getTipoParametro()) {
            ParametroColeccion col = (ParametroColeccion) param;
            Iterator params = col.getParametros().iterator();
            while (params.hasNext() && ret) {
                ParametroVo vo = (ParametroVo) params.next();
                ret &= evaluarParametro(real, vo);
            }
        } else {
            Lote lote = new Lote();
            lote.addParam(param);
            ret &= super.asserts(real, lote);
        }
        return ret;
    }
}

package ar.com.tifad.domainmodel.entities.contracts;

import java.util.Set;

/**
 * Un productor de gas es un agente que inyecta gas en la red de transporte.
 * Puede ser una empresa petrolera que se dedica a la extracci�n en una cuenca
 * o un agente que posee un puerto o punto de regasificaci�n inyectando gas
 * (regasificado) desde un barco.
 * @author Ricardo
 *
 */
public interface IProductor extends IAgente {

    public abstract Set<ICuenca> getCuencas();

    public abstract void setCuencas(Set<ICuenca> basins);
}

package ar.com.tifad.domainmodel.entities.contracts;

/**
 * Representa un punto (o nodo) en la red de gas.
 * @author Ricardo
 *
 */
public interface IPuntoRedDeGas {

    public abstract int getId();

    public abstract void setId(int id);

    public abstract String getNemo();

    public abstract void setNemo(String nemo);

    public abstract String getName();

    public abstract void setName(String name);

    public abstract IReglaDeFlujo getReglaDeFlujo();

    public abstract void setReglaDeFlujo(IReglaDeFlujo flowRole);
}

package domain.model.numbers;

/**
 * Interfase comun a todos los contructores de objetos n�mericos.
 */
public interface NumberBuilder {

    /**
     * Construye un objeto n�merico que represente al valor especificad en el <code>long</code> pasado como parametro.<br/>
     * @param number Valor que con el que se construira el objeto <code>Number</code>
     * @return El Objeto <code>Number</code> representando al valor del parametro.
     */
    public abstract Number build(long number);

    /**
     * Construye un objeto n�merico que represente al valor especificado en el <code>String</code> pasado como parametro.<br/>
     * La interpretaci�n del String y la traducci�n no es generica, depende
     * de que clase concreta de objeto Number que se este construyendo.
     * @param number Valor que con el que se construira el objeto <code>Number</code>
     * @return El Objeto <code>Number</code> representando al valor del parametro.
     */
    public abstract Number build(String number);

    /**
     * Retorna el objeto que representa el cero n�merico.
     * @return El objeto <code>Number</code> cero.
     */
    public abstract Number getZero();

    /**
     * Retorna el objeto que representa el uno n�merico.
     * @return El objeto <code>Number</code> uno.
     */
    public abstract Number getOne();

    /**
     * Retorna el objeto que representa el menos uno n�merico.
     * @return El objeto <code>Number</code> menos uno.
     */
    public abstract Number getMinusOne();

    /**
     * Retorna el objeto de mayor valor n�merico.
     * @return El objeto <code>Number</code> de mayor valor posible.
     */
    public abstract Number getMax();

    /**
     * Retorna el objeto de menor valor n�merico.
     * @return El objeto <code>Number</code> de menor valor posible.
     */
    public abstract Number getMin();
}

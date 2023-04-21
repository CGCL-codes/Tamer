package ar.edu.unlp.info.diseyappweb.model;

import ar.edu.unlp.info.diseyappweb.model.exception.StockMateriaPrimaInsuficiente;

/**
 * La clase StockMateriaPrima contiene la información sobre el stock que posee la empresa de 
 * la materia prima.
 * 
 * @author Martinelli, Federico
 * @author Ducos, David
 */
public class StockMateriaPrima {

    private Integer id;

    private MateriaPrima materiaPrima;

    private Float cantidad;

    private Float cantidadAsignada;

    private Float cantidadMinima;

    /**
	 * Crea una nueva instancia de la clase StockMateriaPrima.
	 */
    public StockMateriaPrima() {
        super();
        this.setMateriaPrima(null);
        this.setCantidad(0f);
        this.setCantidadAsignada(0f);
        this.setCantidadMinima(0f);
    }

    /**
	 * Crea una nueva instancia de la clase StockMateriaPrima tomando los datos de los parámetros.
	 * 
	 * @param materiaPrima Materia prima de la cual se mantiene el stock.
	 */
    public StockMateriaPrima(MateriaPrima materiaPrima) {
        super();
        this.setMateriaPrima(materiaPrima);
        this.setCantidad(0f);
        this.setCantidadAsignada(0f);
        this.setCantidadMinima(0f);
    }

    /**
	 * Crea una nueva instancia de la clase StockMateriaPrima tomando los datos de los parámetros.
	 * 
	 * @param materiaPrima Materia prima de la cual se mantiene el stock.
	 * @param cantidadMinima Cantidad mínima que debe poseer la empresa en stock de la materia prima.  
	 */
    public StockMateriaPrima(MateriaPrima materiaPrima, Float cantidadMinima) {
        super();
        this.setMateriaPrima(materiaPrima);
        this.setCantidad(0f);
        this.setCantidadAsignada(0f);
        this.setCantidadMinima(cantidadMinima);
    }

    /**
	 * Crea una nueva instancia de la clase StockMateriaPrima tomando los datos de los parámetros.
	 * 
	 * @param materiaPrima Materia prima de la cual se mantiene el stock.
	 * @param cantidad Cantidad de Materia prima en stock.
	 * @param cantidadAsignada Cantidad de materia prima asiganda a las ordenes de trabajo.
	 * @param cantidadMinima Cantidad mínima que debe poseer la empresa en stock de la materia prima.  
	 */
    public StockMateriaPrima(MateriaPrima materiaPrima, Float cantidad, Float cantidadAsignada, Float cantidadMinima) {
        super();
        this.setMateriaPrima(materiaPrima);
        this.setCantidad(cantidad);
        this.setCantidadAsignada(cantidadAsignada);
        this.setCantidadMinima(cantidadMinima);
    }

    /**
	 * Cantidad de materia prima disponible en el stock.
	 * 
	 * @return Resultado de calcular la cantidad de materia prima disponible.
	 */
    public Float cantidadDisponible() {
        return this.getCantidad() - this.getCantidadAsignada() - this.getCantidadMinima();
    }

    /**
	 * Incrementa la cantidad de materias primaas asignada.
	 * 
	 * @param cantidadAAsignar cantidad a asignar.
	 * @throws StockMateriaPrimaInsuficiente Ocurre cuando la candidad disponible en el stock no es 
	 * sufuciente para asignar la cantidad requerida.  
	 */
    public void asignar(float cantidadAAsignar) throws StockMateriaPrimaInsuficiente {
        if (this.cantidadDisponible() <= cantidadAAsignar) {
            throw new StockMateriaPrimaInsuficiente(this.getMateriaPrima(), this.cantidadDisponible(), cantidadAAsignar);
        }
        this.setCantidadAsignada(this.getCantidadAsignada() + cantidadAAsignar);
    }

    /**
	 * Indica si el stock pertenece a la materia prima.
	 * 
	 * @param mp Materia prima consultada.
	 * @return Retorna <code>true</code> si el stock pertenece a la materia prima o 
	 * <code>false</code> en caso contrario.
	 */
    public boolean contieneMateriaPrima(MateriaPrima mp) {
        return this.getMateriaPrima().equals(mp);
    }

    /**
	 * Decrementa una cantidad de materia prima.
	 * 
	 * @param cantidad Cantidad a decrementar.
	 */
    public void decrementarStock(Float cantidad) {
        this.setCantidad(this.getCantidad() - cantidad);
        this.setCantidadAsignada(this.getCantidadAsignada() - cantidad);
    }

    /**
	 * Incrementa una cantidad de materia prima.
	 * 
	 * @param cantidad Cantidad a incrementar.
	 */
    public void incrementarStock(Float cantidad) {
        this.setCantidad(this.getCantidad() + cantidad);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getCantidad() {
        return cantidad;
    }

    public void setCantidad(Float cantidad) {
        this.cantidad = cantidad;
    }

    public MateriaPrima getMateriaPrima() {
        return materiaPrima;
    }

    public void setMateriaPrima(MateriaPrima materiaPrima) {
        this.materiaPrima = materiaPrima;
    }

    public Float getCantidadAsignada() {
        return cantidadAsignada;
    }

    public void setCantidadAsignada(Float cantidadAsignada) {
        this.cantidadAsignada = cantidadAsignada;
    }

    public Float getCantidadMinima() {
        return cantidadMinima;
    }

    public void setCantidadMinima(Float cantidadMinima) {
        this.cantidadMinima = cantidadMinima;
    }
}

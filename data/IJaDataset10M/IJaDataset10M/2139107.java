package fr.cnes.sitools.dataset.converter.dto;

import java.util.List;

/**
 * A class to represent the order of converters in the ConverterChainedModel
 * 
 * @author m.gond (AKKA Technologies)
 */
public class ConverterChainedOrderDTO {

    /** The id of the ConverterChainedModel */
    private String id;

    /** List of ids */
    private List<String> idOrder;

    /**
   * Gets the id value
   * @return the id
   */
    public String getId() {
        return id;
    }

    /**
   * Sets the value of id
   * @param id the id to set
   */
    public void setId(String id) {
        this.id = id;
    }

    /**
   * Gets the idOrder value
   * @return the idOrder
   */
    public List<String> getIdOrder() {
        return idOrder;
    }

    /**
   * Sets the value of idOrder
   * @param idOrder the idOrder to set
   */
    public void setIdOrder(List<String> idOrder) {
        this.idOrder = idOrder;
    }
}

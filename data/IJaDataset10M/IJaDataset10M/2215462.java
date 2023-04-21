package no.ugland.utransprod.service;

import java.util.List;
import no.ugland.utransprod.model.ProductArea;

/**
 * Interface for serviceklasse mot tabell PRODUCT_AREA
 * @author atle.brekka
 */
public interface ProductAreaManager {

    String MANAGER_NAME = "productAreaManager";

    /**
     * Finner alle
     * @return produktomr�der
     */
    List<ProductArea> findAll();

    /**
     * Finn basert p� navn
     * @param name
     * @return produktomr�de
     */
    ProductArea findByName(String name);

    /**
     * Sletter produktomr�de
     * @param productArea
     */
    void removeProductArea(ProductArea productArea);

    /**
     * Lagrer produktomr�de
     * @param productArea
     */
    void saveProductArea(ProductArea productArea);

    ProductArea getProductAreaForProductAreaNr(Integer productAreaNr, boolean initCache);

    List<Integer> getProductAreaNrListFromAreaName(String productAreaName);

    List<String> getAllNames();
}

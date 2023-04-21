package no.ugland.utransprod.service;

import java.util.List;
import no.ugland.utransprod.model.GulvsponPackageV;
import no.ugland.utransprod.model.PackableListItem;

/**
 * Interface for serviceklasse mot view GULVSPON_PACKAGE_V
 * @author atle.brekka
 */
public interface GulvsponPackageVManager extends IApplyListManager<PackableListItem> {

    String MANAGER_NAME = "gulvsponPackageVManager";

    /**
     * @see no.ugland.utransprod.service.IApplyListManager#findAllApplyable()
     */
    List<PackableListItem> findAllApplyable();

    /**
     * @see no.ugland.utransprod.service.IApplyListManager#findApplyableByOrderNr(java.lang.String)
     */
    List<PackableListItem> findApplyableByOrderNr(String orderNr);

    /**
     * Oppdaterer view
     * @param gulvsponPackageV
     */
    void refresh(GulvsponPackageV gulvsponPackageV);

    /**
     * @see no.ugland.utransprod.service.IApplyListManager#findApplyableByCustomerNr(java.lang.Integer)
     */
    List<PackableListItem> findApplyableByCustomerNr(Integer customerNr);
}

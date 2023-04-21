package no.ugland.utransprod.gui.action;

import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import no.ugland.utransprod.ProTransException;
import no.ugland.utransprod.gui.WindowInterface;
import no.ugland.utransprod.gui.model.ApplyListInterface;
import no.ugland.utransprod.model.ArticleType;
import no.ugland.utransprod.model.Order;
import no.ugland.utransprod.model.OrderLine;
import no.ugland.utransprod.model.PackableListItem;
import no.ugland.utransprod.model.Produceable;
import no.ugland.utransprod.model.ProductionUnit;
import no.ugland.utransprod.service.IApplyListManager;
import no.ugland.utransprod.service.ManagerRepository;
import no.ugland.utransprod.util.Util;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.internal.Nullable;

public class SetProductionUnitAction extends AbstractAction {

    private ManagerRepository managerRepository;

    private ArticleType articleType;

    private ProduceableProvider produceableProvider;

    private WindowInterface window;

    @Inject
    public SetProductionUnitAction(final ManagerRepository aManagerRepository, @Assisted ArticleType aArticleType, @Assisted @Nullable final ProduceableProvider aProduceableProvider, @Assisted final WindowInterface aWindow) {
        super("Sett prod.enhet...");
        window = aWindow;
        produceableProvider = aProduceableProvider;
        managerRepository = aManagerRepository;
        articleType = aArticleType;
    }

    public void actionPerformed(ActionEvent e) {
        Produceable produceable = produceableProvider.getSelectedProduceable();
        setProductionUnit(produceable);
    }

    public ProductionUnit setProductionUnit(Produceable produceable) {
        ProductionUnit productionUnit = null;
        List<ProductionUnit> productionUnits = managerRepository.getProductionUnitManager().findByArticleTypeProductAreaGroup(articleType, produceable.getProductAreaGroupName());
        if (productionUnits != null && productionUnits.size() != 0) {
            String productionUnitName = produceable.getProductionUnitName();
            ProductionUnit defaultProductionUnit = null;
            if (productionUnitName != null) {
                defaultProductionUnit = managerRepository.getProductionUnitManager().findByName(productionUnitName);
            }
            productionUnit = (ProductionUnit) Util.showOptionsDialogCombo(window, productionUnits, "Velg produksjonsenhet", true, defaultProductionUnit);
            setProductionUnitForOrderLine(productionUnit, produceable);
        }
        return productionUnit;
    }

    public void setProductionUnitForOrderLine(ProductionUnit productionUnit, Produceable produceable) {
        if (productionUnit != null && produceable != null) {
            OrderLine orderLine = managerRepository.getOrderLineManager().findByOrderLineId(produceable.getOrderLineId());
            if (orderLine != null) {
                orderLine.setProductionUnit(productionUnit);
                refreshAndSaveOrder(orderLine);
                produceable.setProductionUnitName(productionUnit.getProductionUnitName());
            }
        }
    }

    private void refreshAndSaveOrder(OrderLine orderLine) {
        Order order = orderLine.getOrder();
        try {
            managerRepository.getOrderManager().refreshObject(order);
            order.setStatus(null);
            managerRepository.getOrderManager().saveOrder(order);
            managerRepository.getOrderLineManager().saveOrderLine(orderLine);
        } catch (ProTransException e) {
            Util.showErrorDialog(window, "Feil", e.getMessage());
            e.printStackTrace();
        }
    }
}

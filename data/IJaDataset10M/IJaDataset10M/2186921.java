package no.ugland.utransprod.gui.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import no.ugland.utransprod.ProTransException;
import no.ugland.utransprod.gui.ArticlePackageViewFactory;
import no.ugland.utransprod.gui.ArticleProductionPackageView;
import no.ugland.utransprod.gui.Login;
import no.ugland.utransprod.gui.WindowInterface;
import no.ugland.utransprod.gui.handlers.ArticlePackageViewHandler.PackProduction;
import no.ugland.utransprod.model.Order;
import no.ugland.utransprod.model.OrderLine;
import no.ugland.utransprod.model.Produceable;
import no.ugland.utransprod.service.ManagerRepository;
import no.ugland.utransprod.service.TakstolProductionVManager;
import no.ugland.utransprod.util.Util;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class TakstolProductionApplyList extends ProductionApplyList {

    private ArticleProductionPackageView articlePackageView;

    private String mainArticleName;

    @Inject
    public TakstolProductionApplyList(@Named(value = "takstolColliName") final String colliName, final Login login, @Named(value = "takstol_article") final String aMainArticleName, final ManagerRepository managerRepository, final ArticlePackageViewFactory aArticlePackageViewFactory) {
        super(login, managerRepository.getTakstolProductionVManager(), colliName, "Takstol", null, managerRepository);
        mainArticleName = aMainArticleName;
        articlePackageView = aArticlePackageViewFactory.create(managerRepository.getArticleTypeManager().findByName("Takstoler"), this, colliName);
    }

    public void setStarted(final Produceable object, final boolean started) {
        if (object != null) {
            List<Applyable> relatedArticles = object.getRelatedArticles();
            List<Applyable> objects = new ArrayList<Applyable>();
            Date startedDate = started ? Util.getCurrentDate() : null;
            if (object.getOrderLineId() != null) {
                objects.add(object);
            }
            if (relatedArticles != null && relatedArticles.size() != 0) {
                objects.addAll(relatedArticles);
            }
            setStartedDate(objects, startedDate);
        }
    }

    public void settStartetKapping(final Produceable produceable, Date startetKappingDato) {
        OrderLine orderLine = managerRepository.getOrderLineManager().findByOrderLineId(produceable.getOrderLineId());
        if (orderLine != null) {
            orderLine.setCuttingStarted(startetKappingDato);
            managerRepository.getOrderLineManager().saveOrderLine(orderLine);
            applyListManager.refresh(produceable);
        }
    }

    public void settKappingFerdig(final Produceable produceable, Date kappingFerdigDato) {
        OrderLine orderLine = managerRepository.getOrderLineManager().findByOrderLineId(produceable.getOrderLineId());
        if (orderLine != null) {
            orderLine.setCuttingDone(kappingFerdigDato);
            managerRepository.getOrderLineManager().saveOrderLine(orderLine);
            applyListManager.refresh(produceable);
        }
    }

    private void setStartedDate(List<Applyable> objects, Date startedDate) {
        for (Applyable object : objects) {
            OrderLine orderLine = managerRepository.getOrderLineManager().findByOrderLineId(object.getOrderLineId());
            if (orderLine != null) {
                orderLine.setActionStarted(startedDate);
                managerRepository.getOrderLineManager().saveOrderLine(orderLine);
                applyListManager.refresh((Produceable) object);
            }
        }
    }

    public void setApplied(Produceable object, boolean applied, WindowInterface window) {
        try {
            if (object != null) {
                List<Applyable> relatedArticles = object.getRelatedArticles();
                List<Applyable> objects = new ArrayList<Applyable>();
                object.setProduced(getProducedDate(object, applied));
                if (object.getOrderLineId() != null) {
                    objects.add(object);
                }
                if (relatedArticles != null && relatedArticles.size() != 0) {
                    objects.addAll(relatedArticles);
                    for (Applyable item : objects) {
                        item.setProduced(getProducedDate(item, applied));
                    }
                }
                applyAllObjects(applied, window, objects);
                if (applied) {
                    articlePackageView.setArticles(objects, PackProduction.PRODUCTION);
                    Util.showEditViewable(articlePackageView, window);
                }
                applyAllObjects(applied, window, objects);
                setOrderReady(object);
                checkCompleteness(object.getColli(), applied);
            }
        } catch (ProTransException e) {
            Util.showErrorDialog(window, "Feil", e.getMessage());
            e.printStackTrace();
        }
    }

    private void applyAllObjects(boolean applied, WindowInterface window, List<Applyable> objects) {
        boolean relatedNotPacked = false;
        for (Applyable item : objects) {
            item.setColli(applied ? item.getColli() : null);
            relatedNotPacked = relatedNotPacked ? relatedNotPacked : item.getColli() == null;
            applyObject((Produceable) item, item.getProduced() != null, window);
        }
    }

    private void setOrderReady(Produceable object) throws ProTransException {
        if (object.getProductAreaGroupName().equalsIgnoreCase("Takstol")) {
            Order order = managerRepository.getOrderManager().findByOrderNr(object.getOrderNr());
            if (isAllProduced(object)) {
                order.setOrderReady(Util.getCurrentDate());
            } else {
                order.setOrderReady(null);
            }
            managerRepository.getOrderManager().saveOrder(order);
        }
    }

    private boolean isAllProduced(Produceable object) {
        if (object.getProduced() != null) {
            return object.isRelatedArticlesComplete();
        }
        return false;
    }

    private void applyObject(Produceable object, boolean applied, WindowInterface window) {
        object.setProduced(getProducedDate(object, applied));
        OrderLine orderLine = managerRepository.getOrderLineManager().findByOrderLineId(object.getOrderLineId());
        if (orderLine != null) {
            String aColliName = Util.getColliName(object.getArticleName());
            handleApply(object, applied, window, aColliName);
        }
    }

    private Date getProducedDate(Applyable applyable, boolean applied) {
        if (applied) {
            return applyable.getProduced() == null ? Util.getCurrentDate() : applyable.getProduced();
        }
        return null;
    }

    public Produceable getApplyObject(final Transportable transportable, final WindowInterface window) {
        List<Produceable> list = ((TakstolProductionVManager) applyListManager).findApplyableByOrderNrAndArticleName(transportable.getOrder().getOrderNr(), mainArticleName);
        return list == null || list.size() == 0 ? null : list != null && list.size() == 1 ? list.get(0) : selectApplyObject(list, window);
    }

    private Produceable selectApplyObject(final List<Produceable> list, final WindowInterface window) {
        return (Produceable) Util.showOptionsDialogCombo(window, list, "Velg artikkel", true, null);
    }
}

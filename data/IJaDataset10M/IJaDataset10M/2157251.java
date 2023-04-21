package br.perfiman.faces;

import java.util.List;
import javax.faces.model.SelectItem;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import br.perfiman.helpers.GeneralComboHelper;
import br.perfiman.model.Asset;
import br.perfiman.model.Broker;
import br.perfiman.model.Trade;
import br.perfiman.service.AbstractService;
import br.perfiman.service.TradeService;
import br.perfiman.utils.ServiceLocator;

public class TradeBean extends AbstractBean<Trade> {

    public TradeBean() throws Exception {
        if (getEntity() == null) {
            setEntity(new Trade());
            getEntity().setAsset(new Asset());
            getEntity().setBroker(new Broker());
        }
        retrieveAll();
    }

    public List<SelectItem> getAssets() throws Exception {
        return GeneralComboHelper.getAssets();
    }

    public List<SelectItem> getBrokers() throws Exception {
        return GeneralComboHelper.getBrokers();
    }

    @Override
    protected AbstractService<Trade> getService() throws Exception {
        if (service == null) {
            service = (TradeService) ServiceLocator.getInstance().get("TradeServiceBean/remote");
        }
        return service;
    }

    public void teste() {
        TimeSeries series = new TimeSeries("Teste", Month.class);
        series.add(new Month(1, 2007), ((TradeService) service).getMonthsProfit(2, 2008));
        series.add(new Month(4, 2008), ((TradeService) service).getMonthsProfit(4, 2008));
        XYDataset dataset = new TimeSeriesCollection(series);
    }
}

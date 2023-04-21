package net.sf.jmoney.pricehistory.external;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jmoney.model2.Commodity;
import net.sf.jmoney.pricehistory.CommodityPricing;
import net.sf.jmoney.pricehistory.CommodityPricingInfo;
import net.sf.jmoney.pricehistory.Price;
import net.sf.jmoney.pricehistory.PriceInfo;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

public class Prices {

    private Commodity commodity;

    private IPriceFetcher fetcher;

    public Prices(Commodity commodity) {
        this.commodity = commodity;
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        final Map<String, IConfigurationElement> extensionMap = new HashMap<String, IConfigurationElement>();
        List<IPriceFetcher> allFetchers = new ArrayList<IPriceFetcher>();
        for (IConfigurationElement element : registry.getConfigurationElementsFor("net.sf.jmoney.pricehistory.sources")) {
            if (element.getName().equals("source")) {
                String commodityClass = element.getAttribute("instanceOf");
                try {
                    if (commodityClass == null || Class.forName(commodityClass).isAssignableFrom(commodity.getClass())) {
                        IPriceFetcher eachFetcher = (IPriceFetcher) element.createExecutableExtension("class");
                        if (eachFetcher.canProcess(commodity)) {
                            allFetchers.add(eachFetcher);
                        }
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (CoreException e) {
                    e.printStackTrace();
                }
            }
        }
        if (allFetchers.size() == 0) {
            throw new RuntimeException("no source for prices for commodity available");
        } else if (allFetchers.size() == 1) {
            fetcher = allFetchers.get(0);
        } else {
            fetcher = allFetchers.get(0);
        }
    }

    public Price getPrice(Date date) {
        CommodityPricing pricing = commodity.getExtension(CommodityPricingInfo.getPropertySet(), true);
        for (Price price : pricing.getPriceCollection()) {
            if (price.getDate().equals(date)) {
                return price;
            }
        }
        Long priceAmount = fetcher.getPrice(date);
        if (priceAmount != null) {
            Price newPrice = pricing.getPriceCollection().createNewElement(PriceInfo.getPropertySet());
            newPrice.setDate(date);
            newPrice.setPrice(priceAmount);
            return newPrice;
        }
        return null;
    }

    public Price[] getPrices(Date startDate, Date endDate) {
        throw new RuntimeException("not implemented");
    }
}

package net.sourceforge.nattable.examples.tutorial.styling;

import java.util.List;
import net.sourceforge.nattable.NatTable;
import net.sourceforge.nattable.data.IDataProvider;
import net.sourceforge.nattable.data.ListDataProvider;
import net.sourceforge.nattable.dataset.pricing.ColumnHeaders;
import net.sourceforge.nattable.dataset.pricing.PricingDataBean;
import net.sourceforge.nattable.dataset.pricing.PricingDataBeanGenerator;
import net.sourceforge.nattable.examples.AbstractNatExample;
import net.sourceforge.nattable.examples.runner.StandaloneNatExampleRunner;
import net.sourceforge.nattable.grid.data.DefaultColumnHeaderDataProvider;
import net.sourceforge.nattable.grid.layer.DefaultGridLayer;
import net.sourceforge.nattable.layer.ILayer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class CustomStyleExample extends AbstractNatExample {

    public static void main(String[] args) throws Exception {
        StandaloneNatExampleRunner.run(new CustomStyleExample());
    }

    @Override
    public String getName() {
        return "Custom styling";
    }

    public Control createExampleControl(Composite parent) {
        List<PricingDataBean> dataList = PricingDataBeanGenerator.getData(1000);
        String[] propertyNames = ColumnHeaders.getProperties();
        IDataProvider bodyDataProvider = new ListDataProvider<PricingDataBean>(dataList, propertyNames);
        IDataProvider columnHeaderDataProvider = new DefaultColumnHeaderDataProvider(ColumnHeaders.getLabels());
        ILayer layer = new DefaultGridLayer(bodyDataProvider, columnHeaderDataProvider);
        return new NatTable(parent, layer);
    }
}

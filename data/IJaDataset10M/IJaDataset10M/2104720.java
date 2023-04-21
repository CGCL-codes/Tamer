package pl.edu.agh.ssm.web.client.component;

import pl.edu.agh.ssm.web.client.rpc.SSMServerConnector;
import pl.edu.agh.ssm.web.client.rpc.SSMServerConnectorAsync;
import pl.edu.agh.ssm.web.client.rpc.wsComponent.MBeanInfo;
import pl.edu.agh.ssm.web.service.SSMServerConnectorImpl;
import com.extjs.gxt.ui.client.data.BaseListLoadConfig;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ChartWindow extends ChartWindowAbstract {

    private final MBeanInfo mbi;

    private final String attributeName;

    @Override
    public String getID() {
        return super.getID() + "_" + attributeName;
    }

    protected void load(BaseListLoadConfig loadConfig, AsyncCallback callback) {
        final SSMServerConnectorAsync serverConnectorAsync = SSMServerConnector.App.getInstance();
        serverConnectorAsync.getPropValues(ChartWindow.this.mbi, ChartWindow.this.attributeName, dchd.getDateFrom(), dchd.getDateTo(), callback);
    }

    public ChartWindow(MBeanInfo mbi, String attributeName) {
        super(mbi.getSsmComponentName());
        this.mbi = mbi;
        this.attributeName = attributeName;
        refresh();
    }

    protected void showChart() {
        super.showChart();
        SSMServerConnectorAsync service = SSMServerConnectorImpl.App.getInstance();
        service.getChart(ChartWindow.this.mbi, ChartWindow.this.attributeName, dchd.getDateFrom(), dchd.getDateTo(), ChartWindow.this);
    }

    @Override
    protected void refresh() {
        showChart();
        listLoader.load();
        layout();
    }
}

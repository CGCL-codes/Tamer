package hotair.component;

import java.util.logging.Level;
import java.util.logging.Logger;
import jmcf.core.DataPullInputInterfaceInfo;
import jmcf.core.DataPushOutputInterfaceInfo;
import jmcf.core.IPullData;
import jmcf.core.IPushData;
import jmcf.core.InterfaceInfo;
import jmcf.core.ServiceClientInterfaceInfo;
import jmcf.distribution.INetworkFileReader;

/**
 *
 * @author Mauro Dragone
 */
public class Translator extends AbstractHotairProcess {

    protected StringBuffer bundle = new StringBuffer();

    protected int nFile;

    protected ie.ucd.hotair.system.Translator trans = new ie.ucd.hotair.system.Translator();

    public Translator() {
        super();
    }

    public Translator(String name) {
        super(name);
    }

    /**
     * Added to allow setting of the Logger level for this type of component
    @Override
    public void init() {
        super.init();
        Logger.getLogger(getClass().getName()).setLevel(Level.ALL);
    }
     */
    public InterfaceInfo[] getInterfacesInfo() {
        return new InterfaceInfo[] { new DataPullInputInterfaceInfo("input", String.class), new DataPushOutputInterfaceInfo("output", String.class), new ServiceClientInterfaceInfo("nfr", INetworkFileReader.class) };
    }

    ;

    public boolean execute() {
        IPullData pullData = (IPullData) this.getClientService("input");
        Object data = null;
        try {
            data = pullData.pull();
        } catch (Throwable th) {
            th.printStackTrace();
        }
        if (data == null) {
            Logger.getLogger(getClass().getName()).log(Level.INFO, "[" + this.getName() + "]: Firing no data exception");
            try {
                fireException("noData");
            } catch (Throwable th) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "[" + this.getName() + "]: Exception occurred", th);
            }
            Logger.getLogger(getClass().getName()).log(Level.INFO, "[" + this.getName() + "]: Fired no data exception");
            return false;
        }
        bundle.setLength(0);
        nFile = 0;
        processBundle((String) data);
        IPushData pushData = (IPushData) this.getClientService("output");
        pushData.push(bundle.toString());
        return true;
    }

    @Override
    protected void processFile(String filePath) {
        String outFileName = trans.translate(filePath);
        bundle.append(getHostName()).append(",").append(outFileName).append("@");
        nFile++;
    }
}

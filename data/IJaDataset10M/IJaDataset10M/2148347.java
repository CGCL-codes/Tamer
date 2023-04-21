package openfarm.networking;

import java.net.UnknownHostException;
import openfarm.pojos.InterpreterNetworkingPojo;
import openfarm.properties.PropertiesReader;
import openfarmtools.interpreter.exceptions.AnalysisComponentNotFoundException;
import openfarmtools.interpreter.exceptions.InvalidRegisterConfigurationException;
import openfarmtools.networking.Networking;
import openfarmtools.util.OpenFarmUtil;
import org.apache.log4j.Logger;
import util.logging.LogUtil;

public class InterpreterNetworking extends Networking<InterpreterNetworkingPojo> {

    private static final Logger log = LogUtil.getLogger(InterpreterNetworking.class);

    private PropertiesReader prop = null;

    public InterpreterNetworking(PropertiesReader prop) throws UnknownHostException {
        this.prop = prop;
    }

    @Override
    public InterpreterNetworkingPojo networkObjectFactory() throws AnalysisComponentNotFoundException, InvalidRegisterConfigurationException {
        InterpreterNetworkingPojo inp = new InterpreterNetworkingPojo();
        try {
            String ip = prop.getProperty("jaxws.server.host");
            if (!OpenFarmUtil.isEmptyString(ip)) inp.setIp(ip); else inp.setIp(getIpAddress());
        } catch (UnknownHostException e) {
            log.warn("Could not get ip from local machine where CCU is running");
            inp.setIp("N/A");
        }
        inp.setPort(Integer.parseInt(prop.getProperty("jaxws.server.port")));
        inp.setWsContext(prop.getProperty("jaxws.server.ws.context"));
        inp.setJaxwsFullUrl("http://" + inp.getIp() + ":" + inp.getPort() + "/" + inp.getWsContext());
        prop.validateBoolean("manager.must.be.alive");
        inp.setManagerMustBeAlive(Boolean.parseBoolean(prop.getProperty("manager.must.be.alive")));
        inp.setManagerUrl(prop.getProperty("manager.url"));
        if (OpenFarmUtil.isEmptyString(System.getProperty("os.name"))) throw new AnalysisComponentNotFoundException("Please set environment variable \"os.name\" in your operating system"); else inp.setBasedOS(System.getProperty("os.name").toLowerCase());
        return inp;
    }
}

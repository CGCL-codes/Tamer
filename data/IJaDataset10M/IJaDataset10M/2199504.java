package fr.cantor.commore.tests;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import fr.cantor.commore.Commore;
import fr.cantor.commore.comm.ClassLoaderServiceFactory;
import fr.cantor.commore.comm.ServiceFactories;
import fr.cantor.commore.comm.ServiceManager;
import fr.cantor.commore.comm.address.ProtocolData;
import fr.cantor.commore.comm.address.TcpProtocolData;
import fr.cantor.commore.tests.manager.CampaignManager;
import fr.cantor.commore.tests.manager.CommandLineManager;
import fr.cantor.commore.tests.perf.PTestPerf;
import fr.cantor.commore.tests.utils.CodesError;
import fr.cantor.commore.tests.utils.ServerProperties;

/**
 * 
 * @author Da Costa Daniel and Baron Erwan
 *
 */
public class Server {

    public static void main(String[] args) {
        Commore.logger.setLevel(Level.INFO);
        try {
            CommandLineManager lineManager = new CommandLineManager();
            if (!lineManager.analyzed(args)) {
                System.exit(CodesError.ERROR_PARAMETER);
            }
            ServiceManager manager = new ServiceManager();
            PTestPerf pTestPerf = publish(manager);
            CampaignManager campaignManager = new CampaignManager(manager, pTestPerf);
            List<File> campaigns = new LinkedList<File>();
            for (String campaign : ServerProperties.getCampaigns()) {
                campaigns.add(new File(ServerProperties.getCampaignDir() + File.separator + campaign));
            }
            campaignManager.execute(campaigns);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(CodesError.FAILED);
        }
    }

    private static PTestPerf publish(ServiceManager manager) throws IOException, Exception {
        ProtocolData tcpPublishAddress = TcpProtocolData.fromString(ServerProperties.getPort());
        manager.addListener(tcpPublishAddress);
        ServiceFactories.addServiceFactory(new ClassLoaderServiceFactory("fr.cantor.commore.tests.service.TestPerfImpl"), "fr.cantor.commore.tests.perf.TestPerf");
        PTestPerf pTestPerf = new PTestPerf();
        pTestPerf.create("perfService");
        manager.publish(pTestPerf);
        return pTestPerf;
    }
}

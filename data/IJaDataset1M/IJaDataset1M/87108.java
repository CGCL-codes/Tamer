package com.rif.server.service.server;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import com.rif.common.serializer.DataSerializerManager;
import com.rif.common.serializer.IDataSerializer;
import com.rif.server.service.definiton.ServiceModelManager;
import com.rif.server.service.definiton.TransportModel;
import com.rif.server.service.definiton.TransportModelManager;
import com.rif.server.service.finder.IServiceFinder;
import com.rif.server.service.finder.ServiceFinderManager;
import com.rif.server.service.manager.ServiceEndpointManagerFactory;
import com.rif.server.service.parser.ServiceModelParserFactory;
import com.rif.server.service.transport.ITransportServer;
import com.rif.server.service.transport.ITransportServerFactory;
import com.rif.server.service.transport.TransportServerFactoryProvider;

/**
 * @author bruce.liu (mailto:jxta.liu@gmail.com)
 * 2011-7-26 下午09:33:10
 */
public class RIFServer {

    private static List<ITransportServer> transportServerList = new ArrayList<ITransportServer>();

    static {
        ServiceLoader<IServiceFinder> serviceFinderLoader = ServiceLoader.load(IServiceFinder.class);
        for (IServiceFinder finder : serviceFinderLoader) {
            ServiceFinderManager.INSTANCE.regiester(finder);
        }
    }

    static {
        ServiceLoader<IDataSerializer> dataSerializerLoader = ServiceLoader.load(IDataSerializer.class);
        for (IDataSerializer dataSerializer : dataSerializerLoader) {
            DataSerializerManager.INSTANCE.regiest(dataSerializer);
        }
    }

    public static boolean start(InputStream[] inputs) {
        ServiceModelParserFactory.INSTANCE.createParser(inputs).parser();
        Map<String, TransportModel> transportModelMap = TransportModelManager.INSTANCE.lookup();
        for (TransportModel transportModel : transportModelMap.values()) {
            ITransportServerFactory factory = TransportServerFactoryProvider.INSTANCE.lookup(transportModel);
            if (null != factory) {
                ITransportServer transportServer = factory.create(transportModel);
                try {
                    transportServer.start();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
                transportServerList.add(transportServer);
            }
        }
        return true;
    }

    public static boolean stop() {
        for (ITransportServer transportServer : transportServerList) {
            try {
                transportServer.stop();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        TransportModelManager.INSTANCE.destroy();
        ServiceModelManager.INSTANCE.destroy();
        ServiceEndpointManagerFactory.INSTANCE.getServiceManager().destroy();
        return true;
    }
}

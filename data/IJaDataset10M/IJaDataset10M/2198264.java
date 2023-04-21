package info.reflectionsofmind.connexion.platform.gui.join;

import info.reflectionsofmind.connexion.platform.core.transport.IClientTransportFactory;
import java.util.List;
import javax.swing.JComboBox;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

public final class TransportComboBox extends JComboBox {

    public TransportComboBox(final JoinGameFrame joinGameFrame) {
        super(wrap(joinGameFrame.getClient().getApplication().getClientTransportFactories()).toArray());
    }

    public IClientTransportFactory getSelectedTransportFactory() {
        return ((TransportWrapper) getSelectedItem()).getTransportFactory();
    }

    private static List<TransportWrapper> wrap(List<IClientTransportFactory> transportFactorys) {
        return Lists.transform(transportFactorys, new Function<IClientTransportFactory, TransportWrapper>() {

            @Override
            public TransportWrapper apply(IClientTransportFactory transportFactory) {
                return new TransportWrapper(transportFactory, transportFactory.getName());
            }
        });
    }

    public static final class TransportWrapper {

        private final IClientTransportFactory transportFactory;

        private final String name;

        private TransportWrapper(final IClientTransportFactory transportFactory, final String name) {
            this.transportFactory = transportFactory;
            this.name = name;
        }

        public IClientTransportFactory getTransportFactory() {
            return this.transportFactory;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}

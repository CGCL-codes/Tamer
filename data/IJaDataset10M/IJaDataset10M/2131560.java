package pl.bristleback.server.bristle.conf.resolver;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import pl.bristleback.server.bristle.api.ConnectionStateListener;
import pl.bristleback.server.bristle.api.DataController;
import pl.bristleback.server.bristle.api.MessageDispatcher;
import pl.bristleback.server.bristle.api.ObjectSender;
import pl.bristleback.server.bristle.api.SerializationEngine;
import pl.bristleback.server.bristle.api.ServerEngine;
import pl.bristleback.server.bristle.api.SystemValueSerializer;
import pl.bristleback.server.bristle.conf.BristleConfig;
import pl.bristleback.server.bristle.conf.DataControllers;
import pl.bristleback.server.bristle.conf.EngineConfig;
import pl.bristleback.server.bristle.conf.InitialConfiguration;
import pl.bristleback.server.bristle.conf.MessageConfiguration;
import pl.bristleback.server.bristle.integration.spring.BristleSpringIntegration;
import pl.bristleback.server.bristle.listener.ListenersContainer;
import pl.bristleback.server.bristle.serialization.FormatType;
import pl.bristleback.server.bristle.utils.ReflectionUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-04-30 15:54:19 <br/>
 *
 * @author Wojciech Niemiec
 */
@Configuration
@Lazy
public class SpringConfigurationResolver {

    private static Logger log = Logger.getLogger(SpringConfigurationResolver.class.getName());

    public static final String CONFIG_BEAN_NAME = "bristlebackConfiguration";

    private BristleSpringIntegration springIntegration;

    private InitialConfiguration initialConfiguration;

    @Bean
    public BristleConfig bristlebackConfigurationFinal() {
        BristleConfig configuration = bristlebackConfiguration();
        configuration.setServerEngine(serverEngine());
        configuration.setSerializationEngine(serializationEngine());
        configuration.setMessageConfiguration(messageConfiguration());
        configuration.setDataControllers(dataControllers());
        configuration.setListenersContainer(listenersContainer());
        return configuration;
    }

    @Bean(name = CONFIG_BEAN_NAME)
    public BristleConfig bristlebackConfiguration() {
        BristleConfig configuration = new BristleConfig();
        configuration.setSpringIntegration(springIntegration);
        configuration.setInitialConfiguration(initialConfiguration);
        return configuration;
    }

    @Bean
    public ServerEngine serverEngine() {
        EngineConfig engineConfiguration = initialConfiguration.getEngineConfiguration();
        String expectedEngineName = engineConfiguration.getName();
        ServerEngine serverEngine = springIntegration.getBean(expectedEngineName, ServerEngine.class);
        serverEngine.setConfiguration(bristlebackConfiguration());
        return serverEngine;
    }

    @Bean
    public SerializationEngine serializationEngine() {
        String serializationEngineName = initialConfiguration.getSerializationEngine();
        SerializationEngine serializationEngine = springIntegration.getBean(serializationEngineName, SerializationEngine.class);
        serializationEngine.init(bristlebackConfiguration());
        return serializationEngine;
    }

    @Bean
    public DataControllers dataControllers() {
        Map<String, DataController> dataControllersMap = new HashMap<String, DataController>();
        for (String acceptedControllerName : initialConfiguration.getAcceptedControllerNames()) {
            DataController controller = springIntegration.getBean(acceptedControllerName, DataController.class);
            controller.init(bristlebackConfiguration());
            dataControllersMap.put(acceptedControllerName, controller);
        }
        DataController defaultController = dataControllersMap.get(initialConfiguration.getDefaultControllerName());
        return new DataControllers(dataControllersMap, defaultController);
    }

    @Bean
    public ListenersContainer listenersContainer() {
        Map<String, ConnectionStateListener> handlers = springIntegration.getBeansOfType(ConnectionStateListener.class);
        List<ConnectionStateListener> connectionStateListeners = new ArrayList<ConnectionStateListener>(handlers.values());
        return new ListenersContainer(connectionStateListeners);
    }

    @Bean
    public MessageConfiguration messageConfiguration() {
        MessageConfiguration messageConfiguration = new MessageConfiguration();
        messageConfiguration.setMessageDispatcher(messageDispatcher());
        messageConfiguration.setMessageSenders(messageSenders());
        messageConfiguration.setSystemSerializers(systemSerializers());
        return messageConfiguration;
    }

    @Bean
    public Map<Class, SystemValueSerializer> systemSerializers() {
        Map<Class, SystemValueSerializer> systemSerializers = new HashMap<Class, SystemValueSerializer>();
        Map<String, SystemValueSerializer> serializers = springIntegration.getBeansOfType(SystemValueSerializer.class);
        FormatType formatType = serializationEngine().getFormatType();
        for (SystemValueSerializer serializer : serializers.values()) {
            if (serializer.getFormatType() == formatType) {
                Class serializerClass = serializer.getClass();
                Class parameterClass = (Class) ReflectionUtils.getParameterTypes(serializerClass, SystemValueSerializer.class)[0];
                systemSerializers.put(parameterClass, serializer);
                serializer.init(bristlebackConfiguration());
            }
        }
        return systemSerializers;
    }

    @Bean
    public MessageDispatcher messageDispatcher() {
        String dispatcherName = initialConfiguration.getMessageDispatcher();
        MessageDispatcher dispatcher = springIntegration.getBean(dispatcherName, MessageDispatcher.class);
        dispatcher.setServer(serverEngine());
        return dispatcher;
    }

    @Bean
    public Map<String, ObjectSender> messageSenders() {
        ServerEngine serverEngine = serverEngine();
        MessageDispatcher messageDispatcher = messageDispatcher();
        Map<String, ObjectSender> senders = springIntegration.getBeansOfType(ObjectSender.class);
        for (ObjectSender sender : senders.values()) {
            sender.setMessageDispatcher(messageDispatcher);
            sender.setServer(serverEngine);
        }
        return senders;
    }

    public void setSpringIntegration(BristleSpringIntegration springIntegration) {
        this.springIntegration = springIntegration;
    }

    public void setInitialConfiguration(InitialConfiguration initialConfiguration) {
        this.initialConfiguration = initialConfiguration;
    }
}

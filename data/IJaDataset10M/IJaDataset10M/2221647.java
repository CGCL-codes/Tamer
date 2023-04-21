package clubmixer.commons.plugins.communication;

import com.slychief.clubmixer.commons.plugins.communication.PluginCommunicationChannel;
import com.slychief.clubmixer.commons.plugins.communication.PluginInvocationResult.Data.Entry;
import com.slychief.clubmixer.server.library.entities.Song;
import com.slychief.clubmixer.server.library.entities.SongArray;
import java.util.HashMap;

/**
 *
 * @author Alexander Schindler
 */
public class ClientsideCommunicationEndpoint implements ICommunicationChannel {

    private final PluginCommunicationChannel comPort;

    private String pluginName;

    /**
     * Constructs ...
     *
     *
     * @param _comPort
     */
    public ClientsideCommunicationEndpoint(PluginCommunicationChannel _comPort) {
        comPort = _comPort;
    }

    /**
     * Method description
     *
     *
     * @param request
     */
    @Override
    public void fireAndForget(PluginInvocationMessage request) {
        comPort.fireAndForget(request.getPluginName(), request.getOperation(), request.getInputParamentersAsJaxbStringArray());
    }

    /**
     * Method description
     *
     *
     * @param operation
     * @param params
     */
    @Override
    public void fireAndForget(String operation, HashMap params) {
        PluginInvocationMessage request = new PluginInvocationMessage(pluginName, operation, params);
        fireAndForget(request);
    }

    /**
     * Method description
     *
     *
     * @param request
     *
     * @return
     */
    @Override
    public PluginInvocationResult requestResponse(PluginInvocationMessage request) {
        com.slychief.clubmixer.commons.plugins.communication.PluginInvocationResult result = comPort.requestResponse(request.getPluginName(), request.getOperation(), request.getInputParamentersAsJaxbStringArray());
        PluginInvocationResult pir = convertToOriginalType(result);
        return pir;
    }

    /**
     * Method description
     *
     *
     * @param operation
     * @param params
     *
     * @return
     */
    @Override
    public PluginInvocationResult requestResponse(String operation, HashMap params) {
        PluginInvocationMessage request = new PluginInvocationMessage(pluginName, operation, params);
        request.setInvocationType(ICommunicationChannel.STANDARD_INVOCATION);
        return requestResponse(request);
    }

    /**
     * Method description
     *
     *
     * @param request
     *
     * @return
     */
    @Override
    public Song[] querySongs(PluginInvocationMessage request) {
        SongArray songs = comPort.querySongs(request.getPluginName(), request.getOperation(), request.getInputParamentersAsJaxbStringArray());
        Song[] result = songs.getItem().toArray(new Song[songs.getItem().size()]);
        return result;
    }

    /**
     * Method description
     *
     *
     * @param operation
     * @param params
     *
     * @return
     */
    @Override
    public Song[] querySongs(String operation, HashMap params) {
        PluginInvocationMessage request = new PluginInvocationMessage(pluginName, operation, params);
        request.setInvocationType(ICommunicationChannel.STANDARD_INVOCATION);
        return querySongs(request);
    }

    /**
     * Method description
     *
     *
     * @param request
     *
     * @return
     */
    @Override
    public Song querySong(PluginInvocationMessage request) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private PluginInvocationResult convertToOriginalType(com.slychief.clubmixer.commons.plugins.communication.PluginInvocationResult input) {
        PluginInvocationResult output = new PluginInvocationResult();
        HashMap data = new HashMap();
        for (Entry entry : input.getData().getEntry()) {
            data.put(entry.getKey(), entry.getValue());
        }
        output.setData(data);
        return output;
    }

    /**
     * Method description
     *
     *
     * @param paramType
     * @param methodName
     * @param <T>
     *
     * @return
     */
    @Override
    public <T> GenericRemoteMethod getGenericRemoteMethod(Class<T> paramType, String methodName) {
        return new GenericRemoteMethod(paramType, pluginName, methodName, this);
    }

    /**
     * Method description
     *
     *
     * @param name
     */
    @Override
    public void setPluginName(String name) {
        pluginName = name;
    }
}

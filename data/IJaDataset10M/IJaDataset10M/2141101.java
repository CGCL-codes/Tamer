package net.solarnetwork.loadtest.client;

import java.util.LinkedHashMap;
import java.util.Map;
import net.solarnetwork.loadtest.TaskState;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

/**
 * Post new power generation.
 * 
 * @author matt
 * @version $Revision: 1211 $
 */
public class PostPower extends BaseHttpClient {

    @Override
    public HttpUriRequest createTaskRequest(TaskState state, String url) {
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("pvAmps", String.valueOf(Math.random() * 6));
        params.put("pvVolts", "230");
        params.put("locationId", state.getNodeState().getLocationIds().get("price").toString());
        HttpPost post = setupEntityForParameters(state, url, params);
        return post;
    }
}

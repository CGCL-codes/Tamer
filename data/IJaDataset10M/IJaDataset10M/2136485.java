package fi.vtt.noen.mfw.bundle.server.plugins.rest.resources;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import fi.vtt.noen.mfw.bundle.common.Logger;
import fi.vtt.noen.mfw.bundle.server.plugins.rest.RestPlugin;
import fi.vtt.noen.mfw.bundle.server.shared.datamodel.Value;

@Path("/client/history")
public class MeasurementHistoryResource {

    private static final Logger log = new Logger(MeasurementHistoryResource.class);

    private RestPlugin restPlugin;

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public MeasurementHistory requestHistory(@HeaderParam("authorization") String authHeader, HistoryRequest request) {
        restPlugin = RestPlugin.getInstance();
        log.debug("measurementHistory request");
        MeasurementHistory measurementHistory = null;
        if (restPlugin.isAlive(authHeader)) {
            measurementHistory = new MeasurementHistory();
            List<Value> measurements = restPlugin.getHistory(request);
            ArrayList<MeasurementValue> values = new ArrayList<MeasurementValue>();
            if (measurements != null) {
                for (Value value : measurements) {
                    String bmid = Long.toString(value.getBm().getBmId());
                    Long timestamp = value.getTime().getTime();
                    values.add(new MeasurementValue(bmid, value.valueString(), timestamp));
                }
            }
            measurementHistory.setValues(values);
        } else {
            throw new WebApplicationException(Status.BAD_REQUEST);
        }
        return measurementHistory;
    }
}

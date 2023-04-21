package servlet.portfolio;

import java.math.BigDecimal;
import java.util.logging.Level;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import servlet.LISEServlet;

@SuppressWarnings("serial")
public class PortfolioPick extends LISEServlet {

    @Override
    protected JSONObject doAction(JSONObject jRequest) throws JSONException {
        if (jRequest.has("id")) {
            try {
                System.out.println("find id");
                request.get().getSession().setAttribute("portfolio", jRequest.getString("id"));
            } catch (Exception e) {
                LOGGER.log(Level.INFO, this.getClass().getName(), e);
                return proceedError(e.getLocalizedMessage());
            } finally {
            }
            JSONObject jResponse = new JSONObject();
            jResponse.put("status", 0);
            return jResponse;
        } else {
            return proceedError("no portfolio selected");
        }
    }
}

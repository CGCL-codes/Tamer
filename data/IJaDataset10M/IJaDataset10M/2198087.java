package nhb.webflag.web;

import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nhb.webflag.db.DBHandler;
import nhb.webflag.db.DBHandlerPool;
import nhb.webflag.web.details.DetailReader;
import nhb.webflag.web.details.DetailRequestParser;
import org.apache.velocity.VelocityContext;

/**
 * display detail information about an character
 *
 * @author hendrik
 */
public class CharacterDetails implements ServletHandler {

    public final void handleRequest(HttpServletRequest request, HttpServletResponse response, Properties initProps, String requestParam) throws Exception {
        VelocityContext context = new VelocityContext();
        DetailRequestParser detail = new DetailRequestParser("character");
        ServletUtil.put(context, request);
        if (!detail.readAndVerifyInputParameters(request, response, requestParam, context)) {
            return;
        }
        DBHandler dbhandler = DBHandlerPool.getDBHandlerPool().getDBHandler();
        boolean res = true;
        try {
            DetailReader reader = new DetailReader(dbhandler, "C", "character", context);
            res = reader.getAndStoreDetails(response, detail.getRealmName(), detail.getObjectName(), detail.getDetailsName());
            context.put("parameter", detail);
        } finally {
            DBHandlerPool.getDBHandlerPool().freeDBHandler(dbhandler);
        }
        if (res) {
            ServletUtil.sendResponseWithVelocity("character/details.vm", context, response);
        }
    }

    public boolean isPostSupported() {
        return false;
    }
}

package net.fdukedom.epicurus.pm.ws;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import com.sun.mail.iap.Response;
import net.fdukedom.epicurus.domain.entity.User;
import net.fdukedom.epicurus.domain.util.UserHelper;
import net.fdukedom.epicurus.pm.client.PMClientPlugin;
import net.fdukedom.epicurus.pm.domain.entity.PrivateMessage;
import net.fdukedom.epicurus.pm.domain.util.PrivateMessageHelper;
import net.fdukedom.epicurus.resource.ResourceDispatcher;
import net.fdukedom.epicurus.service.WSMessageHandler;
import net.fdukedom.epicurus.service.interaction.IdentificationRequest;
import net.fdukedom.epicurus.service.interaction.IdentificationRequestReader;
import net.fdukedom.epicurus.service.interaction.ResponseWriter;

public class PMWSMessageHandler extends WSMessageHandler {

    @Override
    protected String processUpdate(IdentificationRequestReader request) {
        ResourceBundle bundle = ResourceDispatcher.getInstance().getBundle("bundles.ApplicationResources");
        ResponseWriter response = new ResponseWriter();
        User user = UserHelper.getInstance().findByEmail(request.getEmail());
        if (user != null && request.getPassword().equals(user.getPassword())) {
            String strLastDate = request.getTextParameter();
            List<PrivateMessage> messages;
            if (strLastDate.length() == 0) {
                messages = PrivateMessageHelper.getInstance().findByRecepient(user);
            } else {
                Date lastDate;
                try {
                    lastDate = DateFormat.getDateTimeInstance().parse(strLastDate);
                } catch (ParseException e) {
                    response.setMessage("Incorrect date parameter format!");
                    response.setStatus(false);
                    return response.toString();
                }
                messages = PrivateMessageHelper.getInstance().findByRecepientAndDate(user, lastDate);
            }
            for (int i = 0; i < messages.size(); i++) {
                PrivateMessage msg = messages.get(i);
                MessageXMLWrapper msgWrap = new MessageXMLWrapper(msg.getFrom().getName(), msg.getTitle(), msg.getBody(), msg.getCreated(), msg.getIsRead());
                response.addResult(msgWrap.getElement());
            }
            response.setStatus(true);
            return response.toString();
        }
        response.setMessage(bundle.getString("login.errors.failed"));
        response.setStatus(false);
        return response.toString();
    }

    @Override
    protected String processCommit(IdentificationRequestReader request) {
        ResponseWriter writer = new ResponseWriter();
        writer.setMessage("Command '" + IdentificationRequest.ID_COMMIT + "' for plugin '" + PMClientPlugin.PLUGINNAME + "' not implemented!");
        writer.setStatus(false);
        return writer.toString();
    }
}

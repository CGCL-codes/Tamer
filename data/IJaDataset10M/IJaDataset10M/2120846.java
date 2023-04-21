package pl.edu.agh.uddiProxy.parameter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.log4j.Logger;
import pl.edu.agh.uddiProxy.InquiryClient;
import pl.edu.agh.uddiProxy.UDDIClient;
import pl.edu.agh.uddiProxy.dao.DAOHelper;
import pl.edu.agh.uddiProxy.types.PatrameterCollector;
import pl.edu.agh.uddiProxy.types.TModel;
import dk.itst.uddi.client.query.FindBindingResult;
import dk.itst.uddi.client.types.core.AccessPoint;
import dk.itst.uddi.client.types.core.BindingTemplate;

public class AvailabilityParameterCollector implements PatrameterCollector<Integer> {

    private static Logger logger = Logger.getLogger(AvailabilityParameterCollector.class);

    public Integer getParameter(TModel model) {
        UDDIClient client = DAOHelper.getUDDIClient();
        InquiryClient inquiryClient = client.getInquiryClient();
        FindBindingResult bindingResult = inquiryClient.findBinding().addTModel(model.getModel().getTModelKey()).execute();
        int value = 0;
        for (BindingTemplate bindingTemplate : bindingResult.getTemplates()) {
            if (bindingTemplate.isSetAccessPoint()) {
                AccessPoint accessPoint = bindingTemplate.getAccessPoint();
                int tmp = 0;
                try {
                    tmp = testAccesspoint(accessPoint.getStringValue());
                } catch (IOException e) {
                }
                value = Math.max(value, tmp);
                logger.info("test URI: " + accessPoint.getStringValue() + " " + tmp + "%");
            }
        }
        return value;
    }

    private int testAccesspoint(String s) throws IOException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(s);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "text/xml");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode >= 500) {
                return 100;
            } else {
                return 0;
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}

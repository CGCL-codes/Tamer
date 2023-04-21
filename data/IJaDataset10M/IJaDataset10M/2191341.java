package exercisecontrolsystem.entities.dataproviders;

import com.sun.data.provider.impl.ObjectListDataProvider;
import gov.nist.mel.emergency.cap.Alert;
import exercisecontrolsystem.subscription.AlertPublisher;
import java.util.ArrayList;

/**
 *
 * @author Guillaume Radde (guillaume.radde@nist.gov)
 */
public class AlertPublisherDataProvider extends ObjectListDataProvider {

    private ArrayList<AlertPublisher> alertPublisherList = new ArrayList<AlertPublisher>();

    public AlertPublisherDataProvider() {
        alertPublisherList.add(new AlertPublisher() {

            @Override
            public PublisherStatus getPublisherType() {
                return AlertPublisher.PublisherStatus.Exercise;
            }

            @Override
            public ObservableList<Alert> getPublishedAlerts() {
                return null;
            }

            @Override
            public void setPublishedAlerts(ObservableList<Alert> publishedAlerts) {
            }

            @Override
            public String getPublisherName() {
                return "Design time name";
            }

            @Override
            public void setPublisherName(String name) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        setList(alertPublisherList);
    }
}

package net.laubenberger.bogatyr.model.application;

import java.net.URL;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import net.laubenberger.bogatyr.model.application.ModelApplicationImpl.XmlAdapterModelApplication;
import net.laubenberger.bogatyr.model.misc.Document;
import net.laubenberger.bogatyr.model.worker.ModelWorker;
import net.laubenberger.bogatyr.service.localizer.Localizer;
import net.laubenberger.bogatyr.service.property.Property;

/**
 * The interface for the application model.
 *
 * @author Stefan Laubenberger
 * @version 0.9.6 (20110527)
 * @since 0.9.0
 */
@XmlJavaTypeAdapter(XmlAdapterModelApplication.class)
public interface ModelApplication extends Document {

    String MEMBER_UPDATE_LOCATION = "updateLocation";

    String MEMBER_DEBUG = "debug";

    String MEMBER_PROPERTY = "property";

    String MEMBER_LOCALIZER = "localizer";

    String MEMBER_MODEL_WORKER = "modelWorker";

    Boolean isDebug();

    void setDebug(boolean isDebug);

    void setUpdateLocation(URL updateLocation);

    URL getUpdateLocation();

    void setProperty(Property property);

    Property getProperty();

    void setLocalizer(Localizer localizer);

    Localizer getLocalizer();

    ModelWorker getModelWorker();
}

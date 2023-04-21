package uit.server;

import java.util.List;
import uit.server.model.Model;

public interface MapController {

    public abstract Model getImage(Model model) throws Exception;

    public abstract List getLayerList(Model model) throws Exception;

    public abstract Model getFeatures(Model model) throws Exception;

    public abstract List getFeaturesList(Model model) throws Exception;
}

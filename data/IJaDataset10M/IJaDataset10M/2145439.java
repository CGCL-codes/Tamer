package com.alturatec.dienstreise.mvc.ort;

import java.util.Map;
import org.jrcaf.model.ModelEvent;
import org.jrcaf.model.ModelEvent.ModelEventType;
import org.jrcaf.mvc.AbstractController;
import org.jrcaf.mvc.IController;
import org.jrcaf.mvc.annotations.Data;
import com.alturatec.dienstreise.datasource.IOrtSearchable;
import com.alturatec.dienstreise.model.Ort;

public class OrtController extends AbstractController implements IController {

    @Data("ort")
    private Ort rootModel;

    private IOrtSearchable datasource;

    public OrtController() {
        super();
    }

    @Override
    public void init(Map<String, Object> aParameters) {
        if (rootModel == null) rootModel = new Ort();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void update(Map<String, Object> aParameters, String aCalled, String aTarget) {
        if (("neueVerbindung".equals(aCalled)) && ("neueVebindung".equals(aTarget))) {
            setChanged();
            notifyObservers(this, new ModelEvent(ModelEventType.CHANGED, new String[] { ".ort.verbindungen" }));
            dirtyFlag = true;
        }
    }
}

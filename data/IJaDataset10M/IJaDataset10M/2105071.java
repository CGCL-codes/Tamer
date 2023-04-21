package org.shalma.persistence.internal.model;

import java.util.HashMap;
import java.util.Map;
import org.shalma.persistence.internal.model.autoexamen.AnswerModel;
import org.shalma.persistence.internal.model.autoexamen.ConceptModel;
import org.shalma.persistence.internal.model.autoexamen.ImageModel;
import org.shalma.persistence.internal.model.autoexamen.QuestionModel;

public class ModelManager {

    private Map<Class<?>, Model> models = new HashMap<Class<?>, Model>();

    static final String AUTOEXAMEN = "com.autoexamen.client.model.";

    public ModelManager() {
        try {
            init();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Model get(Class<?> type) {
        Model model = models.get(type);
        if (model == null) {
            model = new AnnotatedModel(type);
            models.put(type, model);
        }
        return model;
    }

    private void init() throws ClassNotFoundException {
        models.put(Class.forName(AUTOEXAMEN + "Answer"), new AnswerModel());
        models.put(Class.forName(AUTOEXAMEN + "Concept"), new ConceptModel());
        models.put(Class.forName(AUTOEXAMEN + "Image"), new ImageModel());
        models.put(Class.forName(AUTOEXAMEN + "Question"), new QuestionModel());
    }
}

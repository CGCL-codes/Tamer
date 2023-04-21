package hu.gbalage.owlforms.impl.style;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.semanticweb.owl.model.OWLAnnotation;
import org.semanticweb.owl.model.OWLObject;
import org.semanticweb.owl.model.OWLProperty;
import hu.gbalage.owlforms.OWLFormsConstants;
import hu.gbalage.owlforms.OWLFormsManager;
import hu.gbalage.owlforms.api.Field;
import hu.gbalage.owlforms.api.style.FieldOrderProvider;

/**
 * @author Grill Balazs (balage.g@gmail.com)
 *
 */
public class FieldOrderProviderImpl implements FieldOrderProvider, Comparator<Field> {

    final OWLFormsManager manager;

    final Map<URI, Float> weights = new HashMap<URI, Float>();

    public FieldOrderProviderImpl(OWLFormsManager manager) {
        this.manager = manager;
    }

    protected Float getWeight(Field field) {
        if (!weights.containsKey(field.getURI())) {
            OWLProperty<?, ?> property = field.isPrimitive() ? manager.getDataFactory().getOWLDataProperty(field.getURI()) : manager.getDataFactory().getOWLObjectProperty(field.getURI());
            float value = 0;
            for (OWLAnnotation<OWLObject> a : manager.getAnnotationCollector().collect(property, OWLFormsConstants.ANNOTATION_WEIGHT)) {
                value += Float.parseFloat(a.getAnnotationValueAsConstant().getLiteral());
            }
            weights.put(field.getURI(), value);
        }
        return weights.get(field.getURI());
    }

    public List<Field> orderFields(Collection<Field> fields) {
        List<Field> result = new ArrayList<Field>(fields);
        Collections.sort(result, this);
        return result;
    }

    public int compare(Field o1, Field o2) {
        Float v1 = getWeight(o1);
        Float v2 = getWeight(o2);
        return v1.compareTo(v2);
    }
}

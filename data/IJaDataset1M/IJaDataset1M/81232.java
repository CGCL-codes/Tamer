package nl.contentanalysis.inet.handlers.annotation;

import java.util.LinkedHashMap;
import java.util.Map;
import nl.contentanalysis.inet.extensions.IAnnotationSchemaFieldFactory;
import nl.contentanalysis.inet.model.DataAccessException;
import nl.contentanalysis.inet.model.IAnnotationSchema;
import nl.contentanalysis.inet.model.IAnnotationSchemaField;
import nl.contentanalysis.tools.Toolkit;

public class AdHocChoiceFieldFactory implements IAnnotationSchemaFieldFactory {

    public IAnnotationSchemaField createField(Object data, IAnnotationSchema schema, String label, String fieldname, String params, String editorID) throws DataAccessException {
        Map<String, String> p = Toolkit.paramStringToMap(params);
        String valuestr = p.get("values");
        if (valuestr == null) throw new DataAccessException("AdHocChoiceField requires param values");
        String[] values = valuestr.split(";");
        Map<Object, String> labels = new LinkedHashMap<Object, String>(values.length);
        for (String value : values) {
            if (value.indexOf(':') != -1) {
                String[] v = value.split(":");
                labels.put(Integer.parseInt(v[0]), v[1]);
            } else labels.put(value, value);
        }
        return new ChoiceSchemaField(schema, label, fieldname, editorID, p, labels);
    }
}

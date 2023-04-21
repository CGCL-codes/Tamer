package velox.spring.admin.spi.metadata;

import java.util.HashMap;
import java.util.Map;
import org.springframework.util.StringUtils;

public abstract class AbstractEditMetadataProvider implements EditMetadataProvider {

    public Map<EditField, String> validate(Object object) {
        Map<EditField, String> errors = new HashMap<EditField, String>();
        for (FieldGroup<? extends EditField> fg : getEditFieldGroups(object)) {
            for (EditField ef : fg.getFields()) {
                if (ef.isRequired() && !StringUtils.hasText(ef.getValueAsString())) {
                    errors.put(ef, "Cannot be empty");
                }
            }
        }
        return errors;
    }
}

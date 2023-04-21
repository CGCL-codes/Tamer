package cz.kibo.ekonom.propertyEditor;

import cz.kibo.ekonom.model.School;
import cz.kibo.ekonom.service.SchoolDao;
import java.beans.PropertyEditorSupport;

/**
 *
 * @author tomas
 */
public class SchoolPropertyEditor extends PropertyEditorSupport {

    private SchoolDao schoolDao;

    public SchoolPropertyEditor(SchoolDao schoolDao) {
        this.schoolDao = schoolDao;
    }

    @Override
    public void setAsText(final String text) {
        setValue(schoolDao.findById(Integer.parseInt(text)));
    }

    @Override
    public String getAsText() {
        School school = (School) getValue();
        return school.getId().toString();
    }
}

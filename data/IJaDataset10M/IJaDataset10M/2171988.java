package com.divosa.eformulieren.domain.repository.impl;

import java.util.List;
import com.divosa.eformulieren.domain.domeinobject.Widget;
import com.divosa.eformulieren.domain.domeinobject.WidgetConstraint;
import com.divosa.eformulieren.domain.domeinobject.WidgetRelevance;
import com.divosa.eformulieren.domain.domeinobject.WidgetStruct;
import com.divosa.eformulieren.domain.domeinobject.WidgetType;
import com.divosa.eformulieren.domain.domeinobject.WidgetTypeStruct;
import com.divosa.eformulieren.domain.domeinobject.hibernate.view.ViewWidgetStruct;
import com.divosa.eformulieren.domain.repository.StructDAO;
import com.divosa.eformulieren.util.constant.Constants;
import com.divosa.security.exception.ObjectNotFoundException;

public class HibernateStructDAO extends HibernateBaseDao implements StructDAO {

    private static final String FIND_WIDGETTYPE_STRUCT_PARENT_AND_CHILD = "WidgetTypeStruct.by.parent.and.child";

    private static final String FIND_WIDGET_STRUCT_DEFS_BY_WIDGET_TYPE_AND_FORM = "WidgetStructDefs.by.WidgetType.and.Form";

    private static final String FIND_WIDGET_STRUCT_LOCS_BY_WIDGET_TYPE_AND_FORM = "WidgetStructLocs.by.WidgetType.and.Form";

    private static final String FIND_WIDGET_STRUCT_DEFS_BY_WIDGET_TYPE_FORM_AND_VERSION = "WidgetStructDefs.by.WidgetType.Form.and.version";

    private static final String FIND_WIDGET_STRUCT_LOCS_BY_WIDGET_TYPE_FORM_AND_VERSION = "WidgetStructLocs.by.WidgetType.Form.and.version";

    private static final String FIND_WIDGET_STRUCT_DEFS_BY_WIDGET_TYPE_WIDGET_STATE_AND_VERSION = "WidgetStructDefs.by.WidgetType.and.version";

    private static final String FIND_WIDGET_STRUCT_LOCS_BY_WIDGET_TYPE_WIDGET_STATE_AND_VERSION = "WidgetStructLocs.by.WidgetType.and.version";

    private static final String FIND_WIDGET_STRUCT_LOCS_BY_PARENT_AND_FORM = "WidgetStructLocs.by.parent.and.form";

    private static final String FIND_WIDGET_STRUCT_DEFS_BY_PARENT_AND_FORM = "WidgetStructDefs.by.parent.and.form";

    private static final String FIND_WIDGET_STRUCT_LOCS_BY_PARENT_FORM_AND_VERSION = "WidgetStructLocs.by.parent.form.and.version";

    private static final String FIND_WIDGET_STRUCT_DEFS_BY_PARENT_FORM_AND_VERSION = "WidgetStructDefs.by.parent.form.and.version";

    private static final String FIND_WIDGET_STRUCT_LOCS_BY_PARENT_CHILD_FORM_AND_VERSION = "WidgetStructLocs.by.parent.child.form.and.version";

    private static final String FIND_WIDGET_STRUCT_DEFS_BY_FORM_AND_VERSION = "WidgetStructDefs.by.form.and.version";

    private static final String FIND_WIDGET_STRUCT_LOCS_BY_FORM_AND_VERSION = "WidgetStructLocs.by.form.and.version";

    private static final String FIND_WIDGET_STRUCT_DEFS_BY_PARENT_CHILD_FORM_AND_VERSION = "WidgetStructDefs.by.parent.child.form.and.version";

    private static final String FIND_WIDGET_STRUCT_DEFS_BY_CHILD_FORM_AND_VERSION = "WidgetStructDefs.by.child.form.and.version";

    private static final String FIND_WIDGET_STRUCT_LOCS_BY_CHILD_FORM_AND_VERSION = "WidgetStructLocs.by.child.form.and.version";

    private static final String FIND_WIDGET_RELEVANCES_BY_FORM_AND_VERSION = "Find.WidgetRelevances.by.Form.and.version";

    private static final String FIND_WIDGET_CONSTRAINTS_BY_FORM_AND_VERSION = "Find.WidgetConstraints.by.Form.and.version";

    private static final String ATTR_CHILD = "child";

    private static final String ATTR_WIDGET_TYPE = "widgetType";

    private static final String ATTR_PARENT = "parent";

    private static final String ATTR_FORM = "form";

    private static final String ATTR_VERSION = "version";

    public List<?> loadAll() {
        return loadAll(WidgetStruct.class);
    }

    /**
     * @see com.divosa.eformulieren.domain.repository.StructDAO#getWidgetTypeStructByWidgetTypes(com.divosa.eformulieren.domain.domeinobject.WidgetType,
     * com.divosa.eformulieren.domain.domeinobject.WidgetType)
     */
    public WidgetTypeStruct getWidgetTypeStructByWidgetTypes(WidgetType parent, WidgetType child) throws ObjectNotFoundException {
        List widgetTypeStructs = getHibernateTemplate().findByNamedQueryAndNamedParam(FIND_WIDGETTYPE_STRUCT_PARENT_AND_CHILD, new String[] { ATTR_PARENT, ATTR_CHILD }, new Object[] { parent, child });
        WidgetTypeStruct widgeTypeStruct = null;
        if (widgetTypeStructs.size() == 0) {
            throw new ObjectNotFoundException("WidgetTypeStruct with parent '" + parent.getName() + "' and child '" + child.getName() + " 'not found.");
        } else {
            widgeTypeStruct = (WidgetTypeStruct) widgetTypeStructs.get(0);
        }
        return widgeTypeStruct;
    }

    /**
     * @see com.divosa.eformulieren.domain.repository.StructDAO#getWidgetStructsByParentFormWidgetStateAndVersion(com.divosa.eformulieren.domain.domeinobject.Widget,
     * com.divosa.eformulieren.domain.domeinobject.Widget, int)
     */
    public List<WidgetStruct> getAllWidgetStructsByParentFormWidgetStateAndVersion(Widget parent, Widget form, String widgetState, int version) throws ObjectNotFoundException {
        List<WidgetStruct> widgetStructs = null;
        String query = null;
        if (Constants.WIDGET_STATE_DEF.equals(widgetState)) {
            query = FIND_WIDGET_STRUCT_DEFS_BY_PARENT_FORM_AND_VERSION;
        } else if (Constants.WIDGET_STATE_LOC.equals(widgetState)) {
            query = FIND_WIDGET_STRUCT_LOCS_BY_PARENT_FORM_AND_VERSION;
        }
        widgetStructs = getHibernateTemplate().findByNamedQueryAndNamedParam(query, new String[] { ATTR_PARENT, ATTR_FORM, ATTR_VERSION }, new Object[] { parent, form, version });
        if (widgetStructs.size() == 0) {
            throw new ObjectNotFoundException("No WidgetStructs found for parent widget '" + parent.getDescription() + ", form widget " + form.getDescription() + "' state '" + widgetState + "' and version '" + version + ".");
        }
        return widgetStructs;
    }

    public List<WidgetStruct> getAllWidgetStructsByFormWidgetStateAndVersion(Widget form, String widgetState, int version) throws ObjectNotFoundException {
        List<WidgetStruct> widgetStructs = null;
        String query = null;
        if (Constants.WIDGET_STATE_DEF.equals(widgetState)) {
            query = FIND_WIDGET_STRUCT_DEFS_BY_FORM_AND_VERSION;
        } else if (Constants.WIDGET_STATE_LOC.equals(widgetState)) {
            query = FIND_WIDGET_STRUCT_LOCS_BY_FORM_AND_VERSION;
        }
        widgetStructs = getHibernateTemplate().findByNamedQueryAndNamedParam(query, new String[] { ATTR_FORM, ATTR_VERSION }, new Object[] { form, version });
        if (widgetStructs.size() == 0) {
            throw new ObjectNotFoundException("No WidgetStructs found for form widget " + form.getDescription() + "' state '" + widgetState + "' and version '" + version + ".");
        }
        return widgetStructs;
    }

    public List<WidgetStruct> getAllWidgetStructsByParentChildFormWidgetStateAndVersion(Widget parent, Widget child, Widget form, String widgetState, int version) throws ObjectNotFoundException {
        List<WidgetStruct> widgetStructs = null;
        String query = null;
        if (Constants.WIDGET_STATE_DEF.equals(widgetState)) {
            query = FIND_WIDGET_STRUCT_DEFS_BY_PARENT_CHILD_FORM_AND_VERSION;
        } else if (Constants.WIDGET_STATE_LOC.equals(widgetState)) {
            query = FIND_WIDGET_STRUCT_LOCS_BY_PARENT_CHILD_FORM_AND_VERSION;
        }
        widgetStructs = getHibernateTemplate().findByNamedQueryAndNamedParam(query, new String[] { ATTR_PARENT, ATTR_CHILD, ATTR_FORM, ATTR_VERSION }, new Object[] { parent, child, form, version });
        if (widgetStructs.size() == 0) {
            throw new ObjectNotFoundException("No WidgetStructs found for parent widget '" + parent.getDescription() + ", child widget " + child.getDescription() + ", form widget " + form.getDescription() + "' state '" + widgetState + "' and version '" + version + ".");
        }
        return widgetStructs;
    }

    public List<WidgetStruct> getAllWidgetStructsByChildFormWidgetStateAndVersion(Widget child, Widget form, String widgetState, int version) throws ObjectNotFoundException {
        List<WidgetStruct> widgetStructs = null;
        String query = null;
        if (Constants.WIDGET_STATE_DEF.equals(widgetState)) {
            query = FIND_WIDGET_STRUCT_DEFS_BY_CHILD_FORM_AND_VERSION;
        } else if (Constants.WIDGET_STATE_LOC.equals(widgetState)) {
            query = FIND_WIDGET_STRUCT_LOCS_BY_CHILD_FORM_AND_VERSION;
        }
        widgetStructs = getHibernateTemplate().findByNamedQueryAndNamedParam(query, new String[] { ATTR_CHILD, ATTR_FORM, ATTR_VERSION }, new Object[] { child, form, version });
        if (widgetStructs.size() == 0) {
            throw new ObjectNotFoundException("No WidgetStructs found for child widget " + child.getDescription() + ", form widget " + form.getDescription() + "' state '" + widgetState + "' and version '" + version + ".");
        }
        return widgetStructs;
    }

    public List<WidgetStruct> getAllWidgetStructsByParentFormAndWidgetState(Widget parent, Widget form, String widgetState) throws ObjectNotFoundException {
        List<WidgetStruct> widgetStructs = null;
        String query = null;
        if (Constants.WIDGET_STATE_DEF.equals(widgetState)) {
            query = FIND_WIDGET_STRUCT_DEFS_BY_PARENT_AND_FORM;
        } else if (Constants.WIDGET_STATE_LOC.equals(widgetState)) {
            query = FIND_WIDGET_STRUCT_LOCS_BY_PARENT_AND_FORM;
        } else {
        }
        widgetStructs = getHibernateTemplate().findByNamedQueryAndNamedParam(query, new String[] { ATTR_PARENT, ATTR_FORM }, new Object[] { parent, form });
        if (widgetStructs.size() == 0) {
            throw new ObjectNotFoundException("No WidgetStructs found for parent widget '" + parent.getDescription() + ", form widget " + form.getDescription() + "' and state '" + widgetState + "'.");
        }
        return widgetStructs;
    }

    public List<WidgetStruct> getAllWidgetStructsByWidgetTypeFormAndWidgetState(WidgetType widgetType, Widget form, String widgetState) throws ObjectNotFoundException {
        List<WidgetStruct> widgetStructs = null;
        String query = null;
        if (Constants.WIDGET_STATE_DEF.equals(widgetState)) {
            query = FIND_WIDGET_STRUCT_DEFS_BY_WIDGET_TYPE_AND_FORM;
        } else if (Constants.WIDGET_STATE_LOC.equals(widgetState)) {
            query = FIND_WIDGET_STRUCT_LOCS_BY_WIDGET_TYPE_AND_FORM;
        } else {
        }
        widgetStructs = getHibernateTemplate().findByNamedQueryAndNamedParam(query, new String[] { ATTR_WIDGET_TYPE, ATTR_FORM }, new Object[] { widgetType, form });
        if (widgetStructs.size() == 0) {
            throw new ObjectNotFoundException("No WidgetStructs found for widgetType '" + widgetType.getName() + "' and form " + form.getDescription());
        }
        return widgetStructs;
    }

    public List<WidgetStruct> getAllWidgetStructsByWidgetTypeFormWidgetStateAndVersion(WidgetType widgetType, Widget form, String widgetState, int version) throws ObjectNotFoundException {
        List<WidgetStruct> widgetStructs = null;
        String query = null;
        if (Constants.WIDGET_STATE_DEF.equals(widgetState)) {
            query = FIND_WIDGET_STRUCT_DEFS_BY_WIDGET_TYPE_FORM_AND_VERSION;
        } else if (Constants.WIDGET_STATE_LOC.equals(widgetState)) {
            query = FIND_WIDGET_STRUCT_LOCS_BY_WIDGET_TYPE_FORM_AND_VERSION;
        } else {
        }
        widgetStructs = getHibernateTemplate().findByNamedQueryAndNamedParam(query, new String[] { ATTR_WIDGET_TYPE, ATTR_FORM, ATTR_VERSION }, new Object[] { widgetType, form, version });
        if (widgetStructs.size() == 0) {
            throw new ObjectNotFoundException("No WidgetStructs found for widgetType '" + widgetType.getName() + "' and form " + form.getDescription());
        }
        return widgetStructs;
    }

    public List<WidgetStruct> getAllWidgetStructsByWidgetTypeWidgetStateAndVersion(WidgetType widgetType, String widgetState, int version) throws ObjectNotFoundException {
        List<WidgetStruct> widgetStructs = null;
        String query = null;
        if (Constants.WIDGET_STATE_DEF.equals(widgetState)) {
            query = FIND_WIDGET_STRUCT_DEFS_BY_WIDGET_TYPE_WIDGET_STATE_AND_VERSION;
        } else if (Constants.WIDGET_STATE_LOC.equals(widgetState)) {
            query = FIND_WIDGET_STRUCT_LOCS_BY_WIDGET_TYPE_WIDGET_STATE_AND_VERSION;
        } else {
        }
        widgetStructs = getHibernateTemplate().findByNamedQueryAndNamedParam(query, new String[] { ATTR_WIDGET_TYPE, ATTR_VERSION }, new Object[] { widgetType, version });
        if (widgetStructs.size() == 0) {
            throw new ObjectNotFoundException("No WidgetStructs found for widgetType '" + widgetType.getName() + "', widgetState '" + widgetState + "' and evrsion " + version);
        }
        return widgetStructs;
    }
}

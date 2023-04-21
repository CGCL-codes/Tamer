package org.objectstyle.cayenne.project.validator;

import java.util.Iterator;
import org.objectstyle.cayenne.map.DataMap;
import org.objectstyle.cayenne.project.ProjectPath;
import org.objectstyle.cayenne.query.Query;
import org.objectstyle.cayenne.query.SQLTemplate;
import org.objectstyle.cayenne.util.Util;

/**
 * Validator for SQLTemplate queries.
 * 
 * @author Andrei Adamchik
 * @since 1.1
 */
public class SQLTemplateValidator extends TreeNodeValidator {

    public void validateObject(ProjectPath treeNodePath, Validator validator) {
        SQLTemplate query = (SQLTemplate) treeNodePath.getObject();
        validateName(query, treeNodePath, validator);
        validateRoot(query, treeNodePath, validator);
        validateDefaultSQL(query, treeNodePath, validator);
    }

    protected void validateDefaultSQL(SQLTemplate query, ProjectPath path, Validator validator) {
        if (Util.isEmptyString(query.getDefaultTemplate())) {
            Iterator it = query.getTemplateKeys().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                if (!Util.isEmptyString(query.getCustomTemplate(key))) {
                    return;
                }
            }
            validator.registerWarning("Query has no default SQL template", path);
        }
    }

    protected void validateRoot(SQLTemplate query, ProjectPath path, Validator validator) {
        DataMap map = (DataMap) path.firstInstanceOf(DataMap.class);
        if (query.getRoot() == null && map != null) {
            validator.registerWarning("Query has no root", path);
        }
    }

    protected void validateName(Query query, ProjectPath path, Validator validator) {
        String name = query.getName();
        if (Util.isEmptyString(name)) {
            validator.registerError("Unnamed Query.", path);
            return;
        }
        DataMap map = (DataMap) path.getObjectParent();
        if (map == null) {
            return;
        }
        Iterator it = map.getQueries().iterator();
        while (it.hasNext()) {
            Query otherQuery = (Query) it.next();
            if (otherQuery == query) {
                continue;
            }
            if (name.equals(otherQuery.getName())) {
                validator.registerError("Duplicate Query name: " + name + ".", path);
                break;
            }
        }
    }

    /**
     * @deprecated unused since 1.2
     */
    protected void validateResultType(SQLTemplate query, ProjectPath path, Validator validator) {
    }
}

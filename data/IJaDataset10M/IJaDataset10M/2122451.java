package com.ecmdeveloper.plugin.search.model;

import org.eclipse.jface.resource.ImageDescriptor;
import com.ecmdeveloper.plugin.search.Activator;
import com.ecmdeveloper.plugin.search.editor.QueryIcons;
import com.ecmdeveloper.plugin.search.model.constants.QueryComponentType;

/**
 * @author ricardo.belfor
 *
 */
public class NullTest extends QueryComponent {

    private static final long serialVersionUID = 1L;

    public static final QueryElementDescription DESCRIPTION = new QueryElementDescription(NullTest.class, "Null Test", "Query Field Null Test", QueryIcons.NULL_TEST_ICON, QueryIcons.NULL_TEST_ICON_LARGE) {

        @Override
        public boolean isValidFor(IQueryField queryField) {
            return queryField.isSearchable();
        }
    };

    private boolean negated;

    public NullTest(Query query) {
        super(query);
    }

    public static ImageDescriptor getIcon() {
        return Activator.getImageDescriptor(QueryIcons.NULL_TEST_ICON);
    }

    public static ImageDescriptor getLargeIcon() {
        return Activator.getImageDescriptor(QueryIcons.NULL_TEST_ICON_LARGE);
    }

    public void setNegated(boolean negated) {
        if (this.negated != negated) {
            this.negated = negated;
            firePropertyChange(PROPERTY_CHANGED, null, negated);
        }
    }

    public boolean isNegated() {
        return negated;
    }

    public String toString() {
        return toString(false);
    }

    @Override
    public String toSQL() {
        return toString(true);
    }

    private String toString(boolean strict) {
        if (getField() != null) {
            StringBuffer result = new StringBuffer();
            appendField(result, strict);
            result.append(" IS ");
            if (negated) {
                result.append("NOT ");
            }
            result.append("NULL");
            return result.toString();
        } else {
            return "";
        }
    }

    @Override
    public QueryComponentType getType() {
        return QueryComponentType.NULL_TEST;
    }
}

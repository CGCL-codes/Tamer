package org.progeeks.graph;

import java.util.*;
import java.util.regex.*;
import com.phoenixst.collections.OrderedPair;
import com.phoenixst.plexus.*;
import org.apache.commons.collections.Predicate;
import org.progeeks.meta.*;
import org.progeeks.util.ObjectUtils;

/**
 *  A predicate that will perform a regex match
 *  on a MetaClass field value.
 *
 *  metaClass and metaClassName are mutually exclusive.
 *
 *  If a field is not provided, all fields will be searched.  Alternatively,
 *  a wildcard (*) could be provided.  To narrow the scope, the name
 *  of the object's field should be supplied.
 *
 *  @version   $Revision: 1.8 $
 *  @author    Dave Garvis
 */
public class MetaObjectRegexPredicate implements Predicate, java.io.Serializable {

    static final long serialVersionUID = 1;

    /**
     *  Wildcard, searches all fields.
     */
    public static final String WILDCARD = "*";

    /**
     *  Property that may be set to
     *  narrow the search to a specific MetaClass type.
     *
     *  - optional to the search
     */
    private MetaClass metaClass;

    /**
     *  Same purpose as MetaClass property, but
     *  specified as a string in order to do
     *  a MetaClass.forName() lookup.
     *
     *  - optional to the search
     */
    private String metaClassName;

    /**
     *  The unpatterned field if set.
     */
    private String field;

    /**
     *  The patterned field expression.  This will always
     *  be set to something and is used to generate the fieldPattern.
     */
    private String fieldExpression;

    /**
     *  The expression used in the search.
     *
     *  - required, otherwise consider using <code>MetaObjectPredicate</code>
     */
    private String regExpression;

    /**
     *  Local member defining the regex pattern.
     *
     *  This member is updated when the regExpression
     *  member is set.
     */
    private Pattern pattern = null;

    /**
     *  Pattern used to match field names.  This is updated
     *  when fieldPattern and field are set.
     */
    private Pattern fieldPattern = null;

    public MetaObjectRegexPredicate() {
    }

    /**
     *  Constructor to create a MetaObjectRegexPredicate from a meta class name,
     *  field and expression.
     */
    public MetaObjectRegexPredicate(String metaClassName, String field, String expression) {
        this(null, metaClassName, field, expression);
    }

    /**
     *  Constructor to create a MetaObjectRegexPredicate from a MetaClass,
     *  field and expression.
     */
    public MetaObjectRegexPredicate(MetaClass metaClass, String field, String expression) {
        this(metaClass, null, field, expression);
    }

    /**
     *  Private convenience constructor to assign the members.
     */
    private MetaObjectRegexPredicate(MetaClass metaClass, String metaClassName, String field, String expression) {
        setMetaClass(metaClass);
        setMetaClassName(metaClassName);
        setField(field);
        setExpression(expression);
    }

    /**
     *  Sets the regex to use in the search.
     */
    public void setExpression(String expression) {
        this.regExpression = expression;
        pattern = null;
        if (!isValidExpression(expression)) return;
        String regex = this.regExpression.replaceAll("\\x20+", "\\\\s+");
        pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    }

    /**
     *  Gets the regex string used in the search.
     */
    public String getExpression() {
        return (regExpression);
    }

    /**
     *  Returns the meta-class for which we are searching.
     */
    public MetaClass getMetaClass() {
        return (metaClass);
    }

    /**
     *  Sets the meta-class to be searched later.
     */
    public void setMetaClass(MetaClass metaClass) {
        this.metaClass = metaClass;
    }

    /**
     *  Returns the meta-class name for which we are searching.
     */
    public String getMetaClassName() {
        return (metaClassName);
    }

    /**
     *  Sets the metaClassName for this predicate.
     */
    public void setMetaClassName(String metaClassName) {
        this.metaClassName = metaClassName;
    }

    /**
     *  Returns the field name for which we are checking.
     */
    public String getField() {
        return (field);
    }

    /**
     *  Sets the field name for this predicate.
     */
    public void setField(String field) {
        if (WILDCARD.equals(field) || field == null) setFieldExpression(".*"); else {
            setFieldExpression("^" + field + "\\z");
        }
        this.field = field;
    }

    /**
     *  Sets the field expression used for matching meta-object
     *  field names.
     */
    public void setFieldExpression(String fieldExpression) {
        this.fieldExpression = fieldExpression;
        field = null;
        if (!isValidExpression(fieldExpression)) {
            fieldPattern = Pattern.compile("^\\z");
            return;
        }
        fieldPattern = Pattern.compile(fieldExpression);
    }

    /**
     *  Returns the field expression used for matching meta-object
     *  field names.
     */
    public String getFieldExpression() {
        return (fieldExpression);
    }

    /**
     *  Returns true if the specified node is a meta-object
     *  and it's field value is matched with the search
     *  expression provided.
     *
     *  Evaluation happens as follows:<br>
     *  <ul>
     *      <li>
     *      <code>metaClass</code>:
     *          If this is set, the object will
     *          be evaluated on the basis that it is
     *          an instance of the <code>metaClass</code> type.
     *      </li>
     *
     *      <li>
     *      <code>metaClassName</code>:
     *          If this is set, the object will
     *          be evaluated on the basis that it is
     *          an instance of the meta class as looked up
     *          through the object's class registry.
     *      </li>
     *
     *  <strong>Note:</strong> <code>metaClass</code> and <code>metaClassName</code>
     *  are mutually exclusive.
     *
     *      <li>
     *      <code>expression</code>:
     *          Any <code>null</null> or empty expressions will return <code>false</code>
     *      </li>
     *
     *      <li>
     *      <code>field</code>:
     *          In order to search for an expression, a field should be provided
     *          in order to match against it's contents. If <code>field</code>
     *          has not been provided, matching will performed against all fields.<br>
     *          '*' may be used to perform the evaluation against all fields.  If
     *          <b>any</b> field matches, evaluation is considered to be true.<br>
     *      </li>
     *  </ul>
     */
    public boolean evaluate(Object obj) {
        if (obj instanceof OrderedPair) {
            OrderedPair pair = (OrderedPair) obj;
            Object n = pair.getFirst();
            Graph.Edge edge = (Graph.Edge) pair.getSecond();
            return (evaluate(edge.getOtherEndpoint(n)));
        }
        if (!(obj instanceof MetaObject)) return (false);
        MetaObject o = (MetaObject) obj;
        if (metaClass != null && !metaClass.isInstance(o)) return (false); else if (metaClassName != null) {
            MetaClass mClass = o.getMetaClass().getClassRegistry().getMetaClass(metaClassName);
            if (mClass == null || !mClass.isInstance(o)) return (false);
        }
        if (pattern == null) return (false);
        if (field == null || WILDCARD.equals(field)) return (wildcardMatch(o));
        if (!o.getMetaClass().hasProperty(field)) return (false);
        Object v = o.getProperty(field);
        return (pattern.matcher(v.toString()).find());
    }

    /**
     *  Attempts a match on all the fields of the metaobject.
     */
    private boolean wildcardMatch(MetaObject obj) {
        for (Iterator i = obj.getMetaClass().getPropertyNames().iterator(); i.hasNext(); ) {
            String fName = (String) i.next();
            if (fieldPattern != null && !fieldPattern.matcher(fName).matches()) continue;
            Object v = obj.getProperty(fName);
            if (v == null) continue;
            if (pattern.matcher(v.toString()).find()) return (true);
        }
        return (false);
    }

    private boolean isValidExpression(String expression) {
        return (expression != null && expression.trim().length() > 0);
    }

    public int hashCode() {
        int code = 0;
        if (metaClass != null) code += metaClass.hashCode();
        if (metaClassName != null) code += metaClassName.hashCode();
        if (field != null) code += field.hashCode();
        if (regExpression != null) code += regExpression.hashCode();
        return (code);
    }

    public boolean equals(Object obj) {
        if (obj == null || !getClass().equals(obj.getClass())) return (false);
        MetaObjectRegexPredicate mop = (MetaObjectRegexPredicate) obj;
        if (!ObjectUtils.areEqual(metaClass, mop.metaClass)) return (false);
        if (!ObjectUtils.areEqual(metaClassName, mop.metaClassName)) return (false);
        if (!ObjectUtils.areEqual(field, mop.field)) return (false);
        if (!ObjectUtils.areEqual(regExpression, mop.regExpression)) return (false);
        return (true);
    }

    public String toString() {
        return ("MetaObjectRegexPredicate[" + metaClass + ", " + metaClassName + ", " + field + ", " + regExpression + "]");
    }
}

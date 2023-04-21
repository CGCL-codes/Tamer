package solarnetwork.impl;

import java.util.Arrays;
import java.util.Map;
import solarnetwork.Filterable;
import solarnetwork.ObjectCriteria.JoinType;
import solarnetwork.ObjectCriteria.MatchType;

/**
 * Generic object search filter.
 * 
 * @author matt
 * @version $Revision$
 * @param <T> the object to filter on
 */
public class ObjectSearchFilter<T extends Filterable> implements Cloneable {

    private T filter;

    private MatchType mode;

    private JoinType joinType;

    /**
	 * Construct with a filter using {@link MatchType#EQUAL} and
	 * {@link JoinType#AND}.
	 */
    public ObjectSearchFilter(T filter) {
        this(filter, MatchType.EQUAL, JoinType.AND);
    }

    /**
	 * @param filter
	 * @param mode
	 * @param joinType
	 */
    public ObjectSearchFilter(T filter, MatchType mode, JoinType joinType) {
        super();
        this.filter = filter;
        this.mode = mode;
        this.joinType = joinType;
    }

    /**
	 * Appends this search filter as a string to a StringBuilder.
	 * 
	 * @param buf the buffer to append to
	 */
    public void appendLdapSearchFilter(StringBuilder buf) {
        if (filter == null) {
            return;
        }
        Map<String, ?> filterMap = filter.getFilter();
        if (filterMap == null || filterMap.size() < 1) {
            return;
        }
        if (filterMap.size() > 1) {
            buf.append('(');
        }
        int idx = 0;
        for (Map.Entry<String, ?> me : filterMap.entrySet()) {
            if (idx > 0) {
                buf.append(joinType.toString());
            }
            String attributeName = me.getKey();
            Object value = me.getValue();
            buf.append('(');
            buf.append(attributeName);
            switch(mode) {
                case GREATER_THAN:
                    buf.append(">");
                    break;
                case GREATER_THAN_EQUAL:
                    buf.append(">=");
                    break;
                case LESS_THAN:
                    buf.append("<");
                    break;
                case LESS_THAN_EQUAL:
                    buf.append("<=");
                    break;
                case PRESENT:
                    buf.append("=*");
                    break;
                case APPROX:
                    buf.append("~=");
                    break;
                default:
                    buf.append("=");
                    break;
            }
            if (mode == MatchType.SUBSTRING) {
                if (value == null) {
                    buf.append("*");
                } else {
                    buf.append("*");
                    buf.append(value);
                    buf.append("*");
                }
            } else if (mode == MatchType.SUBSTRING_AT_START) {
                if (value != null) {
                    buf.append(value);
                }
                buf.append("*");
            } else if (mode != MatchType.PRESENT) {
                if (value == null) {
                    buf.append("*");
                } else if (value.getClass().isArray()) {
                    buf.append(Arrays.toString((Object[]) value));
                } else {
                    buf.append(value);
                }
            }
            buf.append(')');
            idx++;
        }
        if (filterMap.size() > 1) {
            buf.append(')');
        }
    }

    /**
	 * Return an LDAP search filter string.
	 * 
	 * @return String
	 */
    public String asLdapSearchFilterString() {
        StringBuilder buf = new StringBuilder();
        appendLdapSearchFilter(buf);
        return buf.toString();
    }

    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
	 * Return an LDAP search filter string.
	 * 
	 * <p>This simply calls {@link #asLdapSearchFilterString()}.</p>
	 * 
	 * @return String
	 */
    @Override
    public String toString() {
        return asLdapSearchFilterString();
    }

    /**
	 * @return the filter
	 */
    public T getFilter() {
        return filter;
    }

    /**
	 * @return the mode
	 */
    public MatchType getMode() {
        return mode;
    }

    /**
	 * @return the joinType
	 */
    public JoinType getJoinType() {
        return joinType;
    }
}

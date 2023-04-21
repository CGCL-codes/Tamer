package fr.cnes.sitools.dataset.plugins.filters.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.ext.wadl.ParameterInfo;
import org.restlet.ext.wadl.ParameterStyle;
import org.restlet.resource.ResourceException;
import fr.cnes.sitools.common.validator.ConstraintViolation;
import fr.cnes.sitools.common.validator.Validator;
import fr.cnes.sitools.dataset.DataSetApplication;
import fr.cnes.sitools.dataset.filter.business.AbstractFilter;
import fr.cnes.sitools.dataset.model.Column;
import fr.cnes.sitools.dataset.model.DataSet;
import fr.cnes.sitools.dataset.model.Operator;
import fr.cnes.sitools.dataset.model.Predicat;
import fr.cnes.sitools.form.components.model.Param;
import fr.cnes.sitools.util.SQLUtils;

/**
 * 
 * Grid filter
 * 
 * @author d. arpin (AKKA technologies)
 */
public final class GridFilter extends AbstractFilter {

    /** The TEMPLATE_PARAM */
    private static final String TEMPLATE_PARAM = "filter[#]";

    /**
   * Default constructor
   */
    public GridFilter() {
        super();
        this.setName("GridFilter");
        this.setDescription("Required when using filtering via LiveGrid ExtJs");
        this.setAuthor("AKKA Technologies");
        this.setClassAuthor("AKKA Technologies");
        this.setClassOwner("CNES");
        this.setVersion("0.1");
        this.setDefaultFilter(true);
        HashMap<String, ParameterInfo> rpd = new HashMap<String, ParameterInfo>();
        ParameterInfo paramInfo = new ParameterInfo("filter[n][columnAlias]", false, "xs:string", ParameterStyle.QUERY, "The alias of the column");
        rpd.put("0", paramInfo);
        paramInfo = new ParameterInfo("filter[n][data][type]", false, "xs:string", ParameterStyle.QUERY, "The Client type of the column (numeric, string, boolean)");
        rpd.put("1", paramInfo);
        paramInfo = new ParameterInfo("filter[n][data][comparison]", false, "xs:string", ParameterStyle.QUERY, "The comparison operator. Should be in the Operator enum (LT, GT, EQ, LIKE ,IN, NOTIN)");
        rpd.put("2", paramInfo);
        paramInfo = new ParameterInfo("filter[n][data][value]", false, "xs:string", ParameterStyle.QUERY, "The value to compare with");
        rpd.put("3", paramInfo);
        this.setRequestParamsDescription(rpd);
    }

    @Override
    public List<Predicat> createPredicats(Request request, List<Predicat> predicats) throws Exception {
        DataSetApplication dsApplication = (DataSetApplication) getContext().getAttributes().get("DataSetApplication");
        DataSet ds = dsApplication.getDataSet();
        String logString = "REQUEST: " + request.toString();
        if (predicats != null) {
            logString += predicats.toString();
        }
        Form params = request.getResourceRef().getQueryAsForm();
        boolean filterExists = true;
        int i = 0;
        while (filterExists) {
            String prefix = TEMPLATE_PARAM.replace("#", Integer.toString(i++));
            String columnAlias = params.getFirstValue(prefix + Param.COLUMN_ALIAS.value());
            if (columnAlias != null) {
                String comparison = params.getFirstValue(prefix + Param.COMPARISON.value());
                Object value = new Object();
                String valueQuoted = SQLUtils.escapeString(params.getFirstValue(prefix + Param.VALUE.value()));
                if ("boolean".equals(params.getFirstValue(prefix + Param.TYPE.value()))) {
                    value = new Boolean(valueQuoted);
                } else if ("string".equals(params.getFirstValue(prefix + Param.TYPE.value()))) {
                    valueQuoted = valueQuoted.replace("*", "%");
                    value = (String) "'" + valueQuoted + "'";
                } else if ("numeric".equals(params.getFirstValue(prefix + Param.TYPE.value()))) {
                    try {
                        if (new Integer(params.getFirstValue(prefix + Param.VALUE.value())) instanceof Integer) {
                            value = new Integer(valueQuoted);
                        }
                    } catch (Exception e) {
                        value = null;
                    }
                    try {
                        if (new Double(params.getFirstValue(prefix + Param.VALUE.value())) instanceof Double) {
                            value = new Double(valueQuoted);
                        }
                    } catch (Exception e) {
                        value = null;
                    }
                } else if ("date".equals(params.getFirstValue(prefix + Param.TYPE.value())) && comparison.equals("eq")) {
                    value = valueQuoted;
                } else {
                    valueQuoted = valueQuoted.replace("*", "%");
                    value = (String) "'" + valueQuoted + "'";
                }
                if (value == null) {
                    throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "The entered value was not understandable");
                }
                Column column = ds.findByColumnAlias(columnAlias);
                if (column != null && column.getFilter()) {
                    if ("date".equals(params.getFirstValue(prefix + Param.TYPE.value())) && comparison.equals("eq")) {
                        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
                        Date t = null;
                        try {
                            t = ft.parse((String) value);
                        } catch (Exception e) {
                            try {
                                ft = new SimpleDateFormat("yyyy-MM-dd h:i:s");
                                t = ft.parse((String) value);
                            } catch (Exception f) {
                                throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "The entered value was not a date");
                            }
                        }
                        GregorianCalendar calendar = new GregorianCalendar();
                        calendar.setTime(t);
                        calendar.set(GregorianCalendar.HOUR, 0);
                        calendar.set(GregorianCalendar.MINUTE, 0);
                        calendar.set(GregorianCalendar.SECOND, 0);
                        t.setTime(calendar.getTimeInMillis());
                        Predicat predicat = new Predicat();
                        predicat.setLeftAttribute(column);
                        predicat.setRightValue("'" + ft.format(t) + "'");
                        predicat.setCompareOperator(convertOperator("gte"));
                        predicats.add(predicat);
                        predicat = new Predicat();
                        predicat.setLeftAttribute(column);
                        long time = t.getTime() + 24 * 60 * 60 * 1000;
                        Date nextDate = new Date(time);
                        predicat.setRightValue("'" + ft.format(nextDate) + "'");
                        predicat.setCompareOperator(convertOperator("lt"));
                        predicats.add(predicat);
                    } else {
                        Predicat predicat = new Predicat();
                        predicat.setLeftAttribute(column);
                        predicat.setRightValue(value);
                        predicat.setCompareOperator(convertOperator(comparison));
                        predicats.add(predicat);
                    }
                }
            } else {
                filterExists = false;
            }
        }
        return predicats;
    }

    /**
   * convert a operator to a SQL operator
   * 
   * @param compareOperator
   *          the operator
   * @return the SQL operator
   */
    public String convertOperator(String compareOperator) {
        for (Operator operator : Operator.values()) {
            if (operator.name().equalsIgnoreCase(compareOperator)) {
                return operator.value();
            }
        }
        return null;
    }

    /**
   * Gets the validator for this Filter
   * 
   * @return the validator for the filter
   */
    @Override
    public Validator<AbstractFilter> getValidator() {
        return new Validator<AbstractFilter>() {

            @Override
            public Set<ConstraintViolation> validate(AbstractFilter item) {
                return null;
            }
        };
    }
}

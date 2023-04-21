package com.antilia.web.field.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import org.apache.wicket.Session;
import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.extensions.yui.calendar.DateTimeField;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.request.ClientInfo;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converters.ZeroPaddingIntegerConverter;
import org.apache.wicket.util.lang.EnumeratedType;
import org.apache.wicket.validation.validator.RangeValidator;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;

/**s
 * Works on a {@link java.util.Date} object. Displays a date field and a {@link DatePicker}, a
 * field for hours and a field for minutes, and an AM/PM field. The format (12h/24h) of the hours
 * field depends on the time format of this {@link AntiliaDateTimeField}'s {@link Locale}, as does the
 * visibility of the AM/PM field (see {@link AntiliaDateTimeField#use12HourFormat}).
 * 
 * @author eelcohillenius
 * @see DateField for a variant with just the date field and date picker
 */
public class AntiliaDateTimeField extends FormComponentPanel<Date> {

    private static class AM_PM extends EnumeratedType {

        private static final long serialVersionUID = 1L;

        static final AM_PM AM = new AM_PM("AM");

        static final AM_PM PM = new AM_PM("PM");

        public static AM_PM[] values() {
            return new AM_PM[] { AM, PM };
        }

        private AM_PM(final String name) {
            super(name);
        }
    }

    private static final IConverter MINUTES_CONVERTER = new ZeroPaddingIntegerConverter(2);

    private static final long serialVersionUID = 1L;

    private AM_PM amOrPm = AM_PM.AM;

    private DropDownChoice<AM_PM> amOrPmChoice;

    private MutableDateTime date;

    private DateTextField dateField;

    private Integer hours;

    private TextField<Integer> hoursField;

    private Integer minutes;

    private TextField<Integer> minutesField;

    /**
	 * Construct.
	 * 
	 * @param id
	 */
    public AntiliaDateTimeField(String id) {
        this(id, null);
    }

    /**
	 * Construct.
	 * 
	 * @param id
	 * @param model
	 */
    @SuppressWarnings("unchecked")
    public AntiliaDateTimeField(String id, IModel<Date> model) {
        super(id, model);
        setType(Date.class);
        PropertyModel<Date> dateFieldModel = new PropertyModel<Date>(this, "date");
        add(dateField = newDateTextField("date", dateFieldModel));
        dateField.add(new AntiliaDatePicker() {

            private static final long serialVersionUID = 1L;

            protected void configure(Map<String, Object> widgetProperties) {
                super.configure(widgetProperties);
                AntiliaDateTimeField.this.configure(widgetProperties);
            }
        });
        add(hoursField = new TextField<Integer>("hours", new PropertyModel<Integer>(this, "hours"), Integer.class));
        hoursField.add(new HoursValidator());
        hoursField.setLabel(new Model<String>("hours"));
        add(minutesField = new TextField<Integer>("minutes", new PropertyModel<Integer>(this, "minutes"), Integer.class) {

            private static final long serialVersionUID = 1L;

            @Override
            public IConverter getConverter(Class type) {
                return MINUTES_CONVERTER;
            }
        });
        minutesField.add(new RangeValidator<Integer>(0, 59));
        minutesField.setLabel(new Model<String>("minutes"));
        add(amOrPmChoice = new DropDownChoice<AM_PM>("amOrPmChoice", new PropertyModel<AM_PM>(this, "amOrPm"), Arrays.asList(AM_PM.values())));
    }

    /**
	 * Gets amOrPm.
	 * 
	 * @return amOrPm
	 */
    public AM_PM getAmOrPm() {
        return amOrPm;
    }

    /**
	 * Gets date.
	 * 
	 * @return date
	 */
    public Date getDate() {
        return (date != null) ? date.toDate() : null;
    }

    /**
	 * Gets hours.
	 * 
	 * @return hours
	 */
    public Integer getHours() {
        return hours;
    }

    protected void configure(Map<String, Object> widgetProperties) {
    }

    /**
	 * @see org.apache.wicket.markup.html.form.FormComponent#getInput()
	 */
    @Override
    public String getInput() {
        return dateField.getInput() + ", " + hoursField.getInput() + ":" + minutesField.getInput();
    }

    /**
	 * Gets minutes.
	 * 
	 * @return minutes
	 */
    public Integer getMinutes() {
        return minutes;
    }

    /**
	 * Sets amOrPm.
	 * 
	 * @param amOrPm
	 *            amOrPm
	 */
    public void setAmOrPm(AM_PM amOrPm) {
        this.amOrPm = amOrPm;
    }

    /**
	 * Sets date.
	 * 
	 * @param date
	 *            date
	 */
    public void setDate(Date date) {
        this.date = (date != null) ? new MutableDateTime(date) : null;
        setDefaultModelObject(date);
    }

    /**
	 * Sets hours.
	 * 
	 * @param hours
	 *            hours
	 */
    public void setHours(Integer hours) {
        this.hours = hours;
    }

    /**
	 * Sets minutes.
	 * 
	 * @param minutes
	 *            minutes
	 */
    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    /**
	 * Gets the client's time zone.
	 * 
	 * @return The client's time zone or null
	 */
    protected TimeZone getClientTimeZone() {
        ClientInfo info = Session.get().getClientInfo();
        if (info instanceof WebClientInfo) {
            return ((WebClientInfo) info).getProperties().getTimeZone();
        }
        return null;
    }

    /**
	 * Sets the converted input, which is an instance of {@link Date}, possibly null. It combines
	 * the inputs of the nested date, hours, minutes and am/pm fields and constructs a date from it.
	 * <p>
	 * Note that overriding this method is a better option than overriding {@link #updateModel()}
	 * like the first versions of this class did. The reason for that is that this method can be
	 * used by form validators without having to depend on the actual model being updated, and this
	 * method is called by the default implementation of {@link #updateModel()} anyway (so we don't
	 * have to override that anymore).
	 * </p>
	 * 
	 * @see org.apache.wicket.markup.html.form.FormComponent#convertInput()
	 */
    @Override
    protected void convertInput() {
        Object dateFieldInput = dateField.getConvertedInput();
        if (dateFieldInput != null) {
            MutableDateTime date = new MutableDateTime(dateFieldInput);
            Integer hours = (Integer) hoursField.getConvertedInput();
            Integer minutes = (Integer) minutesField.getConvertedInput();
            AM_PM amOrPm = amOrPmChoice.getConvertedInput();
            try {
                boolean use12HourFormat = use12HourFormat();
                if (hours != null) {
                    date.set(DateTimeFieldType.hourOfDay(), hours.intValue() % getMaximumHours(use12HourFormat));
                    date.setMinuteOfHour((minutes != null) ? minutes.intValue() : 0);
                }
                if (use12HourFormat) {
                    date.set(DateTimeFieldType.halfdayOfDay(), amOrPm == AM_PM.PM ? 1 : 0);
                }
                TimeZone zone = getClientTimeZone();
                if (zone != null) {
                    date.setMillis(DateTimeZone.getDefault().getMillisKeepLocal(DateTimeZone.forTimeZone(zone), date.getMillis()));
                }
                setConvertedInput(date.toDate());
            } catch (RuntimeException e) {
                AntiliaDateTimeField.this.error(e.getMessage());
                invalid();
            }
        } else {
            setConvertedInput(null);
        }
    }

    /**
	 * create a new {@link DateTextField} instance to be added to this panel.
	 * 
	 * @param dateFieldModel
	 *            model that should be used by the {@link DateTextField}
	 * @return a new date text field instance
	 */
    protected DateTextField newDateTextField(String id, PropertyModel<Date> dateFieldModel) {
        return new DateTextField(id, dateFieldModel, new StyleDateConverter(false));
    }

    /**
	 * @see org.apache.wicket.Component#onBeforeRender()
	 */
    @Override
    protected void onBeforeRender() {
        dateField.setRequired(isRequired());
        hoursField.setRequired(isRequired());
        minutesField.setRequired(isRequired());
        dateField.setEnabled(isEnabled() && isEnableAllowed());
        hoursField.setEnabled(isEnabled() && isEnableAllowed());
        minutesField.setEnabled(isEnabled() && isEnableAllowed());
        amOrPmChoice.setEnabled(isEnabled() && isEnableAllowed());
        boolean use12HourFormat = use12HourFormat();
        amOrPmChoice.setVisible(use12HourFormat);
        Date d = (Date) getDefaultModelObject();
        if (d != null) {
            date = new MutableDateTime(d);
        } else {
            date = null;
        }
        if (date != null) {
            TimeZone zone = getClientTimeZone();
            if (zone != null) {
                date.setZone(DateTimeZone.forTimeZone(zone));
            }
            if (use12HourFormat) {
                int hourOfHalfDay = date.get(DateTimeFieldType.hourOfHalfday());
                hours = new Integer(hourOfHalfDay == 0 ? 12 : hourOfHalfDay);
            } else {
                hours = new Integer(date.get(DateTimeFieldType.hourOfDay()));
            }
            amOrPm = (date.get(DateTimeFieldType.halfdayOfDay()) == 0) ? AM_PM.AM : AM_PM.PM;
            minutes = new Integer(date.getMinuteOfHour());
        }
        super.onBeforeRender();
    }

    /**
	 * Checks whether the current {@link Locale} uses the 12h or 24h time format. This method can be
	 * overridden to e.g. always use 24h format.
	 * 
	 * @return true, if the current {@link Locale} uses the 12h format.<br/>false, otherwise
	 */
    protected boolean use12HourFormat() {
        String pattern = DateTimeFormat.patternForStyle("-S", getLocale());
        return pattern.indexOf('a') != -1 || pattern.indexOf('h') != -1 || pattern.indexOf('K') != -1;
    }

    /**
	 * @return either 12 or 24, depending on the hour format of the current {@link Locale}
	 */
    private int getMaximumHours() {
        return getMaximumHours(use12HourFormat());
    }

    /**
	 * Convenience method (mainly for optimization purposes), in case {@link #use12HourFormat()} has
	 * already been stored in a local variable and thus doesn't need to be computed again.
	 * 
	 * @param use12HourFormat
	 *            the hour format to use
	 * @return either 12 or 24, depending on the parameter <code>use12HourFormat</code>
	 */
    private int getMaximumHours(boolean use12HourFormat) {
        return use12HourFormat ? 12 : 24;
    }

    /**
	 * Validator for the {@link DateTimeField}'s hours field. Behaves like
	 * <code>RangeValidator</code>, setting appropriate range according to
	 * {@link DateTimeField#getMaximumHours()}
	 * 
	 * @see DateTimeField#getMaximumHours()
	 * @author Gerolf Seitz
	 */
    private class HoursValidator extends RangeValidator<Integer> {

        private static final long serialVersionUID = 1L;

        /**
		 * Constructor
		 */
        public HoursValidator() {
            if (getMaximumHours() == 24) {
                setRange(0, 23);
            } else {
                setRange(1, 12);
            }
        }
    }
}

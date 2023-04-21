package de.swm.commons.mobile.client.widgets;

import java.util.Date;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.text.client.DateTimeFormatRenderer;
import com.google.gwt.uibinder.client.UiConstructor;
import de.swm.commons.mobile.client.widgets.DatePopup.DateSelectionHandler;

/**
 * Defines a Date selection box.
 * 
 * 
 * {@link Deprecated} use {@link DatePopup} instead, not compatible with iOS5.
 */
public class DateTextBox extends BoxBase<Date> {

    private boolean isChangedByUser;

    /**
	 * Defines the different date styles.
	 * 
	 * @author wiese.daniel <br>
	 *         copyright (C) 2012, SWM Services GmbH
	 * 
	 */
    public enum DateStyle {

        /** Date sytle. **/
        DATE("dd. MMMM yyyy", "d. MMMM yyyy"), /** time sytle. **/
        TIME("HH:mm", "HH : mm"), /** Date time sytle. **/
        DATETIME("dd.MM.yyyy HH:mm", "d. M. yyyy HH : mm");

        private String pattern;

        private String parse;

        /**
		 * Default constructor.
		 * 
		 * @param pattern
		 *            the pattern
		 * @param parse
		 *            the pattern for parsing
		 */
        private DateStyle(String pattern, String parse) {
            this.pattern = pattern;
            this.parse = parse;
        }

        public String getPattern() {
            return pattern;
        }

        public String getParse() {
            return parse;
        }
    }

    private DatePopup datePopup;

    /**
	 * Default constructor.
	 */
    public DateTextBox() {
        this(DateStyle.DATETIME);
    }

    /**
	 * Alternative UI constructor.
	 * 
	 * @param dateStyle
	 *            the date style as defined in {@link DateStyle}
	 */
    @UiConstructor
    public DateTextBox(final DateStyle dateStyle) {
        super(dateStyle.toString().toLowerCase(), new DateTimeFormatRenderer(DateTimeFormat.getFormat(dateStyle.getPattern())), new DateTimeParser(dateStyle.getPattern()));
        setReadOnly(true);
        isChangedByUser = false;
        addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                datePopup = new DatePopup(getValue(), dateStyle, new DateSelectionHandler() {

                    @Override
                    public void dateSelectionCancelled() {
                    }

                    @Override
                    public void dateSelected(Date selectedDate) {
                        setValue(selectedDate);
                        isChangedByUser = true;
                    }
                });
                datePopup.showCentered(true);
            }
        });
    }

    /**
	 * Liefert true wenn das Datum vom Benutzer veandert wurde.
	 * 
	 * @return the isChangedByUser true wenn veraendert.
	 */
    public boolean isChangedByUser() {
        return isChangedByUser;
    }

    @Override
    public void setValue(Date value) {
        if (value != null) {
            super.setValue(value);
        }
    }
}

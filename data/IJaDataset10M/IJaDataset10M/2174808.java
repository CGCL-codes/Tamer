package org.apache.log4j.filter;

import org.apache.log4j.spi.LoggingEvent;

/**
  The NDCMatchFilter matches a configured value against the
  NDC value of a logging event.

  <p>The filter admits two options <b>ValueToMatch</b> and
  <b>ExactMatch</b>.

  <p>As the name indicates, the value of <b>ValueToMatch</b> property
  determines the string value to match. If <b>ExactMatch</b> is set
  to true, a match will occur only when <b>ValueToMatch</b> exactly
  matches the NDC value of the logging event.  Otherwise, if the
  <b>ExactMatch</b> property is set to <code>false</code>, a match
  will occur when <b>ValueToMatch</b> is contained anywhere within the
  NDC value. The <b>ExactMatch</b> property is set to
  <code>false</code> by default.

  <p>Note that by default <b>ValueToMatch</b> is set to
  <code>null</code> and will only match an empty NDC stack.

  <p>For more information about how the logging event will be
  passed to the appender for reporting, please see
  the {@link MatchFilterBase} class.

  @author Mark Womack

  @since 1.3
*/
public class NDCMatchFilter extends MatchFilterBase {

    /**
    The value to match in the NDC value of the LoggingEvent. */
    String valueToMatch;

    /**
    Do we look for an exact match or just a "contains" match? */
    boolean exactMatch = false;

    /**
    Sets the value to match in the NDC value of the LoggingEvent.

    @param value The value to match. */
    public void setValueToMatch(String value) {
        valueToMatch = value;
    }

    /**
    Gets the value to match in the NDC value of the LoggingEvent.

    @return String The value to match. */
    public String getValueToMatch() {
        return valueToMatch;
    }

    /**
    Set to true if configured value must exactly match the NDC
    value of the LoggingEvent. Set to false if the configured
    value must only be contained in the NDC value of the
    LoggingEvent. Default is false.

    @param exact True if an exact match should be checked for. */
    public void setExactMatch(boolean exact) {
        exactMatch = exact;
    }

    /**
    Returns the true if an exact match will be checked for.

    @return boolean True if an exact match will be checked for. */
    public boolean getExactMatch() {
        return exactMatch;
    }

    /**
    If <b>ExactMatch</b> is set to true, returns true only when
    <b>ValueToMatch</b> exactly matches the NDC value of the
    logging event. If the <b>ExactMatch</b> property
    is set to <code>false</code>, returns true when
    <b>ValueToMatch</b> is contained anywhere within the NDC
    value. Otherwise, false is returned.

    @param event The logging event to match against.
    @return boolean True if matches criteria. */
    protected boolean match(LoggingEvent event) {
        String eventNDC = event.getNDC();
        if (eventNDC == null) {
            return (valueToMatch == null);
        } else {
            if (valueToMatch != null) {
                if (exactMatch) {
                    return eventNDC.equals(valueToMatch);
                } else {
                    return (eventNDC.indexOf(valueToMatch) != -1);
                }
            } else {
                return false;
            }
        }
    }
}

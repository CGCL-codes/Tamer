package ch.gmtech.lab.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import ch.gmtech.lab.ApplicationLogger;
import ch.gmtech.lab.ApplicationLoggerInterface;
import ch.gmtech.lab.gui.UserMessage;

public class CourseCredits implements Command {

    public CourseCredits() {
        _logger = ApplicationLogger.getOnDemandInstance();
    }

    public CourseCredits(int value) {
        this();
        _value = value;
    }

    public void doWith(String aValue) {
        try {
            _value = Integer.valueOf(aValue);
        } catch (NumberFormatException e) {
            _logger.error(new UserMessage("Credito non valido: deve essere un intero!"), e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, false);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, false);
    }

    public void on(StringBuffer aBuffer) {
        aBuffer.append(String.valueOf(_value));
    }

    public void set(int credits) {
        _value = credits;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public int value() {
        return _value;
    }

    private final transient ApplicationLoggerInterface _logger;

    private Integer _value;

    public void on(CreditsCollector creditsCollector) {
        creditsCollector.add(this);
    }

    public CourseCredits sum(CourseCredits aCredits) {
        return new CourseCredits(_value + aCredits.value());
    }

    public boolean isMinorThan(CourseCredits credits) {
        return _value < credits.value();
    }

    public boolean isMaiorThan(CourseCredits credits) {
        return _value > credits.value();
    }
}

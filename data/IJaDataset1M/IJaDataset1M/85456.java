package org.grill.clovercash.storage;

import java.math.BigDecimal;
import org.joda.time.LocalDate;

public class StatementEntry {

    public String payeeName;

    public String memo;

    public LocalDate date;

    public BigDecimal value;

    public String toString() {
        return date.toString() + ": " + payeeName + ": " + value.toPlainString();
    }
}

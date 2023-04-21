package de.zeiban.loppe.data;

import java.math.BigDecimal;
import java.sql.Connection;
import de.zeiban.loppe.dbcore.DbTemplate;

public class SummeGesamtInfoProvider implements Summenprovider {

    private final Connection connection;

    public SummeGesamtInfoProvider(Connection connection) {
        this.connection = connection;
    }

    public BigDecimal getSumme() {
        BigDecimal gessum = new DbTemplate(connection).selectObject("select sum(preis) from kauf");
        return (gessum == null ? BigDecimal.ZERO : gessum);
    }
}

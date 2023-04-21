package br.com.arsmachina.dao.hibernate.dialect;

import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.id.IdentityGenerator;
import org.hibernate.id.SequenceGenerator;

/**
 * {@link PostgreSQLDialect} that defines {@link IdentityGenerator} as the default id value
 * generator instead of {@link SequenceGenerator}. In addition, it fixes the "wrong sequence name
 * in backticks-scaped table name bug".
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class IdentityPostresqlDialect extends PostgreSQLDialect {

    /**
	 * Returns {@link IdentityGenerator} instead of {@link SequenceGenerator}.
	 * 
	 * @see org.hibernate.dialect.PostgreSQLDialect#getNativeIdentifierGeneratorClass()
	 */
    @SuppressWarnings("unchecked")
    @Override
    public Class getNativeIdentifierGeneratorClass() {
        return IdentityGenerator.class;
    }

    /**
	 * Removes the backtickes from the <code>sequenceName</code> and then returns
	 * <code>super.getSequenceNextValString(sequenceName)</code>.
	 * @see org.hibernate.dialect.PostgreSQLDialect#getSequenceNextValString(java.lang.String)
	 */
    @Override
    public String getIdentitySelectString(String table, String column, int type) {
        table = table.replace("\"", "");
        return new StringBuffer().append("select currval('").append(table).append('_').append(column).append("_seq')").toString();
    }
}

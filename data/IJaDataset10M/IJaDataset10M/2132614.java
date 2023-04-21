package schemacrawler.build;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import schemacrawler.schema.JdbcDriverInfo;
import schemacrawler.schema.JdbcDriverProperty;

/**
 * JDBC driver information. Created from metadata returned by a JDBC
 * call, and other sources of information.
 * 
 * @author Sualeh Fatehi sualeh@hotmail.com
 */
final class MutableJdbcDriverInfo implements JdbcDriverInfo {

    public final class JdbcDriverInfoBuilder {

        private static final long serialVersionUID = 8030156654422512161L;

        private String driverName;

        private String driverClassName;

        private String driverVersion;

        private String connectionUrl;

        private boolean jdbcCompliant;

        private final Set<MutableJdbcDriverProperty> jdbcDriverProperties = new HashSet<MutableJdbcDriverProperty>();

        public void addJdbcDriverProperty(final MutableJdbcDriverProperty jdbcDriverProperty) {
            jdbcDriverProperties.add(jdbcDriverProperty);
        }

        public JdbcDriverInfo build() {
            return new MutableJdbcDriverInfo(this);
        }

        public String getConnectionUrl() {
            return connectionUrl;
        }

        public String getDriverClassName() {
            return driverClassName;
        }

        public String getDriverName() {
            return driverName;
        }

        public JdbcDriverProperty[] getDriverProperties() {
            final JdbcDriverProperty[] properties = jdbcDriverProperties.toArray(new JdbcDriverProperty[jdbcDriverProperties.size()]);
            Arrays.sort(properties);
            return properties;
        }

        public String getDriverVersion() {
            return driverVersion;
        }

        public boolean isJdbcCompliant() {
            return jdbcCompliant;
        }

        public void setConnectionUrl(final String connectionUrl) {
            this.connectionUrl = connectionUrl;
        }

        public void setDriverClassName(final String driverClassName) {
            this.driverClassName = driverClassName;
        }

        public void setDriverName(final String driverName) {
            this.driverName = driverName;
        }

        public void setDriverVersion(final String driverVersion) {
            this.driverVersion = driverVersion;
        }

        public void setJdbcCompliant(final boolean jdbcCompliant) {
            this.jdbcCompliant = jdbcCompliant;
        }
    }

    private static final long serialVersionUID = 8030156654422512161L;

    private static final String NEWLINE = System.getProperty("line.separator");

    private final String driverName;

    private final String driverClassName;

    private final String driverVersion;

    private final String connectionUrl;

    private final boolean jdbcCompliant;

    private final Set<MutableJdbcDriverProperty> jdbcDriverProperties;

    public MutableJdbcDriverInfo(final JdbcDriverInfoBuilder builder) {
        driverName = builder.driverName;
        driverClassName = builder.driverClassName;
        driverVersion = builder.driverVersion;
        connectionUrl = builder.connectionUrl;
        jdbcCompliant = builder.jdbcCompliant;
        jdbcDriverProperties = Collections.unmodifiableSet(builder.jdbcDriverProperties);
    }

    /**
   * {@inheritDoc}
   * 
   * @see schemacrawler.schema.JdbcDriverInfo#getConnectionUrl()
   */
    public String getConnectionUrl() {
        return connectionUrl;
    }

    /**
   * {@inheritDoc}
   * 
   * @see schemacrawler.schema.JdbcDriverInfo#getDriverClassName()
   */
    public String getDriverClassName() {
        return driverClassName;
    }

    /**
   * {@inheritDoc}
   * 
   * @see schemacrawler.schema.JdbcDriverInfo#getDriverName()
   */
    public String getDriverName() {
        return driverName;
    }

    /**
   * {@inheritDoc}
   * 
   * @see schemacrawler.schema.JdbcDriverInfo#getDriverProperties()
   */
    public JdbcDriverProperty[] getDriverProperties() {
        final JdbcDriverProperty[] properties = jdbcDriverProperties.toArray(new JdbcDriverProperty[jdbcDriverProperties.size()]);
        Arrays.sort(properties);
        return properties;
    }

    /**
   * {@inheritDoc}
   * 
   * @see schemacrawler.schema.JdbcDriverInfo#getDriverVersion()
   */
    public String getDriverVersion() {
        return driverVersion;
    }

    /**
   * {@inheritDoc}
   * 
   * @see schemacrawler.schema.JdbcDriverInfo#isJdbcCompliant()
   */
    public boolean isJdbcCompliant() {
        return jdbcCompliant;
    }

    /**
   * {@inheritDoc}
   * 
   * @see Object#toString()
   */
    @Override
    public String toString() {
        final StringBuilder info = new StringBuilder();
        info.append("-- driver: ").append(getDriverName()).append(' ').append(getDriverVersion()).append(NEWLINE);
        info.append("-- driver class: ").append(getDriverClassName()).append(NEWLINE);
        info.append("-- url: ").append(getConnectionUrl()).append(NEWLINE);
        info.append("-- jdbc compliant: ").append(isJdbcCompliant());
        return info.toString();
    }
}

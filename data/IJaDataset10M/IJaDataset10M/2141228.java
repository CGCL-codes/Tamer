package com.farata.cleardatabuilder.migration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import org.eclipse.core.runtime.Path;
import com.farata.cleardatabuilder.migration.MigrationRecord.MigrationMethod;
import com.farata.cleardatabuilder.migration.MigrationRecord.MigrationObject;

public class MigrationProperties {

    private final Map<String, MigrationRecord> records = new HashMap<String, MigrationRecord>();

    public MigrationProperties() {
        super();
    }

    public Collection<MigrationRecord> records() {
        return this.records.values();
    }

    public MigrationProperties loadSpringFromClassPath() throws IOException {
        load(MigrationProperties.class.getResourceAsStream("migration-spring.properties"));
        return this;
    }

    public MigrationProperties loadFromClassPath() throws IOException {
        load(MigrationProperties.class.getResourceAsStream("migration.properties"));
        return this;
    }

    /**
	 * Loads migration properties from specified stream which must represent a
	 * property file content.
	 */
    public MigrationProperties load(final InputStream stream) throws IOException {
        final Properties properties = new Properties();
        properties.load(stream);
        load(properties);
        return this;
    }

    public MigrationProperties load(Hashtable<Object, Object> properties) {
        this.records.clear();
        for (final Entry<Object, Object> property : properties.entrySet()) {
            String key = String.valueOf(property.getKey());
            String value = String.valueOf(property.getValue());
            final int lastIndexOfPointInKey = key.lastIndexOf('.');
            if (lastIndexOfPointInKey >= 0) {
                final String recordAliasToken = key.substring(0, lastIndexOfPointInKey);
                final String recordFieldToken = key.substring(lastIndexOfPointInKey + 1);
                final MigrationRecord record = getOrCreateRecord(recordAliasToken);
                if ("source".equalsIgnoreCase(recordFieldToken)) {
                    record.source = new Path(value);
                } else if ("target".equalsIgnoreCase(recordFieldToken)) {
                    record.target = new Path(value);
                } else if ("method".equalsIgnoreCase(recordFieldToken)) {
                    record.method = Enum.valueOf(MigrationMethod.class, value);
                } else if ("object".equalsIgnoreCase(recordFieldToken)) {
                    record.object = Enum.valueOf(MigrationObject.class, value);
                }
            }
        }
        return this;
    }

    private MigrationRecord getOrCreateRecord(final String alias) {
        MigrationRecord result = this.records.get(alias);
        if (result == null) {
            result = new MigrationRecord();
            this.records.put(alias, result);
        }
        return result;
    }
}

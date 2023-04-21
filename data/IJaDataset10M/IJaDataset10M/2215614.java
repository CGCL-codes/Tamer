package blomo.executables.createhbm;

import java.util.Map;
import java.util.Set;

/**
 * @author Malte Schulze
 *
 */
public interface MetadataLoader {

    /**
	 * @param firstColumns columns that should be sorted to the front
	 * @param tables all tables extracted from the DB schema
	 */
    void sortColumns(Map<String, String> firstColumns, Set<Table> tables);

    /**
	 * @param tables all tables extracted from the DB schema
	 * @param url jdbc url
	 * @param user login for DB
	 * @param pwd password for DB login
	 * @param util the util used for generator
	 */
    void fetchMetadata(Set<Table> tables, String url, String user, String pwd, Util util);
}

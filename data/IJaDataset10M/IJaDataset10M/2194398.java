package rover;

import java.util.Map;

/**
 * Information regarding a database table.
 * 
 * @author tzellman
 * 
 */
public interface ITableInfo {

    String getName();

    Map<String, IFieldInfo> getFields();
}

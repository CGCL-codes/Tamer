package er.extensions;

import com.webobjects.foundation.NSDictionary;

public interface ERXGeneratesPrimaryKeyInterface {

    public NSDictionary primaryKeyDictionary(boolean inTransaction);
}

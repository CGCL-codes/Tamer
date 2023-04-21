package snipsnap.storage.serializer;

import org.dom4j.Element;
import java.util.Map;

public interface Serializer {

    /**
   * Special serialize method that does not have dependencies on regular Snip
   * or other implementations used throughout snipsnap. This is necessary to
   * be able to dump a database with a small utility.
   *
   * @param snipMap a map containing the snips data
   * @return an element that can be serializes as XML
   */
    Element serialize(Map snipMap);
}

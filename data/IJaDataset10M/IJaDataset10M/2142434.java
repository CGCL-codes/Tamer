package grammarscope.generator;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import edu.stanford.nlp.trees.GrammaticalRelation;

public class Tweak {

    static Map<String, GrammaticalRelation> initMap(final String thisClassName) throws SecurityException, IllegalArgumentException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        final Map<String, GrammaticalRelation> result = new HashMap<String, GrammaticalRelation>();
        final Class<?> thisClass = Class.forName(thisClassName);
        final Field[] theseFields = thisClass.getDeclaredFields();
        for (final Field f : theseFields) {
            final String name = f.getName();
            final boolean isGr = f.getType().getName().equals("edu.stanford.nlp.trees.GrammaticalRelation");
            if (isGr) {
                final GrammaticalRelation gr = Evaluator.eval(name, thisClass.getName());
                result.put(name, gr);
            }
        }
        return result;
    }
}

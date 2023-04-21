package gilgamesh.annotations.filters;

import static org.apache.commons.lang.ArrayUtils.contains;

public class Exclude extends Filter {

    @Override
    protected boolean filter(String fieldName, String... args) {
        return !contains(args, fieldName);
    }
}

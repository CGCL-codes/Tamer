package newsatort.persistence.source.filter;

import java.util.List;

public class MultiValueFilter extends AbstractValueFilter {

    public MultiValueFilter(List<? extends Object> objectsList) {
        super(objectsList);
    }
}

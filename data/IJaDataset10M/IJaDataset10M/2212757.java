package apollo.gui.detailviewers.sequencealigner.filters;

import java.util.List;

public interface MultiFilter<T> extends Filter<T>, List<Filter<T>> {
}

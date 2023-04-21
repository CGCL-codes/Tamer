package org.nms.spider.helpers.utils;

import java.util.ArrayList;
import java.util.List;
import org.nms.spider.beans.IElement;
import org.nms.spider.helpers.AbstractProcessor;
import org.nms.spider.helpers.IFilter;
import org.nms.spider.helpers.IProcessorHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilterProcessorImpl extends AbstractProcessor implements IProcessorHelper {

    private static final Logger log = LoggerFactory.getLogger(FilterProcessorImpl.class.toString());

    @SuppressWarnings("rawtypes")
    private List<IElement> filteredElements;

    /**
	 * The filter to apply.
	 */
    private IFilter filter;

    @SuppressWarnings("rawtypes")
    @Override
    public List<IElement> process(List<IElement> elements) {
        if (filter == null) {
            log.warn("No filter to apply : returning the elements.");
            return elements;
        }
        List<IElement> result = new ArrayList<IElement>();
        List<IElement> filtered = new ArrayList<IElement>();
        for (IElement e : elements) {
            if (filter.passes(e)) {
                result.add(e);
            } else {
                filtered.add(e);
            }
        }
        this.filteredElements = filtered;
        log.info("FILTERED ELEMENTS : " + filtered.size() + "/" + elements.size());
        return result;
    }

    @SuppressWarnings("rawtypes")
    public List<IElement> getFilteredElements() {
        return filteredElements;
    }

    @SuppressWarnings("rawtypes")
    public void setFilteredElements(List<IElement> filteredElements) {
        this.filteredElements = filteredElements;
    }

    public IFilter getFilter() {
        return filter;
    }

    public void setFilter(IFilter filter) {
        this.filter = filter;
    }
}

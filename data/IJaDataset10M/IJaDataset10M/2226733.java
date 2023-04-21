package edu.sharif.ce.dml.common.data.trace;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Masoud
 * Date: Jul 19, 2007
 * Time: 11:01:19 AM
 */
public interface Tracable {

    public List print();

    /**
     * it should be constant over times.
     * @return
     */
    public List<String> getLabels();
}

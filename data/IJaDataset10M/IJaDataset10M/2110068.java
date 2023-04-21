package fr.cnes.sitools.dataset.filter.model;

import java.io.Serializable;
import java.util.HashMap;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import fr.cnes.sitools.common.model.ExtensionModel;

/**
 * Class bean to store filter definition.
 * 
 * @author m.marseille (AKKA Technologies)
 */
@SuppressWarnings("serial")
@XStreamAlias("filterModel")
public final class FilterModel extends ExtensionModel<FilterParameter> implements Serializable {

    /**
   * The status of the Filter
   */
    private String status;

    /**
   * Constructor.
   */
    public FilterModel() {
        this.setName("NullFilter");
        this.setDescription("Filter with no action.");
        this.setParametersMap(new HashMap<String, FilterParameter>());
        this.setClassVersion("");
        this.setClassAuthor("");
    }

    /**
   * Sets the value of status
   * 
   * @param status
   *          the status to set
   */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
   * Gets the status value
   * 
   * @return the status
   */
    public String getStatus() {
        return status;
    }
}

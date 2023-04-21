package fr.cnes.sitools.dataset.form.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Time period selection representattion
 * 
 * @author BAILLAGOU
 * @version 1.0 01-09-2010 08:59:07
 */
@XStreamAlias("TimePeriodSelection")
public final class TimePeriodSelection extends AbstractPeriodSelection {

    /**
   * Time starts
   */
    private String from;

    /**
   * Time end
   */
    private String to;

    /**
   * Constructor
   */
    public TimePeriodSelection() {
        super();
    }

    /**
   * Get the start
   * 
   * @return the from
   */
    public String getFrom() {
        return from;
    }

    /**
   * Set the start
   * 
   * @param from
   *          the from to set
   */
    public void setFrom(final String from) {
        this.from = from;
    }

    /**
   * Get the end
   * 
   * @return the to
   */
    public String getTo() {
        return to;
    }

    /**
   * Set the end
   * 
   * @param to
   *          the to to set
   */
    public void setTo(String to) {
        this.to = to;
    }
}

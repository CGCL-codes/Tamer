package net.sourceforge.jcodebaseHQ.ticket;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import net.sourceforge.jcodebaseHQ.Constants;
import net.sourceforge.jcodebaseHQ.validator.IntegerValidator;
import net.sourceforge.jcodebaseHQ.validator.StringValidator;

@XmlRootElement(name = Constants.TICKETCATEGORY_XML_TAG)
public class TicketCategory {

    private int id;

    private String name;

    private IntegerValidator notLessThanMinusOneValidator;

    private StringValidator notEmptyOrNullValidator;

    public TicketCategory() {
        this.notLessThanMinusOneValidator = new IntegerValidator(-1);
        this.notEmptyOrNullValidator = new StringValidator(false, false);
    }

    public TicketCategory(String name) {
        this();
        this.setId(-1);
        this.setName(name);
    }

    public TicketCategory(int id, String name) {
        this();
        this.setId(id);
        this.setName(name);
    }

    /**
     * @return the id
     */
    @XmlElement(name = Constants.TICKETCATEGORY_ID_XML_TAG)
    public int getId() {
        return this.id;
    }

    /**
     * @param id
     *        the id to set
     */
    public void setId(int id) {
        this.notLessThanMinusOneValidator.validate(id, "ticketCategoryId");
        this.id = id;
    }

    /**
     * @return the name
     */
    @XmlElement(name = Constants.TICKETCATEGORY_NAME_XML_TAG)
    public String getName() {
        return this.name;
    }

    /**
     * @param name
     *        the name to set
     */
    public void setName(String name) {
        this.notEmptyOrNullValidator.validate(name, "ticketCategoryName");
        this.name = name;
    }

    @Override
    public String toString() {
        return this.getName() + "\n id: " + this.getId();
    }
}

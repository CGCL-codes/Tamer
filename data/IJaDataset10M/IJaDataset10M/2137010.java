package org.blueoxygen.papaje.country;

/**
 * @author leo
 *
 */
public class DeleteCountry extends CountryForm {

    public String execute() {
        super.execute();
        getManager().remove(getCountry());
        setMsg("Delete");
        return SUCCESS;
    }
}

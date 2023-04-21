package doghost.utils;

/**
 *
 * @author duo
 */
public class CustomFormattedDateHMConverter extends CustomFormattedDateConverter {

    public CustomFormattedDateHMConverter() {
        super();
        this.setDateMask("dd/MM/yyyy HH:mm");
    }
}

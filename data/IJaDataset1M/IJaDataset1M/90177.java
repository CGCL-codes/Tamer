package ro.gateway.aida.usr.messaging;

/**
 * @author Mihai Postelnicu
 * PersonFilter
 *
 *  *
 */
public class PersonFilter extends Filter {

    protected String text;

    protected int countryISO3;

    public PersonFilter(long id, long user_id) {
        super(id, user_id);
    }

    /**
       * @return
       */
    public int getCountryISO3() {
        return countryISO3;
    }

    /**
       * @return
       */
    public String getText() {
        return text;
    }

    /**
       * @param i
       */
    public void setCountryISO3(int i) {
        countryISO3 = i;
    }

    /**
       * @param string
       */
    public void setText(String string) {
        text = string;
    }
}

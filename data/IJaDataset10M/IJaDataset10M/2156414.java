package superabbrevs;

/**
 * @author Sune Simonsen
 * class TempTranformationField
 */
public class TempTranformationField implements Field {

    private String code;

    private Integer number;

    public TempTranformationField(Integer number, String code) {
        this.number = number;
        this.code = code;
    }

    public Integer getNumber() {
        return number;
    }

    public String getCode() {
        return code;
    }

    public String toString() {
        return "<code>";
    }

    public int getLength() {
        return 6;
    }
}

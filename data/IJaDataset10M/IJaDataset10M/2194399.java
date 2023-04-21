package net.sf.fallfair.BusinessLogic;

import net.sf.fallfair.BusinessLogic.CRUD.PrizeCRUD;
import net.sf.fallfair.utils.DecimalFormatWrapper;
import net.sf.fallfair.utils.PlaceDescription;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Map;

public class Prize implements Comparable {

    public Prize() {
    }

    public Prize(int prizeId, int place, String description, BigDecimal amount, Exhibitor mExhibitor) {
        this.setPrizeId(prizeId);
        this.setPlace(place);
        this.setDescription(description);
        this.setAmount(amount);
        this.setExhibitor(mExhibitor);
    }

    public String getListDescription() {
        StringBuilder result = new StringBuilder();
        result.append(PlaceDescription.convert(getPlace()));
        if (!getDescription().equals("")) {
            result.append(" - " + getDescription());
        }
        if (getAmount().compareTo(new BigDecimal(0)) != 0) {
            result.append(" - " + DecimalFormatWrapper.formatCurrency(getAmount()));
        }
        return result.toString();
    }

    public int compareTo(Object obj) {
        int result = -1;
        if (obj instanceof Prize) {
            Prize compObj = (Prize) obj;
            result = getPlace() - compObj.getPlace();
        }
        return result;
    }

    public static Prize readById(Map<String, Object> searchCriteria) throws SQLException {
        return PrizeCRUD.readById(searchCriteria);
    }

    public static void update(Section aSection) throws SQLException {
        PrizeCRUD.update(aSection);
    }

    public static void clear(Section aSection) throws SQLException {
        PrizeCRUD.clear(aSection);
    }

    @Override
    public boolean equals(Object obj) {
        boolean equals = false;
        if (obj instanceof Prize) {
            Prize compObj = (Prize) obj;
            if (getPrizeId() == compObj.getPrizeId()) {
                equals = true;
            }
        }
        return equals;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + this.prizeId;
        hash = 19 * hash + this.year;
        hash = 19 * hash + this.place;
        hash = 19 * hash + (this.description != null ? this.description.hashCode() : 0);
        hash = 19 * hash + (this.amount != null ? this.amount.hashCode() : 0);
        hash = 19 * hash + (this.exhibitor != null ? this.exhibitor.hashCode() : 0);
        return hash;
    }

    public int getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(int val) {
        this.prizeId = val;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int val) {
        this.place = val;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String val) {
        this.description = val;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal val) {
        this.amount = val;
    }

    public Exhibitor getExhibitor() {
        if (exhibitor == null) {
            exhibitor = new Exhibitor();
        }
        return exhibitor;
    }

    public void setExhibitor(Exhibitor val) {
        this.exhibitor = val;
    }

    private int prizeId;

    private int year;

    private int place;

    private String description;

    private BigDecimal amount;

    private Exhibitor exhibitor;

    public static final String PRIZE_ID_KEY = "PRIZE_ID";

    public static final String YEAR_KEY = "YEAR";

    public static final String PLACE_KEY = "PLACE";

    public static final String DESCRIPTION_KEY = "DESCRIPTION";

    public static final String AMOUNT_KEY = "AMOUNT";

    public static final String EXHIBITOR_ID_KEY = "EXHIBITOR_ID";

    public static final String PRIZE_IDS_KEY = "PRIZE_IDS";
}

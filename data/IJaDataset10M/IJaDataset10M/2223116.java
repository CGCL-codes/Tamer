package net.sf.fallfair.BusinessLogic;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Map;
import net.sf.fallfair.BusinessLogic.CRUD.FallFairCRUD;

/**
 *
 * @author nathanj
 */
public class FallFair {

    public FallFair() {
    }

    public static FallFair read(Map<String, Object> searchCriteria) throws SQLException {
        return FallFairCRUD.read(searchCriteria);
    }

    public static void update(FallFair fallFair) throws SQLException {
        FallFairCRUD.update(fallFair);
    }

    public int getFallFairId() {
        return fallFairId;
    }

    public void setFallFairId(int fallFairId) {
        this.fallFairId = fallFairId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getFourHPrize() {
        return fourHPrize;
    }

    public void setFourHPrize(BigDecimal fourHPrize) {
        this.fourHPrize = fourHPrize.setScale(2);
    }

    public SectionType getMembershipCalculatedAgainst() {
        return membershipCalculatedAgainst;
    }

    public void setMembershipCalculatedAgainst(SectionType membershipCalculatedAgainst) {
        this.membershipCalculatedAgainst = membershipCalculatedAgainst;
    }

    public BigDecimal getAnnualMembership() {
        return annualMembership;
    }

    public void setAnnualMembership(BigDecimal annualMembership) {
        this.annualMembership = annualMembership.setScale(2);
    }

    public BigDecimal getMinimumAwardedBeforeEntryFee() {
        return minimumAwardedBeforeEntryFee;
    }

    public void setMinimumAwardedBeforeEntryFee(BigDecimal minimumAwardedBeforeEntryFee) {
        this.minimumAwardedBeforeEntryFee = minimumAwardedBeforeEntryFee.setScale(2);
    }

    public BigDecimal getEntryFeePercent() {
        return entryFeePercent;
    }

    public void setEntryFeePercent(BigDecimal entryFeePercent) {
        this.entryFeePercent = entryFeePercent.setScale(2);
    }

    private int fallFairId;

    private int year;

    private String name;

    private BigDecimal fourHPrize;

    private SectionType membershipCalculatedAgainst;

    private BigDecimal annualMembership;

    private BigDecimal minimumAwardedBeforeEntryFee;

    private BigDecimal entryFeePercent;

    public static final String ID_KEY = "FALL_FAIR_ID";

    public static final String YEAR_KEY = "YEAR";
}

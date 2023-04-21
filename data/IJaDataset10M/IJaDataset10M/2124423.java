package net.sf.fallfair.exhibitorentry;

import java.util.List;
import net.sf.fallfair.prize.Prize;
import net.sf.fallfair.utils.PlaceDescription;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public abstract class AbstractExhibitorEntryDataSource implements JRDataSource {

    protected List<ExhibitorEntry> exhibitorEntry;

    protected int exhibitorEntryIndex;

    protected int sectionIndex;

    protected int prizeIndex;

    public AbstractExhibitorEntryDataSource(List<ExhibitorEntry> exhibitorEntry) {
        super();
        this.exhibitorEntry = exhibitorEntry;
        init();
    }

    public void init() {
        this.exhibitorEntryIndex = -1;
        this.sectionIndex = -1;
        this.prizeIndex = -1;
    }

    @Override
    public Object getFieldValue(JRField jasperField) throws JRException {
        if ("exhibitor_id".equals(jasperField.getName())) {
            return this.exhibitorEntry.get(this.exhibitorEntryIndex).getExhibitor().getId();
        } else if ("full_name".equals(jasperField.getName())) {
            return this.exhibitorEntry.get(this.exhibitorEntryIndex).getExhibitor().getContact().getFullname();
        } else if ("address_1".equals(jasperField.getName())) {
            return this.exhibitorEntry.get(this.exhibitorEntryIndex).getExhibitor().getContact().getAddress1();
        } else if ("address_2".equals(jasperField.getName())) {
            return this.exhibitorEntry.get(this.exhibitorEntryIndex).getExhibitor().getContact().getAddress2();
        } else if ("city".equals(jasperField.getName())) {
            return this.exhibitorEntry.get(this.exhibitorEntryIndex).getExhibitor().getContact().getCity();
        } else if ("province_code".equals(jasperField.getName())) {
            return this.exhibitorEntry.get(this.exhibitorEntryIndex).getExhibitor().getContact().getProvince().getProvinceCode();
        } else if ("postal_code".equals(jasperField.getName())) {
            return this.exhibitorEntry.get(this.exhibitorEntryIndex).getExhibitor().getContact().getPostalCode();
        } else if ("exhibitor_code".equals(jasperField.getName())) {
            return this.exhibitorEntry.get(this.exhibitorEntryIndex).getExhibitor().getExhibitorCode();
        } else if ("division_description".equals(jasperField.getName())) {
            return this.exhibitorEntry.get(this.exhibitorEntryIndex).getSectionsEntered().get(this.sectionIndex).getFairClass().getDivision().getDescription();
        } else if ("class_description".equals(jasperField.getName())) {
            return this.exhibitorEntry.get(this.exhibitorEntryIndex).getSectionsEntered().get(this.sectionIndex).getFairClass().getDescription();
        } else if ("section_description".equals(jasperField.getName())) {
            return this.exhibitorEntry.get(this.exhibitorEntryIndex).getSectionsEntered().get(this.sectionIndex).getDescription();
        } else if ("section_place".equals(jasperField.getName())) {
            List<Prize> prizesAwarded = this.exhibitorEntry.get(this.exhibitorEntryIndex).getSectionsEntered().get(this.sectionIndex).getAwarded(this.exhibitorEntry.get(this.exhibitorEntryIndex).getExhibitor());
            if (prizesAwarded.size() == 0) {
                return null;
            }
            return PlaceDescription.convert(prizesAwarded.get(this.prizeIndex).getPlace());
        } else if ("section_place_description".equals(jasperField.getName())) {
            List<Prize> prizesAwarded = this.exhibitorEntry.get(this.exhibitorEntryIndex).getSectionsEntered().get(this.sectionIndex).getAwarded(this.exhibitorEntry.get(this.exhibitorEntryIndex).getExhibitor());
            if (prizesAwarded.size() == 0) {
                return null;
            }
            return prizesAwarded.get(this.prizeIndex).getDescription();
        } else if ("section_place_amount".equals(jasperField.getName())) {
            List<Prize> prizesAwarded = this.exhibitorEntry.get(this.exhibitorEntryIndex).getSectionsEntered().get(this.sectionIndex).getAwarded(this.exhibitorEntry.get(this.exhibitorEntryIndex).getExhibitor());
            if (prizesAwarded.size() == 0) {
                return null;
            }
            return prizesAwarded.get(this.prizeIndex).getAmount();
        } else if ("regular_awarded".equals(jasperField.getName())) {
            return this.exhibitorEntry.get(this.exhibitorEntryIndex).getAwardedBySectionType().get(1);
        } else if ("special_awarded".equals(jasperField.getName())) {
            return this.exhibitorEntry.get(this.exhibitorEntryIndex).getAwardedBySectionType().get(2);
        } else if ("four_h_awarded".equals(jasperField.getName())) {
            return this.exhibitorEntry.get(this.exhibitorEntryIndex).getFourHAwarded();
        } else if ("membership_amount".equals(jasperField.getName())) {
            return this.exhibitorEntry.get(this.exhibitorEntryIndex).getMembershipAmount();
        } else if ("entry_fee".equals(jasperField.getName())) {
            return this.exhibitorEntry.get(this.exhibitorEntryIndex).getEntryFee();
        } else if ("net_awarded".equals(jasperField.getName())) {
            return this.exhibitorEntry.get(this.exhibitorEntryIndex).getNetAwarded();
        } else {
            System.out.println("Unsupported Field Name: " + jasperField.getName());
        }
        return null;
    }
}

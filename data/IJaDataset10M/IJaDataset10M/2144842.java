package org.personalsmartspace.ex.dpi_data_gen.impl.operators;

import org.personalsmartspace.ex.dpi_data_gen.impl.values.ActivityValues;
import org.personalsmartspace.ex.dpi_data_gen.impl.values.StatusValues;

/**
 * @author Elizabeth
 * 
 */
public final class Arthur extends ConfigurablePSS {

    @Override
    protected String getActivity() {
        return ActivityValues.SITTING;
    }

    @Override
    protected String getAge() {
        return "63";
    }

    @Override
    protected String getBankAccount() {
        return "GB57 0302 7789 1000 390";
    }

    @Override
    protected String getCreditCardCRC() {
        return "702";
    }

    @Override
    protected String getCreditCardExpiryDate() {
        return "5/3/2012";
    }

    @Override
    protected String getCreditCardName() {
        return "Arthur Miller";
    }

    @Override
    protected String getCreditCardNumber() {
        return "5319 7757 8097 2291";
    }

    @Override
    protected String getCreditCardStartDate() {
        return "5/3/2002";
    }

    @Override
    protected String getEducation() {
        return "PhD of computer sciences";
    }

    @Override
    protected String getEmail() {
        return "arthur.miller@gmail.com";
    }

    @Override
    protected String getHomeAddress() {
        return "30 John Islip St SW1P 4 London";
    }

    @Override
    protected String getIncome() {
        return "7000 GBP";
    }

    @Override
    protected String getMedicalData() {
        return "Arthur Miller {male;RH-;B}";
    }

    @Override
    protected String getName() {
        return "Arthur Miller";
    }

    @Override
    protected String getPhone() {
        return "0044 15 114 774";
    }

    @Override
    protected String getProfession() {
        return "senior-researcher";
    }

    @Override
    protected String getSex() {
        return "male";
    }

    @Override
    protected String getStatus() {
        return StatusValues.BUSY;
    }

    @Override
    protected String getSymbolicLocation() {
        return null;
    }

    @Override
    protected String getWorkAddress() {
        return "Highlands House, 165 The Broadway, Wimbledon, London, SW19 1NE";
    }

    @Override
    protected String getLocation() {
        return null;
    }

    @Override
    protected String getRole() {
        return "roleA";
    }
}

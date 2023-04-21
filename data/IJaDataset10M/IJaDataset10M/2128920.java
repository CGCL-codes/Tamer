package org.personalsmartspace.ex.dpi_data_gen.impl.operators;

import org.personalsmartspace.ex.dpi_data_gen.impl.values.ActivityValues;
import org.personalsmartspace.ex.dpi_data_gen.impl.values.StatusValues;

/**
 * @author Elizabeth
 * 
 */
public class Alice extends ConfigurablePSS {

    @Override
    protected String getActivity() {
        return ActivityValues.RUNNING;
    }

    @Override
    protected String getAge() {
        return "38";
    }

    @Override
    protected String getBankAccount() {
        return "GB57 0302 0578 7798 456";
    }

    @Override
    protected String getCreditCardCRC() {
        return "457";
    }

    @Override
    protected String getCreditCardExpiryDate() {
        return "12/5/2015";
    }

    @Override
    protected String getCreditCardName() {
        return "Alice Tate";
    }

    @Override
    protected String getCreditCardNumber() {
        return "5790 7800 4790 2467";
    }

    @Override
    protected String getCreditCardStartDate() {
        return "6/26/2010";
    }

    @Override
    protected String getEducation() {
        return "doctor of medicine";
    }

    @Override
    protected String getEmail() {
        return "alice.tate@gmail.com";
    }

    @Override
    protected String getHomeAddress() {
        return "45 Saint Martin's Pl WC2H 0HE London";
    }

    @Override
    protected String getIncome() {
        return "5000 GBP";
    }

    @Override
    protected String getMedicalData() {
        return "Alice Tate {female;diabetes-typeA;RH+;AB}";
    }

    @Override
    protected String getName() {
        return "Alice Tate";
    }

    @Override
    protected String getPhone() {
        return "0044 14 575 779";
    }

    @Override
    protected String getProfession() {
        return "physician";
    }

    @Override
    protected String getSex() {
        return "female";
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
        return "The Royal London Hospital, Whitechapel Road, Whitechapel, London, E1 1BB";
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

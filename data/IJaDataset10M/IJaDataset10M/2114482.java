package com.francetelecom.rd.maps.semeuse.t31d2_slachecking.serializable_objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * ---------------------------------------------------------
 * 
 * @Software_Name : SLO Monitoring
 * @Version : 1.0.0
 * 
 * @Copyright � 2009 France Telecom
 * @License: This software is distributed under the GNU Lesser General Public
 *           License (Version 2.1) as published by the Free Software Foundation,
 *           the text of which is available at
 *           http://www.gnu.org/licenses/lgpl-2.1.html or see the "license.txt"
 *           file for more details.
 * 
 * @--------------------------------------------------------
 * 
 * @Created : 02/2009
 * @Author(s) : Antonin CHAZALET
 * @Contact: antonin.chazalet@gmail.com
 * 
 * @Description :
 * 
 * @--------------------------------------------------------
 */
@SuppressWarnings("serial")
public class CarteBlanche_ServiceLevelAgreement_DTO implements Serializable {

    private String serviceLevelAgreementIdentifier;

    private Date beginingDate;

    private Date endingDate;

    private ArrayList<CarteBlanche_ServiceLevelObjective_DTO> carteBlanche_ServiceLevelObjective_DTO_list;

    public CarteBlanche_ServiceLevelAgreement_DTO(final String serviceLevelAgreementIdentifier_, final Date beginingDate_, final Date endingDate_, final ArrayList<CarteBlanche_ServiceLevelObjective_DTO> carteBlanche_ServiceLevelObjective_DTO_list_) {
        serviceLevelAgreementIdentifier = serviceLevelAgreementIdentifier_;
        beginingDate = beginingDate_;
        endingDate = endingDate_;
        carteBlanche_ServiceLevelObjective_DTO_list = carteBlanche_ServiceLevelObjective_DTO_list_;
    }

    public final String getServiceLevelAgreementIdentifier() {
        return serviceLevelAgreementIdentifier;
    }

    public final Date getBeginingDate() {
        return beginingDate;
    }

    public final Date getEndingDate() {
        return endingDate;
    }

    public final ArrayList<CarteBlanche_ServiceLevelObjective_DTO> getCarteBlanche_ServiceLevelObjective_DTO_list() {
        return carteBlanche_ServiceLevelObjective_DTO_list;
    }

    @Override
    public String toString() {
        String result = this.getClass().getSimpleName() + ": [\n";
        result = result + "    serviceLevelAgreementIdentifier: " + getServiceLevelAgreementIdentifier() + ",\n";
        result = result + "    beginingDate: " + getBeginingDate() + ",\n";
        result = result + "    endingDate: " + getEndingDate() + ",\n";
        result = result + "    carteBlanche_ServiceLevelObjective_DTO_list:\n " + getCarteBlanche_ServiceLevelObjective_DTO_list() + "]\n";
        return result;
    }
}

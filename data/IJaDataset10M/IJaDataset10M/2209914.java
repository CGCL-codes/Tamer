package com.francetelecom.rd.maps.mep.mse.carteblanche.supervision.bouchon_sla_generator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.francetelecom.rd.maps.semeuse.t31d2_slachecking.enumeration.CarteBlanche_JOnASState;
import com.francetelecom.rd.maps.semeuse.t31d2_slachecking.enumeration.CarteBlanche_SLCheckableProperties;
import com.francetelecom.rd.maps.semeuse.t31d2_slachecking.enumeration.CarteBlanche_ServiceLevelObjective_Operator;
import com.francetelecom.rd.maps.semeuse.t31d2_slachecking.serializable_objects.CarteBlanche_ServiceLevelAgreement_DTO;
import com.francetelecom.rd.maps.semeuse.t31d2_slachecking.serializable_objects.CarteBlanche_ServiceLevelObjective_DTO;

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
public class CarteBlanche_ServiceLevelAgreement_DTO_Generator {

    public CarteBlanche_ServiceLevelAgreement_DTO_Generator() {
    }

    /**
	 * @return a list of ServiceLevelAgreement_DTO.
	 * 
	 *         As an important side note, the beginning date of the SLA
	 *         corresponds to the arrival of the SLA from Dragon to this SLC
	 *         prototype. That is why we use new Date();.
	 */
    public List<CarteBlanche_ServiceLevelAgreement_DTO> createSLA_DTO_list_forSemeuse() {
        List<CarteBlanche_ServiceLevelAgreement_DTO> result = null;
        result = new ArrayList<CarteBlanche_ServiceLevelAgreement_DTO>();
        CarteBlanche_ServiceLevelAgreement_DTO carteBlanche_ServiceLevelAgreement_DTO_1 = create_CarteBlanche_ServiceLevelAgreement_DTO_1_forSemeuse();
        CarteBlanche_ServiceLevelAgreement_DTO carteBlanche_ServiceLevelAgreement_DTO_2 = create_CarteBlanche_ServiceLevelAgreement_DTO_2_forSemeuse();
        result.add(carteBlanche_ServiceLevelAgreement_DTO_1);
        result.add(carteBlanche_ServiceLevelAgreement_DTO_2);
        return result;
    }

    private CarteBlanche_ServiceLevelAgreement_DTO create_CarteBlanche_ServiceLevelAgreement_DTO_1_forSemeuse() {
        CarteBlanche_ServiceLevelAgreement_DTO result = null;
        String identifierOfTheSLA = null;
        identifierOfTheSLA = "sla_identifi_1";
        Date beginningDateOfTheSLA = null;
        beginningDateOfTheSLA = new Date();
        Date expirationDateOfTheSLA = null;
        expirationDateOfTheSLA = new Date(new Date().getTime() + new Long(527040000).longValue());
        ArrayList<CarteBlanche_ServiceLevelObjective_DTO> carteBlanche_ServiceLevelObjective_DTO = new ArrayList<CarteBlanche_ServiceLevelObjective_DTO>();
        CarteBlanche_SLCheckableProperties sLO_id_1 = CarteBlanche_SLCheckableProperties.JVM_ALLTHREADSCOUNT;
        CarteBlanche_ServiceLevelObjective_Operator sLO_operator_1 = CarteBlanche_ServiceLevelObjective_Operator.LessThanOrEqualTo;
        Long sLO_threshold_1 = new Long(86);
        CarteBlanche_ServiceLevelObjective_DTO carteBlanche_ServiceLevelObjective_DTO_temp_1 = new CarteBlanche_ServiceLevelObjective_DTO(sLO_id_1, null, null, sLO_operator_1, sLO_threshold_1, Long.class.getCanonicalName());
        carteBlanche_ServiceLevelObjective_DTO.add(carteBlanche_ServiceLevelObjective_DTO_temp_1);
        CarteBlanche_SLCheckableProperties sLO_id_2 = CarteBlanche_SLCheckableProperties.INFRASTRUCTURE_STATE;
        CarteBlanche_ServiceLevelObjective_Operator sLO_operator_2 = CarteBlanche_ServiceLevelObjective_Operator.EqualTo;
        CarteBlanche_JOnASState sLO_threshold_2 = CarteBlanche_JOnASState.RUNNING;
        CarteBlanche_ServiceLevelObjective_DTO carteBlanche_ServiceLevelObjective_DTO_temp_2 = new CarteBlanche_ServiceLevelObjective_DTO(sLO_id_2, null, null, sLO_operator_2, sLO_threshold_2, CarteBlanche_JOnASState.class.getCanonicalName());
        carteBlanche_ServiceLevelObjective_DTO.add(carteBlanche_ServiceLevelObjective_DTO_temp_2);
        CarteBlanche_SLCheckableProperties sLO_id_3 = CarteBlanche_SLCheckableProperties.INFRASTRUCTURE_DEPLOYMENTPLANSCOUNT;
        CarteBlanche_ServiceLevelObjective_Operator sLO_operator_3 = CarteBlanche_ServiceLevelObjective_Operator.LessThanOrEqualTo;
        Long sLO_threshold_3 = new Long(75);
        CarteBlanche_ServiceLevelObjective_DTO carteBlanche_ServiceLevelObjective_DTO_temp_3 = new CarteBlanche_ServiceLevelObjective_DTO(sLO_id_3, null, null, sLO_operator_3, sLO_threshold_3, Long.class.getCanonicalName());
        carteBlanche_ServiceLevelObjective_DTO.add(carteBlanche_ServiceLevelObjective_DTO_temp_3);
        result = new CarteBlanche_ServiceLevelAgreement_DTO(identifierOfTheSLA, beginningDateOfTheSLA, expirationDateOfTheSLA, carteBlanche_ServiceLevelObjective_DTO);
        return result;
    }

    private CarteBlanche_ServiceLevelAgreement_DTO create_CarteBlanche_ServiceLevelAgreement_DTO_2_forSemeuse() {
        CarteBlanche_ServiceLevelAgreement_DTO result = null;
        String identifierOfTheSLA = null;
        identifierOfTheSLA = "sla_identifi_2";
        Date beginningDateOfTheSLA = null;
        beginningDateOfTheSLA = new Date();
        Date expirationDateOfTheSLA = null;
        expirationDateOfTheSLA = new Date(new Date().getTime() + new Long(527040000).longValue());
        ArrayList<CarteBlanche_ServiceLevelObjective_DTO> carteBlanche_ServiceLevelObjective_DTO_list = new ArrayList<CarteBlanche_ServiceLevelObjective_DTO>();
        CarteBlanche_SLCheckableProperties sLO_id_1 = CarteBlanche_SLCheckableProperties.SERVICE_ONSERVICESESSIONSERVLETMAXTIME;
        CarteBlanche_ServiceLevelObjective_Operator sLO_operator_1 = CarteBlanche_ServiceLevelObjective_Operator.LessThanOrEqualTo;
        Long sLO_threshold_1 = new Long(5000);
        CarteBlanche_ServiceLevelObjective_DTO sLO_DTO_temp_1 = new CarteBlanche_ServiceLevelObjective_DTO(sLO_id_1, null, null, sLO_operator_1, sLO_threshold_1, Long.class.getCanonicalName());
        carteBlanche_ServiceLevelObjective_DTO_list.add(sLO_DTO_temp_1);
        result = new CarteBlanche_ServiceLevelAgreement_DTO(identifierOfTheSLA, beginningDateOfTheSLA, expirationDateOfTheSLA, carteBlanche_ServiceLevelObjective_DTO_list);
        return result;
    }
}

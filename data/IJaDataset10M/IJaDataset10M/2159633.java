package com.yoka.limreporter.client.db.generator;

import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.yoka.limreporter.client.LimReporter;
import com.yoka.limreporter.client.db.DataStorage;
import com.yoka.limreporter.client.db.DataStorageException;
import com.yoka.limreporter.client.domain.Appointment;
import com.yoka.limreporter.client.domain.AppointmentTreatment;
import com.yoka.limreporter.client.domain.BodyPart;
import com.yoka.limreporter.client.domain.Company;
import com.yoka.limreporter.client.domain.Doctor;
import com.yoka.limreporter.client.domain.MedicalReferral;
import com.yoka.limreporter.client.domain.Patient;
import com.yoka.limreporter.client.domain.Therapist;
import com.yoka.limreporter.client.domain.Treatment;

/**
 *	Generation data to the database
 */
public class GenerateDatabase {

    private static GenerateType generateType;

    DataStorage dataStorage;

    public GenerateDatabase() {
        generateType = new GenerateType();
        dataStorage = LimReporter.getDataStorage();
    }

    /**
 * Put to the table Patient single entry
 * Return patient_id
 */
    private int generatePatientLine() {
        Patient patient = new Patient();
        String name, surname;
        int id = 0;
        name = generateType.generateName();
        surname = generateType.generateSurname();
        patient.setAddress_city(generateType.generateCity());
        patient.setAddress_code(generateType.generateAddressCode());
        patient.setAddress_street(generateType.generateStreet());
        patient.setEmail_address(generateType.generateEmailAddress(name, surname));
        patient.setInternal_pateint_id(Integer.toString(++id));
        patient.setName(name);
        patient.setSurname(surname);
        patient.setPhone_number(generateType.generatePhoneNumber());
        try {
            dataStorage.addOrUpdatePatient(patient);
        } catch (DataStorageException e) {
            Window.alert("Excepiton" + e);
            System.out.println("Warrning: " + e);
        }
        return patient.getId();
    }

    /**
		 * Takes as parameters : patient_id, patient_company_id, doctor_id, therapist_id
		 * Put to the table Medical_referral single entry
		 * Return medical_referral_id
		 */
    private int generateMedicalReferralLine(int patientId, int patientCompanyId, int doctorId, int therapistId, int bodyPartId) {
        MedicalReferral mr = new MedicalReferral();
        mr.setDoctor_id(doctorId);
        mr.setFinished(Random.nextInt(2));
        mr.setCompany_id(patientCompanyId);
        mr.setPatient_id(patientId);
        mr.setTherapist_id(therapistId);
        mr.setVisits_number(Random.nextInt(9) + 1);
        mr.setBody_part_id(bodyPartId);
        try {
            dataStorage.addOrUpdateMedicalReferral(mr);
        } catch (DataStorageException e) {
            System.out.println("Warrning: " + e);
        }
        return mr.getId();
    }

    /**
	 * Takes as parameter medical_referral_id
	 * Put to the table Appointment single entry
	 * Return appointment_id
	 */
    private int generateAppointmentLine(int medicalReferralId) {
        Appointment appointment = new Appointment();
        appointment.setDate(generateType.generateDate());
        appointment.setMedicalReferralId(medicalReferralId);
        appointment.setTherapist_id(0);
        try {
            dataStorage.addOrUpdateAppointment(appointment);
        } catch (DataStorageException e) {
            System.out.println("Warrning: " + e);
        }
        return appointment.getId();
    }

    /**
	* Takes as parameters appointment_id, treatment_id 
	* Put to the table Appointment single entry
	* Return appointment_id
*/
    private int generateAppointmentTreatmentLine(int appointmentId, int treatmentId) {
        AppointmentTreatment at = new AppointmentTreatment();
        at.setAppointment_id(appointmentId);
        at.setTreatment_id(treatmentId);
        try {
            dataStorage.addOrUpdateAppointmentTreatment(at);
        } catch (DataStorageException e) {
            System.out.println("Warrning: " + e);
        }
        return at.getId();
    }

    /** 
	* Put to the table Treatment all entries
	* Return number of entries
	*/
    private int generateTreatmentAllLine() {
        String[] treatments;
        treatments = generateType.getAllTreatments();
        for (String s : treatments) {
            Treatment treatment = new Treatment();
            treatment.setTreatment(s);
            try {
                dataStorage.addOrUpdateTreatment(treatment);
            } catch (DataStorageException e) {
                System.out.println("Warrning: " + e);
            }
        }
        return treatments.length;
    }

    /** 
	* Put to the table Body_part all entries
	* Return number of entries
	*/
    private int generateBodyPartAllLine() {
        String[] bodyParts;
        bodyParts = generateType.getAllBodyParts();
        for (String s : bodyParts) {
            BodyPart bodyPart = new BodyPart();
            bodyPart.setBody_part(s);
            try {
                dataStorage.addOrUpdateBodyPart(bodyPart);
            } catch (DataStorageException e) {
                System.out.println("Warning: " + e);
            }
        }
        return bodyParts.length;
    }

    /** 
	* Put to the table Patient_company all entries
	* Return number of entries
	*/
    private int generatePatientCompanyAllLine() {
        String[] patientCompanies;
        patientCompanies = generateType.getAllCompanies();
        for (String s : patientCompanies) {
            Company pc = new Company();
            pc.setCompany_name(s);
            try {
                dataStorage.addOrUpdateCompany(pc);
            } catch (DataStorageException e) {
                System.out.println("Warning: " + e);
            }
        }
        return patientCompanies.length;
    }

    /** 
	* Takes as parameters amount therapists 
	* Put to the table Treatment all entries
	* Return number of entries
	*/
    private int generateTherapistAllLine(int amountTherapists) {
        Therapist therapist;
        for (int i = 0; i <= amountTherapists; i++) {
            therapist = new Therapist();
            therapist.setName(generateType.generateName());
            therapist.setSurname(generateType.generateSurname());
            try {
                dataStorage.addOrUpdateTherapist(therapist);
            } catch (DataStorageException e) {
                System.out.println("Warning: " + e);
            }
        }
        return amountTherapists;
    }

    /** 
	* Takes as parameters amount doctors
	* Put to the table Doctor all entries
	* Return number of entries
	*/
    private int generateDoctorAllLine(int amountDoctors) {
        Doctor doctor;
        for (int i = 1; i <= amountDoctors; i++) {
            doctor = new Doctor();
            doctor.setName(generateType.generateName());
            doctor.setSurname(generateType.generateSurname());
            try {
                dataStorage.addOrUpdateDoctor(doctor);
            } catch (DataStorageException e) {
                System.out.println("Warning: " + e);
            }
        }
        return amountDoctors;
    }

    /**
	 * Generated fake data to the database
	 */
    public void generateFakeData(int amountPatients) {
        final int maxMedicalReferral = 5;
        final int maxAppointment = 6;
        final int maxTreatment = 3;
        int amountCompanies = generatePatientCompanyAllLine();
        int amountTreatments = generateTreatmentAllLine();
        int amountBodyParts = generateBodyPartAllLine();
        int amountDoctors = generateDoctorAllLine(10);
        int amountTherapists = generateTherapistAllLine(20);
        for (int i = 1; i <= amountPatients; i++) {
            int patientId = generatePatientLine();
            for (int j = 1; j <= Random.nextInt(maxMedicalReferral) + 1; j++) {
                int medicalReferralId = generateMedicalReferralLine(patientId, Random.nextInt(amountCompanies) + 1, Random.nextInt(amountDoctors) + 1, Random.nextInt(amountTherapists) + 1, Random.nextInt(amountBodyParts) + 1);
                for (int l = 1; l <= Random.nextInt(maxAppointment) + 1; l++) {
                    int appointmentId = generateAppointmentLine(medicalReferralId);
                    for (int n = 1; n <= Random.nextInt(maxTreatment) + 1; n++) {
                        generateAppointmentTreatmentLine(appointmentId, Random.nextInt(amountTreatments) + 1);
                    }
                }
            }
        }
    }
}

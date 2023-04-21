package de.psisystems.dmachinery;

public class Contract {

    Applicant applicant1;

    Applicant applicant2;

    public Contract() {
        super();
    }

    public Contract(Applicant applicant1, Applicant applicant2) {
        super();
        this.applicant1 = applicant1;
        this.applicant2 = applicant2;
    }

    public Applicant getApplicant1() {
        return applicant1;
    }

    public void setApplicant1(Applicant applicant1) {
        this.applicant1 = applicant1;
    }

    public Applicant getApplicant2() {
        return applicant2;
    }

    public void setApplicant2(Applicant applicant2) {
        this.applicant2 = applicant2;
    }
}

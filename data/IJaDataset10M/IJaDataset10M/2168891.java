package chsec.gui.pers_ed;

import chsec.domain.Parishioner;
import chsec.domain.Person;

public class PersonHousehold {

    public Parishioner person;

    public String hhNm;

    public PersonHousehold() {
    }

    public PersonHousehold(Parishioner person, String hhNm) {
        this.person = person;
        this.hhNm = hhNm;
    }

    public boolean equals(Object oo) {
        if (oo instanceof PersonHousehold) {
            PersonHousehold phh = (PersonHousehold) oo;
            return person != null && phh.person != null && hhNm != null && phh.hhNm != null && person.equals(phh.person) && hhNm.equals(phh.hhNm);
        }
        return false;
    }
}

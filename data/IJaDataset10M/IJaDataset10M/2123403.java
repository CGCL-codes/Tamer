package com.advancedpwr.record.examples.generated;

import com.advancedpwr.record.examples.People;
import com.advancedpwr.record.examples.Person;
import java.util.Date;
import com.advancedpwr.record.mock.MockFactory;

public class PeopleFactory extends MockFactory {

    protected People people;

    public People buildPeople() throws Exception {
        if (people != null) {
            return people;
        }
        people = createStrictMock(People.class);
        expect(people.getPersons()).andReturn(buildPersonArray_1_1());
        replay(people);
        return people;
    }

    protected Person[] personArray_1_1;

    protected Person[] buildPersonArray_1_1() throws Exception {
        if (personArray_1_1 != null) {
            return personArray_1_1;
        }
        personArray_1_1 = new Person[2];
        personArray_1_1[0] = buildPerson_2_2();
        personArray_1_1[1] = buildPerson_3_2();
        return personArray_1_1;
    }

    protected Person person_2_2;

    protected Person buildPerson_2_2() throws Exception {
        if (person_2_2 != null) {
            return person_2_2;
        }
        person_2_2 = createStrictMock(Person.class);
        person_2_2.setAnniversary(buildDate_4_1());
        replay(person_2_2);
        return person_2_2;
    }

    protected Date date_4_1;

    protected Date buildDate_4_1() {
        date_4_1 = new Date(975906000000l);
        return date_4_1;
    }

    protected Person person_3_2;

    protected Person buildPerson_3_2() {
        if (person_3_2 != null) {
            return person_3_2;
        }
        person_3_2 = createStrictMock(Person.class);
        replay(person_3_2);
        return person_3_2;
    }
}

package metso.dal.test.dao;

import java.io.Serializable;
import metso.dal.Dto;
import metso.dal.DtoFactory;
import metso.dal.test.business.Person;
import metso.dal.test.business.Structure;

public class TestDtoFactory implements DtoFactory {

    @SuppressWarnings("unchecked")
    public <T extends Serializable> Dto<T> getDalc(Class<T> clazz) {
        Dto<T> dalc = null;
        if (clazz == Person.class) {
            dalc = (Dto<T>) new TestPersonDto();
        } else if (clazz == Structure.class) {
            dalc = (Dto<T>) new TestStructureDto();
        }
        return dalc;
    }
}

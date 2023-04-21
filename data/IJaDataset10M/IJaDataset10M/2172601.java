package org.equanda.test.xejb;

import org.equanda.client.EquandaException;
import org.equanda.client.ExceptionCodes;
import org.equanda.persistence.EquandaConstraintViolation;
import org.equanda.persistence.SelectorsState;
import org.equanda.test.dm.client.Car;
import org.equanda.test.dm.client.EquandaGlobal;
import org.equanda.test.dm.client.UniqueFields;
import org.equanda.test.dm.client.UniqueFieldsSelectorEJB;
import java.util.List;

/**
 * JUnit to test the unique constraint
 *
 * @author NetRom team
 * @author <a href="mailto:joachim@progs.be">Joachim Van der Auwera</a>
 */
public class UniqueFieldsTest extends TestAdapter {

    public void testUniqueFields() throws Exception {
        UniqueFields obj1 = UniqueFields.equandaCreate();
        obj1.setUniqueImmutableInt(1);
        obj1.equandaUpdate();
        UniqueFields obj2 = UniqueFields.equandaCreate();
        obj2.setUniqueImmutableInt(2);
        obj2.equandaUpdate();
        obj1.setUniqueInt(1);
        obj1.equandaUpdate();
        try {
            obj2.setUniqueInt(1);
            obj2.equandaUpdate();
            fail("unique constraint ignored");
        } catch (EquandaConstraintViolation ucv) {
        }
        obj1.removeEntityBean();
        obj2.removeEntityBean();
    }

    public void testUniqueFieldsImmutable() throws Exception {
        UniqueFields obj1 = UniqueFields.equandaCreate();
        obj1.setUniqueImmutableInt(1);
        obj1.equandaUpdate();
        try {
            UniqueFields obj2 = UniqueFields.equandaCreate();
            obj2.setUniqueImmutableInt(1);
            obj2.equandaUpdate();
            obj2.removeEntityBean();
            fail("unique constraint ignored");
        } catch (Exception ucv) {
        }
        obj1.removeEntityBean();
    }

    public void testUniqueSelectCreate() throws Exception {
        UniqueFieldsSelectorEJB selector = EquandaGlobal.getUniqueFieldsSelector();
        UniqueFields obj = UniqueFields.equandaCreate();
        obj.setUniqueImmutableInt(1);
        obj.equandaUpdate();
        UniqueFields ufv = selector.selectImmutable(1);
        assertNotNull(ufv);
        ufv = selector.selectImmutable(2);
        assertNull(ufv);
        ufv = UniqueFields.equandaCreate();
        ufv.setUniqueInt(1);
        ufv.setUniqueImmutableInt(2);
        ufv.equandaUpdate();
        selector.removeEntityBean(ufv.getId());
        ufv = UniqueFields.equandaCreate();
        ufv.setUniqueInt(1);
        try {
            ufv.setUniqueImmutableInt(1);
            ufv.equandaUpdate();
            selector.removeEntityBean(ufv.getId());
            fail("unique test failed");
        } catch (EquandaException upe) {
        }
        obj.removeEntityBean();
        selector.remove();
    }

    public void testUniqueFieldsChanged() throws Exception {
        UniqueFields obj = UniqueFields.equandaCreate();
        obj.setUniqueImmutableInt(1);
        obj.setUniqueInt(1);
        obj.equandaUpdate();
        obj.setUniqueInt(2);
        obj.setUniqueInt(1);
        obj.equandaUpdate();
        obj.removeEntityBean();
    }

    public void testUniqueAndFilters() throws Exception {
        Car car1 = Car.equandaCreate();
        car1.setBrand("CarBrand 1");
        UniqueFields uf1 = UniqueFields.equandaCreate();
        uf1.setUniqueImmutableInt(1);
        uf1.setLinkedCar(car1);
        uf1.equandaUpdate();
        Car car2 = Car.equandaCreate();
        car2.setBrand("CarBrand 2");
        UniqueFields uf2 = UniqueFields.equandaCreate();
        uf2.setUniqueImmutableInt(2);
        uf2.setLinkedCar(car2);
        uf2.equandaUpdate();
        SelectorsState.setFilter("Car", "CarBrand 1");
        List<UniqueFields> list = UniqueFields.selectEverything();
        assertEquals(1, list.size());
        try {
            UniqueFields uf3 = UniqueFields.equandaCreate();
            uf3.setUniqueImmutableInt(2);
            uf3.equandaUpdate();
            fail("uniqueness should not take filter into account");
        } catch (EquandaConstraintViolation ecv) {
            assertEquals(ExceptionCodes.ECV_FIELD_IS_UNIQUE, ecv.getExceptionCode());
        }
        car1 = uf1.getLinkedCar();
        uf1.removeEntityBean();
        car2 = uf2.getLinkedCar();
        uf2.removeEntityBean();
        car1.removeEntityBean();
        car2.removeEntityBean();
    }

    public void testUniqueFor() throws Exception {
        Car car1 = Car.equandaCreate();
        car1.setBrand("CarBrand 1");
        car1.equandaUpdate();
        Car car2 = Car.equandaCreate();
        car2.setBrand("CarBrand 2");
        car2.equandaUpdate();
        UniqueFields uf1 = UniqueFields.equandaCreate();
        uf1.setUniqueImmutableInt(1);
        uf1.setLinkedCar(car1);
        uf1.setUniqueForCar("blue");
        uf1.equandaUpdate();
        UniqueFields uf2 = UniqueFields.equandaCreate();
        uf2.setUniqueImmutableInt(2);
        uf2.setLinkedCar(car2);
        uf2.setUniqueForCar("blue");
        uf2.equandaUpdate();
        UniqueFields uf3 = UniqueFields.equandaCreate();
        uf3.setUniqueImmutableInt(3);
        uf3.setLinkedCar(car1);
        uf3.setUniqueForCar("blue");
        try {
            uf3.equandaUpdate();
            fail("unique-for not activated");
        } catch (EquandaConstraintViolation ecv) {
            assertEquals(ExceptionCodes.ECV_FIELD_IS_UNIQUE, ecv.getExceptionCode());
        }
        uf1.removeEntityBean();
        uf2.removeEntityBean();
        car1.removeEntityBean();
        car2.removeEntityBean();
    }

    public void testUniqueForNull() throws Exception {
        UniqueFields uf1 = UniqueFields.equandaCreate();
        uf1.setUniqueImmutableInt(1);
        uf1.setUniqueForCar("blue");
        uf1.equandaUpdate();
        UniqueFields uf2 = UniqueFields.equandaCreate();
        uf2.setUniqueImmutableInt(2);
        uf2.setUniqueForCar("blue");
        try {
            uf2.equandaUpdate();
            fail("unique-for not activated");
        } catch (EquandaConstraintViolation ecv) {
            assertEquals(ExceptionCodes.ECV_FIELD_IS_UNIQUE, ecv.getExceptionCode());
        }
        uf1.removeEntityBean();
    }
}

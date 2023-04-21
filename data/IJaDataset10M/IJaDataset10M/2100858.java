package uturismu.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;
import uturismu.ServiceFactory;
import uturismu.dto.Accommodation;
import uturismu.dto.enumtype.AccommodationType;
import uturismu.service.backup.AccommodationService;

/**
 * @author "LagrecaSpaccarotella" team.
 * 
 */
public class AccommodationTest {

    private static AccommodationService service;

    @BeforeClass
    public static void init() {
        service = ServiceFactory.getAccommodationService();
    }

    @Test
    public void checkSave() {
        String vatNumber = "0123456";
        String name = "Springfield Inn.";
        Accommodation accommodation1 = new Accommodation();
        accommodation1.setVatNumber(vatNumber);
        accommodation1.setName(name);
        accommodation1.setType(AccommodationType.HOTEL);
        service.save(accommodation1);
        Accommodation accommodation2 = service.findByVatNumber(vatNumber);
        assertThat(accommodation2.getId(), is(equalTo(accommodation1.getId())));
        assertThat(service.rowCount(), is(equalTo(1L)));
    }

    @Test
    public void checkDelete() {
        Long rowCount = service.rowCount();
        Accommodation accommodation = new Accommodation();
        accommodation.setVatNumber("6610");
        accommodation.setName("Nuclear Power B&B");
        accommodation.setType(AccommodationType.BED_AND_BREAKFAST);
        Long id = service.save(accommodation);
        accommodation = service.findById(id);
        service.delete(accommodation);
        assertThat(service.rowCount(), is(equalTo(rowCount)));
    }

    @Test
    public void checkUpdate() {
        Accommodation accommodation = new Accommodation();
        accommodation.setVatNumber(":-)");
        accommodation.setName("Shelbyville Youth");
        accommodation.setType(AccommodationType.HOSTEL);
        service.save(accommodation);
        String newName = "Springfield Youth";
        accommodation.setName(newName);
        service.update(accommodation);
        accommodation = service.findByVatNumber(":-)");
        assertThat(accommodation.getName(), is(equalTo(newName)));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void checkUniqueConstraintViolation() {
        Accommodation a1 = new Accommodation();
        a1.setVatNumber("007");
        a1.setName("Moe's");
        service.save(a1);
        Accommodation a2 = new Accommodation();
        a2.setVatNumber("007");
        a1.setName("Homer Motel");
        service.save(a2);
    }
}

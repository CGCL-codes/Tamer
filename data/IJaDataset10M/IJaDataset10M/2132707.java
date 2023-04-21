package net.sf.pachamama.service;

import java.util.List;
import net.sf.hattori.PopulationManager;
import net.sf.hattori.annotations.ObjectPopulation;
import net.sf.hattori.repository.PersistentObject;
import net.sf.hattori.repository.PersistentObjectDTO;
import net.sf.pachamama.data.util.EntityManagerUtil;
import org.apache.commons.lang.Validate;

public class GenericService {

    private EntityManagerUtil dao;

    public GenericService() {
        dao = EntityManagerUtil.getInstance("pachamama", null);
    }

    public PersistentObjectDTO createPersistentObjectDTO(Class dtoClass) {
        Validate.isTrue(PersistentObjectDTO.class.isAssignableFrom(dtoClass));
        try {
            return (PersistentObjectDTO) dtoClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public PersistentObjectDTO getByName(Class dtoClass, String propertyName, Object propertyValue) {
        Class domainClass = ((ObjectPopulation) dtoClass.getAnnotation(ObjectPopulation.class)).domainObjectClass();
        try {
            dao.beginTransaction();
            PersistentObject object = (PersistentObject) dao.findByPropertyValue(domainClass, propertyName, propertyValue);
            dao.commit();
            return (PersistentObjectDTO) PopulationManager.getInstance().populateDTO(object, dtoClass);
        } catch (Exception e) {
            dao.rollback();
            throw new RuntimeException(e);
        }
    }

    public List list(Class dtoClass) {
        Class domainClass = ((ObjectPopulation) dtoClass.getAnnotation(ObjectPopulation.class)).domainObjectClass();
        try {
            dao.beginTransaction();
            List objectList = dao.list(domainClass);
            dao.commit();
            return PopulationManager.getInstance().populateDTOList(objectList, dtoClass);
        } catch (Exception e) {
            dao.rollback();
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the DTO from the repository by it's persistence id and returns it. A DTO is passed to establish id and
     * class.
     * 
     * @param DTO
     *                PersistentObjectDTO with the DTO to retrieve.
     * @return PersistentObjectDTO with the retrieved DTO.
     */
    public PersistentObjectDTO refresh(PersistentObjectDTO DTO) {
        Validate.isTrue(!(DTO.getId() == null));
        Class domainClass = ((ObjectPopulation) DTO.getClass().getAnnotation(ObjectPopulation.class)).domainObjectClass();
        Validate.isTrue(PersistentObject.class.isAssignableFrom(domainClass));
        dao.beginTransaction();
        PersistentObject object = (PersistentObject) dao.findByPropertyValue(domainClass, "id", DTO.getId());
        dao.commit();
        return (PersistentObjectDTO) PopulationManager.getInstance().populateDTO(object, DTO.getClass());
    }

    public void remove(PersistentObjectDTO dto) {
        Class domainClass = ((ObjectPopulation) dto.getClass().getAnnotation(ObjectPopulation.class)).domainObjectClass();
        try {
            dao.beginTransaction();
            PersistentObject object = PopulationManager.getInstance().populateDomainObject(dto);
            dao.remove(object);
            dao.commit();
        } catch (Exception e) {
            dao.rollback();
            throw new RuntimeException(e);
        }
    }

    public void save(PersistentObjectDTO dto) {
        Class domainClass = ((ObjectPopulation) dto.getClass().getAnnotation(ObjectPopulation.class)).domainObjectClass();
        try {
            dao.beginTransaction();
            PersistentObject object = PopulationManager.getInstance().populateDomainObject(dto);
            dao.persist(object);
            dao.commit();
        } catch (Exception e) {
            e.printStackTrace();
            dao.rollback();
            throw new RuntimeException(e);
        }
    }
}

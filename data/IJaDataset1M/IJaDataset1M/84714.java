package de.campussource.cse.core;

import de.campussource.cse.core.pdm.Entity;
import javax.ejb.Local;

/**
 *
 * @author pete
 */
@Local
public interface EntityManager {

    public int exists(String system, String systemId);

    public int createAccount(de.campussource.cse.core.cdm.AccountType account);

    public int createCourse(de.campussource.cse.core.cdm.CourseType course);

    public int createCategory(de.campussource.cse.core.cdm.CategoryType category);

    public de.campussource.cse.core.cdm.AccountType getAccountContext(int entityId, String system);

    public de.campussource.cse.core.cdm.CourseType getCourseContext(int entityId, String system);

    public de.campussource.cse.core.cdm.CategoryType getCategoryContext(int entityId, String system);

    public int createProxy(String type);

    public boolean updateAttributes(de.campussource.cse.core.cdm.EntityType entity);

    public void deleteEntity(int entityId);

    public Entity getEntity(int entityId);
}

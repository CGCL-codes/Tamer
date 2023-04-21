package org.openuss.lecture;

import java.util.Collection;
import java.util.List;
import org.openuss.foundation.AbstractMockDao;
import org.openuss.security.Membership;

public class UniversityDaoMock extends AbstractMockDao<University> implements UniversityDao {

    public University create(boolean enabled, Membership membership, String name, String ownerName, String shortcut, UniversityType universityType) {
        return null;
    }

    public Object create(int transform, boolean enabled, Membership membership, String name, String ownerName, String shortcut, UniversityType universityType) {
        return null;
    }

    public Object create(int transform, UniversityType universityType, String shortcut, String name, String description, String ownerName, String address, String postcode, String city, String country, String telephone, String telefax, String website, String email, String locale, String theme, Long imageId, boolean enabled) {
        return null;
    }

    public University create(UniversityType universityType, String shortcut, String name, String description, String ownerName, String address, String postcode, String city, String country, String telephone, String telefax, String website, String email, String locale, String theme, Long imageId, boolean enabled) {
        return null;
    }

    public List findByEnabled(boolean enabled) {
        return null;
    }

    public List findByEnabled(int transform, boolean enabled) {
        return null;
    }

    public List findByEnabled(int transform, String queryString, boolean enabled) {
        return null;
    }

    public List findByEnabled(String queryString, boolean enabled) {
        return null;
    }

    public Object findByShortcut(int transform, String queryString, String shortcut) {
        return null;
    }

    public Object findByShortcut(int transform, String shortcut) {
        return null;
    }

    public University findByShortcut(String queryString, String shortcut) {
        return null;
    }

    public University findByShortcut(String shortcut) {
        return null;
    }

    public List findByTypeAndEnabled(int transform, String queryString, UniversityType universityType, boolean enabled) {
        return null;
    }

    public List findByTypeAndEnabled(int transform, UniversityType universityType, boolean enabled) {
        return null;
    }

    public List findByTypeAndEnabled(String queryString, UniversityType universityType, boolean enabled) {
        return null;
    }

    public List findByTypeAndEnabled(UniversityType universityType, boolean enabled) {
        return null;
    }

    public void toUniversityInfo(University sourceEntity, UniversityInfo targetVO) {
    }

    public UniversityInfo toUniversityInfo(University entity) {
        return null;
    }

    public void toUniversityInfoCollection(Collection entities) {
    }

    public void universityInfoToEntity(UniversityInfo sourceVO, University targetEntity, boolean copyIfNull) {
    }

    public University universityInfoToEntity(UniversityInfo universityInfo) {
        return null;
    }

    public void universityInfoToEntityCollection(Collection instances) {
    }

    public University create(UniversityType universityType, String name, String shortName, String shortcut, String description, String ownerName, String address, String postcode, String city, String country, String telephone, String telefax, String website, String email, String locale, String theme, Long imageId, boolean enabled) {
        return null;
    }

    public Object create(int transform, UniversityType universityType, String name, String shortName, String shortcut, String description, String ownerName, String address, String postcode, String city, String country, String telephone, String telefax, String website, String email, String locale, String theme, Long imageId, boolean enabled) {
        return null;
    }
}

package fi.passiba.services.group;

import fi.passiba.hibernate.PaginationInfo;
import fi.passiba.services.persistance.Adress;
import fi.passiba.services.group.persistance.Groups;
import fi.passiba.services.persistance.Person;
import java.util.List;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author haverinen
 */
public interface IGroupServices {

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Groups> findGroupsByLocation(String country, String city, String grouptype);

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Groups> findAllGroups();

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PaginationInfo findPagingInfoForGroups(int maxResult);

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Groups> findGroupsWithPaging(PaginationInfo page);

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Groups> findGroupsByPersonId(Long id);

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Person> findGroupsPersonsByGroupId(Long id);

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Groups findGroupByGroupId(Long id);

    @Transactional(propagation = Propagation.REQUIRED)
    public void addGroup(Groups group);

    @Transactional(propagation = Propagation.REQUIRED)
    public Groups updateGroup(Groups group);

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteGroup(Groups group);
}

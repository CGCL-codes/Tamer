package cz.cvut.phone.core.fasade;

import cz.cvut.phone.core.data.dao.BasicEntityDAOLocal;
import cz.cvut.phone.core.constants.Constants;
import cz.cvut.phone.core.dto.CompanyDTO;
import cz.cvut.phone.core.dto.DepartmentDTO;
import cz.cvut.phone.core.dto.OperationDTO;
import cz.cvut.phone.core.dto.PersonDTO;
import cz.cvut.phone.core.dto.PhoneNumberDTO;
import cz.cvut.phone.core.dto.RoleDTO;
import cz.cvut.phone.core.dto.SettingsDTO;
import cz.cvut.phone.core.dto.adapter.CompanyDTOAdapter;
import cz.cvut.phone.core.dto.adapter.DepartmentDTOAdapter;
import cz.cvut.phone.core.dto.adapter.OperationDTOAdapter;
import cz.cvut.phone.core.dto.adapter.PersonDTOAdapter;
import cz.cvut.phone.core.dto.adapter.PhoneNumberAdapter;
import cz.cvut.phone.core.dto.adapter.RoleDTOAdapter;
import cz.cvut.phone.core.dto.adapter.SettingsDTOAdapter;
import cz.cvut.phone.core.data.filter.PersonEntityFilter;
import cz.cvut.phone.core.data.filter.PhoneNumberFilter;
import cz.cvut.phone.core.data.entity.CompanyEntity;
import cz.cvut.phone.core.data.entity.DepartmentEntity;
import cz.cvut.phone.core.data.entity.EmployeeEntity;
import cz.cvut.phone.core.data.entity.OperationEntity;
import cz.cvut.phone.core.data.entity.PersonEntity;
import cz.cvut.phone.core.data.entity.PersonPhoneNumberMapEntity;
import cz.cvut.phone.core.data.entity.PersonRoleMapEntity;
import cz.cvut.phone.core.data.entity.PersonSettingEntity;
import cz.cvut.phone.core.data.entity.PhoneNumberEntity;
import cz.cvut.phone.core.data.entity.RoleEntity;
import cz.cvut.phone.core.data.entity.RoleOperationMapEntity;
import cz.cvut.phone.core.data.entity.SettingsEntity;
import cz.cvut.phone.core.exception.PhoneISDeleteException;
import cz.cvut.phone.core.exception.PhoneISDuplicateDataException;
import cz.cvut.phone.core.exception.PhoneISException;
import cz.cvut.phone.core.exception.PhoneISOptimisticLockException;
import cz.cvut.phone.core.util.ISCoreUtils;
import cz.cvut.phone.core.util.SHA;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import org.apache.log4j.Logger;

/**
 *
 * @author Frantisek Hradil
 */
@Stateless
public class AdministrationFacase implements AdministrationFacaseLocal {

    @EJB
    private BasicEntityDAOLocal basicEntityFacade;

    @EJB
    private PersonFasadeLocal personFasade;

    private Logger log = Logger.getLogger(Constants.LOGGER_NAME);

    public CompanyDTO findCompanyDTOByCompanyID(Integer companyID) throws PhoneISException {
        try {
            CompanyEntity ce = (CompanyEntity) basicEntityFacade.find(CompanyEntity.class, companyID);
            CompanyDTO companyDTO = CompanyDTOAdapter.parseCompanyEntity(ce);
            return companyDTO;
        } catch (Exception e) {
            log.debug("Chyba pri vyhledavani firmy. [companyID]:[" + companyID + "]. " + e);
            throw new PhoneISException("Chyba pri vyhledavani firmy. " + e);
        }
    }

    /**
     * Find departments
     * @param companyID
     * @param active
     * @return
     * @throws cz.cvut.phone.core.exception.PhoneISException
     */
    public List<DepartmentDTO> findDepartmentDTOsByCompanyID(Integer companyID, char active) throws PhoneISException {
        try {
            List<DepartmentEntity> depEntities = basicEntityFacade.getDepartmentEntityDAOBean().findByCompanyID(companyID, active);
            List<DepartmentDTO> deps = new ArrayList<DepartmentDTO>();
            for (DepartmentEntity entity : depEntities) {
                DepartmentDTO tmp = DepartmentDTOAdapter.parseDepartmentEntity(entity);
                deps.add(tmp);
            }
            return deps;
        } catch (Exception e) {
            log.debug("Chyba pri vyhledavani oddeleni. [companyID]:[" + companyID + "]. " + e);
            throw new PhoneISException("Chyba pri vyhledavani oddeleni. " + e);
        }
    }

    /**
     * Find roles.
     * @param companyID
     * @return
     * @throws cz.cvut.phone.core.exception.PhoneISException
     */
    public List<RoleDTO> findRoleDTOsByCompanyID(Integer companyID) throws PhoneISException {
        try {
            List<RoleEntity> roles = basicEntityFacade.getRoleEntityDAOBean().findByCompanyID(companyID);
            List<RoleDTO> roleDTOs = new ArrayList<RoleDTO>();
            for (RoleEntity r : roles) {
                RoleDTO tmp = RoleDTOAdapter.parseRoleEntity(r);
                tmp.setCompanyId(companyID);
                roleDTOs.add(tmp);
            }
            return roleDTOs;
        } catch (Exception e) {
            log.debug("Chyba pri vyhledavani roli. [companyID]:[" + companyID + "]. " + e);
            throw new PhoneISException("Chyba pri vyhledavani roli. " + e);
        }
    }

    /**
     * Find settings of company
     * @param companyID
     * @return
     * @throws cz.cvut.phone.core.exception.PhoneISException
     */
    public SettingsDTO findSettingsDTOByCompanyID(Integer companyID) throws PhoneISException {
        try {
            SettingsEntity settingsEntity = basicEntityFacade.getSettingsEntityDAOBean().findByCompanyId(companyID);
            return SettingsDTOAdapter.parseSettingsEntity(settingsEntity);
        } catch (Exception e) {
            log.debug("Chyba pri vyhledavani firemniho nastaveni. [companyID]:[" + companyID + "]. " + e);
            throw new PhoneISException("Chyba pri vyhledavani firemniho nastaveni. " + e);
        }
    }

    /**
     * Find role`s operation
     * @param roleId
     * @return
     * @throws cz.cvut.phone.core.exception.PhoneISException
     */
    public List<OperationDTO> findOperationsByRollId(Integer roleId) throws PhoneISException {
        try {
            List<OperationEntity> ops = basicEntityFacade.getOperationEntityDAOBean().findByRollID(roleId);
            List<OperationDTO> opers = new ArrayList<OperationDTO>();
            for (OperationEntity o : ops) {
                opers.add(OperationDTOAdapter.parseOperationEntity(o));
            }
            return opers;
        } catch (Exception e) {
            log.debug("Chyba pri vyhledavani operaci. [roleId]:[" + roleId + "]. " + e);
            throw new PhoneISException("Chyba pri vyhledavani operaci. " + e);
        }
    }

    /**
     * Find person`s roles
     * @param personId
     * @return
     * @throws cz.cvut.phone.core.exception.PhoneISException
     */
    public List<RoleDTO> findRolesByPersonId(Integer personId) throws PhoneISException {
        try {
            List<RoleEntity> roles = basicEntityFacade.getRoleEntityDAOBean().findByPersonID(personId);
            List<RoleDTO> rolesDTO = new ArrayList<RoleDTO>();
            for (RoleEntity r : roles) {
                rolesDTO.add(RoleDTOAdapter.parseRoleEntity(r));
            }
            return rolesDTO;
        } catch (Exception e) {
            log.debug("Chyba pri vyhledavani roli. [personId]:[" + personId + "]. " + e);
            throw new PhoneISException("Chyba pri vyhledavani roli. " + e);
        }
    }

    /**
     * Find company`s person
     * @param companyId
     * @param filter Data which will be use to filter persons
     * @return
     * @throws cz.cvut.phone.core.exception.PhoneISException
     */
    public List<PersonDTO> findPersonDTOByCompanyId(Integer companyId, PersonEntityFilter filter) throws PhoneISException {
        try {
            List<PersonDTO> personsDTO = new ArrayList<PersonDTO>();
            List<PersonEntity> persons = basicEntityFacade.getPersonEntityDAOBean().findByCompanyIdAndFilter(companyId, filter);
            for (PersonEntity p : persons) {
                personsDTO.add(joinAndCreatePersonDTO(p));
            }
            return personsDTO;
        } catch (Exception e) {
            log.debug("Chyba pri vyhledavani osob. [companyID]:[" + companyId + "]. " + e);
            throw new PhoneISException("Chyba pri vyhledavani osob. " + e);
        }
    }

    /**
     * Check if person is employee
     * @param p PersonEntity
     * @return
     * @throws java.lang.Exception
     */
    private PersonDTO joinAndCreatePersonDTO(PersonEntity p) throws Exception {
        PersonDTO dto = PersonDTOAdapter.parsePersonEntity(p);
        try {
            EmployeeEntity eml = basicEntityFacade.getPersonEntityDAOBean().findEmployeeByPersonId(p.getPersonID());
            dto.setDepartment(DepartmentDTOAdapter.parseDepartmentEntity(eml.getDepartment()));
            dto.setEndEmploy(eml.getEndEmploy());
            dto.setBeginEmploy(eml.getBeginEmploy());
        } catch (NoResultException e) {
            log.debug("Osoba neni zamestnancem. Id = " + p.getPersonID());
        } finally {
            return dto;
        }
    }

    /**
     * Find active company`s employee
     * @param companyID
     * @return
     * @throws cz.cvut.phone.core.exception.PhoneISException
     */
    public List<PersonDTO> findActiveEmployeeByCompanyID(Integer companyID) throws PhoneISException {
        List<PersonDTO> personsDTO = new ArrayList<PersonDTO>();
        try {
            List<PersonEntity> persons = basicEntityFacade.getPersonEntityDAOBean().findActivePersonEmployeeByCompanyId(companyID);
            for (PersonEntity p : persons) {
                personsDTO.add(joinAndCreatePersonDTO(p));
            }
        } catch (Exception e) {
            log.debug("Chyba pri vyhledavani aktivnich zamestnancu. [companyID]:[" + companyID + "]. " + e);
            throw new PhoneISException("Chyba pri vyhledavani aktivnich zamestnancu. " + e);
        }
        return personsDTO;
    }

    /**
     * Find company`s person.
     * @param companyId
     * @return
     * @throws cz.cvut.phone.core.exception.PhoneISException
     */
    public List<PersonDTO> findPersonDTOByCompanyId(Integer companyId) throws PhoneISException {
        List<PersonDTO> personsDTO = new ArrayList<PersonDTO>();
        try {
            List<PersonEntity> persons = basicEntityFacade.getPersonEntityDAOBean().findPersonByCompanyId(companyId);
            for (PersonEntity p : persons) {
                personsDTO.add(joinAndCreatePersonDTO(p));
            }
        } catch (Exception e) {
            log.debug("Chyba pri vyhledavani osob. [companyID]:[" + companyId + "]. " + e);
            throw new PhoneISException("Chyba pri vyhledavani osob. " + e);
        }
        return personsDTO;
    }

    /**
     * Find company`s phone numbers
     * @param filter
     * @return
     * @throws cz.cvut.phone.core.exception.PhoneISException
     */
    public List<PhoneNumberDTO> findPhoneNumberDTOsByCompanyID(PhoneNumberFilter filter) throws PhoneISException {
        try {
            List<PhoneNumberEntity> phoneEnts = basicEntityFacade.getPhoneNumberEntityDAOBean().findByFilter(filter);
            List<PhoneNumberDTO> phoneDTOs = new LinkedList<PhoneNumberDTO>();
            for (PhoneNumberEntity pne : phoneEnts) {
                phoneDTOs.add(PhoneNumberAdapter.parsePhoneNumberEntity(pne));
            }
            return phoneDTOs;
        } catch (Exception e) {
            log.debug("Chyba pri vyhledavani telefonnich cisel. [PhoneNumberFilter]:[" + filter + "]. " + e);
            throw new PhoneISException("Chyba pri vyhledavani telefonnich cisel " + e);
        }
    }

    /**
     * Find person`s phone numbers
     * @param personID
     * @return
     * @throws cz.cvut.phone.core.exception.PhoneISException
     */
    public List<PhoneNumberDTO> findPhoneNumberDTOsByPersonID(Integer personID) throws PhoneISException {
        try {
            List<PersonPhoneNumberMapEntity> personPhoneNumberMapEntitys = basicEntityFacade.getPersonPhoneNumberMapEntityDAOBean().findByPersonID(personID);
            List<PhoneNumberDTO> phoneEnts = new LinkedList<PhoneNumberDTO>();
            for (PersonPhoneNumberMapEntity pp : personPhoneNumberMapEntitys) {
                PhoneNumberDTO tmp = PhoneNumberAdapter.parsePhoneNumberEntityWithMap(pp.getPhoneNumber(), pp);
                phoneEnts.add(tmp);
            }
            return phoneEnts;
        } catch (Exception e) {
            log.debug("Chyba pri vyhledavani telefonnich cisel. [personID]:[" + personID + "]. " + e);
            throw new PhoneISException("Chyba pri vyhledavani telefonnich podle personID " + e);
        }
    }

    /**
     * Find free phone numbers.
     * @param companyID
     * @return
     * @throws cz.cvut.phone.core.exception.PhoneISException
     */
    public List<PhoneNumberDTO> findPhoneNumberDTOsFreeByCompanyId(Integer companyID) throws PhoneISException {
        try {
            List<PhoneNumberEntity> phoneEntsFree = basicEntityFacade.getPhoneNumberEntityDAOBean().findPhoneNumberDTOsFreeByCompanyId(companyID);
            List<PhoneNumberDTO> phoneDTOs = new LinkedList<PhoneNumberDTO>();
            for (PhoneNumberEntity pne : phoneEntsFree) {
                phoneDTOs.add(PhoneNumberAdapter.parsePhoneNumberEntity(pne));
            }
            return phoneDTOs;
        } catch (Exception e) {
            log.debug("Chyba pri vyhledavani volnych telefonnich cisel. [companyID]:[" + companyID + "]. " + e);
            throw new PhoneISException("Chyba pri vyhledavani volnych telefonnich cisel podle company id = " + companyID + " " + e);
        }
    }

    /**
     * Update company
     * @param companyDTO
     * @throws cz.cvut.phone.core.exception.PhoneISException
     * @throws cz.cvut.phone.core.exception.PhoneISOptimisticLockException
     */
    public void updateCompany(CompanyDTO companyDTO) throws PhoneISException, PhoneISOptimisticLockException {
        try {
            CompanyEntity companyEntity = CompanyDTOAdapter.parseCompanyDTO(companyDTO);
            basicEntityFacade.merge(companyEntity);
        } catch (PhoneISOptimisticLockException e) {
            log.debug("Chyba updatu firmy - menite neplatna data. " + e);
            throw new PhoneISOptimisticLockException("Chyba updatu firmy - menite neplatna data. " + e);
        } catch (Exception e) {
            log.debug("Chyba updatu firmy. " + e);
            throw new PhoneISException("Chyba updatu firmy. " + e);
        }
    }

    /**
     * Update department
     * @param departmentDTO
     * @throws cz.cvut.phone.core.exception.PhoneISDuplicateDataException If exists department with same shortcut
     * @throws cz.cvut.phone.core.exception.PhoneISException
     * @throws cz.cvut.phone.core.exception.PhoneISOptimisticLockException
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void updateDepartment(DepartmentDTO departmentDTO) throws PhoneISDuplicateDataException, PhoneISException, PhoneISOptimisticLockException {
        List<DepartmentEntity> deps;
        try {
            deps = basicEntityFacade.getDepartmentEntityDAOBean().find(departmentDTO.getCompanyId(), departmentDTO.getShortcut());
        } catch (Exception e) {
            log.debug("Chyba updatu oddeleni (hledani oddeleni podle zkratky) " + e);
            throw new PhoneISException("Chyba updatu oddeleni (hledani oddeleni podle zkratky) " + e);
        }
        if (deps != null && deps.size() > 0) {
            if (departmentDTO.getDepartmentId() == null) {
                throw new PhoneISDuplicateDataException("Oddělení s touto zkratkou již existuje");
            } else {
                for (DepartmentEntity dep : deps) {
                    if (!dep.getDepartmentID().equals(departmentDTO.getDepartmentId()) && dep.getShortcut().equals(departmentDTO.getShortcut())) {
                        throw new PhoneISDuplicateDataException("Oddělení s touto zkratkou již existuje");
                    }
                }
            }
        }
        try {
            DepartmentEntity departmentEntity = DepartmentDTOAdapter.parseDepartmentDTO(departmentDTO);
            CompanyEntity ce = (CompanyEntity) basicEntityFacade.find(CompanyEntity.class, departmentDTO.getCompanyId());
            departmentEntity.setCompany(ce);
            if (departmentEntity.getDepartmentID() == null) {
                basicEntityFacade.persist(departmentEntity);
            } else {
                basicEntityFacade.merge(departmentEntity);
            }
        } catch (PhoneISOptimisticLockException e) {
            log.debug("Chyba updatu oddeleni - menite neplatna data. " + e);
            throw new PhoneISOptimisticLockException("Chyba updatu oddeleni - menite neplatna data. " + e);
        } catch (Exception e) {
            log.debug("Chyba updatu oddeleni. " + e);
            throw new PhoneISException("Chyba updatu oddeleni. " + e);
        }
    }

    /**
     * Update role
     * @param operIds
     * @param roleDTO
     * @throws cz.cvut.phone.core.exception.PhoneISDuplicateDataException If exist role with same name
     * @throws cz.cvut.phone.core.exception.PhoneISException
     * @throws cz.cvut.phone.core.exception.PhoneISOptimisticLockException
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void updateRole(Integer[] operIds, RoleDTO roleDTO) throws PhoneISDuplicateDataException, PhoneISException, PhoneISOptimisticLockException {
        log.debug("Updatuji roli [roleDTO]:" + roleDTO + "]");
        List<RoleEntity> roles;
        try {
            roles = basicEntityFacade.getRoleEntityDAOBean().find(roleDTO.getCompanyId(), roleDTO.getName());
        } catch (Exception e) {
            log.debug("Chyba updatu oddeleni (hledani oddeleni podle zkratky) " + e);
            throw new PhoneISException("Chyba updatu oddeleni (hledani oddeleni podle zkratky) " + e);
        }
        if (roles != null && roles.size() > 0) {
            if (roleDTO.getRoleID() == null) {
                throw new PhoneISDuplicateDataException("Role s tímto názvem již existuje");
            } else {
                for (RoleEntity r : roles) {
                    if (!r.getRoleID().equals(roleDTO.getRoleID()) && r.getName().equals(roleDTO.getName())) {
                        throw new PhoneISDuplicateDataException("Role s tímto názvem již existuje");
                    }
                }
            }
        }
        try {
            RoleEntity roleEntity = RoleDTOAdapter.parseRoleDTO(roleDTO);
            List<OperationEntity> selectedOperations = new ArrayList<OperationEntity>();
            if (operIds != null) {
                for (Integer id : operIds) {
                    OperationEntity oe = (OperationEntity) basicEntityFacade.find(OperationEntity.class, id);
                    selectedOperations.add(oe);
                }
            }
            Integer companyID = roleDTO.getCompanyId();
            CompanyEntity companyEntity = (CompanyEntity) basicEntityFacade.find(CompanyEntity.class, companyID);
            roleEntity.setCompany(companyEntity);
            if (roleEntity.getRoleID() == null) {
                basicEntityFacade.persist(roleEntity);
                for (OperationEntity o : selectedOperations) {
                    RoleOperationMapEntity rom = new RoleOperationMapEntity(roleEntity, o);
                    basicEntityFacade.persist(rom);
                }
            } else {
                basicEntityFacade.merge(roleEntity);
                List<RoleOperationMapEntity> allRoleOperMap = basicEntityFacade.getRoleOperationMapEntityDAOBean().findByRoleID(roleEntity.getRoleID());
                for (OperationEntity selOper : selectedOperations) {
                    RoleOperationMapEntity tmpRom = null;
                    for (RoleOperationMapEntity allRom : allRoleOperMap) {
                        if (selOper.getOperationID().intValue() == allRom.getOperation().getOperationID()) {
                            tmpRom = allRom;
                            break;
                        }
                    }
                    if (tmpRom == null) {
                        tmpRom = new RoleOperationMapEntity(roleEntity, selOper);
                        basicEntityFacade.persist(tmpRom);
                    }
                }
                for (RoleOperationMapEntity allRom : allRoleOperMap) {
                    boolean found = false;
                    for (OperationEntity selOper : selectedOperations) {
                        if (allRom.getOperation().getOperationID().intValue() == selOper.getOperationID().intValue()) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        basicEntityFacade.remove(allRom);
                    }
                }
            }
        } catch (PhoneISOptimisticLockException e) {
            log.debug("Chyba updatu operaci pro roli - menite neplatna data. " + e);
            throw new PhoneISOptimisticLockException("Chyba updatu operaci pro roli - menite neplatna data. " + e);
        } catch (Exception e) {
            log.debug("Chyba updatu operaci pro roli. " + e);
            throw new PhoneISException("Chyba updatu operaci pro roli. " + e);
        }
    }

    /**
     * Change password for person. Password will be encode.
     * @param personDTO
     * @param newPass
     * @throws cz.cvut.phone.core.exception.PhoneISException
     * @throws cz.cvut.phone.core.exception.PhoneISOptimisticLockException
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void changePassForPerson(PersonDTO personDTO, String newPass) throws PhoneISException, PhoneISOptimisticLockException {
        try {
            log.debug("Menim heslo pro osobu [personDTO]:" + personDTO + "]");
            PersonEntity personEntity = (PersonEntity) basicEntityFacade.find(PersonEntity.class, personDTO.getPersonID());
            if (personEntity.getVersion() != personDTO.getVersion()) {
                throw new OptimisticLockException();
            } else {
                personEntity.setPassword(SHA.SHA1(newPass));
                basicEntityFacade.merge(personEntity);
            }
        } catch (PhoneISOptimisticLockException e) {
            log.debug("Chyba zmene hesla - menite neplatna data " + e);
            throw new PhoneISOptimisticLockException("Chyba zmene hesla - menite neplatna data " + e);
        } catch (Exception e) {
            log.debug("Chyba zmene hesla " + e);
            throw new PhoneISException("Chyba zmene hesla " + e);
        }
    }

    /**
     * Create and save person.
     * @param personEntity
     * @param departmentId
     * @param roleIds
     * @param companyId
     * @throws java.lang.Exception
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private void createPerson(PersonEntity personEntity, Integer departmentId, Integer[] roleIds, Integer companyId) throws Exception {
        log.debug("Vytvarim novou osobu.");
        personEntity.setPassword(SHA.SHA1(personEntity.getPassword()));
        personEntity.setActive(Constants.ACTIVE_STATE_ACTIVE);
        personEntity.setAccount(0.0);
        personEntity.setUsername(generateUsername(companyId, personEntity.getFirstname(), personEntity.getLastname()));
        basicEntityFacade.persist(personEntity);
        if (departmentId != null) {
            DepartmentEntity departmentEntity = (DepartmentEntity) basicEntityFacade.find(DepartmentEntity.class, departmentId);
            EmployeeEntity employeeEntity = new EmployeeEntity(departmentEntity, personEntity, new Timestamp(System.currentTimeMillis()));
            basicEntityFacade.persist(employeeEntity);
        }
        for (Integer i : roleIds) {
            RoleEntity r = (RoleEntity) basicEntityFacade.find(RoleEntity.class, i);
            PersonRoleMapEntity prm = new PersonRoleMapEntity(personEntity, r);
            basicEntityFacade.persist(prm);
        }
        if (personEntity.getResponsibleEmployee() != null) {
            Object[] params = { personEntity.getFirstname() + " " + personEntity.getLastname() };
            personFasade.createNewEvent(personEntity.getResponsibleEmployee(), new Timestamp(System.currentTimeMillis()), Constants.EVENT_TYPE_NEW_SUBPERSON, params);
        }
        SettingsEntity se = basicEntityFacade.getSettingsEntityDAOBean().findByCompanyId(companyId);
        PersonSettingEntity pse = new PersonSettingEntity(se.getDefaultLanguage(), personEntity, Constants.CHAR_BOLEAN_FALSE, Constants.PERSON_SETTING_DEFAULT_LINE, Constants.PERSON_SETTING_DEFAULT_DAYS_EVENT, Constants.CHAR_BOLEAN_TRUE, Constants.CHAR_BOLEAN_TRUE, Constants.CHAR_BOLEAN_TRUE, Constants.CHAR_BOLEAN_TRUE, Constants.CHAR_BOLEAN_TRUE);
        basicEntityFacade.persist(pse);
    }

    /**
     * Check if responsible person was change. If yes, system create new event.
     * @param personEntity
     * @throws java.lang.Exception
     */
    private void checkPersonChangeAndCreateEvent(PersonEntity personEntity) throws Exception {
        PersonEntity peOld = (PersonEntity) basicEntityFacade.find(PersonEntity.class, personEntity.getPersonID());
        if (peOld.getResponsibleEmployee() == null && personEntity.getResponsibleEmployee() != null) {
            Object[] params = { personEntity.getFirstname() + " " + personEntity.getLastname() };
            personFasade.createNewEvent(personEntity.getResponsibleEmployee(), new Timestamp(System.currentTimeMillis()), Constants.EVENT_TYPE_NEW_SUBPERSON, params);
        } else if (peOld.getResponsibleEmployee() != null && personEntity.getResponsibleEmployee() == null) {
            Object[] params = { peOld.getFirstname() + " " + peOld.getLastname() };
            personFasade.createNewEvent(peOld.getResponsibleEmployee(), new Timestamp(System.currentTimeMillis()), Constants.EVENT_TYPE_SUBPERSON_CHANGE, params);
        } else if (peOld.getResponsibleEmployee() != null && personEntity.getResponsibleEmployee() != null && !peOld.getResponsibleEmployee().equals(personEntity.getResponsibleEmployee())) {
            Timestamp change = new Timestamp(System.currentTimeMillis());
            Object[] params = { personEntity.getFirstname() + " " + personEntity.getLastname() };
            personFasade.createNewEvent(personEntity.getResponsibleEmployee(), change, Constants.EVENT_TYPE_NEW_SUBPERSON, params);
            personFasade.createNewEvent(peOld.getResponsibleEmployee(), change, Constants.EVENT_TYPE_SUBPERSON_CHANGE, params);
        }
    }

    /**
     * Update person.
     * @param personDTO
     * @param departmentId
     * @param roleIds
     * @throws cz.cvut.phone.core.exception.PhoneISException
     * @throws cz.cvut.phone.core.exception.PhoneISOptimisticLockException
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void updatePerson(PersonDTO personDTO, Integer departmentId, Integer[] roleIds) throws PhoneISException, PhoneISOptimisticLockException {
        try {
            if (roleIds == null || roleIds.length == 0) {
                throw new PhoneISException("Uzivatel musi mit prirazenou alespon 1 roli");
            }
            PersonEntity personEntity = PersonDTOAdapter.parsePersonDTO(personDTO);
            if (personEntity.getPersonID() == null) {
                createPerson(personEntity, departmentId, roleIds, personDTO.getCompanyID());
            } else {
                checkPersonChangeAndCreateEvent(personEntity);
                basicEntityFacade.merge(personEntity);
                if (departmentId == null) {
                    EmployeeEntity employeeEntity = basicEntityFacade.getPersonEntityDAOBean().findEmployeeByPersonId(personEntity.getPersonID());
                    if (employeeEntity != null) {
                        employeeEntity.setEndEmploy(new Timestamp(System.currentTimeMillis()));
                        basicEntityFacade.merge(employeeEntity);
                    }
                } else {
                    EmployeeEntity employeeEntity = basicEntityFacade.getPersonEntityDAOBean().findEmployeeByPersonId(personEntity.getPersonID());
                    if (employeeEntity == null) {
                        employeeEntity = new EmployeeEntity(personEntity, new Timestamp(System.currentTimeMillis()));
                    }
                    DepartmentEntity departmentEntity = (DepartmentEntity) basicEntityFacade.find(DepartmentEntity.class, departmentId);
                    employeeEntity.setEndEmploy(personDTO.getEndEmploy());
                    employeeEntity.setDepartment(departmentEntity);
                    basicEntityFacade.merge(employeeEntity);
                }
                List<PersonRoleMapEntity> allPersonRoleMap = basicEntityFacade.getPersonRoleMapEntityDAOBean().findByPersonID(personEntity.getPersonID());
                List<RoleEntity> selectedRole = new ArrayList<RoleEntity>();
                for (Integer i : roleIds) {
                    RoleEntity r = (RoleEntity) basicEntityFacade.find(RoleEntity.class, i);
                    selectedRole.add(r);
                }
                for (RoleEntity r : selectedRole) {
                    boolean found = false;
                    for (PersonRoleMapEntity prm : allPersonRoleMap) {
                        if (prm.getRole().getRoleID().intValue() == r.getRoleID().intValue()) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        PersonRoleMapEntity prmNew = new PersonRoleMapEntity(personEntity, r);
                        basicEntityFacade.persist(prmNew);
                    }
                }
                for (PersonRoleMapEntity prm : allPersonRoleMap) {
                    boolean found = false;
                    for (RoleEntity r : selectedRole) {
                        if (prm.getRole().getRoleID().intValue() == r.getRoleID().intValue()) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        basicEntityFacade.remove(prm);
                    }
                }
            }
        } catch (PhoneISOptimisticLockException e) {
            log.debug("Chyba updatu osoby - menite neplatna data " + e);
            throw new PhoneISOptimisticLockException("Chyba updatu osoby - menite neplatna data " + e);
        } catch (Exception e) {
            log.debug("Chyba updatu osoby " + e);
            throw new PhoneISException("Chyba updatu osoby " + e);
        }
    }

    /**
     * Update company`s settings.
     * @param settingsDTO
     * @throws cz.cvut.phone.core.exception.PhoneISException
     * @throws cz.cvut.phone.core.exception.PhoneISOptimisticLockException
     */
    public void updateSettings(SettingsDTO settingsDTO) throws PhoneISException, PhoneISOptimisticLockException {
        try {
            SettingsEntity settingsEntity = SettingsDTOAdapter.parseSettingsDTO(settingsDTO);
            CompanyEntity companyEntity = (CompanyEntity) basicEntityFacade.find(CompanyEntity.class, settingsDTO.getCompanyID());
            settingsEntity.setCompany(companyEntity);
            basicEntityFacade.merge(settingsEntity);
        } catch (PhoneISOptimisticLockException e) {
            log.debug("Chyba updatu nastaveni - menite neplatna data " + e);
            throw new PhoneISOptimisticLockException("Chyba updatu nastaveni - menite neplatna data " + e);
        } catch (Exception e) {
            log.debug("Chyba updatu nastaveni" + e);
            throw new PhoneISException("Chyba updatu nastaveni" + e);
        }
    }

    /**
     * Find phone number. If exist in db, return true
     * @param companyID
     * @param number
     * @return
     * @throws java.lang.Exception
     */
    private boolean existNumberInDB(Integer companyID, String number) throws Exception {
        PhoneNumberFilter filter = new PhoneNumberFilter();
        filter.setNumber(number);
        filter.setCompanyID(companyID);
        List<PhoneNumberEntity> phones;
        phones = basicEntityFacade.getPhoneNumberEntityDAOBean().findByFilter(filter);
        if (phones != null && phones.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Update phone number
     * @param phoneDTO
     * @throws cz.cvut.phone.core.exception.PhoneISDuplicateDataException If number exist in DB in the same company
     * @throws cz.cvut.phone.core.exception.PhoneISException
     * @throws cz.cvut.phone.core.exception.PhoneISOptimisticLockException
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void updatePhoneNumber(PhoneNumberDTO phoneDTO) throws PhoneISDuplicateDataException, PhoneISException, PhoneISOptimisticLockException {
        if (phoneDTO.getPhoneNumberID() == null) {
            boolean exist = true;
            try {
                exist = existNumberInDB(phoneDTO.getCompanyID(), phoneDTO.getNumber());
            } catch (Exception e) {
                System.out.println("chyba");
                throw new PhoneISException("Chyba pri kontrole unikatnosti tel. cisla. " + e);
            }
            if (exist) {
                System.out.println("cislo existuje");
                throw new PhoneISDuplicateDataException("Cislo v DB jiz existuje");
            }
        }
        System.out.println("xxxx");
        try {
            PhoneNumberEntity phoneNumberEntity = PhoneNumberAdapter.parsePhoneNumberDTO(phoneDTO);
            CompanyEntity companyEntity = (CompanyEntity) basicEntityFacade.find(CompanyEntity.class, phoneDTO.getCompanyID());
            phoneNumberEntity.setCompany(companyEntity);
            if (phoneNumberEntity.getPhoneNumberID() == null) {
                basicEntityFacade.persist(phoneNumberEntity);
            } else {
                basicEntityFacade.merge(phoneNumberEntity);
            }
        } catch (PhoneISOptimisticLockException e) {
            log.debug("Chyba updatu tel. cisla - menite neplatna data " + e);
            throw new PhoneISOptimisticLockException("Chyba updatu nastaveni - menite neplatna data " + e);
        } catch (Exception e) {
            log.debug("Chyba updatu tel. cisla" + e);
            throw new PhoneISException("Chyba updatu nastaveni" + e);
        }
    }

    /**
     * Deactive department.
     * @param departmentID
     * @throws cz.cvut.phone.core.exception.PhoneISException Eg. if in system there are employees
     * @throws cz.cvut.phone.core.exception.PhoneISOptimisticLockException
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deactiveDepartment(Integer departmentID) throws PhoneISException, PhoneISOptimisticLockException {
        try {
            DepartmentEntity departmentEntity = (DepartmentEntity) basicEntityFacade.find(DepartmentEntity.class, departmentID);
            List<EmployeeEntity> emps = basicEntityFacade.getPersonEntityDAOBean().findEmployeeActiveByDepartmentId(departmentEntity.getDepartmentID());
            if (emps.size() > 0) {
                throw new Exception("V oddeleni jsou aktivni zamestnanci");
            } else {
                departmentEntity.setActive(Constants.ACTIVE_STATE_DEACTIVE);
                basicEntityFacade.merge(departmentEntity);
            }
        } catch (PhoneISOptimisticLockException e) {
            log.debug("Chyba updatu tel. cisla - menite neplatna data " + e);
            throw new PhoneISOptimisticLockException("Chyba updatu nastaveni - menite neplatna data " + e);
        } catch (Exception e) {
            log.debug("Chyba updatu tel. cisla" + e);
            throw new PhoneISException("Chyba updatu nastaveni" + e);
        }
    }

    /**
     * Deactive phone number. Find person which use the number and terminate it
     * @param phoneNumberID
     * @return
     * @throws cz.cvut.phone.core.exception.PhoneISException
     * @throws cz.cvut.phone.core.exception.PhoneISOptimisticLockException
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public PhoneNumberDTO deactivePhoneNumber(Integer phoneNumberID) throws PhoneISException, PhoneISOptimisticLockException {
        try {
            PhoneNumberEntity phoneNumberEntity = (PhoneNumberEntity) basicEntityFacade.find(PhoneNumberEntity.class, phoneNumberID);
            List<PersonPhoneNumberMapEntity> maps = basicEntityFacade.getPersonPhoneNumberMapEntityDAOBean().findByPhoneNumberID(phoneNumberEntity.getPhoneNumberID());
            for (PersonPhoneNumberMapEntity map : maps) {
                map.setEndUse(new Timestamp(System.currentTimeMillis()));
                basicEntityFacade.merge(map);
            }
            phoneNumberEntity.setActive(Constants.ACTIVE_STATE_DEACTIVE);
            basicEntityFacade.merge(phoneNumberEntity);
            PhoneNumberDTO phoneDTO = PhoneNumberAdapter.parsePhoneNumberEntity(phoneNumberEntity);
            return phoneDTO;
        } catch (PhoneISOptimisticLockException e) {
            log.debug("Chyba updatu tel. cisla - menite neplatna data " + e);
            throw new PhoneISOptimisticLockException("Chyba updatu nastaveni - menite neplatna data " + e);
        } catch (Exception e) {
            log.debug("Chyba updatu tel. cisla" + e);
            throw new PhoneISException("Chyba updatu nastaveni" + e);
        }
    }

    /**
     * Deactive person. Find all phone number which person currenly use a terminate it.
     * @param personID
     * @throws cz.cvut.phone.core.exception.PhoneISException
     * @throws cz.cvut.phone.core.exception.PhoneISOptimisticLockException
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deactivePerson(Integer personID) throws PhoneISException, PhoneISOptimisticLockException {
        try {
            PersonEntity personEntity = (PersonEntity) basicEntityFacade.find(PersonEntity.class, personID);
            personEntity.setActive(Constants.ACTIVE_STATE_DEACTIVE);
            EmployeeEntity eml = basicEntityFacade.getPersonEntityDAOBean().findEmployeeByPersonId(personID);
            if (eml != null) {
                eml.setEndEmploy(new Timestamp(System.currentTimeMillis()));
                basicEntityFacade.merge(eml);
            }
            List<PersonPhoneNumberMapEntity> maps = basicEntityFacade.getPersonPhoneNumberMapEntityDAOBean().findUseByPersonId(personID);
            for (PersonPhoneNumberMapEntity map : maps) {
                map.setEndUse(new Timestamp(System.currentTimeMillis()));
                basicEntityFacade.merge(map);
            }
            basicEntityFacade.merge(personEntity);
        } catch (PhoneISOptimisticLockException e) {
            log.debug("Chyba deaktivaci osoby - menite neplatna data " + e);
            throw new PhoneISOptimisticLockException("Chyba deaktivaci osoby - menite neplatna data " + e);
        } catch (Exception e) {
            log.debug("Chyba deaktivaci osoby " + e);
            throw new PhoneISException("Chyba deaktivaci osoby " + e);
        }
    }

    /**
     * Delete phone number.
     * @param personID
     * @param phoneNumberDTO
     * @throws cz.cvut.phone.core.exception.PhoneISException
     */
    public void deletePhoneFromUser(Integer personID, PhoneNumberDTO phoneNumberDTO) throws PhoneISException {
        try {
            PersonPhoneNumberMapEntity pp = basicEntityFacade.getPersonPhoneNumberMapEntityDAOBean().findByPersonIdPhoneIdEndIsNull(personID, phoneNumberDTO.getPhoneNumberID());
            pp.setEndUse(new Timestamp(System.currentTimeMillis()));
            basicEntityFacade.merge(pp);
        } catch (Exception e) {
            log.debug("Chyba odebirani tel. cisla osobe " + e);
            throw new PhoneISException("Chyba odebirani tel. cisla osobe " + e);
        }
    }

    /**
     * Delete department.
     * @param departmentID
     * @throws cz.cvut.phone.core.exception.PhoneISDeleteException
     * @throws cz.cvut.phone.core.exception.PhoneISException
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteDepartment(Integer departmentID) throws PhoneISDeleteException, PhoneISException {
        List<EmployeeEntity> empls = null;
        try {
            empls = basicEntityFacade.getPersonEntityDAOBean().findEmployeeByDepartmentId(departmentID);
        } catch (Exception e) {
            log.debug("Chyba pri mazani oddeleni z DB - nepodaril se nacist seznam osob v oddeleni" + e);
            throw new PhoneISException("Chyba pri mazani oddeleni z DB - nepodaril se nacist seznam osob v oddeleni" + e);
        }
        if (empls != null && empls.size() > 0) {
            throw new PhoneISDeleteException("V oddeleni jsou osoby");
        } else {
            try {
                DepartmentEntity d = (DepartmentEntity) basicEntityFacade.find(DepartmentEntity.class, departmentID);
                basicEntityFacade.remove(d);
            } catch (Exception e) {
                log.debug("Chyba pri mazani oddeleni z DB" + e);
                throw new PhoneISException("Chyba pri mazani oddeleni z DB" + e);
            }
        }
    }

    /**
     * Delete role.
     * @param roleID
     * @throws cz.cvut.phone.core.exception.PhoneISException
     * @throws cz.cvut.phone.core.exception.PhoneISDeleteException
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteRole(Integer roleID) throws PhoneISException, PhoneISDeleteException {
        List<PersonRoleMapEntity> roles = null;
        try {
            roles = basicEntityFacade.getPersonRoleMapEntityDAOBean().findByRoleID(roleID);
        } catch (Exception e) {
            log.debug("Chyba pri mazani role z DB - nepodarilo se zjistit zda ma roli nekdo pridelenou" + e);
            throw new PhoneISException("Chyba pri mazani role z DB - nepodarilo se zjistit zda ma roli nekdo pridelenou" + e);
        }
        if (roles != null && roles.size() > 0) {
            throw new PhoneISDeleteException("V oddeleni jsou osoby");
        } else {
            try {
                List<RoleOperationMapEntity> romes = basicEntityFacade.getRoleOperationMapEntityDAOBean().findByRoleID(roleID);
                for (RoleOperationMapEntity r : romes) {
                    basicEntityFacade.remove(r);
                }
                RoleEntity roleEntity = (RoleEntity) basicEntityFacade.find(RoleEntity.class, roleID);
                basicEntityFacade.remove(roleEntity);
            } catch (Exception e) {
                log.debug("Chyba pri mazani role z DB" + e);
                throw new PhoneISException("Chyba pri mazani role z DB" + e);
            }
        }
    }

    /**
     * Add phone number.
     * @param personID
     * @param phoneNumberID
     * @throws cz.cvut.phone.core.exception.PhoneISException
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void addPhoneNumberForPerson(Integer personID, Integer phoneNumberID) throws PhoneISException {
        try {
            PersonEntity personEntity = (PersonEntity) basicEntityFacade.find(PersonEntity.class, personID);
            PhoneNumberEntity phoneNumberEntity = (PhoneNumberEntity) basicEntityFacade.find(PhoneNumberEntity.class, phoneNumberID);
            List<PersonPhoneNumberMapEntity> usePhone = basicEntityFacade.getPersonPhoneNumberMapEntityDAOBean().findByPhoneNumberID(phoneNumberID);
            for (PersonPhoneNumberMapEntity mapEn : usePhone) {
                if (mapEn.getEndUse() == null) {
                    log.debug("Telefonni cislo je pouzivano. [phoneNumberID]:[" + phoneNumberID + "]");
                    throw new Exception("Number is currently use");
                }
            }
            PersonPhoneNumberMapEntity ppnme = new PersonPhoneNumberMapEntity(new Timestamp(System.currentTimeMillis()), personEntity, phoneNumberEntity);
            basicEntityFacade.persist(ppnme);
        } catch (Exception e) {
            log.debug("Chyba pridani tel. cisla osobe " + e);
            throw new PhoneISException("Chyba pridani tel. cisla osobe " + e);
        }
    }

    /**
     * Generate username.
     * @param companyID
     * @param firstname
     * @param surname
     * @return
     * @throws cz.cvut.phone.core.exception.PhoneISException
     */
    public String generateUsername(Integer companyID, String firstname, String surname) throws PhoneISException {
        try {
            firstname = ISCoreUtils.removeDiacritics(firstname);
            surname = ISCoreUtils.removeDiacritics(surname);
            String usernameGenerate = ISCoreUtils.constructUsername(firstname, surname);
            String username;
            PersonEntityFilter pf = new PersonEntityFilter();
            List<PersonEntity> list = null;
            int i = 1;
            while (true) {
                username = usernameGenerate + i;
                pf.setUsername(username);
                list = basicEntityFacade.getPersonEntityDAOBean().findByCompanyIdAndFilter(companyID, pf);
                if (list == null || list.size() < 1) {
                    break;
                }
                i++;
            }
            return username;
        } catch (Exception e) {
            log.debug("Chyba konstrukci volneho username [companyID, firstname, surname]:[" + companyID + ", " + firstname + ", " + surname + "]." + e);
            throw new PhoneISException("Chyba konstrukci volneho username. " + e);
        }
    }

    public Logger getLog() {
        return log;
    }

    public void setLog(Logger log) {
        this.log = log;
    }
}

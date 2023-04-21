package com.ag.promanagement.control;

import com.ag.dataaccess.daoFactory.JPADaoFactory;
import com.ag.dataaccess.entityManager.EntityManagerHelper;
import com.ag.exceptions.*;
import com.ag.promanagement.CargoType;
import com.ag.promanagement.DocumentType;
import com.ag.promanagement.Process;
import com.ag.promanagement.UserType;
import com.ag.promanagement.Users;
import com.ag.utilities.Utilities;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
*
* @author Zathura Code Generator http://code.google.com/p/zathura
*
*/
public class UsersLogic implements IUsersLogic {

    public List<Users> getUsers() throws Exception {
        List<Users> list = new ArrayList<Users>();
        try {
            list = JPADaoFactory.getInstance().getUsersDAO().findAll(0);
        } catch (Exception e) {
            throw new Exception(ExceptionManager.getInstance().exceptionInGetAll());
        } finally {
            EntityManagerHelper.closeEntityManager();
        }
        return list;
    }

    public void saveUsers(Long cellNumber, Long documentId, String email, String firstName, Long id, String lastName, String login, String password, Long id_CargoType, Long id_DocumentType, Long id_UserType) throws Exception {
        Users entity = null;
        try {
            if ((cellNumber != null) && (Utilities.checkNumberAndCheckWithPrecisionAndScale("" + cellNumber, 131089, 0) == false)) {
                throw new Exception(ExceptionMessages.VARIABLE_LENGTH + "cellNumber");
            }
            if (documentId == null) {
                throw new Exception(ExceptionMessages.VARIABLE_NULL + "documentId");
            }
            if ((documentId != null) && (Utilities.checkNumberAndCheckWithPrecisionAndScale("" + documentId, 131089, 0) == false)) {
                throw new Exception(ExceptionMessages.VARIABLE_LENGTH + "documentId");
            }
            if ((email != null) && (Utilities.checkWordAndCheckWithlength(email, 255) == false)) {
                throw new Exception(ExceptionMessages.VARIABLE_LENGTH + "email");
            }
            if (firstName == null) {
                throw new Exception(ExceptionMessages.VARIABLE_NULL + "firstName");
            }
            if ((firstName != null) && (Utilities.checkWordAndCheckWithlength(firstName, 255) == false)) {
                throw new Exception(ExceptionMessages.VARIABLE_LENGTH + "firstName");
            }
            if (id == null) {
                throw new Exception(ExceptionMessages.VARIABLE_NULL + "id");
            }
            if ((id != null) && (Utilities.checkNumberAndCheckWithPrecisionAndScale("" + id, 131089, 0) == false)) {
                throw new Exception(ExceptionMessages.VARIABLE_LENGTH + "id");
            }
            if (lastName == null) {
                throw new Exception(ExceptionMessages.VARIABLE_NULL + "lastName");
            }
            if ((lastName != null) && (Utilities.checkWordAndCheckWithlength(lastName, 255) == false)) {
                throw new Exception(ExceptionMessages.VARIABLE_LENGTH + "lastName");
            }
            if (login == null) {
                throw new Exception(ExceptionMessages.VARIABLE_NULL + "login");
            }
            if ((login != null) && (Utilities.checkWordAndCheckWithlength(login, 255) == false)) {
                throw new Exception(ExceptionMessages.VARIABLE_LENGTH + "login");
            }
            if (password == null) {
                throw new Exception(ExceptionMessages.VARIABLE_NULL + "password");
            }
            if ((password != null) && (Utilities.checkWordAndCheckWithlength(password, 255) == false)) {
                throw new Exception(ExceptionMessages.VARIABLE_LENGTH + "password");
            }
            if (id_CargoType == null) {
                throw new Exception(ExceptionMessages.VARIABLE_NULL + "id_CargoType");
            }
            if ((id_CargoType != null) && (Utilities.checkNumberAndCheckWithPrecisionAndScale("" + id_CargoType, 131089, 0) == false)) {
                throw new Exception(ExceptionMessages.VARIABLE_LENGTH + "id_CargoType");
            }
            if (id_DocumentType == null) {
                throw new Exception(ExceptionMessages.VARIABLE_NULL + "id_DocumentType");
            }
            if ((id_DocumentType != null) && (Utilities.checkNumberAndCheckWithPrecisionAndScale("" + id_DocumentType, 131089, 0) == false)) {
                throw new Exception(ExceptionMessages.VARIABLE_LENGTH + "id_DocumentType");
            }
            if (id_UserType == null) {
                throw new Exception(ExceptionMessages.VARIABLE_NULL + "id_UserType");
            }
            if ((id_UserType != null) && (Utilities.checkNumberAndCheckWithPrecisionAndScale("" + id_UserType, 131089, 0) == false)) {
                throw new Exception(ExceptionMessages.VARIABLE_LENGTH + "id_UserType");
            }
            ICargoTypeLogic logicCargoType1 = new CargoTypeLogic();
            IDocumentTypeLogic logicDocumentType2 = new DocumentTypeLogic();
            IUserTypeLogic logicUserType3 = new UserTypeLogic();
            CargoType cargoTypeClass = logicCargoType1.getCargoType(id_CargoType);
            DocumentType documentTypeClass = logicDocumentType2.getDocumentType(id_DocumentType);
            UserType userTypeClass = logicUserType3.getUserType(id_UserType);
            if (cargoTypeClass == null) {
                throw new Exception(ExceptionMessages.VARIABLE_NULL);
            }
            if (documentTypeClass == null) {
                throw new Exception(ExceptionMessages.VARIABLE_NULL);
            }
            if (userTypeClass == null) {
                throw new Exception(ExceptionMessages.VARIABLE_NULL);
            }
            entity = getUsers(id);
            if (entity != null) {
                throw new Exception(ExceptionMessages.ENTITY_WITHSAMEKEY);
            }
            entity = new Users();
            entity.setCellNumber(cellNumber);
            entity.setDocumentId(documentId);
            entity.setEmail(email);
            entity.setFirstName(firstName);
            entity.setId(id);
            entity.setLastName(lastName);
            entity.setLogin(login);
            entity.setPassword(password);
            entity.setCargoType(cargoTypeClass);
            entity.setDocumentType(documentTypeClass);
            entity.setUserType(userTypeClass);
            EntityManagerHelper.beginTransaction();
            JPADaoFactory.getInstance().getUsersDAO().save(entity);
            EntityManagerHelper.commit();
        } catch (Exception e) {
            EntityManagerHelper.rollback();
            throw e;
        } finally {
            EntityManagerHelper.closeEntityManager();
        }
    }

    public void deleteUsers(Long id) throws Exception {
        Users entity = null;
        if (id == null) {
            throw new Exception(ExceptionMessages.VARIABLE_NULL);
        }
        List<Process> processes = null;
        entity = getUsers(id);
        if (entity == null) {
            throw new Exception(ExceptionMessages.ENTITY_NULL);
        }
        try {
            processes = JPADaoFactory.getInstance().getProcessDAO().findByProperty("users.id", id, 0);
            if (Utilities.validationsList(processes) == false) {
                throw new Exception(ExceptionManager.getInstance().exceptionDeletingEntityWithChild());
            }
            EntityManagerHelper.beginTransaction();
            JPADaoFactory.getInstance().getUsersDAO().delete(entity);
            EntityManagerHelper.commit();
        } catch (Exception e) {
            EntityManagerHelper.rollback();
            throw e;
        } finally {
            EntityManagerHelper.closeEntityManager();
        }
    }

    public void updateUsers(Long cellNumber, Long documentId, String email, String firstName, Long id, String lastName, String login, String password, Long id_CargoType, Long id_DocumentType, Long id_UserType) throws Exception {
        Users entity = null;
        try {
            if ((cellNumber != null) && (Utilities.checkNumberAndCheckWithPrecisionAndScale("" + cellNumber, 131089, 0) == false)) {
                throw new Exception(ExceptionMessages.VARIABLE_LENGTH + "cellNumber");
            }
            if (documentId == null) {
                throw new Exception(ExceptionMessages.VARIABLE_NULL + "documentId");
            }
            if ((documentId != null) && (Utilities.checkNumberAndCheckWithPrecisionAndScale("" + documentId, 131089, 0) == false)) {
                throw new Exception(ExceptionMessages.VARIABLE_LENGTH + "documentId");
            }
            if ((email != null) && (Utilities.checkWordAndCheckWithlength(email, 255) == false)) {
                throw new Exception(ExceptionMessages.VARIABLE_LENGTH + "email");
            }
            if (firstName == null) {
                throw new Exception(ExceptionMessages.VARIABLE_NULL + "firstName");
            }
            if ((firstName != null) && (Utilities.checkWordAndCheckWithlength(firstName, 255) == false)) {
                throw new Exception(ExceptionMessages.VARIABLE_LENGTH + "firstName");
            }
            if (id == null) {
                throw new Exception(ExceptionMessages.VARIABLE_NULL + "id");
            }
            if ((id != null) && (Utilities.checkNumberAndCheckWithPrecisionAndScale("" + id, 131089, 0) == false)) {
                throw new Exception(ExceptionMessages.VARIABLE_LENGTH + "id");
            }
            if (lastName == null) {
                throw new Exception(ExceptionMessages.VARIABLE_NULL + "lastName");
            }
            if ((lastName != null) && (Utilities.checkWordAndCheckWithlength(lastName, 255) == false)) {
                throw new Exception(ExceptionMessages.VARIABLE_LENGTH + "lastName");
            }
            if (login == null) {
                throw new Exception(ExceptionMessages.VARIABLE_NULL + "login");
            }
            if ((login != null) && (Utilities.checkWordAndCheckWithlength(login, 255) == false)) {
                throw new Exception(ExceptionMessages.VARIABLE_LENGTH + "login");
            }
            if (password == null) {
                throw new Exception(ExceptionMessages.VARIABLE_NULL + "password");
            }
            if ((password != null) && (Utilities.checkWordAndCheckWithlength(password, 255) == false)) {
                throw new Exception(ExceptionMessages.VARIABLE_LENGTH + "password");
            }
            if (id_CargoType == null) {
                throw new Exception(ExceptionMessages.VARIABLE_NULL + "id_CargoType");
            }
            if ((id_CargoType != null) && (Utilities.checkNumberAndCheckWithPrecisionAndScale("" + id_CargoType, 131089, 0) == false)) {
                throw new Exception(ExceptionMessages.VARIABLE_LENGTH + "id_CargoType");
            }
            if (id_DocumentType == null) {
                throw new Exception(ExceptionMessages.VARIABLE_NULL + "id_DocumentType");
            }
            if ((id_DocumentType != null) && (Utilities.checkNumberAndCheckWithPrecisionAndScale("" + id_DocumentType, 131089, 0) == false)) {
                throw new Exception(ExceptionMessages.VARIABLE_LENGTH + "id_DocumentType");
            }
            if (id_UserType == null) {
                throw new Exception(ExceptionMessages.VARIABLE_NULL + "id_UserType");
            }
            if ((id_UserType != null) && (Utilities.checkNumberAndCheckWithPrecisionAndScale("" + id_UserType, 131089, 0) == false)) {
                throw new Exception(ExceptionMessages.VARIABLE_LENGTH + "id_UserType");
            }
            ICargoTypeLogic logicCargoType1 = new CargoTypeLogic();
            IDocumentTypeLogic logicDocumentType2 = new DocumentTypeLogic();
            IUserTypeLogic logicUserType3 = new UserTypeLogic();
            CargoType cargoTypeClass = logicCargoType1.getCargoType(id_CargoType);
            DocumentType documentTypeClass = logicDocumentType2.getDocumentType(id_DocumentType);
            UserType userTypeClass = logicUserType3.getUserType(id_UserType);
            if (cargoTypeClass == null) {
                throw new Exception(ExceptionMessages.VARIABLE_NULL);
            }
            if (documentTypeClass == null) {
                throw new Exception(ExceptionMessages.VARIABLE_NULL);
            }
            if (userTypeClass == null) {
                throw new Exception(ExceptionMessages.VARIABLE_NULL);
            }
            entity = getUsers(id);
            if (entity == null) {
                throw new Exception(ExceptionMessages.ENTITY_NOENTITYTOUPDATE);
            }
            entity.setCellNumber(cellNumber);
            entity.setDocumentId(documentId);
            entity.setEmail(email);
            entity.setFirstName(firstName);
            entity.setId(id);
            entity.setLastName(lastName);
            entity.setLogin(login);
            entity.setPassword(password);
            entity.setCargoType(cargoTypeClass);
            entity.setDocumentType(documentTypeClass);
            entity.setUserType(userTypeClass);
            EntityManagerHelper.beginTransaction();
            JPADaoFactory.getInstance().getUsersDAO().update(entity);
            EntityManagerHelper.commit();
        } catch (Exception e) {
            EntityManagerHelper.rollback();
            throw e;
        } finally {
            EntityManagerHelper.closeEntityManager();
        }
    }

    public Users getUsers(Long id) throws Exception {
        Users entity = null;
        try {
            entity = JPADaoFactory.getInstance().getUsersDAO().findById(id);
        } catch (Exception e) {
            throw new Exception(ExceptionManager.getInstance().exceptionFindingEntity("Users"));
        } finally {
            EntityManagerHelper.closeEntityManager();
        }
        return entity;
    }

    public List<Users> findPageUsers(String sortColumnName, boolean sortAscending, int startRow, int maxResults) throws Exception {
        List<Users> entity = null;
        try {
            entity = JPADaoFactory.getInstance().getUsersDAO().findPageUsers(sortColumnName, sortAscending, startRow, maxResults);
        } catch (Exception e) {
            throw new Exception(ExceptionManager.getInstance().exceptionFindingEntity("Users"));
        } finally {
            EntityManagerHelper.closeEntityManager();
        }
        return entity;
    }

    public Long findTotalNumberUsers() throws Exception {
        Long entity = null;
        try {
            entity = JPADaoFactory.getInstance().getUsersDAO().findTotalNumberUsers();
        } catch (Exception e) {
            throw new Exception(ExceptionManager.getInstance().exceptionFindingEntity("Users Count"));
        } finally {
            EntityManagerHelper.closeEntityManager();
        }
        return entity;
    }

    /**
    *
    * @param varibles
    *            este arreglo debera tener:
    *
    * [0] = String variable = (String) varibles[i]; representa como se llama la
    * variable en el pojo
    *
    * [1] = Boolean booVariable = (Boolean) varibles[i + 1]; representa si el
    * valor necesita o no ''(comillas simples)usado para campos de tipo string
    *
    * [2] = Object value = varibles[i + 2]; representa el valor que se va a
    * buscar en la BD
    *
    * [3] = String comparator = (String) varibles[i + 3]; representa que tipo
    * de busqueda voy a hacer.., ejemplo: where nombre=william o where nombre<>william,
        * en este campo iria el tipo de comparador que quiero si es = o <>
            *
            * Se itera de 4 en 4..., entonces 4 registros del arreglo representan 1
            * busqueda en un campo, si se ponen mas pues el continuara buscando en lo
            * que se le ingresen en los otros 4
            *
            *
            * @param variablesBetween
            *
            * la diferencia son estas dos posiciones
            *
            * [0] = String variable = (String) varibles[j]; la variable ne la BD que va
            * a ser buscada en un rango
            *
            * [1] = Object value = varibles[j + 1]; valor 1 para buscar en un rango
            *
            * [2] = Object value2 = varibles[j + 2]; valor 2 para buscar en un rango
            * ejempolo: a > 1 and a < 5 --> 1 seria value y 5 seria value2
                *
                * [3] = String comparator1 = (String) varibles[j + 3]; comparador 1
                * ejemplo: a comparator1 1 and a < 5
                    *
                    * [4] = String comparator2 = (String) varibles[j + 4]; comparador 2
                    * ejemplo: a comparador1>1  and a comparador2<5  (el original: a > 1 and a <
                            * 5) *
                            * @param variablesBetweenDates(en
                            *            este caso solo para mysql)
                            *  [0] = String variable = (String) varibles[k]; el nombre de la variable que hace referencia a
                            *            una fecha
                            *
                            * [1] = Object object1 = varibles[k + 2]; fecha 1 a comparar(deben ser
                            * dates)
                            *
                            * [2] = Object object2 = varibles[k + 3]; fecha 2 a comparar(deben ser
                            * dates)
                            *
                            * esto hace un between entre las dos fechas.
                            *
                            * @return lista con los objetos que se necesiten
                            * @throws Exception
                            */
    public List<Users> findByCriteria(Object[] variables, Object[] variablesBetween, Object[] variablesBetweenDates) throws Exception {
        List<Users> list = new ArrayList<Users>();
        String where = new String();
        String tempWhere = new String();
        if (variables != null) {
            for (int i = 0; i < variables.length; i++) {
                if ((variables[i] != null) && (variables[i + 1] != null) && (variables[i + 2] != null) && (variables[i + 3] != null)) {
                    String variable = (String) variables[i];
                    Boolean booVariable = (Boolean) variables[i + 1];
                    Object value = variables[i + 2];
                    String comparator = (String) variables[i + 3];
                    if (booVariable.booleanValue()) {
                        tempWhere = (tempWhere.length() == 0) ? ("(model." + variable + " " + comparator + " \'" + value + "\' )") : (tempWhere + " AND (model." + variable + " " + comparator + " \'" + value + "\' )");
                    } else {
                        tempWhere = (tempWhere.length() == 0) ? ("(model." + variable + " " + comparator + " " + value + " )") : (tempWhere + " AND (model." + variable + " " + comparator + " " + value + " )");
                    }
                }
                i = i + 3;
            }
        }
        if (variablesBetween != null) {
            for (int j = 0; j < variablesBetween.length; j++) {
                if ((variablesBetween[j] != null) && (variablesBetween[j + 1] != null) && (variablesBetween[j + 2] != null) && (variablesBetween[j + 3] != null) && (variablesBetween[j + 4] != null)) {
                    String variable = (String) variablesBetween[j];
                    Object value = variablesBetween[j + 1];
                    Object value2 = variablesBetween[j + 2];
                    String comparator1 = (String) variablesBetween[j + 3];
                    String comparator2 = (String) variablesBetween[j + 4];
                    tempWhere = (tempWhere.length() == 0) ? ("(" + value + " " + comparator1 + " " + variable + " and " + variable + " " + comparator2 + " " + value2 + " )") : (tempWhere + " AND (" + value + " " + comparator1 + " " + variable + " and " + variable + " " + comparator2 + " " + value2 + " )");
                }
                j = j + 4;
            }
        }
        if (variablesBetweenDates != null) {
            for (int k = 0; k < variablesBetweenDates.length; k++) {
                if ((variablesBetweenDates[k] != null) && (variablesBetweenDates[k + 1] != null) && (variablesBetweenDates[k + 2] != null)) {
                    String variable = (String) variablesBetweenDates[k];
                    Object object1 = variablesBetweenDates[k + 1];
                    Object object2 = variablesBetweenDates[k + 2];
                    String value = null;
                    String value2 = null;
                    try {
                        Date date1 = (Date) object1;
                        Date date2 = (Date) object2;
                        value = Utilities.formatDateWithoutTimeInAStringForBetweenWhere(date1);
                        value2 = Utilities.formatDateWithoutTimeInAStringForBetweenWhere(date2);
                    } catch (Exception e) {
                        list = null;
                        throw e;
                    }
                    tempWhere = (tempWhere.length() == 0) ? ("(model." + variable + " between \'" + value + "\' and \'" + value2 + "\')") : (tempWhere + " AND (model." + variable + " between \'" + value + "\' and \'" + value2 + "\')");
                }
                k = k + 2;
            }
        }
        if (tempWhere.length() == 0) {
            where = null;
        } else {
            where = "(" + tempWhere + ")";
        }
        try {
            list = JPADaoFactory.getInstance().getUsersDAO().findByCriteria(where);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            EntityManagerHelper.closeEntityManager();
        }
        return list;
    }
}

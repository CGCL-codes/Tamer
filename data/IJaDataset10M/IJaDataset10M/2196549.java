package br.ufmg.lcc.eid.model;

import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import br.ufmg.lcc.arangi.commons.BasicException;
import br.ufmg.lcc.arangi.model.IPersistenceObject;
import br.ufmg.lcc.arangi.model.StandardBusinessObject;
import br.ufmg.lcc.eid.commons.EidException;
import br.ufmg.lcc.eid.dto.ClassDef;
import br.ufmg.lcc.eid.dto.EidClass;
import br.ufmg.lcc.eid.dto.EidObject;

/**
 * 
 * @author tsantos
 *
 */
public class GroupBO extends StandardBusinessObject {

    private static final String GROUP_QUERY = "consulta";

    private static final String GROUP_CLASS = "Grupo";

    /**
	 * Updates group's members 
	 * @param dao objetc used to control transaction
	 * @param guid group's guid
	 * @throws BasicException if any error occurs
	 */
    public void updateGroup(IPersistenceObject dao, String guid) throws BasicException {
        EidObjectBO eidObjectBO = (EidObjectBO) getBusinessObject("br.ufmg.lcc.eid.model.EidObjectBO");
        EidObject group = eidObjectBO.getEidObject(dao, guid);
        if (!group.getType().equals(EidObject.GROUP_TYPE)) {
            throw EidException.eidErrorHandling("Object " + guid + " does not represent a group", "GroupBO.updateGroup.notAGroup", new String[] { guid }, log);
        }
        List<EidClass> classes = group.getClassByName(GROUP_CLASS);
        if (classes == null || classes.size() == 0) {
            throw EidException.eidErrorHandling("Object does not implement Group definition", "GroupBO.updateGroup.noInstanceOfGroupClass", new String[] { guid }, log);
        }
        EidClass groupDef = classes.get(0);
        String condition = (String) groupDef.getPropertyValue(GROUP_QUERY);
        if (StringUtils.isEmpty(condition)) {
            throw EidException.eidErrorHandling("Query for group " + guid + " not defined", "GroupBO.updateGroup.queryNotDefined", new String[] { guid }, log);
        }
        log.info("Updating group " + guid);
        log.debug("Condition: " + condition);
        removeMembers(dao, guid);
        associateMembers(dao, eidObjectBO, guid, condition);
    }

    /**
	 * Updates each group in the system
	 * @param dao
	 * @throws BasicException
	 */
    @SuppressWarnings("unchecked")
    public void updateAllGroups(IPersistenceObject dao) throws BasicException {
        String query = "SELECT e.stringID from EidObject e where e.type = 'group'";
        List<String> guids = dao.executeQuery(query);
        for (String guid : guids) {
            try {
                updateGroup(dao, guid);
            } catch (Exception e) {
                log.error("Error updating group " + guid, e);
            }
        }
    }

    /**
	 * TODO Adicionar suporte a grupos aninhados
	 * TODO Verificar se objetos s�o distintos na consulta (da forma que est�, pode associar uma inst�ncia duas vezes ao mesmo objeto)
	 * Associates members to a given group
	 * @param dao
	 * @param eidObjectBO
	 * @param guid
	 * @param condition
	 * @throws BasicException 
	 */
    @SuppressWarnings("unchecked")
    private void associateMembers(IPersistenceObject dao, EidObjectBO eidObjectBO, String guid, String condition) throws BasicException {
        ClassDefBO classDefBO = (ClassDefBO) getBusinessObject("br.ufmg.lcc.eid.model.ClassDefBO");
        ClassDef groupMembership = classDefBO.getClass(dao, "MembroDeGrupo");
        groupMembership.loadXml(groupMembership.getDefinition());
        List<EidObject> newMembers = dao.executeQuery(condition);
        for (EidObject object : newMembers) {
            EidClass newClass = EidClass.newInstance("br.ufmg.lcc.eid.services.MembroDeGrupo");
            newClass.setPropertyValue("grupoGuid", guid);
            newClass.setClassDefinition(groupMembership);
            object.getEidClasses().add(newClass);
            newClass.setEidObject(object);
            eidObjectBO.doUpdate(dao, object, null);
        }
    }

    /**
	 * Removes all members of a given group
	 * @param dao
	 * @param guid
	 * @throws BasicException
	 */
    private void removeMembers(IPersistenceObject dao, String guid) throws BasicException {
        EidObjectBO eidObjectBO = (EidObjectBO) getBusinessObject("br.ufmg.lcc.eid.model.EidObjectBO");
        List<EidObject> members = eidObjectBO.getObjectsImplementingClass(dao, "MembroDeGrupo", "membrodegrupo.grupoGuid = '" + guid + "'");
        EidClassBO eidClassBO = (EidClassBO) getBusinessObject("br.ufmg.lcc.eid.model.EidClassBO");
        if (members == null || members.size() == 0) {
            log.debug("Group " + guid + " does not have any member");
            return;
        }
        log.debug("Removing " + members.size() + " members of group " + guid);
        for (EidObject object : members) {
            removeObjectFromGroup(dao, object, guid, eidClassBO);
            eidObjectBO.doUpdate(dao, object, null);
        }
    }

    /**
	 * TODO Melhorar este c�digo , � poss�vel fazer de forma mais eficiente
	 * @param object
	 * @param guid
	 * @param eidClassBO 
	 * @throws BasicException 
	 */
    private void removeObjectFromGroup(IPersistenceObject dao, EidObject object, String guid, EidClassBO eidClassBO) throws BasicException {
        List<EidClass> instances = object.getEidClasses();
        for (int i = 0; i < instances.size(); i++) {
            EidClass inst = instances.get(i);
            if (inst.getClassDefinition().getName().equals("MembroDeGrupo")) {
                if (inst.getPropertyValue("grupoGuid").equals(guid)) {
                    inst.setCheckDelete(true);
                }
            }
        }
        object.setEidClasses(instances);
    }

    /**
	 * 
	 * @param dao
	 * @param guidGroup Guid of the group
	 * @return list with the member of the group (0 - empty group,  or n elements)
	 * @throws BasicException
	 */
    public List<EidObject> getGroupMembers(IPersistenceObject dao, String guidGroup) throws BasicException {
        List<EidObject> l = dao.executeQuery("SELECT m.eidObject FROM  MembroDeGrupo m WHERE m.grupoGuid = '" + guidGroup + "'");
        return (l == null ? new LinkedList<EidObject>() : l);
    }
}

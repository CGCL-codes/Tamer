package br.com.mcampos.ejb.cloudsystem.user.collaborator.role.session;

import br.com.mcampos.ejb.cloudsystem.user.collaborator.entity.Collaborator;
import br.com.mcampos.ejb.cloudsystem.user.collaborator.role.entity.CollaboratorRole;
import br.com.mcampos.ejb.cloudsystem.user.collaborator.role.entity.CollaboratorRolePK;
import br.com.mcampos.exception.ApplicationException;
import java.io.Serializable;
import java.util.List;
import javax.ejb.Local;

@Local
public interface CollaboratorRoleSessionLocal extends Serializable {

    CollaboratorRole add(CollaboratorRole entity) throws ApplicationException;

    void delete(CollaboratorRolePK key) throws ApplicationException;

    void delete(Collaborator collaborator) throws ApplicationException;

    CollaboratorRole get(CollaboratorRolePK key) throws ApplicationException;

    List<CollaboratorRole> getAll(Collaborator collaborator) throws ApplicationException;

    void setDefaultRoles(Collaborator collaborato) throws ApplicationException;
}

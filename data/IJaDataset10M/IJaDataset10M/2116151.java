package br.com.mcampos.ejb.cloudsystem.user.attribute.documenttype.facade;

import br.com.mcampos.dto.security.AuthenticationDTO;
import br.com.mcampos.dto.user.attributes.DocumentTypeDTO;
import br.com.mcampos.exception.ApplicationException;
import java.io.Serializable;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface DocumentTypeFacade extends Serializable {

    Integer nextId(AuthenticationDTO auth) throws ApplicationException;

    void delete(AuthenticationDTO auth, Integer id) throws ApplicationException;

    DocumentTypeDTO get(AuthenticationDTO auth, Integer id) throws ApplicationException;

    DocumentTypeDTO add(AuthenticationDTO auth, DocumentTypeDTO dto) throws ApplicationException;

    DocumentTypeDTO update(AuthenticationDTO auth, DocumentTypeDTO dto) throws ApplicationException;

    List<DocumentTypeDTO> getAll() throws ApplicationException;
}

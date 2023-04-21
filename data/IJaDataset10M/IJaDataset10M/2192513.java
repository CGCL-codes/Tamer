package com.voxdei.voxcontentSE.DAO.vdComment;

import java.util.List;
import com.voxdei.voxcontentSE.DAO.basic.BasicDAO;

/**
 * VdCommentI es la Interfaz del DAO para la tabla vd_comment
 * @company VoxDei.
*/
public interface VdCommentI extends BasicDAO<VdComment> {

    /**
	 * Encuenta todos las instancias de vd_comment buscandolas por TYPE.
	 * 
	 * @param value es el valor de type
	 * @return La lista de VdComment que concuerdan
	 */
    public List<?> findAllByType(String value);

    /**
	 * Encuenta todos las instancias de vd_comment buscandolas por ENABLED.
	 * 
	 * @param value es el valor de enabled
	 * @return La lista de VdComment que concuerdan
	 */
    public List<?> findAllByEnabled(Boolean value);

    /**
	 * Encuenta todos las instancias de vd_comment buscandolas por CREATED_DATE.
	 * 
	 * @param value es el valor de createdDate
	 * @return La lista de VdComment que concuerdan
	 */
    public List<?> findAllByCreatedDate(java.util.Date value);

    /**
	 * Encuenta todos las instancias de vd_comment buscandolas por FULL_TEXT.
	 * 
	 * @param value es el valor de fullText
	 * @return La lista de VdComment que concuerdan
	 */
    public List<?> findAllByFullText(String value);

    /**
	 * Encuenta todos las instancias de vd_comment buscandolas por EMAIL.
	 * 
	 * @param value es el valor de email
	 * @return La lista de VdComment que concuerdan
	 */
    public List<?> findAllByEmail(String value);

    /**
	 * Encuenta todos las instancias de vd_comment buscandolas por NICK_COMMENT.
	 * 
	 * @param value es el valor de nickComment
	 * @return La lista de VdComment que concuerdan
	 */
    public List<?> findAllByNickComment(String value);

    /**
	 * Encuenta todos las instancias de vd_comment buscandolas por TITLE.
	 * 
	 * @param value es el valor de title
	 * @return La lista de VdComment que concuerdan
	 */
    public List<?> findAllByTitle(String value);

    /**
	 * Encuenta todos las instancias de vd_comment buscandolas por ID_USER.
	 * 
	 * @param value es el valor de idUser
	 * @return La lista de VdComment que concuerdan
	 */
    public List<?> findAllByIdUser(Long value);

    /**
	 * Encuenta todos las instancias de vd_comment buscandolas por ID_PARENT.
	 * 
	 * @param value es el valor de idParent
	 * @return La lista de VdComment que concuerdan
	 */
    public List<?> findAllByIdParent(Long value);

    /**
	 * Encuenta todos las instancias de vd_comment buscandolas por ID.
	 * 
	 * @param value es el valor de id
	 * @return La lista de VdComment que concuerdan
	 */
    public List<?> findAllById(Long value);
}

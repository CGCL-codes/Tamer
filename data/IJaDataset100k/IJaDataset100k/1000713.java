package com.voxdei.voxcontentSE.DAO.vdBannerClient;

import java.util.List;
import com.voxdei.voxcontentSE.DAO.basic.BasicDAO;

/**
 * VdBannerClientI es la Interfaz del DAO para la tabla vd_banner_client
 * @company VoxDei.
*/
public interface VdBannerClientI extends BasicDAO<VdBannerClient> {

    /**
	 * Encuenta todos las instancias de vd_banner_client buscandolas por COMPANY.
	 * 
	 * @param value es el valor de company
	 * @return La lista de VdBannerClient que concuerdan
	 */
    public List<?> findAllByCompany(String value);

    /**
	 * Encuenta todos las instancias de vd_banner_client buscandolas por URL.
	 * 
	 * @param value es el valor de url
	 * @return La lista de VdBannerClient que concuerdan
	 */
    public List<?> findAllByUrl(String value);

    /**
	 * Encuenta todos las instancias de vd_banner_client buscandolas por ENABLED.
	 * 
	 * @param value es el valor de enabled
	 * @return La lista de VdBannerClient que concuerdan
	 */
    public List<?> findAllByEnabled(Boolean value);

    /**
	 * Encuenta todos las instancias de vd_banner_client buscandolas por CHECKED_OUT.
	 * 
	 * @param value es el valor de checkedOut
	 * @return La lista de VdBannerClient que concuerdan
	 */
    public List<?> findAllByCheckedOut(Boolean value);

    /**
	 * Encuenta todos las instancias de vd_banner_client buscandolas por CHECKED_DATE.
	 * 
	 * @param value es el valor de checkedDate
	 * @return La lista de VdBannerClient que concuerdan
	 */
    public List<?> findAllByCheckedDate(java.util.Date value);

    /**
	 * Encuenta todos las instancias de vd_banner_client buscandolas por DESCRIPTION.
	 * 
	 * @param value es el valor de description
	 * @return La lista de VdBannerClient que concuerdan
	 */
    public List<?> findAllByDescription(String value);

    /**
	 * Encuenta todos las instancias de vd_banner_client buscandolas por CREATED_DATE.
	 * 
	 * @param value es el valor de createdDate
	 * @return La lista de VdBannerClient que concuerdan
	 */
    public List<?> findAllByCreatedDate(java.util.Date value);

    /**
	 * Encuenta todos las instancias de vd_banner_client buscandolas por EMAIL.
	 * 
	 * @param value es el valor de email
	 * @return La lista de VdBannerClient que concuerdan
	 */
    public List<?> findAllByEmail(String value);

    /**
	 * Encuenta todos las instancias de vd_banner_client buscandolas por FULLNAME.
	 * 
	 * @param value es el valor de fullname
	 * @return La lista de VdBannerClient que concuerdan
	 */
    public List<?> findAllByFullname(String value);

    /**
	 * Encuenta todos las instancias de vd_banner_client buscandolas por ID.
	 * 
	 * @param value es el valor de id
	 * @return La lista de VdBannerClient que concuerdan
	 */
    public List<?> findAllById(Long value);
}

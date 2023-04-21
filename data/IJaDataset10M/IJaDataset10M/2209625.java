package org.koossery.adempiere.core.contract.interfaces.ldap;

import java.util.ArrayList;
import java.util.Properties;
import org.koossery.adempiere.core.contract.criteria.ldap.AD_LdapAccessCriteria;
import org.koossery.adempiere.core.contract.dto.ldap.AD_LdapAccessDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.IKTADempiereServiceComposed;

public interface IAD_LdapAccessSVCO extends IKTADempiereServiceComposed {

    public int createAD_LdapAccess(Properties ctx, AD_LdapAccessDTO aD_LdapAccessDTO, String trxname) throws KTAdempiereException;

    public AD_LdapAccessDTO findOneAD_LdapAccess(Properties ctx, int aD_LdapAccessID) throws KTAdempiereException;

    public ArrayList<AD_LdapAccessDTO> findAD_LdapAccess(Properties ctx, AD_LdapAccessCriteria aD_LdapAccessCriteria) throws KTAdempiereException;

    public void updateAD_LdapAccess(Properties ctx, AD_LdapAccessDTO aD_LdapAccessDTO) throws KTAdempiereException;

    public boolean deleteAD_LdapAccess(Properties ctx, AD_LdapAccessCriteria criteria) throws KTAdempiereException;
}

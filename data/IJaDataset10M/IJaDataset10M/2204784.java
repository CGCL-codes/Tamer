package org.koossery.adempiere.core.backend.interfaces.sisv.country;

import java.util.ArrayList;
import java.util.Properties;
import org.koossery.adempiere.core.backend.interfaces.sisv.IKTADempiereSimpleService;
import org.koossery.adempiere.core.contract.criteria.country.C_CountryCriteria;
import org.koossery.adempiere.core.contract.dto.country.C_CountryDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;

public interface IC_CountrySISV extends IKTADempiereSimpleService {

    public int createC_Country(Properties ctx, C_CountryDTO c_CountryDTO, String trxname) throws KTAdempiereAppException;

    public C_CountryDTO getC_Country(Properties ctx, int c_CountryID, String trxname) throws KTAdempiereAppException;

    public ArrayList<C_CountryDTO> findC_Country(Properties ctx, C_CountryCriteria c_CountryCriteria) throws KTAdempiereAppException;

    public void updateC_Country(Properties ctx, C_CountryDTO c_CountryDTO) throws KTAdempiereAppException;

    public boolean deleteC_Country(Properties ctx, C_CountryCriteria c_CountryCriteria) throws KTAdempiereAppException;
}

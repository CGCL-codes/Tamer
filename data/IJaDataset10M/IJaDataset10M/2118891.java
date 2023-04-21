package fr.soleil.hdbtdbArchivingApi.ArchivingApi.ModesManagement.DbMode;

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

/**
 * @author AYADI
 * 
 */
public interface IDbMode {

    public void updateModeRecord(String attribute_name) throws ArchivingException;
}

package fr.mywiki.model.ejbAdapter.services;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionBean;
import fr.mywiki.business.account.AccountLite;
import fr.mywiki.business.entry.EntryLite;
import fr.mywiki.business.structure.StructureLite;
import fr.mywiki.business.versioning.VersionLite;
import fr.mywiki.model.ejbAdapter.EntityHomeCache;
import fr.mywiki.model.ejbAdapter.ServicesAdapter;
import fr.mywiki.model.ejbAdapter.entity.AccountLocal;
import fr.mywiki.model.ejbAdapter.entity.EntryLocal;
import fr.mywiki.model.ejbAdapter.entity.StructureLocal;
import fr.mywiki.model.ejbAdapter.entity.VersionLocal;
import fr.mywiki.model.ejbAdapter.services.business.GetLinkServiceBusiness;

/**
 * This session bean deals with creating new entity beans.
 * 
 * @ejb.bean name = "GetLinkService"
 *		display-name = "GetLinkService"
 *		description = "GetLink Administration Service"
 *		jndi-name = "ejb/MyWiki/GetLinkService"
 *		remote-business-interface = "fr.mywiki.model.ejbAdapter.services.business.GetLinkServiceBusiness"
 *
 * @ejb.home
 * 		extends = "javax.ejb.EJBHome"
 * 		package = "fr.mywiki.model.ejbAdapter.services"
 *  
 * @ejb.interface
 * 		extends = "javax.ejb.EJBObject"
 * 		package = "fr.mywiki.model.ejbAdapter.services"
 *
 * @author tlombard
 */
public class GetLinkServiceBean extends ServicesAdapter implements GetLinkServiceBusiness, SessionBean {

    /**
	 * The serial version UID.
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Default create method
	 * @throws CreateException
	 * @ejb.create-method 
	 */
    public void ejbCreate() throws CreateException {
    }

    /**
	 * Finds and returns all the <code>Value</code> objects linked to a member via
	 * <tt>relName</tt>.
	 * 
	 * @param accountId a <code>Long</code> object corresponding to the identifier of the account.
	 * @param relName the name of the relation to consider.
	 * @return a <code>Collection</code> of <code>Value</code> objects.
	 * 
	 * @ejb:interface-method view-type="local"
	 */
    public Collection getLinksAccount(Long accountId, String relName) throws FinderException {
        Collection col = new HashSet();
        AccountLocal account = EntityHomeCache.getInstance().getAccountHome().findByPrimaryKey(accountId);
        if (AccountLite.LINK_ACCOUNT_STRUCTURE.equals(relName)) {
            col.add(account.getStructure() != null ? account.getStructure().getStructureLite() : null);
        } else return null;
        return col;
    }

    /**
	 * Finds and returns all the <code>Value</code> objects linked to an entry via <tt>relName</tt>.
	 * 
	 * @param entryId a <code>Long</code> object corresponding to the identifier of the entry.
	 * @param relName the name of the relation to consider.
	 * @return a <code>Collection</code> of <code>Value</code> objects.
	 * 
	 * @ejb:interface-method view-type="local"
	 */
    public Collection getLinksEntry(Long entryId, String relName) throws FinderException {
        Collection col = new HashSet();
        EntryLocal entry = EntityHomeCache.getInstance().getEntryHome().findByPrimaryKey(entryId);
        if (EntryLite.LINK_VERSION_ENTRY.equals(relName)) {
            Iterator itVersions = entry.getVersions().iterator();
            while (itVersions.hasNext()) col.add(((VersionLocal) itVersions.next()).getVersionLite());
        } else return null;
        return col;
    }

    /**
	 * Finds and returns all the <code>Value</code> objects linked to an structure via <tt>relName</tt>.
	 * 
	 * @param structureId a <code>Long</code> object corresponding to the identifier of the structure.
	 * @param relName the name of the relation to consider.
	 * @return a <code>Collection</code> of <code>Value</code> objects.
	 * 
	 * @ejb:interface-method view-type="local"
	 */
    public Collection getLinksStructure(Long structureId, String relName) throws FinderException {
        Collection col = new HashSet();
        StructureLocal structure = EntityHomeCache.getInstance().getStructureHome().findByPrimaryKey(structureId);
        if (StructureLite.LINK_PARENT_TO_CHILDREN.equals(relName)) {
            Iterator itStructures = structure.getChildren().iterator();
            while (itStructures.hasNext()) col.add(((StructureLocal) itStructures.next()).getStructureLite());
        } else if (StructureLite.LINK_CHILD_TO_PARENT.equals(relName)) {
            StructureLocal parent = structure.getParent();
            if (parent != null) col.add(parent.getStructureLite());
        } else if (StructureLite.LINK_ACCOUNT_STRUCTURE.equals(relName)) {
            Iterator itAccounts = structure.getAccounts().iterator();
            while (itAccounts.hasNext()) col.add(((AccountLocal) itAccounts.next()).getAccountLite());
        } else return null;
        return col;
    }

    /**
	 * Finds and returns all the <code>Value</code> objects linked to a version
	 * via <tt>relName</tt>.
	 * 
	 * @param versionId
	 *          a <code>Long</code> object corresponding to the identifier of
	 *          the version.
	 * @param relName
	 *          the name of the relation to consider.
	 * @return a <code>Collection</code> of <code>Value</code> objects.
	 * 
	 * @ejb:interface-method view-type="local"
	 */
    public Collection getLinksVersion(Long versionId, String relName) throws FinderException {
        Collection col = new HashSet();
        VersionLocal version = EntityHomeCache.getInstance().getVersionHome().findByPrimaryKey(versionId);
        if (VersionLite.LINK_VERSION_ENTRY.equals(relName)) {
            col.add(version.getEntry().getEntryLite());
        } else return null;
        return col;
    }
}

package saadadb.meta;

import java.util.LinkedHashMap;
import saadadb.exceptions.FatalException;
import saadadb.query.result.SaadaQLResultSet;

/** * @version $Id: DMInterface.java 118 2012-01-06 14:33:51Z laurent.mistahl $

 * This interface is used by Saada instance to implement datamodels.
 * Classes implementing DMInterface are always inner classes of @SaadaInstance
 * A SaadaInstance contains one DMInterface implementation for each implemented datamodel.
 * Methods of the interface are used as accessors by the SaadaInstance
 * @author michel
 *
 */
public interface DMInterface {

    /**
	 * Returns the name of the instance which is also the name of the class.
	 * This name is derived from the name of the VO Resource implemented by 
	 * that interface
	 * @return
	 */
    public String getDMName() throws FatalException;

    /**
	 * Returns the SQL expression computing the DM field with a nickname or a utype matching
	 * utype_or_nickname
	 * @param utype_or_nickname
	 * @return
	 */
    public String getSQLField(String utype_or_nickname) throws FatalException;

    /**
	 * Return the vakue of the DM field according to the mapping and the loaded value (don't forget to loadbusiness attributes)
	 * @param utype_or_nickname
	 * @return
	 * @throws FatalException
	 */
    public Object getDMFieldValue(String utype_or_nickname) throws FatalException;

    /**
	 * Returns the SQL alias of the DM attribute matching utype.
	 * This alias is actually the nickname of the utypehandler of the DM column
	 * @param utype
	 * @return
	 * @throws FatalException
	 */
    public String getSQLAlias(String utype) throws FatalException;

    /**
	 * Reruens a liked hash map of all DM fields. The map is indexed
	 * on the DM field nicknames
	 * @return
	 */
    public LinkedHashMap<String, String> getSQLFields() throws FatalException;

    /**
	 * Return the DM field value read into the result set
	 * @param utype_or_nickname
	 * @param srs
	 * @return
	 */
    public Object getFieldValue(String utype_or_nickname, SaadaQLResultSet srs) throws FatalException;
}

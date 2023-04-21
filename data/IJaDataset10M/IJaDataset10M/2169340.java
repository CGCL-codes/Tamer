package net.sf.traser.web.client;

import com.google.gwt.user.client.rpc.RemoteService;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import net.sf.traser.databinding.management.Authorize;
import net.sf.traser.databinding.management.Manage;
import net.sf.traser.databinding.meshes.ResolutionRuleBaseDescriptionAnswer;
import net.sf.traser.web.client.remote.Configuration;
import net.sf.traser.web.client.remote.DatabaseSetting;
import net.sf.traser.web.client.remote.Item;
import net.sf.traser.web.client.remote.ItemData;
import net.sf.traser.web.client.remote.Partner;
import net.sf.traser.web.client.remote.PropagatorRule;
import net.sf.traser.web.client.remote.PropertyUpdate;
import net.sf.traser.web.client.remote.ReadEvent;
import net.sf.traser.web.client.remote.ReaderSetting;
import net.sf.traser.web.client.remote.Slot;
import net.sf.traser.web.client.remote.User;

/**
 *
 * @author Marcell Szathmári
 */
public interface TraserService extends RemoteService {

    /**
     * 
     * @return
     */
    public boolean isConfigured();

    /**
     * 
     * @param config 
     * @return
     */
    public boolean configure(Configuration config);

    /**
     * Checks credentials and starts a new session if correct.
     * @param user 
     * @param password
     * @return
     */
    public String login(String user, String password);

    /**
     * Checks whether the session ID is still valid.
     * @param sessID
     * @return
     */
    public boolean isSessionStillValid(String sessID);

    /**
     * Changes the password of the user logged in in the session if the provided
     * old password is correct. Returns true on success, false otherwise.
     * @param sessID 
     * @param oldPass
     * @param newPass
     * @return
     */
    public boolean changePassword(String sessID, String oldPass, String newPass);

    /**
     * Returns the list of supported languages on this server.
     * @return
     */
    public String[] listAvailableLanguages();

    /**
     * Changes the language of the logged in user to the specified one.
     * @param sessID
     * @param language
     * @return
     */
    public boolean changeLanguage(String sessID, String language);

    /**
     * Returns the locale preference of a user.
     * @param sessID
     * @return
     */
    public String getLocale(String sessID);

    /**
     * Lists the registered users of the system.
     * @param sessID
     * @return
     */
    public User[] listUsers(String sessID);

    /**
     * Registers a user and returns a password.
     * @param sessID
     * @param user
     * @param name 
     * @return
     */
    public String addUser(String sessID, String user, String name);

    /**
     * Changes the real name of a user.
     * @param sessID
     * @param user 
     * @param name 
     * @return
     */
    public boolean changeUserRealName(String sessID, String user, String name);

    /**
     * Deletes a user.
     * @param sessID
     * @param user
     * @return
     */
    public boolean deleteUser(String sessID, String user);

    /**
     * Changes the role (user/administrator) of a user to the role specified in
     * the <code>administrator</code> flag.
     * @param sessID
     * @param user
     * @param administrator
     */
    public void changeRole(String sessID, String user, boolean administrator);

    /**
     * 
     * @param sessID
     * @return
     */
    public Partner[] listPartners(String sessID);

    /**
     * Logs the session out of the system.
     * @param sessID the session to end.
     */
    public void logout(String sessID);

    /**
     * Returns the list of items hosted by the server.
     * @param sessID
     * @return the list of items hosted by the server.
     */
    public Item[] listItems(String sessID);

    /**
     * Requests the server to create an identifier.
     * @param sessID
     * @param identifier the requested identifier.
     * @param name the name of the item to display instead of its id on user interfaces.
     * @return true if teh identifier was allocated and false otherwise.
     */
    public Item createItem(String sessID, String identifier, String name);

    /**
     * Generates <code>pieces</code> piece of identifier on the <code>host</code> server.
     * @param sessID
     * @param pieces the number of identifiers to generate.
     * @param host the host to create teh identifiers on.
     * @return the array of the generated identifiers.
     */
    public Item[] requestIDs(String sessID, int pieces, String host);

    /**
     * Gets the name of an item.
     * @param sessID
     * @param itemid
     * @return
     */
    public Item getItem(String sessID, String itemid);

    /**
     * Encodes the string into base64 form.
     * @param decoded
     * @return
     */
    public String encodeBase64(String decoded);

    /**
     * Decodes the string from base64 form.
     * @param encoded
     * @return
     */
    public String decodeBase64(String encoded);

    /**
     * Returns the list of recently used items.
     * @param sessID
     * @return the list of recently used items.
     */
    public Item[] getRecentlyUsedItems(String sessID);

    /**
     * Obtains the values of all of the properties of the item valid between the two dates.
     * @param sessID the session id of the client.
     * @param itemid the id of hte item to query in base64 encoded format.
     * @param on the date when the values of the properties are checked, if it is null, the current values are returned.
     * @return the stored property values.
     */
    public ItemData getItemData(String sessID, String itemid, Date on);

    /**
     * Obtains the values of all of the properties of the item valid between the two dates.
     * @param sessID the session id of the client.
     * @param itemid the id of hte item to query in base64 encoded format.
     * @param from the start of the period of time to query. If greater than the end, then
     * just those values are returned that were valid in the whole period of time.
     * @param to the end of the period of time to query. If equal to the start then those
     * values are returned that were valid on that date.
     * @return the stored property values.
     */
    public ItemData getItemData(String sessID, String itemid, Date from, Date to);

    /**
     * Creates a property for the item.
     * @param sessID
     * @param itemid the id of the item to create the property for.
     * @param propertyName the name of the property to create.
     */
    public void createProperty(String sessID, String itemid, String propertyName);

    /**
     * Adds the value of the
     * @param sessID
     * @param pu
     * @return
     */
    public Boolean addValue(String sessID, PropertyUpdate pu);

    /**
     * Returns the current time.
     * @return the current time.
     */
    public Date getTime();

    /**
     * Removes an endpoint from the set of known endpoints.
     * @param sessID
     * @param endpoint the endpoint to remove.
     */
    public void deleteEndpoint(String sessID, String endpoint);

    /**
     * Adds an endpoint to the set of known endpoints of a partner.
     * @param sessID
     * @param partner the partner to associate the added endpoint with.
     * @param endpoint the endpoint to add.
     */
    public void addEndpoint(String sessID, String partner, String endpoint);

    /**
     * Changes the associated familiar name of a partner.
     * @param sessID
     * @param partner the partner to change the familiar name of.
     * @param familiarName the new familiar name to use.
     */
    public void changePartnerFamiliarName(String sessID, String partner, String familiarName);

    /**
     * A search function that returns a list of items that fulfill the criteria at some point in the specified time
     * period. The criteria are AND-ed together.
     * @param sessID
     * @param from the starting date of the period, or minus infinity if null.
     * @param to the ending date of the period, or plus infinity if null.
     * @param criteria the structure containing the criteria, subject to change, just a quick hack.
     * @return the array of found item descriptors.
     */
    public Item[] search(String sessID, Date from, Date to, PropertyUpdate criteria);

    /**
     * Lists the resolvers registered in the system.
     * @param sessID
     * @return the list of resolvers registered in the system.
     */
    public String[] listResolvers(String sessID);

    /**
     * Returns true if the client is running on the local computer and a reader is attached to it. Returns null if the
     * client is not running on the local computer.
     * @return
     */
    public Boolean shouldCaptureScans();

    /**
     * Retrieves the data read from tags. 
     * @param sessId
     * @return
     */
    public ReadEvent getReadIdentifier(String sessId);

    /**
     * Returns the currently used reader settings.
     * @param sessId
     * @return
     */
    public ReaderSetting[] getReaderSetting(String sessId);

    /**
     * Updates the settings of the reader device.
     * @param sessId
     * @param setting
     */
    public void setReaderSetting(String sessId, ReaderSetting setting);

    /**
     * Returns a template for the setting of the reader driver.
     * @param driver the driver to return the template for.
     * @return the setting template.
     */
    public ReaderSetting getReaderSettingTemplate(String driver);

    /**
     * Returns the settings of the database
     * @param sessId
     * @return
     */
    public DatabaseSetting getDatabaseSetting(String sessId);

    /**
     * Updates the settings of the database.
     * @param sessId
     * @param setting
     */
    public void setDatabaseSetting(String sessId, DatabaseSetting setting);

    /**
     * Invokes a slot on the local server.
     * @param sessID
     * @param s
     * @param message
     * @return
     */
    public Slot.InvokationResult invokeSlot(String sessID, Slot s, String message);

    /**
     * Returns the list of slots supported by teh mesasge catalog of traser.
     * @param sessID
     * @return
     */
    public Slot[] listSlots(String sessID);

    /**
     * Removes a slot definition.
     * @param sessId
     * @param s
     */
    public void removeSlot(String sessId, Slot s);

    /**
     * Updates a slot definition. If it does not exist yet, adds it.
     * @param sessId
     * @param s
     */
    public void updateSlot(String sessId, Slot s);

    /**
     * Obtains the mapping rules of other traser servers for a specific resolver implementation.
     * @param sessId
     * @param resolver the id of the resolver to synchronize the rules of.
     * @return returns the
     */
    public Map<String, ResolutionRuleBaseDescriptionAnswer> getOtherMappingRules(String sessId, String resolver);

    /**
     * Returns the description of the mapping rules of a resolver.
     * @param sessID
     * @param resolver the resolver to return the mapping rules of.
     * @return the description of the mapping rules as defined in <code>RuleBasedResolver</code>.
     */
    public ResolutionRuleBaseDescriptionAnswer listResolverRules(String sessID, String resolver);

    /**
     * Saves an EPC mapping rule in the system.
     * @param sessId
     * @param resolver
     * @param rules
     */
    public void saveResolverRules(String sessId, String resolver, ResolutionRuleBaseDescriptionAnswer rules);

    /**
     * Returns the description of authorization of a property of an item. (Only for locally administered items.)
     * @param sessId
     * @param itemid
     * @param property
     * @return
     */
    public Authorize listAuthorization(String sessId, String itemid, String property);

    /**
     * Updates the authorization rules of a property of an item.
     * @param sessId
     * @param rules
     */
    public void updateAuthorization(String sessId, Authorize rules);

    /**
     * Instructs the server to duplicate/override/augment a property of an item locally.
     * @param sessId
     * @param itemId the item to duplicate the property of.
     * @param property the
     * @param level
     */
    public void manage(String sessId, String itemId, String property, Manage.Property.Level level);

    /**
     * Returns the list of defined propagator rules.
     * @param sessId
     * @return
     */
    public PropagatorRule[] listPropagationRules(String sessId);

    /**
     * Returns a description of the available propagators and their parameters.
     * @return
     */
    public Map<String, Collection<String>> listAvailablePropagators();

    /**
     * Returns a description fo the available guard expressions and their parameters.
     * @return
     */
    public Map<String, Collection<String>> listAvailableGuards();

    /**
     * Saves the propagator rule.
     * @param sessId
     * @param p
     */
    public void addPropapagationRule(String sessId, PropagatorRule p);

    /**
     * Deletes the propagator rule.
     * @param sessId
     * @param p
     */
    public void deletePropapagationRule(String sessId, PropagatorRule p);

    /**
     * Changes the propagator rule parameters.
     * @param sessId
     * @param p
     */
    public void changePropagatorParams(String sessId, PropagatorRule p);

    /**
     * Adds a guard expression to a propagator rule.
     * @param sessId
     * @param p
     * @param g
     */
    public void addPropagatorGuardExpression(String sessId, PropagatorRule p, PropagatorRule.Guard g);

    /**
     * Changes the parameters of a guard expression of a propagator.
     * @param sessId
     * @param p
     * @param g
     */
    public void changePropagatorGuardExpressionParameters(String sessId, PropagatorRule p, PropagatorRule.Guard g);

    /**
     * Deletes a guard expression of a propagator rule.
     * @param sessId
     * @param p
     * @param g
     */
    public void deletePropagatorGuardExpression(String sessId, PropagatorRule p, PropagatorRule.Guard g);

    /**
     * Obtains the values of all of the properties of the item valid between the two dates.
     * @param sessID the session id of the client.
     * @param itemids the id of hte item to query in base64 encoded format.
     * @param on the date when the values of the properties are checked, if it is null, the current values are returned.
     * @param props 
     * @return the stored property values.
     */
    public ItemData[] bulkGetItemData(String sessID, Date on, String[] itemids, String[] props);
}

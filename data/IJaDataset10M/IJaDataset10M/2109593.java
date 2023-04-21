package org.gbif.checklistbank.service;

import org.gbif.checklistbank.model.Name;
import org.gbif.ecat.voc.NameType;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

public interface NameStringService extends CRUDService<Name> {

    /**
   */
    public void blacklistNames(Collection<String> names);

    /**
   * Insert names in a batch also inserting the canonicals and parsed names just as nameStringToId() would do for a
   * single name
   */
    public void insertNames(Set<String> names);

    /**
   * Checks if a given name id is blacklisted
   *
   * @param nameId the name id to test
   *
   * @return true if name is blacklisted
   */
    boolean isBlacklisted(Integer nameId);

    /**
   * Checks if a given name and is blacklisted, case insensitive name comparison
   *
   * @param nameString the name to test
   *
   * @return true if name is blacklisted
   */
    public boolean isBlacklisted(String nameString);

    /**
   * Get ID for namestring inserting a new namestring if not yet existing
   *
   * @param nameString entire string of full name
   * @param parseName  if false no parsed name and canonical will be inserted
   */
    public Integer nameStringToId(String nameString, boolean parseName);

    /**
   * Reloads the blacklist which is cached in memory from the datadir & names table
   */
    public void reloadBlacklist();

    /**
   * Search name strings for matches optionally restricting the search to some names based on their
   *
   * @param q        the like query string including any potential % or ?
   * @param type     optional name type to restrict search to. If null will search all names but blacklisted ones
   * @param page     the page number to select starting with 1 for the first page
   * @param pagesize the number of names returned
   */
    public List<Name> search(@Nullable String q, @Nullable NameType type, int page, int pagesize);

    /**
   * Counts the total records machted for a search
   *
   * @param q    the like query string including any potential % or ?
   * @param type optional name type to restrict search to. If null will search all names but blacklisted ones
   *
   * @return number of total unique name strings matched
   */
    public int searchCount(@Nullable String q, @Nullable NameType type);
}

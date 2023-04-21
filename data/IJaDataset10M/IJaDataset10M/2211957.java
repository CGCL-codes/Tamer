package net.sourceforge.ondex.workflow2_old;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * A class to handle aliasises that may be used instead of the UUIDs by the
 * users of the workflow API
 * @author lysenkoa
 *
 */
public class AliasHandler {

    protected final Map<String, UUID> rForwardLookup = new HashMap<String, UUID>();

    protected final Map<UUID, Set<String>> rReverseLookup = new HashMap<UUID, Set<String>>();

    public AliasHandler() {
    }

    public void addAlias(UUID id, String alias) {
        rForwardLookup.put(alias, id);
        Set<String> existingAliases = rReverseLookup.get(id);
        if (existingAliases == null) {
            existingAliases = new HashSet<String>();
            rReverseLookup.put(id, existingAliases);
        }
        existingAliases.add(alias);
    }

    public Set<String> getAliases(UUID id) {
        return rReverseLookup.get(id);
    }

    public UUID getUUID(String alias) {
        return rForwardLookup.get(alias);
    }

    /**
	 * Remove the uuid assosiated with this alias
	 * @param alias - one of the aliases assosiated with the uuid
	 * @return the uuid
	 */
    public UUID unregister(String alias) {
        UUID id = rForwardLookup.remove(alias);
        if (id == null) return null;
        rReverseLookup.remove(id);
        return id;
    }

    /**
	 * Remove the uuid and all aliases
	 * @param id - uuid 
	 */
    public void unregister(UUID id) {
        Set<String> aliases = rReverseLookup.remove(id);
        if (aliases == null) return;
        for (String alias : aliases) {
            rForwardLookup.remove(alias);
        }
    }

    public void clear() {
        rForwardLookup.clear();
        rReverseLookup.clear();
    }

    public boolean isInUse(String alias) {
        return rForwardLookup.containsKey(alias);
    }
}

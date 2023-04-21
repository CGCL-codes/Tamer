package log4j.app;

import prisms.logging.LogEntrySearch;

/** A named log search saved in the user's environment */
public class NamedSearch {

    private String theName;

    private prisms.util.Search theSearch;

    /**
	 * @param name The name of the search
	 * @param search The log search
	 */
    public NamedSearch(String name, prisms.util.Search search) {
        theName = name;
        theSearch = search;
    }

    /** @return The name of the search */
    public String getName() {
        return theName;
    }

    /** @return The search */
    public prisms.util.Search getSearch() {
        return theSearch;
    }

    /** Serializes named searches */
    public static class Serializer implements prisms.util.persisters.SerializablePropertyPersister.PropertySerializer<NamedSearch> {

        private prisms.arch.PrismsEnv theEnv;

        public void configure(prisms.arch.PrismsConfig config, prisms.arch.PrismsEnv env, prisms.arch.event.PrismsProperty<? super NamedSearch[]> property) {
            theEnv = env;
        }

        public String getName(NamedSearch property) {
            return property.getName();
        }

        public String serialize(NamedSearch property) {
            if (property.getSearch() == null) return "";
            return property.getSearch().toString();
        }

        public NamedSearch deserialize(String name, String serialized) {
            prisms.util.Search srch;
            if (name.length() == 0) srch = null; else srch = new LogEntrySearch.LogEntrySearchBuilder(theEnv).createSearch(serialized);
            return new NamedSearch(name, srch);
        }
    }
}

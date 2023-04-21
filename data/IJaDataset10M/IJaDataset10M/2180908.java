package fr.mywiki.business.profil;

import fr.mywiki.business.navigation.SpacesList;
import fr.mywiki.model.ValueInterface;

public class Profile implements ValueInterface {

    private static final long serialVersionUID = 1L;

    /** The type of object */
    public static final String TYPE_ENT = "profile";

    public static final Long ADMIN_PROFILE_ID = new Long(1);

    public static final Long AUTHOR_PROFILE_ID = new Long(2);

    public static final Long ANONYMOUS_PROFILE_ID = new Long(0);

    public static final String FIELD_NAME = "name";

    public static final String FIELD_SPACES = "spaces";

    public static final Profile ADMIN = new Profile(ADMIN_PROFILE_ID, "admin", SpacesList.getSpacesList(ADMIN_PROFILE_ID));

    public static final Profile AUTHOR = new Profile(AUTHOR_PROFILE_ID, "author", SpacesList.getSpacesList(AUTHOR_PROFILE_ID));

    public static final Profile ANONYMOUS = new Profile(ANONYMOUS_PROFILE_ID, "anonymous", SpacesList.getSpacesList(ANONYMOUS_PROFILE_ID));

    /** Unique identifier of the profile. Will be useful when the profiles will be generic. */
    private Long id;

    /** Name of that profile */
    private String name;

    /** Spaces visible by a user with that profile */
    private SpacesList spaces;

    /** Inaccessible builder */
    private Profile() {
    }

    public Profile(Long id, String name, SpacesList spaces) {
        this.id = id;
        this.name = name;
        this.spaces = spaces;
    }

    public String getName() {
        return name;
    }

    public SpacesList getSpaces() {
        return spaces;
    }

    public Long getId() {
        return id;
    }

    /**
	 * Returns the specific key which can be used to display this object on a jsp file.
	 * 
	 * @return a <code>String</code> object.
	 */
    public String getKey() {
        return "profile." + id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpaces(SpacesList spaces) {
        this.spaces = spaces;
    }

    public String getTypeEnt() {
        return TYPE_ENT;
    }

    public Object get(String attName) {
        if (FIELD_NAME.equals(attName)) return getName();
        if (FIELD_SPACES.equals(attName)) return getSpaces();
        return null;
    }

    public void set(String attName, Object value) {
    }
}

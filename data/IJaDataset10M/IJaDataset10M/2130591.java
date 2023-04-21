package your.app.gwt.eo;

import java.util.*;
import java.math.BigDecimal;
import com.webobjects.eocontrol.EOGlobalID;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSKeyValueCodingAdditions;
import com.webobjects.foundation.NSTimestamp;
import wogwt.translatable.rpc.WOGWTClientEOImpl;

public abstract class _TalentClient extends WOGWTClientEOImpl {

    public static final transient String ENTITY_NAME = "Talent";

    public static final String FIRST_NAME_KEY = "firstName";

    public static final String LAST_NAME_KEY = "lastName";

    public static final String MOVIES_DIRECTED_KEY = "moviesDirected";

    public static final String PHOTO_KEY = "photo";

    public static final String ROLES_KEY = "roles";

    private String firstName;

    private String lastName;

    private your.app.gwt.eo.TalentPhotoClient photo;

    private NSArray<your.app.gwt.eo.MovieClient> moviesDirected;

    private NSArray<your.app.gwt.eo.MovieRoleClient> roles;

    public _TalentClient() {
        super();
        moviesDirected = NSArray.EmptyArray;
        roles = NSArray.EmptyArray;
    }

    public _TalentClient(NSDictionary<String, Object> snapshot) {
        super(snapshot);
        if (moviesDirected == null) moviesDirected = NSArray.EmptyArray;
        if (roles == null) roles = NSArray.EmptyArray;
    }

    public NSArray<String> attributeKeys() {
        NSArray<String> keys = new NSArray<String>(new String[] { "firstName", "lastName" });
        return keys;
    }

    public NSArray<String> toOneRelationshipKeys() {
        NSArray<String> keys = new NSArray<String>(new String[] { "photo" });
        return keys;
    }

    public NSArray<String> toManyRelationshipKeys() {
        NSArray<String> keys = new NSArray<String>(new String[] { "moviesDirected", "roles" });
        return keys;
    }

    public int deleteRuleForRelationshipKey(String relationshipKey) {
        if ("moviesDirected".equals(relationshipKey)) return deleteRuleNumber("EODeleteRuleNullify");
        if ("photo".equals(relationshipKey)) return deleteRuleNumber("EODeleteRuleCascade");
        if ("roles".equals(relationshipKey)) return deleteRuleNumber("EODeleteRuleDeny");
        return -1;
    }

    public String inverseForRelationshipKey(String relationshipKey) {
        if ("photo".equals(relationshipKey)) return "talent";
        if ("roles".equals(relationshipKey)) return "talent";
        return null;
    }

    public boolean isReadOnly() {
        return false;
    }

    public boolean ownsDestinationObjectsForRelationshipKey(String relationshipKey) {
        if ("photo".equals(relationshipKey)) return true;
        return false;
    }

    public NSDictionary<String, Object> snapshot() {
        NSMutableDictionary<String, Object> map = new NSMutableDictionary<String, Object>();
        map.put("__globalID", __globalID() == null ? NSKeyValueCoding.NullValue : __globalID());
        map.put("isFault", isFault());
        map.put("firstName", firstName == null ? NSKeyValueCoding.NullValue : firstName);
        map.put("lastName", lastName == null ? NSKeyValueCoding.NullValue : lastName);
        map.put("photo", photo == null ? NSKeyValueCoding.NullValue : photo);
        map.put("moviesDirected", ((NSArray) moviesDirected).mutableClone());
        map.put("roles", ((NSArray) roles).mutableClone());
        return map;
    }

    public Object valueForKey(String key) {
        if ("firstName".equals(key)) {
            return firstName();
        }
        if ("lastName".equals(key)) {
            return lastName();
        }
        if ("photo".equals(key)) {
            return photo();
        }
        if ("moviesDirected".equals(key)) {
            return moviesDirected();
        }
        if ("roles".equals(key)) {
            return roles();
        }
        if ("__globalID".equals(key)) {
            return __globalID();
        }
        if ("isFault".equals(key)) {
            return isFault();
        }
        return handleQueryWithUnboundKey(key);
    }

    public void takeValueForKey(Object value, String key) {
        if ("firstName".equals(key)) {
            setFirstName((String) value);
            return;
        }
        if ("lastName".equals(key)) {
            setLastName((String) value);
            return;
        }
        if ("photo".equals(key)) {
            setPhoto((your.app.gwt.eo.TalentPhotoClient) value);
            return;
        }
        if ("moviesDirected".equals(key)) {
            setMoviesDirected((NSArray) value);
            return;
        }
        if ("roles".equals(key)) {
            setRoles((NSArray) value);
            return;
        }
        if ("__globalID".equals(key)) {
            setGlobalID((EOGlobalID) value);
            return;
        }
        if ("isFault".equals(key)) {
            setIsFault((Boolean) value);
            return;
        }
        handleTakeValueForUnboundKey(value, key);
    }

    public String entityName() {
        return "Talent";
    }

    public String firstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String lastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public your.app.gwt.eo.TalentPhotoClient photo() {
        return photo;
    }

    public void setPhoto(your.app.gwt.eo.TalentPhotoClient photo) {
        this.photo = photo;
    }

    public NSArray<your.app.gwt.eo.MovieClient> moviesDirected() {
        return moviesDirected;
    }

    public void setMoviesDirected(NSArray<your.app.gwt.eo.MovieClient> moviesDirected) {
        this.moviesDirected = moviesDirected;
    }

    public void addToMoviesDirectedRelationship(your.app.gwt.eo.MovieClient object) {
        moviesDirected.add(object);
    }

    public void removeFromMoviesDirectedRelationship(your.app.gwt.eo.MovieClient object) {
        moviesDirected.remove(object);
    }

    public your.app.gwt.eo.MovieClient createMoviesDirectedRelationship() {
        your.app.gwt.eo.MovieClient result = new your.app.gwt.eo.MovieClient();
        moviesDirected.add(result);
        return result;
    }

    public NSArray<your.app.gwt.eo.MovieRoleClient> roles() {
        return roles;
    }

    public void setRoles(NSArray<your.app.gwt.eo.MovieRoleClient> roles) {
        this.roles = roles;
    }

    public void addToRolesRelationship(your.app.gwt.eo.MovieRoleClient object) {
        roles.add(object);
    }

    public void removeFromRolesRelationship(your.app.gwt.eo.MovieRoleClient object) {
        roles.remove(object);
    }

    public your.app.gwt.eo.MovieRoleClient createRolesRelationship() {
        your.app.gwt.eo.MovieRoleClient result = new your.app.gwt.eo.MovieRoleClient();
        roles.add(result);
        return result;
    }
}

package net.bnubot.db.auto;

import java.util.List;
import net.bnubot.db.CommandAlias;
import net.bnubot.db.CustomDataObject;
import net.bnubot.db.Rank;

/**
 * Class _Command was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 * @author cayenne-generated-file
 */
@SuppressWarnings("serial")
public abstract class _Command extends CustomDataObject {

    public static final String CMDGROUP_PROPERTY = "cmdgroup";

    public static final String DESCRIPTION_PROPERTY = "description";

    public static final String NAME_PROPERTY = "name";

    public static final String ALIASES_PROPERTY = "aliases";

    public static final String RANK_PROPERTY = "rank";

    public static final String ID_PK_COLUMN = "id";

    public void setCmdgroup(String cmdgroup) {
        writeProperty(CMDGROUP_PROPERTY, cmdgroup);
    }

    public String getCmdgroup() {
        return (String) readProperty(CMDGROUP_PROPERTY);
    }

    public void setDescription(String description) {
        writeProperty(DESCRIPTION_PROPERTY, description);
    }

    public String getDescription() {
        return (String) readProperty(DESCRIPTION_PROPERTY);
    }

    public void setName(String name) {
        writeProperty(NAME_PROPERTY, name);
    }

    public String getName() {
        return (String) readProperty(NAME_PROPERTY);
    }

    public void addToAliases(CommandAlias obj) {
        addToManyTarget(ALIASES_PROPERTY, obj, true);
    }

    public void removeFromAliases(CommandAlias obj) {
        removeToManyTarget(ALIASES_PROPERTY, obj, true);
    }

    @SuppressWarnings("unchecked")
    public List<CommandAlias> getAliases() {
        return (List<CommandAlias>) readProperty(ALIASES_PROPERTY);
    }

    public void setRank(Rank rank) {
        setToOneTarget(RANK_PROPERTY, rank, true);
    }

    public Rank getRank() {
        return (Rank) readProperty(RANK_PROPERTY);
    }
}

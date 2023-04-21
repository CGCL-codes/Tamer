package net.sf.gham.core.entity.match.matchtablecolumn;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.swing.table.TableCellRenderer;
import net.sf.gham.core.entity.match.IMatch;
import net.sf.gham.swing.table.column.ColumnSavable;
import net.sf.jtwa.Messages;
import org.apache.log4j.Logger;

/**
 *
 * @author  fabio
 */
public class MatchTeamTableColumn extends ColumnSavable<IMatch> {

    protected String fieldName;

    protected Method homeMethod;

    protected Method awayMethod;

    private Object[] extraParameter;

    private Class<?>[] extraParameterClass;

    private boolean isMyTeam;

    /** Creates a new instance of MatchTeamTableColumn */
    public MatchTeamTableColumn(String name, String key, String fieldName) {
        this(name, key, fieldName, true);
    }

    /** Creates a new instance of MatchTeamTableColumn */
    public MatchTeamTableColumn(String name, String key, String fieldName, TableCellRenderer cellRenderer) {
        this(name, key, fieldName, cellRenderer, true);
    }

    /** Creates a new instance of MatchTeamTableColumn */
    public MatchTeamTableColumn(String name, String key, String fieldName, Class<?> extraParameterClass, Object extraParameter) {
        this(name, key, fieldName, new Class[] { extraParameterClass }, new Object[] { extraParameter }, true);
    }

    /** Creates a new instance of MatchTeamTableColumn */
    public MatchTeamTableColumn(String name, String key, String fieldName, TableCellRenderer cellRenderer, Class<?> extraParameterClass, Object extraParameter) {
        this(name, key, fieldName, cellRenderer, new Class[] { extraParameterClass }, new Object[] { extraParameter }, true);
    }

    /** Creates a new instance of MatchTeamTableColumn */
    public MatchTeamTableColumn(String name, String key, String fieldName, boolean isMyTeam) {
        super(key);
        this.name = getName(name, isMyTeam);
        this.fieldName = fieldName;
        retrieveMethods(fieldName, isMyTeam);
    }

    /** Creates a new instance of MatchTeamTableColumn */
    public MatchTeamTableColumn(String name, String key, String fieldName, TableCellRenderer cellRenderer, boolean isMyTeam) {
        super(key, cellRenderer);
        this.name = getName(name, isMyTeam);
        this.fieldName = fieldName;
        retrieveMethods(fieldName, isMyTeam);
    }

    /** Creates a new instance of MatchTeamTableColumn */
    public MatchTeamTableColumn(String name, String key, String fieldName, Class<?>[] extraParameterClass, Object[] extraParameter, boolean isMyTeam) {
        super(key);
        this.name = getName(name, isMyTeam);
        this.fieldName = fieldName;
        this.extraParameter = extraParameter;
        retrieveMethods(fieldName, extraParameterClass, isMyTeam);
    }

    /** Creates a new instance of MatchTeamTableColumn */
    public MatchTeamTableColumn(String name, String key, String fieldName, TableCellRenderer cellRenderer, Class<?>[] extraParameterClass, Object[] extraParameter, boolean isMyTeam) {
        super(key, cellRenderer);
        this.name = getName(name, isMyTeam);
        this.fieldName = fieldName;
        this.extraParameter = extraParameter;
        retrieveMethods(fieldName, extraParameterClass, isMyTeam);
    }

    protected static final String opponent = Messages.getString("Opponent");

    protected static final String opponentShort = Messages.getString("Opponent_short");

    protected String getName(String name, boolean isTeam) {
        if (!isTeam) return opponent + " - " + name;
        return name;
    }

    protected String getNameShort(String name, boolean isTeam) {
        if (!isTeam) return opponentShort + name;
        return name;
    }

    private void retrieveMethods(String fieldName, Class<?>[] extraParameterClass, boolean isMyTeam) {
        this.extraParameterClass = extraParameterClass;
        this.isMyTeam = isMyTeam;
        try {
            homeMethod = IMatch.class.getMethod((isMyTeam ? "getHome" : "getAway") + fieldName, extraParameterClass);
            awayMethod = IMatch.class.getMethod((isMyTeam ? "getAway" : "getHome") + fieldName, extraParameterClass);
        } catch (NoSuchMethodException e) {
            Logger.getRootLogger().error("Error while retrieving field " + fieldName + " of class Match", e);
            homeMethod = null;
        }
    }

    private void retrieveMethods(String fieldName, boolean isMyTeam) {
        this.isMyTeam = isMyTeam;
        try {
            homeMethod = IMatch.class.getMethod((isMyTeam ? "getHome" : "getAway") + fieldName);
            awayMethod = IMatch.class.getMethod((isMyTeam ? "getAway" : "getHome") + fieldName);
        } catch (NoSuchMethodException e) {
            Logger.getRootLogger().error("Error while retrieving field " + fieldName + " of class Match", e);
            homeMethod = null;
        }
    }

    @Override
    public Object getValueAt(IMatch match, Object param) {
        if (match == null || param == null) {
            return null;
        }
        Method m;
        if (param.equals(match.getHomeTeamId())) {
            m = homeMethod;
        } else {
            m = awayMethod;
        }
        try {
            if (extraParameter == null) {
                return m.invoke(match);
            } else {
                return m.invoke(match, extraParameter);
            }
        } catch (IllegalAccessException e) {
            Logger.getRootLogger().error("Error while retrieving value of match " + match.getMatchID(), e);
            return null;
        } catch (InvocationTargetException e) {
            Logger.getRootLogger().error("Error while retrieving value of match " + match.getMatchID(), e);
            return null;
        }
    }

    @Override
    public Class<? extends Object> getClassOfColumn() {
        if (homeMethod == null) {
            return null;
        }
        return homeMethod.getReturnType();
    }

    @Override
    public MatchTeamTableColumn clone(int width) {
        MatchTeamTableColumn r;
        if (extraParameter == null) r = new MatchTeamTableColumn(getName(), getKey(), fieldName, isMyTeam); else r = new MatchTeamTableColumn(getName(), getKey(), fieldName, extraParameterClass, extraParameter, isMyTeam);
        cloneProt(r, width);
        return r;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MatchTeamTableColumn) {
            MatchTeamTableColumn o = (MatchTeamTableColumn) obj;
            return name.equals(o.name) && getKey().equals(o.getKey()) && fieldName.equals(o.fieldName);
        }
        return false;
    }
}

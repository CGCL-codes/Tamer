package net.sf.logsaw.dialect.websphere;

import java.util.ArrayList;
import java.util.List;
import net.sf.logsaw.core.dialect.support.ALogLevelProvider;
import net.sf.logsaw.core.field.Level;

/**
 * @author Philipp Nanz
 */
public final class WebsphereLevelProvider extends ALogLevelProvider {

    public static final Level LEVEL_STDOUT = new Level(1, "STDOUT");

    public static final Level LEVEL_STDERR = new Level(2, "STDERR");

    public static final Level LEVEL_AUDIT = new Level(3, "AUDIT");

    public static final Level LEVEL_INFO = new Level(4, "INFO");

    public static final Level LEVEL_WARN = new Level(5, "WARN");

    public static final Level LEVEL_ERROR = new Level(6, "ERROR");

    public static final Level LEVEL_FATAL = new Level(7, "FATAL");

    @Override
    protected String doRewriteAlias(String name) {
        if ("O".equals(name)) {
            return LEVEL_STDOUT.getName();
        } else if ("R".equals(name)) {
            return LEVEL_STDERR.getName();
        } else if ("A".equals(name)) {
            return LEVEL_AUDIT.getName();
        } else if ("I".equals(name)) {
            return LEVEL_INFO.getName();
        } else if ("W".equals(name)) {
            return LEVEL_WARN.getName();
        } else if ("E".equals(name)) {
            return LEVEL_ERROR.getName();
        } else if ("F".equals(name)) {
            return LEVEL_FATAL.getName();
        }
        return super.doRewriteAlias(name);
    }

    @Override
    public List<Level> getLevels() {
        List<Level> ret = new ArrayList<Level>();
        ret.add(LEVEL_STDOUT);
        ret.add(LEVEL_STDERR);
        ret.add(LEVEL_AUDIT);
        ret.add(LEVEL_INFO);
        ret.add(LEVEL_WARN);
        ret.add(LEVEL_ERROR);
        ret.add(LEVEL_FATAL);
        return ret;
    }

    @Override
    public String getIconPathForLevel(Level lvl) {
        String icon = null;
        if (LEVEL_STDOUT.equals(lvl)) {
            icon = "lvl_stdout.gif";
        } else if (LEVEL_STDERR.equals(lvl)) {
            icon = "lvl_stderr.gif";
        } else if (LEVEL_AUDIT.equals(lvl)) {
            icon = "lvl_audit.gif";
        } else if (LEVEL_INFO.equals(lvl)) {
            icon = "lvl_info.gif";
        } else if (LEVEL_WARN.equals(lvl)) {
            icon = "lvl_warn.gif";
        } else if (LEVEL_ERROR.equals(lvl)) {
            icon = "lvl_error.gif";
        } else if (LEVEL_FATAL.equals(lvl)) {
            icon = "lvl_fatal.gif";
        }
        if (icon == null) {
            return null;
        }
        return "icons/" + icon;
    }
}

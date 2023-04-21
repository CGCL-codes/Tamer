package org.netxilia.api.model;

/**
 * This class represents an alias for a AreaReference in a sheet. An alias can contain an optional sheet name. If that
 * is null, than it's considered to be relative alias to the current sheet.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class AbsoluteAlias {

    private final String sheetName;

    private final Alias alias;

    public AbsoluteAlias(String alias) {
        int pos = alias.indexOf('!');
        if (pos < 0) {
            throw new IllegalArgumentException(alias + " is not a valid alias. Correct format is sheetName!alias");
        } else {
            this.sheetName = alias.substring(0, pos);
            this.alias = new Alias(alias.substring(pos + 1));
        }
    }

    public AbsoluteAlias(String sheetName, Alias alias) {
        this.sheetName = sheetName;
        this.alias = alias;
    }

    public AbsoluteAlias withRelativeSheetName(String baseSheetName) {
        if (SheetFullName.ALIAS_MAIN_SHEET.equals(sheetName)) {
            String relativeName = SheetFullName.relativeName(baseSheetName, sheetName, null);
            return new AbsoluteAlias(relativeName, alias);
        }
        return this;
    }

    public String getSheetName() {
        return sheetName;
    }

    public Alias getAlias() {
        return alias;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((alias == null) ? 0 : alias.hashCode());
        result = prime * result + ((sheetName == null) ? 0 : sheetName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbsoluteAlias other = (AbsoluteAlias) obj;
        if (alias == null) {
            if (other.alias != null) {
                return false;
            }
        } else if (!alias.equals(other.alias)) {
            return false;
        }
        if (sheetName == null) {
            if (other.sheetName != null) {
                return false;
            }
        } else if (!sheetName.equals(other.sheetName)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return sheetName + "!" + alias;
    }

    public static boolean isAbsoluteAlias(String text) {
        if (text == null) {
            return false;
        }
        int pos = text.indexOf('!');
        if (pos < 0) {
            return false;
        }
        return Alias.isAlias(text.substring(pos + 1));
    }
}

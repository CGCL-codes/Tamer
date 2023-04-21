package net.sourceforge.ondex.core.sql2.deleted.metadata;

import net.sourceforge.ondex.core.AttributeName;
import net.sourceforge.ondex.core.Hierarchy;
import net.sourceforge.ondex.core.MetaData;
import net.sourceforge.ondex.core.Unit;

public class DeletedAttributeName implements AttributeName {

    private long sid;

    private String id;

    public DeletedAttributeName(long s, String i) {
        sid = s;
        id = i;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public long getSID() {
        return sid;
    }

    @Override
    public Class<?> getDataType() {
        return null;
    }

    @Override
    public String getDataTypeAsString() {
        return null;
    }

    @Override
    public AttributeName getSpecialisationOf() {
        return null;
    }

    @Override
    public Unit getUnit() {
        return null;
    }

    @Override
    public void setSpecialisationOf(AttributeName specialisationOf) {
    }

    @Override
    public void setUnit(Unit unit) {
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getFullname() {
        return null;
    }

    @Override
    public void setDescription(String description) {
    }

    @Override
    public void setFullname(String fullname) {
    }

    @Override
    public int compareTo(MetaData o) {
        return 0;
    }

    @Override
    public boolean isAssignableTo(AttributeName possibleAncestor) {
        return Hierarchy.Helper.transitiveParent(possibleAncestor, this);
    }

    @Override
    public boolean isAssignableFrom(AttributeName possibleDescendant) {
        return Hierarchy.Helper.transitiveParent(this, possibleDescendant);
    }
}

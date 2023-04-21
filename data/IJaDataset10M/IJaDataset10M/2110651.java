package genj.edit.actions;

import genj.gedcom.Entity;
import genj.gedcom.Gedcom;
import genj.gedcom.GedcomException;
import genj.gedcom.Indi;
import genj.gedcom.Property;
import genj.gedcom.PropertyAlias;

/**
 * Create an alias between records of two individuals indicating that the person is the same
 */
public class CreateAlias extends CreateRelationship {

    private Indi source;

    /** constructor */
    public CreateAlias(Indi source) {
        super(resources.getString("create.alias"), source.getGedcom(), Gedcom.INDI);
        this.source = source;
    }

    /** description of what this does */
    public String getDescription() {
        return resources.getString("create.alias.of", source.toString());
    }

    /** perform the change */
    protected Property change(Entity target, boolean targetIsNew) throws GedcomException {
        PropertyAlias alias = (PropertyAlias) source.addProperty("ALIA", '@' + target.getEntity().getId() + '@');
        try {
            alias.link();
        } catch (GedcomException e) {
            source.delProperty(alias);
            throw e;
        }
        return alias;
    }
}

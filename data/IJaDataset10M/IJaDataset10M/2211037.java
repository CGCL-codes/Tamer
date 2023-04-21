package org.vardb.web.json;

import java.util.ArrayList;
import java.util.List;
import org.vardb.resources.dao.CFamily;
import org.vardb.resources.dao.COrtholog;

public class COrthologJson extends CAbstractJson {

    protected int id;

    protected String identifier;

    protected String name;

    protected String description;

    protected List<Family> families = new ArrayList<Family>();

    public COrthologJson(COrtholog ortholog) {
        this.id = ortholog.getId();
        this.identifier = ortholog.getIdentifier();
        this.name = ortholog.getName();
        this.description = ortholog.getDescription();
        for (CFamily family : ortholog.getFamilies()) {
            this.families.add(new Family(family));
        }
    }
}

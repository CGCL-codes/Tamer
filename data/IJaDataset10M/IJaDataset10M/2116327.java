package org.melati.poem;

/**
 * A possible origin for metadata for a {@link Field}, {@link Column} 
 * or {@link Table}.
 *
 * The definition sources are unified during Poem initialisation.
 *
 * @author WilliamC@paneris.org
 *
 * @see Database#unifyWithDB
 * @see Table#unifyWithDB
 * @see Column#unifyWithIndex
 *
 */
public class DefinitionSource {

    private String name;

    public DefinitionSource(Object what) {
        this.name = what.toString();
    }

    public String toString() {
        return name;
    }

    public static final DefinitionSource dsd = new DefinitionSource("the data structure definition"), infoTables = new DefinitionSource("the data dictionary"), sqlMetaData = new DefinitionSource("the JDBC metadata"), runtime = new DefinitionSource("the running application");
}

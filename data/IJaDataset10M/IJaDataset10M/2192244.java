package pt.isel.meic.agendaagent.ontology;

/**
* Represents a logically nested entry. For example, a <gd:who> representing a contact might have a nested entry from a contact feed.
* Protege name: EntryLink
* @author OntologyBeanGenerator v4.1
* @version 2011/09/1, 01:51:12
*/
public interface EntryLink extends jade.content.Concept {

    /**
   * Specifies the relationship between the containing entity and the contained location. Possible values (see below) are defined by other elements. For example, <gd:when> defines http://schemas.google.com/g/2005#event.
   * Protege name: prop:rel
   */
    public void setProp_rel(Relationship value);

    public Relationship getProp_rel();

    /**
   * Contents of the entry.
   * Protege name: prop:entry
   */
    public void setProp_entry(String value);

    public String getProp_entry();

    /**
   * Specifies the entry URI. If the nested entry is embedded and not linked, this attribute may be omitted.
   * Protege name: prop:href
   */
    public void setProp_href(String value);

    public String getProp_href();

    /**
   * Specifies whether the contained entry is read-only. The default value is "false".
   * Protege name: prop:readOnly
   */
    public void setProp_readOnly(boolean value);

    public boolean getProp_readOnly();
}

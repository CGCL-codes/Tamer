package es.caib.zkib.datamodel.xml.definition;

import java.util.HashMap;
import java.util.Vector;
import org.w3c.dom.Element;
import es.caib.zkib.datamodel.xml.ParseException;
import es.caib.zkib.datamodel.xml.handler.PersistenceHandler;

public class NodeDefinition implements DefinitionInterface {

    String name;

    HashMap finders = new HashMap();

    Vector persistencers = new Vector();

    public NodeDefinition() {
        super();
    }

    public PersistenceHandler[] getPersistenceHandlers() {
        return (PersistenceHandler[]) persistencers.toArray(new PersistenceHandler[0]);
    }

    public void add(PersistenceHandler handler) {
        persistencers.add(handler);
    }

    public FinderDefinition getFinder(String name) {
        return (FinderDefinition) finders.get(name);
    }

    public FinderDefinition[] getFinders() {
        return (FinderDefinition[]) finders.values().toArray(new FinderDefinition[0]);
    }

    public void add(FinderDefinition finder) {
        finders.put(finder.getName(), finder);
    }

    /**
	 * @return Returns the name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name The name to set.
	 */
    public void setName(String name) {
        this.name = name;
    }

    public void test(Element element) throws ParseException {
        if (name == null) throw new ParseException("No name especified for datanode", element);
    }
}

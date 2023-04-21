package mimosa.ontology;

import mimosa.table.DefaultInheritanceTableDeclaration;
import mimosa.table.classeditor.ClassDescription;

/**
 *
 * @author Jean-Pierre Muller
 */
public class InheritanceParameterDeclaration extends DefaultInheritanceTableDeclaration<String, ClassDescription> {

    /**
	 * 
	 */
    public InheritanceParameterDeclaration() {
        super();
        setKeyObject(new String(""));
        setTypeObject(new ClassDescription(null));
    }
}

package net.sourceforge.nrl.parser.model.uml2;

import java.io.File;
import java.io.IOException;
import net.sourceforge.nrl.parser.NRLParserTestCase;
import org.eclipse.emf.ecore.resource.Resource;

public abstract class UMLTestCase extends NRLParserTestCase {

    public UMLTestCase() {
        super();
    }

    protected Resource loadXMI(String fileName) throws IOException {
        File file = new File(fileName);
        Resource res = getResourceForFile(file);
        res.load(null);
        return res;
    }
}

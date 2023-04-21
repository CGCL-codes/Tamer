package org.argouml.uml.diagram;

import java.beans.PropertyVetoException;
import java.io.Writer;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectMember;
import org.argouml.ui.ArgoDiagram;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.xml.pgml.PGMLParser;
import org.tigris.gef.ocl.OCLExpander;
import org.tigris.gef.ocl.TemplateReader;
import org.tigris.gef.util.Dbg;
import org.tigris.gef.util.Util;
import ru.novosoft.uml.foundation.core.MModelElement;

public class ProjectMemberDiagram extends ProjectMember {

    public static final String MEMBER_TYPE = "pgml";

    public static final String FILE_EXT = "." + MEMBER_TYPE;

    public static final String PGML_TEE = "/org/argouml/xml/dtd/PGML.tee";

    public static OCLExpander expander = null;

    private ArgoDiagram _diagram;

    public ProjectMemberDiagram(String name, Project p) {
        super(name, p);
    }

    public ProjectMemberDiagram(ArgoDiagram d, Project p) {
        super(null, p);
        String s = Util.stripJunk(d.getName());
        setName(s);
        setDiagram(d);
        if (d instanceof UMLDiagram) {
            UMLDiagram u = (UMLDiagram) d;
            if (u.getNamespace() instanceof MModelElement) {
                MModelElement me = (MModelElement) u.getNamespace();
            }
        }
    }

    public ArgoDiagram getDiagram() {
        return _diagram;
    }

    public String getType() {
        return MEMBER_TYPE;
    }

    public String getFileExtension() {
        return FILE_EXT;
    }

    public void load() {
        Dbg.log(getClass().getName(), "Reading " + getURL());
        PGMLParser.SINGLETON.setOwnerRegistry(getProject()._UUIDRefs);
        ArgoDiagram d = (ArgoDiagram) PGMLParser.SINGLETON.readDiagram(getURL());
        setDiagram(d);
        try {
            getProject().addDiagram(d);
        } catch (PropertyVetoException pve) {
        }
    }

    public void save(String path, boolean overwrite) {
        save(path, overwrite, null);
    }

    public void save(String path, boolean overwrite, Writer writer) {
        if (expander == null) expander = new OCLExpander(TemplateReader.readFile(PGML_TEE));
        expander.expand(writer, _diagram, "", "");
    }

    protected void setDiagram(ArgoDiagram diagram) {
        _diagram = diagram;
    }
}

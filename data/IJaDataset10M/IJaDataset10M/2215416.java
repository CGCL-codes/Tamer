package net.sourceforge.seqware.webservice.resources.tables;

import net.sourceforge.seqware.common.model.Study;
import net.sourceforge.seqware.common.module.ReturnValue;
import net.sourceforge.seqware.common.util.xmltools.JaxbObject;

/**
 *
 * @author mtaschuk
 */
public class StudyIDResourceTest extends DatabaseResourceIDTest {

    public StudyIDResourceTest() {
        super("/studies/4758");
        jo = new JaxbObject<Study>();
        o = new Study();
    }

    @Override
    protected int testObject(Object o) {
        if (o instanceof Study) {
            Study e = (Study) o;
            if (e.getSwAccession() != Integer.parseInt(id)) {
                System.err.println("Actual ID: " + e.getSwAccession() + " and expected ID: " + Integer.parseInt(id));
                return ReturnValue.INVALIDFILE;
            }
        } else {
            System.err.println("Object is not an instance of Study");
            return ReturnValue.FILENOTREADABLE;
        }
        return ReturnValue.SUCCESS;
    }
}

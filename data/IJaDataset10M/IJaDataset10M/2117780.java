package org.semvers.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import java.io.File;
import org.junit.Test;
import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.impl.jena24.ModelImplJena24;
import org.ontoware.rdf2go.model.Statement;
import org.ontoware.semversion.SemVersion;
import org.ontoware.semversion.Session;
import org.ontoware.semversion.Version;
import org.ontoware.semversion.VersionedModel;

/**
 * https://marcont.svn.sourceforge.net/svnroot/marcont/branches/MarcOnt2
 * @author Szymon
 */
public class TempMainClass {

    @Test
    public void test() throws Exception {
        Model m = ModelFactory.createDefaultModel();
        m.read(FileManager.get().open("E:\\Prace\\DERI\\wget\\koala.owl"), "");
        OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM, m);
        org.ontoware.rdf2go.model.Model m2 = new ModelImplJena24(ontModel);
        SemVersion semVersion = new SemVersion();
        semVersion.startup(new File("/test"));
        semVersion.deleteStore();
        semVersion.createUser("a", "a");
        Session s1 = semVersion.login("a", "a");
        VersionedModel vm = s1.createVersionedModel("thread");
        m2.open();
        vm.commitRoot(m2, "a");
        m2.close();
        Version x = vm.getFirstVersion();
        org.ontoware.rdf2go.model.Model otherModel = x.getContent();
        Model newmodel = ModelFactory.createDefaultModel();
        OntModel ontoModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM, newmodel);
        org.ontoware.rdf2go.model.Model rdf2goModel = new ModelImplJena24(ontoModel);
        rdf2goModel.open();
        otherModel.open();
        ClosableIterator<Statement> it = otherModel.iterator();
        while (it.hasNext()) {
            rdf2goModel.addStatement(it.next());
        }
        rdf2goModel.close();
        otherModel.close();
        s1.close();
        semVersion.shutdown();
        assertTrue(true);
    }
}

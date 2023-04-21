package org.arastreju.core.ontology.binding.rdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.arastreju.api.ArastrejuGate;
import org.arastreju.api.ArastrejuGateFactory;
import org.arastreju.api.ontology.Apriori;
import org.arastreju.api.ontology.binding.AssociationDeclaration;
import org.arastreju.api.ontology.binding.ModelExtract;
import org.arastreju.api.ontology.binding.OntologyIOException;
import org.arastreju.api.ontology.model.Association;
import org.arastreju.api.ontology.model.sn.ResourceNode;
import de.lichtflut.infra.io.SystemResourceLoader;
import de.lichtflut.infra.logging.Log;
import de.lichtflut.infra.security.Crypt;

/**
 * Testcase for rdf binding of semantic model extracts.
 * 
 * Created: 20.07.2009
 *
 * @author Oliver Tigges 
 */
public class RdfBindingTest extends TestCase {

    public void testIO() {
        final RdfXmlBinding rdfb = new RdfXmlBinding();
        try {
            final File tempFile = File.createTempFile("persons", "rdf");
            final ArastrejuGate gate = ArastrejuGateFactory.getInstance().login("root", Crypt.md5Hex("root"));
            final ResourceNode personClass = gate.lookupSemanticNetworkService().resolve(Apriori.PERSON);
            final List<Association> associations = gate.lookupSemanticNetworkService().findIncomingAssociations(personClass);
            associations.addAll(personClass.getAssociations());
            Log.debug(this, "Persons to write: " + associations);
            final List<AssociationDeclaration> decls = new ArrayList<AssociationDeclaration>();
            for (Association assoc : associations) {
                decls.add(new AssociationDeclaration(assoc));
            }
            final OutputStream out = new FileOutputStream(tempFile);
            rdfb.write(new ModelExtract(decls), out);
            out.close();
            rdfb.write(new ModelExtract(decls), System.out);
            final InputStream in = new FileInputStream(tempFile);
            final ModelExtract read = rdfb.read(in);
            in.close();
            Log.info(RdfXmlBinding.class, "read RDF: " + read);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testReadOwl() {
        final RdfXmlBinding rdfb = new RdfXmlBinding();
        final InputStream in = SystemResourceLoader.getInstance().loadResource("resource:rdf/owl-test-01.rdf");
        try {
            final ModelExtract read = rdfb.read(in);
            in.close();
            Log.info(RdfXmlBinding.class, "read RDF: " + read);
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (OntologyIOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    public void testReadDcTerms() {
        final RdfXmlBinding rdfb = new RdfXmlBinding();
        final InputStream in = SystemResourceLoader.getInstance().loadResource("resource:rdf/dcterms.rdf");
        try {
            final ModelExtract read = rdfb.read(in);
            in.close();
            for (AssociationDeclaration ad : read.getAssociations()) {
                if (!ad.isFullyQualified()) {
                    Log.warn(this, "Not FQ: " + ad);
                }
                if (ad.getObject() == null) {
                    Log.warn(this, "No object:" + ad);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (OntologyIOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}

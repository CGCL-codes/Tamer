package org.dbe.kb.server.proxyimpl;

import java.io.*;
import java.util.*;
import org.dbe.kb.proxy.ODI;
import org.dbe.kb.server.common.*;

/**
 * <p> Ontology Proxy </p>
 * <p>TUC/MUSIC 2004</p>
 * @version 1.0
 */
public class ODMproxy extends Modelproxy implements ODI, java.io.Serializable {

    boolean _bundled = false;

    public ODMproxy(boolean inits, String serverAddress) {
        super(inits, serverAddress, "Ontology", "Ontology", "ODM-MOF", KBinfo.KB_Get_ODM_Metamodel, "ODM", KBinfo.Proxy_ODM_Models);
    }

    public void newOntology(String name, String id) {
        _bundled = false;
        Vector v = new Vector();
        v.addElement(name);
        v.addElement(id);
        newModel(v);
    }

    public void importOntology(String filename) throws java.io.IOException, javax.jmi.xmi.MalformedXMIException {
        importOntology(new java.io.FileInputStream(filename));
    }

    public void importOntology(InputStream inStream) throws java.io.IOException, javax.jmi.xmi.MalformedXMIException {
        _bundled = true;
        importModel(inStream);
    }

    public void exportOntology(String filename) throws java.io.IOException {
        exportOntology(new java.io.FileOutputStream(filename));
    }

    public void exportOntology(OutputStream outStream) throws java.io.IOException {
        if (!_bundled) {
            bundleModelElements(_refp.refPackage("Core").refAssociation("Owns_As"), _refp.refPackage("Core").refClass("Element"));
            _bundled = true;
        }
        exportModel(outStream);
    }

    public java.io.InputStream KBgetOntology(String ontologyName) throws java.io.IOException, javax.jmi.xmi.MalformedXMIException {
        java.io.InputStream in = KBgetModel(ontologyName, KBinfo.KB_Get_ODM_Model);
        _bundled = true;
        return in;
    }

    public void KBstoreOntology() throws java.io.IOException {
        if (!_bundled) {
            bundleModelElements(_refp.refPackage("Core").refAssociation("OwnsAs"), _refp.refPackage("Core").refClass("Element"));
            _bundled = true;
        }
        KBstoreModel(KBinfo.KB_Store_ODM_Model);
    }

    public void KBdeleteOntology(String id) throws IOException {
        KBdeleteModel(id, KBinfo.KB_Delete_ODM_Model);
    }

    public Collection KBlistOntologies() {
        java.util.Collection res = null;
        _bundled = true;
        res = KBlistModels(KBinfo.KB_Get_All_ODM_Models);
        return res;
    }

    public java.io.InputStream KBlistOntologiesXMI() {
        return KBlistModelsXMI(KBinfo.KB_Get_All_ODM_Models);
    }

    public javax.jmi.reflect.RefPackage getODMPackage() {
        return getModelPackage();
    }

    public void generateOntologySpecificJMI(String directory) {
        generateModelSpecificJMI(KBinfo.Proxy_ODM_Models, directory);
    }

    public void KBstoreDiagram(String name, String data) {
    }

    ;

    public String KBgetDiagram(String name, String ontologyID) {
        return null;
    }

    ;

    public void KBdeleteDiagram(String name, String ontologyID) {
    }

    ;

    public String[] KBlistDiagrams(String ontologyID) {
        return null;
    }

    ;
}

package org.argouml.xml.xmi;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.argouml.application.api.Argo;
import org.argouml.kernel.Project;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MDataType;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.model_management.MModel;

/** XMI is an XML based exchange format between UML tools. 
 *ArgoUML uses this as standard saving mechanism so that easy interchange 
 *with other tools and compliance with open standards are secured. 
 *XMI version 1.0 for UML 1.3 is used. To convert older models in XMI 
 *(Argo 0.7 used XMI 1.0 for UML1.1) to the latest version, 
 *Meta Integration provides a free key to their Model Bridge. 
 *This also permits you to convert Rational Rose models to ArgoUML! 
 *This currently only includes model information, but no graphical 
 *information (like layout of diagrams).
 *
 */
public class XMIParser {

    public static XMIParser SINGLETON = new XMIParser();

    protected MModel _curModel = null;

    protected Project _proj = null;

    protected HashMap _UUIDRefs = null;

    protected XMIParser() {
    }

    public MModel getCurModel() {
        return _curModel;
    }

    public void setProject(Project p) {
        _proj = p;
    }

    public HashMap getUUIDRefs() {
        return _UUIDRefs;
    }

    public synchronized void readModels(Project p, URL url) throws IOException {
        _proj = p;
        Argo.log.info("=======================================");
        Argo.log.info("== READING MODEL " + url);
        try {
            XMIReader reader = new XMIReader();
            InputSource source = new InputSource(url.openStream());
            source.setSystemId(url.toString());
            _curModel = reader.parse(source);
            if (reader.getErrors()) {
                throw new IOException("XMI file " + url.toString() + " could not be parsed.");
            }
            _UUIDRefs = new HashMap(reader.getXMIUUIDToObjectMap());
        } catch (SAXException saxEx) {
            Exception ex = saxEx.getException();
            if (ex == null) {
                saxEx.printStackTrace();
            } else {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Argo.log.info("=======================================");
        try {
            _proj.addModel((ru.novosoft.uml.foundation.core.MNamespace) _curModel);
        } catch (PropertyVetoException ex) {
            System.err.println("An error occurred adding the model to the project!");
            ex.printStackTrace();
        }
        Collection ownedElements = _curModel.getOwnedElements();
        Iterator oeIterator = ownedElements.iterator();
        while (oeIterator.hasNext()) {
            MModelElement me = (MModelElement) oeIterator.next();
            if (me instanceof MClass) {
                _proj.defineType((MClass) me);
            } else if (me instanceof MDataType) {
                _proj.defineType((MDataType) me);
            }
        }
    }
}

package org.mindswap.markup.store.impl;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.ontology.OntModel;
import org.mindswap.markup.utils.SubmitRDF;
import org.mindswap.markup.MarkupModel;
import org.mindswap.markup.MarkupEditor;
import org.mindswap.markup.store.Store;
import org.mindswap.markup.store.exception.SubmitException;
import org.mindswap.markup.store.*;

public class SiteStore extends StoreImpl implements Store {

    private String mInstanceURL;

    private String mSubmitURL;

    private MarkupModel mMarkupModel;

    private Model mModel;

    private boolean mDirty;

    public SiteStore(String theName, MarkupModel theModel, String theInstanceURL, String theSubmitURL) {
        super(theName);
        mMarkupModel = theModel;
        mSubmitURL = theSubmitURL;
        mInstanceURL = theInstanceURL;
        mModel = MarkupEditor.createOntModel();
        MarkDirty(true);
    }

    public boolean submit(Model theModel) throws SubmitException {
        try {
            java.io.StringWriter sw = new java.io.StringWriter();
            theModel.write(sw, null, mMarkupModel.getBaseURL());
            String aRDFString = sw.toString();
            SubmitRDF submitter = new SubmitRDF(aRDFString, mMarkupModel);
            submitter.addCategory("http://owl.mindswap.org/2003/ont/owlweb.rdf#ProgramData");
            boolean success = submitter.submit(mSubmitURL);
            if (success) {
                MarkDirty(true);
                refresh();
                return true;
            } else return false;
        } catch (Exception ex) {
            throw new SubmitException(ex.getMessage());
        }
    }

    public Model getInstances() {
        return mModel;
    }

    private boolean IsDirty() {
        return mDirty;
    }

    private void MarkDirty(boolean dirty) {
        mDirty = dirty;
    }

    public void connect() {
        refresh();
    }

    public void disconnect() {
        MarkDirty(true);
        mModel.removeAll();
    }

    public void refresh() {
        if (IsDirty()) {
            mModel = MarkupEditor.createOntModel();
            mModel.read(mInstanceURL);
            MarkDirty(false);
        }
    }
}

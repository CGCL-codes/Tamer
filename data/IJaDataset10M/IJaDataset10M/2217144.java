package debugger.resources.lr;

import gate.Gate;
import gate.LanguageResource;
import gate.Document;
import gate.Corpus;
import java.util.*;

/**
 * Copyright (c) Ontos AG (http://www.ontosearch.com).
 * This class is part of JAPE Debugger component for
 * GATE (Copyright (c) "The University of Sheffield" see http://gate.ac.uk/) <br>
 * @author Andrey Shafirin, Vladimir Karasev
 */
public class LRRoot {

    private ArrayList corpora;

    public LRRoot() {
        List lrInstances = Gate.getCreoleRegister().getLrInstances();
        corpora = new ArrayList();
        ArrayList documents = new ArrayList();
        for (Iterator it = lrInstances.iterator(); it.hasNext(); ) {
            LanguageResource currentResource = (LanguageResource) it.next();
            if (currentResource instanceof Document) {
                documents.add(currentResource);
            }
        }
        for (Iterator it = lrInstances.iterator(); it.hasNext(); ) {
            LanguageResource currentResource = (LanguageResource) it.next();
            if (currentResource instanceof Corpus) {
                ArrayList documentsInThisCorpus = new ArrayList();
                for (Iterator iter = documents.iterator(); iter.hasNext(); ) {
                    Document currentDocument = (Document) iter.next();
                    if (((Corpus) currentResource).getDocumentNames().contains(currentDocument.getName())) {
                        documentsInThisCorpus.add(new LrModel(currentDocument));
                    }
                }
                corpora.add(new CorpusModel(documentsInThisCorpus, currentResource.getName()));
            }
        }
    }

    public LrModel getDocumentModel(Document doc) {
        for (int i = 0; i < corpora.size(); i++) {
            CorpusModel model = (CorpusModel) corpora.get(i);
            for (Iterator iterator = model.getLrModels().iterator(); iterator.hasNext(); ) {
                LrModel docModel = (LrModel) iterator.next();
                if (docModel.getLr().equals(doc)) return docModel;
            }
        }
        return null;
    }

    public ArrayList getCorpora() {
        return corpora;
    }
}

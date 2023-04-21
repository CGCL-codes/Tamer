package it.unimib.disco.itis.polimar.functionManagement.functions;

import java.util.Set;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class ExistAllEvaluation extends QualitativeFunction {

    @Override
    public double calculateDom(Set<String> reqSet, Set<String> offSet) {
        for (String req : reqSet) {
            for (String off : offSet) {
                if (off.equals(req)) {
                    return 1;
                } else {
                    InfModel infModel = dc.getInference();
                    Property p = infModel.getProperty("http://www.w3.org/2002/07/owl#sameAs");
                    Resource r = infModel.getResource(req);
                    StmtIterator list = infModel.listStatements(r, p, (RDFNode) null);
                    while (list.hasNext()) {
                        if (list.next().getObject().toString().equals(off)) return 1;
                    }
                }
            }
        }
        return 0;
    }
}

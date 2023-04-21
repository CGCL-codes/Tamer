package com.hp.hpl.jena.assembler.assemblers;

import com.hp.hpl.jena.assembler.*;
import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.*;

public class DocumentManagerAssembler extends AssemblerBase {

    public Object open(Assembler a, Resource root, Mode irrelevant) {
        checkType(root, JA.DocumentManager);
        OntDocumentManager result = createDocumentManager();
        result.setMetadataSearchPath(getPath(a, root), false);
        result.configure(ResourceUtils.reachableClosure(root), false);
        result.setFileManager(getFileManager(a, root));
        return result;
    }

    private String getPath(Assembler a, Resource root) {
        String s = getUniqueString(root, JA.policyPath);
        return s == null ? OntDocumentManager.DEFAULT_METADATA_PATH : s;
    }

    private FileManager getFileManager(Assembler a, Resource root) {
        Resource fm = getUniqueResource(root, JA.fileManager);
        return fm == null ? FileManager.get() : (FileManager) a.open(fm);
    }

    /**
        Tests may subclass and override to supply testable objects.
    */
    protected OntDocumentManager createDocumentManager() {
        return new OntDocumentManager("");
    }
}

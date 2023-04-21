package jsesh.mdc.model.utilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import jsesh.mdc.model.BasicItemList;
import jsesh.mdc.model.Cadrat;
import jsesh.mdc.model.HBox;
import jsesh.mdc.model.InnerGroup;
import jsesh.mdc.model.ModelElement;
import jsesh.mdc.model.ModelElementDeepAdapter;
import jsesh.mdc.model.TopItemList;

/**
 * Expert which can extract innergroups from a list of top level items
 * @author rosmord
 *
 */
public class InnerGroupExtractor {

    private boolean foundOtherElements;

    private List innerGroups;

    /**
	 * Extract Inner groups from a list of TopItems.
	 * @param list
	 */
    public void extract(List list) {
        foundOtherElements = false;
        innerGroups = new ArrayList();
        InnerGroupExtractorAux aux = new InnerGroupExtractorAux();
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            ModelElement elt = (ModelElement) iterator.next();
            elt.accept(aux);
        }
    }

    /**
	 * @return
	 */
    public boolean foundOtherElements() {
        return foundOtherElements;
    }

    /**
	 * @return
	 */
    public List getInnerGroups() {
        return innerGroups;
    }

    private class InnerGroupExtractorAux extends ModelElementDeepAdapter {

        public void visitInnerGroup(InnerGroup g) {
            innerGroups.add(g.deepCopy());
        }

        public void visitDefault(ModelElement t) {
            super.visitDefault(t);
            foundOtherElements = true;
        }

        public void visitBasicItemList(BasicItemList l) {
            super.visitDefault(l);
        }

        public void visitHBox(HBox b) {
            super.visitDefault(b);
        }

        public void visitCadrat(Cadrat c) {
            super.visitDefault(c);
        }

        public void visitTopItemList(TopItemList t) {
            super.visitDefault(t);
        }
    }
}

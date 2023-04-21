package uk.ac.osswatch.simal.wicket.data;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.model.IModel;
import uk.ac.osswatch.simal.model.IResource;

/**
 * A DOAP resource data provider that allows the DOAP Resources to be sorted.
 * 
 */
public class SortableDocumentDataProvider<IDocument> extends SortableFoafResourceDataProvider<IDocument> {

    private static final long serialVersionUID = -6674850425804180338L;

    public static final String SORT_PROPERTY_NAME = "name";

    public static final String SORT_PROPERTY_LABEL = "label";

    /**
   * The set of DoapResources we are providing access to.
   */
    private Set<IDocument> resources;

    /**
   * Create a data provider for the supplied resources.
   */
    public SortableDocumentDataProvider(Set<IDocument> resources) {
        this.resources = resources;
    }

    @Override
    public void setSort(SortParam param) {
        if (validateSortProperty(param.getProperty())) {
            super.setSort(param);
        } else {
            throw new RuntimeException("Set an unknown sort property: " + param.getProperty());
        }
    }

    @Override
    public void setSort(String property, boolean isAscending) {
        if (validateSortProperty(property)) {
            super.setSort(property, isAscending);
        } else {
            throw new RuntimeException("Set an unknown sort property: " + property);
        }
    }

    /**
   * Ensures a sort property is valid, that is IDoapResourceBehaviours can be
   * sorted by it.
   * 
   * @param property
   * @return
   */
    protected boolean validateSortProperty(String property) {
        return property.equals(SORT_PROPERTY_NAME) || property.equals(SORT_PROPERTY_LABEL);
    }

    public Iterator<IDocument> iterator(int first, int count) {
        Comparator<IDocument> comparator = getComparator();
        TreeSet<IDocument> treeSet = new TreeSet<IDocument>(comparator);
        treeSet.addAll(resources);
        TreeSet<IDocument> result = new TreeSet<IDocument>(comparator);
        int idx = 0;
        Iterator<IDocument> all = treeSet.iterator();
        IDocument current;
        while (all.hasNext() && idx - (first + count) < 0) {
            current = all.next();
            if (idx >= first && ((IResource) current).getURI() != "") {
                result.add(current);
            }
            idx++;
        }
        return result.iterator();
    }

    protected Comparator<IDocument> getComparator() {
        IDocumentBehaviourComparator comparator = new IDocumentBehaviourComparator();
        return comparator;
    }

    public IModel<IDocument> model(IDocument object) {
        return (IModel<IDocument>) new DetachableDocumentModel<IDocument>(object);
    }

    public int size() {
        return resources.size();
    }

    class IDocumentBehaviourComparator implements Comparator<IDocument>, Serializable {

        private static final long serialVersionUID = 1044456562070022248L;

        public int compare(IDocument resource1, IDocument resource2) {
            uk.ac.osswatch.simal.model.IDocument doc1 = (uk.ac.osswatch.simal.model.IDocument) resource1;
            uk.ac.osswatch.simal.model.IDocument doc2 = (uk.ac.osswatch.simal.model.IDocument) resource2;
            if (doc1.equals(doc2)) {
                return 0;
            }
            int result = 0;
            String sortField;
            if (getSort() == null) {
                sortField = SORT_PROPERTY_NAME;
            } else {
                sortField = getSort().getProperty();
            }
            if (sortField.equals(SORT_PROPERTY_LABEL)) {
                String name1 = doc1.getLabel();
                String name2 = doc2.getLabel();
                result = name1.compareTo(name2);
            } else if (sortField.equals(SORT_PROPERTY_NAME)) {
                String desc1 = doc1.getDefaultName();
                String desc2 = doc2.getDefaultName();
                if (desc1 == null) {
                    result = 1;
                } else if (desc2 == null) {
                    result = -1;
                } else {
                    result = desc1.compareTo(desc2);
                }
            }
            if (result == 0) {
                result = 1;
            }
            return result;
        }
    }

    /**
   * Update the data in this provider to only include those supplied.
   * 
   * @param resources
   */
    public void resetData(Set<IDocument> resources) {
        this.resources = resources;
    }
}

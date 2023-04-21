package org.isi.monet.core.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.jdom.Element;

public class WorkMapLinkDefinitionList extends ModelDefinitionList {

    private HashSet<String> hsSources;

    private HashSet<String> hsDestinations;

    public WorkMapLinkDefinitionList() {
        this.hsSources = new HashSet<String>();
        this.hsDestinations = new HashSet<String>();
    }

    public Boolean add(BaseModel oBaseModel) {
        WorkMapLinkDefinition oWorkMapLinkDefinition = (WorkMapLinkDefinition) oBaseModel;
        this.hmItems.put(oWorkMapLinkDefinition.getId(), oWorkMapLinkDefinition);
        this.hsSources.add(oWorkMapLinkDefinition.getSource());
        this.hsDestinations.add(oWorkMapLinkDefinition.getDestination());
        return true;
    }

    public Boolean containsSource(String code) {
        return this.hsSources.contains(code);
    }

    public Boolean containsDestination(String code) {
        return this.hsDestinations.contains(code);
    }

    @SuppressWarnings("unchecked")
    public Boolean unserializeFromXML(Element oLinkList) {
        List<Element> lLinkDefinition;
        Iterator<Element> oIterator;
        if (oLinkList == null) return false;
        lLinkDefinition = oLinkList.getChildren("link");
        oIterator = lLinkDefinition.iterator();
        this.clear();
        while (oIterator.hasNext()) {
            WorkMapLinkDefinition oWorkMapLinkDefinition = new WorkMapLinkDefinition();
            oWorkMapLinkDefinition.unserializeFromXML(oIterator.next());
            oWorkMapLinkDefinition.setId(String.valueOf(this.hmItems.size()));
            this.add(oWorkMapLinkDefinition);
        }
        return true;
    }
}

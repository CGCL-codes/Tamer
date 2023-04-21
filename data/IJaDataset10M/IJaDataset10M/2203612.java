package org.nex.ts.server.cube.model;

import java.util.*;
import org.nex.ts.TopicSpacesException;
import org.nex.ts.server.cube.api.ICellFacet;
import org.nex.ts.server.cube.api.ICubeTopic;
import org.nex.ts.smp.api.IAddressableInformationResource;
import org.nex.ts.smp.api.IMapDataProvider;
import org.nex.ts.smp.api.ISubjectProxy;
import org.nex.ts.smp.proxies.SubjectProxy;
import org.nex.ts.server.common.model.Ticket;
import org.openrdf.model.URI;
import org.openrdf.model.Graph;

/**
 * @author park
 *
 */
public class CubeTopic extends SubjectProxy implements ICubeTopic {

    /**
	 * @param p
	 */
    public CubeTopic(ISubjectProxy p) {
        super(p);
    }

    /**
	 * @param db
	 * @param type
	 * @param locator
	 * @param userLocator
	 * @throws TopicSpacesException
	 */
    public CubeTopic(Graph graph, IMapDataProvider db, String locator, String userLocator) throws TopicSpacesException {
        super(graph, db, CUBE.CUBE_TOPIC, locator, userLocator);
    }

    public String getTitle() {
        String result = super.getNameString("en");
        return result;
    }

    public String getDescription() throws TopicSpacesException {
        String result = super.getDescriptionString("en");
        ;
        return result;
    }

    public ICellFacet getFacet(String facetLocator) throws TopicSpacesException {
        ICellFacet result = null;
        ISubjectProxy p = workingMap.getProxyByLocator(facetLocator);
        result = new FacetProxy(p);
        return result;
    }

    public void addFacet(String facetName, Ticket credentials) throws TopicSpacesException {
        if (_graph == null) _graph = database.getGraph();
        ICellFacet f = new FacetProxy(_graph, database, database.newUUID(), credentials.getOwner());
        f.addNameWithLanguage(facetName, credentials.getLocale());
        super.addURIProperty(CUBE.TOPIC_FACET_LIST_PROPERTY, f.getURIString(), true);
        super.workingMap.submitProxyForMerge(f);
    }

    public void removeFacet(String facetLocator) throws TopicSpacesException {
        throw new TopicSpacesException("CubeTopic.removeFacet not implemented.");
    }

    public List<ICellFacet> listFacets(int offset) throws TopicSpacesException {
        List<ICellFacet> result = null;
        List<String> fl = super.listStringPropertyValues(CUBE.TOPIC_FACET_LIST_PROPERTY);
        if (fl != null && fl.size() > 0) {
            int len = fl.size();
            result = new ArrayList<ICellFacet>(len);
            for (int i = 0; i < len; i++) {
                ISubjectProxy p = database.getProxyByURI(fl.get(i));
                result.add(new FacetProxy(p));
            }
        }
        return result;
    }

    public List<String> listFacetHREFs(int offset) throws TopicSpacesException {
        List<String> result = new ArrayList<String>();
        List<ICellFacet> fl = listFacets(offset);
        super.smp.logDebug("CubeTopic.listFacetHREFs " + fl);
        if (fl != null && fl.size() > 0) {
            Iterator<ICellFacet> itr = fl.iterator();
            ICellFacet c;
            while (itr.hasNext()) {
                c = itr.next();
                result.add("<a href=\"javascript:getFacet('" + c.getLocator() + "');\">" + c.getNameString("en") + "</a>");
            }
        }
        return result;
    }

    public void setHostCubeURI(String cubeURI) throws TopicSpacesException {
        super.addProperty(CUBE.HOST_CUBE_PROPERTY, cubeURI, true);
    }

    public String getHostCubeURI() {
        return super.getFirstPropertyValue(CUBE.HOST_CUBE_PROPERTY);
    }

    public void addContent(String subject, String body, String language, Ticket credentials) throws TopicSpacesException {
        boolean needsGraph = database.installGraph(this);
        IAddressableInformationResource air = super.addAIR(subject, body, language, credentials.getOwner());
        super.workingMap.submitProxyForMerge(air);
    }

    public IAddressableInformationResource getContent(String airProxyLocator) throws TopicSpacesException {
        IAddressableInformationResource p = database.getAIRbyURI(database.locatorToURI(airProxyLocator));
        return p;
    }

    public List<String> listTopicContentHREFs() throws TopicSpacesException {
        List<String> result = new ArrayList<String>();
        List<IAddressableInformationResource> airs = super.listAIRs();
        if (airs != null && airs.size() > 0) {
            IAddressableInformationResource a;
            Iterator<IAddressableInformationResource> itr = airs.iterator();
            while (itr.hasNext()) {
                a = itr.next();
                result.add("<a href=\"javascript:getTopicContent('" + a.getLocator() + "');\">" + a.getSubject("en") + "</a>");
            }
        }
        return result;
    }

    public void removeContent(String airProxyLocator) throws TopicSpacesException {
    }
}

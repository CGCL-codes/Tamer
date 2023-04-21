package org.corrib.s3b.mbb.service;

import java.net.URI;
import java.net.URISyntaxException;
import org.corrib.s3b.mbb.beans.URLDecode;
import org.corrib.s3b.mbb.db.rdf.RDFQuery;
import org.corrib.s3b.mbb.db.rdf.Repository;
import org.corrib.s3b.mbb.db.rdf.RepositoryFactory;
import org.corrib.s3b.mbb.db.rdf.SesameWrapper;
import org.corrib.s3b.mbb.service.types.PredefinedNamespace;
import org.openrdf.model.Value;
import org.openrdf.sesame.config.AccessDeniedException;
import org.openrdf.sesame.constants.QueryLanguage;
import org.openrdf.sesame.repository.local.LocalRepository;

/**
 * Class BrowseRelatedService
 * <p>
 * Description: This browsing service generates a set of resources related to
 * the initial set with given property. Each resource in the result set must be
 * connected with at least one resource in the intial set with given resource.
 * Inverse property relation is also allowed; in which case, each resource in
 * the result set much have at least one resource in the initial set related to
 * it with given property.
 * </p>
 * 
 * <pre>
 *  &lt;BrowseService&gt; ::= (&lt;MBBService&gt;)? ’/browse/’ (&lt;Not&gt;)?
 *      &lt;BrowseDef&gt; ’$’
 *      &lt;BrowseDef&gt; ::= &lt;PropertyDef&gt; | &lt;Anything&gt;
 *    &lt;PropertyDef&gt; ::= &lt;NamespaceAbbr&gt; ’:’ &lt;Anything&gt; |
 *      &lt;Anything&gt; ’:’ &lt;SimpleValue&gt; |
 *                      &lt;NamespaceAbbr&gt; ’:’ &lt;SimpleValue&gt;
 * </pre>
 */
public class BrowseRelatedService extends BrowseService {

    /**
	 * Object to access predefined namespaces
	 */
    PredefinedNamespace pnamespace;

    public BrowseRelatedService() {
        super();
        this.pnamespace = PredefinedNamespace.get();
    }

    @Override
    public LocalRepository execute(LocalRepository inGraph) {
        LocalRepository lr = RepositoryFactory.getServiceRepository(this.getUri());
        boolean isNegation = MBBService.NEGATION.equals(this.params.get(1));
        boolean isInverse = MBBService.INVERSE.equals(this.params.get(2));
        String sFullProperty = this.params.get(3);
        String sPropertyNS = this.params.get(4);
        String sPropertyName = this.params.get(5);
        boolean uriprop = (null == sPropertyNS || "".equals(sPropertyNS));
        String[] asFullProperty = null;
        if (uriprop) {
            if (null != sPropertyName && !"".equals(sPropertyName)) sFullProperty = sPropertyName;
            sFullProperty = URLDecode.decode(sFullProperty);
            URI u;
            try {
                u = new URI(sFullProperty);
            } catch (URISyntaxException e) {
                u = null;
            }
            String prop;
            if (u == null || !u.isAbsolute()) {
                asFullProperty = this.pproperty.getURI(sFullProperty);
                prop = sFullProperty;
            } else {
                asFullProperty = new String[1];
                asFullProperty[0] = sFullProperty;
                prop = this.pproperty.getName(sFullProperty);
            }
            this.params.set(3, prop);
        }
        try {
            if (lr.getGraph().getStatements().hasNext()) return lr;
        } catch (AccessDeniedException e) {
            Repository.logger.warning(e.toString());
        }
        RDFQuery rdfquery;
        String restparam;
        String using;
        rdfquery = (isInverse) ? RDFQuery.RDFQ_BROWSE_LISTVALUES_PROPERTY_INVERSE : RDFQuery.RDFQ_BROWSE_LISTVALUES_PROPERTY;
        if (uriprop) {
            using = "";
            restparam = (isNegation) ? "NOT (" : "(";
            if (asFullProperty != null) for (String s : asFullProperty) {
                restparam += "prop = <" + s + "> OR ";
            }
            if (restparam.length() > 4) restparam = restparam.substring(0, restparam.length() - 4);
            restparam += ")";
        } else {
            restparam = "TRUE";
            if (!"*".equals(sPropertyNS)) restparam += ((!isNegation) ? " AND (" : " AND (NOT ") + "namespace(prop) = property:";
            if (!"*".equals(sPropertyName) && !"*".equals(sPropertyNS)) restparam += (!isNegation) ? " AND " : " OR ";
            if (!"*".equals(sPropertyName)) restparam += ((!isNegation) ? "" : "NOT") + " localName(prop)=\"" + sPropertyName + "\"";
            if (!"*".equals(sPropertyNS)) restparam += ") ";
            using = (!"*".equals(sPropertyNS)) ? " USING NAMESPACE mbb = <http://s3b.corrib.org/mbb#>" + RDFQuery.RDFQ_ADD_NAMESPACE.toString(this.pnamespace.getURI(sPropertyNS)) : "";
        }
        String valueQueryPart = rdfquery.toString(restparam);
        valueQueryPart += using;
        Repository.logger.info(valueQueryPart);
        Value[] avUris = SesameWrapper.performVectorQuery(inGraph, QueryLanguage.SERQL, valueQueryPart);
        try {
            SesameWrapper.loadResources(avUris, Repository.MAIN_REPOSITORY.getLocalRepository(), lr.getGraph());
        } catch (AccessDeniedException e) {
            e.printStackTrace();
        }
        this.annotateResults(lr, avUris);
        return lr;
    }

    @Override
    public String getActionType() {
        return "browse";
    }
}

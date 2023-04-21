package org.apache.isis.extensions.restful.viewer.resources.specs;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import nu.xom.Element;
import org.apache.isis.applib.Identifier;
import org.apache.isis.extensions.restful.applib.resources.SpecsResource;
import org.apache.isis.extensions.restful.viewer.html.HtmlClass;
import org.apache.isis.extensions.restful.viewer.html.XhtmlTemplate;
import org.apache.isis.extensions.restful.viewer.resources.ResourceAbstract;
import org.apache.isis.extensions.restful.viewer.util.ActionUtils;
import org.apache.isis.metamodel.spec.ObjectSpecification;
import org.apache.isis.metamodel.spec.feature.ObjectAction;
import org.apache.isis.metamodel.spec.feature.ObjectActionType;
import org.apache.isis.metamodel.spec.feature.ObjectAssociation;
import org.apache.isis.metamodel.spec.feature.OneToManyAssociation;
import org.apache.isis.metamodel.spec.feature.OneToOneAssociation;
import org.apache.isis.metamodel.specloader.SpecificationLoader;
import org.apache.isis.runtime.context.IsisContext;

/**
 * Implementation note: it seems to be necessary to annotate the implementation with {@link Path} rather than
 * the interface (at least under RestEasy 1.0.2 and 1.1-RC2).
 */
@Path("/specs")
public class SpecsResourceImpl extends ResourceAbstract implements SpecsResource {

    public String specs() {
        init();
        final XhtmlTemplate xhtml = new XhtmlTemplate("Specifications", getServletRequest());
        xhtml.appendToBody(asDivNofSession());
        xhtml.appendToBody(resourcesDiv());
        final Element div = xhtmlRenderer.div_p("Specifications", HtmlClass.SECTION);
        final Element ul = xhtmlRenderer.ul(HtmlClass.SPECIFICATIONS);
        final ObjectSpecification[] allSpecifications = getSpecificationLoader().allSpecifications();
        for (final ObjectSpecification spec : sorted(allSpecifications)) {
            final String specFullName = spec.getFullName();
            final String uri = MessageFormat.format("{0}/specs/{1}", getServletRequest().getContextPath(), specFullName);
            ul.appendChild(xhtmlRenderer.li_a(uri, specFullName, "spec", "specs", HtmlClass.SPECIFICATION));
        }
        div.appendChild(ul);
        xhtml.appendToBody(div);
        return xhtml.toXML();
    }

    private ObjectSpecification[] sorted(final ObjectSpecification[] noSpecs) {
        Arrays.sort(noSpecs, new Comparator<ObjectSpecification>() {

            public int compare(ObjectSpecification o1, ObjectSpecification o2) {
                return o1.getFullName().compareTo(o2.getFullName());
            }
        });
        return noSpecs;
    }

    private SpecificationLoader getSpecificationLoader() {
        return IsisContext.getSpecificationLoader();
    }

    public String spec(final String specFullName) {
        init();
        final XhtmlTemplate xhtml = new XhtmlTemplate(specFullName, getServletRequest());
        xhtml.appendToBody(asDivNofSession());
        final ObjectSpecification noSpec = getSpecification(specFullName);
        xhtml.appendToBody(asDivTableFacets(noSpec, noSpec.getFullName()));
        final Element propertiesDivEl = asDivProperties(noSpec);
        xhtml.appendToBody(propertiesDivEl);
        final Element collectionsDivEl = asDivCollections(noSpec);
        xhtml.appendToBody(collectionsDivEl);
        for (final ObjectActionType type : ACTION_TYPES) {
            final Element actionsDivEl = asDivActions(noSpec, type);
            xhtml.appendToBody(actionsDivEl);
        }
        return xhtml.toXML();
    }

    private Element asDivProperties(final ObjectSpecification noSpec) {
        final Element div = xhtmlRenderer.div_p("Properties", HtmlClass.PROPERTIES);
        final List<OneToOneAssociation> properties = noSpec.getPropertyList();
        final Element ul = xhtmlRenderer.ul(HtmlClass.PROPERTIES);
        div.appendChild(ul);
        for (final ObjectAssociation property : properties) {
            final String propertyId = property.getIdentifier().getMemberName();
            final String path = MessageFormat.format("{0}/specs/{1}/property/{2}", getServletRequest().getContextPath(), noSpec.getFullName(), propertyId);
            final Element li = xhtmlRenderer.li_a(path, propertyId, "property", "spec", HtmlClass.PROPERTY);
            ul.appendChild(li);
        }
        return div;
    }

    private Element asDivCollections(final ObjectSpecification noSpec) {
        final Element div = xhtmlRenderer.div_p("Collections", HtmlClass.COLLECTIONS);
        final List<OneToManyAssociation> collections = noSpec.getCollectionList();
        final Element ul = xhtmlRenderer.ul(HtmlClass.COLLECTIONS);
        div.appendChild(ul);
        for (final ObjectAssociation collection : collections) {
            final String collectionId = collection.getIdentifier().getMemberName();
            final String path = MessageFormat.format("{0}/specs/{1}/collection/{2}", getServletRequest().getContextPath(), noSpec.getFullName(), collectionId);
            final Element li = xhtmlRenderer.li_a(path, collectionId, "collection", "spec", HtmlClass.COLLECTION);
            ul.appendChild(li);
        }
        return div;
    }

    private Element asDivActions(final ObjectSpecification noSpec, final ObjectActionType type) {
        final Element div = xhtmlRenderer.div_p(type.name() + " actions", HtmlClass.ACTIONS);
        final ObjectAction[] actions = ActionUtils.flattened(noSpec.getObjectActions(type));
        final Element ul = xhtmlRenderer.ul(HtmlClass.ACTIONS);
        div.appendChild(ul);
        for (final ObjectAction action : actions) {
            final Identifier actionIdentifier = action.getIdentifier();
            final String actionId = actionIdentifier.toNameParmsIdentityString();
            final String noSpecFullName = noSpec.getFullName();
            final String uri = MessageFormat.format("{0}/specs/{1}/action/{2}", getServletRequest().getContextPath(), noSpecFullName, actionId);
            final Element li = xhtmlRenderer.li_a(uri, actionId, "action", "spec", HtmlClass.ACTION);
            ul.appendChild(li);
        }
        return div;
    }

    public String specFacet(final String specFullName, final String facetTypeName) {
        init();
        final String specAndFacet = specFullName + "/facet/" + facetTypeName;
        final XhtmlTemplate xhtml = new XhtmlTemplate(specAndFacet, getServletRequest());
        xhtml.appendToBody(asDivNofSession());
        final ObjectSpecification noSpec = getSpecification(specFullName);
        final String uri = MessageFormat.format("{0}/specs/{1}", getServletRequest().getContextPath(), specFullName);
        xhtml.appendToBody(xhtmlRenderer.aHref(uri, "owning spec", "spec", "facet", HtmlClass.SPECIFICATION));
        try {
            xhtml.appendToBody(divFacetElements(facetTypeName, noSpec));
        } catch (final IllegalArgumentException e) {
            throw new WebApplicationException(responseOfNotFound(e));
        } catch (final ClassNotFoundException e) {
            throw new WebApplicationException(responseOfInternalServerError(e));
        } catch (final IntrospectionException e) {
            throw new WebApplicationException(responseOfInternalServerError(e));
        } catch (final IllegalAccessException e) {
            throw new WebApplicationException(responseOfInternalServerError(e));
        } catch (final InvocationTargetException e) {
            throw new WebApplicationException(responseOfInternalServerError(e));
        }
        return xhtml.toXML();
    }

    public String specProperty(final String specFullName, final String propertyName) {
        init();
        final String specAndProperty = specFullName + "/property/" + propertyName;
        final XhtmlTemplate xhtml = new XhtmlTemplate(specAndProperty, getServletRequest());
        xhtml.appendToBody(asDivNofSession());
        final Element div = xhtmlRenderer.div_p("Owners", null);
        xhtml.appendToBody(div);
        final Element ul = xhtmlRenderer.ul(HtmlClass.SPECIFICATION);
        final String uri = MessageFormat.format("{0}/specs/{1}", getServletRequest().getContextPath(), specFullName);
        ul.appendChild(xhtmlRenderer.aHref(uri, "owning spec", "spec", "property", HtmlClass.SPECIFICATION));
        div.appendChild(ul);
        final ObjectSpecification noSpec = getSpecification(specFullName);
        final ObjectAssociation property = noSpec.getAssociation(propertyName);
        xhtml.appendToBody(asDivTableFacets(property, propertyName));
        return xhtml.toXML();
    }

    public String specCollection(final String specFullName, final String collectionName) {
        init();
        final String specAndCollection = specFullName + "/collection/" + collectionName;
        final XhtmlTemplate xhtml = new XhtmlTemplate(specAndCollection, getServletRequest());
        xhtml.appendToBody(asDivNofSession());
        final Element div = xhtmlRenderer.div_p("Owners", null);
        xhtml.appendToBody(div);
        final Element ul = xhtmlRenderer.ul(HtmlClass.SPECIFICATION);
        final String uri = MessageFormat.format("{0}/specs/{1}", getServletRequest().getContextPath(), specFullName);
        ul.appendChild(xhtmlRenderer.aHref(uri, "owning spec", "spec", "collection", HtmlClass.SPECIFICATION));
        div.appendChild(ul);
        final ObjectSpecification noSpec = getSpecification(specFullName);
        final ObjectAssociation collection = noSpec.getAssociation(collectionName);
        xhtml.appendToBody(asDivTableFacets(collection, collectionName));
        return xhtml.toXML();
    }

    public String specAction(final String specFullName, final String actionId) {
        init();
        final String specAndAction = specFullName + "/action/" + actionId;
        final XhtmlTemplate xhtml = new XhtmlTemplate(specAndAction, getServletRequest());
        xhtml.appendToBody(asDivNofSession());
        final Element div = xhtmlRenderer.div_p("Owners", null);
        xhtml.appendToBody(div);
        final Element ul = xhtmlRenderer.ul(HtmlClass.SPECIFICATION);
        final String specUri = MessageFormat.format("{0}/specs/{1}", getServletRequest().getContextPath(), specFullName);
        ul.appendChild(xhtmlRenderer.li_a(specUri, specFullName, "owning spec", "spec", HtmlClass.SPECIFICATION));
        div.appendChild(ul);
        final ObjectSpecification noSpec = getSpecification(specFullName);
        final ObjectAction action = noSpec.getObjectAction(null, actionId);
        xhtml.appendToBody(asDivTableFacets(action, actionId));
        return xhtml.toXML();
    }

    public String specPropertyFacet(final String specFullName, final String propertyName, final String facetTypeName) {
        init();
        final String specAndPropertyAndFacet = specFullName + "/property/" + propertyName + "/facet/" + facetTypeName;
        final XhtmlTemplate xhtml = new XhtmlTemplate(specAndPropertyAndFacet, getServletRequest());
        xhtml.appendToBody(asDivNofSession());
        final ObjectSpecification noSpec = getSpecification(specFullName);
        final ObjectAssociation property = noSpec.getAssociation(propertyName);
        final Element div = xhtmlRenderer.div_p("Owners", null);
        xhtml.appendToBody(div);
        final Element ul = xhtmlRenderer.ul(HtmlClass.PROPERTIES);
        final String specUri = MessageFormat.format("{0}/specs/{1}", getServletRequest().getContextPath(), specFullName);
        ul.appendChild(xhtmlRenderer.li_a(specUri, specFullName, "owning spec", "spec", "facet"));
        final String propertyUri = MessageFormat.format("{0}/specs/{1}/property/{2}", getServletRequest().getContextPath(), specFullName, propertyName);
        ul.appendChild(xhtmlRenderer.li_a(propertyUri, propertyName, "owning property", "property", "facet"));
        div.appendChild(ul);
        try {
            xhtml.appendToBody(divFacetElements(facetTypeName, property));
        } catch (final IllegalArgumentException e) {
            throw new WebApplicationException(responseOfNotFound(e));
        } catch (final ClassNotFoundException e) {
            throw new WebApplicationException(responseOfInternalServerError(e));
        } catch (final IntrospectionException e) {
            throw new WebApplicationException(responseOfInternalServerError(e));
        } catch (final IllegalAccessException e) {
            throw new WebApplicationException(responseOfInternalServerError(e));
        } catch (final InvocationTargetException e) {
            throw new WebApplicationException(responseOfInternalServerError(e));
        }
        return xhtml.toXML();
    }

    public String specCollectionFacet(final String specFullName, final String collectionId, final String facetTypeName) {
        init();
        final String specAndPropertyAndFacet = specFullName + "/collection/" + collectionId + "/facet/" + facetTypeName;
        final XhtmlTemplate xhtml = new XhtmlTemplate(specAndPropertyAndFacet, getServletRequest());
        xhtml.appendToBody(asDivNofSession());
        final ObjectSpecification noSpec = getSpecification(specFullName);
        final ObjectAssociation collection = noSpec.getAssociation(collectionId);
        final Element div = xhtmlRenderer.div_p("Owners", null);
        xhtml.appendToBody(div);
        final Element ul = xhtmlRenderer.ul(HtmlClass.PROPERTIES);
        final String specUri = MessageFormat.format("{0}/specs/{1}", getServletRequest().getContextPath(), specFullName);
        ul.appendChild(xhtmlRenderer.li_a(specUri, specFullName, "owning spec", "spec", "facet"));
        final String collectionUri = MessageFormat.format("{0}/specs/{1}/collection/{2}", getServletRequest().getContextPath(), specFullName, collectionId);
        ul.appendChild(xhtmlRenderer.li_a(collectionUri, collectionId, "owning collection", "collection", "facet"));
        div.appendChild(ul);
        try {
            xhtml.appendToBody(divFacetElements(facetTypeName, collection));
        } catch (final IllegalArgumentException e) {
            throw new WebApplicationException(responseOfNotFound(e));
        } catch (final ClassNotFoundException e) {
            throw new WebApplicationException(responseOfInternalServerError(e));
        } catch (final IntrospectionException e) {
            throw new WebApplicationException(responseOfInternalServerError(e));
        } catch (final IllegalAccessException e) {
            throw new WebApplicationException(responseOfInternalServerError(e));
        } catch (final InvocationTargetException e) {
            throw new WebApplicationException(responseOfInternalServerError(e));
        }
        return xhtml.toXML();
    }

    public String specActionFacet(final String specFullName, final String actionId, final String facetTypeName) {
        init();
        final String specAndPropertyAndFacet = specFullName + "/action/" + actionId + "/facet/" + facetTypeName;
        final XhtmlTemplate xhtml = new XhtmlTemplate(specAndPropertyAndFacet, getServletRequest());
        xhtml.appendToBody(asDivNofSession());
        final ObjectSpecification noSpec = getSpecification(specFullName);
        final ObjectAction action = noSpec.getObjectAction(null, actionId);
        final Element div = xhtmlRenderer.div_p("Owners", null);
        xhtml.appendToBody(div);
        final Element ul = xhtmlRenderer.ul(HtmlClass.PROPERTIES);
        final String specUri = MessageFormat.format("{0}/specs/{1}", getServletRequest().getContextPath(), specFullName);
        ul.appendChild(xhtmlRenderer.li_a(specUri, specFullName, "owning spec", "spec", "facet"));
        final String actionUri = MessageFormat.format("{0}/specs/{1}/action/{2}", getServletRequest().getContextPath(), specFullName, actionId);
        ul.appendChild(xhtmlRenderer.li_a(actionUri, actionId, "owning action", "collection", "facet"));
        div.appendChild(ul);
        try {
            xhtml.appendToBody(divFacetElements(facetTypeName, action));
        } catch (final IllegalArgumentException e) {
            throw new WebApplicationException(responseOfNotFound(e));
        } catch (final ClassNotFoundException e) {
            throw new WebApplicationException(responseOfInternalServerError(e));
        } catch (final IntrospectionException e) {
            throw new WebApplicationException(responseOfInternalServerError(e));
        } catch (final IllegalAccessException e) {
            throw new WebApplicationException(responseOfInternalServerError(e));
        } catch (final InvocationTargetException e) {
            throw new WebApplicationException(responseOfInternalServerError(e));
        }
        return xhtml.toXML();
    }
}

package uk.ac.ebi.intact.webapp.search.struts.view.beans;

import uk.ac.ebi.intact.model.*;
import uk.ac.ebi.intact.util.SearchReplace;
import uk.ac.ebi.intact.webapp.search.SearchWebappContext;
import java.util.*;
import org.apache.commons.lang.StringUtils;

/**
 * This view bean is used to access the information relating to Features for display by JSPs. For every Component of an
 * Interaction that contains feature information, the will be a feature view bean related to it. TODO: The ranges need
 * handling - a Feature can have more than one...
 *
 * @author Chris Lewington
 * @version $Id: FeatureViewBean.java 10259 2007-11-05 10:18:41Z baranda $
 */
public class FeatureViewBean extends AbstractViewBean {

    /**
     * The Feature we want the beans for.
     */
    private Feature feature;

    /**
     * Holds the URL to perform subsequent searches from JSPs - used to build 'complete' URLs for use by JSPs
     */
    private String searchURL;

    /**
     * URL for searching for CvFeatureType.
     */
    private String cvFeatureTypeSearchURL = "";

    /**
     * URL for searching for CvFeatureIdentification.
     */
    private String cvFeatureIdentSearchURL = "";

    /**
     * Map of retrieved DB URLs already retrieved from the DB. This is basically a cache to avoid recomputation every
     * time a CvDatabase URL is requested.
     */
    private Map<CvObject, String> dbUrls;

    /**
     * Constructor. Takes a Feature that relates to an Interaction, and wraps the beans for it.
     *
     * @param feature     The Feature we are interested in
     */
    public FeatureViewBean(Feature feature) {
        super();
        this.searchURL = SearchWebappContext.getCurrentInstance().getSearchUrl();
        this.feature = feature;
        dbUrls = new HashMap<CvObject, String>();
    }

    /**
     * Adds the shortLabel of the Feature to an internal list used later for highlighting in a display. NOT SURE IF WE
     * STILL NEED THIS!!
     */
    @Override
    public void initHighlightMap() {
        Set<String> set = new HashSet<String>(1);
        set.add(feature.getShortLabel());
        setHighlightMap(set);
    }

    /**
     * Returns the help section.
     */
    @Override
    public String getHelpSection() {
        return "protein.single.view";
    }

    /**
     * Returns the Shortlabel of the given Feature Object
     *
     * @return String contains the shortlabel of the give Feature Object
     */
    public String getFeatureName() {
        return feature.getShortLabel();
    }

    /**
     * Returns the Feature Object itself
     *
     * @return Feature of WrappedFeatureViewBean
     */
    public Feature getFeature() {
        return feature;
    }

    public String getFeatureSummary() {
        return null;
    }

    /**
     * Provides a view bean for any bound Feature.
     *
     * @return featureViewBean a view bean for the Feature bound to this one, or null if there is no bound feature
     */
    public FeatureViewBean getBoundFeatureView() {
        if (feature.getBoundDomain() != null) {
            return new FeatureViewBean(feature.getBoundDomain());
        }
        return null;
    }

    /**
     * Provides the feature type short label.
     *
     * @return String the CvFeatureType shortLabel, or '-' if the feature type itself is null.
     */
    public String getFeatureType(boolean capitalizeFirstLetter) {
        String type = "-";
        if (feature.getCvFeatureType() != null) {
            type = feature.getCvFeatureType().getShortLabel();
            if (capitalizeFirstLetter) {
                type = StringUtils.capitalize(type);
            }
        }
        return type;
    }

    /**
     * Provides the short label of the feature identification.
     *
     * @return String the CvFeatureIdentification shortLabel, or '-' if the identification object itself is null
     */
    public String getFeatureIdentificationName() {
        if (feature.getCvFeatureIdentification() != null) {
            return feature.getCvFeatureIdentification().getShortLabel();
        }
        return "-";
    }

    public String getProteinName() {
        return feature.getComponent().getInteractor().getShortLabel();
    }

    /**
     * Provides the full name of the feature identification.
     *
     * @return String the CvFeatureIdentification full name, or '-' if the identification object itself or its full name
     *         are null
     */
    public String getFeatureIdentFullName() {
        if ((feature.getCvFeatureIdentification() != null) && (feature.getCvFeatureIdentification().getFullName() != null)) {
            return feature.getCvFeatureIdentification().getFullName();
        }
        return "-";
    }

    /**
     * Provides a Collection of Strings with shortlabels of the Xref of this specific Feature
     *
     * @return a Collection of String with the shortlabel of the Xref of the wrapped Feature
     */
    public Collection<FeatureXref> getFeatureXrefs() {
        return feature.getXrefs();
    }

    /**
     * Provides a String representation of a URL to perform a search on CvFeatureType
     *
     * @return String a String representation of a search URL link for CvFeatureType.
     */
    public String getCvFeatureTypeSearchURL() {
        if ((cvFeatureTypeSearchURL.length() == 0) && (feature.getCvFeatureType() != null)) {
            cvFeatureTypeSearchURL = searchURL + feature.getCvFeatureType().getAc() + "&amp;searchClass=CvFeatureType" + "&filter=ac";
        }
        return cvFeatureTypeSearchURL;
    }

    public boolean hasCvFeatureIdentification() {
        return feature.getCvFeatureIdentification() != null;
    }

    /**
     * Provides a String representation of a URL to perform a search on CvFeatureIdentification
     *
     * @return String a String representation of a search URL link for CvFeatureIdentification.
     */
    public String getCvFeatureIdentSearchURL() {
        if ((cvFeatureIdentSearchURL == "") && (feature.getCvFeatureIdentification() != null)) {
            cvFeatureIdentSearchURL = searchURL + feature.getCvFeatureIdentification().getAc() + "&amp;searchClass=CvFeatureIdentification" + "&filter=ac";
        }
        return cvFeatureIdentSearchURL;
    }

    /**
     * Provides a String representation of a URL to provide acces to an Xrefs' database (curently via AC). The URL is at
     * present stored via an Annotation for the Xref in the Intact DB itself.
     *
     * @param xref The Xref for which the DB URL is required
     *
     * @return String a String representation of a DB URL link for the Xref, or a '-' if there is no stored URL link for
     *         this Xref
     */
    public String getPrimaryIdURL(Xref xref) {
        String searchUrl = dbUrls.get(xref.getCvDatabase());
        if (searchUrl == null) {
            Collection<Annotation> annotations = xref.getCvDatabase().getAnnotations();
            Annotation annot = null;
            for (Iterator<Annotation> it = annotations.iterator(); it.hasNext(); ) {
                annot = it.next();
                if (annot.getCvTopic().getShortLabel().equals("search-url")) {
                    searchUrl = annot.getAnnotationText();
                    break;
                }
            }
            dbUrls.put(xref.getCvDatabase(), searchUrl);
        }
        if (searchUrl != null) {
            searchUrl = SearchReplace.replace(searchUrl, "${ac}", xref.getPrimaryId());
        }
        return searchUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FeatureViewBean)) {
            return false;
        }
        final FeatureViewBean featureViewBean = (FeatureViewBean) o;
        if (feature != null ? !feature.equals(featureViewBean.feature) : featureViewBean.feature != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = (feature != null ? feature.hashCode() : 0);
        return result;
    }
}

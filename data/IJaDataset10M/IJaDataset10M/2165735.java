package org.s3b.search.resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.corrib.jonto.kos.KOSEntry;
import org.corrib.jonto.taxonomy.TaxonomyEntry;
import org.foafrealm.manage.Person;

/**
 * @author  Lukasz Porwol
 *
 */
public class RdfResource extends AbstractResource<Person, TaxonomyEntry, KOSEntry> {

    public RdfResource() {
        super();
    }

    public RdfResource(String resourceUri) {
        super();
        setURI(resourceUri);
    }

    public RdfResource(String resourceUri, Double d) {
        super();
        rank = (d == null) ? 0.0d : d;
        setURI(resourceUri);
    }

    /** 
	 * This method checks if there is at least one, the same URI in both RdfResources
	 * 
	 * @see org.s3b.service.search.ResourceImpl#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object arg0) {
        boolean result = false;
        if (arg0 instanceof RdfResource) {
            List<String> uris = new ArrayList<String>(Arrays.asList(((RdfResource) arg0).getURIsAsString()));
            List<String> uris2 = Arrays.asList(this.getURIsAsString());
            result = !Collections.disjoint(uris, uris2);
        }
        return result;
    }

    /** 
	 * 
	 * This method has been overriden. It uses ranking first and then usage, and finally - string
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * TODO use {@link SortingParam} for comparing
	 */
    public int compareTo(Resource<Person, TaxonomyEntry, KOSEntry> o) {
        int val1 = -1 * this.getRank().compareTo(o.getRank());
        if (val1 != 0) return val1;
        return this.getURI().compareTo(o.getURI());
    }

    public void addCategory(TaxonomyEntry _category) {
        categories.add(_category);
    }

    public void addCategories(List<TaxonomyEntry> _categories) {
        categories.addAll(_categories);
    }

    public void addContributor(Person _contributor) {
        contributors.add(_contributor);
    }

    public void addContributors(List<Person> _contributors) {
        contributors.addAll(_contributors);
    }

    public void addKeyword(KOSEntry _keyword) {
        keywords.add(_keyword);
    }

    public void addKeywords(List<KOSEntry> _keywords) {
        keywords.addAll(_keywords);
    }

    public List<TaxonomyEntry> getCategories() {
        return categories;
    }

    public List<Person> getContributors() {
        return contributors;
    }

    public List<KOSEntry> getKeywords() {
        return keywords;
    }

    public void setCategories(List<TaxonomyEntry> _categories) {
        categories = _categories;
    }

    public void setContributors(List<Person> _contributors) {
        contributors = _contributors;
    }

    public void setKeywords(List<KOSEntry> _keywords) {
        keywords = _keywords;
    }
}

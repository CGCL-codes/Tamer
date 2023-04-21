package no.ugland.utransprod.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import no.ugland.utransprod.util.Util;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Klassesom representerer tabell ARTICLE_TYPE
 * @author atle.brekka
 */
public class ArticleType extends BaseObject {

    private static final long serialVersionUID = 1L;

    private Integer articleTypeId;

    private String articleTypeName;

    private String description;

    private Set<ArticleTypeAttribute> articleTypeAttributes;

    private Set<ArticleTypeArticleType> articleTypeArticleTypes;

    private Set<ArticleTypeArticleType> articleTypeArticleTypeRefs;

    private Set<ConstructionTypeArticle> constructionTypeArticles;

    private Integer topLevel;

    private Integer isExtra;

    private String metric;

    private Integer prodCatNo;

    private Integer prodCatNo2;

    private Integer forceImport;

    public static final ArticleType UNKNOWN = new ArticleType() {

        @Override
        public String getArticleTypeName() {
            return "NULL";
        }
    };

    public ArticleType() {
        super();
    }

    /**
     * @param aArticleTypeId
     * @param aArticleTypeName
     * @param aDescription
     * @param someArticleTypeAttributes
     * @param someArticleTypeArticleTypes
     * @param someArticleTypeArticleTypeRefs
     * @param someConstructionTypeArticles
     * @param isTopLevel
     * @param extra
     * @param aMetric
     */
    public ArticleType(final Integer aArticleTypeId, final String aArticleTypeName, final String aDescription, final Set<ArticleTypeAttribute> someArticleTypeAttributes, final Set<ArticleTypeArticleType> someArticleTypeArticleTypes, final Set<ArticleTypeArticleType> someArticleTypeArticleTypeRefs, final Set<ConstructionTypeArticle> someConstructionTypeArticles, final Integer isTopLevel, final Integer extra, final String aMetric, final Integer aProdCatNo, final Integer aProdCatNo2) {
        super();
        this.articleTypeId = aArticleTypeId;
        this.articleTypeName = aArticleTypeName;
        this.description = aDescription;
        this.articleTypeAttributes = someArticleTypeAttributes;
        this.articleTypeArticleTypes = someArticleTypeArticleTypes;
        this.articleTypeArticleTypeRefs = someArticleTypeArticleTypeRefs;
        this.constructionTypeArticles = someConstructionTypeArticles;
        this.topLevel = isTopLevel;
        this.isExtra = extra;
        this.metric = aMetric;
        this.prodCatNo = aProdCatNo;
        this.prodCatNo2 = aProdCatNo2;
    }

    /**
     * @return id
     */
    public final Integer getArticleTypeId() {
        return articleTypeId;
    }

    /**
     * @param aArticleTypeId
     */
    public final void setArticleTypeId(final Integer aArticleTypeId) {
        this.articleTypeId = aArticleTypeId;
    }

    /**
     * @return navn
     */
    public String getArticleTypeName() {
        return articleTypeName;
    }

    /**
     * @param aArticleTypeName
     */
    public final void setArticleTypeName(final String aArticleTypeName) {
        this.articleTypeName = aArticleTypeName;
    }

    /**
     * @return beskrivelse
     */
    public final String getDescription() {
        return description;
    }

    /**
     * @param aDescription
     */
    public final void setDescription(final String aDescription) {
        this.description = aDescription;
    }

    /**
     * @see no.ugland.utransprod.model.BaseObject#equals(java.lang.Object)
     */
    @Override
    public final boolean equals(final Object other) {
        if (!(other instanceof ArticleType)) {
            return false;
        }
        ArticleType castOther = (ArticleType) other;
        return new EqualsBuilder().append(articleTypeName, castOther.articleTypeName).isEquals();
    }

    /**
     * @see no.ugland.utransprod.model.BaseObject#hashCode()
     */
    @Override
    public final int hashCode() {
        return new HashCodeBuilder().append(articleTypeName).toHashCode();
    }

    /**
     * @see no.ugland.utransprod.model.BaseObject#toString()
     */
    @Override
    public final String toString() {
        return articleTypeName;
    }

    /**
     * @return attributter
     */
    public final Set<ArticleTypeAttribute> getArticleTypeAttributes() {
        return articleTypeAttributes;
    }

    /**
     * @param someArticleTypeAttributes
     */
    public final void setArticleTypeAttributes(final Set<ArticleTypeAttribute> someArticleTypeAttributes) {
        this.articleTypeAttributes = someArticleTypeAttributes;
    }

    /**
     * @return attributter
     */
    public final List<Attribute> getAttributes() {
        ArrayList<Attribute> attributes = new ArrayList<Attribute>();
        if (articleTypeAttributes != null) {
            for (ArticleTypeAttribute attribute : articleTypeAttributes) {
                attributes.add(attribute.getAttribute());
            }
        }
        return attributes;
    }

    /**
     * @return artikler som referer til artikkel
     */
    public final Set<ArticleTypeArticleType> getArticleTypeArticleTypes() {
        return articleTypeArticleTypes;
    }

    /**
     * @param someArticleTypeArticleTypes
     */
    public final void setArticleTypeArticleTypes(final Set<ArticleTypeArticleType> someArticleTypeArticleTypes) {
        this.articleTypeArticleTypes = someArticleTypeArticleTypes;
    }

    /**
     * Henter artikler som referer til artikkel
     * @return artikler som referer til artikkel
     */
    public final List<ArticleType> getArticles() {
        ArrayList<ArticleType> articleTypes = new ArrayList<ArticleType>();
        articleTypes.add(this);
        if (articleTypeArticleTypes != null) {
            for (ArticleTypeArticleType article : articleTypeArticleTypes) {
                articleTypes.add(article.getArticleTypeRef());
            }
        }
        return articleTypes;
    }

    /**
     * Kloner artikler som refererer til artikkel
     * @return klonede artikler
     */
    public final Set<ArticleTypeArticleType> getClonedArticleTypeArticleTypes() {
        HashSet<ArticleTypeArticleType> clonedSet = new HashSet<ArticleTypeArticleType>();
        if (articleTypeArticleTypes != null) {
            for (ArticleTypeArticleType article : articleTypeArticleTypes) {
                clonedSet.add(new ArticleTypeArticleType(article.getArticleTypeArticleTypeId(), article.getArticleType(), article.getArticleTypeRef()));
            }
        }
        return clonedSet;
    }

    /**
     * @return referanser til artikkeltype
     */
    public final Set<ArticleTypeArticleType> getArticleTypeArticleTypeRefs() {
        return articleTypeArticleTypeRefs;
    }

    /**
     * @param someArticleTypeArticleTypeRefs
     */
    public final void setArticleTypeArticleTypeRefs(final Set<ArticleTypeArticleType> someArticleTypeArticleTypeRefs) {
        this.articleTypeArticleTypeRefs = someArticleTypeArticleTypeRefs;
    }

    /**
     * @return artikler brukt i garasjetyper
     */
    public final Set<ConstructionTypeArticle> getConstructionTypeArticles() {
        return constructionTypeArticles;
    }

    /**
     * @param someConstructionTypeArticles
     */
    public final void setConstructionTypeArticles(final Set<ConstructionTypeArticle> someConstructionTypeArticles) {
        this.constructionTypeArticles = someConstructionTypeArticles;
    }

    /**
     * @return om artikkel er toppniv�
     */
    public final Integer getTopLevel() {
        return topLevel;
    }

    /**
     * @param isTopLevel
     */
    public final void setTopLevel(final Integer isTopLevel) {
        this.topLevel = isTopLevel;
    }

    /**
     * @return true dersom artikkel er toppniv�
     */
    public final Boolean getTopLevelBoolean() {
        if (topLevel != null && topLevel == 1) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * @param isTopLevel
     */
    public final void setTopeLevelBoolean(final Boolean isTopLevel) {
        setTopLevel(Util.convertBooleanToNumber(isTopLevel));
    }

    /**
     * @return om artikkel er lagt til via garasjepakking
     */
    public final Integer getIsExtra() {
        return isExtra;
    }

    /**
     * @param extra
     */
    public final void setIsExtra(final Integer extra) {
        this.isExtra = extra;
    }

    /**
     * @return true dersom artikkel er lagt til via garasjepakking
     */
    public final Boolean isExtra() {
        return Util.convertNumberToBoolean(isExtra);
    }

    /**
     * @return betegnelse
     */
    public final String getMetric() {
        return metric;
    }

    /**
     * @param aMetric
     */
    public final void setMetric(final String aMetric) {
        this.metric = aMetric;
    }

    public Integer getProdCatNo() {
        return prodCatNo;
    }

    public void setProdCatNo(Integer prodCatNo) {
        this.prodCatNo = prodCatNo;
    }

    public Integer getProdCatNo2() {
        return prodCatNo2;
    }

    public void setProdCatNo2(Integer prodCatNo2) {
        this.prodCatNo2 = prodCatNo2;
    }

    public Integer getForceImport() {
        return forceImport;
    }

    public boolean forceImport() {
        return forceImport != null && forceImport == 1;
    }

    public void setForceImportBoolean(Boolean force) {
        setForceImport(Util.convertBooleanToNumber(force));
    }

    private void setForceImport(Integer force) {
        this.forceImport = force;
    }
}

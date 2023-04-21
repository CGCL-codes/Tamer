package com.acv.webapp.taglib;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.acv.dao.catalog.Media;
import com.acv.dao.catalog.categories.common.model.Category;
import com.acv.dao.catalog.categories.destinations.model.Destination;
import com.acv.dao.catalog.categories.promotions.model.Promotion;
import com.acv.dao.catalog.categories.promotions.model.PromotionI18n;
import com.acv.dao.catalog.categories.promotions.model.PromotionRules;
import com.acv.dao.common.Constants;
import com.acv.service.catalog.CategoryManager;
import com.acv.service.urlrewriter.UrlMapperManager;

/**
 * This tag put ACV promotion information in session for a define marketing spot, iterate the block for each column
 */
public class PromotionTag extends TagSupport {

    final String TYPE_TXT = "Text";

    final String TYPE_URL = "URL";

    final String TYPE_IMG = "Large Image";

    final String TYPE_PDF = "PDF File";

    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(PromotionTag.class);

    /** set the marketing spot wanted */
    private String spot = null;

    /** set the specifique id (destination id by exemple) for specifique promotion */
    private String relatedId = null;

    /** set type */
    private String type = null;

    /** true if first promotion*/
    private boolean first = true;

    /** true if last promotion */
    private boolean last = false;

    /** get the iteration count */
    private int count = 0;

    /** set the max of spot iteration */
    private String maxitem = "400";

    /** number of element left */
    private int still = 0;

    /** language */
    private String lang = null;

    /** onlyIfHavePromotion if true, the tag will only test if promotion for this condition exist, else (false or default) the tag will iterate and load promotion information */
    private boolean onlyIfHavePromotion = false;

    /** promotion actualy treated*/
    private Promotion currentPromotion = null;

    /** manager for database access*/
    private transient CategoryManager categoryManager;

    /** manager for database access*/
    private transient UrlMapperManager urlMapperManager = null;

    /** current list of promotions */
    private Set<Promotion> promotions = null;

    /**
	 * search a object Promotion in promotions with the specifique id and for marketingSpot
	 * @param promotions liste of Promotion
	 * @param id the specifique id wanted
	 * @param marketingSpot , the marketingSpot
	 */
    private void searchPromotion(Set<Promotion> promotions, String id, String marketingSpot) {
        Long idLong = Long.valueOf(id);
        float maxPriority = -1;
        for (Promotion promotion : promotions) {
            for (PromotionRules rule : promotion.getPromotionRules()) {
                if (rule != null && rule.getMarketingSpotCode() != null && rule.getMarketingSpotCode().equals(marketingSpot) && idLong.equals(rule.getRelatedId())) {
                    float priority = 0;
                    if (rule.getPriority() != null) {
                        priority = rule.getPriority();
                    }
                    if (priority > maxPriority) {
                        currentPromotion = promotion;
                        maxPriority = priority;
                    }
                }
            }
        }
    }

    /**
	 * search a object Promotion in promotions and for marquetingSpot
	 * @param promotions liste of Promotion
	 * @param marquetingSpot , the marquetingSpot
	 */
    private void searchGenericPromotion(Set<Promotion> promotions, String marquetingSpot) {
        float maxPriority = -1;
        for (Promotion promotion : promotions) {
            for (PromotionRules rule : promotion.getPromotionRules()) {
                if (rule != null && rule.getMarketingSpotCode() != null && rule.getMarketingSpotCode().equals(marquetingSpot) && rule.getRelatedId() == null) {
                    float priority = 0;
                    if (rule.getPriority() != null) {
                        priority = rule.getPriority();
                    }
                    if (priority > maxPriority) {
                        currentPromotion = promotion;
                        maxPriority = priority;
                    }
                }
            }
        }
    }

    /**
	 * @deprecated
	 * @param promotions
	 * @param category
	 * @param marquetingSpot
	 */
    private void searchCategoryPromotion(List<Promotion> promotions, Category category, String marquetingSpot) {
        if ((currentPromotion == null) && (category instanceof Destination)) {
            Destination destination = (Destination) category;
            if (destination.getParent() instanceof Category) {
                searchCategoryPromotion(promotions, (Category) destination.getParent(), marquetingSpot);
            }
        }
    }

    /**
	 * put all promotion ifnormation in session for use them in jsp
	 * @return true if done
	 */
    private boolean setInfoForBody() {
        if (relatedId != null) {
            searchPromotion(promotions, relatedId, spot);
        }
        if (currentPromotion == null) {
            searchGenericPromotion(promotions, spot);
        }
        if (currentPromotion == null) {
            return false;
        }
        this.pageContext.setAttribute("tagPromotionLink", "#");
        PromotionI18n promotionI18n = currentPromotion.getContent().get(lang);
        if (promotionI18n == null) {
            log.warn("PROMOTION WITHOUT CONTENT " + currentPromotion.getId());
            promotions.remove(currentPromotion);
            currentPromotion = null;
            return setInfoForBody();
        }
        String contentType = promotionI18n.getType();
        if (TYPE_IMG.equals(contentType)) {
            if (currentPromotion.getMediasWithType().get(lang) != null) {
                if (currentPromotion.getMediasWithType().get(lang).get(Constants.MEDIA_PROMOTIONLINK) != null) {
                    for (Media media : currentPromotion.getMediasWithType().get(lang).get(Constants.MEDIA_PROMOTIONLINK)) {
                        this.pageContext.setAttribute("tagPromotionLink", media.getPathNormalSizeImage());
                    }
                }
            }
        } else if (TYPE_TXT.equals(contentType)) {
            this.pageContext.setAttribute("tagPromotionLink", BrowsableUrlGeneratorTag.buildUrl(urlMapperManager, currentPromotion, null, lang));
        } else if (TYPE_PDF.equals(contentType)) {
            this.pageContext.setAttribute("tagPromotionLink", promotionI18n.getPDFValue());
        } else if (TYPE_URL.equals(contentType)) {
            this.pageContext.setAttribute("tagPromotionLink", promotionI18n.getURLValue());
        }
        if (promotionI18n.getPopup() != null && promotionI18n.getPopup() == true) {
            this.pageContext.setAttribute("tagPromotionLinkTarget", "_blank");
        } else {
            this.pageContext.setAttribute("tagPromotionLinkTarget", "");
        }
        this.pageContext.setAttribute("tagPromotion", currentPromotion);
        Media icon = null;
        Set<Media> mediaType = null;
        if (currentPromotion.getMediasWithType().get(lang) != null) {
            mediaType = currentPromotion.getMediasWithType().get(lang).get(Constants.MEDIA_PROMOTION);
        } else if (currentPromotion.getMediasWithType().get(Constants.DEFAULT_CONTENT_LANGUAGE_CODE) != null) {
            mediaType = currentPromotion.getMediasWithType().get(Constants.DEFAULT_CONTENT_LANGUAGE_CODE).get(Constants.MEDIA_PROMOTION);
        }
        if (mediaType != null) {
            for (Media mediaWithType : mediaType) {
                icon = mediaWithType;
            }
        }
        this.pageContext.setAttribute("tagPromotionThumbnails", icon);
        if (promotionI18n.getPDFValue() != null) {
            this.pageContext.setAttribute("tagPromotionPDF", promotionI18n.getPDFValue());
        } else {
            this.pageContext.removeAttribute("tagPromotionPDF");
        }
        still--;
        promotions.remove(currentPromotion);
        currentPromotion = null;
        if ((still == 0) || (promotions.isEmpty())) {
            last = true;
        }
        this.pageContext.setAttribute("tagPromotionFirst", first);
        this.pageContext.setAttribute("tagPromotionLast", last);
        this.pageContext.setAttribute("tagPromotionCount", count);
        return true;
    }

    /**
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
    public int doStartTag() {
        lang = ((Locale) pageContext.getAttribute(Constants.PREFERRED_LOCALE_KEY, PageContext.SESSION_SCOPE)).getLanguage().toUpperCase();
        ServletContext servletContext = this.pageContext.getServletContext();
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        this.categoryManager = (CategoryManager) wac.getBean("categoryManager");
        this.urlMapperManager = (UrlMapperManager) wac.getBean("urlMapperManager");
        promotions = new HashSet<Promotion>(categoryManager.getPromotionsByMarketingSpotCode(spot));
        if (onlyIfHavePromotion) {
            if (relatedId != null) {
                searchPromotion(promotions, relatedId, spot);
            }
            if (currentPromotion == null) {
                searchGenericPromotion(promotions, spot);
            }
            if (currentPromotion == null) {
                return SKIP_BODY;
            } else {
                return EVAL_BODY_INCLUDE;
            }
        }
        still = Integer.valueOf(maxitem);
        if (setInfoForBody()) {
            return EVAL_BODY_INCLUDE;
        }
        return SKIP_BODY;
    }

    /**
	 * @see javax.servlet.jsp.tagext.TagSupport#doAfterBody()
	 * If still promotion to display, iterate the body again with next promotion
	 */
    public int doAfterBody() throws JspException {
        if (onlyIfHavePromotion) {
            return EVAL_PAGE;
        }
        first = false;
        count++;
        if (still == 0) {
            return EVAL_PAGE;
        } else {
            if (setInfoForBody()) {
                return EVAL_BODY_AGAIN;
            }
            return EVAL_PAGE;
        }
    }

    /**
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
    public int doEndTag() throws JspException {
        currentPromotion = null;
        this.pageContext.removeAttribute("tagPromotionLinkTarget");
        this.pageContext.removeAttribute("tagPromotionLink");
        this.pageContext.removeAttribute("tagPromotion");
        this.pageContext.removeAttribute("tagPromotionFirst");
        this.pageContext.removeAttribute("tagPromotionLast");
        this.pageContext.removeAttribute("tagPromotionCount");
        this.pageContext.removeAttribute("tagPromotionPDF");
        return EVAL_PAGE;
    }

    /**
	 * set the marketing spot wanted
	 * @param spot the marketing spot
	 */
    public void setSpot(String spot) {
        this.spot = spot;
    }

    /**
	 * set the specifique id (destination id by exemple) for specifique promotion
	 * @param relatedId the specifique id
	 */
    public void setRelatedId(String relatedId) {
        this.relatedId = relatedId;
    }

    /**
	 * set the type
	 * @param type
	 */
    public void setType(String type) {
        this.type = type;
    }

    /**
	 * set the max of spot iteration
	 * @param maxitem number of max spot display
	 */
    public void setMaxitem(String maxitem) {
        this.maxitem = maxitem;
    }

    /**
	 * get the iteration count
	 * @return count
	 */
    public int getCount() {
        return count;
    }

    /**
	 * @return true if first promotion
	 */
    public boolean isFirst() {
        return first;
    }

    /**
	 * @return true if last promotion
	 */
    public boolean isLast() {
        return last;
    }

    /**
	 * @param onlyIfHavePromotion if true, the tag will only test if promotion for this condition exist, else (false or default) the tag will iterate and load promotion information
	 */
    public void setOnlyIfHavePromotion(boolean onlyIfHavePromotion) {
        this.onlyIfHavePromotion = onlyIfHavePromotion;
    }
}

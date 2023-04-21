package org.shopformat.domain.marketing.content;

import java.util.Arrays;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import org.shopformat.domain.shared.DomainObject;

@Entity
public class ContentPage implements DomainObject<Long> {

    private static final String[] GLOBAL_ELEMENTS = { ".main", ".logo", ".homeNav", ".catNav", ".brandNav", ".accountNav", ".searchControl", ".basketSummary" };

    private static final String[] HOME_ELEMENTS = { ".iconNav", ".newProducts", ".topSellers", ".giftIdeas", ".bargains", ".promotions" };

    private static final String[] CONTACT_ELEMENTS = { ".contactForm" };

    private static final String[] POLICY_ELEMENTS = { ".deliveryMatrix" };

    public static final String[] DEFAULT_GLOBAL_MARKUP = { "<div id=\"container\">", "<div id=\"header\">", ".logo", ".accountNav", ".homeNav", ".searchControl", "</div>", "<div id=\"nav\">", ".brandNav", ".catNav", "</div>", "<div id=\"main\">", ".main", "<div class=\"clear\"></div>", "</div>", "<div id=\"footer\">", ".footer", "</div>", "</div>" };

    public static final String[] DEFAULT_HOME_MARKUP = { "<div id=\"departmentContainer1\">", ".iconNav", "content1", ".giftIdeas", "content2", ".bargains", "content3", "</div>", "<div id=\"departmentContainer2\">", "content4", ".newProducts", "content5", ".topSellers", "</div>" };

    public static final String[] DEFAULT_CONTACT_MARKUP = { "<div id=\"home1\">", "content1", ".contactForm", "content2" };

    public static final String[] DEFAULT_POLICY_MARKUP = { "content1", ".delivery", "content2" };

    public static final String[] DEFAULT_MARKUP = { "content1" };

    private Long id;

    private String displayId;

    private String name;

    private String description;

    private List<String> elements;

    private List<ContentElement> contentElements;

    private boolean fixed;

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Transient
    public String[] getPermittedElements() {
        if (fixed) {
            if (displayId.equals("global")) {
                return GLOBAL_ELEMENTS;
            } else if (displayId.equals("policy")) {
                return POLICY_ELEMENTS;
            } else if (displayId.equals("contact")) {
                return CONTACT_ELEMENTS;
            } else if (displayId.equals("about") || displayId.equals("orderComplete")) {
                return null;
            } else {
                return HOME_ELEMENTS;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Transient
    public List<String> getElements() {
        if (displayId.equals("global")) {
            return Arrays.asList(DEFAULT_GLOBAL_MARKUP);
        } else if (displayId.equals("policy")) {
            return Arrays.asList(DEFAULT_POLICY_MARKUP);
        } else if (displayId.equals("contact")) {
            return Arrays.asList(DEFAULT_CONTACT_MARKUP);
        } else if (displayId.equals("about") || displayId.equals("orderComplete")) {
            return Arrays.asList(DEFAULT_MARKUP);
        } else {
            return Arrays.asList(DEFAULT_HOME_MARKUP);
        }
    }

    public void setElements(List<String> elements) {
        this.elements = elements;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contentPage")
    public List<ContentElement> getContentElements() {
        return contentElements;
    }

    public void setContentElements(List<ContentElement> contentElements) {
        this.contentElements = contentElements;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public String getDisplayId() {
        return displayId;
    }

    public void setDisplayId(String displayId) {
        this.displayId = displayId;
    }
}

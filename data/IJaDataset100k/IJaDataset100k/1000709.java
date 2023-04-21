package com.goodcodeisbeautiful.syndic8.atom10;

/**
 * 
 * <pre>
   atomSource =
    element atom:source {
        atomCommonAttributes,
        (atomAuthor*
          & atomCategory*
          & atomContributor*
          & atomGenerator?
          & atomIcon?
          & atomId?
          & atomLink*
          & atomLogo?
          & atomRights?
          & atomSubtitle?
          & atomTitle?
          & atomUpdated?
          & extensionElement*)
    } * </pre>
 * @author hata
 *
 */
public class Atom10Source extends Atom10AbstractContainer {

    /** source */
    public static final String TAG_SOURCE = "source";

    private Atom10Generator m_generator;

    private Atom10Icon m_icon;

    private Atom10Logo m_logo;

    private Atom10TextConstruct m_subtitle;

    /**
	 * Constructor.
	 */
    public Atom10Source() {
        this(null);
    }

    /**
	 * Constructor.
	 * @param parent is a parent node.
	 */
    public Atom10Source(Atom10CommonAttributes parent) {
        super(parent);
    }

    /**
	 * Get generator.
	 * @return generator or null.
	 */
    public Atom10Generator getGenerator() {
        return m_generator;
    }

    /**
	 * Set generator.
	 * @param generator is a new generator.
	 */
    public void setGenerator(Atom10Generator generator) {
        if (m_generator != null) m_generator.setParentCommonAttributes(null);
        m_generator = generator;
        if (m_generator != null) m_generator.setParentCommonAttributes(this);
    }

    /**
	 * Get icon.
	 * @return icon or null.
	 */
    public Atom10Icon getIcon() {
        return m_icon;
    }

    /**
	 * Set icon.
	 * @param icon is a new icon.
	 */
    public void setIcon(Atom10Icon icon) {
        if (m_icon != null) m_icon.setParentCommonAttributes(null);
        m_icon = icon;
        if (m_icon != null) m_icon.setParentCommonAttributes(this);
    }

    /**
	 * Get logo.
	 * @return logo.
	 */
    public Atom10Logo getLogo() {
        return m_logo;
    }

    /**
	 * Set logo.
	 * @param logo is a new logo.
	 */
    public void setLogo(Atom10Logo logo) {
        if (m_logo != null) m_logo.setParentCommonAttributes(null);
        m_logo = logo;
        if (m_logo != null) m_logo.setParentCommonAttributes(this);
    }

    /**
	 * Set subtitle.
	 * @return subtitle.
	 */
    public Atom10TextConstruct getSubtitle() {
        return m_subtitle;
    }

    /**
	 * Set a new subtitle.
	 * @param subtitle is a new subtitle.
	 */
    public void setSubtitle(Atom10TextConstruct subtitle) {
        if (m_subtitle != null) m_subtitle.setParentCommonAttributes(null);
        m_subtitle = subtitle;
        if (m_subtitle != null) m_subtitle.setParentCommonAttributes(this);
    }
}

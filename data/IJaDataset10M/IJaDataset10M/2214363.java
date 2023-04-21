package org.torweg.pulse.component.core.display;

import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import org.torweg.pulse.configuration.Configuration;
import org.torweg.pulse.service.PulseException;
import org.torweg.pulse.util.INamed;
import org.torweg.pulse.util.NamedComparator;

/**
 * The {@code StyleConfiguration} provides the CSS-styles to be used for display
 * as well as for the style-selection of the pulse website-administration within
 * the viewport.
 * 
 * @author Daniel Dietz
 * @version $Revision$
 */
@XmlRootElement(name = "style-configuration")
@XmlAccessorOrder(XmlAccessOrder.UNDEFINED)
@XmlAccessorType(XmlAccessType.NONE)
public class StyleConfiguration extends Configuration {

    /**
	 * The serialVersionUID.
	 */
    private static final long serialVersionUID = 4171731482728839893L;

    /**
	 * Indicates whether the parent {@code Style} is to be inherited should the
	 * current {@code SitemapNode} not have a {@code Style} applied.
	 */
    @XmlElement(name = "no-style-select-mode")
    private NoStyleSelectMode noStyleSelectMode;

    /**
	 * The {@code Style}s.
	 */
    @XmlElementWrapper(name = "styles")
    @XmlElement(name = "style")
    private final Set<Style> styles = new TreeSet<Style>(new NamedComparator());

    /**
	 * Default constructor.
	 */
    @Deprecated
    protected StyleConfiguration() {
        super();
    }

    /**
	 * Returns the matching {@code Style} for the given name.
	 * 
	 * @param name
	 *            the name
	 * 
	 * @return the requested {@code Style} for the given name if a matching
	 *         {@code Style} exists, {@code null} otherwise
	 */
    public final Style getStyle(final String name) {
        for (Style style : this.styles) {
            if (style.getName().equals(name)) {
                return style;
            }
        }
        return null;
    }

    /**
	 * Returns the default {@code Style}.
	 * 
	 * @return the default {@code Style}
	 * 
	 * @throws PulseException
	 *             if:
	 *             <ul>
	 *             <li>NOT AT LEAST ONE default {@code Style} exists</li>
	 *             <li>MORE THAN ONE default {@code Style}s exist</li>
	 *             </ul>
	 */
    public final Style getDefaultStyle() {
        Style defaultStyle = null;
        boolean existsDefault = false;
        for (Style style : this.styles) {
            if (existsDefault && style.isDefaultStyle()) {
                throw new PulseException("There must be NO MORE THAN ONE default style configured.");
            }
            if (style.isDefaultStyle()) {
                defaultStyle = style;
                existsDefault = true;
            }
        }
        if (defaultStyle == null) {
            throw new PulseException("There must be AT LEAST ONE default style configured.");
        }
        return defaultStyle;
    }

    /**
	 * Returns a randomly selected {@code Style}.
	 * 
	 * @return a randomly selected {@code Style}
	 */
    public final Style getRandomStyle() {
        return (Style) this.styles.toArray()[(new Random()).nextInt(this.styles.size())];
    }

    /**
	 * Returns the {@code NoStyleSelectMode}. The default would be
	 * {@code NoStyleSelectMode.INHERIT_FROM_PARENT_DEFAULT} if not configured.
	 * 
	 * @return noStyleSelectMode
	 */
    public final NoStyleSelectMode getNoStyleSelectMode() {
        if (this.noStyleSelectMode == null) {
            return NoStyleSelectMode.INHERIT_FROM_PARENT_DEFAULT;
        }
        return this.noStyleSelectMode;
    }

    /**
	 * Defines a CSS-style for the {@code StyleConfiguration}.
	 * 
	 * @author Daniel Dietz
	 * @version $Revision$
	 */
    @XmlRootElement(name = "style")
    @XmlAccessorOrder(XmlAccessOrder.UNDEFINED)
    @XmlAccessorType(XmlAccessType.NONE)
    public static final class Style implements INamed {

        /**
		 * The name of the CSS-style.
		 */
        @XmlAttribute(name = "name")
        private String name;

        /**
		 * The path to the CSS.
		 */
        @XmlAttribute(name = "css")
        private String css;

        /**
		 * Indicates the default style.
		 */
        @XmlAttribute(name = "default-style")
        private Boolean defaultStyle;

        /**
		 * Default constructor.
		 */
        @Deprecated
        protected Style() {
            super();
        }

        /**
		 * Returns the name.
		 * 
		 * @return the name
		 */
        public String getName() {
            return this.name;
        }

        /**
		 * Returns the CSS-path.
		 * 
		 * @return the css
		 */
        public String getCss() {
            return this.css;
        }

        /**
		 * Returns whether the current {@code Style} is the default CSS-style.
		 * 
		 * @return the defaultStyle, {@code false} if the defaultStyle is
		 *         {@code null}
		 */
        public boolean isDefaultStyle() {
            if (this.defaultStyle == null) {
                return false;
            }
            return this.defaultStyle;
        }
    }

    /**
	 * Defines constants which determine how the {@code Style} is to be chosen
	 * should a {@code SitemapNode} not have a style set.
	 * 
	 * @author Daniel Dietz
	 * @version $Revision$
	 */
    public enum NoStyleSelectMode {

        /**
		 * DEFAULT: Always choose default theme.
		 */
        DEFAULT, /**
		 * RANDOM: randomly choose a style.
		 */
        RANDOM, /**
		 * INHERIT_FROM_PARENT_DEFAULT: Inherits the style from the first parent
		 * {@code SitemapNode} which has a style assigned or the default style
		 * if no style could be retrieved form any of the parent
		 * {@code SitemapNode}s.
		 */
        INHERIT_FROM_PARENT_DEFAULT, /**
		 * INHERIT_FROM_PARENT_RANDOM: Inherits the style from the first parent
		 * {@code SitemapNode} which has a style assigned or randomly selects a
		 * style if no style could be retrieved form any of the parent
		 * {@code SitemapNode}s.
		 */
        INHERIT_FROM_PARENT_RANDOM
    }
}

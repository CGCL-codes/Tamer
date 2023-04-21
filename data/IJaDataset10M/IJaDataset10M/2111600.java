package net.sf.oval.localization.context;

import net.sf.oval.context.OValContext;

/**
 * @author Sebastian Thomschke
 */
public class ToStringValidationContextRenderer implements OValContextRenderer {

    public static final ToStringValidationContextRenderer INSTANCE = new ToStringValidationContextRenderer();

    /**
	 * {@inheritDoc}
	 */
    public String render(final OValContext ovalContext) {
        return ovalContext.toString();
    }
}

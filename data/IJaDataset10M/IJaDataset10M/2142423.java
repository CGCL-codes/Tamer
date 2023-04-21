package org.emftext.language.office.resource.office.mopp;

public class OfficeDynamicTokenStyler {

    /**
	 * This method is called to dynamically style tokens.
	 * 
	 * @param resource the TextResource that contains the token
	 * @param token the token to obtain a style for
	 * @param staticStyle the token style as set in the editor preferences (is
	 * <code>null</code> if syntax highlighting for the token is disabled)
	 */
    public org.emftext.language.office.resource.office.IOfficeTokenStyle getDynamicTokenStyle(org.emftext.language.office.resource.office.IOfficeTextResource resource, org.emftext.language.office.resource.office.IOfficeTextToken token, org.emftext.language.office.resource.office.IOfficeTokenStyle staticStyle) {
        return staticStyle;
    }
}

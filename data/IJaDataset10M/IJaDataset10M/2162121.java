package net.solarnetwork.node.settings;

/**
 * A string setting selected from a list of possible values.
 * 
 * <p>The {@link #getValueTitles()} is required for this setting, and 
 * provides the list of possible setting values for the user to choose from.</p>
 * 
 * @author matt
 * @version $Revision: 2053 $
 */
public interface MultiValueSettingSpecifier extends TextFieldSettingSpecifier {
}

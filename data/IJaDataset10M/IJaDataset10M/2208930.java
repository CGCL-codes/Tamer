package jezuch.utils.starmapper3.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jezuch.utils.ConcatenationList;
import jezuch.utils.parameters.Validator;

/**
 * This helper class contains methods to simplify {@link Setting}s handling.
 * The methods in this class can be used to derive {@link NormalSetting}s and
 * {@link IndexedSetting}s and at the same time store them in a {@link List},
 * which is exposed in an unmodifiable form in a final field {@link #settings}.
 * The helper can have a "parent" helper from which it "inherits" settings.
 * Settings from the parent helper are contained in the list in
 * {@link #settings} field and any changes to the parent helper's settings are
 * reflected in this list. Duplicate settings (with identical paths) will appear
 * twice (or more).
 * 
 * @author ksobolewski
 */
public class SettingsHelper {

    private final List<Setting> privateSettings = new ArrayList<Setting>();

    /**
	 * An umodifiable version of the list of {@link Setting}s derived using
	 * this helper class.
	 */
    public final List<Setting> settings;

    /**
	 * Constructs a new {@link SettingsHelper} with no parent.
	 */
    public SettingsHelper() {
        this.settings = Collections.unmodifiableList(privateSettings);
    }

    /**
	 * Constructs a new {@link SettingsHelper} with the given parent.
	 */
    public SettingsHelper(SettingsHelper parent) {
        this.settings = new ConcatenationList<Setting>(Collections.unmodifiableList(privateSettings), parent.settings);
    }

    /**
	 * Derives a new {@link NormalSetting} from a {@link NormalSettingBase base}
	 * and a subpath, and registers it as a known setting.
	 * 
	 * @param <T>
	 *            the {@link NormalSetting}'s type
	 * @param base
	 *            the base path
	 * @param type
	 *            the {@link NormalSetting}'s type's {@link Class}
	 * @param defaultValue
	 *            the {@link NormalSetting}'s default value
	 * @param validator
	 *            the {@link NormalSetting}'s {@link Validator}
	 * @param subpath
	 *            the {@link NormalSetting}'s subpath
	 * @return the derived {@link NormalSetting}
	 */
    public <T> NormalSetting<T> deriveSetting(NormalSettingBase base, Class<T> type, T defaultValue, Validator<T> validator, String... subpath) {
        NormalSetting<T> ret = base.derive(type, defaultValue, validator, subpath);
        privateSettings.add(ret);
        return ret;
    }

    /**
	 * Derives a new {@link IndexedSetting} from a
	 * {@link NormalSettingBase base} and a subpath (subpattern), and registers
	 * it as a known setting.
	 * 
	 * @param <T>
	 *            the {@link IndexedSetting}'s type
	 * @param base
	 *            the base path
	 * @param type
	 *            the {@link IndexedSetting}'s type's {@link Class}
	 * @param defaultValue
	 *            the {@link IndexedSetting}'s default value
	 * @param validator
	 *            the {@link IndexedSetting}'s {@link Validator}
	 * @param subpattern
	 *            the {@link IndexedSetting}'s subpattern
	 * @return the derived {@link IndexedSetting}
	 */
    public <T> IndexedSetting<T> deriveIndexed(NormalSettingBase base, Class<T> type, T defaultValue, Validator<T> validator, String... subpattern) {
        IndexedSetting<T> ret = base.deriveIndexed(type, defaultValue, validator, subpattern);
        privateSettings.add(ret);
        return ret;
    }

    /**
	 * Derives a new {@link IndexedSetting} from a
	 * {@link IndexedSettingBase base} and a subpath (subpattern), and registers
	 * it as a known setting.
	 * 
	 * @param <T>
	 *            the {@link IndexedSetting}'s type
	 * @param base
	 *            the base path
	 * @param type
	 *            the {@link IndexedSetting}'s type's {@link Class}
	 * @param defaultValue
	 *            the {@link IndexedSetting}'s default value
	 * @param validator
	 *            the {@link IndexedSetting}'s {@link Validator}
	 * @param subpattern
	 *            the {@link IndexedSetting}'s subpattern
	 * @return the derived {@link IndexedSetting}
	 */
    public <T> IndexedSetting<T> deriveIndexed(IndexedSettingBase base, Class<T> type, T defaultValue, Validator<T> validator, String... subpattern) {
        IndexedSetting<T> ret = base.derive(type, defaultValue, validator, subpattern);
        privateSettings.add(ret);
        return ret;
    }
}

package javax.security.sasl;

import javax.security.auth.callback.ChoiceCallback;

/**
 * This callback is used by {@link SaslClient} and {@link SaslServer} to obtain
 * a realm given a list of realm choices.
 *
 * @since 1.5
 */
public class RealmChoiceCallback extends ChoiceCallback {

    /**
   * Constructs a <code>RealmChoiceCallback</code> with a prompt, a list of
   * choices and a default choice.
   *
   * @param prompt the non-null prompt to use to request the realm.
   * @param choices the non-null list of realms to choose from.
   * @param defaultChoice the choice to be used as the default when the list of
   * choices is displayed. It is an index into the <code>choices</code> array.
   * @param multiple <code>true</code> if multiple choices allowed;
   * <code>false</code> otherwise.
   * @throws IllegalArgumentException if <code>prompt</code> is <code>null</code>
   * or empty, if <code>choices</code> has a length of <code>0</code>, if any
   * element from <code>choices</code> is <code>null</code> or empty, or if
   * <code>defaultChoice</code> does not fall within the array boundary of
   * <code>choices</code>.
   */
    public RealmChoiceCallback(String prompt, String[] choices, int defaultChoice, boolean multiple) {
        super(prompt, choices, defaultChoice, multiple);
    }
}

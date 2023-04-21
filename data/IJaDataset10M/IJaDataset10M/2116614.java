package org.imogene.android.preference;

import org.imogene.android.util.base64.Base64;
import org.imogene.android.util.encryption.EncryptionManager;
import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

public class EncryptedPreference extends Preference {

    public EncryptedPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public CharSequence getSummary() {
        final String enc = getPersistedString(null);
        if (enc != null) {
            return new String(EncryptionManager.getInstance(getContext()).decrypt(Base64.decodeBase64(enc.getBytes())));
        }
        return getPersistedString(getContext().getString(android.R.string.unknownName));
    }
}

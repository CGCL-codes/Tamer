package org.jowidgets.tools.verify;

import org.jowidgets.common.verify.IInputVerifier;

public final class OkVerifier {

    public static final IInputVerifier INSTANCE = new IInputVerifier() {

        @Override
        public boolean verify(final String currentValue, final String input, final int start, final int end) {
            return true;
        }
    };

    private OkVerifier() {
    }
}

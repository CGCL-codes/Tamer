package com.wix.gui.utils;

import com.jix.gui.InstallProgressPanel;

public final class ValidationEvent {

    private final JAdvancedPanel panel;

    private final boolean validated, canGoBack, requestConfirmationOnExit, letUserExit;

    public ValidationEvent(JAdvancedPanel panel, boolean validated) {
        this.panel = panel;
        this.validated = validated;
        this.canGoBack = true;
        this.requestConfirmationOnExit = true;
        this.letUserExit = true;
    }

    public ValidationEvent(JAdvancedPanel panel, boolean validated, boolean canGoBack, boolean requestConfirmationOnExit) {
        this.panel = panel;
        this.validated = validated;
        this.canGoBack = canGoBack;
        this.requestConfirmationOnExit = requestConfirmationOnExit;
        this.letUserExit = true;
    }

    public ValidationEvent(InstallProgressPanel panel, boolean validated, boolean canGoBack, boolean requestConfirmationOnExit, boolean letUserExit) {
        this.panel = panel;
        this.validated = validated;
        this.canGoBack = canGoBack;
        this.requestConfirmationOnExit = requestConfirmationOnExit;
        this.letUserExit = letUserExit;
    }

    public final JAdvancedPanel getPanel() {
        return panel;
    }

    public final boolean isValidated() {
        return validated;
    }

    public final boolean canGoBack() {
        return canGoBack;
    }

    public final boolean requestConfirmationOnExit() {
        return requestConfirmationOnExit;
    }

    public final boolean letUserExit() {
        return letUserExit;
    }
}

package org.eclipse.pde.visualization.dependency.analysis;

import org.eclipse.pde.visualization.dependency.views.PluginVisualizationView;

public class CurrentError extends ErrorReporting {

    ErrorReporting currentError;

    public CurrentError(PluginVisualizationView view, Object bundle, ErrorReporting currentError) {
        super(view, bundle);
        this.currentError = currentError;
    }

    public String getErrorMessage() {
        return currentError.getErrorMessage() + " (Click here to suppress)";
    }

    public void handleError() {
        this.view.handleSuppressError();
    }
}

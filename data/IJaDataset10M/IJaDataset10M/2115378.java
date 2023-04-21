package net.sf.beatrix.ui.detectoreditor.pages.details;

import net.sf.beatrix.core.DetectorConfiguration;
import net.sf.beatrix.core.event.DetectorConfigurationChangedEvent;
import net.sf.beatrix.ui.forms.AbstractDetailsPage;
import net.sf.beatrix.util.event.listener.ChangeListener;

public abstract class AbstractDCDetailsPage extends AbstractDetailsPage {

    private DetectorConfiguration detectorConfiguration;

    private ChangeListener<DetectorConfigurationChangedEvent> detectorListener = new ChangeListener<DetectorConfigurationChangedEvent>() {

        @Override
        public void changed(DetectorConfigurationChangedEvent event) {
            refresh();
        }
    };

    protected void setDetectorConfiguration(DetectorConfiguration detectorConfiguration) {
        if (this.detectorConfiguration != null) {
            this.detectorConfiguration.removeChangeListener(detectorListener);
        }
        this.detectorConfiguration = detectorConfiguration;
        this.detectorConfiguration.addChangeListener(detectorListener);
    }

    protected DetectorConfiguration getDetectorConfiguration() {
        return detectorConfiguration;
    }

    @Override
    public void dispose() {
        if (detectorConfiguration != null) {
            detectorConfiguration.removeChangeListener(detectorListener);
        }
        super.dispose();
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public boolean isStale() {
        return false;
    }

    @Override
    public boolean setFormInput(Object input) {
        return false;
    }
}

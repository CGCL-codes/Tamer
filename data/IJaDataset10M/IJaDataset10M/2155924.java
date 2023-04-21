package org.jampa.gui.wizard.podcastadder;

import org.eclipse.jface.wizard.Wizard;

public class PodcastAdderWizard extends Wizard {

    private PodcastAdderPage page;

    String _podcastName;

    String _podcastUrl;

    public PodcastAdderWizard() {
        super();
        setNeedsProgressMonitor(true);
    }

    public void addPages() {
        page = new PodcastAdderPage();
        addPage(page);
    }

    public String getPodcastName() {
        return _podcastName;
    }

    public String getPodcastUrl() {
        return _podcastUrl;
    }

    @Override
    public boolean performFinish() {
        _podcastName = page.getPodcastName();
        _podcastUrl = page.getPodcastUrl();
        return true;
    }
}

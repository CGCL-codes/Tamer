package zenbad.web.main.repositories.artifact;

import zenbad.business.configuration.ArtifactRepositoryObject;
import zenbad.web.base.AbstractPanel;

public class ArtifactRepositoryPanel extends AbstractPanel {

    private static final long serialVersionUID = 7781247399845750566L;

    public ArtifactRepositoryPanel(final String id, final ArtifactRepositoryModal modal, final ArtifactRepositoryObject repository) {
        super(id);
        add(new ArtifactRepositoryForm("form", modal, repository));
    }
}

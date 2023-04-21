package se.issi.magnolia.module.blossom.dialog;

import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.ItemType;
import info.magnolia.context.MgnlContext;
import org.apache.jackrabbit.value.StringValue;
import org.springframework.beans.factory.InitializingBean;
import se.issi.magnolia.module.blossom.gui.BlossomConfiguredDialog;
import se.issi.magnolia.module.blossom.support.RepositoryUtils;
import javax.jcr.RepositoryException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultBlossomDialogRegistry implements BlossomDialogRegistry, InitializingBean {

    private static final String DIALOGS_PATH = "/modules/blossom/dialogs/autodetected";

    private DialogDescriptionBuilder descriptionBuilder;

    private Map<String, BlossomDialogDescription> dialogs = new HashMap<String, BlossomDialogDescription>();

    public BlossomDialogDescription getDialogDescription(String name) {
        return this.dialogs.get(name);
    }

    public void setDescriptionBuilder(DialogDescriptionBuilder descriptionBuilder) {
        this.descriptionBuilder = descriptionBuilder;
    }

    public void registerDialogFactory(Object factoryObject) throws RepositoryException {
        addDialogDescription(descriptionBuilder.buildDescription(factoryObject));
    }

    public void registerDialogFactories(final Object handler) throws RepositoryException {
        List<BlossomDialogDescription> descriptions = descriptionBuilder.buildDescriptions(handler);
        for (BlossomDialogDescription description : descriptions) {
            addDialogDescription(description);
        }
    }

    protected void addDialogDescription(BlossomDialogDescription dialogDescription) throws RepositoryException {
        dialogs.put(dialogDescription.getName(), dialogDescription);
        writeDialogDefinition(dialogDescription);
    }

    protected void writeDialogDefinition(BlossomDialogDescription dialogDescription) throws RepositoryException {
        Content content = MgnlContext.getSystemContext().getHierarchyManager("config").getContent(DIALOGS_PATH);
        Content content1 = content.createContent(dialogDescription.getName(), ItemType.CONTENTNODE);
        content1.createNodeData("class", new StringValue(BlossomConfiguredDialog.class.getName()));
        content.save();
    }

    public void afterPropertiesSet() throws Exception {
        RepositoryUtils.createEmptyConfigNode(DIALOGS_PATH);
        if (descriptionBuilder == null) descriptionBuilder = new DialogDescriptionBuilder();
    }
}

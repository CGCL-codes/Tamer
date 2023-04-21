package se.issi.magnolia.module.blossom;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import se.issi.magnolia.module.blossom.dialog.BlossomDialogRegistry;
import se.issi.magnolia.module.blossom.dialog.DefaultBlossomDialogRegistry;
import se.issi.magnolia.module.blossom.dialog.DefaultDialogCreator;
import se.issi.magnolia.module.blossom.dialog.DialogCreator;
import se.issi.magnolia.module.blossom.paragraph.BlossomParagraphRegistry;
import se.issi.magnolia.module.blossom.paragraph.DefaultBlossomParagraphRegistry;
import se.issi.magnolia.module.blossom.template.BlossomTemplateRegistry;
import se.issi.magnolia.module.blossom.template.DefaultBlossomTemplateRegistry;

public class BlossomConfiguration implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    public void setParagraphRegistry(BlossomParagraphRegistry paragraphRegistry) {
        BlossomModule.paragraphRegistry = paragraphRegistry;
    }

    public void setTemplateRegistry(BlossomTemplateRegistry templateRegistry) {
        BlossomModule.templateRegistry = templateRegistry;
    }

    public void setDialogRegistry(BlossomDialogRegistry dialogRegistry) {
        BlossomModule.dialogRegistry = dialogRegistry;
    }

    public void setDialogCreator(DialogCreator dialogCreator) {
        BlossomModule.dialogCreator = dialogCreator;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void afterPropertiesSet() throws Exception {
        if (BlossomModule.paragraphRegistry == null) BlossomModule.paragraphRegistry = createDefaultInstance(DefaultBlossomParagraphRegistry.class);
        if (BlossomModule.templateRegistry == null) BlossomModule.templateRegistry = createDefaultInstance(DefaultBlossomTemplateRegistry.class);
        if (BlossomModule.dialogRegistry == null) BlossomModule.dialogRegistry = createDefaultInstance(DefaultBlossomDialogRegistry.class);
        if (BlossomModule.dialogCreator == null) BlossomModule.dialogCreator = createDefaultInstance(DefaultDialogCreator.class);
    }

    @SuppressWarnings("unchecked")
    private <T> T createDefaultInstance(Class<T> clazz) {
        return (T) applicationContext.getAutowireCapableBeanFactory().createBean(clazz);
    }
}

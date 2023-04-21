package rql4j.cms;

import org.apache.log4j.Logger;
import rql4j.builder.*;
import rql4j.domain.IoData;
import rql4j.iodata.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class RqlCommand {

    Queue<RqlBuilder> commands;

    ConcurrentMap<RqlBuilder, IoData> resultMap;

    IoData result = null;

    RemoteCallWorker worker;

    private final Logger logger;

    public RqlCommand(RemoteCallWorker worker) {
        this.logger = Logger.getLogger(RqlCommand.class);
        this.commands = new ConcurrentLinkedQueue<RqlBuilder>();
        this.resultMap = new ConcurrentHashMap<RqlBuilder, IoData>();
        this.worker = worker;
        this.result = null;
    }

    public IoAdminstration getResult(AdministrationBuilder administrationBuilder) {
        return new IoAdminstration(checkExecuted(administrationBuilder));
    }

    public IoProject getResult(ProjectBuilder projectBuilder) {
        return new IoProject(checkExecuted(projectBuilder));
    }

    public IoProjects getResult(ProjectsBuilder projectsBuilder) {
        return new IoProjects(checkExecuted(projectsBuilder));
    }

    public IoElements getResult(ElementsBuilder elementsBuilder) {
        return new IoElements(checkExecuted(elementsBuilder));
    }

    public IoElt getResult(EltBuilder eltBuilder) {
        return new IoElt(checkExecuted(eltBuilder));
    }

    public IoFile getResult(FileBuilder fileBuilder) {
        return new IoFile(checkExecuted(fileBuilder));
    }

    public IoFiles getResult(FilesBuilder filesBuilder) {
        return new IoFiles(checkExecuted(filesBuilder));
    }

    public IoTemplate getResult(TemplateBuilder templateBuilder) {
        return new IoTemplate(checkExecuted(templateBuilder));
    }

    public IoPage getResult(PageBuilder pageBuilder) {
        return new IoPage(checkExecuted(pageBuilder));
    }

    public IoPages getResult(PagesBuilder pagesBuilder) {
        return new IoPages(checkExecuted(pagesBuilder));
    }

    public IoLinks getResult(LinksBuilder linksBuilder) {
        return new IoLinks(checkExecuted(linksBuilder));
    }

    public IoLanguageVariants getResult(LanguageVariantsBuilder languageVariantsBuilder) {
        return new IoLanguageVariants(checkExecuted(languageVariantsBuilder));
    }

    public IoProjectVariants getResult(ProjectVariantsBuilder projectVariantsBuilder) {
        return new IoProjectVariants((checkExecuted(projectVariantsBuilder)));
    }

    public IoFolders getResult(FoldersBuilder foldersBuilder) {
        return new IoFolders(checkExecuted(foldersBuilder));
    }

    public IoTemplates getResult(TemplatesBuilder templatesBuilder) {
        return new IoTemplates(checkExecuted(templatesBuilder));
    }

    public IoCategories getResult(CategoriesBuilder categoriesBuilder) {
        return new IoCategories(checkExecuted(categoriesBuilder));
    }

    public IoKeywords getResult(KeywordsBuilder keywordsBuilder) {
        return new IoKeywords(checkExecuted(keywordsBuilder));
    }

    public IoTemplateVariants getResult(TemplateVariantsBuilder templateVariantsBuilder) {
        return new IoTemplateVariants(checkExecuted(templateVariantsBuilder));
    }

    public IoData getResult(RqlBuilder rqlBuilder) {
        return checkExecuted(rqlBuilder);
    }

    private IoData checkExecuted(RqlBuilder builder) {
        IoData ioData;
        if (!commands.contains(builder)) {
            commands.add(builder);
        }
        execute();
        return resultMap.get(builder);
    }

    public void addCommand(RqlBuilder command) {
        logger.debug("Add RqlCommand: " + command.toString());
        this.commands.add(command);
    }

    public void execute() {
        logger.debug("Execute RqlCommand " + commands.size() + " " + commands.toString());
        while (!commands.isEmpty()) {
            RqlBuilder command = commands.poll();
            this.result = command.execute(this.worker);
            if (command instanceof AdministrationBuilder && result.getLogin() != null && result.getLogin().getGuid() != null) worker.setLoginGuid(result.getLogin().getGuid());
            if (command instanceof AdministrationBuilder && result.getServer() != null && result.getServer().getKey() != null) worker.setSesionKey(result.getServer().getKey());
            this.resultMap.put(command, result);
        }
    }
}

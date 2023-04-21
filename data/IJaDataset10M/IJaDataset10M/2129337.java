package org.jzonic.yawiki.commands.admin;

import java.util.List;
import org.jconfig.Configuration;
import org.jconfig.ConfigurationManager;
import org.jzonic.core.Command;
import org.jzonic.core.CommandException;
import org.jzonic.core.WebContext;
import org.jzonic.jlo.LogManager;
import org.jzonic.jlo.Logger;
import org.jzonic.yawiki.repository.Domain;
import org.jzonic.yawiki.repository.PageInfo;
import org.jzonic.yawiki.repository.VersionDBManager;
import org.jzonic.yawiki.repository.WikiEngine;
import org.jzonic.yawiki.util.FileUtils;

/**
 *
 * @author  mecky
 */
public class ReducePage implements Command {

    private static final Logger logger = LogManager.getLogger("org.jzonic.yawiki");

    private static final Configuration cm = ConfigurationManager.getConfiguration("jZonic");

    public ReducePage() {
    }

    public String execute(WebContext webContext) throws CommandException {
        try {
            String page = webContext.getRequestParameter("name");
            WikiEngine we = new WikiEngine();
            Domain domain = (Domain) webContext.getSessionParameter("DOMAIN");
            VersionDBManager vManager = new VersionDBManager();
            PageInfo currentInfo = vManager.getVersionInfo(page, domain);
            String content = we.getCurrentPlainPageContent(page, domain);
            String fileName = we.getFileName(page, domain);
            try {
                FileUtils.deletePage(fileName);
            } catch (Exception e) {
            }
            List versions = vManager.getVersionHistory(page, domain);
            for (int i = 0; i < (versions.size() - 1); i++) {
                PageInfo info = (PageInfo) versions.get(i);
                if (info.getRevision() > 0) {
                    fileName = we.getVersionFileName(page, info.getRevision(), domain);
                    try {
                        FileUtils.deletePage(fileName);
                    } catch (Exception e) {
                    }
                }
            }
            vManager.deleteVersionHistory(page, domain);
            we.addVersion(page, content, currentInfo.getAuthor(), currentInfo.getMode(), domain);
            return null;
        } catch (Exception e) {
            logger.fatal("execute", e);
            throw new CommandException("Error while reducing page:" + e.getMessage());
        }
    }
}

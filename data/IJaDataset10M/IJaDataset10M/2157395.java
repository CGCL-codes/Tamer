package org.osmorc.issuesubmitter;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.util.net.HttpConfigurable;
import org.osmorc.issuesubmitter.soap.JiraSoapService;
import org.osmorc.issuesubmitter.soap.JiraSoapServiceService;
import org.osmorc.issuesubmitter.soap.JiraSoapServiceServiceLocator;
import org.osmorc.issuesubmitter.soap.RemoteIssue;
import javax.xml.rpc.ServiceException;
import java.io.IOException;

/**
 * Author: Robert F. Beeger (robert@beeger.net)
 */
public class JIRAIssueSubmitter {

    public String reportError(String user, String password, String summary, String description, String stacktrace) throws ServiceException, IOException {
        JiraSoapServiceService jiraSoapServiceGetter = new JiraSoapServiceServiceLocator();
        HttpConfigurable.getInstance().prepareURL(jiraSoapServiceGetter.getJirasoapserviceV2Address());
        JiraSoapService jiraSoapService = jiraSoapServiceGetter.getJirasoapserviceV2();
        String token = jiraSoapService.login(user, password);
        RemoteIssue issue = new RemoteIssue();
        issue.setProject(PROJECT_KEY);
        issue.setType(ERROR_ISSUE_TYPE_ID);
        issue.setSummary(summary);
        issue.setDescription(description + "\n\n" + stacktrace);
        issue.setPriority(PRIORITY_ID);
        issue.setAssignee("");
        issue.setEnvironment(getEvironment());
        RemoteIssue returnedIssue = jiraSoapService.createIssue(token, issue);
        return returnedIssue.getKey();
    }

    private String getEvironment() {
        IdeaPluginDescriptor pluginDescriptor = ApplicationManager.getApplication().getPlugin(PluginId.getId("Osmorc"));
        return String.format("Idea build #%s, Osmorc version %s", ApplicationInfo.getInstance().getBuildNumber(), pluginDescriptor.getVersion());
    }

    private static final String PROJECT_KEY = "OSMORC";

    private static final String ERROR_ISSUE_TYPE_ID = "102";

    private static final String PRIORITY_ID = "5";
}

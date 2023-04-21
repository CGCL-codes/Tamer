package com.lewisshell.helpyourself.web;

import java.io.*;
import java.util.*;
import org.apache.tapestry.*;
import com.lewisshell.helpyourself.*;
import com.lewisshell.helpyourself.psa.*;

/**
 * 
 * @author RichardL
 */
public abstract class ConfigurationPage extends FormPage {

    private static final String FOUND_MESSAGE = "Found";

    private static final String NOT_FOUND_MESSAGE = "Not Found";

    public abstract String getReturnPage();

    public abstract void setReturnPage(String value);

    public abstract Set<Folder> getTransientSelectededPrivateTags();

    public abstract void setTransientSelectededPrivateTags(Set<Folder> value);

    public abstract String getCatalogMessage();

    public abstract void setCatalogMessage(String value);

    public abstract String getInstallationMessage();

    public abstract void setInstallationMessage(String value);

    public abstract Configuration getConfiguration();

    public abstract void setConfiguration(Configuration value);

    public abstract String getSelectedTab();

    public abstract void setSelectedTab(String value);

    public abstract Boolean getTransientSecure();

    public abstract void setTransientSecure(Boolean value);

    public abstract String getTransientUsername();

    public abstract void setTransientUsername(String value);

    public abstract String getTransientPassword();

    public abstract void setTransientPassword(String value);

    public abstract String getTransientPasswordConfirmation();

    public abstract void setTransientPasswordConfirmation(String value);

    public abstract void setPasswordVerificationMessage(String value);

    public void setConfigurationForTransition(Configuration value) {
        this.setConfiguration(value);
        this.setSelectedTab(null);
    }

    public boolean isMainTabSelected() {
        return this.getSelectedTab() == null || this.getSelectedTab().equals("main");
    }

    public void mainTabAction(IRequestCycle cycle) {
        this.setSelectedTab("main");
    }

    public boolean isPrivateTagsTabSelected() {
        return "privateTags".equals(this.getSelectedTab());
    }

    public void privateTagsTabAction(IRequestCycle cycle) {
        this.setSelectedTab("privateTags");
    }

    public boolean isSecurityTabSelected() {
        return "security".equals(this.getSelectedTab());
    }

    public void securityTabAction(IRequestCycle cycle) {
        this.setSelectedTab("security");
    }

    public boolean isConfigurationValid() {
        return this.getConfiguration().isValid();
    }

    public String getCatalogFileName() {
        return this.getConfiguration().getCatalogFileName();
    }

    public void setCatalogFileName(String value) {
        this.getConfiguration().setCatalogFileName(value);
    }

    public void catalogTestAction(IRequestCycle cycle) {
        File catalog = this.getConfiguration().getCatalogFile();
        if (catalog == null || !catalog.exists()) {
            this.setCatalogMessage(NOT_FOUND_MESSAGE);
        } else {
            this.setCatalogMessage(FOUND_MESSAGE);
        }
    }

    public void installationTestAction(IRequestCycle cycle) {
        File installDir = this.getConfiguration().getInstallationDir();
        if (installDir == null || !installDir.exists()) {
            this.setInstallationMessage(NOT_FOUND_MESSAGE);
        } else {
            this.setInstallationMessage(FOUND_MESSAGE);
        }
    }

    public String getInstallationDir() {
        return this.getConfiguration().getInstallationDirName();
    }

    public void setInstallationDir(String value) {
        this.getConfiguration().setInstallationDirName(value);
    }

    public String getCatalogWatchInterval() {
        return String.valueOf(this.getConfiguration().getCatalogWatchIntervalMinutes());
    }

    public void setCatalogWatchInterval(String value) {
        int interval = value == null || value.trim().length() == 0 ? -1 : Integer.parseInt(value);
        this.getConfiguration().setCatalogWatchIntervalMinutes(interval);
    }

    public boolean getIgnoreLocalHostHits() {
        return this.getConfiguration().getIgnoreLocalHostHits();
    }

    public void setIgnoreLocalHostHits(boolean value) {
        this.getConfiguration().setIgnoreLocalHostHits(value);
    }

    public String getServerURLPrefix() {
        return this.getConfiguration().getServerURLPrefix();
    }

    public void setServerURLPrefix(String value) {
        this.getConfiguration().setServerURLPrefix(value);
    }

    private void returnToPage(IRequestCycle cycle) {
        cycle.activate(this.getReturnPage() == null ? "Home" : this.getReturnPage());
    }

    public Collection getTags() {
        Collection tags = new TreeSet(Folder.FOLDER_COMPARATOR);
        tags.addAll(((Global) this.getGlobal()).getCatalog().getFolderRepository().findAllRootTags());
        return tags;
    }

    public Collection getSelectedTags() {
        Set<Folder> selectedTags = this.getTransientSelectededPrivateTags();
        if (this.getTransientSelectededPrivateTags() == null) {
            selectedTags = new HashSet();
            this.setTransientSelectededPrivateTags(selectedTags);
            FolderRepository folderRepository = ((Global) this.getGlobal()).getCatalog().getFolderRepository();
            for (int id : this.getConfiguration().getPrivateFolderIds()) {
                Folder tag = folderRepository.findTagById(id);
                if (tag != null) {
                    selectedTags.add(tag);
                }
            }
        }
        return selectedTags;
    }

    public boolean isSecure() {
        Boolean transientSecure = this.getTransientSecure();
        return transientSecure == null ? this.getConfiguration().isSecure() : transientSecure;
    }

    public void setSecure(boolean value) {
        if (value) {
            this.setTransientSecure(true);
        } else {
            this.getConfiguration().setSecure(false);
        }
    }

    public String getUsername() {
        String transientUsername = this.getTransientUsername();
        return transientUsername == null ? this.getConfiguration().getUsername() : transientUsername;
    }

    public void setUsername(String value) {
        this.setTransientUsername(value);
    }

    public String getPassword() {
        String transientPassword = this.getTransientPassword();
        return transientPassword == null ? this.getConfiguration().getPassword() : transientPassword;
    }

    public void setPassword(String value) {
        this.setTransientPassword(value);
    }

    public String getPasswordConfirmation() {
        return this.getTransientPasswordConfirmation();
    }

    public void setPasswordConfirmation(String value) {
        this.setTransientPasswordConfirmation(value);
    }

    public void verifyPasswordAction(IRequestCycle cycle) {
        if (!this.isSecure()) {
            this.setPasswordVerificationMessage("Security not enabled - no need to verify password");
            return;
        }
        if (this.getPassword() != null) {
            if (this.getPassword().equals(this.getPasswordConfirmation())) {
                this.getConfiguration().setSecure(this.isSecure());
                this.getConfiguration().setUsername(this.getUsername());
                this.getConfiguration().setPassword(this.getPassword());
                this.setPasswordVerificationMessage("Password Verified");
            } else {
                this.setTransientPasswordConfirmation(null);
                this.setPasswordVerificationMessage("Passwords DO NOT match");
            }
        }
    }

    public void okAction(IRequestCycle requestCycle) {
        if (this.getTransientSelectededPrivateTags() != null) {
            this.getConfiguration().setPrivateTags(this.getTransientSelectededPrivateTags());
        }
        ((Global) this.getGlobal()).setConfiguration(this.getConfiguration().save());
        this.returnToPage(requestCycle);
    }

    public void resetAction() {
        this.getConfiguration().load();
    }

    public void cancelAction(IRequestCycle requestCycle) {
        this.returnToPage(requestCycle);
    }

    public boolean isReturnAvailable() {
        return this.getReturnPage() != null;
    }
}

package net.naijatek.myalumni.modules.admin.presentation.form;

import org.apache.struts.upload.FormFile;
import net.naijatek.myalumni.framework.struts.MyAlumniBaseForm;

public class SystemConfigForm extends MyAlumniBaseForm {

    private String systemConfigId;

    private String sessionTimeout;

    private String serverUrl;

    private String albumUrl;

    private String forumUrl;

    private String rssHeader;

    private String rssUrl;

    private String hasDormitory;

    private String birthdayNotification;

    private String logoFileName;

    private String orgFirstYear;

    private String organizationName;

    private String organizationShortName;

    private String orgEmail;

    private String orgAboutUs;

    private String orgIntro;

    private FormFile logoUpload;

    private String twitteruser;

    private String twitterpswd;

    private String memberUserName;

    private String memberFirstName;

    private String memberLastName;

    private String memberEmail;

    private String memberPassword;

    public String getTwitterpswd() {
        return twitterpswd;
    }

    public void setTwitterpswd(String twitterpswd) {
        this.twitterpswd = twitterpswd;
    }

    public String getTwitteruser() {
        return twitteruser;
    }

    public void setTwitteruser(String twitteruser) {
        this.twitteruser = twitteruser;
    }

    public String getOrgAboutUs() {
        return orgAboutUs;
    }

    public void setOrgAboutUs(String orgAboutUs) {
        this.orgAboutUs = orgAboutUs;
    }

    public String getHasDormitory() {
        return hasDormitory;
    }

    public void setHasDormitory(String hasDormitory) {
        this.hasDormitory = hasDormitory;
    }

    public String getRssHeader() {
        return rssHeader;
    }

    public void setRssHeader(String rssHeader) {
        this.rssHeader = rssHeader;
    }

    public String getRssUrl() {
        return rssUrl;
    }

    public void setRssUrl(String rssUrl) {
        this.rssUrl = rssUrl;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(String sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public String getSystemConfigId() {
        return systemConfigId;
    }

    public void setSystemConfigId(String systemConfigId) {
        this.systemConfigId = systemConfigId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrgEmail() {
        return orgEmail;
    }

    public void setOrgEmail(String orgEmail) {
        this.orgEmail = orgEmail;
    }

    public String getOrgFirstYear() {
        return orgFirstYear;
    }

    public void setOrgFirstYear(String orgFirstYear) {
        this.orgFirstYear = orgFirstYear;
    }

    public String getOrganizationShortName() {
        return organizationShortName;
    }

    public void setOrganizationShortName(String organizationShortName) {
        this.organizationShortName = organizationShortName;
    }

    public String getAlbumUrl() {
        return albumUrl;
    }

    public void setAlbumUrl(String albumUrl) {
        this.albumUrl = albumUrl;
    }

    public String getForumUrl() {
        return forumUrl;
    }

    public void setForumUrl(String forumUrl) {
        this.forumUrl = forumUrl;
    }

    public String getBirthdayNotification() {
        return birthdayNotification;
    }

    public void setBirthdayNotification(String birthdayNotification) {
        this.birthdayNotification = birthdayNotification;
    }

    public String getLogoFileName() {
        return logoFileName;
    }

    public void setLogoFileName(String logoFileName) {
        this.logoFileName = logoFileName;
    }

    public FormFile getLogoUpload() {
        return logoUpload;
    }

    public void setLogoUpload(FormFile logoUpload) {
        this.logoUpload = logoUpload;
    }

    public String getOrgIntro() {
        return orgIntro;
    }

    public void setOrgIntro(String orgIntro) {
        this.orgIntro = orgIntro;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    public String getMemberFirstName() {
        return memberFirstName;
    }

    public void setMemberFirstName(String memberFirstName) {
        this.memberFirstName = memberFirstName;
    }

    public String getMemberLastName() {
        return memberLastName;
    }

    public void setMemberLastName(String memberLastName) {
        this.memberLastName = memberLastName;
    }

    public String getMemberPassword() {
        return memberPassword;
    }

    public void setMemberPassword(String memberPassword) {
        this.memberPassword = memberPassword;
    }

    public String getMemberUserName() {
        return memberUserName;
    }

    public void setMemberUserName(String memberUserName) {
        this.memberUserName = memberUserName;
    }
}

package org.snipsnap.feeder.rss;

import gabriel.Principal;
import org.radeox.api.engine.RenderEngine;
import org.radeox.api.engine.context.RenderContext;
import snipsnap.api.container.Components;
import snipsnap.api.label.Labels;
import snipsnap.api.render.context.SnipRenderContext;
import snipsnap.api.security.Permissions;
import snipsnap.api.snip.Access;
import snipsnap.api.snip.Comments;
import snipsnap.api.snip.Modified;
import snipsnap.api.snip.Snip;
import snipsnap.api.snip.SnipSpaceFactory;
import snipsnap.api.snip.attachment.Attachments;
import snipsnap.api.snip.links.Links;
import snipsnap.api.user.User;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;
import java.sql.Timestamp;
import java.util.List;

/**
 * Encapsulates a Snip for RSS as RSS channels are more fine granular
 * than Snip content.
 *
 * @author Stephan J. Schmidt
 * @version $Id:RssSnip.java 1859 2006-08-08 15:10:07 +0200 (Tue, 08 Aug 2006) leo $
 */
public class RssSnip implements Snip {

    private Snip snip;

    private String url;

    private String title;

    private String content;

    public RssSnip(snipsnap.api.snip.Snip snip) {
        this.title = snip.getName();
        this.content = snip.getContent();
        this.snip = snip;
        this.url = "";
    }

    public RssSnip(snipsnap.api.snip.Snip snip, String content) {
        this(snip);
        this.content = content;
    }

    public RssSnip(Snip snip, String content, String title) {
        this(snip, content);
        this.title = title;
    }

    public RssSnip(Snip snip, String content, String title, String url) {
        this(snip, content, title);
        this.url = "#" + url.replace(' ', '_');
    }

    public int getVersion() {
        return snip.getVersion();
    }

    public void setVersion(int version) {
    }

    public void setParentName(String name) {
    }

    public void setOwner(Principal principal) {
    }

    public boolean isOwner(Principal principal) {
        return snip.isOwner(principal);
    }

    public String getParentName() {
        return snip.getParentName();
    }

    public String getCommentedName() {
        return snip.getCommentedName();
    }

    public void setCommentedName(String name) {
    }

    public Writer appendPathTo(Writer writer) throws IOException {
        return snip.appendPathTo(writer);
    }

    public void setApplication(String applicationOid) {
    }

    public String getApplication() {
        return snip.getApplication();
    }

    public String getName() {
        return title;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getNameEncoded() {
        return snip.getNameEncoded() + url;
    }

    public String toXML() {
        return getXMLContent();
    }

    public String getXMLContent() {
        RenderEngine engine = (RenderEngine) Components.getComponent(snipsnap.api.container.Components.DEFAULT_ENGINE);
        RenderContext context = new SnipRenderContext(snip, SnipSpaceFactory.getInstance());
        context.setParameters(snipsnap.api.app.Application.get().getParameters());
        return engine.render(content, context);
    }

    public void handle(HttpServletRequest request) {
        return;
    }

    public String getLink() {
        return null;
    }

    public Principal getOwner() {
        return snip.getOwner();
    }

    public Writer appendTo(Writer s) throws IOException {
        return snip.appendTo(s);
    }

    public Access getAccess() {
        return snip.getAccess();
    }

    public Modified getModified() {
        return snip.getModified();
    }

    public boolean isWeblog() {
        return snip.isWeblog();
    }

    public boolean isNotWeblog() {
        return snip.isNotWeblog();
    }

    public void addPermission(String permission, String role) {
        return;
    }

    public void setPermissions(Permissions permissions) {
        return;
    }

    public Permissions getPermissions() {
        return snip.getPermissions();
    }

    public String getOUser() {
        return snip.getOUser();
    }

    public void setOUser(User oUser) {
        return;
    }

    public void setOUser(String oUser) {
        return;
    }

    public Attachments getAttachments() {
        return snip.getAttachments();
    }

    public void setAttachments(Attachments attachments) {
        return;
    }

    public Labels getLabels() {
        return snip.getLabels();
    }

    public void setLabels(Labels labels) {
        return;
    }

    public Links getBackLinks() {
        return snip.getBackLinks();
    }

    public Links getSnipLinks() {
        return snip.getSnipLinks();
    }

    public void setBackLinks(Links backLinks) {
        return;
    }

    public void setSnipLinks(Links snipLinks) {
        return;
    }

    public int getViewCount() {
        return snip.getViewCount();
    }

    public void setViewCount(int count) {
        return;
    }

    public int incViewCount() {
        return snip.getViewCount();
    }

    public Timestamp getCTime() {
        return snip.getCTime();
    }

    public void setCTime(Timestamp cTime) {
        return;
    }

    public Timestamp getMTime() {
        return snip.getMTime();
    }

    public void setMTime(Timestamp mTime) {
        return;
    }

    public String getCUser() {
        return snip.getCUser();
    }

    public void setCUser(User cUser) {
        return;
    }

    public void setCUser(String cUser) {
        return;
    }

    public String getMUser() {
        return snip.getMUser();
    }

    public void setMUser(User mUser) {
        return;
    }

    public void setMUser(String mUser) {
        return;
    }

    public List getChildren() {
        return snip.getChildren();
    }

    public void setCommentedSnip(Snip comment) {
        return;
    }

    public snipsnap.api.snip.Snip getCommentedSnip() {
        return snip.getCommentedSnip();
    }

    public boolean isComment() {
        return snip.isComment();
    }

    public Comments getComments() {
        return snip.getComments();
    }

    public List getChildrenDateOrder() {
        return snip.getChildrenDateOrder();
    }

    public List getChildrenModifiedOrder() {
        return snip.getChildrenModifiedOrder();
    }

    public void addSnip(Snip snip) {
        return;
    }

    public void removeSnip(Snip snip) {
        return;
    }

    public snipsnap.api.snip.Snip getParent() {
        return snip.getParent();
    }

    public void setDirectParent(Snip parentSnip) {
        return;
    }

    public void setParent(Snip parentSnip) {
        return;
    }

    public String getShortName() {
        return title;
    }

    public void setName(String name) {
        return;
    }

    public void setContent(String content) {
        return;
    }

    public String getAttachmentString() {
        return snip.getAttachmentString();
    }

    public snipsnap.api.snip.Snip copy(String newName) {
        return new RssSnip(snip, content, title, url);
    }
}

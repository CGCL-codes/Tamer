package com.dotmarketing.portlets.links.factories;

import static com.dotmarketing.business.PermissionAPI.PERMISSION_WRITE;
import java.util.List;
import com.dotmarketing.beans.Host;
import com.dotmarketing.beans.Identifier;
import com.dotmarketing.beans.Inode;
import com.dotmarketing.beans.WebAsset;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.PermissionAPI;
import com.dotmarketing.cache.LiveCache;
import com.dotmarketing.cache.WorkingCache;
import com.dotmarketing.db.DotHibernate;
import com.dotmarketing.factories.HostFactory;
import com.dotmarketing.factories.IdentifierFactory;
import com.dotmarketing.factories.InodeFactory;
import com.dotmarketing.factories.WebAssetFactory;
import com.dotmarketing.menubuilders.RefreshMenus;
import com.dotmarketing.portlets.files.model.File;
import com.dotmarketing.portlets.folders.model.Folder;
import com.dotmarketing.portlets.htmlpages.model.HTMLPage;
import com.dotmarketing.portlets.links.model.Link;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.struts.ActionException;

/**
 *
 * @author  will
 */
public class LinkFactory {

    private static PermissionAPI permissionAPI = APILocator.getPermissionAPI();

    /**
	 * @param permissionAPI the permissionAPI to set
	 */
    public static void setPermissionAPI(PermissionAPI permissionAPIRef) {
        permissionAPI = permissionAPIRef;
    }

    public static java.util.List getChildrenLinkByOrder(Inode i) {
        DotHibernate dh = new DotHibernate(Link.class);
        dh.setQuery("from inode in class com.dotmarketing.portlets.links.model.Link where ? in inode.parents.elements order by sort_order");
        dh.setParam(i.getInode());
        return dh.list();
    }

    public static java.util.List getActiveLinks() {
        DotHibernate dh = new DotHibernate(Link.class);
        dh.setQuery("from inode in class com.dotmarketing.portlets.links.model.Link where type='links'");
        return dh.list();
    }

    public static java.util.List getLinksByOrderAndParent(String orderby, Inode o) {
        DotHibernate dh = new DotHibernate(Link.class);
        dh.setQuery("from inode in class com.dotmarketing.portlets.links.model.Link where ? in inode.parents.elements and working = " + com.dotmarketing.db.DbConnectionFactory.getDBTrue() + " or live = " + com.dotmarketing.db.DbConnectionFactory.getDBTrue() + " order by " + orderby);
        dh.setParam(o.getInode());
        return dh.list();
    }

    public static java.util.List getLinksByOrder(String orderby) {
        DotHibernate dh = new DotHibernate(Link.class);
        dh.setQuery("from inode in class com.dotmarketing.portlets.links.model.Link where type='links' and working = " + com.dotmarketing.db.DbConnectionFactory.getDBTrue() + " or live = " + com.dotmarketing.db.DbConnectionFactory.getDBTrue() + " order by " + orderby);
        return dh.list();
    }

    public static java.util.List getLinkChildrenByCondition(Inode o, String condition) {
        try {
            DotHibernate dh = new DotHibernate(Link.class);
            dh.setSQLQuery("SELECT {links.*} from links links, tree tree, inode links_1_ where tree.parent = ? and tree.child = links.inode and links_1_.inode = links.inode and links_1_.type='links' and " + condition + " order by url, sort_order");
            dh.setParam(o.getInode());
            return dh.list();
        } catch (Exception e) {
            Logger.error(LinkFactory.class, "getLinkChildrenByCondition failed:" + e, e);
        }
        return new java.util.ArrayList();
    }

    public static java.util.List getLinkByCondition(String condition) {
        DotHibernate dh = new DotHibernate(Link.class);
        dh.setQuery("from inode in class com.dotmarketing.portlets.links.model.Link where type='links' and " + condition + " order by url, sort_order");
        return dh.list();
    }

    public static java.util.List getLinkChildren(Inode o) {
        try {
            DotHibernate dh = new DotHibernate(Link.class);
            dh.setQuery("from inode in class com.dotmarketing.portlets.links.model.Link where ? in inode.parents.elements order by inode, sort_order");
            dh.setParam(o.getInode());
            return dh.list();
        } catch (Exception e) {
            Logger.error(LinkFactory.class, "getLinkChildren failed:" + e, e);
        }
        return new java.util.ArrayList();
    }

    public static Link getLinkByLiveAndFolderAndTitle(Inode parent, String title) {
        try {
            DotHibernate dh = new DotHibernate(Link.class);
            dh.setQuery("from inode in class com.dotmarketing.portlets.links.model.Link where ? in inode.parents.elements and title =  ? and live = " + com.dotmarketing.db.DbConnectionFactory.getDBTrue());
            dh.setParam(parent.getInode());
            dh.setParam(title);
            return (Link) dh.load();
        } catch (Exception e) {
            Logger.error(LinkFactory.class, "getLinkByLiveAndFolderAndTitle failed:" + e, e);
        }
        return new Link();
    }

    public static java.util.List existsLink(String uri) {
        DotHibernate dh = new DotHibernate(Link.class);
        dh.setQuery("from identifier in class com.dotmarketing.beans.Identifier where uri = ?");
        dh.setParam(uri);
        return ((java.util.List) dh.list());
    }

    public static Link getLinkByFriendlyName(String friendlyName) {
        DotHibernate dh = new DotHibernate(Link.class);
        dh.setQuery("from inode in class com.dotmarketing.portlets.links.model.Link where friendly_name = ? and type='links' and live=" + com.dotmarketing.db.DbConnectionFactory.getDBTrue());
        dh.setParam(friendlyName);
        return (Link) dh.load();
    }

    public static com.dotmarketing.portlets.links.model.Link getLinkFromInode(String strInode, String userId) {
        Logger.debug(LinkFactory.class, "running getLinkFromInode(String strInode, String userId)");
        com.dotmarketing.beans.Inode inode = (com.dotmarketing.beans.Inode) com.dotmarketing.factories.InodeFactory.getInode(strInode, com.dotmarketing.beans.Inode.class);
        if (inode instanceof Link) {
            return ((com.dotmarketing.portlets.links.model.Link) inode);
        }
        if (inode instanceof File) {
            return ((com.dotmarketing.portlets.links.model.Link) LinkFactory.getLinkFromFile((File) inode, userId));
        }
        if (inode instanceof HTMLPage) {
            return ((com.dotmarketing.portlets.links.model.Link) LinkFactory.getLinkFromHTMLPage((HTMLPage) inode, userId));
        }
        return (new Link());
    }

    public static Link getLinkFromFile(File inFile, String userId) {
        Logger.debug(LinkFactory.class, "running getLinkFromFile(File inFile, String userId)");
        com.dotmarketing.beans.Identifier identifier = IdentifierFactory.getIdentifierByInode(inFile);
        StringBuffer url = new StringBuffer();
        String protocol = "http://";
        Host host = HostFactory.getParentHost(inFile);
        url.append(host.getHostname());
        url.append(identifier.getURI());
        Logger.debug(LinkFactory.class, "Identifier is " + protocol + url.toString() + "_self");
        java.util.List linkURIs = LinkFactory.existsLink(protocol + url.toString() + "_self");
        if (linkURIs.size() > 0) {
            Identifier linkIdentifier = (Identifier) linkURIs.get(0);
            return ((Link) IdentifierFactory.getWorkingChildOfClass(linkIdentifier, Link.class));
        } else {
            Link link = new Link();
            link.setTitle(inFile.getTitle());
            link.setFriendlyName(inFile.getFriendlyName());
            link.setProtocal(protocol);
            link.setUrl(url.toString());
            link.setTarget("_self");
            link.setInternal(true);
            return ((Link) link);
        }
    }

    public static Link getLinkFromHTMLPage(HTMLPage inHTMLPage, String userId) {
        Logger.debug(LinkFactory.class, "running getLinkFromHTMLPage(HTMLPage inHTMLPage String userId)");
        com.dotmarketing.beans.Identifier identifier = IdentifierFactory.getIdentifierByInode(inHTMLPage);
        java.lang.StringBuffer url = new java.lang.StringBuffer();
        String protocol = null;
        if (inHTMLPage.isHttpsRequired()) {
            protocol = "https://";
        } else {
            protocol = "http://";
        }
        Host host = HostFactory.getParentHost(inHTMLPage);
        url.append(host.getHostname());
        url.append(identifier.getURI());
        java.util.List linkURIs = LinkFactory.existsLink(protocol + url.toString() + "_self");
        if (linkURIs.size() > 0) {
            Identifier linkIdentifier = (Identifier) linkURIs.get(0);
            return ((Link) IdentifierFactory.getWorkingChildOfClass(linkIdentifier, Link.class));
        } else {
            Link link = new Link();
            Folder parentFolder = (Folder) InodeFactory.getParentOfClass(inHTMLPage, Folder.class);
            link.setTitle(inHTMLPage.getTitle());
            link.setFriendlyName(inHTMLPage.getFriendlyName());
            link.setProtocal(protocol);
            link.setUrl(url.toString());
            link.setTarget("_self");
            link.setInternal(true);
            WebAssetFactory.createAsset(link, userId, parentFolder);
            return ((Link) link);
        }
    }

    public static Link copyLink(Link currentLink, Folder parent) {
        Link newLink = new Link();
        newLink.copy(currentLink);
        newLink.setLocked(false);
        newLink.setLive(false);
        if (existsLinkWithTitleInFolder(currentLink.getTitle(), parent)) {
            newLink.setFriendlyName(currentLink.getFriendlyName() + " (COPY) ");
            newLink.setTitle(currentLink.getTitle() + " (COPY) ");
        } else {
            newLink.setFriendlyName(currentLink.getFriendlyName());
            newLink.setTitle(currentLink.getTitle());
        }
        newLink.setProtocal(currentLink.getProtocal());
        newLink.setLinkCode(currentLink.getLinkCode());
        newLink.setLinkType(currentLink.getLinkType());
        InodeFactory.saveInode(newLink);
        parent.addChild(newLink);
        com.dotmarketing.factories.IdentifierFactory.createNewIdentifier(newLink, parent);
        permissionAPI.copyPermissions(currentLink, newLink);
        return newLink;
    }

    /**
     * Method used to move a link from folder
     * @param currentLink link to move
     * @param parent new parent folder
     * @return true if the move succeced, false if another link with the same name exists on the destination
     */
    public static boolean moveLink(Link currentLink, Folder parent) {
        if (existsLinkWithTitleInFolder(currentLink.getTitle(), parent)) return false;
        Identifier identifier = com.dotmarketing.factories.IdentifierFactory.getParentIdentifier(currentLink);
        WebAsset workingWebAsset = (WebAsset) IdentifierFactory.getWorkingChildOfClass(identifier, Link.class);
        WebAsset liveWebAsset = (WebAsset) IdentifierFactory.getLiveChildOfClass(identifier, Link.class);
        Folder oldParent = (Folder) InodeFactory.getParentOfClass(workingWebAsset, Folder.class);
        oldParent.deleteChild(workingWebAsset);
        if ((liveWebAsset != null) && (liveWebAsset.getInode() > 0)) {
            oldParent.deleteChild(liveWebAsset);
        }
        parent.addChild(workingWebAsset);
        if ((liveWebAsset != null) && (liveWebAsset.getInode() > 0)) {
            parent.addChild(liveWebAsset);
        }
        Host newHost = HostFactory.getParentHost(parent);
        identifier.setHostInode(newHost.getInode());
        identifier.setURI(workingWebAsset.getURI(parent));
        InodeFactory.saveInode(identifier);
        RefreshMenus.deleteMenu(oldParent, parent);
        return true;
    }

    private static boolean existsLinkWithTitleInFolder(String title, Folder parent) {
        Link link = (Link) InodeFactory.getChildOfClassbyCondition(parent, Link.class, "title like '" + title + "'");
        return (link.getInode() > 0);
    }

    public static boolean renameLink(Link link, String newName, User user) throws Exception {
        if (!permissionAPI.doesUserHavePermission(link, PERMISSION_WRITE, user)) throw new ActionException(WebKeys.USER_PERMISSIONS_EXCEPTION);
        Folder folder = (Folder) InodeFactory.getParentOfClass(link, Folder.class);
        Identifier ident = IdentifierFactory.getIdentifierByInode(link);
        Link newLinkVersion = new Link();
        newLinkVersion.copy(link);
        newLinkVersion.setTitle(newName);
        newLinkVersion.setFriendlyName(newName);
        if (existsLinkWithTitleInFolder(newName, folder) || link.isLocked()) return false;
        List<Link> versions = IdentifierFactory.getVersionsandLiveandWorkingChildrenOfClass(ident, Link.class);
        for (Link version : versions) {
            version.setTitle(newName);
            version.setFriendlyName(newName);
            if (version.isLive()) LiveCache.addToLiveAssetToCache(version);
            if (version.isWorking()) WorkingCache.addToWorkingAssetToCache(version);
            InodeFactory.saveInode(version);
        }
        ident.setURI(link.getURI(folder));
        InodeFactory.saveInode(ident);
        RefreshMenus.deleteMenu(link);
        return true;
    }
}

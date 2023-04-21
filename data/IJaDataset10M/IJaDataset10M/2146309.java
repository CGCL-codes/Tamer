package com.j2biz.compote.plugins.admin;

import org.displaytag.decorator.TableDecorator;
import com.j2biz.compote.pojos.Plugin;
import com.j2biz.compote.util.SystemUtils;

/**
 * @author michelson
 * @version $$
 * @since 0.1
 * 
 *  
 */
public class PluginTableDecorator extends TableDecorator {

    /**
     * @return
     */
    public String getInstallState() {
        Plugin p = (Plugin) getCurrentRowObject();
        StringBuffer out = new StringBuffer();
        if (p.isPluginInstalled()) {
            out.append("installed");
        } else {
            out.append("<a href=\"PluginInstallAction.do?id=" + p.getId() + "\">");
            out.append("install");
            out.append("</a>");
        }
        return out.toString();
    }

    /**
     * @return
     */
    public String getName() {
        Plugin object = (Plugin) getCurrentRowObject();
        Long id = object.getId();
        StringBuffer nameStr = new StringBuffer();
        nameStr.append(SystemUtils.getImageFromBundle(getPageContext(), "imagesBundle", "icon.layout", "icon.layout.desc", "0") + SystemUtils.getPixelGif(getPageContext(), "5", "1"));
        nameStr.append("<a href=\"PluginAdminAction.do?method=info&id=" + id + "\">");
        nameStr.append(object.getName());
        nameStr.append("</a>");
        return nameStr.toString();
    }

    /**
     * @return
     */
    public String getActionLink() {
        Plugin object = (Plugin) getCurrentRowObject();
        Long id = object.getId();
        StringBuffer link = new StringBuffer();
        link.append("<table width=\"100%\" border=\"0\" cellspacing=\2\" cellpading=\"2\"><tr>");
        link.append("<td><a href=\"PluginAdminAction.do?method=edit" + "&id=" + id + "\">" + SystemUtils.getImageFromBundle(getPageContext(), "imagesBundle", "icon.edit", "icon.edit.desc", "0") + "</a></td>");
        if (object.getName().equals("core") || object.getName().equals("admin")) {
            link.append("<td>" + SystemUtils.getImageFromBundle(getPageContext(), "imagesBundle", "icon.remove.disabled", "icon.remove.desc", "0") + "</td>");
        } else {
            link.append("<td><a href=\"PluginAdminAction.do?method=removePluginConfirm&id=" + id + "\">" + SystemUtils.getImageFromBundle(getPageContext(), "imagesBundle", "icon.remove", "icon.remove.desc", "0") + "</a></td>");
        }
        link.append("</tr></table>");
        return link.toString();
    }
}

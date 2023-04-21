package net.sourceforge.processdash.net.cms;

import java.io.IOException;
import net.sourceforge.processdash.ui.web.TinyCGIBase;
import net.sourceforge.processdash.util.HTMLUtils;
import net.sourceforge.processdash.util.XMLUtils;

/** This class is used when a snippet editor needs to allow the user to 
 * select a list of items. 
 * 
 * @author Tuma
 *
 */
public abstract class AbstractListEditor extends TinyCGIBase {

    protected String getRequiredParam(String name) throws IOException {
        String result = getParameter(name);
        if (result == null || result.trim().length() == 0) throw new IOException(name + " must be specified");
        return result;
    }

    protected String getOptionalParam(String name, String defaultVal) {
        String result = getParameter(name);
        if (result == null || result.trim().length() == 0) return defaultVal; else return result;
    }

    @Override
    protected void writeContents() throws IOException {
        writeEditor();
    }

    protected void writeEditor() throws IOException {
        String listItemUri = getRequiredParam("itemUri");
        String namespace = getOptionalParam("ns", "$$$_");
        String itemType = getOptionalParam("itemType", "Metric");
        String newItemUri = "dash/snippets/snipEnum" + "?ns=" + namespace + "&type=" + itemType + "&uri=" + HTMLUtils.urlEncode(listItemUri) + "&defaults=true" + getExtraNewItemParams() + "&item=_ITEM_";
        out.write("<input type='hidden' name='cmsIgnored' id='");
        out.write(namespace);
        out.write(itemType);
        out.write("_uri' value='");
        out.write(esc(newItemUri));
        out.write("'/>");
        out.write("<div class='cmsListContainer'>");
        out.write("<div id='");
        out.write(namespace);
        out.write(itemType);
        out.write("_container'>");
        SnippetDataEnumerator.writeListOfItems(out, getTinyWebServer(), listItemUri, namespace, itemType, parameters);
        if ("true".equals(parameters.get("createNewItem"))) SnippetDataEnumerator.writeOneItem(out, getTinyWebServer(), listItemUri, namespace, itemType, parameters, "defaults=true", "autoNew");
        out.write("</div>");
        writeItemAdditionGui(namespace, itemType);
        out.write("</div>");
        out.write("<script type=\"text/javascript\">DashCMS.fixupSortableList('");
        out.write(namespace);
        out.write(itemType);
        out.write("_container', null);</script>");
    }

    protected String getExtraNewItemParams() {
        return "";
    }

    /**
     * Write the HTML for the GUI that the user will interact with to create
     * a new item in the list.
     */
    protected abstract void writeItemAdditionGui(String namespace, String itemType) throws IOException;

    protected static String esc(String s) {
        return XMLUtils.escapeAttribute(s);
    }
}

package org.middleheaven.ui.web.tags;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.global.Culture;
import org.middleheaven.global.text.GlobalLabel;
import org.middleheaven.global.text.LocalizationService;
import org.middleheaven.ui.components.MenuItem;

public class MenuTag extends AbstractBodyTagSupport {

    static final String SUB_MENU_TAG = "<submenu/>";

    StackItem masterStack;

    StackItem parentStack;

    private String varName = "menu";

    LinkedList<StackItem> iteratorStack = new LinkedList<StackItem>();

    private String rootClass;

    private String selectedItemClass;

    public void setVar(String varName) {
        this.varName = varName;
    }

    public void setRootClass(String rootClass) {
        this.rootClass = rootClass;
    }

    public void setSelectedItemClass(String selectedItemClass) {
        this.selectedItemClass = selectedItemClass;
    }

    public MenuItem getCurrentMenuItem() {
        return this.current.menu;
    }

    public void setRoot(MenuItem menu) {
        if (menu != null) {
            masterStack = new StackItem(menu);
        }
    }

    private LocalizationService localizationService;

    public int doStartTag() throws JspException {
        if (masterStack == null) {
            return SKIP_BODY;
        }
        this.localizationService = ServiceRegistry.getService(LocalizationService.class);
        buffer.delete(0, buffer.length());
        write("<ul ");
        if (id != null) {
            writeAttribute("id", id);
        }
        if (!(rootClass == null || rootClass.isEmpty())) {
            writeAttribute("class", rootClass);
        }
        write(">");
        if (masterStack.iterator.hasNext()) {
            return EVAL_BODY_BUFFERED;
        }
        return SKIP_BODY;
    }

    public void doInitBody() {
        iteratorStack.addFirst(masterStack);
        exposeMenu(masterStack.iterator.next());
    }

    private void exposeMenu(MenuItem item) {
        current = new StackItem(item);
        if (!item.isTitleLocalized()) {
            Culture culture = new TagContext(pageContext).getCulture();
            localizationService.getMessage(culture, new GlobalLabel(item.getTitle()), false);
        } else {
            item.setLabel(item.getTitle());
        }
        pageContext.setAttribute(varName, item);
    }

    StackItem current;

    StringBuilder buffer = new StringBuilder();

    private static class StackItem {

        public StackItem(MenuItem menu) {
            this.menu = menu;
            this.iterator = menu.getChildren().iterator();
            this.count = menu.getChildrenCount();
            if (count > 0) {
                widths = BigInteger.valueOf(100L).divideAndRemainder(BigInteger.valueOf(this.count));
            }
        }

        public boolean hasSubItems() {
            return count > 0;
        }

        private int indicator = 0;

        private BigInteger[] widths;

        public MenuItem menu;

        public Iterator<MenuItem> iterator;

        public int count;

        public BigInteger percentWitdh() {
            if (widths == null) {
                return null;
            }
            if (indicator == 0) {
                indicator = 1;
                return widths[0].add(widths[1]);
            } else {
                return widths[0];
            }
        }
    }

    public int doAfterBody() throws JspException {
        try {
            BodyContent bc = getBodyContent();
            buffer.append("\n<li>");
            buffer.append(bc.getString());
            buffer.append("\n</li>");
            bc.clearBody();
            getBodyContent().writeOut(getPreviousOut());
            getBodyContent().clear();
            if (current != null && current.hasSubItems()) {
                buffer.append("\n<ul>");
                iteratorStack.addFirst(current);
            }
            while (!iteratorStack.isEmpty()) {
                StackItem top = iteratorStack.getFirst();
                if (top.iterator.hasNext()) {
                    exposeMenu(top.iterator.next());
                    return EVAL_BODY_BUFFERED;
                } else {
                    balanceTag(buffer, "ul");
                    iteratorStack.removeFirst();
                }
            }
            return SKIP_BODY;
        } catch (IOException e) {
            throw new JspTagException(e.getMessage());
        }
    }

    private StringBuilder balanceTag(StringBuilder buffer, String tag) {
        int pos = buffer.indexOf("<" + tag + ">");
        int count = 0;
        while (pos >= 0) {
            count++;
            pos = buffer.indexOf("<" + tag + ">", pos + 1);
        }
        pos = buffer.indexOf("</" + tag + ">");
        while (pos >= 0) {
            count--;
            pos = buffer.indexOf("</" + tag + ">", pos + 1);
        }
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                buffer.append("\n</" + tag + ">");
            }
        } else if (count < 0) {
            for (int i = 0; i < -count; i++) {
                buffer.insert(0, "\n<" + tag + ">");
            }
        }
        return buffer;
    }

    public int doEndTag() throws JspTagException {
        writeLine(processBuffer(buffer));
        writeLine("</ul>");
        return EVAL_PAGE;
    }

    private String processBuffer(StringBuilder buffer) {
        BufferedReader reader = new BufferedReader(new StringReader(buffer.toString()));
        String line;
        RMenuItem root = new RMenuItem();
        RMenuItem currentRItem = null;
        RMenuItem parent = root;
        try {
            String[] stoppers = { "<li", "</li", "<ul", "</ul" };
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                int[] res = find(line, stoppers, 0);
                if (res[0] >= 0) {
                    do {
                        if (res[1] == 0) {
                            currentRItem = new RMenuItem();
                            currentRItem.addLine(line);
                        } else if (res[1] == 1) {
                            currentRItem.addLine(line);
                            parent.addChild(currentRItem);
                        } else if (res[1] == 2) {
                            parent = currentRItem;
                        } else if (res[1] == 3) {
                            parent = parent.parent;
                        }
                        res = find(line, stoppers, res[0] + 1);
                    } while (res[0] >= 0);
                } else {
                    if (currentRItem != null) {
                        currentRItem.addLine(line);
                    }
                }
            }
        } catch (IOException e) {
        }
        StringBuilder buffer2 = new StringBuilder();
        printMenu(buffer2, root);
        return buffer2.toString();
    }

    private static int[] find(String s, String[] options, int fromIndex) {
        for (int i = 0; i < options.length; i++) {
            int pos = s.indexOf(options[i], fromIndex);
            if (pos >= 0) {
                return new int[] { pos, i };
            }
        }
        return new int[] { -1, -1 };
    }

    private void printMenu(StringBuilder builder, RMenuItem item) {
        if (!item.children.isEmpty()) {
            StringBuilder subItens = new StringBuilder();
            for (RMenuItem sub : item.children) {
                printMenu(subItens, sub);
            }
            int pos = item.builder.indexOf(SUB_MENU_TAG);
            if (pos > 0) {
                item.builder.replace(pos, pos + SUB_MENU_TAG.length(), ensureOneSurroundByTag(balanceTag(subItens, "ul").toString(), "ul"));
            } else {
                item.builder.append(subItens.toString());
            }
        }
        builder.append(item.builder);
    }

    private String ensureOneSurroundByTag(String content, String tag) {
        if (content.startsWith("<" + tag + ">") && content.endsWith("</" + tag + ">")) {
            return content;
        } else {
            return "<" + tag + ">" + content + "</" + tag + ">";
        }
    }

    public void releaseState() {
        this.masterStack = null;
        this.buffer.delete(0, buffer.length());
    }

    private static class RMenuItem {

        StringBuilder builder = new StringBuilder();

        RMenuItem parent;

        List<RMenuItem> children = new LinkedList<RMenuItem>();

        public RMenuItem() {
        }

        public void addLine(String line) {
            builder.append(line).append("\n");
        }

        public void addChild(RMenuItem item) {
            item.parent = this;
            children.add(item);
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            toString(builder, this, 0);
            return builder.toString();
        }

        private static void toString(StringBuilder builder, RMenuItem item, int tabs) {
            for (int i = 0; i < tabs; i++) {
                builder.append("---");
            }
            builder.append(item.builder.toString().trim()).append("\n");
            for (RMenuItem m : item.children) {
                toString(builder, m, tabs + 1);
            }
        }
    }
}

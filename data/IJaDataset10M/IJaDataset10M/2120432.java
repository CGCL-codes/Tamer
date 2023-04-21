package ru.adv.db.app.request;

import java.util.*;
import ru.adv.util.*;
import ru.adv.db.handler.Handler;

/**
 * Описание атрибута tree дял  {@link GetInstruction}
 * @version $Revision: 1.8 $
 */
public class Tree implements ru.adv.db.base.link.TreeType {

    public static final String SELF = "self";

    public static final String SIBLING = "siblings";

    public static final String PARENT = "parent";

    public static final String ANCESTOR = "ancestors";

    public static final String CHILDREN = "children";

    public static final String DESCENDANT = "descendants";

    public static final String NONE = "none";

    private static HashSet defaultItems = new HashSet();

    static {
        defaultItems.add(SELF);
        defaultItems.add(SIBLING);
        defaultItems.add(PARENT);
        defaultItems.add(ANCESTOR);
        defaultItems.add(CHILDREN);
        defaultItems.add(DESCENDANT);
    }

    private HashMap items = new HashMap();

    private boolean none = false;

    /**
	* Конструктор, инициализируется строкой, которая имеет вид:
	* <PRE>
	* treeString ~ { none | (sibling|parent|ancestor|children|descendant)[+], ... }
	* + указывает на включение указанных нод для дальнейшей обработки
	* пример: "sibling+,ancestor"
	* </PRE>
	*/
    public Tree(String treeString) throws TreeParserException {
        String token;
        boolean plus = false;
        if (treeString == null) treeString = SIBLING;
        if (treeString.trim().equals(NONE)) {
            this.none = true;
        } else {
            for (Iterator i = Strings.split(treeString, ",").iterator(); i.hasNext(); ) {
                token = ((String) i.next()).trim();
                plus = false;
                if (token.endsWith("+")) {
                    plus = true;
                    token = token.substring(0, token.length() - 1);
                }
                if (defaultItems.contains(token)) {
                    items.put(token, new Boolean(plus));
                } else {
                    throw new TreeParserException("Illegal tree string: '" + treeString + "'");
                }
            }
        }
    }

    /**
	* Проверяет, установлен ли plus, для указанного типа tree 
	* {@link ru.adv.db.handler.Handler#TREE_ANCESTOR treeMode}
	* @param treeMode Одна из констант {@link ru.adv.db.handler.Handler#TREE_ANCESTOR constans}
	* @see ru.adv.db.handler.Handler#TREE_ANCESTOR
	*/
    public boolean isPlus(int treeMode) {
        switch(treeMode) {
            case Handler.TREE_SIBLING:
                return isSiblingPlus();
            case Handler.TREE_PARENT:
                return isParentPlus();
            case Handler.TREE_ANCESTOR:
                return isAncestorPlus();
            case Handler.TREE_CHILDREN:
                return isChildrenPlus();
            case Handler.TREE_DESCENDANT:
                return isDescendantPlus();
        }
        return false;
    }

    /**
	* установлено значение <code>none</code>
	*/
    public boolean isNone() {
        return this.none;
    }

    /**
	* установлено значение <code>self</code>
	*/
    public boolean isSelf() {
        return isSet(SELF);
    }

    /**
	* установлено значение <code>self+</code>
	*/
    public boolean isSelfPlus() {
        return isSetPlus(SELF);
    }

    /**
	* установлено только одно значение <code>self</code>
	*/
    public boolean isOnlySelf() {
        return items.size() == 1 && isSelf();
    }

    /**
	* установлено значение <code>sibling</code>
	*/
    public boolean isSibling() {
        return isSet(SIBLING);
    }

    /**
	* установлено значение <code>sibling+</code>
	*/
    public boolean isSiblingPlus() {
        return isSetPlus(SIBLING);
    }

    /**
	* установлено значение <code>parent</code>
	*/
    public boolean isParent() {
        return isSet(PARENT);
    }

    /**
	* установлено значение <code>parent+</code>
	*/
    public boolean isParentPlus() {
        return isSetPlus(PARENT);
    }

    /**
	* установлено значение <code>ancestor</code>
	*/
    public boolean isAncestor() {
        return isSet(ANCESTOR);
    }

    /**
	* установлено значение <code>ancestor+</code>
	*/
    public boolean isAncestorPlus() {
        return isSetPlus(ANCESTOR);
    }

    /**
	* установлено значение <code>children</code>
	*/
    public boolean isChildren() {
        return isSet(CHILDREN);
    }

    /**
	* установлено значение <code>children+</code>
	*/
    public boolean isChildrenPlus() {
        return isSetPlus(CHILDREN);
    }

    /**
	* установлено значение <code>descendant</code>
	*/
    public boolean isDescendant() {
        return isSet(DESCENDANT);
    }

    /**
	* установлено значение <code>descendant+</code>
	*/
    public boolean isDescendantPlus() {
        return isSetPlus(DESCENDANT);
    }

    /**
	* установлено значение item
	*/
    private boolean isSet(String item) {
        return items.containsKey(item);
    }

    /**
	* установлено значение item
	*/
    private boolean isSetPlus(String item) {
        return isSet(item) && ((Boolean) items.get(item)).booleanValue();
    }

    public String toString() {
        String str = "Tree:";
        if (isNone()) return str += NONE;
        for (Iterator i = items.keySet().iterator(); i.hasNext(); ) {
            String item = (String) i.next();
            boolean plus = ((Boolean) items.get(item)).booleanValue();
            str += item;
            if (plus) str += "+";
            if (i.hasNext()) str += ",";
        }
        return str;
    }
}

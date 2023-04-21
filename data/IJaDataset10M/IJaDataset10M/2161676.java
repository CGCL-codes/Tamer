package com.potix.zk.ui;

import com.potix.zk.mesg.MZk;
import com.potix.zk.ui.sys.ComponentsCtrl;
import com.potix.zk.ui.sys.ExecutionsCtrl;

/**
 * A representation of a component path.
 *
 * @author <a href="mailto:tomyeh@potix.com">tomyeh@potix.com</a>
 */
public class Path {

    private final String _path;

    public Path() {
        this((String) null);
    }

    public Path(String path) {
        _path = normalize(path);
    }

    public Path(String parent, String child) {
        this(parent == null || parent.length() == 0 ? child : child == null || child.length() == 0 ? parent : parent + '/' + child);
    }

    public Path(Path parent, String child) {
        this(parent != null ? parent.getPath() : null, child);
    }

    /** Returns the path of the specified component. */
    public Path(Component comp) {
        this(getPath(comp));
    }

    /** Returns the path (after normalized).
	 */
    public String getPath() {
        return _path;
    }

    /** Returns the component with this path.
	 * @exception ComponentNotFoundException is thrown if
	 */
    public Component getComponent() {
        return getComponent0(null, _path);
    }

    /** Returns the path of the specified component.
	 */
    public static final String getPath(Component comp) {
        final StringBuffer sb = new StringBuffer(64);
        for (; ; ) {
            if (sb.length() > 0) sb.insert(0, '/');
            final String compId = comp.getId();
            if (ComponentsCtrl.isAutoId(compId)) throw new UiException(MZk.AUTO_ID_NOT_ALLOWED_IN_PATH, comp);
            sb.insert(0, compId);
            IdSpace is = comp.getSpaceOwner();
            if (is instanceof Page) break;
            if (is == comp) {
                final Component p = ((Component) is).getParent();
                if (p == null) break;
                is = p.getSpaceOwner();
                if (is instanceof Page) break;
            }
            comp = (Component) is;
        }
        sb.insert(0, '/');
        return sb.toString();
    }

    /** Returns the component of the specified path.
	 * @exception ComponentNotFoundException is thrown if
	 */
    public static final Component getComponent(String path) {
        return getComponent0(null, normalize(path));
    }

    /** Returns the component of the specified path which is related
	 * to the specified ID space.
	 *
	 * @param is the current ID space. It is required only if path is related
	 * (in other words, not starting with / or //).
	 * @exception ComponentNotFoundException is thrown if
	 */
    public static final Component getComponent(IdSpace is, String path) {
        return getComponent0(is, normalize(path));
    }

    private static final Component getComponent0(IdSpace is, String path) {
        Component found = null;
        for (int j = 0, k; ; j = k + 1) {
            k = path.indexOf('/', j);
            if (k == 0) {
                if (path.length() == 1) throw new ComponentNotFoundException("Unable to resolve " + path + ". Cause: no component ID specified");
                final Page page = ExecutionsCtrl.getCurrentCtrl().getCurrentPage();
                if (path.charAt(1) == '/') {
                    k = path.indexOf('/', 2);
                    if (k < 0) throw new ComponentNotFoundException("Unable to resolve " + path + ". Cause: the result cannot be page.");
                    final String nm = path.substring(2, k);
                    is = page.getDesktop().getPage(nm);
                } else {
                    is = page;
                }
                continue;
            }
            final String nm = k >= 0 ? path.substring(j, k) : path.substring(j);
            if ("..".equals(nm)) {
                if (!(is instanceof Component)) throw new ComponentNotFoundException("Unable to resolve " + path + ". Cause: .. cannot be applied to page");
                final Component c = (Component) is;
                final Component p = c.getParent();
                is = p != null ? p.getSpaceOwner() : (IdSpace) c.getPage();
                if (k < 0) {
                    if (is instanceof Page) throw new ComponentNotFoundException("Unable to resolve " + path + ". Cause: the result cannot be page.");
                    return (Component) is;
                }
                continue;
            }
            if (is == null) throw new ComponentNotFoundException("The current ID space is required because path is related: " + path);
            final Component c = is.getFellow(nm);
            if (k < 0) return c;
            if (!(c instanceof IdSpace)) throw new ComponentNotFoundException("Unable to resolve " + path + ". Cause: " + c + " is not an ID space owner");
            is = (IdSpace) c;
        }
    }

    /**
	 * Normalizes the giving path.
	 * It removes consecutive slahses, ending slahes,
	 * redudant . and ..
	 */
    private static final String normalize(String path) {
        if (path == null) return "";
        final StringBuffer sb = new StringBuffer(path);
        boolean slash = false;
        for (int j = 0, len = sb.length(); j < len; ++j) {
            final boolean curslash = sb.charAt(j) == '/';
            if (curslash && slash && j != 1) {
                sb.deleteCharAt(j);
                --j;
                --len;
            }
            slash = curslash;
        }
        if (sb.length() > 1 && slash) sb.setLength(sb.length() - 1);
        while (sb.length() >= 2 && sb.charAt(0) == '.' && sb.charAt(1) == '/') sb.delete(0, 2);
        for (int j = 0; (j = sb.indexOf("/./", j)) >= 0; ) sb.delete(j + 1, j + 3);
        int len = sb.length();
        if (len >= 2 && sb.charAt(len - 1) == '.' && sb.charAt(len - 2) == '/') if (len == 2) return "/"; else sb.delete(len - 2, len);
        for (int j = 0; (j = sb.indexOf("/../", j)) >= 0; ) j = removeDotDot(sb, j);
        len = sb.length();
        if (len >= 3 && sb.charAt(len - 1) == '.' && sb.charAt(len - 2) == '.' && sb.charAt(len - 3) == '/') if (len == 3) return "/"; else removeDotDot(sb, len - 3);
        return sb.length() == path.length() ? path : sb.toString();
    }

    /** Removes "/..".
	 * @param j points '/' in "/.."
	 * @return the next index to search from
	 */
    private static int removeDotDot(StringBuffer sb, int j) {
        int k = j;
        while (--k >= 0 && sb.charAt(k) != '/') ;
        if (k + 3 == j && sb.charAt(k + 1) == '.' && sb.charAt(k + 2) == '.') return j + 4;
        sb.delete(j, j + 3);
        if (j == 0) return 0;
        if (k < 0) {
            sb.delete(0, j < sb.length() ? j + 1 : j);
            return 0;
        }
        if (j >= sb.length()) ++k;
        sb.delete(k, j);
        return k;
    }

    public boolean equals(Object o) {
        return o instanceof Path && ((Path) o)._path.equals(_path);
    }

    public int hashCode() {
        return _path.hashCode();
    }

    public String toString() {
        return _path;
    }
}

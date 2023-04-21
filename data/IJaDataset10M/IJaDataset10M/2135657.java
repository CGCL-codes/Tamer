package abstrasy.libraries.mintk;

import abstrasy.Bivalence;
import abstrasy.externals.External;
import abstrasy.externals.ExternalObject;
import abstrasy.Node;
import abstrasy.PCoder;
import abstrasy.libraries.mintk.impl.MinTk_Component;
import abstrasy.libraries.mintk.impl.MinTk_Menu;
import abstrasy.libraries.mintk.impl.MinTk_Menu_Bar;
import java.awt.Color;
import java.awt.Font;
import java.awt.MenuComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class External_Menu_Bar extends JMenuBar implements ExternalObject, MinTk_Menu_Bar {

    public External_Menu_Bar() throws Exception {
        super();
        this.set_client(External.getCurrentInterpreterThreadName());
        this.set_id(this.get_id());
        this.set_self();
    }

    public void add(MinTk_Component menu) {
        this.add((JMenu) menu);
    }

    public Node external_add(Node node) throws Exception {
        node.isGoodArgsLength(true, 2);
        MinTk_Menu item = (MinTk_Menu) External.getArgExternalInstance(node, 1, MinTk_Menu.class);
        this.add(item);
        return null;
    }

    String _client = null;

    public void set_client(String _client) {
        this._client = _client;
    }

    public String get_client() {
        return _client;
    }

    private String _id = null;

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_id() {
        if (_id == null) {
            _id = COMPONENT + "-" + MinTk.getSerialID();
        }
        return _id;
    }

    public Node external_set_id(Node node) throws Exception {
        node.isGoodArgsLength(true, 2);
        this.set_id(node.getSubNode(1, Node.TYPE_STRING).getString());
        return null;
    }

    public Node external_get_id(Node node) throws Exception {
        node.isGoodArgsLength(true, 1);
        return new Node(this.get_id());
    }

    public Node external_set_client(Node node) throws Exception {
        node.isGoodArgsLength(true, 2);
        this.set_client(node.getSubNode(1, Node.TYPE_STRING).getString());
        return null;
    }

    public Node external_get_client(Node node) throws Exception {
        node.isGoodArgsLength(true, 1);
        return new Node(this.get_client());
    }

    public void remove(MinTk_Component menu) {
        this.remove((MenuComponent) menu);
        ;
    }

    public Node external_remove(Node node) throws Exception {
        node.isGoodArgsLength(true, 2);
        MinTk_Menu item = (MinTk_Menu) External.getArgExternalInstance(node, 1, MinTk_Menu.class);
        this.remove(item);
        return null;
    }

    Node _self = null;

    public void set_self() throws Exception {
        _self = External.getVar(PCoder.SELF);
    }

    public Node get_self() {
        return _self;
    }

    /**
	 * FontCap_
	 */
    public void set_font(Font font) {
        MinTk._set_font(this, font);
    }

    public Font get_font() {
        return MinTk._get_font(this);
    }

    public Node external_set_font(Node node) throws Exception {
        return MinTk._external_set_font(this, node);
    }

    public Node external_get_font(Node node) throws Exception {
        return MinTk._external_get_font(this, node);
    }

    /**
	 * interface BgFgColor_
	 */
    public void set_bg_color(Color color) {
        MinTk._set_bg_color(this, color);
    }

    public Color get_bg_color() {
        return MinTk._get_bg_color(this);
    }

    public Node external_set_bg_color(Node node) throws Exception {
        return MinTk._external_set_bg_color(this, node);
    }

    public Node external_get_bg_color(Node node) throws Exception {
        return MinTk._external_get_bg_color(this, node);
    }

    public void set_fg_color(Color color) {
        MinTk._set_fg_color(this, color);
    }

    public Color get_fg_color() {
        return MinTk._get_fg_color(this);
    }

    public Node external_set_fg_color(Node node) throws Exception {
        return MinTk._external_set_fg_color(this, node);
    }

    public Node external_get_fg_color(Node node) throws Exception {
        return MinTk._external_get_fg_color(this, node);
    }

    public Object clone_my_self(Bivalence bival) {
        return null;
    }
}

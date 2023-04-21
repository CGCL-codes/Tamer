package org.formaria.oracle.forms.xml.visitor;

import org.formaria.oracle.forms.xml.node.FormsNode;
import org.formaria.oracle.forms.xml.node.Alert;
import org.formaria.oracle.forms.xml.node.Block;
import org.formaria.oracle.forms.xml.node.Canvas;
import org.formaria.oracle.forms.xml.node.CompoundText;
import org.formaria.oracle.forms.xml.node.Coordinate;
import org.formaria.oracle.forms.xml.node.DataSourceArgument;
import org.formaria.oracle.forms.xml.node.DataSourceColumn;
import org.formaria.oracle.forms.xml.node.Editor;
import org.formaria.oracle.forms.xml.node.Font;
import org.formaria.oracle.forms.xml.node.FormModule;
import org.formaria.oracle.forms.xml.node.Graphics;
import org.formaria.oracle.forms.xml.node.IndexedListWithName;
import org.formaria.oracle.forms.xml.node.IndexedListWithoutName;
import org.formaria.oracle.forms.xml.node.Item;
import org.formaria.oracle.forms.xml.node.LOV;
import org.formaria.oracle.forms.xml.node.LOVColumnMapping;
import org.formaria.oracle.forms.xml.node.Menu;
import org.formaria.oracle.forms.xml.node.MenuItem;
import org.formaria.oracle.forms.xml.node.MenuModule;
import org.formaria.oracle.forms.xml.node.Module;
import org.formaria.oracle.forms.xml.node.ModuleParameter;
import org.formaria.oracle.forms.xml.node.ObjectGroup;
import org.formaria.oracle.forms.xml.node.ObjectGroupChild;
import org.formaria.oracle.forms.xml.node.ObjectLibrary;
import org.formaria.oracle.forms.xml.node.ObjectLibraryTab;
import org.formaria.oracle.forms.xml.node.Point;
import org.formaria.oracle.forms.xml.node.ProgramUnit;
import org.formaria.oracle.forms.xml.node.PropertyClass;
import org.formaria.oracle.forms.xml.node.RadioButton;
import org.formaria.oracle.forms.xml.node.RecordGroup;
import org.formaria.oracle.forms.xml.node.RecordGroupColumn;
import org.formaria.oracle.forms.xml.node.Relation;
import org.formaria.oracle.forms.xml.node.Report;
import org.formaria.oracle.forms.xml.node.TabPage;
import org.formaria.oracle.forms.xml.node.TextSegment;
import org.formaria.oracle.forms.xml.node.Trigger;
import org.formaria.oracle.forms.xml.node.VisualAttribute;
import org.formaria.oracle.forms.xml.node.VisualState;
import org.formaria.oracle.forms.xml.node.Window;

public class FormsNodeVisitorAdapter implements FormsNodeVisitor {

    public Object visit(FormsNode node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(Alert node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(Block node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(Canvas node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(CompoundText node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(Coordinate node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(DataSourceArgument node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(DataSourceColumn node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(Editor node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(Font node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(FormModule node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(Graphics node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(IndexedListWithName node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(IndexedListWithoutName node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(Item node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(LOV node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(LOVColumnMapping node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(Menu node, Object data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object visit(MenuItem node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(MenuModule node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(Module node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(ModuleParameter node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(ObjectGroup node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(ObjectGroupChild node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(ObjectLibrary node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(ObjectLibraryTab node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(Point node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(ProgramUnit node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(PropertyClass node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(RadioButton node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(RecordGroup node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(RecordGroupColumn node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(Relation node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(Report node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(TabPage node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(TextSegment node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(Trigger node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(VisualAttribute node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(VisualState node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(Window node, Object data) {
        return node.childrenAccept(this, data);
    }
}

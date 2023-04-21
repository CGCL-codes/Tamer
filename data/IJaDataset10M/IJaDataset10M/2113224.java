package umlc.parseTree;

/**
 *
 * @author  Ryan
 * @version 
 */
public class UmlOperation implements TreeNode {

    public UmlModifier visibility;

    public String name;

    public java.util.Vector parameters;

    public UmlType return_type;

    /** Creates new UmlOperation */
    public UmlOperation(UmlModifier _visibility, String _name, java.util.Vector _parameters, UmlType _return_type) {
        visibility = _visibility;
        name = _name;
        parameters = _parameters;
        return_type = _return_type;
    }

    void pretty_print() {
        visibility.pretty_print();
        System.out.print(" " + name + "(");
        for (int i = 0; i < parameters.size(); i++) {
            ((UmlParameter) parameters.elementAt(i)).pretty_print();
        }
        System.out.print("):");
        return_type.pretty_print();
        System.out.println("");
    }
}

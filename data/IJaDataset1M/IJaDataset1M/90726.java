package nacaLib.varEx;

/**
 * @author U930DI
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EditInMapRedefineNum extends EditInMapRedefine {

    EditInMapRedefineNum(DeclareTypeEditInMapRedefineNum declareTypeEditInMapRedefine) {
        super(declareTypeEditInMapRedefine);
    }

    protected EditInMapRedefineNum() {
        super();
    }

    protected VarBase allocCopy() {
        EditInMapRedefine v = new EditInMapRedefine();
        return v;
    }

    public EditInMapRedefine allocOccursedItem(VarDefBuffer varDefItem) {
        EditInMapRedefineNum vItem = new EditInMapRedefineNum();
        vItem.m_bufferPos = m_bufferPos;
        vItem.m_varDef = varDefItem;
        vItem.m_attrManager = vItem.getEditAttributManager();
        return vItem;
    }
}

package nacaLib.varEx;

/**
 * @author U930DI
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EditInMapRedefineNumEdited extends EditInMapRedefine {

    EditInMapRedefineNumEdited(DeclareTypeEditInMapRedefineNumEdited declareTypeEditInMapRedefine) {
        super(declareTypeEditInMapRedefine);
    }

    protected EditInMapRedefineNumEdited() {
        super();
    }

    protected VarBase allocCopy() {
        EditInMapRedefineNumEdited v = new EditInMapRedefineNumEdited();
        return v;
    }

    public EditInMapRedefine allocOccursedItem(VarDefBuffer varDefItem) {
        EditInMapRedefineNumEdited vItem = new EditInMapRedefineNumEdited();
        vItem.m_varDef = varDefItem;
        int nOffset = m_bufferPos.m_nAbsolutePosition - m_varDef.m_nDefaultAbsolutePosition;
        vItem.m_bufferPos = new VarBufferPos(m_bufferPos, varDefItem.m_nDefaultAbsolutePosition + nOffset);
        vItem.m_varTypeId = varDefItem.getTypeId();
        vItem.m_attrManager = vItem.getEditAttributManager();
        return vItem;
    }
}

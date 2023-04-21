package net.sf.signs.intermediate;

import java.io.PrintStream;
import java.util.ArrayList;
import net.sf.signs.Port;
import net.sf.signs.SigType;
import net.sf.signs.SignsException;
import net.sf.signs.SourceLocation;

@SuppressWarnings("serial")
public class FileDeclaration extends BlockDeclarativeItem {

    private TypeDefinition td;

    private FileOpenInformation foi;

    public FileDeclaration(String id_, TypeDefinition td_, FileOpenInformation foi_, IntermediateObject parent_, SourceLocation location_) {
        super(id_, parent_, location_);
        td = td_;
        foi = foi_;
        td.setParent(this);
        foi.setParent(this);
    }

    @Override
    public void dump(PrintStream out_) {
    }

    @Override
    public void elaborate(OperationCache cache_) throws SignsException {
        String filename = null;
        int mode = -1;
        if (foi != null) {
            Operation filenameOp = foi.getStringExpr();
            if (filenameOp != null) {
                filenameOp.computeType(null, cache_);
                filename = filenameOp.getStringConstant(cache_);
            }
            mode = foi.getMode();
        }
        SigType t = td.elaborate(cache_);
        SigType elementType = t.getElementType(getLocation());
        FileObject fo = new FileObject(t, filename, mode, this, getLocation());
        getResolver().add(id, fo);
        ArrayList<InterfaceDeclaration> interfaces = new ArrayList<InterfaceDeclaration>(2);
        InterfaceSignalDeclaration isd = new InterfaceSignalDeclaration("F", td, Port.DIR_IN, this, getLocation());
        interfaces.add(isd);
        isd = new InterfaceSignalDeclaration("VALUE", new TypeDefinitionElaborated(elementType, this, getLocation()), Port.DIR_OUT, this, getLocation());
        interfaces.add(isd);
        SubProgram sub = new SubProgram("READ", interfaces, null, this, getLocation());
        getResolver().add(sub);
        interfaces = new ArrayList<InterfaceDeclaration>(2);
        isd = new InterfaceSignalDeclaration("F", td, Port.DIR_IN, this, getLocation());
        interfaces.add(isd);
        isd = new InterfaceSignalDeclaration("VALUE", new TypeDefinitionElaborated(elementType, this, getLocation()), Port.DIR_IN, this, getLocation());
        interfaces.add(isd);
        sub = new SubProgram("WRITE", interfaces, null, this, getLocation());
        getResolver().add(sub);
        interfaces = new ArrayList<InterfaceDeclaration>(1);
        isd = new InterfaceSignalDeclaration("F", td, Port.DIR_IN, this, getLocation());
        interfaces.add(isd);
        sub = new SubProgram("ENDFILE", interfaces, new Name("BOOLEAN", this, getLocation()), this, getLocation());
        getResolver().add(sub);
    }

    @Override
    public IntermediateObject getChild(int idx_) {
        return null;
    }

    @Override
    public int getNumChildren() {
        return 0;
    }
}

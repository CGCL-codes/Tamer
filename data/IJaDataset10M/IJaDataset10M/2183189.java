package plcopen.type.group.poubody;

import java.util.ArrayList;
import java.util.List;
import plcopen.inf.type.group.fbd.IBlock;
import plcopen.inf.type.group.fbd.IInOutVariableInBlock;
import plcopen.inf.type.group.fbd.IInVariableInBlock;
import plcopen.inf.type.group.fbd.IOutVariableInBlock;
import plcopen.model.GraphicElement;
import plcopen.type.Dimension;

/**
 * IBlock을 구현한 클래스.
 * 
 * @author swkim
 * 
 */
public class BlockImpl extends GraphicElement implements IBlock {

    long executionOrderID = 0;

    List<IInOutVariableInBlock> inOutVariables = new ArrayList<IInOutVariableInBlock>();

    String instanceName = "";

    List<IInVariableInBlock> inVariables = new ArrayList<IInVariableInBlock>();

    List<IOutVariableInBlock> outVariables = new ArrayList<IOutVariableInBlock>();

    String typeName = "";

    String addDataName = "";

    String addDataHandleUnkown = "";

    public BlockImpl() {
        super();
        this.setSize(new Dimension(50, 50));
    }

    public long getExecutionOrderID() {
        return executionOrderID;
    }

    public List<IInOutVariableInBlock> getInOutVariables() {
        return inOutVariables;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public List<IInVariableInBlock> getInVariables() {
        return inVariables;
    }

    public List<IOutVariableInBlock> getOutVariables() {
        return outVariables;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setExecutionOrderID(long executionOrderID) {
        this.executionOrderID = executionOrderID;
    }

    public void setInOutVariables(List<IInOutVariableInBlock> inOutVariables) {
        this.inOutVariables = inOutVariables;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public void setInVariables(List<IInVariableInBlock> inVariables) {
        this.inVariables = inVariables;
    }

    public void setOutVariables(List<IOutVariableInBlock> outVariables) {
        this.outVariables = outVariables;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public void setAddDataName(String addDataName) {
        this.addDataName = addDataName;
    }

    @Override
    public void setAddDataHandleUnknown(String handle) {
        if (handle.equals("discard") || handle.equals("preserve") || handle.equals("implementation")) this.addDataHandleUnkown = handle; else this.addDataHandleUnkown = "";
    }

    @Override
    public String getAddDataName() {
        return this.addDataName;
    }
}

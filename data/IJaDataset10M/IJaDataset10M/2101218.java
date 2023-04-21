package plcopen.inf.type.body;

import java.util.List;
import plcopen.inf.model.IPLCElement;
import plcopen.inf.type.IBody;
import plcopen.inf.type.group.common.IActionBlock;
import plcopen.inf.type.group.common.IComment;
import plcopen.inf.type.group.common.IConnector;
import plcopen.inf.type.group.common.IContinuation;
import plcopen.inf.type.group.common.IError;
import plcopen.inf.type.group.fbd.IBlock;
import plcopen.inf.type.group.fbd.IInOutVariable;
import plcopen.inf.type.group.fbd.IInVariable;
import plcopen.inf.type.group.fbd.IJump;
import plcopen.inf.type.group.fbd.ILabel;
import plcopen.inf.type.group.fbd.IOutVariable;
import plcopen.inf.type.group.fbd.IReturn;

/**
 * FBD의 자식들로 생성가능한 모든 element들을 정의하고, 참조하는 interface.
 * 
 * 본 interface에는 FBD와 CommonObject의 11가지 요소에 대해서 참조하는 get/set함수에 대해서만 정의한다.
 * 
 * @author swkim
 * 
 */
public interface IFBD extends IBody {

    public static final String ID_BLOCK = "block";

    public List<IActionBlock> getActionBlks();

    public List<IBlock> getBlocks();

    public List<IComment> getComments();

    public List<IConnector> getConnectors();

    public List<IContinuation> getContinuations();

    public String getDocumentation();

    public List<IError> getErrors();

    public List<IInOutVariable> getInOutVariables();

    public List<IInVariable> getInVariables();

    public List<IJump> getJumps();

    public List<ILabel> getLabels();

    public List<IOutVariable> getOutVariables();

    public List<IReturn> getReturns();

    public void setActionBlks(List<IActionBlock> actionBlks);

    public void setBlocks(List<IBlock> blocks);

    public void setComments(List<IComment> comments);

    public void setConnectors(List<IConnector> connectors);

    public void setContinuations(List<IContinuation> continuations);

    public void setDocumentation(String documentation);

    public void setErrors(List<IError> errors);

    public void setInOutVariables(List<IInOutVariable> inOutVariables);

    public void setInVariables(List<IInVariable> inVariables);

    public void setJumps(List<IJump> jumps);

    public void setLabels(List<ILabel> labels);

    public void setOutVariables(List<IOutVariable> outVariables);

    public void setParent(IPLCElement parent);

    public void setReturns(List<IReturn> returns);
}

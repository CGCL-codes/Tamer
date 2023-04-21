package plcopen.model;

import plcopen.inf.model.IActionType;
import plcopen.inf.model.IPLCElement;
import plcopen.inf.model.ITransitionType;
import plcopen.inf.type.IBody;

/**
 * IActionType과 ITransitionType에 대해 구현하는 클래스. 구현하고자 하는 Interface가 모두 POU의 행위를
 * 정의하기 때문에 본 클래스의 이름이 POUBehaviorImpl이 되었다.
 * 
 * @author swkim
 * 
 */
public class POUBehaviorImpl implements IActionType, ITransitionType, IPLCElement {

    String name = "";

    String documentation = "";

    IBody body;

    private IPLCElement parent;

    public IBody getBody() {
        return body;
    }

    public String getDocumentation() {
        return documentation;
    }

    public String getName() {
        return name;
    }

    public void setBody(IBody body) {
        this.body = body;
    }

    public void setDocumentation(String documentation) {
        if (documentation == null) this.documentation = ""; else this.documentation = documentation;
    }

    public void setName(String name) {
        if (name == null) this.name = ""; else this.name = name;
    }

    public IPLCElement getParent() {
        return parent;
    }

    public void setParent(IPLCElement parent) {
        this.parent = parent;
    }
}

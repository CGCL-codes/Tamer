package plcopen.inf.model;

import plcopen.inf.type.IDataType;
import plcopen.inf.type.IValue;

/**
 * IVariableList���붿냼濡��ъ슜�섎뒗 Variable���뺤쓽�쒕떎. PLCOpen�먯꽌 �ъ슜�섎뒗 Variable���щ윭 怨녹뿉���ㅼ뼇��
 * �섎�濡��ъ슜媛�뒫�섏�留� �ㅼ쭅 Variable List�먯꽌 �ъ슜�섎뒗 variable濡쒕쭔 �쒖젙�쒕떎.
 * 
 * @author swkim
 * 
 */
public interface IVariable {

    public static final String ID_NAME = "name";

    public static final String ID_DOCUMENTATION = "documentation";

    public static final String ID_INITVALUE = "initialValue";

    public static final String ID_ADDRESS = "address";

    public static final String ID_TYPE = "type";

    public static final String ID_VARIABLE = "variable";

    public String getName();

    public void setName(String name);

    public String getAddress();

    public void setAddress(String address);

    public String getDocumentation();

    public void setDocumentation(String documentation);

    public IValue getInitialValue();

    public void setInitialValue(IValue initialValue);

    public void setType(IDataType type);

    public IDataType getType();
}

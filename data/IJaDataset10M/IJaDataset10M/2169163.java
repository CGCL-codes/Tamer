package plcopen.inf.type;

/**
 * IValue를 구성하는 3가지 중 하나. 가장 간단하게 문자열로 해당 값을 가진다.
 * 
 * @author swkim
 * 
 */
public interface ISimpleValue extends IValue {

    public static final String ID_VALUE = "value";

    public String getValue();

    public void setValue(String value);
}

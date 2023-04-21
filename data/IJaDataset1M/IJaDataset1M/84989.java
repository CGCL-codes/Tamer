package purej.model;

/**
 * �� ���� ������
 * 
 * @author Administrator
 * 
 */
public interface ValueChangeListener {

    public void beforChange(String key, Object value) throws ModelException;

    public void afterChange(String key, Object value) throws ModelException;
}

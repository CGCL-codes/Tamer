package purej.service.adapter;

import java.util.List;

/**
 * ���� �ƴ���
 * 
 * @author Administrator
 * 
 */
public interface ServiceAdapter {

    /**
     * ���� ���̵� ��ȯ�Ѵ�.
     * 
     * @return
     */
    public String getServiceID();

    /**
     * ���� ���̵� �����Ѵ�.
     * 
     * @param string
     */
    public void setServiceID(String string);

    /**
     * ���۷��̼Ǹ��� ��ȯ�Ѵ�.
     * 
     * @return
     */
    public String getOperationName();

    /**
     * ���۷��̼Ǹ��� �����Ѵ�.
     * 
     * @param string
     */
    public void setOperationName(String string);

    /**
     * �Ķ���� ����Ʈ�� �����Ѵ�.
     * 
     * @param list
     */
    public void setParameterList(List<Object> list);

    /**
     * �Ķ���� ����Ʈ�� ��ȯ�Ѵ�.
     * 
     * @return
     */
    public List<Object> getParameterList();

    /**
     * �Ķ���͸� �߰��Ѵ�.
     * 
     * @param key
     * @param value
     */
    public void addParameter(String key, String value);

    /**
     * �Ķ���͸� �߰��Ѵ�.
     * 
     * @param key
     * @param value
     */
    public void addParameter(String key, Object value);

    /**
     * �Ķ���͸� �߰��Ѵ�.
     * 
     * @param key
     * @param value
     */
    public void addParameter(String value);

    /**
     * �Ķ���͸� �߰��Ѵ�.
     * 
     * @param key
     * @param value
     */
    public void addParameter(Object value);

    /**
     * ���񽺸� �����Ͽ� ��� ��ȯ�Ѵ�.
     * 
     * @return
     * @throws Exception
     */
    public Object process() throws Exception;

    /**
     * ���񽺸� �����Ѵ�.
     * 
     */
    public void cleanup();
}

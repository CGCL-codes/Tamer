package org.easybi;

/**
 * ����EasyBI����objectҪʵ�������������Chart, Report�ȵ�
 * @author steve
 *
 */
public interface EasyBIObject {

    public String getName();

    public boolean isReloadData();

    /**
     * ����report��ģ���ģ���׺
     * @return
     */
    public String getTemplateName();
}

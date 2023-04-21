package ces.platform.bbs;

import ces.platform.bbs.exception.UnauthorizedException;

/**
 * @author sword
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface ForumColor {

    /**
     * ��������ݿ���
     * @throws UnauthorizedException
     */
    public void save() throws UnauthorizedException;

    /**
     * �õ�������������ɫ
     * @return String ������������ɫ
     */
    public String getAlertColor();

    /**
     * �õ����������ɫ
     * @return String ���������ɫ
     */
    public String getMarrowColor();

    /**
     * �õ��߿����ɫ
     * @return String �߿����ɫ
     */
    public String getBorderColor();

    /**
     * �õ����ݱ�������ɫ
     * @return String ���ݱ�������ɫ
     */
    public String getContentBkColor();

    /**
     * �õ��ڶ������ݱ�������ɫ
     * @return String �ڶ������ݱ�������ɫ
     */
    public String getContentBkColor2();

    /**
     * �õ��������ֵ���ɫ
     * @return String �������ֵ���ɫ
     */
    public String getContentColor();

    /**
     * �õ�����ɫ���õ�����
     * @return String ��ɫ���õ�����
     */
    public String getDescription();

    /**
     * �õ�����ɫ���õı��
     * @return String ����ɫ���õı��
     */
    public int getId();

    /**
     * �õ�ҳ�汳������ɫ
     * @return String ҳ�汳������ɫ
     */
    public String getPageBkColor();

    /**
     * �õ�ҳ����ɫ
     * @return String ҳ����ɫ
     */
    public String getPageColor();

    /**
     * �õ��̶������ɫ
     * @return Sring �̶������ɫ
     */
    public String getStickyColor();

    /**
     * �õ�����ɫ���õ����
     * @return String ��ɫ���õ����
     */
    public String getTitle();

    /**
     * �õ�����ı�����ɫ
     * @return String ����ı�����ɫ
     */
    public String getTitleBkColor();

    /**
     * �õ�������ɫ
     * @return String ������ɫ
     */
    public String getTitleColor();

    /**
     * ���þ�����������ɫ
     * @param string ������������ɫ
     */
    public void setAlertColor(String string);

    /**
     * ���þ�������ɫ
     * @param string ��������ɫ
     */
    public void setMarrowColor(String string);

    /**
     * ���ñ߿���ɫ
     * @param string �߿���ɫ
     */
    public void setBorderColor(String string);

    /**
     * �������ݱ�����ɫ
     * @param string ���ݱ�����ɫ
     */
    public void setContentBkColor(String string);

    /**
     * ���õڶ������ݱ�����ɫ
     * @param string �ڶ������ݱ�����ɫ
     */
    public void setContentBkColor2(String string);

    /**
     * ��������������ɫ
     * @param string ����������ɫ
     */
    public void setContentColor(String string);

    /**
     * ���ñ���ɫ���õ�����
     * @param string ��ɫ���õ�����
     */
    public void setDescription(String string);

    /**
     * ���ñ���ɫ���õı��
     * @param i ��ɫ���õı��
     */
    public void setId(int i);

    /**
     * ����ҳ�汳����ɫ
     * @param string ҳ�汳����ɫ
     */
    public void setPageBkColor(String string);

    /**
     * ����ҳ����ɫ
     * @param string ҳ����ɫ
     */
    public void setPageColor(String string);

    /**
     * ���ù̶�����ɫ
     * @param string �̶�����ɫ
     */
    public void setStickyColor(String string);

    /**
     * ���ñ���ɫ���õ����
     * @param string ��ɫ���õ����
     */
    public void setTitle(String string);

    /**
     * ���ñ��ⱳ����ɫ
     * @param string ���ⱳ����ɫ
     */
    public void setTitleBkColor(String string);

    /**
     * ���ñ�����ɫ
     * @param string ������ɫ
     */
    public void setTitleColor(String string);
}

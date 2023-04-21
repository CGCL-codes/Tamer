package com.gever.sysman.privilege.util;

import java.util.Map;

/**
��չ�ӿ�
 */
public interface Extensible {

    /**
   ��ȡ��չ����
   @return java.util.Map
   @roseuid 40B69DA500CB
    */
    public Map getProperties();

    /**
   ������չ����
   @param properties - ��չ����
   @roseuid 40B69DB7005D
    */
    public void setProperties(Map properties);

    /**
   ���key��ȡ��չ����
   @param key
   @return java.lang.String
   @roseuid 40B69DBF0119
    */
    public String getProperty(String key);

    /**
   ������չ����
   @param key - ָ����keyֵ
   @param value - ָ��������ֵ
   @roseuid 40B69DC30251
    */
    public void setProperty(String key, String value);
}

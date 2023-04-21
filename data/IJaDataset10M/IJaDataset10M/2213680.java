package com.pioneer.frame.dao;

import java.io.Serializable;

public interface IValueObject extends Serializable, Cloneable {

    /**
	 * @desc:
	 * @return :ȡ����������
	 * @auther : pionner
	 * @date : 2007-4-1
	 * 
	 */
    public Object getObjectID();

    /**
	 * @desc: ��ݱ������ȡ�������ֵ
	 * @param varName
	 * @return :
	 * @auther : pionner
	 * @date : 2007-4-1
	 * 
	 */
    public Object getValueByName(String varName);

    /**
	 * @desc: ȡ��vo���Ӧ�����������
	 * @return :
	 * @auther : pionner
	 * @date : 2007-4-1
	 * 
	 */
    public String[] getColumnnames();

    /**
	 * @desc: ȡ��vo������������
	 * @return :
	 * @auther : pionner
	 * @date : 2007-4-1
	 */
    public String[] getVariblenames();

    /**
	 * @desc: ��ݱ���Ƹ�vo����ֵ
	 * @param varName
	 * @param val
	 * @return :
	 * @auther : pionner
	 * @date : 2007-4-1
	 */
    public Object setValueByName(String varName, String val);

    /**
	 * @desc: ȡ��vo���Ӧ�ı�����.
	 * @return :
	 * @auther : pionner
	 * @date : 2007-4-1
	 * 
	 */
    public String getTablename();

    /**
	 * @desc: У������Ƿ�Ϸ�.
	 * @return :
	 * @auther : pionner
	 * @date : 2007-4-1
	 * 
	 */
    public boolean isValid();
}

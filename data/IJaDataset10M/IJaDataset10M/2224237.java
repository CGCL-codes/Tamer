package org.carp.engine;

import java.sql.PreparedStatement;
import org.apache.log4j.Logger;
import org.carp.engine.parameter.Parameter;
import org.carp.exception.CarpException;
import org.carp.type.TypesMapping;

/**
 * ��������
 * ����Statement����Ĳ�ѯ����
 * @author zhou	
 * @since 0.1
 */
public class ParametersProcessor {

    private static final Logger logger = Logger.getLogger(ParametersProcessor.class);

    private PreparedStatement ps;

    public ParametersProcessor(PreparedStatement ps) {
        this.ps = ps;
    }

    /**
	 * ����PreparedStatement����
	 * @param value ����ֵ
	 * @param typeCls ��������
	 * @param index ��������
	 * @throws Exception
	 */
    public void setStatementParameters(Object value, Class<?> typeCls, int index) throws Exception {
        if (TypesMapping.getParamsClass(typeCls) == null) {
            throw new CarpException("��֧�ֵĲ������ͣ�" + typeCls);
        }
        Parameter param = (Parameter) TypesMapping.getParamsClass(typeCls).newInstance();
        if (logger.isDebugEnabled()) {
            logger.debug("ParameterType : " + typeCls + " ,  ParameterIndex �� " + index + " ,  Value  : " + value);
        }
        param.setValue(ps, value, index);
    }

    /**
	 * ����PreparedStatement����
	 * @param value ����ֵ
	 * @param javaType ��������
	 * @param index ��������
	 * @throws Exception
	 */
    public void setStatementParameters(Object value, int javaType, int index) throws Exception {
        if (TypesMapping.getParamsClass(javaType) == null) {
            throw new CarpException("��֧�ֵĲ������ͣ�" + javaType);
        }
        Parameter param = (Parameter) TypesMapping.getParamsClass(javaType).newInstance();
        if (logger.isDebugEnabled()) {
            logger.debug("ParameterIndex �� " + index + " ,  Value  : " + value);
        }
        param.setValue(ps, value, index);
    }
}

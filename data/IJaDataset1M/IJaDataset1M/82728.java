package openminer.data;

import java.util.*;
import openminer.data.dataset.*;

/**
 * ��ݱ��װ��
 * @author TL
 *
 */
public class InstancesLoader {

    private InputDataSet m_DataSet = null;

    private List m_AttNameList = new ArrayList();

    public InstancesLoader(InputDataSet dataSet) {
        m_DataSet = dataSet;
    }

    /**
	 * ����Ҫת�ص�����
	 * @param attName ��������
	 */
    public void addAttributeToLoad(String attName) throws Exception {
        m_AttNameList.add(attName);
    }

    /**
	 * �����ڴ��������ʵ��
	 * @param instsName
	 * @return
	 * @throws Exception
	 */
    public MemoryInstances buildMemoryInstances(String instsName) throws Exception {
        MemoryInstances insts = new MemoryInstances();
        insts.setName(instsName);
        loadAttributes(insts);
        loadInstacesData(insts);
        return insts;
    }

    /**
	 * �������ʵ��
	 * @param instsName
	 * @return
	 * @throws Exception
	 */
    public PagedInstances buildPagedInstances(String instsName, int maxFetchSize) throws Exception {
        PagedInstances insts = new PagedInstances(maxFetchSize);
        insts.setName(instsName);
        loadAttributes(insts);
        insts.connectInputDataSet(m_DataSet);
        return insts;
    }

    /**
	 * �������ʵ��
	 * @param attList
	 * @return
	 * @throws Exception
	 */
    public PagedInstances buildPagedInstances(List attList) throws Exception {
        PagedInstances insts = new PagedInstances();
        insts.setAttributeList(attList);
        insts.connectInputDataSet(m_DataSet);
        return insts;
    }

    /**
	 * װ����ݱ��е�����
	 * @param insts
	 * @throws Exception
	 */
    private void loadAttributes(Instances insts) throws Exception {
        int colIndex;
        Attribute att;
        String attName;
        int attType;
        for (int i = 0; i < m_AttNameList.size(); i++) {
            attName = (String) m_AttNameList.get(i);
            colIndex = m_DataSet.getColumnIndex(attName);
            if (colIndex == -1) {
                throw new Exception("Cannot find the attribute : " + attName + " in thess columns.");
            }
            attType = m_DataSet.getColumnType(colIndex);
            att = new Attribute(attType, attName);
            if (m_DataSet.getColumnValueField(colIndex) != null) att.setValueField(m_DataSet.getColumnValueField(colIndex));
            insts.addAttribute(att);
            System.out.println(attName + " " + ValueTypes.getTypeName(attType));
        }
    }

    /**
	 * װ����ݱ��Ԫ�����
	 * @param insts
	 * @throws Exception
	 */
    private void loadInstacesData(MemoryInstances insts) throws Exception {
        int i;
        Instance inst;
        Attribute att;
        int colIndex;
        while (m_DataSet.nextRow()) {
            inst = new Instance(insts.getAttributeCount());
            for (i = 0; i < insts.getAttributeCount(); i++) {
                att = insts.getAttribute(i);
                colIndex = m_DataSet.getColumnIndex(att.getName());
                switch(att.getType()) {
                    case ValueTypes.DOUBLE:
                        {
                            double value = m_DataSet.getDouble(colIndex);
                            inst.setValue(i, new Double(value));
                            break;
                        }
                    case ValueTypes.INTEGER:
                        {
                            int value = m_DataSet.getInt(colIndex);
                            inst.setValue(i, new Integer(value));
                            break;
                        }
                    case ValueTypes.STRING:
                        {
                            String str = m_DataSet.getString(colIndex);
                            inst.setValue(i, str);
                            break;
                        }
                }
            }
            insts.addInstance(inst);
        }
    }
}

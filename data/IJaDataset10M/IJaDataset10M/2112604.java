package org.jcube;

import org.jcube.template.CubeTemplate;
import org.jcube.template.DimensionTemplate;
import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Cube����.
 * ��������Cube�͹���Cube.
 * ����߱����Cube��ʾ�����
 * <pre>
            cube = new JCube();
            dimTemplates[0] = new DimensionTemplate(0, "org", "��֯��");
            dimTemplates[1] = new DimensionTemplate(1, "show", "Ӫҵ��־");
            dimTemplates[2] = new DimensionTemplate(2, "pur", "�۵�����־");
            valueTemplates[0] = new ValueTemplate(3, "count", "�������");
            valueTemplates[0].setFormat(new DecimalFormat("#0"));
            cube.buildCube(rset, template);
 * </pre>
 * User: liujidong
 * Date: 2007-7-3
 * Time: 11:22:05
 * To change this template use File | Settings | File Templates.
 */
public class JCube {

    private List<List> dimensionArray;

    CubeTemplate cubeTemplate;

    public List getDimensionArray() {
        return dimensionArray;
    }

    public CubeTemplate getCubeTemplate() {
        return cubeTemplate;
    }

    public void buildCube(String[][] cubeData, CubeTemplate template) {
        this.cubeTemplate = template;
        dimensionArray = new ArrayList();
        for (int i = 0; i < template.getDimTemplates().length; i++) dimensionArray.add(new ArrayList());
        for (int i = 0; i < cubeData.length; i++) {
            for (int x = 0; x < template.getValueTemplates().length; x++) {
                Value value = new Value(Double.parseDouble(cubeData[i][template.getValueTemplates()[x].getColumn()]));
                for (int j = 0; j < template.getDimTemplates().length; j++) {
                    List dimensions = (List) dimensionArray.get(j);
                    Dimension dim1 = Dimension.addDimension(dimensions, cubeData[i][template.getDimTemplates()[j].getColumn()], template.getValueTemplates().length);
                    dim1.addValue(x, value);
                }
            }
        }
    }

    public void buildCube(ResultSet cubeDataSrc, CubeTemplate template) throws SQLException {
        this.cubeTemplate = template;
        dimensionArray = new ArrayList();
        for (int i = 0; i < template.getDimTemplates().length; i++) dimensionArray.add(new ArrayList());
        while (cubeDataSrc.next()) {
            for (int x = 0; x < template.getValueTemplates().length; x++) {
                Value value = new Value(cubeDataSrc.getDouble(template.getValueTemplates()[x].getColumnName()));
                for (int j = 0; j < template.getDimTemplates().length; j++) {
                    List dimensions = (List) dimensionArray.get(j);
                    Dimension dim1 = Dimension.addDimension(dimensions, cubeDataSrc.getString(template.getDimTemplates()[j].getColumnName()), template.getValueTemplates().length);
                    dim1.addValue(x, value);
                }
            }
        }
    }

    public List getDimension(DimensionTemplate dimensionTemplate) {
        for (int i = 0; i < this.cubeTemplate.getDimTemplates().length; i++) {
            if (cubeTemplate.getDimTemplates()[i].getName().equals(dimensionTemplate.getName())) return (List) this.dimensionArray.get(i);
        }
        throw new RuntimeException("�޷��ҵ���Ӧά��");
    }
}

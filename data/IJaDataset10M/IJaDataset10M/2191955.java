package data;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import tools.Pair;

/**
 * �������Euclidian KNN����ʱ�������ĵ�
 */
public class TextBasedData extends DataModel<String, Double> {

    private String m_textID = null;

    private List<VectorSpaceModel<String, Double>> m_textAttributes = null;

    private TextBasedDataSet m_owner = null;

    /**
	 * @Stringodo  
	 * ��Ӧ�ı����� ����ֵ�������Ϣ Ҫ���Լ�¼
	 * 
	 */
    private int m_classID;

    public TextBasedData(TextBasedDataSet owner) {
        m_textID = null;
        m_classID = -1;
        m_textAttributes = new ArrayList<VectorSpaceModel<String, Double>>();
        m_owner = owner;
    }

    public TextBasedData(String textID, TextBasedDataSet owner) {
        m_textID = textID;
        m_classID = -1;
        m_textAttributes = new ArrayList<VectorSpaceModel<String, Double>>();
        m_owner = owner;
    }

    public TextBasedData(String textID, List<VectorSpaceModel<String, Double>> textAttributes, TextBasedDataSet owner) {
        m_textID = textID;
        m_classID = -1;
        m_textAttributes = textAttributes;
        m_owner = owner;
    }

    /**
	 * �õ������mean 
	 * @param textAttributes
	 * @param owner
	 */
    public TextBasedData(List<VectorSpaceModel<String, Double>> textAttributes, TextBasedDataSet owner) {
        m_textID = null;
        m_classID = -1;
        m_textAttributes = textAttributes;
        m_owner = owner;
    }

    public void updateVSM(int attrIndex, VectorSpaceModel<String, Double> attrContext, boolean remove) {
        if (remove) m_textAttributes.remove(attrIndex);
        m_textAttributes.add(attrIndex, attrContext);
    }

    /**
	 *  Normalization ��ŷʽ���� 
	 */
    public double distanceTo(DataModel<String, Double> another, boolean normalization) {
        double distance = Double.MAX_VALUE;
        if (another != null) {
            try {
                if (another instanceof TextBasedData) {
                    distance = 0;
                    TextBasedData another_text = (TextBasedData) another;
                    List<VectorSpaceModel<String, Double>> another_textAttributes = another_text.getTextAttribute();
                    Iterator<VectorSpaceModel<String, Double>> aI = another_textAttributes.iterator();
                    Iterator<VectorSpaceModel<String, Double>> mI = m_textAttributes.iterator();
                    Iterator<Pair<String, Double>> weightI = m_owner.getAttrMeta().iterator();
                    while (weightI.hasNext()) {
                        VectorSpaceModel<String, Double> mString = (VectorSpaceModel<String, Double>) mI.next();
                        VectorSpaceModel<String, Double> aString = (VectorSpaceModel<String, Double>) aI.next();
                        Pair<String, Double> wP = weightI.next();
                        distance += mString.distanceTo(aString, normalization) * wP.getSecond().doubleValue();
                    }
                } else throw new Exception("not Shop");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return distance;
    }

    public List<VectorSpaceModel<String, Double>> getTextAttribute() {
        return m_textAttributes;
    }

    public String getShopID() {
        return m_textID;
    }

    public int getShopClassID() {
        return m_classID;
    }

    public void setOwner(TextBasedDataSet owner) {
        m_owner = owner;
    }

    public TextBasedDataSet getOwner() {
        return m_owner;
    }

    public void setShopClassID(int classID) {
        m_classID = classID;
    }

    public static void main(String[] args) {
        TextBasedDataSet test = new TextBasedDataSet();
        List<Pair<String, Double>> textAttributeMeta = new ArrayList<Pair<String, Double>>();
        textAttributeMeta.add(new Pair<String, Double>("test", 1.0));
        test.setAttrMeta(textAttributeMeta);
        List<VectorSpaceModel<String, Double>> lvsmTestA = new ArrayList<VectorSpaceModel<String, Double>>();
        VectorSpaceModel<String, Double> vsmTestA = new VectorSpaceModel<String, Double>();
        vsmTestA.setValue("a", 8.0);
        vsmTestA.setValue("b", 3.0);
        vsmTestA.setValue("c", 12.0);
        lvsmTestA.add(vsmTestA);
        List<VectorSpaceModel<String, Double>> lvsmTestB = new ArrayList<VectorSpaceModel<String, Double>>();
        VectorSpaceModel<String, Double> vsmTestB = new VectorSpaceModel<String, Double>();
        vsmTestB.setValue("a", 7.0);
        vsmTestB.setValue("d", 3.0);
        vsmTestB.setValue("f", 12.0);
        lvsmTestB.add(vsmTestB);
        TextBasedData testA = new TextBasedData("1", lvsmTestA, test);
        TextBasedData testB = new TextBasedData("2", lvsmTestB, test);
        System.out.print(testA.distanceTo(testB, false));
        System.out.print(testA.distanceTo(testB, true));
    }
}

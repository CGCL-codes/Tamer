package xalan.smartgwt.criteria;

import static org.junit.Assert.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Before;
import org.junit.Test;

public class CriteriaParserTest {

    private CriteriaParser instance;

    @Before
    public void setUp() {
        this.instance = new CriteriaParser();
    }

    @Test
    public void testJSONObject() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        String json = "{name=\"json\",bool:true,int:1,double:2.2,func:function(a){ return a; },array:[1,2]}";
        JSONObject jsonObject = JSONObject.fromObject(json);
        Object bean = JSONObject.toBean(jsonObject);
        assertEquals(jsonObject.get("name"), PropertyUtils.getProperty(bean, "name"));
        assertEquals(jsonObject.get("bool"), PropertyUtils.getProperty(bean, "bool"));
        assertEquals(jsonObject.get("int"), PropertyUtils.getProperty(bean, "int"));
        assertEquals(jsonObject.get("double"), PropertyUtils.getProperty(bean, "double"));
        assertEquals(jsonObject.get("func"), PropertyUtils.getProperty(bean, "func"));
        assertEquals(1, jsonObject.getJSONArray("array").get(0));
        assertEquals(2, jsonObject.getJSONArray("array").get(1));
        assertEquals(2.2, jsonObject.get("double"));
        assertEquals("json", jsonObject.get("name"));
    }

    @Test
    public void testJSONObjectFilter() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        String json = "{'fieldName':'zespol','operator':'iEquals','value':'z1'}";
        JSONObject jsonObject = JSONObject.fromObject(json);
        assertEquals("zespol", jsonObject.get("fieldName"));
        assertEquals("iEquals", jsonObject.get("operator"));
        assertEquals("z1", jsonObject.get("value"));
    }

    @Test
    public void testJSONObjectFilter2() {
        String json = "{'_constructor':'AdvancedCriteria','operator':'or','criteria':[{'fieldName':'liczba','operator':'between','start':1,'end':10},{'_constructor':'AdvancedCriteria','operator':'and','criteria':[{'fieldName':'segment','operator':'iContains','value':'sm'},{'fieldName':'klient','operator':'iStartsWith','value':'Firma'}]}]}";
        JSONObject jsonObject = JSONObject.fromObject(json);
        assertEquals("AdvancedCriteria", jsonObject.get("_constructor"));
        assertEquals("or", jsonObject.get("operator"));
        JSONArray criteriaArray = jsonObject.getJSONArray("criteria");
        JSONObject c1 = (JSONObject) criteriaArray.get(0);
        assertEquals("liczba", c1.get("fieldName"));
        assertEquals("between", c1.get("operator"));
        JSONObject c2 = (JSONObject) criteriaArray.get(1);
        assertEquals("AdvancedCriteria", c2.get("_constructor"));
        assertEquals("and", c2.get("operator"));
    }

    @Test
    public void testJSONObjectFilter3() {
        String json = "{\"_constructor\": \"AdvancedCriteria\", \"operator\":\"and\",\"criteria\": [{\"fieldName\":\"region\",\"operator\":\"iStartsWith\",\"value\":\"W\"}]}";
        JSONObject jsonObject = JSONObject.fromObject(json);
        assertEquals("AdvancedCriteria", jsonObject.get("_constructor"));
        assertEquals("and", jsonObject.get("operator"));
        JSONArray criteriaArray = jsonObject.getJSONArray("criteria");
        JSONObject c1 = (JSONObject) criteriaArray.get(0);
        assertEquals("region", c1.get("fieldName"));
        assertEquals("iStartsWith", c1.get("operator"));
        assertEquals("W", c1.get("value"));
    }

    @Test
    public void testParseSimple() {
        String json = "{'fieldName':'zespol','operator':'iEquals','value':'z1'}";
        Criteria c = this.instance.parse(json);
        assertTrue(c instanceof ValueCriteria);
        ValueCriteria criteria = (ValueCriteria) c;
        assertEquals("zespol", criteria.getFieldName());
        assertEquals(Operator.IEQUALS, criteria.getOperator());
        assertEquals("z1", criteria.getValue());
    }

    @Test
    public void testParseSimpleBetween() {
        String json = "{'fieldName':'liczba','operator':'between','start':1,'end':10}";
        Criteria c = this.instance.parse(json);
        assertTrue(c instanceof BetweenCriteria);
        BetweenCriteria criteria = (BetweenCriteria) c;
        assertEquals("liczba", criteria.getFieldName());
        assertEquals(Operator.BETWEEN, criteria.getOperator());
        assertEquals(1, criteria.getStart());
        assertEquals(10, criteria.getEnd());
    }

    @Test
    public void testParseSimpleNull() {
        String json = "{'fieldName':'liczba','operator':'isNull'}";
        Criteria c = this.instance.parse(json);
        assertTrue(c instanceof NullCriteria);
        NullCriteria criteria = (NullCriteria) c;
        assertEquals("liczba", criteria.getFieldName());
        assertEquals(Operator.IS_NULL, criteria.getOperator());
    }

    @Test
    public void testParseSimpleField() {
        String json = "{'fieldName':'liczba','operator':'greaterThanField','value':'druga_liczba'}";
        Criteria c = this.instance.parse(json);
        assertTrue(c instanceof FieldCriteria);
        FieldCriteria criteria = (FieldCriteria) c;
        assertEquals("liczba", criteria.getFieldName());
        assertEquals(Operator.GREATER_THAN_FIELD, criteria.getOperator());
        assertEquals("druga_liczba", criteria.getOtherFieldName());
    }

    @Test
    public void testParseAdvance() {
        String json = "{'_constructor':'AdvancedCriteria','operator':'or','criteria':[{'fieldName':'liczba','operator':'between','start':1,'end':10},{'_constructor':'AdvancedCriteria','operator':'and','criteria':[{'fieldName':'segment','operator':'iContains','value':'sm'},{'fieldName':'klient','operator':'iStartsWith','value':'Firma'}]}]}";
        Criteria c = this.instance.parse(json);
        assertTrue(c instanceof AdvanceCriteria);
        AdvanceCriteria criteria = (AdvanceCriteria) c;
        assertEquals(Operator.OR, criteria.getOperator());
        List<Criteria> childs = criteria.getCriteria();
        BetweenCriteria c1 = (BetweenCriteria) childs.get(0);
        assertEquals("liczba", c1.getFieldName());
        assertEquals(Operator.BETWEEN, c1.getOperator());
        assertEquals(1, c1.getStart());
        assertEquals(10, c1.getEnd());
        AdvanceCriteria c2 = (AdvanceCriteria) childs.get(1);
        assertEquals(Operator.AND, c2.getOperator());
        List<Criteria> c2childs = c2.getCriteria();
        ValueCriteria c21 = (ValueCriteria) c2childs.get(0);
        assertEquals("segment", c21.getFieldName());
        assertEquals(Operator.ICONTAINS, c21.getOperator());
        assertEquals("sm", c21.getValue());
        ValueCriteria c22 = (ValueCriteria) c2childs.get(1);
        assertEquals("klient", c22.getFieldName());
        assertEquals(Operator.ISTARTS_WITH, c22.getOperator());
        assertEquals("Firma", c22.getValue());
        displayTree(criteria, 0);
    }

    @Test
    public void testAllOperators() {
        Map<String, Operator> valueToOperator = new HashMap<String, Operator>();
        for (Operator op : Operator.values()) {
            valueToOperator.put(op.getValue(), op);
            System.out.println(op);
        }
        System.out.println();
        String json = "{'_constructor':'AdvancedCriteria','operator':'and','criteria':[{'fieldName':'zespol','operator':'iEquals','value':'z1'},{'fieldName':'segment','operator':'iNotEqual','value':'s1'},{'fieldName':'klient','operator':'iContains','value':'firma'},{'fieldName':'telefon','operator':'iStartsWith','value':'700'},{'fieldName':'telefon','operator':'iEndsWith','value':'62'},{'fieldName':'zespol','operator':'iNotContains','value':'90'},{'fieldName':'zespol','operator':'iNotStartsWith','value':'wa'},{'fieldName':'zespol','operator':'iNotEndsWith','value':'ow'},{'fieldName':'zespol','operator':'equals','value':'wroclaw'},{'fieldName':'zespol','operator':'notEqual','value':'gdansk'},{'fieldName':'zespol','operator':'lessThan','value':'100'},{'fieldName':'zespol','operator':'greaterThan','value':'50'},{'fieldName':'zespol','operator':'lessOrEqual','value':'90'},{'fieldName':'zespol','operator':'greaterOrEqual','value':'80'},{'fieldName':'zespol','operator':'between','start':'20','end':'40'},{'fieldName':'zespol','operator':'isNull'},{'fieldName':'zespol','operator':'notNull'},{'fieldName':'zespol','operator':'equalsField','value':'doradca'},{'fieldName':'spotkania30','operator':'notEqualField','value':'spotkania90'},{'fieldName':'zespol','operator':'greaterThanField','value':'spotkania90'},{'fieldName':'zespol','operator':'lessThanField','value':'doradca'},{'fieldName':'zespol','operator':'greaterOrEqualField','value':'spotkania90'},{'fieldName':'zespol','operator':'lessOrEqualField','value':'spotkania30'}]}";
        JSONObject jsonObject = JSONObject.fromObject(json);
        JSONArray array = jsonObject.getJSONArray("criteria");
        for (int i = 0; i < array.size(); ++i) {
            JSONObject ob = array.getJSONObject(i);
            Operator op = valueToOperator.get(ob.get("operator"));
            System.out.print(op);
            System.out.println();
        }
    }

    private void displayTree(Criteria c, int level) {
        String tab = "";
        for (int i = 0; i < level; ++i) {
            tab += "\t";
        }
        if (c instanceof SimpleCriteria) {
            SimpleCriteria sc = (SimpleCriteria) c;
            System.out.println(tab + "" + sc.getFieldName() + " " + sc.getOperator());
        } else {
            AdvanceCriteria sc = (AdvanceCriteria) c;
            System.out.println(tab + "" + sc.getOperator());
            for (Criteria child : sc.getCriteria()) {
                displayTree(child, level + 1);
            }
        }
    }
}

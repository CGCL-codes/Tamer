package net.sf.csutils.core.query.impl;

import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import javax.xml.registry.JAXRException;
import net.sf.csutils.core.model.ROMetaModel;
import net.sf.csutils.core.model.impl.ROMetaModelReader;
import net.sf.csutils.core.query.CsqlParser;
import net.sf.csutils.core.query.CsqlStatement;
import net.sf.csutils.core.query.tree.And;
import net.sf.csutils.core.query.tree.Eq;
import net.sf.csutils.core.query.tree.Negation;
import net.sf.csutils.core.query.tree.NodeFactory;
import net.sf.csutils.core.query.tree.Or;
import net.sf.csutils.core.query.tree.UnaryMinus;
import net.sf.csutils.core.registry.jaxmas.JaxMasRegistryInfo;
import org.junit.Assert;
import org.junit.Test;
import antlr.RecognitionException;

public class SqlQueryGeneratorTest {

    private final NodeFactory nodeFactory = new NodeFactory() {

        @Override
        public RecognitionException error(String pMessage) {
            return new RecognitionException(pMessage);
        }
    };

    @Test
    public void testParseAnd() throws Exception {
        final Eq eq1 = nodeFactory.eq(Integer.valueOf(3), Integer.valueOf(4));
        final Eq eq2 = nodeFactory.eq(Integer.valueOf(4), Integer.valueOf(5));
        final Eq eq3 = nodeFactory.eq(Integer.valueOf(5), Integer.valueOf(6));
        final SqlQueryGenerator xg1 = new SqlQueryGenerator();
        xg1.parse(nodeFactory.and(eq1, eq2));
        final String got1 = xg1.sb.toString();
        Assert.assertEquals("(3=4 and 4=5)", got1);
        final SqlQueryGenerator xg2 = new SqlQueryGenerator();
        xg2.parse(nodeFactory.and(nodeFactory.and(eq1, eq2), eq3));
        final String got2 = xg2.sb.toString();
        Assert.assertEquals("(3=4 and 4=5 and 5=6)", got2);
    }

    @Test
    public void testParseOr() throws Exception {
        final Eq eq1 = nodeFactory.eq(Integer.valueOf(3), Integer.valueOf(4));
        final Eq eq2 = nodeFactory.eq(Integer.valueOf(4), Integer.valueOf(5));
        final Eq eq3 = nodeFactory.eq(Integer.valueOf(5), Integer.valueOf(6));
        final SqlQueryGenerator xg1 = new SqlQueryGenerator();
        xg1.parse(nodeFactory.or(eq1, eq2));
        final String got1 = xg1.sb.toString();
        Assert.assertEquals("(3=4 or 4=5)", got1);
        final SqlQueryGenerator xg2 = new SqlQueryGenerator();
        xg2.parse(nodeFactory.or(nodeFactory.or(eq1, eq2), eq3));
        final String got2 = xg2.sb.toString();
        Assert.assertEquals("(3=4 or 4=5 or 5=6)", got2);
    }

    @Test
    public void testParseNegation() throws Exception {
        final Eq eq1 = nodeFactory.eq(Integer.valueOf(3), Integer.valueOf(4));
        final Negation negation1 = (Negation) nodeFactory.negate(eq1);
        final SqlQueryGenerator xg1 = new SqlQueryGenerator();
        xg1.parse(negation1);
        final String got1 = xg1.sb.toString();
        Assert.assertEquals("NOT 3=4", got1);
    }

    @Test
    public void testParseRelationalExpressionString() throws Exception {
        {
            final SqlQueryGenerator xg1 = new SqlQueryGenerator();
            xg1.parse(nodeFactory.eq(Integer.valueOf(3), Integer.valueOf(4)), "=");
            final String got = xg1.sb.toString();
            Assert.assertEquals("3=4", got);
        }
        {
            final SqlQueryGenerator xg1 = new SqlQueryGenerator();
            xg1.parse(nodeFactory.ne(Integer.valueOf(3), Integer.valueOf(4)), "!=");
            final String got = xg1.sb.toString();
            Assert.assertEquals("3!=4", got);
        }
        {
            final SqlQueryGenerator xg1 = new SqlQueryGenerator();
            xg1.parse(nodeFactory.lt(Integer.valueOf(3), Integer.valueOf(4)), "<");
            final String got = xg1.sb.toString();
            Assert.assertEquals("3<4", got);
        }
        {
            final SqlQueryGenerator xg1 = new SqlQueryGenerator();
            xg1.parse(nodeFactory.le(Integer.valueOf(3), Integer.valueOf(4)), "<=");
            final String got = xg1.sb.toString();
            Assert.assertEquals("3<=4", got);
        }
        {
            final SqlQueryGenerator xg1 = new SqlQueryGenerator();
            xg1.parse(nodeFactory.gt(Integer.valueOf(3), Integer.valueOf(4)), ">");
            final String got = xg1.sb.toString();
            Assert.assertEquals("3>4", got);
        }
        {
            final SqlQueryGenerator xg1 = new SqlQueryGenerator();
            xg1.parse(nodeFactory.ge(Integer.valueOf(3), Integer.valueOf(4)), ">=");
            final String got = xg1.sb.toString();
            Assert.assertEquals("3>=4", got);
        }
    }

    @Test
    public void testParseConcatenation() throws Exception {
        final SqlQueryGenerator xg1 = new SqlQueryGenerator();
        xg1.parse(nodeFactory.concatenate("a", "b"));
        final String got1 = xg1.sb.toString();
        Assert.assertEquals("concat('a','b')", got1);
        final SqlQueryGenerator xg2 = new SqlQueryGenerator();
        xg2.parse(nodeFactory.concatenate(nodeFactory.concatenate("a", "b"), "c"));
        final String got2 = xg2.sb.toString();
        Assert.assertEquals("concat('a','b','c')", got2);
    }

    @Test
    public void testParseAdditiveExpression() throws Exception {
        final SqlQueryGenerator xg1 = new SqlQueryGenerator();
        xg1.parse(nodeFactory.addExpression(Integer.valueOf(3), Integer.valueOf(4)));
        final String got1 = xg1.sb.toString();
        Assert.assertEquals("3+4", got1);
        final SqlQueryGenerator xg2 = new SqlQueryGenerator();
        xg2.parse(nodeFactory.minusExpression(Integer.valueOf(3), Integer.valueOf(4)));
        final String got2 = xg2.sb.toString();
        Assert.assertEquals("3-4", got2);
        final SqlQueryGenerator xg3 = new SqlQueryGenerator();
        xg3.parse(nodeFactory.minusExpression(nodeFactory.addExpression(Integer.valueOf(3), Integer.valueOf(4)), Integer.valueOf(5)));
        final String got3 = xg3.sb.toString();
        Assert.assertEquals("3+4-5", got3);
    }

    @Test
    public void testParseMultiplicativeExpression() throws Exception {
        final SqlQueryGenerator xg1 = new SqlQueryGenerator();
        xg1.parse(nodeFactory.multiplyExpression(Integer.valueOf(3), Integer.valueOf(4)));
        final String got1 = xg1.sb.toString();
        Assert.assertEquals("3*4", got1);
        final SqlQueryGenerator xg2 = new SqlQueryGenerator();
        xg2.parse(nodeFactory.divideExpression(Integer.valueOf(3), Integer.valueOf(4)));
        final String got2 = xg2.sb.toString();
        Assert.assertEquals("3/4", got2);
        final SqlQueryGenerator xg3 = new SqlQueryGenerator();
        xg3.parse(nodeFactory.divideExpression(nodeFactory.multiplyExpression(Integer.valueOf(3), Integer.valueOf(4)), Integer.valueOf(5)));
        final String got3 = xg3.sb.toString();
        Assert.assertEquals("3*4/5", got3);
    }

    @Test
    public void testParseUnaryMinus() throws Exception {
        final SqlQueryGenerator xg1 = new SqlQueryGenerator();
        xg1.parse((UnaryMinus) nodeFactory.unaryMinus("a"));
        final String got1 = xg1.sb.toString();
        Assert.assertEquals("-'a'", got1);
    }

    @Test
    public void testParseParameter() throws Exception {
        final SqlQueryGenerator xg1 = new SqlQueryGenerator();
        final Eq eq1 = nodeFactory.eq(Integer.valueOf(3), nodeFactory.namedParameter("foo"));
        final Eq eq2 = nodeFactory.eq(Integer.valueOf(4), nodeFactory.parameter());
        final Eq eq3 = nodeFactory.eq(Integer.valueOf(5), nodeFactory.numberedParameter("12"));
        final Eq eq4 = nodeFactory.eq(Integer.valueOf(6), nodeFactory.parameter());
        xg1.parse(nodeFactory.and(nodeFactory.and(nodeFactory.and(eq1, eq2), eq3), eq4));
        final String got = xg1.sb.toString();
        Assert.assertEquals("(3=:foo and 4=? and 5=? and 6=?)", got);
        final List<XQueryGenerator.Parameter> parameters = xg1.getParameters();
        Assert.assertEquals(4, parameters.size());
        final SqlQueryGenerator.Parameter p1 = parameters.get(0);
        Assert.assertNotNull(p1);
        Assert.assertTrue(p1 instanceof XQueryGenerator.NamedParameter);
        Assert.assertEquals(3, p1.getOffset());
        Assert.assertEquals(4, p1.getLength());
        Assert.assertEquals("foo", ((XQueryGenerator.NamedParameter) p1).getName());
        final SqlQueryGenerator.Parameter p2 = parameters.get(1);
        Assert.assertNotNull(p2);
        Assert.assertTrue(p2 instanceof XQueryGenerator.NumberedParameter);
        Assert.assertEquals(14, p2.getOffset());
        Assert.assertEquals(2, p2.getLength());
        Assert.assertEquals(0, ((XQueryGenerator.NumberedParameter) p2).getNum());
        final SqlQueryGenerator.Parameter p3 = parameters.get(2);
        Assert.assertNotNull(p3);
        Assert.assertTrue(p3 instanceof XQueryGenerator.NumberedParameter);
        Assert.assertEquals(22, p3.getOffset());
        Assert.assertEquals(3, p3.getLength());
        Assert.assertEquals(12, ((XQueryGenerator.NumberedParameter) p3).getNum());
        final SqlQueryGenerator.Parameter p4 = parameters.get(3);
        Assert.assertNotNull(p4);
        Assert.assertTrue(p4 instanceof XQueryGenerator.NumberedParameter);
        Assert.assertEquals(30, p4.getOffset());
        Assert.assertEquals(2, p4.getLength());
        Assert.assertEquals(1, ((XQueryGenerator.NumberedParameter) p4).getNum());
    }

    @Test
    public void testParseString() throws Exception {
        final SqlQueryGenerator xg1 = new SqlQueryGenerator();
        xg1.parse("a");
        final String got1 = xg1.sb.toString();
        Assert.assertEquals("'a'", got1);
    }

    @Test
    public void testParseNumber() throws Exception {
        final SqlQueryGenerator xg1 = new SqlQueryGenerator();
        xg1.parse(new Double("1.0"));
        final String got1 = xg1.sb.toString();
        Assert.assertEquals("1.0", got1);
    }

    @Test
    public void testParseIsEmpty() throws Exception {
        final SqlQueryGenerator xg1 = new SqlQueryGenerator();
        xg1.parse(nodeFactory.isEmpty(Integer.valueOf(3)));
        final String got1 = xg1.sb.toString();
        Assert.assertEquals("''=3", got1);
    }

    @Test
    public void testParseIsNull() throws Exception {
        final SqlQueryGenerator xg1 = new SqlQueryGenerator();
        xg1.parse(nodeFactory.isNull(Integer.valueOf(3)));
        final String got1 = xg1.sb.toString();
        Assert.assertEquals("3 IS NULL", got1);
        final SqlQueryGenerator xg2 = new SqlQueryGenerator();
        xg2.parse((Negation) nodeFactory.negate(nodeFactory.isNull(Integer.valueOf(3))));
        final String got2 = xg2.sb.toString();
        Assert.assertEquals("NOT 3 IS NULL", got2);
    }

    @Test
    public void testParseCalendar() throws Exception {
        final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal.set(Calendar.YEAR, 2010);
        cal.set(Calendar.MONTH, Calendar.FEBRUARY);
        cal.set(Calendar.DAY_OF_MONTH, 6);
        cal.set(Calendar.HOUR_OF_DAY, 15);
        cal.set(Calendar.MINUTE, 23);
        cal.set(Calendar.SECOND, 16);
        cal.set(Calendar.MILLISECOND, 347);
        final SqlQueryGenerator xg = new SqlQueryGenerator();
        xg.parse(cal);
        final String got = xg.sb.toString();
        Assert.assertEquals("'2010-02-06 16:23:16.0000347'", got);
    }

    @Test
    public void testParseBoolean() throws Exception {
        final SqlQueryGenerator xg1 = new SqlQueryGenerator();
        xg1.parse(Boolean.TRUE);
        final String got1 = xg1.sb.toString();
        Assert.assertEquals("true", got1);
        final SqlQueryGenerator xg2 = new SqlQueryGenerator();
        xg2.parse(Boolean.FALSE);
        final String got2 = xg2.sb.toString();
        Assert.assertEquals("false", got2);
    }

    @Test
    public void testParseExpression() throws Exception {
        final Eq eq1 = nodeFactory.eq(Integer.valueOf(3), Integer.valueOf(4));
        final Eq eq2 = nodeFactory.eq(Integer.valueOf(4), Integer.valueOf(5));
        final And and = nodeFactory.and(eq1, eq2);
        final Eq eq3 = nodeFactory.eq(Boolean.TRUE, "a");
        final Or or = nodeFactory.or(and, eq3);
        final SqlQueryGenerator xg = new SqlQueryGenerator();
        xg.parseExpression(or);
        final String got = xg.sb.toString();
        Assert.assertEquals("((3=4 and 4=5) or true='a')", got);
    }

    private String getSqlQuery(CsqlStatement pSt) {
        return ((SqlCsqlStatement) pSt).getSqlQuery();
    }

    @Test
    public void testParseMethodCall() throws Exception {
        final URL url = getClass().getResource("registryObjectModel.xml");
        Assert.assertNotNull(url);
        final ROMetaModel model = new ROMetaModelReader().read(url);
        final String[] methodNames = new String[] { "key", "status", "stability", "userVersion", "majorVersion", "minorVersion" };
        for (String methodName : methodNames) {
            final String q1 = "FROM User u WHERE u." + methodName + "()=?";
            final CsqlStatement st1 = new CsqlParser(model, JaxMasRegistryInfo.getInstance()).parse(null, q1);
            final String fieldName;
            if ("key".equals(methodName)) {
                fieldName = "ro.roKey";
            } else if ("status".equals(methodName)) {
                fieldName = "re.status";
            } else if ("stability".equals(methodName)) {
                fieldName = "re.stability";
            } else if ("userVersion".equals(methodName)) {
                fieldName = "re.userVersion";
            } else if ("minorVersion".equals(methodName)) {
                fieldName = "re.minorVersion";
            } else if ("majorVersion".equals(methodName)) {
                fieldName = "re.majorVersion";
            } else {
                throw new IllegalStateException("Invalid method name: " + methodName);
            }
            Assert.assertEquals("SELECT uro.roKey, uro.roType, uro.roOwnerRestricting, uro.roOwnerCascading," + " uro.pos, ure.customType, ure.expiration, ure.status, ure.stability," + " ure.majorVersion, ure.minorVersion, ure.userVersion" + " FROM RegistryObjects uro JOIN RegistryEntries ure ON uro.roType=5" + " AND uro.roKey=ure.roKey WHERE uro.roType=5 AND uro.roType=5" + " AND ure.customType IN (SELECT ctro.roKey FROM RegistryObjects ctro" + " JOIN Concepts ctco ON ctro.roType=?" + " AND ctro.roKey=ctco.roKey" + " WHERE ctro.roOwnerRestricting=? AND ctco.value=?)" + " AND u" + fieldName + "=?", getSqlQuery(st1));
        }
    }

    @Test
    public void testParseSelectStatement() throws Exception {
        final URL url = getClass().getResource("registryObjectModel.xml");
        Assert.assertNotNull(url);
        final ROMetaModel model = new ROMetaModelReader().read(url);
        final String q1 = "FROM User u WHERE u.name=? or u.name()=? or u.name('de_DE')=?";
        final CsqlStatement st1 = new CsqlParser(model, JaxMasRegistryInfo.getInstance()).parse(null, q1);
        final String expect = "SELECT uro.roKey, uro.roType, uro.roOwnerRestricting, uro.roOwnerCascading," + " uro.pos, ure.customType, ure.expiration, ure.status, ure.stability," + " ure.majorVersion, ure.minorVersion, ure.userVersion" + " FROM RegistryObjects uro JOIN RegistryEntries ure" + " ON uro.roType=5 AND uro.roKey=ure.roKey" + " WHERE uro.roType=5 AND uro.roType=5 AND" + " ure.customType IN (SELECT ctro.roKey FROM RegistryObjects ctro" + " JOIN Concepts ctco ON ctro.roType=? AND ctro.roKey=ctco.roKey" + " WHERE ctro.roOwnerRestricting=? AND ctco.value=?)" + " AND (EXISTS(SELECT uros.roKey FROM RegistryObjectSlots uros" + " JOIN RegistryObjectSlotValues urosv ON uros.id=urosv.id" + " WHERE uros.roKey=uro.roKey AND uros.name='{SomeNamespace}name' AND urosv.val=?)" + " or EXISTS(SELECT uls0.roKey FROM LocalizedStrings uls0" + " WHERE uls0.roKey=uro.roKey AND uls0.lsType=0 AND uls0.val=?)" + " or EXISTS(SELECT uls1.roKey FROM LocalizedStrings uls1" + " WHERE uls1.roKey=uro.roKey AND uls1.lsType=0 AND" + " uls1.locale='de_DE' AND uls1.val=?))";
        final String got = getSqlQuery(st1);
        Assert.assertEquals(expect, got);
        final String q2 = "FROM User u JOIN u.employeeOf o WHERE u.name=? AND o.name=?";
        final CsqlStatement st2 = new CsqlParser(model, JaxMasRegistryInfo.getInstance()).parse(null, q2);
        Assert.assertEquals("SELECT uro.roKey, uro.roType, uro.roOwnerRestricting, uro.roOwnerCascading," + " uro.pos, oro.roKey, oro.roType, oro.roOwnerRestricting, oro.roOwnerCascading," + " oro.pos, ure.customType, ure.expiration, ure.status, ure.stability," + " ure.majorVersion, ure.minorVersion, ure.userVersion, ore.customType," + " ore.expiration, ore.status, ore.stability, ore.majorVersion," + " ore.minorVersion, ore.userVersion FROM RegistryObjects uro" + " JOIN RegistryObjects oro ON oro.roType=5" + " JOIN RegistryEntries ure ON uro.roType=5 AND uro.roKey=ure.roKey" + " JOIN RegistryEntries ore ON oro.roType=5 AND oro.roKey=ore.roKey" + " WHERE uro.roType=5 AND uro.roType=5" + " AND ure.customType IN (SELECT ctro.roKey FROM RegistryObjects ctro" + " JOIN Concepts ctco ON ctro.roType=?" + " AND ctro.roKey=ctco.roKey" + " WHERE ctro.roOwnerRestricting=? AND ctco.value=?)" + " AND (EXISTS(SELECT uros.roKey FROM RegistryObjectSlots uros" + " JOIN RegistryObjectSlotValues urosv ON uros.id=urosv.id" + " WHERE uros.roKey=uro.roKey AND uros.name='{SomeNamespace}name' AND urosv.val=?)" + " and EXISTS(SELECT oros.roKey FROM RegistryObjectSlots oros" + " JOIN RegistryObjectSlotValues orosv ON oros.id=orosv.id" + " WHERE oros.roKey=oro.roKey AND oros.name='{SomeNamespace}name' AND orosv.val=?))" + " AND oro.roType=5" + " AND ore.customType IN (SELECT ctro.roKey FROM RegistryObjects ctro" + " JOIN Concepts ctco ON ctro.roType=?" + " AND ctro.roKey=ctco.roKey" + " WHERE ctro.roOwnerRestricting=? AND ctco.value=?)" + " AND EXISTS(SELECT uassoc.roKey FROM RegistryObjects uassocro" + " JOIN Associations uassoc ON uassocro.roKey=uassoc.roKey" + " AND uassocro.roOwnerCascading=uro.roKey" + " AND uassoc.roKeyTarget=oro.roKey" + " JOIN RegistryObjectSlots uros ON uassocro.roKey=uros.roKey" + " JOIN RegistryObjectSlotValues urosv ON uros.id=urosv.id AND uros.name=? AND urosv.val=?)", getSqlQuery(st2));
    }

    @Test
    public void testNamespaces() throws Exception {
        final URL url = getClass().getResource("registryObjectModel2.xml");
        Assert.assertNotNull(url);
        final ROMetaModel model = new ROMetaModelReader().read(url);
        final String q1 = "FROM Organization AS o";
        try {
            new CsqlParser(model, JaxMasRegistryInfo.getInstance()).parse(null, q1);
            Assert.fail("Expected exception");
        } catch (JAXRException e) {
            Assert.assertEquals("Multiple matching object types found for Organization: {SomeNamespace}Organization, {OtherNamespace}Organization", e.getMessage());
        }
        final String q2 = "declare namespace s='SomeNamespace' FROM s:Organization AS o";
        final CsqlStatement st2 = new CsqlParser(model, JaxMasRegistryInfo.getInstance()).parse(null, q2);
        Assert.assertEquals("SELECT oro.roKey, oro.roType, oro.roOwnerRestricting, oro.roOwnerCascading," + " oro.pos, ore.customType, ore.expiration, ore.status, ore.stability," + " ore.majorVersion, ore.minorVersion, ore.userVersion" + " FROM RegistryObjects oro JOIN RegistryEntries ore" + " ON oro.roType=5 AND oro.roKey=ore.roKey WHERE oro.roType=5 AND oro.roType=5" + " AND ore.customType IN (SELECT ctro.roKey FROM RegistryObjects ctro" + " JOIN Concepts ctco ON ctro.roType=? AND ctro.roKey=ctco.roKey" + " WHERE ctro.roOwnerRestricting=? AND ctco.value=?)", getSqlQuery(st2));
        final String q3 = "declare namespace o='OtherNamespace' FROM o:Organization AS o";
        final CsqlStatement st3 = new CsqlParser(model, JaxMasRegistryInfo.getInstance()).parse(null, q3);
        Assert.assertEquals("SELECT oro.roKey, oro.roType, oro.roOwnerRestricting, oro.roOwnerCascading," + " oro.pos, ore.customType, ore.expiration, ore.status, ore.stability," + " ore.majorVersion, ore.minorVersion, ore.userVersion" + " FROM RegistryObjects oro JOIN RegistryEntries ore ON oro.roType=5" + " AND oro.roKey=ore.roKey WHERE oro.roType=5 AND oro.roType=5" + " AND ore.customType IN (SELECT ctro.roKey FROM RegistryObjects ctro" + " JOIN Concepts ctco ON ctro.roType=? AND ctro.roKey=ctco.roKey" + " WHERE ctro.roOwnerRestricting=? AND ctco.value=?)", getSqlQuery(st3));
    }

    @Test
    public void testLike() throws Exception {
        final URL url = getClass().getResource("registryObjectModel.xml");
        Assert.assertNotNull(url);
        final ROMetaModel model = new ROMetaModelReader().read(url);
        final String q1 = "FROM User AS u WHERE u.name() LIKE '%abc'";
        final CsqlStatement st1 = new CsqlParser(model, JaxMasRegistryInfo.getInstance()).parse(null, q1);
        Assert.assertEquals("SELECT uro.roKey, uro.roType, uro.roOwnerRestricting, uro.roOwnerCascading," + " uro.pos, ure.customType, ure.expiration, ure.status, ure.stability," + " ure.majorVersion, ure.minorVersion, ure.userVersion" + " FROM RegistryObjects uro JOIN RegistryEntries ure ON uro.roType=5" + " AND uro.roKey=ure.roKey WHERE uro.roType=5 AND uro.roType=5" + " AND ure.customType IN (SELECT ctro.roKey FROM RegistryObjects ctro" + " JOIN Concepts ctco ON ctro.roType=? AND ctro.roKey=ctco.roKey" + " WHERE ctro.roOwnerRestricting=? AND ctco.value=?)" + " AND EXISTS(SELECT uls0.roKey FROM LocalizedStrings uls0" + " WHERE uls0.roKey=uro.roKey AND uls0.lsType=0 AND uls0.val LIKE '%abc')", getSqlQuery(st1));
        final String q2 = "FROM User AS u WHERE UPPER(u.name()) LIKE UPPER('%abc')";
        final CsqlStatement st2 = new CsqlParser(model, JaxMasRegistryInfo.getInstance()).parse(null, q2);
        Assert.assertEquals("SELECT uro.roKey, uro.roType, uro.roOwnerRestricting, uro.roOwnerCascading," + " uro.pos, ure.customType, ure.expiration, ure.status, ure.stability," + " ure.majorVersion, ure.minorVersion, ure.userVersion" + " FROM RegistryObjects uro JOIN RegistryEntries ure" + " ON uro.roType=5 AND uro.roKey=ure.roKey WHERE uro.roType=5" + " AND uro.roType=5 AND ure.customType IN (SELECT ctro.roKey" + " FROM RegistryObjects ctro JOIN Concepts ctco ON ctro.roType=?" + " AND ctro.roKey=ctco.roKey WHERE ctro.roOwnerRestricting=? AND ctco.value=?)" + " AND EXISTS(SELECT uls0.roKey FROM LocalizedStrings uls0" + " WHERE uls0.roKey=uro.roKey AND uls0.lsType=0 AND UPPER(uls0.val) LIKE UPPER('%abc'))", getSqlQuery(st2));
        final String q3 = "FROM User AS u WHERE LOWER(u.name()) LIKE LOWER('%abc')";
        final CsqlStatement st3 = new CsqlParser(model, JaxMasRegistryInfo.getInstance()).parse(null, q3);
        Assert.assertEquals("SELECT uro.roKey, uro.roType, uro.roOwnerRestricting, uro.roOwnerCascading," + " uro.pos, ure.customType, ure.expiration, ure.status, ure.stability," + " ure.majorVersion, ure.minorVersion, ure.userVersion" + " FROM RegistryObjects uro JOIN RegistryEntries ure ON uro.roType=5" + " AND uro.roKey=ure.roKey WHERE uro.roType=5 AND uro.roType=5" + " AND ure.customType IN (SELECT ctro.roKey FROM RegistryObjects ctro" + " JOIN Concepts ctco ON ctro.roType=? AND ctro.roKey=ctco.roKey" + " WHERE ctro.roOwnerRestricting=? AND ctco.value=?)" + " AND EXISTS(SELECT uls0.roKey FROM LocalizedStrings uls0" + " WHERE uls0.roKey=uro.roKey AND uls0.lsType=0 AND LOWER(uls0.val) LIKE LOWER('%abc'))", getSqlQuery(st3));
        final String q4 = "FROM User AS u WHERE upper(u.name())=upper(u.description())";
        final CsqlStatement st4 = new CsqlParser(model, JaxMasRegistryInfo.getInstance()).parse(null, q4);
        Assert.assertEquals("SELECT uro.roKey, uro.roType, uro.roOwnerRestricting, uro.roOwnerCascading," + " uro.pos, ure.customType, ure.expiration, ure.status, ure.stability," + " ure.majorVersion, ure.minorVersion, ure.userVersion" + " FROM RegistryObjects uro JOIN RegistryEntries ure ON uro.roType=5" + " AND uro.roKey=ure.roKey WHERE uro.roType=5 AND uro.roType=5" + " AND ure.customType IN (SELECT ctro.roKey FROM RegistryObjects ctro" + " JOIN Concepts ctco ON ctro.roType=? AND ctro.roKey=ctco.roKey" + " WHERE ctro.roOwnerRestricting=? AND ctco.value=?)" + " AND EXISTS(SELECT uls1.roKey FROM LocalizedStrings uls1" + " WHERE uls1.roKey=uro.roKey AND uls1.lsType=0 AND" + " EXISTS(SELECT uls0.roKey FROM LocalizedStrings uls0 WHERE" + " uls0.roKey=uro.roKey AND uls0.lsType=0 AND UPPER(uls0.val)=UPPER(uls1.val)))", getSqlQuery(st4));
    }

    @Test
    public void testNoNamespaceObject() throws Exception {
        final URL url = getClass().getResource("registryObjectModel.xml");
        Assert.assertNotNull(url);
        final ROMetaModel model = new ROMetaModelReader().read(url);
        final String q1 = "FROM NoNamespaceObject n";
        final CsqlStatement st1 = new CsqlParser(model, JaxMasRegistryInfo.getInstance()).parse(null, q1);
        Assert.assertEquals("SELECT nro.roKey, nro.roType, nro.roOwnerRestricting, nro.roOwnerCascading," + " nro.pos, nre.customType, nre.expiration, nre.status, nre.stability," + " nre.majorVersion, nre.minorVersion, nre.userVersion" + " FROM RegistryObjects nro JOIN RegistryEntries nre ON nro.roType=5" + " AND nro.roKey=nre.roKey WHERE nro.roType=5 AND nro.roType=5" + " AND nre.customType IN (SELECT ctro.roKey FROM RegistryObjects ctro" + " JOIN Concepts ctco ON ctro.roType=? AND ctro.roKey=ctco.roKey" + " WHERE ctro.roOwnerRestricting=? AND ctco.value=?)", getSqlQuery(st1));
    }

    @Test
    public void testAllTypes() throws Exception {
        final URL url = getClass().getResource("registryObjectModel.xml");
        Assert.assertNotNull(url);
        final ROMetaModel model = new ROMetaModelReader().read(url);
        final String q1 = "DECLARE NAMESPACE q='" + AbstractQueryGenerator.NS_QUERY + "'\n" + "FROM q:allTypes o WHERE o.name()=?";
        final CsqlStatement st1 = new CsqlParser(model, JaxMasRegistryInfo.getInstance()).parse(null, q1);
        Assert.assertEquals("SELECT oro.roKey, oro.roType, oro.roOwnerRestricting, oro.roOwnerCascading," + " oro.pos, oc.value, ore.customType, ore.expiration, ore.status, ore.stability," + " ore.majorVersion, ore.minorVersion, ore.userVersion, oassoc.roKeyTarget," + " oassoc.roKeyType, oassoc.num, ocl.roKeyConcept, ocl.num" + " FROM RegistryObjects oro LEFT OUTER JOIN Concepts oc ON oro.roType=10" + " AND oro.roKey=oc.roKey LEFT OUTER JOIN RegistryEntries ore" + " ON (oro.roType=7 OR oro.roType=5) AND oro.roKey=ore.roKey" + " LEFT OUTER JOIN Associations oassoc ON oro.roType=0" + " AND oro.roKey=oassoc.roKey LEFT OUTER JOIN Classifications ocl" + " ON oro.roType=2 AND oro.roKey=ocl.roKey" + " WHERE EXISTS(SELECT ols0.roKey FROM LocalizedStrings ols0" + " WHERE ols0.roKey=oro.roKey AND ols0.lsType=0 AND ols0.val=?)", getSqlQuery(st1));
        final String q2 = "FROM * o WHERE o.name()=?";
        final CsqlStatement st2 = new CsqlParser(model, JaxMasRegistryInfo.getInstance()).parse(null, q2);
        Assert.assertEquals("SELECT oro.roKey, oro.roType, oro.roOwnerRestricting, oro.roOwnerCascading," + " oro.pos, oc.value, ore.customType, ore.expiration, ore.status, ore.stability," + " ore.majorVersion, ore.minorVersion, ore.userVersion, oassoc.roKeyTarget," + " oassoc.roKeyType, oassoc.num, ocl.roKeyConcept, ocl.num" + " FROM RegistryObjects oro LEFT OUTER JOIN Concepts oc ON oro.roType=10" + " AND oro.roKey=oc.roKey LEFT OUTER JOIN RegistryEntries ore" + " ON (oro.roType=7 OR oro.roType=5) AND oro.roKey=ore.roKey" + " LEFT OUTER JOIN Associations oassoc ON oro.roType=0" + " AND oro.roKey=oassoc.roKey LEFT OUTER JOIN Classifications ocl" + " ON oro.roType=2 AND oro.roKey=ocl.roKey" + " WHERE EXISTS(SELECT ols0.roKey FROM LocalizedStrings ols0" + " WHERE ols0.roKey=oro.roKey AND ols0.lsType=0 AND ols0.val=?)", getSqlQuery(st2));
    }

    @Test
    public void testBuiltinTypes() throws Exception {
        final URL url = getClass().getResource("registryObjectModel.xml");
        Assert.assertNotNull(url);
        final ROMetaModel model = new ROMetaModelReader().read(url);
        final String q1 = "FROM Service s WHERE s.name()=?";
        final CsqlStatement st1 = new CsqlParser(model, JaxMasRegistryInfo.getInstance()).parse(null, q1);
        Assert.assertEquals("SELECT sro.roKey, sro.roType, sro.roOwnerRestricting, sro.roOwnerCascading," + " sro.pos, sre.customType, sre.expiration, sre.status, sre.stability," + " sre.majorVersion, sre.minorVersion, sre.userVersion" + " FROM RegistryObjects sro JOIN RegistryEntries sre ON sro.roType=6" + " AND sro.roKey=sre.roKey WHERE sro.roType=6" + " AND EXISTS(SELECT sls0.roKey FROM LocalizedStrings sls0" + " WHERE sls0.roKey=sro.roKey AND sls0.lsType=0 AND sls0.val=?)", getSqlQuery(st1));
    }

    @Test
    public void testIn() throws Exception {
        final URL url = getClass().getResource("registryObjectModel.xml");
        Assert.assertNotNull(url);
        final ROMetaModel model = new ROMetaModelReader().read(url);
        final String q1 = "FROM Service s WHERE s.name() IN (?)";
        final CsqlStatement st1 = new CsqlParser(model, JaxMasRegistryInfo.getInstance()).parse(null, q1);
        Assert.assertEquals("SELECT sro.roKey, sro.roType, sro.roOwnerRestricting, sro.roOwnerCascading," + " sro.pos, sre.customType, sre.expiration, sre.status, sre.stability," + " sre.majorVersion, sre.minorVersion, sre.userVersion" + " FROM RegistryObjects sro JOIN RegistryEntries sre ON sro.roType=6" + " AND sro.roKey=sre.roKey WHERE sro.roType=6" + " AND EXISTS(SELECT sls0.roKey FROM LocalizedStrings sls0" + " WHERE sls0.roKey=sro.roKey AND sls0.lsType=0 AND sls0.val IN (?))", getSqlQuery(st1));
    }
}

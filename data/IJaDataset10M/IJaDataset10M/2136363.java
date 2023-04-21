package br.com.bit.ideias.reflection.rql;

import static org.junit.Assert.fail;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import br.com.bit.ideias.reflection.core.Introspector;
import br.com.bit.ideias.reflection.exceptions.NoResultException;
import br.com.bit.ideias.reflection.exceptions.SyntaxException;
import br.com.bit.ideias.reflection.exceptions.TooManyResultException;
import br.com.bit.ideias.reflection.rql.query.Query;
import br.com.bit.ideias.reflection.test.artefacts.ClasseDominio;

/**
 * 
 * @author Leonarod Augusto de Souza Campos
 * @since 21/11/2009 - Dia da consciência negra!
 */
public class RqlTest {

    private final String QUERY_DOMINIO = "FROM br.com.bit.ideias.reflection.test.artefacts.ClasseDominio WHERE target eq 'field' and ";

    private final String QUERY_FILHA = "FROM br.com.bit.ideias.reflection.test.artefacts.ClasseDominioFilha WHERE ";

    private Query query;

    @Test
    public void testForClassShouldWorkWithOrWithoutFromClause() throws Exception {
        String from = "from br.com.bit.ideias.reflection.test.artefacts.ClasseDominio ";
        Introspector.forClass(ClasseDominio.class).query(from + "where name eq 'getAtributoPrivadoString'").uniqueResult();
        Introspector.forClass(ClasseDominio.class).query("where name eq 'getAtributoPrivadoString'").uniqueResult();
        Introspector.forClass(ClasseDominio.class).query("name eq 'getAtributoPrivadoString'").uniqueResult();
    }

    @Test
    public void testInvalidNumberOfParenthesisShouldThrowSyntaxException() throws Exception {
        String message = "Invalid number of parenthesis should have thrown SyntaxException";
        assertThrowsSyntaxException(message, "(");
        assertThrowsSyntaxException(message, ")");
        assertThrowsSyntaxException(message, "(()");
        assertThrowsSyntaxException(message, "(())()(");
    }

    @Test
    public void testInvalidNumberOfConjunctionsShouldThrowSyntaxException() throws Exception {
        String message = "Invalid number of AND/OR should have thrown exception";
        assertThrowsSyntaxException(message, "name eq '' name eq ''");
        assertThrowsSyntaxException(message, "name eq '' and name eq '' or");
    }

    @Test
    public void testUnknownClausesShouldThrowSyntaxException() throws Exception {
        String message = "Invalid number of AND/OR should have thrown exception";
        assertThrowsSyntaxException(message, "yyy eq ''");
        assertThrowsSyntaxException(message, "name eq 'abc' and yyy eq ''");
    }

    @Test
    public void testUnknownOperatorsShouldThrowSyntaxException() throws Exception {
        String message = "Invalid number of AND/OR should have thrown exception";
        assertThrowsSyntaxException(message, "yyy eq ''");
        assertThrowsSyntaxException(message, "name ab 'abc' and method ii ''");
    }

    @Test
    public void testInvalidOperatorForClauseShouldThrowSyntaxException() throws Exception {
        String message = "Invalid operator for clause should have thrown exception";
        assertThrowsSyntaxException(message, "annotation like ''");
        assertThrowsSyntaxException(message, "MODIFIER ne ''");
    }

    @Test
    public void testInvalidRightHand() throws Exception {
        String message = "Right hand must be a literal";
        assertThrowsSyntaxException(message, "annotation like atributoPrivado");
    }

    @Test
    public void testUnterminatedStringShouldThrowSyntaxException() throws Exception {
        String message = "Unterminated String should have thrown exception";
        assertThrowsSyntaxException(message, "yyy eq '");
        assertThrowsSyntaxException(message, "name ab ''abc'");
    }

    @Test
    public void testRestrictionEqComTargetTypeNaoEspecificado() throws Exception {
        query = Introspector.createQuery("FROM br.com.bit.ideias.reflection.test.artefacts.ClasseDominio WHERE name like '%tributoPrivadoString'");
        final List<Member> result = query.list();
        Assert.assertEquals(3, result.size());
    }

    @Test
    public void testRestrictionWithTargetConstructorShouldReturnTheConstructorsOfTheClass() throws Exception {
        query = Introspector.createQuery("FROM br.com.bit.ideias.reflection.test.artefacts.ClasseDominio WHERE target = 'constructor'");
        final List<Member> result = query.list();
        Assert.assertEquals(3, result.size());
    }

    @Test(expected = SyntaxException.class)
    public void testAnnotationShouldThrowExceptionIfNotAnnotation() throws Exception {
        Introspector.createQuery("FROM br.com.bit.ideias.reflection.test.artefacts.ClasseDominio WHERE annotation eq 'br.com.bit.ideias.reflection.test.artefacts.ClasseDominio'").list();
    }

    @Test(expected = SyntaxException.class)
    public void testAnnotationShouldThrowExceptionIfNotClass() throws Exception {
        Introspector.createQuery("FROM br.com.bit.ideias.reflection.test.artefacts.ClasseDominio WHERE annotation eq 'ImNotAClass'").list();
    }

    @Test
    public void testRestrictionEqComTargetTypeNaoEspecificadoUsingForClass() throws Exception {
        query = Introspector.forClass(ClasseDominio.class).query("name like '%tributoPrivadoString'");
        final List<Member> result = query.list();
        Assert.assertEquals(3, result.size());
    }

    @Test(expected = SyntaxException.class)
    public void testNullQueryShouldThrowSyntaxException() throws Exception {
        Introspector.createQuery(null).list();
    }

    @Test(expected = SyntaxException.class)
    public void testEmptyQueryShouldThrowSyntaxException() throws Exception {
        Introspector.createQuery("  ").list();
    }

    @Test
    public void testFromClassShouldReturnMembers() throws Exception {
        List<Member> members = Introspector.createQuery("from br.com.bit.ideias.reflection.test.artefacts.ClasseDominio").list();
        Assert.assertEquals(39, members.size());
    }

    @Test(expected = SyntaxException.class)
    public void testFromClassShouldThrowExceptionIfNotParseableToClass() throws Exception {
        Introspector.createQuery("from br_com.bit.ideias.reflection.test.artefacts.").list();
    }

    @Test(expected = SyntaxException.class)
    public void testQueryShouldThrowExceptionIfNotOrdered() throws Exception {
        Introspector.createQuery("br.com.bit.ideias.reflection.test.artefacts.ClasseDominio from").list();
    }

    @Test
    public void testRestrictionEqDirectCriterion() throws Exception {
        final Field field = ClasseDominio.class.getDeclaredField("atributoPrivadoInt");
        final Query localCriterion = query = Introspector.createQuery(QUERY_DOMINIO + "name eq 'atributoPrivadoInt'");
        final List<Field> result = localCriterion.list();
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(field, result.get(0));
    }

    @Test
    public void testRestrictionEqWithParenthesisShouldReturnNoResults() throws Exception {
        final Query localCriterion = query = Introspector.createQuery(QUERY_DOMINIO + "name eq '(atributoPrivadoInt)'");
        final List<Field> result = localCriterion.list();
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testRestrictionEq() throws Exception {
        final Field field = ClasseDominio.class.getDeclaredField("atributoPrivadoInt");
        query = Introspector.createQuery(QUERY_DOMINIO + "name = 'atributoPrivadoInt'");
        final List<Member> result = query.list();
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(field, result.get(0));
    }

    @Test
    public void shouldReturnEitherWhenUsingDisjunction() throws Exception {
        final Field field = ClasseDominio.class.getDeclaredField("atributoPrivadoInt");
        query = Introspector.createQuery(QUERY_DOMINIO + "name eq 'atributoPrivadoInt' and (name eq 'yzxabc' or name eq 'atributoPrivadoInt')");
        final List<Member> result = query.list();
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(field, result.get(0));
    }

    @Test
    public void testRestrictionEqEncadeado() throws Exception {
        query = Introspector.createQuery(QUERY_DOMINIO + "name eq 'atributoPrivadoInt' and name eq 'isAlive'");
        final List<Member> result = query.list();
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testRestrictionNe() throws Exception {
        query = Introspector.createQuery(QUERY_DOMINIO + "name != 'atributoPrivadoInt' and name ne 'constante'");
        final List<Member> result = query.list();
        Assert.assertEquals(7, result.size());
    }

    @Test
    public void testRestrictionNeEncadead() throws Exception {
        query = Introspector.createQuery(QUERY_DOMINIO + "name ne 'atributoPrivadoInt' and name ne 'isAlive' and name ne 'Privative' and name ne 'constante'");
        final List<Member> result = query.list();
        Assert.assertEquals(5, result.size());
    }

    @Test
    public void testRestrictionLike() throws Exception {
        query = Introspector.createQuery(QUERY_DOMINIO + "name like '%atributo%'");
        final List<Member> result = query.list();
        Assert.assertEquals(4, result.size());
    }

    @Test
    public void testRestrictionLikeEq() throws Exception {
        final Field field = ClasseDominio.class.getDeclaredField("atributoPrivadoInt");
        query = Introspector.createQuery(QUERY_DOMINIO + "name like 'atributoPrivadoInt'");
        final List<Member> result = query.list();
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(field, result.get(0));
    }

    @Test
    public void testRestrictionLikeEncadeado() throws Exception {
        query = Introspector.createQuery(QUERY_DOMINIO + "name like '%atributo%' and name ne 'atributoPrivadoInt'");
        final List<Member> result = query.list();
        Assert.assertEquals(3, result.size());
    }

    @Test
    public void testRestrictionLikeComLikeTypeStart() throws Exception {
        query = Introspector.createQuery(QUERY_DOMINIO + "name like 'atributo%'");
        final List<Member> result = query.list();
        Assert.assertEquals(4, result.size());
    }

    @Test
    public void testRestrictionLikeComLikeTypeEnd() throws Exception {
        query = Introspector.createQuery(QUERY_DOMINIO + "name like '%ive'");
        final List<Member> result = query.list();
        Assert.assertEquals(2, result.size());
    }

    @Test
    public void testRestrictionLikeComLikeTypeAnywhere() throws Exception {
        query = Introspector.createQuery(QUERY_DOMINIO + "name like '%Priva%'");
        final List<Member> result = query.list();
        Assert.assertEquals(5, result.size());
    }

    @Test
    public void testRestrictionRegex() throws Exception {
        query = Introspector.createQuery(QUERY_DOMINIO + "name like '/comeca[P|p]riva/'");
        final List<Member> result = query.list();
        Assert.assertEquals(2, result.size());
    }

    @Test
    public void testRestrictionRegexWithUnterminatedRegexShouldThrowException() throws Exception {
        assertThrowsSyntaxException("Regex should start and end with /", QUERY_DOMINIO + "name like '/comeca[P|p]riva'");
        assertThrowsSyntaxException("Regex should start and end with /", QUERY_DOMINIO + "name like 'comeca[P|p]riva/'");
    }

    @Test
    public void testRestrictionIn() throws Exception {
        query = Introspector.createQuery(QUERY_DOMINIO + "name in ('atributoPrivadoInt','comecaPriva')");
        final List<Member> result = query.list();
        Assert.assertEquals(2, result.size());
    }

    @Test
    public void testRestrictionAnnotatedWith() throws Exception {
        query = Introspector.createQuery(QUERY_DOMINIO + "annotation eq 'br.com.bit.ideias.reflection.test.artefacts.MyAnnotation'");
        final List<Member> result = query.list();
        Assert.assertEquals(3, result.size());
    }

    @Test
    public void testRestrictionAnnotatedNe() throws Exception {
        query = Introspector.createQuery(QUERY_DOMINIO + "annotation ne 'br.com.bit.ideias.reflection.test.artefacts.MyAnnotation'");
        final List<Member> result = query.list();
        Assert.assertEquals(6, result.size());
    }

    @Test
    public void testRestrictionAnnotatedWithComDoisAdd() throws Exception {
        query = Introspector.createQuery(QUERY_DOMINIO + "annotation eq 'br.com.bit.ideias.reflection.test.artefacts.MyAnnotation' and annotation eq 'br.com.bit.ideias.reflection.test.artefacts.MyAnnotation2'");
        final List<Member> result = query.list();
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testRestrictionShowOnlyPublicTrue() throws Exception {
        query = Introspector.createQuery(QUERY_DOMINIO + "name like '/comeca[P|p]riva/' and modifier eq 'public'");
        final List<Member> result = query.list();
        Assert.assertEquals(1, result.size());
    }

    @Test(expected = TooManyResultException.class)
    public void testRestrictionUniqueShouldThrowAnExceptionIfThereAreMoreThanOneResult() throws Exception {
        query = Introspector.createQuery(QUERY_DOMINIO + "annotation eq 'br.com.bit.ideias.reflection.test.artefacts.MyAnnotation'");
        query.uniqueResult();
    }

    @Test(expected = NoResultException.class)
    public void testRestrictionUniqueShouldThrowAnExceptionIfThereAreNoResults() throws Exception {
        query = Introspector.createQuery(QUERY_DOMINIO + "name eq 'xyzu'");
        query.uniqueResult();
    }

    @Test
    public void testRestrictionUniqueShouldReturnOnlyOneMember() throws Exception {
        query = Introspector.createQuery(QUERY_DOMINIO + "name like '/comeca[P|p]riva/' and modifier eq 'public'");
        final Field fieldActual = query.uniqueResult();
        final Field fieldExpected = ClasseDominio.class.getDeclaredField("comecaPriva");
        Assert.assertEquals(fieldExpected, fieldActual);
    }

    @Test
    public void testRestrictionShowOnlyPublicFalse() throws Exception {
        query = Introspector.createQuery(QUERY_DOMINIO + "name like '/comeca[P|p]riva/' and modifier eq 'PRIVATE' or modifier eq 'PROTECTED'");
        final List<Member> result = query.list();
        Assert.assertEquals(1, result.size());
    }

    @Test
    public void testRestrictionShowOnlyPublicTrueComAtributosNaClassePai() throws Exception {
        query = Introspector.createQuery(QUERY_FILHA + "name like '%atributo%' and modifier eq 'PRIVATE' or modifier eq 'PROTECTED'");
        final List<Member> result = query.list();
        Assert.assertEquals(5, result.size());
    }

    @Test
    public void testRestrictionEqComPropriedadeNaClassePai() throws Exception {
        query = Introspector.createQuery(QUERY_FILHA + "name like '/comeca[P|p]riva/'");
        final List<Member> result = query.list();
        Assert.assertEquals(2, result.size());
    }

    @Test
    public void testRestrictionAnnotatedWithNaClassePai() throws Exception {
        query = Introspector.createQuery(QUERY_DOMINIO + "annotation eq 'br.com.bit.ideias.reflection.test.artefacts.MyAnnotation'");
        final List<Member> result = query.list();
        Assert.assertEquals(3, result.size());
    }

    @Test
    public void testTypeEq() {
        query = Introspector.createQuery(QUERY_DOMINIO + "fieldclass eq 'java.lang.String'");
        final List<Member> result = query.list();
        Assert.assertEquals(2, result.size());
    }

    @Test
    public void testMethodReturnClass() {
        query = Introspector.createQuery(QUERY_DOMINIO + "methodreturnclass eq 'java.lang.Integer'");
        final List<Member> result = query.list();
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testWithParams() {
        query = Introspector.createQuery(QUERY_DOMINIO + "method with ('java.lang.String')");
        final List<Member> result = query.list();
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testWithParamsComMaisDeUmParametro() {
        query = Introspector.createQuery(QUERY_DOMINIO + "method with ('java.lang.String', 'java.lang.Integer', 'java.lang.Boolean')");
        final List<Member> result = query.list();
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testCriteriaProcuraPorFieldsConstantes() {
        query = Introspector.createQuery(QUERY_DOMINIO + "modifier eq 'final' and modifier eq 'static'");
        final List<Member> result = query.list();
        Assert.assertEquals(1, result.size());
    }

    @Test
    public void testCriteriaProcuraPorFieldsEstaticos() {
        query = Introspector.createQuery(QUERY_DOMINIO + "modifier eq 'static'");
        final List<Member> result = query.list();
        Assert.assertEquals(1, result.size());
    }

    @Test
    public void testCriteriaProcuraPorFieldsSynchronized() {
        query = Introspector.createQuery(QUERY_DOMINIO + "modifier eq 'SYNCHRONIZED'");
        final List<Field> result = query.list();
        Assert.assertEquals(0, result.size());
    }

    private void assertThrowsSyntaxException(String message, String rql) {
        try {
            Introspector.createQuery("FROM br.com.bit.ideias.reflection.test.artefacts.ClasseDominio WHERE " + rql).list();
            fail(message);
        } catch (SyntaxException e) {
        }
    }
}

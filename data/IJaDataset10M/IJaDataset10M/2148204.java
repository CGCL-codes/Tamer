package org.esfinge.querybuilder.jpa1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.esfinge.querybuilder.exception.InvalidQuerySequenceException;
import org.esfinge.querybuilder.methodparser.ComparisonType;
import org.esfinge.querybuilder.methodparser.OrderingDirection;
import org.esfinge.querybuilder.methodparser.QueryRepresentation;
import org.esfinge.querybuilder.methodparser.QueryVisitor;
import org.junit.Test;

public class TestJPAQLQueryVisitor extends GenericTestJPAQLQueryVisitor {

    @Test
    public void singleEntity() {
        visitor.visitEntity("Person");
        visitor.visitEnd();
        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();
        assertEquals(query, "SELECT o FROM Person o");
    }

    @Test
    public void oneCondition() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitEnd();
        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();
        assertEquals(query, "SELECT o FROM Person o WHERE o.name = :name");
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void conectorBeforeCondition() {
        visitor.visitEntity("Person");
        visitor.visitConector("and");
        visitor.visitEnd();
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void firstConector() {
        visitor.visitConector("and");
        visitor.visitEnd();
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void firstCondition() {
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitEnd();
    }

    @Test
    public void twoConditions() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitConector("and");
        visitor.visitCondition("lastName", ComparisonType.EQUALS);
        visitor.visitEnd();
        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();
        assertEquals(query, "SELECT o FROM Person o WHERE o.name = :name and o.lastName = :lastName");
    }

    @Test
    public void compositeCondition() {
        visitor.visitEntity("Person");
        visitor.visitCondition("address.city", ComparisonType.EQUALS);
        visitor.visitEnd();
        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();
        assertEquals(query, "SELECT o FROM Person o WHERE o.address.city = :city");
    }

    @Test
    public void complexQuery() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitConector("or");
        visitor.visitCondition("lastName", ComparisonType.EQUALS);
        visitor.visitConector("and");
        visitor.visitCondition("address.city", ComparisonType.EQUALS);
        visitor.visitEnd();
        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();
        assertEquals(query, "SELECT o FROM Person o WHERE o.name = :name or o.lastName = :lastName and o.address.city = :city");
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void finishWithConector() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitConector("and");
        visitor.visitEnd();
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void entityAfterStart() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitEntity("Person");
        visitor.visitEnd();
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void twoEntities() {
        visitor.visitEntity("Person");
        visitor.visitEntity("Person");
        visitor.visitEnd();
    }

    @Test
    public void differentConditionTypes() {
        testCondition(ComparisonType.GREATER, "age", "o.age > :age");
        testCondition(ComparisonType.GREATER_OR_EQUALS, "age", "o.age >= :age");
        testCondition(ComparisonType.LESSER, "age", "o.age < :age");
        testCondition(ComparisonType.LESSER_OR_EQUALS, "age", "o.age <= :age");
        testCondition(ComparisonType.NOT_EQUALS, "age", "o.age <> :age");
    }

    @Test
    public void stringConditionTypes() {
        testCondition(ComparisonType.CONTAINS, "name", "o.name LIKE :name");
        testCondition(ComparisonType.STARTS, "name", "o.name LIKE :name");
        testCondition(ComparisonType.ENDS, "name", "o.name LIKE :name");
    }

    public void testCondition(ComparisonType cp, String property, String comparison) {
        QueryVisitor visitor = new JPAQLQueryVisitor();
        visitor.visitEntity("Person");
        visitor.visitCondition(property, cp);
        visitor.visitEnd();
        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();
        assertEquals(query, "SELECT o FROM Person o WHERE " + comparison);
    }

    @Test
    public void fixParameterQuery() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS, "Maria");
        visitor.visitEnd();
        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();
        assertEquals(query, "SELECT o FROM Person o WHERE o.name = :nameEQUALS");
        assertEquals(qr.getFixParameterValue("nameEQUALS"), "Maria");
        assertTrue(qr.getFixParameters().contains("nameEQUALS"));
    }

    @Test
    public void mixedWithfixParameterQuery() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS, "Maria");
        visitor.visitConector("and");
        visitor.visitCondition("age", ComparisonType.GREATER);
        visitor.visitEnd();
        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();
        assertEquals(query, "SELECT o FROM Person o WHERE o.name = :nameEQUALS and o.age > :age");
        assertEquals(qr.getFixParameterValue("nameEQUALS"), "Maria");
        assertTrue(qr.getFixParameters().contains("nameEQUALS"));
    }

    @Test
    public void mixedWithfixParameterQueryFromOtherClass() {
        visitor.visitEntity("Person");
        visitor.visitCondition("address.state", ComparisonType.EQUALS, "SP");
        visitor.visitConector("and");
        visitor.visitCondition("age", ComparisonType.GREATER);
        visitor.visitEnd();
        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();
        assertEquals(query, "SELECT o FROM Person o WHERE o.address.state = :stateEQUALS and o.age > :age");
        assertEquals(qr.getFixParameterValue("stateEQUALS"), "SP");
        assertTrue(qr.getFixParameters().contains("stateEQUALS"));
    }

    @Test
    public void oneOrderBy() {
        visitor.visitEntity("Person");
        visitor.visitOrderBy("age", OrderingDirection.ASC);
        visitor.visitEnd();
        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();
        assertEquals(query, "SELECT o FROM Person o ORDER BY o.age ASC");
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void wrongOrderBy() {
        visitor.visitOrderBy("age", OrderingDirection.ASC);
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void orderByAfterConector() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitConector("and");
        visitor.visitOrderBy("age", OrderingDirection.ASC);
        visitor.visitEnd();
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void entityAfterOrderBy() {
        visitor.visitEntity("Person");
        visitor.visitOrderBy("age", OrderingDirection.ASC);
        visitor.visitEntity("Person");
        visitor.visitEnd();
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void conditionAfterOrderBy() {
        visitor.visitEntity("Person");
        visitor.visitOrderBy("age", OrderingDirection.ASC);
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitEnd();
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void conectorAfterOrderBy() {
        visitor.visitEntity("Person");
        visitor.visitOrderBy("age", OrderingDirection.ASC);
        visitor.visitConector("and");
        visitor.visitEnd();
    }

    @Test
    public void twoOrderBy() {
        visitor.visitEntity("Person");
        visitor.visitOrderBy("age", OrderingDirection.ASC);
        visitor.visitOrderBy("name", OrderingDirection.DESC);
        visitor.visitEnd();
        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();
        assertEquals(query, "SELECT o FROM Person o ORDER BY o.age ASC, o.name DESC");
    }

    @Test
    public void orderByWithConditions() {
        visitor.visitEntity("Person");
        visitor.visitCondition("address.state", ComparisonType.EQUALS, "SP");
        visitor.visitConector("and");
        visitor.visitCondition("age", ComparisonType.GREATER);
        visitor.visitOrderBy("age", OrderingDirection.ASC);
        visitor.visitOrderBy("name", OrderingDirection.DESC);
        visitor.visitEnd();
        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();
        assertEquals(query, "SELECT o FROM Person o WHERE o.address.state = :stateEQUALS and o.age > :age ORDER BY o.age ASC, o.name DESC");
    }
}

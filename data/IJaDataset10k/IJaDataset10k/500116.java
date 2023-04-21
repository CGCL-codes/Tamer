package com.sun.ebxml.registry.query.filter;

import java.sql.*;
import com.sun.ebxml.registry.*;
import org.oasis.ebxml.registry.bindings.query.*;
import org.oasis.ebxml.registry.bindings.query.types.*;
import org.oasis.ebxml.registry.bindings.rs.*;

/**
 * Class Declaration for FilterProcessor
 * @see
 * @author Nikola Stojanovic
 */
public class FilterProcessor implements SQLConverter {

    private String selectColumn = null;

    private boolean isNativeFilter = false;

    private boolean isReverseSelectNeeded = false;

    private ClauseHandler clauseHandler = new ClauseHandler();

    private ClauseType sqlClause = new ClauseType();

    public ClauseType getNewClause(FilterType filter) throws RegistryException {
        sqlClause.clause = convertToSQL(filter);
        sqlClause.isSubSelectNeeded = !isNativeFilter;
        sqlClause.isReverseSelectNeeded = isReverseSelectNeeded;
        return sqlClause;
    }

    public void setSelectColumn(String selColumn) {
        selectColumn = selColumn;
    }

    public boolean isReverseSelectNeeded() {
        return isReverseSelectNeeded;
    }

    public String convertToSQL(Object obj) throws RegistryException {
        if (!(obj instanceof org.oasis.ebxml.registry.bindings.query.FilterType)) {
            throw new RegistryException("Unexpected object " + obj + ". Was expecting org.oasis.ebxml.registry.bindings.query.FilterType.");
        }
        FilterType filter = (FilterType) obj;
        String sqlQuery = null;
        String whereClause = null;
        if (!isNativeFilter) {
            sqlQuery = "SELECT " + selectColumn + " FROM " + getTableName(filter);
            whereClause = "WHERE ";
        } else {
            whereClause = "";
        }
        Clause clause = filter.getClause();
        SimpleClause simpleClause = clause.getSimpleClause();
        CompoundClause compoundClause = clause.getCompoundClause();
        if (simpleClause != null) {
            whereClause += convertSimpleClause(simpleClause);
        } else if (compoundClause != null) {
            whereClause += convertCompoundClause(compoundClause);
        } else {
            throw new RegistryException("Unexpected Clause " + obj + ". Was expecting org.oasis.ebxml.registry.bindings.query.SimpleClause or org.oasis.ebxml.registry.bindings.query.CompoundClause.");
        }
        sqlClause.isReverseSelectNeeded = isReverseSelectNeeded;
        if (!isNativeFilter) {
            return sqlQuery + " " + whereClause;
        } else {
            return whereClause;
        }
    }

    public String addNativeWhereClause(String whereClause, FilterType nativeFilter) throws RegistryException {
        setNativeFilter(true);
        sqlClause.clause = convertToSQL(nativeFilter);
        return clauseHandler.addWhereClause(whereClause, sqlClause);
    }

    public String addForeignWhereClause(String whereClause, FilterType foreignFilter) throws RegistryException {
        setNativeFilter(false);
        sqlClause.clause = convertToSQL(foreignFilter);
        return clauseHandler.addWhereClause(whereClause, sqlClause);
    }

    private void setNativeFilter(boolean isNatFilter) {
        isNativeFilter = isNatFilter;
        if (isNativeFilter) {
            sqlClause.isSubSelectNeeded = false;
            setSelectColumn("");
        } else {
            sqlClause.isSubSelectNeeded = true;
        }
    }

    private String getTableName(FilterType filter) throws RegistryException {
        if (filter.getClass().getName().equals("org.oasis.ebxml.registry.bindings.query.RegistryObjectFilter")) {
            return "RegistryObject";
        } else if (filter.getClass().getName().equals("org.oasis.ebxml.registry.bindings.query.RegistryEntryFilter")) {
            return "RegistryEntry";
        } else if (filter.getClass().getName().equals("org.oasis.ebxml.registry.bindings.query.AssociationFilter")) {
            return "Association";
        } else if (filter.getClass().getName().equals("org.oasis.ebxml.registry.bindings.query.AuditableEventFilter")) {
            return "AuditableEvent";
        } else if (filter.getClass().getName().equals("org.oasis.ebxml.registry.bindings.query.ClassificationFilter")) {
            return "Classification";
        } else if (filter.getClass().getName().equals("org.oasis.ebxml.registry.bindings.query.ClassificationNodeFilter")) {
            return "ClassificationNode";
        } else if (filter.getClass().getName().equals("org.oasis.ebxml.registry.bindings.query.ClassificationSchemeFilter")) {
            return "ClassificationScheme";
        } else if (filter.getClass().getName().equals("org.oasis.ebxml.registry.bindings.query.EmailAddress")) {
            return "EmailAddress";
        } else if (filter.getClass().getName().equals("org.oasis.ebxml.registry.bindings.query.ExternalIdentifierFilter")) {
            return "ExternalIdentifier";
        } else if (filter.getClass().getName().equals("org.oasis.ebxml.registry.bindings.query.ExternalLinkFilter")) {
            return "ExternalLink";
        } else if (filter.getClass().getName().equals("org.oasis.ebxml.registry.bindings.query.ExtrinsicObjectFilter")) {
            return "ExtrinsicObject";
        } else if (filter.getClass().getName().equals("org.oasis.ebxml.registry.bindings.query.OrganizationFilter")) {
            return "Organization";
        } else if (filter.getClass().getName().equals("org.oasis.ebxml.registry.bindings.query.RegistryPackageFilter")) {
            return "RegistryPackage";
        } else if (filter.getClass().getName().equals("org.oasis.ebxml.registry.bindings.query.ServiceFilter")) {
            return "Service";
        } else if (filter.getClass().getName().equals("org.oasis.ebxml.registry.bindings.query.ServiceBindingFilter")) {
            return "ServiceBinding";
        } else if (filter.getClass().getName().equals("org.oasis.ebxml.registry.bindings.query.SpecificationLinkFilter")) {
            return "SpecificationLink";
        } else if (filter.getClass().getName().equals("org.oasis.ebxml.registry.bindings.query.UserFilter")) {
            return "User";
        } else if (filter.getClass().getName().equals("org.oasis.ebxml.registry.bindings.query.SlotFilter")) {
            return "Slot";
        } else if (filter.getClass().getName().equals("org.oasis.ebxml.registry.bindings.query.SlotValueFilter")) {
            return "SlotValue";
        } else if (filter.getClass().getName().equals("org.oasis.ebxml.registry.bindings.query.PostalAddressFilter")) {
            return "PostalAddress";
        } else if (filter.getClass().getName().equals("org.oasis.ebxml.registry.bindings.query.TelephoneNumberFilter")) {
            return "TelephoneNumber";
        } else if (filter.getClass().getName().equals("org.oasis.ebxml.registry.bindings.query.LocalizedStringFilter")) {
            return "";
        } else {
            throw new RegistryException("Unexpected filter" + filter);
        }
    }

    private String convertSimpleClause(SimpleClause simpleClause) throws RegistryException {
        StringClause stringClause = simpleClause.getStringClause();
        BooleanClause booleanClause = simpleClause.getBooleanClause();
        RationalClause rationalClause = simpleClause.getRationalClause();
        String predicate = null;
        String whereClause = "";
        if (stringClause != null) {
            StringPredicateType stringPredicate = stringClause.getStringPredicate();
            predicate = stringPredicate.toString();
            whereClause += simpleClause.getLeftArgument() + " " + buildStringSQLPredicate(predicate, stringClause.getContent());
        } else if (booleanClause != null) {
            Boolean boleanPredicate = new Boolean(booleanClause.getBooleanPredicate());
            whereClause += simpleClause.getLeftArgument() + " = " + boleanPredicate.toString();
        } else if (rationalClause != null) {
            String rightArgument = null;
            LogicalPredicateType logicalPredicate = rationalClause.getLogicalPredicate();
            predicate = logicalPredicate.toString();
            if (rationalClause.hasIntClause()) {
                Integer number = new Integer(rationalClause.getIntClause());
                rightArgument = number.toString();
            } else if (rationalClause.hasFloatClause()) {
                Float number = new Float(rationalClause.getFloatClause());
                rightArgument = number.toString();
            } else {
                Timestamp dateTime = new Timestamp(((rationalClause.getDateTimeClause()).getTime()));
                rightArgument = dateTime.toString();
            }
            whereClause += simpleClause.getLeftArgument() + " " + buildLogicalSQLPredicate(predicate, rightArgument);
        }
        return whereClause;
    }

    private String convertCompoundClause(CompoundClause compoundClause) throws RegistryException {
        String whereClause = "";
        Clause subClause[] = compoundClause.getClause();
        ConnectivePredicateType predicate = compoundClause.getConnectivePredicate();
        String connectivePredicate = predicate.toString();
        if (subClause.length != 2) {
            throw new RegistryException("Invalid Compound Clause: " + compoundClause);
        } else {
            for (int i = 0; i < subClause.length; i++) {
                if (i == 0) {
                    if (subClause[i].getSimpleClause() != null) {
                        whereClause = convertSimpleClause(subClause[i].getSimpleClause());
                    } else {
                        whereClause = convertCompoundClause(subClause[i].getCompoundClause());
                    }
                } else {
                    if (subClause[i].getSimpleClause() != null) {
                        whereClause = clauseHandler.buildConnectiveSQL(whereClause, connectivePredicate, convertSimpleClause(subClause[i].getSimpleClause()));
                    } else {
                        whereClause = clauseHandler.buildConnectiveSQL(whereClause, connectivePredicate, convertCompoundClause(subClause[i].getCompoundClause()));
                    }
                }
            }
        }
        return whereClause;
    }

    private String buildStringSQLPredicate(String stringPredicate, String rightArgument) throws RegistryException {
        isReverseSelectNeeded = false;
        if (stringPredicate.equals("Contains")) {
            return "LIKE '%" + rightArgument + "%'";
        } else if (stringPredicate.equals("-Contains")) {
            isReverseSelectNeeded = true;
            return "NOT LIKE '%" + rightArgument + "%'";
        } else if (stringPredicate.equals("StartsWith")) {
            return "LIKE '" + rightArgument + "%'";
        } else if (stringPredicate.equals("-StartsWith")) {
            isReverseSelectNeeded = true;
            return "NOT LIKE '" + rightArgument + "%'";
        } else if (stringPredicate.equals("Equal")) {
            return "= '" + rightArgument + "'";
        } else if (stringPredicate.equals("-Equal")) {
            isReverseSelectNeeded = true;
            return "<> '" + rightArgument + "'";
        } else if (stringPredicate.equals("EndsWith")) {
            return "LIKE '%" + rightArgument + "'";
        } else if (stringPredicate.equals("-EndsWith")) {
            isReverseSelectNeeded = true;
            return "NOT LIKE '%" + rightArgument + "'";
        } else {
            throw new RegistryException("Invalid string predicate: " + stringPredicate);
        }
    }

    private String buildLogicalSQLPredicate(String logicalPredicate, String rightArgument) throws RegistryException {
        isReverseSelectNeeded = false;
        if (logicalPredicate.equals("EQ")) {
            return "= " + "\'" + rightArgument + "\'";
        } else if (logicalPredicate.equals("GE")) {
            return ">= " + "\'" + rightArgument + "\'";
        } else if (logicalPredicate.equals("GT")) {
            return ">" + "\'" + rightArgument + "\'";
        } else if (logicalPredicate.equals("LE")) {
            return "<=" + "\'" + rightArgument + "\'";
        } else if (logicalPredicate.equals("LT")) {
            return "<" + "\'" + rightArgument + "\'";
        } else if (logicalPredicate.equals("NE")) {
            isReverseSelectNeeded = true;
            return "<>" + "\'" + rightArgument + "\'";
        } else {
            throw new RegistryException("Invalid logical predicate: " + logicalPredicate);
        }
    }
}

package upm.fi.oeg.sparql.lqp;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import java.util.Map.Entry;
import org.antlr.runtime.tree.CommonTree;
import org.apache.log4j.Logger;
import uk.org.ogsadai.context.OGSADAIContext;
import uk.org.ogsadai.converters.databaseschema.TableMetaData;
import uk.org.ogsadai.converters.databaseschema.TableMetaDataImpl;
import uk.org.ogsadai.dqp.common.DataDictionary;
import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.dqp.common.EvaluationNode;
import uk.org.ogsadai.dqp.common.PhysicalSchema;
import uk.org.ogsadai.dqp.common.TableSchema;
import uk.org.ogsadai.dqp.common.simple.SimplePhysicalSchema;
import uk.org.ogsadai.dqp.common.simple.SimpleTableSchema;
import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeImpl;
import uk.org.ogsadai.dqp.lqp.CommonPredicate;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.Predicate;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.exceptions.TableNotFoundException;
import uk.org.ogsadai.dqp.lqp.operators.ApplyOperator;
import uk.org.ogsadai.dqp.lqp.operators.InnerThetaJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.LeftOuterJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.ProductOperator;
import uk.org.ogsadai.dqp.lqp.operators.ProjectOperator;
import uk.org.ogsadai.dqp.lqp.operators.SelectOperator;
import uk.org.ogsadai.dqp.lqp.udf.FunctionRepository;
import uk.org.ogsadai.dqp.lqp.udf.repository.NoSuchFunctionException;
import uk.org.ogsadai.dqp.presentation.common.SimpleDataNode;
import uk.org.ogsadai.dqp.presentation.common.SimpleEvaluationNode;
import uk.org.ogsadai.expression.ArithmeticExpressionOperand;
import uk.org.ogsadai.expression.EqualExpression;
import uk.org.ogsadai.expression.Expression;
import uk.org.ogsadai.expression.ExpressionFactory;
import uk.org.ogsadai.expression.Operand;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.expression.arithmetic.Constant;
import uk.org.ogsadai.expression.arithmetic.ExpressionException;
import uk.org.ogsadai.expression.arithmetic.TableColumn;
import uk.org.ogsadai.parser.SQLParserException;
import uk.org.ogsadai.parser.sql92query.SQLQueryParser;
import uk.org.ogsadai.tuple.TupleTypes;
import uk.org.ogsadai.tuple.converters.StringConversionException;
import upm.fi.oeg.sparql.lqp.federation.SimpleRDFDataDictionary;
import upm.fi.oeg.sparql.lqp.operators.RDFServiceScanOperator;
import upm.fi.oeg.sparql.lqp.operators.RDFVarServiceScanOperator;
import upm.fi.oeg.sparql.lqp.udf.FunctionFilter;
import upm.fi.oeg.sparql.lqp.udf.FunctionRegex;
import upm.fi.oeg.sparql.parser.impl.SparqlParser;

/**
 * This class builds a Graph which contains the relations in a group graph
 * pattern
 * 
 * @author Carlos Buil Aranda
 * @email cbuil@fi.upm.es
 * @institution Unversidad Politecnica de Madrid
 * 
 */
public class BuildServiceGraph {

    /** Logger. */
    private static final Logger LOG = Logger.getLogger(BuildServiceGraph.class);

    /**
     * default constructor
     */
    BuildServiceGraph() {
    }

    /**
     * @param dataDictionary
     * @param group
     * @param selectVars
     * @param prefixMap
     * @return an operator representing the LQP in a GGP (Group Graph Pattern)
     * @throws LQPException
     * @throws ExpressionException
     * @throws SQLParserException
     */
    public Operator buildGroupGraphPattern(DataDictionary dataDictionary, CommonTree group, ArrayList<Attribute> selectVars, Map<String, String> prefixMap) throws LQPException, ExpressionException, SQLParserException {
        Operator queryRoot = null;
        List<CommonTree> servicesList = new ArrayList<CommonTree>();
        List<CommonTree> triplesList = new ArrayList<CommonTree>();
        SparqlDQPUtil.getServices(group, servicesList, triplesList);
        List<Operator> serviceOperatorList = null;
        serviceOperatorList = new ArrayList<Operator>();
        serviceOperatorList = generateServices(servicesList, triplesList, dataDictionary, selectVars, prefixMap);
        queryRoot = generateServiceRoot(serviceOperatorList, group, dataDictionary, selectVars, prefixMap);
        return queryRoot;
    }

    /**
     * Generates the query plan
     * 
     * @param serviceOperatorList
     * @param group
     * @param dataDictionary
     * @param selectVars
     * @param prefixMap
     * @return a service scan operator
     * @throws ExpressionException
     * @throws SQLParserException
     * @throws LQPException
     */
    private Operator generateServiceRoot(List<Operator> serviceOperatorList, CommonTree group, DataDictionary dataDictionary, ArrayList<Attribute> selectVars, Map<String, String> prefixMap) throws ExpressionException, SQLParserException, LQPException {
        List<Node> nodes = new ArrayList<Node>();
        HashMap<Edge, Set<Node>> edges = new HashMap<Edge, Set<Node>>();
        int i = 0;
        for (Operator operator : serviceOperatorList) {
            List<Attribute> attributes = operator.getHeading().getAttributes();
            Node node = new Node();
            node.attributes = attributes;
            node.operator = operator;
            node.weight = 1000 - i;
            nodes.add(node);
            addServiceEdges(node, nodes, edges, "INNER_JOIN", 1000 - i);
            i++;
        }
        nodes = createJoins(nodes, edges);
        nodes = generateOptionals(nodes, group, dataDictionary, selectVars, prefixMap);
        while (nodes.size() > 1) {
            ProductOperator product = new ProductOperator();
            Node newNodeProduct = new Node();
            Node newNodeProduct1 = new Node();
            Node newNodeProduct2 = new Node();
            newNodeProduct1 = nodes.get(0);
            newNodeProduct2 = nodes.get(1);
            product.setChild(0, newNodeProduct1.operator);
            product.setChild(1, newNodeProduct2.operator);
            product.update();
            newNodeProduct.operator = product;
            newNodeProduct.attributes = product.getHeading().getAttributes();
            nodes.add(newNodeProduct);
            nodes.remove(newNodeProduct1);
            nodes.remove(newNodeProduct2);
        }
        Operator queryRoot = null;
        if (nodes.size() > 0) {
            queryRoot = nodes.get(0).operator;
        }
        return queryRoot;
    }

    /**
     * This method receives as input an AST with a filter and creates a SELECT
     * operator which contains and applies the filter expression
     * 
     * @param dataDictionary
     * @param ast
     * @return list of filter predicates
     */
    public String getFilterString(DataDictionary dataDictionary, CommonTree ast, Map<String, String> prefixMap) {
        String predicate = "";
        if (ast.getChild(0).getChild(0).getType() == SparqlParser.CONSTRAINT) {
            CommonTree filterAST = (CommonTree) ast.getChild(0).getChild(0);
            StringBuffer filterString = new StringBuffer();
            if (filterAST.getChild(0).getType() == SparqlParser.BUILTINCALL) {
                filterString.append(filterAST.getChild(0).getChild(0).getText());
                filterString.append(",");
                filterString.append(filterAST.getChild(0).getChild(0).getChild(0).getText());
                filterString.append(",");
                filterString.append(filterAST.getChild(0).getChild(0).getChild(1).getChild(0).getText());
            } else if (filterAST.getChild(0).getType() == SparqlParser.RELATIONALEXPRESSION) {
                filterString.append(filterAST.getChild(0).getChild(0).getChild(0).getText());
                filterString.append(filterAST.getChild(0).getChild(1).getText());
                filterString.append(filterAST.getChild(0).getChild(2).getChild(0).getText());
            } else {
                filterString.append(filterAST.getChild(0).getText());
                filterString.append(filterAST.getChild(1).getText());
                filterString.append(filterAST.getChild(2).getChild(0).getText());
            }
            predicate = filterString.toString();
        }
        return predicate;
    }

    /**
     * Creates Join operators from the query plan graph
     * 
     * @param nodes
     * @param edges
     * 
     * @return a single node once all the operators have been joined
     * @throws LQPException
     * @throws SQLParserException
     * @throws ExpressionException
     */
    private List<Node> createJoins(List<Node> nodes, Map<Edge, Set<Node>> edges) throws LQPException, ExpressionException, SQLParserException {
        Set<Node> connectedNodes = null;
        int i = 0;
        while (!edges.isEmpty()) {
            Edge edge = edges.keySet().iterator().next();
            List<Attribute> entry = edge.attributes;
            connectedNodes = edges.get(edge);
            InnerThetaJoinOperator innerThetaJoinOperator = null;
            Operator newOperator = innerThetaJoinOperator;
            List<String> predicateList = new ArrayList<String>();
            predicateList = SparqlDQPUtil.getPredicate(entry);
            Predicate p = new CommonPredicate(ExpressionFactory.buildExpression(SQLQueryParser.getInstance().parseSQLForCondition(predicateList.get(0)), null));
            innerThetaJoinOperator = new InnerThetaJoinOperator(p);
            Operator prOp0 = null;
            Operator prOp1 = null;
            Iterator<Node> iterator = connectedNodes.iterator();
            Node prOp0Node = null;
            Node prOp1Node = null;
            while (iterator.hasNext()) {
                ApplyOperator apply;
                prOp0Node = (Node) iterator.next();
                prOp0 = prOp0Node.operator;
                if (iterator.hasNext()) {
                    prOp1Node = (Node) iterator.next();
                    prOp1 = prOp1Node.operator;
                }
                if (prOp0.getAnnotation("depends") != null) {
                    if (prOp0 instanceof RDFVarServiceScanOperator) {
                        Set<Attribute> servVarList = new HashSet<Attribute>();
                        servVarList.add(((RDFVarServiceScanOperator) prOp0).getServiceVar());
                        apply = new ApplyOperator(innerThetaJoinOperator, servVarList);
                        SparqlDQPUtil.connectBinary(apply, prOp1, prOp0);
                        apply.update();
                        newOperator = apply;
                    }
                } else if (prOp1.getAnnotation("depends") != null) {
                    if (prOp1 instanceof RDFVarServiceScanOperator) {
                        Set<Attribute> servVarList = new HashSet<Attribute>();
                        servVarList.add(((RDFVarServiceScanOperator) prOp1).getServiceVar());
                        apply = new ApplyOperator(innerThetaJoinOperator, servVarList);
                        SparqlDQPUtil.connectBinary(apply, prOp0, prOp1);
                        apply.update();
                        newOperator = apply;
                    }
                } else {
                    SparqlDQPUtil.connectBinary(innerThetaJoinOperator, prOp0, prOp1);
                    innerThetaJoinOperator.update();
                    newOperator = innerThetaJoinOperator;
                }
            }
            Node newNode = new Node();
            newNode.operator = newOperator;
            newNode.attributes = newOperator.getHeading().getAttributes();
            newNode.weight = 1000 + i;
            nodes.removeAll(connectedNodes);
            nodes.add(newNode);
            for (Iterator<Edge> it = edges.keySet().iterator(); it.hasNext(); ) {
                Edge entryEdge = it.next();
                Set<Attribute> entryAttribute = new HashSet<Attribute>();
                entryAttribute.addAll(entryEdge.attributes);
                for (Iterator<Node> it2 = connectedNodes.iterator(); it2.hasNext(); ) {
                    Node n = it2.next();
                    if (edges.get(entryEdge).contains(n)) {
                        it.remove();
                        break;
                    }
                }
            }
            addServiceEdges(newNode, nodes, edges, "INNER_JOIN", 1000);
        }
        return nodes;
    }

    /**
     * Add all possible edges from the specify node to the existing nodes.
     * 
     * @param node
     * @param nodes
     * @param edges
     * @param join_type
     * @param weight
     */
    private void addServiceEdges(Node node, List<Node> nodes, Map<Edge, Set<Node>> edges, String join_type, int weight) {
        int i = 0;
        for (Node existingNode : nodes) {
            if (existingNode != node) {
                Set<Attribute> commonVariables = SparqlDQPUtil.getCommonVariables(existingNode.attributes, node.attributes);
                if (!commonVariables.isEmpty()) {
                    Set<Node> edgesWithSameLabel = edges.get(commonVariables);
                    if (edgesWithSameLabel == null) {
                        edgesWithSameLabel = new HashSet<Node>();
                        Edge edge = new Edge();
                        List<Attribute> attr = new ArrayList<Attribute>();
                        attr.addAll(commonVariables);
                        edge.attributes = attr;
                        edge.joinOperator = join_type;
                        edge.weight = weight - i;
                        edges.put(edge, edgesWithSameLabel);
                    }
                    edgesWithSameLabel.add(node);
                    edgesWithSameLabel.add(existingNode);
                }
                i++;
            }
        }
    }

    /**
     * Reads a SERVICE list from the AST and returns a list of SERVICE scan
     * operators
     * 
     * @param servicesList
     * @param defaultGraphTripleList
     * @param dataDictionary
     * @param selectVars
     * @param prefixMap
     * @return a list of SERVICE scan operators
     * @throws LQPException
     */
    private List<Operator> generateServices(List<CommonTree> servicesList, List<CommonTree> defaultGraphTripleList, DataDictionary dataDictionary, ArrayList<Attribute> selectVars, Map<String, String> prefixMap) throws LQPException {
        List<Operator> serviceOperatorList;
        serviceOperatorList = new ArrayList<Operator>();
        String defaultGraphQuery = SparqlDQPUtil.getQuery(defaultGraphTripleList, prefixMap);
        List<String> triplesVarList = null;
        triplesVarList = new ArrayList<String>();
        for (CommonTree tripleCT : defaultGraphTripleList) {
            SparqlDQPUtil.getServiceVars(tripleCT, triplesVarList);
        }
        if (!triplesVarList.isEmpty()) {
            serviceOperatorList.add(createDefaultServiceScan(defaultGraphQuery, triplesVarList, dataDictionary, prefixMap));
        }
        for (CommonTree ct : servicesList) {
            String query = "";
            List<String> varList = null;
            varList = new ArrayList<String>();
            List<String> varServiceList = null;
            varServiceList = new ArrayList<String>();
            String source = null;
            SparqlDQPUtil.randomId();
            String sourceUUID = SparqlDQPUtil.randomId();
            source = SparqlDQPUtil.getSource(ct.getChild(0));
            List<String> boundList = new ArrayList<String>();
            SparqlDQPUtil.getTerms(ct, boundList);
            SparqlDQPUtil.getServiceVars(ct, varList);
            StringBuffer serviceSelectVars = new StringBuffer();
            for (String var : varList) {
                if (!varServiceList.contains(var)) {
                    varServiceList.add(var);
                    serviceSelectVars.append("?");
                    serviceSelectVars.append(var);
                    serviceSelectVars.append(" ");
                }
            }
            DataNode newDataNode = null;
            newDataNode = getExecutionNode(dataDictionary, source, sourceUUID);
            query = "SELECT " + serviceSelectVars.toString() + " WHERE { " + query + " " + SparqlDQPUtil.getQuery(ct, prefixMap) + " }";
            Operator queryRoot = null;
            RDFServiceScanOperator serviceScan = new RDFServiceScanOperator(query, sourceUUID, varServiceList, boundList, source);
            serviceScan.setDataDictionary(dataDictionary);
            Annotation.addDataNodeAnnotation(serviceScan, newDataNode);
            List<Attribute> attrbs = new ArrayList<Attribute>();
            for (String string : varServiceList) {
                Attribute attr = new AttributeImpl(string, TupleTypes._STRING, sourceUUID);
                attr.addAnnotation("resourceId", newDataNode.getResourceID().toString());
                attrbs.add(attr);
                ProjectOperator project = new ProjectOperator(serviceScan, attrbs);
                project.addAnnotation("originalSource", source);
                project.addAnnotation("safe", "");
                project.update();
                queryRoot = project;
            }
            serviceOperatorList.add(queryRoot);
        }
        return serviceOperatorList;
    }

    /**
     * 
     * I create the SERVICE that queries the default graph. Querying the default
     * graph is treated like querying a remote endpoint
     * 
     * @param defaultGraphQuery
     * @param triplesVarList
     * @param dataDictionary
     * @param prefixMap
     * @return
     * @throws LQPException
     */
    public Operator createDefaultServiceScan(String defaultGraphQuery, List<String> triplesVarList, DataDictionary dataDictionary, Map<String, String> prefixMap) throws LQPException {
        OGSADAIContext globalContext = OGSADAIContext.getInstance();
        String source = (String) globalContext.get("defaultGraph");
        String sourceUUID = SparqlDQPUtil.randomId();
        DataNode newDataNode = getExecutionNode(dataDictionary, source, sourceUUID);
        List<String> boundList = new ArrayList<String>();
        RDFServiceScanOperator serviceScan = new RDFServiceScanOperator(defaultGraphQuery, source.substring(source.lastIndexOf("/") + 1), triplesVarList, boundList, source);
        serviceScan.setDataDictionary(dataDictionary);
        Annotation.addDataNodeAnnotation(serviceScan, newDataNode);
        List<Attribute> attrbs = new ArrayList<Attribute>();
        for (String string : triplesVarList) {
            Attribute attr = new AttributeImpl(string, TupleTypes._STRING, source.substring(source.lastIndexOf("/") + 1));
            attr.addAnnotation("resourceId", newDataNode.getResourceID().toString());
            attrbs.add(attr);
        }
        ProjectOperator project = new ProjectOperator(serviceScan, attrbs);
        project.addAnnotation("originalSource", source);
        project.addAnnotation("safe", "");
        project.update();
        return project;
    }

    /**
     * I get the AST and I generate SERVICE scan operators for each OPTIONAL. I
     * also create the inner joins in the optional if there is more than one
     * SERVICE_SCAN operator. I return a list of nodes, one per OPTIONAL in the
     * query, later they will be joined using LEFT Operators
     * 
     * @param nodes
     * @param ct
     * @param dataDictionary
     * @param selectVars
     * @param prefixMap
     * @return a list of nodes, one per OPTIONAL in the SPARQL query
     * @throws LQPException
     * @throws SQLParserException
     * @throws ExpressionException
     */
    private List<Node> generateOptionals(List<Node> nodes, CommonTree ct, DataDictionary dataDictionary, ArrayList<Attribute> selectVars, Map<String, String> prefixMap) throws ExpressionException, SQLParserException, LQPException {
        List<CommonTree> optionalList;
        optionalList = new ArrayList<CommonTree>();
        SparqlDQPUtil.getOptionals(ct, optionalList);
        List<Node> optionalOperatorList = null;
        optionalOperatorList = new ArrayList<Node>();
        for (CommonTree optional : optionalList) {
            List<Operator> optionalServiceOperatorList = null;
            optionalServiceOperatorList = new ArrayList<Operator>();
            List<CommonTree> innerServicesList = new ArrayList<CommonTree>();
            List<CommonTree> innerTripleList = new ArrayList<CommonTree>();
            SparqlDQPUtil.getServices(optional, innerServicesList, innerTripleList);
            optionalServiceOperatorList.addAll(optionalServiceOperatorList.size(), generateServices(innerServicesList, innerTripleList, dataDictionary, selectVars, prefixMap));
            List<Node> optionalNodes = new ArrayList<Node>();
            HashMap<Edge, Set<Node>> optionalEdges = new HashMap<Edge, Set<Node>>();
            int i = 0;
            for (Operator operator : optionalServiceOperatorList) {
                List<Attribute> attributes = operator.getHeading().getAttributes();
                Node node = new Node();
                node.attributes = attributes;
                node.operator = operator;
                optionalNodes.add(node);
                addServiceEdges(node, optionalNodes, optionalEdges, "INNER_JOIN", 900 - i);
                i++;
            }
            optionalOperatorList.addAll(createJoins(optionalNodes, optionalEdges));
        }
        HashMap<Edge, Set<Node>> optionalEdges = new HashMap<Edge, Set<Node>>();
        int i = 0;
        for (Node optionalNode : optionalOperatorList) {
            optionalNode.weight = 900 - i;
            nodes.add(optionalNode);
            addServiceEdges(optionalNode, nodes, optionalEdges, "LEFT_JOIN", 1000 - i);
            i = i + 10;
        }
        SortedSet<Map.Entry<Edge, Set<Node>>> sortedEdges = new TreeSet<Map.Entry<Edge, Set<Node>>>(new EdgeOrder());
        sortedEdges.addAll(optionalEdges.entrySet());
        while (!sortedEdges.isEmpty()) {
            LOG.debug("creating joins using an ordered map");
            for (Entry<Edge, Set<Node>> entry : sortedEdges) {
                LOG.debug("Attributes: " + entry.getKey().attributes);
                LOG.debug("Nodes: " + entry.getValue());
            }
            LeftOuterJoinOperator leftOuterJoinOperator = null;
            Edge entry = sortedEdges.first().getKey();
            Set<Node> connectedNodes = sortedEdges.first().getValue();
            List<String> predicateList = new ArrayList<String>();
            predicateList = SparqlDQPUtil.getPredicate(entry.attributes);
            Predicate p = new CommonPredicate(ExpressionFactory.buildExpression(SQLQueryParser.getInstance().parseSQLForCondition(predicateList.get(0)), null));
            leftOuterJoinOperator = new LeftOuterJoinOperator(p);
            Operator prOp0 = null;
            Operator prOp1 = null;
            Iterator<Node> iterator = connectedNodes.iterator();
            Node prOp0Node = null;
            Node prOp1Node = null;
            while (iterator.hasNext()) {
                prOp0Node = (Node) iterator.next();
                prOp0 = prOp0Node.operator;
                if (iterator.hasNext()) {
                    prOp1Node = (Node) iterator.next();
                    prOp1 = prOp1Node.operator;
                }
                if (prOp0Node.weight > prOp1Node.weight) {
                    SparqlDQPUtil.connectBinary(leftOuterJoinOperator, prOp0, prOp1);
                } else {
                    SparqlDQPUtil.connectBinary(leftOuterJoinOperator, prOp1, prOp0);
                }
                leftOuterJoinOperator.update();
            }
            Node newNode = new Node();
            newNode.operator = leftOuterJoinOperator;
            newNode.attributes = leftOuterJoinOperator.getHeading().getAttributes();
            newNode.weight = prOp0Node.weight + prOp1Node.weight;
            nodes.removeAll(connectedNodes);
            nodes.add(newNode);
            for (Iterator<Entry<Edge, Set<Node>>> it = sortedEdges.iterator(); it.hasNext(); ) {
                Entry<Edge, Set<Node>> entry2 = it.next();
                Set<Node> sortedNodes = entry2.getValue();
                for (Node node : sortedNodes) {
                    if (connectedNodes.contains(node)) {
                        it.remove();
                        break;
                    }
                }
            }
            HashMap<Edge, Set<Node>> optionalEdges2 = new HashMap<Edge, Set<Node>>();
            for (Iterator<Entry<Edge, Set<Node>>> it = sortedEdges.iterator(); it.hasNext(); ) {
                Entry<Edge, Set<Node>> entryTmp = it.next();
                optionalEdges2.put(entryTmp.getKey(), entryTmp.getValue());
                it.remove();
            }
            addServiceEdges(newNode, nodes, optionalEdges2, "LEFT_JOIN", 1000 + i);
            i = i + 10;
            sortedEdges.addAll(optionalEdges2.entrySet());
        }
        return nodes;
    }

    /**
     * @author Carlos Buil Aranda
     * @email cbuil@fi.upm.es
     * @institution Unversidad Politecnica de Madrid
     * 
     */
    class Node {

        List<Attribute> attributes;

        Operator operator;

        int weight;
    }

    /**
     * @author Carlos Buil Aranda
     * @email cbuil@fi.upm.es
     * @institution Unversidad Politecnica de Madrid
     * 
     */
    class Edge {

        List<Attribute> attributes;

        int weight;

        String joinOperator;
    }

    /**
     * Class to specify the order in which the left joins are performed
     * 
     * @author Carlos Buil Aranda
     * @email cbuil@fi.upm.es
     * @institution Universidad Politecnica de Madrid
     * 
     */
    class EdgeOrder implements Comparator<Map.Entry<Edge, Set<Node>>> {

        public int compare(Entry<Edge, Set<Node>> o1, Entry<Edge, Set<Node>> o2) {
            int sizeDiff = o2.getKey().weight - o1.getKey().weight;
            if (sizeDiff != 0) {
                return sizeDiff;
            } else {
                Set<Node> patterns1 = o1.getValue();
                Set<Node> patterns2 = o2.getValue();
                if (patterns1.size() - patterns2.size() == 0) {
                    return 1;
                } else return patterns2.size() - patterns1.size();
            }
        }
    }

    /**
     * generates a single SERVICE operator from a CommonTree
     * 
     * @param ct
     * @param selectVars
     * @param dataDictionary
     * @param prefixMap
     * 
     * @return The SERVICE operator
     * @throws LQPException
     */
    public Operator generateSingleService(CommonTree ct, List<Attribute> selectVars, DataDictionary dataDictionary, Map<String, String> prefixMap) throws LQPException {
        String query = "";
        List<String> varList = null;
        varList = new ArrayList<String>();
        List<String> varServiceList = null;
        String source = null;
        source = SparqlDQPUtil.getSource(ct.getChild(0));
        varServiceList = new ArrayList<String>();
        List<String> boundList = new ArrayList<String>();
        SparqlDQPUtil.getServiceVars(ct, varList);
        SparqlDQPUtil.getTerms(ct, boundList);
        StringBuffer serviceSelectVars = new StringBuffer();
        for (String var : varList) {
            if (!varServiceList.contains(var)) {
                varServiceList.add(var);
                serviceSelectVars.append("?");
                serviceSelectVars.append(var);
                serviceSelectVars.append(" ");
            }
        }
        query = "SELECT " + serviceSelectVars.toString() + " WHERE { " + query + " " + SparqlDQPUtil.getQuery(ct, prefixMap) + " }";
        Operator queryRoot = null;
        String sourceUUID = SparqlDQPUtil.randomId();
        sourceUUID = UUID.randomUUID().toString().replaceAll("[0-9]+", "").replaceAll("-", "");
        LOG.debug("Generated data respource ID: " + sourceUUID + " for source: " + source);
        RDFServiceScanOperator serviceScan = new RDFServiceScanOperator(query, sourceUUID, varServiceList, boundList, source);
        serviceScan.setDataDictionary(dataDictionary);
        DataNode newDataNode = null;
        newDataNode = getExecutionNode(dataDictionary, source, sourceUUID);
        Annotation.addDataNodeAnnotation(serviceScan, newDataNode);
        List<Attribute> attrbs = new ArrayList<Attribute>();
        for (String string : varServiceList) {
            Attribute attr = new AttributeImpl(string, TupleTypes._STRING, sourceUUID);
            attr.addAnnotation("resourceId", newDataNode.getResourceID().toString());
            attrbs.add(attr);
        }
        ProjectOperator project = new ProjectOperator(serviceScan, attrbs);
        project.addAnnotation("originalSource", source.substring(source.lastIndexOf("/") + 1));
        project.addAnnotation("safe", "");
        project.update();
        queryRoot = project;
        return queryRoot;
    }

    /**
     * gets the node in which the query will be executed
     * 
     * @param dataDictionary
     * @param source
     * @return
     */
    private DataNode getExecutionNode(DataDictionary dataDictionary, String source, String sourceUUID) {
        DataNode newDataNode = null;
        try {
            Set<EvaluationNode> serverList = ((SimpleRDFDataDictionary) dataDictionary).getEvaluationNodes();
            Random rand = new Random(serverList.size());
            int x = rand.nextInt(serverList.size());
            EvaluationNode server = (EvaluationNode) serverList.toArray()[x];
            EvaluationNode newEvaluationNode = new SimpleEvaluationNode(server.getServer().getDefaultBaseServicesURL().toString(), "DataRequestExecutionResource", "DataSourceService", "DataSinkService", false);
            newDataNode = new SimpleDataNode("DataRequestExecutionResource", sourceUUID, newEvaluationNode);
            synchronized (this) {
                String catalog = dataDictionary.getTableSchemas().iterator().next().getSchema().getCatalogName();
                TableMetaData tableMetaData = null;
                PhysicalSchema physicalSchema = new SimplePhysicalSchema("tableMetadataName", "tableMetadataSchema", 10000, 10, 10);
                tableMetaData = new TableMetaDataImpl(catalog, source, "someTableName");
                TableSchema schema = new SimpleTableSchema(catalog, newDataNode, tableMetaData, physicalSchema);
                ((SimpleRDFDataDictionary) dataDictionary).add(schema);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return newDataNode;
    }
}

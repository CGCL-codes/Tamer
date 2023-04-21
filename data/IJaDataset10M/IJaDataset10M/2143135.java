package qp.optimizer;

import qp.operators.*;
import qp.utils.*;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Enumeration;
import java.io.*;

public class PlanCost {

    int cost;

    int numtuple;

    /**
	 * If buffers are not enough for a selected join then this plan is not
	 * feasible and return a cost of infinity
	 **/
    boolean isFeasible;

    /**
	 * Hashtable stores mapping from Attribute name to number of distinct values
	 * of that attribute
	 **/
    Hashtable ht;

    public PlanCost() {
        ht = new Hashtable();
        cost = 0;
    }

    /** returns the cost of the plan **/
    public int getCost(Operator root) {
        isFeasible = true;
        numtuple = calculateCost(root);
        if (isFeasible == true) {
            return cost;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    /** get number of tuples in estimated results **/
    public int getNumTuples() {
        return numtuple;
    }

    /** returns number of tuples in the root **/
    protected int calculateCost(Operator node) {
        if (node.getOpType() == OpType.JOIN) {
            return getStatistics((Join) node);
        } else if (node.getOpType() == OpType.SELECT) {
            return getStatistics((Select) node);
        } else if (node.getOpType() == OpType.PROJECT) {
            return getStatistics((Project) node);
        } else if (node.getOpType() == OpType.SCAN) {
            return getStatistics((Scan) node);
        } else if (node.getOpType() == OpType.ORDERBY) {
            return getStatistics((OrderBy) node);
        } else if (node.getOpType() == OpType.DISTINCT) {
            return getStatistics((Distinct) node);
        }
        return -1;
    }

    /**
	 * projection will not change any statistics No cost involved as done on the
	 * fly
	 **/
    protected int getStatistics(Project node) {
        return calculateCost(node.getBase());
    }

    /** calculates the statistics, and cost of join operation **/
    protected int getStatistics(Join node) {
        int lefttuples = calculateCost(node.getLeft());
        int righttuples = calculateCost(node.getRight());
        if (isFeasible == false) {
            return -1;
        }
        Condition con = node.getCondition();
        Schema leftschema = node.getLeft().getSchema();
        Schema rightschema = node.getRight().getSchema();
        int tuplesize = node.getSchema().getTupleSize();
        int outcapacity = Batch.getPageSize() / tuplesize;
        int leftuplesize = leftschema.getTupleSize();
        int leftcapacity = Batch.getPageSize() / leftuplesize;
        int righttuplesize = rightschema.getTupleSize();
        int rightcapacity = Batch.getPageSize() / righttuplesize;
        int leftpages = (int) Math.ceil(((double) lefttuples) / (double) leftcapacity);
        int rightpages = (int) Math.ceil(((double) righttuples) / (double) rightcapacity);
        Attribute leftjoinAttr = con.getLhs();
        Attribute rightjoinAttr = (Attribute) con.getRhs();
        int leftattrind = leftschema.indexOf(leftjoinAttr);
        int rightattrind = rightschema.indexOf(rightjoinAttr);
        leftjoinAttr = leftschema.getAttribute(leftattrind);
        rightjoinAttr = rightschema.getAttribute(rightattrind);
        int leftattrdistn = ((Integer) ht.get(leftjoinAttr)).intValue();
        int rightattrdistn = ((Integer) ht.get(rightjoinAttr)).intValue();
        int outtuples = (int) Math.ceil(((double) lefttuples * righttuples) / (double) Math.max(leftattrdistn, rightattrdistn));
        int mindistinct = Math.min(leftattrdistn, rightattrdistn);
        ht.put(leftjoinAttr, new Integer(mindistinct));
        ht.put(leftjoinAttr, new Integer(mindistinct));
        int joinType = node.getJoinType();
        int numbuff = BufferManager.getBuffersPerJoin();
        int joincost;
        switch(joinType) {
            case JoinType.NESTEDJOIN:
                joincost = leftpages + leftpages * rightpages;
                break;
            case JoinType.BLOCKNESTED:
                joincost = (int) (rightpages + leftpages * Math.ceil(1.0 * rightpages / (numbuff - 2)));
                break;
            case JoinType.SORTMERGE:
                int N1_left = (int) Math.ceil(1.0 * leftpages / numbuff);
                int N1_right = (int) Math.ceil(1.0 * rightpages / numbuff);
                int P_left = (int) Math.ceil(1.0 * Math.log(N1_left) / Math.log(numbuff - 1)) + 1;
                int P_right = (int) Math.ceil(1.0 * Math.log(N1_right) / Math.log(numbuff - 1)) + 1;
                double sortcost_left = 2 * leftpages * (P_left);
                double sortcost_right = 2 * rightpages * (P_right);
                joincost = (int) (sortcost_left + sortcost_right + leftpages + rightpages);
                break;
            case JoinType.HASHJOIN:
                joincost = 0;
                break;
            default:
                joincost = 0;
                break;
        }
        cost = cost + joincost;
        System.out.println("JOIN COST: " + joincost);
        return outtuples;
    }

    /**
	 * Find number of incoming tuples, Using the selectivity find # of output
	 * tuples And statistics about the attributes Selection is performed on the
	 * fly, so no cost involved
	 **/
    protected int getStatistics(Select node) {
        int intuples = calculateCost(node.getBase());
        if (isFeasible == false) {
            return Integer.MAX_VALUE;
        }
        Condition con = node.getCondition();
        Schema schema = node.getSchema();
        Attribute attr = con.getLhs();
        int index = schema.indexOf(attr);
        Attribute fullattr = schema.getAttribute(index);
        int exprtype = con.getExprType();
        Integer temp = (Integer) ht.get(fullattr);
        int numdistinct = temp.intValue();
        int outtuples;
        if (exprtype == Condition.EQUAL) {
            outtuples = (int) Math.ceil((double) intuples / (double) numdistinct);
        } else if (exprtype == Condition.NOTEQUAL) {
            outtuples = (int) Math.ceil(intuples - ((double) intuples / (double) numdistinct));
        } else {
            outtuples = (int) Math.ceil(0.5 * intuples);
        }
        for (int i = 0; i < schema.getNumCols(); i++) {
            Attribute attri = schema.getAttribute(i);
            int oldvalue = ((Integer) ht.get(attri)).intValue();
            int newvalue = (int) Math.ceil(((double) outtuples / (double) intuples) * oldvalue);
            ht.put(attri, new Integer(newvalue));
        }
        return outtuples;
    }

    /**
	 * the statistics file <tablename>.stat to find the statistics about that
	 * table; This table contains number of tuples in the table number of
	 * distinct values of each attribute
	 **/
    protected int getStatistics(Scan node) {
        String tablename = node.getTabName();
        String filename = Util.data + tablename + ".stat";
        Schema schema = node.getSchema();
        int numAttr = schema.getNumCols();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(filename));
        } catch (IOException io) {
            System.out.println("Error in opening file " + filename);
            System.exit(1);
        }
        String line = null;
        try {
            line = in.readLine();
        } catch (IOException io) {
            System.out.println("Error in readin first line of " + filename);
            System.exit(1);
        }
        StringTokenizer tokenizer = new StringTokenizer(line);
        if (tokenizer.countTokens() != 1) {
            System.out.println("incorrect format of statastics file " + filename);
            System.exit(1);
        }
        String temp = tokenizer.nextToken();
        int numtuples = Integer.parseInt(temp);
        try {
            line = in.readLine();
        } catch (IOException io) {
            System.out.println("error in reading second line of " + filename);
            System.exit(1);
        }
        tokenizer = new StringTokenizer(line);
        if (tokenizer.countTokens() != numAttr) {
            System.out.println("incorrect format of statastics file " + filename);
            System.exit(1);
        }
        for (int i = 0; i < numAttr; i++) {
            Attribute attr = schema.getAttribute(i);
            temp = tokenizer.nextToken();
            Integer distinctValues = Integer.valueOf(temp);
            ht.put(attr, distinctValues);
        }
        int tuplesize = schema.getTupleSize();
        int pagesize = Batch.getPageSize() / tuplesize;
        int numpages = (int) Math.ceil((double) numtuples / (double) pagesize);
        cost = cost + numpages;
        System.out.println("SCAN COST: " + numpages);
        try {
            in.close();
        } catch (IOException io) {
            System.out.println("error in closing the file " + filename);
            System.exit(1);
        }
        return numtuples;
    }

    protected int getStatistics(OrderBy node) {
        int batchSize = Batch.getPageSize();
        int tupleSize = node.getSchema().getTupleSize();
        int tuplePerPage = batchSize / tupleSize;
        int numOfTuples = calculateCost(node.getBase());
        int numOfPages = (int) Math.ceil(numOfTuples / tuplePerPage);
        int numOfBuffers = BufferManager.getNumBuffer();
        int N1 = (int) Math.ceil(numOfPages / numOfBuffers);
        cost += 2 * numOfPages * (Math.ceil(Math.log(N1) / Math.log(numOfBuffers - 1)) + 1);
        return numOfTuples;
    }

    protected int getStatistics(Distinct node) {
        int batchSize = Batch.getPageSize();
        int tupleSize = node.getSchema().getTupleSize();
        int tuplePerPage = batchSize / tupleSize;
        int numOfTuples = calculateCost(node.getBase());
        int numOfPages = (int) Math.ceil(numOfTuples / tuplePerPage);
        int numOfBuffers = BufferManager.getNumBuffer();
        int N1 = (int) Math.ceil(numOfPages / numOfBuffers);
        cost += 2 * numOfPages * (Math.ceil(Math.log(N1) / Math.log(numOfBuffers - 1)) + 1);
        return numOfTuples;
    }
}

package dbs_project.myDB.querylayer.predicate;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import dbs_project.exceptions.InvalidKeyException;
import dbs_project.exceptions.InvalidRangeException;
import dbs_project.exceptions.NoSuchColumnException;
import dbs_project.exceptions.NoSuchIndexException;
import dbs_project.exceptions.NoSuchRowException;
import dbs_project.exceptions.QueryExecutionException;
import dbs_project.exceptions.RangeQueryNotSupportedException;
import dbs_project.myDB.indexlayer.MyIndexableTable;
import dbs_project.myDB.querylayer.JoinRestriction;
import dbs_project.myDB.querylayer.KeyColumnPair;
import dbs_project.query.predicate.ExpressionElement;
import dbs_project.query.predicate.Operator;

public class PredicateParser {

    public static List<JoinRestriction> getJoinRestriction(List<MyIndexableTable> tables, ExpressionElement pred, List<KeyColumnPair> keyColPairLst) throws QueryExecutionException, NoSuchColumnException, NoSuchRowException, InvalidRangeException, InvalidKeyException, RangeQueryNotSupportedException, NoSuchIndexException, ParseException {
        List<JoinRestriction> restrictions = new ArrayList<JoinRestriction>();
        MyExpressionVisitor visitor = new MyExpressionVisitor();
        pred.accept(visitor);
        switch(visitor.getOperator()) {
            case AND:
                for (ExpressionElement operand : visitor.getOperands()) {
                    restrictions = mergeRestriction(restrictions, getJoinRestriction(tables, operand, keyColPairLst), Operator.AND);
                }
                break;
            case OR:
                for (ExpressionElement operand : visitor.getOperands()) {
                    restrictions = mergeRestriction(restrictions, getJoinRestriction(tables, operand, keyColPairLst), Operator.OR);
                }
                break;
            default:
                if (isRestriction(pred)) {
                    restrictions.add(new JoinRestriction(tables, pred));
                } else if (isKeyColumnPair(pred)) {
                    keyColPairLst.add(new KeyColumnPair(tables, pred));
                } else {
                    getJoinRestriction(tables, pred, keyColPairLst);
                }
        }
        return restrictions;
    }

    private static List<JoinRestriction> mergeRestriction(List<JoinRestriction> target, List<JoinRestriction> source, Operator op) {
        if (target.isEmpty()) {
            return source;
        } else if (source.isEmpty()) {
            return target;
        } else {
            int j;
            switch(op) {
                case AND:
                    for (int i = 0; i < source.size(); i++) {
                        for (j = 0; j < target.size(); j++) {
                            if (source.get(i).getTable().getname().hashCode() == target.get(j).getTable().getname().hashCode()) {
                                target.get(j).andRowIDs(source.get(i).getRowIDs());
                                break;
                            }
                        }
                        if (j == target.size()) {
                            target.add(source.get(i));
                        }
                    }
                    break;
                case OR:
                    for (int i = 0; i < source.size(); i++) {
                        for (j = 0; j < target.size(); j++) {
                            if (source.get(i).getTable().getname().hashCode() == target.get(j).getTable().getname().hashCode()) {
                                target.get(j).orRowIDs(source.get(i).getRowIDs());
                                break;
                            }
                        }
                        if (j == target.size()) {
                            target.add(source.get(i));
                        }
                    }
                    break;
                default:
                    ;
            }
            return target;
        }
    }

    private static boolean isRestriction(ExpressionElement pred) {
        MyExpressionVisitor visitor = new MyExpressionVisitor();
        pred.accept(visitor);
        MyExpressionVisitor rightVisitor = new MyExpressionVisitor();
        visitor.getOperand(1).accept(rightVisitor);
        switch(rightVisitor.getConstantType()) {
            case COLUMN_NAME:
                return false;
            case VALUE_LITERAL:
                return true;
            case NULL_LITERAL:
                return false;
            default:
                return false;
        }
    }

    private static boolean isKeyColumnPair(ExpressionElement pred) {
        MyExpressionVisitor visitor = new MyExpressionVisitor();
        pred.accept(visitor);
        MyExpressionVisitor rightVisitor = new MyExpressionVisitor();
        visitor.getOperand(1).accept(rightVisitor);
        switch(rightVisitor.getConstantType()) {
            case COLUMN_NAME:
                return true;
            case VALUE_LITERAL:
                return false;
            case NULL_LITERAL:
                return false;
            default:
                return false;
        }
    }
}

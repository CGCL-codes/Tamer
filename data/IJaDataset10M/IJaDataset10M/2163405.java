package com.google.gwt.dev.jjs.impl;

import com.google.gwt.core.ext.linker.StatementRanges;
import com.google.gwt.core.ext.soyc.Range;
import com.google.gwt.dev.jjs.SourceInfo;
import com.google.gwt.dev.util.editdistance.GeneralEditDistance;
import com.google.gwt.dev.util.editdistance.GeneralEditDistances;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Re-orders function declarations according to a given metric and clustering
 * algorithm in order to boost gzip/deflation compression efficiency. This
 * version uses the edit-distance algorithm as a metric, and a semi-greedy
 * strategy for grouping functions together.
 */
public class JsFunctionClusterer extends JsAbstractTextTransformer {

    /**
   * Used by isFunctionDeclaration to check a statement is a function
   * declaration or not. This should match standard declarations, such as
   * "function a() { ... }" and "jslink.a=function() { ... }". The latter form
   * is typically emitted by the cross-site linker.
   */
    private static final Pattern functionDeclarationPattern = Pattern.compile("function |[a-zA-Z][.$_a-zA-Z0-9]*=function");

    /**
   * Functions which have an edit-distance greater than this limit are
   * considered equally different.
   */
    private static final int MAX_DISTANCE_LIMIT = 100;

    /**
   * Maximum number of functions to search for minimal edit-distance before
   * giving up.
   */
    private static final int SEARCH_LIMIT = 10;

    /**
   * Tells whether a statement is a function declaration or not.
   */
    private static boolean isFunctionDeclaration(String code) {
        return functionDeclarationPattern.matcher(code).lookingAt();
    }

    /**
   * Number of function declarations found.
   */
    private int numFunctions;

    /**
   * The statement indices after clustering. The element at index j represents
   * the index of the statement in the original code that is moved to index j
   * in the new code after clustering.
   */
    private int[] reorderedIndices;

    public JsFunctionClusterer(JsAbstractTextTransformer xformer) {
        super(xformer);
    }

    public JsFunctionClusterer(String js, StatementRanges statementRanges, Map<Range, SourceInfo> sourceInfoMap) {
        super(js, statementRanges, sourceInfoMap);
    }

    @Override
    public void exec() {
        LinkedList<Integer> functionIndices = new LinkedList<Integer>();
        for (int i = 0; i < statementRanges.numStatements(); i++) {
            String code = getJsForRange(i);
            if (isFunctionDeclaration(code)) {
                functionIndices.add(i);
            }
        }
        numFunctions = functionIndices.size();
        if (functionIndices.size() < 2) {
            return;
        }
        Collections.sort(functionIndices, new Comparator<Integer>() {

            public int compare(Integer index1, Integer index2) {
                return stmtSize(index1) - (stmtSize(index2));
            }
        });
        int[] clusteredIndices = new int[functionIndices.size()];
        int currentFunction = 0;
        clusteredIndices[currentFunction] = functionIndices.get(0);
        functionIndices.remove(0);
        while (!functionIndices.isEmpty()) {
            String currentCode = getJsForRange(clusteredIndices[currentFunction]);
            final GeneralEditDistance editDistance = GeneralEditDistances.getLevenshteinDistance(currentCode);
            int bestIndex = 0;
            int bestFunction = functionIndices.getFirst();
            int bestDistance = MAX_DISTANCE_LIMIT;
            int count = 0;
            for (int functionIndex : functionIndices) {
                if (count >= SEARCH_LIMIT) {
                    break;
                }
                String testCode = getJsForRange(functionIndex);
                int dist = editDistance.getDistance(testCode, bestDistance);
                if (dist < bestDistance) {
                    bestDistance = dist;
                    bestIndex = count;
                    bestFunction = functionIndex;
                }
                count++;
            }
            currentFunction++;
            clusteredIndices[currentFunction] = bestFunction;
            functionIndices.remove(bestIndex);
        }
        reorderedIndices = Arrays.copyOf(clusteredIndices, statementRanges.numStatements());
        recomputeJsAndStatementRanges(clusteredIndices);
    }

    /**
   * Returns the array of reordered statement indices after clustering.
   * @return The array of indices, where the element at index j represents
   * the index of the statement in the original code that is moved to index j
   * in the new code after clustering.
   */
    public int[] getReorderedIndices() {
        return reorderedIndices;
    }

    @Override
    protected void endStatements(StringBuilder newJs, ArrayList<Integer> starts, ArrayList<Integer> ends) {
        int j = numFunctions;
        for (int i = 0; i < statementRanges.numStatements(); i++) {
            String code = getJsForRange(i);
            if (!isFunctionDeclaration(code)) {
                addStatement(j, code, newJs, starts, ends);
                reorderedIndices[j] = i;
                j++;
            }
        }
        super.endStatements(newJs, starts, ends);
    }

    /**
   * Fixes the index ranges of individual expressions in the generated
   * JS after function clustering has reordered statements. Loops over
   * each expression, determines which statement it falls in, and shifts
   * the indices according to where that statement moved.
   */
    @Override
    protected void updateSourceInfoMap() {
        if (sourceInfoMap != null) {
            Map<Range, Range> statementShifts = new HashMap<Range, Range>();
            for (int j = 0; j < statementRanges.numStatements(); j++) {
                int permutedStart = statementRanges.start(j);
                int permutedEnd = statementRanges.end(j);
                int originalStart = originalStatementRanges.start(reorderedIndices[j]);
                int originalEnd = originalStatementRanges.end(reorderedIndices[j]);
                statementShifts.put(new Range(originalStart, originalEnd), new Range(permutedStart, permutedEnd));
            }
            Range[] oldStatementRanges = statementShifts.keySet().toArray(new Range[0]);
            Arrays.sort(oldStatementRanges, Range.SOURCE_ORDER_COMPARATOR);
            Range[] oldExpressionRanges = sourceInfoMap.keySet().toArray(new Range[0]);
            Arrays.sort(oldExpressionRanges, Range.SOURCE_ORDER_COMPARATOR);
            Map<Range, SourceInfo> updatedInfoMap = new HashMap<Range, SourceInfo>();
            Range entireProgram = new Range(0, oldStatementRanges[oldStatementRanges.length - 1].getEnd());
            for (int i = 0, j = 0; j < oldExpressionRanges.length; j++) {
                Range oldExpression = oldExpressionRanges[j];
                if (oldExpression.equals(entireProgram)) {
                    updatedInfoMap.put(oldExpression, sourceInfoMap.get(oldExpression));
                    continue;
                }
                if (!oldStatementRanges[i].contains(oldExpressionRanges[j])) {
                    i++;
                    assert oldStatementRanges[i].contains(oldExpressionRanges[j]);
                }
                Range oldStatement = oldStatementRanges[i];
                Range newStatement = statementShifts.get(oldStatement);
                int shift = newStatement.getStart() - oldStatement.getStart();
                Range oldExpressionRange = oldExpressionRanges[j];
                Range newExpressionRange = new Range(oldExpressionRange.getStart() + shift, oldExpressionRange.getEnd() + shift);
                updatedInfoMap.put(newExpressionRange, sourceInfoMap.get(oldExpressionRange));
            }
            sourceInfoMap = updatedInfoMap;
        }
    }

    private int stmtSize(int index1) {
        return statementRanges.end(index1) - statementRanges.start(index1);
    }
}

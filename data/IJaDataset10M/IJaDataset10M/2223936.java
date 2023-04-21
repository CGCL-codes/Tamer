package org.xmlpull.v1.builder.xpath.saxpath.com.werken.saxpath;

import org.xmlpull.v1.builder.xpath.saxpath.XPathHandler;

class DefaultXPathHandler implements XPathHandler {

    static class Singleton {

        static final DefaultXPathHandler instance = new DefaultXPathHandler();
    }

    public static XPathHandler getInstance() {
        return Singleton.instance;
    }

    public void startXPath() {
    }

    public void endXPath() {
    }

    public void startPathExpr() {
    }

    public void endPathExpr() {
    }

    public void startAbsoluteLocationPath() {
    }

    public void endAbsoluteLocationPath() {
    }

    public void startRelativeLocationPath() {
    }

    public void endRelativeLocationPath() {
    }

    public void startNameStep(int axis, String prefix, String localName) {
    }

    public void startTextNodeStep(int axis) {
    }

    public void endTextNodeStep() {
    }

    public void startCommentNodeStep(int axis) {
    }

    public void endCommentNodeStep() {
    }

    public void startAllNodeStep(int axis) {
    }

    public void endAllNodeStep() {
    }

    public void startProcessingInstructionNodeStep(int axis, String name) {
    }

    public void endProcessingInstructionNodeStep() {
    }

    public void endNameStep() {
    }

    public void startPredicate() {
    }

    public void endPredicate() {
    }

    public void startFilterExpr() {
    }

    public void endFilterExpr() {
    }

    public void startOrExpr() {
    }

    public void endOrExpr(boolean create) {
    }

    public void startAndExpr() {
    }

    public void endAndExpr(boolean create) {
    }

    public void startEqualityExpr() {
    }

    public void endEqualityExpr(int operator) {
    }

    public void startRelationalExpr() {
    }

    public void endRelationalExpr(int operator) {
    }

    public void startAdditiveExpr() {
    }

    public void endAdditiveExpr(int operator) {
    }

    public void startMultiplicativeExpr() {
    }

    public void endMultiplicativeExpr(int operator) {
    }

    public void startUnaryExpr() {
    }

    public void endUnaryExpr(int operator) {
    }

    public void startUnionExpr() {
    }

    public void endUnionExpr(boolean create) {
    }

    public void number(int number) {
    }

    public void number(double number) {
    }

    public void literal(String literal) {
    }

    public void variableReference(String prefix, String variableName) {
    }

    public void startFunction(String prefix, String functionName) {
    }

    public void endFunction() {
    }
}

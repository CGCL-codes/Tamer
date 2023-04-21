package calculator;

import java.io.*;

public class DumpVisitor implements Visitor {

    private PrintWriter out;

    public DumpVisitor(PrintWriter out) {
        this.out = out;
    }

    public void visitPlusExpression(PlusExpression plusExpression) {
        out.print("(");
        plusExpression.getLeftExpression().accept(this);
        out.print(" + ");
        plusExpression.getRightExpression().accept(this);
        out.print(")");
    }

    public void visitMinusExpression(MinusExpression minusExpression) {
        out.print("(");
        minusExpression.getLeftExpression().accept(this);
        out.print(" - ");
        minusExpression.getRightExpression().accept(this);
        out.print(")");
    }

    public void visitMultiplyExpression(MultiplyExpression multiplyExpression) {
        out.print("(");
        multiplyExpression.getLeftExpression().accept(this);
        out.print(" * ");
        multiplyExpression.getRightExpression().accept(this);
        out.print(")");
    }

    public void visitDivideExpression(DivideExpression divideExpression) {
        out.print("(");
        divideExpression.getLeftExpression().accept(this);
        out.print(" / ");
        divideExpression.getRightExpression().accept(this);
        out.print(")");
    }

    public void visitRemainExpression(RemainExpression remainExpression) {
        out.print("(");
        remainExpression.getLeftExpression().accept(this);
        out.print(" % ");
        remainExpression.getRightExpression().accept(this);
        out.print(")");
        out.flush();
    }

    public void visitNumberLiteral(NumberLiteral numberLiteral) {
        out.print(numberLiteral.getValue());
    }

    public static void main(String[] args) throws Exception {
        Reader reader;
        if (args.length > 0) {
            reader = new FileReader(args[0]);
        } else {
            reader = new InputStreamReader(System.in);
        }
        Scanner scanner = new Scanner(reader);
        Parser parser = new Parser();
        Expression expression = (Expression) parser.parse(scanner);
        final PrintWriter out = new PrintWriter(System.out);
        expression.accept(new DumpVisitor(out));
        out.flush();
    }
}

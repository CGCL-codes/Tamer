package behaviour.interpreter;

import java.io.*;

public class Application {

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("> ");
        String line = reader.readLine();
        while (!"exit".equals(line)) {
            Scanner scanner = new Scanner("()+-", new StringReader(line));
            ExpressionParser parser = new ExpressionParser(scanner);
            try {
                Expression expr = parser.parseExpression();
                System.out.println(expr.evaluate());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
            }
            System.out.print("> ");
            line = reader.readLine();
        }
    }
}

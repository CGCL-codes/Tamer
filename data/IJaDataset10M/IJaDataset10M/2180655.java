package jresolution.proposition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class Tester {

    /**
	 * @param args
	 * @throws IOException
	 */
    public static void main(String[] args) throws IOException {
        if (args.length >= 1) {
            int length;
            String clause;
            StringTokenizer tokenizer;
            Set<Proposition> arguments;
            Set<Clause> clauses = new HashSet<Clause>();
            String token;
            for (length = 0; ; length++) {
                System.out.println("Clause " + length + ":");
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                clause = reader.readLine();
                tokenizer = new StringTokenizer(clause);
                if (!tokenizer.hasMoreTokens()) {
                    break;
                }
                arguments = new HashSet<Proposition>();
                while (tokenizer.hasMoreTokens()) {
                    token = tokenizer.nextToken();
                    if (token.charAt(0) == '!') {
                        arguments.add(new Proposition(false, token.substring(1)));
                    } else {
                        arguments.add(new Proposition(true, token));
                    }
                }
                clauses.add(new Clause(arguments));
                System.out.println(clauses);
            }
            Resolver resolver = new Resolver();
            System.out.println(resolver.resolve(clauses));
        } else {
            Proposition[] p = new Proposition[5];
            Proposition[] pn = new Proposition[5];
            Set<Proposition> arguments0 = new HashSet<Proposition>();
            Set<Proposition> arguments1 = new HashSet<Proposition>();
            for (int i = 0; i < p.length; i++) {
                p[i] = new Proposition(true, "p" + i);
                arguments0.add(p[i]);
                arguments1.add(p[i]);
            }
            for (int i = 0; i < p.length; i++) {
                pn[i] = new Proposition(false, "p" + i);
            }
            Clause clause0 = new Clause(arguments0);
            Clause clause1 = new Clause(arguments1);
            System.out.println(clause0);
            System.out.println(clause1);
            System.out.println(clause0.equals(clause1));
            for (int i = 0; i < p.length; i++) {
                clause1.remove(p[i]);
                clause1.add(pn[i]);
            }
            System.out.println(clause0);
            System.out.println(clause1);
            System.out.println(clause0.equals(clause1));
            Resolver resolver = new Resolver();
            Set<Clause> inputSet = new HashSet<Clause>();
            inputSet.add(clause0);
            inputSet.add(clause1);
            System.out.println(resolver.resolve(inputSet));
            System.out.println(clause0);
            System.out.println(clause1);
            System.out.println();
            Set<Clause> input = new HashSet<Clause>();
            input.add(clause0);
            input.add(clause1);
            System.out.println(resolver.resolve(input));
        }
    }
}

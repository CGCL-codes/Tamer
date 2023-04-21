package com.bluebrim.swing.client.maskfield;

/**
  <condition> := <expression> [ <conjunction> <condition> ]
  <conjunction> := '&' | '|'
  <expression> := '(' <expression> ')' | <character-set>
  <character-set> := [ '!' ] <characters-list>
**/
public class MaskParser {

    private String expandSet(String text) {
        int i = 0;
        StringBuffer buffer = new StringBuffer();
        while (i < text.length()) {
            if (i < text.length() - 2 && text.charAt(i + 1) == '-') {
                int from = (int) text.charAt(i);
                int to = (int) text.charAt(i + 2);
                if (from > to) {
                    int temp = from;
                    from = to;
                    to = temp;
                }
                for (int c = from; c <= to; c++) {
                    buffer.append((char) c);
                }
                i += 3;
            } else {
                buffer.append(text.charAt(i));
                i++;
            }
        }
        return buffer.toString();
    }

    public static void expect(MaskToken token, char chr) {
        if (!token.equals(chr)) throw new MaskException("Syntax error: '" + chr + "' expected at " + token.pos);
    }

    public static void main(String[] args) {
        MaskParser parser = new MaskParser();
        MaskElement element = parser.parseMacro("[a-z,�,�,�,A-Z,�,�,�]");
        System.out.println(element);
        System.out.println(element.match('�'));
    }

    public MaskElement parseCondition(MaskTokenizer tokenizer) {
        MaskElement node = parseExpression(tokenizer);
        if (tokenizer.hasMoreTokens()) {
            MaskToken next = tokenizer.nextToken();
            if (next.equals('|')) {
                return new MaskCondition(MaskCondition.OR, node, parseCondition(tokenizer));
            }
            if (next.equals('&')) {
                return new MaskCondition(MaskCondition.AND, node, parseCondition(tokenizer));
            }
        }
        tokenizer.ignoreToken();
        return node;
    }

    private MaskElement parseExpression(MaskTokenizer tokenizer) {
        MaskToken token = tokenizer.nextToken();
        if (token.equals('(')) {
            MaskElement node = parseCondition(tokenizer);
            expect(tokenizer.nextToken(), ')');
            return new MaskExpression(node);
        }
        tokenizer.ignoreToken();
        return parseSet(tokenizer);
    }

    public MaskElement parseMacro(String text) {
        MaskTokenizer tokenizer = new MaskTokenizer("&|![]() ", "");
        tokenizer.tokenize(text);
        return parseCondition(tokenizer);
    }

    private MaskElement parseSet(MaskTokenizer tokenizer) {
        expect(tokenizer.nextToken(), '[');
        MaskToken token = tokenizer.nextToken();
        boolean negate = token.equals('!');
        if (negate) token = tokenizer.nextToken();
        expect(tokenizer.nextToken(), ']');
        return new MaskSet(negate, expandSet(token.text));
    }
}

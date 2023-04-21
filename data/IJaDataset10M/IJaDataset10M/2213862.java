package fn.parser;

public class FnParseNode extends SimpleNode {

    public Token firstToken, lastToken;

    public FnParseNode(int i) {
        super(i);
    }

    public FnParseNode(FnParser p, int i) {
        super(p, i);
    }

    public int getFirstLineNumber() {
        return firstToken.beginLine;
    }

    public int getLastLineNumber() {
        return lastToken.endLine;
    }

    public int getFirstColumn() {
        return firstToken.beginColumn;
    }

    public int getLastColumn() {
        return lastToken.endColumn;
    }

    public String dumpTokens() {
        StringBuffer image = new StringBuffer();
        Token currentToken;
        for (currentToken = firstToken; currentToken != lastToken; currentToken = currentToken.next) {
            image.append(currentToken.image);
            image.append(" ");
        }
        image.append(currentToken.image);
        return image.toString();
    }

    public String dumpBlock() {
        StringBuffer image = new StringBuffer();
        Token currentToken;
        if (firstToken.next == lastToken) {
            return "";
        }
        for (currentToken = firstToken.next; currentToken.next != lastToken; currentToken = currentToken.next) {
            image.append(currentToken.image);
            if (currentToken.image.equals(";") || currentToken.image.equals("{") || currentToken.image.equals("}")) {
                image.append("\n");
            } else {
                image.append(" ");
            }
        }
        image.append(currentToken.image);
        return image.toString();
    }

    public int numberOfTokens() {
        Token currentToken;
        int tokenCount = 0;
        for (currentToken = firstToken; currentToken != lastToken; currentToken = currentToken.next) {
            tokenCount++;
        }
        return ++tokenCount;
    }

    public String dumpTokens(int first, int last) {
        StringBuffer image = new StringBuffer();
        Token currentToken;
        int tokenCounter = 0;
        assert (last <= numberOfTokens());
        assert (last >= 1);
        assert (first >= 0);
        assert (first < last);
        for (currentToken = firstToken; tokenCounter != first; currentToken = currentToken.next) {
            tokenCounter++;
        }
        while (tokenCounter < last) {
            image.append(currentToken.image);
            currentToken = currentToken.next;
            tokenCounter++;
        }
        return image.toString();
    }
}

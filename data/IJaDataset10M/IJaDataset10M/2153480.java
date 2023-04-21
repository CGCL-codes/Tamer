package org.jmlspecs.jml4.ast;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.internal.compiler.flow.FlowContext;
import org.eclipse.jdt.internal.compiler.flow.FlowInfo;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;

public class JmlStoreRefKeyword extends JmlStoreRef {

    public static final int NOTHING = 1;

    public static final int EVERYTHING = 2;

    public static final int NOT_SPECIFIED = 3;

    private final String keywordLexeme;

    private final int code;

    public JmlStoreRefKeyword(String keywordLexeme) {
        this.keywordLexeme = keywordLexeme;
        this.code = keywordLexeme2Code(keywordLexeme);
    }

    public StringBuffer print(int indent, StringBuffer output) {
        return output.append(this.keywordLexeme);
    }

    public void analyseCode(BlockScope scope, FlowContext methodContext, FlowInfo flowInfo) {
    }

    public void resolve(BlockScope scope) {
    }

    private static int keywordLexeme2Code(String lexeme) {
        switch(lexeme.charAt(4)) {
            case 'h':
                return NOTHING;
            case 'r':
                return EVERYTHING;
            case '_':
                return NOT_SPECIFIED;
            default:
                Assert.isTrue(false, "Unexpected StoreRef Keyword: '" + lexeme + "'");
                return 0;
        }
    }
}

package sandbox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import koopa.tokenizers.Tokenizer;
import koopa.tokenizers.cobol.CompilerDirectivesTokenizer;
import koopa.tokenizers.cobol.ContinuationWeldingTokenizer;
import koopa.tokenizers.cobol.LineContinuationTokenizer;
import koopa.tokenizers.cobol.LineSplittingTokenizer;
import koopa.tokenizers.cobol.ProgramAreaTokenizer;
import koopa.tokenizers.cobol.PseudoLiteralTokenizer;
import koopa.tokenizers.cobol.SeparatorTokenizer;
import koopa.tokenizers.cobol.SourceFormat;
import koopa.tokenizers.cobol.SourceFormattingDirectivesFilter;
import koopa.tokenizers.cobol.TokenCountVerifiyingTokenizer;
import koopa.tokenizers.cobol.TokenStateVerifiyingTokenizer;
import koopa.tokenizers.cobol.tags.AreaTag;
import koopa.tokenizers.cobol.tags.ContinuationsTag;
import koopa.tokenizers.cobol.tags.SyntacticTag;
import koopa.tokenizers.generic.FilteringTokenizer;
import koopa.tokens.Token;
import koopa.tokens.TokenFilter;

public class TokenizerTest {

    public static void main(String[] args) throws IOException {
        final List<Token> fixedTokens = process("testsuite/koopa/test.CBL", SourceFormat.FIXED);
        final List<Token> freeTokens = process("testsuite/koopa/free.CBL", SourceFormat.FREE);
        boolean mismatched = false;
        final int numberOfFixedTokens = fixedTokens.size();
        final int numberOfFreeTokens = freeTokens.size();
        final int numberOfTokens = Math.min(numberOfFixedTokens, numberOfFreeTokens);
        for (int i = 0; !mismatched && i < numberOfTokens; i++) {
            final Token fixedToken = fixedTokens.get(i);
            final Token freeToken = freeTokens.get(i);
            if (!fixedToken.getText().equals(freeToken.getText())) {
                System.out.println("Mismatch starts with " + fixedToken + " vs. " + freeToken + ".");
                mismatched = true;
                break;
            }
            for (Object tag : fixedToken.getTags()) {
                if (tag instanceof ContinuationsTag) {
                    continue;
                }
                if (!freeToken.hasTag(tag)) {
                    System.out.println(fixedToken + " has tag " + tag + ", but " + freeToken + " does not.");
                    mismatched = true;
                }
            }
            for (Object tag : freeToken.getTags()) {
                if (!fixedToken.hasTag(tag)) {
                    System.out.println(freeToken + " has tag " + tag + ", but " + fixedToken + " does not.");
                    mismatched = true;
                }
            }
        }
        if (mismatched || numberOfFixedTokens != numberOfFreeTokens) {
            System.out.println("Inputs differ in the tokens they produce.");
            System.out.println("That's bad!");
        } else {
            System.out.println("Inputs generate the same tokens.");
            System.out.println("That's good !");
        }
    }

    public static List<Token> process(String filename, SourceFormat format) throws IOException {
        System.out.println("Tokenizing " + filename);
        FileReader reader = new FileReader(filename);
        Tokenizer tokenizer;
        tokenizer = new LineSplittingTokenizer(new BufferedReader(reader));
        tokenizer = new CompilerDirectivesTokenizer(tokenizer);
        tokenizer = new ProgramAreaTokenizer(tokenizer, format);
        tokenizer = new SourceFormattingDirectivesFilter(tokenizer);
        if (format == SourceFormat.FIXED) {
            tokenizer = new LineContinuationTokenizer(tokenizer);
            tokenizer = new ContinuationWeldingTokenizer(tokenizer);
        }
        tokenizer = new SeparatorTokenizer(tokenizer);
        tokenizer = new PseudoLiteralTokenizer(tokenizer);
        TokenCountVerifiyingTokenizer countVerifier = null;
        tokenizer = countVerifier = new TokenCountVerifiyingTokenizer(tokenizer);
        TokenStateVerifiyingTokenizer stateVerifier = null;
        tokenizer = stateVerifier = new TokenStateVerifiyingTokenizer(tokenizer);
        tokenizer = new FilteringTokenizer(tokenizer, AreaTag.PROGRAM_TEXT_AREA);
        tokenizer = new FilteringTokenizer(tokenizer, new TokenFilter() {

            public boolean accepts(Token token) {
                return !token.hasTag(SyntacticTag.SEPARATOR) || !token.getText().trim().equals("");
            }
        });
        List<Token> tokens = new ArrayList<Token>();
        Token nextToken = null;
        while ((nextToken = tokenizer.nextToken()) != null) {
            tokens.add(nextToken);
        }
        tokenizer.quit();
        System.out.println("Processed " + tokens.size() + " top level token(s).");
        if (countVerifier != null) {
            for (String message : countVerifier.getErrorMessages()) {
                System.out.println(message);
            }
        }
        if (stateVerifier != null) {
            for (String message : stateVerifier.getErrorMessages()) {
                System.out.println(message);
            }
        }
        System.out.println();
        return tokens;
    }
}

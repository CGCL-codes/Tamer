package org.thymeleaf.standard.inliner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Arguments;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.dom.AbstractTextNode;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.standard.expression.StandardExpressionProcessor;

/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public class StandardTextTextInliner implements IStandardTextInliner {

    private static final Logger logger = LoggerFactory.getLogger(StandardTextTextInliner.class);

    public static final StandardTextTextInliner INSTANCE = new StandardTextTextInliner();

    public static final String TEXT_INLINE_EVAL = "\\[\\[(.*?)\\]\\]";

    public static final Pattern TEXT_INLINE_EVAL_PATTERN = Pattern.compile(TEXT_INLINE_EVAL, Pattern.DOTALL);

    private StandardTextTextInliner() {
        super();
    }

    public void inline(final Arguments arguments, final AbstractTextNode text) {
        final String content = text.getContent();
        final String textContent = processTextInline(content, arguments);
        text.setContent(textContent);
    }

    static String processTextInline(final String input, final Arguments arguments) {
        if (input == null || input.length() == 0) {
            return input;
        }
        final Matcher matcher = TEXT_INLINE_EVAL_PATTERN.matcher(input);
        if (matcher.find()) {
            final StringBuilder strBuilder = new StringBuilder();
            int curr = 0;
            do {
                strBuilder.append(input.substring(curr, matcher.start(0)));
                final String match = matcher.group(1);
                if (logger.isTraceEnabled()) {
                    logger.trace("[THYMELEAF][{}] Applying text inline evaluation on \"{}\"", TemplateEngine.threadIndex(), match);
                }
                try {
                    final Object result = StandardExpressionProcessor.processExpression(arguments, match);
                    strBuilder.append(result);
                } catch (final TemplateProcessingException e) {
                    strBuilder.append(match);
                }
                curr = matcher.end(0);
            } while (matcher.find());
            strBuilder.append(input.substring(curr));
            return strBuilder.toString();
        }
        return input;
    }
}

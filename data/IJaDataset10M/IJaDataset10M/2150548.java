package com.jetbrains.au.jslintplugin.js.error.processor;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.util.TextRange;
import com.jetbrains.au.jslintplugin.js.error.ErrorBeanWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

/**
 * User: Dmitriy Bandurin
 * Date: 16.11.11
 * Time: 20:07
 *
 * Main interface for processing errors from JSLint
 */
public interface ErrorProcessor {

    /**
     *
     * @param text verified text
     * @param offset current offset of errorWrapper in text parameter
     * @param errorWrapper information about find errorWrapper
     * @return text range to highlight errorWrapper in text
     */
    @NotNull
    TextRange getSelectionRange(@NotNull final String text, final int offset, @NotNull ErrorBeanWrapper errorWrapper);

    /**
     *
     * @param errorWrapper errorWrapper info
     * @return configuration option linked with errorWrapper
     */
    @Nullable
    String getRelatedOption(@NotNull ErrorBeanWrapper errorWrapper);

    /**
     *
     * @param errorBeanWrapper error info
     * @return message for annotation tooltip
     */
    @NotNull
    String getMessage(@NotNull final ErrorBeanWrapper errorBeanWrapper);

    /**
     *
     * @param errorWrapper errorWrapper info
     * @return list of available fix for errorWrapper
     */
    @NotNull
    List<IntentionAction> getFixes(ErrorBeanWrapper errorWrapper);
}

package org.netbeans.lib.lexer.test;

import java.util.HashMap;
import java.util.Map;
import org.netbeans.api.lexer.InputAttributes;
import org.netbeans.api.lexer.Language;
import org.netbeans.api.lexer.LanguagePath;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenId;
import org.netbeans.spi.lexer.LanguageEmbedding;
import org.netbeans.spi.lexer.LanguageProvider;
import org.openide.util.Lookup;

/**
 * Language provider for various lexer-related tests.
 * <br/>
 * For using it a "META-INF/services/org.netbeans.spi.lexer.LanguageProvider" file
 * should be creating containing a single line with "org.netbeans.lib.lexer.test.TestLanguageProvider".
 * <br/>
 * Then the tests should register their test languages into it.
 *
 * @author Miloslav Metelka
 */
public class TestLanguageProvider extends LanguageProvider {

    private static Map<String, Language<?>> mime2language = new HashMap<String, Language<?>>();

    private static Map<String, Map<TokenId, LanguageEmbedding<?>>> mime2embeddings = new HashMap<String, Map<TokenId, LanguageEmbedding<?>>>();

    private static final Object LOCK = new String("TestLanguageProvider.LOCK");

    public static void register(Language language) {
        register(language.mimeType(), language);
    }

    public static void register(String mimePath, Language language) {
        synchronized (LOCK) {
            mime2language.put(mimePath, language);
        }
        fireChange();
    }

    public static void registerEmbedding(String mimePath, TokenId id, Language<?> language, int startSkipLength, int endSkipLength, boolean joinSections) {
        registerEmbedding(mimePath, id, LanguageEmbedding.create(language, startSkipLength, endSkipLength, joinSections));
    }

    public static void registerEmbedding(String mimePath, TokenId id, LanguageEmbedding<?> embedding) {
        synchronized (LOCK) {
            Map<TokenId, LanguageEmbedding<?>> id2embedding = mime2embeddings.get(mimePath);
            if (id2embedding == null) {
                id2embedding = new HashMap<TokenId, LanguageEmbedding<?>>();
                mime2embeddings.put(mimePath, id2embedding);
            }
            id2embedding.put(id, embedding);
        }
        fireChange();
    }

    public static void fireChange() {
        TestLanguageProvider tlp = Lookup.getDefault().lookup(TestLanguageProvider.class);
        assert tlp != null : "No TestLanguageProvider in default Lookup";
        tlp.firePropertyChange(PROP_LANGUAGE);
        tlp.firePropertyChange(PROP_EMBEDDED_LANGUAGE);
    }

    public TestLanguageProvider() {
    }

    public Language<?> findLanguage(String mimeType) {
        synchronized (LOCK) {
            return mime2language.get(mimeType);
        }
    }

    public LanguageEmbedding<?> findLanguageEmbedding(Token<?> token, LanguagePath languagePath, InputAttributes inputAttributes) {
        Map<TokenId, LanguageEmbedding<?>> id2embedding = mime2embeddings.get(languagePath.mimePath());
        return (id2embedding != null) ? id2embedding.get(token.id()) : null;
    }
}

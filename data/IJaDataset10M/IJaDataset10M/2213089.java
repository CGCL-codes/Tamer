package net.conquiris.qs;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.concurrent.ThreadSafe;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Iterables;

/**
 * QS query dictionary. Used for converting QueryTokens to strings and parsing strings to
 * QueryTokens.
 * @author Andres Rodriguez
 */
@ThreadSafe
public final class QSFactory {

    private static final QSFactory EMPTY = new QSFactory(ImmutableBiMap.<Class<? extends QueryToken>, String>of());

    private static final QSFactory DEFAULT = new Builder(EMPTY).put(QSBoost.class).put(QSTerm.class).build();

    /** Query tokens map. */
    private final ImmutableBiMap<Class<? extends QueryToken>, String> queries;

    public static QSFactory get() {
        return DEFAULT;
    }

    /**
	 * Creates a new builder starting with an existing dictionary.
	 * @param qs Dictionary to use.
	 * @return A new builder.
	 */
    public static Builder builder(QSFactory qs) {
        return new Builder(qs);
    }

    /**
	 * Creates a new builder starting with the default dictionary.
	 * @return A new builder.
	 */
    public static Builder builder() {
        return new Builder(DEFAULT);
    }

    private QueryToken checkQuery(QueryToken query) {
        return checkNotNull(query, "Null query");
    }

    /**
	 * Constructor.
	 * @param queries Query dictionary.
	 */
    private QSFactory(Map<Class<? extends QueryToken>, String> queries) {
        this.queries = ImmutableBiMap.copyOf(queries);
    }

    /**
	 * Returns the query key for a query token.
	 * @param query Query.
	 * @return The requested key.
	 * @throws IllegalArgumentException if the query type is unknown.
	 */
    String getQueryKey(QueryToken query) {
        String key = queries.get(query.getClass());
        checkArgument(key != null, "Unknown query type [%s]", query.getClass().getName());
        return key;
    }

    /**
	 * Returns the query token type for a query key.
	 * @param key Query key.
	 * @return The requested token type.
	 * @throws IllegalArgumentException if the query key is unknown.
	 */
    Class<? extends QueryToken> getQueryToken(String key) {
        Class<? extends QueryToken> query = queries.inverse().get(key);
        checkArgument(key != null, "Unknown query key [%s]", key);
        return query;
    }

    public void write(QueryToken query, Appendable a) throws IOException {
        doWrite(checkQuery(query), a);
    }

    private void doWrite(QueryToken query, Appendable a) throws IOException {
        query.write(true, this, a);
    }

    public String write(QueryToken query) {
        checkQuery(query);
        StringBuilder b = new StringBuilder();
        try {
            write(query, b);
        } catch (IOException e) {
        }
        return b.toString();
    }

    public QueryToken parse(String string) {
        return QSAll.get();
    }

    /**
	 * Builder for QS objects.
	 * @author Andres Rodriguez
	 */
    @NotThreadSafe
    public static class Builder {

        private final QSFactory previous;

        private BiMap<Class<? extends QueryToken>, String> current = null;

        private Builder(QSFactory previous) {
            this.previous = checkNotNull(previous);
        }

        private BiMap<Class<? extends QueryToken>, String> checkMap() {
            return (current != null) ? current : previous.queries;
        }

        private BiMap<Class<? extends QueryToken>, String> workMap() {
            return HashBiMap.create(checkMap());
        }

        private void checkEntry(BiMap<Class<? extends QueryToken>, String> map, Class<? extends QueryToken> queryType, String key) {
            checkNotNull(queryType);
            QSStrings.checkKey(key);
            checkArgument(!map.containsKey(queryType), "Duplicate query type [%s]", queryType.getName());
            checkArgument(!map.containsValue(key), "Duplicate query key [%s]", key);
        }

        private String getQueryKey(Class<? extends QueryToken> queryType) {
            checkNotNull(queryType);
            QueryKey key = queryType.getAnnotation(QueryKey.class);
            checkArgument(key != null, "Query type [%s] has no QueryKey annotation", queryType.getName());
            return key.value();
        }

        /**
		 * Adds a query type to the dictionary.
		 * @param queryType Query type.
		 * @param key Key to use.
		 * @return This builder.
		 * @throws IllegalKeyException if the key contains invalid characters.
		 * @throws IllegalArgumentException if the the key and/or query type are already being used.
		 */
        public Builder put(Class<? extends QueryToken> queryType, String key) {
            checkEntry(checkMap(), queryType, key);
            if (current == null) {
                current = HashBiMap.create(previous.queries);
            }
            current.put(queryType, key);
            return this;
        }

        /**
		 * Adds a query type to the QS dictionary. The key is obtained from the QueryKey annotation.
		 * @param queryType Query type.
		 * @return This builder.
		 * @throws IllegalKeyException if the key contains invalid characters.
		 * @throws IllegalArgumentException if the the key and/or query type are already being used.
		 */
        public Builder put(Class<? extends QueryToken> queryType) {
            return put(queryType, getQueryKey(queryType));
        }

        /**
		 * Adds a collection of query types to the QS dictionary. If an exception is thrown no
		 * modification is made to the dictionary.
		 * @param queries Query types to add. If {@code null} or empty no action is performed.
		 * @return This builder.
		 * @throws IllegalKeyException if any of the key contains invalid characters.
		 * @throws IllegalArgumentException if any of the keys and/or query types are already being
		 *           used.
		 */
        public Builder putAll(@Nullable Map<Class<? extends QueryToken>, String> queries) {
            if (queries == null || queries.isEmpty()) {
                return this;
            }
            BiMap<Class<? extends QueryToken>, String> map = workMap();
            for (Entry<Class<? extends QueryToken>, String> e : queries.entrySet()) {
                checkEntry(map, e.getKey(), e.getValue());
                map.put(e.getKey(), e.getValue());
            }
            current = map;
            return this;
        }

        /**
		 * Adds a collection of query types to the QS dictionary. The keys are obtained from the
		 * QueryKey annotation in each class. If an exception is thrown no modification is made to the
		 * dictionary.
		 * @param queries Query types to add. If {@code null} or empty no action is performed.
		 * @return This builder.
		 * @throws IllegalKeyException if any of the key contains invalid characters.
		 * @throws IllegalArgumentException if any of the keys and/or query types are already being
		 *           used.
		 */
        public Builder putAll(@Nullable Iterable<Class<? extends QueryToken>> queries) {
            if (queries == null || Iterables.isEmpty(queries)) {
                return this;
            }
            BiMap<Class<? extends QueryToken>, String> map = workMap();
            for (Class<? extends QueryToken> type : queries) {
                String k = getQueryKey(type);
                checkEntry(map, type, k);
                map.put(type, k);
            }
            current = map;
            return this;
        }

        /**
		 * Builds the query dictionary.
		 * @return The requested dictionay.
		 */
        public QSFactory build() {
            if (current == null) {
                return previous;
            }
            return new QSFactory(current);
        }
    }
}

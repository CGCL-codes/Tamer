package hacathon.android.travel.util;

import java.net.URI;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class UrlBuilder {

    private String base;

    private String parameterDelimiter = "&";

    private final Map<String, UrlParameter> urlParameters = new LinkedHashMap<String, UrlParameter>() {

        private static final long serialVersionUID = 1L;

        @Override
        public UrlParameter get(final Object key) {
            final UrlParameter value = super.get(key);
            if (value != null) {
                return value;
            }
            final UrlParameter p = new UrlParameter();
            p.setKey((String) key);
            put((String) key, p);
            return p;
        }
    };

    public void setBase(final String base) {
        this.base = base;
    }

    public void setParameterDelimiter(final String delimiter) {
        this.parameterDelimiter = delimiter;
    }

    public String build() {
        final StringBuffer sb = new StringBuffer(100);
        final URI uri = URI.create(base);
        if (uri.getScheme() != null) {
            sb.append(uri.getScheme());
            sb.append(":");
        }
        if (uri.getAuthority() != null) {
            sb.append("//");
            sb.append(uri.getAuthority());
        }
        if (uri.getPath() != null) {
            sb.append(uri.getPath());
        } else if (uri.getSchemeSpecificPart() != null) {
            sb.append(uri.getSchemeSpecificPart());
        }
        boolean questionAppeared = false;
        if (uri.getQuery() != null) {
            questionAppeared = true;
            sb.append('?');
            sb.append(uri.getQuery());
        }
        for (final Iterator<Map.Entry<String, UrlParameter>> it = urlParameters.entrySet().iterator(); it.hasNext(); ) {
            final Map.Entry<String, UrlParameter> entry = it.next();
            final String key = entry.getKey();
            final UrlParameter parameter = entry.getValue();
            final String[] values = parameter.getValues();
            for (int i = 0; i < values.length; i++) {
                final String value = values[i];
                if (questionAppeared) {
                    sb.append(parameterDelimiter);
                } else {
                    sb.append('?');
                    questionAppeared = true;
                }
                sb.append(key);
                sb.append('=');
                sb.append(value);
            }
        }
        if (uri.getFragment() != null) {
            sb.append('#');
            sb.append(uri.getFragment());
        }
        return new String(sb);
    }

    public void add(final String key, final String value) {
        final UrlParameter p = urlParameters.get(key);
        p.addValue(value);
    }

    public final String getBase() {
        return base;
    }

    public final Map<String, UrlParameter> getUrlParameters() {
        return urlParameters;
    }
}

package cpmake;

import java.util.regex.*;

class SearchPath {

    private Pattern m_pattern;

    private String m_path;

    public SearchPath(String pattern, String path) {
        m_pattern = Pattern.compile(pattern);
        m_path = path;
    }

    public boolean matches(String file) {
        return (m_pattern.matcher(file).matches());
    }

    public String getPath() {
        return (m_path);
    }
}

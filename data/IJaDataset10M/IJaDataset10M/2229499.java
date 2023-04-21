package net.sourceforge.basher.internal.impl;

import java.util.Arrays;
import java.util.Properties;

/**
 * @author Johan Lindquist
 */
public class BasherBootConfiguration {

    private String _activeBasherContext;

    private Properties _results;

    private boolean _failIfNoTasks;

    private String[] _includedFiles;

    public String getActiveBasherContext() {
        return _activeBasherContext;
    }

    public void setActiveBasherContext(final String activeBasherContext) {
        _activeBasherContext = activeBasherContext;
    }

    public Properties getResults() {
        return _results;
    }

    public void setResults(final Properties results) {
        _results = results;
    }

    public boolean isFailIfNoTasks() {
        return _failIfNoTasks;
    }

    public void setFailIfNoTasks(final boolean failIfNoTasks) {
        _failIfNoTasks = failIfNoTasks;
    }

    public String[] getIncludedFiles() {
        return _includedFiles;
    }

    public void setIncludedFiles(final String[] includedFiles) {
        this._includedFiles = includedFiles;
    }

    public BasherBootConfiguration(final String activeBasherContext, final Properties results, final boolean failIfNoTasks) {
        _activeBasherContext = activeBasherContext;
        _results = results;
        _failIfNoTasks = failIfNoTasks;
    }

    @Override
    public String toString() {
        return "BasherBootConfiguration{" + "_activeBasherContext='" + _activeBasherContext + '\'' + ", _results=" + _results + ", _failIfNoTasks=" + _failIfNoTasks + ", _includedFiles=" + (_includedFiles == null ? null : Arrays.asList(_includedFiles)) + '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BasherBootConfiguration that = (BasherBootConfiguration) o;
        if (_failIfNoTasks != that._failIfNoTasks) {
            return false;
        }
        if (_activeBasherContext != null ? !_activeBasherContext.equals(that._activeBasherContext) : that._activeBasherContext != null) {
            return false;
        }
        if (!Arrays.equals(_includedFiles, that._includedFiles)) {
            return false;
        }
        if (_results != null ? !_results.equals(that._results) : that._results != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = _activeBasherContext != null ? _activeBasherContext.hashCode() : 0;
        result = 31 * result + (_results != null ? _results.hashCode() : 0);
        result = 31 * result + (_failIfNoTasks ? 1 : 0);
        result = 31 * result + (_includedFiles != null ? Arrays.hashCode(_includedFiles) : 0);
        return result;
    }
}

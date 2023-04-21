package net.metasimian.spelunk;

import net.metasimian.spelunk.analyzer.Analyzer;
import net.metasimian.spelunk.filter.Filter;
import net.metasimian.spelunk.report.Report;
import net.metasimian.spelunk.source.DataSource;

public class Spelunk {

    DataSource dataSource;

    Analyzer analyzer;

    Filter filter;

    public Spelunk(DataSource dataSource, Analyzer analyzer, Filter filter) {
        super();
        this.dataSource = dataSource;
        this.analyzer = analyzer;
        this.filter = filter;
    }

    public void dig() throws Exception {
        dataSource.process(analyzer, filter);
        Report report = analyzer.getReport();
        report.closeReport();
    }
}

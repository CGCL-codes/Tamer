package org.eclipse.babel.build.core.reports;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import org.eclipse.babel.build.core.LocaleProxy;
import org.eclipse.babel.build.core.coverage.LanguagePackCoverageReport;
import org.eclipse.babel.build.core.coverage.PluginCoverageInformation;
import org.eclipse.babel.build.core.coverage.ResourceCoverageInformation;

public class TextCoverageReport implements CoverageReport {

    private final LanguagePackCoverageReport coverage;

    public TextCoverageReport(LanguagePackCoverageReport coverage) {
        this.coverage = coverage;
    }

    public void render(OutputStream stream) throws Exception {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));
        writer.write("Number of total plugins: " + coverage.getPluginCoverageReports().size());
        writer.newLine();
        for (LocaleProxy locale : coverage.getMatchesPerLocale().keySet()) {
            writer.write("Number of matched plugins for " + locale.getName() + ": " + coverage.getMatchesPerLocale().get(locale));
            writer.newLine();
        }
        for (PluginCoverageInformation pluginReport : coverage.getPluginCoverageReports()) {
            writer.newLine();
            writer.write(pluginReport.getEclipseArchivePlugin().getName());
            writer.newLine();
            for (LocaleProxy locale : pluginReport.getPluginMatchingPerLocale().keySet()) {
                writer.write(locale.getName() + " -> " + pluginReport.getPluginMatchingPerLocale().get(locale));
                writer.newLine();
            }
            for (String resourcePath : pluginReport.getResourceCoverage().keySet()) {
                ResourceCoverageInformation resourceCoverageInfo = pluginReport.getResourceCoverage().get(resourcePath);
                writer.write(resourcePath);
                writer.newLine();
                for (LocaleProxy locale : resourceCoverageInfo.getRecordedLocales()) {
                    writer.write("Locale " + locale.getName() + ": " + resourceCoverageInfo.getMatchingForLocale(locale));
                    writer.newLine();
                    writer.write("Locale property coverage " + locale.getName() + ": " + resourceCoverageInfo.getMatchedPercentageForLocale(locale));
                    writer.newLine();
                }
            }
        }
    }
}

package samples;

import org.punit.reporter.chart.OverviewReporter;
import org.punit.reporter.chart.image.ImageRender;
import org.punit.reporter.chart.pdf.PDFRender;
import org.punit.reporter.stream.file.FileLogger;
import org.punit.runner.ExecutorPoolImpl;
import org.punit.runner.SoloRunner;

public class RunnerSamples {

    public static void main(String[] args) {
        SoloRunner runner = new SoloRunner();
        runner.setExecutorPool(new ExecutorPoolImpl(5));
        runner.addEventListener(new FileLogger());
        runner.addEventListener(new OverviewReporter(new ImageRender()));
        runner.addEventListener(new OverviewReporter(new PDFRender()));
        runner.run(AllTestSuite.class);
    }
}

package uk.co.cocking.getinline2.builder.test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static uk.co.cocking.getinline2.testhelpers.FileTesting.*;
import org.junit.Test;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;
import uk.co.cocking.getinline2.Runner;
import uk.co.cocking.getinline2.builder.Builder;
import uk.co.cocking.getinline2.pipeline.Consumer;
import uk.co.cocking.getinline2.pipeline.io.StringLineWriter;
import uk.co.cocking.getinline2.testhelpers.RssTestDataGenerator;

public class BuilderTest {

    Server server;

    @Test
    public void assemblesAStringProcessingPipeline() {
        StringLineWriter stringLineWriter = new StringLineWriter();
        Consumer<String> sink = Builder.SCB.stringLineReader().stringFilter(containsString("bar")).stringLineWriter(stringLineWriter).build();
        Runner<String> runner = new Runner<String>(sink);
        runner.run("foo\nbar");
        String result = stringLineWriter.toString();
        assertThat(result, not(containsString("foo\n")));
        assertThat(result, containsString("bar\n"));
    }

    @Test
    public void assemblesAMixedProcessingPipeline() {
        StringLineWriter stringLineWriter = new StringLineWriter();
        Consumer<String> sink = Builder.SCB.stringLineReader().stringFilter(equalTo("bar")).stringWrapper("wrappingField").formatAsCsv().stringLineWriter(stringLineWriter).build();
        Runner<String> runner = new Runner<String>(sink);
        runner.run("foo\nbar");
        String result = stringLineWriter.toString();
        assertThat(result, not(containsString("foo")));
        assertThat(result, containsString("\"bar\"\n"));
    }

    @Test
    public void canBuildAFieldExtractor() {
        StringLineWriter stringLineWriter = new StringLineWriter();
        Consumer<String> sink = Builder.SCB.stringLineReader().stringWrapper("wrappingField").fieldExtractor("wrappingField").stringLineWriter(stringLineWriter).build();
        Runner<String> runner = new Runner<String>(sink);
        runner.run("foo\nbar");
        String result = stringLineWriter.toString();
        assertThat(result, containsString("foo\n"));
        assertThat(result, containsString("bar\n"));
    }

    @Test
    public void canBuildAFieldFilter() {
        StringLineWriter stringLineWriter = new StringLineWriter();
        Consumer<String> sink = Builder.SCB.stringLineReader().stringWrapper("wrappingField").selectIfField("wrappingField", equalTo("bar")).fieldExtractor("wrappingField").stringLineWriter(stringLineWriter).build();
        Runner<String> runner = new Runner<String>(sink);
        runner.run("foo\nbar");
        String result = stringLineWriter.toString();
        assertThat(result, not(containsString("foo\n")));
        assertThat(result, containsString("bar\n"));
    }

    @Test
    public void canBuildAnRssReader() {
        StringLineWriter stringLineWriter = new StringLineWriter();
        Consumer<String> sink = Builder.SCB.stringLineReader().readRssItems().formatAsCsv().stringLineWriter(stringLineWriter).build();
        Runner<String> runner = new Runner<String>(sink);
        runner.run(new RssTestDataGenerator().rssFeedString());
        String result = stringLineWriter.toString();
        assertThat(result, containsString("\"Star City\""));
    }

    @Test
    public void canBuildAFileReader() {
        StringLineWriter stringLineWriter = new StringLineWriter();
        Consumer<String> sink = Builder.SCB.fileReader().stringLineWriter(stringLineWriter).build();
        Runner<String> runner = new Runner<String>(sink);
        runner.run("test/foo.txt");
        String result = stringLineWriter.toString();
        assertThat(result, containsString("one\n"));
        assertThat(result, containsString("four\n"));
    }

    @Test
    public void canBuildAFileWriter() {
        String outputFileName = "test/out.txt";
        final File outputFile = new File(outputFileName);
        delete(outputFile);
        assertThat(outputFile, not(exists()));
        Consumer<String> sink = Builder.SCB.stringLineReader().writeToFile(outputFileName).build();
        Runner<String> runner = new Runner<String>(sink);
        runner.run("foo\nbar");
        assertThat(outputFile, exists());
        assertThat(outputFile, contains("foo\n", "bar\n"));
    }

    @Test
    public void canBuildAUrlRetriever() throws Exception {
        StringLineWriter stringLineWriter = new StringLineWriter();
        publishHTML();
        Consumer<String> sink = Builder.SCB.retrieveUrls().stringLineWriter(stringLineWriter).build();
        Runner<String> runner = new Runner<String>(sink);
        runner.run("http://localhost:8123");
        stopJetty();
        assertThat(stringLineWriter.toString(), containsString("<html/>"));
    }

    private void publishHTML() throws Exception {
        Handler handler = new AbstractHandler() {

            public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException, ServletException {
                response.setContentType("text/html");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().print("<html/>");
                ((Request) request).setHandled(true);
            }
        };
        server = new Server(8123);
        server.setHandler(handler);
        server.start();
    }

    private void stopJetty() throws Exception {
        server.stop();
    }
}

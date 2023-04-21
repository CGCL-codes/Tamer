package de.andreavicentini.magicphoto.batch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.magiclabs.basix.Functions;
import org.magiclabs.basix.IClosure;
import org.magiclabs.basix.IRenderer;
import org.magiclabs.basix.MagicString;
import org.magiclabs.basix.Magics;
import de.andreavicentini.magicphoto.domain.pictures.IDescription;
import de.andreavicentini.magicphoto.domain.pictures.IPicture;
import de.andreavicentini.visions.Execution;
import de.andreavicentini.visions.Execution.IActivity;
import de.andreavicentini.visions.Execution.IContext;

public class MagicDescription {

    class FilesIterator {

        private final File root;

        public FilesIterator(File root) {
            this.root = root;
        }

        public void iterate(IClosure<File> operation) {
            iterate(this.root, operation);
        }

        private void iterate(File file, final IClosure<File> operation) {
            operation.process(file);
            if (!file.isDirectory()) return;
            Magics.each(file.listFiles(), new IClosure<File>() {

                public void process(File object) {
                    iterate(object, operation);
                }
            });
        }
    }

    class FilterByName implements IActivity<File, File> {

        private final String name;

        public FilterByName(String name) {
            this.name = name;
        }

        public File process(IContext<File, File> ctx) throws Exception {
            File file = ctx.getValue();
            if (file.getName().equals(this.name)) ctx.proceed();
            return null;
        }
    }

    class FileRemover implements IActivity<File, File> {

        private final String name;

        public FileRemover(String name) {
            this.name = name;
        }

        public File process(IContext<File, File> ctx) throws Exception {
            File fNormal = ctx.getValue();
            File fOrig = new File(fNormal.getParentFile(), this.name);
            fOrig.delete();
            return ctx.proceed();
        }
    }

    class FileRenamer implements IActivity<File, File> {

        private final String original;

        public FileRenamer(String original) {
            this.original = original;
        }

        public File process(IContext<File, File> ctx) throws Exception {
            File fNormal = ctx.getValue();
            File fOrig = new File(fNormal.getParentFile(), this.original);
            fNormal.renameTo(fOrig);
            return ctx.proceed();
        }
    }

    class DescriptionProcessor implements IActivity<File, File> {

        private final String input;

        private final Execution<String, String> tuners;

        public DescriptionProcessor(String input, Execution<String, String> tuners) {
            this.input = input;
            this.tuners = tuners;
        }

        public File process(IContext<File, File> ctx) throws Exception {
            File fOutput = ctx.getValue();
            File fInput = new File(fOutput.getParent(), this.input);
            final LineWriter w = new LineWriter(fOutput);
            LineReader r = new LineReader(fInput);
            r.read(new IClosure<String>() {

                public void process(String line) {
                    w.process(tuners.execute(line));
                }
            });
            r.close();
            w.close();
            return null;
        }
    }

    class FakeDescription implements IDescription {

        public String text;

        public List<Keyword> keywords = new ArrayList<Keyword>();

        public String getFulllengthText() {
            Keyword[] keywords = this.listKeywords();
            return new MagicString().append(this.text, new IRenderer.NotNull<Object>(new IRenderer.Default(), "")).append(" ").append(keywords, new IRenderer.Default(), " | ", " | ", "").toString();
        }

        public String getLocation() {
            return null;
        }

        public String getText() {
            return null;
        }

        public Keyword[] listKeywords() {
            return this.keywords.toArray(new Keyword[0]);
        }

        public boolean matches(String token) {
            return false;
        }

        public void updateWith(String key, String value) {
            if (key.endsWith(".desc")) {
                this.text = value;
                return;
            }
            if (key.endsWith(".keywords")) {
                Functions.build(this.keywords, value.split(" "), new Functions.IBuilder<List<Keyword>, String>() {

                    public void build(List<Keyword> result, String element) {
                        element = element.trim();
                        if (element.equals("")) return;
                        result.add(new Keyword(element));
                    }
                });
                return;
            }
        }
    }

    class FakePicture implements IPicture {

        private final FakeDescription description = new FakeDescription();

        private final String name;

        public FakePicture(String name) {
            this.name = name;
        }

        public Date getCreationDate() {
            return null;
        }

        public IDescription getDescription() {
            return this.description;
        }

        public long getDimension() {
            return 0;
        }

        public File getFile() {
            return null;
        }

        public void updateWith(String key, String value) {
            this.description.updateWith(key, value);
        }
    }

    class PropertiesIterator extends HashMap<String, FakePicture> {

        public PropertiesIterator(File input) throws IOException {
            Properties properties = new Properties();
            InputStreamReader r = new InputStreamReader(new FileInputStream(input), Charset.forName("ISO-8859-1"));
            properties.load(r);
            r.close();
            for (Iterator i = properties.keySet().iterator(); i.hasNext(); ) {
                String key = (String) i.next();
                String value = properties.getProperty(key);
                String name = extractName(key);
                FakePicture picture = this.get(name);
                if (picture == null) {
                    picture = new FakePicture(name);
                    this.put(name, picture);
                }
                picture.updateWith(key, value);
            }
        }

        private String extractName(String key) {
            return key.substring(0, key.lastIndexOf('.'));
        }

        public void iterate(IClosure<String> closure) {
            for (String key : this.keySet()) {
                closure.process(quote(key) + " " + this.get(key).getDescription().getFulllengthText());
            }
        }

        private String quote(String name) {
            return (name.indexOf(' ') == -1) ? name : '"' + name + '"';
        }
    }

    class DescriptionCreator implements IActivity<File, File> {

        private final String input;

        public DescriptionCreator(String input) {
            this.input = input;
        }

        public File process(IContext<File, File> ctx) throws Exception {
            File fOutput = ctx.getValue();
            File fInput = new File(fOutput.getParent(), this.input);
            PropertiesIterator properties = new PropertiesIterator(fInput);
            final LineWriter w = new LineWriter(fOutput);
            properties.iterate(w);
            w.close();
            return null;
        }
    }

    class LineWriter implements IClosure<String> {

        private final PrintStream writer;

        public LineWriter(File output) throws IOException {
            this.writer = new PrintStream(new FileOutputStream(output), true, "windows-1252");
        }

        public void close() throws IOException {
            this.writer.close();
        }

        public void process(String line) {
            this.writer.println(line);
        }
    }

    class LineReader {

        private final File input;

        public LineReader(File input) {
            this.input = input;
        }

        public void close() {
        }

        public void read(IClosure<String> processor) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.input), Charset.forName("windows-1252")));
            String line;
            while ((line = reader.readLine()) != null) processor.process(line);
            reader.close();
        }
    }

    class CheckClick implements IActivity<String, String> {

        public String process(IContext<String, String> ctx) throws Exception {
            String line = ctx.getValue();
            if (line.startsWith("g,") && line.indexOf("click") == -1) {
                System.out.println("adding click to: " + line);
                ctx.setValue(line + " | click");
            }
            return ctx.proceed();
        }
    }

    class CheckNo implements IActivity<String, String> {

        public String process(IContext<String, String> ctx) throws Exception {
            String line = ctx.getValue();
            if (line.startsWith("x,") && line.indexOf("no") == -1) {
                System.out.println("adding no to: " + line);
                ctx.setValue(line + " | no");
            }
            return ctx.proceed();
        }
    }

    interface Descriptions {

        String ORIGINAL = "descript.ion_ORIGINAL";

        String NORMAL = "descript.ion";

        String IMAGE_DESCRIPTION = "image-descriptors.properties";
    }

    public static void main(String args[]) {
        new MagicDescription().run(new File(args[0]));
    }

    public void run(File directory) {
        final Execution<String, String> tuners = new Execution<String, String>().add(new CheckClick()).add(new CheckNo());
        final Execution<File, File> e = new Execution<File, File>().add(new FileRemover(Descriptions.ORIGINAL)).add(new FileRenamer(Descriptions.ORIGINAL)).add(new DescriptionCreator(Descriptions.IMAGE_DESCRIPTION));
        e.execute(new File(directory, Descriptions.NORMAL));
    }
}

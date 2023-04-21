package jorgan.skin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import jorgan.disposition.Console;
import jorgan.io.SkinStream;
import jorgan.problem.ElementProblems;
import jorgan.problem.Problem;
import jorgan.problem.Severity;
import jorgan.swing.ImageCache;
import jorgan.util.IOUtils;
import bias.Configuration;
import bias.util.MessageBuilder;

/**
 * Manager of skins.
 */
public abstract class SkinManager {

    private static Configuration config = Configuration.getRoot().get(SkinManager.class);

    private static final String SKIN_FILE = "skin.xml";

    private Map<String, Skin> skins = new HashMap<String, Skin>();

    private ElementProblems problems;

    private boolean flushImagesOnClose;

    public SkinManager(ElementProblems problems) {
        this.problems = problems;
        config.read(this);
    }

    public void setFlushImagesOnClose(boolean flushImages) {
        this.flushImagesOnClose = flushImages;
    }

    public Skin getSkin(Console console) {
        if (console == null) {
            throw new IllegalArgumentException("console must not be null");
        }
        problems.removeProblem(new Problem(Severity.ERROR, console, "skin", null));
        if (console.getSkin() == null) {
            return null;
        }
        Skin skin = skins.get(console.getSkin());
        if (skin != null) {
            return skin;
        }
        try {
            skin = loadSkin(resolve(console.getSkin()));
            skins.put(console.getSkin(), skin);
        } catch (IOException e) {
            problems.addProblem(new Problem(Severity.ERROR, console, "skin", createMessage("skinLoad", console.getSkin())));
        }
        return skin;
    }

    protected abstract File resolve(String skin) throws IOException;

    protected String createMessage(String key, Object... args) {
        MessageBuilder builder = new MessageBuilder();
        return config.get(key).read(builder).build(args);
    }

    private Skin loadSkin(File file) throws IOException {
        Skin skin;
        Resolver resolver = createSkinDirectory(file);
        if (resolver == null) {
            resolver = createSkinZip(file);
            if (resolver == null) {
                throw new IOException("no skin");
            }
        }
        URL skinFile = resolver.resolve(SKIN_FILE);
        if (skinFile == null) {
            throw new IOException("missing skin file");
        }
        InputStream input = skinFile.openStream();
        try {
            skin = new SkinStream().read(input);
            skin.setResolver(resolver);
        } finally {
            IOUtils.closeQuietly(input);
        }
        return skin;
    }

    private Resolver createSkinDirectory(File file) {
        if (file.isDirectory()) {
            return new SkinDirectory(file);
        }
        return null;
    }

    private Resolver createSkinZip(File file) {
        try {
            return new SkinZip(file);
        } catch (IOException e) {
        }
        return null;
    }

    /**
	 * A source of a skin contained in a directory.
	 */
    private class SkinDirectory implements Resolver {

        private File directory;

        private SkinDirectory(File directory) {
            this.directory = directory;
        }

        public URL resolve(String name) {
            try {
                File file = new File(directory, name);
                if (file.exists()) {
                    return file.toURI().toURL();
                }
            } catch (MalformedURLException ex) {
            }
            return null;
        }
    }

    /**
	 * A source of a skin contained in a zipFile.
	 */
    private class SkinZip implements Resolver {

        private File file;

        private ZipFile zipFile;

        private SkinZip(File file) throws IOException {
            this.file = file;
            this.zipFile = new ZipFile(file);
        }

        public URL resolve(String name) {
            ZipEntry entry = zipFile.getEntry(name);
            if (entry != null) {
                try {
                    return new URL("jar:" + file.toURI().toURL() + "!/" + name);
                } catch (MalformedURLException ex) {
                }
            }
            return null;
        }
    }

    public void destroy() {
        if (flushImagesOnClose) {
            ImageCache.flush();
        }
    }
}

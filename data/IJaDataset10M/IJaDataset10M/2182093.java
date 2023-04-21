package de.grogra.pf.io;

import java.io.*;
import de.grogra.vfs.*;
import de.grogra.pf.registry.*;
import java.util.jar.Manifest;

public class GSReader extends FilterBase implements ObjectSource, ProjectLoader {

    public GSReader(FilterItem item, FilterSource source) {
        super(item, source);
        setFlavor(IOFlavor.PROJECT_LOADER);
    }

    private LocalFileSystem fs;

    private File project;

    public Object getObject() throws IOException {
        project = ((FileSource) source).getInputFile();
        fs = new LocalFileSystem(IO.PROJECT_FS, project.getParentFile());
        return this;
    }

    public void loadRegistry(Registry r) throws IOException {
        r.initFileSystem(fs);
        File m = new File(new File(project.getParentFile(), "META-INF"), "MANIFEST.MF");
        if (m.isFile()) {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(m));
            Manifest mf;
            try {
                mf = new Manifest(in);
            } finally {
                in.close();
            }
            fs.setManifest(mf);
        }
        FilterSource f = new FileSource(fs, project, IO.toSystemId(fs, project), Registry.MIME_TYPE, r, null);
        f = IO.createPipeline(f, IOFlavor.REGISTRY_LOADER);
        if (!(f instanceof ObjectSource)) {
            throw new AssertionError("gs-pipeline = " + f);
        }
        ((RegistryLoader) ((ObjectSource) f).getObject()).loadRegistry(r);
    }

    public void loadGraph(Registry r) throws IOException {
        loadGraph(fs, r);
    }

    static void loadGraph(FileSystem fs, Registry r) throws IOException {
        String gfName = (String) r.getImportAttribute("graph");
        if (gfName == null) {
            throw new IOException(IO.I18N.msg("gs.graphfile-not-specified"));
        }
        Object gf = fs.getFile(gfName);
        if (gf == null) {
            throw new IOException(IO.I18N.msg("gs.file-not-found", gfName));
        }
        FilterSource f = new FileSource(fs, gf, r, null);
        f = IO.createPipeline(f, IOFlavor.GRAPH_LOADER);
        if (!(f instanceof ObjectSource)) {
            throw new IOException(IO.I18N.msg("gs.invalid-file-type", gfName));
        }
        ((GraphLoader) ((ObjectSource) f).getObject()).loadGraph(r);
        r.forAll(null, null, new ItemVisitor() {

            public void visit(Item item, Object info) {
                if (!item.validate()) {
                    System.err.println("Removed " + item);
                    item.removeFromChain();
                }
            }
        }, null, false);
    }
}

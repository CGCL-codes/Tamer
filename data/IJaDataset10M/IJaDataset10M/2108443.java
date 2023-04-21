package as.ide.core.common;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.osgi.framework.Bundle;
import as.ide.core.dom.BlockDef;
import as.ide.core.dom.BlockEntry;
import as.ide.core.dom.CompilationUnit;
import as.ide.core.dom.MethodDef;
import as.ide.core.dom.PackageBlock;
import as.ide.core.dom.PackageDecl;
import as.ide.core.dom.QualifiedName;
import as.ide.core.dom.VariableDef;
import as.ide.core.parser.ASParser;
import as.ide.core.parser.ParserFactory;
import as.ide.core.utils.Log;

public class SyntaxTree {

    /** for internal library, shared by all syntax tree. map a package name to its PackageNode */
    private static final HashMap<String, PackageNode> internalPackageNodeMap = new HashMap<String, PackageNode>();

    /** for internal library, shared by all syntax tree. map a block name to its package name */
    private static final HashMap<String, ArrayList<String>> internalBlockPackageNameMap = new HashMap<String, ArrayList<String>>();

    /** map a package name to its PackageNode */
    private HashMap<String, PackageNode> packageNodeMap;

    /** map a block name to its package name */
    private HashMap<String, ArrayList<String>> blockPackageNameMap;

    private boolean loaded = false;

    private ASProjectConfiguration cfg;

    private int workCount;

    private int workLoad = 500000;

    private static final Object lock = new Object();

    public SyntaxTree(ASProjectConfiguration cfg) {
        this.cfg = cfg;
        packageNodeMap = new HashMap<String, PackageNode>();
        blockPackageNameMap = new HashMap<String, ArrayList<String>>();
    }

    public void load() {
        synchronized (lock) {
            if (loaded) return;
            loaded = true;
        }
        new Job("Build Library") {

            @Override
            protected IStatus run(IProgressMonitor monitor) {
                loadLibraries(monitor);
                return Status.OK_STATUS;
            }
        }.schedule(100);
    }

    private void loadLibraries(IProgressMonitor monitor) {
        workCount = 0;
        monitor.beginTask("Building libraries...", workLoad);
        try {
            if (internalPackageNodeMap.size() == 0) {
                Bundle bundle = as.ide.core.Activator.getDefault().getBundle();
                URL url = bundle.getEntry("/lib/flex3");
                if (url == null) {
                    Log.error("Failed to load the library in /lib/flex3", null);
                }
                build(url, monitor);
            }
            monitor.setTaskName("Building external sources...");
            String[] paths = cfg.getPathElements();
            if (paths != null) {
                for (String path : paths) {
                    build(new File(path), monitor, false);
                }
            }
            monitor.setTaskName("Building sources...");
            String srcPath = cfg.getSourceFolderPath();
            if (srcPath != null) {
                build(new File(srcPath), monitor, false);
            }
        } catch (Exception e) {
            Log.error(e.getLocalizedMessage(), e);
        } finally {
            loaded = true;
            monitor.done();
        }
    }

    public boolean hasLoaded() {
        return this.loaded;
    }

    public void reload(IProgressMonitor monitor) {
        clean();
        load();
    }

    public void clean() {
        loaded = false;
        packageNodeMap.clear();
        blockPackageNameMap.clear();
    }

    /**
	 * Build the library
	 * @param url the url to the library
	 * @param monitor
	 * @return
	 */
    private void build(URL url, IProgressMonitor monitor) {
        try {
            url = FileLocator.toFileURL(url);
            File file = new File(url.toURI());
            build(file, monitor, true);
        } catch (Exception e) {
            Log.error(e.getLocalizedMessage(), e);
        }
    }

    private void build(File file, IProgressMonitor monitor, boolean internal) {
        if (monitor.isCanceled()) return;
        if (!file.exists()) return;
        if (file.isFile()) {
            if (file.getName().endsWith(".as")) {
                parseFile(file, internal);
                int val = workCount++;
                if (val > workLoad) val = workLoad;
                monitor.worked(val);
            }
        } else {
            File[] fs = file.listFiles();
            for (File f : fs) {
                build(f, monitor, internal);
            }
        }
    }

    private void parseFile(File file, boolean internal) {
        try {
            ASParser parser = ParserFactory.getTempParser(file);
            parser.doParse();
            addCompilationUnit(parser.getCompilationUnit(), internal);
        } catch (Exception e) {
            Log.error(file.getAbsolutePath(), e);
        }
    }

    private void addCompilationUnit(CompilationUnit cu, boolean internal) {
        if (cu == null) return;
        PackageDecl pkg = cu.getPackage();
        if (pkg == null) return;
        HashMap<String, PackageNode> map;
        if (internal) {
            map = internalPackageNodeMap;
        } else {
            map = packageNodeMap;
        }
        String name = pkg.getName();
        PackageNode pkgNode = map.get(name);
        if (pkgNode == null) {
            pkgNode = new PackageNodeImpl();
            map.put(name, pkgNode);
        }
        ((PackageNodeImpl) pkgNode).setPackage(pkg, internal);
        ((PackageNodeImpl) pkgNode).setBlockEntry(cu.getBlockEntry());
    }

    public void addCompilationUnit(CompilationUnit cu) {
        this.addCompilationUnit(cu, false);
    }

    /**
	 * Get all the package names
	 * @return
	 */
    public String[] getPackageNames() {
        String[] names1 = internalPackageNodeMap.keySet().toArray(new String[0]);
        String[] names2 = packageNodeMap.keySet().toArray(new String[0]);
        return combine(names1, names2);
    }

    /**
	 * Get all the block names
	 * @return
	 */
    public String[] getBlockNames() {
        String[] names1 = internalBlockPackageNameMap.keySet().toArray(new String[0]);
        String[] names2 = blockPackageNameMap.keySet().toArray(new String[0]);
        return combine(names1, names2);
    }

    private String[] combine(String[] names1, String[] names2) {
        int len1 = names1.length;
        int len2 = names2.length;
        String[] names = new String[len1 + len2];
        System.arraycopy(names1, 0, names, 0, len1);
        System.arraycopy(names2, 0, names, len1, len2);
        return names;
    }

    /**
	 * Get the PackageNode of the given package specified by its name.
	 * the PackageNode includes all the classes in the package but NOT 
	 * the possible sub-packages in the package
	 */
    public PackageNode getPackage(String packageName) {
        PackageNode node = internalPackageNodeMap.get(packageName);
        if (node != null) {
            return node;
        }
        return packageNodeMap.get(packageName);
    }

    private boolean validTypeName(String typeName) {
        if (typeName == null) return false;
        if (typeName.trim().length() == 0) return false;
        if (typeName.equals("*")) return false;
        if (typeName.toLowerCase().equals("void")) return false;
        return true;
    }

    private BlockDef resolve(String pkgName, String typeName) {
        if (!validTypeName(typeName)) {
            return null;
        }
        PackageNode pkg = internalPackageNodeMap.get(pkgName);
        if (pkg != null) {
            BlockDef blk = pkg.getBlock(typeName);
            if (blk != null) return blk;
        }
        pkg = packageNodeMap.get(pkgName);
        if (pkg != null) {
            BlockDef blk = pkg.getBlock(typeName);
            if (blk != null) return blk;
        }
        if (pkgName == null || pkgName.trim().length() == 0) {
            ArrayList<String> list = internalBlockPackageNameMap.get(typeName);
            if (list != null && list.size() > 0) {
                pkgName = list.get(0);
                BlockDef blk = internalPackageNodeMap.get(pkgName).getBlock(typeName);
                if (blk != null) {
                    return blk;
                }
            }
            list = blockPackageNameMap.get(typeName);
            if (list != null && list.size() > 0) {
                pkgName = list.get(0);
                return packageNodeMap.get(pkgName).getBlock(typeName);
            }
        }
        return null;
    }

    /**
	 * Resolve the Block definition given the type name. If the type name is qualified,
	 * the block definition will return if it exists; If the type name only contains the block name,
	 * the first block definition will be found and return if there are one or more blocks with
	 * that name.
	 * 
	 * @param typeName the name of the block
	 * @return null if not find, or if type == * or VOID; 
	 * 		Otherwise the first block definition of the given name
	 */
    public BlockDef resolveBlockDefByName(String typeName) {
        return resolve(null, typeName);
    }

    /**
	 * Resolve the block definition of the given QualifiedName
	 * @param qName
	 * @return null if not find, or the first block definition with the given name
	 */
    public BlockDef resolveBlockDefByName(QualifiedName qName) {
        if (qName == null) return null;
        return resolve(qName.getQualifier(), qName.getName());
    }

    /**
	 * Resolve the block definition with the given package name and block name
	 * @param pkgName
	 * @param blockName
	 * @return null if not find.
	 */
    public BlockDef resolveBlockDefByName(String pkgName, String blockName) {
        return resolve(pkgName, blockName);
    }

    /**
	 * Resolve all the block definitions of the given type name. Blocks with
	 * the same name may be defined in different packages
	 * @param typeName
	 * @return
	 */
    public BlockDef[] resolveAllBlockDefsByName(String typeName) {
        if (!validTypeName(typeName)) {
            return null;
        }
        try {
            ArrayList<BlockDef> list = new ArrayList<BlockDef>();
            ArrayList<String> listX = internalBlockPackageNameMap.get(typeName);
            if (listX != null) {
                for (String pkgName : listX) {
                    BlockDef blk = internalPackageNodeMap.get(pkgName).getBlock(typeName);
                    list.add(blk);
                }
            }
            ArrayList<String> listY = blockPackageNameMap.get(typeName);
            if (listY != null) {
                for (String pkgName : listY) {
                    BlockDef blk = packageNodeMap.get(pkgName).getBlock(typeName);
                    list.add(blk);
                }
            }
            if (list.size() > 0) {
                return list.toArray(new BlockDef[0]);
            }
        } catch (NullPointerException e) {
        }
        return null;
    }

    private class PackageNodeImpl implements PackageNode {

        /** Map the name and the block defined in this package block */
        private HashMap<String, BlockDef> blockMap;

        private HashMap<String, VariableDef> variableMap;

        private HashMap<String, MethodDef> methodMap;

        public PackageNodeImpl() {
            blockMap = new HashMap<String, BlockDef>();
        }

        void setPackage(PackageDecl pkg, boolean internal) {
            if (pkg == null) return;
            PackageBlock pkgBlk = pkg.getPkgBlock();
            if (pkgBlk != null) {
                String pkgName = pkg.getName();
                BlockEntry entry = pkgBlk.getBlockEntry();
                if (entry != null) {
                    addBlocks(entry.getClassDefs(), pkgName, internal);
                    addBlocks(entry.getInterfaces(), pkgName, internal);
                }
            }
        }

        void setBlockEntry(BlockEntry entry) {
            if (entry != null) {
                addVariables(entry);
                addMethods(entry);
            }
        }

        private void addBlocks(BlockDef[] blks, String pkgName, boolean internal) {
            if (blks != null) {
                for (BlockDef blk : blks) {
                    String name = blk.getName();
                    blockMap.put(name, blk);
                    HashMap<String, ArrayList<String>> map;
                    if (internal) {
                        map = internalBlockPackageNameMap;
                    } else {
                        map = blockPackageNameMap;
                    }
                    ArrayList<String> list = map.get(name);
                    if (list == null) {
                        list = new ArrayList<String>();
                        map.put(name, list);
                    }
                    if (!list.contains(pkgName)) {
                        list.add(pkgName);
                    }
                }
            }
        }

        private void addMethods(BlockEntry entry) {
            MethodDef[] mtds = entry.getMethods();
            if (mtds != null && mtds.length > 0) {
                if (methodMap == null) {
                    methodMap = new HashMap<String, MethodDef>();
                }
                for (MethodDef mtd : mtds) {
                    if (mtd.isPrivate()) continue;
                    String name = mtd.getName();
                    this.methodMap.put(name, mtd);
                }
            }
        }

        private void addVariables(BlockEntry entry) {
            VariableDef[] vrbs = entry.getVariables();
            if (vrbs != null && vrbs.length > 0) {
                if (variableMap == null) {
                    variableMap = new HashMap<String, VariableDef>();
                }
                for (VariableDef vrb : vrbs) {
                    if (vrb.isPrivate()) continue;
                    String name = vrb.getName();
                    variableMap.put(name, vrb);
                }
            }
        }

        public BlockDef getBlock(String blockName) {
            return blockMap.get(blockName);
        }

        public String[] getBlockNames() {
            return blockMap.keySet().toArray(new String[0]);
        }

        public String[] getVariableNames() {
            if (variableMap == null) return null;
            return variableMap.keySet().toArray(new String[0]);
        }

        public String[] getMethodNames() {
            if (methodMap == null) return null;
            return methodMap.keySet().toArray(new String[0]);
        }

        public VariableDef getVariable(String vrbName) {
            if (variableMap == null) return null;
            return variableMap.get(vrbName);
        }

        public MethodDef getMethod(String mtdName) {
            if (methodMap == null) return null;
            return methodMap.get(mtdName);
        }
    }
}

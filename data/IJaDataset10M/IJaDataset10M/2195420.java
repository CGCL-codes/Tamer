package net.sf.joafip.meminspector.service.inspect;

import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.file.service.FileOutputStreamNIO;
import net.sf.joafip.meminspector.ExcludedClass;
import net.sf.joafip.meminspector.entity.IncludedExcluded;
import net.sf.joafip.meminspector.entity.MemoryImage;
import net.sf.joafip.meminspector.entity.NodeForObject;
import net.sf.joafip.meminspector.entity.NodeForObjectTO;
import net.sf.joafip.meminspector.service.MemInspectorException;
import com.thoughtworks.xstream.XStream;

/**
 * 
 * @author luc peuvrier
 * 
 */
@ExcludedClass
@NotStorableClass
public final class MemInspector {

    private static final InstanceVisitor instanceVisitor = InstanceVisitor.getInstance();

    private final TreeForObjectManager treeForObjectManager = new TreeForObjectManager();

    private final IncludedExcluded includedExcluded = new IncludedExcluded();

    private DiffVisitor diffVisitor;

    private int pad;

    private MemoryImage currentMemoryImage;

    /** only to force class load */
    @SuppressWarnings("unused")
    private static final MemoryImage IMAGE = new MemoryImage(null, null, null);

    public void setMainPackageName(final String mainPackageName) {
        includedExcluded.setMainPackageName(mainPackageName);
    }

    public void setMainPackageName(final String[] mainPackagesName) {
        includedExcluded.setMainPackageName(mainPackagesName);
    }

    public void setExcludedMainPackagesName(final String[] excludedMainPackagesName) {
        includedExcluded.setExcludedMainPackagesName(excludedMainPackagesName);
    }

    public void addExludedField(final String declaringClassName, final String fieldName) {
        includedExcluded.addExludedField(declaringClassName, fieldName);
    }

    public void addExcludedField(final Class<?> declaringClass, final String fieldName) {
        includedExcluded.addExcludedField(declaringClass, fieldName);
    }

    public void addDoNotInspectClass(final String[] doNotInspectClassesName) {
        includedExcluded.addDoNotInspectClass(doNotInspectClassesName);
    }

    public void addDoNotInspectClass(final Class<?> doNotInspectClass) {
        includedExcluded.addDoNotInspectClass(doNotInspectClass);
    }

    public void addDoNotInspectClass(final Class<?>[] doNotInspectClasses) {
        includedExcluded.addDoNotInspectClass(doNotInspectClasses);
    }

    public void addDoNotInspectClass(final String doNotInspectClassName) {
        includedExcluded.addDoNotInspectClass(doNotInspectClassName);
    }

    public void addExcludedClass(final String excludedClassName) {
        includedExcluded.addExcludedClass(excludedClassName);
    }

    public void addExcludedClass(final String[] excludedClassesName) {
        includedExcluded.addExcludedClass(excludedClassesName);
    }

    public void addExcludedClass(final Class<?> excludedClass) {
        includedExcluded.addExcludedClass(excludedClass);
    }

    public void addExcludedClass(final Class<?>[] excludedClasses) {
        includedExcluded.addExcludedClass(excludedClasses);
    }

    public void addIgnoredClass(final String ignoredClassName) {
        includedExcluded.addIgnoredClass(ignoredClassName);
    }

    public void addIgnoredClass(final String[] ignoredClassesName) {
        includedExcluded.addIgnoredClass(ignoredClassesName);
    }

    public void addIgnoredClass(final Class<?> ignoredClass) {
        includedExcluded.addIgnoredClass(ignoredClass);
    }

    public void addIgnoredClass(final Class<?>[] ignoredClasses) {
        includedExcluded.addIgnoredClass(ignoredClasses);
    }

    /**
	 * 
	 * @param rootObject
	 *            root object of object graph
	 * @param markNew
	 *            true if mark differencies, false if record initial state
	 * @throws MemInspectorException
	 */
    public void inspect(final Object rootObject, final boolean markNew) throws MemInspectorException {
        final MemoryImage previous = currentMemoryImage;
        currentMemoryImage = treeForObjectManager.createObjectNodeTree(rootObject, includedExcluded);
        if (markNew) {
            diffVisitor = new DiffVisitor();
            diffVisitor.setIgnoredClassSet(includedExcluded.getIgnoredClassSet());
            diffVisitor.setPrevious(previous);
            visitCurrent(diffVisitor);
        }
    }

    @ExcludedClass
    private static class DiffVisitor implements ITreeNodeForObjectVisitor {

        private Set<String> ignoredClassSet;

        private MemoryImage previous;

        public void setIgnoredClassSet(final Set<String> ignoredClassSet) {
            this.ignoredClassSet = ignoredClassSet;
        }

        public void setPrevious(final MemoryImage previous) {
            this.previous = previous;
        }

        @Override
        public void beginVisit(final NodeForObject nodeForObject, final boolean firstVisit) throws MemInspectorException {
            final Object object = nodeForObject.getObject();
            if (firstVisit && object != null && !ignoredClassSet.contains(object.getClass().getName()) && !previous.contains(object)) {
                nodeForObject.setMarkedNew();
            }
        }

        @Override
        public void endVisit(final NodeForObject nodeForObject) throws MemInspectorException {
        }
    }

    public long memoryUsed(final Object rootObject) throws MemInspectorException {
        final MemoryUsageListener listener = new MemoryUsageListener();
        instanceVisitor.visit(rootObject, listener, new IncludedExcluded());
        return listener.getSize();
    }

    public boolean added() {
        final NodeForObject rootNode = currentMemoryImage.getRootNode();
        return rootNode.isMarkedNewSon() || rootNode.isMarkedNew();
    }

    public List<NodeForObject> addedList() throws MemInspectorException {
        final List<NodeForObject> list = new LinkedList<NodeForObject>();
        final ITreeNodeForObjectVisitor visitor = new ITreeNodeForObjectVisitor() {

            public void beginVisit(final NodeForObject nodeForObject, final boolean firstVisit) {
                if (firstVisit && nodeForObject.isMarkedNew()) {
                    list.add(nodeForObject);
                }
            }

            @Override
            public void endVisit(final NodeForObject nodeForObject) throws MemInspectorException {
            }
        };
        visitCurrent(visitor);
        return list;
    }

    private class SearchObjectVisitor implements ITreeNodeForObjectVisitor {

        private final Deque<NodeForObject> queue = new LinkedList<NodeForObject>();

        private final Object object;

        private Deque<NodeForObject> result;

        public SearchObjectVisitor(final Object object) {
            super();
            this.object = object;
        }

        public void beginVisit(final NodeForObject nodeForObject, final boolean firstVisit) {
            queue.push(nodeForObject);
            if (firstVisit && nodeForObject.getObject() == object) {
                result = new LinkedList<NodeForObject>(queue);
            }
        }

        @Override
        public void endVisit(final NodeForObject nodeForObject) throws MemInspectorException {
            queue.pop();
        }

        public Deque<NodeForObject> getResult() {
            return result;
        }
    }

    public Deque<NodeForObject> searchObject(final Object object) throws MemInspectorException {
        final SearchObjectVisitor visitor = new SearchObjectVisitor(object);
        visitCurrent(visitor);
        return visitor.getResult();
    }

    public List<Object> getInstanceOfClass(final String className) {
        return currentMemoryImage.getInstanceOfClass(className);
    }

    public void log(final PrintStream stream) throws MemInspectorException {
        final ITreeNodeForObjectVisitor visitor = new ITreeNodeForObjectVisitor() {

            public void beginVisit(final NodeForObject nodeForObject, final boolean firstVisit) {
                String[] split = nodeForObject.toString().split("\n");
                for (String elt : split) {
                    pad(stream);
                    stream.print(elt);
                    stream.print('\n');
                }
                if (!firstVisit) {
                    pad(stream);
                    stream.print(" (already visited)");
                    stream.print('\n');
                }
                pad++;
            }

            private void pad(final PrintStream stream) {
                for (int count = 0; count < pad; count++) {
                    stream.print("  ");
                }
            }

            public void endVisit(final NodeForObject object) {
                pad--;
            }
        };
        pad = 0;
        visitCurrent(visitor);
    }

    public void xstreamTree(final File outputFile) throws MemInspectorException {
        final XStream stream = new XStream();
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(outputFile);
            stream.toXML(currentMemoryImage.getRootNode(), outputStream);
        } catch (FileNotFoundException exception) {
            throw new MemInspectorException(exception);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException exception) {
                }
            }
        }
    }

    public void xmlEncode(final File outputFile) throws MemInspectorException {
        try {
            final OutputStream out = new FileOutputStream(outputFile);
            final XMLEncoder xmlEncoder = new XMLEncoder(out);
            xmlEncoder.writeObject(currentMemoryImage.getRootNode());
            xmlEncoder.close();
        } catch (FileNotFoundException exception) {
            throw new MemInspectorException(exception);
        }
    }

    public void serialize(final File outputFile) throws MemInspectorException {
        final NodeForObjectTransferObjectFactory factory = NodeForObjectTransferObjectFactory.getInstance();
        try {
            final OutputStream out = new FileOutputStreamNIO(outputFile);
            final int rootNodeId = currentMemoryImage.getRootNode().getId();
            out.write(rootNodeId & 0xff);
            out.write((rootNodeId >> 8) & 0xff);
            out.write((rootNodeId >> 16) & 0xff);
            out.write((rootNodeId >> 24) & 0xff);
            final ITreeNodeForObjectVisitor visitor = new ITreeNodeForObjectVisitor() {

                public void beginVisit(final NodeForObject object, final boolean firstVisit) throws MemInspectorException {
                    if (firstVisit) {
                        try {
                            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            final ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                            final NodeForObjectTO nodeForObjectTO = factory.createNodeForObjectTO(object);
                            objectOutputStream.writeObject(nodeForObjectTO);
                            objectOutputStream.close();
                            final byte[] byteArray = byteArrayOutputStream.toByteArray();
                            final int length = byteArray.length;
                            out.write(length & 0xff);
                            out.write((length >> 8) & 0xff);
                            out.write((length >> 16) & 0xff);
                            out.write((length >> 24) & 0xff);
                            out.write(byteArray);
                        } catch (IOException exception) {
                            throw new MemInspectorException(exception);
                        }
                    }
                }

                public void endVisit(final NodeForObject object) {
                }
            };
            visitCurrent(visitor);
            out.flush();
            out.close();
        } catch (FileNotFoundException exception) {
            throw new MemInspectorException(exception);
        } catch (IOException exception) {
            throw new MemInspectorException(exception);
        }
    }

    private void visitCurrent(final ITreeNodeForObjectVisitor visitor) throws MemInspectorException {
        currentMemoryImage.getRootNode().accept(visitor, new TreeSet<NodeForObject>(), 0);
    }
}

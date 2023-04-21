package jmelib.codegen.project;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.ClassReader;
import java.util.Collection;

/**
 * @author Dmitry Shyshkin
 */
public class ClassCollection implements ResourceCollection {

    private ResourceCollection resourceCollection;

    private NamedCache<ClassNode> classes = new NamedCache<ClassNode>() {

        public ClassNode create(String name) {
            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader(resourceCollection.getResource(name + ".class"));
            classReader.accept(classNode, ClassReader.SKIP_CODE);
            return classNode;
        }
    };

    public ClassCollection(ResourceCollection resourceCollection) {
        this.resourceCollection = resourceCollection;
    }

    public boolean hasResource(String name) {
        return resourceCollection.hasResource(name);
    }

    public byte[] getResource(String name) {
        return resourceCollection.getResource(name);
    }

    public Collection<String> getResources() {
        return resourceCollection.getResources();
    }

    public ClassNode getClassResource(String name) {
        if (!hasResource(name + ".class")) {
            return null;
        }
        return classes.get(name);
    }
}

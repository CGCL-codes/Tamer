package org.rubypeople.rdt.internal.core.builder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.Assert;
import org.eclipse.core.resources.IFile;
import org.jruby.ast.Node;
import org.rubypeople.rdt.internal.core.util.ListUtil;

final class ShamIndexUpdater extends IndexUpdater {

    public ShamIndexUpdater() {
        super(null);
    }

    private Map updates = new HashMap();

    public void update(IFile file, Node rootNode, boolean skipFlush) {
        updates.put(file, createArgList(rootNode, skipFlush));
    }

    private List createArgList(Node rootNode, boolean skipFlush) {
        return ListUtil.create(rootNode, new Boolean(skipFlush));
    }

    public void assertUpdated(IFile expectedFile, Node expectedRootNode, boolean expectedSkipFlush) {
        Object args = updates.get(expectedFile);
        Assert.assertNotNull(expectedFile + " should have been updated", args);
        Assert.assertEquals(createArgList(expectedRootNode, expectedSkipFlush), args);
    }
}

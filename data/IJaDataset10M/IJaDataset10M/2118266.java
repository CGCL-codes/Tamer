package cz.cuni.mff.ksi.jinfer.fileselector.nodes;

import cz.cuni.mff.ksi.jinfer.base.objects.Input;
import cz.cuni.mff.ksi.jinfer.fileselector.FileSelectorTopComponent;
import java.io.File;
import java.util.Collection;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.windows.TopComponent;

/**
 *
 * @author sviro
 */
public class RootNode extends AbstractNode {

    private static final String[] FOLDERS = { "XML", "XSD", "QUERIES" };

    public RootNode(final FileSelectorTopComponent topComponent, final Input input) {
        super(new Children.Keys<String>() {

            @Override
            protected void addNotify() {
                setKeys(FOLDERS);
            }

            @Override
            protected Node[] createNodes(final String folderName) {
                final Collection<File> files;
                if (folderName.equals("XML")) {
                    files = input.getDocuments();
                } else if (folderName.equals("XSD")) {
                    files = input.getSchemas();
                } else if (folderName.equals("QUERIES")) {
                    files = input.getQueries();
                } else {
                    throw new IllegalArgumentException("unknown folder type");
                }
                return new Node[] { new FolderNode(folderName, files, topComponent) };
            }
        });
    }
}

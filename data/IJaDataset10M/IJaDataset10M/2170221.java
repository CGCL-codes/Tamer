package org.formaria.editor.netbeans.project;

import javax.swing.event.ChangeEvent;
import org.formaria.editor.project.*;
import org.formaria.editor.project.pages.PageResource;
import org.openide.nodes.Children;
import org.openide.nodes.Index;
import org.openide.nodes.Node;

/**
 * Children of the project
 * <p>Copyright (c) Formaria Ltd., 1998-2007</p>
 * $Revision: 1.3 $
 * License: see license.txt
 */
public class ProjectChildren extends Children.Keys {

    private final ProjectCookie cookie;

    public ProjectChildren(ProjectCookie cookie) {
        this.cookie = cookie;
    }

    protected Node[] createNodes(Object key) {
        return null;
    }

    public Index getIndex() {
        return new IndexImpl();
    }

    private class IndexImpl extends Index.Support {

        public Node[] getNodes() {
            return ProjectChildren.this.getNodes();
        }

        public int getNodesCount() {
            return getNodes().length;
        }

        public void reorder(int[] perm) {
            try {
                EditorProject s = cookie.getProject();
                if (s.getSize() != perm.length) {
                    throw new IllegalArgumentException();
                }
                PageResource[] pages = new PageResource[perm.length];
                for (int i = 0; i < perm.length; i++) {
                    pages[perm[i]] = s.getPage(i);
                }
                cookie.setProject(s);
                fireChangeEvent(new ChangeEvent(IndexImpl.this));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

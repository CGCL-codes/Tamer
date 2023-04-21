package edu.gsbme.wasabi.UI.Views.Visualisation;

import java.util.Iterator;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import edu.gsbme.wasabi.UI.editor.MultiPageEditor;
import edu.gsbme.wasabi.UI.editor.MultiPageEditor.EditorMode;
import edu.gsbme.yakitori.Renderer.Controller.RenderedReferenceLib;
import edu.gsbme.yakitori.util.ObjectIndexTree.RenderObjTreeNode;

/**
 * Geometric rendered object view
 * @author David
 *
 */
public class RenderObjListView extends ViewPart {

    private IPartListener partListener = new IPartListener() {

        @Override
        public void partActivated(IWorkbenchPart part) {
            if (!(part instanceof MultiPageEditor)) return;
            loadTreeViewerData(part);
        }

        @Override
        public void partBroughtToTop(IWorkbenchPart part) {
            if (!(part instanceof MultiPageEditor)) return;
            loadTreeViewerData(part);
        }

        @Override
        public void partClosed(IWorkbenchPart part) {
        }

        @Override
        public void partDeactivated(IWorkbenchPart part) {
        }

        @Override
        public void partOpened(IWorkbenchPart part) {
            if (!(part instanceof MultiPageEditor)) return;
            loadTreeViewerData(part);
        }
    };

    CTabFolder folder;

    TreeViewer all_treeviewer;

    TreeViewer dim0_treeviewer;

    TreeViewer dim1_treeviewer;

    TreeViewer dim2_treeviewer;

    TreeViewer dim3_treeviewer;

    @Override
    public void createPartControl(Composite parent) {
        getSite().getWorkbenchWindow().getPartService().addPartListener(partListener);
        parent.setLayout(new FillLayout());
        folder = new CTabFolder(parent, SWT.TOP);
        folder.setUnselectedCloseVisible(false);
        folder.setSimple(false);
        final RenderObjContentProvider contentProvider = new RenderObjContentProvider();
        final RenderObjLabelProvider labelProvider = new RenderObjLabelProvider();
        CTabItem item = new CTabItem(folder, SWT.NONE);
        item.setText("All Objects");
        Composite composite = new Composite(folder, SWT.NONE);
        composite.setLayout(new FillLayout());
        all_treeviewer = new TreeViewer(composite, SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
        all_treeviewer.setContentProvider(contentProvider);
        all_treeviewer.setLabelProvider(labelProvider);
        all_treeviewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                if (event.getSelection().isEmpty()) {
                    return;
                }
                if (event.getSelection() instanceof IStructuredSelection) {
                    IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                    for (Iterator iterator = selection.iterator(); iterator.hasNext(); ) {
                        Object domain = iterator.next();
                        if (domain instanceof RenderObjTreeNode) {
                            IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
                            if (part instanceof MultiPageEditor) {
                                RenderObjTreeNode node = (RenderObjTreeNode) domain;
                                if (node.getIndex() != -1) {
                                    ((MultiPageEditor) part).returnVisualEditor().returnRenderer().returnRenderController().clearSelectedObject();
                                    ((MultiPageEditor) part).returnVisualEditor().returnRenderer().returnRenderController().setSelectedObject(node.getIndex());
                                }
                            }
                        }
                    }
                }
            }
        });
        item.setControl(composite);
        item = new CTabItem(folder, SWT.NONE);
        item.setText("Points");
        composite = new Composite(folder, SWT.NONE);
        composite.setLayout(new FillLayout());
        dim0_treeviewer = new TreeViewer(composite, SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
        dim0_treeviewer.setContentProvider(contentProvider);
        dim0_treeviewer.setLabelProvider(labelProvider);
        dim0_treeviewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                if (event.getSelection().isEmpty()) {
                    return;
                }
                if (event.getSelection() instanceof IStructuredSelection) {
                    IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                    for (Iterator iterator = selection.iterator(); iterator.hasNext(); ) {
                        Object domain = iterator.next();
                        if (domain instanceof RenderObjTreeNode) {
                            IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
                            if (part instanceof MultiPageEditor) {
                                RenderObjTreeNode node = (RenderObjTreeNode) domain;
                                System.out.println(node.getIndex());
                                if (node.getIndex() != -1) {
                                    ((MultiPageEditor) part).returnVisualEditor().returnRenderer().returnRenderController().clearSelectedObject();
                                    ((MultiPageEditor) part).returnVisualEditor().returnRenderer().returnRenderController().setSelectedObject(node.getIndex());
                                }
                            }
                        }
                    }
                }
            }
        });
        item.setControl(composite);
        item = new CTabItem(folder, SWT.NONE);
        item.setText("Dim 1");
        composite = new Composite(folder, SWT.NONE);
        composite.setLayout(new FillLayout());
        dim1_treeviewer = new TreeViewer(composite, SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
        dim1_treeviewer.setContentProvider(contentProvider);
        dim1_treeviewer.setLabelProvider(labelProvider);
        dim1_treeviewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                if (event.getSelection().isEmpty()) {
                    return;
                }
                if (event.getSelection() instanceof IStructuredSelection) {
                    IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                    for (Iterator iterator = selection.iterator(); iterator.hasNext(); ) {
                        Object domain = iterator.next();
                        if (domain instanceof RenderObjTreeNode) {
                            IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
                            if (part instanceof MultiPageEditor) {
                                RenderObjTreeNode node = (RenderObjTreeNode) domain;
                                System.out.println(node.getIndex());
                                if (node.getIndex() != -1) {
                                    ((MultiPageEditor) part).returnVisualEditor().returnRenderer().returnRenderController().clearSelectedObject();
                                    ((MultiPageEditor) part).returnVisualEditor().returnRenderer().returnRenderController().setSelectedObject(node.getIndex());
                                }
                            }
                        }
                    }
                }
            }
        });
        item.setControl(composite);
        item = new CTabItem(folder, SWT.NONE);
        item.setText("Dim 2");
        composite = new Composite(folder, SWT.NONE);
        composite.setLayout(new FillLayout());
        dim2_treeviewer = new TreeViewer(composite, SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
        dim2_treeviewer.setContentProvider(contentProvider);
        dim2_treeviewer.setLabelProvider(labelProvider);
        dim2_treeviewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                if (event.getSelection().isEmpty()) {
                    return;
                }
                if (event.getSelection() instanceof IStructuredSelection) {
                    IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                    for (Iterator iterator = selection.iterator(); iterator.hasNext(); ) {
                        Object domain = iterator.next();
                        if (domain instanceof RenderObjTreeNode) {
                            IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
                            if (part instanceof MultiPageEditor) {
                                RenderObjTreeNode node = (RenderObjTreeNode) domain;
                                System.out.println(node.getIndex());
                                if (node.getIndex() != -1) {
                                    ((MultiPageEditor) part).returnVisualEditor().returnRenderer().returnRenderController().clearSelectedObject();
                                    ((MultiPageEditor) part).returnVisualEditor().returnRenderer().returnRenderController().setSelectedObject(node.getIndex());
                                }
                            }
                        }
                    }
                }
            }
        });
        item.setControl(composite);
        item = new CTabItem(folder, SWT.NONE);
        item.setText("Dim 3");
        composite = new Composite(folder, SWT.NONE);
        composite.setLayout(new FillLayout());
        dim3_treeviewer = new TreeViewer(composite, SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
        dim3_treeviewer.setContentProvider(contentProvider);
        dim3_treeviewer.setLabelProvider(labelProvider);
        dim3_treeviewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                if (event.getSelection().isEmpty()) {
                    return;
                }
                if (event.getSelection() instanceof IStructuredSelection) {
                    IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                    for (Iterator iterator = selection.iterator(); iterator.hasNext(); ) {
                        Object domain = iterator.next();
                        if (domain instanceof RenderObjTreeNode) {
                            IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
                            if (part instanceof MultiPageEditor) {
                                RenderObjTreeNode node = (RenderObjTreeNode) domain;
                                System.out.println(node.getIndex());
                                if (node.getIndex() != -1) {
                                    ((MultiPageEditor) part).returnVisualEditor().returnRenderer().returnRenderController().clearSelectedObject();
                                    ((MultiPageEditor) part).returnVisualEditor().returnRenderer().returnRenderController().setSelectedObject(node.getIndex());
                                }
                            }
                        }
                    }
                }
            }
        });
        item.setControl(composite);
        folder.setSelection(0);
    }

    private void loadTreeViewerData(IWorkbenchPart part) {
        if (part instanceof MultiPageEditor) {
            MultiPageEditor editor = (MultiPageEditor) part;
            if (editor.mode == EditorMode.FML) {
                RenderedReferenceLib lib = editor.returnVisualEditor().returnRenderer().returnRenderController().getRenderReferenceLib();
                all_treeviewer.setInput(lib.indexTree.root);
                all_treeviewer.expandToLevel(2);
                dim0_treeviewer.setInput(lib.indexTree.getDimensionNode(0));
                dim0_treeviewer.expandToLevel(2);
                dim1_treeviewer.setInput(lib.indexTree.getDimensionNode(1));
                dim1_treeviewer.expandToLevel(2);
                dim2_treeviewer.setInput(lib.indexTree.getDimensionNode(2));
                dim2_treeviewer.expandToLevel(2);
                dim3_treeviewer.setInput(lib.indexTree.getDimensionNode(3));
                dim3_treeviewer.expandToLevel(2);
            } else {
                all_treeviewer.setInput(null);
                dim0_treeviewer.setInput(null);
                dim1_treeviewer.setInput(null);
                dim2_treeviewer.setInput(null);
                dim3_treeviewer.setInput(null);
            }
        } else {
            all_treeviewer.setInput(null);
            dim0_treeviewer.setInput(null);
            dim1_treeviewer.setInput(null);
            dim2_treeviewer.setInput(null);
            dim3_treeviewer.setInput(null);
        }
    }

    @Override
    public void setFocus() {
    }

    public void dispose() {
        getSite().getWorkbenchWindow().getPartService().removePartListener(partListener);
        super.dispose();
    }
}

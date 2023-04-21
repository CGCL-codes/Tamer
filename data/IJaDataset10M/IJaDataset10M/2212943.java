package no.uia.sudoku;

import hub.metrik.lang.eprovide.StepNotification;
import hub.metrik.lang.eprovide.editors.tef.ModelDocumentProvider;
import hub.metrik.lang.eprovide.editors.tef.TransactionalModelEditor;
import hub.sam.tef.layout.AbstractLayoutManager;
import hub.sam.tef.layout.BlockLayout;
import hub.sam.tef.modelcreating.IModelCreatingContext;
import java.util.ArrayList;
import java.util.List;
import no.uia.sudoku.util.SudokuAdapterFactory;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;

/**
 * Editor for .cttcn files based on TEF.
 * 
 * @author <a href="mailto:sadilek@informatik.hu-berlin.de">Daniel Sadilek</a>
 */
public class SudokuEditor extends TransactionalModelEditor {

    private static final String EDITING_DOMAIN_ID = "no.uia.sudoku.diagram.EditingDomain";

    private Adapter stepListener;

    @Override
    public AbstractLayoutManager createLayout() {
        return new BlockLayout();
    }

    @Override
    protected AdapterFactory[] createItemProviderAdapterFactories() {
        return new AdapterFactory[] { new SudokuAdapterFactory() };
    }

    /**
	 * Override super method to use a {@link ModelDocumentProvider} that adds (and also removes) an
	 * adapter, which updates the editor text when an EProvide step is performed.
	 * 
	 * @see hub.sam.tef.editor.model.ModelEditor#initialiseDocumentProvider()
	 */
    @Override
    protected void initialiseDocumentProvider() {
        setDocumentProvider(new ModelDocumentProvider(this, false) {

            @Override
            protected boolean setDocumentContent(final IDocument document, IEditorInput editorInput) throws CoreException {
                boolean result = super.setDocumentContent(document, editorInput);
                stepListener = new AdapterImpl() {

                    @Override
                    public void notifyChanged(Notification notification) {
                        if (notification instanceof StepNotification) {
                            PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {

                                public void run() {
                                    try {
                                        updateDocumentContents(document, getCurrentModel());
                                    } catch (CoreException ce) {
                                        throw new RuntimeException("Could not update editor text after EProvide step.", ce);
                                    }
                                }
                            });
                            waitForReconciliation();
                        }
                    }
                };
                getCurrentModel().eAdapters().add(stepListener);
                return result;
            }

            @Override
            protected void modelDispose() {
                super.modelDispose();
                getCurrentModel().eAdapters().remove(stepListener);
            }
        });
    }

    @Override
    public void updateCurrentModel(final IModelCreatingContext context) {
        execute(new Runnable() {

            public void run() {
                Puzzle puzzle = (Puzzle) context.getResource().getContents().get(0);
                if (puzzle.getIDimension() == 9 && puzzle.getCells().size() == 81) {
                    puzzle.getFields().clear();
                    for (int i = 0; i < 81; ++i) {
                        Cell cell = puzzle.getCells().get(i);
                        int rowIndex = i / 9;
                        int columnIndex = i % 9;
                        int boxRowIndex = i / 27;
                        int boxColumnIndex = (i / 3) % 3;
                        int boxIndex = boxRowIndex * 3 + boxColumnIndex;
                        Row row = (Row) puzzle.getField(SudokuPackage.ROW, rowIndex);
                        if (row == null) {
                            row = SudokuFactory.eINSTANCE.createRow();
                            puzzle.getFields().add(row);
                        }
                        row.getCells().add(cell);
                        Column column = (Column) puzzle.getField(SudokuPackage.COLUMN, columnIndex);
                        if (column == null) {
                            column = SudokuFactory.eINSTANCE.createColumn();
                            puzzle.getFields().add(column);
                        }
                        column.getCells().add(cell);
                        Box box = (Box) puzzle.getField(SudokuPackage.BOX, boxIndex);
                        if (box == null) {
                            box = SudokuFactory.eINSTANCE.createBox();
                            puzzle.getFields().add(box);
                        }
                        box.getCells().add(cell);
                    }
                }
            }
        });
        super.updateCurrentModel(context);
    }

    @Override
    public IModelCreatingContext createModelCreatingContext() {
        return super.createModelCreatingContext();
    }

    @Override
    protected EPackage[] createMetaModelPackages() {
        return new EPackage[] { SudokuPackage.eINSTANCE };
    }

    @Override
    protected Bundle getPluginBundle() {
        return SudokuPlugin.getDefault().getBundle();
    }

    @Override
    protected String getSyntaxPath() {
        return "syntax/sudoku.etslt";
    }

    @Override
    public List<IRule> getAdditionalPresentationRules() {
        List<IRule> result = new ArrayList<IRule>();
        result.add(new SingleLineRule("#", null, new Token(new TextAttribute(new Color(Display.getCurrent(), new RGB(255, 46, 248)), null, SWT.NORMAL))));
        return result;
    }

    protected String getEditingDomainId() {
        return EDITING_DOMAIN_ID;
    }
}

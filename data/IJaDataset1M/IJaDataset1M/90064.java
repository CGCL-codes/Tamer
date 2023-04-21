package org.remus.infomngmnt.test.navigation;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import junit.framework.Assert;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.actions.WorkspaceModifyDelegatingOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.remus.infomngmnt.Category;
import org.remus.infomngmnt.InformationUnit;
import org.remus.infomngmnt.InformationUnitListItem;
import org.remus.infomngmnt.common.ui.UIUtil;
import org.remus.infomngmnt.core.commands.CommandFactory;
import org.remus.infomngmnt.core.model.ApplicationModelPool;
import org.remus.infomngmnt.test.util.InfoItemUtils;
import org.remus.infomngmnt.util.CategoryUtil;
import org.remus.infomngmnt.util.EditingUtil;

/**
 * @author Tom Seidel <tom.seidel@remus-software.org>
 */
public class SingleItemNavigationTest {

    private final EditingDomain domain = EditingUtil.getInstance().getNavigationEditingDomain();

    public static final String CAT_SUB_SUB_1 = "CAT_SUB_SUB_1";

    public static final String CAT_SUB_1 = "CAT_SUB_1";

    private Category createCategory;

    private Category subCategory;

    @Before
    public void initialize() throws Exception {
        this.createCategory = InfoItemUtils.createNewCategory(CAT_SUB_1, "Inbox");
        this.subCategory = InfoItemUtils.createNewCategory(CAT_SUB_SUB_1, "Inbox/" + CAT_SUB_1);
    }

    @After
    public void cleanUp() {
        final Category category = CategoryUtil.findCategory("Inbox", false).getChildren().get(0);
        WorkspaceModifyDelegatingOperation operation = new WorkspaceModifyDelegatingOperation(new IRunnableWithProgress() {

            public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                Command deleteCategoryCommand = CommandFactory.DELETE_SYNCHRONIZABLE_CATEGORY(category, EditingUtil.getInstance().getNavigationEditingDomain());
                EditingUtil.getInstance().getNavigationEditingDomain().getCommandStack().execute(deleteCategoryCommand);
            }
        });
        try {
            new ProgressMonitorDialog(UIUtil.getDisplay().getActiveShell()).run(true, true, operation);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createItem() throws Exception {
        InfoItemUtils.createSimpleTextInfoUnit(this.createCategory);
        Assert.assertEquals(1, this.createCategory.getInformationUnit().size());
    }

    @Test
    public void createItemAndCheckCache() throws Exception {
        InformationUnit createSimpleTextInfoUnit = InfoItemUtils.createSimpleTextInfoUnit(this.createCategory);
        InformationUnitListItem itemById = ApplicationModelPool.getInstance().getItemById(createSimpleTextInfoUnit.getId(), new NullProgressMonitor());
        Assert.assertNotNull(itemById);
    }

    @Test
    public void deleteItem() throws Exception {
        InformationUnit createSimpleTextInfoUnit = InfoItemUtils.createSimpleTextInfoUnit(this.createCategory);
        IFile file = (IFile) createSimpleTextInfoUnit.getAdapter(IFile.class);
        Assert.assertTrue(file.exists());
        InformationUnitListItem itemById = ApplicationModelPool.getInstance().getItemById(createSimpleTextInfoUnit.getId(), new NullProgressMonitor());
        Command command = CommandFactory.DELETE_INFOUNIT(Collections.singletonList(itemById), this.domain);
        this.domain.getCommandStack().execute(command);
        Assert.assertFalse(file.exists());
        itemById = ApplicationModelPool.getInstance().getItemById(createSimpleTextInfoUnit.getId(), new NullProgressMonitor());
        Assert.assertNull(itemById);
    }

    @Test
    public void deleteItemAndRestore() throws Exception {
        InformationUnit createSimpleTextInfoUnit = InfoItemUtils.createSimpleTextInfoUnit(this.createCategory);
        IFile file = (IFile) createSimpleTextInfoUnit.getAdapter(IFile.class);
        Assert.assertTrue(file.exists());
        InformationUnitListItem itemById = ApplicationModelPool.getInstance().getItemById(createSimpleTextInfoUnit.getId(), new NullProgressMonitor());
        Command command = CommandFactory.DELETE_INFOUNIT(Collections.singletonList(itemById), this.domain);
        this.domain.getCommandStack().execute(command);
        Assert.assertFalse(file.exists());
        itemById = ApplicationModelPool.getInstance().getItemById(createSimpleTextInfoUnit.getId(), new NullProgressMonitor());
        Assert.assertNull(itemById);
        this.domain.getCommandStack().undo();
        Assert.assertTrue(file.exists());
        itemById = ApplicationModelPool.getInstance().getItemById(createSimpleTextInfoUnit.getId(), new NullProgressMonitor());
        Assert.assertNotNull(itemById);
    }
}

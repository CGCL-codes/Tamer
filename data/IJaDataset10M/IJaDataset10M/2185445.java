package org.antlride.unit.ui.wizards;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import junit.framework.Assert;
import org.antlride.support.ui.Selections;
import org.antlride.ui.UIPlugin;
import org.antlride.ui.wizards.AbstractNewGrammarWizard;
import org.antlride.ui.wizards.NewGrammar;
import org.antlride.ui.wizards.NewGrammarService;
import org.easymock.EasyMock;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardContainer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Unit test for {@link AbstractNewGrammarWizard}.
 * 
 * @author Edgar Espina
 * @since 2.1.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ AbstractNewGrammarWizard.class, UIPlugin.class, Selections.class })
public class NewGrammarWizardTest {

    @Test
    public void init() throws Exception {
        IStructuredSelection selection = EasyMock.createMock(IStructuredSelection.class);
        IResource resource = EasyMock.createMock(IResource.class);
        IPath selectedPath = Path.fromOSString("path/to/somewhere");
        EasyMock.expect(resource.getFullPath()).andReturn(selectedPath);
        PowerMock.mockStatic(Selections.class);
        EasyMock.expect(Selections.first(selection, IResource.class)).andReturn(resource);
        IDialogSettings dialogSettings = EasyMock.createMock(IDialogSettings.class);
        PowerMock.mockStatic(UIPlugin.class);
        UIPlugin plugin = PowerMock.createMock(UIPlugin.class);
        EasyMock.expect(UIPlugin.getInstace()).andReturn(plugin).times(2);
        EasyMock.expect(plugin.getDialogSettings()).andReturn(dialogSettings);
        NewGrammarService newGrammarService = EasyMock.createMock(NewGrammarService.class);
        IPath defaultHome = Path.fromOSString("/home/user");
        EasyMock.expect(newGrammarService.defaultContainer(selectedPath)).andReturn(defaultHome);
        EasyMock.expect(plugin.getService(NewGrammarService.class)).andReturn(newGrammarService);
        AbstractNewGrammarWizard wizard = PowerMock.createPartialMock(AbstractNewGrammarWizard.class, "setDialogSettings", "setWindowTitle", "createNewGrammar");
        wizard.setDialogSettings(dialogSettings);
        EasyMock.expectLastCall();
        wizard.setWindowTitle("New Grammar");
        EasyMock.expectLastCall();
        PowerMock.expectPrivate(wizard, "createNewGrammar").andReturn(new NewGrammar());
        EasyMock.replay(newGrammarService);
        EasyMock.replay(resource);
        PowerMock.replay(Selections.class);
        EasyMock.replay(selection);
        EasyMock.replay(dialogSettings);
        PowerMock.replay(UIPlugin.class);
        EasyMock.replay(plugin);
        PowerMock.replay(wizard);
        wizard.init(null, selection);
        Assert.assertEquals(defaultHome.toOSString(), wizard.getModel().getLocation());
        EasyMock.verify(newGrammarService);
        EasyMock.verify(resource);
        PowerMock.verify(Selections.class);
        EasyMock.verify(selection);
        EasyMock.verify(dialogSettings);
        PowerMock.verify(UIPlugin.class);
        EasyMock.verify(plugin);
        PowerMock.verify(wizard);
    }

    @Test
    public void performFinish() throws IOException, CoreException, InvocationTargetException, InterruptedException {
        PowerMock.mockStatic(UIPlugin.class);
        UIPlugin plugin = PowerMock.createMock(UIPlugin.class);
        EasyMock.expect(UIPlugin.getInstace()).andReturn(plugin).times(1);
        IWizardContainer context = EasyMock.createMock(IWizardContainer.class);
        NewGrammarService newGrammarService = EasyMock.createMock(NewGrammarService.class);
        newGrammarService.createAndOpen(context, null);
        EasyMock.expectLastCall();
        EasyMock.expect(plugin.getService(NewGrammarService.class)).andReturn(newGrammarService);
        AbstractNewGrammarWizard wizard = PowerMock.createPartialMock(AbstractNewGrammarWizard.class, "getContainer");
        EasyMock.expect(wizard.getContainer()).andReturn(context);
        EasyMock.replay(context);
        EasyMock.replay(newGrammarService);
        PowerMock.replay(UIPlugin.class);
        EasyMock.replay(plugin);
        PowerMock.replay(wizard);
        wizard.performFinish();
        EasyMock.verify(context);
        EasyMock.verify(newGrammarService);
        PowerMock.verify(UIPlugin.class);
        EasyMock.verify(plugin);
        PowerMock.verify(wizard);
    }
}

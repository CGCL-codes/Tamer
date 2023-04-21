package net.sourceforge.taggerplugin.wizard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import net.sourceforge.taggerplugin.TaggerActivator;
import net.sourceforge.taggerplugin.io.TagIoFactory;
import net.sourceforge.taggerplugin.io.TagIoFormat;
import net.sourceforge.taggerplugin.manager.TagManager;
import net.sourceforge.taggerplugin.model.Tag;
import net.sourceforge.taggerplugin.util.IoUtils;
import net.sourceforge.taggerplugin.util.SynchWithDisplay;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IImportWizard;

/**
 * Wizard for the importing of tag data.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TagImportWizard extends AbstractTagExternalizationWizard implements IImportWizard {

    /**
	 * Creates a new tag import wizard.
	 */
    public TagImportWizard() {
        super(TagExternalizationWizardType.IMPORT);
    }

    /**
	 * @see AbstractTagExternalizationWizard#doFinish(IProgressMonitor, File, TagIoFormat)
	 */
    protected void doFinish(final IProgressMonitor monitor, final File file, final TagIoFormat format) throws CoreException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            final Tag[] tags = TagIoFactory.create(format).readTags(reader, monitor);
            SynchWithDisplay.synch(new Runnable() {

                public void run() {
                    final TagManager mgr = TagManager.getInstance();
                    for (Tag tag : tags) {
                        if (!mgr.tagExists(tag.getId())) {
                            mgr.addTag(tag);
                        }
                    }
                }
            });
        } catch (Exception ex) {
            throw new CoreException(new Status(IStatus.ERROR, TaggerActivator.PLUGIN_ID, IStatus.OK, ex.getMessage(), ex));
        } finally {
            IoUtils.closeQuietly(reader);
        }
    }
}

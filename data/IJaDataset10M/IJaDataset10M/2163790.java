package de.andreavicentini.magicphoto.ui.directory;

import java.io.File;
import java.text.MessageFormat;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionDelegate;
import org.magiclabs.basix.IClosure;
import org.magiclabs.basix.Magics;
import org.magiclabs.basix.Magics.AbortException;
import de.andreavicentini.magiclogger.service.ILogEntry;
import de.andreavicentini.magiclogger.service.LoggerService;
import de.andreavicentini.magicphoto.batch.Executor;
import de.andreavicentini.magicphoto.batch.IExecutor;
import de.andreavicentini.magicphoto.domain.directory.IDirectory;
import de.andreavicentini.magicphoto.domain.layer.LayeredModel;
import de.andreavicentini.magicphoto.domain.pictures.IPicture;

public class ExportAction extends ActionDelegate implements IViewActionDelegate {

    private IWorkbenchWindow window;

    @Override
    public void run(IAction action) {
        try {
            File target = chooseDirectory();
            iterate(target, LayeredModel.current.getRoot());
        } catch (AbortException e) {
        }
    }

    private void iterate(File target, IDirectory root) {
        final File newTarget = new File(target, root.getPath().getName());
        newTarget.mkdir();
        Magics.each(root.listSubdirectories(), new IClosure<IDirectory>() {

            public void process(IDirectory object) {
                iterate(newTarget, object);
            }
        });
        Magics.each(root.listPictures(), new IClosure<IPicture>() {

            public void process(IPicture object) {
                makeLink(newTarget, object);
            }
        });
    }

    private final IExecutor executor = new Executor();

    private void makeLink(File target, IPicture source) {
        String targetFile = new File(target, source.getFile().getName()).getAbsolutePath();
        String sourceFile = source.getFile().getAbsolutePath();
        try {
            String cmd = MessageFormat.format("fsutil hardlink create \"{0}\" \"{1}\"", targetFile, sourceFile);
            System.out.println(cmd);
            executor.execute(cmd, IExecutor.NIL);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private File chooseDirectory() throws AbortException {
        DirectoryDialog dialog = new DirectoryDialog(this.window.getShell());
        String string = dialog.open();
        if (string == null) throw new Magics.AbortException();
        return new File(string);
    }

    public void init(IViewPart view) {
        window = view.getSite().getWorkbenchWindow();
    }
}

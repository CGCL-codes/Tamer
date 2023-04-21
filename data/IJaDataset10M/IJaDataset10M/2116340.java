package net.simplemodel.core.generator.internal;

import java.io.IOException;
import java.io.OutputStream;
import net.simplemodel.core.SimplemodelCore;
import net.simplemodel.core.ast.SMModuleDeclaration;
import net.simplemodel.core.ast.SMTypeDeclaration;
import net.simplemodel.core.generator.IGenerationResultItem;
import net.simplemodel.core.generator.IGeneratorContext;
import net.simplemodel.core.generator.IGeneratorItem;
import net.simplemodel.core.generator.ITemplate;
import net.simplemodel.core.generator.ITemplateContext;
import net.simplemodel.core.generator.WillNotGenerateException;
import ognl.Ognl;
import ognl.OgnlException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;

public class TemplateGeneratorItem implements IGeneratorItem {

    private static void createFolder(IContainer container) throws CoreException {
        if (!container.exists() && container instanceof IFolder) {
            IFolder folder = (IFolder) container;
            createFolder(folder.getParent());
            folder.create(false, true, new NullProgressMonitor());
        }
    }

    private final ITemplate template;

    private final IFolder srcFolder;

    private final String packageName;

    private final String compilationUnitName;

    private final String generateCondition;

    public TemplateGeneratorItem(IFolder srcFolder, ITemplate template, String packageName, String compilationUnitName, String generateCondition) {
        if (srcFolder == null) throw new IllegalArgumentException();
        if (!srcFolder.exists()) throw new IllegalArgumentException();
        if (template == null) throw new IllegalArgumentException();
        if (packageName == null) throw new IllegalArgumentException();
        if (compilationUnitName == null) throw new IllegalArgumentException();
        if (generateCondition == null) throw new IllegalArgumentException();
        this.srcFolder = srcFolder;
        this.template = template;
        this.packageName = packageName;
        this.compilationUnitName = compilationUnitName;
        this.generateCondition = generateCondition;
    }

    @Override
    public IGenerationResultItem generate(final IGeneratorContext generatorContext, final SMModuleDeclaration module, final SMTypeDeclaration input) throws CoreException {
        final ITemplateContext context = new TemplateContextImpl(module, generatorContext, input);
        try {
            if (!Boolean.TRUE.equals(Ognl.getValue(generateCondition, context))) return null;
        } catch (OgnlException e) {
            throw new CoreException(new Status(IStatus.ERROR, SimplemodelCore.PLUGIN_ID, "Failed to generate", e));
        }
        final String compilationUnitName;
        final String packageName;
        try {
            compilationUnitName = (String) Ognl.getValue(this.compilationUnitName, context);
            packageName = (String) Ognl.getValue(this.packageName, context);
        } catch (OgnlException e) {
            throw new CoreException(new Status(IStatus.ERROR, SimplemodelCore.PLUGIN_ID, "Failed to generate", e));
        }
        StringBuilder path = new StringBuilder();
        if (packageName != null && !"".equals(packageName)) path.append(packageName.replace('.', '/')).append('/');
        path.append(compilationUnitName).append(".java");
        final IFile file = srcFolder.getFile(path.toString());
        createFolder(file.getParent());
        final String generatedText;
        try {
            generatedText = template.generate(context);
        } catch (WillNotGenerateException e) {
            return null;
        }
        return new IGenerationResultItem() {

            @Override
            public IFile getFile() {
                return file;
            }

            @Override
            public void postProcess() throws CoreException {
            }

            @Override
            public void preProcess() throws CoreException {
            }

            @Override
            public void writeContent(OutputStream os) throws CoreException {
                try {
                    os.write(generatedText.getBytes());
                } catch (IOException e) {
                    throw new CoreException(new Status(IStatus.ERROR, SimplemodelCore.PLUGIN_ID, "Failed to write generated output!", e));
                }
            }
        };
    }
}

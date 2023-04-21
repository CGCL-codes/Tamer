package org.pluginbuilder.core.internal.templates;

import java.io.IOException;
import java.io.InputStream;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.pluginbuilder.core.Activator;
import org.pluginbuilder.core.ResourceUtil;

public class Template {

    public static final Path TEMPLATE_BASE_DIR = new Path("templates/generator");

    private IPath relativePath;

    private String originalContent;

    private String content;

    public static Template createTemplate(Path path) throws TemplateNotFoundException {
        InputStream inputStream;
        try {
            IPath fullPath = TEMPLATE_BASE_DIR.append(path);
            inputStream = FileLocator.openStream(Activator.getDefault().getBundle(), fullPath, false);
        } catch (IOException e) {
            throw new TemplateNotFoundException(e);
        }
        String content;
        try {
            content = ResourceUtil.getContents(inputStream);
        } catch (CoreException e) {
            throw new TemplateNotFoundException(e);
        }
        return new Template(path, content);
    }

    public Template(Path path, String content) {
        relativePath = path;
        this.originalContent = content;
        reset();
    }

    public void replace(String token, String value) {
        content = content.replace(token, value);
    }

    public void write(IProject project) {
        ResourceUtil.createOrUpdateFile(project.getName(), relativePath.toString(), content);
    }

    public void reset() {
        content = originalContent;
    }

    public String getSubsitutedContent() {
        return content;
    }
}

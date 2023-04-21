package org.vexi.build.jsdoc;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipFile;
import org.vexi.tools.autodoc.SourcePath;
import ebuild.api.IAssemblerArgument;
import ebuild.api.IEBuild;
import ebuild.api.IInput;
import ebuild.api.log.ILogger;
import ebuild.api.plugin.AbstractAssembler;
import ebuild.api.plugin.BuildPluginException;
import ebuild.api.plugin.IPropertyMap;
import ebuild.util.CollectionUtil;
import ebuild.util.JSONUtil;

public class JSDocAssembler extends AbstractAssembler {

    public Collection<File> assemble(IAssemblerArgument argument) throws BuildPluginException {
        ILogger logger = argument.getLogger();
        IEBuild ebuild = argument.getEBuild();
        IInput input = argument.getInputMap().expectLoneInput();
        Collection<File> archives = input.getArtifacts();
        if (archives.size() == 0) throw new BuildPluginException("Empty source input (0 archives)");
        try {
            SourcePath path = new SourcePath();
            logger.log("Source:");
            for (File f : archives) {
                logger.log("    " + ebuild.formatAsDisplayPath(f));
                path.addSourceJar(new ZipFile(f));
            }
            IPropertyMap props = argument.getPropertyMap();
            String copyright = (argument.getLastCommitDate().getYear() + 1900) + props.expectString("copyright-holder");
            JSDoc jsdoc = new JSDoc(path, argument.getOutputDirectory());
            jsdoc.setName(props.expectString("name"));
            jsdoc.setVersion(argument.getProductKey().toString());
            jsdoc.setCopyRight(copyright);
            jsdoc.setProjectWWW(props.expectString("project-www"));
            List<String> libraryRoots = JSONUtil.readList(props.expectString("library-roots"));
            for (String s : libraryRoots) {
                String[] ss = s.split(",", 2);
                String name = ss[0];
                String jppFile = ss[1];
                jsdoc.addLibraryRoot(name, jppFile);
            }
            List<String> concepts = JSONUtil.readList(props.expectString("concepts"));
            for (String s : concepts) {
                String[] ss = s.split(",", 2);
                String name = ss[0];
                String jppFile = ss[1];
                jsdoc.addConcept(name, jppFile);
            }
            logger.prime();
            jsdoc.doIt();
            return CollectionUtil.singletonList(argument.getOutputDirectory());
        } catch (Exception e) {
            throw new BuildPluginException(e);
        }
    }
}

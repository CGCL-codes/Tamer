package de.mpiwg.vspace.oaw.generation;

import java.io.File;
import java.util.List;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.EObject;
import de.mpiwg.vspace.common.project.ProjectManager;
import de.mpiwg.vspace.generation.copier.BatchImageProcessor;
import de.mpiwg.vspace.generation.copier.CopyFile;
import de.mpiwg.vspace.generation.copier.GenerationCopier;
import de.mpiwg.vspace.generation.copier.ICopyFile;
import de.mpiwg.vspace.genmodel.AdditionalFile;
import de.mpiwg.vspace.genmodel.CSSFile;
import de.mpiwg.vspace.genmodel.GenFile;
import de.mpiwg.vspace.genmodel.JavascriptFile;
import de.mpiwg.vspace.metamodel.transformed.AlienSlideLink;
import de.mpiwg.vspace.metamodel.transformed.BranchingPointChoice;
import de.mpiwg.vspace.metamodel.transformed.CategorizedLink;
import de.mpiwg.vspace.metamodel.transformed.CategorizedLinkType;
import de.mpiwg.vspace.metamodel.transformed.Exhibition;
import de.mpiwg.vspace.metamodel.transformed.GenerableItem;
import de.mpiwg.vspace.metamodel.transformed.Image;
import de.mpiwg.vspace.metamodel.transformed.ImageType;
import de.mpiwg.vspace.metamodel.transformed.Link;
import de.mpiwg.vspace.metamodel.transformed.Navigation;
import de.mpiwg.vspace.metamodel.transformed.NavigationItem;
import de.mpiwg.vspace.metamodel.transformed.Video;
import de.mpiwg.vspace.oaw.generation.helper.CacheManager;
import de.mpiwg.vspace.util.images.ImageProcessor;

public class FileCopyHelper {

    public static void init() {
        CacheManager.INSTANCE.init();
    }

    public static void copyImage(Image image) {
        if (image.getType() != ImageType.LOCAL) return;
        GenFile genfile = image.getGenfile();
        String path = image.getImagePath();
        IProject project = ProjectManager.getInstance().getCurrentProject();
        String root = project.getLocation().toOSString() + File.separator;
        String sourcePath = root + path;
        String filename = image.getId().hashCode() + "_w" + image.getWidth() + "h" + image.getHeight();
        genfile.setName(filename);
        BatchImageProcessor.INSTANCE.addImageToResize(genfile, sourcePath, image.getWidth(), image.getHeight());
    }

    public static void copyAbsoluteImage(Image image) {
        if (image == null) return;
        if (image.getType() != ImageType.LOCAL) return;
        GenFile genfile = image.getGenfile();
        if (genfile == null) return;
        String sourcePath = image.getImagePath();
        String filename = image.getId() + "_w" + image.getWidth() + "h" + image.getHeight();
        genfile.setName(filename);
        BatchImageProcessor.INSTANCE.addImageToResize(genfile, sourcePath, image.getWidth(), image.getHeight());
    }

    public static void copyNavigationImages(Navigation navigation) {
        if (navigation == null) return;
        List<EObject> contents = navigation.eContents();
        if (contents == null) return;
        for (EObject eo : contents) {
            if (!(eo instanceof NavigationItem)) continue;
            NavigationItem item = (NavigationItem) eo;
            copyNavigationItem(item);
        }
    }

    public static void copyAdditionalFiles(CategorizedLink link) {
        if (link.getType() == CategorizedLinkType.LOCAL) {
            GenFile genfile = link.getGenfile();
            if (genfile == null) return;
            String filename = link.getFilename();
            if (filename == null || filename.equals("")) return;
            String filepath = link.getFilepath();
            if (filepath == null) return;
            File addFile = new File(filepath);
            if (addFile.exists()) {
                String targetPath = genfile.getPath(null);
                ICopyFile copy = new CopyFile(addFile.getAbsolutePath(), targetPath);
                GenerationCopier.INSTANCE.addFile(copy);
            }
        }
    }

    public static void copyNavigationItem(NavigationItem item) {
        Image icon = item.getIcon();
        if (icon == null) return;
        GenFile genfile = icon.getGenfile();
        if (genfile == null) return;
        String iconpath = icon.getImagePath();
        if (iconpath == null) return;
        Path iconPathP = new Path(icon.getImagePath());
        File iconFile = null;
        if (iconPathP.isAbsolute()) iconFile = iconPathP.toFile(); else {
            IProject project = ProjectManager.getInstance().getCurrentProject();
            String root = project.getLocation().toOSString() + File.separator;
            iconFile = new File(root + iconpath);
        }
        if (!iconFile.exists()) {
            item.setIcon(null);
            return;
        }
        String targetPath = genfile.getPath(null);
        ICopyFile copy = new CopyFile(iconFile.getAbsolutePath(), targetPath);
        GenerationCopier.INSTANCE.addFile(copy);
    }

    public static void copyLinkImage(Link link) {
        if (link == null) return;
        if ((link.getIcon() != null) && link.getShowIcon() && link.getIcon().getGenfile() != null && !CacheManager.INSTANCE.isGenfileCached(link.getIcon().getGenfile())) {
            String iconpath = link.getIcon().getImagePath();
            Path iconPathP = new Path(iconpath);
            File iconFile = null;
            if (iconPathP.isAbsolute()) iconFile = iconPathP.toFile(); else {
                IProject project = ProjectManager.getInstance().getCurrentProject();
                String root = project.getLocation().toOSString() + File.separator;
                iconFile = new File(root + iconpath);
            }
            if (!iconFile.exists()) {
                link.setIcon(null);
            } else {
                CacheManager.INSTANCE.addGenfile(link.getIcon().getGenfile());
                GenFile genfile = link.getIcon().getGenfile();
                String filename = genfile.getName() + "_" + link.getIconAngle();
                genfile.setName(filename);
                BatchImageProcessor.INSTANCE.addImageToRotate(genfile, iconFile.getAbsolutePath(), link.getIconAngle());
            }
        }
        if (link.getBackgroundImage() != null && link.getShowBackground() && link.getBackgroundImage().getGenfile() != null) {
            String bgPath = link.getBackgroundImage().getImagePath();
            Path bgPathP = new Path(bgPath);
            File bgFile = null;
            if (bgPathP.isAbsolute()) bgFile = bgPathP.toFile(); else {
                IProject project = ProjectManager.getInstance().getCurrentProject();
                String root = project.getLocation().toOSString() + File.separator;
                bgFile = new File(root + bgPath);
            }
            if (bgFile.exists()) {
                GenFile genfile = link.getBackgroundImage().getGenfile();
                String targetPath = genfile.getPath(null);
                ICopyFile copy = new CopyFile(bgFile.getAbsolutePath(), targetPath);
                GenerationCopier.INSTANCE.addFile(copy);
            } else {
                link.setBackgroundImage(null);
            }
        }
    }

    public static void copyCSS(GenerableItem item) {
        if (item.getGenfile() == null) return;
        List<AdditionalFile> addFiles = item.getGenfile().getAdditionalFiles();
        if (addFiles == null) return;
        for (AdditionalFile af : addFiles) {
            if (af instanceof CSSFile) {
                String path = af.getPathOfOriginal();
                String targetPath = af.getPath(null);
                ICopyFile copy = new CopyFile(path, targetPath);
                GenerationCopier.INSTANCE.addFile(copy);
            }
        }
    }

    public static void copyJavascripts(Exhibition exhibition) {
        List<AdditionalFile> addFiles = exhibition.getGenfile().getAdditionalFiles();
        if (addFiles == null) return;
        for (AdditionalFile af : addFiles) {
            if (af instanceof JavascriptFile) {
                String path = af.getPathOfOriginal();
                String targetPath = af.getPath(null);
                ICopyFile copy = new CopyFile(path, targetPath);
                GenerationCopier.INSTANCE.addFile(copy);
            }
        }
    }

    public static void copyBranchingPointChoiceImage(BranchingPointChoice choice) {
        Image image = choice.getImage();
        if (image == null) return;
        GenFile genfile = image.getGenfile();
        if (genfile != null) {
            String path = image.getImagePath();
            String targetPath = genfile.getPath(null);
            ICopyFile copy = new CopyFile(path, targetPath);
            GenerationCopier.INSTANCE.addFile(copy);
        }
    }

    public static void copyAlienSlideImage(AlienSlideLink link) {
        Image image = link.getImage();
        if (image == null) return;
        GenFile genfile = image.getGenfile();
        if (genfile != null) {
            String path = image.getImagePath();
            String targetPath = genfile.getPath(null);
            ICopyFile copy = new CopyFile(path, targetPath);
            GenerationCopier.INSTANCE.addFile(copy);
        }
    }

    public static void copyVideoPlayerFiles(Video video) {
        List<AdditionalFile> files = video.getGenfile().getAdditionalFiles();
        for (AdditionalFile file : files) {
            String newPath = file.getPath();
            String oldPath = file.getPathOfOriginal();
            if (newPath != null && oldPath != null) {
                ICopyFile copy = new CopyFile(oldPath, newPath);
                GenerationCopier.INSTANCE.addFile(copy);
            }
        }
        String videoPath = video.getVideoPath();
        String newVideoPath = video.getGenfile().getPath();
        if (videoPath != null && newVideoPath != null) {
            ICopyFile copy = new CopyFile(videoPath, newVideoPath);
            GenerationCopier.INSTANCE.addFile(copy);
        }
    }
}
